package com.odison.saleview.util;

import android.os.AsyncTask;
import android.widget.Toast;

import com.odison.saleview.AppContext;

/**
 * 非UI线程调用toast
 *
 * Created by odison on 2015/12/15.
 */
// A class that will run Toast messages in the main GUI context
public class ToastMessageTask extends AsyncTask<String, String, String> {
    String toastMessage;

    @Override
    protected String doInBackground(String... params) {
        toastMessage = params[0];
        return toastMessage;
    }

    protected void OnProgressUpdate(String... values) {
        super.onProgressUpdate(values);
    }
    // This is executed in the context of the main GUI thread
    protected void onPostExecute(String result){
        Toast toast = Toast.makeText(AppContext.getInstance(), result, Toast.LENGTH_SHORT);
        toast.show();
    }
}