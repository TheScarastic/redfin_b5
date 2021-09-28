package com.google.android.systemui.elmyra.actions;

import android.content.Context;
import com.android.systemui.statusbar.phone.StatusBar;
import com.google.android.systemui.elmyra.actions.SetupWizardAction;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class SetupWizardAction_Builder_Factory implements Factory<SetupWizardAction.Builder> {
    private final Provider<Context> contextProvider;
    private final Provider<StatusBar> statusBarProvider;

    public SetupWizardAction_Builder_Factory(Provider<Context> provider, Provider<StatusBar> provider2) {
        this.contextProvider = provider;
        this.statusBarProvider = provider2;
    }

    @Override // javax.inject.Provider
    public SetupWizardAction.Builder get() {
        return newInstance(this.contextProvider.get(), this.statusBarProvider.get());
    }

    public static SetupWizardAction_Builder_Factory create(Provider<Context> provider, Provider<StatusBar> provider2) {
        return new SetupWizardAction_Builder_Factory(provider, provider2);
    }

    public static SetupWizardAction.Builder newInstance(Context context, StatusBar statusBar) {
        return new SetupWizardAction.Builder(context, statusBar);
    }
}
