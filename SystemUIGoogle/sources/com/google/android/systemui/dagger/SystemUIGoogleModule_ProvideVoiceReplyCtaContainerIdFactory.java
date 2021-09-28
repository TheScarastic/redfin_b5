package com.google.android.systemui.dagger;

import dagger.internal.Factory;
/* loaded from: classes2.dex */
public final class SystemUIGoogleModule_ProvideVoiceReplyCtaContainerIdFactory implements Factory<Integer> {

    /* access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static final class InstanceHolder {
        private static final SystemUIGoogleModule_ProvideVoiceReplyCtaContainerIdFactory INSTANCE = new SystemUIGoogleModule_ProvideVoiceReplyCtaContainerIdFactory();
    }

    @Override // javax.inject.Provider
    public Integer get() {
        return Integer.valueOf(provideVoiceReplyCtaContainerId());
    }

    public static SystemUIGoogleModule_ProvideVoiceReplyCtaContainerIdFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static int provideVoiceReplyCtaContainerId() {
        return SystemUIGoogleModule.provideVoiceReplyCtaContainerId();
    }
}
