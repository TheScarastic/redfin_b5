package com.android.wallpaper.module;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.android.wallpaper.module.NetworkStatusNotifier;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public class DefaultNetworkStatusNotifier implements NetworkStatusNotifier {
    public Context mAppContext;
    public ConnectivityManager mConnectivityManager;
    public List<NetworkStatusNotifier.Listener> mListeners = new ArrayList();
    public BroadcastReceiver mReceiver;

    public DefaultNetworkStatusNotifier(Context context) {
        Context applicationContext = context.getApplicationContext();
        this.mAppContext = applicationContext;
        this.mConnectivityManager = (ConnectivityManager) applicationContext.getSystemService("connectivity");
    }

    public int getNetworkStatus() {
        NetworkInfo activeNetworkInfo = this.mConnectivityManager.getActiveNetworkInfo();
        return (activeNetworkInfo == null || !activeNetworkInfo.isConnected()) ? 0 : 1;
    }

    public void registerListener(NetworkStatusNotifier.Listener listener) {
        if (this.mReceiver == null) {
            this.mReceiver = new BroadcastReceiver() { // from class: com.android.wallpaper.module.DefaultNetworkStatusNotifier.1
                @Override // android.content.BroadcastReceiver
                public void onReceive(Context context, Intent intent) {
                    int networkStatus = DefaultNetworkStatusNotifier.this.getNetworkStatus();
                    for (int i = 0; i < DefaultNetworkStatusNotifier.this.mListeners.size(); i++) {
                        DefaultNetworkStatusNotifier.this.mListeners.get(i).onNetworkChanged(networkStatus);
                    }
                }
            };
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
            if (!this.mListeners.contains(listener)) {
                this.mListeners.add(listener);
            }
            this.mAppContext.registerReceiver(this.mReceiver, intentFilter);
        } else if (!this.mListeners.contains(listener)) {
            this.mListeners.add(listener);
        }
    }

    public void unregisterListener(NetworkStatusNotifier.Listener listener) {
        BroadcastReceiver broadcastReceiver;
        this.mListeners.remove(listener);
        if (this.mListeners.isEmpty() && (broadcastReceiver = this.mReceiver) != null) {
            this.mAppContext.unregisterReceiver(broadcastReceiver);
            this.mReceiver = null;
        }
    }
}
