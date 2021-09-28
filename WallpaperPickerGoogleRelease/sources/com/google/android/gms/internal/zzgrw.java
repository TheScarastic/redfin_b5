package com.google.android.gms.internal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
/* loaded from: classes.dex */
public final class zzgrw implements Cloneable {
    public Object zzb;
    public List<zzgsb> zzc = new ArrayList();

    @Override // java.lang.Object
    public final boolean equals(Object obj) {
        List<zzgsb> list;
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzgrw)) {
            return false;
        }
        zzgrw zzgrw = (zzgrw) obj;
        if (this.zzb == null || zzgrw.zzb == null) {
            List<zzgsb> list2 = this.zzc;
            if (list2 != null && (list = zzgrw.zzc) != null) {
                return list2.equals(list);
            }
            try {
                return Arrays.equals(zzb(), zzgrw.zzb());
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        } else {
            Objects.requireNonNull(null);
            throw null;
        }
    }

    @Override // java.lang.Object
    public final int hashCode() {
        try {
            return Arrays.hashCode(zzb()) + 527;
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public final int zza() {
        if (this.zzb == null) {
            Iterator<zzgsb> it = this.zzc.iterator();
            if (!it.hasNext()) {
                return 0;
            }
            Objects.requireNonNull(it.next());
            throw null;
        }
        Objects.requireNonNull(null);
        throw null;
    }

    public final byte[] zzb() throws IOException {
        zza();
        byte[] bArr = new byte[0];
        zza(new zzgrr(bArr, 0, 0));
        return bArr;
    }

    /* renamed from: zzc */
    public final zzgrw clone() {
        zzgrw zzgrw = new zzgrw();
        try {
            List<zzgsb> list = this.zzc;
            if (list == null) {
                zzgrw.zzc = null;
            } else {
                zzgrw.zzc.addAll(list);
            }
            Object obj = this.zzb;
            if (obj != null) {
                if (obj instanceof zzgrz) {
                    zzgrw.zzb = (zzgrz) ((zzgrz) obj).clone();
                } else if (obj instanceof byte[]) {
                    zzgrw.zzb = ((byte[]) obj).clone();
                } else {
                    int i = 0;
                    if (obj instanceof byte[][]) {
                        byte[][] bArr = (byte[][]) obj;
                        byte[][] bArr2 = new byte[bArr.length];
                        zzgrw.zzb = bArr2;
                        while (i < bArr.length) {
                            bArr2[i] = (byte[]) bArr[i].clone();
                            i++;
                        }
                    } else if (obj instanceof boolean[]) {
                        zzgrw.zzb = ((boolean[]) obj).clone();
                    } else if (obj instanceof int[]) {
                        zzgrw.zzb = ((int[]) obj).clone();
                    } else if (obj instanceof long[]) {
                        zzgrw.zzb = ((long[]) obj).clone();
                    } else if (obj instanceof float[]) {
                        zzgrw.zzb = ((float[]) obj).clone();
                    } else if (obj instanceof double[]) {
                        zzgrw.zzb = ((double[]) obj).clone();
                    } else if (obj instanceof zzgrz[]) {
                        zzgrz[] zzgrzArr = (zzgrz[]) obj;
                        zzgrz[] zzgrzArr2 = new zzgrz[zzgrzArr.length];
                        zzgrw.zzb = zzgrzArr2;
                        while (i < zzgrzArr.length) {
                            zzgrzArr2[i] = (zzgrz) zzgrzArr[i].clone();
                            i++;
                        }
                    }
                }
            }
            return zzgrw;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }

    public final void zza(zzgrr zzgrr) throws IOException {
        if (this.zzb == null) {
            Iterator<zzgsb> it = this.zzc.iterator();
            if (it.hasNext()) {
                Objects.requireNonNull(it.next());
                zzgrr.zzc(0);
                zzgrr.zzd((byte[]) null);
                throw null;
            }
            return;
        }
        Objects.requireNonNull(null);
        throw null;
    }
}
