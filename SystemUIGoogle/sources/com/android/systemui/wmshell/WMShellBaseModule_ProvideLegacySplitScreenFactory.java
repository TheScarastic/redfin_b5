package com.android.systemui.wmshell;

import com.android.wm.shell.legacysplitscreen.LegacySplitScreen;
import com.android.wm.shell.legacysplitscreen.LegacySplitScreenController;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import java.util.Optional;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class WMShellBaseModule_ProvideLegacySplitScreenFactory implements Factory<Optional<LegacySplitScreen>> {
    private final Provider<Optional<LegacySplitScreenController>> splitScreenControllerProvider;

    public WMShellBaseModule_ProvideLegacySplitScreenFactory(Provider<Optional<LegacySplitScreenController>> provider) {
        this.splitScreenControllerProvider = provider;
    }

    @Override // javax.inject.Provider
    public Optional<LegacySplitScreen> get() {
        return provideLegacySplitScreen(this.splitScreenControllerProvider.get());
    }

    public static WMShellBaseModule_ProvideLegacySplitScreenFactory create(Provider<Optional<LegacySplitScreenController>> provider) {
        return new WMShellBaseModule_ProvideLegacySplitScreenFactory(provider);
    }

    public static Optional<LegacySplitScreen> provideLegacySplitScreen(Optional<LegacySplitScreenController> optional) {
        return (Optional) Preconditions.checkNotNullFromProvides(WMShellBaseModule.provideLegacySplitScreen(optional));
    }
}
