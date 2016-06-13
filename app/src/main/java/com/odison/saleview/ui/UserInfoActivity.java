package com.odison.saleview.ui;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.odison.saleview.AppContext;
import com.odison.saleview.R;
import com.odison.saleview.base.Constants;
import com.odison.saleview.base.Dict;
import com.odison.saleview.bean.Courier;
import com.odison.saleview.util.StringUtils;
import com.odison.saleview.util.TLog;
import com.odison.saleview.util.ToastMessageTask;
import com.odison.saleview.util.UIHelper;

public class UserInfoActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView mTxvUserName;
    private TextView mTxvUserAddress;
    private TextView mTxvPhone;
    private TextView mTxvSignTime;
    private Switch mSwhUserState;

    private Button mBtnLogout;
    private Button mBtnGoAccount;
    private Button mBtnChangePwd;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(Constants.INTENT_ACTION_REMOTE_ACCOUNT_UPDATE.equals(intent.getAction())){
                refreshUi();
                TLog.log("UserInfoActivity receive broadcast to update ui");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_back);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO go back
                onBackPressed();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initView();

        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.INTENT_ACTION_REMOTE_ACCOUNT_UPDATE);

        registerReceiver(mReceiver,filter);

        Intent intent = new Intent();
        intent.setAction(Constants.INTENT_ACTION_REQUEST_GET_INFO);

        sendBroadcast(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    private void initView() {
        mTxvUserName = (TextView) findViewById(R.id.txv_user_name);
        mTxvUserAddress = (TextView) findViewById(R.id.txv_user_address);
        mTxvPhone = (TextView) findViewById(R.id.txv_user_phone);
        mTxvSignTime = (TextView) findViewById(R.id.txv_user_signtime);

        mSwhUserState = (Switch) findViewById(R.id.swh_user_state);

        mBtnLogout = (Button) findViewById(R.id.btn_user_logout);
        mBtnGoAccount = (Button) findViewById(R.id.btn_user_goaccount);
        mBtnChangePwd = (Button) findViewById(R.id.btn_user_changepwd);

        if(AppContext.getInstance().isLogin()){
            mTxvUserName.setText("未登录");
            mTxvUserAddress.setText("");
            mTxvPhone.setText("");
            mTxvSignTime.setText("");
        }

//        if(AppContext.getInstance().isLogin())
//            refreshUi();
//        else{
//            new ToastMessageTask().execute("请先登录...");
//        }

        mSwhUserState.setOnCheckedChangeListener(stateChecked);

        mBtnLogout.setOnClickListener(this);
        mBtnGoAccount.setOnClickListener(this);
        mBtnChangePwd.setOnClickListener(this);
    }

    private void refreshUi() {
        Courier courier = AppContext.getInstance().getLocalCourier();

        if(null != courier && courier.getName() != null && courier.getName().length()!= 0){
            mTxvUserName.setText(courier.getName());
            mTxvUserAddress.setText(courier.getAddress());
            mTxvSignTime.setText(StringUtils.getShortDateString(courier.getSignTime()));
            mTxvPhone.setText(courier.getPhone());

            if(AppContext.getInstance().getCourierState() == Dict.COURIER_GIS_STATE_WAIT){
                mSwhUserState.setChecked(true);
            }else {
                mSwhUserState.setChecked(false);
            }
        }
    }

    private CompoundButton.OnCheckedChangeListener stateChecked = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(AppContext.getInstance().isLogin()){
                //TODO 更改状态需要向服务器请求
                Courier courier = AppContext.getInstance().getLocalCourier();
                if( courier.getState() == Dict.COURIER_GIS_STATE_WAIT || courier.getState() == Dict.COURIER_GIS_STATE_REST){

                }else{
                    new ToastMessageTask().execute("当前状态无法改变");
                    mSwhUserState.setChecked(true);
                }
            }
        }
    };


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.btn_user_logout){
            new AlertDialog.Builder(this).setTitle("警告")
                    .setMessage("确定退出账号？")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //TODO send logout broadcast
                            Intent intent = new Intent();
                            intent.setAction(Constants.INTENT_ACTION_LOGOUT);
                            sendBroadcast(intent);
                            finish();
                        }
                    })
                    //.setPositiveButton("取消",null)
                    .show();
        }else if(id == R.id.btn_user_goaccount){
            UIHelper.showAccountActivity(this);
        }else if(id == R.id.btn_user_changepwd){
            UIHelper.showChangePwdActivity(this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_userinfo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.mnu_item_user_info_account) {
            if (AppContext.getInstance().isLogin()) {
                UIHelper.showAccountActivity(this);
            } else {
                new ToastMessageTask().execute(getString(R.string.toast_no_login_tips));
                UIHelper.showLoginActivity(this);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
