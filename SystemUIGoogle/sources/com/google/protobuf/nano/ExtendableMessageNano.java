package com.google.protobuf.nano;

import com.google.protobuf.nano.ExtendableMessageNano;
import java.io.IOException;
/* loaded from: classes2.dex */
public abstract class ExtendableMessageNano<M extends ExtendableMessageNano<M>> extends MessageNano {
    protected FieldArray unknownFieldData;

    /* access modifiers changed from: protected */
    @Override // com.google.protobuf.nano.MessageNano
    public int computeSerializedSize() {
        return 0;
    }

    @Override // com.google.protobuf.nano.MessageNano
    public void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
    }

    @Override // com.google.protobuf.nano.MessageNano
    public M clone() throws CloneNotSupportedException {
        M m = (M) ((ExtendableMessageNano) super.clone());
        InternalNano.cloneUnknownFieldData(this, m);
        return m;
    }
}
