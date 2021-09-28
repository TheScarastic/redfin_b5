package com.android.systemui.qs;

import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class QSDetailDisplayer_Factory implements Factory<QSDetailDisplayer> {

    /* access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class InstanceHolder {
        private static final QSDetailDisplayer_Factory INSTANCE = new QSDetailDisplayer_Factory();
    }

    @Override // javax.inject.Provider
    public QSDetailDisplayer get() {
        return newInstance();
    }

    public static QSDetailDisplayer_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static QSDetailDisplayer newInstance() {
        return new QSDetailDisplayer();
    }
}
