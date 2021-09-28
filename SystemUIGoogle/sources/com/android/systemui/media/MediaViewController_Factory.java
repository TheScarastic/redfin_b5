package com.android.systemui.media;

import android.content.Context;
import com.android.systemui.statusbar.policy.ConfigurationController;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class MediaViewController_Factory implements Factory<MediaViewController> {
    private final Provider<ConfigurationController> configurationControllerProvider;
    private final Provider<Context> contextProvider;
    private final Provider<MediaHostStatesManager> mediaHostStatesManagerProvider;

    public MediaViewController_Factory(Provider<Context> provider, Provider<ConfigurationController> provider2, Provider<MediaHostStatesManager> provider3) {
        this.contextProvider = provider;
        this.configurationControllerProvider = provider2;
        this.mediaHostStatesManagerProvider = provider3;
    }

    @Override // javax.inject.Provider
    public MediaViewController get() {
        return newInstance(this.contextProvider.get(), this.configurationControllerProvider.get(), this.mediaHostStatesManagerProvider.get());
    }

    public static MediaViewController_Factory create(Provider<Context> provider, Provider<ConfigurationController> provider2, Provider<MediaHostStatesManager> provider3) {
        return new MediaViewController_Factory(provider, provider2, provider3);
    }

    public static MediaViewController newInstance(Context context, ConfigurationController configurationController, MediaHostStatesManager mediaHostStatesManager) {
        return new MediaViewController(context, configurationController, mediaHostStatesManager);
    }
}
