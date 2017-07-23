package com.base.baseenvironment.baseActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.base.baseenvironment.MyApplication;
import com.base.baseenvironment.R;
import com.base.baseenvironment.baseFragment.BaseFragment;

public abstract class TabHostActivity extends BaseActivity {

    public static FragmentTabHost mTabHost;
    public static int id = 0;
    @Override
    protected int getContentViewId() {
        return R.layout.activity_tabhost;
    }

    @Override
    protected int getFragmentViewId() {
        return R.id.fragment;
    }

    protected  abstract  void initTabHost(FragmentTabHost tabHost);
    TabHostReceiver receiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.fragment);
        mTabHost.getTabWidget().setDividerDrawable(null);
        initTabHost(mTabHost);
        receiver = new TabHostReceiver();
        IntentFilter filter = new IntentFilter() ;
        filter.addAction("tabhost") ;
        this.registerReceiver(receiver,filter);
    }


    protected TabHost.TabSpec setTabHost(String tag,int icon,int text){

        View view = LayoutInflater.from(this).inflate(R.layout.tabhost_item, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
        imageView.setImageResource(icon);
        TextView textView = (TextView) view.findViewById(R.id.text);
        textView.setText(text);
        TabHost.TabSpec spec= mTabHost.newTabSpec(tag).setIndicator(view);
        return  spec;
    }

    protected TabHost.TabSpec setTabHost(String tag,int icon,int text,int textColor){

        View view = LayoutInflater.from(this).inflate(R.layout.tabhost_item, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
        imageView.setImageResource(icon);
        TextView textView = (TextView) view.findViewById(R.id.text);
        textView.setTextColor(textColor);
        textView.setText(text);
        TabHost.TabSpec spec= mTabHost.newTabSpec(tag).setIndicator(view);
        return  spec;
    }


    @Override
    public void onBackPressed() {
        BaseFragment fragment = (BaseFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
        if (fragment != null && fragment.onBackPressed()){
            fragment.onBackPressed();
            return;
        }
        ((MyApplication)getApplication()).exit();
    }

    public class TabHostReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
                 int id = intent.getIntExtra("id",0);
                 mTabHost.setCurrentTab(id);

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (receiver != null){
            this.unregisterReceiver(receiver);
        }
    }

}





