package com.android.systemui.statusbar.notification.people;

import com.android.systemui.statusbar.policy.ExtensionController;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class NotificationPersonExtractorPluginBoundary_Factory implements Factory<NotificationPersonExtractorPluginBoundary> {
    private final Provider<ExtensionController> extensionControllerProvider;

    public NotificationPersonExtractorPluginBoundary_Factory(Provider<ExtensionController> provider) {
        this.extensionControllerProvider = provider;
    }

    @Override // javax.inject.Provider
    public NotificationPersonExtractorPluginBoundary get() {
        return newInstance(this.extensionControllerProvider.get());
    }

    public static NotificationPersonExtractorPluginBoundary_Factory create(Provider<ExtensionController> provider) {
        return new NotificationPersonExtractorPluginBoundary_Factory(provider);
    }

    public static NotificationPersonExtractorPluginBoundary newInstance(ExtensionController extensionController) {
        return new NotificationPersonExtractorPluginBoundary(extensionController);
    }
}
