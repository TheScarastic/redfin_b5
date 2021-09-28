package com.android.systemui.qs;

import com.android.systemui.plugins.qs.QS;
import com.android.systemui.tuner.TunerService;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class QSAnimator_Factory implements Factory<QSAnimator> {
    private final Provider<Executor> executorProvider;
    private final Provider<QSExpansionPathInterpolator> qsExpansionPathInterpolatorProvider;
    private final Provider<QSPanelController> qsPanelControllerProvider;
    private final Provider<QS> qsProvider;
    private final Provider<QSTileHost> qsTileHostProvider;
    private final Provider<QuickQSPanel> quickPanelProvider;
    private final Provider<QuickQSPanelController> quickQSPanelControllerProvider;
    private final Provider<QuickStatusBarHeader> quickStatusBarHeaderProvider;
    private final Provider<QSSecurityFooter> securityFooterProvider;
    private final Provider<TunerService> tunerServiceProvider;

    public QSAnimator_Factory(Provider<QS> provider, Provider<QuickQSPanel> provider2, Provider<QuickStatusBarHeader> provider3, Provider<QSPanelController> provider4, Provider<QuickQSPanelController> provider5, Provider<QSTileHost> provider6, Provider<QSSecurityFooter> provider7, Provider<Executor> provider8, Provider<TunerService> provider9, Provider<QSExpansionPathInterpolator> provider10) {
        this.qsProvider = provider;
        this.quickPanelProvider = provider2;
        this.quickStatusBarHeaderProvider = provider3;
        this.qsPanelControllerProvider = provider4;
        this.quickQSPanelControllerProvider = provider5;
        this.qsTileHostProvider = provider6;
        this.securityFooterProvider = provider7;
        this.executorProvider = provider8;
        this.tunerServiceProvider = provider9;
        this.qsExpansionPathInterpolatorProvider = provider10;
    }

    @Override // javax.inject.Provider
    public QSAnimator get() {
        return newInstance(this.qsProvider.get(), this.quickPanelProvider.get(), this.quickStatusBarHeaderProvider.get(), this.qsPanelControllerProvider.get(), this.quickQSPanelControllerProvider.get(), this.qsTileHostProvider.get(), this.securityFooterProvider.get(), this.executorProvider.get(), this.tunerServiceProvider.get(), this.qsExpansionPathInterpolatorProvider.get());
    }

    public static QSAnimator_Factory create(Provider<QS> provider, Provider<QuickQSPanel> provider2, Provider<QuickStatusBarHeader> provider3, Provider<QSPanelController> provider4, Provider<QuickQSPanelController> provider5, Provider<QSTileHost> provider6, Provider<QSSecurityFooter> provider7, Provider<Executor> provider8, Provider<TunerService> provider9, Provider<QSExpansionPathInterpolator> provider10) {
        return new QSAnimator_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10);
    }

    public static QSAnimator newInstance(QS qs, QuickQSPanel quickQSPanel, QuickStatusBarHeader quickStatusBarHeader, QSPanelController qSPanelController, QuickQSPanelController quickQSPanelController, QSTileHost qSTileHost, Object obj, Executor executor, TunerService tunerService, QSExpansionPathInterpolator qSExpansionPathInterpolator) {
        return new QSAnimator(qs, quickQSPanel, quickStatusBarHeader, qSPanelController, quickQSPanelController, qSTileHost, (QSSecurityFooter) obj, executor, tunerService, qSExpansionPathInterpolator);
    }
}
