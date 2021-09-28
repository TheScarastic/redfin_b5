package com.android.systemui.dagger;

import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class SystemUIDefaultModule_ProvideAllowNotificationLongPressFactory implements Factory<Boolean> {

    /* access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class InstanceHolder {
        private static final SystemUIDefaultModule_ProvideAllowNotificationLongPressFactory INSTANCE = new SystemUIDefaultModule_ProvideAllowNotificationLongPressFactory();
    }

    @Override // javax.inject.Provider
    public Boolean get() {
        return Boolean.valueOf(provideAllowNotificationLongPress());
    }

    public static SystemUIDefaultModule_ProvideAllowNotificationLongPressFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static boolean provideAllowNotificationLongPress() {
        return SystemUIDefaultModule.provideAllowNotificationLongPress();
    }
}
