package com.google.android.systemui.assist.uihints;

import dagger.internal.Factory;
/* loaded from: classes2.dex */
public final class GoBackHandler_Factory implements Factory<GoBackHandler> {

    /* loaded from: classes2.dex */
    private static final class InstanceHolder {
        private static final GoBackHandler_Factory INSTANCE = new GoBackHandler_Factory();
    }

    @Override // javax.inject.Provider
    public GoBackHandler get() {
        return newInstance();
    }

    public static GoBackHandler_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static GoBackHandler newInstance() {
        return new GoBackHandler();
    }
}
