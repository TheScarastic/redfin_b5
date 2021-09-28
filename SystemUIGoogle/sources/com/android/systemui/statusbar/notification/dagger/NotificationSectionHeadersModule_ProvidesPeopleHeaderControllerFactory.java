package com.android.systemui.statusbar.notification.dagger;

import com.android.systemui.statusbar.notification.collection.render.SectionHeaderController;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class NotificationSectionHeadersModule_ProvidesPeopleHeaderControllerFactory implements Factory<SectionHeaderController> {
    private final Provider<SectionHeaderControllerSubcomponent> subcomponentProvider;

    public NotificationSectionHeadersModule_ProvidesPeopleHeaderControllerFactory(Provider<SectionHeaderControllerSubcomponent> provider) {
        this.subcomponentProvider = provider;
    }

    @Override // javax.inject.Provider
    public SectionHeaderController get() {
        return providesPeopleHeaderController(this.subcomponentProvider.get());
    }

    public static NotificationSectionHeadersModule_ProvidesPeopleHeaderControllerFactory create(Provider<SectionHeaderControllerSubcomponent> provider) {
        return new NotificationSectionHeadersModule_ProvidesPeopleHeaderControllerFactory(provider);
    }

    public static SectionHeaderController providesPeopleHeaderController(SectionHeaderControllerSubcomponent sectionHeaderControllerSubcomponent) {
        return (SectionHeaderController) Preconditions.checkNotNullFromProvides(NotificationSectionHeadersModule.providesPeopleHeaderController(sectionHeaderControllerSubcomponent));
    }
}
