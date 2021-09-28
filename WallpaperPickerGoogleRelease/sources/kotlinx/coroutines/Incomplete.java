package kotlinx.coroutines;

import org.jetbrains.annotations.Nullable;
/* loaded from: classes.dex */
public interface Incomplete {
    @Nullable
    NodeList getList();

    boolean isActive();
}
