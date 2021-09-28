package kotlin.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* loaded from: classes.dex */
public class CollectionsKt___CollectionsKt extends CollectionsKt__ReversedViewsKt {
    public static final <T> T first(@NotNull List<? extends T> list) {
        if (!list.isEmpty()) {
            return (T) list.get(0);
        }
        throw new NoSuchElementException("List is empty.");
    }

    @NotNull
    public static final <T, C extends Collection<? super T>> C toCollection(@NotNull Iterable<? extends T> iterable, @NotNull C c) {
        Iterator<? extends T> it = iterable.iterator();
        while (it.hasNext()) {
            c.add(it.next());
        }
        return c;
    }

    @NotNull
    public static final <T> List<T> toList(@NotNull Iterable<? extends T> iterable) {
        List list;
        Intrinsics.checkNotNullParameter(iterable, "$this$toList");
        boolean z = iterable instanceof Collection;
        if (z) {
            Collection collection = (Collection) iterable;
            int size = collection.size();
            if (size == 0) {
                return EmptyList.INSTANCE;
            }
            if (size != 1) {
                return toMutableList(collection);
            }
            return CollectionsKt__CollectionsKt.listOf(iterable instanceof List ? ((List) iterable).get(0) : iterable.iterator().next());
        }
        Intrinsics.checkNotNullParameter(iterable, "$this$toMutableList");
        if (z) {
            list = toMutableList((Collection) iterable);
        } else {
            ArrayList arrayList = new ArrayList();
            toCollection(iterable, arrayList);
            list = arrayList;
        }
        return CollectionsKt__CollectionsKt.optimizeReadOnlyList(list);
    }

    @NotNull
    public static final <T> List<T> toMutableList(@NotNull Collection<? extends T> collection) {
        Intrinsics.checkNotNullParameter(collection, "$this$toMutableList");
        return new ArrayList(collection);
    }
}
