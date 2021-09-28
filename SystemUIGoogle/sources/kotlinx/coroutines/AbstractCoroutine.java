package kotlinx.coroutines;

import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: AbstractCoroutine.kt */
/* loaded from: classes2.dex */
public abstract class AbstractCoroutine<T> extends JobSupport implements Job, Continuation<T> {
    private final CoroutineContext context;
    protected final CoroutineContext parentContext;

    public int getDefaultResumeMode$kotlinx_coroutines_core() {
        return 0;
    }

    protected void onCancelled(Throwable th, boolean z) {
        Intrinsics.checkParameterIsNotNull(th, "cause");
    }

    protected void onCompleted(T t) {
    }

    protected void onStart() {
    }

    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public AbstractCoroutine(CoroutineContext coroutineContext, boolean z) {
        super(z);
        Intrinsics.checkParameterIsNotNull(coroutineContext, "parentContext");
        this.parentContext = coroutineContext;
        this.context = coroutineContext.plus(this);
    }

    @Override // kotlin.coroutines.Continuation
    public final CoroutineContext getContext() {
        return this.context;
    }

    public CoroutineContext getCoroutineContext() {
        return this.context;
    }

    @Override // kotlinx.coroutines.JobSupport, kotlinx.coroutines.Job
    public boolean isActive() {
        return super.isActive();
    }

    public final void initParentJob$kotlinx_coroutines_core() {
        initParentJobInternal$kotlinx_coroutines_core((Job) this.parentContext.get(Job.Key));
    }

    @Override // kotlinx.coroutines.JobSupport
    public final void onStartInternal$kotlinx_coroutines_core() {
        onStart();
    }

    /* JADX DEBUG: Multi-variable search result rejected for r2v0, resolved type: java.lang.Object */
    /* JADX WARN: Multi-variable type inference failed */
    @Override // kotlinx.coroutines.JobSupport
    protected final void onCompletionInternal(Object obj) {
        if (obj instanceof CompletedExceptionally) {
            CompletedExceptionally completedExceptionally = (CompletedExceptionally) obj;
            onCancelled(completedExceptionally.cause, completedExceptionally.getHandled());
            return;
        }
        onCompleted(obj);
    }

    @Override // kotlin.coroutines.Continuation
    public final void resumeWith(Object obj) {
        makeCompletingOnce$kotlinx_coroutines_core(CompletedExceptionallyKt.toState(obj), getDefaultResumeMode$kotlinx_coroutines_core());
    }

    @Override // kotlinx.coroutines.JobSupport
    public final void handleOnCompletionException$kotlinx_coroutines_core(Throwable th) {
        Intrinsics.checkParameterIsNotNull(th, "exception");
        CoroutineExceptionHandlerKt.handleCoroutineException(this.context, th);
    }

    @Override // kotlinx.coroutines.JobSupport
    public String nameString$kotlinx_coroutines_core() {
        String coroutineName = CoroutineContextKt.getCoroutineName(this.context);
        if (coroutineName == null) {
            return super.nameString$kotlinx_coroutines_core();
        }
        return '\"' + coroutineName + "\":" + super.nameString$kotlinx_coroutines_core();
    }

    public final <R> void start(CoroutineStart coroutineStart, R r, Function2<? super R, ? super Continuation<? super T>, ? extends Object> function2) {
        Intrinsics.checkParameterIsNotNull(coroutineStart, "start");
        Intrinsics.checkParameterIsNotNull(function2, "block");
        initParentJob$kotlinx_coroutines_core();
        coroutineStart.invoke(function2, r, this);
    }
}
