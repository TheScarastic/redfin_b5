package com.google.android.systemui.assist.uihints;

import android.app.PendingIntent;
import android.util.Log;
import com.google.android.systemui.assist.uihints.NgaMessageHandler;
/* loaded from: classes2.dex */
class TouchOutsideHandler implements NgaMessageHandler.ConfigInfoListener {
    private PendingIntent mTouchOutside;

    @Override // com.google.android.systemui.assist.uihints.NgaMessageHandler.ConfigInfoListener
    public void onConfigInfo(NgaMessageHandler.ConfigInfo configInfo) {
        this.mTouchOutside = configInfo.onTouchOutside;
    }

    /* access modifiers changed from: package-private */
    public void onTouchOutside() {
        PendingIntent pendingIntent = this.mTouchOutside;
        if (pendingIntent != null) {
            try {
                pendingIntent.send();
            } catch (PendingIntent.CanceledException unused) {
                Log.w("TouchOutsideHandler", "Touch outside PendingIntent canceled");
            }
        }
    }
}
