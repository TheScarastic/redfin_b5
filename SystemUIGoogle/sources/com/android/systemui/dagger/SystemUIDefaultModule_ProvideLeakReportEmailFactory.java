package com.android.systemui.dagger;

import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class SystemUIDefaultModule_ProvideLeakReportEmailFactory implements Factory<String> {

    /* access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class InstanceHolder {
        private static final SystemUIDefaultModule_ProvideLeakReportEmailFactory INSTANCE = new SystemUIDefaultModule_ProvideLeakReportEmailFactory();
    }

    @Override // javax.inject.Provider
    public String get() {
        return provideLeakReportEmail();
    }

    public static SystemUIDefaultModule_ProvideLeakReportEmailFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static String provideLeakReportEmail() {
        return SystemUIDefaultModule.provideLeakReportEmail();
    }
}
