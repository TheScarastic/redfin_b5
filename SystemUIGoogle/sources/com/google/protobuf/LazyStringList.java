package com.google.protobuf;

import java.util.List;
/* loaded from: classes2.dex */
public interface LazyStringList extends List {
    void add(ByteString byteString);

    List<?> getUnderlyingElements();

    LazyStringList getUnmodifiableView();
}
