package com.google.protobuf;
/* loaded from: classes.dex */
public interface Parser<MessageType> {
    MessageType parseFrom(byte[] bArr) throws InvalidProtocolBufferException;
}
