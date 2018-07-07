package com.zc.video.api;

import android.content.Context;

import com.zc.video.mode.Channel;
import com.zc.video.mode.Site;

public class SiteApi {

    public void onGetChannelAlbums(Context context, int pageNo, int pageSize, int siteId, int channelId, OnGetChannelAlbumListener listener) {
        switch (siteId) {
            case Site.LETV:
                new LetvApi().onGetChannelAlbums(new Channel(channelId, context), pageNo, pageSize, listener);
                break;
            case Site.SOHU:
                new SohuApi().onGetChannelAlbums(new Channel(channelId, context), pageNo, pageSize, listener);
                break;
        }
    }
}
