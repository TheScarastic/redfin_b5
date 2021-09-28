package com.android.systemui.media;

import android.content.ComponentName;
import android.content.Context;
import com.android.systemui.media.ResumeMediaBrowser;
/* loaded from: classes.dex */
public class ResumeMediaBrowserFactory {
    private final MediaBrowserFactory mBrowserFactory;
    private final Context mContext;

    public ResumeMediaBrowserFactory(Context context, MediaBrowserFactory mediaBrowserFactory) {
        this.mContext = context;
        this.mBrowserFactory = mediaBrowserFactory;
    }

    public ResumeMediaBrowser create(ResumeMediaBrowser.Callback callback, ComponentName componentName) {
        return new ResumeMediaBrowser(this.mContext, callback, componentName, this.mBrowserFactory);
    }
}
