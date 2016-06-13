package com.odison.saleview.bean;

import com.baidu.mapapi.model.LatLng;

import java.util.List;

/**
 * Created by odison on 2016/1/25.
 */
public class MapRouteNode {
    private LatLng location;
    private String title;
    private Integer distance;

    public List<LatLng> getWayPoints() {
        return wayPoints;
    }

    public void setWayPoints(List<LatLng> wayPoints) {
        this.wayPoints = wayPoints;
    }

    private List<LatLng> wayPoints;

    private String exitInstructions;

    public String getEntranceInstructions() {
        return entranceInstructions;
    }

    public void setEntranceInstructions(String entranceInstructions) {
        this.entranceInstructions = entranceInstructions;
    }

    public String getExitInstructions() {
        return exitInstructions;
    }

    public void setExitInstructions(String exitInstructions) {
        this.exitInstructions = exitInstructions;
    }

    private String entranceInstructions;

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    private Integer duration;

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
