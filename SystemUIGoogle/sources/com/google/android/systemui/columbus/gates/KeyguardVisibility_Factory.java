package com.google.android.systemui.columbus.gates;

import android.content.Context;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import dagger.Lazy;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class KeyguardVisibility_Factory implements Factory<KeyguardVisibility> {
    private final Provider<Context> contextProvider;
    private final Provider<KeyguardStateController> keyguardStateControllerProvider;

    public KeyguardVisibility_Factory(Provider<Context> provider, Provider<KeyguardStateController> provider2) {
        this.contextProvider = provider;
        this.keyguardStateControllerProvider = provider2;
    }

    @Override // javax.inject.Provider
    public KeyguardVisibility get() {
        return newInstance(this.contextProvider.get(), DoubleCheck.lazy(this.keyguardStateControllerProvider));
    }

    public static KeyguardVisibility_Factory create(Provider<Context> provider, Provider<KeyguardStateController> provider2) {
        return new KeyguardVisibility_Factory(provider, provider2);
    }

    public static KeyguardVisibility newInstance(Context context, Lazy<KeyguardStateController> lazy) {
        return new KeyguardVisibility(context, lazy);
    }
}
