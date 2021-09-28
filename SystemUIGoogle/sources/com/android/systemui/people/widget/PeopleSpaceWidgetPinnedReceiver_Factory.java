package com.android.systemui.people.widget;

import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class PeopleSpaceWidgetPinnedReceiver_Factory implements Factory<PeopleSpaceWidgetPinnedReceiver> {
    private final Provider<PeopleSpaceWidgetManager> peopleSpaceWidgetManagerProvider;

    public PeopleSpaceWidgetPinnedReceiver_Factory(Provider<PeopleSpaceWidgetManager> provider) {
        this.peopleSpaceWidgetManagerProvider = provider;
    }

    @Override // javax.inject.Provider
    public PeopleSpaceWidgetPinnedReceiver get() {
        return newInstance(this.peopleSpaceWidgetManagerProvider.get());
    }

    public static PeopleSpaceWidgetPinnedReceiver_Factory create(Provider<PeopleSpaceWidgetManager> provider) {
        return new PeopleSpaceWidgetPinnedReceiver_Factory(provider);
    }

    public static PeopleSpaceWidgetPinnedReceiver newInstance(PeopleSpaceWidgetManager peopleSpaceWidgetManager) {
        return new PeopleSpaceWidgetPinnedReceiver(peopleSpaceWidgetManager);
    }
}
