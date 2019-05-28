package com.example.cosmos;


import android.content.Context;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private WebView browser = null;
    Button back, fwd, refresh;
    SearchView searchView = null;

    ProgressBar pgBar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(!isConnected(MainActivity.this)) {
    Intent noInternetIntent =new Intent(getApplicationContext(),NoInternetActivity.class);
    startActivity(noInternetIntent);

        }

        else {



            setContentView(R.layout.activity_main);
            pgBar = (ProgressBar) findViewById(R.id.progress_id);
            searchView = (SearchView) findViewById(R.id.searchViewId);
            browser = (WebView) findViewById(R.id.brow_id);
            back = (Button) findViewById(R.id.back_id);
            fwd = (Button) findViewById(R.id.fwd_id);
            refresh = (Button) findViewById(R.id.refresh_id);
            browser.getSettings().setJavaScriptEnabled(true);

            browser.setWebViewClient(new MyWebViewClient());
            browser.clearCache(true);
            browser.clearHistory();
            browser.getSettings().setJavaScriptEnabled(true);
            browser.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
            browser.getSettings().setBuiltInZoomControls(true);

            browser.loadUrl("https://www.google.com");
            browser.setWebChromeClient(new WebChromeClient() {
                @Override
                public void onProgressChanged(WebView view, int newProgress) {
                    pgBar.setProgress(newProgress);
                    if (newProgress == 100)
                        pgBar.setVisibility(View.GONE);
                    else
                        pgBar.setVisibility(View.VISIBLE);
                }
            });

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {


                    String editTextValue = searchView.getQuery().toString();
                    if (!editTextValue.startsWith("http://"))
                        editTextValue = "http://" + editTextValue;

                    String url = editTextValue;
                    browser.loadUrl(url);

                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);//To initialze the keyboard
                    inputMethodManager.hideSoftInputFromWindow(searchView.getWindowToken(), 0);//to Hide it


                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });

            //This is the event handling code for forward Button
            fwd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (browser.canGoForward()) {
                        browser.goForward();
                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(), "No Forward Page is available", Toast.LENGTH_SHORT);
                        toast.setMargin(50, 50);
                        toast.show();
                    }
                }
            });
            //This is the event handling code for back Button
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (browser.canGoBack()) {
                        browser.goBack();
                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(), "No previous Page is available", Toast.LENGTH_SHORT);
                        toast.setMargin(50, 50);
                        toast.show();
                    }
                }
            });
            refresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    browser.reload();
                }
            });
        }

    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            browser.loadUrl(url);
            return true;
        }
    }

    public boolean isConnected(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();

        if (netinfo != null && netinfo.isConnectedOrConnecting()) {
            android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if((mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting())) return true;
        else return false;
        } else
        return false;
    }


}



