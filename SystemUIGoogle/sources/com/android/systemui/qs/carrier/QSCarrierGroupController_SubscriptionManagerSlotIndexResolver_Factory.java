package com.android.systemui.qs.carrier;

import com.android.systemui.qs.carrier.QSCarrierGroupController;
import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class QSCarrierGroupController_SubscriptionManagerSlotIndexResolver_Factory implements Factory<QSCarrierGroupController.SubscriptionManagerSlotIndexResolver> {

    /* access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class InstanceHolder {
        private static final QSCarrierGroupController_SubscriptionManagerSlotIndexResolver_Factory INSTANCE = new QSCarrierGroupController_SubscriptionManagerSlotIndexResolver_Factory();
    }

    @Override // javax.inject.Provider
    public QSCarrierGroupController.SubscriptionManagerSlotIndexResolver get() {
        return newInstance();
    }

    public static QSCarrierGroupController_SubscriptionManagerSlotIndexResolver_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static QSCarrierGroupController.SubscriptionManagerSlotIndexResolver newInstance() {
        return new QSCarrierGroupController.SubscriptionManagerSlotIndexResolver();
    }
}
