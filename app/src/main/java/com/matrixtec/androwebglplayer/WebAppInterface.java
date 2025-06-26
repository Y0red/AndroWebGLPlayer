package com.matrixtec.androwebglplayer;

import android.content.Context;
import android.content.Intent;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

public class WebAppInterface {
    Context mContext;

    /** Instantiate the interface and set the context. */
    WebAppInterface(Context c) {
        mContext = c;
    }

    /** Show a toast from the web page. */
    @JavascriptInterface
    public void showToast(String toast) {
        Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
    }

    @JavascriptInterface
    public  void closeWebApp() {
        Intent intent = new Intent(mContext, MainActivity.class);
        mContext.startActivity(intent);
        //MainActivity.getInstance().onCloseSpinner();
    }

    @JavascriptInterface
    public void tryAgain()
    {
        MainActivity.getInstance().close();
       // ((MainActivity)mContext).close();
    }
}
