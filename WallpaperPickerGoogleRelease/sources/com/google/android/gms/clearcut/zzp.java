package com.google.android.gms.clearcut;

import java.util.Comparator;
/* loaded from: classes.dex */
public final class zzp implements Comparator<byte[]> {
    public zzp(int i) {
    }

    /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object, java.lang.Object] */
    @Override // java.util.Comparator
    public final /* synthetic */ int compare(byte[] bArr, byte[] bArr2) {
        byte[] bArr3 = bArr;
        byte[] bArr4 = bArr2;
        if (bArr3 == null && bArr4 == null) {
            return 0;
        }
        if (bArr3 == null) {
            return -1;
        }
        if (bArr4 == null) {
            return 1;
        }
        int min = Math.min(bArr3.length, bArr4.length);
        for (int i = 0; i < min; i++) {
            if (bArr3[i] != bArr4[i]) {
                return bArr3[i] - bArr4[i];
            }
        }
        return bArr3.length - bArr4.length;
    }

    @Override // java.util.Comparator, java.lang.Object
    public boolean equals(Object obj) {
        throw new UnsupportedOperationException("what are you doing?");
    }
}
