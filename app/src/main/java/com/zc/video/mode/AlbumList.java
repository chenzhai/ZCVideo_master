package com.zc.video.mode;

import android.util.Log;

import java.util.ArrayList;

public class AlbumList extends ArrayList<Album> {
    private static final String TAG = AlbumList.class.getSimpleName();

    public void debug () {
        for (Album a : this) {
            Log.d(TAG, ">> albumlist " + a.toString());
        }
    }
}
