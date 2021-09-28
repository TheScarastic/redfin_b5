package com.android.systemui.qs.dagger;

import com.android.systemui.qs.QSFooter;
import com.android.systemui.qs.QSFooterViewController;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class QSFragmentModule_ProvidesQSFooterFactory implements Factory<QSFooter> {
    private final Provider<QSFooterViewController> qsFooterViewControllerProvider;

    public QSFragmentModule_ProvidesQSFooterFactory(Provider<QSFooterViewController> provider) {
        this.qsFooterViewControllerProvider = provider;
    }

    @Override // javax.inject.Provider
    public QSFooter get() {
        return providesQSFooter(this.qsFooterViewControllerProvider.get());
    }

    public static QSFragmentModule_ProvidesQSFooterFactory create(Provider<QSFooterViewController> provider) {
        return new QSFragmentModule_ProvidesQSFooterFactory(provider);
    }

    public static QSFooter providesQSFooter(QSFooterViewController qSFooterViewController) {
        return (QSFooter) Preconditions.checkNotNullFromProvides(QSFragmentModule.providesQSFooter(qSFooterViewController));
    }
}
