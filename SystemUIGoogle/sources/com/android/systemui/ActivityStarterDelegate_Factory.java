package com.android.systemui;

import com.android.systemui.statusbar.phone.StatusBar;
import dagger.Lazy;
import dagger.internal.Factory;
import java.util.Optional;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class ActivityStarterDelegate_Factory implements Factory<ActivityStarterDelegate> {
    private final Provider<Optional<Lazy<StatusBar>>> statusBarProvider;

    public ActivityStarterDelegate_Factory(Provider<Optional<Lazy<StatusBar>>> provider) {
        this.statusBarProvider = provider;
    }

    @Override // javax.inject.Provider
    public ActivityStarterDelegate get() {
        return newInstance(this.statusBarProvider.get());
    }

    public static ActivityStarterDelegate_Factory create(Provider<Optional<Lazy<StatusBar>>> provider) {
        return new ActivityStarterDelegate_Factory(provider);
    }

    public static ActivityStarterDelegate newInstance(Optional<Lazy<StatusBar>> optional) {
        return new ActivityStarterDelegate(optional);
    }
}
