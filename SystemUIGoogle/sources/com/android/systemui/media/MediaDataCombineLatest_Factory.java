package com.android.systemui.media;

import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class MediaDataCombineLatest_Factory implements Factory<MediaDataCombineLatest> {

    /* access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class InstanceHolder {
        private static final MediaDataCombineLatest_Factory INSTANCE = new MediaDataCombineLatest_Factory();
    }

    @Override // javax.inject.Provider
    public MediaDataCombineLatest get() {
        return newInstance();
    }

    public static MediaDataCombineLatest_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static MediaDataCombineLatest newInstance() {
        return new MediaDataCombineLatest();
    }
}
