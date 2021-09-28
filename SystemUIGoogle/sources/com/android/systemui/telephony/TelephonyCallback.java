package com.android.systemui.telephony;

import android.telephony.ServiceState;
import android.telephony.TelephonyCallback;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public class TelephonyCallback extends android.telephony.TelephonyCallback implements TelephonyCallback.ActiveDataSubscriptionIdListener, TelephonyCallback.CallStateListener, TelephonyCallback.ServiceStateListener {
    private final List<TelephonyCallback.ActiveDataSubscriptionIdListener> mActiveDataSubscriptionIdListeners = new ArrayList();
    private final List<TelephonyCallback.CallStateListener> mCallStateListeners = new ArrayList();
    private final List<TelephonyCallback.ServiceStateListener> mServiceStateListeners = new ArrayList();

    public boolean hasAnyListeners() {
        return !this.mActiveDataSubscriptionIdListeners.isEmpty() || !this.mCallStateListeners.isEmpty() || !this.mServiceStateListeners.isEmpty();
    }

    public void onActiveDataSubscriptionIdChanged(int i) {
        ArrayList arrayList;
        synchronized (this.mActiveDataSubscriptionIdListeners) {
            arrayList = new ArrayList(this.mActiveDataSubscriptionIdListeners);
        }
        arrayList.forEach(new Consumer(i) { // from class: com.android.systemui.telephony.TelephonyCallback$$ExternalSyntheticLambda0
            public final /* synthetic */ int f$0;

            {
                this.f$0 = r1;
            }

            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                TelephonyCallback.$r8$lambda$LyKejouXrE_8m1XWEURMrN8MNEw(this.f$0, (TelephonyCallback.ActiveDataSubscriptionIdListener) obj);
            }
        });
    }

    public void addActiveDataSubscriptionIdListener(TelephonyCallback.ActiveDataSubscriptionIdListener activeDataSubscriptionIdListener) {
        this.mActiveDataSubscriptionIdListeners.add(activeDataSubscriptionIdListener);
    }

    public void removeActiveDataSubscriptionIdListener(TelephonyCallback.ActiveDataSubscriptionIdListener activeDataSubscriptionIdListener) {
        this.mActiveDataSubscriptionIdListeners.remove(activeDataSubscriptionIdListener);
    }

    public void onCallStateChanged(int i) {
        ArrayList arrayList;
        synchronized (this.mCallStateListeners) {
            arrayList = new ArrayList(this.mCallStateListeners);
        }
        arrayList.forEach(new Consumer(i) { // from class: com.android.systemui.telephony.TelephonyCallback$$ExternalSyntheticLambda1
            public final /* synthetic */ int f$0;

            {
                this.f$0 = r1;
            }

            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                TelephonyCallback.$r8$lambda$ustG0rlmL1bwIR5ioCfTJuO7y1E(this.f$0, (TelephonyCallback.CallStateListener) obj);
            }
        });
    }

    public void addCallStateListener(TelephonyCallback.CallStateListener callStateListener) {
        this.mCallStateListeners.add(callStateListener);
    }

    public void removeCallStateListener(TelephonyCallback.CallStateListener callStateListener) {
        this.mCallStateListeners.remove(callStateListener);
    }

    public void onServiceStateChanged(ServiceState serviceState) {
        ArrayList arrayList;
        synchronized (this.mServiceStateListeners) {
            arrayList = new ArrayList(this.mServiceStateListeners);
        }
        arrayList.forEach(new Consumer(serviceState) { // from class: com.android.systemui.telephony.TelephonyCallback$$ExternalSyntheticLambda2
            public final /* synthetic */ ServiceState f$0;

            {
                this.f$0 = r1;
            }

            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                TelephonyCallback.$r8$lambda$vk_lKHWaAzI6c2pkXye62_KdExU(this.f$0, (TelephonyCallback.ServiceStateListener) obj);
            }
        });
    }

    public void addServiceStateListener(TelephonyCallback.ServiceStateListener serviceStateListener) {
        this.mServiceStateListeners.add(serviceStateListener);
    }

    public void removeServiceStateListener(TelephonyCallback.ServiceStateListener serviceStateListener) {
        this.mServiceStateListeners.remove(serviceStateListener);
    }
}
