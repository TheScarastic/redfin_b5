package com.android.systemui.statusbar.notification.dagger;

import com.android.systemui.statusbar.notification.dagger.SectionHeaderControllerSubcomponent;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class NotificationSectionHeadersModule_ProvidesAlertingHeaderSubcomponentFactory implements Factory<SectionHeaderControllerSubcomponent> {
    private final Provider<SectionHeaderControllerSubcomponent.Builder> builderProvider;

    public NotificationSectionHeadersModule_ProvidesAlertingHeaderSubcomponentFactory(Provider<SectionHeaderControllerSubcomponent.Builder> provider) {
        this.builderProvider = provider;
    }

    @Override // javax.inject.Provider
    public SectionHeaderControllerSubcomponent get() {
        return providesAlertingHeaderSubcomponent(this.builderProvider);
    }

    public static NotificationSectionHeadersModule_ProvidesAlertingHeaderSubcomponentFactory create(Provider<SectionHeaderControllerSubcomponent.Builder> provider) {
        return new NotificationSectionHeadersModule_ProvidesAlertingHeaderSubcomponentFactory(provider);
    }

    public static SectionHeaderControllerSubcomponent providesAlertingHeaderSubcomponent(Provider<SectionHeaderControllerSubcomponent.Builder> provider) {
        return (SectionHeaderControllerSubcomponent) Preconditions.checkNotNullFromProvides(NotificationSectionHeadersModule.providesAlertingHeaderSubcomponent(provider));
    }
}
