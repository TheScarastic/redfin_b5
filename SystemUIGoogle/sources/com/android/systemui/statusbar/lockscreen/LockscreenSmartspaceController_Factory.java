package com.android.systemui.statusbar.lockscreen;

import android.app.smartspace.SmartspaceManager;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Handler;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.BcSmartspaceDataPlugin;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.statusbar.FeatureFlags;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.util.concurrency.Execution;
import com.android.systemui.util.settings.SecureSettings;
import dagger.internal.Factory;
import java.util.Optional;
import java.util.concurrent.Executor;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class LockscreenSmartspaceController_Factory implements Factory<LockscreenSmartspaceController> {
    private final Provider<ActivityStarter> activityStarterProvider;
    private final Provider<ConfigurationController> configurationControllerProvider;
    private final Provider<ContentResolver> contentResolverProvider;
    private final Provider<Context> contextProvider;
    private final Provider<Execution> executionProvider;
    private final Provider<FalsingManager> falsingManagerProvider;
    private final Provider<FeatureFlags> featureFlagsProvider;
    private final Provider<Handler> handlerProvider;
    private final Provider<Optional<BcSmartspaceDataPlugin>> optionalPluginProvider;
    private final Provider<SecureSettings> secureSettingsProvider;
    private final Provider<SmartspaceManager> smartspaceManagerProvider;
    private final Provider<StatusBarStateController> statusBarStateControllerProvider;
    private final Provider<Executor> uiExecutorProvider;
    private final Provider<UserTracker> userTrackerProvider;

    public LockscreenSmartspaceController_Factory(Provider<Context> provider, Provider<FeatureFlags> provider2, Provider<SmartspaceManager> provider3, Provider<ActivityStarter> provider4, Provider<FalsingManager> provider5, Provider<SecureSettings> provider6, Provider<UserTracker> provider7, Provider<ContentResolver> provider8, Provider<ConfigurationController> provider9, Provider<StatusBarStateController> provider10, Provider<Execution> provider11, Provider<Executor> provider12, Provider<Handler> provider13, Provider<Optional<BcSmartspaceDataPlugin>> provider14) {
        this.contextProvider = provider;
        this.featureFlagsProvider = provider2;
        this.smartspaceManagerProvider = provider3;
        this.activityStarterProvider = provider4;
        this.falsingManagerProvider = provider5;
        this.secureSettingsProvider = provider6;
        this.userTrackerProvider = provider7;
        this.contentResolverProvider = provider8;
        this.configurationControllerProvider = provider9;
        this.statusBarStateControllerProvider = provider10;
        this.executionProvider = provider11;
        this.uiExecutorProvider = provider12;
        this.handlerProvider = provider13;
        this.optionalPluginProvider = provider14;
    }

    @Override // javax.inject.Provider
    public LockscreenSmartspaceController get() {
        return newInstance(this.contextProvider.get(), this.featureFlagsProvider.get(), this.smartspaceManagerProvider.get(), this.activityStarterProvider.get(), this.falsingManagerProvider.get(), this.secureSettingsProvider.get(), this.userTrackerProvider.get(), this.contentResolverProvider.get(), this.configurationControllerProvider.get(), this.statusBarStateControllerProvider.get(), this.executionProvider.get(), this.uiExecutorProvider.get(), this.handlerProvider.get(), this.optionalPluginProvider.get());
    }

    public static LockscreenSmartspaceController_Factory create(Provider<Context> provider, Provider<FeatureFlags> provider2, Provider<SmartspaceManager> provider3, Provider<ActivityStarter> provider4, Provider<FalsingManager> provider5, Provider<SecureSettings> provider6, Provider<UserTracker> provider7, Provider<ContentResolver> provider8, Provider<ConfigurationController> provider9, Provider<StatusBarStateController> provider10, Provider<Execution> provider11, Provider<Executor> provider12, Provider<Handler> provider13, Provider<Optional<BcSmartspaceDataPlugin>> provider14) {
        return new LockscreenSmartspaceController_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14);
    }

    public static LockscreenSmartspaceController newInstance(Context context, FeatureFlags featureFlags, SmartspaceManager smartspaceManager, ActivityStarter activityStarter, FalsingManager falsingManager, SecureSettings secureSettings, UserTracker userTracker, ContentResolver contentResolver, ConfigurationController configurationController, StatusBarStateController statusBarStateController, Execution execution, Executor executor, Handler handler, Optional<BcSmartspaceDataPlugin> optional) {
        return new LockscreenSmartspaceController(context, featureFlags, smartspaceManager, activityStarter, falsingManager, secureSettings, userTracker, contentResolver, configurationController, statusBarStateController, execution, executor, handler, optional);
    }
}
