package com.google.android.systemui.autorotate.proto.nano;

import com.google.protobuf.nano.CodedInputByteBufferNano;
import com.google.protobuf.nano.CodedOutputByteBufferNano;
import com.google.protobuf.nano.InternalNano;
import com.google.protobuf.nano.MessageNano;
import com.google.protobuf.nano.WireFormatNano;
import java.io.IOException;
/* loaded from: classes2.dex */
public final class AutorotateProto$DeviceRotatedSensorSample extends MessageNano {
    private static volatile AutorotateProto$DeviceRotatedSensorSample[] _emptyArray;
    public int sensorType;
    public int timestampOffset;
    public float xValue;
    public float yValue;
    public float zValue;

    public static AutorotateProto$DeviceRotatedSensorSample[] emptyArray() {
        if (_emptyArray == null) {
            synchronized (InternalNano.LAZY_INIT_LOCK) {
                if (_emptyArray == null) {
                    _emptyArray = new AutorotateProto$DeviceRotatedSensorSample[0];
                }
            }
        }
        return _emptyArray;
    }

    public AutorotateProto$DeviceRotatedSensorSample() {
        clear();
    }

    public AutorotateProto$DeviceRotatedSensorSample clear() {
        this.timestampOffset = 0;
        this.sensorType = 0;
        this.xValue = 0.0f;
        this.yValue = 0.0f;
        this.zValue = 0.0f;
        this.cachedSize = -1;
        return this;
    }

    @Override // com.google.protobuf.nano.MessageNano
    public void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
        int i = this.timestampOffset;
        if (i != 0) {
            codedOutputByteBufferNano.writeInt32(1, i);
        }
        int i2 = this.sensorType;
        if (i2 != 0) {
            codedOutputByteBufferNano.writeInt32(2, i2);
        }
        if (Float.floatToIntBits(this.xValue) != Float.floatToIntBits(0.0f)) {
            codedOutputByteBufferNano.writeFloat(3, this.xValue);
        }
        if (Float.floatToIntBits(this.yValue) != Float.floatToIntBits(0.0f)) {
            codedOutputByteBufferNano.writeFloat(4, this.yValue);
        }
        if (Float.floatToIntBits(this.zValue) != Float.floatToIntBits(0.0f)) {
            codedOutputByteBufferNano.writeFloat(5, this.zValue);
        }
        super.writeTo(codedOutputByteBufferNano);
    }

    /* access modifiers changed from: protected */
    @Override // com.google.protobuf.nano.MessageNano
    public int computeSerializedSize() {
        int computeSerializedSize = super.computeSerializedSize();
        int i = this.timestampOffset;
        if (i != 0) {
            computeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(1, i);
        }
        int i2 = this.sensorType;
        if (i2 != 0) {
            computeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(2, i2);
        }
        if (Float.floatToIntBits(this.xValue) != Float.floatToIntBits(0.0f)) {
            computeSerializedSize += CodedOutputByteBufferNano.computeFloatSize(3, this.xValue);
        }
        if (Float.floatToIntBits(this.yValue) != Float.floatToIntBits(0.0f)) {
            computeSerializedSize += CodedOutputByteBufferNano.computeFloatSize(4, this.yValue);
        }
        return Float.floatToIntBits(this.zValue) != Float.floatToIntBits(0.0f) ? computeSerializedSize + CodedOutputByteBufferNano.computeFloatSize(5, this.zValue) : computeSerializedSize;
    }

    @Override // com.google.protobuf.nano.MessageNano
    public AutorotateProto$DeviceRotatedSensorSample mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        while (true) {
            int readTag = codedInputByteBufferNano.readTag();
            if (readTag == 0) {
                return this;
            }
            if (readTag == 8) {
                this.timestampOffset = codedInputByteBufferNano.readInt32();
            } else if (readTag == 16) {
                int readInt32 = codedInputByteBufferNano.readInt32();
                if (readInt32 == 0 || readInt32 == 1 || readInt32 == 2) {
                    this.sensorType = readInt32;
                }
            } else if (readTag == 29) {
                this.xValue = codedInputByteBufferNano.readFloat();
            } else if (readTag == 37) {
                this.yValue = codedInputByteBufferNano.readFloat();
            } else if (readTag == 45) {
                this.zValue = codedInputByteBufferNano.readFloat();
            } else if (!WireFormatNano.parseUnknownField(codedInputByteBufferNano, readTag)) {
                return this;
            }
        }
    }
}
