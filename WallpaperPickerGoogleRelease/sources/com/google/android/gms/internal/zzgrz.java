package com.google.android.gms.internal;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
/* loaded from: classes.dex */
public abstract class zzgrz {
    public volatile int zzaz = -1;

    public static final byte[] toByteArray(zzgrz zzgrz) {
        int serializedSize = zzgrz.getSerializedSize();
        byte[] bArr = new byte[serializedSize];
        try {
            zzgrr zzgrr = new zzgrr(bArr, 0, serializedSize);
            zzgrz.writeTo(zzgrr);
            if (zzgrr.zza.remaining() == 0) {
                return bArr;
            }
            throw new IllegalStateException(String.format("Did not write as much data as expected, %s bytes remaining.", Integer.valueOf(zzgrr.zza.remaining())));
        } catch (IOException e) {
            throw new RuntimeException("Serializing to a byte array threw an IOException (should never happen).", e);
        }
    }

    public zzgrz clone() throws CloneNotSupportedException {
        return (zzgrz) super.clone();
    }

    public int computeSerializedSize() {
        return 0;
    }

    public int getSerializedSize() {
        int computeSerializedSize = computeSerializedSize();
        this.zzaz = computeSerializedSize;
        return computeSerializedSize;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        try {
            zzgsa.zza(null, this, new StringBuffer(), stringBuffer);
            return stringBuffer.toString();
        } catch (IllegalAccessException e) {
            String valueOf = String.valueOf(e.getMessage());
            return valueOf.length() != 0 ? "Error printing proto: ".concat(valueOf) : new String("Error printing proto: ");
        } catch (InvocationTargetException e2) {
            String valueOf2 = String.valueOf(e2.getMessage());
            return valueOf2.length() != 0 ? "Error printing proto: ".concat(valueOf2) : new String("Error printing proto: ");
        }
    }

    public void writeTo(zzgrr zzgrr) throws IOException {
    }
}
