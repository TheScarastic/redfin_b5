package androidx.appcompat;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;
/* loaded from: classes.dex */
public class R$dimen {
    public static long zza(long j, long j2, long j3) {
        long j4 = (j ^ j2) * j3;
        long j5 = ((j4 ^ (j4 >>> 47)) ^ j2) * j3;
        return (j5 ^ (j5 >>> 47)) * j3;
    }

    public static long zza(byte[] bArr) {
        int length = bArr.length;
        if (length < 0 || length > bArr.length) {
            throw new IndexOutOfBoundsException(R$dimen$$ExternalSyntheticOutline0.m(67, "Out of bound index with offput: 0 and length: ", length));
        }
        int i = 37;
        long j = -5435081209227447693L;
        char c = 0;
        if (length <= 32) {
            if (length > 16) {
                long j2 = ((long) (length << 1)) - 7286425919675154353L;
                long zzb = zzb(bArr, 0) * -5435081209227447693L;
                long zzb2 = zzb(bArr, 8);
                int i2 = length + 0;
                long zzb3 = zzb(bArr, i2 - 8) * j2;
                return zza((zzb(bArr, i2 - 16) * -7286425919675154353L) + Long.rotateRight(zzb3, 30) + Long.rotateRight(zzb + zzb2, 43), Long.rotateRight(zzb2 - 7286425919675154353L, 18) + zzb + zzb3, j2);
            } else if (length >= 8) {
                long j3 = ((long) (length << 1)) - 7286425919675154353L;
                long zzb4 = zzb(bArr, 0) - 7286425919675154353L;
                long zzb5 = zzb(bArr, (length + 0) - 8);
                return zza((Long.rotateRight(zzb5, 37) * j3) + zzb4, (Long.rotateRight(zzb4, 25) + zzb5) * j3, j3);
            } else if (length >= 4) {
                return zza(((long) length) + ((((long) zza(bArr, 0)) & 4294967295L) << 3), ((long) zza(bArr, (length + 0) - 4)) & 4294967295L, ((long) (length << 1)) - 7286425919675154353L);
            } else if (length <= 0) {
                return -7286425919675154353L;
            } else {
                long j4 = (((long) ((bArr[0] & 255) + ((bArr[(length >> 1) + 0] & 255) << 8))) * -7286425919675154353L) ^ (((long) (length + ((bArr[(length - 1) + 0] & 255) << 2))) * -4348849565147123417L);
                return (j4 ^ (j4 >>> 47)) * -7286425919675154353L;
            }
        } else if (length <= 64) {
            long j5 = ((long) (length << 1)) - 7286425919675154353L;
            long zzb6 = zzb(bArr, 0) * -7286425919675154353L;
            long zzb7 = zzb(bArr, 8);
            int i3 = length + 0;
            long zzb8 = zzb(bArr, i3 - 8) * j5;
            long rotateRight = Long.rotateRight(zzb8, 30) + Long.rotateRight(zzb6 + zzb7, 43) + (zzb(bArr, i3 - 16) * -7286425919675154353L);
            long zza = zza(rotateRight, Long.rotateRight(zzb7 - 7286425919675154353L, 18) + zzb6 + zzb8, j5);
            long zzb9 = zzb(bArr, 16) * j5;
            long zzb10 = zzb(bArr, 24);
            long zzb11 = (zzb(bArr, i3 - 32) + rotateRight) * j5;
            return zza(Long.rotateRight(zzb11, 30) + Long.rotateRight(zzb9 + zzb10, 43) + ((zzb(bArr, i3 - 24) + zza) * j5), Long.rotateRight(zzb10 + zzb6, 18) + zzb9 + zzb11, j5);
        } else {
            long j6 = 2480279821605975764L;
            long j7 = 1390051526045402406L;
            long[] jArr = new long[2];
            long[] jArr2 = new long[2];
            int i4 = length - 1;
            int i5 = ((i4 / 64) << 6) + 0;
            int i6 = i4 & 63;
            int i7 = (i5 + i6) - 63;
            long zzb12 = zzb(bArr, 0) + 95310865018149119L;
            int i8 = 0;
            while (true) {
                long j8 = zzb12 + j6 + jArr[c];
                long j9 = j6 + jArr[1];
                long rotateRight2 = (Long.rotateRight(zzb(bArr, i8 + 8) + j8, i) * j) ^ jArr2[1];
                long zzb13 = zzb(bArr, i8 + 40) + jArr[0] + (Long.rotateRight(zzb(bArr, i8 + 48) + j9, 42) * j);
                long rotateRight3 = Long.rotateRight(j7 + jArr2[0], 33) * j;
                zza(bArr, i8, jArr[1] * j, rotateRight2 + jArr2[0], jArr);
                zza(bArr, i8 + 32, jArr2[1] + rotateRight3, zzb(bArr, i8 + 16) + zzb13, jArr2);
                i8 += 64;
                if (i8 == i5) {
                    long j10 = ((rotateRight2 & 255) << 1) - 5435081209227447693L;
                    jArr2[0] = jArr2[0] + ((long) i6);
                    jArr[0] = jArr[0] + jArr2[0];
                    jArr2[0] = jArr2[0] + jArr[0];
                    long j11 = rotateRight3 + zzb13 + jArr[0];
                    long j12 = zzb13 + jArr[1];
                    long rotateRight4 = (Long.rotateRight(zzb(bArr, i7 + 8) + j11, 37) * j10) ^ (jArr2[1] * 9);
                    long zzb14 = zzb(bArr, i7 + 40) + (jArr[0] * 9) + (Long.rotateRight(zzb(bArr, i7 + 48) + j12, 42) * j10);
                    long rotateRight5 = Long.rotateRight(rotateRight2 + jArr2[0], 33) * j10;
                    zza(bArr, i7, jArr[1] * j10, rotateRight4 + jArr2[0], jArr);
                    zza(bArr, i7 + 32, jArr2[1] + rotateRight5, zzb(bArr, i7 + 16) + zzb14, jArr2);
                    return zza((((zzb14 >>> 47) ^ zzb14) * -4348849565147123417L) + zza(jArr[0], jArr2[0], j10) + rotateRight4, zza(jArr[1], jArr2[1], j10) + rotateRight5, j10);
                }
                j = -5435081209227447693L;
                i = 37;
                c = 0;
                i5 = i5;
                i6 = i6;
                j6 = zzb13;
                j7 = rotateRight2;
                zzb12 = rotateRight3;
            }
        }
    }

    public static long zzb(byte[] bArr, int i) {
        ByteBuffer wrap = ByteBuffer.wrap(bArr, i, 8);
        wrap.order(ByteOrder.LITTLE_ENDIAN);
        return wrap.getLong();
    }

    public static void zza(StringBuilder sb, HashMap<String, String> hashMap) {
        sb.append("{");
        boolean z = true;
        for (String str : hashMap.keySet()) {
            if (!z) {
                sb.append(",");
            } else {
                z = false;
            }
            String str2 = hashMap.get(str);
            sb.append("\"");
            sb.append(str);
            sb.append("\":");
            if (str2 == null) {
                sb.append("null");
            } else {
                sb.append("\"");
                sb.append(str2);
                sb.append("\"");
            }
        }
        sb.append("}");
    }

    public static void zza(byte[] bArr, int i, long j, long j2, long[] jArr) {
        long zzb = zzb(bArr, i);
        long zzb2 = zzb(bArr, i + 8);
        long zzb3 = zzb(bArr, i + 16);
        long zzb4 = zzb(bArr, i + 24);
        long j3 = j + zzb;
        long rotateRight = Long.rotateRight(j2 + j3 + zzb4, 21);
        long j4 = zzb2 + j3 + zzb3;
        jArr[0] = j4 + zzb4;
        jArr[1] = Long.rotateRight(j4, 44) + rotateRight + j3;
    }

    public static int zza(byte[] bArr, int i) {
        return ((bArr[i + 3] & 255) << 24) | (bArr[i] & 255) | ((bArr[i + 1] & 255) << 8) | ((bArr[i + 2] & 255) << 16);
    }
}
