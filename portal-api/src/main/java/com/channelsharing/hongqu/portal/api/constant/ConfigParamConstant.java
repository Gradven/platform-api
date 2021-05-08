package com.channelsharing.hongqu.portal.api.constant;

/**
 * Created by liuhangjun on 2018/2/9.
 */
public interface ConfigParamConstant {

	// 技术年费
	String PORTAL_SHOP_YEAR_SERVICE_FEE = "portal.shop.year.service.fee";

	// 购物车商品最大数据量限制
	String PORTAL_CART_GOODS_MAX_NUMBER = "portal.cart.goods.max.number";

	// 店铺默认背景图
	String PORTAL_SHOP_DEFAULT_BACKGROUND_URL = "portal.shop.default.background.url";
	String PORTAL_SHOP_DEFAULT_DESCRIPTION = "portal.shop.default.description";
	String PORTAL_SHOP_AGENT_GOODS_MAX_NUMBER = "portal.shop.agent.goods.max.number";

	// 用户店铺访问记录最大数
	String PORTAL_SHOP_VISIT_MAX_NUMBER = "portal.shop.visit_max_number";

	// 用户可填写的收货地址数
	String PORTAL_ADDRESS_MAX_NUMBER = "portal.address.max_number";

	// 提现最小金额，单位：元
	String PORTAL_WITHDRAW_MIN_AMOUNT = "portal.withdraw.min.amount";
	
	// 用户确认订单后N天，店主收益到账
	String PORTAL_ORDER_PROFIT_CONFIRM_DAYS = "portal.order.profit.confirm.days";
	
	// 用户确认订单后N天，店主收益到账（查询收益的时间范围）
	String PORTAL_ORDER_PROFIT_CONFIRM_DAYS_RANGE = "portal.order.profit.confirm.days.range";
	
	// VIP邀请码
	String PORTAL_INVITATION_VIP_CODE = "portal.invitation.vip.code";
	
	// 发货超过N天，自动确认收货时间
	String PORTAL_ORDER_GOODS_AUTO_CONFIRM_DAYS = "portal.order.goods.auto.confirm.days";
}
