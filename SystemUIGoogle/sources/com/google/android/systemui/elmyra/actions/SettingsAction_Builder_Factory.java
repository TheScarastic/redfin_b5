package com.google.android.systemui.elmyra.actions;

import android.content.Context;
import com.android.systemui.statusbar.phone.StatusBar;
import com.google.android.systemui.elmyra.actions.SettingsAction;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class SettingsAction_Builder_Factory implements Factory<SettingsAction.Builder> {
    private final Provider<Context> contextProvider;
    private final Provider<StatusBar> statusBarProvider;

    public SettingsAction_Builder_Factory(Provider<Context> provider, Provider<StatusBar> provider2) {
        this.contextProvider = provider;
        this.statusBarProvider = provider2;
    }

    @Override // javax.inject.Provider
    public SettingsAction.Builder get() {
        return newInstance(this.contextProvider.get(), this.statusBarProvider.get());
    }

    public static SettingsAction_Builder_Factory create(Provider<Context> provider, Provider<StatusBar> provider2) {
        return new SettingsAction_Builder_Factory(provider, provider2);
    }

    public static SettingsAction.Builder newInstance(Context context, StatusBar statusBar) {
        return new SettingsAction.Builder(context, statusBar);
    }
}
