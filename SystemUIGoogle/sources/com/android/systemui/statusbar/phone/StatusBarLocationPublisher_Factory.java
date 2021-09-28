package com.android.systemui.statusbar.phone;

import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class StatusBarLocationPublisher_Factory implements Factory<StatusBarLocationPublisher> {

    /* access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class InstanceHolder {
        private static final StatusBarLocationPublisher_Factory INSTANCE = new StatusBarLocationPublisher_Factory();
    }

    @Override // javax.inject.Provider
    public StatusBarLocationPublisher get() {
        return newInstance();
    }

    public static StatusBarLocationPublisher_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static StatusBarLocationPublisher newInstance() {
        return new StatusBarLocationPublisher();
    }
}
