package com.base.baseenvironment.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;

/**
 * Created by Administrator on 2017/4/4.
 */

public class MyUtils {

    private static long lastClickTime = 0;
    public static final long INTERVAL = 500L; //防止连续点击的时间间隔

    public static boolean isFastDoubleClick(Context context) {
        long time = System.currentTimeMillis();

        if (lastClickTime != 0 && ( time - lastClickTime ) < INTERVAL )
        {
            lastClickTime = time;
            ToastUtils.showToast(context,"双击退出应用！");
            return false;
        }
        lastClickTime = time;
        return true;
    }


    // webview 一些基本设置
    public static void setWebView(WebView web, Activity activity, String url) {
        WebSettings webSettings = web.getSettings();
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setJavaScriptEnabled(true);
        // 设置cookies
       // MyUtils.synCookies(activity, url);

      //  web.addJavascriptInterface(new JavaScriptinterface(activity), "android");
        web.getSettings().setSupportZoom(true);
        web.getSettings().setDomStorageEnabled(true);

        web.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        web.getSettings().setUseWideViewPort(true);
        web.getSettings().setLoadWithOverviewMode(true);
        web.getSettings().setSupportZoom(true);
        web.getSettings().setBuiltInZoomControls(true);
        web.getSettings().setAllowFileAccess(true);
        web.getSettings().setBlockNetworkImage(false);
        web.requestFocus();
        if (Build.VERSION.SDK_INT <= 18) {
            web.getSettings().setSavePassword(false);
        }

         web.loadUrl(url);
    }

    public static final String COOKIE_DOMAIN = ".qmlzsm.com";
    public static void syncCookie(String url, String cookie, Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            CookieSyncManager.createInstance(context);
        }
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setCookie(url, cookie);// 如果没有特殊需求，这里只需要将session
        // id以"key=value"形式作为cookie即可
        String newCookie = cookieManager.getCookie(url);
        String str = cookieManager.getCookie(COOKIE_DOMAIN);
        if (str != null && !str.isEmpty()) {
            String[] arr = str.split(";");

        }

    }


}
