package com.zc.video;


import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zc.video.base.BaseFragment;
import com.zc.video.mode.Site;
import com.zc.video.widget.PullLoadRecyclerView;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailListFragment extends BaseFragment {

    private static int mSiteId;
    private static int mChannelId;
    private static final String CHANNEL_ID = "channelid";
    private static final String SITE_ID = "siteid";
    private static final int REFRESH_DURATION = 1500;
    private static final int LOADMORE_DURATION = 3000;
    private PullLoadRecyclerView mRecyclerView;
    private TextView mEmptyView;
    private int mColumns;
    private DetailListAdapter mAdapter;
    private Handler mHandler = new Handler(Looper.getMainLooper());

    public DetailListFragment() {

    }

    public static Fragment newInstance(int siteId, int channelId) {
        DetailListFragment fragment = new DetailListFragment();
        mSiteId = siteId;
        mChannelId = channelId;
        Bundle bundle = new Bundle();
        bundle.putInt(CHANNEL_ID, mChannelId);
        bundle.putInt(SITE_ID, mSiteId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_detail_list;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadData();
        mAdapter = new DetailListAdapter();
        if(mSiteId == Site.LETV) {
            mColumns = 2;
            mAdapter.setColumns(mColumns);
        }
    }

    @Override
    protected void initView() {
        mEmptyView = mContentView.findViewById(R.id.tv_empty);
        mEmptyView.setText(getActivity().getResources().getString(R.string.load_more_text));
        mRecyclerView = mContentView.findViewById(R.id.pullloadRecyclerView);
        mRecyclerView.setGridLayout(3);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setOnPullLoadMoreListener(new PullLoadMoreListener());
    }

    class PullLoadMoreListener implements PullLoadRecyclerView.OnPullLoadMoreListener {

        @Override
        public void reFresh() {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    reFreshData();
                    mRecyclerView.setRefreshCompleted();
                }
            },REFRESH_DURATION);
        }

        @Override
        public void loadMore() {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    loadData();
                    mRecyclerView.setLoadMoreCompleted();
                }
            },LOADMORE_DURATION);

        }
    }

    private void loadData() {
    }

    private void reFreshData() {

    }

    class DetailListAdapter extends RecyclerView.Adapter {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }

        public void setColumns(int columns) {

        }
    }
    @Override
    protected void initData() {

    }
}
