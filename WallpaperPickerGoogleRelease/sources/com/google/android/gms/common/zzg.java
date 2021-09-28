package com.google.android.gms.common;

import java.util.Arrays;
/* loaded from: classes.dex */
public final class zzg extends zzf {
    public final byte[] zza;

    public zzg(byte[] bArr) {
        super(Arrays.copyOfRange(bArr, 0, 25));
        this.zza = bArr;
    }

    @Override // com.google.android.gms.common.zzf
    public final byte[] zza() {
        return this.zza;
    }
}
