package com.google.android.systemui.columbus.proto.nano;

import com.google.protobuf.nano.CodedInputByteBufferNano;
import com.google.protobuf.nano.CodedOutputByteBufferNano;
import com.google.protobuf.nano.InvalidProtocolBufferNanoException;
import com.google.protobuf.nano.MessageNano;
import com.google.protobuf.nano.WireFormatNano;
import java.io.IOException;
/* loaded from: classes2.dex */
public final class ColumbusProto$NanoappEvents extends MessageNano {
    public ColumbusProto$NanoappEvent[] batchedEvents;

    public ColumbusProto$NanoappEvents() {
        clear();
    }

    public ColumbusProto$NanoappEvents clear() {
        this.batchedEvents = ColumbusProto$NanoappEvent.emptyArray();
        this.cachedSize = -1;
        return this;
    }

    @Override // com.google.protobuf.nano.MessageNano
    public void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
        ColumbusProto$NanoappEvent[] columbusProto$NanoappEventArr = this.batchedEvents;
        if (columbusProto$NanoappEventArr != null && columbusProto$NanoappEventArr.length > 0) {
            int i = 0;
            while (true) {
                ColumbusProto$NanoappEvent[] columbusProto$NanoappEventArr2 = this.batchedEvents;
                if (i >= columbusProto$NanoappEventArr2.length) {
                    break;
                }
                ColumbusProto$NanoappEvent columbusProto$NanoappEvent = columbusProto$NanoappEventArr2[i];
                if (columbusProto$NanoappEvent != null) {
                    codedOutputByteBufferNano.writeMessage(1, columbusProto$NanoappEvent);
                }
                i++;
            }
        }
        super.writeTo(codedOutputByteBufferNano);
    }

    /* access modifiers changed from: protected */
    @Override // com.google.protobuf.nano.MessageNano
    public int computeSerializedSize() {
        int computeSerializedSize = super.computeSerializedSize();
        ColumbusProto$NanoappEvent[] columbusProto$NanoappEventArr = this.batchedEvents;
        if (columbusProto$NanoappEventArr != null && columbusProto$NanoappEventArr.length > 0) {
            int i = 0;
            while (true) {
                ColumbusProto$NanoappEvent[] columbusProto$NanoappEventArr2 = this.batchedEvents;
                if (i >= columbusProto$NanoappEventArr2.length) {
                    break;
                }
                ColumbusProto$NanoappEvent columbusProto$NanoappEvent = columbusProto$NanoappEventArr2[i];
                if (columbusProto$NanoappEvent != null) {
                    computeSerializedSize += CodedOutputByteBufferNano.computeMessageSize(1, columbusProto$NanoappEvent);
                }
                i++;
            }
        }
        return computeSerializedSize;
    }

    @Override // com.google.protobuf.nano.MessageNano
    public ColumbusProto$NanoappEvents mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        while (true) {
            int readTag = codedInputByteBufferNano.readTag();
            if (readTag == 0) {
                return this;
            }
            if (readTag == 10) {
                int repeatedFieldArrayLength = WireFormatNano.getRepeatedFieldArrayLength(codedInputByteBufferNano, 10);
                ColumbusProto$NanoappEvent[] columbusProto$NanoappEventArr = this.batchedEvents;
                int length = columbusProto$NanoappEventArr == null ? 0 : columbusProto$NanoappEventArr.length;
                int i = repeatedFieldArrayLength + length;
                ColumbusProto$NanoappEvent[] columbusProto$NanoappEventArr2 = new ColumbusProto$NanoappEvent[i];
                if (length != 0) {
                    System.arraycopy(columbusProto$NanoappEventArr, 0, columbusProto$NanoappEventArr2, 0, length);
                }
                while (length < i - 1) {
                    columbusProto$NanoappEventArr2[length] = new ColumbusProto$NanoappEvent();
                    codedInputByteBufferNano.readMessage(columbusProto$NanoappEventArr2[length]);
                    codedInputByteBufferNano.readTag();
                    length++;
                }
                columbusProto$NanoappEventArr2[length] = new ColumbusProto$NanoappEvent();
                codedInputByteBufferNano.readMessage(columbusProto$NanoappEventArr2[length]);
                this.batchedEvents = columbusProto$NanoappEventArr2;
            } else if (!WireFormatNano.parseUnknownField(codedInputByteBufferNano, readTag)) {
                return this;
            }
        }
    }

    public static ColumbusProto$NanoappEvents parseFrom(byte[] bArr) throws InvalidProtocolBufferNanoException {
        return (ColumbusProto$NanoappEvents) MessageNano.mergeFrom(new ColumbusProto$NanoappEvents(), bArr);
    }
}
