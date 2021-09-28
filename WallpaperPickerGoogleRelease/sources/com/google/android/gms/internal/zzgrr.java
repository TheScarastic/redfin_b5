package com.google.android.gms.internal;

import androidx.appcompat.R$dimen$$ExternalSyntheticOutline0;
import java.io.IOException;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ReadOnlyBufferException;
/* loaded from: classes.dex */
public final class zzgrr {
    public final ByteBuffer zza;

    public zzgrr(byte[] bArr, int i, int i2) {
        ByteBuffer wrap = ByteBuffer.wrap(bArr, i, i2);
        this.zza = wrap;
        wrap.order(ByteOrder.LITTLE_ENDIAN);
    }

    public static int zza(CharSequence charSequence) {
        int length = charSequence.length();
        int i = 0;
        int i2 = 0;
        while (i2 < length && charSequence.charAt(i2) < 128) {
            i2++;
        }
        int i3 = length;
        while (true) {
            if (i2 >= length) {
                break;
            }
            char charAt = charSequence.charAt(i2);
            if (charAt < 2048) {
                i3 += (127 - charAt) >>> 31;
                i2++;
            } else {
                int length2 = charSequence.length();
                while (i2 < length2) {
                    char charAt2 = charSequence.charAt(i2);
                    if (charAt2 < 2048) {
                        i += (127 - charAt2) >>> 31;
                    } else {
                        i += 2;
                        if (55296 <= charAt2 && charAt2 <= 57343) {
                            if (Character.codePointAt(charSequence, i2) >= 65536) {
                                i2++;
                            } else {
                                throw new IllegalArgumentException(R$dimen$$ExternalSyntheticOutline0.m(39, "Unpaired surrogate at index ", i2));
                            }
                        }
                    }
                    i2++;
                }
                i3 += i;
            }
        }
        if (i3 >= length) {
            return i3;
        }
        StringBuilder sb = new StringBuilder(54);
        sb.append("UTF-8 length does not fit in int: ");
        sb.append(((long) i3) + 4294967296L);
        throw new IllegalArgumentException(sb.toString());
    }

    public static int zzb(int i, String str) {
        int zzb = zzb(i);
        int zza = zza(str);
        return zzb + zzd(zza) + zza;
    }

    public static int zzb(long j) {
        if ((-128 & j) == 0) {
            return 1;
        }
        if ((-16384 & j) == 0) {
            return 2;
        }
        if ((-2097152 & j) == 0) {
            return 3;
        }
        if ((-268435456 & j) == 0) {
            return 4;
        }
        if ((-34359738368L & j) == 0) {
            return 5;
        }
        if ((-4398046511104L & j) == 0) {
            return 6;
        }
        if ((-562949953421312L & j) == 0) {
            return 7;
        }
        if ((-72057594037927936L & j) == 0) {
            return 8;
        }
        return (j & Long.MIN_VALUE) == 0 ? 9 : 10;
    }

    public static int zzd(int i) {
        if ((i & -128) == 0) {
            return 1;
        }
        if ((i & -16384) == 0) {
            return 2;
        }
        if ((-2097152 & i) == 0) {
            return 3;
        }
        return (i & -268435456) == 0 ? 4 : 5;
    }

    public static int zzf(int i, long j) {
        return zzb(j) + zzb(i);
    }

    public final void zzc(int i) throws IOException {
        while ((i & -128) != 0) {
            zzf((i & 127) | 128);
            i >>>= 7;
        }
        zzf(i);
    }

    public final void zzd(byte[] bArr) throws IOException {
        int length = bArr.length;
        if (this.zza.remaining() >= length) {
            this.zza.put(bArr, 0, length);
            return;
        }
        throw new zzgrs(this.zza.position(), this.zza.limit());
    }

    public final void zzf(int i) throws IOException {
        byte b = (byte) i;
        if (this.zza.hasRemaining()) {
            this.zza.put(b);
            return;
        }
        throw new zzgrs(this.zza.position(), this.zza.limit());
    }

    public static int zzb(int i, zzgrz zzgrz) {
        int zzb = zzb(i);
        int serializedSize = zzgrz.getSerializedSize();
        return zzd(serializedSize) + serializedSize + zzb;
    }

    public static int zzb(int i, byte[] bArr) {
        return zzb(i) + zzd(bArr.length) + bArr.length;
    }

    public static void zza(CharSequence charSequence, ByteBuffer byteBuffer) {
        int i;
        int i2;
        char charAt;
        if (!byteBuffer.isReadOnly()) {
            int i3 = 0;
            if (byteBuffer.hasArray()) {
                try {
                    byte[] array = byteBuffer.array();
                    int arrayOffset = byteBuffer.arrayOffset() + byteBuffer.position();
                    int remaining = byteBuffer.remaining();
                    int length = charSequence.length();
                    int i4 = remaining + arrayOffset;
                    while (i3 < length) {
                        int i5 = i3 + arrayOffset;
                        if (i5 >= i4 || (charAt = charSequence.charAt(i3)) >= 128) {
                            break;
                        }
                        array[i5] = (byte) charAt;
                        i3++;
                    }
                    if (i3 == length) {
                        i = arrayOffset + length;
                    } else {
                        i = arrayOffset + i3;
                        while (i3 < length) {
                            char charAt2 = charSequence.charAt(i3);
                            if (charAt2 >= 128 || i >= i4) {
                                if (charAt2 < 2048 && i <= i4 - 2) {
                                    int i6 = i + 1;
                                    array[i] = (byte) ((charAt2 >>> 6) | 960);
                                    i = i6 + 1;
                                    array[i6] = (byte) ((charAt2 & '?') | 128);
                                } else if ((charAt2 < 55296 || 57343 < charAt2) && i <= i4 - 3) {
                                    int i7 = i + 1;
                                    array[i] = (byte) ((charAt2 >>> '\f') | 480);
                                    int i8 = i7 + 1;
                                    array[i7] = (byte) (((charAt2 >>> 6) & 63) | 128);
                                    i2 = i8 + 1;
                                    array[i8] = (byte) ((charAt2 & '?') | 128);
                                } else if (i <= i4 - 4) {
                                    int i9 = i3 + 1;
                                    if (i9 != charSequence.length()) {
                                        char charAt3 = charSequence.charAt(i9);
                                        if (Character.isSurrogatePair(charAt2, charAt3)) {
                                            int codePoint = Character.toCodePoint(charAt2, charAt3);
                                            int i10 = i + 1;
                                            array[i] = (byte) ((codePoint >>> 18) | 240);
                                            int i11 = i10 + 1;
                                            array[i10] = (byte) (((codePoint >>> 12) & 63) | 128);
                                            int i12 = i11 + 1;
                                            array[i11] = (byte) (((codePoint >>> 6) & 63) | 128);
                                            i = i12 + 1;
                                            array[i12] = (byte) ((codePoint & 63) | 128);
                                            i3 = i9;
                                        } else {
                                            i3 = i9;
                                        }
                                    }
                                    StringBuilder sb = new StringBuilder(39);
                                    sb.append("Unpaired surrogate at index ");
                                    sb.append(i3 - 1);
                                    throw new IllegalArgumentException(sb.toString());
                                } else {
                                    StringBuilder sb2 = new StringBuilder(37);
                                    sb2.append("Failed writing ");
                                    sb2.append(charAt2);
                                    sb2.append(" at index ");
                                    sb2.append(i);
                                    throw new ArrayIndexOutOfBoundsException(sb2.toString());
                                }
                                i3++;
                            } else {
                                i2 = i + 1;
                                array[i] = (byte) charAt2;
                            }
                            i = i2;
                            i3++;
                        }
                    }
                    byteBuffer.position(i - byteBuffer.arrayOffset());
                } catch (ArrayIndexOutOfBoundsException e) {
                    BufferOverflowException bufferOverflowException = new BufferOverflowException();
                    bufferOverflowException.initCause(e);
                    throw bufferOverflowException;
                }
            } else {
                int length2 = charSequence.length();
                while (i3 < length2) {
                    char charAt4 = charSequence.charAt(i3);
                    if (charAt4 < 128) {
                        byteBuffer.put((byte) charAt4);
                    } else if (charAt4 < 2048) {
                        byteBuffer.put((byte) ((charAt4 >>> 6) | 960));
                        byteBuffer.put((byte) ((charAt4 & '?') | 128));
                    } else if (charAt4 < 55296 || 57343 < charAt4) {
                        byteBuffer.put((byte) ((charAt4 >>> '\f') | 480));
                        byteBuffer.put((byte) (((charAt4 >>> 6) & 63) | 128));
                        byteBuffer.put((byte) ((charAt4 & '?') | 128));
                    } else {
                        int i13 = i3 + 1;
                        if (i13 != charSequence.length()) {
                            char charAt5 = charSequence.charAt(i13);
                            if (Character.isSurrogatePair(charAt4, charAt5)) {
                                int codePoint2 = Character.toCodePoint(charAt4, charAt5);
                                byteBuffer.put((byte) ((codePoint2 >>> 18) | 240));
                                byteBuffer.put((byte) (((codePoint2 >>> 12) & 63) | 128));
                                byteBuffer.put((byte) (((codePoint2 >>> 6) & 63) | 128));
                                byteBuffer.put((byte) ((codePoint2 & 63) | 128));
                                i3 = i13;
                            } else {
                                i3 = i13;
                            }
                        }
                        throw new IllegalArgumentException(R$dimen$$ExternalSyntheticOutline0.m(39, "Unpaired surrogate at index ", i3 - 1));
                    }
                    i3++;
                }
            }
        } else {
            throw new ReadOnlyBufferException();
        }
    }

    public final void zzb(int i, long j) throws IOException {
        zzc((i << 3) | 0);
        zza(j);
    }

    public static int zzb(int i) {
        return zzd(i << 3);
    }

    public static int zza(int i) {
        if (i >= 0) {
            return zzd(i);
        }
        return 10;
    }

    public final void zza(int i, int i2) throws IOException {
        zzc((i << 3) | 0);
        if (i2 >= 0) {
            zzc(i2);
        } else {
            zza((long) i2);
        }
    }

    public final void zza(int i, zzgrz zzgrz) throws IOException {
        zzc((i << 3) | 2);
        if (zzgrz.zzaz < 0) {
            zzgrz.getSerializedSize();
        }
        zzc(zzgrz.zzaz);
        zzgrz.writeTo(this);
    }

    public final void zza(int i, String str) throws IOException {
        zzc((i << 3) | 2);
        try {
            int zzd = zzd(str.length());
            if (zzd == zzd(str.length() * 3)) {
                int position = this.zza.position();
                if (this.zza.remaining() >= zzd) {
                    this.zza.position(position + zzd);
                    zza(str, this.zza);
                    int position2 = this.zza.position();
                    this.zza.position(position);
                    zzc((position2 - position) - zzd);
                    this.zza.position(position2);
                    return;
                }
                throw new zzgrs(position + zzd, this.zza.limit());
            }
            zzc(zza(str));
            zza(str, this.zza);
        } catch (BufferOverflowException e) {
            zzgrs zzgrs = new zzgrs(this.zza.position(), this.zza.limit());
            zzgrs.initCause(e);
            throw zzgrs;
        }
    }

    public final void zza(int i, byte[] bArr) throws IOException {
        zzc((i << 3) | 2);
        zzc(bArr.length);
        zzd(bArr);
    }

    public final void zza(long j) throws IOException {
        while ((-128 & j) != 0) {
            zzf((((int) j) & 127) | 128);
            j >>>= 7;
        }
        zzf((int) j);
    }
}
