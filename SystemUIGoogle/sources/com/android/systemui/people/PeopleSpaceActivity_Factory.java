package com.android.systemui.people;

import com.android.systemui.people.widget.PeopleSpaceWidgetManager;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class PeopleSpaceActivity_Factory implements Factory<PeopleSpaceActivity> {
    private final Provider<PeopleSpaceWidgetManager> peopleSpaceWidgetManagerProvider;

    public PeopleSpaceActivity_Factory(Provider<PeopleSpaceWidgetManager> provider) {
        this.peopleSpaceWidgetManagerProvider = provider;
    }

    @Override // javax.inject.Provider
    public PeopleSpaceActivity get() {
        return newInstance(this.peopleSpaceWidgetManagerProvider.get());
    }

    public static PeopleSpaceActivity_Factory create(Provider<PeopleSpaceWidgetManager> provider) {
        return new PeopleSpaceActivity_Factory(provider);
    }

    public static PeopleSpaceActivity newInstance(PeopleSpaceWidgetManager peopleSpaceWidgetManager) {
        return new PeopleSpaceActivity(peopleSpaceWidgetManager);
    }
}
