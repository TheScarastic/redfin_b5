package kotlinx.coroutines.channels;
/* compiled from: LinkedListChannel.kt */
/* loaded from: classes2.dex */
public class LinkedListChannel<E> extends AbstractChannel<E> {
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

    /* access modifiers changed from: protected */
    @Override // kotlinx.coroutines.channels.AbstractSendChannel
    public Object offerInternal(E e) {
        ReceiveOrClosed<?> sendBuffered;
        do {
            Object offerInternal = super.offerInternal(e);
            Object obj = AbstractChannelKt.OFFER_SUCCESS;
            if (offerInternal == obj) {
                return obj;
            }
            if (offerInternal == AbstractChannelKt.OFFER_FAILED) {
                sendBuffered = sendBuffered(e);
                if (sendBuffered == null) {
                    return obj;
                }
            } else if (offerInternal instanceof Closed) {
                return offerInternal;
            } else {
                throw new IllegalStateException(("Invalid offerInternal result " + offerInternal).toString());
            }
        } while (!(sendBuffered instanceof Closed));
        return sendBuffered;
    }
}
