package com.android.systemui.statusbar.notification.stack;

import android.content.Context;
import com.android.systemui.statusbar.notification.stack.StackScrollAlgorithm;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class AmbientState_Factory implements Factory<AmbientState> {
    private final Provider<StackScrollAlgorithm.BypassController> bypassControllerProvider;
    private final Provider<Context> contextProvider;
    private final Provider<StackScrollAlgorithm.SectionProvider> sectionProvider;

    public AmbientState_Factory(Provider<Context> provider, Provider<StackScrollAlgorithm.SectionProvider> provider2, Provider<StackScrollAlgorithm.BypassController> provider3) {
        this.contextProvider = provider;
        this.sectionProvider = provider2;
        this.bypassControllerProvider = provider3;
    }

    @Override // javax.inject.Provider
    public AmbientState get() {
        return newInstance(this.contextProvider.get(), this.sectionProvider.get(), this.bypassControllerProvider.get());
    }

    public static AmbientState_Factory create(Provider<Context> provider, Provider<StackScrollAlgorithm.SectionProvider> provider2, Provider<StackScrollAlgorithm.BypassController> provider3) {
        return new AmbientState_Factory(provider, provider2, provider3);
    }

    public static AmbientState newInstance(Context context, StackScrollAlgorithm.SectionProvider sectionProvider, StackScrollAlgorithm.BypassController bypassController) {
        return new AmbientState(context, sectionProvider, bypassController);
    }
}
