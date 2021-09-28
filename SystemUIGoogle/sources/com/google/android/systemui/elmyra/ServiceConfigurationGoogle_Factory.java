package com.google.android.systemui.elmyra;

import android.content.Context;
import com.google.android.systemui.elmyra.actions.CameraAction;
import com.google.android.systemui.elmyra.actions.LaunchOpa;
import com.google.android.systemui.elmyra.actions.SettingsAction;
import com.google.android.systemui.elmyra.actions.SetupWizardAction;
import com.google.android.systemui.elmyra.actions.SilenceCall;
import com.google.android.systemui.elmyra.actions.UnpinNotifications;
import com.google.android.systemui.elmyra.feedback.AssistInvocationEffect;
import com.google.android.systemui.elmyra.feedback.SquishyNavigationButtons;
import com.google.android.systemui.elmyra.gates.TelephonyActivity;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class ServiceConfigurationGoogle_Factory implements Factory<ServiceConfigurationGoogle> {
    private final Provider<AssistInvocationEffect> assistInvocationEffectProvider;
    private final Provider<CameraAction.Builder> cameraActionBuilderProvider;
    private final Provider<Context> contextProvider;
    private final Provider<LaunchOpa.Builder> launchOpaBuilderProvider;
    private final Provider<SettingsAction.Builder> settingsActionBuilderProvider;
    private final Provider<SetupWizardAction.Builder> setupWizardActionBuilderProvider;
    private final Provider<SilenceCall> silenceCallProvider;
    private final Provider<SquishyNavigationButtons> squishyNavigationButtonsProvider;
    private final Provider<TelephonyActivity> telephonyActivityProvider;
    private final Provider<UnpinNotifications> unpinNotificationsProvider;

    public ServiceConfigurationGoogle_Factory(Provider<Context> provider, Provider<AssistInvocationEffect> provider2, Provider<LaunchOpa.Builder> provider3, Provider<SettingsAction.Builder> provider4, Provider<CameraAction.Builder> provider5, Provider<SetupWizardAction.Builder> provider6, Provider<SquishyNavigationButtons> provider7, Provider<UnpinNotifications> provider8, Provider<SilenceCall> provider9, Provider<TelephonyActivity> provider10) {
        this.contextProvider = provider;
        this.assistInvocationEffectProvider = provider2;
        this.launchOpaBuilderProvider = provider3;
        this.settingsActionBuilderProvider = provider4;
        this.cameraActionBuilderProvider = provider5;
        this.setupWizardActionBuilderProvider = provider6;
        this.squishyNavigationButtonsProvider = provider7;
        this.unpinNotificationsProvider = provider8;
        this.silenceCallProvider = provider9;
        this.telephonyActivityProvider = provider10;
    }

    @Override // javax.inject.Provider
    public ServiceConfigurationGoogle get() {
        return newInstance(this.contextProvider.get(), this.assistInvocationEffectProvider.get(), this.launchOpaBuilderProvider.get(), this.settingsActionBuilderProvider.get(), this.cameraActionBuilderProvider.get(), this.setupWizardActionBuilderProvider.get(), this.squishyNavigationButtonsProvider.get(), this.unpinNotificationsProvider.get(), this.silenceCallProvider.get(), this.telephonyActivityProvider.get());
    }

    public static ServiceConfigurationGoogle_Factory create(Provider<Context> provider, Provider<AssistInvocationEffect> provider2, Provider<LaunchOpa.Builder> provider3, Provider<SettingsAction.Builder> provider4, Provider<CameraAction.Builder> provider5, Provider<SetupWizardAction.Builder> provider6, Provider<SquishyNavigationButtons> provider7, Provider<UnpinNotifications> provider8, Provider<SilenceCall> provider9, Provider<TelephonyActivity> provider10) {
        return new ServiceConfigurationGoogle_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10);
    }

    public static ServiceConfigurationGoogle newInstance(Context context, AssistInvocationEffect assistInvocationEffect, LaunchOpa.Builder builder, SettingsAction.Builder builder2, CameraAction.Builder builder3, SetupWizardAction.Builder builder4, SquishyNavigationButtons squishyNavigationButtons, UnpinNotifications unpinNotifications, SilenceCall silenceCall, TelephonyActivity telephonyActivity) {
        return new ServiceConfigurationGoogle(context, assistInvocationEffect, builder, builder2, builder3, builder4, squishyNavigationButtons, unpinNotifications, silenceCall, telephonyActivity);
    }
}
