package com.google.android.systemui.assist.uihints.input;

import com.google.android.systemui.assist.uihints.IconController;
import com.google.android.systemui.assist.uihints.TranscriptionController;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class InputModule_ProvideTouchActionRegionsFactory implements Factory<Set<TouchActionRegion>> {
    private final Provider<IconController> iconControllerProvider;
    private final Provider<TranscriptionController> transcriptionControllerProvider;

    public InputModule_ProvideTouchActionRegionsFactory(Provider<IconController> provider, Provider<TranscriptionController> provider2) {
        this.iconControllerProvider = provider;
        this.transcriptionControllerProvider = provider2;
    }

    @Override // javax.inject.Provider
    public Set<TouchActionRegion> get() {
        return provideTouchActionRegions(this.iconControllerProvider.get(), this.transcriptionControllerProvider.get());
    }

    public static InputModule_ProvideTouchActionRegionsFactory create(Provider<IconController> provider, Provider<TranscriptionController> provider2) {
        return new InputModule_ProvideTouchActionRegionsFactory(provider, provider2);
    }

    public static Set<TouchActionRegion> provideTouchActionRegions(IconController iconController, TranscriptionController transcriptionController) {
        return (Set) Preconditions.checkNotNullFromProvides(InputModule.provideTouchActionRegions(iconController, transcriptionController));
    }
}
