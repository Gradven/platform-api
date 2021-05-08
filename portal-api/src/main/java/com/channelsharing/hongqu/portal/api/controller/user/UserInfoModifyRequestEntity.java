/**
 * Copyright &copy; 2016-2022 liuhangjun All rights reserved.
 */
package com.channelsharing.hongqu.portal.api.controller.user;

import com.channelsharing.hongqu.portal.api.validations.Sensitive;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

/**
 * 用户信息Model
 * @author liuhangjun
 * @version 2017-06-15
 */
@Data
public class UserInfoModifyRequestEntity {
    
    @ApiModelProperty(value = "用户id", example = "1")
    @NotNull
	private String id;          //用户id
    
    @Sensitive(message = "用户昵称中包含了敏感词")
    @ApiModelProperty(value = "用户昵称", example = "户昵称户昵称")
    @Length(min=0, max=30, message="用户昵称长度必须介于 0 和 30 之间")
	private String nickname;		// 用户昵称
    
    @ApiModelProperty(value = "头像地址", example = "http://www.xxxx.com")
    @Length(min=0, max=255, message="头像长度必须介于 0 和 255 之间")
	private String avatar;		// 头像地址
    
    @ApiModelProperty(value = "性别：1：男，2：女", example = "1")
    @Range(min = 1, max = 2, message = "性别的值必须介于 1 和 2 之间")
    private Integer sex;       //性别
    
    @Sensitive(message = "个人简介中包含了敏感词")
    @ApiModelProperty(value = "个人简介", example = "个人简介个人简介")
    @Length(min=0, max=200, message="个人简介长度必须介于 0 和 255 之间")
	private String description;  //简介
    
    @ApiModelProperty(value = "地址", example = "地址地址地址地址")
    @Length(min=0, max=32, message="地址长度必须介于 0 和 255 之间")
    private String address;    //地址
    
    @ApiModelProperty(value = "公司", example = "公司公司公司公司")
    @Length(min=0, max=128, message="地址长度必须介于 0 和 128 之间")
    private String company;    //公司
    
    @ApiModelProperty(value = "联系方式", example = "联系方式联系方式联系方式")
    @Length(min=0, max=128, message="联系方式长度必须介于 0 和 128 之间")
    private String contact;    //联系方式

    

}
