package kotlin.sequences;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import kotlin.collections.CollectionsKt__CollectionsKt;
import org.jetbrains.annotations.NotNull;
/* loaded from: classes.dex */
public final class SequencesKt extends SequencesKt___SequencesJvmKt {
    @NotNull
    public static final <T> List<T> toList(@NotNull Sequence<? extends T> sequence) {
        ArrayList arrayList = new ArrayList();
        Iterator<? extends T> it = sequence.iterator();
        while (it.hasNext()) {
            arrayList.add(it.next());
        }
        return CollectionsKt__CollectionsKt.optimizeReadOnlyList(arrayList);
    }
}
