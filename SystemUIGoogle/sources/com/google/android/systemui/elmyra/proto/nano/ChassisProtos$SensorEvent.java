package com.google.android.systemui.elmyra.proto.nano;

import com.google.protobuf.nano.CodedInputByteBufferNano;
import com.google.protobuf.nano.CodedOutputByteBufferNano;
import com.google.protobuf.nano.MessageNano;
import com.google.protobuf.nano.WireFormatNano;
import java.io.IOException;
/* loaded from: classes2.dex */
public final class ChassisProtos$SensorEvent extends MessageNano {
    public long timestamp;
    public float[] values;

    public ChassisProtos$SensorEvent() {
        clear();
    }

    public ChassisProtos$SensorEvent clear() {
        this.timestamp = 0;
        this.values = WireFormatNano.EMPTY_FLOAT_ARRAY;
        this.cachedSize = -1;
        return this;
    }

    @Override // com.google.protobuf.nano.MessageNano
    public void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
        long j = this.timestamp;
        if (j != 0) {
            codedOutputByteBufferNano.writeUInt64(1, j);
        }
        float[] fArr = this.values;
        if (fArr != null && fArr.length > 0) {
            int i = 0;
            while (true) {
                float[] fArr2 = this.values;
                if (i >= fArr2.length) {
                    break;
                }
                codedOutputByteBufferNano.writeFloat(2, fArr2[i]);
                i++;
            }
        }
        super.writeTo(codedOutputByteBufferNano);
    }

    /* access modifiers changed from: protected */
    @Override // com.google.protobuf.nano.MessageNano
    public int computeSerializedSize() {
        int computeSerializedSize = super.computeSerializedSize();
        long j = this.timestamp;
        if (j != 0) {
            computeSerializedSize += CodedOutputByteBufferNano.computeUInt64Size(1, j);
        }
        float[] fArr = this.values;
        return (fArr == null || fArr.length <= 0) ? computeSerializedSize : computeSerializedSize + (fArr.length * 4) + (fArr.length * 1);
    }

    @Override // com.google.protobuf.nano.MessageNano
    public ChassisProtos$SensorEvent mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        while (true) {
            int readTag = codedInputByteBufferNano.readTag();
            if (readTag == 0) {
                return this;
            }
            if (readTag == 8) {
                this.timestamp = codedInputByteBufferNano.readUInt64();
            } else if (readTag == 18) {
                int readRawVarint32 = codedInputByteBufferNano.readRawVarint32();
                int pushLimit = codedInputByteBufferNano.pushLimit(readRawVarint32);
                int i = readRawVarint32 / 4;
                float[] fArr = this.values;
                int length = fArr == null ? 0 : fArr.length;
                int i2 = i + length;
                float[] fArr2 = new float[i2];
                if (length != 0) {
                    System.arraycopy(fArr, 0, fArr2, 0, length);
                }
                while (length < i2) {
                    fArr2[length] = codedInputByteBufferNano.readFloat();
                    length++;
                }
                this.values = fArr2;
                codedInputByteBufferNano.popLimit(pushLimit);
            } else if (readTag == 21) {
                int repeatedFieldArrayLength = WireFormatNano.getRepeatedFieldArrayLength(codedInputByteBufferNano, 21);
                float[] fArr3 = this.values;
                int length2 = fArr3 == null ? 0 : fArr3.length;
                int i3 = repeatedFieldArrayLength + length2;
                float[] fArr4 = new float[i3];
                if (length2 != 0) {
                    System.arraycopy(fArr3, 0, fArr4, 0, length2);
                }
                while (length2 < i3 - 1) {
                    fArr4[length2] = codedInputByteBufferNano.readFloat();
                    codedInputByteBufferNano.readTag();
                    length2++;
                }
                fArr4[length2] = codedInputByteBufferNano.readFloat();
                this.values = fArr4;
            } else if (!WireFormatNano.parseUnknownField(codedInputByteBufferNano, readTag)) {
                return this;
            }
        }
    }
}
