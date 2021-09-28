package com.google.common.collect;
/* loaded from: classes2.dex */
public abstract class ForwardingObject {
    protected abstract Object delegate();

    @Override // java.lang.Object
    public String toString() {
        return delegate().toString();
    }
}
