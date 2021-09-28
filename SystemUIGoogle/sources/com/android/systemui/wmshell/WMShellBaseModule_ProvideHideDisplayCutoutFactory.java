package com.android.systemui.wmshell;

import com.android.wm.shell.hidedisplaycutout.HideDisplayCutout;
import com.android.wm.shell.hidedisplaycutout.HideDisplayCutoutController;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import java.util.Optional;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class WMShellBaseModule_ProvideHideDisplayCutoutFactory implements Factory<Optional<HideDisplayCutout>> {
    private final Provider<Optional<HideDisplayCutoutController>> hideDisplayCutoutControllerProvider;

    public WMShellBaseModule_ProvideHideDisplayCutoutFactory(Provider<Optional<HideDisplayCutoutController>> provider) {
        this.hideDisplayCutoutControllerProvider = provider;
    }

    @Override // javax.inject.Provider
    public Optional<HideDisplayCutout> get() {
        return provideHideDisplayCutout(this.hideDisplayCutoutControllerProvider.get());
    }

    public static WMShellBaseModule_ProvideHideDisplayCutoutFactory create(Provider<Optional<HideDisplayCutoutController>> provider) {
        return new WMShellBaseModule_ProvideHideDisplayCutoutFactory(provider);
    }

    public static Optional<HideDisplayCutout> provideHideDisplayCutout(Optional<HideDisplayCutoutController> optional) {
        return (Optional) Preconditions.checkNotNullFromProvides(WMShellBaseModule.provideHideDisplayCutout(optional));
    }
}
