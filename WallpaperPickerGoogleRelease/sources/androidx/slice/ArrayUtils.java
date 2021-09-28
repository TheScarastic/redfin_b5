package androidx.slice;

import java.util.Objects;
/* loaded from: classes.dex */
public class ArrayUtils {
    public static <T> boolean contains(T[] tArr, T t) {
        for (T t2 : tArr) {
            if (Objects.equals(t2, t)) {
                return true;
            }
        }
        return false;
    }
}
