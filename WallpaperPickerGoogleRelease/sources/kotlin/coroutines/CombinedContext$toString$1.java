package kotlin.coroutines;

import kotlin.coroutines.CoroutineContext;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* loaded from: classes.dex */
public final class CombinedContext$toString$1 extends Lambda implements Function2<String, CoroutineContext.Element, String> {
    public static final CombinedContext$toString$1 INSTANCE = new CombinedContext$toString$1();

    public CombinedContext$toString$1() {
        super(2);
    }

    /* Return type fixed from 'java.lang.Object' to match base method */
    /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object, java.lang.Object] */
    @Override // kotlin.jvm.functions.Function2
    public String invoke(String str, CoroutineContext.Element element) {
        String str2 = str;
        CoroutineContext.Element element2 = element;
        Intrinsics.checkNotNullParameter(str2, "acc");
        Intrinsics.checkNotNullParameter(element2, "element");
        if (str2.length() == 0) {
            return element2.toString();
        }
        return str2 + ", " + element2;
    }
}
