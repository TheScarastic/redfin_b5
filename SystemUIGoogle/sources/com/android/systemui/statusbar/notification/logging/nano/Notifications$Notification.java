package com.android.systemui.statusbar.notification.logging.nano;

import com.google.protobuf.nano.CodedInputByteBufferNano;
import com.google.protobuf.nano.CodedOutputByteBufferNano;
import com.google.protobuf.nano.InternalNano;
import com.google.protobuf.nano.MessageNano;
import com.google.protobuf.nano.WireFormatNano;
import java.io.IOException;
/* loaded from: classes.dex */
public final class Notifications$Notification extends MessageNano {
    private static volatile Notifications$Notification[] _emptyArray;
    public int groupInstanceId;
    public int instanceId;
    public boolean isGroupSummary;
    public String packageName;
    public int section;
    public int uid;

    public static Notifications$Notification[] emptyArray() {
        if (_emptyArray == null) {
            synchronized (InternalNano.LAZY_INIT_LOCK) {
                if (_emptyArray == null) {
                    _emptyArray = new Notifications$Notification[0];
                }
            }
        }
        return _emptyArray;
    }

    public Notifications$Notification() {
        clear();
    }

    public Notifications$Notification clear() {
        this.uid = 0;
        this.packageName = "";
        this.instanceId = 0;
        this.groupInstanceId = 0;
        this.isGroupSummary = false;
        this.section = 0;
        this.cachedSize = -1;
        return this;
    }

    @Override // com.google.protobuf.nano.MessageNano
    public void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
        int i = this.uid;
        if (i != 0) {
            codedOutputByteBufferNano.writeInt32(1, i);
        }
        if (!this.packageName.equals("")) {
            codedOutputByteBufferNano.writeString(2, this.packageName);
        }
        int i2 = this.instanceId;
        if (i2 != 0) {
            codedOutputByteBufferNano.writeInt32(3, i2);
        }
        int i3 = this.groupInstanceId;
        if (i3 != 0) {
            codedOutputByteBufferNano.writeInt32(4, i3);
        }
        boolean z = this.isGroupSummary;
        if (z) {
            codedOutputByteBufferNano.writeBool(5, z);
        }
        int i4 = this.section;
        if (i4 != 0) {
            codedOutputByteBufferNano.writeInt32(6, i4);
        }
        super.writeTo(codedOutputByteBufferNano);
    }

    /* access modifiers changed from: protected */
    @Override // com.google.protobuf.nano.MessageNano
    public int computeSerializedSize() {
        int computeSerializedSize = super.computeSerializedSize();
        int i = this.uid;
        if (i != 0) {
            computeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(1, i);
        }
        if (!this.packageName.equals("")) {
            computeSerializedSize += CodedOutputByteBufferNano.computeStringSize(2, this.packageName);
        }
        int i2 = this.instanceId;
        if (i2 != 0) {
            computeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(3, i2);
        }
        int i3 = this.groupInstanceId;
        if (i3 != 0) {
            computeSerializedSize += CodedOutputByteBufferNano.computeInt32Size(4, i3);
        }
        boolean z = this.isGroupSummary;
        if (z) {
            computeSerializedSize += CodedOutputByteBufferNano.computeBoolSize(5, z);
        }
        int i4 = this.section;
        return i4 != 0 ? computeSerializedSize + CodedOutputByteBufferNano.computeInt32Size(6, i4) : computeSerializedSize;
    }

    @Override // com.google.protobuf.nano.MessageNano
    public Notifications$Notification mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        while (true) {
            int readTag = codedInputByteBufferNano.readTag();
            if (readTag == 0) {
                return this;
            }
            if (readTag == 8) {
                this.uid = codedInputByteBufferNano.readInt32();
            } else if (readTag == 18) {
                this.packageName = codedInputByteBufferNano.readString();
            } else if (readTag == 24) {
                this.instanceId = codedInputByteBufferNano.readInt32();
            } else if (readTag == 32) {
                this.groupInstanceId = codedInputByteBufferNano.readInt32();
            } else if (readTag == 40) {
                this.isGroupSummary = codedInputByteBufferNano.readBool();
            } else if (readTag == 48) {
                int readInt32 = codedInputByteBufferNano.readInt32();
                switch (readInt32) {
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                        this.section = readInt32;
                        continue;
                }
            } else if (!WireFormatNano.parseUnknownField(codedInputByteBufferNano, readTag)) {
                return this;
            }
        }
    }
}
