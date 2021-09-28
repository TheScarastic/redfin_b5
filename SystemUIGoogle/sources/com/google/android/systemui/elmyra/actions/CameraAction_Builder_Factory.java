package com.google.android.systemui.elmyra.actions;

import android.content.Context;
import com.android.systemui.statusbar.phone.StatusBar;
import com.google.android.systemui.elmyra.actions.CameraAction;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class CameraAction_Builder_Factory implements Factory<CameraAction.Builder> {
    private final Provider<Context> contextProvider;
    private final Provider<StatusBar> statusBarProvider;

    public CameraAction_Builder_Factory(Provider<Context> provider, Provider<StatusBar> provider2) {
        this.contextProvider = provider;
        this.statusBarProvider = provider2;
    }

    @Override // javax.inject.Provider
    public CameraAction.Builder get() {
        return newInstance(this.contextProvider.get(), this.statusBarProvider.get());
    }

    public static CameraAction_Builder_Factory create(Provider<Context> provider, Provider<StatusBar> provider2) {
        return new CameraAction_Builder_Factory(provider, provider2);
    }

    public static CameraAction.Builder newInstance(Context context, StatusBar statusBar) {
        return new CameraAction.Builder(context, statusBar);
    }
}
