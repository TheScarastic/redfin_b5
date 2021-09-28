package kotlinx.coroutines.internal;

import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: ExceptionsConstuctor.kt */
/* access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class ExceptionsConstuctorKt$tryCopyException$5$1 extends Lambda implements Function1 {
    public static final ExceptionsConstuctorKt$tryCopyException$5$1 INSTANCE = new ExceptionsConstuctorKt$tryCopyException$5$1();

    ExceptionsConstuctorKt$tryCopyException$5$1() {
        super(1);
    }

    public final Void invoke(Throwable th) {
        Intrinsics.checkParameterIsNotNull(th, "it");
        return null;
    }
}
