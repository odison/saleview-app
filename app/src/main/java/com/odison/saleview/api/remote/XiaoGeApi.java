package com.odison.saleview.api.remote;

import com.alibaba.fastjson.JSON;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.odison.saleview.AppConfig;
import com.odison.saleview.api.ApiHttpClient;
import com.odison.saleview.base.BaseParameterNames;
import com.odison.saleview.base.Dict;
import com.odison.saleview.bean.Courier;
import com.odison.saleview.bean.CourierGis;
import com.odison.saleview.bean.MainOrder;
import com.odison.saleview.bean.SubOrder;
import com.odison.saleview.bean.SubOrderEx;
import com.odison.saleview.util.OrderUtil;

import java.util.List;


/**
 * Created by odison on 2015/12/13.
 */
public class XiaoGeApi {
    public static void login(String username, String password,
                             String mac,
                             AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("username", username);
        params.put("pwd", password);
        params.put("mac", mac);
        String loginurl = "action/api/login_validate";
        ApiHttpClient.post(loginurl, params, handler);
    }

    /**
     * 登录接口
     *
     * @param username
     * @param password
     * @param mac
     * @param handler
     */
    public static void login(String username, String password, String mac,
                             JsonHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put(BaseParameterNames.LOGIN_PHONE, username);
        params.put(BaseParameterNames.LOGIN_PSWD, password);
        params.put(BaseParameterNames.APP_MAC, mac);
        String loginUrl = "login/login.json";
        ApiHttpClient.post(loginUrl, params, handler);
    }

    /**
     * 退出
     *
     * @param mac
     * @param handler
     */
    public static void logout(String mac, JsonHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put(BaseParameterNames.APP_TOKEN, AppConfig.app_token);
        params.put(BaseParameterNames.APP_MAC, mac);
        String url = "login/logout.json";
        ApiHttpClient.post(url, params, handler);
    }
    /*
    Courier api begin
     */

    /**
     * Courier getinfo
     *
     * @param mac
     * @param handler
     */
    public static void getInfo(String mac, JsonHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put(BaseParameterNames.APP_TOKEN, AppConfig.app_token);
        params.put(BaseParameterNames.APP_MAC, mac);
        String url = "courier/getInfo.json";
        ApiHttpClient.post(url, params, handler);
    }

    public static void modifyInfo(Courier courier, String mac, JsonHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        Object obj = JSON.toJSON(courier);
        // params.;
        params.put(BaseParameterNames.APP_TOKEN, AppConfig.app_token);
        //JSONObject obj = JSON.

        //params.put("courier",obj);
        params.put(BaseParameterNames.APP_MAC, mac);
        String url = "courier/modifyInfo.json";
        ApiHttpClient.post(url, params, handler);
    }

    /**
     * 更改密码，已测试
     *
     * @param mac
     * @param oldPwd
     * @param newPwd
     * @param handler
     */
    public static void changePwd(String mac, String oldPwd, String newPwd, JsonHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put(BaseParameterNames.APP_TOKEN, AppConfig.app_token);
        params.put(BaseParameterNames.APP_MAC, mac);
        params.put(BaseParameterNames.COURIER_OLDPWD, oldPwd);
        params.put(BaseParameterNames.COURIER_NEWPWD, newPwd);
        String url = "courier/changePwd.json";
        ApiHttpClient.post(url, params, handler);
    }

    public static void updateGis(String mac, CourierGis courierGis, JsonHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put(BaseParameterNames.APP_TOKEN, AppConfig.app_token);
        params.put(BaseParameterNames.APP_MAC, mac);
        params.put(BaseParameterNames.COURIER_GIS_STATE, courierGis.getState());
        params.put(BaseParameterNames.COURIER_GIS_LNG, courierGis.getLng());
        params.put(BaseParameterNames.COURIER_GIS_LAT, courierGis.getLat());
        String url = "courier/updateGis.json";
        ApiHttpClient.post(url, params, handler);
    }
    /*
    Courier api end
     */


    /*
    Account api begin
     */

    /**
     * 账户明细列表
     *
     * @param mac
     * @param page
     * @param handler
     */
    public static void listAccountDetail(String mac, Integer page, JsonHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put(BaseParameterNames.APP_TOKEN, AppConfig.app_token);
        params.put(BaseParameterNames.APP_MAC, mac);
        params.put(BaseParameterNames.PAGE_SIZE, Dict.PAGE_SIZE_MAX);
        params.put(BaseParameterNames.PAGE_CUR, page);
        String url = "account/listDetails.json";
        ApiHttpClient.post(url, params, handler);
    }
    /*
    Account api end
     */


    /*
    Order api begin
     */


    /**
     * 获取我的未完成订单
     *
     * @param mac
     * @param handler
     */
    public static void getMyOrder(String mac, JsonHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put(BaseParameterNames.APP_TOKEN, AppConfig.app_token);
        params.put(BaseParameterNames.APP_MAC, mac);
        params.put(BaseParameterNames.PAGE_SIZE, Dict.PAGE_SIZE_MAX);
        params.put(BaseParameterNames.PAGE_CUR, 1);
        String url = "order/getMyOrder.json";
        ApiHttpClient.post(url, params, handler);
    }

    /**
     * 获取服务器已打包的主订单列表
     *
     * @param mac
     * @param state
     * @param page
     * @param handler
     */
    public static void listMainOrder(String mac, Integer state, Integer page, JsonHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put(BaseParameterNames.APP_TOKEN, AppConfig.app_token);
        params.put(BaseParameterNames.APP_MAC, mac);
        params.put(BaseParameterNames.PAGE_SIZE, Dict.DEFAULT_PAGE_SIZE);
        params.put(BaseParameterNames.PAGE_CUR, page);
        params.put(BaseParameterNames.ORDER_STATE, state);
        String url = "order/mainList.json";
        ApiHttpClient.post(url, params, handler);
    }

    public static void listHisMainOrder(String mac, String startDate, String endDate, JsonHttpResponseHandler handler) {

    }

    /**
     * 子订单列表
     *
     * @param mac
     * @param mainOrder
     * @param handler
     */
    public static void listSubOrder(String mac, MainOrder mainOrder, JsonHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put(BaseParameterNames.APP_TOKEN, AppConfig.app_token);
        params.put(BaseParameterNames.APP_MAC, mac);
        params.put(BaseParameterNames.MAIN_ORDER_ID, mainOrder.getId());
        String url = "order/subList.json";
        ApiHttpClient.post(url, params, handler);
    }

    /**
     *
     * @param mac
     * @param mainOrder
     * @param handler
     */
    public static void takeOrder(String mac, MainOrder mainOrder, JsonHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put(BaseParameterNames.APP_TOKEN, AppConfig.app_token);
        params.put(BaseParameterNames.APP_MAC, mac);
        params.put(BaseParameterNames.MAIN_ORDER_ID, mainOrder.getId());
        String url = "order/takeOrder.json";
        ApiHttpClient.post(url, params, handler);
    }

    public static void embraceOrder(String mac, MainOrder mainOrder, JsonHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put(BaseParameterNames.APP_TOKEN, AppConfig.app_token);
        params.put(BaseParameterNames.APP_MAC, mac);
        params.put(BaseParameterNames.MAIN_ORDER_ID, mainOrder.getId());
        params.put(BaseParameterNames.MAIN_ORDER_EMBRACE_CODE, mainOrder.getEmbraceCode());
        String url = "order/embraceOrder.json";
        ApiHttpClient.post(url, params, handler);
    }

    public static void finishOrder(String mac, SubOrderEx subOrderEx, JsonHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put(BaseParameterNames.APP_TOKEN, AppConfig.app_token);
        params.put(BaseParameterNames.APP_MAC, mac);
        params.put(BaseParameterNames.SUB_ORDER_ID, subOrderEx.getId());
        params.put(BaseParameterNames.ORDER_STATE, subOrderEx.getState());
        params.put(BaseParameterNames.SUB_MAIN_ORDER_ID, subOrderEx.getMainOrderId());
        String url = "order/finishOrder.json";
        ApiHttpClient.post(url, params, handler);
    }

    public static void updateSortSubOrder(String mac, MainOrder mainOrder,
                                          List<SubOrder> list, JsonHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put(BaseParameterNames.APP_TOKEN, AppConfig.app_token);
        params.put(BaseParameterNames.APP_MAC, mac);

        params.put(BaseParameterNames.MAIN_ORDER_ID, mainOrder.getId());
        params.put(BaseParameterNames.SORT_SUBORDER_INDEXS, OrderUtil.getSubIndexs(list));
        String url = "order/sortSubOrders.json";
        ApiHttpClient.post(url, params, handler);
    }

    /*
    Order api end
     */
}
