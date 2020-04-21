package com.wdy.base.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.wdy.base.R;
import com.wdy.base.adapter.RefreshTestAdapter;
import com.wdy.base.bean.RefreshTestItemBean;
import com.wdy.base.module.base.WDYBaseActivity;
import com.wdy.base.module.view.refresh.WDYRefreshLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：王东一
 * 创建时间：2020/4/21.
 * 测试新的下拉刷新和上提加载更多
 */
public class RefreshActivity extends WDYBaseActivity {
    private WDYRefreshLayout refresh;
    private RefreshTestAdapter adapter;
    private List<RefreshTestItemBean> list = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refresh);
        initView();
    }

    private void initView() {
        refresh = findViewById(R.id.refresh);
        refresh.addHeardView(R.layout.layout_view_heard);
        refresh.addBottomView(R.layout.layout_view_bottom);
        refresh.setRefreshColor(R.color.text_green);
        refresh.setOnRefreshListener(() -> {
            new Handler(getMainLooper()).postDelayed(() -> {
                refresh.refreshComplete();
            }, 3000);
        });
        refresh.setCanLoadMore(true);
        refresh.setOnLoadListener(() -> {
            new Handler(getMainLooper()).postDelayed(() -> {
                refresh.loadComplete();
            }, 3000);
        });
        adapter = new RefreshTestAdapter(this, list);
        refresh.getRecyclerView().setLayoutManager(new LinearLayoutManager(this));
        refresh.getRecyclerView().setAdapter(adapter);
        addData();
    }

    private void addData() {
        list.add(buildBean("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1587447191978&di=7c136c613ffbab7102dd4d17aea91297&imgtype=0&src=http%3A%2F%2Fi0.hdslb.com%2Fbfs%2Farchive%2F3d4999b8a86738bf4b8722250c702ab21230f449.jpg"));
        list.add(buildBean("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1587447326905&di=b0232d356af9c4eeeecf8e7a73f72b63&imgtype=0&src=http%3A%2F%2Fhbimg.b0.upaiyun.com%2F147d87906db56268e586e2b7f689b61c4f92d8e917109-7sPeYb_fw658"));
        list.add(buildBean("https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=2648425139,1866460594&fm=26&gp=0.jpg"));
        list.add(buildBean("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1587447446606&di=ff5ddae3ca2baca3713c8f66d24baa55&imgtype=0&src=http%3A%2F%2Fimg.pconline.com.cn%2Fimages%2Fupload%2Fupc%2Ftx%2Fwallpaper%2F1210%2F19%2Fc1%2F14518324_1350636692665.jpg"));
        list.add(buildBean("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1587447474705&di=1d5539bfc6d161b1a267d99b16960c0a&imgtype=0&src=http%3A%2F%2Fpics5.baidu.com%2Ffeed%2F4a36acaf2edda3cc5a983872397d5c07203f92fa.jpeg%3Ftoken%3D959d3c0f12fb1a8c2426db84aa6528e3g"));

        list.add(buildBean("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=3715511003,208539341&fm=26&gp=0.jpg"));

        list.add(buildBean("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1587447595922&di=5fcf33d104778a1b4b034c9ef98b0df8&imgtype=0&src=http%3A%2F%2Fimg1.lukou.com%2Fstatic%2Fp%2Fblog%2Flarge%2F0002%2F99%2F32%2F22%2F2993222.jpg"));

        list.add(buildBean("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1587447573412&di=f5f6c79a30b7bfe0548a7ce0fb412cbb&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201503%2F08%2F20150308144505_PaWiZ.jpeg"));

        list.add(buildBean("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=3742858054,2068789041&fm=26&gp=0.jpg"));

        adapter.notifyDataSetChanged();
    }

    private RefreshTestItemBean buildBean(String url) {
        RefreshTestItemBean bean = new RefreshTestItemBean();
        bean.setTextName("精美壁纸");
        bean.setUrl(url);
        return bean;
    }
}
