package com.google.android.systemui.columbus;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import java.util.Set;
/* loaded from: classes2.dex */
public final class ColumbusModule_ProvideBlockingSystemKeysFactory implements Factory<Set<Integer>> {

    /* loaded from: classes2.dex */
    private static final class InstanceHolder {
        private static final ColumbusModule_ProvideBlockingSystemKeysFactory INSTANCE = new ColumbusModule_ProvideBlockingSystemKeysFactory();
    }

    @Override // javax.inject.Provider
    public Set<Integer> get() {
        return provideBlockingSystemKeys();
    }

    public static ColumbusModule_ProvideBlockingSystemKeysFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static Set<Integer> provideBlockingSystemKeys() {
        return (Set) Preconditions.checkNotNullFromProvides(ColumbusModule.provideBlockingSystemKeys());
    }
}
