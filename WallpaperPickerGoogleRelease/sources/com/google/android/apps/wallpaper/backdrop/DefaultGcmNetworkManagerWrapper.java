package com.google.android.apps.wallpaper.backdrop;

import android.content.Context;
/* loaded from: classes.dex */
public class DefaultGcmNetworkManagerWrapper {
    public static DefaultGcmNetworkManagerWrapper sInstance;
    public static final Object sInstanceLock = new Object();
    public Context mAppContext;

    public DefaultGcmNetworkManagerWrapper(Context context) {
        this.mAppContext = context;
    }
}
