package com.google.android.systemui.assist.uihints;

import android.view.ViewGroup;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class AssistantUIHintsModule_ProvideParentViewGroupFactory implements Factory<ViewGroup> {
    private final Provider<OverlayUiHost> overlayUiHostProvider;

    public AssistantUIHintsModule_ProvideParentViewGroupFactory(Provider<OverlayUiHost> provider) {
        this.overlayUiHostProvider = provider;
    }

    @Override // javax.inject.Provider
    public ViewGroup get() {
        return provideParentViewGroup(this.overlayUiHostProvider.get());
    }

    public static AssistantUIHintsModule_ProvideParentViewGroupFactory create(Provider<OverlayUiHost> provider) {
        return new AssistantUIHintsModule_ProvideParentViewGroupFactory(provider);
    }

    public static ViewGroup provideParentViewGroup(Object obj) {
        return (ViewGroup) Preconditions.checkNotNullFromProvides(AssistantUIHintsModule.provideParentViewGroup((OverlayUiHost) obj));
    }
}
