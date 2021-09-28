package com.android.systemui.tv;

import dagger.internal.Factory;
/* loaded from: classes2.dex */
public final class TvSystemUIModule_ProvideLeakReportEmailFactory implements Factory<String> {

    /* loaded from: classes2.dex */
    private static final class InstanceHolder {
        private static final TvSystemUIModule_ProvideLeakReportEmailFactory INSTANCE = new TvSystemUIModule_ProvideLeakReportEmailFactory();
    }

    @Override // javax.inject.Provider
    public String get() {
        return provideLeakReportEmail();
    }

    public static TvSystemUIModule_ProvideLeakReportEmailFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static String provideLeakReportEmail() {
        return TvSystemUIModule.provideLeakReportEmail();
    }
}
