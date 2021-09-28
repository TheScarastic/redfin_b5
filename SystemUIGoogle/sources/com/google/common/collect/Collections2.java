package com.google.common.collect;

import com.google.common.base.Preconditions;
import java.util.Collection;
/* loaded from: classes2.dex */
public final class Collections2 {
    /* access modifiers changed from: package-private */
    public static boolean safeContains(Collection<?> collection, Object obj) {
        Preconditions.checkNotNull(collection);
        try {
            return collection.contains(obj);
        } catch (ClassCastException | NullPointerException unused) {
            return false;
        }
    }

    /* access modifiers changed from: package-private */
    public static StringBuilder newStringBuilderForCollection(int i) {
        CollectPreconditions.checkNonnegative(i, "size");
        return new StringBuilder((int) Math.min(((long) i) * 8, 1073741824L));
    }
}
