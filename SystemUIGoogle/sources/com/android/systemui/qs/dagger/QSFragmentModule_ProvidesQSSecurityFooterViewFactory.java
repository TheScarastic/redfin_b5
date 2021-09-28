package com.android.systemui.qs.dagger;

import android.view.LayoutInflater;
import android.view.View;
import com.android.systemui.qs.QSPanel;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class QSFragmentModule_ProvidesQSSecurityFooterViewFactory implements Factory<View> {
    private final Provider<LayoutInflater> layoutInflaterProvider;
    private final Provider<QSPanel> qsPanelProvider;

    public QSFragmentModule_ProvidesQSSecurityFooterViewFactory(Provider<LayoutInflater> provider, Provider<QSPanel> provider2) {
        this.layoutInflaterProvider = provider;
        this.qsPanelProvider = provider2;
    }

    @Override // javax.inject.Provider
    public View get() {
        return providesQSSecurityFooterView(this.layoutInflaterProvider.get(), this.qsPanelProvider.get());
    }

    public static QSFragmentModule_ProvidesQSSecurityFooterViewFactory create(Provider<LayoutInflater> provider, Provider<QSPanel> provider2) {
        return new QSFragmentModule_ProvidesQSSecurityFooterViewFactory(provider, provider2);
    }

    public static View providesQSSecurityFooterView(LayoutInflater layoutInflater, QSPanel qSPanel) {
        return (View) Preconditions.checkNotNullFromProvides(QSFragmentModule.providesQSSecurityFooterView(layoutInflater, qSPanel));
    }
}
