package com.odison.saleview.bean;

import java.util.Date;

public class CourierGis  {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tbl_courier_gis.courier_id
     *
     * @mbggenerated
     */
    private Integer courierId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tbl_courier_gis.state
     *
     * @mbggenerated
     */
    private Integer state;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tbl_courier_gis.lng
     *
     * @mbggenerated
     */
    private Double lng;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tbl_courier_gis.lat
     *
     * @mbggenerated
     */
    private Double lat;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tbl_courier_gis.updtime
     *
     * @mbggenerated
     */
    private Date updateTime;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tbl_courier_gis.courier_id
     *
     * @return the value of tbl_courier_gis.courier_id
     *
     * @mbggenerated
     */
    public Integer getCourierId() {
        return courierId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tbl_courier_gis.courier_id
     *
     * @param courierId the value for tbl_courier_gis.courier_id
     *
     * @mbggenerated
     */
    public void setCourierId(Integer courierId) {
        this.courierId = courierId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tbl_courier_gis.state
     *
     * @return the value of tbl_courier_gis.state
     *
     * @mbggenerated
     */
    public Integer getState() {
        return state;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tbl_courier_gis.state
     *
     * @param state the value for tbl_courier_gis.state
     *
     * @mbggenerated
     */
    public void setState(Integer state) {
        this.state = state;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tbl_courier_gis.lng
     *
     * @return the value of tbl_courier_gis.lng
     *
     * @mbggenerated
     */
    public Double getLng() {
        return lng;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tbl_courier_gis.lng
     *
     * @param lng the value for tbl_courier_gis.lng
     *
     * @mbggenerated
     */
    public void setLng(Double lng) {
        this.lng = lng;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tbl_courier_gis.lat
     *
     * @return the value of tbl_courier_gis.lat
     *
     * @mbggenerated
     */
    public Double getLat() {
        return lat;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tbl_courier_gis.lat
     *
     * @param lat the value for tbl_courier_gis.lat
     *
     * @mbggenerated
     */
    public void setLat(Double lat) {
        this.lat = lat;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tbl_courier_gis.updtime
     *
     * @return the value of tbl_courier_gis.updtime
     *
     * @mbggenerated
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tbl_courier_gis.updtime
     *
     * @param updateTime the value for tbl_courier_gis.updtime
     *
     * @mbggenerated
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}