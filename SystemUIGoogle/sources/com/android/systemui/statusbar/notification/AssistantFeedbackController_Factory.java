package com.android.systemui.statusbar.notification;

import android.content.Context;
import android.os.Handler;
import com.android.systemui.util.DeviceConfigProxy;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class AssistantFeedbackController_Factory implements Factory<AssistantFeedbackController> {
    private final Provider<Context> contextProvider;
    private final Provider<Handler> handlerProvider;
    private final Provider<DeviceConfigProxy> proxyProvider;

    public AssistantFeedbackController_Factory(Provider<Handler> provider, Provider<Context> provider2, Provider<DeviceConfigProxy> provider3) {
        this.handlerProvider = provider;
        this.contextProvider = provider2;
        this.proxyProvider = provider3;
    }

    @Override // javax.inject.Provider
    public AssistantFeedbackController get() {
        return newInstance(this.handlerProvider.get(), this.contextProvider.get(), this.proxyProvider.get());
    }

    public static AssistantFeedbackController_Factory create(Provider<Handler> provider, Provider<Context> provider2, Provider<DeviceConfigProxy> provider3) {
        return new AssistantFeedbackController_Factory(provider, provider2, provider3);
    }

    public static AssistantFeedbackController newInstance(Handler handler, Context context, DeviceConfigProxy deviceConfigProxy) {
        return new AssistantFeedbackController(handler, context, deviceConfigProxy);
    }
}
