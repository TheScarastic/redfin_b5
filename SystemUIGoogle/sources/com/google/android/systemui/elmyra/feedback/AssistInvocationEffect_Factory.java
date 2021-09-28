package com.google.android.systemui.elmyra.feedback;

import com.google.android.systemui.assist.AssistManagerGoogle;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class AssistInvocationEffect_Factory implements Factory<AssistInvocationEffect> {
    private final Provider<AssistManagerGoogle> assistManagerGoogleProvider;
    private final Provider<OpaHomeButton> opaHomeButtonProvider;
    private final Provider<OpaLockscreen> opaLockscreenProvider;

    public AssistInvocationEffect_Factory(Provider<AssistManagerGoogle> provider, Provider<OpaHomeButton> provider2, Provider<OpaLockscreen> provider3) {
        this.assistManagerGoogleProvider = provider;
        this.opaHomeButtonProvider = provider2;
        this.opaLockscreenProvider = provider3;
    }

    @Override // javax.inject.Provider
    public AssistInvocationEffect get() {
        return newInstance(this.assistManagerGoogleProvider.get(), this.opaHomeButtonProvider.get(), this.opaLockscreenProvider.get());
    }

    public static AssistInvocationEffect_Factory create(Provider<AssistManagerGoogle> provider, Provider<OpaHomeButton> provider2, Provider<OpaLockscreen> provider3) {
        return new AssistInvocationEffect_Factory(provider, provider2, provider3);
    }

    public static AssistInvocationEffect newInstance(AssistManagerGoogle assistManagerGoogle, OpaHomeButton opaHomeButton, OpaLockscreen opaLockscreen) {
        return new AssistInvocationEffect(assistManagerGoogle, opaHomeButton, opaLockscreen);
    }
}
