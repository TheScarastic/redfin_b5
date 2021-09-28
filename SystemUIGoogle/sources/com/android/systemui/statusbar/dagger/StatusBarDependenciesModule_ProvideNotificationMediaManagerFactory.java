package com.android.systemui.statusbar.dagger;

import android.content.Context;
import com.android.systemui.media.MediaDataManager;
import com.android.systemui.statusbar.FeatureFlags;
import com.android.systemui.statusbar.MediaArtworkProcessor;
import com.android.systemui.statusbar.NotificationMediaManager;
import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.collection.NotifCollection;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.util.DeviceConfigProxy;
import com.android.systemui.util.concurrency.DelayableExecutor;
import dagger.Lazy;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class StatusBarDependenciesModule_ProvideNotificationMediaManagerFactory implements Factory<NotificationMediaManager> {
    private final Provider<Context> contextProvider;
    private final Provider<DeviceConfigProxy> deviceConfigProxyProvider;
    private final Provider<FeatureFlags> featureFlagsProvider;
    private final Provider<KeyguardBypassController> keyguardBypassControllerProvider;
    private final Provider<DelayableExecutor> mainExecutorProvider;
    private final Provider<MediaArtworkProcessor> mediaArtworkProcessorProvider;
    private final Provider<MediaDataManager> mediaDataManagerProvider;
    private final Provider<NotifCollection> notifCollectionProvider;
    private final Provider<NotifPipeline> notifPipelineProvider;
    private final Provider<NotificationEntryManager> notificationEntryManagerProvider;
    private final Provider<NotificationShadeWindowController> notificationShadeWindowControllerProvider;
    private final Provider<StatusBar> statusBarLazyProvider;

    public StatusBarDependenciesModule_ProvideNotificationMediaManagerFactory(Provider<Context> provider, Provider<StatusBar> provider2, Provider<NotificationShadeWindowController> provider3, Provider<NotificationEntryManager> provider4, Provider<MediaArtworkProcessor> provider5, Provider<KeyguardBypassController> provider6, Provider<NotifPipeline> provider7, Provider<NotifCollection> provider8, Provider<FeatureFlags> provider9, Provider<DelayableExecutor> provider10, Provider<DeviceConfigProxy> provider11, Provider<MediaDataManager> provider12) {
        this.contextProvider = provider;
        this.statusBarLazyProvider = provider2;
        this.notificationShadeWindowControllerProvider = provider3;
        this.notificationEntryManagerProvider = provider4;
        this.mediaArtworkProcessorProvider = provider5;
        this.keyguardBypassControllerProvider = provider6;
        this.notifPipelineProvider = provider7;
        this.notifCollectionProvider = provider8;
        this.featureFlagsProvider = provider9;
        this.mainExecutorProvider = provider10;
        this.deviceConfigProxyProvider = provider11;
        this.mediaDataManagerProvider = provider12;
    }

    @Override // javax.inject.Provider
    public NotificationMediaManager get() {
        return provideNotificationMediaManager(this.contextProvider.get(), DoubleCheck.lazy(this.statusBarLazyProvider), DoubleCheck.lazy(this.notificationShadeWindowControllerProvider), this.notificationEntryManagerProvider.get(), this.mediaArtworkProcessorProvider.get(), this.keyguardBypassControllerProvider.get(), this.notifPipelineProvider.get(), this.notifCollectionProvider.get(), this.featureFlagsProvider.get(), this.mainExecutorProvider.get(), this.deviceConfigProxyProvider.get(), this.mediaDataManagerProvider.get());
    }

    public static StatusBarDependenciesModule_ProvideNotificationMediaManagerFactory create(Provider<Context> provider, Provider<StatusBar> provider2, Provider<NotificationShadeWindowController> provider3, Provider<NotificationEntryManager> provider4, Provider<MediaArtworkProcessor> provider5, Provider<KeyguardBypassController> provider6, Provider<NotifPipeline> provider7, Provider<NotifCollection> provider8, Provider<FeatureFlags> provider9, Provider<DelayableExecutor> provider10, Provider<DeviceConfigProxy> provider11, Provider<MediaDataManager> provider12) {
        return new StatusBarDependenciesModule_ProvideNotificationMediaManagerFactory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12);
    }

    public static NotificationMediaManager provideNotificationMediaManager(Context context, Lazy<StatusBar> lazy, Lazy<NotificationShadeWindowController> lazy2, NotificationEntryManager notificationEntryManager, MediaArtworkProcessor mediaArtworkProcessor, KeyguardBypassController keyguardBypassController, NotifPipeline notifPipeline, NotifCollection notifCollection, FeatureFlags featureFlags, DelayableExecutor delayableExecutor, DeviceConfigProxy deviceConfigProxy, MediaDataManager mediaDataManager) {
        return (NotificationMediaManager) Preconditions.checkNotNullFromProvides(StatusBarDependenciesModule.provideNotificationMediaManager(context, lazy, lazy2, notificationEntryManager, mediaArtworkProcessor, keyguardBypassController, notifPipeline, notifCollection, featureFlags, delayableExecutor, deviceConfigProxy, mediaDataManager));
    }
}
