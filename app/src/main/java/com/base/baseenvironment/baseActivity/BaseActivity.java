package com.base.baseenvironment.baseActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;


import com.base.baseenvironment.MyApplication;
import com.base.baseenvironment.R;
import com.base.baseenvironment.baseFragment.BaseFragment;
import com.base.baseenvironment.utils.premission.PermissionLisenter;
import com.base.baseenvironment.utils.premission.PermissionUtil;

import java.lang.reflect.Field;

public abstract class BaseActivity extends AppCompatActivity {
    // 获取ContentView
    protected abstract int getContentViewId();

    // 获取承载Fragment的View
    protected abstract int getFragmentViewId();

    MyApplication myApplication;
    PermissionLisenter permissionLisenter;

    // 添加Fragment
    public void addFragment(BaseFragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(getFragmentViewId(), fragment, fragment.getClass().getSimpleName())
                .addToBackStack(fragment.getClass().getSimpleName())
                .commitAllowingStateLoss();
    }

    // 移除当前Fragment
    public void removeFragment() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack();
            Log.e("log", "remove");
        } else {
            finish();
            Log.e("log", "finish");
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());
        myApplication = (MyApplication) getApplication();
        myApplication.addActivity(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        myApplication.removeActivity(this);
    }


    // 用户权限 申请 的回调方法
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

                if (PermissionUtil.buildVersion()){
                    if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                        // 判断用户是否 点击了不再提醒。(检测该权限是否还可以申请)
                        boolean b = shouldShowRequestPermissionRationale(permissions[0]);
                        if (!b) {
                            // 用户还是想用我的 APP 的
                            // 提示用户去应用设置界面手动开启权限
                            PermissionUtil.showDialogTipUserRequestPermission(this, "权限被禁用，需要跳转到权限管理，手动打开权限", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    goToAppSetting();
                                }
                            });
                        } else
                            finish();
                    } else {
                        if (permissionLisenter != null){
                            permissionLisenter.permissionRunnable(requestCode);
                        }
                    }
                }


    }

    public void setPermission(PermissionLisenter permissionLisenter){
          this.permissionLisenter = permissionLisenter;
    };

    // 跳转到当前应用的设置界面
    private void goToAppSetting() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 123);
    }


    @Override
    public void onBackPressed() {
        BaseFragment fragment = (BaseFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
        if (fragment != null && fragment.onBackPressed()){
            fragment.onBackPressed();
            return;
        }
    }
}
