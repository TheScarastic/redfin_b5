package com.google.android.gms.common.internal;

import android.app.Activity;
import android.content.Intent;
import com.google.android.gms.common.api.internal.zzcf;
/* loaded from: classes.dex */
public final class zzh extends zzg {
    public final /* synthetic */ int $r8$classId = 0;
    public final /* synthetic */ Intent zza;
    public final /* synthetic */ Object zzb;
    public final /* synthetic */ int zzc;

    public zzh(Intent intent, Activity activity, int i) {
        this.zza = intent;
        this.zzb = activity;
        this.zzc = i;
    }

    @Override // com.google.android.gms.common.internal.zzg
    public final void zza() {
        switch (this.$r8$classId) {
            case 0:
                Intent intent = this.zza;
                if (intent != null) {
                    ((Activity) this.zzb).startActivityForResult(intent, this.zzc);
                    return;
                }
                return;
            default:
                Intent intent2 = this.zza;
                if (intent2 != null) {
                    ((zzcf) this.zzb).startActivityForResult(intent2, this.zzc);
                    return;
                }
                return;
        }
    }
}
