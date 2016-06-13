package com.odison.saleview.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.odison.saleview.R;
import com.odison.saleview.api.remote.FuYangApi;
import com.odison.saleview.util.TLog;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class SaleViewActivity extends AppCompatActivity {
    Button btnTest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale_view);

        btnTest = (Button)findViewById(R.id.test);
        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FuYangApi.test("user", "password", "mac", mJsonTestHandler);
            }
        });

    }

    private JsonHttpResponseHandler mJsonTestHandler = new JsonHttpResponseHandler(){
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            TLog.log("SaleViewActivity:mJsonTestHandler:onSuccess", response.toString());
            super.onSuccess(statusCode, headers, response);
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            super.onFailure(statusCode, headers, responseString, throwable);
        }
    };
}
