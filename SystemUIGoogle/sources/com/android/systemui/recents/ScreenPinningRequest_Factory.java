package com.android.systemui.recents;

import android.content.Context;
import com.android.systemui.statusbar.phone.StatusBar;
import dagger.Lazy;
import dagger.internal.Factory;
import java.util.Optional;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class ScreenPinningRequest_Factory implements Factory<ScreenPinningRequest> {
    private final Provider<Context> contextProvider;
    private final Provider<Optional<Lazy<StatusBar>>> statusBarOptionalLazyProvider;

    public ScreenPinningRequest_Factory(Provider<Context> provider, Provider<Optional<Lazy<StatusBar>>> provider2) {
        this.contextProvider = provider;
        this.statusBarOptionalLazyProvider = provider2;
    }

    @Override // javax.inject.Provider
    public ScreenPinningRequest get() {
        return newInstance(this.contextProvider.get(), this.statusBarOptionalLazyProvider.get());
    }

    public static ScreenPinningRequest_Factory create(Provider<Context> provider, Provider<Optional<Lazy<StatusBar>>> provider2) {
        return new ScreenPinningRequest_Factory(provider, provider2);
    }

    public static ScreenPinningRequest newInstance(Context context, Optional<Lazy<StatusBar>> optional) {
        return new ScreenPinningRequest(context, optional);
    }
}
