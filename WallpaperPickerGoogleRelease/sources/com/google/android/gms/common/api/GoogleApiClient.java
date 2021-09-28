package com.google.android.gms.common.api;

import android.os.Bundle;
import android.os.Looper;
import com.google.android.gms.common.ConnectionResult;
import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;
/* loaded from: classes.dex */
public abstract class GoogleApiClient {
    public static final Set<GoogleApiClient> zza = Collections.newSetFromMap(new WeakHashMap());

    /* loaded from: classes.dex */
    public interface ConnectionCallbacks {
        void onConnected(Bundle bundle);

        void onConnectionSuspended(int i);
    }

    /* loaded from: classes.dex */
    public interface OnConnectionFailedListener {
        void onConnectionFailed(ConnectionResult connectionResult);
    }

    public Looper zzc() {
        throw new UnsupportedOperationException();
    }
}
