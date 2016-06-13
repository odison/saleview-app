package com.odison.saleview.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

//import org.apache.http.Header;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.odison.saleview.AppContext;
import com.odison.saleview.AppManager;
import com.odison.saleview.R;
import com.odison.saleview.base.BaseParameterNames;
import com.odison.saleview.base.Constants;
import com.odison.saleview.base.Dict;
import com.odison.saleview.bean.MainOrder;
import com.odison.saleview.bean.MerchantLocation;
import com.odison.saleview.bean.SubOrder;
import com.odison.saleview.bean.SubOrderEx;
import com.odison.saleview.util.CoordinateUtil;
import com.odison.saleview.util.TLog;
import com.odison.saleview.util.ToastMessageTask;
import com.odison.saleview.util.UIHelper;
//import com.tencent.mapsdk.raster.model.LatLng;
//import com.tencent.lbssearch.TencentSearch;
//import com.tencent.lbssearch.httpresponse.BaseObject;
//import com.tencent.lbssearch.httpresponse.HttpResponseListener;
//import com.tencent.lbssearch.object.param.Address2GeoParam;
//import com.tencent.lbssearch.object.result.Address2GeoResultObject;
//import com.tencent.mapsdk.raster.model.LatLng;
//import com.tencent.mapsdk.raster.model.Marker;
//import com.tencent.mapsdk.raster.model.MarkerOptions;
//import com.tencent.tencentmap.mapsdk.map.MapView;
//import com.tencent.tencentmap.mapsdk.map.TencentMap;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OrderDetailMapActivity extends AppCompatActivity {

    private MainOrder mMainOrder = new MainOrder();
    private List<SubOrder> listSubOrder = new ArrayList<SubOrder>();
    private List<SubOrderEx> listSubOrderEx = new ArrayList<>();
    private MerchantLocation mLocation = new MerchantLocation();


    private InfoWindow mInfoWindow;


    private int iMapIndex = 0;
//    private Integer[] iBitMap = {R.drawable.icon_marka, R.drawable.icon_markb,
//            R.drawable.icon_markc, R.drawable.icon_markd, R.drawable.icon_marke,
//            R.drawable.icon_markf, R.drawable.icon_markg, R.drawable.icon_markh,
//            R.drawable.icon_marki, R.drawable.icon_markj
//    };

    private TextView mTxvMainOrderCounty;
    private TextView mTxvMainOrderSubCount;
    private TextView mTxvMainOrderLocationAddress;
    private TextView mTxvMainOrderReword;
    private TextView mTxvMainOrderCatch;


    private MapView mMapView;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Constants.INTENT_ACTION_BACKTOUI_GEO_DONE.equals(intent.getAction())) {
                boolean catched = intent.getBooleanExtra(Constants.INTENT_ACTIVITY_SUB_ORDER_CAUGHT,true);
                if(!catched) {
                    listSubOrderEx = (List<SubOrderEx>) intent.getSerializableExtra(Constants.INTENT_ACTIVITY_SUB_ORDER_EX_LIST);
                    for (SubOrderEx subOrderEx : listSubOrderEx
                            ) {
                        LatLng latLng = CoordinateUtil.GCJ02_TO_BD09(subOrderEx.getLat(), subOrderEx.getLng());
                        mMapView.getMap().addOverlay(new MarkerOptions().title(subOrderEx.getReceiverAddress())
                                .icon(BitmapDescriptorFactory.fromResource(Dict.MARKER_BITMAPS[subOrderEx.getSubIndex()%Dict.MARKER_BITMAPS.length]))
                                        .position(latLng).anchor(0.5f, 1.0f)
                        );
                    }
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail_map);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //添加标题栏back操作按钮
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initView();

        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.INTENT_ACTION_BACKTOUI_GEO_DONE);

        registerReceiver(mReceiver, filter);

        Intent intent = getIntent();
        mMainOrder = (MainOrder) intent.getSerializableExtra(Constants.INTENT_ACTIVITY_MAIN_ORDER);
        listSubOrder = (List<SubOrder>) intent.getSerializableExtra(Constants.INTENT_ACTIVITY_SUB_ORDER_LIST);
        mLocation = (MerchantLocation) intent.getSerializableExtra(Constants.INTENT_ACTIVITY_MERCHANT_LOCATION);

        mTxvMainOrderCatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO catch mainorder
            }
        });

        refreshView();

//        // 将google地图、soso地图、aliyun地图、mapabc地图和amap地图// 所用坐标转换成百度坐标
//        CoordinateConverter converter = new CoordinateConverter();
//        converter.from(CoordinateConverter.CoordType.COMMON);
//        // sourceLatLng待转换坐标
//        converter.coord(new LatLng(mLocation.getLat(), mLocation.getLng()));
        LatLng desLatLng = CoordinateUtil.GCJ02_TO_BD09(mLocation.getLat(), mLocation.getLng());

        //添加仓储的位置
        mMapView.getMap().addOverlay(new MarkerOptions()
                .title(mLocation.getLocation())
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_markcang))
                .position(desLatLng).anchor(0.5f, 1.0f));


        mMapView.getMap().animateMapStatus(MapStatusUpdateFactory.newLatLng(desLatLng));
        mMapView.getMap().animateMapStatus(MapStatusUpdateFactory.zoomTo(13.0f));
        int idx = 0;
        for (SubOrder subOrder : listSubOrder) {
            subOrder.setSubIndex(idx++);
        }


        Intent intent2Borad = new Intent();
        intent2Borad.setAction(Constants.INTENT_ACTION_REQUEST_GEO_SUBORDER);
        intent2Borad.putExtra(Constants.INTENT_ACTIVITY_SUB_ORDER_CAUGHT, false);
        intent2Borad.putExtra(Constants.INTENT_ACTIVITY_SUB_ORDER_LIST, (Serializable) listSubOrder);

        sendBroadcast(intent2Borad);


//        for (final SubOrder subOrder : listSubOrder) {
//            TencentSearch txSearch = new TencentSearch(this);
//            Address2GeoParam param = new Address2GeoParam().address(subOrder.getReceiverAddress());
//
//            txSearch.address2geo(param, new HttpResponseListener() {
//                @Override
//                public void onSuccess(int i, Header[] headers, BaseObject baseObject) {
//                    if (baseObject != null) {
//                        Address2GeoResultObject oj = (Address2GeoResultObject) baseObject;
//
//                        if (oj.result != null) {
//                            TLog.log("OrderDetailMapActivity:Addr2Geo", "Address:" + subOrder.getReceiverAddress() +
//                                    "location:" + oj.result.location.toString());
////                            new ToastMessageTask().execute("Address:" + subOrder.getReceiverAddress() +
////                                    "location:" + oj.result.location.toString());
//
//                            LatLng latLng = CoordinateUtil.GCJ02_TO_BD09(oj.result.location.lat, oj.result.location.lng);
//                            mMapView.getMap().addOverlay(new MarkerOptions().title(subOrder.getReceiverAddress())
//                                            .icon(BitmapDescriptorFactory.fromResource(iBitMap[subOrder.getSubIndex()]))
//                                            .position(latLng).anchor(0.5f, 1.0f)
//
//                            );
//                            //iMapIndex++;
//
//                        }
//
//                    }
//                }
//
//                @Override
//                public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
//
//                }
//            });
//        }

        mMapView.getMap().setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                LatLng latLng = marker.getPosition();
                TextView tv = new TextView(getApplicationContext());
                tv.setBackgroundResource(R.drawable.location_tips);
                tv.setPadding(30, 20, 30, 50);
                tv.setText(marker.getTitle().toString());
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMapView.getMap().hideInfoWindow();
                    }
                });
                mInfoWindow = new InfoWindow(tv, latLng, -47);
                mMapView.getMap().showInfoWindow(mInfoWindow);

                return false;
            }
        });


    }

    private JsonHttpResponseHandler mJsonHandler = new JsonHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
            TLog.log("OrderDetailMapActivity:takeMainOrder:", response.toString());
            try {
                if(response.getString(BaseParameterNames.JSON_RESPONSE_STATE).equals("true")){
                    //TODO 接单成功 跳转到我的订单
                    new ToastMessageTask().execute("接单成功");
                    AppContext.getInstance().setCourierState(Dict.COURIER_GIS_STATE_TAKEN);
                    AppManager.getAppManager().finishActivity(OrderActivity.class);
                    AppManager.getAppManager().finishActivity(OrderDetailActivity.class);
                    UIHelper.showMyOrderActivity(OrderDetailMapActivity.this, Dict.MAINORDER_STATE_CATCHED);

                    //
                    Intent intent = new Intent();
                    intent.setAction(Constants.INTENT_ACTION_BACKTOUI_UPD_MYORDER);
                    sendBroadcast(intent);
                    finish();
                }else{
                    //TODO 接单失败 显示失败原因？跳转到远程订单界面
                    new ToastMessageTask().execute("该订单不存在或被他人接取...");
                    AppManager.getAppManager().finishActivity(OrderDetailActivity.class);
                    finish();
                }
            } catch (JSONException e) {
                TLog.log("json:exception",e.toString());
            }
        }

        @Override
        public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONObject errorResponse) {
            super.onFailure(statusCode, headers, throwable, errorResponse);
        }
    };

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

//    @Override
//    protected void onStop() {
//        mMapView.onStop();
//        super.onStop();
//    }

    @Override
    protected void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    private void initView() {
        mTxvMainOrderCounty = (TextView) findViewById(R.id.txv_order_detail_main_county);
        mTxvMainOrderLocationAddress = (TextView) findViewById(R.id.txv_order_detail_main_location_address);
        mTxvMainOrderSubCount = (TextView) findViewById(R.id.txv_order_detail_main_suborder_count);
        mTxvMainOrderReword = (TextView) findViewById(R.id.txv_order_detail_main_reword);
        mTxvMainOrderCatch = (TextView) findViewById(R.id.txv_order_detail_main_catch);

        mMapView = (MapView) findViewById(R.id.mv_order_detail);
        mMapView.showZoomControls(false);
        mMapView.getMap().getUiSettings().setRotateGesturesEnabled(false);

    }

    private void refreshView() {
        if (mMainOrder != null && mMainOrder.getCounty() != null)
            mTxvMainOrderCounty.setText("区域：" + mMainOrder.getCounty());
        if (mMainOrder != null && mMainOrder.getAddress() != null)
            mTxvMainOrderLocationAddress.setText("取货地址：" + mMainOrder.getAddress());
        if (mMainOrder != null && mMainOrder.getSubCount() != null)
            mTxvMainOrderSubCount.setText(mMainOrder.getSubCount() + " 个子订单");
        if (mMainOrder != null && mMainOrder.getReword() != null)
            mTxvMainOrderReword.setText("跑腿费：" + mMainOrder.getReword() + " 元");
    }

}
