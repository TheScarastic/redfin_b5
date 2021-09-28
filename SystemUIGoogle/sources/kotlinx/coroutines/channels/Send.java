package kotlinx.coroutines.channels;

import kotlinx.coroutines.internal.LockFreeLinkedListNode;
/* compiled from: AbstractChannel.kt */
/* loaded from: classes2.dex */
public abstract class Send extends LockFreeLinkedListNode {
    public abstract void completeResumeSend(Object obj);

    public abstract Object getPollResult();

    public abstract Object tryResumeSend(Object obj);
}
