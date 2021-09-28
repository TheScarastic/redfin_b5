package com.google.android.systemui.assist.uihints;

import android.content.Context;
import com.google.android.systemui.assist.GoogleAssistLogger;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class GoogleDefaultUiController_Factory implements Factory<GoogleDefaultUiController> {
    private final Provider<Context> contextProvider;
    private final Provider<GoogleAssistLogger> googleAssistLoggerProvider;

    public GoogleDefaultUiController_Factory(Provider<Context> provider, Provider<GoogleAssistLogger> provider2) {
        this.contextProvider = provider;
        this.googleAssistLoggerProvider = provider2;
    }

    @Override // javax.inject.Provider
    public GoogleDefaultUiController get() {
        return newInstance(this.contextProvider.get(), this.googleAssistLoggerProvider.get());
    }

    public static GoogleDefaultUiController_Factory create(Provider<Context> provider, Provider<GoogleAssistLogger> provider2) {
        return new GoogleDefaultUiController_Factory(provider, provider2);
    }

    public static GoogleDefaultUiController newInstance(Context context, GoogleAssistLogger googleAssistLogger) {
        return new GoogleDefaultUiController(context, googleAssistLogger);
    }
}
