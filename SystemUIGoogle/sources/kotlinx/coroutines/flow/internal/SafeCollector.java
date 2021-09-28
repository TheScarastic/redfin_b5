package kotlinx.coroutines.flow.internal;

import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.Job;
import kotlinx.coroutines.flow.FlowCollector;
import kotlinx.coroutines.internal.ScopeCoroutine;
/* compiled from: SafeCollector.kt */
/* loaded from: classes2.dex */
public final class SafeCollector<T> implements FlowCollector<T> {
    private final CoroutineContext collectContext;
    private final int collectContextSize;
    private final FlowCollector<T> collector;
    private CoroutineContext lastEmissionContext;

    /* JADX DEBUG: Multi-variable search result rejected for r2v0, resolved type: kotlinx.coroutines.flow.FlowCollector<? super T> */
    /* JADX WARN: Multi-variable type inference failed */
    public SafeCollector(FlowCollector<? super T> flowCollector, CoroutineContext coroutineContext) {
        Intrinsics.checkParameterIsNotNull(flowCollector, "collector");
        Intrinsics.checkParameterIsNotNull(coroutineContext, "collectContext");
        this.collector = flowCollector;
        this.collectContext = coroutineContext;
        this.collectContextSize = ((Number) coroutineContext.fold(0, SafeCollector$collectContextSize$1.INSTANCE)).intValue();
    }

    @Override // kotlinx.coroutines.flow.FlowCollector
    public Object emit(T t, Continuation<? super Unit> continuation) {
        CoroutineContext context = continuation.getContext();
        if (this.lastEmissionContext != context) {
            checkContext(context);
            this.lastEmissionContext = context;
        }
        return this.collector.emit(t, continuation);
    }

    private final void checkContext(CoroutineContext coroutineContext) {
        if (((Number) coroutineContext.fold(0, new Function2<Integer, CoroutineContext.Element, Integer>(this) { // from class: kotlinx.coroutines.flow.internal.SafeCollector$checkContext$result$1
            final /* synthetic */ SafeCollector this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
            }

            /* Return type fixed from 'java.lang.Object' to match base method */
            /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object, java.lang.Object] */
            @Override // kotlin.jvm.functions.Function2
            public /* bridge */ /* synthetic */ Integer invoke(Integer num, CoroutineContext.Element element) {
                return Integer.valueOf(invoke(num.intValue(), element));
            }

            public final int invoke(int i, CoroutineContext.Element element) {
                Intrinsics.checkParameterIsNotNull(element, "element");
                CoroutineContext.Key<?> key = element.getKey();
                CoroutineContext.Element element2 = this.this$0.collectContext.get(key);
                if (key == Job.Key) {
                    Job job = (Job) element2;
                    Job job2 = this.this$0.transitiveCoroutineParent((Job) element, job);
                    if (job2 == job) {
                        return job == null ? i : i + 1;
                    }
                    throw new IllegalStateException(("Flow invariant is violated:\n\t\tEmission from another coroutine is detected.\n\t\tChild of " + job2 + ", expected child of " + job + ".\n\t\tFlowCollector is not thread-safe and concurrent emissions are prohibited.\n\t\tTo mitigate this restriction please use 'channelFlow' builder instead of 'flow'").toString());
                } else if (element != element2) {
                    return Integer.MIN_VALUE;
                } else {
                    return i + 1;
                }
            }
        })).intValue() != this.collectContextSize) {
            throw new IllegalStateException(("Flow invariant is violated:\n\t\tFlow was collected in " + this.collectContext + ",\n\t\tbut emission happened in " + coroutineContext + ".\n\t\tPlease refer to 'flow' documentation or use 'flowOn' instead").toString());
        }
    }

    /* access modifiers changed from: private */
    public final Job transitiveCoroutineParent(Job job, Job job2) {
        while (job != null) {
            if (job == job2 || !(job instanceof ScopeCoroutine)) {
                return job;
            }
            job = ((ScopeCoroutine) job).getParent$kotlinx_coroutines_core();
        }
        return null;
    }
}
