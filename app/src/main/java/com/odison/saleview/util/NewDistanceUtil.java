package com.odison.saleview.util;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;

/**
 *
 *
 * Created by odison on 2016/1/8.
 */
public class NewDistanceUtil {
    public static Double getDistance(LatLng start,LatLng end){
        start = CoordinateUtil.GCJ02_TO_BD09(start);
        end = CoordinateUtil.GCJ02_TO_BD09(end);
        return DistanceUtil.getDistance(start,end);
    }
}
