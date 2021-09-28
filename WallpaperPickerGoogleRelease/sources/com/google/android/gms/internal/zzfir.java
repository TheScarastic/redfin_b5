package com.google.android.gms.internal;

import android.database.ContentObserver;
/* loaded from: classes.dex */
public final class zzfir extends ContentObserver {
    public final /* synthetic */ zzfiq zza;

    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public zzfir(zzfiq zzfiq) {
        super(null);
        this.zza = zzfiq;
    }

    @Override // android.database.ContentObserver
    public final void onChange(boolean z) {
        zzfiq zzfiq = this.zza;
        synchronized (zzfiq.zze) {
            zzfiq.zzf = null;
        }
    }
}
