package com.odison.saleview;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

import com.odison.saleview.ui.MainActivity;
import com.odison.saleview.util.UIHelper;
//import net.oschina.app.util.TDevice;
//
//import org.kymjs.kjframe.http.KJAsyncTask;
//import org.kymjs.kjframe.utils.FileUtils;
//import org.kymjs.kjframe.utils.PreferenceHelper;
//
//import java.io.File;

/**
 * 应用启动界面
 * 参考oschina app
 * @author odison
 * @created
 */
public class AppStart extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 防止第三方跳转时出现双实例
        Activity aty = AppManager.getActivity(MainActivity.class);
        if (aty != null && !aty.isFinishing()) {
            finish();
        }
        // SystemTool.gc(this); //针对性能好的手机使用，加快应用相应速度

        final View view = View.inflate(this, R.layout.app_start, null);
        setContentView(view);
        // 渐变展示启动屏
        AlphaAnimation aa = new AlphaAnimation(0.5f, 1.0f);
        aa.setDuration(800);
        view.startAnimation(aa);
        aa.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationEnd(Animation arg0) {
                redirectTo();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}

            @Override
            public void onAnimationStart(Animation animation) {}
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
//        int cacheVersion = PreferenceHelper.readInt(this, "first_install",
//                "first_install", -1);
//        int currentVersion = TDevice.getVersionCode();
//        if (cacheVersion < currentVersion) {
//            PreferenceHelper.write(this, "first_install", "first_install",
//                    currentVersion);
//            cleanImageCache();
//        }
    }



    /**
     * 跳转到...
     */
    private void redirectTo() {
        Log.i("haha","haha");
        UIHelper.showSaleViewActivity(this);
        //
        //UIHelper.showMainActivity(this);
        //close self
        finish();
    }
}
