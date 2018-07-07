package com.zc.video.base;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class BaseFragment extends Fragment {

    protected View mContentView;

    public BaseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContentView = getActivity().getLayoutInflater().inflate(getLayoutId(), container,false);
        initView();
        initData();
        return mContentView;
    }

    protected abstract int getLayoutId();
    protected abstract void initView();
    protected abstract void initData();

}
