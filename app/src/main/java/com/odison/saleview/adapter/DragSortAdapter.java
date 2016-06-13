package com.odison.saleview.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.odison.saleview.R;
import com.odison.saleview.base.Constants;
import com.odison.saleview.bean.SubOrder;

import java.util.List;

/**
 * DragSortListView adapter
 * 跟通常的listview adapter区别就是
 * remove 跟 insert 两个函数
 * 当拖动的时候要先将item从队列中删除，然后再相应的地方作插入
 * 类比链表的操作
 * <p/>
 * Created by odison on 2016/1/5.
 */
public class DragSortAdapter extends BaseAdapter {

    private Context context;
    private List<SubOrder> items;

    private LayoutInflater mInflater;

    public boolean isModify() {
        return modify;
    }

    public void setModify(boolean modify) {
        this.modify = modify;
    }

    private boolean modify;

    public DragSortAdapter(Context context, List<SubOrder> data) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
        this.items = data;
        this.modify = false;
    }

    public List<SubOrder> getItems() {
        return items;
    }

    public void setItems(List<SubOrder> data) {
        items = data;
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
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_mysuborder, null);

            viewHolder.subOrderId = (TextView) convertView.findViewById(R.id.txv_item_mysub_id);
            viewHolder.receiverName = (TextView) convertView.findViewById(R.id.txv_item_mysub_receiver_name);
            viewHolder.receiverAddress = (TextView) convertView.findViewById(R.id.txv_item_mysub_receiver_address);
            viewHolder.commodity = (TextView) convertView.findViewById(R.id.txv_item_mysub_commodity);
            viewHolder.subOrderState = (TextView) convertView.findViewById(R.id.txv_item_mysub_state);
            viewHolder.dragHandler = (ImageView) convertView.findViewById(R.id.drag_handle);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.subOrderId.setText("订单号：" + items.get(position).getId().toString());
        viewHolder.receiverName.setText("收件人：" + items.get(position).getReceiverName());
        viewHolder.receiverAddress.setText("地址：" + items.get(position).getReceiverAddress());
        viewHolder.commodity.setText("商品类型：" + items.get(position).getCommodityId().toString());
        viewHolder.subOrderState.setText("状态：" + Constants.SUBORDER_STATE_DESCRIBE[items.get(position).getState() % Constants.SUBORDER_STATE_DESCRIBE.length]);


        return convertView;
    }

    public void remove(int position) {//删除指定位置的item
        modify = true;
        items.remove(position);
        this.notifyDataSetChanged();//不要忘记更改适配器对象的数据源
    }

    public void insert(SubOrder item, int position) {
        items.add(position, item);
        this.notifyDataSetChanged();
    }

    class ViewHolder {
        TextView subOrderId;
        TextView receiverName;
        TextView receiverAddress;
        TextView commodity;
        TextView subOrderState;
        ImageView dragHandler;
    }
}
