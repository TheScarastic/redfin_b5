package com.android.systemui.tuner;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.android.systemui.Dependency;
/* loaded from: classes2.dex */
public abstract class TunerService {
    private final Context mContext;

    /* loaded from: classes2.dex */
    public interface Tunable {
        void onTuningChanged(String str, String str2);
    }

    public abstract void addTunable(Tunable tunable, String... strArr);

    public abstract void clearAll();

    public abstract int getValue(String str, int i);

    public abstract String getValue(String str);

    public abstract boolean isTunerEnabled();

    public abstract void removeTunable(Tunable tunable);

    public abstract void setTunerEnabled(boolean z);

    public abstract void setValue(String str, int i);

    public abstract void setValue(String str, String str2);

    public abstract void showResetRequest(Runnable runnable);

    public TunerService(Context context) {
        this.mContext = context;
    }

    /* loaded from: classes2.dex */
    public static class ClearReceiver extends BroadcastReceiver {
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if ("com.android.systemui.action.CLEAR_TUNER".equals(intent.getAction())) {
                ((TunerService) Dependency.get(TunerService.class)).clearAll();
            }
        }
    }

    public static boolean parseIntegerSwitch(String str, boolean z) {
        if (str == null) {
            return z;
        }
        try {
            return Integer.parseInt(str) != 0;
        } catch (NumberFormatException unused) {
            return z;
        }
    }
}
