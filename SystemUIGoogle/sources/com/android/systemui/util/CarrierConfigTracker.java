package com.android.systemui.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.PersistableBundle;
import android.telephony.CarrierConfigManager;
import android.telephony.SubscriptionManager;
import android.util.SparseArray;
/* loaded from: classes2.dex */
public class CarrierConfigTracker extends BroadcastReceiver {
    private final CarrierConfigManager mCarrierConfigManager;
    private boolean mDefaultCallStrengthConfig;
    private boolean mDefaultCallStrengthConfigLoaded;
    private boolean mDefaultNoCallingConfig;
    private boolean mDefaultNoCallingConfigLoaded;
    private final SparseArray<Boolean> mCallStrengthConfigs = new SparseArray<>();
    private final SparseArray<Boolean> mNoCallingConfigs = new SparseArray<>();

    public CarrierConfigTracker(Context context) {
        this.mCarrierConfigManager = (CarrierConfigManager) context.getSystemService(CarrierConfigManager.class);
        context.registerReceiver(this, new IntentFilter("android.telephony.action.CARRIER_CONFIG_CHANGED"));
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        PersistableBundle configForSubId;
        if (intent.getAction() == "android.telephony.action.CARRIER_CONFIG_CHANGED") {
            int intExtra = intent.getIntExtra("android.telephony.extra.SUBSCRIPTION_INDEX", -1);
            if (SubscriptionManager.isValidSubscriptionId(intExtra) && (configForSubId = this.mCarrierConfigManager.getConfigForSubId(intExtra)) != null) {
                boolean z = configForSubId.getBoolean("use_ip_for_calling_indicator_bool");
                this.mCallStrengthConfigs.put(intExtra, Boolean.valueOf(configForSubId.getBoolean("display_call_strength_indicator_bool")));
                this.mNoCallingConfigs.put(intExtra, Boolean.valueOf(z));
            }
        }
    }

    public boolean getCallStrengthConfig(int i) {
        if (this.mCallStrengthConfigs.indexOfKey(i) >= 0) {
            return this.mCallStrengthConfigs.get(i).booleanValue();
        }
        if (!this.mDefaultCallStrengthConfigLoaded) {
            this.mDefaultCallStrengthConfig = CarrierConfigManager.getDefaultConfig().getBoolean("display_call_strength_indicator_bool");
            this.mDefaultCallStrengthConfigLoaded = true;
        }
        return this.mDefaultCallStrengthConfig;
    }

    public boolean getNoCallingConfig(int i) {
        if (this.mNoCallingConfigs.indexOfKey(i) >= 0) {
            return this.mNoCallingConfigs.get(i).booleanValue();
        }
        if (!this.mDefaultNoCallingConfigLoaded) {
            this.mDefaultNoCallingConfig = CarrierConfigManager.getDefaultConfig().getBoolean("use_ip_for_calling_indicator_bool");
            this.mDefaultNoCallingConfigLoaded = true;
        }
        return this.mDefaultNoCallingConfig;
    }
}
