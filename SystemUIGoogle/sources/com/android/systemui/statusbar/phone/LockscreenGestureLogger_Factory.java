package com.android.systemui.statusbar.phone;

import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class LockscreenGestureLogger_Factory implements Factory<LockscreenGestureLogger> {

    /* access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class InstanceHolder {
        private static final LockscreenGestureLogger_Factory INSTANCE = new LockscreenGestureLogger_Factory();
    }

    @Override // javax.inject.Provider
    public LockscreenGestureLogger get() {
        return newInstance();
    }

    public static LockscreenGestureLogger_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static LockscreenGestureLogger newInstance() {
        return new LockscreenGestureLogger();
    }
}
