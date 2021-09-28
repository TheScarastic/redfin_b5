package com.android.systemui.statusbar.notification.stack;

import android.content.res.Resources;
import android.view.ViewConfiguration;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.statusbar.notification.stack.NotificationSwipeHelper;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class NotificationSwipeHelper_Builder_Factory implements Factory<NotificationSwipeHelper.Builder> {
    private final Provider<FalsingManager> falsingManagerProvider;
    private final Provider<Resources> resourcesProvider;
    private final Provider<ViewConfiguration> viewConfigurationProvider;

    public NotificationSwipeHelper_Builder_Factory(Provider<Resources> provider, Provider<ViewConfiguration> provider2, Provider<FalsingManager> provider3) {
        this.resourcesProvider = provider;
        this.viewConfigurationProvider = provider2;
        this.falsingManagerProvider = provider3;
    }

    @Override // javax.inject.Provider
    public NotificationSwipeHelper.Builder get() {
        return newInstance(this.resourcesProvider.get(), this.viewConfigurationProvider.get(), this.falsingManagerProvider.get());
    }

    public static NotificationSwipeHelper_Builder_Factory create(Provider<Resources> provider, Provider<ViewConfiguration> provider2, Provider<FalsingManager> provider3) {
        return new NotificationSwipeHelper_Builder_Factory(provider, provider2, provider3);
    }

    public static NotificationSwipeHelper.Builder newInstance(Resources resources, ViewConfiguration viewConfiguration, FalsingManager falsingManager) {
        return new NotificationSwipeHelper.Builder(resources, viewConfiguration, falsingManager);
    }
}
