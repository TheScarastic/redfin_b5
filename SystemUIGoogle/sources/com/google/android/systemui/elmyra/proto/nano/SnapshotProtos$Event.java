package com.google.android.systemui.elmyra.proto.nano;

import com.google.protobuf.nano.CodedInputByteBufferNano;
import com.google.protobuf.nano.CodedOutputByteBufferNano;
import com.google.protobuf.nano.InternalNano;
import com.google.protobuf.nano.MessageNano;
import com.google.protobuf.nano.WireFormatNano;
import java.io.IOException;
/* loaded from: classes2.dex */
public final class SnapshotProtos$Event extends MessageNano {
    private static volatile SnapshotProtos$Event[] _emptyArray;
    private int typesCase_ = 0;
    private Object types_;

    public SnapshotProtos$Event clearTypes() {
        this.typesCase_ = 0;
        this.types_ = null;
        return this;
    }

    public static SnapshotProtos$Event[] emptyArray() {
        if (_emptyArray == null) {
            synchronized (InternalNano.LAZY_INIT_LOCK) {
                if (_emptyArray == null) {
                    _emptyArray = new SnapshotProtos$Event[0];
                }
            }
        }
        return _emptyArray;
    }

    public boolean hasSensorEvent() {
        return this.typesCase_ == 1;
    }

    public ChassisProtos$SensorEvent getSensorEvent() {
        if (this.typesCase_ == 1) {
            return (ChassisProtos$SensorEvent) this.types_;
        }
        return null;
    }

    public boolean hasGestureStage() {
        return this.typesCase_ == 2;
    }

    public int getGestureStage() {
        if (this.typesCase_ == 2) {
            return ((Integer) this.types_).intValue();
        }
        return 0;
    }

    public SnapshotProtos$Event() {
        clear();
    }

    public SnapshotProtos$Event clear() {
        clearTypes();
        this.cachedSize = -1;
        return this;
    }

    @Override // com.google.protobuf.nano.MessageNano
    public void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
        if (this.typesCase_ == 1) {
            codedOutputByteBufferNano.writeMessage(1, (MessageNano) this.types_);
        }
        if (this.typesCase_ == 2) {
            codedOutputByteBufferNano.writeEnum(2, ((Integer) this.types_).intValue());
        }
        super.writeTo(codedOutputByteBufferNano);
    }

    /* access modifiers changed from: protected */
    @Override // com.google.protobuf.nano.MessageNano
    public int computeSerializedSize() {
        int computeSerializedSize = super.computeSerializedSize();
        if (this.typesCase_ == 1) {
            computeSerializedSize += CodedOutputByteBufferNano.computeMessageSize(1, (MessageNano) this.types_);
        }
        return this.typesCase_ == 2 ? computeSerializedSize + CodedOutputByteBufferNano.computeEnumSize(2, ((Integer) this.types_).intValue()) : computeSerializedSize;
    }

    @Override // com.google.protobuf.nano.MessageNano
    public SnapshotProtos$Event mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        while (true) {
            int readTag = codedInputByteBufferNano.readTag();
            if (readTag == 0) {
                return this;
            }
            if (readTag == 10) {
                if (this.typesCase_ != 1) {
                    this.types_ = new ChassisProtos$SensorEvent();
                }
                codedInputByteBufferNano.readMessage((MessageNano) this.types_);
                this.typesCase_ = 1;
            } else if (readTag == 16) {
                this.types_ = Integer.valueOf(codedInputByteBufferNano.readEnum());
                this.typesCase_ = 2;
            } else if (!WireFormatNano.parseUnknownField(codedInputByteBufferNano, readTag)) {
                return this;
            }
        }
    }
}
