package com.google.android.systemui.assist.uihints;

import com.android.systemui.statusbar.phone.StatusBar;
import dagger.Lazy;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class OverlappedElementController_Factory implements Factory<OverlappedElementController> {
    private final Provider<StatusBar> statusBarLazyProvider;

    public OverlappedElementController_Factory(Provider<StatusBar> provider) {
        this.statusBarLazyProvider = provider;
    }

    @Override // javax.inject.Provider
    public OverlappedElementController get() {
        return newInstance(DoubleCheck.lazy(this.statusBarLazyProvider));
    }

    public static OverlappedElementController_Factory create(Provider<StatusBar> provider) {
        return new OverlappedElementController_Factory(provider);
    }

    public static OverlappedElementController newInstance(Lazy<StatusBar> lazy) {
        return new OverlappedElementController(lazy);
    }
}
