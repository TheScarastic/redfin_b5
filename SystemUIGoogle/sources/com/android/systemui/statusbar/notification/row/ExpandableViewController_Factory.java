package com.android.systemui.statusbar.notification.row;

import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class ExpandableViewController_Factory implements Factory<ExpandableViewController> {
    private final Provider<ExpandableView> viewProvider;

    public ExpandableViewController_Factory(Provider<ExpandableView> provider) {
        this.viewProvider = provider;
    }

    @Override // javax.inject.Provider
    public ExpandableViewController get() {
        return newInstance(this.viewProvider.get());
    }

    public static ExpandableViewController_Factory create(Provider<ExpandableView> provider) {
        return new ExpandableViewController_Factory(provider);
    }

    public static ExpandableViewController newInstance(ExpandableView expandableView) {
        return new ExpandableViewController(expandableView);
    }
}
