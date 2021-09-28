package com.android.systemui.wmshell;

import com.android.wm.shell.bubbles.BubbleController;
import com.android.wm.shell.bubbles.Bubbles;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import java.util.Optional;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class WMShellBaseModule_ProvideBubblesFactory implements Factory<Optional<Bubbles>> {
    private final Provider<Optional<BubbleController>> bubbleControllerProvider;

    public WMShellBaseModule_ProvideBubblesFactory(Provider<Optional<BubbleController>> provider) {
        this.bubbleControllerProvider = provider;
    }

    @Override // javax.inject.Provider
    public Optional<Bubbles> get() {
        return provideBubbles(this.bubbleControllerProvider.get());
    }

    public static WMShellBaseModule_ProvideBubblesFactory create(Provider<Optional<BubbleController>> provider) {
        return new WMShellBaseModule_ProvideBubblesFactory(provider);
    }

    public static Optional<Bubbles> provideBubbles(Optional<BubbleController> optional) {
        return (Optional) Preconditions.checkNotNullFromProvides(WMShellBaseModule.provideBubbles(optional));
    }
}
