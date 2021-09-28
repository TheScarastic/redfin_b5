package com.android.systemui.statusbar.notification.dagger;

import com.android.systemui.statusbar.notification.dagger.SectionHeaderControllerSubcomponent;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class NotificationSectionHeadersModule_ProvidesIncomingHeaderSubcomponentFactory implements Factory<SectionHeaderControllerSubcomponent> {
    private final Provider<SectionHeaderControllerSubcomponent.Builder> builderProvider;

    public NotificationSectionHeadersModule_ProvidesIncomingHeaderSubcomponentFactory(Provider<SectionHeaderControllerSubcomponent.Builder> provider) {
        this.builderProvider = provider;
    }

    @Override // javax.inject.Provider
    public SectionHeaderControllerSubcomponent get() {
        return providesIncomingHeaderSubcomponent(this.builderProvider);
    }

    public static NotificationSectionHeadersModule_ProvidesIncomingHeaderSubcomponentFactory create(Provider<SectionHeaderControllerSubcomponent.Builder> provider) {
        return new NotificationSectionHeadersModule_ProvidesIncomingHeaderSubcomponentFactory(provider);
    }

    public static SectionHeaderControllerSubcomponent providesIncomingHeaderSubcomponent(Provider<SectionHeaderControllerSubcomponent.Builder> provider) {
        return (SectionHeaderControllerSubcomponent) Preconditions.checkNotNullFromProvides(NotificationSectionHeadersModule.providesIncomingHeaderSubcomponent(provider));
    }
}
