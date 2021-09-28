package com.google.protobuf;

import com.google.protobuf.ArrayDecoders;
import com.google.protobuf.GeneratedMessageLite;
import com.google.protobuf.MessageLite;
import java.io.IOException;
/* loaded from: classes.dex */
public abstract class AbstractParser<MessageType extends MessageLite> implements Parser<MessageType> {
    public static final ExtensionRegistryLite EMPTY_REGISTRY = ExtensionRegistryLite.getEmptyRegistry();

    public final MessageType checkMessageInitialized(MessageType messagetype) throws InvalidProtocolBufferException {
        UninitializedMessageException uninitializedMessageException;
        if (messagetype.isInitialized()) {
            return messagetype;
        }
        if (messagetype instanceof AbstractMessageLite) {
            AbstractMessageLite abstractMessageLite = (AbstractMessageLite) messagetype;
            uninitializedMessageException = new UninitializedMessageException();
        } else {
            uninitializedMessageException = new UninitializedMessageException();
        }
        InvalidProtocolBufferException invalidProtocolBufferException = new InvalidProtocolBufferException(uninitializedMessageException.getMessage());
        invalidProtocolBufferException.setUnfinishedMessage(messagetype);
        throw invalidProtocolBufferException;
    }

    @Override // com.google.protobuf.Parser
    public Object parseFrom(byte[] bArr) throws InvalidProtocolBufferException {
        ExtensionRegistryLite extensionRegistryLite = EMPTY_REGISTRY;
        int length = bArr.length;
        GeneratedMessageLite generatedMessageLite = (GeneratedMessageLite) ((GeneratedMessageLite.DefaultInstanceBasedParser) this).defaultInstance.dynamicMethod(GeneratedMessageLite.MethodToInvoke.NEW_MUTABLE_INSTANCE, null, null);
        try {
            Schema schemaFor = Protobuf.INSTANCE.schemaFor((Protobuf) generatedMessageLite);
            schemaFor.mergeFrom(generatedMessageLite, bArr, 0, length + 0, new ArrayDecoders.Registers(extensionRegistryLite));
            schemaFor.makeImmutable(generatedMessageLite);
            if (generatedMessageLite.memoizedHashCode == 0) {
                checkMessageInitialized(generatedMessageLite);
                return generatedMessageLite;
            }
            throw new RuntimeException();
        } catch (IOException e) {
            if (e.getCause() instanceof InvalidProtocolBufferException) {
                throw ((InvalidProtocolBufferException) e.getCause());
            }
            InvalidProtocolBufferException invalidProtocolBufferException = new InvalidProtocolBufferException(e.getMessage());
            invalidProtocolBufferException.setUnfinishedMessage(generatedMessageLite);
            throw invalidProtocolBufferException;
        } catch (IndexOutOfBoundsException unused) {
            InvalidProtocolBufferException truncatedMessage = InvalidProtocolBufferException.truncatedMessage();
            truncatedMessage.setUnfinishedMessage(generatedMessageLite);
            throw truncatedMessage;
        }
    }
}
