package com.zc.video;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.zc.video.base.BaseActivity;

public class HomeActivity extends BaseActivity {
    //1234567890
    private FragmentManager mFragmentManager;
    private Fragment mHomeFragment;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_home;
    }

    @Override
    protected void initView() {
        initFragment();
    }

    private void initFragment() {
        mFragmentManager = getSupportFragmentManager();
        mHomeFragment = mFragmentManager.findFragmentByTag("HomeFragment");

        if (mHomeFragment == null) {
            mHomeFragment = new HomeFragment();
            mFragmentManager.beginTransaction().replace(R.id.fl_main_content, mHomeFragment, "HomeFragment").commit();
        }
    }

    @Override
    protected void initData() {
    }
}
