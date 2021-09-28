package com.google.android.gms.internal;

import com.google.android.gms.internal.zzgrt;
import java.io.IOException;
/* loaded from: classes.dex */
public abstract class zzgrt<M extends zzgrt<M>> extends zzgrz {
    public zzgrv zzay;

    @Override // com.google.android.gms.internal.zzgrz
    public M clone() throws CloneNotSupportedException {
        M m = (M) ((zzgrt) super.clone());
        Object obj = zzgrx.zzb;
        zzgrv zzgrv = this.zzay;
        if (zzgrv != null) {
            m.zzay = (zzgrv) zzgrv.clone();
        }
        return m;
    }

    @Override // com.google.android.gms.internal.zzgrz
    public int computeSerializedSize() {
        if (this.zzay != null) {
            int i = 0;
            while (true) {
                zzgrv zzgrv = this.zzay;
                if (i >= zzgrv.zze) {
                    break;
                }
                zzgrv.zzd[i].zza();
                i++;
            }
        }
        return 0;
    }

    @Override // com.google.android.gms.internal.zzgrz
    public void writeTo(zzgrr zzgrr) throws IOException {
        if (this.zzay != null) {
            int i = 0;
            while (true) {
                zzgrv zzgrv = this.zzay;
                if (i < zzgrv.zze) {
                    zzgrv.zzd[i].zza(zzgrr);
                    i++;
                } else {
                    return;
                }
            }
        }
    }
}
