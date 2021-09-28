package com.google.android.systemui.columbus.feedback;

import com.android.systemui.assist.AssistManager;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class AssistInvocationEffect_Factory implements Factory<AssistInvocationEffect> {
    private final Provider<AssistManager> assistManagerProvider;

    public AssistInvocationEffect_Factory(Provider<AssistManager> provider) {
        this.assistManagerProvider = provider;
    }

    @Override // javax.inject.Provider
    public AssistInvocationEffect get() {
        return newInstance(this.assistManagerProvider.get());
    }

    public static AssistInvocationEffect_Factory create(Provider<AssistManager> provider) {
        return new AssistInvocationEffect_Factory(provider);
    }

    public static AssistInvocationEffect newInstance(AssistManager assistManager) {
        return new AssistInvocationEffect(assistManager);
    }
}
