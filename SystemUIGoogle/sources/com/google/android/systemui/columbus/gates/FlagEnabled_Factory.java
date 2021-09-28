package com.google.android.systemui.columbus.gates;

import android.content.Context;
import android.os.Handler;
import com.google.android.systemui.columbus.ColumbusSettings;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class FlagEnabled_Factory implements Factory<FlagEnabled> {
    private final Provider<ColumbusSettings> columbusSettingsProvider;
    private final Provider<Context> contextProvider;
    private final Provider<Handler> handlerProvider;

    public FlagEnabled_Factory(Provider<Context> provider, Provider<ColumbusSettings> provider2, Provider<Handler> provider3) {
        this.contextProvider = provider;
        this.columbusSettingsProvider = provider2;
        this.handlerProvider = provider3;
    }

    @Override // javax.inject.Provider
    public FlagEnabled get() {
        return newInstance(this.contextProvider.get(), this.columbusSettingsProvider.get(), this.handlerProvider.get());
    }

    public static FlagEnabled_Factory create(Provider<Context> provider, Provider<ColumbusSettings> provider2, Provider<Handler> provider3) {
        return new FlagEnabled_Factory(provider, provider2, provider3);
    }

    public static FlagEnabled newInstance(Context context, ColumbusSettings columbusSettings, Handler handler) {
        return new FlagEnabled(context, columbusSettings, handler);
    }
}
