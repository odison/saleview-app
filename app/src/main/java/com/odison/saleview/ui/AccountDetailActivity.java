package com.odison.saleview.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.odison.saleview.AppConfig;
import com.odison.saleview.AppContext;
import com.odison.saleview.R;
import com.odison.saleview.api.remote.XiaoGeApi;
import com.odison.saleview.base.BaseParameterNames;
import com.odison.saleview.base.Constants;
import com.odison.saleview.base.Dict;
import com.odison.saleview.bean.AccountDetail;
import com.odison.saleview.bean.DefaultPage;
import com.odison.saleview.util.StringUtils;
import com.odison.saleview.util.TLog;
import com.odison.saleview.util.ToastMessageTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class AccountDetailActivity extends AppCompatActivity {

    private List<AccountDetail> data = new ArrayList<AccountDetail>();
    private PullToRefreshListView mPullToRefreshListView;

    MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_detail);
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

        mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.ptflv_account_detail);
        adapter = new MyAdapter(this);
        mPullToRefreshListView.setAdapter(adapter);

        /**
         *
         */
        mPullToRefreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        init();

        mPullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                if (AppContext.getInstance().isLogin()) {
                    XiaoGeApi.listAccountDetail(AppConfig.app_mac, mCurPage, mJsonHandler);
                }
            }
        });

        /**
         * call listAccountDetail when activity onCreate
         */
        XiaoGeApi.listAccountDetail(AppConfig.app_mac,mCurPage,mJsonHandler);

//        // set on bottom listener
//        listView.setOnBottomListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                new GetDataTask(false).execute();
//            }
//        });
//        listView.setOnItemClickListener(new OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                ToastUtils.show(context, R.string.drop_down_tip);
//            }
//        });
        // listView.setShowFooterWhenNoMore(true);

    }

    private boolean myContain(List<AccountDetail> list, AccountDetail accountDetail) {
        for (AccountDetail account : list) {
            if (account.getId() == accountDetail.getId()) {
                return true;
            }
        }
        return false;
    }

    private Integer mCurPage = 1;

    private JsonHttpResponseHandler mJsonHandler = new JsonHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            TLog.log("AccountDetail:onSuccess", response.toString());

            try {
                if (response.getString(BaseParameterNames.JSON_RESPONSE_STATE).equals("true")) {
                    JSONObject obj = response.getJSONObject(BaseParameterNames.JSON_RESPONSE_DATA);
                    DefaultPage defaultPage = JSON.parseObject(obj.getJSONObject(BaseParameterNames.PAGE).toString(), DefaultPage.class);
                    if (mCurPage == defaultPage.getTotalPage()) {
                        mCurPage = 1;
                    } else {
                        mCurPage++;
                    }

                    List<AccountDetail> listAccountDetail = JSON.parseArray(obj.getJSONArray(BaseParameterNames.JSON_RESPONSE_LIST_ACCOUNT_DETAIL).toString(),
                            AccountDetail.class);
                    for (AccountDetail accountDeatil : listAccountDetail
                            ) {
                        if(!myContain(data,accountDeatil)){
                            adapter.addFirst(accountDeatil);
                        }
                    }

                    mPullToRefreshListView.onRefreshComplete();
                    adapter.notifyDataSetChanged();
                } else {
                    TLog.log("AccountDetail:onSuccess:false", response.toString());
                    mPullToRefreshListView.onRefreshComplete();
                }
            } catch (JSONException e) {
//                e.printStackTrace();
                mPullToRefreshListView.onRefreshComplete();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            TLog.log("AccountDetail:onFailure", errorResponse.toString());
            new ToastMessageTask().execute("network error,AccountDetail:onFailure");
            mPullToRefreshListView.onRefreshComplete();
        }
    };

    private void init() {
        ILoadingLayout startLabels = mPullToRefreshListView
                .getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel(getString(R.string.pulltofresh_list_header_pull_text));// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel(getString(R.string.pulltofresh_list_header_loading_text));// 刷新时
        startLabels.setReleaseLabel(getString(R.string.pulltofresh_list_header_release_text));// 下来达到一定距离时，显示的提示

        ILoadingLayout endLabels = mPullToRefreshListView.getLoadingLayoutProxy(
                false, true);
        endLabels.setPullLabel("上拉刷新...");// 刚下拉时，显示的提示
        endLabels.setRefreshingLabel("正在载入...");// 刷新时
        endLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示

        // 设置下拉刷新文本
        mPullToRefreshListView.getLoadingLayoutProxy(false, true)
                .setPullLabel("上拉刷新...");
        mPullToRefreshListView.getLoadingLayoutProxy(false, true).setReleaseLabel(
                "放开刷新...");
        mPullToRefreshListView.getLoadingLayoutProxy(false, true).setRefreshingLabel(
                "正在加载...");
        // 设置上拉刷新文本
        mPullToRefreshListView.getLoadingLayoutProxy(true, false)
                .setPullLabel("下拉刷新...");
        mPullToRefreshListView.getLoadingLayoutProxy(true, false).setReleaseLabel(
                "放开刷新...");
        mPullToRefreshListView.getLoadingLayoutProxy(true, false).setRefreshingLabel(
                "正在加载...");
    }

//    private class GetDataTask extends AsyncTask<Void, Void, String[]> {
//
//        private boolean isDropDown;
//
//        public GetDataTask(boolean isDropDown) {
//            this.isDropDown = isDropDown;
//        }
//
//        @Override
//        protected String[] doInBackground(Void... params) {
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                ;
//            }
//            return mStrings;
//        }
//
//        @Override
//        protected void onPostExecute(String[] result) {
//
//            if (isDropDown) {
//                listItems.addFirst("Added after drop down");
//                adapter.notifyDataSetChanged();
//
//                // should call onDropDownComplete function of DropDownListView at end of drop down complete.
//                SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd HH:mm:ss");
//                //listView.onDropDownComplete(getString(R.string.update_at) + dateFormat.format(new Date()));
//            } else {
//                moreDataCount++;
//                listItems.add("Added after on bottom");
//                adapter.notifyDataSetChanged();
//
//                if (moreDataCount >= MORE_DATA_MAX_COUNT) {
//                    listView.setHasMore(false);
//                }
//
//                // should call onBottomComplete function of DropDownListView at end of on bottom complete.
//                listView.onBottomComplete();
//            }
//
//            super.onPostExecute(result);
//        }
//    }

    private class MyAdapter extends BaseAdapter {

        private LayoutInflater mInflater;

        public MyAdapter(Context context) {
            mInflater = LayoutInflater.from(context);
        }

        public void addFirst(AccountDetail accountDetail) {
            data.add(0, accountDetail);
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
                convertView = mInflater.inflate(R.layout.item_account_detail, null);

                viewHolder.actionDescript = (TextView) convertView.findViewById(R.id.txv_item_account_detail_action_descript);
                viewHolder.date = (TextView) convertView.findViewById(R.id.txv_item_account_detail_date);
                viewHolder.balance = (TextView) convertView.findViewById(R.id.txv_item_account_detail_balance);
                viewHolder.amounts = (TextView) convertView.findViewById(R.id.txv_item_account_detail_amounts);
                viewHolder.relateOrderNo = (TextView) convertView.findViewById(R.id.txv_item_account_detail_relate_order_no);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            int iSize = Constants.ACCOUNT_DETAIL_ACTION_DESCRIPT.length;
            viewHolder.actionDescript.setText(Constants.ACCOUNT_DETAIL_ACTION_DESCRIPT[data.get(position).getActionType() % iSize]);
            viewHolder.date.setText(StringUtils.getDateString(data.get(position).getInsertTime()));
            viewHolder.balance.setText(data.get(position).getBeforeBalance().toString() +
                    "--->" + data.get(position).getAfterBalance().toString());
            String symbol = "+";
            if (data.get(position).getActionType() == Dict.ACCOUNT_DETAIL_ACTION_ENCASH) {
                symbol = "-";
            }
            viewHolder.amounts.setText(symbol + data.get(position).getAmount().toString());
            viewHolder.relateOrderNo.setText("关联订单号：" + data.get(position).getRelatedOrderNo());

            return convertView;
        }


        public final class ViewHolder {
            TextView actionDescript;
            TextView date;
            TextView balance;
            TextView amounts;
            TextView relateOrderNo;
        }
    }

}
