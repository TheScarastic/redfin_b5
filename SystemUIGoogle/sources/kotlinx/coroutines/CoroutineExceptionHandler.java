package kotlinx.coroutines;

import kotlin.coroutines.CoroutineContext;
/* compiled from: CoroutineExceptionHandler.kt */
/* loaded from: classes2.dex */
public interface CoroutineExceptionHandler extends CoroutineContext.Element {
    public static final Key Key = Key.$$INSTANCE;

    void handleException(CoroutineContext coroutineContext, Throwable th);

    /* compiled from: CoroutineExceptionHandler.kt */
    /* loaded from: classes2.dex */
    public static final class Key implements CoroutineContext.Key<CoroutineExceptionHandler> {
        static final /* synthetic */ Key $$INSTANCE = new Key();

        private Key() {
        }
    }
}
