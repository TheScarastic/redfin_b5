package com.android.systemui.statusbar.notification.collection;

import com.android.internal.statusbar.IStatusBarService;
import com.android.systemui.statusbar.notification.row.NotifInflationErrorManager;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class NotifInflaterImpl_Factory implements Factory<NotifInflaterImpl> {
    private final Provider<NotifInflationErrorManager> errorManagerProvider;
    private final Provider<NotifCollection> notifCollectionProvider;
    private final Provider<NotifPipeline> notifPipelineProvider;
    private final Provider<IStatusBarService> statusBarServiceProvider;

    public NotifInflaterImpl_Factory(Provider<IStatusBarService> provider, Provider<NotifCollection> provider2, Provider<NotifInflationErrorManager> provider3, Provider<NotifPipeline> provider4) {
        this.statusBarServiceProvider = provider;
        this.notifCollectionProvider = provider2;
        this.errorManagerProvider = provider3;
        this.notifPipelineProvider = provider4;
    }

    @Override // javax.inject.Provider
    public NotifInflaterImpl get() {
        return newInstance(this.statusBarServiceProvider.get(), this.notifCollectionProvider.get(), this.errorManagerProvider.get(), this.notifPipelineProvider.get());
    }

    public static NotifInflaterImpl_Factory create(Provider<IStatusBarService> provider, Provider<NotifCollection> provider2, Provider<NotifInflationErrorManager> provider3, Provider<NotifPipeline> provider4) {
        return new NotifInflaterImpl_Factory(provider, provider2, provider3, provider4);
    }

    public static NotifInflaterImpl newInstance(IStatusBarService iStatusBarService, NotifCollection notifCollection, NotifInflationErrorManager notifInflationErrorManager, NotifPipeline notifPipeline) {
        return new NotifInflaterImpl(iStatusBarService, notifCollection, notifInflationErrorManager, notifPipeline);
    }
}
