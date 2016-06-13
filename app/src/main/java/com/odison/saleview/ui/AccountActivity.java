package com.odison.saleview.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.odison.saleview.AppContext;
import com.odison.saleview.R;
import com.odison.saleview.base.Constants;
import com.odison.saleview.bean.Account;
import com.odison.saleview.util.TLog;
import com.odison.saleview.util.UIHelper;

public class AccountActivity extends AppCompatActivity implements View.OnClickListener
         {

    private TextView mTxvAccountBalance;

    private TextView mTxvAccountBalanceFuture;

    private Button mBtnAccountDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
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

        init();

        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.INTENT_ACTION_REMOTE_ACCOUNT_UPDATE);

        registerReceiver(mReceiver, filter);

        //send to backgroundservice to get account info from server
        Intent intent = new Intent();
        intent.setAction(Constants.INTENT_ACTION_REQUEST_GET_INFO);
        sendBroadcast(intent);
        //
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    private void init() {
        mTxvAccountBalance = (TextView) findViewById(R.id.txv_account_balance);
        mTxvAccountBalanceFuture = (TextView) findViewById(R.id.txv_account_balance_future);
        mBtnAccountDetail = (Button) findViewById(R.id.btn_account_detail);

        refreshBalance();

        mBtnAccountDetail.setOnClickListener(this);
    }

    private void refreshBalance() {
        Account account = AppContext.getInstance().getLocalAccount();
        if (null != account && account.getId() != 0) {
            TLog.log("find account,update ui");
            mTxvAccountBalance.setText(account.getBalance().toString());
        }
    }


    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Constants.INTENT_ACTION_REMOTE_ACCOUNT_UPDATE.equals(intent.getAction())) {
                refreshBalance();
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_account_detail:
                UIHelper.showAccountDetailActivity(this);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_account_detail, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_item_account_detail_show_detail:
                //TODO shou account detail activity
                UIHelper.showAccountDetailActivity(AccountActivity.this);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

//    @Override
//    public boolean onMenuItemClick(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.menu_item_account_detail_show_detail:
//                //TODO shou account detail activity
//                UIHelper.showAccountDetailActivity(AccountActivity.this);
//                break;
//            default:
//                break;
//        }
//        return true;
//    }
}
