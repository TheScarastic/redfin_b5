package com.google.android.systemui.assist.uihints;

import android.os.Handler;
import com.android.systemui.navigationbar.NavigationModeController;
import com.google.android.systemui.assist.uihints.NgaMessageHandler;
import dagger.internal.Factory;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class NgaMessageHandler_Factory implements Factory<NgaMessageHandler> {
    private final Provider<AssistantPresenceHandler> assistantPresenceHandlerProvider;
    private final Provider<Set<NgaMessageHandler.AudioInfoListener>> audioInfoListenersProvider;
    private final Provider<Set<NgaMessageHandler.CardInfoListener>> cardInfoListenersProvider;
    private final Provider<Set<NgaMessageHandler.ChipsInfoListener>> chipsInfoListenersProvider;
    private final Provider<Set<NgaMessageHandler.ClearListener>> clearListenersProvider;
    private final Provider<Set<NgaMessageHandler.ConfigInfoListener>> configInfoListenersProvider;
    private final Provider<Set<NgaMessageHandler.EdgeLightsInfoListener>> edgeLightsInfoListenersProvider;
    private final Provider<Set<NgaMessageHandler.GoBackListener>> goBackListenersProvider;
    private final Provider<Set<NgaMessageHandler.GreetingInfoListener>> greetingInfoListenersProvider;
    private final Provider<Handler> handlerProvider;
    private final Provider<Set<NgaMessageHandler.KeepAliveListener>> keepAliveListenersProvider;
    private final Provider<Set<NgaMessageHandler.KeyboardInfoListener>> keyboardInfoListenersProvider;
    private final Provider<Set<NgaMessageHandler.NavBarVisibilityListener>> navBarVisibilityListenersProvider;
    private final Provider<NavigationModeController> navigationModeControllerProvider;
    private final Provider<NgaUiController> ngaUiControllerProvider;
    private final Provider<Set<NgaMessageHandler.StartActivityInfoListener>> startActivityInfoListenersProvider;
    private final Provider<Set<NgaMessageHandler.TakeScreenshotListener>> takeScreenshotListenersProvider;
    private final Provider<Set<NgaMessageHandler.TranscriptionInfoListener>> transcriptionInfoListenersProvider;
    private final Provider<Set<NgaMessageHandler.WarmingListener>> warmingListenersProvider;
    private final Provider<Set<NgaMessageHandler.ZerostateInfoListener>> zerostateInfoListenersProvider;

    public NgaMessageHandler_Factory(Provider<NgaUiController> provider, Provider<AssistantPresenceHandler> provider2, Provider<NavigationModeController> provider3, Provider<Set<NgaMessageHandler.KeepAliveListener>> provider4, Provider<Set<NgaMessageHandler.AudioInfoListener>> provider5, Provider<Set<NgaMessageHandler.CardInfoListener>> provider6, Provider<Set<NgaMessageHandler.ConfigInfoListener>> provider7, Provider<Set<NgaMessageHandler.EdgeLightsInfoListener>> provider8, Provider<Set<NgaMessageHandler.TranscriptionInfoListener>> provider9, Provider<Set<NgaMessageHandler.GreetingInfoListener>> provider10, Provider<Set<NgaMessageHandler.ChipsInfoListener>> provider11, Provider<Set<NgaMessageHandler.ClearListener>> provider12, Provider<Set<NgaMessageHandler.StartActivityInfoListener>> provider13, Provider<Set<NgaMessageHandler.KeyboardInfoListener>> provider14, Provider<Set<NgaMessageHandler.ZerostateInfoListener>> provider15, Provider<Set<NgaMessageHandler.GoBackListener>> provider16, Provider<Set<NgaMessageHandler.TakeScreenshotListener>> provider17, Provider<Set<NgaMessageHandler.WarmingListener>> provider18, Provider<Set<NgaMessageHandler.NavBarVisibilityListener>> provider19, Provider<Handler> provider20) {
        this.ngaUiControllerProvider = provider;
        this.assistantPresenceHandlerProvider = provider2;
        this.navigationModeControllerProvider = provider3;
        this.keepAliveListenersProvider = provider4;
        this.audioInfoListenersProvider = provider5;
        this.cardInfoListenersProvider = provider6;
        this.configInfoListenersProvider = provider7;
        this.edgeLightsInfoListenersProvider = provider8;
        this.transcriptionInfoListenersProvider = provider9;
        this.greetingInfoListenersProvider = provider10;
        this.chipsInfoListenersProvider = provider11;
        this.clearListenersProvider = provider12;
        this.startActivityInfoListenersProvider = provider13;
        this.keyboardInfoListenersProvider = provider14;
        this.zerostateInfoListenersProvider = provider15;
        this.goBackListenersProvider = provider16;
        this.takeScreenshotListenersProvider = provider17;
        this.warmingListenersProvider = provider18;
        this.navBarVisibilityListenersProvider = provider19;
        this.handlerProvider = provider20;
    }

    @Override // javax.inject.Provider
    public NgaMessageHandler get() {
        return newInstance(this.ngaUiControllerProvider.get(), this.assistantPresenceHandlerProvider.get(), this.navigationModeControllerProvider.get(), this.keepAliveListenersProvider.get(), this.audioInfoListenersProvider.get(), this.cardInfoListenersProvider.get(), this.configInfoListenersProvider.get(), this.edgeLightsInfoListenersProvider.get(), this.transcriptionInfoListenersProvider.get(), this.greetingInfoListenersProvider.get(), this.chipsInfoListenersProvider.get(), this.clearListenersProvider.get(), this.startActivityInfoListenersProvider.get(), this.keyboardInfoListenersProvider.get(), this.zerostateInfoListenersProvider.get(), this.goBackListenersProvider.get(), this.takeScreenshotListenersProvider.get(), this.warmingListenersProvider.get(), this.navBarVisibilityListenersProvider.get(), this.handlerProvider.get());
    }

    public static NgaMessageHandler_Factory create(Provider<NgaUiController> provider, Provider<AssistantPresenceHandler> provider2, Provider<NavigationModeController> provider3, Provider<Set<NgaMessageHandler.KeepAliveListener>> provider4, Provider<Set<NgaMessageHandler.AudioInfoListener>> provider5, Provider<Set<NgaMessageHandler.CardInfoListener>> provider6, Provider<Set<NgaMessageHandler.ConfigInfoListener>> provider7, Provider<Set<NgaMessageHandler.EdgeLightsInfoListener>> provider8, Provider<Set<NgaMessageHandler.TranscriptionInfoListener>> provider9, Provider<Set<NgaMessageHandler.GreetingInfoListener>> provider10, Provider<Set<NgaMessageHandler.ChipsInfoListener>> provider11, Provider<Set<NgaMessageHandler.ClearListener>> provider12, Provider<Set<NgaMessageHandler.StartActivityInfoListener>> provider13, Provider<Set<NgaMessageHandler.KeyboardInfoListener>> provider14, Provider<Set<NgaMessageHandler.ZerostateInfoListener>> provider15, Provider<Set<NgaMessageHandler.GoBackListener>> provider16, Provider<Set<NgaMessageHandler.TakeScreenshotListener>> provider17, Provider<Set<NgaMessageHandler.WarmingListener>> provider18, Provider<Set<NgaMessageHandler.NavBarVisibilityListener>> provider19, Provider<Handler> provider20) {
        return new NgaMessageHandler_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14, provider15, provider16, provider17, provider18, provider19, provider20);
    }

    public static NgaMessageHandler newInstance(NgaUiController ngaUiController, AssistantPresenceHandler assistantPresenceHandler, NavigationModeController navigationModeController, Set<NgaMessageHandler.KeepAliveListener> set, Set<NgaMessageHandler.AudioInfoListener> set2, Set<NgaMessageHandler.CardInfoListener> set3, Set<NgaMessageHandler.ConfigInfoListener> set4, Set<NgaMessageHandler.EdgeLightsInfoListener> set5, Set<NgaMessageHandler.TranscriptionInfoListener> set6, Set<NgaMessageHandler.GreetingInfoListener> set7, Set<NgaMessageHandler.ChipsInfoListener> set8, Set<NgaMessageHandler.ClearListener> set9, Set<NgaMessageHandler.StartActivityInfoListener> set10, Set<NgaMessageHandler.KeyboardInfoListener> set11, Set<NgaMessageHandler.ZerostateInfoListener> set12, Set<NgaMessageHandler.GoBackListener> set13, Set<NgaMessageHandler.TakeScreenshotListener> set14, Set<NgaMessageHandler.WarmingListener> set15, Set<NgaMessageHandler.NavBarVisibilityListener> set16, Handler handler) {
        return new NgaMessageHandler(ngaUiController, assistantPresenceHandler, navigationModeController, set, set2, set3, set4, set5, set6, set7, set8, set9, set10, set11, set12, set13, set14, set15, set16, handler);
    }
}
