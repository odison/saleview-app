package com.odison.saleview.base;

/**
 * 常量类
 *
 * @author odison
 */

public class Constants {

    public static final String APP_NAME = "com.odison.app";

    public static final String INTENT_ACTION_LOGOUT = "com.odison.app.action.LOGOUT";

    public static final String INTENT_ACTION_SERVICE_LOCATION_START = "com.odison.app.action.LOCATION_START";

    public static final String INTENT_ACTION_SERVICE_LOCATION_STOP = "com.odison.app.action.LOCATION_STOP";

    public static final String INTENT_ACTION_LOCATION_CHANGE = "com.odison.app.action.LOCATION_CHANGE";
    //前往揽件
    public static final String INTENT_ACTION_ORDER_GO_EMBRACE = "com.odison.app.action.GO_EMBRACE";

    public static final String INTENT_ACTION_ORDER_GO_DISPATCHING = "com.odison.app.action.GO_DISPATCHING";

    public static final String INTENT_ACTION_SERVICE_NOTICE_GET_ORDER_START = "com.odison.app.action.GETORDER_START";

    public static final String INTENT_ACTION_SERVICE_NOTICE_GET_ORDER_STOP = "com.odison.app.action.GETORDER_STOP";

    public static final String INTENT_ACTION_SERVICE_LOCATION_INTERVAL = "com.odison.app.action.LOCATION_INTERVAL";

    public static final String INTENT_ACTION_REQUEST_GET_INFO = "com.odison.app.action.request.GET_INFO";

    public static final String INTENT_ACTION_REQUEST_MOD_INFO = "com.odison.app.action.request.MOD_INFO";

    public static final String INTENT_ACTION_REQUEST_GET_SUBORDER = "com.odison.app.action.request.GET_SUBORDER";

    public static final String INTENT_ACTION_REQUEST_UPD_SUBORDER_SORT = "com.odison.app.action.request.UPD_SUBORDER_SORT";

    public static final String INTENT_ACTION_REQUEST_GEO_SUBORDER = "com.odison.app.action.request.GEO_SUBORDER";

    public static final String INTENT_ACTION_REQUEST_GET_CATCHED_MAINORDER = "com.odison.app.action.request.GET_CATCHED_MAINORDER";

    public static final String INTENT_ACTION_REQUEST_GET_MYORDER = "com.odison.app.action.request.GET_MYORDER";

    public static final String INTENT_ACTION_BACKTOUI_GET_MYORDER = "com.odison.app.action.ui.GET_MYORDER";

    public static final String INTENT_ACTION_BACKTOUI_GEO_DONE = "com.odison.app.action.ui.GEO_DONE";

    public static final String INTENT_ACTION_BACKTOUI_GET_CATCHED_MAINORDER = "com.odison.app.action.ui.GET_CATCHED_MAINORDER";

    public static final String INTENT_ACTION_BACKTOUI_GET_SUBORDER = "com.odison.app.action.ui.GET_SUBORDER";
    //提示主界面远程服务器有可接取的订单
    public static final String INTENT_ACTION_BACKTOUI_HAVE_ORDERS = "com.odison.app.action.ui.HAVE_ORDERS";
    //接单之后或者登录成功之后提示主界面主动发起广播获取我的订单
    public static final String INTENT_ACTION_BACKTOUI_UPD_MYORDER = "com.odison.app.action.ui.UPD_MYORDER";

    public static final String INTENT_ACTION_MAIN_REFRESH = "com.odison.app.action.ui.REFRESH";

    public static final String INTENT_ACTION_REMOTE_ACCOUNT_UPDATE = "com.odison.app.action.ui.ACCOUNT_UPDATE";

    public static final String INTENT_ACTION_GO_ACTIVITY_USERINFO = "com.odison.app.action.ui.go.USER_INFO";

    public static final String INTENT_ACTIVITY_MAIN_ORDER_ID = "ActivityMainOrderId";

    public static final String INTENT_ACTIVITY_MAIN_ORDER = "AcitvityMainOrder";

    public static final String INTENT_ACTIVITY_SUB_ORDER_LIST = "ActivitySubOrderList";

    public static final String INTENT_ACTIVITY_SUB_ORDER_EX_LIST = "ActivitySubOrderExList";

    public static final String INTENT_ACTIVITY_MERCHANT_LOCATION = "ActivityMerchantLocation";

    public static final String INTENT_ACTIVITY_SUB_ORDER_CAUGHT = "ActivitySubOrderCaught";

    public static final String INTENT_ACTIVITY_MAIN_ORDER_CAUGHT = "ActivityMainOrderCaught";

    public static final String INTENT_ACTIVITY_MAIN_ORDER_STATE = "ActivityMainOrderState";


    public static final String[] ACCOUNT_DETAIL_ACTION_DESCRIPT = {"", "充值", "提现", "订单发布扣款", "订单配送收入"};

    public static final String[] SUBORDER_STATE_DESCRIBE = {"草稿","已发布","已打包","已接单","已揽件，配送中","物业签收","用户签收"};


}
