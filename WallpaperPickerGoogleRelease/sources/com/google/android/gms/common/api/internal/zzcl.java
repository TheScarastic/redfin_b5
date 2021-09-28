package com.google.android.gms.common.api.internal;
/* loaded from: classes.dex */
public final class zzcl<L> {
    public final L zza;
    public final String zzb;

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof zzcl)) {
            return false;
        }
        zzcl zzcl = (zzcl) obj;
        return this.zza == zzcl.zza && this.zzb.equals(zzcl.zzb);
    }

    public final int hashCode() {
        return this.zzb.hashCode() + (System.identityHashCode(this.zza) * 31);
    }
}
