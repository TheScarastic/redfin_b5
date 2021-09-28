package com.android.systemui.statusbar.policy;

import android.os.Looper;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class CallbackHandler_Factory implements Factory<CallbackHandler> {
    private final Provider<Looper> looperProvider;

    public CallbackHandler_Factory(Provider<Looper> provider) {
        this.looperProvider = provider;
    }

    @Override // javax.inject.Provider
    public CallbackHandler get() {
        return newInstance(this.looperProvider.get());
    }

    public static CallbackHandler_Factory create(Provider<Looper> provider) {
        return new CallbackHandler_Factory(provider);
    }

    public static CallbackHandler newInstance(Looper looper) {
        return new CallbackHandler(looper);
    }
}
