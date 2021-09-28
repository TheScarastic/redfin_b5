package com.android.systemui.people.widget;

import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class PeopleSpaceWidgetProvider_Factory implements Factory<PeopleSpaceWidgetProvider> {
    private final Provider<PeopleSpaceWidgetManager> peopleSpaceWidgetManagerProvider;

    public PeopleSpaceWidgetProvider_Factory(Provider<PeopleSpaceWidgetManager> provider) {
        this.peopleSpaceWidgetManagerProvider = provider;
    }

    @Override // javax.inject.Provider
    public PeopleSpaceWidgetProvider get() {
        return newInstance(this.peopleSpaceWidgetManagerProvider.get());
    }

    public static PeopleSpaceWidgetProvider_Factory create(Provider<PeopleSpaceWidgetManager> provider) {
        return new PeopleSpaceWidgetProvider_Factory(provider);
    }

    public static PeopleSpaceWidgetProvider newInstance(PeopleSpaceWidgetManager peopleSpaceWidgetManager) {
        return new PeopleSpaceWidgetProvider(peopleSpaceWidgetManager);
    }
}
