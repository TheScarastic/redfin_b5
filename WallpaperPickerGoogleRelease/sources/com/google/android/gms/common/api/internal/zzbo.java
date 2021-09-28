package com.google.android.gms.common.api.internal;

import android.os.Handler;
/* loaded from: classes.dex */
public final class zzbo implements zzm {
    public final /* synthetic */ zzbn zza;

    public zzbo(zzbn zzbn) {
        this.zza = zzbn;
    }

    @Override // com.google.android.gms.common.api.internal.zzm
    public final void zza(boolean z) {
        Handler handler = this.zza.zzq;
        handler.sendMessage(handler.obtainMessage(1, Boolean.valueOf(z)));
    }
}
