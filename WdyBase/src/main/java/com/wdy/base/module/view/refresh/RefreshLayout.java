package com.wdy.base.module.view.refresh;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.wdy.base.module.R;
import com.wdy.base.module.util.CodeUtil;
import com.wdy.base.module.util.WDYLog;

/**
 * 作者：王东一 on 2016/3/31 16:09
 **/
public class RefreshLayout extends SwipeRefreshLayout implements SwipeRefreshLayout.OnRefreshListener {
    //滑动到最下面时的上拉操作
    private int mTouchSlop;

    //实例
    private RecyclerView mRecyclerView = null;

    //上拉监听器, 到了最底部的上拉加载操作
    private OnLoadListener mOnLoadListener;

    /**
     * ListView的加载中footer
     */
    private LinearLayout footerView;

    /**
     * 按下时的y坐标
     */
    private int mYDown;
    /**
     * 抬起时的y坐标, 与mYDown一起用于滑动到底部时判断是上拉还是下拉
     */
    private int mLastY;

    //是否在加载中 ( 上拉加载更多 )
    private boolean isLoading = false;

    //是否可以加载更多
    private boolean canLoadMore = true;
    // 是否存在左右滑动事件
    private boolean mDragger;
    // 记录手指按下的位置
    private float mStartY, mStartX;
    // 出发事件的最短距离
    private int mInTouchSlop;
    //最后一位
    private int lastVisibleItem;
    public Handler refreshHandler = new Handler();
    public Runnable refreshRunnable;
    public Runnable loadMoreRunnable;

    // param context
    public RefreshLayout(Context context) {
        this(context, null);
    }

    public RefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnRefreshListener(this);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        setColorSchemeResources(R.color.top_color);
        setSize(SwipeRefreshLayout.DEFAULT);
        setProgressViewEndTarget(false, CodeUtil.dip2px(context, 60));
        refreshRunnable = new Runnable() {
            @Override
            public void run() {
                try {
                    try {
                        if (getParent() != null)
                            setRefreshing(false);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        loadMoreRunnable = new Runnable() {
            @Override
            public void run() {
                setLoading(false);
                if (footerView != null) {
                    footerView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (footerView != null)
                                footerView.setVisibility(GONE);
                        }
                    }, 300);

                }
            }
        };
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        // 初始化对象
        if (mRecyclerView == null) {
            getView();
        }
    }

    /**
     * 获取对象
     */
    private void getView() {
        int children = getChildCount();
        if (children > 0) {
            for (int i = 0; i < children; i++) {
                if (getChildAt(i) instanceof RelativeLayout) {
                    RelativeLayout relativeLayout = ((RelativeLayout) getChildAt(i));
                    for (int j = 0; j < relativeLayout.getChildCount(); j++) {
                        if (relativeLayout.getChildAt(j) instanceof RecyclerView) {
                            mRecyclerView = (RecyclerView) ((RelativeLayout) getChildAt(i)).getChildAt(j);
                            // 设置滚动监听器给, 使得滚动的情况下也可以自动加载
                            if (mRecyclerView != null) {
                                mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                                    @Override
                                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                                        super.onScrollStateChanged(recyclerView, newState);
                                        try {
                                            if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == mRecyclerView.getAdapter().getItemCount()) {
                                                if (canLoad()) {
                                                    mRecyclerView.scrollToPosition(lastVisibleItem);
                                                    footerView.setVisibility(VISIBLE);
                                                    loadData();
                                                }
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    @Override
                                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                                        super.onScrolled(recyclerView, dx, dy);
                                        if (mRecyclerView.getLayoutManager() instanceof LinearLayoutManager)
                                            lastVisibleItem = ((LinearLayoutManager) mRecyclerView.getLayoutManager()).findLastVisibleItemPosition();
                                    }
                                });
                                WDYLog.d(VIEW_LOG_TAG, "找到子view");
                            }
                        }
                        if (relativeLayout.getChildAt(j) instanceof LinearLayout) {
                            footerView = (LinearLayout) relativeLayout.getChildAt(j);
                            if (footerView != null)
                                footerView.setVisibility(GONE);
                        }
                    }
                }


            }
        }
    }

    //    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        int action = ev.getAction();
//        switch (action) {
//            case MotionEvent.ACTION_DOWN:
//                // 记录手指按下的位置
//                mStartY = ev.getY();
//                mStartX = ev.getX();
//                //初始化左右滑动事件为false
//                mDragger = false;
//                break;
//            case MotionEvent.ACTION_MOVE:
//                //如果左右滑动事件为true  直接返回false 不拦截事件
//                if (mDragger) {
//                    return false;
//                }
//
//                // 获取当前手指位置
//                float endY = ev.getY();
//                float endX = ev.getX();
//                //获取X,Y滑动距离的绝对值
//                float distanceX = Math.abs(endX - mStartX);
//                float distanceY = Math.abs(endY - mStartY);
//
//                // 如果X轴位移大于Y轴距离，那么将事件交给其他控件
//                if (distanceX > mTouchSlop && distanceX > distanceY) {
//                    mDragger = true;
//                    return false;
//                }
//                break;
//            case MotionEvent.ACTION_UP:
//            case MotionEvent.ACTION_CANCEL:
//                //初始化左右滑动事件为false
//                mDragger = false;
//                break;
//        }
//        return super.onInterceptTouchEvent(ev);
//    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getAction();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                // 按下
                mYDown = (int) event.getRawY();
                if (mRecyclerView.getLayoutManager() instanceof LinearLayoutManager)
                    if (((LinearLayoutManager) mRecyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition() == 0) {
                        setEnabled(true);
                    } else if (mRecyclerView.getChildCount() > 0 && mRecyclerView.getChildAt(0).getTop() == 0) {
                        setEnabled(true);
                    } else if (mRecyclerView.getChildCount() == 0) {
                        setEnabled(true);
                    } else {
                        setEnabled(false);
                    }
                break;

            case MotionEvent.ACTION_MOVE:
                // 移动
                mLastY = (int) event.getRawY();
                break;

            case MotionEvent.ACTION_UP:

                // 抬起
//                if (canLoad()) {
//                    loadData();
//                }
                break;
            default:
                break;
        }

        return super.dispatchTouchEvent(event);
    }

    @Override
    public void setRefreshing(boolean refreshing) {
        super.setRefreshing(refreshing);
    }

    public boolean isCanLoadMore() {
        return canLoadMore;
    }

    //是否可以下拉刷新
    public void setCanRefresh(boolean canRefresh) {
        setEnabled(canRefresh);
    }

    //是否可以加载更多
    public void setCanLoadMore(boolean canLoadMore) {
        this.canLoadMore = canLoadMore;
    }

    /**
     * 是否可以加载更多, 条件是到了最底部, 不在加载中, 且为上拉操作.
     * <p/>
     * return
     */
    private boolean canLoad() {
        return !isLoading && isPullUp() && isCanLoadMore();
    }


    /**
     * 是否是上拉操作
     * <p/>
     * return
     */
    private boolean isPullUp() {
        return (mYDown - mLastY) >= mTouchSlop;
    }

    /**
     * 如果到了最底部,而且是上拉操作.那么执行onLoad方法
     */
    private void loadData() {
        if (mOnLoadListener != null) {
            // 设置状态
            setLoading(true);
            //
            mOnLoadListener.onLoad();
        }
    }

    /**
     * param loading
     */
    public void setLoading(boolean loading) {
        isLoading = loading;
        if (isLoading) {

        } else {

            mYDown = 0;
            mLastY = 0;
        }
    }

    /**
     * param loadListener
     */
    public void setOnLoadListener(OnLoadListener loadListener) {
        mOnLoadListener = loadListener;

    }


    public void LoadMoreComplete() {
        //关闭加载更多
        refreshHandler.postDelayed(loadMoreRunnable, 300);
    }


    public void AutoRefresh() {
        if (getContext() != null)
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    setRefreshing(true);
                    autoRefreshListener.refresh();
                }
            }, 300);
    }

    public void setAutoRefreshListener(OnAutoRefreshListener autoRefreshListener) {
        this.autoRefreshListener = autoRefreshListener;
    }

    private OnAutoRefreshListener autoRefreshListener;

    //关闭刷新
    public void RefreshComplete() {
        setRefreshing(false);
//        refreshHandler.postDelayed(refreshRunnable, 300);
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (!hasWindowFocus) {
            closeHandler();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        closeHandler();
    }

    @Override
    public void onRefresh() {
        if (autoRefreshListener != null)
            autoRefreshListener.refresh();
    }

    public void closeHandler() {
        if (refreshRunnable != null)
            refreshHandler.removeCallbacks(refreshRunnable);
        if (loadMoreRunnable != null)
            refreshHandler.removeCallbacks(loadMoreRunnable);
    }
}
