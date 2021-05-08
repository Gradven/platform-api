package com.channelsharing.hongqu.supplier.api.controller.supplier;

import com.channelsharing.hongqu.supplier.api.controller.BaseController;
import com.channelsharing.hongqu.supplier.api.service.SupplierUserService;
import com.channelsharing.hongqu.supplier.api.entity.SupplierUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Created by liuhangjun on 2018/1/18.
 */
@Api(tags = "供应商用户注册、激活、个人信息接口")
@Validated
@RestController
@RequestMapping("/v1/supplierUser")
public class SupplierUserController extends BaseController {

    @Autowired
    private SupplierUserService supplierUserService;

    @ApiOperation("供应商用户登录系统")
    @PostMapping("/login")
    public SupplierUser login(@RequestBody @Validated SupplierUserLoginRequestEntity supplierUserLoginRequestEntity){

        SupplierUser supplierUser = new SupplierUser();
        BeanUtils.copyProperties(supplierUserLoginRequestEntity, supplierUser);

        SupplierUser result = supplierUserService.login(supplierUser);
        if(result != null)
            super.addSupplierUserToSession(result);

        return result;

    }

    @ApiOperation("获取供应商用户自己个人信息")
    @GetMapping("/self")
    public SupplierUser getSelf(){

        SupplierUser supplierUser = new SupplierUser();
        supplierUser.setId(super.currentSupplierUserId());

        return supplierUserService.findOne(supplierUser);
    }

    @ApiOperation("提交一条供应商用户")
    //@PostMapping("/add")
    public void add(SupplierUser enUser){

        supplierUserService.add(enUser);

    }

    /**@ApiOperation("修改一条供应商用户")
    @PatchMapping
    public void modify(@RequestBody @Validated EntUserModifyRequest supplierUserModifyRequest){

        supplierUserService.modify(supplierUser);

    }*/

    @ApiOperation(value="修改密码")
    @PatchMapping("/password/modify")
    public void modifyPassword(@RequestBody @Validated SupplierUserPwdModifyRequestEntity modifyPasswordRequestEntity) {

        supplierUserService.modifyPassword(super.currentSupplierUserId(), modifyPasswordRequestEntity.getOldPassword(), modifyPasswordRequestEntity.getNewPassword());

    }


}
