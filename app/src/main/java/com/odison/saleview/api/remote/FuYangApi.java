package com.odison.saleview.api.remote;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.odison.saleview.api.ApiHttpClient;
import com.odison.saleview.base.BaseParameterNames;

/**
 * Created by PC-8 on 2016/6/13.
 */
public class FuYangApi {
    public static void test(String username, String password, String mac,
                             JsonHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put(BaseParameterNames.LOGIN_PHONE, username);
        params.put(BaseParameterNames.LOGIN_PSWD, password);
        params.put(BaseParameterNames.APP_MAC, mac);
        String loginUrl = "c=Batch&a=apitest";
        ApiHttpClient.post(loginUrl, params, handler);
    }
}
