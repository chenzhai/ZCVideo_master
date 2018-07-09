package com.zc.video.api;

import com.zc.video.mode.AlbumList;
import com.zc.video.mode.ErrorInfo;

public interface OnGetChannelAlbumListener {

    void onGetChannelAlbumSuccess(AlbumList albumList);
    void onGetChannelAlbumFailed(ErrorInfo info);

}
