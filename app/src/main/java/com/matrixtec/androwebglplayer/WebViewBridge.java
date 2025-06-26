package com.matrixtec.androwebglplayer;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class WebViewBridge {

    private final Context context;
    private final WebView webView;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    /**
     * Constructor for the bridge.
     * @param context The application context.
     * @param webView The WebView to which this bridge is attached.
     */
    public WebViewBridge(Context context, WebView webView) {
        this.context = context;
        this.webView = webView;
    }

    /**
     * This is the single entry point for messages from JavaScript.
     * It is exposed to the WebView with the @JavascriptInterface annotation.
     * @param jsonString The JSON-formatted string message from the web app.
     */
    @JavascriptInterface
    public void postMessage(String jsonString) {
        try {
            JSONObject json = new JSONObject(jsonString);
            String action = json.optString("action");
            JSONObject data = json.optJSONObject("data");
            String callbackId = json.optString("callbackId", null);

            // Use a handler to run the action on the main UI thread.
            mainHandler.post(() -> {
                switch (action) {
                    case "showToast":
                        showToast(data);
                        break;
                    case "getDeviceInfo":
                        getDeviceInfo(callbackId);
                        break;
                    case "saveUser":
                        showData(data);
                        break;
                    case "onClose":
                        onclose();
                    // Add more actions here
                    default:
                        // Optionally handle unknown actions
                        break;
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
            // It's good practice to handle JSON parsing errors.
        }
    }
    private  void onclose()
    {

    }
    private void showToast(JSONObject data) {
        if (data == null) return;
        String message = data.optString("message", "No message");
        String durationStr = data.optString("duration", "short");
        int duration = "long".equals(durationStr) ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT;
        Log.d("data", message);
        Toast.makeText(context, message, duration).show();
    }
    private void showData(JSONObject data) {
        if (data == null) return;
        String message = data.optString("username", "No username");
        Log.d("data", message);

        //String durationStr = data.optString("duration", "short");
        //int duration = "long".equals(durationStr) ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT;
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
    private void getDeviceInfo(String callbackId) {
        if (callbackId == null) return;

        JSONObject deviceInfo = new JSONObject();
        try {
            deviceInfo.put("os", "Android");
            deviceInfo.put("version", Build.VERSION.SDK_INT);
            deviceInfo.put("manufacturer", Build.MANUFACTURER);
            deviceInfo.put("model", Build.MODEL);
        } catch (JSONException e) {
            // This is unlikely to happen with hardcoded keys, but good practice.
            e.printStackTrace();
        }

        // Send the result back to JavaScript using the callbackId.
        sendResponseToJs(callbackId, deviceInfo);
    }

    /**
     * Sends a response back to JavaScript for a specific callback.
     * @param callbackId The unique ID for the callback.
     * @param data The JSON data to send as the response.
     */
    private void sendResponseToJs(String callbackId, JSONObject data) {
        final String script = String.format("WebViewBridge._handleNativeCallback('%s', %s);", callbackId, data.toString());
        webView.evaluateJavascript(script, null);
    }

    /**
     * Dispatches a general event to the JavaScript side.
     * @param eventName The name of the event.
     * @param data The JSON data payload for the event.
     */
    public void dispatchEventToJs(String eventName, JSONObject data) {
        final String script = String.format("WebViewBridge._handleNativeEvent('%s', %s);", eventName, data.toString());
        webView.evaluateJavascript(script, null);
    }
}