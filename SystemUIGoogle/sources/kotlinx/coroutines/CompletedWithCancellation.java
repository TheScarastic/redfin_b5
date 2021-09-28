package kotlinx.coroutines;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
/* compiled from: CancellableContinuationImpl.kt */
/* access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class CompletedWithCancellation {
    public final Function1<Throwable, Unit> onCancellation;
    public final Object result;

    public String toString() {
        return "CompletedWithCancellation[" + this.result + ']';
    }
}
