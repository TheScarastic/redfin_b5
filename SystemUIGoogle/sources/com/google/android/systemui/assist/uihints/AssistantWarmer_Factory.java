package com.google.android.systemui.assist.uihints;

import android.content.Context;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class AssistantWarmer_Factory implements Factory<AssistantWarmer> {
    private final Provider<Context> contextProvider;

    public AssistantWarmer_Factory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    @Override // javax.inject.Provider
    public AssistantWarmer get() {
        return newInstance(this.contextProvider.get());
    }

    public static AssistantWarmer_Factory create(Provider<Context> provider) {
        return new AssistantWarmer_Factory(provider);
    }

    public static AssistantWarmer newInstance(Context context) {
        return new AssistantWarmer(context);
    }
}
