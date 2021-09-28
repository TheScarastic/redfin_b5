package com.google.protobuf;

import java.io.IOException;
/* loaded from: classes.dex */
public abstract class UnknownFieldSchema<T, B> {
    public abstract void addVarint(B b, int i, long j);

    public abstract T getFromMessage(Object obj);

    public abstract int getSerializedSize(T t);

    public abstract int getSerializedSizeAsMessageSet(T t);

    public abstract void makeImmutable(Object obj);

    public abstract T merge(T t, T t2);

    public abstract B newBuilder();

    public abstract void setToMessage(Object obj, T t);

    public abstract void writeAsMessageSetTo(T t, Writer writer) throws IOException;

    public abstract void writeTo(T t, Writer writer) throws IOException;
}
