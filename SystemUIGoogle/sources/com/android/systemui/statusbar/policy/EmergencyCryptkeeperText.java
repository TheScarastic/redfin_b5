package com.android.systemui.statusbar.policy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.provider.Settings;
import android.telephony.SubscriptionInfo;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.KeyguardUpdateMonitorCallback;
import com.android.systemui.Dependency;
import java.util.List;
/* loaded from: classes.dex */
public class EmergencyCryptkeeperText extends TextView {
    private KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    private final KeyguardUpdateMonitorCallback mCallback = new KeyguardUpdateMonitorCallback() { // from class: com.android.systemui.statusbar.policy.EmergencyCryptkeeperText.1
        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public void onPhoneStateChanged(int i) {
            EmergencyCryptkeeperText.this.update();
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public void onRefreshCarrierInfo() {
            EmergencyCryptkeeperText.this.update();
        }
    };
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() { // from class: com.android.systemui.statusbar.policy.EmergencyCryptkeeperText.2
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if ("android.intent.action.AIRPLANE_MODE".equals(intent.getAction())) {
                EmergencyCryptkeeperText.this.update();
            }
        }
    };

    private boolean iccCardExist(int i) {
        return i == 2 || i == 3 || i == 4 || i == 5 || i == 6 || i == 7 || i == 8 || i == 9 || i == 10;
    }

    public EmergencyCryptkeeperText(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setVisibility(8);
    }

    @Override // android.widget.TextView, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        KeyguardUpdateMonitor keyguardUpdateMonitor = (KeyguardUpdateMonitor) Dependency.get(KeyguardUpdateMonitor.class);
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        keyguardUpdateMonitor.registerCallback(this.mCallback);
        getContext().registerReceiver(this.mReceiver, new IntentFilter("android.intent.action.AIRPLANE_MODE"));
        update();
    }

    @Override // android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        KeyguardUpdateMonitor keyguardUpdateMonitor = this.mKeyguardUpdateMonitor;
        if (keyguardUpdateMonitor != null) {
            keyguardUpdateMonitor.removeCallback(this.mCallback);
        }
        getContext().unregisterReceiver(this.mReceiver);
    }

    public void update() {
        boolean isDataCapable = ((TelephonyManager) ((TextView) this).mContext.getSystemService(TelephonyManager.class)).isDataCapable();
        int i = 0;
        boolean z = true;
        boolean z2 = Settings.Global.getInt(((TextView) this).mContext.getContentResolver(), "airplane_mode_on", 0) == 1;
        if (!isDataCapable || z2) {
            setText((CharSequence) null);
            setVisibility(8);
            return;
        }
        List<SubscriptionInfo> filteredSubscriptionInfo = this.mKeyguardUpdateMonitor.getFilteredSubscriptionInfo(false);
        int size = filteredSubscriptionInfo.size();
        CharSequence charSequence = null;
        for (int i2 = 0; i2 < size; i2++) {
            int simState = this.mKeyguardUpdateMonitor.getSimState(filteredSubscriptionInfo.get(i2).getSubscriptionId());
            CharSequence carrierName = filteredSubscriptionInfo.get(i2).getCarrierName();
            if (iccCardExist(simState) && !TextUtils.isEmpty(carrierName)) {
                z = false;
                charSequence = carrierName;
            }
        }
        if (z) {
            if (size != 0) {
                charSequence = filteredSubscriptionInfo.get(0).getCarrierName();
            } else {
                charSequence = getContext().getText(17040137);
                Intent registerReceiver = getContext().registerReceiver(null, new IntentFilter("android.telephony.action.SERVICE_PROVIDERS_UPDATED"));
                if (registerReceiver != null) {
                    charSequence = registerReceiver.getStringExtra("android.telephony.extra.PLMN");
                }
            }
        }
        setText(charSequence);
        if (TextUtils.isEmpty(charSequence)) {
            i = 8;
        }
        setVisibility(i);
    }
}
