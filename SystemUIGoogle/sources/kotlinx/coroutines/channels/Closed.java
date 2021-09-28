package kotlinx.coroutines.channels;

import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.DebugKt;
/* compiled from: AbstractChannel.kt */
/* loaded from: classes2.dex */
public final class Closed<E> extends Send implements ReceiveOrClosed<E> {
    public final Throwable closeCause;

    @Override // kotlinx.coroutines.channels.ReceiveOrClosed
    public Closed<E> getOfferResult() {
        return this;
    }

    @Override // kotlinx.coroutines.channels.Send
    public Closed<E> getPollResult() {
        return this;
    }

    public final Throwable getSendException() {
        Throwable th = this.closeCause;
        return th != null ? th : new ClosedSendChannelException("Channel was closed");
    }

    public final Throwable getReceiveException() {
        Throwable th = this.closeCause;
        return th != null ? th : new ClosedReceiveChannelException("Channel was closed");
    }

    @Override // kotlinx.coroutines.channels.Send
    public Object tryResumeSend(Object obj) {
        return AbstractChannelKt.CLOSE_RESUMED;
    }

    @Override // kotlinx.coroutines.channels.Send
    public void completeResumeSend(Object obj) {
        Intrinsics.checkParameterIsNotNull(obj, "token");
        if (DebugKt.getASSERTIONS_ENABLED()) {
            if (!(obj == AbstractChannelKt.CLOSE_RESUMED)) {
                throw new AssertionError();
            }
        }
    }

    @Override // kotlinx.coroutines.channels.ReceiveOrClosed
    public Object tryResumeReceive(E e, Object obj) {
        return AbstractChannelKt.CLOSE_RESUMED;
    }

    @Override // kotlinx.coroutines.channels.ReceiveOrClosed
    public void completeResumeReceive(Object obj) {
        Intrinsics.checkParameterIsNotNull(obj, "token");
        if (DebugKt.getASSERTIONS_ENABLED()) {
            if (!(obj == AbstractChannelKt.CLOSE_RESUMED)) {
                throw new AssertionError();
            }
        }
    }

    @Override // kotlinx.coroutines.internal.LockFreeLinkedListNode
    public String toString() {
        return "Closed[" + this.closeCause + ']';
    }
}
