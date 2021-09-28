package com.android.systemui.statusbar.notification.collection.init;

import com.android.systemui.dump.DumpManager;
import com.android.systemui.statusbar.FeatureFlags;
import com.android.systemui.statusbar.notification.collection.NotifCollection;
import com.android.systemui.statusbar.notification.collection.NotifInflaterImpl;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.ShadeListBuilder;
import com.android.systemui.statusbar.notification.collection.coalescer.GroupCoalescer;
import com.android.systemui.statusbar.notification.collection.coordinator.NotifCoordinators;
import com.android.systemui.statusbar.notification.collection.render.ShadeViewManagerFactory;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class NotifPipelineInitializer_Factory implements Factory<NotifPipelineInitializer> {
    private final Provider<DumpManager> dumpManagerProvider;
    private final Provider<FeatureFlags> featureFlagsProvider;
    private final Provider<GroupCoalescer> groupCoalescerProvider;
    private final Provider<ShadeListBuilder> listBuilderProvider;
    private final Provider<NotifCollection> notifCollectionProvider;
    private final Provider<NotifCoordinators> notifCoordinatorsProvider;
    private final Provider<NotifInflaterImpl> notifInflaterProvider;
    private final Provider<NotifPipeline> pipelineWrapperProvider;
    private final Provider<ShadeViewManagerFactory> shadeViewManagerFactoryProvider;

    public NotifPipelineInitializer_Factory(Provider<NotifPipeline> provider, Provider<GroupCoalescer> provider2, Provider<NotifCollection> provider3, Provider<ShadeListBuilder> provider4, Provider<NotifCoordinators> provider5, Provider<NotifInflaterImpl> provider6, Provider<DumpManager> provider7, Provider<ShadeViewManagerFactory> provider8, Provider<FeatureFlags> provider9) {
        this.pipelineWrapperProvider = provider;
        this.groupCoalescerProvider = provider2;
        this.notifCollectionProvider = provider3;
        this.listBuilderProvider = provider4;
        this.notifCoordinatorsProvider = provider5;
        this.notifInflaterProvider = provider6;
        this.dumpManagerProvider = provider7;
        this.shadeViewManagerFactoryProvider = provider8;
        this.featureFlagsProvider = provider9;
    }

    @Override // javax.inject.Provider
    public NotifPipelineInitializer get() {
        return newInstance(this.pipelineWrapperProvider.get(), this.groupCoalescerProvider.get(), this.notifCollectionProvider.get(), this.listBuilderProvider.get(), this.notifCoordinatorsProvider.get(), this.notifInflaterProvider.get(), this.dumpManagerProvider.get(), this.shadeViewManagerFactoryProvider.get(), this.featureFlagsProvider.get());
    }

    public static NotifPipelineInitializer_Factory create(Provider<NotifPipeline> provider, Provider<GroupCoalescer> provider2, Provider<NotifCollection> provider3, Provider<ShadeListBuilder> provider4, Provider<NotifCoordinators> provider5, Provider<NotifInflaterImpl> provider6, Provider<DumpManager> provider7, Provider<ShadeViewManagerFactory> provider8, Provider<FeatureFlags> provider9) {
        return new NotifPipelineInitializer_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9);
    }

    public static NotifPipelineInitializer newInstance(NotifPipeline notifPipeline, GroupCoalescer groupCoalescer, NotifCollection notifCollection, ShadeListBuilder shadeListBuilder, NotifCoordinators notifCoordinators, NotifInflaterImpl notifInflaterImpl, DumpManager dumpManager, ShadeViewManagerFactory shadeViewManagerFactory, FeatureFlags featureFlags) {
        return new NotifPipelineInitializer(notifPipeline, groupCoalescer, notifCollection, shadeListBuilder, notifCoordinators, notifInflaterImpl, dumpManager, shadeViewManagerFactory, featureFlags);
    }
}
