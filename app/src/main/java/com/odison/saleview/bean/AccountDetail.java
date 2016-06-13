package com.odison.saleview.bean;

import java.math.BigDecimal;
import java.util.Date;

public class AccountDetail  {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tbl_account_detail.id
     *
     * @mbggenerated
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tbl_account_detail.account_id
     *
     * @mbggenerated
     */
    private Integer accountId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tbl_account_detail.user_type
     *
     * @mbggenerated
     */
    private Integer userType;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tbl_account_detail.action_type
     *
     * @mbggenerated
     */
    private Integer actionType;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tbl_account_detail.before_balance
     *
     * @mbggenerated
     */
    private BigDecimal beforeBalance;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tbl_account_detail.after_balance
     *
     * @mbggenerated
     */
    private BigDecimal afterBalance;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tbl_account_detail.amount
     *
     * @mbggenerated
     */
    private BigDecimal amount;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tbl_account_detail.ins_time
     *
     * @mbggenerated
     */
    private Date insertTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tbl_account_detail.related_order_no
     *
     * @mbggenerated
     */
    private String relatedOrderNo;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tbl_account_detail.id
     *
     * @return the value of tbl_account_detail.id
     *
     * @mbggenerated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tbl_account_detail.id
     *
     * @param id the value for tbl_account_detail.id
     *
     * @mbggenerated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tbl_account_detail.account_id
     *
     * @return the value of tbl_account_detail.account_id
     *
     * @mbggenerated
     */
    public Integer getAccountId() {
        return accountId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tbl_account_detail.account_id
     *
     * @param accountId the value for tbl_account_detail.account_id
     *
     * @mbggenerated
     */
    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tbl_account_detail.user_type
     *
     * @return the value of tbl_account_detail.user_type
     *
     * @mbggenerated
     */
    public Integer getUserType() {
        return userType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tbl_account_detail.user_type
     *
     * @param userType the value for tbl_account_detail.user_type
     *
     * @mbggenerated
     */
    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tbl_account_detail.action_type
     *
     * @return the value of tbl_account_detail.action_type
     *
     * @mbggenerated
     */
    public Integer getActionType() {
        return actionType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tbl_account_detail.action_type
     *
     * @param actionType the value for tbl_account_detail.action_type
     *
     * @mbggenerated
     */
    public void setActionType(Integer actionType) {
        this.actionType = actionType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tbl_account_detail.before_balance
     *
     * @return the value of tbl_account_detail.before_balance
     *
     * @mbggenerated
     */
    public BigDecimal getBeforeBalance() {
        return beforeBalance;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tbl_account_detail.before_balance
     *
     * @param beforeBalance the value for tbl_account_detail.before_balance
     *
     * @mbggenerated
     */
    public void setBeforeBalance(BigDecimal beforeBalance) {
        this.beforeBalance = beforeBalance;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tbl_account_detail.after_balance
     *
     * @return the value of tbl_account_detail.after_balance
     *
     * @mbggenerated
     */
    public BigDecimal getAfterBalance() {
        return afterBalance;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tbl_account_detail.after_balance
     *
     * @param afterBalance the value for tbl_account_detail.after_balance
     *
     * @mbggenerated
     */
    public void setAfterBalance(BigDecimal afterBalance) {
        this.afterBalance = afterBalance;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tbl_account_detail.amount
     *
     * @return the value of tbl_account_detail.amount
     *
     * @mbggenerated
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tbl_account_detail.amount
     *
     * @param amount the value for tbl_account_detail.amount
     *
     * @mbggenerated
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tbl_account_detail.ins_time
     *
     * @return the value of tbl_account_detail.ins_time
     *
     * @mbggenerated
     */
    public Date getInsertTime() {
        return insertTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tbl_account_detail.ins_time
     *
     * @param insertTime the value for tbl_account_detail.ins_time
     *
     * @mbggenerated
     */
    public void setInsertTime(Date insertTime) {
        this.insertTime = insertTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tbl_account_detail.related_order_no
     *
     * @return the value of tbl_account_detail.related_order_no
     *
     * @mbggenerated
     */
    public String getRelatedOrderNo() {
        return relatedOrderNo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tbl_account_detail.related_order_no
     *
     * @param relatedOrderNo the value for tbl_account_detail.related_order_no
     *
     * @mbggenerated
     */
    public void setRelatedOrderNo(String relatedOrderNo) {
        this.relatedOrderNo = relatedOrderNo == null ? null : relatedOrderNo.trim();
    }
}