package com.google.common.collect;
/* loaded from: classes.dex */
public abstract class ForwardingObject {
    /* renamed from: delegate */
    public abstract Object mo22delegate();

    @Override // java.lang.Object
    public String toString() {
        return mo22delegate().toString();
    }
}
