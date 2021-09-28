package com.android.systemui.statusbar.notification.stack;

import com.android.systemui.statusbar.notification.NotificationSectionsFeatureManager;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class NotificationRoundnessManager_Factory implements Factory<NotificationRoundnessManager> {
    private final Provider<KeyguardBypassController> keyguardBypassControllerProvider;
    private final Provider<NotificationSectionsFeatureManager> sectionsFeatureManagerProvider;

    public NotificationRoundnessManager_Factory(Provider<KeyguardBypassController> provider, Provider<NotificationSectionsFeatureManager> provider2) {
        this.keyguardBypassControllerProvider = provider;
        this.sectionsFeatureManagerProvider = provider2;
    }

    @Override // javax.inject.Provider
    public NotificationRoundnessManager get() {
        return newInstance(this.keyguardBypassControllerProvider.get(), this.sectionsFeatureManagerProvider.get());
    }

    public static NotificationRoundnessManager_Factory create(Provider<KeyguardBypassController> provider, Provider<NotificationSectionsFeatureManager> provider2) {
        return new NotificationRoundnessManager_Factory(provider, provider2);
    }

    public static NotificationRoundnessManager newInstance(KeyguardBypassController keyguardBypassController, NotificationSectionsFeatureManager notificationSectionsFeatureManager) {
        return new NotificationRoundnessManager(keyguardBypassController, notificationSectionsFeatureManager);
    }
}
