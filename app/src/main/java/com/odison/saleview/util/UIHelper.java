package com.odison.saleview.util;

import android.Manifest;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.NotificationCompat;

import com.odison.saleview.AppContext;
import com.odison.saleview.R;
import com.odison.saleview.base.Constants;
import com.odison.saleview.bean.MainOrder;
import com.odison.saleview.bean.MerchantLocation;
import com.odison.saleview.bean.SubOrder;
import com.odison.saleview.service.BackgroundService;
import com.odison.saleview.service.LocationBDService;
import com.odison.saleview.service.LocationService;
import com.odison.saleview.ui.AccountActivity;
import com.odison.saleview.ui.AccountDetailActivity;
import com.odison.saleview.ui.ChangePwdActivity;
import com.odison.saleview.ui.LoginActivity;
import com.odison.saleview.ui.MainActivity;
import com.odison.saleview.ui.MyOrderActivity;
import com.odison.saleview.ui.OrderActivity;
import com.odison.saleview.ui.OrderDetailActivity;
import com.odison.saleview.ui.OrderDetailMapActivity;
import com.odison.saleview.ui.SaleViewActivity;
import com.odison.saleview.ui.SettingsActivity;
import com.odison.saleview.ui.UserInfoActivity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by odison on 2015/12/14.
 * <p/>
 * 界面辅助类，界面跳转都从这里打开
 */
public class UIHelper {
    /**
     * 打开登录界面
     *
     * @param context
     */
    public static void showLoginActivity(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    public static void showSettingActivity(Context context) {
        Intent intent = new Intent(context, SettingsActivity.class);
        context.startActivity(intent);
    }

    public static void showMainActivity(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    public static void showSaleViewActivity(Context context){
        Intent intent = new Intent(context, SaleViewActivity.class);
        context.startActivity(intent);
    }

    public static void showOrderActivity(Context context) {
        Intent intent = new Intent(context, OrderActivity.class);
        context.startActivity(intent);
    }

    public static void showAccountActivity(Context context) {
        Intent intent = new Intent(context, AccountActivity.class);
        context.startActivity(intent);
    }

    public static void showAccountDetailActivity(Context context) {
        Intent intent = new Intent(context, AccountDetailActivity.class);
        context.startActivity(intent);
    }

    public static void showUserInfoActivity(Context context) {
        Intent intent = new Intent(context, UserInfoActivity.class);
        context.startActivity(intent);
    }

    public static void showChangePwdActivity(Context context) {
        Intent intent = new Intent(context, ChangePwdActivity.class);
        context.startActivity(intent);
    }

    public static void showOrderDetailActivity(Context context, MainOrder mainOrder) {
        Intent intent = new Intent(context, OrderDetailActivity.class);
        intent.putExtra(Constants.INTENT_ACTIVITY_MAIN_ORDER_ID, mainOrder.getId().toString());
        context.startActivity(intent);
    }

    public static void showOrderDetailMapActivity(Context context,
                                                  MainOrder mainOrder,
                                                  List<SubOrder> listOrders,
                                                  MerchantLocation location) {
        Intent intent = new Intent(context, OrderDetailMapActivity.class);
        intent.putExtra(Constants.INTENT_ACTIVITY_MAIN_ORDER, mainOrder);
        intent.putExtra(Constants.INTENT_ACTIVITY_SUB_ORDER_LIST, (Serializable) listOrders);
        intent.putExtra(Constants.INTENT_ACTIVITY_MERCHANT_LOCATION, location);
        context.startActivity(intent);
    }

    public static void showMyOrderActivity(Context context, Integer mainOrderState) {
        Intent intent = new Intent(context, MyOrderActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putInt(Constants.INTENT_ACTIVITY_MAIN_ORDER_STATE, mainOrderState);
        intent.putExtra(Constants.INTENT_ACTIVITY_MAIN_ORDER_STATE, mainOrderState);
        context.startActivity(intent);
    }

    public static void testService(Context context) {
        Intent intent = new Intent(context, LocationService.class);
        context.startService(intent);
    }

    public static void startLocationService(Context context) {
        Intent intent = new Intent(context, LocationService.class);
        context.startService(intent);
    }

    public static void startLocationBDService(Context context) {
        Intent intent = new Intent(context, LocationBDService.class);
        context.startService(intent);
    }

    public static void startBackgroundService(Context context) {
        Intent intent = new Intent(context, BackgroundService.class);
        context.startService(intent);
    }

    public static void stopLocationBDService(Context context) {
        Intent intent = new Intent(context, LocationBDService.class);
        context.stopService(intent);
    }

    public static void stopBackgroundService(Context context) {
        Intent intent = new Intent(context, BackgroundService.class);
        context.stopService(intent);
    }

    public static void testStopService(Context context) {
        Intent intent = new Intent(context, LocationService.class);
        context.stopService(intent);
    }


    public static void notifySimpleNotification(String ticker, String title, String content, Class<?> cls) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(AppContext.getInstance());
        mBuilder.setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示
                .setPriority(Notification.PRIORITY_DEFAULT)//设置该通知优先级
                .setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
                .setOngoing(false)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
                .setDefaults(Notification.DEFAULT_ALL)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合：//Notification.DEFAULT_ALL  Notification.DEFAULT_SOUND 添加声音 // requires VIBRATE permission
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(content)
                .setTicker(ticker);
        //点击的意图ACTION是跳转到Intent
        Intent resultIntent = new Intent(AppContext.getInstance(), cls);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(AppContext.getInstance(),
                0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pendingIntent);
        AppContext.getInstance().getmNotificationManager().notify(1, mBuilder.build());
    }

    public static void notifyClearAll(){
        AppContext.getInstance().getmNotificationManager().cancelAll();
    }

    public static void simpleCall(Context context, String phone) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + phone));
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        context.startActivity(intent);
    }

//    public static void sendBroadCastSingle( String actionName){
//        Intent intent = new Intent();
//        intent.setAction(actionName);
//        sendBroadcast(intent);
//    }
}
