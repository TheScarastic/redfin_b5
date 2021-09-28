package com.android.systemui.assist;

import android.content.Context;
import com.android.internal.app.AssistUtils;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class AssistModule_ProvideAssistUtilsFactory implements Factory<AssistUtils> {
    private final Provider<Context> contextProvider;

    public AssistModule_ProvideAssistUtilsFactory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    @Override // javax.inject.Provider
    public AssistUtils get() {
        return provideAssistUtils(this.contextProvider.get());
    }

    public static AssistModule_ProvideAssistUtilsFactory create(Provider<Context> provider) {
        return new AssistModule_ProvideAssistUtilsFactory(provider);
    }

    public static AssistUtils provideAssistUtils(Context context) {
        return (AssistUtils) Preconditions.checkNotNullFromProvides(AssistModule.provideAssistUtils(context));
    }
}
