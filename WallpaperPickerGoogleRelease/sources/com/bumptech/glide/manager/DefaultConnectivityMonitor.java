package com.bumptech.glide.manager;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.manager.ConnectivityMonitor;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.util.Util;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
/* loaded from: classes.dex */
public final class DefaultConnectivityMonitor implements ConnectivityMonitor {
    public final BroadcastReceiver connectivityReceiver = new BroadcastReceiver() { // from class: com.bumptech.glide.manager.DefaultConnectivityMonitor.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            DefaultConnectivityMonitor defaultConnectivityMonitor = DefaultConnectivityMonitor.this;
            boolean z = defaultConnectivityMonitor.isConnected;
            defaultConnectivityMonitor.isConnected = defaultConnectivityMonitor.isConnected(context);
            if (z != DefaultConnectivityMonitor.this.isConnected) {
                if (Log.isLoggable("ConnectivityMonitor", 3)) {
                    boolean z2 = DefaultConnectivityMonitor.this.isConnected;
                    StringBuilder sb = new StringBuilder(40);
                    sb.append("connectivity changed, isConnected: ");
                    sb.append(z2);
                    Log.d("ConnectivityMonitor", sb.toString());
                }
                DefaultConnectivityMonitor defaultConnectivityMonitor2 = DefaultConnectivityMonitor.this;
                ConnectivityMonitor.ConnectivityListener connectivityListener = defaultConnectivityMonitor2.listener;
                boolean z3 = defaultConnectivityMonitor2.isConnected;
                RequestManager.RequestManagerConnectivityListener requestManagerConnectivityListener = (RequestManager.RequestManagerConnectivityListener) connectivityListener;
                Objects.requireNonNull(requestManagerConnectivityListener);
                if (z3) {
                    RequestTracker requestTracker = requestManagerConnectivityListener.requestTracker;
                    Iterator it = ((ArrayList) Util.getSnapshot(requestTracker.requests)).iterator();
                    while (it.hasNext()) {
                        Request request = (Request) it.next();
                        if (!request.isComplete() && !request.isCleared()) {
                            request.clear();
                            if (!requestTracker.isPaused) {
                                request.begin();
                            } else {
                                requestTracker.pendingRequests.add(request);
                            }
                        }
                    }
                }
            }
        }
    };
    public final Context context;
    public boolean isConnected;
    public boolean isRegistered;
    public final ConnectivityMonitor.ConnectivityListener listener;

    public DefaultConnectivityMonitor(Context context, ConnectivityMonitor.ConnectivityListener connectivityListener) {
        this.context = context.getApplicationContext();
        this.listener = connectivityListener;
    }

    @SuppressLint({"MissingPermission"})
    public boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        Objects.requireNonNull(connectivityManager, "Argument must not be null");
        try {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetworkInfo == null || !activeNetworkInfo.isConnected()) {
                return false;
            }
            return true;
        } catch (RuntimeException e) {
            if (Log.isLoggable("ConnectivityMonitor", 5)) {
                Log.w("ConnectivityMonitor", "Failed to determine connectivity status when connectivity changed", e);
            }
            return true;
        }
    }

    @Override // com.bumptech.glide.manager.LifecycleListener
    public void onDestroy() {
    }

    @Override // com.bumptech.glide.manager.LifecycleListener
    public void onStart() {
        if (!this.isRegistered) {
            this.isConnected = isConnected(this.context);
            try {
                this.context.registerReceiver(this.connectivityReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
                this.isRegistered = true;
            } catch (SecurityException e) {
                if (Log.isLoggable("ConnectivityMonitor", 5)) {
                    Log.w("ConnectivityMonitor", "Failed to register", e);
                }
            }
        }
    }

    @Override // com.bumptech.glide.manager.LifecycleListener
    public void onStop() {
        if (this.isRegistered) {
            this.context.unregisterReceiver(this.connectivityReceiver);
            this.isRegistered = false;
        }
    }
}
