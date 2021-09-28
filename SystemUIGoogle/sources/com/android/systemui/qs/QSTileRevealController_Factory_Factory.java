package com.android.systemui.qs;

import android.content.Context;
import com.android.systemui.qs.QSTileRevealController;
import com.android.systemui.qs.customize.QSCustomizerController;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class QSTileRevealController_Factory_Factory implements Factory<QSTileRevealController.Factory> {
    private final Provider<Context> contextProvider;
    private final Provider<QSCustomizerController> qsCustomizerControllerProvider;

    public QSTileRevealController_Factory_Factory(Provider<Context> provider, Provider<QSCustomizerController> provider2) {
        this.contextProvider = provider;
        this.qsCustomizerControllerProvider = provider2;
    }

    @Override // javax.inject.Provider
    public QSTileRevealController.Factory get() {
        return newInstance(this.contextProvider.get(), this.qsCustomizerControllerProvider.get());
    }

    public static QSTileRevealController_Factory_Factory create(Provider<Context> provider, Provider<QSCustomizerController> provider2) {
        return new QSTileRevealController_Factory_Factory(provider, provider2);
    }

    public static QSTileRevealController.Factory newInstance(Context context, QSCustomizerController qSCustomizerController) {
        return new QSTileRevealController.Factory(context, qSCustomizerController);
    }
}
