package kotlinx.coroutines.internal;

import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* loaded from: classes.dex */
public final class LockFreeLinkedListKt {
    @NotNull
    public static final Object CONDITION_FALSE = new Symbol("CONDITION_FALSE");

    static {
        Intrinsics.checkParameterIsNotNull("ALREADY_REMOVED", "symbol");
        Intrinsics.checkParameterIsNotNull("LIST_EMPTY", "symbol");
        Intrinsics.checkParameterIsNotNull("REMOVE_PREPARED", "symbol");
    }

    @NotNull
    public static final LockFreeLinkedListNode unwrap(@NotNull Object obj) {
        LockFreeLinkedListNode lockFreeLinkedListNode;
        Intrinsics.checkParameterIsNotNull(obj, "$this$unwrap");
        Removed removed = (Removed) (!(obj instanceof Removed) ? null : obj);
        return (removed == null || (lockFreeLinkedListNode = removed.ref) == null) ? (LockFreeLinkedListNode) obj : lockFreeLinkedListNode;
    }
}
