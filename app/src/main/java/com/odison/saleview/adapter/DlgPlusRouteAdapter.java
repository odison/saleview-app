package com.odison.saleview.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.odison.saleview.R;
import com.odison.saleview.bean.MapRouteNode;
import com.odison.saleview.util.StringUtils;

import java.util.List;

/**
 * Created by odison on 2016/1/25.
 */
public class DlgPlusRouteAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<MapRouteNode> items;
    //private View.OnClickListener m

    public DlgPlusRouteAdapter(Context context, List<MapRouteNode> data){
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
        MapRouteNode item = items.get(position);
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_route_node, null);

            viewHolder.title = (TextView) convertView.findViewById(R.id.txv_item_route_node_title);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        String title = " "+position+" "+item.getTitle()+"[行驶 "+StringUtils.getDistance(item.getDistance())+"]";
        viewHolder.title.setText(title);

        return convertView;
    }
    class ViewHolder{
        TextView title;
    }

}
