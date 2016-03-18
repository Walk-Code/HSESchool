package com.layout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

@SuppressLint("ClickableViewAccessibility")
public class CustomWebView extends WebView{

    @SuppressLint("NewApi")
    public CustomWebView(final Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        
        this.setFocusable(false);

        setWebChromeClient(new WebChromeClient());

        setWebViewClient(new WebViewClient() {
           @Override
		public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
               Toast.makeText(context, description, Toast.LENGTH_LONG).show();
           }

           @Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) { 
        	   //  重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
        	   return super.shouldOverrideUrlLoading(view, url);
           }
       });
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        // TODO Auto-generated method stub
        return false;
    }
}
