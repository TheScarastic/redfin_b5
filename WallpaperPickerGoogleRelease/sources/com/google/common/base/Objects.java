package com.google.common.base;
/* loaded from: classes.dex */
public final class Objects {
    public static boolean equal(Object obj, Object obj2) {
        return obj == obj2 || (obj != null && obj.equals(obj2));
    }
}
