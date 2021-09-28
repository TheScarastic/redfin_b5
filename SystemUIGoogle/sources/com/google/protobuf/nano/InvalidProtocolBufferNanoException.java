package com.google.protobuf.nano;

import java.io.IOException;
/* loaded from: classes2.dex */
public class InvalidProtocolBufferNanoException extends IOException {
    private static final long serialVersionUID = -1616151763072450476L;

    public InvalidProtocolBufferNanoException(String str) {
        super(str);
    }

    /* access modifiers changed from: package-private */
    public static InvalidProtocolBufferNanoException truncatedMessage() {
        return new InvalidProtocolBufferNanoException("While parsing a protocol message, the input ended unexpectedly in the middle of a field.  This could mean either that the input has been truncated or that an embedded message misreported its own length.");
    }

    /* access modifiers changed from: package-private */
    public static InvalidProtocolBufferNanoException negativeSize() {
        return new InvalidProtocolBufferNanoException("CodedInputStream encountered an embedded string or message which claimed to have negative size.");
    }

    /* access modifiers changed from: package-private */
    public static InvalidProtocolBufferNanoException malformedVarint() {
        return new InvalidProtocolBufferNanoException("CodedInputStream encountered a malformed varint.");
    }

    /* access modifiers changed from: package-private */
    public static InvalidProtocolBufferNanoException invalidTag() {
        return new InvalidProtocolBufferNanoException("Protocol message contained an invalid tag (zero).");
    }

    /* access modifiers changed from: package-private */
    public static InvalidProtocolBufferNanoException invalidEndTag() {
        return new InvalidProtocolBufferNanoException("Protocol message end-group tag did not match expected tag.");
    }

    /* access modifiers changed from: package-private */
    public static InvalidProtocolBufferNanoException invalidWireType() {
        return new InvalidProtocolBufferNanoException("Protocol message tag had invalid wire type.");
    }

    /* access modifiers changed from: package-private */
    public static InvalidProtocolBufferNanoException recursionLimitExceeded() {
        return new InvalidProtocolBufferNanoException("Protocol message had too many levels of nesting.  May be malicious.  Use CodedInputStream.setRecursionLimit() to increase the depth limit.");
    }
}
