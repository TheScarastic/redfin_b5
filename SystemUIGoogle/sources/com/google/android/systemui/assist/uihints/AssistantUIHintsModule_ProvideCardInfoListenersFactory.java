package com.google.android.systemui.assist.uihints;

import com.google.android.systemui.assist.uihints.NgaMessageHandler;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class AssistantUIHintsModule_ProvideCardInfoListenersFactory implements Factory<Set<NgaMessageHandler.CardInfoListener>> {
    private final Provider<GlowController> glowControllerProvider;
    private final Provider<LightnessProvider> lightnessProvider;
    private final Provider<ScrimController> scrimControllerProvider;
    private final Provider<TranscriptionController> transcriptionControllerProvider;

    public AssistantUIHintsModule_ProvideCardInfoListenersFactory(Provider<GlowController> provider, Provider<ScrimController> provider2, Provider<TranscriptionController> provider3, Provider<LightnessProvider> provider4) {
        this.glowControllerProvider = provider;
        this.scrimControllerProvider = provider2;
        this.transcriptionControllerProvider = provider3;
        this.lightnessProvider = provider4;
    }

    @Override // javax.inject.Provider
    public Set<NgaMessageHandler.CardInfoListener> get() {
        return provideCardInfoListeners(this.glowControllerProvider.get(), this.scrimControllerProvider.get(), this.transcriptionControllerProvider.get(), this.lightnessProvider.get());
    }

    public static AssistantUIHintsModule_ProvideCardInfoListenersFactory create(Provider<GlowController> provider, Provider<ScrimController> provider2, Provider<TranscriptionController> provider3, Provider<LightnessProvider> provider4) {
        return new AssistantUIHintsModule_ProvideCardInfoListenersFactory(provider, provider2, provider3, provider4);
    }

    public static Set<NgaMessageHandler.CardInfoListener> provideCardInfoListeners(GlowController glowController, ScrimController scrimController, TranscriptionController transcriptionController, Object obj) {
        return (Set) Preconditions.checkNotNullFromProvides(AssistantUIHintsModule.provideCardInfoListeners(glowController, scrimController, transcriptionController, (LightnessProvider) obj));
    }
}
