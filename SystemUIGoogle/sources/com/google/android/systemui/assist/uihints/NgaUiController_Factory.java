package com.google.android.systemui.assist.uihints;

import android.content.Context;
import com.android.systemui.assist.AssistLogger;
import com.android.systemui.assist.AssistManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.google.android.systemui.assist.uihints.edgelights.EdgeLightsController;
import dagger.Lazy;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class NgaUiController_Factory implements Factory<NgaUiController> {
    private final Provider<AssistLogger> assistLoggerProvider;
    private final Provider<AssistManager> assistManagerProvider;
    private final Provider<AssistantPresenceHandler> assistantPresenceHandlerProvider;
    private final Provider<AssistantWarmer> assistantWarmerProvider;
    private final Provider<ColorChangeHandler> colorChangeHandlerProvider;
    private final Provider<Context> contextProvider;
    private final Provider<EdgeLightsController> edgeLightsControllerProvider;
    private final Provider<FlingVelocityWrapper> flingVelocityProvider;
    private final Provider<GlowController> glowControllerProvider;
    private final Provider<IconController> iconControllerProvider;
    private final Provider<LightnessProvider> lightnessProvider;
    private final Provider<NavBarFader> navBarFaderProvider;
    private final Provider<ScrimController> scrimControllerProvider;
    private final Provider<StatusBarStateController> statusBarStateControllerProvider;
    private final Provider<TimeoutManager> timeoutManagerProvider;
    private final Provider<TouchInsideHandler> touchInsideHandlerProvider;
    private final Provider<TranscriptionController> transcriptionControllerProvider;
    private final Provider<OverlayUiHost> uiHostProvider;

    public NgaUiController_Factory(Provider<Context> provider, Provider<TimeoutManager> provider2, Provider<AssistantPresenceHandler> provider3, Provider<TouchInsideHandler> provider4, Provider<ColorChangeHandler> provider5, Provider<OverlayUiHost> provider6, Provider<EdgeLightsController> provider7, Provider<GlowController> provider8, Provider<ScrimController> provider9, Provider<TranscriptionController> provider10, Provider<IconController> provider11, Provider<LightnessProvider> provider12, Provider<StatusBarStateController> provider13, Provider<AssistManager> provider14, Provider<FlingVelocityWrapper> provider15, Provider<AssistantWarmer> provider16, Provider<NavBarFader> provider17, Provider<AssistLogger> provider18) {
        this.contextProvider = provider;
        this.timeoutManagerProvider = provider2;
        this.assistantPresenceHandlerProvider = provider3;
        this.touchInsideHandlerProvider = provider4;
        this.colorChangeHandlerProvider = provider5;
        this.uiHostProvider = provider6;
        this.edgeLightsControllerProvider = provider7;
        this.glowControllerProvider = provider8;
        this.scrimControllerProvider = provider9;
        this.transcriptionControllerProvider = provider10;
        this.iconControllerProvider = provider11;
        this.lightnessProvider = provider12;
        this.statusBarStateControllerProvider = provider13;
        this.assistManagerProvider = provider14;
        this.flingVelocityProvider = provider15;
        this.assistantWarmerProvider = provider16;
        this.navBarFaderProvider = provider17;
        this.assistLoggerProvider = provider18;
    }

    @Override // javax.inject.Provider
    public NgaUiController get() {
        return newInstance(this.contextProvider.get(), this.timeoutManagerProvider.get(), this.assistantPresenceHandlerProvider.get(), this.touchInsideHandlerProvider.get(), this.colorChangeHandlerProvider.get(), this.uiHostProvider.get(), this.edgeLightsControllerProvider.get(), this.glowControllerProvider.get(), this.scrimControllerProvider.get(), this.transcriptionControllerProvider.get(), this.iconControllerProvider.get(), this.lightnessProvider.get(), this.statusBarStateControllerProvider.get(), DoubleCheck.lazy(this.assistManagerProvider), this.flingVelocityProvider.get(), this.assistantWarmerProvider.get(), this.navBarFaderProvider.get(), this.assistLoggerProvider.get());
    }

    public static NgaUiController_Factory create(Provider<Context> provider, Provider<TimeoutManager> provider2, Provider<AssistantPresenceHandler> provider3, Provider<TouchInsideHandler> provider4, Provider<ColorChangeHandler> provider5, Provider<OverlayUiHost> provider6, Provider<EdgeLightsController> provider7, Provider<GlowController> provider8, Provider<ScrimController> provider9, Provider<TranscriptionController> provider10, Provider<IconController> provider11, Provider<LightnessProvider> provider12, Provider<StatusBarStateController> provider13, Provider<AssistManager> provider14, Provider<FlingVelocityWrapper> provider15, Provider<AssistantWarmer> provider16, Provider<NavBarFader> provider17, Provider<AssistLogger> provider18) {
        return new NgaUiController_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14, provider15, provider16, provider17, provider18);
    }

    public static NgaUiController newInstance(Context context, Object obj, AssistantPresenceHandler assistantPresenceHandler, TouchInsideHandler touchInsideHandler, ColorChangeHandler colorChangeHandler, Object obj2, EdgeLightsController edgeLightsController, GlowController glowController, ScrimController scrimController, TranscriptionController transcriptionController, IconController iconController, Object obj3, StatusBarStateController statusBarStateController, Lazy<AssistManager> lazy, Object obj4, AssistantWarmer assistantWarmer, NavBarFader navBarFader, AssistLogger assistLogger) {
        return new NgaUiController(context, (TimeoutManager) obj, assistantPresenceHandler, touchInsideHandler, colorChangeHandler, (OverlayUiHost) obj2, edgeLightsController, glowController, scrimController, transcriptionController, iconController, (LightnessProvider) obj3, statusBarStateController, lazy, (FlingVelocityWrapper) obj4, assistantWarmer, navBarFader, assistLogger);
    }
}
