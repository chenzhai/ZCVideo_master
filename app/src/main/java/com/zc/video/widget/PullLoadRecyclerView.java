package com.zc.video.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zc.video.R;

public class PullLoadRecyclerView extends LinearLayout {

    private Context mContext;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private View mFootView;
    private boolean mIsRefresh = false;
    private boolean mIsLoadMore = false;
    private OnPullLoadMoreListener mOnPullLoadMoreListener;

    public PullLoadRecyclerView(Context context) {
        super(context);
        initView(context);
    }

    public PullLoadRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public PullLoadRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        mContext = context;
        View view = LayoutInflater.from(mContext).inflate(R.layout.pull_loadmore_layout, null);
        mSwipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(android.R.color.holo_green_dark,null),
                getResources().getColor(android.R.color.holo_blue_dark,null),
                getResources().getColor(android.R.color.holo_orange_dark,null));
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayoutOnRefresh());

        mRecyclerView = view.findViewById(R.id.recyclerview);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mIsLoadMore || mIsRefresh;
            }
        });
        mRecyclerView.setVerticalScrollBarEnabled(false);
        mRecyclerView.addOnScrollListener(new RecyclerViewOnScroll());

        mFootView = view.findViewById(R.id.footer_view);
        TextView textView = mFootView.findViewById(R.id.iv_load_text);
        mFootView.setVisibility(View.GONE);
        this.addView(view);
    }

    public void setGridLayout(int spanCount) {
        GridLayoutManager manager = new GridLayoutManager(mContext, spanCount);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        if(adapter != null) {
            mRecyclerView.setAdapter(adapter);
        }
    }

    class SwipeRefreshLayoutOnRefresh implements SwipeRefreshLayout.OnRefreshListener {

        @Override
        public void onRefresh() {
            if(!mIsRefresh) {
                mIsRefresh = true;
                refreshData();
            }
        }
    }

    class RecyclerViewOnScroll extends RecyclerView.OnScrollListener {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int firstItem = 0;
            int lastItem = 0;
            RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
            int totalCount = manager.getItemCount();

            if(manager instanceof GridLayoutManager) {
                GridLayoutManager gridLayoutManager = (GridLayoutManager)manager;
                firstItem = gridLayoutManager.findFirstCompletelyVisibleItemPosition();
                lastItem = gridLayoutManager.findLastCompletelyVisibleItemPosition();

                if(firstItem ==0 || firstItem == RecyclerView.NO_POSITION) {
                    lastItem = gridLayoutManager.findLastVisibleItemPosition();
                }
            }

            if(mSwipeRefreshLayout.isEnabled()) {
                mSwipeRefreshLayout.setEnabled(true);
            } else {
                mSwipeRefreshLayout.setEnabled(false);
            }

            if(!mIsLoadMore
                    && totalCount == lastItem
                    && mSwipeRefreshLayout.isEnabled()
                    && !mIsRefresh
                    && (dx > 0 || dy > 0)) {
                mIsLoadMore = true;
                loadMoreData();
            }
        }
    }

    private void loadMoreData() {
        if (mOnPullLoadMoreListener != null) {
            mFootView.animate().translationY(0).setInterpolator(new AccelerateDecelerateInterpolator())
                    .setDuration(300).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    mFootView.setVisibility(View.VISIBLE);
                    //mAnimationDrawable.start();
                }
            }).start();
            invalidate();
            mOnPullLoadMoreListener.loadMore();
        }
    }

    private void refreshData() {
        if (mOnPullLoadMoreListener != null) {
            mOnPullLoadMoreListener.reFresh();
        }
    }

    public void setRefreshCompleted() {
        mIsRefresh = false;
        setRefreshing(false);
    }

    private void setRefreshing(final boolean isRefreshing) {
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(isRefreshing);
            }
        });
    }

    public void setLoadMoreCompleted() {
        mIsLoadMore = false;
        mIsRefresh = false;
        setRefreshing(false);
        mFootView.animate().translationY(mFootView.getHeight()).setInterpolator(new AccelerateDecelerateInterpolator())
                .setDuration(300).start();
    }

    public interface OnPullLoadMoreListener {
        void reFresh();
        void loadMore();
    }

    public void setOnPullLoadMoreListener(OnPullLoadMoreListener listener) {
        mOnPullLoadMoreListener = listener;
    }
}
