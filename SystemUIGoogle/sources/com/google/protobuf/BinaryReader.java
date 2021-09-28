package com.google.protobuf;

import com.google.protobuf.MapEntryLite;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;
/* loaded from: classes2.dex */
abstract class BinaryReader implements Reader {
    public static BinaryReader newInstance(ByteBuffer byteBuffer, boolean z) {
        if (byteBuffer.hasArray()) {
            return new SafeHeapReader(byteBuffer, z);
        }
        throw new IllegalArgumentException("Direct buffers not yet supported");
    }

    private BinaryReader() {
    }

    /* access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static final class SafeHeapReader extends BinaryReader {
        private final byte[] buffer;
        private final boolean bufferIsImmutable;
        private int endGroupTag;
        private final int initialPos;
        private int limit;
        private int pos;
        private int tag;

        public SafeHeapReader(ByteBuffer byteBuffer, boolean z) {
            super();
            this.bufferIsImmutable = z;
            this.buffer = byteBuffer.array();
            int arrayOffset = byteBuffer.arrayOffset() + byteBuffer.position();
            this.pos = arrayOffset;
            this.initialPos = arrayOffset;
            this.limit = byteBuffer.arrayOffset() + byteBuffer.limit();
        }

        private boolean isAtEnd() {
            return this.pos == this.limit;
        }

        @Override // com.google.protobuf.Reader
        public int getFieldNumber() throws IOException {
            if (isAtEnd()) {
                return Integer.MAX_VALUE;
            }
            int readVarint32 = readVarint32();
            this.tag = readVarint32;
            if (readVarint32 == this.endGroupTag) {
                return Integer.MAX_VALUE;
            }
            return WireFormat.getTagFieldNumber(readVarint32);
        }

        @Override // com.google.protobuf.Reader
        public int getTag() {
            return this.tag;
        }

        @Override // com.google.protobuf.Reader
        public boolean skipField() throws IOException {
            int i;
            if (isAtEnd() || (i = this.tag) == this.endGroupTag) {
                return false;
            }
            int tagWireType = WireFormat.getTagWireType(i);
            if (tagWireType == 0) {
                skipVarint();
                return true;
            } else if (tagWireType == 1) {
                skipBytes(8);
                return true;
            } else if (tagWireType == 2) {
                skipBytes(readVarint32());
                return true;
            } else if (tagWireType == 3) {
                skipGroup();
                return true;
            } else if (tagWireType == 5) {
                skipBytes(4);
                return true;
            } else {
                throw InvalidProtocolBufferException.invalidWireType();
            }
        }

        @Override // com.google.protobuf.Reader
        public double readDouble() throws IOException {
            requireWireType(1);
            return Double.longBitsToDouble(readLittleEndian64());
        }

        @Override // com.google.protobuf.Reader
        public float readFloat() throws IOException {
            requireWireType(5);
            return Float.intBitsToFloat(readLittleEndian32());
        }

        @Override // com.google.protobuf.Reader
        public long readUInt64() throws IOException {
            requireWireType(0);
            return readVarint64();
        }

        @Override // com.google.protobuf.Reader
        public long readInt64() throws IOException {
            requireWireType(0);
            return readVarint64();
        }

        @Override // com.google.protobuf.Reader
        public int readInt32() throws IOException {
            requireWireType(0);
            return readVarint32();
        }

        @Override // com.google.protobuf.Reader
        public long readFixed64() throws IOException {
            requireWireType(1);
            return readLittleEndian64();
        }

        @Override // com.google.protobuf.Reader
        public int readFixed32() throws IOException {
            requireWireType(5);
            return readLittleEndian32();
        }

        @Override // com.google.protobuf.Reader
        public boolean readBool() throws IOException {
            requireWireType(0);
            if (readVarint32() != 0) {
                return true;
            }
            return false;
        }

        @Override // com.google.protobuf.Reader
        public String readString() throws IOException {
            return readStringInternal(false);
        }

        @Override // com.google.protobuf.Reader
        public String readStringRequireUtf8() throws IOException {
            return readStringInternal(true);
        }

        public String readStringInternal(boolean z) throws IOException {
            requireWireType(2);
            int readVarint32 = readVarint32();
            if (readVarint32 == 0) {
                return "";
            }
            requireBytes(readVarint32);
            if (z) {
                byte[] bArr = this.buffer;
                int i = this.pos;
                if (!Utf8.isValidUtf8(bArr, i, i + readVarint32)) {
                    throw InvalidProtocolBufferException.invalidUtf8();
                }
            }
            String str = new String(this.buffer, this.pos, readVarint32, Internal.UTF_8);
            this.pos += readVarint32;
            return str;
        }

        @Override // com.google.protobuf.Reader
        public <T> T readMessage(Class<T> cls, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            requireWireType(2);
            return (T) readMessage(Protobuf.getInstance().schemaFor((Class) cls), extensionRegistryLite);
        }

        @Override // com.google.protobuf.Reader
        public <T> T readMessageBySchemaWithCheck(Schema<T> schema, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            requireWireType(2);
            return (T) readMessage(schema, extensionRegistryLite);
        }

        private <T> T readMessage(Schema<T> schema, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            int readVarint32 = readVarint32();
            requireBytes(readVarint32);
            int i = this.limit;
            int i2 = this.pos + readVarint32;
            this.limit = i2;
            try {
                T newInstance = schema.newInstance();
                schema.mergeFrom(newInstance, this, extensionRegistryLite);
                schema.makeImmutable(newInstance);
                if (this.pos == i2) {
                    return newInstance;
                }
                throw InvalidProtocolBufferException.parseFailure();
            } finally {
                this.limit = i;
            }
        }

        @Override // com.google.protobuf.Reader
        public <T> T readGroup(Class<T> cls, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            requireWireType(3);
            return (T) readGroup(Protobuf.getInstance().schemaFor((Class) cls), extensionRegistryLite);
        }

        @Override // com.google.protobuf.Reader
        public <T> T readGroupBySchemaWithCheck(Schema<T> schema, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            requireWireType(3);
            return (T) readGroup(schema, extensionRegistryLite);
        }

        private <T> T readGroup(Schema<T> schema, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            int i = this.endGroupTag;
            this.endGroupTag = WireFormat.makeTag(WireFormat.getTagFieldNumber(this.tag), 4);
            try {
                T newInstance = schema.newInstance();
                schema.mergeFrom(newInstance, this, extensionRegistryLite);
                schema.makeImmutable(newInstance);
                if (this.tag == this.endGroupTag) {
                    return newInstance;
                }
                throw InvalidProtocolBufferException.parseFailure();
            } finally {
                this.endGroupTag = i;
            }
        }

        @Override // com.google.protobuf.Reader
        public ByteString readBytes() throws IOException {
            ByteString byteString;
            requireWireType(2);
            int readVarint32 = readVarint32();
            if (readVarint32 == 0) {
                return ByteString.EMPTY;
            }
            requireBytes(readVarint32);
            if (this.bufferIsImmutable) {
                byteString = ByteString.wrap(this.buffer, this.pos, readVarint32);
            } else {
                byteString = ByteString.copyFrom(this.buffer, this.pos, readVarint32);
            }
            this.pos += readVarint32;
            return byteString;
        }

        @Override // com.google.protobuf.Reader
        public int readUInt32() throws IOException {
            requireWireType(0);
            return readVarint32();
        }

        @Override // com.google.protobuf.Reader
        public int readEnum() throws IOException {
            requireWireType(0);
            return readVarint32();
        }

        @Override // com.google.protobuf.Reader
        public int readSFixed32() throws IOException {
            requireWireType(5);
            return readLittleEndian32();
        }

        @Override // com.google.protobuf.Reader
        public long readSFixed64() throws IOException {
            requireWireType(1);
            return readLittleEndian64();
        }

        @Override // com.google.protobuf.Reader
        public int readSInt32() throws IOException {
            requireWireType(0);
            return CodedInputStream.decodeZigZag32(readVarint32());
        }

        @Override // com.google.protobuf.Reader
        public long readSInt64() throws IOException {
            requireWireType(0);
            return CodedInputStream.decodeZigZag64(readVarint64());
        }

        @Override // com.google.protobuf.Reader
        public void readDoubleList(List<Double> list) throws IOException {
            int i;
            int i2;
            if (list instanceof DoubleArrayList) {
                DoubleArrayList doubleArrayList = (DoubleArrayList) list;
                int tagWireType = WireFormat.getTagWireType(this.tag);
                if (tagWireType == 1) {
                    do {
                        doubleArrayList.addDouble(readDouble());
                        if (!isAtEnd()) {
                            i2 = this.pos;
                        } else {
                            return;
                        }
                    } while (readVarint32() == this.tag);
                    this.pos = i2;
                } else if (tagWireType == 2) {
                    int readVarint32 = readVarint32();
                    verifyPackedFixed64Length(readVarint32);
                    int i3 = this.pos + readVarint32;
                    while (this.pos < i3) {
                        doubleArrayList.addDouble(Double.longBitsToDouble(readLittleEndian64_NoCheck()));
                    }
                } else {
                    throw InvalidProtocolBufferException.invalidWireType();
                }
            } else {
                int tagWireType2 = WireFormat.getTagWireType(this.tag);
                if (tagWireType2 == 1) {
                    do {
                        list.add(Double.valueOf(readDouble()));
                        if (!isAtEnd()) {
                            i = this.pos;
                        } else {
                            return;
                        }
                    } while (readVarint32() == this.tag);
                    this.pos = i;
                } else if (tagWireType2 == 2) {
                    int readVarint322 = readVarint32();
                    verifyPackedFixed64Length(readVarint322);
                    int i4 = this.pos + readVarint322;
                    while (this.pos < i4) {
                        list.add(Double.valueOf(Double.longBitsToDouble(readLittleEndian64_NoCheck())));
                    }
                } else {
                    throw InvalidProtocolBufferException.invalidWireType();
                }
            }
        }

        @Override // com.google.protobuf.Reader
        public void readFloatList(List<Float> list) throws IOException {
            int i;
            int i2;
            if (list instanceof FloatArrayList) {
                FloatArrayList floatArrayList = (FloatArrayList) list;
                int tagWireType = WireFormat.getTagWireType(this.tag);
                if (tagWireType == 2) {
                    int readVarint32 = readVarint32();
                    verifyPackedFixed32Length(readVarint32);
                    int i3 = this.pos + readVarint32;
                    while (this.pos < i3) {
                        floatArrayList.addFloat(Float.intBitsToFloat(readLittleEndian32_NoCheck()));
                    }
                } else if (tagWireType == 5) {
                    do {
                        floatArrayList.addFloat(readFloat());
                        if (!isAtEnd()) {
                            i2 = this.pos;
                        } else {
                            return;
                        }
                    } while (readVarint32() == this.tag);
                    this.pos = i2;
                } else {
                    throw InvalidProtocolBufferException.invalidWireType();
                }
            } else {
                int tagWireType2 = WireFormat.getTagWireType(this.tag);
                if (tagWireType2 == 2) {
                    int readVarint322 = readVarint32();
                    verifyPackedFixed32Length(readVarint322);
                    int i4 = this.pos + readVarint322;
                    while (this.pos < i4) {
                        list.add(Float.valueOf(Float.intBitsToFloat(readLittleEndian32_NoCheck())));
                    }
                } else if (tagWireType2 == 5) {
                    do {
                        list.add(Float.valueOf(readFloat()));
                        if (!isAtEnd()) {
                            i = this.pos;
                        } else {
                            return;
                        }
                    } while (readVarint32() == this.tag);
                    this.pos = i;
                } else {
                    throw InvalidProtocolBufferException.invalidWireType();
                }
            }
        }

        @Override // com.google.protobuf.Reader
        public void readUInt64List(List<Long> list) throws IOException {
            int i;
            int i2;
            if (list instanceof LongArrayList) {
                LongArrayList longArrayList = (LongArrayList) list;
                int tagWireType = WireFormat.getTagWireType(this.tag);
                if (tagWireType == 0) {
                    do {
                        longArrayList.addLong(readUInt64());
                        if (!isAtEnd()) {
                            i2 = this.pos;
                        } else {
                            return;
                        }
                    } while (readVarint32() == this.tag);
                    this.pos = i2;
                } else if (tagWireType == 2) {
                    int readVarint32 = this.pos + readVarint32();
                    while (this.pos < readVarint32) {
                        longArrayList.addLong(readVarint64());
                    }
                    requirePosition(readVarint32);
                } else {
                    throw InvalidProtocolBufferException.invalidWireType();
                }
            } else {
                int tagWireType2 = WireFormat.getTagWireType(this.tag);
                if (tagWireType2 == 0) {
                    do {
                        list.add(Long.valueOf(readUInt64()));
                        if (!isAtEnd()) {
                            i = this.pos;
                        } else {
                            return;
                        }
                    } while (readVarint32() == this.tag);
                    this.pos = i;
                } else if (tagWireType2 == 2) {
                    int readVarint322 = this.pos + readVarint32();
                    while (this.pos < readVarint322) {
                        list.add(Long.valueOf(readVarint64()));
                    }
                    requirePosition(readVarint322);
                } else {
                    throw InvalidProtocolBufferException.invalidWireType();
                }
            }
        }

        @Override // com.google.protobuf.Reader
        public void readInt64List(List<Long> list) throws IOException {
            int i;
            int i2;
            if (list instanceof LongArrayList) {
                LongArrayList longArrayList = (LongArrayList) list;
                int tagWireType = WireFormat.getTagWireType(this.tag);
                if (tagWireType == 0) {
                    do {
                        longArrayList.addLong(readInt64());
                        if (!isAtEnd()) {
                            i2 = this.pos;
                        } else {
                            return;
                        }
                    } while (readVarint32() == this.tag);
                    this.pos = i2;
                } else if (tagWireType == 2) {
                    int readVarint32 = this.pos + readVarint32();
                    while (this.pos < readVarint32) {
                        longArrayList.addLong(readVarint64());
                    }
                    requirePosition(readVarint32);
                } else {
                    throw InvalidProtocolBufferException.invalidWireType();
                }
            } else {
                int tagWireType2 = WireFormat.getTagWireType(this.tag);
                if (tagWireType2 == 0) {
                    do {
                        list.add(Long.valueOf(readInt64()));
                        if (!isAtEnd()) {
                            i = this.pos;
                        } else {
                            return;
                        }
                    } while (readVarint32() == this.tag);
                    this.pos = i;
                } else if (tagWireType2 == 2) {
                    int readVarint322 = this.pos + readVarint32();
                    while (this.pos < readVarint322) {
                        list.add(Long.valueOf(readVarint64()));
                    }
                    requirePosition(readVarint322);
                } else {
                    throw InvalidProtocolBufferException.invalidWireType();
                }
            }
        }

        @Override // com.google.protobuf.Reader
        public void readInt32List(List<Integer> list) throws IOException {
            int i;
            int i2;
            if (list instanceof IntArrayList) {
                IntArrayList intArrayList = (IntArrayList) list;
                int tagWireType = WireFormat.getTagWireType(this.tag);
                if (tagWireType == 0) {
                    do {
                        intArrayList.addInt(readInt32());
                        if (!isAtEnd()) {
                            i2 = this.pos;
                        } else {
                            return;
                        }
                    } while (readVarint32() == this.tag);
                    this.pos = i2;
                } else if (tagWireType == 2) {
                    int readVarint32 = this.pos + readVarint32();
                    while (this.pos < readVarint32) {
                        intArrayList.addInt(readVarint32());
                    }
                    requirePosition(readVarint32);
                } else {
                    throw InvalidProtocolBufferException.invalidWireType();
                }
            } else {
                int tagWireType2 = WireFormat.getTagWireType(this.tag);
                if (tagWireType2 == 0) {
                    do {
                        list.add(Integer.valueOf(readInt32()));
                        if (!isAtEnd()) {
                            i = this.pos;
                        } else {
                            return;
                        }
                    } while (readVarint32() == this.tag);
                    this.pos = i;
                } else if (tagWireType2 == 2) {
                    int readVarint322 = this.pos + readVarint32();
                    while (this.pos < readVarint322) {
                        list.add(Integer.valueOf(readVarint32()));
                    }
                    requirePosition(readVarint322);
                } else {
                    throw InvalidProtocolBufferException.invalidWireType();
                }
            }
        }

        @Override // com.google.protobuf.Reader
        public void readFixed64List(List<Long> list) throws IOException {
            int i;
            int i2;
            if (list instanceof LongArrayList) {
                LongArrayList longArrayList = (LongArrayList) list;
                int tagWireType = WireFormat.getTagWireType(this.tag);
                if (tagWireType == 1) {
                    do {
                        longArrayList.addLong(readFixed64());
                        if (!isAtEnd()) {
                            i2 = this.pos;
                        } else {
                            return;
                        }
                    } while (readVarint32() == this.tag);
                    this.pos = i2;
                } else if (tagWireType == 2) {
                    int readVarint32 = readVarint32();
                    verifyPackedFixed64Length(readVarint32);
                    int i3 = this.pos + readVarint32;
                    while (this.pos < i3) {
                        longArrayList.addLong(readLittleEndian64_NoCheck());
                    }
                } else {
                    throw InvalidProtocolBufferException.invalidWireType();
                }
            } else {
                int tagWireType2 = WireFormat.getTagWireType(this.tag);
                if (tagWireType2 == 1) {
                    do {
                        list.add(Long.valueOf(readFixed64()));
                        if (!isAtEnd()) {
                            i = this.pos;
                        } else {
                            return;
                        }
                    } while (readVarint32() == this.tag);
                    this.pos = i;
                } else if (tagWireType2 == 2) {
                    int readVarint322 = readVarint32();
                    verifyPackedFixed64Length(readVarint322);
                    int i4 = this.pos + readVarint322;
                    while (this.pos < i4) {
                        list.add(Long.valueOf(readLittleEndian64_NoCheck()));
                    }
                } else {
                    throw InvalidProtocolBufferException.invalidWireType();
                }
            }
        }

        @Override // com.google.protobuf.Reader
        public void readFixed32List(List<Integer> list) throws IOException {
            int i;
            int i2;
            if (list instanceof IntArrayList) {
                IntArrayList intArrayList = (IntArrayList) list;
                int tagWireType = WireFormat.getTagWireType(this.tag);
                if (tagWireType == 2) {
                    int readVarint32 = readVarint32();
                    verifyPackedFixed32Length(readVarint32);
                    int i3 = this.pos + readVarint32;
                    while (this.pos < i3) {
                        intArrayList.addInt(readLittleEndian32_NoCheck());
                    }
                } else if (tagWireType == 5) {
                    do {
                        intArrayList.addInt(readFixed32());
                        if (!isAtEnd()) {
                            i2 = this.pos;
                        } else {
                            return;
                        }
                    } while (readVarint32() == this.tag);
                    this.pos = i2;
                } else {
                    throw InvalidProtocolBufferException.invalidWireType();
                }
            } else {
                int tagWireType2 = WireFormat.getTagWireType(this.tag);
                if (tagWireType2 == 2) {
                    int readVarint322 = readVarint32();
                    verifyPackedFixed32Length(readVarint322);
                    int i4 = this.pos + readVarint322;
                    while (this.pos < i4) {
                        list.add(Integer.valueOf(readLittleEndian32_NoCheck()));
                    }
                } else if (tagWireType2 == 5) {
                    do {
                        list.add(Integer.valueOf(readFixed32()));
                        if (!isAtEnd()) {
                            i = this.pos;
                        } else {
                            return;
                        }
                    } while (readVarint32() == this.tag);
                    this.pos = i;
                } else {
                    throw InvalidProtocolBufferException.invalidWireType();
                }
            }
        }

        @Override // com.google.protobuf.Reader
        public void readBoolList(List<Boolean> list) throws IOException {
            int i;
            int i2;
            if (list instanceof BooleanArrayList) {
                BooleanArrayList booleanArrayList = (BooleanArrayList) list;
                int tagWireType = WireFormat.getTagWireType(this.tag);
                if (tagWireType == 0) {
                    do {
                        booleanArrayList.addBoolean(readBool());
                        if (!isAtEnd()) {
                            i2 = this.pos;
                        } else {
                            return;
                        }
                    } while (readVarint32() == this.tag);
                    this.pos = i2;
                } else if (tagWireType == 2) {
                    int readVarint32 = this.pos + readVarint32();
                    while (this.pos < readVarint32) {
                        booleanArrayList.addBoolean(readVarint32() != 0);
                    }
                    requirePosition(readVarint32);
                } else {
                    throw InvalidProtocolBufferException.invalidWireType();
                }
            } else {
                int tagWireType2 = WireFormat.getTagWireType(this.tag);
                if (tagWireType2 == 0) {
                    do {
                        list.add(Boolean.valueOf(readBool()));
                        if (!isAtEnd()) {
                            i = this.pos;
                        } else {
                            return;
                        }
                    } while (readVarint32() == this.tag);
                    this.pos = i;
                } else if (tagWireType2 == 2) {
                    int readVarint322 = this.pos + readVarint32();
                    while (this.pos < readVarint322) {
                        list.add(Boolean.valueOf(readVarint32() != 0));
                    }
                    requirePosition(readVarint322);
                } else {
                    throw InvalidProtocolBufferException.invalidWireType();
                }
            }
        }

        @Override // com.google.protobuf.Reader
        public void readStringList(List<String> list) throws IOException {
            readStringListInternal(list, false);
        }

        @Override // com.google.protobuf.Reader
        public void readStringListRequireUtf8(List<String> list) throws IOException {
            readStringListInternal(list, true);
        }

        public void readStringListInternal(List<String> list, boolean z) throws IOException {
            int i;
            int i2;
            if (WireFormat.getTagWireType(this.tag) != 2) {
                throw InvalidProtocolBufferException.invalidWireType();
            } else if (!(list instanceof LazyStringList) || z) {
                do {
                    list.add(readStringInternal(z));
                    if (!isAtEnd()) {
                        i = this.pos;
                    } else {
                        return;
                    }
                } while (readVarint32() == this.tag);
                this.pos = i;
            } else {
                LazyStringList lazyStringList = (LazyStringList) list;
                do {
                    lazyStringList.add(readBytes());
                    if (!isAtEnd()) {
                        i2 = this.pos;
                    } else {
                        return;
                    }
                } while (readVarint32() == this.tag);
                this.pos = i2;
            }
        }

        /* JADX DEBUG: Multi-variable search result rejected for r4v0, resolved type: java.util.List<T> */
        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.protobuf.Reader
        public <T> void readMessageList(List<T> list, Schema<T> schema, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            int i;
            if (WireFormat.getTagWireType(this.tag) == 2) {
                int i2 = this.tag;
                do {
                    list.add(readMessage(schema, extensionRegistryLite));
                    if (!isAtEnd()) {
                        i = this.pos;
                    } else {
                        return;
                    }
                } while (readVarint32() == i2);
                this.pos = i;
                return;
            }
            throw InvalidProtocolBufferException.invalidWireType();
        }

        /* JADX DEBUG: Multi-variable search result rejected for r4v0, resolved type: java.util.List<T> */
        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.protobuf.Reader
        public <T> void readGroupList(List<T> list, Schema<T> schema, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            int i;
            if (WireFormat.getTagWireType(this.tag) == 3) {
                int i2 = this.tag;
                do {
                    list.add(readGroup(schema, extensionRegistryLite));
                    if (!isAtEnd()) {
                        i = this.pos;
                    } else {
                        return;
                    }
                } while (readVarint32() == i2);
                this.pos = i;
                return;
            }
            throw InvalidProtocolBufferException.invalidWireType();
        }

        @Override // com.google.protobuf.Reader
        public void readBytesList(List<ByteString> list) throws IOException {
            int i;
            if (WireFormat.getTagWireType(this.tag) == 2) {
                do {
                    list.add(readBytes());
                    if (!isAtEnd()) {
                        i = this.pos;
                    } else {
                        return;
                    }
                } while (readVarint32() == this.tag);
                this.pos = i;
                return;
            }
            throw InvalidProtocolBufferException.invalidWireType();
        }

        @Override // com.google.protobuf.Reader
        public void readUInt32List(List<Integer> list) throws IOException {
            int i;
            int i2;
            if (list instanceof IntArrayList) {
                IntArrayList intArrayList = (IntArrayList) list;
                int tagWireType = WireFormat.getTagWireType(this.tag);
                if (tagWireType == 0) {
                    do {
                        intArrayList.addInt(readUInt32());
                        if (!isAtEnd()) {
                            i2 = this.pos;
                        } else {
                            return;
                        }
                    } while (readVarint32() == this.tag);
                    this.pos = i2;
                } else if (tagWireType == 2) {
                    int readVarint32 = this.pos + readVarint32();
                    while (this.pos < readVarint32) {
                        intArrayList.addInt(readVarint32());
                    }
                } else {
                    throw InvalidProtocolBufferException.invalidWireType();
                }
            } else {
                int tagWireType2 = WireFormat.getTagWireType(this.tag);
                if (tagWireType2 == 0) {
                    do {
                        list.add(Integer.valueOf(readUInt32()));
                        if (!isAtEnd()) {
                            i = this.pos;
                        } else {
                            return;
                        }
                    } while (readVarint32() == this.tag);
                    this.pos = i;
                } else if (tagWireType2 == 2) {
                    int readVarint322 = this.pos + readVarint32();
                    while (this.pos < readVarint322) {
                        list.add(Integer.valueOf(readVarint32()));
                    }
                } else {
                    throw InvalidProtocolBufferException.invalidWireType();
                }
            }
        }

        @Override // com.google.protobuf.Reader
        public void readEnumList(List<Integer> list) throws IOException {
            int i;
            int i2;
            if (list instanceof IntArrayList) {
                IntArrayList intArrayList = (IntArrayList) list;
                int tagWireType = WireFormat.getTagWireType(this.tag);
                if (tagWireType == 0) {
                    do {
                        intArrayList.addInt(readEnum());
                        if (!isAtEnd()) {
                            i2 = this.pos;
                        } else {
                            return;
                        }
                    } while (readVarint32() == this.tag);
                    this.pos = i2;
                } else if (tagWireType == 2) {
                    int readVarint32 = this.pos + readVarint32();
                    while (this.pos < readVarint32) {
                        intArrayList.addInt(readVarint32());
                    }
                } else {
                    throw InvalidProtocolBufferException.invalidWireType();
                }
            } else {
                int tagWireType2 = WireFormat.getTagWireType(this.tag);
                if (tagWireType2 == 0) {
                    do {
                        list.add(Integer.valueOf(readEnum()));
                        if (!isAtEnd()) {
                            i = this.pos;
                        } else {
                            return;
                        }
                    } while (readVarint32() == this.tag);
                    this.pos = i;
                } else if (tagWireType2 == 2) {
                    int readVarint322 = this.pos + readVarint32();
                    while (this.pos < readVarint322) {
                        list.add(Integer.valueOf(readVarint32()));
                    }
                } else {
                    throw InvalidProtocolBufferException.invalidWireType();
                }
            }
        }

        @Override // com.google.protobuf.Reader
        public void readSFixed32List(List<Integer> list) throws IOException {
            int i;
            int i2;
            if (list instanceof IntArrayList) {
                IntArrayList intArrayList = (IntArrayList) list;
                int tagWireType = WireFormat.getTagWireType(this.tag);
                if (tagWireType == 2) {
                    int readVarint32 = readVarint32();
                    verifyPackedFixed32Length(readVarint32);
                    int i3 = this.pos + readVarint32;
                    while (this.pos < i3) {
                        intArrayList.addInt(readLittleEndian32_NoCheck());
                    }
                } else if (tagWireType == 5) {
                    do {
                        intArrayList.addInt(readSFixed32());
                        if (!isAtEnd()) {
                            i2 = this.pos;
                        } else {
                            return;
                        }
                    } while (readVarint32() == this.tag);
                    this.pos = i2;
                } else {
                    throw InvalidProtocolBufferException.invalidWireType();
                }
            } else {
                int tagWireType2 = WireFormat.getTagWireType(this.tag);
                if (tagWireType2 == 2) {
                    int readVarint322 = readVarint32();
                    verifyPackedFixed32Length(readVarint322);
                    int i4 = this.pos + readVarint322;
                    while (this.pos < i4) {
                        list.add(Integer.valueOf(readLittleEndian32_NoCheck()));
                    }
                } else if (tagWireType2 == 5) {
                    do {
                        list.add(Integer.valueOf(readSFixed32()));
                        if (!isAtEnd()) {
                            i = this.pos;
                        } else {
                            return;
                        }
                    } while (readVarint32() == this.tag);
                    this.pos = i;
                } else {
                    throw InvalidProtocolBufferException.invalidWireType();
                }
            }
        }

        @Override // com.google.protobuf.Reader
        public void readSFixed64List(List<Long> list) throws IOException {
            int i;
            int i2;
            if (list instanceof LongArrayList) {
                LongArrayList longArrayList = (LongArrayList) list;
                int tagWireType = WireFormat.getTagWireType(this.tag);
                if (tagWireType == 1) {
                    do {
                        longArrayList.addLong(readSFixed64());
                        if (!isAtEnd()) {
                            i2 = this.pos;
                        } else {
                            return;
                        }
                    } while (readVarint32() == this.tag);
                    this.pos = i2;
                } else if (tagWireType == 2) {
                    int readVarint32 = readVarint32();
                    verifyPackedFixed64Length(readVarint32);
                    int i3 = this.pos + readVarint32;
                    while (this.pos < i3) {
                        longArrayList.addLong(readLittleEndian64_NoCheck());
                    }
                } else {
                    throw InvalidProtocolBufferException.invalidWireType();
                }
            } else {
                int tagWireType2 = WireFormat.getTagWireType(this.tag);
                if (tagWireType2 == 1) {
                    do {
                        list.add(Long.valueOf(readSFixed64()));
                        if (!isAtEnd()) {
                            i = this.pos;
                        } else {
                            return;
                        }
                    } while (readVarint32() == this.tag);
                    this.pos = i;
                } else if (tagWireType2 == 2) {
                    int readVarint322 = readVarint32();
                    verifyPackedFixed64Length(readVarint322);
                    int i4 = this.pos + readVarint322;
                    while (this.pos < i4) {
                        list.add(Long.valueOf(readLittleEndian64_NoCheck()));
                    }
                } else {
                    throw InvalidProtocolBufferException.invalidWireType();
                }
            }
        }

        @Override // com.google.protobuf.Reader
        public void readSInt32List(List<Integer> list) throws IOException {
            int i;
            int i2;
            if (list instanceof IntArrayList) {
                IntArrayList intArrayList = (IntArrayList) list;
                int tagWireType = WireFormat.getTagWireType(this.tag);
                if (tagWireType == 0) {
                    do {
                        intArrayList.addInt(readSInt32());
                        if (!isAtEnd()) {
                            i2 = this.pos;
                        } else {
                            return;
                        }
                    } while (readVarint32() == this.tag);
                    this.pos = i2;
                } else if (tagWireType == 2) {
                    int readVarint32 = this.pos + readVarint32();
                    while (this.pos < readVarint32) {
                        intArrayList.addInt(CodedInputStream.decodeZigZag32(readVarint32()));
                    }
                } else {
                    throw InvalidProtocolBufferException.invalidWireType();
                }
            } else {
                int tagWireType2 = WireFormat.getTagWireType(this.tag);
                if (tagWireType2 == 0) {
                    do {
                        list.add(Integer.valueOf(readSInt32()));
                        if (!isAtEnd()) {
                            i = this.pos;
                        } else {
                            return;
                        }
                    } while (readVarint32() == this.tag);
                    this.pos = i;
                } else if (tagWireType2 == 2) {
                    int readVarint322 = this.pos + readVarint32();
                    while (this.pos < readVarint322) {
                        list.add(Integer.valueOf(CodedInputStream.decodeZigZag32(readVarint32())));
                    }
                } else {
                    throw InvalidProtocolBufferException.invalidWireType();
                }
            }
        }

        @Override // com.google.protobuf.Reader
        public void readSInt64List(List<Long> list) throws IOException {
            int i;
            int i2;
            if (list instanceof LongArrayList) {
                LongArrayList longArrayList = (LongArrayList) list;
                int tagWireType = WireFormat.getTagWireType(this.tag);
                if (tagWireType == 0) {
                    do {
                        longArrayList.addLong(readSInt64());
                        if (!isAtEnd()) {
                            i2 = this.pos;
                        } else {
                            return;
                        }
                    } while (readVarint32() == this.tag);
                    this.pos = i2;
                } else if (tagWireType == 2) {
                    int readVarint32 = this.pos + readVarint32();
                    while (this.pos < readVarint32) {
                        longArrayList.addLong(CodedInputStream.decodeZigZag64(readVarint64()));
                    }
                } else {
                    throw InvalidProtocolBufferException.invalidWireType();
                }
            } else {
                int tagWireType2 = WireFormat.getTagWireType(this.tag);
                if (tagWireType2 == 0) {
                    do {
                        list.add(Long.valueOf(readSInt64()));
                        if (!isAtEnd()) {
                            i = this.pos;
                        } else {
                            return;
                        }
                    } while (readVarint32() == this.tag);
                    this.pos = i;
                } else if (tagWireType2 == 2) {
                    int readVarint322 = this.pos + readVarint32();
                    while (this.pos < readVarint322) {
                        list.add(Long.valueOf(CodedInputStream.decodeZigZag64(readVarint64())));
                    }
                } else {
                    throw InvalidProtocolBufferException.invalidWireType();
                }
            }
        }

        /* JADX INFO: finally extract failed */
        @Override // com.google.protobuf.Reader
        public <K, V> void readMap(Map<K, V> map, MapEntryLite.Metadata<K, V> metadata, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            requireWireType(2);
            int readVarint32 = readVarint32();
            requireBytes(readVarint32);
            int i = this.limit;
            this.limit = this.pos + readVarint32;
            try {
                throw null;
            } catch (Throwable th) {
                this.limit = i;
                throw th;
            }
        }

        private int readVarint32() throws IOException {
            int i;
            int i2 = this.pos;
            int i3 = this.limit;
            if (i3 != i2) {
                byte[] bArr = this.buffer;
                int i4 = i2 + 1;
                byte b = bArr[i2];
                if (b >= 0) {
                    this.pos = i4;
                    return b;
                } else if (i3 - i4 < 9) {
                    return (int) readVarint64SlowPath();
                } else {
                    int i5 = i4 + 1;
                    int i6 = b ^ (bArr[i4] << 7);
                    if (i6 < 0) {
                        i = i6 ^ -128;
                    } else {
                        int i7 = i5 + 1;
                        int i8 = i6 ^ (bArr[i5] << 14);
                        if (i8 >= 0) {
                            i = i8 ^ 16256;
                        } else {
                            i5 = i7 + 1;
                            int i9 = i8 ^ (bArr[i7] << 21);
                            if (i9 < 0) {
                                i = i9 ^ -2080896;
                            } else {
                                i7 = i5 + 1;
                                byte b2 = bArr[i5];
                                i = (i9 ^ (b2 << 28)) ^ 266354560;
                                if (b2 < 0) {
                                    i5 = i7 + 1;
                                    if (bArr[i7] < 0) {
                                        i7 = i5 + 1;
                                        if (bArr[i5] < 0) {
                                            i5 = i7 + 1;
                                            if (bArr[i7] < 0) {
                                                i7 = i5 + 1;
                                                if (bArr[i5] < 0) {
                                                    i5 = i7 + 1;
                                                    if (bArr[i7] < 0) {
                                                        throw InvalidProtocolBufferException.malformedVarint();
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        i5 = i7;
                    }
                    this.pos = i5;
                    return i;
                }
            } else {
                throw InvalidProtocolBufferException.truncatedMessage();
            }
        }

        public long readVarint64() throws IOException {
            long j;
            long j2;
            long j3;
            int i;
            int i2 = this.pos;
            int i3 = this.limit;
            if (i3 != i2) {
                byte[] bArr = this.buffer;
                int i4 = i2 + 1;
                byte b = bArr[i2];
                if (b >= 0) {
                    this.pos = i4;
                    return (long) b;
                } else if (i3 - i4 < 9) {
                    return readVarint64SlowPath();
                } else {
                    int i5 = i4 + 1;
                    int i6 = b ^ (bArr[i4] << 7);
                    if (i6 < 0) {
                        i = i6 ^ -128;
                    } else {
                        int i7 = i5 + 1;
                        int i8 = i6 ^ (bArr[i5] << 14);
                        if (i8 >= 0) {
                            j = (long) (i8 ^ 16256);
                            i5 = i7;
                        } else {
                            i5 = i7 + 1;
                            int i9 = i8 ^ (bArr[i7] << 21);
                            if (i9 < 0) {
                                i = i9 ^ -2080896;
                            } else {
                                long j4 = (long) i9;
                                int i10 = i5 + 1;
                                long j5 = j4 ^ (((long) bArr[i5]) << 28);
                                if (j5 >= 0) {
                                    j3 = 266354560;
                                } else {
                                    i5 = i10 + 1;
                                    long j6 = j5 ^ (((long) bArr[i10]) << 35);
                                    if (j6 < 0) {
                                        j2 = -34093383808L;
                                    } else {
                                        i10 = i5 + 1;
                                        j5 = j6 ^ (((long) bArr[i5]) << 42);
                                        if (j5 >= 0) {
                                            j3 = 4363953127296L;
                                        } else {
                                            i5 = i10 + 1;
                                            j6 = j5 ^ (((long) bArr[i10]) << 49);
                                            if (j6 < 0) {
                                                j2 = -558586000294016L;
                                            } else {
                                                int i11 = i5 + 1;
                                                long j7 = (j6 ^ (((long) bArr[i5]) << 56)) ^ 71499008037633920L;
                                                if (j7 < 0) {
                                                    i5 = i11 + 1;
                                                    if (((long) bArr[i11]) < 0) {
                                                        throw InvalidProtocolBufferException.malformedVarint();
                                                    }
                                                } else {
                                                    i5 = i11;
                                                }
                                                j = j7;
                                            }
                                        }
                                    }
                                    j = j6 ^ j2;
                                }
                                j = j5 ^ j3;
                                i5 = i10;
                            }
                        }
                        this.pos = i5;
                        return j;
                    }
                    j = (long) i;
                    this.pos = i5;
                    return j;
                }
            } else {
                throw InvalidProtocolBufferException.truncatedMessage();
            }
        }

        private long readVarint64SlowPath() throws IOException {
            long j = 0;
            for (int i = 0; i < 64; i += 7) {
                byte readByte = readByte();
                j |= ((long) (readByte & Byte.MAX_VALUE)) << i;
                if ((readByte & 128) == 0) {
                    return j;
                }
            }
            throw InvalidProtocolBufferException.malformedVarint();
        }

        private byte readByte() throws IOException {
            int i = this.pos;
            if (i != this.limit) {
                byte[] bArr = this.buffer;
                this.pos = i + 1;
                return bArr[i];
            }
            throw InvalidProtocolBufferException.truncatedMessage();
        }

        private int readLittleEndian32() throws IOException {
            requireBytes(4);
            return readLittleEndian32_NoCheck();
        }

        private long readLittleEndian64() throws IOException {
            requireBytes(8);
            return readLittleEndian64_NoCheck();
        }

        private int readLittleEndian32_NoCheck() {
            int i = this.pos;
            byte[] bArr = this.buffer;
            this.pos = i + 4;
            return (bArr[i] & 255) | ((bArr[i + 1] & 255) << 8) | ((bArr[i + 2] & 255) << 16) | ((bArr[i + 3] & 255) << 24);
        }

        private long readLittleEndian64_NoCheck() {
            int i = this.pos;
            byte[] bArr = this.buffer;
            this.pos = i + 8;
            return ((((long) bArr[i + 7]) & 255) << 56) | (((long) bArr[i]) & 255) | ((((long) bArr[i + 1]) & 255) << 8) | ((((long) bArr[i + 2]) & 255) << 16) | ((((long) bArr[i + 3]) & 255) << 24) | ((((long) bArr[i + 4]) & 255) << 32) | ((((long) bArr[i + 5]) & 255) << 40) | ((((long) bArr[i + 6]) & 255) << 48);
        }

        private void skipVarint() throws IOException {
            int i = this.limit;
            int i2 = this.pos;
            if (i - i2 >= 10) {
                byte[] bArr = this.buffer;
                int i3 = 0;
                while (i3 < 10) {
                    int i4 = i2 + 1;
                    if (bArr[i2] >= 0) {
                        this.pos = i4;
                        return;
                    } else {
                        i3++;
                        i2 = i4;
                    }
                }
            }
            skipVarintSlowPath();
        }

        private void skipVarintSlowPath() throws IOException {
            for (int i = 0; i < 10; i++) {
                if (readByte() >= 0) {
                    return;
                }
            }
            throw InvalidProtocolBufferException.malformedVarint();
        }

        private void skipBytes(int i) throws IOException {
            requireBytes(i);
            this.pos += i;
        }

        private void skipGroup() throws IOException {
            int i = this.endGroupTag;
            this.endGroupTag = WireFormat.makeTag(WireFormat.getTagFieldNumber(this.tag), 4);
            while (getFieldNumber() != Integer.MAX_VALUE && skipField()) {
            }
            if (this.tag == this.endGroupTag) {
                this.endGroupTag = i;
                return;
            }
            throw InvalidProtocolBufferException.parseFailure();
        }

        private void requireBytes(int i) throws IOException {
            if (i < 0 || i > this.limit - this.pos) {
                throw InvalidProtocolBufferException.truncatedMessage();
            }
        }

        private void requireWireType(int i) throws IOException {
            if (WireFormat.getTagWireType(this.tag) != i) {
                throw InvalidProtocolBufferException.invalidWireType();
            }
        }

        private void verifyPackedFixed64Length(int i) throws IOException {
            requireBytes(i);
            if ((i & 7) != 0) {
                throw InvalidProtocolBufferException.parseFailure();
            }
        }

        private void verifyPackedFixed32Length(int i) throws IOException {
            requireBytes(i);
            if ((i & 3) != 0) {
                throw InvalidProtocolBufferException.parseFailure();
            }
        }

        private void requirePosition(int i) throws IOException {
            if (this.pos != i) {
                throw InvalidProtocolBufferException.truncatedMessage();
            }
        }
    }
}
