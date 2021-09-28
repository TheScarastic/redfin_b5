package com.android.systemui.tracing.nano;

import com.google.protobuf.nano.CodedInputByteBufferNano;
import com.google.protobuf.nano.CodedOutputByteBufferNano;
import com.google.protobuf.nano.MessageNano;
import com.google.protobuf.nano.WireFormatNano;
import java.io.IOException;
/* loaded from: classes2.dex */
public final class SystemUiTraceFileProto extends MessageNano {
    public SystemUiTraceEntryProto[] entry;
    public long magicNumber;

    public SystemUiTraceFileProto() {
        clear();
    }

    public SystemUiTraceFileProto clear() {
        this.magicNumber = 0;
        this.entry = SystemUiTraceEntryProto.emptyArray();
        this.cachedSize = -1;
        return this;
    }

    @Override // com.google.protobuf.nano.MessageNano
    public void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
        long j = this.magicNumber;
        if (j != 0) {
            codedOutputByteBufferNano.writeFixed64(1, j);
        }
        SystemUiTraceEntryProto[] systemUiTraceEntryProtoArr = this.entry;
        if (systemUiTraceEntryProtoArr != null && systemUiTraceEntryProtoArr.length > 0) {
            int i = 0;
            while (true) {
                SystemUiTraceEntryProto[] systemUiTraceEntryProtoArr2 = this.entry;
                if (i >= systemUiTraceEntryProtoArr2.length) {
                    break;
                }
                SystemUiTraceEntryProto systemUiTraceEntryProto = systemUiTraceEntryProtoArr2[i];
                if (systemUiTraceEntryProto != null) {
                    codedOutputByteBufferNano.writeMessage(2, systemUiTraceEntryProto);
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
        long j = this.magicNumber;
        if (j != 0) {
            computeSerializedSize += CodedOutputByteBufferNano.computeFixed64Size(1, j);
        }
        SystemUiTraceEntryProto[] systemUiTraceEntryProtoArr = this.entry;
        if (systemUiTraceEntryProtoArr != null && systemUiTraceEntryProtoArr.length > 0) {
            int i = 0;
            while (true) {
                SystemUiTraceEntryProto[] systemUiTraceEntryProtoArr2 = this.entry;
                if (i >= systemUiTraceEntryProtoArr2.length) {
                    break;
                }
                SystemUiTraceEntryProto systemUiTraceEntryProto = systemUiTraceEntryProtoArr2[i];
                if (systemUiTraceEntryProto != null) {
                    computeSerializedSize += CodedOutputByteBufferNano.computeMessageSize(2, systemUiTraceEntryProto);
                }
                i++;
            }
        }
        return computeSerializedSize;
    }

    @Override // com.google.protobuf.nano.MessageNano
    public SystemUiTraceFileProto mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        while (true) {
            int readTag = codedInputByteBufferNano.readTag();
            if (readTag == 0) {
                return this;
            }
            if (readTag == 9) {
                this.magicNumber = codedInputByteBufferNano.readFixed64();
            } else if (readTag == 18) {
                int repeatedFieldArrayLength = WireFormatNano.getRepeatedFieldArrayLength(codedInputByteBufferNano, 18);
                SystemUiTraceEntryProto[] systemUiTraceEntryProtoArr = this.entry;
                int length = systemUiTraceEntryProtoArr == null ? 0 : systemUiTraceEntryProtoArr.length;
                int i = repeatedFieldArrayLength + length;
                SystemUiTraceEntryProto[] systemUiTraceEntryProtoArr2 = new SystemUiTraceEntryProto[i];
                if (length != 0) {
                    System.arraycopy(systemUiTraceEntryProtoArr, 0, systemUiTraceEntryProtoArr2, 0, length);
                }
                while (length < i - 1) {
                    systemUiTraceEntryProtoArr2[length] = new SystemUiTraceEntryProto();
                    codedInputByteBufferNano.readMessage(systemUiTraceEntryProtoArr2[length]);
                    codedInputByteBufferNano.readTag();
                    length++;
                }
                systemUiTraceEntryProtoArr2[length] = new SystemUiTraceEntryProto();
                codedInputByteBufferNano.readMessage(systemUiTraceEntryProtoArr2[length]);
                this.entry = systemUiTraceEntryProtoArr2;
            } else if (!WireFormatNano.parseUnknownField(codedInputByteBufferNano, readTag)) {
                return this;
            }
        }
    }
}
