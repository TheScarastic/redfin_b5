package com.google.android.systemui.assist.uihints;

import com.google.android.systemui.assist.uihints.NgaMessageHandler;
import com.google.android.systemui.assist.uihints.edgelights.EdgeLightsController;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class AssistantUIHintsModule_ProvideAudioInfoListenersFactory implements Factory<Set<NgaMessageHandler.AudioInfoListener>> {
    private final Provider<EdgeLightsController> edgeLightsControllerProvider;
    private final Provider<GlowController> glowControllerProvider;

    public AssistantUIHintsModule_ProvideAudioInfoListenersFactory(Provider<EdgeLightsController> provider, Provider<GlowController> provider2) {
        this.edgeLightsControllerProvider = provider;
        this.glowControllerProvider = provider2;
    }

    @Override // javax.inject.Provider
    public Set<NgaMessageHandler.AudioInfoListener> get() {
        return provideAudioInfoListeners(this.edgeLightsControllerProvider.get(), this.glowControllerProvider.get());
    }

    public static AssistantUIHintsModule_ProvideAudioInfoListenersFactory create(Provider<EdgeLightsController> provider, Provider<GlowController> provider2) {
        return new AssistantUIHintsModule_ProvideAudioInfoListenersFactory(provider, provider2);
    }

    public static Set<NgaMessageHandler.AudioInfoListener> provideAudioInfoListeners(EdgeLightsController edgeLightsController, GlowController glowController) {
        return (Set) Preconditions.checkNotNullFromProvides(AssistantUIHintsModule.provideAudioInfoListeners(edgeLightsController, glowController));
    }
}
