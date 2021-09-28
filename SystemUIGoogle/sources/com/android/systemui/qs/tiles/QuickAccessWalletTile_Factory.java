package com.android.systemui.qs.tiles;

import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;
import com.android.internal.logging.MetricsLogger;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.qs.QSHost;
import com.android.systemui.qs.logging.QSLogger;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.settings.SecureSettings;
import com.android.systemui.wallet.controller.QuickAccessWalletController;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class QuickAccessWalletTile_Factory implements Factory<QuickAccessWalletTile> {
    private final Provider<ActivityStarter> activityStarterProvider;
    private final Provider<Looper> backgroundLooperProvider;
    private final Provider<FalsingManager> falsingManagerProvider;
    private final Provider<QSHost> hostProvider;
    private final Provider<KeyguardStateController> keyguardStateControllerProvider;
    private final Provider<Handler> mainHandlerProvider;
    private final Provider<MetricsLogger> metricsLoggerProvider;
    private final Provider<PackageManager> packageManagerProvider;
    private final Provider<QSLogger> qsLoggerProvider;
    private final Provider<QuickAccessWalletController> quickAccessWalletControllerProvider;
    private final Provider<SecureSettings> secureSettingsProvider;
    private final Provider<StatusBarStateController> statusBarStateControllerProvider;

    public QuickAccessWalletTile_Factory(Provider<QSHost> provider, Provider<Looper> provider2, Provider<Handler> provider3, Provider<FalsingManager> provider4, Provider<MetricsLogger> provider5, Provider<StatusBarStateController> provider6, Provider<ActivityStarter> provider7, Provider<QSLogger> provider8, Provider<KeyguardStateController> provider9, Provider<PackageManager> provider10, Provider<SecureSettings> provider11, Provider<QuickAccessWalletController> provider12) {
        this.hostProvider = provider;
        this.backgroundLooperProvider = provider2;
        this.mainHandlerProvider = provider3;
        this.falsingManagerProvider = provider4;
        this.metricsLoggerProvider = provider5;
        this.statusBarStateControllerProvider = provider6;
        this.activityStarterProvider = provider7;
        this.qsLoggerProvider = provider8;
        this.keyguardStateControllerProvider = provider9;
        this.packageManagerProvider = provider10;
        this.secureSettingsProvider = provider11;
        this.quickAccessWalletControllerProvider = provider12;
    }

    @Override // javax.inject.Provider
    public QuickAccessWalletTile get() {
        return newInstance(this.hostProvider.get(), this.backgroundLooperProvider.get(), this.mainHandlerProvider.get(), this.falsingManagerProvider.get(), this.metricsLoggerProvider.get(), this.statusBarStateControllerProvider.get(), this.activityStarterProvider.get(), this.qsLoggerProvider.get(), this.keyguardStateControllerProvider.get(), this.packageManagerProvider.get(), this.secureSettingsProvider.get(), this.quickAccessWalletControllerProvider.get());
    }

    public static QuickAccessWalletTile_Factory create(Provider<QSHost> provider, Provider<Looper> provider2, Provider<Handler> provider3, Provider<FalsingManager> provider4, Provider<MetricsLogger> provider5, Provider<StatusBarStateController> provider6, Provider<ActivityStarter> provider7, Provider<QSLogger> provider8, Provider<KeyguardStateController> provider9, Provider<PackageManager> provider10, Provider<SecureSettings> provider11, Provider<QuickAccessWalletController> provider12) {
        return new QuickAccessWalletTile_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12);
    }

    public static QuickAccessWalletTile newInstance(QSHost qSHost, Looper looper, Handler handler, FalsingManager falsingManager, MetricsLogger metricsLogger, StatusBarStateController statusBarStateController, ActivityStarter activityStarter, QSLogger qSLogger, KeyguardStateController keyguardStateController, PackageManager packageManager, SecureSettings secureSettings, QuickAccessWalletController quickAccessWalletController) {
        return new QuickAccessWalletTile(qSHost, looper, handler, falsingManager, metricsLogger, statusBarStateController, activityStarter, qSLogger, keyguardStateController, packageManager, secureSettings, quickAccessWalletController);
    }
}
