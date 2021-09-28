package kotlin;

import kotlin.internal.PlatformImplementationsKt;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: Exceptions.kt */
/* access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class ExceptionsKt__ExceptionsKt {
    public static void addSuppressed(Throwable th, Throwable th2) {
        Intrinsics.checkNotNullParameter(th, "$this$addSuppressed");
        Intrinsics.checkNotNullParameter(th2, "exception");
        if (th != th2) {
            PlatformImplementationsKt.IMPLEMENTATIONS.addSuppressed(th, th2);
        }
    }
}
