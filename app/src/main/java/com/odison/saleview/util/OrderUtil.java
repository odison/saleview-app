package com.odison.saleview.util;

import com.baidu.mapapi.model.LatLng;
import com.odison.saleview.base.Dict;
import com.odison.saleview.bean.MainOrder;
import com.odison.saleview.bean.SubOrder;
import com.odison.saleview.bean.SubOrderEx;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by odison on 2016/1/9.
 */
public class OrderUtil {
    public static String getSubIndexs(List<SubOrder> subOrderList) {
        if (subOrderList.size() == 0)
            return null;
        String sortIndexs = subOrderList.get(0).getId();
        for (int i = 1; i != subOrderList.size(); ++i) {
            sortIndexs = sortIndexs + "," + subOrderList.get(i).getId();
        }
        return sortIndexs;
    }

    public static Boolean isOrderFinished(MainOrder mainOrder) {
        if (mainOrder.getId() != null && mainOrder.getSubCount().equals(mainOrder.getSubFinishedCount())) {
            return true;
        } else {
            return false;
        }
    }


    public static Integer getPosition(List<SubOrder> list, SubOrderEx subOrderEx) {
        int position = -1;
        if (subOrderEx == null || subOrderEx.getId() == null)
            return position;
        for (int i = 0; i != list.size(); ++i) {
            if (list.get(i).getId() == subOrderEx.getId()) {
                position = i;
                break;
            }
        }
        return position;
    }

    public static Integer getPositionEx(List<SubOrderEx> list, SubOrderEx subOrderEx) {
        int position = -1;
        if (subOrderEx == null || subOrderEx.getId() == null)
            return position;
        for (int i = 0; i != list.size(); ++i) {
            if (list.get(i).getId() == subOrderEx.getId()) {
                position = i;
                break;
            }
        }
        return position;
    }

    public static List<SubOrderEx> getUnfinishedSubOrderExs(List<SubOrderEx> list, Integer iCursor) {
        List<SubOrderEx> data = new ArrayList<>();
//        Collections.sort(list);
//        for (SubOrderEx subOrderEx : list) {
//            if(subOrderEx.getState() == Dict.SUBORDER_STATE_DISPATCHING ||
//                    subOrderEx.getState() == Dict.SUBORDER_STATE_CATCHED){
//                data.add(subOrderEx);
//            }
//        }
        int iMax = list.size();
        for (int i = iCursor; i != iCursor + iMax; ++i) {
            SubOrderEx subOrderEx = list.get(i % iMax);
            if (subOrderEx.getState() == Dict.SUBORDER_STATE_DISPATCHING ||
                    subOrderEx.getState() == Dict.SUBORDER_STATE_CATCHED) {
                data.add(subOrderEx);
            }
        }
        return data;
    }

    public static List<SubOrderEx> getSubOrderExList(List<SubOrder> subOrders, Map<String, LatLng> map) {
        List<SubOrderEx> list = new ArrayList<>();
        if (subOrders.size() == 0 || map.size() == 0)
            return list;
        for (SubOrder subOrder : subOrders) {
            SubOrderEx subOrderEx = new SubOrderEx(subOrder);
            subOrderEx.setLat(map.get(subOrder.getId()).latitude);
            subOrderEx.setLng(map.get(subOrder.getId()).longitude);
            list.add(subOrderEx);
        }
        return list;
    }

    public static Boolean haveUnfinished(List<SubOrderEx> subOrderExes){
        Boolean unfinished = false;
        if(subOrderExes.size() == 0) return unfinished;
        for (SubOrderEx subOrderEx:subOrderExes
             ) {
            if(subOrderEx!= null && subOrderEx.getState() != null &&
                    subOrderEx.getState() != Dict.SUBORDER_STATE_DISPATCHED_PERSONAL
                    && subOrderEx.getState() != Dict.SUBORDER_STATE_DISPATCHED_PROPERTY){
                unfinished = true;
                break;
            }
        }
        return unfinished;
    }

    public static Boolean isSubOrderUnfinished(SubOrderEx subOrderEx){
        Boolean unfinished = false;
        if(subOrderEx != null && subOrderEx.getState() != null
                && subOrderEx.getState() != Dict.SUBORDER_STATE_DISPATCHED_PERSONAL
                && subOrderEx.getState() != Dict.SUBORDER_STATE_DISPATCHED_PROPERTY){
            unfinished = true;
        }
        return unfinished;
    }
}
