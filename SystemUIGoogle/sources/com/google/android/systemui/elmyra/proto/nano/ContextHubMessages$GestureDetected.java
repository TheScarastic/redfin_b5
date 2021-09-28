package com.google.android.systemui.elmyra.proto.nano;

import com.google.protobuf.nano.CodedInputByteBufferNano;
import com.google.protobuf.nano.CodedOutputByteBufferNano;
import com.google.protobuf.nano.InvalidProtocolBufferNanoException;
import com.google.protobuf.nano.MessageNano;
import com.google.protobuf.nano.WireFormatNano;
import java.io.IOException;
/* loaded from: classes2.dex */
public final class ContextHubMessages$GestureDetected extends MessageNano {
    public boolean hapticConsumed;
    public boolean hostSuspended;

    public ContextHubMessages$GestureDetected() {
        clear();
    }

    public ContextHubMessages$GestureDetected clear() {
        this.hostSuspended = false;
        this.hapticConsumed = false;
        this.cachedSize = -1;
        return this;
    }

    @Override // com.google.protobuf.nano.MessageNano
    public void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
        boolean z = this.hostSuspended;
        if (z) {
            codedOutputByteBufferNano.writeBool(1, z);
        }
        boolean z2 = this.hapticConsumed;
        if (z2) {
            codedOutputByteBufferNano.writeBool(2, z2);
        }
        super.writeTo(codedOutputByteBufferNano);
    }

    /* access modifiers changed from: protected */
    @Override // com.google.protobuf.nano.MessageNano
    public int computeSerializedSize() {
        int computeSerializedSize = super.computeSerializedSize();
        boolean z = this.hostSuspended;
        if (z) {
            computeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(1, z);
        }
        boolean z2 = this.hapticConsumed;
        return z2 ? computeSerializedSize + CodedOutputByteBufferNano.computeBoolSize(2, z2) : computeSerializedSize;
    }

    @Override // com.google.protobuf.nano.MessageNano
    public ContextHubMessages$GestureDetected mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        while (true) {
            int readTag = codedInputByteBufferNano.readTag();
            if (readTag == 0) {
                return this;
            }
            if (readTag == 8) {
                this.hostSuspended = codedInputByteBufferNano.readBool();
            } else if (readTag == 16) {
                this.hapticConsumed = codedInputByteBufferNano.readBool();
            } else if (!WireFormatNano.parseUnknownField(codedInputByteBufferNano, readTag)) {
                return this;
            }
        }
    }

    public static ContextHubMessages$GestureDetected parseFrom(byte[] bArr) throws InvalidProtocolBufferNanoException {
        return (ContextHubMessages$GestureDetected) MessageNano.mergeFrom(new ContextHubMessages$GestureDetected(), bArr);
    }
}
