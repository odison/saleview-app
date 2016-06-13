package com.odison.saleview.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.odison.saleview.R;
import com.odison.saleview.base.Dict;
import com.odison.saleview.bean.SubOrderEx;


import java.util.List;

/**
 * DialogPlus listview adapter
 *
 * Created by odison on 2016/1/6.
 */
public class DlgPlusOrderCtrlAdapter extends BaseAdapter {

    private Context context;
    private List<SubOrderEx> items;
    private LayoutInflater mInflater;
    private View.OnClickListener mViewOnClick;


    public DlgPlusOrderCtrlAdapter(Context context, List<SubOrderEx> data, View.OnClickListener mOnClick){
        this.context = context;
        mInflater = LayoutInflater.from(context);
        this.items = data;
        this.mViewOnClick = mOnClick;
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
            convertView = mInflater.inflate(R.layout.item_dlg_order_ctrl, null);

            viewHolder.subOrderId = (TextView) convertView.findViewById(R.id.txv_item_dlg_order_ctrl_id);
            viewHolder.receiverName = (TextView) convertView.findViewById(R.id.txv_item_dlg_order_ctrl_receiver_name);
            viewHolder.receiverAddress = (TextView) convertView.findViewById(R.id.txv_item_dlg_order_ctrl_receiver_address);
            viewHolder.commodity = (TextView) convertView.findViewById(R.id.txv_item_dlg_order_ctrl_commodity);
            viewHolder.dispatchPersonal = (TextView) convertView.findViewById(R.id.txv_item_dlg_order_ctrl_dispatch_personal);
            viewHolder.dispatchProperty = (TextView) convertView.findViewById(R.id.txv_item_dlg_order_ctrl_dispatch_property);
            viewHolder.errorDispatchPhone = (TextView) convertView.findViewById(R.id.txv_item_dlg_order_ctrl_dispatch_phone_no_response);
            viewHolder.errorDispatchEmbrace = (TextView) convertView.findViewById(R.id.txv_item_dlg_order_ctrl_dispatch_error_embrace);
            viewHolder.phone = (ImageButton) convertView.findViewById(R.id.imb_item_dlg_order_ctrl_phone);
            //暂时不显示
            viewHolder.errorDispatchEmbrace.setVisibility(View.GONE);
            viewHolder.errorDispatchPhone.setVisibility(View.GONE);
            //设置点击事件
            viewHolder.dispatchPersonal.setOnClickListener(mViewOnClick);
            viewHolder.dispatchProperty.setOnClickListener(mViewOnClick);
            viewHolder.errorDispatchEmbrace.setOnClickListener(mViewOnClick);
            viewHolder.errorDispatchPhone.setOnClickListener(mViewOnClick);
            viewHolder.phone.setOnClickListener(mViewOnClick);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.subOrderId.setText("订单号：" + item.getId().toString());
        viewHolder.receiverName.setText("收件人：" + item.getReceiverName());
        viewHolder.receiverAddress.setText("地址：" + item.getReceiverAddress());
        viewHolder.commodity.setText("商品类型：" + item.getCommodityId().toString());

        if(item.getState() != null && item.getState() != Dict.SUBORDER_STATE_DISPATCHING){
            viewHolder.dispatchProperty.setEnabled(false);
            viewHolder.dispatchPersonal.setEnabled(false);
            viewHolder.phone.setEnabled(false);
        }
        return convertView;
    }

    class ViewHolder{
        TextView subOrderId;
        TextView receiverName;
        TextView receiverAddress;
        TextView commodity;
        TextView dispatchPersonal;
        TextView dispatchProperty;
        TextView errorDispatchPhone;
        TextView errorDispatchEmbrace;
        ImageButton phone;
    }
}
