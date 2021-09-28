package com.google.android.gms.common.api.internal;
/* loaded from: classes.dex */
public final class zzbq implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ zzbp zza;

    public zzbq(zzbp zzbp, int i) {
        this.$r8$classId = i;
        if (i != 1) {
            this.zza = zzbp;
        } else {
            this.zza = zzbp;
        }
    }

    @Override // java.lang.Runnable
    public final void run() {
        switch (this.$r8$classId) {
            case 0:
                this.zza.zzn();
                return;
            default:
                this.zza.zzo();
                return;
        }
    }
}
