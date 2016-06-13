package com.odison.saleview.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.odison.saleview.AppConfig;
import com.odison.saleview.AppContext;
import com.odison.saleview.api.remote.XiaoGeApi;
import com.odison.saleview.base.Constants;
import com.odison.saleview.bean.CourierGis;
import com.odison.saleview.util.ToastMessageTask;
import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;
import com.tencent.mapsdk.raster.model.LatLng;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * tx 地图sdk 定位服务，目前未使用，改用百度定位
 */
public class LocationService extends Service implements TencentLocationListener {
    //LocationManager mLocationManager;

    private TencentLocation mLocation;

    private Integer mLocationInterval = 5000;
    // 20 * 5s = 100s 上传一次坐标到服务器
    private Integer mLocationUploadInterval = 20;

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

    public LocationService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //startLocation();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new ToastMessageTask().execute("service create");
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.INTENT_ACTION_SERVICE_LOCATION_START);
        filter.addAction(Constants.INTENT_ACTION_SERVICE_LOCATION_STOP);
        filter.addAction(Constants.INTENT_ACTION_LOGOUT);

        registerReceiver(mReceiver, filter);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        new ToastMessageTask().execute("service stoped");
        if (mStart) stopLocation();
        unregisterReceiver(mReceiver);
        // stopLocation();
    }

    private void startLocation() {
        if (mStart) return;
        TencentLocationRequest request = TencentLocationRequest.create();
        request.setInterval(mLocationInterval);
        TencentLocationManager.getInstance(AppContext.getInstance())
                .requestLocationUpdates(request, this);
        mStart = true;
    }

    private void stopLocation() {
        if (mStart) {
            TencentLocationManager.getInstance(AppContext.getInstance())
                    .removeUpdates(this);
            mStart = false;
        }
    }


//    private void startLocation() {
//        TencentLocationRequest request = TencentLocationRequest.create();
//        request.setInterval(AppConfig.APP_LOCATION_INTERVAL);
//        mLocationManager.requestLocationUpdates(request, AppContext.getInstance());
//
////        mRequestParams = request.toString() + ", 坐标系="
////                + DemoUtils.toString(mLocationManager.getCoordinateType());
//    }
//
//    private void stopLocation() {
//        mLocationManager.removeUpdates(this);
//    }

//    @Override
//    public void onLocationChanged(TencentLocation tencentLocation, int i, String s) {
//
//    }
//
//    @Override
//    public void onStatusUpdate(String s, int i, String s1) {
//
//    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
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

    @Override
    public void onLocationChanged(TencentLocation tencentLocation, int i, String s) {
        if (i == tencentLocation.ERROR_OK) {

            // String tmp = "lat:"+tencentLocation.getLatitude()+" lng:"+tencentLocation.getLongitude();
            //new ToastMessageTask().execute(tmp);

            mLocationTick++;
            LatLng location = new LatLng(tencentLocation.getLatitude(), tencentLocation.getLongitude());
//            AppContext.getInstance().setmLocation(location);
            Intent intent = new Intent();
            intent.setAction(Constants.INTENT_ACTION_LOCATION_CHANGE);
            intent.putExtra("lat", location.getLatitude());
            intent.putExtra("lng", location.getLongitude());
            intent.putExtra("accuracy", tencentLocation.getAccuracy());
            sendBroadcast(intent);

            if (mLocationTick.equals(mLocationUploadInterval)) {
                mLocationTick %= mLocationUploadInterval;
                CourierGis courierGis = new CourierGis();
                courierGis.setLat(location.getLatitude());
                courierGis.setLng(location.getLongitude());
                //
                courierGis.setState(AppContext.getInstance().getCourierState());
                if(AppContext.getInstance().isLocationUploadStart())
                    XiaoGeApi.updateGis(AppConfig.app_mac,courierGis,mJsonHandler);
            }
        }
    }

    @Override
    public void onStatusUpdate(String s, int i, String s1) {

    }
}
