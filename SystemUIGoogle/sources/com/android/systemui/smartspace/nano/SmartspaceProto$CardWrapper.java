package com.android.systemui.smartspace.nano;

import com.android.systemui.smartspace.nano.SmartspaceProto$SmartspaceUpdate;
import com.google.protobuf.nano.CodedInputByteBufferNano;
import com.google.protobuf.nano.CodedOutputByteBufferNano;
import com.google.protobuf.nano.MessageNano;
import com.google.protobuf.nano.WireFormatNano;
import java.io.IOException;
import java.util.Arrays;
/* loaded from: classes.dex */
public final class SmartspaceProto$CardWrapper extends MessageNano {
    public SmartspaceProto$SmartspaceUpdate.SmartspaceCard card;
    public long gsaUpdateTime;
    public int gsaVersionCode;
    public byte[] icon;
    public boolean isIconGrayscale;
    public long publishTime;

    public SmartspaceProto$CardWrapper() {
        clear();
    }

    public SmartspaceProto$CardWrapper clear() {
        this.card = null;
        this.publishTime = 0;
        this.gsaUpdateTime = 0;
        this.gsaVersionCode = 0;
        this.icon = WireFormatNano.EMPTY_BYTES;
        this.isIconGrayscale = false;
        this.cachedSize = -1;
        return this;
    }

    @Override // com.google.protobuf.nano.MessageNano
    public void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
        SmartspaceProto$SmartspaceUpdate.SmartspaceCard smartspaceCard = this.card;
        if (smartspaceCard != null) {
            codedOutputByteBufferNano.writeMessage(1, smartspaceCard);
        }
        long j = this.publishTime;
        if (j != 0) {
            codedOutputByteBufferNano.writeInt64(2, j);
        }
        long j2 = this.gsaUpdateTime;
        if (j2 != 0) {
            codedOutputByteBufferNano.writeInt64(3, j2);
        }
        int i = this.gsaVersionCode;
        if (i != 0) {
            codedOutputByteBufferNano.writeInt32(4, i);
        }
        if (!Arrays.equals(this.icon, WireFormatNano.EMPTY_BYTES)) {
            codedOutputByteBufferNano.writeBytes(5, this.icon);
        }
        boolean z = this.isIconGrayscale;
        if (z) {
            codedOutputByteBufferNano.writeBool(6, z);
        }
        super.writeTo(codedOutputByteBufferNano);
    }

    /* access modifiers changed from: protected */
    @Override // com.google.protobuf.nano.MessageNano
    public int computeSerializedSize() {
        int computeSerializedSize = super.computeSerializedSize();
        SmartspaceProto$SmartspaceUpdate.SmartspaceCard smartspaceCard = this.card;
        if (smartspaceCard != null) {
            computeSerializedSize += CodedOutputByteBufferNano.computeMessageSize(1, smartspaceCard);
        }
        long j = this.publishTime;
        if (j != 0) {
            computeSerializedSize += CodedOutputByteBufferNano.computeInt64Size(2, j);
        }
        long j2 = this.gsaUpdateTime;
        if (j2 != 0) {
            computeSerializedSize += CodedOutputByteBufferNano.computeInt64Size(3, j2);
        }
        int i = this.gsaVersionCode;
        if (i != 0) {
            computeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(4, i);
        }
        if (!Arrays.equals(this.icon, WireFormatNano.EMPTY_BYTES)) {
            computeSerializedSize += CodedOutputByteBufferNano.computeBytesSize(5, this.icon);
        }
        boolean z = this.isIconGrayscale;
        return z ? computeSerializedSize + CodedOutputByteBufferNano.computeBoolSize(6, z) : computeSerializedSize;
    }

    @Override // com.google.protobuf.nano.MessageNano
    public SmartspaceProto$CardWrapper mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        while (true) {
            int readTag = codedInputByteBufferNano.readTag();
            if (readTag == 0) {
                return this;
            }
            if (readTag == 10) {
                if (this.card == null) {
                    this.card = new SmartspaceProto$SmartspaceUpdate.SmartspaceCard();
                }
                codedInputByteBufferNano.readMessage(this.card);
            } else if (readTag == 16) {
                this.publishTime = codedInputByteBufferNano.readInt64();
            } else if (readTag == 24) {
                this.gsaUpdateTime = codedInputByteBufferNano.readInt64();
            } else if (readTag == 32) {
                this.gsaVersionCode = codedInputByteBufferNano.readInt32();
            } else if (readTag == 42) {
                this.icon = codedInputByteBufferNano.readBytes();
            } else if (readTag == 48) {
                this.isIconGrayscale = codedInputByteBufferNano.readBool();
            } else if (!WireFormatNano.parseUnknownField(codedInputByteBufferNano, readTag)) {
                return this;
            }
        }
    }
}
