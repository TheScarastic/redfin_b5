package com.google.android.systemui.autorotate.proto.nano;

import com.google.protobuf.nano.CodedInputByteBufferNano;
import com.google.protobuf.nano.CodedOutputByteBufferNano;
import com.google.protobuf.nano.MessageNano;
import com.google.protobuf.nano.WireFormatNano;
import java.io.IOException;
/* loaded from: classes2.dex */
public final class AutorotateProto$DeviceRotatedSensorHeader extends MessageNano {
    public long timestampBase;

    public AutorotateProto$DeviceRotatedSensorHeader() {
        clear();
    }

    public AutorotateProto$DeviceRotatedSensorHeader clear() {
        this.timestampBase = 0;
        this.cachedSize = -1;
        return this;
    }

    @Override // com.google.protobuf.nano.MessageNano
    public void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
        long j = this.timestampBase;
        if (j != 0) {
            codedOutputByteBufferNano.writeInt64(1, j);
        }
        super.writeTo(codedOutputByteBufferNano);
    }

    /* access modifiers changed from: protected */
    @Override // com.google.protobuf.nano.MessageNano
    public int computeSerializedSize() {
        int computeSerializedSize = super.computeSerializedSize();
        long j = this.timestampBase;
        return j != 0 ? computeSerializedSize + CodedOutputByteBufferNano.computeInt64Size(1, j) : computeSerializedSize;
    }

    @Override // com.google.protobuf.nano.MessageNano
    public AutorotateProto$DeviceRotatedSensorHeader mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        while (true) {
            int readTag = codedInputByteBufferNano.readTag();
            if (readTag == 0) {
                return this;
            }
            if (readTag == 8) {
                this.timestampBase = codedInputByteBufferNano.readInt64();
            } else if (!WireFormatNano.parseUnknownField(codedInputByteBufferNano, readTag)) {
                return this;
            }
        }
    }
}
