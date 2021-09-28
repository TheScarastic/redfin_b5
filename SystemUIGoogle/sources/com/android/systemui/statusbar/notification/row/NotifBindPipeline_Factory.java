package com.android.systemui.statusbar.notification.row;

import android.os.Looper;
import com.android.systemui.statusbar.notification.collection.notifcollection.CommonNotifCollection;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class NotifBindPipeline_Factory implements Factory<NotifBindPipeline> {
    private final Provider<CommonNotifCollection> collectionProvider;
    private final Provider<NotifBindPipelineLogger> loggerProvider;
    private final Provider<Looper> mainLooperProvider;

    public NotifBindPipeline_Factory(Provider<CommonNotifCollection> provider, Provider<NotifBindPipelineLogger> provider2, Provider<Looper> provider3) {
        this.collectionProvider = provider;
        this.loggerProvider = provider2;
        this.mainLooperProvider = provider3;
    }

    @Override // javax.inject.Provider
    public NotifBindPipeline get() {
        return newInstance(this.collectionProvider.get(), this.loggerProvider.get(), this.mainLooperProvider.get());
    }

    public static NotifBindPipeline_Factory create(Provider<CommonNotifCollection> provider, Provider<NotifBindPipelineLogger> provider2, Provider<Looper> provider3) {
        return new NotifBindPipeline_Factory(provider, provider2, provider3);
    }

    public static NotifBindPipeline newInstance(CommonNotifCollection commonNotifCollection, NotifBindPipelineLogger notifBindPipelineLogger, Looper looper) {
        return new NotifBindPipeline(commonNotifCollection, notifBindPipelineLogger, looper);
    }
}
