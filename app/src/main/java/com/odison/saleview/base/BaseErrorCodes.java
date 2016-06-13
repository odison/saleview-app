package com.odison.saleview.base;

/**
 * 基本错误码定义类、其他错误吗定义类继承此类 错误码规则，ERR-B开头，B后面的第一位为模块代码，P表示公共，其他由继承类自行定义
 * 
 * @author Blazz
 * 
 */
public class BaseErrorCodes {
	/*============= 公共类    =====================*/
	
	/**
	 * 成功
	 * 
	 */
	public static final String SUCCESS = "ERR-BP00000";
	
	
	/**
	 * API调用成功
	 * 
	 */
	public static final String API_SUCCESS = "ERR-BP0000";

	/**
	 * 失败
	 * 
	 */
	public static final String UNKNOWN = "ERR-BP99999";

	/**
	 * 登录超时
	 * 
	 */
	public static final String SESSION_EXPIRED = "ERR-BP00001";

	/**
	 * 无访问权限
	 * 
	 */
	public static final String PERMISSION_DENIED = "ERR-BP00002";

	/**
	 * CSRF攻击
	 * 
	 */
	public static final String CSRF_ATTACK = "ERR-BP00003";

	/**
	 * 注入攻击
	 * 
	 */
	public static final String SCRIPT_INJURE = "ERR-BP00004";
	
	/**
	 * 重复提交问题
	 * 
	 */
	public static final String FORM_RESUBMIT = "ERR-BP00005";
	
	/**
	 * 验证签名失败
	 */
	public static final String SIGN_FAILED = "ERR-BP00006";
	
}
