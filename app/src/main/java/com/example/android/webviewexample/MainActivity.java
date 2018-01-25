package com.example.android.webviewexample;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

public class MainActivity extends AppCompatActivity {

    private static final String LOAD_HTML_CODE = "loadHTMLCode";
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();

        int htmlCode = intent.getIntExtra(LOAD_HTML_CODE, 0);


        webView = (WebView) findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new WebAppInterface(this), "Android");

        loadHTML(htmlCode);
    }

    public class WebAppInterface {
        Context mContext;

        /**
         * Instantiate the interface and set the context
         */
        WebAppInterface(Context c) {
            mContext = c;
        }

        /**
         * Show a notification from the web page
         */
        @JavascriptInterface
        public void showNotification(String firstName, String lastName, String textBox) {
            Intent refreshActivity = new Intent(MainActivity.this, MainActivity.class);
            /*Handle unfinished form by throwing error message*/
            if (firstName.isEmpty() || firstName == null || lastName.isEmpty() || lastName == null) {
                refreshActivity.putExtra(LOAD_HTML_CODE, 1);
            } else {
                refreshActivity.putExtra(LOAD_HTML_CODE, 2);
            }

            android.support.v4.app.NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(getApplicationContext())
                            .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                            .setContentTitle("Form submitted!")
                            .setContentText("Name: " + firstName + " " + lastName + ", Details: " + textBox);

            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            mNotificationManager.notify(001, mBuilder.build());


            startActivity(refreshActivity);
        }
    }

    public void loadHTML(int msg) {
        switch(msg) {
            case 0:
                webView.loadUrl("file:///android_asset/blank.html");
                break;
            case 1:
                webView.loadUrl("file:///android_asset/error.html");
                break;
            case 2:
                webView.loadUrl("file:///android_asset/success.html");
                break;
            default:
                webView.loadUrl("file:///android_asset/error.html");
        }
    }
}