package kotlinx.coroutines.internal;

import java.util.List;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.MainCoroutineDispatcher;
/* compiled from: MainDispatchers.kt */
/* loaded from: classes2.dex */
public final class MainDispatchersKt {
    public static final MainCoroutineDispatcher tryCreateDispatcher(MainDispatcherFactory mainDispatcherFactory, List<? extends MainDispatcherFactory> list) {
        Intrinsics.checkParameterIsNotNull(mainDispatcherFactory, "$this$tryCreateDispatcher");
        Intrinsics.checkParameterIsNotNull(list, "factories");
        try {
            return mainDispatcherFactory.createDispatcher(list);
        } catch (Throwable th) {
            return new MissingMainCoroutineDispatcher(th, mainDispatcherFactory.hintOnError());
        }
    }
}
