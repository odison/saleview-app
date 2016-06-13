package com.odison.saleview.util;

import com.odison.saleview.bean.SubOrder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/1/7.
 */
public class TestUtil {
    public static List<SubOrder> getTestData() {
        List<SubOrder> list = new ArrayList<>();
        for (int i = 0; i != 10; ++i) {
            SubOrder subOrder = new SubOrder();
            subOrder.setId("1234567890123456780" + i);
            subOrder.setState(0);
            subOrder.setReceiverName("李四");
            subOrder.setReceiverAddress("中山西路292号");
            subOrder.setCommodityId(1);
            subOrder.setSubIndex(i);
            list.add(subOrder);
        }
        return list;
    }
}
