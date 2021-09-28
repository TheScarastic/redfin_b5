package okio.internal;

import kotlin.jvm.internal.Intrinsics;
import okio.Buffer;
import okio.Options;
import okio.Platform;
import okio.Segment;
/* compiled from: Buffer.kt */
/* loaded from: classes2.dex */
public final class BufferKt {
    private static final byte[] HEX_DIGIT_BYTES = Platform.asUtf8ToByteArray("0123456789abcdef");

    public static final boolean rangeEquals(Segment segment, int i, byte[] bArr, int i2, int i3) {
        Intrinsics.checkNotNullParameter(segment, "segment");
        Intrinsics.checkNotNullParameter(bArr, "bytes");
        int i4 = segment.limit;
        byte[] bArr2 = segment.data;
        while (i2 < i3) {
            if (i == i4) {
                segment = segment.next;
                Intrinsics.checkNotNull(segment);
                byte[] bArr3 = segment.data;
                int i5 = segment.pos;
                i4 = segment.limit;
                bArr2 = bArr3;
                i = i5;
            }
            if (bArr2[i] != bArr[i2]) {
                return false;
            }
            i++;
            i2++;
        }
        return true;
    }

    public static /* synthetic */ int selectPrefix$default(Buffer buffer, Options options, boolean z, int i, Object obj) {
        if ((i & 2) != 0) {
            z = false;
        }
        return selectPrefix(buffer, options, z);
    }

    public static final int selectPrefix(Buffer buffer, Options options, boolean z) {
        int i;
        int i2;
        int i3;
        Segment segment;
        int i4;
        Intrinsics.checkNotNullParameter(buffer, "<this>");
        Intrinsics.checkNotNullParameter(options, "options");
        Segment segment2 = buffer.head;
        if (segment2 == null) {
            return z ? -2 : -1;
        }
        byte[] bArr = segment2.data;
        int i5 = segment2.pos;
        int i6 = segment2.limit;
        int[] trie$external__okio__android_common__okio_lib = options.getTrie$external__okio__android_common__okio_lib();
        Segment segment3 = segment2;
        int i7 = -1;
        int i8 = 0;
        loop0: while (true) {
            int i9 = i8 + 1;
            int i10 = trie$external__okio__android_common__okio_lib[i8];
            int i11 = i9 + 1;
            int i12 = trie$external__okio__android_common__okio_lib[i9];
            if (i12 != -1) {
                i7 = i12;
            }
            if (segment3 == null) {
                break;
            }
            if (i10 < 0) {
                int i13 = i11 + (i10 * -1);
                while (true) {
                    int i14 = i5 + 1;
                    int i15 = i11 + 1;
                    if ((bArr[i5] & 255) != trie$external__okio__android_common__okio_lib[i11]) {
                        return i7;
                    }
                    boolean z2 = i15 == i13;
                    if (i14 == i6) {
                        Intrinsics.checkNotNull(segment3);
                        Segment segment4 = segment3.next;
                        Intrinsics.checkNotNull(segment4);
                        i4 = segment4.pos;
                        byte[] bArr2 = segment4.data;
                        i3 = segment4.limit;
                        if (segment4 != segment2) {
                            segment = segment4;
                            bArr = bArr2;
                        } else if (!z2) {
                            break loop0;
                        } else {
                            bArr = bArr2;
                            segment = null;
                        }
                    } else {
                        i3 = i6;
                        i4 = i14;
                        segment = segment3;
                    }
                    if (z2) {
                        i2 = trie$external__okio__android_common__okio_lib[i15];
                        i = i4;
                        i6 = i3;
                        segment3 = segment;
                        break;
                    }
                    i5 = i4;
                    i6 = i3;
                    i11 = i15;
                    segment3 = segment;
                }
            } else {
                i = i5 + 1;
                int i16 = bArr[i5] & 255;
                int i17 = i11 + i10;
                while (i11 != i17) {
                    if (i16 == trie$external__okio__android_common__okio_lib[i11]) {
                        i2 = trie$external__okio__android_common__okio_lib[i11 + i10];
                        if (i == i6) {
                            segment3 = segment3.next;
                            Intrinsics.checkNotNull(segment3);
                            int i18 = segment3.pos;
                            byte[] bArr3 = segment3.data;
                            i6 = segment3.limit;
                            i = i18;
                            bArr = bArr3;
                            if (segment3 == segment2) {
                                segment3 = null;
                            }
                        }
                    } else {
                        i11++;
                    }
                }
                return i7;
            }
            if (i2 >= 0) {
                return i2;
            }
            i8 = -i2;
            i5 = i;
        }
        if (z) {
            return -2;
        }
        return i7;
    }
}
