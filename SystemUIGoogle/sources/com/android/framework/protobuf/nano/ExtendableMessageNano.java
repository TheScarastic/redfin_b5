package com.android.framework.protobuf.nano;

import com.android.framework.protobuf.nano.ExtendableMessageNano;
import java.io.IOException;
/* loaded from: classes.dex */
public abstract class ExtendableMessageNano<M extends ExtendableMessageNano<M>> extends MessageNano {
    protected FieldArray unknownFieldData;

    @Override // com.android.framework.protobuf.nano.MessageNano
    protected int computeSerializedSize() {
        return 0;
    }

    @Override // com.android.framework.protobuf.nano.MessageNano
    public void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
    }

    @Override // com.android.framework.protobuf.nano.MessageNano
    public M clone() throws CloneNotSupportedException {
        M m = (M) ((ExtendableMessageNano) super.clone());
        InternalNano.cloneUnknownFieldData(this, m);
        return m;
    }
}
