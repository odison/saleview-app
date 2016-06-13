package com.odison.saleview.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.odison.saleview.AppConfig;
import com.odison.saleview.AppContext;
import com.odison.saleview.R;
import com.odison.saleview.api.ApiHttpClient;
import com.odison.saleview.api.remote.XiaoGeApi;
import com.odison.saleview.base.BaseParameterNames;
import com.odison.saleview.bean.User;
import com.odison.saleview.util.TLog;
import com.odison.saleview.util.ToastMessageTask;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ChangePwdActivity extends AppCompatActivity {

    //ui
    private EditText mEdtOldPswd;
    private EditText mEdtNewPswd;
    private EditText mEdtNewPswdConfirm;

    private View mProgressView;
    private View mSubmitFormView;

    private Button mBtnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pwd);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_back);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO go back
                onBackPressed();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initView();


    }

    private void initView(){
        mEdtOldPswd = (EditText) findViewById(R.id.edt_old_pswd);
        mEdtNewPswd = (EditText) findViewById(R.id.edt_new_pwd);
        mEdtNewPswdConfirm = (EditText) findViewById(R.id.edt_new_pwd_confirm);

        mBtnSubmit = (Button) findViewById(R.id.btn_changepwd);
        mBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptSubmit();
            }
        });

        mProgressView =  findViewById(R.id.pcb_changepwd);
        mSubmitFormView = findViewById(R.id.changepwd_form);
    }

    private void attemptSubmit(){
        if(ApiHttpClient.getHttpClient() == null){
            return;
        }

        mEdtOldPswd.setError(null);
        mEdtNewPswd.setError(null);
        mEdtNewPswdConfirm.setError(null);

        String oldPswd = mEdtOldPswd.getText().toString();
        String newPswd = mEdtNewPswd.getText().toString();
        String newPswdConfirm = mEdtNewPswdConfirm.getText().toString();

        User user = AppContext.getInstance().getLocalUser();

        View focusView = null;
        boolean cancel = false;
        if(TextUtils.isEmpty(oldPswd)){
            mEdtOldPswd.setError(getString(R.string.error_field_required));
            focusView = mEdtOldPswd;
            cancel = true;
        } else if(!user.getPassword().equals(oldPswd)){
            mEdtOldPswd.setError(getString(R.string.error_changepwd_oldpwd_not_equal));
            focusView = mEdtNewPswd;
            cancel = true;
        }

        if(!TextUtils.isEmpty(newPswd) && !isPasswordValid(newPswd)){
            mEdtNewPswd.setError(getString(R.string.error_changepwd_password_to_short));
            focusView = mEdtNewPswd;
            cancel = true;
        }

        if(oldPswd.equals(newPswd)){
            mEdtNewPswd.setError(getString(R.string.error_changepwd_newpwd_equal_old));
            focusView = mEdtNewPswd;
            cancel = true;
        }

        if(!newPswd.equals(newPswdConfirm)){
            mEdtNewPswdConfirm.setError(getString(R.string.error_changepwd_password_confrim_input_not_equal));
            focusView = mEdtNewPswdConfirm;
            cancel = true;
        }


        if(cancel){
            focusView.requestFocus();
        }else{
            showProgress(true);
            XiaoGeApi.changePwd(AppConfig.app_mac,oldPswd,newPswd,mJsonHandler);
        }


    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mSubmitFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mSubmitFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mSubmitFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mSubmitFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }


    private JsonHttpResponseHandler mJsonHandler = new JsonHttpResponseHandler(){
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            TLog.log("changepwd",response.toString());
            try {
                if(response.getString(BaseParameterNames.JSON_RESPONSE_STATE).equals("true")){


                    AppContext.getInstance().updatePassword(mEdtNewPswd.getText().toString());
                    showProgress(false);
                    finish();
                }else{
                    showProgress(false);
                    new ToastMessageTask().execute("更改密码失败");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            showProgress(false);
            new ToastMessageTask().execute(getString(R.string.error_login_network));
            TLog.log("ChangePwdActivity:remote:",getString(R.string.error_login_network));
        }
    };

}
