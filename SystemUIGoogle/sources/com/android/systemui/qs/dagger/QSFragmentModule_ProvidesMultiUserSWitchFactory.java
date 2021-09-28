package com.android.systemui.qs.dagger;

import com.android.systemui.qs.QSFooterView;
import com.android.systemui.statusbar.phone.MultiUserSwitch;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class QSFragmentModule_ProvidesMultiUserSWitchFactory implements Factory<MultiUserSwitch> {
    private final Provider<QSFooterView> qsFooterViewProvider;

    public QSFragmentModule_ProvidesMultiUserSWitchFactory(Provider<QSFooterView> provider) {
        this.qsFooterViewProvider = provider;
    }

    @Override // javax.inject.Provider
    public MultiUserSwitch get() {
        return providesMultiUserSWitch(this.qsFooterViewProvider.get());
    }

    public static QSFragmentModule_ProvidesMultiUserSWitchFactory create(Provider<QSFooterView> provider) {
        return new QSFragmentModule_ProvidesMultiUserSWitchFactory(provider);
    }

    public static MultiUserSwitch providesMultiUserSWitch(QSFooterView qSFooterView) {
        return (MultiUserSwitch) Preconditions.checkNotNullFromProvides(QSFragmentModule.providesMultiUserSWitch(qSFooterView));
    }
}
