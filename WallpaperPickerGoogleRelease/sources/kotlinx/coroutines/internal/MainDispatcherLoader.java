package kotlinx.coroutines.internal;

import java.util.Iterator;
import java.util.List;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.SequencesKt;
import kotlin.sequences.SequencesKt___SequencesJvmKt;
import kotlinx.coroutines.MainCoroutineDispatcher;
import org.jetbrains.annotations.NotNull;
/* loaded from: classes.dex */
public final class MainDispatcherLoader {
    public static final boolean FAST_SERVICE_LOADER_ENABLED;
    @NotNull
    public static final MainCoroutineDispatcher dispatcher;

    static {
        MainCoroutineDispatcher mainCoroutineDispatcher;
        List<? extends MainDispatcherFactory> list;
        Object obj;
        String systemProp = SystemPropsKt.systemProp("kotlinx.coroutines.fast.service.loader");
        boolean parseBoolean = systemProp != null ? Boolean.parseBoolean(systemProp) : true;
        FAST_SERVICE_LOADER_ENABLED = parseBoolean;
        try {
            if (parseBoolean) {
                ClassLoader classLoader = MainDispatcherFactory.class.getClassLoader();
                Intrinsics.checkExpressionValueIsNotNull(classLoader, "clz.classLoader");
                list = FastServiceLoader.loadProviders$kotlinx_coroutines_core(MainDispatcherFactory.class, classLoader);
            } else {
                Iterator m = MainDispatcherLoader$$ExternalSyntheticServiceLoad0.m();
                Intrinsics.checkExpressionValueIsNotNull(m, "ServiceLoader.load(\n    …             ).iterator()");
                list = SequencesKt.toList(SequencesKt___SequencesJvmKt.asSequence(m));
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
            if (mainDispatcherFactory == null || (mainCoroutineDispatcher = mainDispatcherFactory.createDispatcher(list)) == null) {
                mainCoroutineDispatcher = new MissingMainCoroutineDispatcher(null, null, 2);
            }
        } catch (Throwable th) {
            mainCoroutineDispatcher = new MissingMainCoroutineDispatcher(th, null, 2);
        }
        dispatcher = mainCoroutineDispatcher;
    }
}
