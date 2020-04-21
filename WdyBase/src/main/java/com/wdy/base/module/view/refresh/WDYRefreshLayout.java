package com.wdy.base.module.view.refresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.wdy.base.module.R;

/**
 * 作者：王东一
 * 创建时间：2020/4/14.
 * 上拉和下拉的刷新控件
 */
public class WDYRefreshLayout extends LinearLayout {
    private Context context;
    private ViewHolder viewHolder;
    private boolean scrollIsBottom = false;
    private OnLoadListener onLoadListener;
    private boolean canLoadMore = true;//是否可以加载更多

    public WDYRefreshLayout(Context context) {
        super(context);
        initLayout((context));
    }

    public WDYRefreshLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initLayout((context));
    }

    public WDYRefreshLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initLayout((context));
    }

    private void initLayout(Context context) {
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.wdy_refresh_layout, this);
        viewHolder = new ViewHolder(view);
        init();
    }

    private void init() {
        if (viewHolder != null) {
            viewHolder.wdy_refresh.setOnChildScrollUpCallback((parent, child) -> {
                initLoadMore();
                return false;
            });
            viewHolder.wdy_nested.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
                // 滚动到底
                scrollIsBottom = oldScrollY >= (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) ||
                        scrollY >= (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight());
            });
        }

    }

    private void initLoadMore() {
        if (canLoadMore) {
            if (viewHolder != null && scrollIsBottom && viewHolder.wdy_loading_layout.getVisibility() != VISIBLE) {
                viewHolder.wdy_loading_layout.setVisibility(VISIBLE);
                if (onLoadListener != null) {
                    onLoadListener.onLoad();
                }
            }
        }
    }

    /**
     * @param color 设置下拉颜色
     */
    public void setRefreshColor(int color) {
        if (viewHolder != null) {
            viewHolder.wdy_refresh.setColorSchemeResources(color);
        }
    }

    /**
     * @param color 设置加载更多颜色
     */
    public void setLoadColor(int color) {
        if (viewHolder != null) {
            viewHolder.wdy_loading.setPaintColor(color);
        }
    }

    /**
     * @param color 设置颜色
     */
    public void setAllColor(int color) {
        setRefreshColor(color);
        setLoadColor(color);
    }

    /**
     * @param onRefreshListener 下拉刷新
     */
    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        if (viewHolder != null) {
            viewHolder.wdy_refresh.setOnRefreshListener(onRefreshListener::onRefresh);
        }
    }

    /**
     * @param onLoadListener 加载更多
     */
    public void setOnLoadListener(OnLoadListener onLoadListener) {
        if (viewHolder != null) {
            this.onLoadListener = onLoadListener;
        }
    }

    /**
     * 全部刷新结束
     */
    public void allCompete() {
        refreshComplete();
        loadComplete();
    }

    /**
     * 下拉刷新结束
     */
    public void refreshComplete() {
        if (viewHolder.wdy_refresh != null) {
            viewHolder.wdy_refresh.setRefreshing(false);
        }
    }

    /**
     * 加载更多结束
     */
    public void loadComplete() {
        if (viewHolder != null) {
            viewHolder.wdy_loading_layout.setVisibility(GONE);
        }
    }

    /**
     * @param can 是否可加载更多
     */
    public void setCanLoadMore(boolean can) {
        this.canLoadMore = can;
    }

    /**
     * @param can 是否可以下拉刷新
     */
    public void setCanRefresh(boolean can) {
        if (viewHolder != null) {
            viewHolder.wdy_refresh.setEnabled(can);
        }
    }

    /**
     * @param can 是否可以下拉刷新和加载更多
     */
    public void setCanAll(boolean can) {
        setCanLoadMore(can);
        setCanRefresh(can);
    }

    /**
     * @param view 头部添加
     */
    public void addHeardView(View view) {
        if (viewHolder != null) {
            viewHolder.wdy_top_layout.addView(view);
            viewHolder.wdy_top_layout.setVisibility(VISIBLE);
        }
    }

    /**
     * @param layout 头部添加
     */
    public void addHeardView(int layout) {
        if (viewHolder != null) {
            viewHolder.wdy_top_layout.removeAllViewsInLayout();
            View view = LayoutInflater.from(context).inflate(layout, this, false);
            LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            viewHolder.wdy_top_layout.addView(view, params);
            viewHolder.wdy_top_layout.setVisibility(VISIBLE);
        }
    }

    /**
     * @param view 底部添加
     */
    public void addBottomView(View view) {
        if (viewHolder != null) {
            viewHolder.wdy_bottom_layout.addView(view);
            viewHolder.wdy_bottom_layout.setVisibility(VISIBLE);
        }
    }
    /**
     * @param layout 底部添加
     */
    public void addBottomView(int layout) {
        if (viewHolder != null) {
            viewHolder.wdy_bottom_layout.removeAllViewsInLayout();
            View view = LayoutInflater.from(context).inflate(layout, this, false);
            LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            viewHolder.wdy_bottom_layout.addView(view, params);
            viewHolder.wdy_bottom_layout.setVisibility(VISIBLE);
        }
    }
    /**
     * @param visibility 头部是否显示
     */
    public void setHeardVisibility(int visibility) {
        if (viewHolder != null) {
            viewHolder.wdy_top_layout.setVisibility(visibility);
        }
    }

    /**
     * @param visibility 底部是否显示
     */
    public void setBottomVisibility(int visibility) {
        if (viewHolder != null) {
            viewHolder.wdy_bottom_layout.setVisibility(visibility);
        }
    }

    public RecyclerView getRecyclerView() {
        if (viewHolder != null) {
            return viewHolder.wdy_recyclerView;
        }
        return null;
    }

    private static class ViewHolder {
        View rootView;
        LinearLayout wdy_top_layout;
        RecyclerView wdy_recyclerView;
        LinearLayout wdy_bottom_layout;
        LoadingView wdy_loading;
        TextView wdy_loading_text;
        LinearLayout wdy_loading_layout;
        LinearLayout wdy_content_layout;
        NestedScrollView wdy_nested;
        SwipeRefreshLayout wdy_refresh;

        ViewHolder(View rootView) {
            this.rootView = rootView;
            this.wdy_top_layout = rootView.findViewById(R.id.wdy_top_layout);
            this.wdy_recyclerView = rootView.findViewById(R.id.wdy_recyclerView);
            this.wdy_bottom_layout = rootView.findViewById(R.id.wdy_bottom_layout);
            this.wdy_loading = rootView.findViewById(R.id.wdy_loading);
            this.wdy_loading_text = rootView.findViewById(R.id.wdy_loading_text);
            this.wdy_loading_layout = rootView.findViewById(R.id.wdy_loading_layout);
            this.wdy_content_layout = rootView.findViewById(R.id.wdy_content_layout);
            this.wdy_nested = rootView.findViewById(R.id.wdy_nested);
            this.wdy_refresh = rootView.findViewById(R.id.wdy_refresh);
        }

    }
}
