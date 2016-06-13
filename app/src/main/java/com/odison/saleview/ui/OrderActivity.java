package com.odison.saleview.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.odison.saleview.AppConfig;
import com.odison.saleview.AppManager;
import com.odison.saleview.R;
import com.odison.saleview.api.remote.XiaoGeApi;
import com.odison.saleview.base.BaseParameterNames;
import com.odison.saleview.base.Dict;
import com.odison.saleview.bean.DefaultPage;
import com.odison.saleview.bean.MainOrder;
import com.odison.saleview.util.TLog;
import com.odison.saleview.util.ToastMessageTask;
import com.odison.saleview.util.UIHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class OrderActivity extends AppCompatActivity {

    private PullToRefreshListView mPullToRefreshListView;
    private List<MainOrder> data  = new ArrayList<MainOrder>();

    MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
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

        mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.ptflv_mainorder);
        //data = getData();
        adapter = new MyAdapter(this);
        mPullToRefreshListView.setAdapter(adapter);

        /**
		 * Mode.BOTH：同时支持上拉下拉
         * Mode.PULL_FROM_START：只支持下拉Pulling Down
         * Mode.PULL_FROM_END：只支持上拉Pulling Up
		 */
		/**
		 * 如果Mode设置成Mode.BOTH，需要设置刷新Listener为OnRefreshListener2，并实现onPullDownToRefresh()、onPullUpToRefresh()两个方法。
         * 如果Mode设置成Mode.PULL_FROM_START或Mode.PULL_FROM_END，需要设置刷新Listener为OnRefreshListener，同时实现onRefresh()方法。
         * 当然也可以设置为OnRefreshListener2，但是Mode.PULL_FROM_START的时候只调用onPullDownToRefresh()方法，
         * Mode.PULL_FROM的时候只调用onPullUpToRefresh()方法.
		 */
        mPullToRefreshListView.setMode(Mode.BOTH);
        init();
        //管理activity
        AppManager.getAppManager().addActivity(this);

        /**
		 * setOnRefreshListener(OnRefreshListener listener):设置刷新监听器；
		 * setOnLastItemVisibleListener(OnLastItemVisibleListener listener):设置是否到底部监听器；
		 * setOnPullEventListener(OnPullEventListener listener);设置事件监听器；
		 * onRefreshComplete()：设置刷新完成
		 */
		/**
		 * pulltorefresh.setOnScrollListener()
		 */
        // SCROLL_STATE_TOUCH_SCROLL 正在滚动
        // SCROLL_STATE_FLING 手指做了抛的动作（手指离开屏幕前，用力滑了一下）
        // SCROLL_STATE_IDLE 停止滚动
		/**
		 * setOnLastItemVisibleListener
		 * 当用户拉到底时调用
		 */
		/**
		 * setOnTouchListener是监控从点下鼠标 （可能拖动鼠标）到放开鼠标（鼠标可以换成手指）的整个过程 ，他的回调函数是onTouchEvent（MotionEvent event）,
		 * 然后通过判断event.getAction()是MotionEvent.ACTION_UP还是ACTION_DOWN还是ACTION_MOVE分别作不同行为。
         * setOnClickListener的监控时间只监控到手指ACTION_DOWN时发生的行为
		 */
        mPullToRefreshListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                XiaoGeApi.listMainOrder(AppConfig.app_mac, Dict.MAINORDER_STATE_PUSHED,
                        mPage, mJsonHandler);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                mPullToRefreshListView.onRefreshComplete();
            }
        });

//        //go to suborder form if click
        mPullToRefreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MainOrder mainOrder = (MainOrder) adapter.getItem(position);
                //new ToastMessageTask().execute(""+mainOrder.getId());
                UIHelper.showOrderDetailActivity(OrderActivity.this,mainOrder);
            }
        });

        //进入时候获取一次数据
        XiaoGeApi.listMainOrder(AppConfig.app_mac, Dict.MAINORDER_STATE_PUSHED,
                mPage, mJsonHandler);


    }
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

    /**
     * 单机测试数据 查看界面显示
     * @return
     */
    private List<MainOrder> getData(){
        List<MainOrder> list = new ArrayList<MainOrder>();
        for(int i = 3;i < 10;i ++){
            MainOrder mainOrder = new MainOrder();
            mainOrder.setId(i);
            mainOrder.setAddress("地址:" + i);
            mainOrder.setReword(new BigDecimal(0));
            mainOrder.setCounty("路桥");
            mainOrder.setSubCount(10);
            list.add(mainOrder);
        }

        return list;
    }
//
//    private class FinishRefresh extends AsyncTask<Void, Void, Void> {
//        @Override
//        protected Void doInBackground(Void... params) {
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void result){
////        	adapter.notifyDataSetChanged();
//            //onRefreshComplete 停止刷新
//            mPullToRefreshListView.onRefreshComplete();
//        }
//    }

    private boolean myContain(List<MainOrder> list,MainOrder mainOrder){
        for (MainOrder order:list) {
            if(order.getId().equals(mainOrder.getId())){
                return true;
            }
        }
        return false;
    }

    private Integer mPage = 1;

    private JsonHttpResponseHandler mJsonHandler = new JsonHttpResponseHandler(){
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            TLog.log("OrderActivity:listOrder",response.toString());
            try {
                if(response.getString(BaseParameterNames.JSON_RESPONSE_STATE).equals("true")){

                    JSONObject obj = response.getJSONObject(BaseParameterNames.JSON_RESPONSE_DATA);
                    DefaultPage defaultPage = JSON.parseObject(obj.getJSONObject(BaseParameterNames.PAGE).toString(),DefaultPage.class);
                    if(mPage == defaultPage.getTotalPage()){
                        mPage = 1;
                    }else {
                        mPage++;
                    }
                    List<MainOrder> listMainOrder = JSON.parseArray(obj.getJSONArray(BaseParameterNames.JSON_RESPONSE_LIST_MAIN_ORDER).toString(),
                            MainOrder.class);
                    if (listMainOrder.size() == 0){
                        new ToastMessageTask().execute("没有更多了...");
                        mPullToRefreshListView.onRefreshComplete();
                    }else {
                        for (MainOrder mainOrder : listMainOrder) {
                            if (!myContain(data, mainOrder))
                                adapter.addFirst(mainOrder);
                        }
                        //
                        mPullToRefreshListView.onRefreshComplete();
                        adapter.notifyDataSetChanged();
                    }
                }else{
                    mPullToRefreshListView.onRefreshComplete();
                }
            } catch (JSONException e) {
                mPullToRefreshListView.onRefreshComplete();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            TLog.log("OrderActivity:listMainOrder:error:",errorResponse.toString());
            mPullToRefreshListView.onRefreshComplete();
            new ToastMessageTask().execute("没有更多了");
        }
    };

    private class MyAdapter extends BaseAdapter {
        private LayoutInflater mInflater;

        public MyAdapter(Context context) {
            // TODO Auto-generated constructor stub
            mInflater = LayoutInflater.from(context);
        }

        public void addFirst(MainOrder bean){
            data.add(0, bean);
        }

        public void addLast(MainOrder bean){
            data.add(bean);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            //pulltorefresh 有个head是位置0
            return data.get(position-1);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            ViewHolder viewHolder = null;
            if(convertView == null){
                viewHolder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.item_mainorder_1, null);

                viewHolder.county = (TextView) convertView.findViewById(R.id.txv_item_main_county);
                viewHolder.reword = (TextView) convertView.findViewById(R.id.txv_item_main_reword);
                viewHolder.subcount = (TextView) convertView.findViewById(R.id.txv_item_main_suborder_count);
                viewHolder.address = (TextView) convertView.findViewById(R.id.txv_item_main_location_address);

                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) convertView.getTag();
            }
//            convertView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    MainOrder mainOrder = getItem()
//                }
//            });

            //viewHolder.county.setText("区域：");
            viewHolder.county.setText("区域："+data.get(position).getCounty());
            viewHolder.address.setText("取货地址："+data.get(position).getAddress());
            viewHolder.subcount.setText(data.get(position).getSubCount().toString()+ " 个子订单");
            viewHolder.reword.setText("跑腿费：￥"+ data.get(position).getReword().toString() + " 元");
            //StringBuilder sb =
//            viewHolder.title.setText(data.get(position).getTitle());
//            viewHolder.content.setText(data.get(position).getContent());

            return convertView;
        }

        public final class ViewHolder{
            public TextView county;
            public TextView address;
            public TextView subcount;
            public TextView reword;
//            TextView title;
//            TextView content;
        }
    }

}
