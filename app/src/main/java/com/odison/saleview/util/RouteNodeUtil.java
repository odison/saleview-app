package com.odison.saleview.util;

import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.route.DrivingRouteLine;
import com.odison.saleview.bean.MapRouteNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by odison on 2016/1/25.
 */
public class RouteNodeUtil {
    public static List<MapRouteNode> getRouteNodes(RouteLine routeLine) {
        List<MapRouteNode> mapRouteNodes = new ArrayList<>();
        if (routeLine == null || routeLine.getAllStep().size() == 0)
            return mapRouteNodes;
        for(int i = 0;i!= routeLine.getAllStep().size();++i){
            MapRouteNode mapRouteNode = new MapRouteNode();
            DrivingRouteLine.DrivingStep step = (DrivingRouteLine.DrivingStep)routeLine.getAllStep().get(i);
            //获取数据
            mapRouteNode.setTitle(step.getInstructions());
            mapRouteNode.setLocation(step.getEntrance().getLocation());
            mapRouteNode.setDistance(step.getDistance());
            mapRouteNode.setDuration(step.getDuration());
            mapRouteNode.setExitInstructions(step.getExitInstructions());
            mapRouteNode.setEntranceInstructions(step.getEntranceInstructions());
            mapRouteNode.setWayPoints(step.getWayPoints());
            mapRouteNodes.add(mapRouteNode);
        }
        return mapRouteNodes;
    }
}
