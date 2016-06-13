package com.odison.saleview.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.design.internal.NavigationMenuView;
import android.view.KeyEvent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.odison.saleview.AppConfig;
import com.odison.saleview.AppContext;
import com.odison.saleview.AppManager;
import com.odison.saleview.R;
import com.odison.saleview.adapter.DlgPlusListAdapter;
import com.odison.saleview.adapter.DlgPlusOrderCtrlAdapter;
import com.odison.saleview.adapter.DlgPlusRouteAdapter;
import com.odison.saleview.api.remote.XiaoGeApi;
import com.odison.saleview.base.BaseParameterNames;
import com.odison.saleview.base.Constants;
import com.odison.saleview.base.Dict;
import com.odison.saleview.bean.MainOrder;
import com.odison.saleview.bean.MapRouteNode;
import com.odison.saleview.bean.MerchantLocation;
import com.odison.saleview.bean.SubOrder;
import com.odison.saleview.bean.SubOrderEx;
import com.odison.saleview.mapapi.overlayutil.DrivingRouteOverlay;
import com.odison.saleview.mapapi.overlayutil.OverlayManager;
import com.odison.saleview.util.CoordinateUtil;
import com.odison.saleview.util.OrderUtil;
import com.odison.saleview.util.RouteNodeUtil;
import com.odison.saleview.util.TLog;
import com.odison.saleview.util.ToastMessageTask;
import com.odison.saleview.util.UIHelper;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ListHolder;
import com.orhanobut.dialogplus.OnItemClickListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        SensorEventListener,
        NavigationMenuView.OnCreateContextMenuListener,
        OnGetRoutePlanResultListener, BaiduMap.OnMapClickListener {

    private SensorManager sensorManager;
    private Sensor acc_sensor;
    private Sensor mag_sensor;
    //加速度传感器数据
    float accValues[] = new float[3];
    //地磁传感器数据
    float magValues[] = new float[3];
    //旋转矩阵，用来保存磁场和加速度的数据
    float r[] = new float[9];
    //模拟方向传感器的数据（原始数据为弧度）
    float values[] = new float[3];
    float mDirection = 0;

    private MapView mapView = null;
    private BaiduMap mBaiduMap;

    private long firstTime = 0;

    private boolean onceLocation = false;

    private SubOrderEx mSubOrderEx;


//    private LocationOverlay mLocationOverlay;


    private ImageView mQuickImageView;

    //    private View mViewQuickOption;
//    private ImageView mImageViewQuickSound;
    private ImageButton mImageViewPosition;

    private Integer mMapType;
    private ImageButton mImgBtnMapSwitch;

    private LocationMode mCurrentMode;
    private ImageButton mImgBtnMapLocSwitch;

    private ImageButton mImgBtnMapZoomIn;
    private ImageButton mImgBtnMapZoomOut;

    private ImageButton mImgBtnPreOrder;
    private ImageButton mImgBtnNextOrder;

    private ImageButton mImgBtnQuickPhone;
    private ImageButton mImgBtnRouteList;

    private ImageButton mImgBtnEmbrace;
    private ImageButton mImgBtnDispatch;

    private ImageButton mImgBtnListDetail;

    //
    private boolean mPositionStart;

    private boolean mCompassLocSet = false;
    private boolean mFirstLocation = false;
    private boolean mFirstLocationEnd = false;

    private DialogPlus mDlgPlusOrderList;
    private DialogPlus mDlgPlusOrderCtrl;
    private DialogPlus mDlgPlusRouteList;

    private DlgPlusOrderCtrlAdapter mDlgPlusOrderCtrlAdapter;
    private DlgPlusListAdapter mDlgPlusListAdapter;
    private DlgPlusRouteAdapter mDlgPlusRouteListAdapter;

    private Map<String, Marker> mMarkerList;
    private Marker mMarkerStorage;
    //marker 窗口
    private InfoWindow mInfoWindow;


    private TextView mTxvShowSubOrderExAddress;

    private View mViewShowSubOrderExAddress;

    //绘制路径相关
    int nodeIndex = -1; // 节点索引,供浏览节点时使用
    RouteLine route = null;
    OverlayManager routeOverlay = null;
    boolean useDefaultIcon = false;
    // 搜索相关
    RoutePlanSearch mSearch = null;    // 搜索模块，也可去掉地图模块独立使用

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //接收后台的定位数据，实时显示在地图上
            if (Constants.INTENT_ACTION_LOCATION_CHANGE.equals(intent.getAction())) {
                double lat = intent.getDoubleExtra("lat", 0.00);
                double lng = intent.getDoubleExtra("lng", 0.00);
                float accuracy = intent.getFloatExtra("accuracy", 0);
//                String tmp = "lat:"+lat+"-lng:"+lng;
//                new ToastMessageTask().execute(tmp);
                //new ToastMessageTask().execute(LocationDemo.this, "ss");
                MyLocationData locData = new MyLocationData.Builder()
                        .accuracy(accuracy)
                                // 此处设置开发者获取到的方向信息，顺时针0-360
                        .direction(mDirection)
                        .latitude(lat)
                        .longitude(lng).build();
                mapView.getMap().setMyLocationData(locData);

                MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(new LatLng(lat, lng));
                // mBaiduMap.animateMapStatus(u);
                mapView.getMap().animateMapStatus(u);

                if (mFirstLocation && !mFirstLocationEnd) {
                    //第一次定位完成后，关闭定位
                    mFirstLocationEnd = true;
                    Intent broadCastIntent = new Intent();
                    broadCastIntent.setAction(Constants.INTENT_ACTION_SERVICE_LOCATION_STOP);
                    sendBroadcast(broadCastIntent);
                }

            } else if (Constants.INTENT_ACTION_LOGOUT.equals(intent.getAction())) {
                //TODO log out ctrl to hide quick imagebutton
                //clear marker
                mBaiduMap.clear();
                mSubOrderEx = null;
                showCurrentSubOrder();
                UIHelper.notifyClearAll();


            } else if (Constants.INTENT_ACTION_BACKTOUI_GET_MYORDER.equals(intent.getAction())) {
                boolean caught = intent.getBooleanExtra(Constants.INTENT_ACTIVITY_MAIN_ORDER_CAUGHT, false);
                if (caught) {
                    MainOrder mainOrder = (MainOrder) intent.getSerializableExtra(Constants.INTENT_ACTIVITY_MAIN_ORDER);
                    MerchantLocation merchantLocation = (MerchantLocation) intent.getSerializableExtra(Constants.INTENT_ACTIVITY_MERCHANT_LOCATION);
                    List<SubOrder> list = (List<SubOrder>) intent.getSerializableExtra(Constants.INTENT_ACTIVITY_SUB_ORDER_LIST);

//                    if (AppContext.getInstance().getmMainOrder().getId() == null) {
//                            || OrderUtil.isOrderFinished(AppContext.getInstance().getmMainOrder())){
                    AppContext.getInstance().setmMainOrder(mainOrder);
                    AppContext.getInstance().setmSubOrders(list);
                    AppContext.getInstance().setmMerchantLocation(merchantLocation);
                    AppContext.getInstance().setmCusor(0);

                    if(mMarkerStorage != null)
                        mMarkerStorage.remove();
                    //添加仓储标记
                    LatLng latLng = CoordinateUtil.GCJ02_TO_BD09(merchantLocation.getLat(), merchantLocation.getLng());
                    mMarkerStorage = (Marker)mBaiduMap.addOverlay(new MarkerOptions()
                            .title(merchantLocation.getLocation())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_markcang))
                            .position(latLng).anchor(0.5f, 1.0f));

                    Intent intent2Board = new Intent();
                    intent2Board.setAction(Constants.INTENT_ACTION_REQUEST_GEO_SUBORDER);
                    intent2Board.putExtra(Constants.INTENT_ACTIVITY_SUB_ORDER_CAUGHT, true);
                    intent2Board.putExtra(Constants.INTENT_ACTIVITY_SUB_ORDER_LIST, (Serializable) list);

                    sendBroadcast(intent2Board);

                    //初始化的时候发现有未完成订单
                    AppContext.getInstance().setCourierState(Dict.COURIER_GIS_STATE_SENDING);

                    UIHelper.notifySimpleNotification("有未完成的订单", "订单号：" + mainOrder.getId(),
                            "已完成：" + mainOrder.getSubFinishedCount() + "/" + mainOrder.getSubCount(),
                            MyOrderActivity.class);

//                        AppContext.getInstance().noti
//                    }
                } else {
                    new ToastMessageTask().execute("当前没有未完成的订单，请前往接单界面接取订单");
                }
            } else if (Constants.INTENT_ACTION_BACKTOUI_GEO_DONE.equals(intent.getAction())) {
                boolean caught = intent.getBooleanExtra(Constants.INTENT_ACTIVITY_SUB_ORDER_CAUGHT, false);
                if (caught) {
                    List<SubOrderEx> subOrderExList = (List<SubOrderEx>) intent.getSerializableExtra(Constants.INTENT_ACTIVITY_SUB_ORDER_EX_LIST);
                    AppContext.getInstance().refreshCourierState();
                    for (int i = 0; i != subOrderExList.size(); ++i) {
                        subOrderExList.get(i).setSubIndex(i);
                    }
                    AppContext.getInstance().setmSubOrderExs(subOrderExList);

                    //clear marker
                    for (String key : mMarkerList.keySet()) {
                        mMarkerList.get(key).remove();
                    }
                    mMarkerList.clear();

                    for (SubOrderEx subOrderEx : subOrderExList
                            ) {
                        if (subOrderEx.getState() == Dict.SUBORDER_STATE_CATCHED ||
                                subOrderEx.getState() == Dict.SUBORDER_STATE_DISPATCHING) {
                            LatLng latLng = CoordinateUtil.GCJ02_TO_BD09(subOrderEx.getLat(), subOrderEx.getLng());
                            Marker marker = (Marker) mBaiduMap.addOverlay(new MarkerOptions().title(subOrderEx.getReceiverAddress())
                                    .icon(BitmapDescriptorFactory.fromResource(Dict.MARKER_BITMAPS[subOrderEx.getSubIndex() % Dict.MARKER_BITMAPS.length]))
                                    .position(latLng).anchor(0.5f, 1.0f));
                            mMarkerList.put(subOrderEx.getId(), marker);
                        }
                    }

                    mSubOrderEx = AppContext.getInstance().getFirstOrder();
                    showCurrentSubOrder();


                }
            } else if (Constants.INTENT_ACTION_BACKTOUI_UPD_MYORDER.equals(intent.getAction())) {
                //TODO 发送广播给后台 获取我的订单，此调用一般为接单之后触发
                Intent intent2Borad = new Intent();
                intent2Borad.setAction(Constants.INTENT_ACTION_REQUEST_GET_MYORDER);

                sendBroadcast(intent2Borad);
            } else if (Constants.INTENT_ACTION_MAIN_REFRESH.equals(intent.getAction())) {
                List<SubOrderEx> subOrderExes = AppContext.getInstance().getmSubOrderExs();
                //刷新快递员状态
                AppContext.getInstance().refreshCourierState();

                //clear marker
                for (String key : mMarkerList.keySet()) {
                    mMarkerList.get(key).remove();
                }
                mMarkerList.clear();
                //
                for (SubOrderEx subOrderEx : subOrderExes
                        ) {
                    if (subOrderEx.getState() == Dict.SUBORDER_STATE_CATCHED ||
                            subOrderEx.getState() == Dict.SUBORDER_STATE_DISPATCHING) {
                        LatLng latLng = CoordinateUtil.GCJ02_TO_BD09(subOrderEx.getLat(), subOrderEx.getLng());
                        Marker marker = (Marker) mBaiduMap.addOverlay(new MarkerOptions().title(subOrderEx.getReceiverAddress())
                                .icon(BitmapDescriptorFactory.fromResource(Dict.MARKER_BITMAPS[subOrderEx.getSubIndex() % Dict.MARKER_BITMAPS.length]))
                                .position(latLng).anchor(0.5f, 1.0f));
                        mMarkerList.put(subOrderEx.getId(), marker);
                    }
                }

                mSubOrderEx = AppContext.getInstance().getCurrentSubOrder();
                if (mSubOrderEx != null && mSubOrderEx.getState() != null &&
                        (mSubOrderEx.getState() == Dict.SUBORDER_STATE_DISPATCHED_PERSONAL ||
                                mSubOrderEx.getState() == Dict.SUBORDER_STATE_DISPATCHED_PROPERTY)) {
                    mSubOrderEx = AppContext.getInstance().getNextOrder();
                }

                showCurrentSubOrder();
                mapToViewSubOrder(mSubOrderEx);


            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ButterKnife.inject(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //
        mPositionStart = false;

        //
        mMarkerList = new HashMap<>();
        //进入app开启坐标上传开关
        AppContext.getInstance().setLocationUploadStart(true);


        SubOrder subOrder = new SubOrder();
        mSubOrderEx = new SubOrderEx(subOrder);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);

        mQuickImageView = (ImageView) findViewById(R.id.quick_option_iv);
        mQuickImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int id = v.getId();
                switch (id) {
                    // 点击了快速操作按钮
                    case R.id.quick_option_iv:

//                        UIHelper.notifySimpleNotification("有新订单","区域：路桥","包含10个子订单,赏金××",UserInfoActivity.class);
//                        //TODO 测试定位服务开启
//                        AppContext.getInstance().setCourierState(Dict.COURIER_GIS_STATE_WAIT);
//                        AppContext.getInstance().setLocationUploadStart(true);
//                        Intent intent = new Intent();
//                        //intent.setAction(Constants.INTENT_ACTION_REQUEST_GET_INFO);
//                        intent.setAction(Constants.INTENT_ACTION_SERVICE_LOCATION_START);
//                        sendBroadcast(intent);
                        //SpeechUtil.simpleSpeech(AppContext.getInstance(),"我是谁");
                        //语音提示测试
                        //AppContext.getInstance().simpleSpeek("在这里，你看到了什么？");
                        //showQuickOption();
                        if (AppContext.getInstance().isLogin()) {
//                            mSubOrderEx = AppContext.getInstance().getCurrentSubOrder();
                            if (mSubOrderEx != null && mSubOrderEx.getId() != null) {
                                showOrderCtrlForm(MainActivity.this, mSubOrderEx, OrderDispatchCtrlOnClickListener);
                            }else{
                                new ToastMessageTask().execute("当前没有配送订单，或者订单已经配送完成");
                            }
                        } else {
                            new ToastMessageTask().execute(getString(R.string.toast_no_login_tips));
                        }
                        break;
                    default:
                        break;
                }
            }
        });


        //管理activity
        AppManager.getAppManager().addActivity(this);
        //init map type
        mMapType = BaiduMap.MAP_TYPE_NORMAL;
        mCurrentMode = LocationMode.NORMAL;

        initMap();

        initView();

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        acc_sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mag_sensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        //给传感器注册监听：
        sensorManager.registerListener(this, acc_sensor, SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this, mag_sensor, SensorManager.SENSOR_DELAY_GAME);

        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.INTENT_ACTION_LOCATION_CHANGE);
        filter.addAction(Constants.INTENT_ACTION_ORDER_GO_EMBRACE);
        filter.addAction(Constants.INTENT_ACTION_ORDER_GO_DISPATCHING);
        filter.addAction(Constants.INTENT_ACTION_LOGOUT);
        filter.addAction(Constants.INTENT_ACTION_BACKTOUI_GET_MYORDER);
        filter.addAction(Constants.INTENT_ACTION_BACKTOUI_GEO_DONE);
        filter.addAction(Constants.INTENT_ACTION_BACKTOUI_UPD_MYORDER);
        filter.addAction(Constants.INTENT_ACTION_MAIN_REFRESH);

        registerReceiver(mReceiver, filter);

        if (AppContext.getInstance().isLogin()) {
            //已自动登录的情况需要向服务器请求是否有已接取的单
            Intent intent = new Intent();
            intent.setAction(Constants.INTENT_ACTION_REQUEST_GET_MYORDER);

            sendBroadcast(intent);
//            AppContext.getInstance().simpleSpeek("请稍后，系统正在获取您的订单信息");
        } else {
            UIHelper.notifySimpleNotification("欢迎使用富氧到家，前往登录获取更多服务", "点击前往登录", "", LoginActivity.class);
//            AppContext.getInstance().simpleSpeek("欢迎使用富氧到家，前往登录获取更多服务");
        }

    }

    private void initMap() {
        mapView = (MapView) findViewById(R.id.mapview);

        mBaiduMap = mapView.getMap();
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        mapView.showZoomControls(false);

        // 设置地图定位状态
        mapView.getMap().setMyLocationConfigeration(new MyLocationConfiguration(
                mCurrentMode, true, null));

        // 设置地图缩放级别
        mapView.getMap().animateMapStatus(MapStatusUpdateFactory.zoomTo(16.0f));

        //指南针
        mBaiduMap.getUiSettings().setCompassEnabled(true);

        //设置地图类型
        mBaiduMap.setMapType(mMapType);


        // 指南针位置需要在地图载入完成之后设置
        mBaiduMap.setOnMapLoadedCallback(new BaiduMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                if (!mCompassLocSet) {
                    mCompassLocSet = true;
                    //x - 横向
                    mBaiduMap.getUiSettings().setCompassPosition(new Point(100, 260));
                }

                if (!mFirstLocation) {
                    //第一次定位
                    mFirstLocation = true;
                    Intent intent = new Intent();
                    intent.setAction(Constants.INTENT_ACTION_SERVICE_LOCATION_START);
                    sendBroadcast(intent);
                }
            }
        });

        //设置marker点击显示窗口
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
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
                        mBaiduMap.hideInfoWindow();
                    }
                });
                mInfoWindow = new InfoWindow(tv, latLng, -47);
                mBaiduMap.showInfoWindow(mInfoWindow);

                return false;
            }
        });

        // 初始化搜索模块，注册事件监听
        mSearch = RoutePlanSearch.newInstance();
        mSearch.setOnGetRoutePlanResultListener(this);
    }

    private void initView() {
        //地图视图状态切换
        mImgBtnMapSwitch = (ImageButton) findViewById(R.id.ib_main_quick_option_map_switch);
        mImgBtnMapSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (mMapType) {
                    case BaiduMap.MAP_TYPE_NORMAL:
                        mMapType = BaiduMap.MAP_TYPE_SATELLITE;
                        mBaiduMap.setMapType(mMapType);
                        mImgBtnMapSwitch.setImageResource(R.drawable.v1_switch_2);
                        break;

                    default:
                        mMapType = BaiduMap.MAP_TYPE_NORMAL;
                        mBaiduMap.setMapType(mMapType);
                        mImgBtnMapSwitch.setImageResource(R.drawable.v1_switch_1);
                        break;
                }
            }
        });

        //定位状态切换
        mImgBtnMapLocSwitch = (ImageButton) findViewById(R.id.ib_main_quick_option_loc_switch);
        mImgBtnMapLocSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (mCurrentMode) {
                    case NORMAL:
                        mCurrentMode = LocationMode.FOLLOWING;
                        mImgBtnMapLocSwitch.setImageResource(R.drawable.location_1);
                        break;

                    case FOLLOWING:
                        mCurrentMode = LocationMode.COMPASS;
                        mImgBtnMapLocSwitch.setImageResource(R.drawable.location_2);

                        break;
                    case COMPASS:
                        mCurrentMode = LocationMode.NORMAL;
                        mImgBtnMapLocSwitch.setImageResource(R.drawable.location_0);

                        break;
                    default:
                        break;
                }
                mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
                        mCurrentMode, true, null));
                //跟随状态跟普通状态恢复地图俯视角度
                if (mCurrentMode == LocationMode.NORMAL || mCurrentMode == LocationMode.FOLLOWING) {
                    MapStatus ms = new MapStatus.Builder(mBaiduMap.getMapStatus())
                            .overlook(0)
                            .rotate(0)
                            .build();

                    MapStatusUpdate u = MapStatusUpdateFactory.newMapStatus(ms);
                    mBaiduMap.animateMapStatus(u);
                } else {
                    //AppContext.getInstance().setCourierState(Dict.COURIER_GIS_STATE_WAIT);
                    Intent intent = new Intent();
                    //intent.setAction(Constants.INTENT_ACTION_REQUEST_GET_INFO);
                    intent.setAction(Constants.INTENT_ACTION_SERVICE_LOCATION_START);
                    sendBroadcast(intent);
                }
            }
        });

        mImgBtnMapZoomIn = (ImageButton) findViewById(R.id.ib_main_quick_option_zoomin);
        mImgBtnMapZoomIn.setOnClickListener(ZoomCtrlOnClickListener);

        mImgBtnMapZoomOut = (ImageButton) findViewById(R.id.ib_main_quick_option_zoomout);
        mImgBtnMapZoomOut.setOnClickListener(ZoomCtrlOnClickListener);

        mImgBtnEmbrace = (ImageButton) findViewById(R.id.ib_main_quick_option_embrace);
        mImgBtnEmbrace.setOnClickListener(OrderCtrlOnClickListener);

        mImgBtnDispatch = (ImageButton) findViewById(R.id.ib_main_quick_option_go);
        mImgBtnDispatch.setOnClickListener(OrderCtrlOnClickListener);

        mImgBtnNextOrder = (ImageButton) findViewById(R.id.ib_main_quick_option_next);
        mImgBtnNextOrder.setOnClickListener(OrderCtrlOnClickListener);

        mImgBtnPreOrder = (ImageButton) findViewById(R.id.ib_main_quick_option_pre);
        mImgBtnPreOrder.setOnClickListener(OrderCtrlOnClickListener);

        mImgBtnQuickPhone = (ImageButton) findViewById(R.id.ib_main_quick_option_phone);
        mImgBtnQuickPhone.setOnClickListener(OrderCtrlOnClickListener);

        mImgBtnListDetail = (ImageButton) findViewById(R.id.ib_main_quick_option_list_detail);
        mImgBtnListDetail.setOnClickListener(OrderCtrlOnClickListener);

        mImgBtnRouteList = (ImageButton) findViewById(R.id.ib_main_quick_option_route_list);
        mImgBtnRouteList.setOnClickListener(OrderCtrlOnClickListener);


        mTxvShowSubOrderExAddress = (TextView) findViewById(R.id.txv_main_activity_order_address);
//        mTxvShowSubOrderExAddress.setVisibility(View.INVISIBLE);

        mViewShowSubOrderExAddress = findViewById(R.id.layout_main_order_info);
        mViewShowSubOrderExAddress.setVisibility(View.INVISIBLE);


    }

    private void refreshView(boolean show) {
        //TODO 更新当前ui显示
//        mImgBtnEmbrace.setVisibility(show ? View.VISIBLE: View.GONE);
//        mImgBtn

    }

    private void initMarker() {
        if (!AppContext.getInstance().isLogin())
            return;
        List<SubOrderEx> list = AppContext.getInstance().getmSubOrderExs();

    }

    private View.OnClickListener OrderCtrlOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ib_main_quick_option_next:
                    if (AppContext.getInstance().isLogin()) {
                        mSubOrderEx = AppContext.getInstance().getNextOrder();
                        if (mSubOrderEx != null && mSubOrderEx.getId() != null) {
                            showCurrentSubOrder();
                            mapToViewSubOrder(mSubOrderEx);
                            searchRoute(mSubOrderEx);
                        } else {
                            new ToastMessageTask().execute("当前没有配送订单，或者订单已经配送完成");
                        }
                    } else {
                        new ToastMessageTask().execute("请先前往登录");
                    }

//                    SubOrderEx subOrderEx1 = new SubOrderEx(TestUtil.getTestData().get(0));
//                    List<SubOrderEx> lst = new ArrayList<>();
//                    lst.add(subOrderEx1);
//                    DlgPlusOrderCtrlAdapter adapter1 = new DlgPlusOrderCtrlAdapter(MainActivity.this,
//                            lst, OrderDispatchCtrlOnClickListener);
//                    mDlgPlusOrderCtrl = DialogPlus.newDialog(MainActivity.this)
//                            .setContentHolder(new ListHolder())
//                            .setAdapter(adapter1)
//
//                            .setExpanded(true).create();
//                    mDlgPlusOrderCtrl.show();
                    break;
                case R.id.ib_main_quick_option_pre:
                    if (AppContext.getInstance().isLogin()) {
                        mSubOrderEx = AppContext.getInstance().getPreOrder();
                        if (mSubOrderEx != null && mSubOrderEx.getId() != null) {
                            showCurrentSubOrder();
                            mapToViewSubOrder(mSubOrderEx);
                            searchRoute(mSubOrderEx);
                        } else {
                            new ToastMessageTask().execute("当前没有配送订单，或者订单已经配送完成");
                        }
                    } else {
                        new ToastMessageTask().execute("请先前往登录");
                    }
                    break;
                case R.id.ib_main_quick_option_go:
                    //start thread
                    if (AppContext.getInstance().isLogin()) {
                        mSubOrderEx = AppContext.getInstance().getCurrentSubOrder();
                        if (mSubOrderEx != null && mSubOrderEx.getLng() != null && mSubOrderEx.getLat() != null) {
                            if (routeOverlay != null)
                                routeOverlay.removeFromMap();
                            LatLng en = new LatLng(mSubOrderEx.getLat(), mSubOrderEx.getLng());
                            LatLng st = AppContext.getInstance().getmLocation();
                            searchRoute(st, en);
                            new ToastMessageTask().execute("开始搜索路线，请稍后");
                        }
                    }
                    break;
                case R.id.ib_main_quick_option_phone:
                    //获取当前配送订单
                    if (AppContext.getInstance().isLogin()) {
                        SubOrderEx subOrderEx = AppContext.getInstance().getCurrentSubOrder();
                        if (subOrderEx != null && subOrderEx.getReceiverPhone() != null) {
                            UIHelper.simpleCall(MainActivity.this, subOrderEx.getReceiverPhone());
                        } else {
                            new ToastMessageTask().execute("当前没有配送订单，或者订单已经配送完成");
                        }
                    } else {
                        new ToastMessageTask().execute("请先前往登录");
                    }
                    break;
                case R.id.ib_main_quick_option_embrace:
                    //TODO 向仓储移动
                    if (AppContext.getInstance().isLogin()) {
                        MerchantLocation merchantLocation = AppContext.getInstance().getmMerchantLocation();
                        if (merchantLocation != null && merchantLocation.getLat() != null
                                && merchantLocation.getLng() != null) {
                            mapToViewLocation(merchantLocation.getLat(),
                                    merchantLocation.getLng());
                            //画路径
                            if (routeOverlay != null)
                                routeOverlay.removeFromMap();
                            LatLng en = new LatLng(merchantLocation.getLat(), merchantLocation.getLng());
                            LatLng st = AppContext.getInstance().getmLocation();
                            searchRoute(st, en);
                            new ToastMessageTask().execute("开始搜索路线，请稍后");
                        }
                    } else {
                        new ToastMessageTask().execute("请先前往登录");
                    }
                    break;
                case R.id.ib_main_quick_option_list_detail:
                    if (AppContext.getInstance().isLogin()) {
                        showOrderListForm(MainActivity.this, AppContext.getInstance().getmSubOrderExs(),
                                AppContext.getInstance().getmCusor());
                    } else {
                        new ToastMessageTask().execute("请先前往登录");
                    }
                    break;
                case R.id.ib_main_quick_option_route_list:
                    if (AppContext.getInstance().isLogin()) {
                        if (route != null && route.getAllStep() != null && route.getAllStep().size() != 0) {
                            showRouteNodeForm(MainActivity.this,
                                    RouteNodeUtil.getRouteNodes(route));
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 显示当前配送订单（未完成的）列表
     *
     * @param context
     * @param data
     */
    public void showOrderListForm(Context context, List<SubOrderEx> data, Integer iCursor) {
        List<SubOrderEx> list = OrderUtil.getUnfinishedSubOrderExs(data, iCursor);
        mDlgPlusListAdapter = new DlgPlusListAdapter(context, list);
        mDlgPlusOrderList = DialogPlus.newDialog(MainActivity.this)
                .setHeader(R.layout.head_dlg_order)
                .setContentHolder(new ListHolder())
                .setAdapter(mDlgPlusListAdapter)
                .setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
                        SubOrderEx subOrderEx = (SubOrderEx) item;
//                        new ToastMessageTask().execute(position + " " + ((SubOrderEx)item).getId() );
                        mapToViewSubOrder(subOrderEx);
                    }
                })
                .setExpanded(true).create();
        mDlgPlusOrderList.show();
    }

    /**
     * 显示配送到达之后的操作窗体
     *
     * @param context
     * @param subOrderEx
     * @param clickHandler
     */
    public void showOrderCtrlForm(Context context, SubOrderEx subOrderEx, View.OnClickListener clickHandler) {
        List<SubOrderEx> list = new ArrayList<>();
//        SubOrderEx subOrderEx = new SubOrderEx(subOrder);
        list.add(subOrderEx);
        mDlgPlusOrderCtrlAdapter = new DlgPlusOrderCtrlAdapter(MainActivity.this,
                list, clickHandler);
        mDlgPlusOrderCtrl = DialogPlus.newDialog(context)
                .setHeader(R.layout.head_dlg_order_ctrl)
                .setContentHolder(new ListHolder())
                .setAdapter(mDlgPlusOrderCtrlAdapter)
                .setExpanded(true).create();
        mDlgPlusOrderCtrl.show();
    }

    /**
     * 显示路线详情窗体
     *
     * @param context
     * @param mapRouteNodes
     */
    public void showRouteNodeForm(Context context, List<MapRouteNode> mapRouteNodes) {
        mDlgPlusRouteListAdapter = new DlgPlusRouteAdapter(context,
                mapRouteNodes);
        mDlgPlusRouteList = DialogPlus.newDialog(context)
                .setHeader(R.layout.head_route_detail)
                .setContentHolder(new ListHolder())
                .setAdapter(mDlgPlusRouteListAdapter)
                .setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
                        MapRouteNode mapRouteNode = (MapRouteNode) item;
//                        new ToastMessageTask().execute(position + " " + ((SubOrderEx)item).getId() );
                        mapToView(mapRouteNode.getLocation());
                    }
                })
                .setExpanded(true).create();
        mDlgPlusRouteList.show();
    }

    /**
     * 订单配送完成操作回调函数
     */
    private View.OnClickListener OrderDispatchCtrlOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.txv_item_dlg_order_ctrl_dispatch_personal:
                    //new ToastMessageTask().execute("personal clicked");
                    if (AppContext.getInstance().isLogin()) {
                        mSubOrderEx.setState(Dict.SUBORDER_STATE_DISPATCHED_PERSONAL);
                        if (mSubOrderEx != null && mSubOrderEx.getId() != null) {
                            XiaoGeApi.finishOrder(AppConfig.app_mac, mSubOrderEx, mHandler);
                        }
                    }
//                    mDlgPlusOrderCtrl.dismiss();
                    break;
                case R.id.txv_item_dlg_order_ctrl_dispatch_property:

                    if (AppContext.getInstance().isLogin()) {
                        mSubOrderEx.setState(Dict.SUBORDER_STATE_DISPATCHED_PROPERTY);
                        if (mSubOrderEx != null && mSubOrderEx.getId() != null) {
                            XiaoGeApi.finishOrder(AppConfig.app_mac, mSubOrderEx, mHandler);
                        }
                    }
                    break;
                case R.id.txv_item_dlg_order_ctrl_dispatch_phone_no_response:
                    break;
                case R.id.txv_item_dlg_order_ctrl_dispatch_error_embrace:
                    break;
                case R.id.imb_item_dlg_order_ctrl_phone:
                    if (AppContext.getInstance().isLogin()) {
                        if (mSubOrderEx != null && mSubOrderEx.getReceiverPhone() != null) {
                            UIHelper.simpleCall(MainActivity.this, mSubOrderEx.getReceiverPhone());
                        } else {
                            new ToastMessageTask().execute("当前没有配送订单，或者订单已经配送完成");
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 地图放大缩小操作
     */
    private View.OnClickListener ZoomCtrlOnClickListener =
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int id = v.getId();
                    float zoomLevel = mBaiduMap.getMapStatus().zoom;
                    MapStatusUpdate u;
                    switch (id) {
                        case R.id.ib_main_quick_option_zoomin:
                            u = MapStatusUpdateFactory.zoomTo(zoomLevel + 1);
                            mBaiduMap.animateMapStatus(u);
                            break;
                        case R.id.ib_main_quick_option_zoomout:
                            u = MapStatusUpdateFactory.zoomTo(zoomLevel - 1);
                            mBaiduMap.animateMapStatus(u);
                            break;
                        default:
                            break;
                    }
                }
            };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }


//    @Override
//    protected void onStop() {
//        mapView.onStop();
//        super.onStop();
//        //TODO activity 进入后台后关闭广播接收
//        //unregisterReceiver(mReceiver);
//        //stopLocation();
//        //UIHelper.testService(this);
//    }

    @Override
    protected void onResume() {
        //UIHelper.testStopService(this);
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * 显示覆盖界面，显示当前配送的订单信息，并提供相应的操作
     */
    private void showQuickOption() {

        Toast.makeText(AppContext.getInstance(), "quickoption clicked ", Toast.LENGTH_LONG).show();
//        final QuickOptionDialog dialog = new QuickOptionDialog(
//                MainActivity.this);
//        dialog.setCancelable(true);
//        dialog.setCanceledOnTouchOutside(true);
//        dialog.show();
    }

    /**
     * 监听返回--是否退出程序
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            long secondTime = System.currentTimeMillis();
            if (secondTime - firstTime > 2000) {//如果两次按键时间间隔大于2000毫秒，则不退出
                Toast.makeText(MainActivity.this, "再按一次退出程序",
                        Toast.LENGTH_SHORT).show();
                firstTime = secondTime;//更新firstTime
                return true;
            } else {
                // android.os.Process.killProcess(android.os.Process.myPid());
                UIHelper.notifyClearAll();
                UIHelper.stopLocationBDService(this);
                UIHelper.stopBackgroundService(this);
                System.exit(0);//否则退出程序
            }
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    //
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.mnu_item_main_login) {
            if (AppContext.getInstance().isLogin()) {
                UIHelper.showUserInfoActivity(this);
            } else {
                UIHelper.showLoginActivity(this);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_menu_account) {
            if (AppContext.getInstance().isLogin())
                UIHelper.showAccountActivity(this);
            else {
                new ToastMessageTask().execute(getString(R.string.toast_no_login_tips));
                UIHelper.showLoginActivity(this);
            }

        } else if (id == R.id.nav_menu_userinfo) {
            if (AppContext.getInstance().isLogin())
                UIHelper.showUserInfoActivity(this);
            else {
                new ToastMessageTask().execute(getString(R.string.toast_no_login_tips));
                UIHelper.showLoginActivity(this);
            }
        } else if (id == R.id.nav_send) {
            UIHelper.showLoginActivity(this);
        } else if (id == R.id.nav_menu_getorder) {
            if (AppContext.getInstance().isLogin())
                UIHelper.showOrderActivity(this);
            else {
                new ToastMessageTask().execute(getString(R.string.toast_no_login_tips));
                UIHelper.showLoginActivity(this);
            }
        } else if (id == R.id.nav_menu_order) {
            if (AppContext.getInstance().isLogin())
                UIHelper.showMyOrderActivity(this, Dict.MAINORDER_STATE_DISPATCHING);
            else {
                new ToastMessageTask().execute(getString(R.string.toast_no_login_tips));
                UIHelper.showLoginActivity(this);
            }
        } else if (id == R.id.nav_menu_embrace_order) {
            if (AppContext.getInstance().isLogin()) {
                UIHelper.showMyOrderActivity(this, Dict.MAINORDER_STATE_CATCHED);
            } else {
                new ToastMessageTask().execute(getString(R.string.toast_no_login_tips));
                UIHelper.showLoginActivity(this);
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

//    //enable为true时，菜单添加图标有效，enable为false时无效。4.0系统默认无效
//    private void setIconEnable(Menu menu, boolean enable)
//    {
//        try
//        {
//            Class<?> clazz = Class.forName("com.android.internal.view.menu.MenuBuilder");
//            Method m = clazz.getDeclaredMethod("setOptionalIconsVisible", boolean.class);
//            m.setAccessible(true);
//
//            //MenuBuilder实现Menu接口，创建菜单时，传进来的menu其实就是MenuBuilder对象(java的多态特征)
//            m.invoke(menu, enable);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    //方向定位的次数，每200次取一次方向
    private Integer rotateCount = 0;
    private Integer rotateMax = 200;

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            accValues = event.values.clone();//这里是对象，需要克隆一份，否则共用一份数据
        } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            magValues = event.values.clone();//这里是对象，需要克隆一份，否则共用一份数据
        }
        /**public static boolean getRotationMatrix (float[] R, float[] I, float[] gravity, float[] geomagnetic)
         * 填充旋转数组r
         * r：要填充的旋转数组
         * I:将磁场数据转换进实际的重力坐标中 一般默认情况下可以设置为null
         * gravity:加速度传感器数据
         * geomagnetic：地磁传感器数据
         */
        SensorManager.getRotationMatrix(r, null, accValues, magValues);
        /**
         * public static float[] getOrientation (float[] R, float[] values)
         * R：旋转数组
         * values ：模拟方向传感器的数据
         */

        SensorManager.getOrientation(r, values);


//        //将弧度转化为角度后输出
        //增加间隔，地图会自动刷新，不然会导致地图卡住
        if (rotateCount++ >= rotateMax) {

            rotateCount = 0;
            mDirection = (float) (Math.toDegrees(values[0]));
            if (mCurrentMode == LocationMode.NORMAL || mCurrentMode == LocationMode.FOLLOWING)
                return;

            MapStatus ms = new MapStatus.Builder(mBaiduMap.getMapStatus()).rotate(mDirection).build();
            MapStatusUpdate u = MapStatusUpdateFactory.newMapStatus(ms);
            mBaiduMap.animateMapStatus(u, 100);

        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void mapToViewSubOrder(SubOrderEx subOrderEx) {
        if (subOrderEx != null && subOrderEx.getLat() != null && subOrderEx.getLng() != null)
            mapToViewLocation(subOrderEx.getLat(), subOrderEx.getLng());
    }

    private void mapToViewLocation(Double lat, Double lng) {
        LatLng latLng = CoordinateUtil.GCJ02_TO_BD09(lat, lng);
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(latLng);
        mBaiduMap.animateMapStatus(u);
    }

    private void mapToView(LatLng latLng) {
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(latLng);
        mBaiduMap.animateMapStatus(u);
    }

    private void showCurrentSubOrder() {
        if (mSubOrderEx != null && mSubOrderEx.getReceiverAddress() != null && mSubOrderEx.getReceiverPhone() != null
                && mSubOrderEx.getReceiverName() != null) {
            mViewShowSubOrderExAddress.setVisibility(View.VISIBLE);
            mTxvShowSubOrderExAddress.setText("当前配送地址：" + mSubOrderEx.getReceiverAddress());
        } else {
            mViewShowSubOrderExAddress.setVisibility(View.INVISIBLE);
        }
    }

    //订单完成回调
    private JsonHttpResponseHandler mHandler = new JsonHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            TLog.log("Main:finishOrder:", response.toString());
            try {
                if (response.getString(BaseParameterNames.JSON_RESPONSE_STATE).equals("true")) {
                    //TODO 1 send broadcast to background service to refresh suborder
                    AppContext.getInstance().finishSubOrder(mSubOrderEx);
                    //TODO 2 go next order, if null change courier state to rest
                    ((Marker)mMarkerList.get(mSubOrderEx.getId())).remove();

                    mMarkerList.remove(mSubOrderEx.getId());
                    if(routeOverlay!= null)
                        routeOverlay.removeFromMap();
                    mSubOrderEx = AppContext.getInstance().getNextOrder();
                    if(mSubOrderEx == null ||  mSubOrderEx.getId() == null){
                        if(mMarkerStorage != null)
                            mMarkerStorage.remove();
                        mMarkerStorage = null;
                        mBaiduMap.clear();
                    }
                    showCurrentSubOrder();
                    mDlgPlusOrderCtrl.dismiss();

                    mapToViewSubOrder(mSubOrderEx);

                    searchRoute(mSubOrderEx);

                } else {
                    //TODO 此时会出现什么错误？
                    new ToastMessageTask().execute("当前订单状态为不可完成状态，请检查后重试");
                    mSubOrderEx = AppContext.getInstance().getCurrentSubOrder();
                }
            } catch (JSONException e) {
                TLog.log("main:finishOrder:jsonerror:", e.toString());
                new ToastMessageTask().execute("解析报文出错，请检查后重试");
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            new ToastMessageTask().execute("网络通信错误，请检查网络后重试");
        }
    };

    @Override
    public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {

    }

    @Override
    public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {

    }

    @Override
    public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {
        if (drivingRouteResult == null || drivingRouteResult.error != SearchResult.ERRORNO.NO_ERROR) {
            new ToastMessageTask().execute("抱歉，未找到结果");
            return;
        }
        if (drivingRouteResult.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            new ToastMessageTask().execute("搜索路径错误，起始点或者终点不在可搜索范围内");
            // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
            // result.getSuggestAddrInfo()
            return;
        }
        TLog.log("GetDrivingRoute:", drivingRouteResult.toString());
        if (drivingRouteResult.error == SearchResult.ERRORNO.NO_ERROR) {
            new ToastMessageTask().execute("路径搜索成功");
            TLog.log("GetDrivingRoute:RouteLines.size:", drivingRouteResult.getRouteLines().size() + "");
            nodeIndex = -1;
            route = drivingRouteResult.getRouteLines().get(0);
//            new ToastMessageTask().execute("距离："+route.getDistance());
            DrivingRouteOverlay overlay = new MyDrivingRouteOverlay(mBaiduMap);
            routeOverlay = overlay;
            mBaiduMap.setOnMarkerClickListener(overlay);
            overlay.setData(drivingRouteResult.getRouteLines().get(0));
            overlay.addToMap();
            overlay.zoomToSpan();
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {
        mBaiduMap.hideInfoWindow();
    }

    @Override
    public boolean onMapPoiClick(MapPoi mapPoi) {
        return false;
    }

    // 定制RouteOverly
    private class MyDrivingRouteOverlay extends DrivingRouteOverlay {

        public MyDrivingRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
//            if (useDefaultIcon) {
//                return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
//            }
            return BitmapDescriptorFactory.fromResource(R.drawable.ic_1_1_temp);
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
//            if (useDefaultIcon) {
//                return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
//            }
            return BitmapDescriptorFactory.fromResource(R.drawable.ic_1_1_temp);
//            return null;
        }
    }

    private void searchRoute(LatLng st, LatLng en) {
        //传入参数为gcj02 坐标
        PlanNode stNode = PlanNode.withLocation(CoordinateUtil.GCJ02_TO_BD09(st));
        PlanNode enNode = PlanNode.withLocation(CoordinateUtil.GCJ02_TO_BD09(en));

        if (AppContext.getInstance().isLogin()) {
            mSearch.drivingSearch((new DrivingRoutePlanOption())
                    .from(stNode).to(enNode));
        }
    }

    private void searchRoute(SubOrderEx subOrderEx) {
        //画路径
        if (routeOverlay != null)
            routeOverlay.removeFromMap();
        if (subOrderEx != null && subOrderEx.getLat() != null && subOrderEx.getLng() != null) {
            LatLng en = new LatLng(subOrderEx.getLat(), subOrderEx.getLng());
            LatLng st = AppContext.getInstance().getmLocation();
            searchRoute(st, en);
        }
    }


//    private List<SubOrder> getTestData() {
//        List<SubOrder> list = new ArrayList<>();
//        for (int i = 0; i != 10; ++i) {
//            SubOrder subOrder = new SubOrder();
//            subOrder.setId("1234567890123456780" + i);
//            subOrder.setState(0);
//            subOrder.setReceiverName("李四");
//            subOrder.setReceiverAddress("中山西路292号");
//            subOrder.setCommodityId(1);
//            subOrder.setSubIndex(i);
//            list.add(subOrder);
//        }
//        return list;
//    }
}

