package com.android.systemui.statusbar;

import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class MediaArtworkProcessor_Factory implements Factory<MediaArtworkProcessor> {

    /* access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class InstanceHolder {
        private static final MediaArtworkProcessor_Factory INSTANCE = new MediaArtworkProcessor_Factory();
    }

    @Override // javax.inject.Provider
    public MediaArtworkProcessor get() {
        return newInstance();
    }

    public static MediaArtworkProcessor_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static MediaArtworkProcessor newInstance() {
        return new MediaArtworkProcessor();
    }
}
