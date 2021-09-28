package kotlin.coroutines;

import kotlin.coroutines.CoroutineContext;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: CoroutineContextImpl.kt */
/* loaded from: classes2.dex */
final class CombinedContext$toString$1 extends Lambda implements Function2<String, CoroutineContext.Element, String> {
    public static final CombinedContext$toString$1 INSTANCE = new CombinedContext$toString$1();

    CombinedContext$toString$1() {
        super(2);
    }

    public final String invoke(String str, CoroutineContext.Element element) {
        Intrinsics.checkNotNullParameter(str, "acc");
        Intrinsics.checkNotNullParameter(element, "element");
        if (str.length() == 0) {
            return element.toString();
        }
        return str + ", " + element;
    }
}
