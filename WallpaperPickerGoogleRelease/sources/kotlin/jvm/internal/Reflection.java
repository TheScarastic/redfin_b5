package kotlin.jvm.internal;

import java.util.Objects;
import kotlin.reflect.KClass;
/* loaded from: classes.dex */
public class Reflection {
    public static final KClass[] EMPTY_K_CLASS_ARRAY;
    public static final ReflectionFactory factory;

    static {
        ReflectionFactory reflectionFactory = null;
        try {
            reflectionFactory = (ReflectionFactory) Class.forName("kotlin.reflect.jvm.internal.ReflectionFactoryImpl").newInstance();
        } catch (ClassCastException | ClassNotFoundException | IllegalAccessException | InstantiationException unused) {
        }
        if (reflectionFactory == null) {
            reflectionFactory = new ReflectionFactory();
        }
        factory = reflectionFactory;
        EMPTY_K_CLASS_ARRAY = new KClass[0];
    }

    public static KClass getOrCreateKotlinClass(Class cls) {
        Objects.requireNonNull(factory);
        return new ClassReference(cls);
    }
}
