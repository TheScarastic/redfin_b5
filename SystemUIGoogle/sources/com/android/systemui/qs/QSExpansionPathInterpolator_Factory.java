package com.android.systemui.qs;

import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class QSExpansionPathInterpolator_Factory implements Factory<QSExpansionPathInterpolator> {

    /* access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class InstanceHolder {
        private static final QSExpansionPathInterpolator_Factory INSTANCE = new QSExpansionPathInterpolator_Factory();
    }

    @Override // javax.inject.Provider
    public QSExpansionPathInterpolator get() {
        return newInstance();
    }

    public static QSExpansionPathInterpolator_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static QSExpansionPathInterpolator newInstance() {
        return new QSExpansionPathInterpolator();
    }
}
