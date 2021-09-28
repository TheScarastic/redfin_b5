package com.google.protobuf;
/* access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public interface SchemaFactory {
    <T> Schema<T> createSchema(Class<T> cls);
}
