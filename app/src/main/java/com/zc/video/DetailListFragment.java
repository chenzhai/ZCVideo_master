package com.zc.video;


import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zc.video.api.OnGetChannelAlbumListener;
import com.zc.video.api.SiteApi;
import com.zc.video.base.BaseFragment;
import com.zc.video.mode.Album;
import com.zc.video.mode.AlbumList;
import com.zc.video.mode.Channel;
import com.zc.video.mode.ErrorInfo;
import com.zc.video.mode.Site;
import com.zc.video.utils.ImageUtils;
import com.zc.video.widget.PullLoadRecyclerView;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailListFragment extends BaseFragment {

    private static final String TAG = "DetailListFragment";
    private int mSiteId;
    private int mChannelId;
    private static final String CHANNEL_ID = "channelid";
    private static final String SITE_ID = "siteid";
    private static final int REFRESH_DURATION = 1500;
    private static final int LOADMORE_DURATION = 3000;
    private PullLoadRecyclerView mRecyclerView;
    private int pageNo;
    private int pageSize = 30;
    private TextView mEmptyView;
    private int mColumns;
    private DetailListAdapter mAdapter;
    private Handler mHandler = new Handler(Looper.getMainLooper());

    public DetailListFragment() {

    }

    public static Fragment newInstance(int siteId, int channelId) {
        DetailListFragment fragment = new DetailListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(CHANNEL_ID, channelId);
        bundle.putInt(SITE_ID, siteId);
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
        if (getArguments() != null ) {
            mSiteId = getArguments().getInt(SITE_ID);
            mChannelId = getArguments().getInt(CHANNEL_ID);
        }
        pageNo = 0;
        mAdapter = new DetailListAdapter(getActivity(), new Channel(mChannelId, getActivity()));
        loadData();
        if(mSiteId == Site.LETV) {
            mColumns = 2;
            mAdapter.setColumns(mColumns);
        } else {
            mColumns = 3;
            mAdapter.setColumns(mColumns);
        }
    }

    @Override
    protected void initView() {
        mEmptyView = mContentView.findViewById(R.id.tv_empty);
        mEmptyView.setText(getActivity().getResources().getString(R.string.load_more_text));
        mRecyclerView = mContentView.findViewById(R.id.pullloadRecyclerView);
        mRecyclerView.setGridLayout(mColumns);
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
        pageNo ++;
        SiteApi.onGetChannelAlbums(getActivity(), pageNo, pageSize, mSiteId, mChannelId, new OnGetChannelAlbumListener() {

            @Override
            public void onGetChannelAlbumSuccess(AlbumList albumList) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mEmptyView.setVisibility(View.GONE);
                    }
                });
                for (Album album : albumList) {
                    mAdapter.setData(album);
                }
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.notifyDataSetChanged();
                    }
                });
                //for (Album album : albumList) {
                //   Log.d(TAG, ">> album " + album.toString());
                //}
            }

            @Override
            public void onGetChannelAlbumFailed(ErrorInfo info) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mEmptyView.setText(getActivity().getResources().getString(R.string.data_failed_tip));
                    }
                });
            }
        });
    }

    private void reFreshData() {
        pageNo = 0;
        mAdapter = null;
        mAdapter = new DetailListAdapter(getActivity(), new Channel(mChannelId, getActivity()));
        loadData();
        if(mSiteId == Site.LETV) {
            mColumns = 2;
            mAdapter.setColumns(mColumns);
        } else {
            mColumns = 3;
            mAdapter.setColumns(mColumns);
        }
        mRecyclerView.setAdapter(mAdapter);
        Toast.makeText(getActivity(), "已加载到最新数据", Toast.LENGTH_LONG).show();
    }

    class DetailListAdapter extends RecyclerView.Adapter {

        private Context mContext;
        private Channel mChannel;
        private AlbumList mAlbumList = new AlbumList();
        private int mColumns;

        public DetailListAdapter(Context context, Channel channel) {
            mContext = context;
            mChannel = channel;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view  = ((Activity)mContext).getLayoutInflater().inflate(R.layout.detailist_item, null);
            ItemViewHolder itemViewHolder = new ItemViewHolder(view);
            view.setTag(itemViewHolder);
            return itemViewHolder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (mAlbumList.size() == 0) {
                return;
            }
            final Album album = getItem(position);

            if (holder instanceof ItemViewHolder) {
                ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
                itemViewHolder.albumName.setText(album.getTitle());
                if (album.getTip().isEmpty()) {
                    itemViewHolder.albumTip.setVisibility(View.GONE);
                } else {
                    itemViewHolder.albumTip.setText(album.getTip());
                }
                Point point = null;
                //重新计算宽高
                if (mColumns == 2) {
                    point = ImageUtils.getHorPostSize(mContext, mColumns);
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(point.x, point.y);
                    itemViewHolder.albumPoster.setLayoutParams(params);
                } else {
                    point = ImageUtils.getVerPostSize(mContext, mColumns);
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(point.x, point.y);
                    itemViewHolder.albumPoster.setLayoutParams(params);
                }

                if (album.getVerImgUrl() != null) {
                    ImageUtils.disPlayImage(itemViewHolder.albumPoster, album.getVerImgUrl(), point.x, point.y);
                } else if (album.getHorImgUrl() != null){
                    ImageUtils.disPlayImage(itemViewHolder.albumPoster, album.getHorImgUrl(), point.x, point.y);
                } else {
                    //TOD 默认图
                }
                /*itemViewHolder.resultContainer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mChannelId == Channel.DOCUMENTRY|| mChannelId == Channel.MOVIE || mChannelId== Channel.VARIETY || mChannelId == Channel.MUSIC) {
                            AlbumDetailActivity.launch(getActivity(), album, 0, true);
                        } else {
                            AlbumDetailActivity.launch(getActivity(), album);
                        }
                    }
                });*/

            }
        }

        private Album getItem(int position) {
            return mAlbumList.get(position);
        }

        @Override
        public int getItemCount() {
            if (mAlbumList.size() > 0) {
                return  mAlbumList.size();
            }
            return 0;
        }

        public void setColumns(int columns) {
            mColumns = columns;
        }

        public void setData(Album album) {
            mAlbumList.add(album);
        }

        public class ItemViewHolder extends RecyclerView.ViewHolder {

            private LinearLayout resultContainer;
            private ImageView albumPoster;
            private TextView albumName;
            private TextView albumTip;

            public ItemViewHolder(View view) {
                super(view);
                resultContainer = (LinearLayout) view.findViewById(R.id.album_container);
                albumPoster = (ImageView) view.findViewById(R.id.iv_album_poster);
                albumTip = (TextView) view.findViewById(R.id.tv_album_tip);
                albumName = (TextView) view.findViewById(R.id.tv_album_name);
            }
        }
    }
    @Override
    protected void initData() {

    }
}
