package com.android.systemui.media;

import com.android.systemui.media.MediaHost;
import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class MediaHost_MediaHostStateHolder_Factory implements Factory<MediaHost.MediaHostStateHolder> {

    /* access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class InstanceHolder {
        private static final MediaHost_MediaHostStateHolder_Factory INSTANCE = new MediaHost_MediaHostStateHolder_Factory();
    }

    @Override // javax.inject.Provider
    public MediaHost.MediaHostStateHolder get() {
        return newInstance();
    }

    public static MediaHost_MediaHostStateHolder_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static MediaHost.MediaHostStateHolder newInstance() {
        return new MediaHost.MediaHostStateHolder();
    }
}
