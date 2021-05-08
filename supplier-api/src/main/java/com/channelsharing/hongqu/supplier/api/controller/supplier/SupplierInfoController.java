package com.channelsharing.hongqu.supplier.api.controller.supplier;

import com.channelsharing.hongqu.supplier.api.controller.BaseController;
import com.channelsharing.hongqu.supplier.api.entity.SupplierInfo;
import com.channelsharing.hongqu.supplier.api.enums.SupplierStatus;
import com.channelsharing.hongqu.supplier.api.service.SupplierInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Created by liuhangjun on 2018/1/30.
 */
@Api(tags = "供应商信息注册、修改和查看接口")
@Validated
@RestController
@RequestMapping("/v1/supplierInfo")
public class SupplierInfoController extends BaseController {


    @Autowired
    private SupplierInfoService supplierInfoService;

    @ApiOperation("提交一条供应商信息")
    @PostMapping("/add")
    public void add(@RequestBody @Validated SupplierInfoAddRequestEntity supplierInfoAddRequestEntity){

        SupplierInfo supplierInfo = new SupplierInfo();
        BeanUtils.copyProperties(supplierInfoAddRequestEntity, supplierInfo);
        supplierInfo.setStatus(SupplierStatus.inreviewed.getCode());

        supplierInfoService.add(supplierInfo);

    }

    @ApiOperation("修改一条供应商信息")
    @PatchMapping("/modify")
    public void modify(@RequestBody @Validated SupplierInfoModifyRequestEntity supplierInfoModifyRequestEntity){

        SupplierInfo supplierInfo = new SupplierInfo();
        BeanUtils.copyProperties(supplierInfoModifyRequestEntity, supplierInfo);
        supplierInfo.setStatus(SupplierStatus.inreviewed.getCode());

        supplierInfoService.modify(supplierInfo);

    }


    @ApiOperation("查询登录用户自己的供应商信息")
    @GetMapping("/self")
    public SupplierInfo self(){

        SupplierInfo supplierInfo = new SupplierInfo();

        supplierInfo.setId(super.currentSupplierUser().getSupplierId());
        SupplierInfo result = supplierInfoService.findOne(supplierInfo);

        return result;
    }
}
