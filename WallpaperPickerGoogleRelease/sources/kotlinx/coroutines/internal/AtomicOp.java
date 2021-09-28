package kotlinx.coroutines.internal;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import kotlinx.coroutines.DebugKt;
import org.jetbrains.annotations.Nullable;
/* loaded from: classes.dex */
public abstract class AtomicOp<T> extends OpDescriptor {
    public static final AtomicReferenceFieldUpdater _consensus$FU = AtomicReferenceFieldUpdater.newUpdater(AtomicOp.class, Object.class, "_consensus");
    public volatile Object _consensus = AtomicKt.NO_DECISION;

    public abstract void complete(T t, @Nullable Object obj);

    /* JADX DEBUG: Multi-variable search result rejected for r4v0, resolved type: java.lang.Object */
    /* JADX WARN: Multi-variable type inference failed */
    @Override // kotlinx.coroutines.internal.OpDescriptor
    @Nullable
    public final Object perform(@Nullable Object obj) {
        Object obj2 = this._consensus;
        Object obj3 = AtomicKt.NO_DECISION;
        if (obj2 == obj3) {
            obj2 = prepare(obj);
            boolean z = DebugKt.DEBUG;
            if (!_consensus$FU.compareAndSet(this, obj3, obj2)) {
                obj2 = this._consensus;
            }
        }
        complete(obj, obj2);
        return obj2;
    }

    @Nullable
    public abstract Object prepare(T t);
}
