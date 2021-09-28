package com.android.systemui.wallet.dagger;

import android.content.Context;
import android.service.quickaccesswallet.QuickAccessWalletClient;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class WalletModule_ProvideQuickAccessWalletClientFactory implements Factory<QuickAccessWalletClient> {
    private final Provider<Context> contextProvider;

    public WalletModule_ProvideQuickAccessWalletClientFactory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    @Override // javax.inject.Provider
    public QuickAccessWalletClient get() {
        return provideQuickAccessWalletClient(this.contextProvider.get());
    }

    public static WalletModule_ProvideQuickAccessWalletClientFactory create(Provider<Context> provider) {
        return new WalletModule_ProvideQuickAccessWalletClientFactory(provider);
    }

    public static QuickAccessWalletClient provideQuickAccessWalletClient(Context context) {
        return (QuickAccessWalletClient) Preconditions.checkNotNullFromProvides(WalletModule.provideQuickAccessWalletClient(context));
    }
}
