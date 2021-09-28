package com.android.systemui.statusbar.notification.collection.render;

import android.view.LayoutInflater;
import com.android.systemui.plugins.ActivityStarter;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class SectionHeaderNodeControllerImpl_Factory implements Factory<SectionHeaderNodeControllerImpl> {
    private final Provider<ActivityStarter> activityStarterProvider;
    private final Provider<String> clickIntentActionProvider;
    private final Provider<Integer> headerTextResIdProvider;
    private final Provider<LayoutInflater> layoutInflaterProvider;
    private final Provider<String> nodeLabelProvider;

    public SectionHeaderNodeControllerImpl_Factory(Provider<String> provider, Provider<LayoutInflater> provider2, Provider<Integer> provider3, Provider<ActivityStarter> provider4, Provider<String> provider5) {
        this.nodeLabelProvider = provider;
        this.layoutInflaterProvider = provider2;
        this.headerTextResIdProvider = provider3;
        this.activityStarterProvider = provider4;
        this.clickIntentActionProvider = provider5;
    }

    @Override // javax.inject.Provider
    public SectionHeaderNodeControllerImpl get() {
        return newInstance(this.nodeLabelProvider.get(), this.layoutInflaterProvider.get(), this.headerTextResIdProvider.get().intValue(), this.activityStarterProvider.get(), this.clickIntentActionProvider.get());
    }

    public static SectionHeaderNodeControllerImpl_Factory create(Provider<String> provider, Provider<LayoutInflater> provider2, Provider<Integer> provider3, Provider<ActivityStarter> provider4, Provider<String> provider5) {
        return new SectionHeaderNodeControllerImpl_Factory(provider, provider2, provider3, provider4, provider5);
    }

    public static SectionHeaderNodeControllerImpl newInstance(String str, LayoutInflater layoutInflater, int i, ActivityStarter activityStarter, String str2) {
        return new SectionHeaderNodeControllerImpl(str, layoutInflater, i, activityStarter, str2);
    }
}
