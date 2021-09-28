package com.google.android.systemui.assist.uihints.input;

import com.google.android.systemui.assist.uihints.GlowController;
import com.google.android.systemui.assist.uihints.ScrimController;
import com.google.android.systemui.assist.uihints.TranscriptionController;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class InputModule_ProvideTouchInsideRegionsFactory implements Factory<Set<TouchInsideRegion>> {
    private final Provider<GlowController> glowControllerProvider;
    private final Provider<ScrimController> scrimControllerProvider;
    private final Provider<TranscriptionController> transcriptionControllerProvider;

    public InputModule_ProvideTouchInsideRegionsFactory(Provider<GlowController> provider, Provider<ScrimController> provider2, Provider<TranscriptionController> provider3) {
        this.glowControllerProvider = provider;
        this.scrimControllerProvider = provider2;
        this.transcriptionControllerProvider = provider3;
    }

    @Override // javax.inject.Provider
    public Set<TouchInsideRegion> get() {
        return provideTouchInsideRegions(this.glowControllerProvider.get(), this.scrimControllerProvider.get(), this.transcriptionControllerProvider.get());
    }

    public static InputModule_ProvideTouchInsideRegionsFactory create(Provider<GlowController> provider, Provider<ScrimController> provider2, Provider<TranscriptionController> provider3) {
        return new InputModule_ProvideTouchInsideRegionsFactory(provider, provider2, provider3);
    }

    public static Set<TouchInsideRegion> provideTouchInsideRegions(GlowController glowController, ScrimController scrimController, TranscriptionController transcriptionController) {
        return (Set) Preconditions.checkNotNullFromProvides(InputModule.provideTouchInsideRegions(glowController, scrimController, transcriptionController));
    }
}
