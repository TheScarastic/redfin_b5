package com.google.protobuf;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
/* loaded from: classes.dex */
public final class UnknownFieldSetLite {
    public static final UnknownFieldSetLite DEFAULT_INSTANCE = new UnknownFieldSetLite(0, new int[0], new Object[0], false);
    public int count;
    public boolean isMutable;
    public int memoizedSerializedSize;
    public Object[] objects;
    public int[] tags;

    public UnknownFieldSetLite() {
        this(0, new int[8], new Object[8], true);
    }

    public static UnknownFieldSetLite newInstance() {
        return new UnknownFieldSetLite(0, new int[8], new Object[8], true);
    }

    public static void writeField(int i, Object obj, Writer writer) throws IOException {
        int i2 = i >>> 3;
        int i3 = i & 7;
        if (i3 == 0) {
            ((CodedOutputStreamWriter) writer).output.writeUInt64(i2, ((Long) obj).longValue());
        } else if (i3 == 1) {
            ((CodedOutputStreamWriter) writer).output.writeFixed64(i2, ((Long) obj).longValue());
        } else if (i3 == 2) {
            ((CodedOutputStreamWriter) writer).output.writeBytes(i2, (ByteString) obj);
        } else if (i3 == 3) {
            CodedOutputStreamWriter codedOutputStreamWriter = (CodedOutputStreamWriter) writer;
            codedOutputStreamWriter.output.writeTag(i2, 3);
            ((UnknownFieldSetLite) obj).writeTo(writer);
            codedOutputStreamWriter.output.writeTag(i2, 4);
        } else if (i3 == 5) {
            ((CodedOutputStreamWriter) writer).output.writeFixed32(i2, ((Integer) obj).intValue());
        } else {
            throw new RuntimeException(InvalidProtocolBufferException.invalidWireType());
        }
    }

    public boolean equals(Object obj) {
        boolean z;
        boolean z2;
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof UnknownFieldSetLite)) {
            return false;
        }
        UnknownFieldSetLite unknownFieldSetLite = (UnknownFieldSetLite) obj;
        int i = this.count;
        if (i == unknownFieldSetLite.count) {
            int[] iArr = this.tags;
            int[] iArr2 = unknownFieldSetLite.tags;
            int i2 = 0;
            while (true) {
                if (i2 >= i) {
                    z = true;
                    break;
                } else if (iArr[i2] != iArr2[i2]) {
                    z = false;
                    break;
                } else {
                    i2++;
                }
            }
            if (z) {
                Object[] objArr = this.objects;
                Object[] objArr2 = unknownFieldSetLite.objects;
                int i3 = this.count;
                int i4 = 0;
                while (true) {
                    if (i4 >= i3) {
                        z2 = true;
                        break;
                    } else if (!objArr[i4].equals(objArr2[i4])) {
                        z2 = false;
                        break;
                    } else {
                        i4++;
                    }
                }
                if (z2) {
                    return true;
                }
            }
        }
        return false;
    }

    public int getSerializedSize() {
        int i;
        int i2 = this.memoizedSerializedSize;
        if (i2 != -1) {
            return i2;
        }
        int i3 = 0;
        for (int i4 = 0; i4 < this.count; i4++) {
            int i5 = this.tags[i4];
            int i6 = i5 >>> 3;
            int i7 = i5 & 7;
            if (i7 == 0) {
                i = CodedOutputStream.computeUInt64Size(i6, ((Long) this.objects[i4]).longValue());
            } else if (i7 == 1) {
                i = CodedOutputStream.computeFixed64Size(i6, ((Long) this.objects[i4]).longValue());
            } else if (i7 == 2) {
                i = CodedOutputStream.computeBytesSize(i6, (ByteString) this.objects[i4]);
            } else if (i7 == 3) {
                i3 = ((UnknownFieldSetLite) this.objects[i4]).getSerializedSize() + (CodedOutputStream.computeTagSize(i6) * 2) + i3;
            } else if (i7 == 5) {
                i = CodedOutputStream.computeFixed32Size(i6, ((Integer) this.objects[i4]).intValue());
            } else {
                throw new IllegalStateException(InvalidProtocolBufferException.invalidWireType());
            }
            i3 = i + i3;
        }
        this.memoizedSerializedSize = i3;
        return i3;
    }

    public int hashCode() {
        int i = this.count;
        int i2 = (527 + i) * 31;
        int[] iArr = this.tags;
        int i3 = 17;
        int i4 = 17;
        for (int i5 = 0; i5 < i; i5++) {
            i4 = (i4 * 31) + iArr[i5];
        }
        int i6 = (i2 + i4) * 31;
        Object[] objArr = this.objects;
        int i7 = this.count;
        for (int i8 = 0; i8 < i7; i8++) {
            i3 = (i3 * 31) + objArr[i8].hashCode();
        }
        return i6 + i3;
    }

    public void storeField(int i, Object obj) {
        if (this.isMutable) {
            int i2 = this.count;
            int[] iArr = this.tags;
            if (i2 == iArr.length) {
                int i3 = i2 + (i2 < 4 ? 8 : i2 >> 1);
                this.tags = Arrays.copyOf(iArr, i3);
                this.objects = Arrays.copyOf(this.objects, i3);
            }
            int[] iArr2 = this.tags;
            int i4 = this.count;
            iArr2[i4] = i;
            this.objects[i4] = obj;
            this.count = i4 + 1;
            return;
        }
        throw new UnsupportedOperationException();
    }

    public void writeTo(Writer writer) throws IOException {
        if (this.count != 0) {
            Objects.requireNonNull(writer);
            for (int i = 0; i < this.count; i++) {
                writeField(this.tags[i], this.objects[i], writer);
            }
        }
    }

    public UnknownFieldSetLite(int i, int[] iArr, Object[] objArr, boolean z) {
        this.memoizedSerializedSize = -1;
        this.count = i;
        this.tags = iArr;
        this.objects = objArr;
        this.isMutable = z;
    }
}
