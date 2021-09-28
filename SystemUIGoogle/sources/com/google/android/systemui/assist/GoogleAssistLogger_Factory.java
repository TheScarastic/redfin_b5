package com.google.android.systemui.assist;

import android.content.Context;
import com.android.internal.app.AssistUtils;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.assist.PhoneStateMonitor;
import com.google.android.systemui.assist.uihints.AssistantPresenceHandler;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class GoogleAssistLogger_Factory implements Factory<GoogleAssistLogger> {
    private final Provider<AssistUtils> assistUtilsProvider;
    private final Provider<AssistantPresenceHandler> assistantPresenceHandlerProvider;
    private final Provider<Context> contextProvider;
    private final Provider<PhoneStateMonitor> phoneStateMonitorProvider;
    private final Provider<UiEventLogger> uiEventLoggerProvider;

    public GoogleAssistLogger_Factory(Provider<Context> provider, Provider<UiEventLogger> provider2, Provider<AssistUtils> provider3, Provider<PhoneStateMonitor> provider4, Provider<AssistantPresenceHandler> provider5) {
        this.contextProvider = provider;
        this.uiEventLoggerProvider = provider2;
        this.assistUtilsProvider = provider3;
        this.phoneStateMonitorProvider = provider4;
        this.assistantPresenceHandlerProvider = provider5;
    }

    @Override // javax.inject.Provider
    public GoogleAssistLogger get() {
        return newInstance(this.contextProvider.get(), this.uiEventLoggerProvider.get(), this.assistUtilsProvider.get(), this.phoneStateMonitorProvider.get(), this.assistantPresenceHandlerProvider.get());
    }

    public static GoogleAssistLogger_Factory create(Provider<Context> provider, Provider<UiEventLogger> provider2, Provider<AssistUtils> provider3, Provider<PhoneStateMonitor> provider4, Provider<AssistantPresenceHandler> provider5) {
        return new GoogleAssistLogger_Factory(provider, provider2, provider3, provider4, provider5);
    }

    public static GoogleAssistLogger newInstance(Context context, UiEventLogger uiEventLogger, AssistUtils assistUtils, PhoneStateMonitor phoneStateMonitor, AssistantPresenceHandler assistantPresenceHandler) {
        return new GoogleAssistLogger(context, uiEventLogger, assistUtils, phoneStateMonitor, assistantPresenceHandler);
    }
}
