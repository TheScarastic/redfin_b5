package com.android.systemui.media;

import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class MediaHostStatesManager_Factory implements Factory<MediaHostStatesManager> {

    /* access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class InstanceHolder {
        private static final MediaHostStatesManager_Factory INSTANCE = new MediaHostStatesManager_Factory();
    }

    @Override // javax.inject.Provider
    public MediaHostStatesManager get() {
        return newInstance();
    }

    public static MediaHostStatesManager_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static MediaHostStatesManager newInstance() {
        return new MediaHostStatesManager();
    }
}
