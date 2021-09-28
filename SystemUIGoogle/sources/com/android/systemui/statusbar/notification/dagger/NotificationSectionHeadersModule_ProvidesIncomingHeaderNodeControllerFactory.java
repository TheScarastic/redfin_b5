package com.android.systemui.statusbar.notification.dagger;

import com.android.systemui.statusbar.notification.collection.render.NodeController;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class NotificationSectionHeadersModule_ProvidesIncomingHeaderNodeControllerFactory implements Factory<NodeController> {
    private final Provider<SectionHeaderControllerSubcomponent> subcomponentProvider;

    public NotificationSectionHeadersModule_ProvidesIncomingHeaderNodeControllerFactory(Provider<SectionHeaderControllerSubcomponent> provider) {
        this.subcomponentProvider = provider;
    }

    @Override // javax.inject.Provider
    public NodeController get() {
        return providesIncomingHeaderNodeController(this.subcomponentProvider.get());
    }

    public static NotificationSectionHeadersModule_ProvidesIncomingHeaderNodeControllerFactory create(Provider<SectionHeaderControllerSubcomponent> provider) {
        return new NotificationSectionHeadersModule_ProvidesIncomingHeaderNodeControllerFactory(provider);
    }

    public static NodeController providesIncomingHeaderNodeController(SectionHeaderControllerSubcomponent sectionHeaderControllerSubcomponent) {
        return (NodeController) Preconditions.checkNotNullFromProvides(NotificationSectionHeadersModule.providesIncomingHeaderNodeController(sectionHeaderControllerSubcomponent));
    }
}
