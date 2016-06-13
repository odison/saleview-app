package com.odison.saleview.base;

public class ErrorCodes extends BaseErrorCodes {
	/**
	 * 登陆失败
	 */
	public static final String USER_LOGIN_FAILED = "ERR-BU00001";



	

	/**
	 * 已在其他设备登陆
	 */
	public static final String COURIER_LOGINED_IN_OTHER_DEVICE = "ERR-BC00001";
	/**
	 *未登陆
	 */
	public static final String COURIER_NOT_LOGINED = "ERR-BC00002";
	/**
	 *已销户
	 */
	public static final String COURIER_NOT_ACTIVE = "ERR-BC00003";
	/**
	 *已销户
	 */
	public static final String COURIER_LOGIN_FAILED = "ERR-BC00004";
	/**
	 * 注册失败
	 */
	public static final String COURIER_REG_FAILED = "ERR-BC00005";
	/**
	 * 更新位置失败
	 */
	public static final String COURIER_UPDATE_GIS_FAILED = "ERR-BC00006";
	/**
	 * 添加账号失败
	 */
	public static final String COURIER_ADD_FAILED = "ERR-BC00007";
	/**
	 * 修改资料失败
	 */
	public static final String COURIER_CHANGE_INFO_FAILED = "ERR-BC00008";
	/**
	 * 修改资料失败
	 */
	public static final String COURIER_CHANGE_PWD_FAILED = "ERR-BC00009";
	/**
	 * 置休息状态失败
	 */
	public static final String COURIER_RESET_FAILED = "ERR-BC00010";
	
	
	

	/**
	 * 无法接未发布的单
	 */
	public static final String ORDER_CATCH_NOT_PACK = "ERR-BO00004";
	/**
	 * 接单失败
	 */
	public static final String ORDER_CATCH_FAILED = "ERR-BO00005";
	/**
	 * 无法揽件
	 */
	public static final String ORDER_EMBRACE_NOT_ENABLE = "ERR-BO00006";
	/**
	 * 揽件失败
	 */
	public static final String ORDER_EMBRACE_FAILED = "ERR-BO00007";
	/**
	 * 无法完成配送状态
	 */
	public static final String ORDER_FINISH_NOT_ENABLE = "ERR-BO00008";
	/**
	 * 完成配送失败
	 */
	public static final String ORDER_FINISH_FAILED = "ERR-BO00009";
	/**
	 * 存在未揽件订单
	 */
	public static final String ORDER_HAS_NOT_EMBRACED = "ERR-BO00010";
	/**
	 * 无法打包
	 */
	public static final String ORDER_PACK_NOT_PUB = "ERR-BO00011";
	/**
	 * 打包失败
	 */
	public static final String ORDER_PACKED_FAILED = "ERR-BO00012";
	/**
	 * 无法取消子单
	 */
	public static final String SUB_ORDER_CANCEL_NOT_PUB = "ERR-BO00013";
	/**
	 * 取消子单失败
	 */
	public static final String SUB_ORDER_CANCEL_FAILED = "ERR-BO00014";
	/**
	 * 主单无法推送
	 */
	public static final String ORDER_PUSH_NOT_CREATE = "ERR-BO00015";
	/**
	 * 推送主单失败
	 */
	public static final String ORDER_PUSH_FAILED = "ERR-BO00016";
	/**
	 * 无法取消主单
	 */
	public static final String MAIN_ORDER_CANCEL_NOT_CREATE = "ERR-BO00017";
	/**
	 * 取消主单失败
	 */
	public static final String MAIN_ORDER_CANCEL_FAILED = "ERR-BO00018";
	/**
	 * 未找到已接取的订单
	 */
	public static final String MAIN_ORDER_CATCHED_NOT_FOUND = "ERR-BO00019";
	
	/**
	 * 不能排序他人订单
	 */
	public static final String ORDER_INDEX_SORT_NOT_ENABLE = "ERR-BO00020";

	
	

	
}
