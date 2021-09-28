package com.android.systemui.statusbar.notification.dagger;

import com.android.systemui.statusbar.notification.collection.render.NodeController;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class NotificationSectionHeadersModule_ProvidesSilentHeaderNodeControllerFactory implements Factory<NodeController> {
    private final Provider<SectionHeaderControllerSubcomponent> subcomponentProvider;

    public NotificationSectionHeadersModule_ProvidesSilentHeaderNodeControllerFactory(Provider<SectionHeaderControllerSubcomponent> provider) {
        this.subcomponentProvider = provider;
    }

    @Override // javax.inject.Provider
    public NodeController get() {
        return providesSilentHeaderNodeController(this.subcomponentProvider.get());
    }

    public static NotificationSectionHeadersModule_ProvidesSilentHeaderNodeControllerFactory create(Provider<SectionHeaderControllerSubcomponent> provider) {
        return new NotificationSectionHeadersModule_ProvidesSilentHeaderNodeControllerFactory(provider);
    }

    public static NodeController providesSilentHeaderNodeController(SectionHeaderControllerSubcomponent sectionHeaderControllerSubcomponent) {
        return (NodeController) Preconditions.checkNotNullFromProvides(NotificationSectionHeadersModule.providesSilentHeaderNodeController(sectionHeaderControllerSubcomponent));
    }
}
