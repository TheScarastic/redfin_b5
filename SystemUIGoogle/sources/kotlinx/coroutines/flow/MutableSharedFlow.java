package kotlinx.coroutines.flow;
/* compiled from: SharedFlow.kt */
/* loaded from: classes2.dex */
public interface MutableSharedFlow<T> extends FlowCollector<T>, FlowCollector {
    boolean tryEmit(T t);
}
