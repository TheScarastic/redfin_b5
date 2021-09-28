package com.android.systemui.media;

import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class SmartspaceMediaDataProvider_Factory implements Factory<SmartspaceMediaDataProvider> {

    /* access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class InstanceHolder {
        private static final SmartspaceMediaDataProvider_Factory INSTANCE = new SmartspaceMediaDataProvider_Factory();
    }

    @Override // javax.inject.Provider
    public SmartspaceMediaDataProvider get() {
        return newInstance();
    }

    public static SmartspaceMediaDataProvider_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static SmartspaceMediaDataProvider newInstance() {
        return new SmartspaceMediaDataProvider();
    }
}
