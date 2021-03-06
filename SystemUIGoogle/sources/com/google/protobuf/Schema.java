package com.google.protobuf;

import java.io.IOException;
/* access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public interface Schema<T> {
    boolean equals(T t, T t2);

    int hashCode(T t);

    boolean isInitialized(T t);

    void makeImmutable(T t);

    void mergeFrom(T t, Reader reader, ExtensionRegistryLite extensionRegistryLite) throws IOException;

    void mergeFrom(T t, T t2);

    T newInstance();
}
