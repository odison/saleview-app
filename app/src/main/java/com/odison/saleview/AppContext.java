package com.odison.saleview;

import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

//import com.loopj.android.http.AsyncHttpClient;
//import com.loopj.android.http.PersistentCookieStore;

//import net.oschina.app.api.ApiHttpClient;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.odison.saleview.api.ApiHttpClient;
import com.odison.saleview.api.remote.XiaoGeApi;
import com.odison.saleview.base.BaseParameterNames;
import com.odison.saleview.base.Config;
import com.odison.saleview.base.Constants;
import com.odison.saleview.base.Dict;
import com.odison.saleview.bean.Account;
import com.odison.saleview.bean.Courier;
import com.odison.saleview.bean.MainOrder;
import com.odison.saleview.bean.MerchantLocation;
import com.odison.saleview.bean.SubOrder;
import com.odison.saleview.bean.SubOrderEx;
import com.odison.saleview.bean.User;
import com.odison.saleview.util.CyptoUtils;
import com.odison.saleview.util.OSUtil;
import com.odison.saleview.util.OrderUtil;
import com.odison.saleview.util.StringUtils;
import com.odison.saleview.util.TLog;
import com.odison.saleview.util.ToastMessageTask;
import com.odison.saleview.util.UIHelper;
//import com.tencent.mapsdk.raster.model.LatLng;
//import net.oschina.app.cache.DataCleanManager;
//import net.oschina.app.util.MethodsCompat;
//import net.oschina.app.util.TLog;
//import net.oschina.app.util.UIHelper;

//import org.kymjs.kjframe.KJBitmap;
//import org.kymjs.kjframe.bitmap.BitmapConfig;
//import org.kymjs.kjframe.utils.KJLoger;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import cz.msebera.android.httpclient.Header;


/**
 * 全局应用程序类：用于保存和调用全局应用配置及访问网络数据
 *
 * @author odison
 * @version 1.0
 * @created
 */
public class AppContext extends Application {


    private static AppContext instance;

    private int loginUid;

    public NotificationManager getmNotificationManager() {
        return mNotificationManager;
    }

    private SpeechSynthesizer mTTS;

    private NotificationManager mNotificationManager;


    //我的主订单
    private MainOrder mMainOrder;
    //我的子订单
    private List<SubOrder> mSubOrders;


    private MerchantLocation mMerchantLocation;

    //
    private List<SubOrderEx> mSubOrderExs;


    //保存SubOrders's LatLng
    private Map<String, LatLng> mMapSubOrderLatLngs;


    //
    private Integer mCusor;

    private Integer mAppState;

    public boolean isSound() {
        return sound;
    }

    public void setSound(boolean sound) {
        this.sound = sound;
    }

    //语音协助开关
    private boolean sound;

    //保存账号登录状态
    private boolean login;
    //保存 gcj02 坐标的 位置信息
    private LatLng mLocation;
    //向服务器上传位置信息开关
    private boolean locationUploadStart;
    //快递员状态
    private Integer courierState;


    @Override
    public void onCreate() {
        super.onCreate();
        String processName = OSUtil.getProcessName(this, android.os.Process.myPid());
        instance = this;
        //每个进程初始化都会对application oncreate
        //baidu定位需要用到 location.f remote进程，这里做个过滤
        //这样处理不知道对不对
        if (processName != null && processName.equals(AppConfig.APP_NAME)) {
            SDKInitializer.initialize(this);

            init();
            TLog.log("haha");

//            mMainOrder = new MainOrder();
//            //
//            mSubOrders = new ArrayList<>();
//            //
//            mSubOrderExs = new ArrayList<>();
//            //
//            mMerchantLocation = new MerchantLocation();
//            //
//            mMapSubOrderLatLngs = new HashMap<>();
//            //
//            mCusor = 0;
//            //
//            sound = false;
//            //
//            mAppState = Dict.APP_STATE_UNLOGIN;
//            //此时启动服务
//            UIHelper.startBackgroundService(this);
//            //
//            UIHelper.startLocationBDService(this);
//            //UIHelper.startLocationService(this);
//            //刚开启时默认不上传位置信息
//            locationUploadStart = false;
//            //初始化为休息状态
//            courierState = Dict.COURIER_GIS_STATE_REST;
//            //
//            mLocation = new LatLng(0, 0);
//            //new ToastMessageTask().execute("UUID:" + getUUID());
//            initLogin();
//
//            mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//            SpeechUtility.createUtility(this, Config.IFLYTEK_INIT);
//            initTTS();
        }

    }

    /**
     * 初始化httpclient
     */
    private void init() {
        AsyncHttpClient client = new AsyncHttpClient();
        PersistentCookieStore myCookieStore = new PersistentCookieStore(this);
        client.setCookieStore(myCookieStore);
        ApiHttpClient.setHttpClient(client);
        ApiHttpClient.setCookie(ApiHttpClient.getCookie(this));
        //KJLoger.openDebutLog(true);
        TLog.DEBUG = BuildConfig.DEBUG;
    }


    private void initTTS() {
        mTTS = SpeechSynthesizer.createSynthesizer(this, new InitListener() {
            @Override
            public void onInit(int i) {
                TLog.log("AppContext:initTTs:", "" + i);

            }
        });
        //2.合成参数设置，详见《 MSC Reference Manual》 SpeechSynthesizer 类
        //设置发音人（更多在线发音人，用户可参见 附录12.2
        mTTS.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
        // 设置本地合成发音人 voicer为空，默认通过语记界面指定发音人。
        mTTS.setParameter(SpeechConstant.VOICE_NAME, "");

    }

    /**
     * 调用方法：AppContext.getInstance().simpleSpeek(str)
     */
    public void simpleSpeek(String str) {
        if (sound)
            mTTS.startSpeaking(str, null);
    }

    private void initLogin() {
        AppConfig.app_mac = getUUID();
        if (isNetworkAvailable()) {
            User user = getLocalUser();

            TLog.log(user.toString());
//        Log.d("init",user.toString());
            //set mac

            if (user != null && user.getPhone() != null && user.getPhone().length() != 0) {
//            new ToastMessageTask().execute("login called");
                TLog.log("find local user info,auto login...");
                XiaoGeApi.login(user.getPhone(), user.getPassword(), AppConfig.app_mac, mJsonHandler);
            } else {
                TLog.log("can not find local user info");
                this.cleanLoginInfo();
            }
        } else {
            new ToastMessageTask().execute("当前没有网络,请打开网络后重试");
        }
    }

    /*
 * Load file content to String
 */
    public static String loadFileAsString(String filePath) throws java.io.IOException {
        StringBuffer fileData = new StringBuffer(1000);
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        char[] buf = new char[1024];
        int numRead = 0;
        while ((numRead = reader.read(buf)) != -1) {
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
        }
        reader.close();
        return fileData.toString();
    }

    /*
     * Get the STB MacAddress
     */
    public String getMacAddress() {
        try {
            return loadFileAsString("/sys/class/net/eth0/address")
                    .toUpperCase().substring(0, 17);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean isNetworkAvailable() {
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) instance.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        } else {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();
            if (networkInfo != null && networkInfo.length > 0) {
                for (int i = 0; i < networkInfo.length; i++) {
                    // System.out.println(i + "===状态===" + networkInfo[i].getState());
                    //System.out.println(i + "===类型===" + networkInfo[i].getTypeName());
                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 获得当前app运行的AppContext
     *
     * @return
     */
    public static AppContext getInstance() {
        return instance;
    }


    public boolean containsProperty(String key) {
        Properties props = getProperties();
        return props.containsKey(key);
    }

    public void setProperties(Properties ps) {
        AppConfig.getAppConfig(this).set(ps);
    }

    public Properties getProperties() {
        return AppConfig.getAppConfig(this).get();
    }

    public void setProperty(String key, String value) {
        AppConfig.getAppConfig(this).set(key, value);
    }

    /**
     * 获取cookie时传AppConfig.CONF_COOKIE
     *
     * @param key
     * @return
     */
    public String getProperty(String key) {
        return AppConfig.getAppConfig(this).get(key);
    }

    public void removeProperty(String... key) {
        AppConfig.getAppConfig(this).remove(key);
    }

    /**
     * 获取App唯一标识
     *
     * @return
     */
    public String getAppId() {
        String uniqueID = getProperty(AppConfig.CONF_APP_UNIQUEID);
        if (StringUtils.isEmpty(uniqueID)) {
            uniqueID = UUID.randomUUID().toString();
            setProperty(AppConfig.CONF_APP_UNIQUEID, uniqueID);
        }
        return uniqueID;
    }

    /**
     * 获取App安装包信息
     *
     * @return
     */
    public PackageInfo getPackageInfo() {
        PackageInfo info = null;
        try {
            info = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace(System.err);
        }
        if (info == null)
            info = new PackageInfo();
        return info;
    }

    /**
     * 保存登录信息
     *
     * @param courier 用户信息
     */
    @SuppressWarnings("serial")
    public void saveCourierInfo(final Courier courier) {
        this.loginUid = courier.getId();
        this.login = true;
        setProperties(new Properties() {
            {
                setProperty("courier.uid", String.valueOf(courier.getId()));
                setProperty("courier.name", courier.getName());
                setProperty("courier.phone", courier.getPhone());
                setProperty("courier.password",
                        CyptoUtils.encode("fuyangApp", courier.getPassword()));
                setProperty("courier.address", courier.getAddress());
                setProperty("courier.signtime", StringUtils.getDateString(courier.getSignTime()));
                TLog.log(getProperty("courier.signtime"));
                setProperty("courier.score", String.valueOf(courier.getScore()));
                setProperty("courier.state", String.valueOf(courier.getState()));
                setProperty("courier.updtime", StringUtils.getDateString(courier.getUpdateTime()));
                TLog.log("updtime" + getProperty("courier.updtime"));

            }
        });
    }

    /**
     * @param user
     */
    public void saveUserInfo(final User user) {
        setProperties(new Properties() {
            {
                setProperty("user.phone", user.getPhone());
                setProperty("user.password", CyptoUtils.encode("fuyangApp", user.getPassword()));
            }
        });
    }

    public void saveAccountInfo(final Account account) {
        setProperties(new Properties() {
            {
                setProperty("account.id", String.valueOf(account.getId()));
                setProperty("account.usertype", String.valueOf(account.getUserType()));
                setProperty("account.balance", String.valueOf(account.getBalance()));
            }
        });
    }

    public void updatePassword(final String password) {
        setProperties(new Properties() {
            {
                setProperty("user.password", CyptoUtils.encode("fuyangApp", password));
                setProperty("courier.password", CyptoUtils.encode("fuyangApp", password));
            }
        });
    }


    /**
     * 更新用户信息
     *
     * @param user
     */
    @SuppressWarnings("serial")
    public void updateUserInfo(final Courier user) {
//        setProperties(new Properties() {
//            {
//                setProperty("user.name", user.getName());
//            }
//        });
    }

    /**
     * 获得登录用户的信息
     *
     * @return
     */
    public Courier getLoginUser() {
        Courier user = new Courier();
        user.setPhone("13511442500");
        user.setName("杨");
        user.setPassword("111111");
//        user.setName(getProperty("user.name"));
//        user.setPhone(getProperty("user.phone"));
//        user.setPassword(CyptoUtils.decode("fuyangApp",getProperty("user.password")));
        return user;
    }

    public Courier getLocalCourier() {
        Courier courier = new Courier();

        courier.setId(StringUtils.toInt(getProperty("courier.uid")));
        courier.setName(getProperty("courier.name"));
        courier.setPhone(getProperty("courier.phone"));
        courier.setAddress(getProperty("courier.address"));
        courier.setPassword(CyptoUtils.decode("fuyangApp", getProperty("courier.password")));
        courier.setSignTime(StringUtils.toDate(getProperty("courier.signtime")));
        courier.setState(StringUtils.toInt(getProperty("courier.state")));
        courier.setUpdateTime(StringUtils.toDate(getProperty("courier.updtime")));

        return courier;
    }


    public User getLocalUser() {
        User user = new User();
        user.setPhone(getProperty("user.phone"));
        user.setPassword(CyptoUtils.decode("fuyangApp", getProperty("user.password")));
        return user;
    }

    public Account getLocalAccount() {
        Account account = new Account();
        account.setId(StringUtils.toInt(getProperty("account.id")));
        account.setUserType(StringUtils.toInt(getProperty("account.usertype")));
        account.setBalance(StringUtils.toBigDecimal(getProperty("account.balance")));

        return account;
    }

    /**
     * 清除登录信息
     */
    public void cleanLoginInfo() {
        this.loginUid = 0;
        this.login = false;
        removeProperty("courier.uid", "courier.name", "courier.phone", "courier.password",
                "courier.address", "courier.signTime", "courier.score",
                "courier.state", "courier.updTime",
                "user.phone", "user.password");
    }

    public void cleanOrderInfo() {
        mCusor = 0;
        mMerchantLocation = new MerchantLocation();
        mMainOrder = new MainOrder();
        mSubOrders = new ArrayList<>();
        mSubOrderExs = new ArrayList<>();
    }

    public int getLoginUid() {
        return loginUid;
    }

    public boolean isLogin() {
        return login;
    }

    /**
     * 用户注销
     */
    public void Logout() {
        cleanLoginInfo();
        // ApiHttpClient.cleanCookie();
        // this.cleanCookie();
        this.login = false;
        this.loginUid = 0;

        this.mMapSubOrderLatLngs.clear();
        this.mSubOrderExs.clear();
        this.mMainOrder = null;
        this.mCusor = 0;
        this.mSubOrders.clear();
        //TODO 发送广播退出
        Intent intent = new Intent(Constants.INTENT_ACTION_LOGOUT);
        sendBroadcast(intent);
    }

    /**
     * 清除保存的缓存
     */
//    public void cleanCookie() {
//        removeProperty(AppConfig.CONF_COOKIE);
//    }

    /**
     * 清除app缓存
     */
    public void clearAppCache() {
//        DataCleanManager.cleanDatabases(this);
//        // 清除数据缓存
//        DataCleanManager.cleanInternalCache(this);
//        // 2.2版本才有将应用缓存转移到sd卡的功能
//        if (isMethodsCompat(android.os.Build.VERSION_CODES.FROYO)) {
//            DataCleanManager.cleanCustomCache(MethodsCompat
//                    .getExternalCacheDir(this));
//        }
//        // 清除编辑器保存的临时内容
//        Properties props = getProperties();
//        for (Object key : props.keySet()) {
//            String _key = key.toString();
//            if (_key.startsWith("temp"))
//                removeProperty(_key);
//        }
//        new KJBitmap().cleanCache();
    }

    public static void setLoadImage(boolean flag) {
        // set(KEY_LOAD_IMAGE, flag);
    }

    /**
     * 判断当前版本是否兼容目标版本的方法
     *
     * @param VersionCode
     * @return
     */
    public static boolean isMethodsCompat(int VersionCode) {
        int currentVersion = android.os.Build.VERSION.SDK_INT;
        return currentVersion >= VersionCode;
    }


    //登录回调
    private final JsonHttpResponseHandler mJsonHandler = new JsonHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            super.onSuccess(statusCode, headers, response);
            //new ToastMessageTask().execute(response.toString());
            TLog.log("login:", response.toString());
            try {
                if (response.getString(BaseParameterNames.JSON_RESPONSE_STATE).equals("true")) {

                    mAppState = Dict.APP_STATE_LOGIN;
                    login = true;
                    JSONObject obj = response.getJSONObject(BaseParameterNames.JSON_RESPONSE_DATA);
                    AppConfig.app_token = obj.getString(BaseParameterNames.APP_TOKEN);


                    //new ToastMessageTask().execute(AppConfig.app_token);
                    //sendBroadCast to get info
                    Intent intent = new Intent();
                    intent.setAction(Constants.INTENT_ACTION_REQUEST_GET_INFO);
                    sendBroadcast(intent);
                } else {
                    String responseCode = response.getString(BaseParameterNames.JSON_RESPONSE_CODE);
                    String responseMessage = response.getString(BaseParameterNames.JSON_RESPONSE_MESSAGE);
                    TLog.error(responseCode + " " + responseMessage);
                    login = false;
                    mAppState = Dict.APP_STATE_UNLOGIN;
                    cleanLoginInfo();
                    new ToastMessageTask().execute("自动登录失败 " + responseCode);
                }
            } catch (JSONException e) {
                TLog.error(e.toString());
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            new ToastMessageTask().execute("" + R.string.error_login_network);
        }
    };

    public Integer getCourierState() {
        return courierState;
    }

    public void setCourierState(Integer courierState) {
        this.courierState = courierState;
    }

    public Integer getmCusor() {
        return mCusor;
    }

    public void setmCusor(Integer mCusor) {
        this.mCusor = mCusor;
    }


    public MerchantLocation getmMerchantLocation() {
        return mMerchantLocation;
    }

    public void setmMerchantLocation(MerchantLocation mMerchantLocation) {
        this.mMerchantLocation = mMerchantLocation;
    }

    public Map<String, LatLng> getmMapSubOrderLatLngs() {
        return mMapSubOrderLatLngs;
    }

    public void setmMapSubOrderLatLngs(Map<String, LatLng> mMapSubOrderLatLngs) {
        this.mMapSubOrderLatLngs = mMapSubOrderLatLngs;
    }

    public LatLng getmLocation() {
        return mLocation;
    }

    public void setmLocation(LatLng mLocation) {
        this.mLocation = mLocation;
    }

    public void setLogin(boolean login) {
        this.login = login;
    }

    public boolean isLocationUploadStart() {
        return locationUploadStart;
    }

    public void setLocationUploadStart(boolean locationUploadStart) {
        this.locationUploadStart = locationUploadStart;
    }

    public List<SubOrderEx> getmSubOrderExs() {
        return mSubOrderExs;
    }

    public void setmSubOrderExs(List<SubOrderEx> mSubOrderExs) {
        this.mSubOrderExs = mSubOrderExs;
    }

    public MainOrder getmMainOrder() {
        return mMainOrder;
    }

    public void setmMainOrder(MainOrder mMainOrder) {
        this.mMainOrder = mMainOrder;
    }

    public List<SubOrder> getmSubOrders() {
        return mSubOrders;
    }

    public void setmSubOrders(List<SubOrder> mSubOrders) {
        this.mSubOrders = mSubOrders;
    }

    public String getUUID() {
        //for emulation test
//        return "ffffffff-a250-8e80-ffff-ffffe03f45bc";
        final TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
        final String tmDevice, tmSerial, tmPhone, androidId;

        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" + android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
        String uniqueId = deviceUuid.toString();
        //Log.d("debug","uuid="+uniqueId);
        return uniqueId;
    }


    public SubOrderEx getNextOrder() {
        int iCur = mCusor + 1;
        //get not finished suborder
        int iMax = mSubOrderExs.size();
        int iFound = mCusor;
        for (int i = iCur; i != iCur + iMax; ++i) {
            if (mSubOrderExs.get(i % iMax).getState() == Dict.SUBORDER_STATE_CATCHED ||
                    mSubOrderExs.get(i % iMax).getState() == Dict.SUBORDER_STATE_DISPATCHING) {
                iFound = i % iMax;
                break;
            }
        }
//        new ToastMessageTask().execute("iFound:"+iFound);

        if (iFound == mCusor) {
            return null;
        } else {
            mCusor = iFound;
            return mSubOrderExs.get(mCusor);
        }
    }

    public SubOrderEx getPreOrder() {
        int iCur = mCusor - 1;
        int iMax = mSubOrderExs.size();
        int iFound = mCusor;
        for (int i = iCur; i != iCur - iMax; --i) {
            if (mSubOrderExs.get((i + iMax) % iMax).getState() == Dict.SUBORDER_STATE_CATCHED ||
                    mSubOrderExs.get((i + iMax) % iMax).getState() == Dict.SUBORDER_STATE_DISPATCHING) {
                iFound = (i + iMax) % iMax;
                break;
            }
        }
//        new ToastMessageTask().execute("iFound:"+iFound);

        if (iFound == mCusor) {
            return null;
        } else {
            mCusor = iFound;
            return mSubOrderExs.get(mCusor);
        }
    }

    /**
     * 从0 开始顺序获取未完成的订单
     *
     * @return
     */
    public SubOrderEx getFirstOrder() {
        int iCur = 0;
        int iMax = mSubOrderExs.size();
        int iFound = -1;
        for (int i = iCur; i != iMax; ++i) {
            if (mSubOrderExs.get(i).getState() == Dict.SUBORDER_STATE_CATCHED ||
                    mSubOrderExs.get(i).getState() == Dict.SUBORDER_STATE_DISPATCHING) {
                iFound = i;
                break;
            }
        }
//        new ToastMessageTask().execute("iFound:"+iFound);
        if (iFound == -1) {
            return null;
        } else {
            mCusor = iFound;
            return mSubOrderExs.get(mCusor);
        }
    }

    public SubOrderEx getCursorOrder() {
        if (mSubOrderExs.size() > mCusor) {
            return mSubOrderExs.get(mCusor);
        } else
            return null;
    }

    /**
     * 获取当前配送的子订单
     *
     * @return
     */
    public SubOrderEx getCurrentSubOrder() {
        if (mSubOrderExs.size() > mCusor) {
            return mSubOrderExs.get(mCusor);
        } else
            return null;
    }

    public void finishSubOrder(SubOrderEx subOrderEx) {
        int position = OrderUtil.getPosition(mSubOrders, subOrderEx);
        if (position != -1) {
            mSubOrders.get(position).setState(subOrderEx.getState());
        }

        position = OrderUtil.getPositionEx(mSubOrderExs, subOrderEx);
        if (position != -1) {
            mSubOrderExs.get(position).setState(subOrderEx.getState());
        }
    }

    public void refreshCourierState() {
        if (this.mMainOrder != null && this.mMainOrder.getState() != null) {
            if (this.mMainOrder.getState() == Dict.MAINORDER_STATE_CATCHED) {
                this.courierState = Dict.COURIER_GIS_STATE_TAKEN;
            } else {
                if (OrderUtil.haveUnfinished(this.mSubOrderExs)) {
                    this.courierState = Dict.COURIER_GIS_STATE_SENDING;
                } else {
                    this.courierState = Dict.COURIER_GIS_STATE_WAIT;
                }
            }
        } else {
            this.courierState = Dict.COURIER_GIS_STATE_WAIT;
        }

    }

//    public void setEmbrace(){
//        this.mMainOrder.setState(Dict.MAINORDER_STATE_DISPATCHING);
//        for(int i = 0; i!= mSubOrderExs.size();++i){
//
//        }
//    }


}
