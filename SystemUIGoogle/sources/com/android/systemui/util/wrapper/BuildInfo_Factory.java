package com.android.systemui.util.wrapper;

import dagger.internal.Factory;
/* loaded from: classes2.dex */
public final class BuildInfo_Factory implements Factory<BuildInfo> {

    /* access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static final class InstanceHolder {
        private static final BuildInfo_Factory INSTANCE = new BuildInfo_Factory();
    }

    @Override // javax.inject.Provider
    public BuildInfo get() {
        return newInstance();
    }

    public static BuildInfo_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static BuildInfo newInstance() {
        return new BuildInfo();
    }
}
