package com.android.systemui.wmshell;

import com.android.wm.shell.apppairs.AppPairs;
import com.android.wm.shell.apppairs.AppPairsController;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import java.util.Optional;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class WMShellBaseModule_ProvideAppPairsFactory implements Factory<Optional<AppPairs>> {
    private final Provider<Optional<AppPairsController>> appPairsControllerProvider;

    public WMShellBaseModule_ProvideAppPairsFactory(Provider<Optional<AppPairsController>> provider) {
        this.appPairsControllerProvider = provider;
    }

    @Override // javax.inject.Provider
    public Optional<AppPairs> get() {
        return provideAppPairs(this.appPairsControllerProvider.get());
    }

    public static WMShellBaseModule_ProvideAppPairsFactory create(Provider<Optional<AppPairsController>> provider) {
        return new WMShellBaseModule_ProvideAppPairsFactory(provider);
    }

    public static Optional<AppPairs> provideAppPairs(Optional<AppPairsController> optional) {
        return (Optional) Preconditions.checkNotNullFromProvides(WMShellBaseModule.provideAppPairs(optional));
    }
}
