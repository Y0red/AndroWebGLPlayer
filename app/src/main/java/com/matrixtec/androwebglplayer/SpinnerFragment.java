package com.matrixtec.androwebglplayer;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.webkit.WebViewAssetLoader;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SpinnerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SpinnerFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Context mContext;

    public SpinnerFragment(Context context) {
        // Required empty public constructor
        mContext = context;
    }
///  ///////////////////////////////
///
public interface SpinnerListener {
    public void onCloseSpinner();
}
    public SpinnerListener listener;

    public static SpinnerFragment newInstance(String param1, String param2, Context c) {
        SpinnerFragment fragment = new SpinnerFragment(c);
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_spinner, container, false);

        listener = (SpinnerListener) mContext;

        WebView webView = (WebView) view.findViewById(R.id.spinnerWebView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAllowFileAccessFromFileURLs(true); //Maybe you don't need this rule
        webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        webView.addJavascriptInterface(new WebViewBridge(mContext, webView), "AndroidBridge");

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl){
                webView.loadUrl("file:///android_asset/SpinnerNew/index.html");
                Toast.makeText(mContext, description, Toast.LENGTH_SHORT).show();
            }
        });
        webView.loadUrl("file:///android_asset/bridge/index.html");
       // webView.loadUrl("https://games.fun.et/2048test/");

       // Button close =  (Button) view.findViewById(R.id.closeButton);
       // close.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View view) {
        //        listener.onCloseSpinner();
         //   }
       // });

        Toast.makeText(mContext, "Opening Spinner...", Toast.LENGTH_SHORT).show();
        return view;
    }
}