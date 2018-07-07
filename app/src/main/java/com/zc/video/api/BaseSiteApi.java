package com.zc.video.api;

import com.zc.video.mode.Channel;

public abstract class BaseSiteApi {

    abstract public void onGetChannelAlbums(Channel channel, int pageNo, int pageSize, OnGetChannelAlbumListener listener);
}
