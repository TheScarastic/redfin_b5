package com.google.android.gms.common.api.internal;

import android.os.DeadObjectException;
import androidx.preference.R$layout;
import androidx.viewpager2.widget.FakeDrag$$ExternalSyntheticOutline0;
import com.adobe.xmp.XMPPathFactory$$ExternalSyntheticOutline0;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.internal.zzn;
import java.util.Objects;
/* loaded from: classes.dex */
public final class zzd<A extends zzn<? extends Result, Api.zzb>> extends zzb {
    public final A zza;

    public zzd(int i, A a) {
        super(i);
        this.zza = a;
    }

    @Override // com.google.android.gms.common.api.internal.zzb
    public final void zza(zzbp<?> zzbp) throws DeadObjectException {
        try {
            this.zza.zzb(zzbp.zzc);
        } catch (RuntimeException e) {
            String simpleName = e.getClass().getSimpleName();
            String localizedMessage = e.getLocalizedMessage();
            this.zza.zzc(new Status(10, FakeDrag$$ExternalSyntheticOutline0.m(XMPPathFactory$$ExternalSyntheticOutline0.m(localizedMessage, simpleName.length() + 2), simpleName, ": ", localizedMessage)));
        }
    }

    @Override // com.google.android.gms.common.api.internal.zzb
    public final void zza(Status status) {
        this.zza.zzc(status);
    }

    @Override // com.google.android.gms.common.api.internal.zzb
    public final void zza(zzaf zzaf, boolean z) {
        A a = this.zza;
        zzaf.zza.put(a, Boolean.valueOf(z));
        zzag zzag = new zzag(zzaf, a);
        Objects.requireNonNull(a);
        R$layout.zzb(true, "Callback cannot be null.");
        synchronized (a.zza) {
            if (a.zze()) {
                zzag.zza(a.zzj);
            } else {
                a.zzf.add(zzag);
            }
        }
    }
}
