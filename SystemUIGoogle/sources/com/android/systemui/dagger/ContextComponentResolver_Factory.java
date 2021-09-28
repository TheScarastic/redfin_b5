package com.android.systemui.dagger;

import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import com.android.systemui.SystemUI;
import com.android.systemui.recents.RecentsImplementation;
import dagger.internal.Factory;
import java.util.Map;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class ContextComponentResolver_Factory implements Factory<ContextComponentResolver> {
    private final Provider<Map<Class<?>, Provider<Activity>>> activityCreatorsProvider;
    private final Provider<Map<Class<?>, Provider<BroadcastReceiver>>> broadcastReceiverCreatorsProvider;
    private final Provider<Map<Class<?>, Provider<RecentsImplementation>>> recentsCreatorsProvider;
    private final Provider<Map<Class<?>, Provider<Service>>> serviceCreatorsProvider;
    private final Provider<Map<Class<?>, Provider<SystemUI>>> systemUICreatorsProvider;

    public ContextComponentResolver_Factory(Provider<Map<Class<?>, Provider<Activity>>> provider, Provider<Map<Class<?>, Provider<Service>>> provider2, Provider<Map<Class<?>, Provider<SystemUI>>> provider3, Provider<Map<Class<?>, Provider<RecentsImplementation>>> provider4, Provider<Map<Class<?>, Provider<BroadcastReceiver>>> provider5) {
        this.activityCreatorsProvider = provider;
        this.serviceCreatorsProvider = provider2;
        this.systemUICreatorsProvider = provider3;
        this.recentsCreatorsProvider = provider4;
        this.broadcastReceiverCreatorsProvider = provider5;
    }

    @Override // javax.inject.Provider
    public ContextComponentResolver get() {
        return newInstance(this.activityCreatorsProvider.get(), this.serviceCreatorsProvider.get(), this.systemUICreatorsProvider.get(), this.recentsCreatorsProvider.get(), this.broadcastReceiverCreatorsProvider.get());
    }

    public static ContextComponentResolver_Factory create(Provider<Map<Class<?>, Provider<Activity>>> provider, Provider<Map<Class<?>, Provider<Service>>> provider2, Provider<Map<Class<?>, Provider<SystemUI>>> provider3, Provider<Map<Class<?>, Provider<RecentsImplementation>>> provider4, Provider<Map<Class<?>, Provider<BroadcastReceiver>>> provider5) {
        return new ContextComponentResolver_Factory(provider, provider2, provider3, provider4, provider5);
    }

    public static ContextComponentResolver newInstance(Map<Class<?>, Provider<Activity>> map, Map<Class<?>, Provider<Service>> map2, Map<Class<?>, Provider<SystemUI>> map3, Map<Class<?>, Provider<RecentsImplementation>> map4, Map<Class<?>, Provider<BroadcastReceiver>> map5) {
        return new ContextComponentResolver(map, map2, map3, map4, map5);
    }
}
