package com.google.android.systemui.elmyra.proto.nano;

import com.google.protobuf.nano.CodedInputByteBufferNano;
import com.google.protobuf.nano.CodedOutputByteBufferNano;
import com.google.protobuf.nano.InvalidProtocolBufferNanoException;
import com.google.protobuf.nano.MessageNano;
import com.google.protobuf.nano.WireFormatNano;
import java.io.IOException;
/* loaded from: classes2.dex */
public final class ChassisProtos$Chassis extends MessageNano {
    public ElmyraFilters$Filter[] defaultFilters;
    public ChassisProtos$Sensor[] sensors;

    public ChassisProtos$Chassis() {
        clear();
    }

    public ChassisProtos$Chassis clear() {
        this.sensors = ChassisProtos$Sensor.emptyArray();
        this.defaultFilters = ElmyraFilters$Filter.emptyArray();
        this.cachedSize = -1;
        return this;
    }

    @Override // com.google.protobuf.nano.MessageNano
    public void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
        ChassisProtos$Sensor[] chassisProtos$SensorArr = this.sensors;
        int i = 0;
        if (chassisProtos$SensorArr != null && chassisProtos$SensorArr.length > 0) {
            int i2 = 0;
            while (true) {
                ChassisProtos$Sensor[] chassisProtos$SensorArr2 = this.sensors;
                if (i2 >= chassisProtos$SensorArr2.length) {
                    break;
                }
                ChassisProtos$Sensor chassisProtos$Sensor = chassisProtos$SensorArr2[i2];
                if (chassisProtos$Sensor != null) {
                    codedOutputByteBufferNano.writeMessage(1, chassisProtos$Sensor);
                }
                i2++;
            }
        }
        ElmyraFilters$Filter[] elmyraFilters$FilterArr = this.defaultFilters;
        if (elmyraFilters$FilterArr != null && elmyraFilters$FilterArr.length > 0) {
            while (true) {
                ElmyraFilters$Filter[] elmyraFilters$FilterArr2 = this.defaultFilters;
                if (i >= elmyraFilters$FilterArr2.length) {
                    break;
                }
                ElmyraFilters$Filter elmyraFilters$Filter = elmyraFilters$FilterArr2[i];
                if (elmyraFilters$Filter != null) {
                    codedOutputByteBufferNano.writeMessage(2, elmyraFilters$Filter);
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
        ChassisProtos$Sensor[] chassisProtos$SensorArr = this.sensors;
        int i = 0;
        if (chassisProtos$SensorArr != null && chassisProtos$SensorArr.length > 0) {
            int i2 = 0;
            while (true) {
                ChassisProtos$Sensor[] chassisProtos$SensorArr2 = this.sensors;
                if (i2 >= chassisProtos$SensorArr2.length) {
                    break;
                }
                ChassisProtos$Sensor chassisProtos$Sensor = chassisProtos$SensorArr2[i2];
                if (chassisProtos$Sensor != null) {
                    computeSerializedSize += CodedOutputByteBufferNano.computeMessageSize(1, chassisProtos$Sensor);
                }
                i2++;
            }
        }
        ElmyraFilters$Filter[] elmyraFilters$FilterArr = this.defaultFilters;
        if (elmyraFilters$FilterArr != null && elmyraFilters$FilterArr.length > 0) {
            while (true) {
                ElmyraFilters$Filter[] elmyraFilters$FilterArr2 = this.defaultFilters;
                if (i >= elmyraFilters$FilterArr2.length) {
                    break;
                }
                ElmyraFilters$Filter elmyraFilters$Filter = elmyraFilters$FilterArr2[i];
                if (elmyraFilters$Filter != null) {
                    computeSerializedSize += CodedOutputByteBufferNano.computeMessageSize(2, elmyraFilters$Filter);
                }
                i++;
            }
        }
        return computeSerializedSize;
    }

    @Override // com.google.protobuf.nano.MessageNano
    public ChassisProtos$Chassis mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        while (true) {
            int readTag = codedInputByteBufferNano.readTag();
            if (readTag == 0) {
                return this;
            }
            if (readTag == 10) {
                int repeatedFieldArrayLength = WireFormatNano.getRepeatedFieldArrayLength(codedInputByteBufferNano, 10);
                ChassisProtos$Sensor[] chassisProtos$SensorArr = this.sensors;
                int length = chassisProtos$SensorArr == null ? 0 : chassisProtos$SensorArr.length;
                int i = repeatedFieldArrayLength + length;
                ChassisProtos$Sensor[] chassisProtos$SensorArr2 = new ChassisProtos$Sensor[i];
                if (length != 0) {
                    System.arraycopy(chassisProtos$SensorArr, 0, chassisProtos$SensorArr2, 0, length);
                }
                while (length < i - 1) {
                    chassisProtos$SensorArr2[length] = new ChassisProtos$Sensor();
                    codedInputByteBufferNano.readMessage(chassisProtos$SensorArr2[length]);
                    codedInputByteBufferNano.readTag();
                    length++;
                }
                chassisProtos$SensorArr2[length] = new ChassisProtos$Sensor();
                codedInputByteBufferNano.readMessage(chassisProtos$SensorArr2[length]);
                this.sensors = chassisProtos$SensorArr2;
            } else if (readTag == 18) {
                int repeatedFieldArrayLength2 = WireFormatNano.getRepeatedFieldArrayLength(codedInputByteBufferNano, 18);
                ElmyraFilters$Filter[] elmyraFilters$FilterArr = this.defaultFilters;
                int length2 = elmyraFilters$FilterArr == null ? 0 : elmyraFilters$FilterArr.length;
                int i2 = repeatedFieldArrayLength2 + length2;
                ElmyraFilters$Filter[] elmyraFilters$FilterArr2 = new ElmyraFilters$Filter[i2];
                if (length2 != 0) {
                    System.arraycopy(elmyraFilters$FilterArr, 0, elmyraFilters$FilterArr2, 0, length2);
                }
                while (length2 < i2 - 1) {
                    elmyraFilters$FilterArr2[length2] = new ElmyraFilters$Filter();
                    codedInputByteBufferNano.readMessage(elmyraFilters$FilterArr2[length2]);
                    codedInputByteBufferNano.readTag();
                    length2++;
                }
                elmyraFilters$FilterArr2[length2] = new ElmyraFilters$Filter();
                codedInputByteBufferNano.readMessage(elmyraFilters$FilterArr2[length2]);
                this.defaultFilters = elmyraFilters$FilterArr2;
            } else if (!WireFormatNano.parseUnknownField(codedInputByteBufferNano, readTag)) {
                return this;
            }
        }
    }

    public static ChassisProtos$Chassis parseFrom(byte[] bArr) throws InvalidProtocolBufferNanoException {
        return (ChassisProtos$Chassis) MessageNano.mergeFrom(new ChassisProtos$Chassis(), bArr);
    }
}
