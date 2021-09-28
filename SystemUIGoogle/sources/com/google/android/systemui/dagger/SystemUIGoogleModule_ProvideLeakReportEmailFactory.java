package com.google.android.systemui.dagger;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
/* loaded from: classes2.dex */
public final class SystemUIGoogleModule_ProvideLeakReportEmailFactory implements Factory<String> {

    /* access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static final class InstanceHolder {
        private static final SystemUIGoogleModule_ProvideLeakReportEmailFactory INSTANCE = new SystemUIGoogleModule_ProvideLeakReportEmailFactory();
    }

    @Override // javax.inject.Provider
    public String get() {
        return provideLeakReportEmail();
    }

    public static SystemUIGoogleModule_ProvideLeakReportEmailFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static String provideLeakReportEmail() {
        return (String) Preconditions.checkNotNullFromProvides(SystemUIGoogleModule.provideLeakReportEmail());
    }
}
