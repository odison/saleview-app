package com.odison.saleview.base;

import com.odison.saleview.R;

/**
 * Created by odison on 2015/12/23.
 */
public class Dict {

    public static final int DEFAULT_PAGE_SIZE = 5;
    public static final int PAGE_SIZE_MAX = 20;

    public static final int MAINORDER_STATE_CREATE = 0;
    public static final int MAINORDER_STATE_PUSHED = 1;
    public static final int MAINORDER_STATE_CATCHED = 2;
    public static final int MAINORDER_STATE_DISPATCHING = 3;
    public static final int MAINORDER_STATE_FINISHED = 4;

    public static final int COURIER_GIS_STATE_WAIT = 0;
    public static final int COURIER_GIS_STATE_SENDING = 1;
    public static final int COURIER_GIS_STATE_TAKEN = 2;
    public static final int COURIER_GIS_STATE_FINISHED = 3;
    public static final int COURIER_GIS_STATE_REST = 4;//添加快递员休息状态，此状态不接单

    public static final int ACCOUNT_DETAIL_ACTION_CHARGE = 1; // 充值
    public static final int ACCOUNT_DETAIL_ACTION_ENCASH = 2; // 提现
    public static final int ACCOUNT_DETAIL_ACTION_PAY = 3; // 订单发布扣款
    public static final int ACCOUNT_DETAIL_ACTION_INCOME = 4; // 订单配送收入


    public static final int SUBORDER_STATE_DRAFT = 0;
    public static final int SUBORDER_STATE_PUBLISHED = 1;
    public static final int SUBORDER_STATE_PACKAGED = 2;
    public static final int SUBORDER_STATE_CATCHED = 3;
    public static final int SUBORDER_STATE_DISPATCHING = 4;
    public static final int SUBORDER_STATE_DISPATCHED_PROPERTY = 5;
    public static final int SUBORDER_STATE_DISPATCHED_PERSONAL = 6;

    public static final int APP_STATE_UNLOGIN = 0;
    public static final int APP_STATE_LOGIN = 1;
    public static final int APP_STATE_GETINFO = 2;
    public static final int APP_STATE_GETORDERLIST = 3;

    //Marker 图标集合
    public static final Integer[] MARKER_BITMAPS = {R.drawable.icon_mark1, R.drawable.icon_mark2, R.drawable.icon_mark3, R.drawable.icon_mark4,
            R.drawable.icon_mark5, R.drawable.icon_mark6, R.drawable.icon_mark7, R.drawable.icon_mark8,
            R.drawable.icon_mark9, R.drawable.icon_mark10, R.drawable.icon_mark11, R.drawable.icon_mark12,
            R.drawable.icon_mark13, R.drawable.icon_mark14, R.drawable.icon_mark15, R.drawable.icon_mark16,
            R.drawable.icon_mark17, R.drawable.icon_mark18, R.drawable.icon_mark19, R.drawable.icon_mark20,
            R.drawable.icon_mark21, R.drawable.icon_mark22, R.drawable.icon_mark23, R.drawable.icon_mark24,
            R.drawable.icon_mark25, R.drawable.icon_mark26, R.drawable.icon_mark27, R.drawable.icon_mark28,
            R.drawable.icon_mark29, R.drawable.icon_mark30, R.drawable.icon_mark31, R.drawable.icon_mark32,
            R.drawable.icon_mark33, R.drawable.icon_mark34, R.drawable.icon_mark35, R.drawable.icon_mark36,
            R.drawable.icon_mark37, R.drawable.icon_mark38, R.drawable.icon_mark39, R.drawable.icon_mark40,
            R.drawable.icon_mark41, R.drawable.icon_mark42, R.drawable.icon_mark43, R.drawable.icon_mark44,
            R.drawable.icon_mark45, R.drawable.icon_mark46, R.drawable.icon_mark47, R.drawable.icon_mark48,
            R.drawable.icon_mark49, R.drawable.icon_mark50, R.drawable.icon_mark51, R.drawable.icon_mark52,
            R.drawable.icon_mark53, R.drawable.icon_mark54, R.drawable.icon_mark55, R.drawable.icon_mark56,
            R.drawable.icon_mark57, R.drawable.icon_mark58, R.drawable.icon_mark59, R.drawable.icon_mark60,
            R.drawable.icon_mark61, R.drawable.icon_mark62, R.drawable.icon_mark63, R.drawable.icon_mark64,
            R.drawable.icon_mark65, R.drawable.icon_mark66, R.drawable.icon_mark67, R.drawable.icon_mark68,
            R.drawable.icon_mark69, R.drawable.icon_mark70, R.drawable.icon_mark71, R.drawable.icon_mark72,
            R.drawable.icon_mark73, R.drawable.icon_mark74, R.drawable.icon_mark75, R.drawable.icon_mark76,
            R.drawable.icon_mark77, R.drawable.icon_mark78, R.drawable.icon_mark79, R.drawable.icon_mark80,
            R.drawable.icon_mark81, R.drawable.icon_mark82, R.drawable.icon_mark83, R.drawable.icon_mark84,
            R.drawable.icon_mark85, R.drawable.icon_mark86, R.drawable.icon_mark87, R.drawable.icon_mark88,
            R.drawable.icon_mark89, R.drawable.icon_mark90, R.drawable.icon_mark91, R.drawable.icon_mark92,
            R.drawable.icon_mark93, R.drawable.icon_mark94, R.drawable.icon_mark95, R.drawable.icon_mark96,
            R.drawable.icon_mark97, R.drawable.icon_mark98, R.drawable.icon_mark99
    };


}
