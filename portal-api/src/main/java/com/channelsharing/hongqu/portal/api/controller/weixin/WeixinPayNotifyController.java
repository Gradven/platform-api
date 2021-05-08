package com.channelsharing.hongqu.portal.api.controller.weixin;

import java.util.HashMap;
import java.util.Map;

import com.channelsharing.hongqu.portal.api.constant.WeixinConstant;
import com.channelsharing.hongqu.portal.api.controller.BaseController;
import com.channelsharing.hongqu.portal.api.entity.OrderInfo;
import com.channelsharing.hongqu.portal.api.entity.OrderShopServe;
import com.channelsharing.hongqu.portal.api.entity.WeixinNotifyData;
import com.channelsharing.hongqu.portal.api.enums.PayType;
import com.channelsharing.hongqu.portal.api.service.OrderShopServeService;
import com.channelsharing.hongqu.portal.api.service.WeixinNotifyDataService;
import com.channelsharing.hongqu.portal.api.weixin.WeixinPayConfig;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayUtil;
import com.channelsharing.common.utils.AmountUtils;
import com.channelsharing.common.utils.DateUtils;
import com.channelsharing.pub.enums.CancelType;
import com.channelsharing.pub.enums.OrderStatus;
import com.channelsharing.hongqu.portal.api.service.OrderInfoService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * Created by liuhangjun on 2018/3/6.
 */
@Api(tags = "微信支付回调接口")
@RestController
@RequestMapping(value = "/v1/wxpay/notify")
public class WeixinPayNotifyController extends BaseController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private OrderInfoService orderInfoService;

	@Autowired
	private OrderShopServeService orderShopServeService;

	@Autowired
	private WeixinNotifyDataService weixinNotifyDataService;

	@ApiOperation(value = "微信支付店铺技术服务结果回调地址")
	@PostMapping("/shop")
	public ResponseEntity<String> payShopNotify(@RequestBody String notifyData) throws Exception {

		logger.debug("Received weixin pay notify information of shop, notify data is: {}", notifyData);

		Map<String, String> returnMap = new HashMap<>();

		WeixinPayConfig config = new WeixinPayConfig();
		WXPay wxpay = new WXPay(config);

		Map<String, String> notifyMap = WXPayUtil.xmlToMap(notifyData); // 转换成map

		// 对应微信定义的字段
		String payTimeEnd; // 对应支付时间 orderShopServe.payTime
		String resultCode; // 结果码
		String outTradeNo; // 对应订单号 orderShopServe.sn
		String transactionId; // 对应支付号 orderShopServe.payNo
		String totalFee; // 对应支付年费 orderShopServe.fee

		if (wxpay.isPayResultNotifySignatureValid(notifyMap)) {

			// 签名正确
			// 进行处理。
			// 注意特殊情况：订单已经退款，但收到了支付结果成功的通知，不应把商户侧订单状态从退款改成支付成功
			// 本小程序暂时不做退款业务处理
			payTimeEnd = notifyMap.get(WeixinConstant.PAY_TIME_END);
			resultCode = notifyMap.get(WeixinConstant.RESULT_CODE);
			outTradeNo = notifyMap.get(WeixinConstant.OUT_TRADE_NO);
			transactionId = notifyMap.get(WeixinConstant.TRANSACTION_ID);
			totalFee = notifyMap.get(WeixinConstant.TOTAL_FEE);

			// 先将通知信息入库，再处理业务逻辑
			WeixinNotifyData weixinNotifyData = new WeixinNotifyData();
			weixinNotifyData.setOrderSn(outTradeNo);
			weixinNotifyData.setContent(notifyData);
			weixinNotifyDataService.add(weixinNotifyData);

			// 订单业务处理
			OrderShopServe orderShopServeQuery = new OrderShopServe();
			orderShopServeQuery.setSn(outTradeNo);
			OrderShopServe orderShopServe = orderShopServeService.findOne(orderShopServeQuery);

			if (orderShopServe == null) {
				returnMap.put(WeixinConstant.RETURN_CODE, WeixinConstant.FAIL_STR);
				returnMap.put(WeixinConstant.RETURN_MSG, "没有此订单信息");
			} else if (OrderStatus.cancel.getCode() == orderShopServe.getStatus()) {
				returnMap.put(WeixinConstant.RETURN_CODE, WeixinConstant.FAIL_STR);
				returnMap.put(WeixinConstant.RETURN_MSG, "订单已经取消");
			} else if (OrderStatus.paid.getCode() == orderShopServe.getStatus()) {
				returnMap.put(WeixinConstant.RETURN_CODE, WeixinConstant.SUCCESS_STR);
				returnMap.put(WeixinConstant.RETURN_MSG, "OK");
			} else if (OrderStatus.unPay.getCode() == orderShopServe.getStatus()) {

				if (StringUtils.equals(resultCode, WeixinConstant.RESULT_CODE_FAIL)) {
					returnMap.put(WeixinConstant.RETURN_CODE, WeixinConstant.RESULT_CODE_FAIL);
					returnMap.put(WeixinConstant.RETURN_MSG, "微信订单通知处理结果失败，本系统做订单取消处理");

					orderShopServeService.cancelOrder(outTradeNo, CancelType.weixinPayFailedCancel, null, null);

				} else {
					// 微信订单处理成功，以下做业务处理
					OrderShopServe entity = new OrderShopServe();
					entity.setSn(outTradeNo);
					entity.setPayTime(DateUtils.parseDate(payTimeEnd));
					entity.setPayNo(transactionId);
					entity.setPayMoney(AmountUtils.changeF2Y(Integer.parseInt(totalFee))); // 单位换算下，将分换算为元
					entity.setPayType(PayType.weixin.getCode());
					orderShopServeService.payOrder(entity);

					returnMap.put(WeixinConstant.RETURN_CODE, WeixinConstant.SUCCESS_STR);
					returnMap.put(WeixinConstant.RETURN_MSG, "OK");
				}

			} else {
				returnMap.put(WeixinConstant.RETURN_CODE, WeixinConstant.FAIL_STR);
				returnMap.put(WeixinConstant.RETURN_MSG, "订单处理其他错误");
			}

		} else {
			// 签名错误，如果数据里没有sign字段，也认为是签名错误
			returnMap.put(WeixinConstant.RETURN_CODE, WeixinConstant.FAIL_STR);
			returnMap.put(WeixinConstant.RETURN_MSG, "签名校验失败");

		}

		return ResponseEntity.ok(WXPayUtil.mapToXml(returnMap));
	}

	@ApiOperation(value = "微信支付商品结果回调地址")
	@PostMapping("/goods")
	public ResponseEntity<String> payGoodsNotify(@RequestBody String notifyData) throws Exception {

		logger.debug("Received weixin pay notify information of goods, notify data is: {}", notifyData);

		Map<String, String> returnMap = new HashMap<>();

		WeixinPayConfig config = new WeixinPayConfig();
		WXPay wxpay = new WXPay(config);

		Map<String, String> notifyMap = WXPayUtil.xmlToMap(notifyData); // 转换成map

		String payTimeEnd;
		String resultCode;
		String sn = "";
		String paymentNo;
		String totalFee;

		if (wxpay.isPayResultNotifySignatureValid(notifyMap)) {
			// 签名正确
			// 进行处理。
			// 注意特殊情况：订单已经退款，但收到了支付结果成功的通知，不应把商户侧订单状态从退款改成支付成功
			// 本小程序暂时不做退款业务处理
			payTimeEnd = notifyMap.get(WeixinConstant.PAY_TIME_END);
			resultCode = notifyMap.get(WeixinConstant.RESULT_CODE);
			sn = notifyMap.get(WeixinConstant.OUT_TRADE_NO);
			paymentNo = notifyMap.get(WeixinConstant.TRANSACTION_ID);
			totalFee = notifyMap.get(WeixinConstant.TOTAL_FEE);

			// 先将通知信息入库，再处理业务逻辑
			WeixinNotifyData weixinNotifyData = new WeixinNotifyData();
			weixinNotifyData.setOrderSn(sn);
			weixinNotifyData.setContent(notifyData);
			weixinNotifyDataService.add(weixinNotifyData);

			OrderInfo orderInfoQuery = new OrderInfo();
			orderInfoQuery.setSn(sn);
			OrderInfo orderInfo = orderInfoService.findOne(orderInfoQuery);

			if (orderInfo == null) {
				returnMap.put(WeixinConstant.RETURN_CODE, WeixinConstant.FAIL_STR);
				returnMap.put(WeixinConstant.RETURN_MSG, "没有此订单信息");
			} else if (OrderStatus.cancel.getCode() == orderInfo.getStatus()) {
				returnMap.put(WeixinConstant.RETURN_CODE, WeixinConstant.FAIL_STR);
				returnMap.put(WeixinConstant.RETURN_MSG, "订单已经取消");
			} else if (OrderStatus.paid.getCode() == orderInfo.getStatus()) {
				returnMap.put(WeixinConstant.RETURN_CODE, WeixinConstant.SUCCESS_STR);
				returnMap.put(WeixinConstant.RETURN_MSG, "OK");
			} else if (OrderStatus.unPay.getCode() == orderInfo.getStatus()) {

				if (StringUtils.equals(resultCode, WeixinConstant.RESULT_CODE_FAIL)) {
					returnMap.put(WeixinConstant.RETURN_CODE, WeixinConstant.RESULT_CODE_FAIL);
					returnMap.put(WeixinConstant.RETURN_MSG, "微信订单通知处理结果失败，本系统做订单取消处理");

					orderInfoService.cancelOrder(sn, CancelType.weixinPayFailedCancel, null, null);

				} else {
					// 微信订单处理成功，以下做业务处理
					OrderInfo entity = new OrderInfo();
					entity.setSn(sn);
					entity.setPayType(PayType.weixin.getCode());
					entity.setPayTime(DateUtils.parseDate(payTimeEnd));
					entity.setPayNo(paymentNo);
					entity.setPayMoney(AmountUtils.changeF2Y(Integer.parseInt(totalFee)));
					orderInfoService.payOrder(entity);

					returnMap.put(WeixinConstant.RETURN_CODE, WeixinConstant.SUCCESS_STR);
					returnMap.put(WeixinConstant.RETURN_MSG, "OK");
				}

			} else {
				returnMap.put(WeixinConstant.RETURN_CODE, WeixinConstant.FAIL_STR);
				returnMap.put(WeixinConstant.RETURN_MSG, "订单处理其他错误");
			}

		} else {
			// 签名错误，如果数据里没有sign字段，也认为是签名错误
			returnMap.put(WeixinConstant.RETURN_CODE, WeixinConstant.FAIL_STR);
			returnMap.put(WeixinConstant.RETURN_MSG, "签名校验失败");

		}

		return ResponseEntity.ok(WXPayUtil.mapToXml(returnMap));
	}
}
