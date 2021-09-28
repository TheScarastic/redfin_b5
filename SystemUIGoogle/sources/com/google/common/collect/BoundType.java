package com.google.common.collect;
/* loaded from: classes2.dex */
public enum BoundType {
    OPEN(false),
    CLOSED(true);
    
    final boolean inclusive;

    BoundType(boolean z) {
        this.inclusive = z;
    }

    /* access modifiers changed from: package-private */
    public static BoundType forBoolean(boolean z) {
        return z ? CLOSED : OPEN;
    }
}
