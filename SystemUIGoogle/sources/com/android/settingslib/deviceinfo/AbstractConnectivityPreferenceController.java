package com.android.settingslib.deviceinfo;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnStart;
import com.android.settingslib.core.lifecycle.events.OnStop;
/* loaded from: classes.dex */
public abstract class AbstractConnectivityPreferenceController extends AbstractPreferenceController implements LifecycleObserver, OnStart, OnStop {
    private final BroadcastReceiver mConnectivityReceiver;

    protected abstract String[] getConnectivityIntents();

    @Override // com.android.settingslib.core.lifecycle.events.OnStop
    public void onStop() {
        this.mContext.unregisterReceiver(this.mConnectivityReceiver);
    }

    @Override // com.android.settingslib.core.lifecycle.events.OnStart
    public void onStart() {
        IntentFilter intentFilter = new IntentFilter();
        for (String str : getConnectivityIntents()) {
            intentFilter.addAction(str);
        }
        this.mContext.registerReceiver(this.mConnectivityReceiver, intentFilter, "android.permission.CHANGE_NETWORK_STATE", null);
    }
}
