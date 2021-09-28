package kotlinx.coroutines.channels;

import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.channels.AbstractSendChannel;
import kotlinx.coroutines.internal.LockFreeLinkedListHead;
import kotlinx.coroutines.internal.LockFreeLinkedListNode;
/* compiled from: ConflatedChannel.kt */
/* loaded from: classes2.dex */
public class ConflatedChannel<E> extends AbstractChannel<E> {
    @Override // kotlinx.coroutines.channels.AbstractChannel
    protected final boolean isBufferAlwaysEmpty() {
        return true;
    }

    @Override // kotlinx.coroutines.channels.AbstractSendChannel
    protected final boolean isBufferAlwaysFull() {
        return false;
    }

    /* access modifiers changed from: protected */
    @Override // kotlinx.coroutines.channels.AbstractChannel
    public final boolean isBufferEmpty() {
        return true;
    }

    /* access modifiers changed from: protected */
    @Override // kotlinx.coroutines.channels.AbstractSendChannel
    public final boolean isBufferFull() {
        return false;
    }

    @Override // kotlinx.coroutines.channels.AbstractSendChannel
    protected void onClosedIdempotent(LockFreeLinkedListNode lockFreeLinkedListNode) {
        Intrinsics.checkParameterIsNotNull(lockFreeLinkedListNode, "closed");
        LockFreeLinkedListNode prevNode = lockFreeLinkedListNode.getPrevNode();
        if (!(prevNode instanceof AbstractSendChannel.SendBuffered)) {
            prevNode = null;
        }
        AbstractSendChannel.SendBuffered<? extends E> sendBuffered = (AbstractSendChannel.SendBuffered) prevNode;
        if (sendBuffered != null) {
            conflatePreviousSendBuffered(sendBuffered);
        }
    }

    private final ReceiveOrClosed<?> sendConflated(E e) {
        LockFreeLinkedListNode lockFreeLinkedListNode;
        AbstractSendChannel.SendBuffered<? extends E> sendBuffered = new AbstractSendChannel.SendBuffered<>(e);
        LockFreeLinkedListHead queue = getQueue();
        do {
            Object prev = queue.getPrev();
            if (prev != null) {
                lockFreeLinkedListNode = (LockFreeLinkedListNode) prev;
                if (lockFreeLinkedListNode instanceof ReceiveOrClosed) {
                    return (ReceiveOrClosed) lockFreeLinkedListNode;
                }
            } else {
                throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.internal.Node /* = kotlinx.coroutines.internal.LockFreeLinkedListNode */");
            }
        } while (!lockFreeLinkedListNode.addNext(sendBuffered, queue));
        conflatePreviousSendBuffered(sendBuffered);
        return null;
    }

    private final void conflatePreviousSendBuffered(AbstractSendChannel.SendBuffered<? extends E> sendBuffered) {
        for (LockFreeLinkedListNode prevNode = sendBuffered.getPrevNode(); prevNode instanceof AbstractSendChannel.SendBuffered; prevNode = prevNode.getPrevNode()) {
            if (!prevNode.remove()) {
                prevNode.helpRemove();
            }
        }
    }

    /* access modifiers changed from: protected */
    @Override // kotlinx.coroutines.channels.AbstractSendChannel
    public Object offerInternal(E e) {
        ReceiveOrClosed<?> sendConflated;
        do {
            Object offerInternal = super.offerInternal(e);
            Object obj = AbstractChannelKt.OFFER_SUCCESS;
            if (offerInternal == obj) {
                return obj;
            }
            if (offerInternal == AbstractChannelKt.OFFER_FAILED) {
                sendConflated = sendConflated(e);
                if (sendConflated == null) {
                    return obj;
                }
            } else if (offerInternal instanceof Closed) {
                return offerInternal;
            } else {
                throw new IllegalStateException(("Invalid offerInternal result " + offerInternal).toString());
            }
        } while (!(sendConflated instanceof Closed));
        return sendConflated;
    }
}
