package kotlinx.coroutines.internal;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.WeakHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Reflection;
import org.jetbrains.annotations.NotNull;
/* loaded from: classes.dex */
public final class ExceptionsConstuctorKt {
    public static final int throwableFields = fieldsCountOrDefault(Throwable.class, -1);
    public static final ReentrantReadWriteLock cacheLock = new ReentrantReadWriteLock();
    public static final WeakHashMap<Class<? extends Throwable>, Function1<Throwable, Throwable>> exceptionCtors = new WeakHashMap<>();

    public static final int fieldsCountOrDefault(@NotNull Class<?> cls, int i) {
        Object obj;
        Reflection.getOrCreateKotlinClass(cls);
        int i2 = 0;
        while (true) {
            try {
                Field[] declaredFields = cls.getDeclaredFields();
                Intrinsics.checkExpressionValueIsNotNull(declaredFields, "declaredFields");
                int i3 = 0;
                for (Field field : declaredFields) {
                    Intrinsics.checkExpressionValueIsNotNull(field, "it");
                    if (!Modifier.isStatic(field.getModifiers())) {
                        i3++;
                    }
                }
                i2 += i3;
                cls = cls.getSuperclass();
                if (cls == null) {
                    break;
                }
            } catch (Throwable th) {
                obj = ResultKt.createFailure(th);
            }
        }
        obj = Integer.valueOf(i2);
        Object valueOf = Integer.valueOf(i);
        if (obj instanceof Result.Failure) {
            obj = valueOf;
        }
        return ((Number) obj).intValue();
    }
}
