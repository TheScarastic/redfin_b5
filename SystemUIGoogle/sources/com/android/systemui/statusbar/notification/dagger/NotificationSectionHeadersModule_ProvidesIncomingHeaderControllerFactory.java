package com.android.systemui.statusbar.notification.dagger;

import com.android.systemui.statusbar.notification.collection.render.SectionHeaderController;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class NotificationSectionHeadersModule_ProvidesIncomingHeaderControllerFactory implements Factory<SectionHeaderController> {
    private final Provider<SectionHeaderControllerSubcomponent> subcomponentProvider;

    public NotificationSectionHeadersModule_ProvidesIncomingHeaderControllerFactory(Provider<SectionHeaderControllerSubcomponent> provider) {
        this.subcomponentProvider = provider;
    }

    @Override // javax.inject.Provider
    public SectionHeaderController get() {
        return providesIncomingHeaderController(this.subcomponentProvider.get());
    }

    public static NotificationSectionHeadersModule_ProvidesIncomingHeaderControllerFactory create(Provider<SectionHeaderControllerSubcomponent> provider) {
        return new NotificationSectionHeadersModule_ProvidesIncomingHeaderControllerFactory(provider);
    }

    public static SectionHeaderController providesIncomingHeaderController(SectionHeaderControllerSubcomponent sectionHeaderControllerSubcomponent) {
        return (SectionHeaderController) Preconditions.checkNotNullFromProvides(NotificationSectionHeadersModule.providesIncomingHeaderController(sectionHeaderControllerSubcomponent));
    }
}
