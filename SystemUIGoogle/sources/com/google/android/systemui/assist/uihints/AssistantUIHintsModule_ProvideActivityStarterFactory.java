package com.google.android.systemui.assist.uihints;

import com.android.systemui.statusbar.phone.StatusBar;
import com.google.android.systemui.assist.uihints.NgaMessageHandler;
import dagger.Lazy;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class AssistantUIHintsModule_ProvideActivityStarterFactory implements Factory<NgaMessageHandler.StartActivityInfoListener> {
    private final Provider<StatusBar> statusBarLazyProvider;

    public AssistantUIHintsModule_ProvideActivityStarterFactory(Provider<StatusBar> provider) {
        this.statusBarLazyProvider = provider;
    }

    @Override // javax.inject.Provider
    public NgaMessageHandler.StartActivityInfoListener get() {
        return provideActivityStarter(DoubleCheck.lazy(this.statusBarLazyProvider));
    }

    public static AssistantUIHintsModule_ProvideActivityStarterFactory create(Provider<StatusBar> provider) {
        return new AssistantUIHintsModule_ProvideActivityStarterFactory(provider);
    }

    public static NgaMessageHandler.StartActivityInfoListener provideActivityStarter(Lazy<StatusBar> lazy) {
        return (NgaMessageHandler.StartActivityInfoListener) Preconditions.checkNotNullFromProvides(AssistantUIHintsModule.provideActivityStarter(lazy));
    }
}
