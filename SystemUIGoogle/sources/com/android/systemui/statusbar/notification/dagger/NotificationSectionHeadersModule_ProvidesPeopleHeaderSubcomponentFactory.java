package com.android.systemui.statusbar.notification.dagger;

import com.android.systemui.statusbar.notification.dagger.SectionHeaderControllerSubcomponent;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class NotificationSectionHeadersModule_ProvidesPeopleHeaderSubcomponentFactory implements Factory<SectionHeaderControllerSubcomponent> {
    private final Provider<SectionHeaderControllerSubcomponent.Builder> builderProvider;

    public NotificationSectionHeadersModule_ProvidesPeopleHeaderSubcomponentFactory(Provider<SectionHeaderControllerSubcomponent.Builder> provider) {
        this.builderProvider = provider;
    }

    @Override // javax.inject.Provider
    public SectionHeaderControllerSubcomponent get() {
        return providesPeopleHeaderSubcomponent(this.builderProvider);
    }

    public static NotificationSectionHeadersModule_ProvidesPeopleHeaderSubcomponentFactory create(Provider<SectionHeaderControllerSubcomponent.Builder> provider) {
        return new NotificationSectionHeadersModule_ProvidesPeopleHeaderSubcomponentFactory(provider);
    }

    public static SectionHeaderControllerSubcomponent providesPeopleHeaderSubcomponent(Provider<SectionHeaderControllerSubcomponent.Builder> provider) {
        return (SectionHeaderControllerSubcomponent) Preconditions.checkNotNullFromProvides(NotificationSectionHeadersModule.providesPeopleHeaderSubcomponent(provider));
    }
}
