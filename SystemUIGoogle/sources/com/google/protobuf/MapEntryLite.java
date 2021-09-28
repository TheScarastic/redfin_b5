package com.google.protobuf;

import java.io.IOException;
/* loaded from: classes2.dex */
public class MapEntryLite<K, V> {
    private final Metadata<K, V> metadata;

    /* loaded from: classes2.dex */
    static class Metadata<K, V> {
    }

    /* access modifiers changed from: package-private */
    public static <K, V> void writeTo(CodedOutputStream codedOutputStream, Metadata<K, V> metadata, K k, V v) throws IOException {
        throw null;
    }

    /* access modifiers changed from: package-private */
    public static <K, V> int computeSerializedSize(Metadata<K, V> metadata, K k, V v) {
        throw null;
    }

    /* access modifiers changed from: package-private */
    public Metadata<K, V> getMetadata() {
        return this.metadata;
    }
}
