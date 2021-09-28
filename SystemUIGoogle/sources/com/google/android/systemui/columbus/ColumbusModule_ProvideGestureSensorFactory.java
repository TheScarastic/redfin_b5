package com.google.android.systemui.columbus;

import android.content.Context;
import com.google.android.systemui.columbus.sensors.CHREGestureSensor;
import com.google.android.systemui.columbus.sensors.GestureSensor;
import com.google.android.systemui.columbus.sensors.GestureSensorImpl;
import dagger.Lazy;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class ColumbusModule_ProvideGestureSensorFactory implements Factory<GestureSensor> {
    private final Provider<GestureSensorImpl> apGestureSensorProvider;
    private final Provider<CHREGestureSensor> chreGestureSensorProvider;
    private final Provider<ColumbusSettings> columbusSettingsProvider;
    private final Provider<Context> contextProvider;

    public ColumbusModule_ProvideGestureSensorFactory(Provider<Context> provider, Provider<ColumbusSettings> provider2, Provider<CHREGestureSensor> provider3, Provider<GestureSensorImpl> provider4) {
        this.contextProvider = provider;
        this.columbusSettingsProvider = provider2;
        this.chreGestureSensorProvider = provider3;
        this.apGestureSensorProvider = provider4;
    }

    @Override // javax.inject.Provider
    public GestureSensor get() {
        return provideGestureSensor(this.contextProvider.get(), this.columbusSettingsProvider.get(), DoubleCheck.lazy(this.chreGestureSensorProvider), DoubleCheck.lazy(this.apGestureSensorProvider));
    }

    public static ColumbusModule_ProvideGestureSensorFactory create(Provider<Context> provider, Provider<ColumbusSettings> provider2, Provider<CHREGestureSensor> provider3, Provider<GestureSensorImpl> provider4) {
        return new ColumbusModule_ProvideGestureSensorFactory(provider, provider2, provider3, provider4);
    }

    public static GestureSensor provideGestureSensor(Context context, ColumbusSettings columbusSettings, Lazy<CHREGestureSensor> lazy, Lazy<GestureSensorImpl> lazy2) {
        return (GestureSensor) Preconditions.checkNotNullFromProvides(ColumbusModule.provideGestureSensor(context, columbusSettings, lazy, lazy2));
    }
}
