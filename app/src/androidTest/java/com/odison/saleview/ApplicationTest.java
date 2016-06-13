package com.odison.saleview;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.odison.saleview.api.remote.XiaoGeApi;
import com.odison.saleview.bean.Courier;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    public void test() throws Exception {
        final int expected = 1;
        final int reality = 1;
        assertEquals(expected, reality);
    }

    public void testApi() throws Exception{
        Courier courier = new Courier();
        courier.setPassword("111111");
        courier.setPhone("13511442500");
        courier.setId(1);
        courier.setName("111");
        XiaoGeApi.modifyInfo(courier,"1111111",new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
            }
        });
    }
}