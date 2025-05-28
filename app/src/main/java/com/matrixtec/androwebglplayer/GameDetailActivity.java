package com.matrixtec.androwebglplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import android.app.ActionBar;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.net.URI;
import java.net.URISyntaxException;

public class GameDetailActivity extends AppCompatActivity {

    private WebView gameWebView;
    private WebSettings webSettings;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        setContentView(R.layout.activity_game_detail);
        gameWebView = findViewById(R.id.gameWebView);
        progressBar = findViewById(R.id.progressBar);

        gameWebView  =  new WebView(this);
        gameWebView.addJavascriptInterface(new WebAppInterface(this), "Android");
        setContentView(gameWebView);


        webSettings = gameWebView.getSettings();


        gameWebView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView webView, String url){
                progressBar.setVisibility(View.GONE);
            }
        });

        gameWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress){
                progressBar.setProgress(newProgress, true);
                if (newProgress < 100 && progressBar.getVisibility() == View.GONE) {
                    progressBar.setVisibility(View.VISIBLE);
                }
            }
        });


        String gameLink = getIntent().getStringExtra("gameLink");
        String gameName = getIntent().getStringExtra("gameName");
        String gameOrientation = getIntent().getStringExtra("gameOrientation");

        if (gameLink != null && !gameLink.isEmpty() && isUriSecure(gameLink) && isUriTrusted(gameLink, "games.fun.et")) {
            if(gameOrientation.equals("landscape"))setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            else setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

            webSettings.setJavaScriptEnabled(true);
            //gameWebView.loadData(EncodeHTML(Maccle(gameLink)), "text/html", "base64");
            gameWebView.loadUrl(gameLink);
            Toast.makeText(GameDetailActivity.this, "Starting Game:- "+ gameName, Toast.LENGTH_SHORT).show();
        } else {
            // Handle case where game link is missing
            Toast.makeText(GameDetailActivity.this, "Empty Link", Toast.LENGTH_SHORT).show();
            webSettings.setJavaScriptEnabled(false);
            finish(); // Close the activity
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (gameWebView != null) {
            webSettings.setJavaScriptEnabled(false);
            gameWebView.destroy();
            finish();
        }
    }
    public boolean isUriSecure(String url) {
        try{
            URI uri = new URI(url);
            String scheme = uri.getScheme();
            if ("https".equalsIgnoreCase(scheme)) { // Enforce HTTPS scheme for security
                return true;
            } else {
                // Log an error or handle the case where the URL is not secure
                System.err.println("Attempted to load a non-HTTPS URL: " + url);
                return false;
            }
        }catch (URISyntaxException e) {
            // Handle exception for improper URL format
            System.err.println("Invalid URL syntax: " + url);
            return false;
        }
    }
    public boolean isUriTrusted(String incomingUri, String trustedHostName) throws NullPointerException {
        try {
            Uri uri = Uri.parse(incomingUri);
            return uri.getScheme().equals("https") &&
                    uri.getHost().equals(trustedHostName);
        } catch (NullPointerException e) {
            Toast.makeText(GameDetailActivity.this, "incomingUri is null or not well-formed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            throw new NullPointerException("incomingUri is null or not well-formed");
        }
    }
    public String EncodeHTML(@NonNull String html) { return Base64.encodeToString(html.getBytes(), Base64.NO_PADDING);}
    public String BaseHTML() {return "<html><body>'%23' is the percent code for ‘#‘ </body></html>";}
    public  String Maccle(String url) {
        return  "<!DOCTYPE html>\n" +
                "<html lang=\"en\">"+
                "  <head>\n" +
                "    <meta charset=\"UTF-8\" />\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" />\n" +
                "  </head>"+
                "  <body>\n" +
                "    <h1>Parent Window</h1>\n" +
                "<input type=\"button\" value=\"Say hello\" onClick=\"closeGame()\" />"+
                "    <!-- Embed the iframe that contains the h5game.html page -->\n" +
                "    <iframe\n" +
                "      id=\"game-iframe\"\n" +
                "      src="+ url +
                "      width=\"100%\"\n" +
                "      height=\"90%\n"+
                "      frameborder=\"0\"\n" +
                "allowFullScreen"+
                "    ></iframe>\n" +
                "\n" +
                "    <script type=\"text/javascript\">\n" +
                "function showAndroidToast(toast) {\n" +
                "        Android.showToast(toast);} "+
                "const closeGame = () => {\n" +
                "    const iframe = document.getElementById('game-iframe');\n" +
                "    iframe.src = '';\n" +
                "    iframe.style.display = 'none';\n" +
                "    \n" +
                "    // Remove class from body to allow scrolling\n" +
                "    document.body.style.overflow = 'auto';\n" +
                "Android.closeWebApp()"+
                "  };\n"+
                "    </script>\n" +
                "    \n" +
                "  </body>\n"+
                "</html>\n";
    }

    public  String Mac() {
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "  <head>\n" +
                "    <meta charset=\"UTF-8\" />\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=2.0\" />\n" +
                "    <title>Parent Window</title>\n" +
                "  </head>\n" +
                "  <body>\n" +
                "    <h1>Parent Window</h1>\n" +
                "    <!-- Embed the iframe that contains the h5game.html page -->\n" +
                "    <iframe\n" +
                "      id=\"gameIframe\"\n" +
                "      src=\"https://games.fun.et/merge2048/index.html\"\n" +
                "      width=\"100%\"\n" +
                "      height=\"100%\"\n" +
                "    ></iframe>\n" +
                "\n" +
                "    <script type=\"text/javascript\">\n" +
                "      // Handle payment response callback (already provided)\n" +
                "      function handleinitDataCallback(response) { \n" +
                "        try {\n" +
                "          console.debug(\"[Parent] Payment callback received:\", response);\n" +
                "          const responseData = JSON.parse(response);\n" +
                "    \n" +
                "          // Send the response back to the game iframe\n" +
                "          const gameIframe = document.getElementById(\"gameIframe\");\n" +
                "          if (gameIframe && gameIframe.contentWindow) {\n" +
                "            gameIframe.contentWindow.postMessage(\n" +
                "              {\n" +
                "                type: \"PAYMENT_SUCCESS\",\n" +
                "                data: responseData,\n" +
                "              },\n" +
                "              \"*\"\n" +
                "            );\n" +
                "            console.debug(\"[Parent] Payment response sent to game\");\n" +
                "          } else {\n" +
                "            console.error(\"[Parent] Game iframe not found\");\n" +
                "          }\n" +
                "        } catch (error) {\n" +
                "          console.error(\"[Parent] Error handling payment callback:\", error);\n" +
                "        }\n" +
                "      }\n" +
                "    \n" +
                "      // Listen for messages from the iframe\n" +
                "      window.addEventListener(\"message\", function (event) {\n" +
                "        // Skip messages from React DevTools\n" +
                "        if (event.data.source === \"react-devtools-content-script\") {\n" +
                "          return;\n" +
                "        }\n" +
                "    \n" +
                "        // Log the received message\n" +
                "        console.debug(\"[Parent] Message received from iframe:\", event.data);\n" +
                "        \n" +
                "        // Log \"it is coming\" when a message is received\n" +
                "        console.log(\"[parent] connections\");\n" +
                "\n" +
                "        // Example: Trigger the payment response simulation\n" +
                "        handleinitDataCallback(\n" +
                "          '{\"result\":\"PAYMENT_SUCCESS\",\"resultCode\":\"1\",\"transactionTime\":\"TEST_TIME\",\"amount\":300,\"prepayId\":\"prepayId\", \"data\": {\"message\": \"Payment successful\"}}'\n" +
                "        );\n" +
                "      });\n" +
                "    </script>\n" +
                "    \n" +
                "  </body>\n" +
                "</html>\n";
    }
}