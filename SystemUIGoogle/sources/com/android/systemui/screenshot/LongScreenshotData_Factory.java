package com.android.systemui.screenshot;

import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class LongScreenshotData_Factory implements Factory<LongScreenshotData> {

    /* access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class InstanceHolder {
        private static final LongScreenshotData_Factory INSTANCE = new LongScreenshotData_Factory();
    }

    @Override // javax.inject.Provider
    public LongScreenshotData get() {
        return newInstance();
    }

    public static LongScreenshotData_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static LongScreenshotData newInstance() {
        return new LongScreenshotData();
    }
}
