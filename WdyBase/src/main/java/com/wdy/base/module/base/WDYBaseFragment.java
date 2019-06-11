package com.wdy.base.module.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;

import com.wdy.base.module.util.ToastUtil;

/**
 * 作者：王东一
 * 创建时间：2019-05-14.
 */
public abstract class WDYBaseFragment extends Fragment {
    //懒加载
    protected boolean isViewShown = false;
    protected View main;
    protected boolean isBuild = false;
    protected Context wdyContext;
    protected Bundle savedInstanceState;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        wdyContext = context;
    }


    @Override
    public void onDetach() {
        super.onDetach();
        wdyContext = null;
    }

    protected void setContentView(LayoutInflater inflater, int layoutResID) {
        if (main == null) {
            this.main = inflater.inflate(layoutResID, null);
            if (!isViewShown && !isBuild) {
                lazyLoad();
            }
        }
    }

    protected void showToast(String content) {
        ToastUtil.getToast(wdyContext).showMiddleToast(content);
    }

    //初始化代码
    protected abstract void init();

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isViewShown = null != getView();
    }


    protected void lazyLoad() {
        isBuild = true;
        init();
    }
}
