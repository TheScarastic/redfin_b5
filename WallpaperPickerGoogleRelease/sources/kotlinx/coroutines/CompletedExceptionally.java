package kotlinx.coroutines;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* loaded from: classes.dex */
public class CompletedExceptionally {
    public static final AtomicIntegerFieldUpdater _handled$FU = AtomicIntegerFieldUpdater.newUpdater(CompletedExceptionally.class, "_handled");
    public volatile int _handled;
    @NotNull
    public final Throwable cause;

    public CompletedExceptionally(Throwable th, boolean z, int i) {
        z = (i & 2) != 0 ? false : z;
        Intrinsics.checkParameterIsNotNull(th, "cause");
        this.cause = th;
        int i2 = z ? 1 : 0;
        int i3 = z ? 1 : 0;
        int i4 = z ? 1 : 0;
        this._handled = i2;
    }

    @NotNull
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Intrinsics.checkParameterIsNotNull(this, "$this$classSimpleName");
        sb.append("CompletedExceptionally");
        sb.append('[');
        sb.append(this.cause);
        sb.append(']');
        return sb.toString();
    }
}
