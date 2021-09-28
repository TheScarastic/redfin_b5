package com.android.systemui.statusbar;

import android.content.Context;
import android.util.DisplayMetrics;
import com.android.systemui.classifier.FalsingCollector;
import com.android.systemui.media.MediaHierarchyManager;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.statusbar.notification.stack.AmbientState;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.statusbar.phone.LockscreenGestureLogger;
import com.android.systemui.statusbar.phone.ScrimController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class LockscreenShadeTransitionController_Factory implements Factory<LockscreenShadeTransitionController> {
    private final Provider<AmbientState> ambientStateProvider;
    private final Provider<ConfigurationController> configurationControllerProvider;
    private final Provider<Context> contextProvider;
    private final Provider<NotificationShadeDepthController> depthControllerProvider;
    private final Provider<DisplayMetrics> displayMetricsProvider;
    private final Provider<FalsingCollector> falsingCollectorProvider;
    private final Provider<FalsingManager> falsingManagerProvider;
    private final Provider<FeatureFlags> featureFlagsProvider;
    private final Provider<KeyguardBypassController> keyguardBypassControllerProvider;
    private final Provider<NotificationLockscreenUserManager> lockScreenUserManagerProvider;
    private final Provider<LockscreenGestureLogger> lockscreenGestureLoggerProvider;
    private final Provider<MediaHierarchyManager> mediaHierarchyManagerProvider;
    private final Provider<ScrimController> scrimControllerProvider;
    private final Provider<SysuiStatusBarStateController> statusBarStateControllerProvider;

    public LockscreenShadeTransitionController_Factory(Provider<SysuiStatusBarStateController> provider, Provider<LockscreenGestureLogger> provider2, Provider<KeyguardBypassController> provider3, Provider<NotificationLockscreenUserManager> provider4, Provider<FalsingCollector> provider5, Provider<AmbientState> provider6, Provider<DisplayMetrics> provider7, Provider<MediaHierarchyManager> provider8, Provider<ScrimController> provider9, Provider<NotificationShadeDepthController> provider10, Provider<FeatureFlags> provider11, Provider<Context> provider12, Provider<ConfigurationController> provider13, Provider<FalsingManager> provider14) {
        this.statusBarStateControllerProvider = provider;
        this.lockscreenGestureLoggerProvider = provider2;
        this.keyguardBypassControllerProvider = provider3;
        this.lockScreenUserManagerProvider = provider4;
        this.falsingCollectorProvider = provider5;
        this.ambientStateProvider = provider6;
        this.displayMetricsProvider = provider7;
        this.mediaHierarchyManagerProvider = provider8;
        this.scrimControllerProvider = provider9;
        this.depthControllerProvider = provider10;
        this.featureFlagsProvider = provider11;
        this.contextProvider = provider12;
        this.configurationControllerProvider = provider13;
        this.falsingManagerProvider = provider14;
    }

    @Override // javax.inject.Provider
    public LockscreenShadeTransitionController get() {
        return newInstance(this.statusBarStateControllerProvider.get(), this.lockscreenGestureLoggerProvider.get(), this.keyguardBypassControllerProvider.get(), this.lockScreenUserManagerProvider.get(), this.falsingCollectorProvider.get(), this.ambientStateProvider.get(), this.displayMetricsProvider.get(), this.mediaHierarchyManagerProvider.get(), this.scrimControllerProvider.get(), this.depthControllerProvider.get(), this.featureFlagsProvider.get(), this.contextProvider.get(), this.configurationControllerProvider.get(), this.falsingManagerProvider.get());
    }

    public static LockscreenShadeTransitionController_Factory create(Provider<SysuiStatusBarStateController> provider, Provider<LockscreenGestureLogger> provider2, Provider<KeyguardBypassController> provider3, Provider<NotificationLockscreenUserManager> provider4, Provider<FalsingCollector> provider5, Provider<AmbientState> provider6, Provider<DisplayMetrics> provider7, Provider<MediaHierarchyManager> provider8, Provider<ScrimController> provider9, Provider<NotificationShadeDepthController> provider10, Provider<FeatureFlags> provider11, Provider<Context> provider12, Provider<ConfigurationController> provider13, Provider<FalsingManager> provider14) {
        return new LockscreenShadeTransitionController_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14);
    }

    public static LockscreenShadeTransitionController newInstance(SysuiStatusBarStateController sysuiStatusBarStateController, LockscreenGestureLogger lockscreenGestureLogger, KeyguardBypassController keyguardBypassController, NotificationLockscreenUserManager notificationLockscreenUserManager, FalsingCollector falsingCollector, AmbientState ambientState, DisplayMetrics displayMetrics, MediaHierarchyManager mediaHierarchyManager, ScrimController scrimController, NotificationShadeDepthController notificationShadeDepthController, FeatureFlags featureFlags, Context context, ConfigurationController configurationController, FalsingManager falsingManager) {
        return new LockscreenShadeTransitionController(sysuiStatusBarStateController, lockscreenGestureLogger, keyguardBypassController, notificationLockscreenUserManager, falsingCollector, ambientState, displayMetrics, mediaHierarchyManager, scrimController, notificationShadeDepthController, featureFlags, context, configurationController, falsingManager);
    }
}
