package com.google.android.systemui.elmyra.proto.nano;

import com.google.protobuf.nano.CodedInputByteBufferNano;
import com.google.protobuf.nano.CodedOutputByteBufferNano;
import com.google.protobuf.nano.InternalNano;
import com.google.protobuf.nano.MessageNano;
import com.google.protobuf.nano.WireFormatNano;
import java.io.IOException;
/* loaded from: classes2.dex */
public final class ChassisProtos$Sensor extends MessageNano {
    private static volatile ChassisProtos$Sensor[] _emptyArray;
    public ElmyraFilters$Filter[] filters;
    public int gain;
    public float sensitivity;
    public int source;

    public static ChassisProtos$Sensor[] emptyArray() {
        if (_emptyArray == null) {
            synchronized (InternalNano.LAZY_INIT_LOCK) {
                if (_emptyArray == null) {
                    _emptyArray = new ChassisProtos$Sensor[0];
                }
            }
        }
        return _emptyArray;
    }

    public ChassisProtos$Sensor() {
        clear();
    }

    public ChassisProtos$Sensor clear() {
        this.source = 0;
        this.gain = 0;
        this.sensitivity = 0.0f;
        this.filters = ElmyraFilters$Filter.emptyArray();
        this.cachedSize = -1;
        return this;
    }

    @Override // com.google.protobuf.nano.MessageNano
    public void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
        int i = this.source;
        if (i != 0) {
            codedOutputByteBufferNano.writeUInt32(1, i);
        }
        int i2 = this.gain;
        if (i2 != 0) {
            codedOutputByteBufferNano.writeInt32(2, i2);
        }
        if (Float.floatToIntBits(this.sensitivity) != Float.floatToIntBits(0.0f)) {
            codedOutputByteBufferNano.writeFloat(3, this.sensitivity);
        }
        ElmyraFilters$Filter[] elmyraFilters$FilterArr = this.filters;
        if (elmyraFilters$FilterArr != null && elmyraFilters$FilterArr.length > 0) {
            int i3 = 0;
            while (true) {
                ElmyraFilters$Filter[] elmyraFilters$FilterArr2 = this.filters;
                if (i3 >= elmyraFilters$FilterArr2.length) {
                    break;
                }
                ElmyraFilters$Filter elmyraFilters$Filter = elmyraFilters$FilterArr2[i3];
                if (elmyraFilters$Filter != null) {
                    codedOutputByteBufferNano.writeMessage(4, elmyraFilters$Filter);
                }
                i3++;
            }
        }
        super.writeTo(codedOutputByteBufferNano);
    }

    /* access modifiers changed from: protected */
    @Override // com.google.protobuf.nano.MessageNano
    public int computeSerializedSize() {
        int computeSerializedSize = super.computeSerializedSize();
        int i = this.source;
        if (i != 0) {
            computeSerializedSize += CodedOutputByteBufferNano.computeUInt32Size(1, i);
        }
        int i2 = this.gain;
        if (i2 != 0) {
            computeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(2, i2);
        }
        if (Float.floatToIntBits(this.sensitivity) != Float.floatToIntBits(0.0f)) {
            computeSerializedSize += CodedOutputByteBufferNano.computeFloatSize(3, this.sensitivity);
        }
        ElmyraFilters$Filter[] elmyraFilters$FilterArr = this.filters;
        if (elmyraFilters$FilterArr != null && elmyraFilters$FilterArr.length > 0) {
            int i3 = 0;
            while (true) {
                ElmyraFilters$Filter[] elmyraFilters$FilterArr2 = this.filters;
                if (i3 >= elmyraFilters$FilterArr2.length) {
                    break;
                }
                ElmyraFilters$Filter elmyraFilters$Filter = elmyraFilters$FilterArr2[i3];
                if (elmyraFilters$Filter != null) {
                    computeSerializedSize += CodedOutputByteBufferNano.computeMessageSize(4, elmyraFilters$Filter);
                }
                i3++;
            }
        }
        return computeSerializedSize;
    }

    @Override // com.google.protobuf.nano.MessageNano
    public ChassisProtos$Sensor mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        while (true) {
            int readTag = codedInputByteBufferNano.readTag();
            if (readTag == 0) {
                return this;
            }
            if (readTag == 8) {
                this.source = codedInputByteBufferNano.readUInt32();
            } else if (readTag == 16) {
                this.gain = codedInputByteBufferNano.readInt32();
            } else if (readTag == 29) {
                this.sensitivity = codedInputByteBufferNano.readFloat();
            } else if (readTag == 34) {
                int repeatedFieldArrayLength = WireFormatNano.getRepeatedFieldArrayLength(codedInputByteBufferNano, 34);
                ElmyraFilters$Filter[] elmyraFilters$FilterArr = this.filters;
                int length = elmyraFilters$FilterArr == null ? 0 : elmyraFilters$FilterArr.length;
                int i = repeatedFieldArrayLength + length;
                ElmyraFilters$Filter[] elmyraFilters$FilterArr2 = new ElmyraFilters$Filter[i];
                if (length != 0) {
                    System.arraycopy(elmyraFilters$FilterArr, 0, elmyraFilters$FilterArr2, 0, length);
                }
                while (length < i - 1) {
                    elmyraFilters$FilterArr2[length] = new ElmyraFilters$Filter();
                    codedInputByteBufferNano.readMessage(elmyraFilters$FilterArr2[length]);
                    codedInputByteBufferNano.readTag();
                    length++;
                }
                elmyraFilters$FilterArr2[length] = new ElmyraFilters$Filter();
                codedInputByteBufferNano.readMessage(elmyraFilters$FilterArr2[length]);
                this.filters = elmyraFilters$FilterArr2;
            } else if (!WireFormatNano.parseUnknownField(codedInputByteBufferNano, readTag)) {
                return this;
            }
        }
    }
}
