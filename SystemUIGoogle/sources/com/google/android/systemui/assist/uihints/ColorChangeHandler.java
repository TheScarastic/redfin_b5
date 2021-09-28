package com.google.android.systemui.assist.uihints;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.google.android.systemui.assist.uihints.NgaMessageHandler;
/* loaded from: classes2.dex */
public class ColorChangeHandler implements NgaMessageHandler.ConfigInfoListener {
    private final Context mContext;
    private boolean mIsDark;
    private PendingIntent mPendingIntent;

    /* access modifiers changed from: package-private */
    public ColorChangeHandler(Context context) {
        this.mContext = context;
    }

    /* access modifiers changed from: package-private */
    public void onColorChange(boolean z) {
        this.mIsDark = z;
        sendColor();
    }

    @Override // com.google.android.systemui.assist.uihints.NgaMessageHandler.ConfigInfoListener
    public void onConfigInfo(NgaMessageHandler.ConfigInfo configInfo) {
        this.mPendingIntent = configInfo.onColorChanged;
        sendColor();
    }

    private void sendColor() {
        if (this.mPendingIntent != null) {
            Intent intent = new Intent();
            intent.putExtra("is_dark", this.mIsDark);
            try {
                this.mPendingIntent.send(this.mContext, 0, intent);
            } catch (PendingIntent.CanceledException unused) {
                Log.w("ColorChangeHandler", "SysUI assist UI color changed PendingIntent canceled");
            }
        }
    }
}
