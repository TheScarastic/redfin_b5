package kotlin.sequences;

import java.util.Iterator;
import org.jetbrains.annotations.NotNull;
/* loaded from: classes.dex */
public interface Sequence<T> {
    @NotNull
    Iterator<T> iterator();
}
