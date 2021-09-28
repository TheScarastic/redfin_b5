package com.google.android.systemui.elmyra.proto.nano;

import com.google.protobuf.nano.CodedInputByteBufferNano;
import com.google.protobuf.nano.CodedOutputByteBufferNano;
import com.google.protobuf.nano.InternalNano;
import com.google.protobuf.nano.InvalidProtocolBufferNanoException;
import com.google.protobuf.nano.MessageNano;
import com.google.protobuf.nano.WireFormatNano;
import java.io.IOException;
/* loaded from: classes2.dex */
public final class SnapshotProtos$Snapshot extends MessageNano {
    private static volatile SnapshotProtos$Snapshot[] _emptyArray;
    public SnapshotProtos$Event[] events;
    public SnapshotProtos$SnapshotHeader header;
    public float sensitivitySetting;

    public static SnapshotProtos$Snapshot[] emptyArray() {
        if (_emptyArray == null) {
            synchronized (InternalNano.LAZY_INIT_LOCK) {
                if (_emptyArray == null) {
                    _emptyArray = new SnapshotProtos$Snapshot[0];
                }
            }
        }
        return _emptyArray;
    }

    public SnapshotProtos$Snapshot() {
        clear();
    }

    public SnapshotProtos$Snapshot clear() {
        this.header = null;
        this.events = SnapshotProtos$Event.emptyArray();
        this.sensitivitySetting = 0.0f;
        this.cachedSize = -1;
        return this;
    }

    @Override // com.google.protobuf.nano.MessageNano
    public void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
        SnapshotProtos$SnapshotHeader snapshotProtos$SnapshotHeader = this.header;
        if (snapshotProtos$SnapshotHeader != null) {
            codedOutputByteBufferNano.writeMessage(1, snapshotProtos$SnapshotHeader);
        }
        SnapshotProtos$Event[] snapshotProtos$EventArr = this.events;
        if (snapshotProtos$EventArr != null && snapshotProtos$EventArr.length > 0) {
            int i = 0;
            while (true) {
                SnapshotProtos$Event[] snapshotProtos$EventArr2 = this.events;
                if (i >= snapshotProtos$EventArr2.length) {
                    break;
                }
                SnapshotProtos$Event snapshotProtos$Event = snapshotProtos$EventArr2[i];
                if (snapshotProtos$Event != null) {
                    codedOutputByteBufferNano.writeMessage(2, snapshotProtos$Event);
                }
                i++;
            }
        }
        if (Float.floatToIntBits(this.sensitivitySetting) != Float.floatToIntBits(0.0f)) {
            codedOutputByteBufferNano.writeFloat(3, this.sensitivitySetting);
        }
        super.writeTo(codedOutputByteBufferNano);
    }

    /* access modifiers changed from: protected */
    @Override // com.google.protobuf.nano.MessageNano
    public int computeSerializedSize() {
        int computeSerializedSize = super.computeSerializedSize();
        SnapshotProtos$SnapshotHeader snapshotProtos$SnapshotHeader = this.header;
        if (snapshotProtos$SnapshotHeader != null) {
            computeSerializedSize += CodedOutputByteBufferNano.computeMessageSize(1, snapshotProtos$SnapshotHeader);
        }
        SnapshotProtos$Event[] snapshotProtos$EventArr = this.events;
        if (snapshotProtos$EventArr != null && snapshotProtos$EventArr.length > 0) {
            int i = 0;
            while (true) {
                SnapshotProtos$Event[] snapshotProtos$EventArr2 = this.events;
                if (i >= snapshotProtos$EventArr2.length) {
                    break;
                }
                SnapshotProtos$Event snapshotProtos$Event = snapshotProtos$EventArr2[i];
                if (snapshotProtos$Event != null) {
                    computeSerializedSize += CodedOutputByteBufferNano.computeMessageSize(2, snapshotProtos$Event);
                }
                i++;
            }
        }
        return Float.floatToIntBits(this.sensitivitySetting) != Float.floatToIntBits(0.0f) ? computeSerializedSize + CodedOutputByteBufferNano.computeFloatSize(3, this.sensitivitySetting) : computeSerializedSize;
    }

    @Override // com.google.protobuf.nano.MessageNano
    public SnapshotProtos$Snapshot mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        while (true) {
            int readTag = codedInputByteBufferNano.readTag();
            if (readTag == 0) {
                return this;
            }
            if (readTag == 10) {
                if (this.header == null) {
                    this.header = new SnapshotProtos$SnapshotHeader();
                }
                codedInputByteBufferNano.readMessage(this.header);
            } else if (readTag == 18) {
                int repeatedFieldArrayLength = WireFormatNano.getRepeatedFieldArrayLength(codedInputByteBufferNano, 18);
                SnapshotProtos$Event[] snapshotProtos$EventArr = this.events;
                int length = snapshotProtos$EventArr == null ? 0 : snapshotProtos$EventArr.length;
                int i = repeatedFieldArrayLength + length;
                SnapshotProtos$Event[] snapshotProtos$EventArr2 = new SnapshotProtos$Event[i];
                if (length != 0) {
                    System.arraycopy(snapshotProtos$EventArr, 0, snapshotProtos$EventArr2, 0, length);
                }
                while (length < i - 1) {
                    snapshotProtos$EventArr2[length] = new SnapshotProtos$Event();
                    codedInputByteBufferNano.readMessage(snapshotProtos$EventArr2[length]);
                    codedInputByteBufferNano.readTag();
                    length++;
                }
                snapshotProtos$EventArr2[length] = new SnapshotProtos$Event();
                codedInputByteBufferNano.readMessage(snapshotProtos$EventArr2[length]);
                this.events = snapshotProtos$EventArr2;
            } else if (readTag == 29) {
                this.sensitivitySetting = codedInputByteBufferNano.readFloat();
            } else if (!WireFormatNano.parseUnknownField(codedInputByteBufferNano, readTag)) {
                return this;
            }
        }
    }

    public static SnapshotProtos$Snapshot parseFrom(byte[] bArr) throws InvalidProtocolBufferNanoException {
        return (SnapshotProtos$Snapshot) MessageNano.mergeFrom(new SnapshotProtos$Snapshot(), bArr);
    }
}
