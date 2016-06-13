package com.odison.saleview.bean;

/**
 * Created by odison on 2016/1/1.
 */
public class SubOrderEx extends SubOrder {
    private Double lat;
    private Double lng;

    public SubOrderEx(SubOrder subOrder){
        setId(subOrder.getId());
        setArrivedTime(subOrder.getArrivedTime());
        setCommodityId(subOrder.getCommodityId());
        setCreateTime(subOrder.getCreateTime());
        setEmbraceTime(subOrder.getEmbraceTime());
        setMainOrderId(subOrder.getMainOrderId());
        setMerchantId(subOrder.getMerchantId());
        setMerchantLocationId(subOrder.getMerchantLocationId());
        setMerchantOrderId(subOrder.getMerchantOrderId());
        setPackTime(subOrder.getPackTime());
        setReceiverAddress(subOrder.getReceiverAddress());
        setReceiverName(subOrder.getReceiverName());
        setReceiverPhone(subOrder.getReceiverPhone());
        setState(subOrder.getState());
        setSubIndex(subOrder.getSubIndex());
        setTakeTime(subOrder.getTakeTime());
    }
    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public Double distance;
}
