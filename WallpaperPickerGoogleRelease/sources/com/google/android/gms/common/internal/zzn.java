package com.google.android.gms.common.internal;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.internal.BaseGmsClient;
/* loaded from: classes.dex */
public final class zzn implements BaseGmsClient.BaseOnConnectionFailedListener {
    public final /* synthetic */ GoogleApiClient.OnConnectionFailedListener zza;

    public zzn(GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener) {
        this.zza = onConnectionFailedListener;
    }
}
