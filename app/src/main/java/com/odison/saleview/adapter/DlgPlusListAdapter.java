package com.odison.saleview.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.odison.saleview.AppContext;
import com.odison.saleview.R;
import com.odison.saleview.bean.SubOrderEx;
import com.odison.saleview.util.NewDistanceUtil;
import com.odison.saleview.util.StringUtils;

import java.util.List;

/**
 * DialogPlus listview adapter
 * <p/>
 * Created by odison on 2016/1/6.
 */
public class DlgPlusListAdapter extends BaseAdapter {

    private Context context;
    private List<SubOrderEx> items;
    private LayoutInflater mInflater;


    public DlgPlusListAdapter(Context context, List<SubOrderEx> data) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
        this.items = data;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        SubOrderEx item = items.get(position);
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_dlg_order, null);

            viewHolder.subOrderId = (TextView) convertView.findViewById(R.id.txv_item_dlg_order_id);
            viewHolder.receiverName = (TextView) convertView.findViewById(R.id.txv_item_dlg_order_receiver_name);
            viewHolder.receiverAddress = (TextView) convertView.findViewById(R.id.txv_item_dlg_order_receiver_address);
            viewHolder.commodity = (TextView) convertView.findViewById(R.id.txv_item_dlg_order_commodity);
            viewHolder.subIndex = (TextView) convertView.findViewById(R.id.txv_item_dlg_order_subindex);
            viewHolder.distance = (TextView) convertView.findViewById(R.id.txv_item_dlg_order_distance);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.subOrderId.setText("订单号：" + item.getId().toString());
        viewHolder.receiverName.setText("收件人：" + item.getReceiverName());
        viewHolder.receiverAddress.setText("地址：" + item.getReceiverAddress());
        viewHolder.commodity.setText("商品类型：" + item.getCommodityId().toString());
        viewHolder.subIndex.setText("" + (item.getSubIndex() + 1));
        //显示距离
        Double distance = NewDistanceUtil.getDistance(new LatLng(item.getLat(), item.getLng()),
                AppContext.getInstance().getmLocation());
        viewHolder.distance.setText(StringUtils.getDistance(distance));

        return convertView;
    }

    class ViewHolder {
        TextView subOrderId;
        TextView receiverName;
        TextView receiverAddress;
        TextView commodity;
        TextView subIndex;
        TextView distance;
    }
}
