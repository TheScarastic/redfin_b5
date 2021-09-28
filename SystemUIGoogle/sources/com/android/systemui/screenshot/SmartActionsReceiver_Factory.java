package com.android.systemui.screenshot;

import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class SmartActionsReceiver_Factory implements Factory<SmartActionsReceiver> {
    private final Provider<ScreenshotSmartActions> screenshotSmartActionsProvider;

    public SmartActionsReceiver_Factory(Provider<ScreenshotSmartActions> provider) {
        this.screenshotSmartActionsProvider = provider;
    }

    @Override // javax.inject.Provider
    public SmartActionsReceiver get() {
        return newInstance(this.screenshotSmartActionsProvider.get());
    }

    public static SmartActionsReceiver_Factory create(Provider<ScreenshotSmartActions> provider) {
        return new SmartActionsReceiver_Factory(provider);
    }

    public static SmartActionsReceiver newInstance(ScreenshotSmartActions screenshotSmartActions) {
        return new SmartActionsReceiver(screenshotSmartActions);
    }
}
