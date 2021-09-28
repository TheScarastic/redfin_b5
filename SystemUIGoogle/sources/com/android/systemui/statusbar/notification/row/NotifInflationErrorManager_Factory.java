package com.android.systemui.statusbar.notification.row;

import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class NotifInflationErrorManager_Factory implements Factory<NotifInflationErrorManager> {

    /* access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class InstanceHolder {
        private static final NotifInflationErrorManager_Factory INSTANCE = new NotifInflationErrorManager_Factory();
    }

    @Override // javax.inject.Provider
    public NotifInflationErrorManager get() {
        return newInstance();
    }

    public static NotifInflationErrorManager_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static NotifInflationErrorManager newInstance() {
        return new NotifInflationErrorManager();
    }
}
