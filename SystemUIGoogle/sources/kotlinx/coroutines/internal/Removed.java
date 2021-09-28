package kotlinx.coroutines.internal;

import kotlin.jvm.internal.Intrinsics;
/* compiled from: LockFreeLinkedList.kt */
/* access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class Removed {
    public final LockFreeLinkedListNode ref;

    public Removed(LockFreeLinkedListNode lockFreeLinkedListNode) {
        Intrinsics.checkParameterIsNotNull(lockFreeLinkedListNode, "ref");
        this.ref = lockFreeLinkedListNode;
    }

    public String toString() {
        return "Removed[" + this.ref + ']';
    }
}
