package com.android.systemui.statusbar.notification.row;

import com.android.systemui.statusbar.notification.collection.notifcollection.CommonNotifCollection;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class NotifRemoteViewCacheImpl_Factory implements Factory<NotifRemoteViewCacheImpl> {
    private final Provider<CommonNotifCollection> collectionProvider;

    public NotifRemoteViewCacheImpl_Factory(Provider<CommonNotifCollection> provider) {
        this.collectionProvider = provider;
    }

    @Override // javax.inject.Provider
    public NotifRemoteViewCacheImpl get() {
        return newInstance(this.collectionProvider.get());
    }

    public static NotifRemoteViewCacheImpl_Factory create(Provider<CommonNotifCollection> provider) {
        return new NotifRemoteViewCacheImpl_Factory(provider);
    }

    public static NotifRemoteViewCacheImpl newInstance(CommonNotifCollection commonNotifCollection) {
        return new NotifRemoteViewCacheImpl(commonNotifCollection);
    }
}
