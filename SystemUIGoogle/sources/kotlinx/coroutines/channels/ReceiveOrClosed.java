package kotlinx.coroutines.channels;
/* compiled from: AbstractChannel.kt */
/* loaded from: classes2.dex */
public interface ReceiveOrClosed<E> {
    void completeResumeReceive(Object obj);

    Object getOfferResult();

    Object tryResumeReceive(E e, Object obj);
}
