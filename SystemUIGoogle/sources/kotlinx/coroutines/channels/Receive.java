package kotlinx.coroutines.channels;

import kotlinx.coroutines.internal.LockFreeLinkedListNode;
/* compiled from: AbstractChannel.kt */
/* access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public abstract class Receive<E> extends LockFreeLinkedListNode implements ReceiveOrClosed<E> {
    public abstract void resumeReceiveClosed(Closed<?> closed);

    @Override // kotlinx.coroutines.channels.ReceiveOrClosed
    public Object getOfferResult() {
        return AbstractChannelKt.OFFER_SUCCESS;
    }
}
