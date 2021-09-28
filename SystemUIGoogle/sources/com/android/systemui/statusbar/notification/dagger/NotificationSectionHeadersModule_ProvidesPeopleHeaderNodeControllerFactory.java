package com.android.systemui.statusbar.notification.dagger;

import com.android.systemui.statusbar.notification.collection.render.NodeController;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class NotificationSectionHeadersModule_ProvidesPeopleHeaderNodeControllerFactory implements Factory<NodeController> {
    private final Provider<SectionHeaderControllerSubcomponent> subcomponentProvider;

    public NotificationSectionHeadersModule_ProvidesPeopleHeaderNodeControllerFactory(Provider<SectionHeaderControllerSubcomponent> provider) {
        this.subcomponentProvider = provider;
    }

    @Override // javax.inject.Provider
    public NodeController get() {
        return providesPeopleHeaderNodeController(this.subcomponentProvider.get());
    }

    public static NotificationSectionHeadersModule_ProvidesPeopleHeaderNodeControllerFactory create(Provider<SectionHeaderControllerSubcomponent> provider) {
        return new NotificationSectionHeadersModule_ProvidesPeopleHeaderNodeControllerFactory(provider);
    }

    public static NodeController providesPeopleHeaderNodeController(SectionHeaderControllerSubcomponent sectionHeaderControllerSubcomponent) {
        return (NodeController) Preconditions.checkNotNullFromProvides(NotificationSectionHeadersModule.providesPeopleHeaderNodeController(sectionHeaderControllerSubcomponent));
    }
}
