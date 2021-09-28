package com.google.android.systemui.columbus.feedback;

import android.os.PowerManager;
import dagger.Lazy;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class UserActivity_Factory implements Factory<UserActivity> {
    private final Provider<PowerManager> powerManagerProvider;

    public UserActivity_Factory(Provider<PowerManager> provider) {
        this.powerManagerProvider = provider;
    }

    @Override // javax.inject.Provider
    public UserActivity get() {
        return newInstance(DoubleCheck.lazy(this.powerManagerProvider));
    }

    public static UserActivity_Factory create(Provider<PowerManager> provider) {
        return new UserActivity_Factory(provider);
    }

    public static UserActivity newInstance(Lazy<PowerManager> lazy) {
        return new UserActivity(lazy);
    }
}
