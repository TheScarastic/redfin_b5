package kotlinx.coroutines.internal;

import kotlin.jvm.internal.Intrinsics;
/* compiled from: LockFreeLinkedList.kt */
/* loaded from: classes2.dex */
public final class LockFreeLinkedListKt {
    private static final Object CONDITION_FALSE = new Symbol("CONDITION_FALSE");
    private static final Object ALREADY_REMOVED = new Symbol("ALREADY_REMOVED");
    private static final Object LIST_EMPTY = new Symbol("LIST_EMPTY");
    private static final Object REMOVE_PREPARED = new Symbol("REMOVE_PREPARED");

    public static final Object getCONDITION_FALSE() {
        return CONDITION_FALSE;
    }

    public static final LockFreeLinkedListNode unwrap(Object obj) {
        LockFreeLinkedListNode lockFreeLinkedListNode;
        Intrinsics.checkParameterIsNotNull(obj, "$this$unwrap");
        Removed removed = (Removed) (!(obj instanceof Removed) ? null : obj);
        return (removed == null || (lockFreeLinkedListNode = removed.ref) == null) ? (LockFreeLinkedListNode) obj : lockFreeLinkedListNode;
    }
}
