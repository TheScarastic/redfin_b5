package com.android.systemui.tv;

import dagger.internal.Factory;
/* loaded from: classes2.dex */
public final class TvSystemUIModule_ProvideAllowNotificationLongPressFactory implements Factory<Boolean> {

    /* loaded from: classes2.dex */
    private static final class InstanceHolder {
        private static final TvSystemUIModule_ProvideAllowNotificationLongPressFactory INSTANCE = new TvSystemUIModule_ProvideAllowNotificationLongPressFactory();
    }

    @Override // javax.inject.Provider
    public Boolean get() {
        return Boolean.valueOf(provideAllowNotificationLongPress());
    }

    public static TvSystemUIModule_ProvideAllowNotificationLongPressFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static boolean provideAllowNotificationLongPress() {
        return TvSystemUIModule.provideAllowNotificationLongPress();
    }
}
