package com.channelsharing.hongqu.portal.api.weixin;

import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;

import com.channelsharing.hongqu.portal.api.constant.WeixinConstant;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayConfig;
import com.github.wxpay.sdk.WXPayConstants;
import com.github.wxpay.sdk.WXPayUtil;
import com.google.common.collect.Sets;
import com.channelsharing.common.exception.SystemInnerBusinessException;
import com.channelsharing.common.utils.CustomHttpHeaderUtil;
import com.channelsharing.common.utils.DateUtils;

/**
 * Created by liuhangjun on 2018/3/26.
 */
@Component
public class WeixinPayUtil {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private static final String TRANSFER = "/mmpaymkttransfers/promotion/transfers";

	@Autowired
	private Environment env;

	private Set<String> envSet;

	@PostConstruct
	private void init() {
		envSet = Sets.newHashSet(env.getActiveProfiles());
	}

	/**
	 * 微信企业付款
	 *
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> transfer(Long userId, int amount, String tradeNo, String openId, String clientIp,
			String checkName, String desc) throws Exception {
		// 开发、测试环境不调用微信进行转账
		if (envSet.contains("dev") || envSet.contains("test")) {
			Map<String, String> respMap = new HashMap<>();
			respMap.put("return_code", WXPayConstants.SUCCESS);
			respMap.put("result_code", WXPayConstants.SUCCESS);
			return respMap;
		}

		WXPayConfig config = new WeixinPayConfig();
		WXPay wxpay = new WXPay(config);

		Map<String, String> reqData = new TreeMap<>(Comparator.naturalOrder());
		reqData.put("amount", String.valueOf(amount));
		reqData.put("partner_trade_no", tradeNo); // 交易流水号
		reqData.put("openid", openId);
		reqData.put("spbill_create_ip", clientIp);
		reqData.put("check_name", checkName);
		reqData.put("desc", desc);
		reqData.put("mch_appid", WeixinConstant.WEIXIN_MINA_APP_ID);
		reqData.put("mchid", WeixinConstant.WEIXIN_MCH_ID);

		wxpay.fillRequestData(reqData);

		int maxRetry = 3;
		Map<String, String> respMap = null;
		for (int i = 1; i <= maxRetry; i++) {
			String resp = wxpay.requestWithCert(TRANSFER, reqData, config.getHttpConnectTimeoutMs(),
					config.getHttpReadTimeoutMs());
			respMap = WXPayUtil.xmlToMap(resp);

			String err_code = respMap.get("err_code");
			// 业务结果未明确的异常码，做重试
			if (StringUtils.equalsAny(err_code, "SYSTEMERROR", "FREQ_LIMIT") && i < maxRetry) {
				continue;
			} else {
				break;
			}
		}

		return respMap;
	}

	/**
	 * 微信统一下单接口
	 *
	 * @param sn
	 *            商户订单号，要求32字符内，例如：152093451284214716
	 * @param fee
	 *            100 单位分
	 */
	public Map unifiedOrder(String sn, @NotNull Integer fee, String openId, String notifyUrl) {

		logger.debug("Before unifiedOrder fee is {}", fee);
		
		// 如果测试环境和开发环境，设置付费为1分钱
		if (envSet.contains("dev") || envSet.contains("test")) {
			fee = 1;
		}
		
		
		logger.debug("After unifiedOrder fee is {}", fee);

		if (openId == null) {
			throw new SystemInnerBusinessException("获取用户openId失败！");
		}

		int timeLen = 14;

		String timeStart = DateUtils.getLongDate();
		timeStart = StringUtils.substring(timeStart, 0, timeLen);

		Long expiredTime = 30 * 60L; // 30分钟;
		Date timeExpiredDate = DateUtils.addSecond(expiredTime); //
		String timeExpired = DateUtils.getDate(timeExpiredDate, DateUtils.LONG_DATE_PATTERN);
		timeExpired = StringUtils.substring(timeExpired, 0, timeLen);

		WeixinPayConfig config = null;

		try {
			config = new WeixinPayConfig();
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}

		WXPay wxpay = null;
		try {
			wxpay = new WXPay(config, notifyUrl, true, false);
		} catch (Exception e) {
			e.printStackTrace();
		}

		String nonceStr = WXPayUtil.generateNonceStr();

		Map<String, String> data = new HashMap<>();
		data.put("fee_type", "CNY");
		data.put("trade_type", "JSAPI"); // 此处指定为小程序支付
		data.put("body", WeixinConstant.WEIXIN_PAY_BODY);
		data.put("out_trade_no", sn);
		data.put("total_fee", fee.toString());
		data.put("spbill_create_ip", CustomHttpHeaderUtil.getRemoteIp());
		data.put("notify_url", notifyUrl);
		data.put("openid", openId);
		data.put("time_start", timeStart);
		data.put("time_expire", timeExpired);
		data.put("nonce_str", nonceStr);

		logger.debug("Weixin pay unifiedOrder param --- time_start :{}, time_expire:{}", timeStart, timeExpired);

		Map<String, String> respMap = null;
		try {
			if (wxpay == null)
				return respMap;
			respMap = wxpay.unifiedOrder(data);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SystemInnerBusinessException("请求微信统一订单接口返回结果失败！");
		}

		logger.debug("Weixin pay unifiedOrder result : {}", respMap);

		String returnCode = respMap.get(WeixinConstant.RETURN_CODE);
		String returnMsg = respMap.get(WeixinConstant.RETURN_MSG);

		if (StringUtils.equals(returnCode, WeixinConstant.FAIL_STR)) {
			// 失败打印日志, 并抛出错误给前台
			logger.error("Request Weixin pay unifiedOrder error, error message is: {}", returnMsg);
			throw new SystemInnerBusinessException("请求微信统一订单接口失败！");

		} else if (StringUtils.equals(returnCode, WeixinConstant.SUCCESS_STR)) {
			// 如果成功则先验证微信签名，对内容再次签名，返回给接口调用方

			String weixinSign = respMap.get("sign");
			Map mapTmp = respMap;
			mapTmp.remove("sign");
			mapTmp.remove(WeixinConstant.RETURN_CODE);
			mapTmp.remove(WeixinConstant.RETURN_MSG);
			mapTmp.remove(WeixinConstant.RESULT_CODE);

			String verifySign = null;
			try {
				verifySign = WXPayUtil.generateSignature(mapTmp, WeixinConstant.WEIXIN_MCH_KEY,
						WXPayConstants.SignType.MD5);
			} catch (Exception e) {
				e.printStackTrace();
				throw new SystemInnerBusinessException("系统接受微信支付生成校验签名信息失败");
			}

			// 微信返回的sign签名校验
			if (StringUtils.equals(weixinSign, verifySign)) {
				throw new SystemInnerBusinessException("微信统一下单签名验证不通过");
			}

			// 再次签名操作，返回给小程序验证使用
			String timeStamp = Long.toString(System.currentTimeMillis() / 1000);
			String nonce_str = respMap.get("nonce_str");
			String prepay_id = respMap.get("prepay_id");
			String appId = respMap.get("appid");
			// String mch_id = respMap.get("mch_id");

			Map<String, String> signMap = new HashMap<>();
			signMap.put("appId", appId);
			signMap.put("nonceStr", nonce_str);
			signMap.put("package", "prepay_id=" + prepay_id);
			signMap.put("signType", WXPayConstants.SignType.MD5.toString());
			signMap.put("timeStamp", timeStamp);

			String md5 = null;
			try {
				md5 = WXPayUtil.generateSignature(signMap, WeixinConstant.WEIXIN_MCH_KEY, WXPayConstants.SignType.MD5);
			} catch (Exception e) {
				e.printStackTrace();
				throw new SystemInnerBusinessException("系统返回微信支付生成校验签名信息失败");
			}

			Map<String, String> returnMap = new HashMap<>();
			returnMap.put("paySign", md5);
			returnMap.put("package", "prepay_id=" + prepay_id);
			returnMap.put("nonceStr", nonce_str);
			returnMap.put("timeStamp", timeStamp);
			returnMap.put("signType", WXPayConstants.SignType.MD5.toString());
			returnMap.put("sn", sn);

			logger.debug("Weixin pay return map is : {}", returnMap.toString());

			return returnMap;

		}

		logger.debug("Weixin pay error returnCode: {}, returnMsg: {} ", returnCode, returnMsg);

		return null;
	}

}
