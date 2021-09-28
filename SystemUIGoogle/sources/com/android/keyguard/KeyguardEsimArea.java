package com.android.keyguard;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.UserHandle;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.euicc.EuiccManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.android.systemui.R$string;
/* loaded from: classes.dex */
class KeyguardEsimArea extends Button implements View.OnClickListener {
    private EuiccManager mEuiccManager;
    private BroadcastReceiver mReceiver;

    public KeyguardEsimArea(Context context) {
        this(context, null);
    }

    public KeyguardEsimArea(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public KeyguardEsimArea(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 16974425);
    }

    public KeyguardEsimArea(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.mReceiver = new BroadcastReceiver() { // from class: com.android.keyguard.KeyguardEsimArea.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                int resultCode;
                if ("com.android.keyguard.disable_esim".equals(intent.getAction()) && (resultCode = getResultCode()) != 0) {
                    Log.e("KeyguardEsimArea", "Error disabling esim, result code = " + resultCode);
                    AlertDialog create = new AlertDialog.Builder(((Button) KeyguardEsimArea.this).mContext).setMessage(R$string.error_disable_esim_msg).setTitle(R$string.error_disable_esim_title).setCancelable(false).setPositiveButton(R$string.ok, (DialogInterface.OnClickListener) null).create();
                    create.getWindow().setType(2009);
                    create.show();
                }
            }
        };
        this.mEuiccManager = (EuiccManager) context.getSystemService("euicc");
        setOnClickListener(this);
    }

    @Override // android.widget.TextView, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        ((Button) this).mContext.registerReceiver(this.mReceiver, new IntentFilter("com.android.keyguard.disable_esim"), "com.android.systemui.permission.SELF", null);
    }

    public static boolean isEsimLocked(Context context, int i) {
        SubscriptionInfo activeSubscriptionInfo;
        if (((EuiccManager) context.getSystemService("euicc")).isEnabled() && (activeSubscriptionInfo = SubscriptionManager.from(context).getActiveSubscriptionInfo(i)) != null && activeSubscriptionInfo.isEmbedded()) {
            return true;
        }
        return false;
    }

    @Override // android.view.View
    protected void onDetachedFromWindow() {
        ((Button) this).mContext.unregisterReceiver(this.mReceiver);
        super.onDetachedFromWindow();
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        Intent intent = new Intent("com.android.keyguard.disable_esim");
        intent.setPackage(((Button) this).mContext.getPackageName());
        this.mEuiccManager.switchToSubscription(-1, PendingIntent.getBroadcastAsUser(((Button) this).mContext, 0, intent, 167772160, UserHandle.SYSTEM));
    }
}
