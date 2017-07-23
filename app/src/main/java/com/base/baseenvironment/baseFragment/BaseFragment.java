package com.base.baseenvironment.baseFragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.base.baseenvironment.baseActivity.BaseActivity;


public abstract class BaseFragment extends Fragment {
    // 获取布局id
    protected  abstract int getLayoutId();
    protected  abstract  void initView(View view , Bundle savedInstanceState);
    public  abstract  boolean onBackPressed();
    View view;
    // 防止getActivity == null
    BaseActivity mActivity;


    public BaseActivity getMyActivity(){
        return mActivity;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = (BaseActivity)context;
    }

    // 添加Fragment
    protected  void addFragment(BaseFragment fragment){
        if (fragment != null){
            mActivity.addFragment(fragment);
        }
    }
    // 移除Fragment
    protected  void removeFragment(){
        mActivity.removeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         view = inflater.inflate(getLayoutId(),null);
        initView(view,savedInstanceState);
        return  view;
    }


}
