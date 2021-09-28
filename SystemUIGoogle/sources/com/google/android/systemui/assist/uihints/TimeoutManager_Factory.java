package com.google.android.systemui.assist.uihints;

import com.android.systemui.assist.AssistManager;
import dagger.Lazy;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class TimeoutManager_Factory implements Factory<TimeoutManager> {
    private final Provider<AssistManager> assistManagerProvider;

    public TimeoutManager_Factory(Provider<AssistManager> provider) {
        this.assistManagerProvider = provider;
    }

    @Override // javax.inject.Provider
    public TimeoutManager get() {
        return newInstance(DoubleCheck.lazy(this.assistManagerProvider));
    }

    public static TimeoutManager_Factory create(Provider<AssistManager> provider) {
        return new TimeoutManager_Factory(provider);
    }

    public static TimeoutManager newInstance(Lazy<AssistManager> lazy) {
        return new TimeoutManager(lazy);
    }
}
