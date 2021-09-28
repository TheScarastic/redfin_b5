package com.google.android.systemui.dagger;

import dagger.internal.Factory;
/* loaded from: classes2.dex */
public final class SystemUIGoogleModule_ProvideAllowNotificationLongPressFactory implements Factory<Boolean> {

    /* access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static final class InstanceHolder {
        private static final SystemUIGoogleModule_ProvideAllowNotificationLongPressFactory INSTANCE = new SystemUIGoogleModule_ProvideAllowNotificationLongPressFactory();
    }

    @Override // javax.inject.Provider
    public Boolean get() {
        return Boolean.valueOf(provideAllowNotificationLongPress());
    }

    public static SystemUIGoogleModule_ProvideAllowNotificationLongPressFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static boolean provideAllowNotificationLongPress() {
        return SystemUIGoogleModule.provideAllowNotificationLongPress();
    }
}
