package com.base.baseenvironment.presenter;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Administrator on 2017/8/4.
 */

public interface BasePresenter {
    void initData();

   public interface InitDataListener{
        void onResponse(Object object);
    }
}


