package com.channelsharing.hongqu.portal.api.controller.session;

import com.channelsharing.common.enums.BooleanEnum;
import com.channelsharing.hongqu.portal.api.controller.user.UserInfoController;
import com.channelsharing.hongqu.portal.api.entity.ShopInfo;
import com.channelsharing.hongqu.portal.api.entity.UserInfo;
import com.channelsharing.hongqu.portal.api.service.ShopInfoService;
import com.channelsharing.hongqu.portal.api.service.SnsAccessService;
import com.channelsharing.hongqu.portal.api.controller.BaseController;
import com.channelsharing.hongqu.portal.api.enums.AccountType;
import com.channelsharing.hongqu.portal.api.enums.UserStatus;
import com.channelsharing.hongqu.portal.api.service.UserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;

/**
 * Created by liuhangjun on 2018/3/6.
 */
@Api(tags = "用户登录会话接口")
@RestController
@RequestMapping(value = "/v1/session")
public class SessionController extends BaseController {

	@Resource
	private UserInfoController userInfoController;

	@Resource
	private UserInfoService userInfoService;

	@Autowired
	private SnsAccessService snsAccessService;
	
	@Autowired
	private ShopInfoService shopInfoService;

	@ApiOperation(value = "邮箱手机号用户登录，返回用户信息")
	@PostMapping
	public UserInfo login(@RequestBody @Validated SessionCreateRequestEntity createSessionRequestEntity) {

		UserInfo queryEntity = new UserInfo();
		BeanUtils.copyProperties(createSessionRequestEntity, queryEntity);

		String account = createSessionRequestEntity.getAccount();
		userInfoController.validateAccount(account, queryEntity);
		queryEntity.setStatus(UserStatus.activated.getCode());

		UserInfo retUserInfo = userInfoService.login(queryEntity);
		
		// 判断店主缴费是否还在有效期内，在：就有店主的代理商品权益，不在：没有代理商品的权益
		this.dealShopRight(retUserInfo);
		
		super.addUserToSession(retUserInfo);

		return retUserInfo;

	}


	@ApiOperation(value = "前端(h5和小程序)第三方登录, 返回用户信息")
	@PostMapping("/frontThird")
	public UserInfo frontThirdLogin(@RequestBody @Validated FrontThirdSessionRequestEntity frontThirdSessionRequestEntity) throws IOException {

		AccountType accountType = AccountType.valueOf(frontThirdSessionRequestEntity.getAccountType());

		UserInfo userInfo = snsAccessService.getUserInfoFromThirdParty(
				accountType, frontThirdSessionRequestEntity.getCode(), frontThirdSessionRequestEntity.getMinaVerifyMap());

		UserInfo retUserInfo = userInfoService.thirdLogin(userInfo);
		
		// 判断店主缴费是否还在有效期内，在：就有店主的代理商品权益，不在：没有代理商品的权益
		this.dealShopRight(retUserInfo);

		super.addUserToSession(retUserInfo);

		return retUserInfo;
	}


	@ApiOperation(value = "删除会话信息")
	@DeleteMapping
	public void logout() {
		HttpSession session = super.getSession(false);
		if (session != null) {
			session.invalidate();
		}
	}
	
	/**
	 * 判断店主缴费是否还在有效期内，在：就有店主的代理商品权益，不在：没有代理商品的权益
	 * @param userInfo
	 */
	private void dealShopRight(UserInfo userInfo){
		// 判断店主缴费是否还在有效期内，在：就有店主的代理商品权益，不在：没有代理商品的权益
		Long shopId = userInfo.getShopId();
		if (shopId != null){
			ShopInfo shopInfo = shopInfoService.findOne(shopId);
			
			Date expireTime = shopInfo.getExpireTime();
			if (expireTime == null){
				userInfo.setShopRightFlag(BooleanEnum.no.getCode());
			}else {
				// 判断是否过期
				boolean bln = expireTime.before(new Date());
				
				if (bln){
					userInfo.setShopRightFlag(BooleanEnum.no.getCode());
				}else {
					userInfo.setShopRightFlag(BooleanEnum.yes.getCode());
				}
			}
		}
	}

}
