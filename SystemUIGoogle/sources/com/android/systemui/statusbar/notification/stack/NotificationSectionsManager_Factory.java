package com.android.systemui.statusbar.notification.stack;

import com.android.systemui.media.KeyguardMediaController;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.notification.NotificationSectionsFeatureManager;
import com.android.systemui.statusbar.notification.collection.render.SectionHeaderController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class NotificationSectionsManager_Factory implements Factory<NotificationSectionsManager> {
    private final Provider<SectionHeaderController> alertingHeaderControllerProvider;
    private final Provider<ConfigurationController> configurationControllerProvider;
    private final Provider<SectionHeaderController> incomingHeaderControllerProvider;
    private final Provider<KeyguardMediaController> keyguardMediaControllerProvider;
    private final Provider<NotificationSectionsLogger> loggerProvider;
    private final Provider<SectionHeaderController> peopleHeaderControllerProvider;
    private final Provider<NotificationSectionsFeatureManager> sectionsFeatureManagerProvider;
    private final Provider<SectionHeaderController> silentHeaderControllerProvider;
    private final Provider<StatusBarStateController> statusBarStateControllerProvider;

    public NotificationSectionsManager_Factory(Provider<StatusBarStateController> provider, Provider<ConfigurationController> provider2, Provider<KeyguardMediaController> provider3, Provider<NotificationSectionsFeatureManager> provider4, Provider<NotificationSectionsLogger> provider5, Provider<SectionHeaderController> provider6, Provider<SectionHeaderController> provider7, Provider<SectionHeaderController> provider8, Provider<SectionHeaderController> provider9) {
        this.statusBarStateControllerProvider = provider;
        this.configurationControllerProvider = provider2;
        this.keyguardMediaControllerProvider = provider3;
        this.sectionsFeatureManagerProvider = provider4;
        this.loggerProvider = provider5;
        this.incomingHeaderControllerProvider = provider6;
        this.peopleHeaderControllerProvider = provider7;
        this.alertingHeaderControllerProvider = provider8;
        this.silentHeaderControllerProvider = provider9;
    }

    @Override // javax.inject.Provider
    public NotificationSectionsManager get() {
        return newInstance(this.statusBarStateControllerProvider.get(), this.configurationControllerProvider.get(), this.keyguardMediaControllerProvider.get(), this.sectionsFeatureManagerProvider.get(), this.loggerProvider.get(), this.incomingHeaderControllerProvider.get(), this.peopleHeaderControllerProvider.get(), this.alertingHeaderControllerProvider.get(), this.silentHeaderControllerProvider.get());
    }

    public static NotificationSectionsManager_Factory create(Provider<StatusBarStateController> provider, Provider<ConfigurationController> provider2, Provider<KeyguardMediaController> provider3, Provider<NotificationSectionsFeatureManager> provider4, Provider<NotificationSectionsLogger> provider5, Provider<SectionHeaderController> provider6, Provider<SectionHeaderController> provider7, Provider<SectionHeaderController> provider8, Provider<SectionHeaderController> provider9) {
        return new NotificationSectionsManager_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9);
    }

    public static NotificationSectionsManager newInstance(StatusBarStateController statusBarStateController, ConfigurationController configurationController, KeyguardMediaController keyguardMediaController, NotificationSectionsFeatureManager notificationSectionsFeatureManager, NotificationSectionsLogger notificationSectionsLogger, SectionHeaderController sectionHeaderController, SectionHeaderController sectionHeaderController2, SectionHeaderController sectionHeaderController3, SectionHeaderController sectionHeaderController4) {
        return new NotificationSectionsManager(statusBarStateController, configurationController, keyguardMediaController, notificationSectionsFeatureManager, notificationSectionsLogger, sectionHeaderController, sectionHeaderController2, sectionHeaderController3, sectionHeaderController4);
    }
}
