package com.odison.saleview.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.model.LatLng;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.odison.saleview.AppConfig;
import com.odison.saleview.AppContext;
import com.odison.saleview.api.remote.XiaoGeApi;
import com.odison.saleview.base.Constants;
import com.odison.saleview.bean.CourierGis;
import com.odison.saleview.util.CoordinateUtil;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * baidu map sdk 定位服务，接收广播开启或者关闭定位，通过广播发送定位信息给activity
 * 定位坐标返回gcj-02 坐标系，在地图使用的时候通过坐标转换为bd0911
 */
public class LocationBDService extends Service {

    // 定位相关
    private LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();

    private Integer mLocationInterval = 5000;
    // 20 * 5s = 100s 上传一次坐标到服务器
    private Integer mLocationUploadInterval = 5;

    private Integer mLocationTick = 0;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Constants.INTENT_ACTION_SERVICE_LOCATION_START.equals(intent.getAction())) {
                startLocation();
            } else if (Constants.INTENT_ACTION_SERVICE_LOCATION_STOP.equals(intent.getAction()) ||
                    Constants.INTENT_ACTION_LOGOUT.equals(intent.getAction())) {
                stopLocation();
            }
        }
    };

    private boolean mStart = false;

    public LocationBDService() {
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //new ToastMessageTask().execute("service create");
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.INTENT_ACTION_SERVICE_LOCATION_START);
        filter.addAction(Constants.INTENT_ACTION_SERVICE_LOCATION_STOP);
        filter.addAction(Constants.INTENT_ACTION_LOGOUT);

        registerReceiver(mReceiver, filter);

        // 定位初始化
        mLocClient = new LocationClient(AppContext.getInstance());
        mLocClient.registerLocationListener(myListener);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        if (mStart)
            mLocClient.stop();
        super.onDestroy();
    }

    public void startLocation() {
        if (mStart) return;

        LocationClientOption option = new LocationClientOption();

        option.setOpenGps(true);// 打开gps
        //返回gcj02  再转成bd0911 供地图显示
        option.setCoorType("gcj02"); // 设置坐标类型
        option.setScanSpan(mLocationInterval);
        mLocClient.setLocOption(option);
        mLocClient.start();
        mStart = true;
    }

    public void stopLocation() {
        if (mStart) {
            mLocClient.stop();
            mStart = false;
        }
    }


    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
//            new ToastMessageTask().execute("lat:"+location.getLatitude()+",lng:"+location.getLongitude());
            if (location == null) {
                return;
            }

            mLocationTick++;
            LatLng latlng = new LatLng(location.getLatitude(), location.getLongitude());
            //存储gcj02坐标系坐标
            //App统一存储gcj02 坐标，计算时转换为bd0911
            AppContext.getInstance().setmLocation(latlng);
            LatLng desLatLng = CoordinateUtil.GCJ02_TO_BD09(latlng);

            Intent intent = new Intent();
            intent.setAction(Constants.INTENT_ACTION_LOCATION_CHANGE);
            intent.putExtra("lat", desLatLng.latitude);
            intent.putExtra("lng", desLatLng.longitude);
            intent.putExtra("accuracy", location.getRadius());
            sendBroadcast(intent);

//            new ToastMessageTask().execute(""+mLocationTick+" "+AppContext.getInstance().getCourierState()+ " "+AppContext.getInstance().isLocationUploadStart());

            if (mLocationTick >= mLocationUploadInterval) {
                mLocationTick = 0;
                CourierGis courierGis = new CourierGis();
                courierGis.setLat(location.getLatitude());
                courierGis.setLng(location.getLongitude());
                //
                AppContext.getInstance().refreshCourierState();
                courierGis.setState(AppContext.getInstance().getCourierState());
                //登录状态才上传坐标，减轻服务器压力
                if (AppContext.getInstance().isLogin() &&
                        AppContext.getInstance().isLocationUploadStart())
                    XiaoGeApi.updateGis(AppConfig.app_mac, courierGis, mJsonHandler);
            }//if

        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }

    private JsonHttpResponseHandler mJsonHandler = new JsonHttpResponseHandler() {
        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            super.onFailure(statusCode, headers, throwable, errorResponse);
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            super.onSuccess(statusCode, headers, response);
        }
    };
}
