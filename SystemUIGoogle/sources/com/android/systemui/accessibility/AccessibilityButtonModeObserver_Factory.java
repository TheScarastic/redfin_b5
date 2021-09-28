package com.android.systemui.accessibility;

import android.content.Context;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class AccessibilityButtonModeObserver_Factory implements Factory<AccessibilityButtonModeObserver> {
    private final Provider<Context> contextProvider;

    public AccessibilityButtonModeObserver_Factory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    @Override // javax.inject.Provider
    public AccessibilityButtonModeObserver get() {
        return newInstance(this.contextProvider.get());
    }

    public static AccessibilityButtonModeObserver_Factory create(Provider<Context> provider) {
        return new AccessibilityButtonModeObserver_Factory(provider);
    }

    public static AccessibilityButtonModeObserver newInstance(Context context) {
        return new AccessibilityButtonModeObserver(context);
    }
}
