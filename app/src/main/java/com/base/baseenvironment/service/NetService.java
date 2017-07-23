package com.base.baseenvironment.service;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.IBinder;

import com.base.baseenvironment.baseFragment.BaseWebFragment;


/**
     * 判断网络连接
     * @author Administrator
     *
     */
    public class NetService extends Service {
        BaseWebFragment.NetReceiver netReceiver;

        @Override
        public void onCreate() {
            super.onCreate();
            netReceiver = new BaseWebFragment.NetReceiver();
            IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
            registerReceiver(netReceiver, filter);
        }


        @Override
        public IBinder onBind(Intent arg0) {

            return null;
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
           unregisterReceiver(netReceiver);

        }

    }
