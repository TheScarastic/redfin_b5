package okio;

import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.nio.charset.Charset;
import kotlin.collections.ArraysKt___ArraysJvmKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Charsets;
import okio.internal.BufferKt;
/* compiled from: Buffer.kt */
/* loaded from: classes2.dex */
public final class Buffer implements BufferedSource, BufferedSink, Cloneable, ByteChannel {
    public Segment head;
    private long size;

    @Override // okio.Source, java.io.Closeable, java.lang.AutoCloseable, java.nio.channels.Channel
    public void close() {
    }

    @Override // java.io.Flushable
    public void flush() {
    }

    @Override // okio.BufferedSource
    public Buffer getBuffer() {
        return this;
    }

    @Override // java.nio.channels.Channel
    public boolean isOpen() {
        return true;
    }

    public final long size() {
        return this.size;
    }

    public final void setSize$external__okio__android_common__okio_lib(long j) {
        this.size = j;
    }

    public long indexOfElement(ByteString byteString, long j) {
        int i;
        int i2;
        Intrinsics.checkNotNullParameter(byteString, "targetBytes");
        long j2 = 0;
        if (j >= 0) {
            Segment segment = this.head;
            if (segment == null) {
                return -1;
            }
            if (size() - j < j) {
                j2 = size();
                while (j2 > j) {
                    segment = segment.prev;
                    Intrinsics.checkNotNull(segment);
                    j2 -= (long) (segment.limit - segment.pos);
                }
                if (byteString.size() == 2) {
                    byte b = byteString.getByte(0);
                    byte b2 = byteString.getByte(1);
                    while (j2 < size()) {
                        byte[] bArr = segment.data;
                        i = (int) ((((long) segment.pos) + j) - j2);
                        int i3 = segment.limit;
                        while (i < i3) {
                            byte b3 = bArr[i];
                            if (!(b3 == b || b3 == b2)) {
                                i++;
                            }
                            i2 = segment.pos;
                        }
                        j2 += (long) (segment.limit - segment.pos);
                        segment = segment.next;
                        Intrinsics.checkNotNull(segment);
                        j = j2;
                    }
                    return -1;
                }
                byte[] internalArray$external__okio__android_common__okio_lib = byteString.internalArray$external__okio__android_common__okio_lib();
                while (j2 < size()) {
                    byte[] bArr2 = segment.data;
                    i = (int) ((((long) segment.pos) + j) - j2);
                    int i4 = segment.limit;
                    while (i < i4) {
                        byte b4 = bArr2[i];
                        int length = internalArray$external__okio__android_common__okio_lib.length;
                        int i5 = 0;
                        while (i5 < length) {
                            byte b5 = internalArray$external__okio__android_common__okio_lib[i5];
                            i5++;
                            if (b4 == b5) {
                                i2 = segment.pos;
                            }
                        }
                        i++;
                    }
                    j2 += (long) (segment.limit - segment.pos);
                    segment = segment.next;
                    Intrinsics.checkNotNull(segment);
                    j = j2;
                }
                return -1;
            }
            while (true) {
                long j3 = ((long) (segment.limit - segment.pos)) + j2;
                if (j3 > j) {
                    break;
                }
                segment = segment.next;
                Intrinsics.checkNotNull(segment);
                j2 = j3;
            }
            if (byteString.size() == 2) {
                byte b6 = byteString.getByte(0);
                byte b7 = byteString.getByte(1);
                while (j2 < size()) {
                    byte[] bArr3 = segment.data;
                    i = (int) ((((long) segment.pos) + j) - j2);
                    int i6 = segment.limit;
                    while (i < i6) {
                        byte b8 = bArr3[i];
                        if (!(b8 == b6 || b8 == b7)) {
                            i++;
                        }
                        i2 = segment.pos;
                    }
                    j2 += (long) (segment.limit - segment.pos);
                    segment = segment.next;
                    Intrinsics.checkNotNull(segment);
                    j = j2;
                }
                return -1;
            }
            byte[] internalArray$external__okio__android_common__okio_lib2 = byteString.internalArray$external__okio__android_common__okio_lib();
            while (j2 < size()) {
                byte[] bArr4 = segment.data;
                i = (int) ((((long) segment.pos) + j) - j2);
                int i7 = segment.limit;
                while (i < i7) {
                    byte b9 = bArr4[i];
                    int length2 = internalArray$external__okio__android_common__okio_lib2.length;
                    int i8 = 0;
                    while (i8 < length2) {
                        byte b10 = internalArray$external__okio__android_common__okio_lib2[i8];
                        i8++;
                        if (b9 == b10) {
                            i2 = segment.pos;
                        }
                    }
                    i++;
                }
                j2 += (long) (segment.limit - segment.pos);
                segment = segment.next;
                Intrinsics.checkNotNull(segment);
                j = j2;
            }
            return -1;
            return ((long) (i - i2)) + j2;
        }
        throw new IllegalArgumentException(Intrinsics.stringPlus("fromIndex < 0: ", Long.valueOf(j)).toString());
    }

    public boolean exhausted() {
        return this.size == 0;
    }

    @Override // okio.BufferedSource
    public boolean request(long j) {
        return this.size >= j;
    }

    public byte readByte() throws EOFException {
        if (size() != 0) {
            Segment segment = this.head;
            Intrinsics.checkNotNull(segment);
            int i = segment.pos;
            int i2 = segment.limit;
            int i3 = i + 1;
            byte b = segment.data[i];
            setSize$external__okio__android_common__okio_lib(size() - 1);
            if (i3 == i2) {
                this.head = segment.pop();
                SegmentPool segmentPool = SegmentPool.INSTANCE;
                SegmentPool.recycle(segment);
            } else {
                segment.pos = i3;
            }
            return b;
        }
        throw new EOFException();
    }

    public String readUtf8() {
        return readString(this.size, Charsets.UTF_8);
    }

    public String readUtf8(long j) throws EOFException {
        return readString(j, Charsets.UTF_8);
    }

    public String readString(long j, Charset charset) throws EOFException {
        Intrinsics.checkNotNullParameter(charset, "charset");
        int i = (j > 0 ? 1 : (j == 0 ? 0 : -1));
        if (!(i >= 0 && j <= 2147483647L)) {
            throw new IllegalArgumentException(Intrinsics.stringPlus("byteCount: ", Long.valueOf(j)).toString());
        } else if (this.size < j) {
            throw new EOFException();
        } else if (i == 0) {
            return "";
        } else {
            Segment segment = this.head;
            Intrinsics.checkNotNull(segment);
            int i2 = segment.pos;
            if (((long) i2) + j > ((long) segment.limit)) {
                return new String(readByteArray(j), charset);
            }
            int i3 = (int) j;
            String str = new String(segment.data, i2, i3, charset);
            int i4 = segment.pos + i3;
            segment.pos = i4;
            this.size -= j;
            if (i4 == segment.limit) {
                this.head = segment.pop();
                SegmentPool segmentPool = SegmentPool.INSTANCE;
                SegmentPool.recycle(segment);
            }
            return str;
        }
    }

    public int readInt() throws EOFException {
        if (size() >= 4) {
            Segment segment = this.head;
            Intrinsics.checkNotNull(segment);
            int i = segment.pos;
            int i2 = segment.limit;
            if (((long) (i2 - i)) < 4) {
                return (readByte() & 255) | ((readByte() & 255) << 24) | ((readByte() & 255) << 16) | ((readByte() & 255) << 8);
            }
            byte[] bArr = segment.data;
            int i3 = i + 1;
            int i4 = i3 + 1;
            int i5 = ((bArr[i] & 255) << 24) | ((bArr[i3] & 255) << 16);
            int i6 = i4 + 1;
            int i7 = i5 | ((bArr[i4] & 255) << 8);
            int i8 = i6 + 1;
            int i9 = i7 | (bArr[i6] & 255);
            setSize$external__okio__android_common__okio_lib(size() - 4);
            if (i8 == i2) {
                this.head = segment.pop();
                SegmentPool segmentPool = SegmentPool.INSTANCE;
                SegmentPool.recycle(segment);
            } else {
                segment.pos = i8;
            }
            return i9;
        }
        throw new EOFException();
    }

    @Override // java.nio.channels.ReadableByteChannel
    public int read(ByteBuffer byteBuffer) throws IOException {
        Intrinsics.checkNotNullParameter(byteBuffer, "sink");
        Segment segment = this.head;
        if (segment == null) {
            return -1;
        }
        int min = Math.min(byteBuffer.remaining(), segment.limit - segment.pos);
        byteBuffer.put(segment.data, segment.pos, min);
        int i = segment.pos + min;
        segment.pos = i;
        this.size -= (long) min;
        if (i == segment.limit) {
            this.head = segment.pop();
            SegmentPool segmentPool = SegmentPool.INSTANCE;
            SegmentPool.recycle(segment);
        }
        return min;
    }

    @Override // okio.BufferedSink
    public Buffer writeUtf8(String str) {
        Intrinsics.checkNotNullParameter(str, "string");
        return writeUtf8(str, 0, str.length());
    }

    public final byte getByte(long j) {
        Util.checkOffsetAndCount(size(), j, 1);
        Segment segment = this.head;
        if (segment == null) {
            Intrinsics.checkNotNull(null);
            throw null;
        } else if (size() - j < j) {
            long size = size();
            while (size > j) {
                segment = segment.prev;
                Intrinsics.checkNotNull(segment);
                size -= (long) (segment.limit - segment.pos);
            }
            return segment.data[(int) ((((long) segment.pos) + j) - size)];
        } else {
            long j2 = 0;
            while (true) {
                int i = segment.limit;
                int i2 = segment.pos;
                long j3 = ((long) (i - i2)) + j2;
                if (j3 > j) {
                    return segment.data[(int) ((((long) i2) + j) - j2)];
                }
                segment = segment.next;
                Intrinsics.checkNotNull(segment);
                j2 = j3;
            }
        }
    }

    public final void clear() {
        skip(size());
    }

    @Override // java.nio.channels.WritableByteChannel
    public int write(ByteBuffer byteBuffer) throws IOException {
        Intrinsics.checkNotNullParameter(byteBuffer, "source");
        int remaining = byteBuffer.remaining();
        int i = remaining;
        while (i > 0) {
            Segment writableSegment$external__okio__android_common__okio_lib = writableSegment$external__okio__android_common__okio_lib(1);
            int min = Math.min(i, 8192 - writableSegment$external__okio__android_common__okio_lib.limit);
            byteBuffer.get(writableSegment$external__okio__android_common__okio_lib.data, writableSegment$external__okio__android_common__okio_lib.limit, min);
            i -= min;
            writableSegment$external__okio__android_common__okio_lib.limit += min;
        }
        this.size += (long) remaining;
        return remaining;
    }

    public void skip(long j) throws EOFException {
        while (j > 0) {
            Segment segment = this.head;
            if (segment != null) {
                int min = (int) Math.min(j, (long) (segment.limit - segment.pos));
                long j2 = (long) min;
                setSize$external__okio__android_common__okio_lib(size() - j2);
                j -= j2;
                int i = segment.pos + min;
                segment.pos = i;
                if (i == segment.limit) {
                    this.head = segment.pop();
                    SegmentPool segmentPool = SegmentPool.INSTANCE;
                    SegmentPool.recycle(segment);
                }
            } else {
                throw new EOFException();
            }
        }
    }

    @Override // okio.BufferedSource
    public long indexOf(ByteString byteString) throws IOException {
        Intrinsics.checkNotNullParameter(byteString, "bytes");
        return indexOf(byteString, 0);
    }

    @Override // okio.BufferedSource
    public long indexOfElement(ByteString byteString) {
        Intrinsics.checkNotNullParameter(byteString, "targetBytes");
        return indexOfElement(byteString, 0);
    }

    public final Segment writableSegment$external__okio__android_common__okio_lib(int i) {
        boolean z = true;
        if (i < 1 || i > 8192) {
            z = false;
        }
        if (z) {
            Segment segment = this.head;
            if (segment == null) {
                SegmentPool segmentPool = SegmentPool.INSTANCE;
                Segment take = SegmentPool.take();
                this.head = take;
                take.prev = take;
                take.next = take;
                return take;
            }
            Intrinsics.checkNotNull(segment);
            Segment segment2 = segment.prev;
            Intrinsics.checkNotNull(segment2);
            if (segment2.limit + i > 8192 || !segment2.owner) {
                SegmentPool segmentPool2 = SegmentPool.INSTANCE;
                segment2 = segment2.push(SegmentPool.take());
            }
            return segment2;
        }
        throw new IllegalArgumentException("unexpected capacity".toString());
    }

    @Override // java.lang.Object
    public String toString() {
        return snapshot().toString();
    }

    @Override // java.lang.Object
    public Buffer clone() {
        return copy();
    }

    public byte[] readByteArray(long j) throws EOFException {
        if (!(j >= 0 && j <= 2147483647L)) {
            throw new IllegalArgumentException(Intrinsics.stringPlus("byteCount: ", Long.valueOf(j)).toString());
        } else if (size() >= j) {
            byte[] bArr = new byte[(int) j];
            readFully(bArr);
            return bArr;
        } else {
            throw new EOFException();
        }
    }

    public void readFully(byte[] bArr) throws EOFException {
        Intrinsics.checkNotNullParameter(bArr, "sink");
        int i = 0;
        while (i < bArr.length) {
            int read = read(bArr, i, bArr.length - i);
            if (read != -1) {
                i += read;
            } else {
                throw new EOFException();
            }
        }
    }

    public int read(byte[] bArr, int i, int i2) {
        Intrinsics.checkNotNullParameter(bArr, "sink");
        Util.checkOffsetAndCount((long) bArr.length, (long) i, (long) i2);
        Segment segment = this.head;
        if (segment == null) {
            return -1;
        }
        int min = Math.min(i2, segment.limit - segment.pos);
        byte[] bArr2 = segment.data;
        int i3 = segment.pos;
        byte[] unused = ArraysKt___ArraysJvmKt.copyInto(bArr2, bArr, i, i3, i3 + min);
        segment.pos += min;
        setSize$external__okio__android_common__okio_lib(size() - ((long) min));
        if (segment.pos == segment.limit) {
            this.head = segment.pop();
            SegmentPool segmentPool = SegmentPool.INSTANCE;
            SegmentPool.recycle(segment);
        }
        return min;
    }

    public ByteString readByteString() {
        return readByteString(size());
    }

    public ByteString readByteString(long j) throws EOFException {
        if (!(j >= 0 && j <= 2147483647L)) {
            throw new IllegalArgumentException(Intrinsics.stringPlus("byteCount: ", Long.valueOf(j)).toString());
        } else if (size() < j) {
            throw new EOFException();
        } else if (j < 4096) {
            return new ByteString(readByteArray(j));
        } else {
            ByteString snapshot = snapshot((int) j);
            skip(j);
            return snapshot;
        }
    }

    @Override // okio.BufferedSource
    public int select(Options options) {
        Intrinsics.checkNotNullParameter(options, "options");
        int selectPrefix$default = BufferKt.selectPrefix$default(this, options, false, 2, null);
        if (selectPrefix$default == -1) {
            return -1;
        }
        skip((long) options.getByteStrings$external__okio__android_common__okio_lib()[selectPrefix$default].size());
        return selectPrefix$default;
    }

    @Override // okio.BufferedSink
    public Buffer writeUtf8(String str, int i, int i2) {
        char charAt;
        char c;
        Intrinsics.checkNotNullParameter(str, "string");
        if (i >= 0) {
            if (i2 >= i) {
                if (i2 <= str.length()) {
                    while (i < i2) {
                        char charAt2 = str.charAt(i);
                        if (charAt2 < 128) {
                            Segment writableSegment$external__okio__android_common__okio_lib = writableSegment$external__okio__android_common__okio_lib(1);
                            byte[] bArr = writableSegment$external__okio__android_common__okio_lib.data;
                            int i3 = writableSegment$external__okio__android_common__okio_lib.limit - i;
                            int min = Math.min(i2, 8192 - i3);
                            int i4 = i + 1;
                            bArr[i + i3] = (byte) charAt2;
                            while (true) {
                                i = i4;
                                if (i >= min || (charAt = str.charAt(i)) >= 128) {
                                    break;
                                }
                                i4 = i + 1;
                                bArr[i + i3] = (byte) charAt;
                            }
                            int i5 = writableSegment$external__okio__android_common__okio_lib.limit;
                            int i6 = (i3 + i) - i5;
                            writableSegment$external__okio__android_common__okio_lib.limit = i5 + i6;
                            setSize$external__okio__android_common__okio_lib(size() + ((long) i6));
                        } else {
                            if (charAt2 < 2048) {
                                Segment writableSegment$external__okio__android_common__okio_lib2 = writableSegment$external__okio__android_common__okio_lib(2);
                                byte[] bArr2 = writableSegment$external__okio__android_common__okio_lib2.data;
                                int i7 = writableSegment$external__okio__android_common__okio_lib2.limit;
                                bArr2[i7] = (byte) ((charAt2 >> 6) | 192);
                                bArr2[i7 + 1] = (byte) ((charAt2 & '?') | 128);
                                writableSegment$external__okio__android_common__okio_lib2.limit = i7 + 2;
                                setSize$external__okio__android_common__okio_lib(size() + 2);
                            } else if (charAt2 < 55296 || charAt2 > 57343) {
                                Segment writableSegment$external__okio__android_common__okio_lib3 = writableSegment$external__okio__android_common__okio_lib(3);
                                byte[] bArr3 = writableSegment$external__okio__android_common__okio_lib3.data;
                                int i8 = writableSegment$external__okio__android_common__okio_lib3.limit;
                                bArr3[i8] = (byte) ((charAt2 >> '\f') | 224);
                                bArr3[i8 + 1] = (byte) ((63 & (charAt2 >> 6)) | 128);
                                bArr3[i8 + 2] = (byte) ((charAt2 & '?') | 128);
                                writableSegment$external__okio__android_common__okio_lib3.limit = i8 + 3;
                                setSize$external__okio__android_common__okio_lib(size() + 3);
                            } else {
                                int i9 = i + 1;
                                if (i9 < i2) {
                                    c = str.charAt(i9);
                                } else {
                                    c = 0;
                                }
                                if (charAt2 <= 56319) {
                                    if (56320 <= c && c <= 57343) {
                                        int i10 = (((charAt2 & 1023) << 10) | (c & 1023)) + 65536;
                                        Segment writableSegment$external__okio__android_common__okio_lib4 = writableSegment$external__okio__android_common__okio_lib(4);
                                        byte[] bArr4 = writableSegment$external__okio__android_common__okio_lib4.data;
                                        int i11 = writableSegment$external__okio__android_common__okio_lib4.limit;
                                        bArr4[i11] = (byte) ((i10 >> 18) | 240);
                                        bArr4[i11 + 1] = (byte) (((i10 >> 12) & 63) | 128);
                                        bArr4[i11 + 2] = (byte) (((i10 >> 6) & 63) | 128);
                                        bArr4[i11 + 3] = (byte) ((i10 & 63) | 128);
                                        writableSegment$external__okio__android_common__okio_lib4.limit = i11 + 4;
                                        setSize$external__okio__android_common__okio_lib(size() + 4);
                                        i += 2;
                                    }
                                }
                                writeByte(63);
                                i = i9;
                            }
                            i++;
                        }
                    }
                    return this;
                }
                throw new IllegalArgumentException(("endIndex > string.length: " + i2 + " > " + str.length()).toString());
            }
            throw new IllegalArgumentException(("endIndex < beginIndex: " + i2 + " < " + i).toString());
        }
        throw new IllegalArgumentException(Intrinsics.stringPlus("beginIndex < 0: ", Integer.valueOf(i)).toString());
    }

    public long writeAll(Source source) throws IOException {
        Intrinsics.checkNotNullParameter(source, "source");
        long j = 0;
        while (true) {
            long read = source.read(this, 8192);
            if (read == -1) {
                return j;
            }
            j += read;
        }
    }

    @Override // okio.BufferedSink
    public Buffer writeByte(int i) {
        Segment writableSegment$external__okio__android_common__okio_lib = writableSegment$external__okio__android_common__okio_lib(1);
        byte[] bArr = writableSegment$external__okio__android_common__okio_lib.data;
        int i2 = writableSegment$external__okio__android_common__okio_lib.limit;
        writableSegment$external__okio__android_common__okio_lib.limit = i2 + 1;
        bArr[i2] = (byte) i;
        setSize$external__okio__android_common__okio_lib(size() + 1);
        return this;
    }

    public Buffer writeInt(int i) {
        Segment writableSegment$external__okio__android_common__okio_lib = writableSegment$external__okio__android_common__okio_lib(4);
        byte[] bArr = writableSegment$external__okio__android_common__okio_lib.data;
        int i2 = writableSegment$external__okio__android_common__okio_lib.limit;
        int i3 = i2 + 1;
        bArr[i2] = (byte) ((i >>> 24) & 255);
        int i4 = i3 + 1;
        bArr[i3] = (byte) ((i >>> 16) & 255);
        int i5 = i4 + 1;
        bArr[i4] = (byte) ((i >>> 8) & 255);
        bArr[i5] = (byte) (i & 255);
        writableSegment$external__okio__android_common__okio_lib.limit = i5 + 1;
        setSize$external__okio__android_common__okio_lib(size() + 4);
        return this;
    }

    public void write(Buffer buffer, long j) {
        Segment segment;
        Intrinsics.checkNotNullParameter(buffer, "source");
        if (buffer != this) {
            Util.checkOffsetAndCount(buffer.size(), 0, j);
            while (j > 0) {
                Segment segment2 = buffer.head;
                Intrinsics.checkNotNull(segment2);
                int i = segment2.limit;
                Segment segment3 = buffer.head;
                Intrinsics.checkNotNull(segment3);
                if (j < ((long) (i - segment3.pos))) {
                    Segment segment4 = this.head;
                    if (segment4 != null) {
                        Intrinsics.checkNotNull(segment4);
                        segment = segment4.prev;
                    } else {
                        segment = null;
                    }
                    if (segment != null && segment.owner) {
                        if ((((long) segment.limit) + j) - ((long) (segment.shared ? 0 : segment.pos)) <= 8192) {
                            Segment segment5 = buffer.head;
                            Intrinsics.checkNotNull(segment5);
                            segment5.writeTo(segment, (int) j);
                            buffer.setSize$external__okio__android_common__okio_lib(buffer.size() - j);
                            setSize$external__okio__android_common__okio_lib(size() + j);
                            return;
                        }
                    }
                    Segment segment6 = buffer.head;
                    Intrinsics.checkNotNull(segment6);
                    buffer.head = segment6.split((int) j);
                }
                Segment segment7 = buffer.head;
                Intrinsics.checkNotNull(segment7);
                long j2 = (long) (segment7.limit - segment7.pos);
                buffer.head = segment7.pop();
                Segment segment8 = this.head;
                if (segment8 == null) {
                    this.head = segment7;
                    segment7.prev = segment7;
                    segment7.next = segment7;
                } else {
                    Intrinsics.checkNotNull(segment8);
                    Segment segment9 = segment8.prev;
                    Intrinsics.checkNotNull(segment9);
                    segment9.push(segment7).compact();
                }
                buffer.setSize$external__okio__android_common__okio_lib(buffer.size() - j2);
                setSize$external__okio__android_common__okio_lib(size() + j2);
                j -= j2;
            }
            return;
        }
        throw new IllegalArgumentException("source == this".toString());
    }

    @Override // okio.Source
    public long read(Buffer buffer, long j) {
        Intrinsics.checkNotNullParameter(buffer, "sink");
        if (!(j >= 0)) {
            throw new IllegalArgumentException(Intrinsics.stringPlus("byteCount < 0: ", Long.valueOf(j)).toString());
        } else if (size() == 0) {
            return -1;
        } else {
            if (j > size()) {
                j = size();
            }
            buffer.write(this, j);
            return j;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:65:?, code lost:
        return ((long) (r4 - r6.pos)) + r10;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public long indexOf(okio.ByteString r17, long r18) throws java.io.IOException {
        /*
        // Method dump skipped, instructions count: 299
        */
        throw new UnsupportedOperationException("Method not decompiled: okio.Buffer.indexOf(okio.ByteString, long):long");
    }

    @Override // java.lang.Object
    public boolean equals(Object obj) {
        if (this != obj) {
            if (!(obj instanceof Buffer)) {
                return false;
            }
            Buffer buffer = (Buffer) obj;
            if (size() != buffer.size()) {
                return false;
            }
            if (size() != 0) {
                Segment segment = this.head;
                Intrinsics.checkNotNull(segment);
                Segment segment2 = buffer.head;
                Intrinsics.checkNotNull(segment2);
                int i = segment.pos;
                int i2 = segment2.pos;
                long j = 0;
                while (j < size()) {
                    long min = (long) Math.min(segment.limit - i, segment2.limit - i2);
                    if (0 < min) {
                        long j2 = 0;
                        while (true) {
                            j2++;
                            int i3 = i + 1;
                            int i4 = i2 + 1;
                            if (segment.data[i] != segment2.data[i2]) {
                                return false;
                            }
                            if (j2 >= min) {
                                i = i3;
                                i2 = i4;
                                break;
                            }
                            i = i3;
                            i2 = i4;
                        }
                    }
                    if (i == segment.limit) {
                        segment = segment.next;
                        Intrinsics.checkNotNull(segment);
                        i = segment.pos;
                    }
                    if (i2 == segment2.limit) {
                        segment2 = segment2.next;
                        Intrinsics.checkNotNull(segment2);
                        i2 = segment2.pos;
                    }
                    j += min;
                }
            }
        }
        return true;
    }

    @Override // java.lang.Object
    public int hashCode() {
        Segment segment = this.head;
        if (segment == null) {
            return 0;
        }
        int i = 1;
        do {
            int i2 = segment.limit;
            for (int i3 = segment.pos; i3 < i2; i3++) {
                i = (i * 31) + segment.data[i3];
            }
            segment = segment.next;
            Intrinsics.checkNotNull(segment);
        } while (segment != this.head);
        return i;
    }

    public final Buffer copy() {
        Buffer buffer = new Buffer();
        if (size() != 0) {
            Segment segment = this.head;
            Intrinsics.checkNotNull(segment);
            Segment sharedCopy = segment.sharedCopy();
            buffer.head = sharedCopy;
            sharedCopy.prev = sharedCopy;
            sharedCopy.next = sharedCopy;
            for (Segment segment2 = segment.next; segment2 != segment; segment2 = segment2.next) {
                Segment segment3 = sharedCopy.prev;
                Intrinsics.checkNotNull(segment3);
                Intrinsics.checkNotNull(segment2);
                segment3.push(segment2.sharedCopy());
            }
            buffer.setSize$external__okio__android_common__okio_lib(size());
        }
        return buffer;
    }

    public final ByteString snapshot() {
        if (size() <= 2147483647L) {
            return snapshot((int) size());
        }
        throw new IllegalStateException(Intrinsics.stringPlus("size > Int.MAX_VALUE: ", Long.valueOf(size())).toString());
    }

    public final ByteString snapshot(int i) {
        if (i == 0) {
            return ByteString.EMPTY;
        }
        Util.checkOffsetAndCount(size(), 0, (long) i);
        Segment segment = this.head;
        int i2 = 0;
        int i3 = 0;
        int i4 = 0;
        while (i3 < i) {
            Intrinsics.checkNotNull(segment);
            int i5 = segment.limit;
            int i6 = segment.pos;
            if (i5 != i6) {
                i3 += i5 - i6;
                i4++;
                segment = segment.next;
            } else {
                throw new AssertionError("s.limit == s.pos");
            }
        }
        byte[][] bArr = new byte[i4];
        int[] iArr = new int[i4 * 2];
        Segment segment2 = this.head;
        int i7 = 0;
        while (i2 < i) {
            Intrinsics.checkNotNull(segment2);
            bArr[i7] = segment2.data;
            i2 += segment2.limit - segment2.pos;
            iArr[i7] = Math.min(i2, i);
            iArr[i7 + i4] = segment2.pos;
            segment2.shared = true;
            i7++;
            segment2 = segment2.next;
        }
        return new SegmentedByteString(bArr, iArr);
    }
}
