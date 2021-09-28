package com.android.systemui.dagger;

import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class GlobalModule_ProvideIsTestHarnessFactory implements Factory<Boolean> {

    /* access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class InstanceHolder {
        private static final GlobalModule_ProvideIsTestHarnessFactory INSTANCE = new GlobalModule_ProvideIsTestHarnessFactory();
    }

    @Override // javax.inject.Provider
    public Boolean get() {
        return Boolean.valueOf(provideIsTestHarness());
    }

    public static GlobalModule_ProvideIsTestHarnessFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static boolean provideIsTestHarness() {
        return GlobalModule.provideIsTestHarness();
    }
}
