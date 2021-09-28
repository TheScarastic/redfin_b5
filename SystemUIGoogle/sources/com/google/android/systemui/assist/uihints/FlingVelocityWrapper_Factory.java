package com.google.android.systemui.assist.uihints;

import dagger.internal.Factory;
/* loaded from: classes2.dex */
public final class FlingVelocityWrapper_Factory implements Factory<FlingVelocityWrapper> {

    /* loaded from: classes2.dex */
    private static final class InstanceHolder {
        private static final FlingVelocityWrapper_Factory INSTANCE = new FlingVelocityWrapper_Factory();
    }

    @Override // javax.inject.Provider
    public FlingVelocityWrapper get() {
        return newInstance();
    }

    public static FlingVelocityWrapper_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static FlingVelocityWrapper newInstance() {
        return new FlingVelocityWrapper();
    }
}
