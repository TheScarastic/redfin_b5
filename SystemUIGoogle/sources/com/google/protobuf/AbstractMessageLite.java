package com.google.protobuf;

import com.google.protobuf.AbstractMessageLite;
import com.google.protobuf.AbstractMessageLite.Builder;
import com.google.protobuf.MessageLite;
/* loaded from: classes2.dex */
public abstract class AbstractMessageLite<MessageType extends AbstractMessageLite<MessageType, BuilderType>, BuilderType extends Builder<MessageType, BuilderType>> implements MessageLite {
    protected int memoizedHashCode = 0;

    /* access modifiers changed from: package-private */
    public UninitializedMessageException newUninitializedMessageException() {
        return new UninitializedMessageException(this);
    }

    /* loaded from: classes2.dex */
    public static abstract class Builder<MessageType extends AbstractMessageLite<MessageType, BuilderType>, BuilderType extends Builder<MessageType, BuilderType>> implements MessageLite.Builder {
        protected abstract BuilderType internalMergeFrom(MessageType messagetype);

        /* JADX DEBUG: Multi-variable search result rejected for r1v0, resolved type: com.google.protobuf.AbstractMessageLite$Builder<MessageType extends com.google.protobuf.AbstractMessageLite<MessageType, BuilderType>, BuilderType extends com.google.protobuf.AbstractMessageLite$Builder<MessageType, BuilderType>> */
        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.protobuf.MessageLite.Builder
        public BuilderType mergeFrom(MessageLite messageLite) {
            if (getDefaultInstanceForType().getClass().isInstance(messageLite)) {
                return (BuilderType) internalMergeFrom((AbstractMessageLite) messageLite);
            }
            throw new IllegalArgumentException("mergeFrom(MessageLite) can only merge messages of the same type.");
        }

        /* access modifiers changed from: protected */
        public static UninitializedMessageException newUninitializedMessageException(MessageLite messageLite) {
            return new UninitializedMessageException(messageLite);
        }
    }
}
