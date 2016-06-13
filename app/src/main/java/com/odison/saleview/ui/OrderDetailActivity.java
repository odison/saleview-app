package com.odison.saleview.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.odison.saleview.AppConfig;
import com.odison.saleview.AppContext;
import com.odison.saleview.AppManager;
import com.odison.saleview.R;
import com.odison.saleview.api.remote.XiaoGeApi;
import com.odison.saleview.base.BaseParameterNames;
import com.odison.saleview.base.Constants;
import com.odison.saleview.base.Dict;
import com.odison.saleview.bean.MainOrder;
import com.odison.saleview.bean.MerchantLocation;
import com.odison.saleview.bean.SubOrder;
import com.odison.saleview.util.StringUtils;
import com.odison.saleview.util.TLog;
import com.odison.saleview.util.ToastMessageTask;
import com.odison.saleview.util.UIHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class OrderDetailActivity extends AppCompatActivity {

    private TextView mTxvMainOrderCounty;
    private TextView mTxvMainOrderSubCount;
    private TextView mTxvMainOrderLocationAddress;
    private TextView mTxvMainOrderReword;
    private TextView mTxvMainOrderCatch;

    private ListView mListView;

    private MainOrder mMainOrder = new MainOrder();

    private MerchantLocation mLocation = new MerchantLocation();

    private List<SubOrder> data = new ArrayList<SubOrder>();

    MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //添加标题栏back操作按钮
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //这个加了，上面那几行可以注释。。。
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initView();

        //管理activity
        AppManager.getAppManager().addActivity(this);


        Intent intent = getIntent();
        String orderId = intent.getExtras().getString(Constants.INTENT_ACTIVITY_MAIN_ORDER_ID);
        mMainOrder.setId(StringUtils.toInt(orderId));

        mTxvMainOrderCatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(AppContext.getInstance().isLogin())
                    XiaoGeApi.takeOrder(AppConfig.app_mac,mMainOrder,mJsonCatchHandler);
            }
        });

        adapter = new MyAdapter(this);
        mListView.setAdapter(adapter);

        if (AppContext.getInstance().isLogin())
            XiaoGeApi.listSubOrder(AppConfig.app_mac, mMainOrder, mJsonHandler);

    }

    private JsonHttpResponseHandler mJsonHandler = new JsonHttpResponseHandler() {
        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            TLog.log("OrderDetailActivity:listSubOrder:onFailure",
                    errorResponse.toString());
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            TLog.log("OrderDetailActivity:listSubOrder:onSuccess", response.toString());
            try {
                if (response.getString(BaseParameterNames.JSON_RESPONSE_STATE).equals("true")) {
                    JSONObject obj = response.getJSONObject(BaseParameterNames.JSON_RESPONSE_DATA);
                    mMainOrder = JSON.parseObject(obj.getJSONObject(BaseParameterNames.MAIN_ORDER).toString(),
                            MainOrder.class);
                    mLocation = JSON.parseObject(obj.getJSONObject(BaseParameterNames.MERCHANT_LOCATION).toString(),
                            MerchantLocation.class);
                    List<SubOrder> listSubOrder = JSON.parseArray(obj.getJSONArray(BaseParameterNames.LIST_SUBORDER).toString(),
                            SubOrder.class);
                    refreshView();

                    for (SubOrder suborder:listSubOrder
                            ) {
                        adapter.addLast(suborder);
                    }

                    adapter.notifyDataSetChanged();
                    //mListView.notifyData
                    //mListView.deferNotifyDataSetChanged();
                } else {
                    //TODO show window for back action
                    new ToastMessageTask().execute("无法获取到相应的信息");
                }
            } catch (JSONException e) {
//                e.printStackTrace();
            }
        }
    };

    private JsonHttpResponseHandler mJsonCatchHandler = new JsonHttpResponseHandler() {
        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            TLog.log("OrderDetailActivity:takeOrder:onFailure",
                    errorResponse.toString());
            new ToastMessageTask().execute("网络错误");
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            TLog.log("OrderDetailActivity:takeOrder:onSuccess",
                    response.toString());
            try {
                if(response.getString(BaseParameterNames.JSON_RESPONSE_STATE).equals("true")){
                    //TODO 接单成功 跳转到我的订单
                    new ToastMessageTask().execute("接单成功");
                    AppContext.getInstance().setCourierState(Dict.COURIER_GIS_STATE_TAKEN);
                    AppManager.getAppManager().finishActivity(OrderActivity.class);
                    UIHelper.showMyOrderActivity(OrderDetailActivity.this, Dict.MAINORDER_STATE_CATCHED);
                    //
                    Intent intent = new Intent();
                    intent.setAction(Constants.INTENT_ACTION_BACKTOUI_UPD_MYORDER);
                    sendBroadcast(intent);
                    finish();
                }else{
                    //TODO 接单失败 显示失败原因？跳转到远程订单界面
                    new ToastMessageTask().execute("该订单不存在或被人接取...");
                    finish();
                }
            } catch (JSONException e) {
                TLog.log("json:exception",e.toString());
            }
        }
    };

    private void initView() {
        mTxvMainOrderCounty = (TextView) findViewById(R.id.txv_order_detail_main_county);
        mTxvMainOrderLocationAddress = (TextView) findViewById(R.id.txv_order_detail_main_location_address);
        mTxvMainOrderSubCount = (TextView) findViewById(R.id.txv_order_detail_main_suborder_count);
        mTxvMainOrderReword = (TextView) findViewById(R.id.txv_order_detail_main_reword);
        mTxvMainOrderCatch = (TextView) findViewById(R.id.txv_order_detail_main_catch);

        mListView = (ListView) findViewById(R.id.lv_order_detail_suborder);

    }

    private void refreshView() {
        if (mMainOrder != null && mMainOrder.getCounty() != null)
            mTxvMainOrderCounty.setText("区域：" + mMainOrder.getCounty());
        if (mMainOrder != null && mMainOrder.getAddress() != null)
            mTxvMainOrderLocationAddress.setText("取货地址：" + mMainOrder.getAddress());
        if (mMainOrder != null && mMainOrder.getSubCount() != null)
            mTxvMainOrderSubCount.setText(mMainOrder.getSubCount() + " 个子订单");
        if (mMainOrder != null && mMainOrder.getReword() != null)
            mTxvMainOrderReword.setText("跑腿费：" + mMainOrder.getReword() + " 元");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_order_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_order_detail_mapview:
                UIHelper.showOrderDetailMapActivity(OrderDetailActivity.this, mMainOrder, data, mLocation);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private class MyAdapter extends BaseAdapter {

        private LayoutInflater mInflater;

        public MyAdapter(Context context) {
            mInflater = LayoutInflater.from(context);
        }

        public void addFirst(SubOrder subOrder) {
            data.add(0, subOrder);
        }

        public void addLast(SubOrder subOrder){
            data.add(subOrder);
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.item_suborder, null);

                viewHolder.subOrderId = (TextView) convertView.findViewById(R.id.txv_item_sub_id);
                viewHolder.receiverName = (TextView) convertView.findViewById(R.id.txv_item_sub_receiver_name);
                viewHolder.receiverAddress = (TextView) convertView.findViewById(R.id.txv_item_sub_receiver_address);
                viewHolder.commodity = (TextView) convertView.findViewById(R.id.txv_item_sub_commodity);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.subOrderId.setText("订单号：" + data.get(position).getId().toString());
            viewHolder.receiverName.setText("收件人：" + data.get(position).getReceiverName());
            viewHolder.receiverAddress.setText("地址：" + data.get(position).getReceiverAddress());
            viewHolder.commodity.setText("商品类型：" + data.get(position).getCommodityId().toString());

            return convertView;
        }


        public final class ViewHolder {
            TextView subOrderId;
            TextView receiverName;
            TextView receiverAddress;
            TextView commodity;
        }
    }

}
