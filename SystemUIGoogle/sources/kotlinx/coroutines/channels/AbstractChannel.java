package kotlinx.coroutines.channels;

import kotlin.Result;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsJvmKt;
import kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsKt;
import kotlin.coroutines.jvm.internal.Boxing;
import kotlin.coroutines.jvm.internal.DebugProbesKt;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.CancelHandler;
import kotlinx.coroutines.CancellableContinuation;
import kotlinx.coroutines.CancellableContinuationImpl;
import kotlinx.coroutines.internal.StackTraceRecoveryKt;
/* compiled from: AbstractChannel.kt */
/* loaded from: classes2.dex */
public abstract class AbstractChannel<E> extends AbstractSendChannel<E> implements Channel<E> {
    protected abstract boolean isBufferAlwaysEmpty();

    /* access modifiers changed from: protected */
    public abstract boolean isBufferEmpty();

    protected void onReceiveDequeued() {
    }

    protected void onReceiveEnqueued() {
    }

    /* compiled from: AbstractChannel.kt */
    /* loaded from: classes2.dex */
    private static final class Itr<E> implements ChannelIterator<E> {
        private final AbstractChannel<E> channel;
        private Object result = AbstractChannelKt.POLL_FAILED;

        final /* synthetic */ Object hasNextSuspend(Continuation<? super Boolean> continuation) {
            CancellableContinuationImpl cancellableContinuationImpl = new CancellableContinuationImpl(IntrinsicsKt__IntrinsicsJvmKt.intercepted(continuation), 0);
            ReceiveHasNext receiveHasNext = new ReceiveHasNext(this, cancellableContinuationImpl);
            while (true) {
                if (!getChannel().enqueueReceive(receiveHasNext)) {
                    Object pollInternal = getChannel().pollInternal();
                    setResult(pollInternal);
                    if (!(pollInternal instanceof Closed)) {
                        if (pollInternal != AbstractChannelKt.POLL_FAILED) {
                            Boolean boxBoolean = Boxing.boxBoolean(true);
                            Result.Companion companion = Result.Companion;
                            cancellableContinuationImpl.resumeWith(Result.m670constructorimpl(boxBoolean));
                            break;
                        }
                    } else {
                        Closed closed = (Closed) pollInternal;
                        if (closed.closeCause == null) {
                            Boolean boxBoolean2 = Boxing.boxBoolean(false);
                            Result.Companion companion2 = Result.Companion;
                            cancellableContinuationImpl.resumeWith(Result.m670constructorimpl(boxBoolean2));
                        } else {
                            Throwable receiveException = closed.getReceiveException();
                            Result.Companion companion3 = Result.Companion;
                            cancellableContinuationImpl.resumeWith(Result.m670constructorimpl(ResultKt.createFailure(receiveException)));
                        }
                    }
                } else {
                    getChannel().removeReceiveOnCancel(cancellableContinuationImpl, receiveHasNext);
                    break;
                }
            }
            Object result = cancellableContinuationImpl.getResult();
            if (result == IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
                DebugProbesKt.probeCoroutineSuspended(continuation);
            }
            return result;
        }

        public Itr(AbstractChannel<E> abstractChannel) {
            Intrinsics.checkParameterIsNotNull(abstractChannel, "channel");
            this.channel = abstractChannel;
        }

        public final AbstractChannel<E> getChannel() {
            return this.channel;
        }

        public final void setResult(Object obj) {
            this.result = obj;
        }

        @Override // kotlinx.coroutines.channels.ChannelIterator
        public Object hasNext(Continuation<? super Boolean> continuation) {
            Object obj = this.result;
            Object obj2 = AbstractChannelKt.POLL_FAILED;
            if (obj != obj2) {
                return Boxing.boxBoolean(hasNextResult(obj));
            }
            Object pollInternal = this.channel.pollInternal();
            this.result = pollInternal;
            if (pollInternal != obj2) {
                return Boxing.boxBoolean(hasNextResult(pollInternal));
            }
            return hasNextSuspend(continuation);
        }

        private final boolean hasNextResult(Object obj) {
            if (!(obj instanceof Closed)) {
                return true;
            }
            Closed closed = (Closed) obj;
            if (closed.closeCause == null) {
                return false;
            }
            throw StackTraceRecoveryKt.recoverStackTrace(closed.getReceiveException());
        }

        @Override // kotlinx.coroutines.channels.ChannelIterator
        public E next() {
            E e = (E) this.result;
            if (!(e instanceof Closed)) {
                Object obj = AbstractChannelKt.POLL_FAILED;
                if (e != obj) {
                    this.result = obj;
                    return e;
                }
                throw new IllegalStateException("'hasNext' should be called prior to 'next' invocation");
            }
            throw StackTraceRecoveryKt.recoverStackTrace(((Closed) e).getReceiveException());
        }
    }

    protected Object pollInternal() {
        Send takeFirstSendOrPeekClosed;
        Object tryResumeSend;
        do {
            takeFirstSendOrPeekClosed = takeFirstSendOrPeekClosed();
            if (takeFirstSendOrPeekClosed == null) {
                return AbstractChannelKt.POLL_FAILED;
            }
            tryResumeSend = takeFirstSendOrPeekClosed.tryResumeSend(null);
        } while (tryResumeSend == null);
        takeFirstSendOrPeekClosed.completeResumeSend(tryResumeSend);
        return takeFirstSendOrPeekClosed.getPollResult();
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x004d  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final boolean enqueueReceive(kotlinx.coroutines.channels.Receive<? super E> r8) {
        /*
            r7 = this;
            boolean r0 = r7.isBufferAlwaysEmpty()
        */
        //  java.lang.String r1 = "null cannot be cast to non-null type kotlinx.coroutines.internal.Node /* = kotlinx.coroutines.internal.LockFreeLinkedListNode */"
        /*
            r2 = 0
            r3 = 1
            if (r0 == 0) goto L_0x0029
            kotlinx.coroutines.internal.LockFreeLinkedListHead r0 = r7.getQueue()
        L_0x000e:
            java.lang.Object r4 = r0.getPrev()
            if (r4 == 0) goto L_0x0023
            kotlinx.coroutines.internal.LockFreeLinkedListNode r4 = (kotlinx.coroutines.internal.LockFreeLinkedListNode) r4
            boolean r5 = r4 instanceof kotlinx.coroutines.channels.Send
            r5 = r5 ^ r3
            if (r5 != 0) goto L_0x001c
            goto L_0x004b
        L_0x001c:
            boolean r4 = r4.addNext(r8, r0)
            if (r4 == 0) goto L_0x000e
            goto L_0x004a
        L_0x0023:
            kotlin.TypeCastException r7 = new kotlin.TypeCastException
            r7.<init>(r1)
            throw r7
        L_0x0029:
            kotlinx.coroutines.internal.LockFreeLinkedListHead r0 = r7.getQueue()
            kotlinx.coroutines.channels.AbstractChannel$enqueueReceive$$inlined$addLastIfPrevAndIf$1 r4 = new kotlinx.coroutines.channels.AbstractChannel$enqueueReceive$$inlined$addLastIfPrevAndIf$1
            r4.<init>(r8, r8, r7)
        L_0x0032:
            java.lang.Object r5 = r0.getPrev()
            if (r5 == 0) goto L_0x0051
            kotlinx.coroutines.internal.LockFreeLinkedListNode r5 = (kotlinx.coroutines.internal.LockFreeLinkedListNode) r5
            boolean r6 = r5 instanceof kotlinx.coroutines.channels.Send
            r6 = r6 ^ r3
            if (r6 != 0) goto L_0x0040
            goto L_0x004b
        L_0x0040:
            int r5 = r5.tryCondAddNext(r8, r0, r4)
            if (r5 == r3) goto L_0x004a
            r6 = 2
            if (r5 == r6) goto L_0x004b
            goto L_0x0032
        L_0x004a:
            r2 = r3
        L_0x004b:
            if (r2 == 0) goto L_0x0050
            r7.onReceiveEnqueued()
        L_0x0050:
            return r2
        L_0x0051:
            kotlin.TypeCastException r7 = new kotlin.TypeCastException
            r7.<init>(r1)
            throw r7
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.channels.AbstractChannel.enqueueReceive(kotlinx.coroutines.channels.Receive):boolean");
    }

    @Override // kotlinx.coroutines.channels.ReceiveChannel
    public final ChannelIterator<E> iterator() {
        return new Itr(this);
    }

    /* access modifiers changed from: protected */
    @Override // kotlinx.coroutines.channels.AbstractSendChannel
    public ReceiveOrClosed<E> takeFirstReceiveOrPeekClosed() {
        ReceiveOrClosed<E> takeFirstReceiveOrPeekClosed = super.takeFirstReceiveOrPeekClosed();
        if (takeFirstReceiveOrPeekClosed != null && !(takeFirstReceiveOrPeekClosed instanceof Closed)) {
            onReceiveDequeued();
        }
        return takeFirstReceiveOrPeekClosed;
    }

    /* access modifiers changed from: private */
    public final void removeReceiveOnCancel(CancellableContinuation<?> cancellableContinuation, Receive<?> receive) {
        cancellableContinuation.invokeOnCancellation(new RemoveReceiveOnCancel(this, receive));
    }

    /* compiled from: AbstractChannel.kt */
    /* access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public final class RemoveReceiveOnCancel extends CancelHandler {
        private final Receive<?> receive;
        final /* synthetic */ AbstractChannel this$0;

        public RemoveReceiveOnCancel(AbstractChannel abstractChannel, Receive<?> receive) {
            Intrinsics.checkParameterIsNotNull(receive, "receive");
            this.this$0 = abstractChannel;
            this.receive = receive;
        }

        /* Return type fixed from 'java.lang.Object' to match base method */
        /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object] */
        @Override // kotlin.jvm.functions.Function1
        public /* bridge */ /* synthetic */ Unit invoke(Throwable th) {
            invoke(th);
            return Unit.INSTANCE;
        }

        @Override // kotlinx.coroutines.CancelHandlerBase
        public void invoke(Throwable th) {
            if (this.receive.remove()) {
                this.this$0.onReceiveDequeued();
            }
        }

        public String toString() {
            return "RemoveReceiveOnCancel[" + this.receive + ']';
        }
    }

    /* compiled from: AbstractChannel.kt */
    /* access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static final class ReceiveHasNext<E> extends Receive<E> {
        public final CancellableContinuation<Boolean> cont;
        public final Itr<E> iterator;

        @Override // kotlinx.coroutines.internal.LockFreeLinkedListNode
        public String toString() {
            return "ReceiveHasNext";
        }

        /* JADX DEBUG: Multi-variable search result rejected for r3v0, resolved type: kotlinx.coroutines.CancellableContinuation<? super java.lang.Boolean> */
        /* JADX WARN: Multi-variable type inference failed */
        public ReceiveHasNext(Itr<E> itr, CancellableContinuation<? super Boolean> cancellableContinuation) {
            Intrinsics.checkParameterIsNotNull(itr, "iterator");
            Intrinsics.checkParameterIsNotNull(cancellableContinuation, "cont");
            this.iterator = itr;
            this.cont = cancellableContinuation;
        }

        @Override // kotlinx.coroutines.channels.ReceiveOrClosed
        public Object tryResumeReceive(E e, Object obj) {
            Object tryResume = this.cont.tryResume(Boolean.TRUE, obj);
            if (tryResume != null) {
                if (obj != null) {
                    return new IdempotentTokenValue(tryResume, e);
                }
                this.iterator.setResult(e);
            }
            return tryResume;
        }

        @Override // kotlinx.coroutines.channels.ReceiveOrClosed
        public void completeResumeReceive(Object obj) {
            Intrinsics.checkParameterIsNotNull(obj, "token");
            if (obj instanceof IdempotentTokenValue) {
                IdempotentTokenValue idempotentTokenValue = (IdempotentTokenValue) obj;
                this.iterator.setResult(idempotentTokenValue.value);
                this.cont.completeResume(idempotentTokenValue.token);
                return;
            }
            this.cont.completeResume(obj);
        }

        @Override // kotlinx.coroutines.channels.Receive
        public void resumeReceiveClosed(Closed<?> closed) {
            Object obj;
            Intrinsics.checkParameterIsNotNull(closed, "closed");
            if (closed.closeCause == null) {
                obj = CancellableContinuation.DefaultImpls.tryResume$default(this.cont, Boolean.FALSE, null, 2, null);
            } else {
                obj = this.cont.tryResumeWithException(StackTraceRecoveryKt.recoverStackTrace(closed.getReceiveException(), this.cont));
            }
            if (obj != null) {
                this.iterator.setResult(closed);
                this.cont.completeResume(obj);
            }
        }
    }

    /* compiled from: AbstractChannel.kt */
    /* loaded from: classes2.dex */
    private static final class IdempotentTokenValue<E> {
        public final Object token;
        public final E value;

        public IdempotentTokenValue(Object obj, E e) {
            Intrinsics.checkParameterIsNotNull(obj, "token");
            this.token = obj;
            this.value = e;
        }
    }
}
