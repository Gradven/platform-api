package com.channelsharing.hongqu.portal.api.controller.user;

import com.channelsharing.common.utils.RandomUtil;
import com.channelsharing.hongqu.portal.api.controller.BaseController;
import com.channelsharing.hongqu.portal.api.entity.ShopInfo;
import com.channelsharing.hongqu.portal.api.entity.UserInfo;
import com.channelsharing.hongqu.portal.api.service.ShopInfoService;
import com.channelsharing.hongqu.portal.api.service.UserBindMobileService;
import com.channelsharing.common.enums.BooleanEnum;
import com.channelsharing.common.exception.BadRequestException;
import com.channelsharing.common.validator.Validator;
import com.channelsharing.hongqu.portal.api.service.UserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by liuhangjun on 2018/6/29.
 */
@Api(tags = "用户绑定手机号码")
@Validated
@RestController
@RequestMapping(value = "/v1/user/bindMobile")
public class UserBindMobileController extends BaseController {
    
    @Autowired
    private UserBindMobileService userBindMobileService;
    
    @Autowired
    private ShopInfoService shopInfoService;
    
    @Autowired
    private UserInfoService userInfoService;
    
    
    @ApiOperation(value="手机号码绑定，发送验证码到手机")
    @GetMapping("/sendCode/")
    public void sendCode(@RequestParam String mobile,
                         @ApiParam(value = "邀请码", example = "TDAC1")
                         @RequestParam String invitationCode){
        
        if (!Validator.isMobile(mobile))
            throw new BadRequestException("手机号码格式不正确");

        userBindMobileService.sendCode(super.currentUserId(), mobile, invitationCode);
        
        
    }
    
    @Transactional
    @ApiOperation(value="输入验证码，进行验证, 验证通过自动开店")
    @GetMapping("/verifyCode/autoAddShop")
    public Long verifyCode(@RequestParam String mobile,
                         @RequestParam String code){
        
        if (!Validator.isMobile(mobile))
            throw new BadRequestException("手机号码格式不正确");
    
        UserInfo userInfo = userBindMobileService.verifyCode(super.currentUserId(), mobile, code);
        
        if (userInfo != null){
            
            StringBuffer shopName = new StringBuffer();
            shopName.append(userInfo.getNickname()).append("的小店");
    
            ShopInfo shopInfoTmp = shopInfoService.findByName(shopName.toString());
            
            if (shopInfoTmp != null && shopInfoTmp.getName() != null){
                shopName = shopName.append(RandomUtil.createRandomCharData(5)) ;
            }
            
            ShopInfo shopInfo = new ShopInfo();
            shopInfo.setName(shopName.toString());
            
            shopInfo.setUserId(super.currentUserId());
            shopInfo.setCertificateFlag(BooleanEnum.no.getCode());
            shopInfo.setLogo(userInfo.getAvatar());
            ShopInfo shopInfoResult = shopInfoService.addWithResult(shopInfo);
            userInfo.setShopId(shopInfoResult.getId());
            
            super.addUserToSession(userInfo);
    
            UserInfo update = new UserInfo();
            update.setId(super.currentUserId());
            update.setShopId(shopInfoResult.getId());
            userInfoService.modify(update);
            
            return shopInfoResult.getId();
        }
        
        return null;
    }
    
}
