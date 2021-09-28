package kotlin.coroutines;

import kotlin.coroutines.ContinuationInterceptor;
import kotlin.coroutines.CoroutineContext;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* loaded from: classes.dex */
public final class CoroutineContext$plus$1 extends Lambda implements Function2<CoroutineContext, CoroutineContext.Element, CoroutineContext> {
    public static final CoroutineContext$plus$1 INSTANCE = new CoroutineContext$plus$1();

    public CoroutineContext$plus$1() {
        super(2);
    }

    /* Return type fixed from 'java.lang.Object' to match base method */
    /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object, java.lang.Object] */
    @Override // kotlin.jvm.functions.Function2
    public CoroutineContext invoke(CoroutineContext coroutineContext, CoroutineContext.Element element) {
        CombinedContext combinedContext;
        CoroutineContext coroutineContext2 = coroutineContext;
        CoroutineContext.Element element2 = element;
        Intrinsics.checkNotNullParameter(coroutineContext2, "acc");
        Intrinsics.checkNotNullParameter(element2, "element");
        CoroutineContext minusKey = coroutineContext2.minusKey(element2.getKey());
        EmptyCoroutineContext emptyCoroutineContext = EmptyCoroutineContext.INSTANCE;
        if (minusKey == emptyCoroutineContext) {
            return element2;
        }
        int i = ContinuationInterceptor.$r8$clinit;
        ContinuationInterceptor.Key key = ContinuationInterceptor.Key.$$INSTANCE;
        ContinuationInterceptor continuationInterceptor = (ContinuationInterceptor) minusKey.get(key);
        if (continuationInterceptor == null) {
            combinedContext = new CombinedContext(minusKey, element2);
        } else {
            CoroutineContext minusKey2 = minusKey.minusKey(key);
            if (minusKey2 == emptyCoroutineContext) {
                return new CombinedContext(element2, continuationInterceptor);
            }
            combinedContext = new CombinedContext(new CombinedContext(minusKey2, element2), continuationInterceptor);
        }
        return combinedContext;
    }
}
