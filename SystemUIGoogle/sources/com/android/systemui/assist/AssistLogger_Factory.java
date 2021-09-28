package com.android.systemui.assist;

import android.content.Context;
import com.android.internal.app.AssistUtils;
import com.android.internal.logging.UiEventLogger;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class AssistLogger_Factory implements Factory<AssistLogger> {
    private final Provider<AssistUtils> assistUtilsProvider;
    private final Provider<Context> contextProvider;
    private final Provider<PhoneStateMonitor> phoneStateMonitorProvider;
    private final Provider<UiEventLogger> uiEventLoggerProvider;

    public AssistLogger_Factory(Provider<Context> provider, Provider<UiEventLogger> provider2, Provider<AssistUtils> provider3, Provider<PhoneStateMonitor> provider4) {
        this.contextProvider = provider;
        this.uiEventLoggerProvider = provider2;
        this.assistUtilsProvider = provider3;
        this.phoneStateMonitorProvider = provider4;
    }

    @Override // javax.inject.Provider
    public AssistLogger get() {
        return newInstance(this.contextProvider.get(), this.uiEventLoggerProvider.get(), this.assistUtilsProvider.get(), this.phoneStateMonitorProvider.get());
    }

    public static AssistLogger_Factory create(Provider<Context> provider, Provider<UiEventLogger> provider2, Provider<AssistUtils> provider3, Provider<PhoneStateMonitor> provider4) {
        return new AssistLogger_Factory(provider, provider2, provider3, provider4);
    }

    public static AssistLogger newInstance(Context context, UiEventLogger uiEventLogger, AssistUtils assistUtils, PhoneStateMonitor phoneStateMonitor) {
        return new AssistLogger(context, uiEventLogger, assistUtils, phoneStateMonitor);
    }
}
