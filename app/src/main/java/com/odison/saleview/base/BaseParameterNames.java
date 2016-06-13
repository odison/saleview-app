package com.odison.saleview.base;

/**
 * 需要被预留的parameterName定义
 *
 * @author Blazz
 */
public class BaseParameterNames {

    /**
     * APP的ID
     */
    public static final String APP_ID = "appId";

    /**
     * APP的_input_charset
     */
    public static final String APP_INPUT_CHARSET = "inputCharset";

    /**
     * APP的_sign
     */
    public static final String APP_SIGN = "sign";

    /**
     * APP的_sign_type
     */
    public static final String APP_SIGN_TYPE = "signType";


    /**
     * APP的_token
     */
    public static final String APP_TOKEN = "token";


    /**
     * APP的_mac
     */
    public static final String APP_MAC = "mac";

    /**
     * CSRF的Token
     */
    public static final String CSRF_TOKEN = "SECURE_TOKEN";

    /**
     * 图形验证码：验证码参数
     */
    public static final String AUTH_CODE = "authcode";

    /**
     * JSONP：callback参数
     */
    public static final String JSONP_CALLBACK = "jsonpCallback";

    public static final String JSON_RESPONSE_STATE = "successful";

    public static final String JSON_RESPONSE_DATA = "data";

    public static final String JSON_RESPONSE_CODE = "code";

    public static final String JSON_RESPONSE_MESSAGE = "message";

    public static final String JSON_RESPONSE_LIST_MAIN_ORDER = "mainOrders";

    public static final String JSON_RESPONSE_LIST_ACCOUNT_DETAIL = "details";

    /**
     * initPage参数为1的表示是此功能起始页，之后的异常处理等都会返回此链接
     */
    public static final String INIT_PAGE = "initPage";

    /**
     * 错误返回地址
     */
    public static final String ERROR_RETURN_URL = "errorReturnPage";

    public static final String PAGE = "page";

    public static final String PAGE_SIZE = "pagesize";
    public static final String PAGE_CUR = "curpage";


    /**
     * 登录手机号参数名
     */
    public static final String LOGIN_PHONE = "phone";
    /**
     * 登录密码参数
     */
    public static final String LOGIN_PSWD = "password";
    /**
     * 用户新密码参数
     */
    public static final String COURIER_NEWPWD = "newPwd";

    /**
     * 用户就密码参数
     */
    public static final String COURIER_OLDPWD = "oldPwd";

    public static final String COURIER_STATE = "state";

    public static final String COURIER_GIS_STATE = "state";

    public static final String COURIER_GIS_LAT = "lat";

    public static final String COURIER_GIS_LNG = "lng";


    public static final String ORDER_STATE = "state";

    public static final String MAIN_ORDER_ID = "id";

    public static final String MAIN_ORDER = "mainOrder";

    public static final String MAIN_ORDER_EMBRACE_CODE = "embraceCode";

    public static final String SUB_ORDER_ID = "id";

    public static final String MERCHANT_LOCATION ="merLocation";

    public static final String LIST_SUBORDER = "subOrders";

    public static final String SUB_MAIN_ORDER_ID = "mainOrderId";

    public static final String SORT_SUBORDER_INDEXS = "subIndexes";
}
