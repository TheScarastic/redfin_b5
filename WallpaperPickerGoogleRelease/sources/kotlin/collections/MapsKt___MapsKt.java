package kotlin.collections;

import java.util.Iterator;
import java.util.Map;
import kotlin.Pair;
import org.jetbrains.annotations.NotNull;
/* loaded from: classes.dex */
public class MapsKt___MapsKt extends MapsKt__MapsJVMKt {
    /* JADX DEBUG: Multi-variable search result rejected for r3v0, resolved type: M extends java.util.Map<? super K, ? super V> */
    /* JADX WARN: Multi-variable type inference failed */
    @NotNull
    public static final <K, V, M extends Map<? super K, ? super V>> M toMap(@NotNull Iterable<? extends Pair<? extends K, ? extends V>> iterable, @NotNull M m) {
        Iterator<? extends Pair<? extends K, ? extends V>> it = iterable.iterator();
        while (it.hasNext()) {
            Pair pair = (Pair) it.next();
            m.put(pair.component1(), pair.component2());
        }
        return m;
    }
}
