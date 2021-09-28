package com.android.systemui.telephony;

import android.telephony.TelephonyCallback;
import android.telephony.TelephonyManager;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public class TelephonyListenerManager {
    private final Executor mExecutor;
    private boolean mListening = false;
    private final TelephonyCallback mTelephonyCallback;
    private final TelephonyManager mTelephonyManager;

    public TelephonyListenerManager(TelephonyManager telephonyManager, Executor executor, TelephonyCallback telephonyCallback) {
        this.mTelephonyManager = telephonyManager;
        this.mExecutor = executor;
        this.mTelephonyCallback = telephonyCallback;
    }

    public void addActiveDataSubscriptionIdListener(TelephonyCallback.ActiveDataSubscriptionIdListener activeDataSubscriptionIdListener) {
        this.mTelephonyCallback.addActiveDataSubscriptionIdListener(activeDataSubscriptionIdListener);
        updateListening();
    }

    public void removeActiveDataSubscriptionIdListener(TelephonyCallback.ActiveDataSubscriptionIdListener activeDataSubscriptionIdListener) {
        this.mTelephonyCallback.removeActiveDataSubscriptionIdListener(activeDataSubscriptionIdListener);
        updateListening();
    }

    public void addCallStateListener(TelephonyCallback.CallStateListener callStateListener) {
        this.mTelephonyCallback.addCallStateListener(callStateListener);
        updateListening();
    }

    public void removeCallStateListener(TelephonyCallback.CallStateListener callStateListener) {
        this.mTelephonyCallback.removeCallStateListener(callStateListener);
        updateListening();
    }

    public void addServiceStateListener(TelephonyCallback.ServiceStateListener serviceStateListener) {
        this.mTelephonyCallback.addServiceStateListener(serviceStateListener);
        updateListening();
    }

    public void removeServiceStateListener(TelephonyCallback.ServiceStateListener serviceStateListener) {
        this.mTelephonyCallback.removeServiceStateListener(serviceStateListener);
        updateListening();
    }

    private void updateListening() {
        if (!this.mListening && this.mTelephonyCallback.hasAnyListeners()) {
            this.mListening = true;
            this.mTelephonyManager.registerTelephonyCallback(this.mExecutor, this.mTelephonyCallback);
        } else if (this.mListening && !this.mTelephonyCallback.hasAnyListeners()) {
            this.mTelephonyManager.unregisterTelephonyCallback(this.mTelephonyCallback);
            this.mListening = false;
        }
    }
}
