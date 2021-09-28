package kotlinx.coroutines.channels;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.TypeCastException;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsJvmKt;
import kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugProbesKt;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.CancellableContinuationImpl;
import kotlinx.coroutines.CancellableContinuationKt;
import kotlinx.coroutines.DebugKt;
import kotlinx.coroutines.DebugStringsKt;
import kotlinx.coroutines.internal.LockFreeLinkedListHead;
import kotlinx.coroutines.internal.LockFreeLinkedListNode;
import kotlinx.coroutines.internal.StackTraceRecoveryKt;
/* compiled from: AbstractChannel.kt */
/* loaded from: classes2.dex */
public abstract class AbstractSendChannel<E> implements SendChannel<E> {
    private static final AtomicReferenceFieldUpdater onCloseHandler$FU = AtomicReferenceFieldUpdater.newUpdater(AbstractSendChannel.class, Object.class, "onCloseHandler");
    private final LockFreeLinkedListHead queue = new LockFreeLinkedListHead();
    private volatile Object onCloseHandler = null;

    protected String getBufferDebugString() {
        return "";
    }

    protected abstract boolean isBufferAlwaysFull();

    /* access modifiers changed from: protected */
    public abstract boolean isBufferFull();

    protected void onClosedIdempotent(LockFreeLinkedListNode lockFreeLinkedListNode) {
        Intrinsics.checkParameterIsNotNull(lockFreeLinkedListNode, "closed");
    }

    /* access modifiers changed from: protected */
    public final LockFreeLinkedListHead getQueue() {
        return this.queue;
    }

    /* access modifiers changed from: protected */
    public Object offerInternal(E e) {
        ReceiveOrClosed<E> takeFirstReceiveOrPeekClosed;
        Object tryResumeReceive;
        do {
            takeFirstReceiveOrPeekClosed = takeFirstReceiveOrPeekClosed();
            if (takeFirstReceiveOrPeekClosed == null) {
                return AbstractChannelKt.OFFER_FAILED;
            }
            tryResumeReceive = takeFirstReceiveOrPeekClosed.tryResumeReceive(e, null);
        } while (tryResumeReceive == null);
        takeFirstReceiveOrPeekClosed.completeResumeReceive(tryResumeReceive);
        return takeFirstReceiveOrPeekClosed.getOfferResult();
    }

    /* access modifiers changed from: protected */
    public final Closed<?> getClosedForSend() {
        LockFreeLinkedListNode prevNode = this.queue.getPrevNode();
        if (!(prevNode instanceof Closed)) {
            prevNode = null;
        }
        Closed<?> closed = (Closed) prevNode;
        if (closed == null) {
            return null;
        }
        helpClose(closed);
        return closed;
    }

    /* access modifiers changed from: protected */
    public final Send takeFirstSendOrPeekClosed() {
        LockFreeLinkedListNode lockFreeLinkedListNode;
        LockFreeLinkedListHead lockFreeLinkedListHead = this.queue;
        while (true) {
            Object next = lockFreeLinkedListHead.getNext();
            if (next != null) {
                lockFreeLinkedListNode = (LockFreeLinkedListNode) next;
                lockFreeLinkedListNode = null;
                if (lockFreeLinkedListNode != lockFreeLinkedListHead && (lockFreeLinkedListNode instanceof Send)) {
                    if ((((Send) lockFreeLinkedListNode) instanceof Closed) || lockFreeLinkedListNode.remove()) {
                        break;
                    }
                    lockFreeLinkedListNode.helpDelete();
                }
            } else {
                throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.internal.Node /* = kotlinx.coroutines.internal.LockFreeLinkedListNode */");
            }
        }
        return (Send) lockFreeLinkedListNode;
    }

    /* access modifiers changed from: protected */
    public final ReceiveOrClosed<?> sendBuffered(E e) {
        LockFreeLinkedListNode lockFreeLinkedListNode;
        LockFreeLinkedListHead lockFreeLinkedListHead = this.queue;
        SendBuffered sendBuffered = new SendBuffered(e);
        do {
            Object prev = lockFreeLinkedListHead.getPrev();
            if (prev != null) {
                lockFreeLinkedListNode = (LockFreeLinkedListNode) prev;
                if (lockFreeLinkedListNode instanceof ReceiveOrClosed) {
                    return (ReceiveOrClosed) lockFreeLinkedListNode;
                }
            } else {
                throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.internal.Node /* = kotlinx.coroutines.internal.LockFreeLinkedListNode */");
            }
        } while (!lockFreeLinkedListNode.addNext(sendBuffered, lockFreeLinkedListHead));
        return null;
    }

    /* access modifiers changed from: private */
    public final boolean getFull() {
        return !(this.queue.getNextNode() instanceof ReceiveOrClosed) && isBufferFull();
    }

    @Override // kotlinx.coroutines.channels.SendChannel
    public final Object send(E e, Continuation<? super Unit> continuation) {
        if (offer(e)) {
            return Unit.INSTANCE;
        }
        return sendSuspend(e, continuation);
    }

    public final boolean offer(E e) {
        Throwable sendException;
        Throwable recoverStackTrace;
        Object offerInternal = offerInternal(e);
        if (offerInternal == AbstractChannelKt.OFFER_SUCCESS) {
            return true;
        }
        if (offerInternal == AbstractChannelKt.OFFER_FAILED) {
            Closed<?> closedForSend = getClosedForSend();
            if (closedForSend == null || (sendException = closedForSend.getSendException()) == null || (recoverStackTrace = StackTraceRecoveryKt.recoverStackTrace(sendException)) == null) {
                return false;
            }
            throw recoverStackTrace;
        } else if (offerInternal instanceof Closed) {
            throw StackTraceRecoveryKt.recoverStackTrace(((Closed) offerInternal).getSendException());
        } else {
            throw new IllegalStateException(("offerInternal returned " + offerInternal).toString());
        }
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0044, code lost:
        if (r3 != false) goto L_0x0049;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0048, code lost:
        return kotlinx.coroutines.channels.AbstractChannelKt.ENQUEUE_FAILED;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0049, code lost:
        return null;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final java.lang.Object enqueueSend(kotlinx.coroutines.channels.Send r5) {
        /*
            r4 = this;
            boolean r0 = r4.isBufferAlwaysFull()
        */
        //  java.lang.String r1 = "null cannot be cast to non-null type kotlinx.coroutines.internal.Node /* = kotlinx.coroutines.internal.LockFreeLinkedListNode */"
        /*
            if (r0 == 0) goto L_0x0024
            kotlinx.coroutines.internal.LockFreeLinkedListHead r4 = r4.queue
        L_0x000a:
            java.lang.Object r0 = r4.getPrev()
            if (r0 == 0) goto L_0x001e
            kotlinx.coroutines.internal.LockFreeLinkedListNode r0 = (kotlinx.coroutines.internal.LockFreeLinkedListNode) r0
            boolean r2 = r0 instanceof kotlinx.coroutines.channels.ReceiveOrClosed
            if (r2 == 0) goto L_0x0017
            return r0
        L_0x0017:
            boolean r0 = r0.addNext(r5, r4)
            if (r0 == 0) goto L_0x000a
            goto L_0x0049
        L_0x001e:
            kotlin.TypeCastException r4 = new kotlin.TypeCastException
            r4.<init>(r1)
            throw r4
        L_0x0024:
            kotlinx.coroutines.internal.LockFreeLinkedListHead r0 = r4.queue
            kotlinx.coroutines.channels.AbstractSendChannel$enqueueSend$$inlined$addLastIfPrevAndIf$1 r2 = new kotlinx.coroutines.channels.AbstractSendChannel$enqueueSend$$inlined$addLastIfPrevAndIf$1
            r2.<init>(r5, r5, r4)
        L_0x002b:
            java.lang.Object r4 = r0.getPrev()
            if (r4 == 0) goto L_0x004b
            kotlinx.coroutines.internal.LockFreeLinkedListNode r4 = (kotlinx.coroutines.internal.LockFreeLinkedListNode) r4
            boolean r3 = r4 instanceof kotlinx.coroutines.channels.ReceiveOrClosed
            if (r3 == 0) goto L_0x0038
            return r4
        L_0x0038:
            int r4 = r4.tryCondAddNext(r5, r0, r2)
            r3 = 1
            if (r4 == r3) goto L_0x0044
            r3 = 2
            if (r4 == r3) goto L_0x0043
            goto L_0x002b
        L_0x0043:
            r3 = 0
        L_0x0044:
            if (r3 != 0) goto L_0x0049
            java.lang.Object r4 = kotlinx.coroutines.channels.AbstractChannelKt.ENQUEUE_FAILED
            return r4
        L_0x0049:
            r4 = 0
            return r4
        L_0x004b:
            kotlin.TypeCastException r4 = new kotlin.TypeCastException
            r4.<init>(r1)
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.channels.AbstractSendChannel.enqueueSend(kotlinx.coroutines.channels.Send):java.lang.Object");
    }

    final /* synthetic */ Object sendSuspend(E e, Continuation<? super Unit> continuation) {
        CancellableContinuationImpl cancellableContinuationImpl = new CancellableContinuationImpl(IntrinsicsKt__IntrinsicsJvmKt.intercepted(continuation), 0);
        while (true) {
            if (getFull()) {
                SendElement sendElement = new SendElement(e, cancellableContinuationImpl);
                Object enqueueSend = enqueueSend(sendElement);
                if (enqueueSend == null) {
                    CancellableContinuationKt.removeOnCancellation(cancellableContinuationImpl, sendElement);
                    break;
                } else if (enqueueSend instanceof Closed) {
                    Closed closed = (Closed) enqueueSend;
                    helpClose(closed);
                    Throwable sendException = closed.getSendException();
                    Result.Companion companion = Result.Companion;
                    cancellableContinuationImpl.resumeWith(Result.m670constructorimpl(ResultKt.createFailure(sendException)));
                    break;
                } else if (enqueueSend != AbstractChannelKt.ENQUEUE_FAILED && !(enqueueSend instanceof Receive)) {
                    throw new IllegalStateException(("enqueueSend returned " + enqueueSend).toString());
                }
            }
            Object offerInternal = offerInternal(e);
            if (offerInternal == AbstractChannelKt.OFFER_SUCCESS) {
                Unit unit = Unit.INSTANCE;
                Result.Companion companion2 = Result.Companion;
                cancellableContinuationImpl.resumeWith(Result.m670constructorimpl(unit));
                break;
            } else if (offerInternal != AbstractChannelKt.OFFER_FAILED) {
                if (offerInternal instanceof Closed) {
                    Closed closed2 = (Closed) offerInternal;
                    helpClose(closed2);
                    Throwable sendException2 = closed2.getSendException();
                    Result.Companion companion3 = Result.Companion;
                    cancellableContinuationImpl.resumeWith(Result.m670constructorimpl(ResultKt.createFailure(sendException2)));
                } else {
                    throw new IllegalStateException(("offerInternal returned " + offerInternal).toString());
                }
            }
        }
        Object result = cancellableContinuationImpl.getResult();
        if (result == IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
            DebugProbesKt.probeCoroutineSuspended(continuation);
        }
        return result;
    }

    /* access modifiers changed from: private */
    public final void helpClose(Closed<?> closed) {
        while (true) {
            LockFreeLinkedListNode prevNode = closed.getPrevNode();
            if ((prevNode instanceof LockFreeLinkedListHead) || !(prevNode instanceof Receive)) {
                break;
            } else if (!prevNode.remove()) {
                prevNode.helpRemove();
            } else {
                ((Receive) prevNode).resumeReceiveClosed(closed);
            }
        }
        onClosedIdempotent(closed);
    }

    /* access modifiers changed from: protected */
    public ReceiveOrClosed<E> takeFirstReceiveOrPeekClosed() {
        LockFreeLinkedListNode lockFreeLinkedListNode;
        LockFreeLinkedListHead lockFreeLinkedListHead = this.queue;
        while (true) {
            Object next = lockFreeLinkedListHead.getNext();
            if (next != null) {
                lockFreeLinkedListNode = (LockFreeLinkedListNode) next;
                lockFreeLinkedListNode = null;
                if (lockFreeLinkedListNode != lockFreeLinkedListHead && (lockFreeLinkedListNode instanceof ReceiveOrClosed)) {
                    if ((((ReceiveOrClosed) lockFreeLinkedListNode) instanceof Closed) || lockFreeLinkedListNode.remove()) {
                        break;
                    }
                    lockFreeLinkedListNode.helpDelete();
                }
            } else {
                throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.internal.Node /* = kotlinx.coroutines.internal.LockFreeLinkedListNode */");
            }
        }
        return (ReceiveOrClosed) lockFreeLinkedListNode;
    }

    public String toString() {
        return DebugStringsKt.getClassSimpleName(this) + '@' + DebugStringsKt.getHexAddress(this) + '{' + getQueueDebugStateString() + '}' + getBufferDebugString();
    }

    private final String getQueueDebugStateString() {
        String str;
        LockFreeLinkedListNode nextNode = this.queue.getNextNode();
        if (nextNode == this.queue) {
            return "EmptyQueue";
        }
        if (nextNode instanceof Closed) {
            str = nextNode.toString();
        } else if (nextNode instanceof Receive) {
            str = "ReceiveQueued";
        } else if (nextNode instanceof Send) {
            str = "SendQueued";
        } else {
            str = "UNEXPECTED:" + nextNode;
        }
        LockFreeLinkedListNode prevNode = this.queue.getPrevNode();
        if (prevNode == nextNode) {
            return str;
        }
        String str2 = str + ",queueSize=" + countQueueSize();
        if (!(prevNode instanceof Closed)) {
            return str2;
        }
        return str2 + ",closedForSend=" + prevNode;
    }

    private final int countQueueSize() {
        LockFreeLinkedListHead lockFreeLinkedListHead = this.queue;
        Object next = lockFreeLinkedListHead.getNext();
        if (next != null) {
            int i = 0;
            for (LockFreeLinkedListNode lockFreeLinkedListNode = (LockFreeLinkedListNode) next; !Intrinsics.areEqual(lockFreeLinkedListNode, lockFreeLinkedListHead); lockFreeLinkedListNode = lockFreeLinkedListNode.getNextNode()) {
                if (lockFreeLinkedListNode instanceof LockFreeLinkedListNode) {
                    i++;
                }
            }
            return i;
        }
        throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.internal.Node /* = kotlinx.coroutines.internal.LockFreeLinkedListNode */");
    }

    /* compiled from: AbstractChannel.kt */
    /* loaded from: classes2.dex */
    public static final class SendBuffered<E> extends Send {
        public final E element;

        public SendBuffered(E e) {
            this.element = e;
        }

        @Override // kotlinx.coroutines.channels.Send
        public Object getPollResult() {
            return this.element;
        }

        @Override // kotlinx.coroutines.channels.Send
        public Object tryResumeSend(Object obj) {
            return AbstractChannelKt.SEND_RESUMED;
        }

        @Override // kotlinx.coroutines.channels.Send
        public void completeResumeSend(Object obj) {
            Intrinsics.checkParameterIsNotNull(obj, "token");
            if (DebugKt.getASSERTIONS_ENABLED()) {
                if (!(obj == AbstractChannelKt.SEND_RESUMED)) {
                    throw new AssertionError();
                }
            }
        }
    }
}
