package com.android.systemui.dagger;

import com.android.internal.logging.UiEventLogger;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
/* loaded from: classes.dex */
public final class GlobalModule_ProvideUiEventLoggerFactory implements Factory<UiEventLogger> {

    /* access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class InstanceHolder {
        private static final GlobalModule_ProvideUiEventLoggerFactory INSTANCE = new GlobalModule_ProvideUiEventLoggerFactory();
    }

    @Override // javax.inject.Provider
    public UiEventLogger get() {
        return provideUiEventLogger();
    }

    public static GlobalModule_ProvideUiEventLoggerFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static UiEventLogger provideUiEventLogger() {
        return (UiEventLogger) Preconditions.checkNotNullFromProvides(GlobalModule.provideUiEventLogger());
    }
}
