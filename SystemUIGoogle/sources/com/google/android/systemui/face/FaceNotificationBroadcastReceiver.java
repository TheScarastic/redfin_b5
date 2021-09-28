package com.google.android.systemui.face;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
/* loaded from: classes2.dex */
public class FaceNotificationBroadcastReceiver extends BroadcastReceiver {
    private final Context mContext;

    /* access modifiers changed from: package-private */
    public FaceNotificationBroadcastReceiver(Context context) {
        this.mContext = context;
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action == null) {
            Log.e("FaceNotificationBCR", "Received broadcast with null action.");
            this.mContext.unregisterReceiver(this);
            return;
        }
        if (action.equals("face_action_show_reenroll_dialog")) {
            FaceNotificationDialogFactory.createReenrollDialog(this.mContext).show();
        }
        this.mContext.unregisterReceiver(this);
    }
}
