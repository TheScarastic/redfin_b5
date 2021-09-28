package com.google.protobuf;

import com.google.protobuf.AbstractMessageLite;
import com.google.protobuf.GeneratedMessageLite;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Objects;
import java.util.RandomAccess;
/* loaded from: classes.dex */
public final class Internal {
    public static final byte[] EMPTY_BYTE_ARRAY;
    public static final Charset UTF_8 = Charset.forName("UTF-8");

    /* loaded from: classes.dex */
    public interface EnumLite {
        int getNumber();
    }

    /* loaded from: classes.dex */
    public interface EnumVerifier {
        boolean isInRange(int i);
    }

    /* loaded from: classes.dex */
    public interface ProtobufList<E> extends List<E>, RandomAccess {
        boolean isModifiable();

        void makeImmutable();

        ProtobufList<E> mutableCopyWithCapacity(int i);
    }

    static {
        Charset.forName("ISO-8859-1");
        byte[] bArr = new byte[0];
        EMPTY_BYTE_ARRAY = bArr;
        ByteBuffer.wrap(bArr);
        CodedInputStream.newInstance(bArr, 0, bArr.length, false);
    }

    public static int hashBoolean(boolean z) {
        return z ? 1231 : 1237;
    }

    public static int hashLong(long j) {
        return (int) (j ^ (j >>> 32));
    }

    public static Object mergeMessage(Object obj, Object obj2) {
        MessageLite messageLite = (MessageLite) obj2;
        AbstractMessageLite.Builder builder = (AbstractMessageLite.Builder) ((MessageLite) obj).toBuilder();
        Objects.requireNonNull(builder);
        GeneratedMessageLite.Builder builder2 = (GeneratedMessageLite.Builder) builder;
        if (builder2.defaultInstance.getClass().isInstance(messageLite)) {
            builder2.copyOnWrite();
            builder2.mergeFromInstance(builder2.instance, (GeneratedMessageLite) ((AbstractMessageLite) messageLite));
            return builder2.buildPartial();
        }
        throw new IllegalArgumentException("mergeFrom(MessageLite) can only merge messages of the same type.");
    }
}
