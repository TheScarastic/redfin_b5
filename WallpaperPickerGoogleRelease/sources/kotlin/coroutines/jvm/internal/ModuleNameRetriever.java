package kotlin.coroutines.jvm.internal;

import java.lang.reflect.Method;
import org.jetbrains.annotations.Nullable;
/* loaded from: classes.dex */
public final class ModuleNameRetriever {
    @Nullable
    public static Cache cache;
    public static final Cache notOnJava9 = new Cache(null, null, null);

    /* loaded from: classes.dex */
    public static final class Cache {
        @Nullable
        public final Method getDescriptorMethod;
        @Nullable
        public final Method getModuleMethod;
        @Nullable
        public final Method nameMethod;

        public Cache(@Nullable Method method, @Nullable Method method2, @Nullable Method method3) {
            this.getModuleMethod = method;
            this.getDescriptorMethod = method2;
            this.nameMethod = method3;
        }
    }
}
