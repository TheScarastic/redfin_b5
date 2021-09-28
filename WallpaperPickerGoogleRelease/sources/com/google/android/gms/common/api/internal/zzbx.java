package com.google.android.gms.common.api.internal;

import android.os.Looper;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Api.ApiOptions;
import com.google.android.gms.common.api.GoogleApi;
/* loaded from: classes.dex */
public final class zzbx<O extends Api.ApiOptions> extends zzal {
    public final GoogleApi<O> zza;

    public zzbx(GoogleApi<O> googleApi) {
        super("Method is not supported by connectionless client. APIs supporting connectionless client must not call this method.");
        this.zza = googleApi;
    }

    @Override // com.google.android.gms.common.api.GoogleApiClient
    public final Looper zzc() {
        return this.zza.zzf;
    }
}
