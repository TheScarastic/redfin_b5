package kotlinx.coroutines.internal;

import java.util.Iterator;
import java.util.List;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.SequencesKt__SequencesKt;
import kotlin.sequences.SequencesKt___SequencesKt;
import kotlinx.coroutines.MainCoroutineDispatcher;
/* compiled from: MainDispatchers.kt */
/* loaded from: classes2.dex */
public final class MainDispatcherLoader {
    private static final boolean FAST_SERVICE_LOADER_ENABLED = SystemPropsKt.systemProp("kotlinx.coroutines.fast.service.loader", true);
    public static final MainDispatcherLoader INSTANCE;
    public static final MainCoroutineDispatcher dispatcher;

    static {
        MainDispatcherLoader mainDispatcherLoader = new MainDispatcherLoader();
        INSTANCE = mainDispatcherLoader;
        dispatcher = mainDispatcherLoader.loadMainDispatcher();
    }

    private MainDispatcherLoader() {
    }

    private final MainCoroutineDispatcher loadMainDispatcher() {
        List list;
        Object obj;
        MainCoroutineDispatcher tryCreateDispatcher;
        try {
            if (FAST_SERVICE_LOADER_ENABLED) {
                FastServiceLoader fastServiceLoader = FastServiceLoader.INSTANCE;
                ClassLoader classLoader = MainDispatcherFactory.class.getClassLoader();
                Intrinsics.checkExpressionValueIsNotNull(classLoader, "clz.classLoader");
                list = fastServiceLoader.load$kotlinx_coroutines_core(MainDispatcherFactory.class, classLoader);
            } else {
                Iterator m = MainDispatcherLoader$$ExternalSyntheticServiceLoad0.m();
                Intrinsics.checkExpressionValueIsNotNull(m, "ServiceLoader.load(\n    …             ).iterator()");
                list = SequencesKt___SequencesKt.toList(SequencesKt__SequencesKt.asSequence(m));
            }
            Iterator it = list.iterator();
            if (!it.hasNext()) {
                obj = null;
            } else {
                obj = it.next();
                if (it.hasNext()) {
                    int loadPriority = ((MainDispatcherFactory) obj).getLoadPriority();
                    do {
                        Object next = it.next();
                        int loadPriority2 = ((MainDispatcherFactory) next).getLoadPriority();
                        if (loadPriority < loadPriority2) {
                            obj = next;
                            loadPriority = loadPriority2;
                        }
                    } while (it.hasNext());
                }
            }
            MainDispatcherFactory mainDispatcherFactory = (MainDispatcherFactory) obj;
            if (mainDispatcherFactory == null || (tryCreateDispatcher = MainDispatchersKt.tryCreateDispatcher(mainDispatcherFactory, list)) == null) {
                return new MissingMainCoroutineDispatcher(null, null, 2, null);
            }
            return tryCreateDispatcher;
        } catch (Throwable th) {
            return new MissingMainCoroutineDispatcher(th, null, 2, null);
        }
    }
}
