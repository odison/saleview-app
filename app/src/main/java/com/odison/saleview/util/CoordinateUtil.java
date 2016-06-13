package com.odison.saleview.util;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;

/**
 * 地图坐标转换
 * GCJ02---BD09
 * <p/>
 * Created by odison on 2016/1/2.
 */
public class CoordinateUtil {
    public static LatLng GCJ02_TO_BD09(Double lat, Double lng) {
        return GCJ02_TO_BD09(new LatLng(lat, lng));
    }

    public static LatLng GCJ02_TO_BD09(Float lat, Float lng) {
        return GCJ02_TO_BD09(new LatLng(lat, lng));
    }

    public static LatLng GCJ02_TO_BD09(LatLng latLng) {
        // 将google地图、soso地图、aliyun地图、mapabc地图和amap地图// 所用坐标转换成百度坐标
        CoordinateConverter coordinateConverter = new CoordinateConverter();
        coordinateConverter.from(CoordinateConverter.CoordType.COMMON);
        // sourceLatLng待转换坐标
        coordinateConverter.coord(latLng);
        return coordinateConverter.convert();
    }
}
