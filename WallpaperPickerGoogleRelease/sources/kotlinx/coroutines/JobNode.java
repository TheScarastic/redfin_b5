package kotlinx.coroutines;

import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.Job;
import kotlinx.coroutines.internal.LockFreeLinkedListKt;
import kotlinx.coroutines.internal.LockFreeLinkedListNode;
import kotlinx.coroutines.internal.Removed;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* loaded from: classes.dex */
public abstract class JobNode<J extends Job> extends CompletionHandlerBase implements DisposableHandle, Incomplete {
    @NotNull
    public final J job;

    public JobNode(@NotNull J j) {
        Intrinsics.checkParameterIsNotNull(j, "job");
        this.job = j;
    }

    @Override // kotlinx.coroutines.DisposableHandle
    public void dispose() {
        Object state$kotlinx_coroutines_core;
        Object next;
        LockFreeLinkedListNode lockFreeLinkedListNode;
        Removed removed;
        Object next2;
        J j = this.job;
        if (j != null) {
            JobSupport jobSupport = (JobSupport) j;
            Intrinsics.checkParameterIsNotNull(this, "node");
            do {
                state$kotlinx_coroutines_core = jobSupport.getState$kotlinx_coroutines_core();
                if (state$kotlinx_coroutines_core instanceof JobNode) {
                    if (state$kotlinx_coroutines_core != this) {
                        return;
                    }
                } else if ((state$kotlinx_coroutines_core instanceof Incomplete) && ((Incomplete) state$kotlinx_coroutines_core).getList() != null) {
                    do {
                        next = getNext();
                        if ((next instanceof Removed) || next == this) {
                            return;
                        }
                        if (next != null) {
                            lockFreeLinkedListNode = (LockFreeLinkedListNode) next;
                            removed = (Removed) lockFreeLinkedListNode._removedRef;
                            if (removed == null) {
                                removed = new Removed(lockFreeLinkedListNode);
                                LockFreeLinkedListNode._removedRef$FU.lazySet(lockFreeLinkedListNode, removed);
                            }
                        } else {
                            throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.internal.Node /* = kotlinx.coroutines.internal.LockFreeLinkedListNode */");
                        }
                    } while (!LockFreeLinkedListNode._next$FU.compareAndSet(this, next, removed));
                    LockFreeLinkedListNode markPrev = markPrev();
                    Object obj = this._next;
                    if (obj != null) {
                        LockFreeLinkedListNode lockFreeLinkedListNode2 = ((Removed) obj).ref;
                        loop2: while (true) {
                            LockFreeLinkedListNode lockFreeLinkedListNode3 = lockFreeLinkedListNode2;
                            LockFreeLinkedListNode lockFreeLinkedListNode4 = null;
                            while (true) {
                                Object next3 = lockFreeLinkedListNode3.getNext();
                                if (next3 instanceof Removed) {
                                    lockFreeLinkedListNode3.markPrev();
                                    lockFreeLinkedListNode3 = ((Removed) next3).ref;
                                } else {
                                    next2 = markPrev.getNext();
                                    if (next2 instanceof Removed) {
                                        if (lockFreeLinkedListNode4 != null) {
                                            break;
                                        }
                                        markPrev = LockFreeLinkedListKt.unwrap(markPrev._prev);
                                    } else if (next2 != this) {
                                        if (next2 != null) {
                                            LockFreeLinkedListNode lockFreeLinkedListNode5 = (LockFreeLinkedListNode) next2;
                                            if (lockFreeLinkedListNode5 == lockFreeLinkedListNode3) {
                                                break loop2;
                                            }
                                            lockFreeLinkedListNode4 = markPrev;
                                            markPrev = lockFreeLinkedListNode5;
                                        } else {
                                            throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.internal.Node /* = kotlinx.coroutines.internal.LockFreeLinkedListNode */");
                                        }
                                    } else if (LockFreeLinkedListNode._next$FU.compareAndSet(markPrev, this, lockFreeLinkedListNode3)) {
                                        break loop2;
                                    }
                                }
                            }
                            markPrev.markPrev();
                            LockFreeLinkedListNode._next$FU.compareAndSet(lockFreeLinkedListNode4, markPrev, ((Removed) next2).ref);
                            markPrev = lockFreeLinkedListNode4;
                            lockFreeLinkedListNode2 = lockFreeLinkedListNode3;
                        }
                        lockFreeLinkedListNode.correctPrev(LockFreeLinkedListKt.unwrap(this._prev), null);
                        return;
                    }
                    throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.internal.Removed");
                } else {
                    return;
                }
            } while (!JobSupport._state$FU.compareAndSet(jobSupport, state$kotlinx_coroutines_core, JobSupportKt.EMPTY_ACTIVE));
            return;
        }
        throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.JobSupport");
    }

    @Override // kotlinx.coroutines.Incomplete
    @Nullable
    public NodeList getList() {
        return null;
    }

    @Override // kotlinx.coroutines.Incomplete
    public boolean isActive() {
        return true;
    }
}
