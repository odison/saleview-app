package com.odison.saleview.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import com.alibaba.fastjson.JSON;
import com.baidu.mapapi.model.LatLng;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.odison.saleview.AppConfig;
import com.odison.saleview.AppContext;
import com.odison.saleview.api.ApiHttpClient;
import com.odison.saleview.api.remote.XiaoGeApi;
import com.odison.saleview.base.BaseParameterNames;
import com.odison.saleview.base.Dict;
import com.odison.saleview.bean.Account;
import com.odison.saleview.base.Constants;
import com.odison.saleview.bean.Courier;
import com.odison.saleview.bean.MainOrder;
import com.odison.saleview.bean.MerchantLocation;
import com.odison.saleview.bean.SubOrder;
import com.odison.saleview.bean.SubOrderEx;
import com.odison.saleview.util.OrderUtil;
import com.odison.saleview.util.TLog;
import com.odison.saleview.util.ToastMessageTask;
import com.odison.saleview.util.UIHelper;
import com.tencent.lbssearch.TencentSearch;
import com.tencent.lbssearch.httpresponse.BaseObject;
import com.tencent.lbssearch.httpresponse.HttpResponseListener;
import com.tencent.lbssearch.object.param.Address2GeoParam;
import com.tencent.lbssearch.object.result.Address2GeoResultObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

/**
 * 后台service，包括用户信息获取，订单获取都在此实现
 * User:
 * INTENT_ACTION_REQUEST_GET_INFO
 * INTENT_ACTION_REQUEST_MOD_INFO
 * INTENT_ACTION_LOGOUT
 * <p/>
 * Order:
 * INTENT_ACTION_REQUEST_GEO_SUBORDER
 * INTENT_ACTION_REQUEST_GET_SUBORDER
 * INTENT_ACTION_REQUEST_GET_MYORDER
 * INTENT_ACTION_REQUEST_GET_CATCHED_MAINORDER
 */
public class BackgroundService extends Service {

    private List<LatLng> listLatLng = new ArrayList<>();
    //存储geo转换时候的坐标信息，key为相应子订单的id
    private Map<String, LatLng> mapLatLng = new HashMap();

    private List<SubOrderEx> mListSubOrderEx = new ArrayList<>();
    //存储广播送过来的SubOrder list
    private List<SubOrder> mListSubOrder = new ArrayList<>();

    public BackgroundService() {
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Constants.INTENT_ACTION_REQUEST_GET_INFO.equals(intent.getAction())) {
                if (AppContext.getInstance().isLogin())
                    XiaoGeApi.getInfo(AppConfig.app_mac, mJsonGetInfoHandler);
            } else if (Constants.INTENT_ACTION_REQUEST_MOD_INFO.equals(intent.getAction())) {

            } else if (Constants.INTENT_ACTION_LOGOUT.equals(intent.getAction())) {
                ApiHttpClient.cancelAll(AppContext.getInstance());
                AppContext.getInstance().cleanLoginInfo();
                AppContext.getInstance().cleanOrderInfo();
            } else if (Constants.INTENT_ACTION_GO_ACTIVITY_USERINFO.equals(intent.getAction())) {
                UIHelper.showUserInfoActivity(AppContext.getInstance());
            } else if (Constants.INTENT_ACTION_REQUEST_GEO_SUBORDER.equals(intent.getAction())) {
                boolean caught = intent.getBooleanExtra(Constants.INTENT_ACTIVITY_SUB_ORDER_CAUGHT, false);
                mListSubOrder = (List<SubOrder>) intent.getSerializableExtra(Constants.INTENT_ACTIVITY_SUB_ORDER_LIST);
                getListLatLng(mListSubOrder, caught);
            } else if (Constants.INTENT_ACTION_REQUEST_GET_CATCHED_MAINORDER.equals(intent.getAction())) {
                if (AppContext.getInstance().isLogin()) {
                    int iMainOrderState = intent.getIntExtra(Constants.INTENT_ACTIVITY_MAIN_ORDER_STATE,
                            Dict.MAINORDER_STATE_CATCHED);
                    XiaoGeApi.listMainOrder(AppConfig.app_mac, iMainOrderState, 1, mJsonGetCaughtMainOrderHandler);
                }
            } else if (Constants.INTENT_ACTION_REQUEST_GET_SUBORDER.equals(intent.getAction())) {
                MainOrder mainOrder = (MainOrder) intent.getSerializableExtra(Constants.INTENT_ACTIVITY_MAIN_ORDER);
                if (AppContext.getInstance().isLogin()) {
                    if (mainOrder != null && mainOrder.getId() != null)
                        XiaoGeApi.listSubOrder(AppConfig.app_mac, mainOrder, mJsonGetSubOrderHandler);
                }
            } else if (Constants.INTENT_ACTION_REQUEST_GET_MYORDER.equals(intent.getAction())) {
                if (AppContext.getInstance().isLogin()) {
                    XiaoGeApi.getMyOrder(AppConfig.app_mac, mJsonGetMyOrderHandler);
                }
            } else if (Constants.INTENT_ACTION_REQUEST_UPD_SUBORDER_SORT.equals(intent.getAction())) {
                if (AppContext.getInstance().isLogin()) {
                    MainOrder mainOrder = AppContext.getInstance().getmMainOrder();
                    List<SubOrder> list = AppContext.getInstance().getmSubOrders();
                    AppContext.getInstance().setmSubOrderExs(OrderUtil.getSubOrderExList(list, AppContext.getInstance().getmMapSubOrderLatLngs()));
                    Intent intent2Broad = new Intent();
                    intent2Broad.setAction(Constants.INTENT_ACTION_MAIN_REFRESH);

                    sendBroadcast(intent2Broad);

                    if (mainOrder != null && mainOrder.getId() != null && list.size() != 0) {
                        XiaoGeApi.updateSortSubOrder(AppConfig.app_mac, mainOrder, list, mJsonUpdSortSubOrderIndexHandler);
                    }
                }
            }
        }
    };

    private JsonHttpResponseHandler mJsonGetInfoHandler = new JsonHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            TLog.log("getInfo:", response.toString());
            try {
                if (response.getString(BaseParameterNames.JSON_RESPONSE_STATE).equals("true")) {
                    JSONObject obj = response.getJSONObject(BaseParameterNames.JSON_RESPONSE_DATA);
                    Courier courier = JSON.parseObject(obj.getJSONObject("info").toString(), Courier.class);
                    Account account = JSON.parseObject(obj.getJSONObject("account").toString(), Account.class);
                    AppContext.getInstance().saveCourierInfo(courier);
                    AppContext.getInstance().saveAccountInfo(account);
                    //TODO send broadcast
                    Intent intent = new Intent();
                    intent.setAction(Constants.INTENT_ACTION_REMOTE_ACCOUNT_UPDATE);
                    sendBroadcast(intent);

                } else {
                    //TODO get error
                }
            } catch (JSONException e) {
                TLog.error(e.toString());
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
            super.onFailure(statusCode, headers, throwable, errorResponse);
        }
    };

    private JsonHttpResponseHandler mJsonGetCaughtMainOrderHandler = new JsonHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            TLog.log("BackgroundService:listOrder", response.toString());
            try {
                if (response.getString(BaseParameterNames.JSON_RESPONSE_STATE).equals("true")) {

                    JSONObject obj = response.getJSONObject(BaseParameterNames.JSON_RESPONSE_DATA);

                    List<MainOrder> listMainOrder = JSON.parseArray(obj.getJSONArray(BaseParameterNames.JSON_RESPONSE_LIST_MAIN_ORDER).toString(),
                            MainOrder.class);
                    Intent intent = new Intent();
                    intent.setAction(Constants.INTENT_ACTION_BACKTOUI_GET_CATCHED_MAINORDER);
                    if (listMainOrder != null && listMainOrder.size() != 0) {
                        AppContext.getInstance().setmMainOrder(listMainOrder.get(0));
                        //TODO send broadcast to activity

                        intent.putExtra(Constants.INTENT_ACTIVITY_MAIN_ORDER, listMainOrder.get(0));


                    } else {
                        intent.putExtra(Constants.INTENT_ACTIVITY_MAIN_ORDER, new MainOrder());
//                        new ToastMessageTask().execute("当前没有未完成的订单，请先前往接单界面接取订单");
                    }
                    sendBroadcast(intent);
                } else {
                    TLog.log("BackgroundService:listOrder:stateerror", response.toString());
                }
            } catch (JSONException e) {
                TLog.error(e.toString());
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            super.onFailure(statusCode, headers, throwable, errorResponse);
        }
    };

    private JsonHttpResponseHandler mJsonGetSubOrderHandler = new JsonHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            TLog.log("BackgroundService:mlistSubOrder:onSuccess", response.toString());
            try {
                if (response.getString(BaseParameterNames.JSON_RESPONSE_STATE).equals("true")) {
                    JSONObject obj = response.getJSONObject(BaseParameterNames.JSON_RESPONSE_DATA);
                    MainOrder mainOrder = JSON.parseObject(obj.getJSONObject(BaseParameterNames.MAIN_ORDER).toString(),
                            MainOrder.class);
                    MerchantLocation merchantLocation = JSON.parseObject(obj.getJSONObject(BaseParameterNames.MERCHANT_LOCATION).toString(),
                            MerchantLocation.class);
                    List<SubOrder> listSubOrder = JSON.parseArray(obj.getJSONArray(BaseParameterNames.LIST_SUBORDER).toString(),
                            SubOrder.class);

                    Intent intent = new Intent();
                    intent.setAction(Constants.INTENT_ACTION_BACKTOUI_GET_SUBORDER);
                    intent.putExtra(Constants.INTENT_ACTIVITY_SUB_ORDER_LIST, (Serializable) listSubOrder);
                    intent.putExtra(Constants.INTENT_ACTIVITY_MAIN_ORDER, mainOrder);
                    intent.putExtra(Constants.INTENT_ACTIVITY_MERCHANT_LOCATION, merchantLocation);

                    sendBroadcast(intent);
                } else {
                    //TODO show window for back action
                    new ToastMessageTask().execute("无法获取到相应的信息");
                }
            } catch (JSONException e) {
                TLog.error(e.toString());
//                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            super.onFailure(statusCode, headers, throwable, errorResponse);
        }
    };

    private JsonHttpResponseHandler mJsonGetMyOrderHandler = new JsonHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            TLog.log("BackgroundService:getMyOrder:onSuccess", response.toString());

            try {
                Intent intent = new Intent();
                intent.setAction(Constants.INTENT_ACTION_BACKTOUI_GET_MYORDER);
                if (response.getString(BaseParameterNames.JSON_RESPONSE_STATE).equals("true")) {
                    JSONObject obj = response.getJSONObject(BaseParameterNames.JSON_RESPONSE_DATA);
                    try {
                        MainOrder mainOrder = JSON.parseObject(obj.getJSONObject(BaseParameterNames.MAIN_ORDER).toString(),
                                MainOrder.class);
                        MerchantLocation merchantLocation = JSON.parseObject(obj.getJSONObject(BaseParameterNames.MERCHANT_LOCATION).toString(),
                                MerchantLocation.class);
                        List<SubOrder> listSubOrder = JSON.parseArray(obj.getJSONArray(BaseParameterNames.LIST_SUBORDER).toString(),
                                SubOrder.class);

                        intent.putExtra(Constants.INTENT_ACTIVITY_SUB_ORDER_LIST, (Serializable) listSubOrder);
                        intent.putExtra(Constants.INTENT_ACTIVITY_MAIN_ORDER, mainOrder);
                        intent.putExtra(Constants.INTENT_ACTIVITY_MERCHANT_LOCATION, merchantLocation);
                        intent.putExtra(Constants.INTENT_ACTIVITY_MAIN_ORDER_CAUGHT, true);

                    } catch (JSONException eTransData) {
                        intent.putExtra(Constants.INTENT_ACTIVITY_MAIN_ORDER_CAUGHT, false);
                    }

                } else {
                    intent.putExtra(Constants.INTENT_ACTIVITY_MAIN_ORDER_CAUGHT, false);
                }
                sendBroadcast(intent);
            } catch (JSONException e) {
                TLog.error(e.toString());
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            super.onFailure(statusCode, headers, throwable, errorResponse);
        }
    };

    private JsonHttpResponseHandler mJsonUpdSortSubOrderIndexHandler = new JsonHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            super.onSuccess(statusCode, headers, response);
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            super.onFailure(statusCode, headers, throwable, errorResponse);
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.INTENT_ACTION_REQUEST_GET_INFO);
        filter.addAction(Constants.INTENT_ACTION_REQUEST_MOD_INFO);

        filter.addAction(Constants.INTENT_ACTION_REQUEST_GET_SUBORDER);
        filter.addAction(Constants.INTENT_ACTION_REQUEST_UPD_SUBORDER_SORT);
        filter.addAction(Constants.INTENT_ACTION_REQUEST_GEO_SUBORDER);
        filter.addAction(Constants.INTENT_ACTION_REQUEST_GET_CATCHED_MAINORDER);
        filter.addAction(Constants.INTENT_ACTION_REQUEST_GET_MYORDER);

        filter.addAction(Constants.INTENT_ACTION_LOGOUT);

        //test for service start activity
        filter.addAction(Constants.INTENT_ACTION_GO_ACTIVITY_USERINFO);

        registerReceiver(mReceiver, filter);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }


    /**
     * geo查询 子订单各地址坐标
     *
     * @param list
     */
    private void getListLatLng(final List<SubOrder> list, final Boolean catched) {
        mapLatLng.clear();
        mListSubOrderEx.clear();
        for (final SubOrder subOrder : list) {
            TencentSearch txSearch = new TencentSearch(this);
            Address2GeoParam param = new Address2GeoParam().address(subOrder.getReceiverAddress());

            txSearch.address2geo(param, new HttpResponseListener() {
                @Override
                public void onSuccess(int i, org.apache.http.Header[] headers, BaseObject baseObject) {
                    if (baseObject != null) {
                        Address2GeoResultObject oj = (Address2GeoResultObject) baseObject;

                        if (oj.result != null) {
                            TLog.log("Background:Addr2Geo", "Address:" + subOrder.getReceiverAddress() +
                                    "location:" + oj.result.location.toString());

                            LatLng latlng = new LatLng(oj.result.location.lat, oj.result.location.lng);
                            mapLatLng.put(subOrder.getId(), latlng);

                            if (mapLatLng.size() == list.size()) {
                                TLog.log("Background:Addr2Geo:generate", mapLatLng.size() + "");
                                if (catched)
                                    AppContext.getInstance().setmMapSubOrderLatLngs(mapLatLng);
                                generateListSubOrderEx(list, mapLatLng, catched);
                            }

                        }

                    }
                }

                @Override
                public void onFailure(int i, org.apache.http.Header[] headers, String s, Throwable throwable) {
                    TLog.error("" + subOrder.getId() + " addr2Geo error");
                    mapLatLng.put(subOrder.getId(), new LatLng(0, 0));
                }
            });
        }
    }

    private void generateListSubOrderEx(List<SubOrder> list, Map<String, LatLng> map, Boolean catched) {
//        for (SubOrder subOrder : list) {
//            SubOrderEx subOrderEx = new SubOrderEx(subOrder);
//            LatLng latlng = map.get(subOrder.getId());
//            subOrderEx.setLat(latlng.latitude);
//            subOrderEx.setLng(latlng.longitude);
//            mListSubOrderEx.add(subOrderEx);
//        }
        mListSubOrderEx = OrderUtil.getSubOrderExList(list, map);

        Intent intent = new Intent();
        intent.setAction(Constants.INTENT_ACTION_BACKTOUI_GEO_DONE);
        intent.putExtra(Constants.INTENT_ACTIVITY_SUB_ORDER_CAUGHT, catched);
        intent.putExtra(Constants.INTENT_ACTIVITY_SUB_ORDER_EX_LIST, (Serializable) mListSubOrderEx);
        sendBroadcast(intent);
    }


}
