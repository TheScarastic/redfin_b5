package com.google.android.systemui.elmyra.proto.nano;

import com.google.protobuf.nano.CodedInputByteBufferNano;
import com.google.protobuf.nano.CodedOutputByteBufferNano;
import com.google.protobuf.nano.InternalNano;
import com.google.protobuf.nano.MessageNano;
import com.google.protobuf.nano.WireFormatNano;
import java.io.IOException;
/* loaded from: classes2.dex */
public final class ElmyraFilters$Filter extends MessageNano {
    private static volatile ElmyraFilters$Filter[] _emptyArray;
    private int parametersCase_ = 0;
    private Object parameters_;

    public ElmyraFilters$Filter clearParameters() {
        this.parametersCase_ = 0;
        this.parameters_ = null;
        return this;
    }

    public static ElmyraFilters$Filter[] emptyArray() {
        if (_emptyArray == null) {
            synchronized (InternalNano.LAZY_INIT_LOCK) {
                if (_emptyArray == null) {
                    _emptyArray = new ElmyraFilters$Filter[0];
                }
            }
        }
        return _emptyArray;
    }

    public ElmyraFilters$Filter() {
        clear();
    }

    public ElmyraFilters$Filter clear() {
        clearParameters();
        this.cachedSize = -1;
        return this;
    }

    @Override // com.google.protobuf.nano.MessageNano
    public void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
        if (this.parametersCase_ == 1) {
            codedOutputByteBufferNano.writeMessage(1, (MessageNano) this.parameters_);
        }
        if (this.parametersCase_ == 2) {
            codedOutputByteBufferNano.writeMessage(2, (MessageNano) this.parameters_);
        }
        if (this.parametersCase_ == 3) {
            codedOutputByteBufferNano.writeMessage(3, (MessageNano) this.parameters_);
        }
        if (this.parametersCase_ == 4) {
            codedOutputByteBufferNano.writeMessage(4, (MessageNano) this.parameters_);
        }
        super.writeTo(codedOutputByteBufferNano);
    }

    /* access modifiers changed from: protected */
    @Override // com.google.protobuf.nano.MessageNano
    public int computeSerializedSize() {
        int computeSerializedSize = super.computeSerializedSize();
        if (this.parametersCase_ == 1) {
            computeSerializedSize += CodedOutputByteBufferNano.computeMessageSize(1, (MessageNano) this.parameters_);
        }
        if (this.parametersCase_ == 2) {
            computeSerializedSize += CodedOutputByteBufferNano.computeMessageSize(2, (MessageNano) this.parameters_);
        }
        if (this.parametersCase_ == 3) {
            computeSerializedSize += CodedOutputByteBufferNano.computeMessageSize(3, (MessageNano) this.parameters_);
        }
        return this.parametersCase_ == 4 ? computeSerializedSize + CodedOutputByteBufferNano.computeMessageSize(4, (MessageNano) this.parameters_) : computeSerializedSize;
    }

    @Override // com.google.protobuf.nano.MessageNano
    public ElmyraFilters$Filter mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        while (true) {
            int readTag = codedInputByteBufferNano.readTag();
            if (readTag == 0) {
                return this;
            }
            if (readTag == 10) {
                if (this.parametersCase_ != 1) {
                    this.parameters_ = new MessageNano() { // from class: com.google.android.systemui.elmyra.proto.nano.ElmyraFilters$FIRFilter
                        public float[] coefficients;

                        {
                            clear();
                        }

                        public ElmyraFilters$FIRFilter clear() {
                            this.coefficients = WireFormatNano.EMPTY_FLOAT_ARRAY;
                            this.cachedSize = -1;
                            return this;
                        }

                        @Override // com.google.protobuf.nano.MessageNano
                        public void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
                            float[] fArr = this.coefficients;
                            if (fArr != null && fArr.length > 0) {
                                int i = 0;
                                while (true) {
                                    float[] fArr2 = this.coefficients;
                                    if (i >= fArr2.length) {
                                        break;
                                    }
                                    codedOutputByteBufferNano.writeFloat(1, fArr2[i]);
                                    i++;
                                }
                            }
                            super.writeTo(codedOutputByteBufferNano);
                        }

                        /* access modifiers changed from: protected */
                        @Override // com.google.protobuf.nano.MessageNano
                        public int computeSerializedSize() {
                            int computeSerializedSize = super.computeSerializedSize();
                            float[] fArr = this.coefficients;
                            return (fArr == null || fArr.length <= 0) ? computeSerializedSize : computeSerializedSize + (fArr.length * 4) + (fArr.length * 1);
                        }

                        @Override // com.google.protobuf.nano.MessageNano
                        public ElmyraFilters$FIRFilter mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano2) throws IOException {
                            while (true) {
                                int readTag2 = codedInputByteBufferNano2.readTag();
                                if (readTag2 == 0) {
                                    return this;
                                }
                                if (readTag2 == 10) {
                                    int readRawVarint32 = codedInputByteBufferNano2.readRawVarint32();
                                    int pushLimit = codedInputByteBufferNano2.pushLimit(readRawVarint32);
                                    int i = readRawVarint32 / 4;
                                    float[] fArr = this.coefficients;
                                    int length = fArr == null ? 0 : fArr.length;
                                    int i2 = i + length;
                                    float[] fArr2 = new float[i2];
                                    if (length != 0) {
                                        System.arraycopy(fArr, 0, fArr2, 0, length);
                                    }
                                    while (length < i2) {
                                        fArr2[length] = codedInputByteBufferNano2.readFloat();
                                        length++;
                                    }
                                    this.coefficients = fArr2;
                                    codedInputByteBufferNano2.popLimit(pushLimit);
                                } else if (readTag2 == 13) {
                                    int repeatedFieldArrayLength = WireFormatNano.getRepeatedFieldArrayLength(codedInputByteBufferNano2, 13);
                                    float[] fArr3 = this.coefficients;
                                    int length2 = fArr3 == null ? 0 : fArr3.length;
                                    int i3 = repeatedFieldArrayLength + length2;
                                    float[] fArr4 = new float[i3];
                                    if (length2 != 0) {
                                        System.arraycopy(fArr3, 0, fArr4, 0, length2);
                                    }
                                    while (length2 < i3 - 1) {
                                        fArr4[length2] = codedInputByteBufferNano2.readFloat();
                                        codedInputByteBufferNano2.readTag();
                                        length2++;
                                    }
                                    fArr4[length2] = codedInputByteBufferNano2.readFloat();
                                    this.coefficients = fArr4;
                                } else if (!WireFormatNano.parseUnknownField(codedInputByteBufferNano2, readTag2)) {
                                    return this;
                                }
                            }
                        }
                    };
                }
                codedInputByteBufferNano.readMessage((MessageNano) this.parameters_);
                this.parametersCase_ = 1;
            } else if (readTag == 18) {
                if (this.parametersCase_ != 2) {
                    this.parameters_ = new MessageNano() { // from class: com.google.android.systemui.elmyra.proto.nano.ElmyraFilters$HighpassFilter
                        public float cutoff;
                        public float rate;

                        {
                            clear();
                        }

                        public ElmyraFilters$HighpassFilter clear() {
                            this.cutoff = 0.0f;
                            this.rate = 0.0f;
                            this.cachedSize = -1;
                            return this;
                        }

                        @Override // com.google.protobuf.nano.MessageNano
                        public void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
                            if (Float.floatToIntBits(this.cutoff) != Float.floatToIntBits(0.0f)) {
                                codedOutputByteBufferNano.writeFloat(1, this.cutoff);
                            }
                            if (Float.floatToIntBits(this.rate) != Float.floatToIntBits(0.0f)) {
                                codedOutputByteBufferNano.writeFloat(2, this.rate);
                            }
                            super.writeTo(codedOutputByteBufferNano);
                        }

                        /* access modifiers changed from: protected */
                        @Override // com.google.protobuf.nano.MessageNano
                        public int computeSerializedSize() {
                            int computeSerializedSize = super.computeSerializedSize();
                            if (Float.floatToIntBits(this.cutoff) != Float.floatToIntBits(0.0f)) {
                                computeSerializedSize += CodedOutputByteBufferNano.computeFloatSize(1, this.cutoff);
                            }
                            return Float.floatToIntBits(this.rate) != Float.floatToIntBits(0.0f) ? computeSerializedSize + CodedOutputByteBufferNano.computeFloatSize(2, this.rate) : computeSerializedSize;
                        }

                        @Override // com.google.protobuf.nano.MessageNano
                        public ElmyraFilters$HighpassFilter mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano2) throws IOException {
                            while (true) {
                                int readTag2 = codedInputByteBufferNano2.readTag();
                                if (readTag2 == 0) {
                                    return this;
                                }
                                if (readTag2 == 13) {
                                    this.cutoff = codedInputByteBufferNano2.readFloat();
                                } else if (readTag2 == 21) {
                                    this.rate = codedInputByteBufferNano2.readFloat();
                                } else if (!WireFormatNano.parseUnknownField(codedInputByteBufferNano2, readTag2)) {
                                    return this;
                                }
                            }
                        }
                    };
                }
                codedInputByteBufferNano.readMessage((MessageNano) this.parameters_);
                this.parametersCase_ = 2;
            } else if (readTag == 26) {
                if (this.parametersCase_ != 3) {
                    this.parameters_ = new MessageNano() { // from class: com.google.android.systemui.elmyra.proto.nano.ElmyraFilters$LowpassFilter
                        public float cutoff;
                        public float rate;

                        {
                            clear();
                        }

                        public ElmyraFilters$LowpassFilter clear() {
                            this.cutoff = 0.0f;
                            this.rate = 0.0f;
                            this.cachedSize = -1;
                            return this;
                        }

                        @Override // com.google.protobuf.nano.MessageNano
                        public void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
                            if (Float.floatToIntBits(this.cutoff) != Float.floatToIntBits(0.0f)) {
                                codedOutputByteBufferNano.writeFloat(1, this.cutoff);
                            }
                            if (Float.floatToIntBits(this.rate) != Float.floatToIntBits(0.0f)) {
                                codedOutputByteBufferNano.writeFloat(2, this.rate);
                            }
                            super.writeTo(codedOutputByteBufferNano);
                        }

                        /* access modifiers changed from: protected */
                        @Override // com.google.protobuf.nano.MessageNano
                        public int computeSerializedSize() {
                            int computeSerializedSize = super.computeSerializedSize();
                            if (Float.floatToIntBits(this.cutoff) != Float.floatToIntBits(0.0f)) {
                                computeSerializedSize += CodedOutputByteBufferNano.computeFloatSize(1, this.cutoff);
                            }
                            return Float.floatToIntBits(this.rate) != Float.floatToIntBits(0.0f) ? computeSerializedSize + CodedOutputByteBufferNano.computeFloatSize(2, this.rate) : computeSerializedSize;
                        }

                        @Override // com.google.protobuf.nano.MessageNano
                        public ElmyraFilters$LowpassFilter mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano2) throws IOException {
                            while (true) {
                                int readTag2 = codedInputByteBufferNano2.readTag();
                                if (readTag2 == 0) {
                                    return this;
                                }
                                if (readTag2 == 13) {
                                    this.cutoff = codedInputByteBufferNano2.readFloat();
                                } else if (readTag2 == 21) {
                                    this.rate = codedInputByteBufferNano2.readFloat();
                                } else if (!WireFormatNano.parseUnknownField(codedInputByteBufferNano2, readTag2)) {
                                    return this;
                                }
                            }
                        }
                    };
                }
                codedInputByteBufferNano.readMessage((MessageNano) this.parameters_);
                this.parametersCase_ = 3;
            } else if (readTag == 34) {
                if (this.parametersCase_ != 4) {
                    this.parameters_ = new MessageNano() { // from class: com.google.android.systemui.elmyra.proto.nano.ElmyraFilters$MedianFilter
                        public int windowSize;

                        {
                            clear();
                        }

                        public ElmyraFilters$MedianFilter clear() {
                            this.windowSize = 0;
                            this.cachedSize = -1;
                            return this;
                        }

                        @Override // com.google.protobuf.nano.MessageNano
                        public void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
                            int i = this.windowSize;
                            if (i != 0) {
                                codedOutputByteBufferNano.writeUInt32(1, i);
                            }
                            super.writeTo(codedOutputByteBufferNano);
                        }

                        /* access modifiers changed from: protected */
                        @Override // com.google.protobuf.nano.MessageNano
                        public int computeSerializedSize() {
                            int computeSerializedSize = super.computeSerializedSize();
                            int i = this.windowSize;
                            return i != 0 ? computeSerializedSize + CodedOutputByteBufferNano.computeUInt32Size(1, i) : computeSerializedSize;
                        }

                        @Override // com.google.protobuf.nano.MessageNano
                        public ElmyraFilters$MedianFilter mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano2) throws IOException {
                            while (true) {
                                int readTag2 = codedInputByteBufferNano2.readTag();
                                if (readTag2 == 0) {
                                    return this;
                                }
                                if (readTag2 == 8) {
                                    this.windowSize = codedInputByteBufferNano2.readUInt32();
                                } else if (!WireFormatNano.parseUnknownField(codedInputByteBufferNano2, readTag2)) {
                                    return this;
                                }
                            }
                        }
                    };
                }
                codedInputByteBufferNano.readMessage((MessageNano) this.parameters_);
                this.parametersCase_ = 4;
            } else if (!WireFormatNano.parseUnknownField(codedInputByteBufferNano, readTag)) {
                return this;
            }
        }
    }
}
