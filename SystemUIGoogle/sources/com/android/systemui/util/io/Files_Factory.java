package com.android.systemui.util.io;

import dagger.internal.Factory;
/* loaded from: classes2.dex */
public final class Files_Factory implements Factory<Files> {

    /* access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static final class InstanceHolder {
        private static final Files_Factory INSTANCE = new Files_Factory();
    }

    @Override // javax.inject.Provider
    public Files get() {
        return newInstance();
    }

    public static Files_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static Files newInstance() {
        return new Files();
    }
}
