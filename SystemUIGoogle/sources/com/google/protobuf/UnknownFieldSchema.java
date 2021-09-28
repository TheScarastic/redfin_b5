package com.google.protobuf;

import java.io.IOException;
/* access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public abstract class UnknownFieldSchema<T, B> {
    abstract void addFixed32(B b, int i, int i2);

    abstract void addFixed64(B b, int i, long j);

    abstract void addGroup(B b, int i, T t);

    /* access modifiers changed from: package-private */
    public abstract void addLengthDelimited(B b, int i, ByteString byteString);

    /* access modifiers changed from: package-private */
    public abstract void addVarint(B b, int i, long j);

    /* access modifiers changed from: package-private */
    public abstract B getBuilderFromMessage(Object obj);

    /* access modifiers changed from: package-private */
    public abstract T getFromMessage(Object obj);

    /* access modifiers changed from: package-private */
    public abstract void makeImmutable(Object obj);

    /* access modifiers changed from: package-private */
    public abstract T merge(T t, T t2);

    /* access modifiers changed from: package-private */
    public abstract B newBuilder();

    /* access modifiers changed from: package-private */
    public abstract void setBuilderToMessage(Object obj, B b);

    /* access modifiers changed from: package-private */
    public abstract void setToMessage(Object obj, T t);

    /* access modifiers changed from: package-private */
    public abstract boolean shouldDiscardUnknownFields(Reader reader);

    abstract T toImmutable(B b);

    /* access modifiers changed from: package-private */
    public final boolean mergeOneFieldFrom(B b, Reader reader) throws IOException {
        int tag = reader.getTag();
        int tagFieldNumber = WireFormat.getTagFieldNumber(tag);
        int tagWireType = WireFormat.getTagWireType(tag);
        if (tagWireType == 0) {
            addVarint(b, tagFieldNumber, reader.readInt64());
            return true;
        } else if (tagWireType == 1) {
            addFixed64(b, tagFieldNumber, reader.readFixed64());
            return true;
        } else if (tagWireType == 2) {
            addLengthDelimited(b, tagFieldNumber, reader.readBytes());
            return true;
        } else if (tagWireType == 3) {
            B newBuilder = newBuilder();
            int makeTag = WireFormat.makeTag(tagFieldNumber, 4);
            mergeFrom(newBuilder, reader);
            if (makeTag == reader.getTag()) {
                addGroup(b, tagFieldNumber, toImmutable(newBuilder));
                return true;
            }
            throw InvalidProtocolBufferException.invalidEndTag();
        } else if (tagWireType == 4) {
            return false;
        } else {
            if (tagWireType == 5) {
                addFixed32(b, tagFieldNumber, reader.readFixed32());
                return true;
            }
            throw InvalidProtocolBufferException.invalidWireType();
        }
    }

    final void mergeFrom(B b, Reader reader) throws IOException {
        while (reader.getFieldNumber() != Integer.MAX_VALUE && mergeOneFieldFrom(b, reader)) {
        }
    }
}
