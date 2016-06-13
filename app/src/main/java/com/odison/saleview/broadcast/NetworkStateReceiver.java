package com.odison.saleview.broadcast;

/**
 * Created by PC-8 on 2015/12/15.
 */

/*
        <receiver android:name=".NetworkStateReceiver">
        	<intent-filter>
        		<action android:name="android.net.conn.CONNECTIVITY_CHANGE"/>
        		<category android:name="android.intent.category.DEFAULT" />
        	</intent-filter>
        </receiver>
 */
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

public class NetworkStateReceiver extends BroadcastReceiver {

    private static final String TAG = "NetworkStateReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "network state changed.");
        if (!isNetworkAvailable(context)) {
            Toast.makeText(context, "network disconnected!", 0).show();
        }
    }

    /**
     * 网络是否可用
     *
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager mgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] info = mgr.getAllNetworkInfo();
        if (info != null) {
            for (int i = 0; i < info.length; i++) {
                if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }

}
