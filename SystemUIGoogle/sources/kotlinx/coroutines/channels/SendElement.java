package kotlinx.coroutines.channels;

import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.CancellableContinuation;
/* compiled from: AbstractChannel.kt */
/* loaded from: classes2.dex */
public final class SendElement extends Send {
    public final CancellableContinuation<Unit> cont;
    private final Object pollResult;

    @Override // kotlinx.coroutines.channels.Send
    public Object getPollResult() {
        return this.pollResult;
    }

    /* JADX DEBUG: Multi-variable search result rejected for r3v0, resolved type: kotlinx.coroutines.CancellableContinuation<? super kotlin.Unit> */
    /* JADX WARN: Multi-variable type inference failed */
    public SendElement(Object obj, CancellableContinuation<? super Unit> cancellableContinuation) {
        Intrinsics.checkParameterIsNotNull(cancellableContinuation, "cont");
        this.pollResult = obj;
        this.cont = cancellableContinuation;
    }

    @Override // kotlinx.coroutines.channels.Send
    public Object tryResumeSend(Object obj) {
        return this.cont.tryResume(Unit.INSTANCE, obj);
    }

    @Override // kotlinx.coroutines.channels.Send
    public void completeResumeSend(Object obj) {
        Intrinsics.checkParameterIsNotNull(obj, "token");
        this.cont.completeResume(obj);
    }

    @Override // kotlinx.coroutines.internal.LockFreeLinkedListNode
    public String toString() {
        return "SendElement(" + getPollResult() + ')';
    }
}
