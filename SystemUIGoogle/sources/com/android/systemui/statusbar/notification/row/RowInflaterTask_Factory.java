package com.android.systemui.statusbar.notification.row;

import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class RowInflaterTask_Factory implements Factory<RowInflaterTask> {

    /* access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class InstanceHolder {
        private static final RowInflaterTask_Factory INSTANCE = new RowInflaterTask_Factory();
    }

    @Override // javax.inject.Provider
    public RowInflaterTask get() {
        return newInstance();
    }

    public static RowInflaterTask_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static RowInflaterTask newInstance() {
        return new RowInflaterTask();
    }
}
