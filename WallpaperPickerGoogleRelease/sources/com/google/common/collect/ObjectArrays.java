package com.google.common.collect;

import androidx.appcompat.R$dimen$$ExternalSyntheticOutline0;
import java.lang.reflect.Array;
import java.util.Iterator;
/* loaded from: classes.dex */
public final class ObjectArrays {
    public static Object checkElementNotNull(Object obj, int i) {
        if (obj != null) {
            return obj;
        }
        throw new NullPointerException(R$dimen$$ExternalSyntheticOutline0.m(20, "at index ", i));
    }

    public static Object[] fillArray(Iterable<?> iterable, Object[] objArr) {
        Iterator<?> it = iterable.iterator();
        int i = 0;
        while (it.hasNext()) {
            objArr[i] = it.next();
            i++;
        }
        return objArr;
    }

    public static <T> T[] newArray(T[] tArr, int i) {
        return (T[]) ((Object[]) Array.newInstance(tArr.getClass().getComponentType(), i));
    }
}
