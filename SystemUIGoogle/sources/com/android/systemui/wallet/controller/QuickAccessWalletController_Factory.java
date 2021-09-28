package com.android.systemui.wallet.controller;

import android.content.Context;
import android.service.quickaccesswallet.QuickAccessWalletClient;
import com.android.systemui.util.settings.SecureSettings;
import com.android.systemui.util.time.SystemClock;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class QuickAccessWalletController_Factory implements Factory<QuickAccessWalletController> {
    private final Provider<SystemClock> clockProvider;
    private final Provider<Context> contextProvider;
    private final Provider<Executor> executorProvider;
    private final Provider<QuickAccessWalletClient> quickAccessWalletClientProvider;
    private final Provider<SecureSettings> secureSettingsProvider;

    public QuickAccessWalletController_Factory(Provider<Context> provider, Provider<Executor> provider2, Provider<SecureSettings> provider3, Provider<QuickAccessWalletClient> provider4, Provider<SystemClock> provider5) {
        this.contextProvider = provider;
        this.executorProvider = provider2;
        this.secureSettingsProvider = provider3;
        this.quickAccessWalletClientProvider = provider4;
        this.clockProvider = provider5;
    }

    @Override // javax.inject.Provider
    public QuickAccessWalletController get() {
        return newInstance(this.contextProvider.get(), this.executorProvider.get(), this.secureSettingsProvider.get(), this.quickAccessWalletClientProvider.get(), this.clockProvider.get());
    }

    public static QuickAccessWalletController_Factory create(Provider<Context> provider, Provider<Executor> provider2, Provider<SecureSettings> provider3, Provider<QuickAccessWalletClient> provider4, Provider<SystemClock> provider5) {
        return new QuickAccessWalletController_Factory(provider, provider2, provider3, provider4, provider5);
    }

    public static QuickAccessWalletController newInstance(Context context, Executor executor, SecureSettings secureSettings, QuickAccessWalletClient quickAccessWalletClient, SystemClock systemClock) {
        return new QuickAccessWalletController(context, executor, secureSettings, quickAccessWalletClient, systemClock);
    }
}
