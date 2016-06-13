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
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.mobeta.android.dslv.DragSortListView;
import com.odison.saleview.AppConfig;
import com.odison.saleview.AppContext;
import com.odison.saleview.AppManager;
import com.odison.saleview.R;
import com.odison.saleview.adapter.DragSortAdapter;
import com.odison.saleview.api.remote.XiaoGeApi;
import com.odison.saleview.base.BaseParameterNames;
import com.odison.saleview.base.Constants;
import com.odison.saleview.base.Dict;
import com.odison.saleview.bean.MainOrder;
import com.odison.saleview.bean.MerchantLocation;
import com.odison.saleview.bean.SubOrder;
import com.odison.saleview.bean.SubOrderEx;
import com.odison.saleview.util.TLog;
import com.odison.saleview.util.ToastMessageTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class MyOrderActivity extends AppCompatActivity {

    private TextView mTxvMainOrderCounty;
    private TextView mTxvMainOrderSubCount;
    private TextView mTxvMainOrderLocationAddress;
    private TextView mTxvMainOrderReword;
    private TextView mTxvMainOrderProgress;
    private TextView mTxvMainOrderPassword;

    private AlertDialog mEmbraceAlertDialog;

    private View mViewMainOrder;

    private Integer iMainOrderState;

    private DragSortListView dragSortListView;

    private List<SubOrder> data;
    private MainOrder mMainOrder;
    private MerchantLocation mMerchantLocation;


    private DragSortAdapter adapter;

    //监听器在手机拖动停下的时候触发
    private DragSortListView.DropListener onDrop =
            new DragSortListView.DropListener() {
                @Override
                public void drop(int from, int to) {//from to 分别表示 被拖动控件原位置 和目标位置
                    if (from != to) {
                        //标记修改状态
                        //adapter.setModify(true);
                        SubOrder item = (SubOrder) adapter.getItem(from);//得到listview的适配器
                        adapter.remove(from);//在适配器中”原位置“的数据。
                        adapter.insert(item, to);//在目标位置中插入被拖动的控件。
                    }
                }
            };

    private BroadcastReceiver mReceiver =
            new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (Constants.INTENT_ACTION_BACKTOUI_GET_CATCHED_MAINORDER.equals(intent.getAction())) {
                        //TODO refresh mainorder view and send broadcast to get suborders
                        mMainOrder = (MainOrder) intent.getSerializableExtra(Constants.INTENT_ACTIVITY_MAIN_ORDER);
//                        refreshView();

                        Intent intentBroadCast = new Intent();
                        intentBroadCast.setAction(Constants.INTENT_ACTION_REQUEST_GET_SUBORDER);
                        intentBroadCast.putExtra(Constants.INTENT_ACTIVITY_MAIN_ORDER, mMainOrder);

                        sendBroadcast(intentBroadCast);
                    } else if (Constants.INTENT_ACTION_BACKTOUI_GET_SUBORDER.equals(intent.getAction())) {
                        mMerchantLocation = (MerchantLocation) intent.getSerializableExtra(Constants.INTENT_ACTIVITY_MERCHANT_LOCATION);
                        data = (List<SubOrder>) intent.getSerializableExtra(Constants.INTENT_ACTIVITY_SUB_ORDER_LIST);
                        adapter.setItems(data);
                        adapter.notifyDataSetChanged();
                        refreshView();

                    } else if (Constants.INTENT_ACTION_BACKTOUI_GEO_DONE.equals(intent.getAction())) {
                        boolean catched = intent.getBooleanExtra(Constants.INTENT_ACTIVITY_SUB_ORDER_CAUGHT, false);
                        if (catched) {
                            List<SubOrderEx> list = (List<SubOrderEx>) intent.getSerializableExtra(Constants.INTENT_ACTIVITY_SUB_ORDER_EX_LIST);
                            //TODO 保存数据到AppContext
                        }
//                        List<SubOrderEx> list = (List<SubOrderEx>) intent.getSerializableExtra(Constants.)
                    }
                }
            };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myorder);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intentActivity = getIntent();
        iMainOrderState = intentActivity.getIntExtra(Constants.INTENT_ACTIVITY_MAIN_ORDER_STATE,Dict.MAINORDER_STATE_CATCHED);

        initView();

        refreshView();

        //
        //data = getTestData();

        data = new ArrayList<>();
        mMainOrder = new MainOrder();
        mMerchantLocation = new MerchantLocation();

        dragSortListView.setDropListener(onDrop);

        adapter = new DragSortAdapter(this, data);

        dragSortListView.setAdapter(adapter);
        dragSortListView.setDragEnabled(true);

        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.INTENT_ACTION_BACKTOUI_GET_CATCHED_MAINORDER);
        filter.addAction(Constants.INTENT_ACTION_BACKTOUI_GET_SUBORDER);

        registerReceiver(mReceiver, filter);

        //send msg to background service to get data
        Intent intent = new Intent();
        intent.setAction(Constants.INTENT_ACTION_REQUEST_GET_CATCHED_MAINORDER);
        intent.putExtra(Constants.INTENT_ACTIVITY_MAIN_ORDER_STATE,iMainOrderState);

        sendBroadcast(intent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_my_order, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_my_order_save_sort:
                if (adapter.isModify()) {
                    new AlertDialog.Builder(this).setTitle("提示")
                            .setMessage("保存顺序到服务器？")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //TODO send save sort broadcast
                                    saveSortSubOrder();
                                    finish();
                                }
                            })
                            .show();
                }
                break;
//            case R.id.home:
//                if (adapter.isModify()) {
//                    new AlertDialog.Builder(this).setTitle("警告")
//                            .setMessage("排序顺序有改动，是否保存？")
//                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    //TODO send save sort broadcast
//                                    saveSortSubOrder();
//                                    finish();
//                                }
//                            })
//                            .show();
//                }
//                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView() {

        mViewMainOrder = findViewById(R.id.layout_myorder_detail_mainorder);
        mViewMainOrder.setVisibility(View.GONE);

        mTxvMainOrderCounty = (TextView) findViewById(R.id.txv_myorder_detail_main_county);
        mTxvMainOrderLocationAddress = (TextView) findViewById(R.id.txv_myorder_detail_main_location_address);
        mTxvMainOrderReword = (TextView) findViewById(R.id.txv_myorder_detail_main_reword);
        mTxvMainOrderProgress = (TextView) findViewById(R.id.txv_myorder_detail_main_progress);
        mTxvMainOrderPassword = (TextView) findViewById(R.id.txv_myorder_detail_main_password);

        mTxvMainOrderPassword.setVisibility(View.GONE);

        //查看取件密码
        mTxvMainOrderPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEmbraceAlertDialog = new AlertDialog.Builder(MyOrderActivity.this).setTitle("订单包密码")
                        .setMessage(mMainOrder.getEmbraceCode())
                        .setPositiveButton("揽件", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (AppContext.getInstance().isLogin()) {
                                    if (mMainOrder != null && mMainOrder.getId() != null
                                            && mMainOrder.getEmbraceCode() != null) {
                                        new ToastMessageTask().execute(mMainOrder.getId()+" "+mMainOrder.getEmbraceCode());
                                        XiaoGeApi.embraceOrder(AppConfig.app_mac,
                                                mMainOrder, mEmbraceJsonHandler);
                                    }
                                } else {
                                    new ToastMessageTask().execute("您当前未登录，请登录后重试");
                                    dialog.dismiss();
                                }
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
            }
        });

        dragSortListView = (DragSortListView) findViewById(R.id.dslv_myorder_detail_suborder);
    }

    private JsonHttpResponseHandler mEmbraceJsonHandler = new JsonHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            TLog.log("embraceOrder:", response.toString());
            try {
                if (response.getString(BaseParameterNames.JSON_RESPONSE_STATE).equals("true")) {
                    //TODO 添加揽件操作后续
                    new ToastMessageTask().execute("订单号：" + mMainOrder.getId() + " 揽件成功");
                    mEmbraceAlertDialog.dismiss();
                    mTxvMainOrderPassword.setVisibility(View.GONE);

                    Intent intent = new Intent();
                    intent.setAction(Constants.INTENT_ACTION_BACKTOUI_UPD_MYORDER);

                    sendBroadcast(intent);

                    AppManager.getAppManager().finishActivity(MyOrderActivity.this);

                } else {
                    String responseCode = response.getString(BaseParameterNames.JSON_RESPONSE_CODE);
                    String responseMessage = response.getString(BaseParameterNames.JSON_RESPONSE_MESSAGE);
                    TLog.error(responseCode+" "+responseMessage);

                    new ToastMessageTask().execute("揽件失败，"+responseCode+" "+responseMessage);
                    //TODO get error
                }
            } catch (JSONException e) {
                TLog.error(e.toString());
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            TLog.error("网络错误，请检查网络后重试...");
            new ToastMessageTask().execute("网络错误，请检查网络后重试...");
        }
    };

    @Override
    protected void onDestroy() {
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    private void refreshView() {

//        mTxvMainOrderCounty.setText("");
//        mTxvMainOrderLocationAddress.setText("");
//        mTxvMainOrderReword.setText("");
//        mTxvMainOrderProgress.setText("");


        if (mMainOrder != null && mMainOrder.getCounty() != null) {
            mViewMainOrder.setVisibility(View.VISIBLE);
            mTxvMainOrderCounty.setText("区域：" + mMainOrder.getCounty());
        }

        if (mMainOrder != null && mMainOrder.getAddress() != null)
            mTxvMainOrderLocationAddress.setText("取货地址：" + mMainOrder.getAddress());
        if (mMainOrder != null && mMainOrder.getReword() != null)
            mTxvMainOrderReword.setText("跑腿费：" + mMainOrder.getReword() + " 元");
        if (mMainOrder != null && mMainOrder.getSubFinishedCount() != null && mMainOrder.getSubCount() != null) {
            mTxvMainOrderProgress.setText("当前进度：" + mMainOrder.getSubFinishedCount() + "/" + mMainOrder.getSubCount());
        }

        if (mMainOrder != null && mMainOrder.getEmbraceCode() != null) {
            if (mMainOrder.getState() != null && mMainOrder.getState() == Dict.MAINORDER_STATE_CATCHED)
                mTxvMainOrderPassword.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onBackPressed() {
        if (adapter.isModify()) {
            new AlertDialog.Builder(this).setTitle("提示")
                    .setMessage("排序顺序有改动，是否保存？")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //TODO send save sort broadcast
                            saveSortSubOrder();
                            finish();
                        }
                    })
                    .show();
        } else {
            super.onBackPressed();
        }
    }

    /**
     * 保存排序后的子订单列表
     */
    public void saveSortSubOrder() {
        List<SubOrder> list = adapter.getItems();

        if(list.size() == 0){
            new ToastMessageTask().execute("当前没有订单");
            return;
        }
        //set sub index
        for (int i = 0; i != list.size(); ++i) {
            list.get(i).setSubIndex(i);
        }
        AppContext.getInstance().setmSubOrders(list);
//        if(AppContext.getInstance().getmMapSubOrderLatLngs() != null
//                && AppContext.getInstance().getmMapSubOrderLatLngs().size() == list.size()){
//            AppContext.getInstance().setmSubOrderExs(OrderUtil.getSubOrderExList(list,
//                    AppContext.getInstance().getmMapSubOrderLatLngs()));
//        }
        Intent intent = new Intent();
        intent.setAction(Constants.INTENT_ACTION_REQUEST_UPD_SUBORDER_SORT);

        sendBroadcast(intent);
    }
}
