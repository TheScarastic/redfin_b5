package com.google.protobuf;
/* loaded from: classes2.dex */
public class LazyFieldLite {
    private static final ExtensionRegistryLite EMPTY_REGISTRY = ExtensionRegistryLite.getEmptyRegistry();
    private ByteString delayedBytes;
    private ExtensionRegistryLite extensionRegistry;
    private volatile ByteString memoizedBytes;
    protected volatile MessageLite value;

    public MessageLite getValue(MessageLite messageLite) {
        ensureInitialized(messageLite);
        return this.value;
    }

    public MessageLite setValue(MessageLite messageLite) {
        MessageLite messageLite2 = this.value;
        this.delayedBytes = null;
        this.memoizedBytes = null;
        this.value = messageLite;
        return messageLite2;
    }

    /* JADX DEBUG: Failed to insert an additional move for type inference into block B:21:0x000c */
    /* JADX DEBUG: Multi-variable search result rejected for r4v4, resolved type: com.google.protobuf.ByteString */
    /* JADX WARN: Multi-variable type inference failed */
    protected void ensureInitialized(MessageLite messageLite) {
        if (this.value == null) {
            synchronized (this) {
                if (this.value == null) {
                    try {
                        if (this.delayedBytes != null) {
                            this.value = (MessageLite) messageLite.getParserForType().parseFrom(this.delayedBytes, this.extensionRegistry);
                            this.memoizedBytes = this.delayedBytes;
                            messageLite = messageLite;
                        } else {
                            this.value = messageLite;
                            this.memoizedBytes = ByteString.EMPTY;
                            messageLite = messageLite;
                        }
                    } catch (InvalidProtocolBufferException unused) {
                        this.value = messageLite;
                        ByteString byteString = ByteString.EMPTY;
                        this.memoizedBytes = byteString;
                        messageLite = byteString;
                    }
                }
            }
        }
    }
}
