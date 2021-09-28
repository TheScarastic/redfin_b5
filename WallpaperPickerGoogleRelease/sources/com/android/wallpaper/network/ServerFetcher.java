package com.android.wallpaper.network;

import com.android.volley.VolleyError;
import com.google.chrome.dongle.imax.wallpaper2.protos.ImaxWallpaperProto$Image;
import java.util.List;
/* loaded from: classes.dex */
public interface ServerFetcher {

    /* loaded from: classes.dex */
    public interface NextImageInCollectionCallback {
        void onError(VolleyError volleyError);

        void onSuccess(ImaxWallpaperProto$Image imaxWallpaperProto$Image, String str);
    }

    /* loaded from: classes.dex */
    public interface ResultsCallback<T> {
        void onError(VolleyError volleyError);

        void onSuccess(List<T> list);
    }
}
