package com.google.android.systemui.elmyra.actions;

import android.content.Context;
import com.android.systemui.statusbar.phone.StatusBar;
import com.google.android.systemui.elmyra.actions.LaunchOpa;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class LaunchOpa_Builder_Factory implements Factory<LaunchOpa.Builder> {
    private final Provider<Context> contextProvider;
    private final Provider<StatusBar> statusBarProvider;

    public LaunchOpa_Builder_Factory(Provider<Context> provider, Provider<StatusBar> provider2) {
        this.contextProvider = provider;
        this.statusBarProvider = provider2;
    }

    @Override // javax.inject.Provider
    public LaunchOpa.Builder get() {
        return newInstance(this.contextProvider.get(), this.statusBarProvider.get());
    }

    public static LaunchOpa_Builder_Factory create(Provider<Context> provider, Provider<StatusBar> provider2) {
        return new LaunchOpa_Builder_Factory(provider, provider2);
    }

    public static LaunchOpa.Builder newInstance(Context context, StatusBar statusBar) {
        return new LaunchOpa.Builder(context, statusBar);
    }
}
