package com.google.android.systemui.dagger;

import dagger.internal.Factory;
/* loaded from: classes2.dex */
public final class SystemUIGoogleModule_ProvideVoiceReplyCtaLayoutFactory implements Factory<Integer> {

    /* access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static final class InstanceHolder {
        private static final SystemUIGoogleModule_ProvideVoiceReplyCtaLayoutFactory INSTANCE = new SystemUIGoogleModule_ProvideVoiceReplyCtaLayoutFactory();
    }

    @Override // javax.inject.Provider
    public Integer get() {
        return Integer.valueOf(provideVoiceReplyCtaLayout());
    }

    public static SystemUIGoogleModule_ProvideVoiceReplyCtaLayoutFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static int provideVoiceReplyCtaLayout() {
        return SystemUIGoogleModule.provideVoiceReplyCtaLayout();
    }
}
