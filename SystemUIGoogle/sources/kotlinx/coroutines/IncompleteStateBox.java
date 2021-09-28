package kotlinx.coroutines;

import kotlin.jvm.internal.Intrinsics;
/* compiled from: JobSupport.kt */
/* access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class IncompleteStateBox {
    public final Incomplete state;

    public IncompleteStateBox(Incomplete incomplete) {
        Intrinsics.checkParameterIsNotNull(incomplete, "state");
        this.state = incomplete;
    }
}
