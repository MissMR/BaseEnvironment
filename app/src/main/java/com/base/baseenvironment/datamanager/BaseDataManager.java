package com.base.baseenvironment.datamanager;

import com.base.baseenvironment.presenter.BasePresenter;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/8/4.
 */

public interface BaseDataManager {
    void initData(String url, HashMap<String,String> map, BasePresenter.InitDataListener listener);
}
