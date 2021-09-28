package com.google.protobuf;
/* loaded from: classes2.dex */
public interface MessageLite extends MessageLiteOrBuilder {

    /* loaded from: classes2.dex */
    public interface Builder extends MessageLiteOrBuilder, Cloneable {
        MessageLite build();

        MessageLite buildPartial();

        Builder mergeFrom(MessageLite messageLite);
    }

    Parser<? extends MessageLite> getParserForType();

    Builder newBuilderForType();

    Builder toBuilder();
}
