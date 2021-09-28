package com.android.keyguard;

import android.media.AudioManager;
import android.telephony.TelephonyManager;
import com.android.keyguard.KeyguardSecurityContainerController;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class KeyguardHostViewController_Factory implements Factory<KeyguardHostViewController> {
    private final Provider<AudioManager> audioManagerProvider;
    private final Provider<KeyguardSecurityContainerController.Factory> keyguardSecurityContainerControllerFactoryProvider;
    private final Provider<KeyguardUpdateMonitor> keyguardUpdateMonitorProvider;
    private final Provider<TelephonyManager> telephonyManagerProvider;
    private final Provider<ViewMediatorCallback> viewMediatorCallbackProvider;
    private final Provider<KeyguardHostView> viewProvider;

    public KeyguardHostViewController_Factory(Provider<KeyguardHostView> provider, Provider<KeyguardUpdateMonitor> provider2, Provider<AudioManager> provider3, Provider<TelephonyManager> provider4, Provider<ViewMediatorCallback> provider5, Provider<KeyguardSecurityContainerController.Factory> provider6) {
        this.viewProvider = provider;
        this.keyguardUpdateMonitorProvider = provider2;
        this.audioManagerProvider = provider3;
        this.telephonyManagerProvider = provider4;
        this.viewMediatorCallbackProvider = provider5;
        this.keyguardSecurityContainerControllerFactoryProvider = provider6;
    }

    @Override // javax.inject.Provider
    public KeyguardHostViewController get() {
        return newInstance(this.viewProvider.get(), this.keyguardUpdateMonitorProvider.get(), this.audioManagerProvider.get(), this.telephonyManagerProvider.get(), this.viewMediatorCallbackProvider.get(), this.keyguardSecurityContainerControllerFactoryProvider.get());
    }

    public static KeyguardHostViewController_Factory create(Provider<KeyguardHostView> provider, Provider<KeyguardUpdateMonitor> provider2, Provider<AudioManager> provider3, Provider<TelephonyManager> provider4, Provider<ViewMediatorCallback> provider5, Provider<KeyguardSecurityContainerController.Factory> provider6) {
        return new KeyguardHostViewController_Factory(provider, provider2, provider3, provider4, provider5, provider6);
    }

    public static KeyguardHostViewController newInstance(KeyguardHostView keyguardHostView, KeyguardUpdateMonitor keyguardUpdateMonitor, AudioManager audioManager, TelephonyManager telephonyManager, ViewMediatorCallback viewMediatorCallback, Object obj) {
        return new KeyguardHostViewController(keyguardHostView, keyguardUpdateMonitor, audioManager, telephonyManager, viewMediatorCallback, (KeyguardSecurityContainerController.Factory) obj);
    }
}
