package com.google.android.systemui.assist.uihints;

import dagger.internal.Factory;
/* loaded from: classes2.dex */
public final class TouchOutsideHandler_Factory implements Factory<TouchOutsideHandler> {

    /* loaded from: classes2.dex */
    private static final class InstanceHolder {
        private static final TouchOutsideHandler_Factory INSTANCE = new TouchOutsideHandler_Factory();
    }

    @Override // javax.inject.Provider
    public TouchOutsideHandler get() {
        return newInstance();
    }

    public static TouchOutsideHandler_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static TouchOutsideHandler newInstance() {
        return new TouchOutsideHandler();
    }
}
