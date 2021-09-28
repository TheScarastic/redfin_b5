package com.google.android.systemui.assist.uihints;

import com.google.android.systemui.assist.uihints.NgaMessageHandler;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class AssistantUIHintsModule_ProvideConfigInfoListenersFactory implements Factory<Set<NgaMessageHandler.ConfigInfoListener>> {
    private final Provider<AssistantPresenceHandler> assistantPresenceHandlerProvider;
    private final Provider<ColorChangeHandler> colorChangeHandlerProvider;
    private final Provider<ConfigurationHandler> configurationHandlerProvider;
    private final Provider<KeyboardMonitor> keyboardMonitorProvider;
    private final Provider<TaskStackNotifier> taskStackNotifierProvider;
    private final Provider<TouchInsideHandler> touchInsideHandlerProvider;
    private final Provider<TouchOutsideHandler> touchOutsideHandlerProvider;

    public AssistantUIHintsModule_ProvideConfigInfoListenersFactory(Provider<AssistantPresenceHandler> provider, Provider<TouchInsideHandler> provider2, Provider<TouchOutsideHandler> provider3, Provider<TaskStackNotifier> provider4, Provider<KeyboardMonitor> provider5, Provider<ColorChangeHandler> provider6, Provider<ConfigurationHandler> provider7) {
        this.assistantPresenceHandlerProvider = provider;
        this.touchInsideHandlerProvider = provider2;
        this.touchOutsideHandlerProvider = provider3;
        this.taskStackNotifierProvider = provider4;
        this.keyboardMonitorProvider = provider5;
        this.colorChangeHandlerProvider = provider6;
        this.configurationHandlerProvider = provider7;
    }

    @Override // javax.inject.Provider
    public Set<NgaMessageHandler.ConfigInfoListener> get() {
        return provideConfigInfoListeners(this.assistantPresenceHandlerProvider.get(), this.touchInsideHandlerProvider.get(), this.touchOutsideHandlerProvider.get(), this.taskStackNotifierProvider.get(), this.keyboardMonitorProvider.get(), this.colorChangeHandlerProvider.get(), this.configurationHandlerProvider.get());
    }

    public static AssistantUIHintsModule_ProvideConfigInfoListenersFactory create(Provider<AssistantPresenceHandler> provider, Provider<TouchInsideHandler> provider2, Provider<TouchOutsideHandler> provider3, Provider<TaskStackNotifier> provider4, Provider<KeyboardMonitor> provider5, Provider<ColorChangeHandler> provider6, Provider<ConfigurationHandler> provider7) {
        return new AssistantUIHintsModule_ProvideConfigInfoListenersFactory(provider, provider2, provider3, provider4, provider5, provider6, provider7);
    }

    public static Set<NgaMessageHandler.ConfigInfoListener> provideConfigInfoListeners(AssistantPresenceHandler assistantPresenceHandler, TouchInsideHandler touchInsideHandler, Object obj, Object obj2, Object obj3, ColorChangeHandler colorChangeHandler, ConfigurationHandler configurationHandler) {
        return (Set) Preconditions.checkNotNullFromProvides(AssistantUIHintsModule.provideConfigInfoListeners(assistantPresenceHandler, touchInsideHandler, (TouchOutsideHandler) obj, (TaskStackNotifier) obj2, (KeyboardMonitor) obj3, colorChangeHandler, configurationHandler));
    }
}
