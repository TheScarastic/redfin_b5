package com.google.protobuf;

import com.google.protobuf.GeneratedMessageLite;
/* loaded from: classes.dex */
public final class NewInstanceSchemaLite implements NewInstanceSchema {
    @Override // com.google.protobuf.NewInstanceSchema
    public Object newInstance(Object obj) {
        return ((GeneratedMessageLite) obj).dynamicMethod(GeneratedMessageLite.MethodToInvoke.NEW_MUTABLE_INSTANCE, null, null);
    }
}
