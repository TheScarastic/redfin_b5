package com.android.systemui.statusbar.notification.dagger;

import com.android.systemui.statusbar.notification.collection.render.NodeController;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class NotificationSectionHeadersModule_ProvidesAlertingHeaderNodeControllerFactory implements Factory<NodeController> {
    private final Provider<SectionHeaderControllerSubcomponent> subcomponentProvider;

    public NotificationSectionHeadersModule_ProvidesAlertingHeaderNodeControllerFactory(Provider<SectionHeaderControllerSubcomponent> provider) {
        this.subcomponentProvider = provider;
    }

    @Override // javax.inject.Provider
    public NodeController get() {
        return providesAlertingHeaderNodeController(this.subcomponentProvider.get());
    }

    public static NotificationSectionHeadersModule_ProvidesAlertingHeaderNodeControllerFactory create(Provider<SectionHeaderControllerSubcomponent> provider) {
        return new NotificationSectionHeadersModule_ProvidesAlertingHeaderNodeControllerFactory(provider);
    }

    public static NodeController providesAlertingHeaderNodeController(SectionHeaderControllerSubcomponent sectionHeaderControllerSubcomponent) {
        return (NodeController) Preconditions.checkNotNullFromProvides(NotificationSectionHeadersModule.providesAlertingHeaderNodeController(sectionHeaderControllerSubcomponent));
    }
}
