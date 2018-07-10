package com.zc.video;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;

import com.zc.video.base.BaseActivity;
import com.zc.video.mode.Site;

import java.util.HashMap;

public class DetailListActivity extends BaseActivity {

    public static final String CHANNEL_ID = "channelid";
    private int mChannelId;
    private ViewPager mViewPager;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_detail_list;
    }

    @Override
    protected void initView() {
        Intent intent = getIntent();
        if (intent != null) {
            mChannelId = intent.getIntExtra(CHANNEL_ID, 0);
        }

        mViewPager = findViewById(R.id.pager);
        mViewPager.setAdapter(new SitePagerAdapter(getSupportFragmentManager(), this, mChannelId));
    }

    class SitePagerAdapter extends FragmentPagerAdapter {

        private Context mContext;
        private int mChannelID;
        private HashMap<Integer, DetailListFragment> mPagerMap;

        public SitePagerAdapter(FragmentManager fm, Context context, int channelID) {
            super(fm);
            mContext = context;
            mChannelID = channelID;
            mPagerMap = new HashMap<>();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Object obj = super.instantiateItem(container, position);
            if(obj instanceof DetailListFragment) {
                mPagerMap.put(position, (DetailListFragment)obj);
            }
            return obj;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
            mPagerMap.remove(position);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = DetailListFragment.newInstance(position + 1, mChannelID);
            return fragment;
        }

        @Override
        public int getCount() {
            return Site.MAX_SITE;
        }
    }

    @Override
    protected void initData() {

    }

    public static void launchDeatilListActivity(Context context, int channelID) {
        Intent intent = new Intent(context, DetailListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(CHANNEL_ID, channelID);
        context.startActivity(intent);
    }
}
