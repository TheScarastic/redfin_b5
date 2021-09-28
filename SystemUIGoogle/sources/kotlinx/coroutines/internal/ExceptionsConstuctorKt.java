package kotlinx.coroutines.internal;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Comparator;
import java.util.WeakHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.TypeCastException;
import kotlin.Unit;
import kotlin.collections.ArraysKt___ArraysKt;
import kotlin.comparisons.ComparisonsKt__ComparisonsKt;
import kotlin.jvm.JvmClassMappingKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.CopyableThrowable;
/* compiled from: ExceptionsConstuctor.kt */
/* loaded from: classes2.dex */
public final class ExceptionsConstuctorKt {
    private static final int throwableFields = fieldsCountOrDefault(Throwable.class, -1);
    private static final ReentrantReadWriteLock cacheLock = new ReentrantReadWriteLock();
    private static final WeakHashMap<Class<? extends Throwable>, Function1<Throwable, Throwable>> exceptionCtors = new WeakHashMap<>();

    public static final <E extends Throwable> E tryCopyException(E e) {
        Object obj;
        Intrinsics.checkParameterIsNotNull(e, "exception");
        Object obj2 = null;
        if (e instanceof CopyableThrowable) {
            try {
                Result.Companion companion = Result.Companion;
                obj = Result.m670constructorimpl(((CopyableThrowable) e).createCopy());
            } catch (Throwable th) {
                Result.Companion companion2 = Result.Companion;
                obj = Result.m670constructorimpl(ResultKt.createFailure(th));
            }
            if (!Result.m674isFailureimpl(obj)) {
                obj2 = obj;
            }
            return (E) ((Throwable) obj2);
        }
        ReentrantReadWriteLock reentrantReadWriteLock = cacheLock;
        ReentrantReadWriteLock.ReadLock readLock = reentrantReadWriteLock.readLock();
        readLock.lock();
        try {
            Function1<Throwable, Throwable> function1 = exceptionCtors.get(e.getClass());
            if (function1 != null) {
                return (E) function1.invoke(e);
            }
            int i = 0;
            if (throwableFields != fieldsCountOrDefault(e.getClass(), 0)) {
                ReentrantReadWriteLock.ReadLock readLock2 = reentrantReadWriteLock.readLock();
                int readHoldCount = reentrantReadWriteLock.getWriteHoldCount() == 0 ? reentrantReadWriteLock.getReadHoldCount() : 0;
                for (int i2 = 0; i2 < readHoldCount; i2++) {
                    readLock2.unlock();
                }
                ReentrantReadWriteLock.WriteLock writeLock = reentrantReadWriteLock.writeLock();
                writeLock.lock();
                try {
                    exceptionCtors.put(e.getClass(), ExceptionsConstuctorKt$tryCopyException$4$1.INSTANCE);
                    Unit unit = Unit.INSTANCE;
                    return null;
                } finally {
                    while (i < readHoldCount) {
                        readLock2.lock();
                        i++;
                    }
                    writeLock.unlock();
                }
            } else {
                Constructor<?>[] constructors = e.getClass().getConstructors();
                Intrinsics.checkExpressionValueIsNotNull(constructors, "exception.javaClass.constructors");
                Function1<Throwable, Throwable> function12 = null;
                for (Constructor constructor : ArraysKt___ArraysKt.sortedWith(constructors, new Comparator<T>() { // from class: kotlinx.coroutines.internal.ExceptionsConstuctorKt$tryCopyException$$inlined$sortedByDescending$1
                    @Override // java.util.Comparator
                    public final int compare(T t, T t2) {
                        int compareValues;
                        Constructor constructor2 = (Constructor) t2;
                        Intrinsics.checkExpressionValueIsNotNull(constructor2, "it");
                        Integer valueOf = Integer.valueOf(constructor2.getParameterTypes().length);
                        Constructor constructor3 = (Constructor) t;
                        Intrinsics.checkExpressionValueIsNotNull(constructor3, "it");
                        compareValues = ComparisonsKt__ComparisonsKt.compareValues(valueOf, Integer.valueOf(constructor3.getParameterTypes().length));
                        return compareValues;
                    }
                })) {
                    Intrinsics.checkExpressionValueIsNotNull(constructor, "constructor");
                    function12 = createConstructor(constructor);
                    if (function12 != null) {
                        break;
                    }
                }
                ReentrantReadWriteLock reentrantReadWriteLock2 = cacheLock;
                ReentrantReadWriteLock.ReadLock readLock3 = reentrantReadWriteLock2.readLock();
                int readHoldCount2 = reentrantReadWriteLock2.getWriteHoldCount() == 0 ? reentrantReadWriteLock2.getReadHoldCount() : 0;
                for (int i3 = 0; i3 < readHoldCount2; i3++) {
                    readLock3.unlock();
                }
                ReentrantReadWriteLock.WriteLock writeLock2 = reentrantReadWriteLock2.writeLock();
                writeLock2.lock();
                try {
                    exceptionCtors.put(e.getClass(), function12 != null ? function12 : ExceptionsConstuctorKt$tryCopyException$5$1.INSTANCE);
                    Unit unit2 = Unit.INSTANCE;
                    while (i < readHoldCount2) {
                        readLock3.lock();
                        i++;
                    }
                    writeLock2.unlock();
                    if (function12 != null) {
                        return (E) function12.invoke(e);
                    }
                    return null;
                } finally {
                    while (i < readHoldCount2) {
                        readLock3.lock();
                        i++;
                    }
                    writeLock2.unlock();
                }
            }
        } finally {
            readLock.unlock();
        }
    }

    private static final Function1<Throwable, Throwable> createConstructor(Constructor<?> constructor) {
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        int length = parameterTypes.length;
        if (length == 0) {
            return new Function1<Throwable, Throwable>(constructor) { // from class: kotlinx.coroutines.internal.ExceptionsConstuctorKt$createConstructor$$inlined$safeCtor$4
                final /* synthetic */ Constructor $constructor$inlined;

                {
                    this.$constructor$inlined = r1;
                }

                public final Throwable invoke(Throwable th) {
                    Object obj;
                    Object newInstance;
                    Intrinsics.checkParameterIsNotNull(th, "e");
                    try {
                        Result.Companion companion = Result.Companion;
                        newInstance = this.$constructor$inlined.newInstance(new Object[0]);
                    } catch (Throwable th2) {
                        Result.Companion companion2 = Result.Companion;
                        obj = Result.m670constructorimpl(ResultKt.createFailure(th2));
                    }
                    if (newInstance != null) {
                        Throwable th3 = (Throwable) newInstance;
                        th3.initCause(th);
                        obj = Result.m670constructorimpl(th3);
                        if (Result.m674isFailureimpl(obj)) {
                            obj = null;
                        }
                        return (Throwable) obj;
                    }
                    throw new TypeCastException("null cannot be cast to non-null type kotlin.Throwable");
                }
            };
        }
        if (length == 1) {
            Class<?> cls = parameterTypes[0];
            if (Intrinsics.areEqual(cls, Throwable.class)) {
                return new Function1<Throwable, Throwable>(constructor) { // from class: kotlinx.coroutines.internal.ExceptionsConstuctorKt$createConstructor$$inlined$safeCtor$2
                    final /* synthetic */ Constructor $constructor$inlined;

                    {
                        this.$constructor$inlined = r1;
                    }

                    public final Throwable invoke(Throwable th) {
                        Object obj;
                        Object newInstance;
                        Intrinsics.checkParameterIsNotNull(th, "e");
                        try {
                            Result.Companion companion = Result.Companion;
                            newInstance = this.$constructor$inlined.newInstance(th);
                        } catch (Throwable th2) {
                            Result.Companion companion2 = Result.Companion;
                            obj = Result.m670constructorimpl(ResultKt.createFailure(th2));
                        }
                        if (newInstance != null) {
                            obj = Result.m670constructorimpl((Throwable) newInstance);
                            if (Result.m674isFailureimpl(obj)) {
                                obj = null;
                            }
                            return (Throwable) obj;
                        }
                        throw new TypeCastException("null cannot be cast to non-null type kotlin.Throwable");
                    }
                };
            }
            if (Intrinsics.areEqual(cls, String.class)) {
                return new Function1<Throwable, Throwable>(constructor) { // from class: kotlinx.coroutines.internal.ExceptionsConstuctorKt$createConstructor$$inlined$safeCtor$3
                    final /* synthetic */ Constructor $constructor$inlined;

                    {
                        this.$constructor$inlined = r1;
                    }

                    public final Throwable invoke(Throwable th) {
                        Object obj;
                        Object newInstance;
                        Intrinsics.checkParameterIsNotNull(th, "e");
                        try {
                            Result.Companion companion = Result.Companion;
                            newInstance = this.$constructor$inlined.newInstance(th.getMessage());
                        } catch (Throwable th2) {
                            Result.Companion companion2 = Result.Companion;
                            obj = Result.m670constructorimpl(ResultKt.createFailure(th2));
                        }
                        if (newInstance != null) {
                            Throwable th3 = (Throwable) newInstance;
                            th3.initCause(th);
                            obj = Result.m670constructorimpl(th3);
                            if (Result.m674isFailureimpl(obj)) {
                                obj = null;
                            }
                            return (Throwable) obj;
                        }
                        throw new TypeCastException("null cannot be cast to non-null type kotlin.Throwable");
                    }
                };
            }
            return null;
        } else if (length == 2 && Intrinsics.areEqual(parameterTypes[0], String.class) && Intrinsics.areEqual(parameterTypes[1], Throwable.class)) {
            return new Function1<Throwable, Throwable>(constructor) { // from class: kotlinx.coroutines.internal.ExceptionsConstuctorKt$createConstructor$$inlined$safeCtor$1
                final /* synthetic */ Constructor $constructor$inlined;

                {
                    this.$constructor$inlined = r1;
                }

                public final Throwable invoke(Throwable th) {
                    Object obj;
                    Object newInstance;
                    Intrinsics.checkParameterIsNotNull(th, "e");
                    try {
                        Result.Companion companion = Result.Companion;
                        newInstance = this.$constructor$inlined.newInstance(th.getMessage(), th);
                    } catch (Throwable th2) {
                        Result.Companion companion2 = Result.Companion;
                        obj = Result.m670constructorimpl(ResultKt.createFailure(th2));
                    }
                    if (newInstance != null) {
                        obj = Result.m670constructorimpl((Throwable) newInstance);
                        if (Result.m674isFailureimpl(obj)) {
                            obj = null;
                        }
                        return (Throwable) obj;
                    }
                    throw new TypeCastException("null cannot be cast to non-null type kotlin.Throwable");
                }
            };
        } else {
            return null;
        }
    }

    private static final int fieldsCountOrDefault(Class<?> cls, int i) {
        Object obj;
        JvmClassMappingKt.getKotlinClass(cls);
        try {
            Result.Companion companion = Result.Companion;
            obj = Result.m670constructorimpl(Integer.valueOf(fieldsCount$default(cls, 0, 1, null)));
        } catch (Throwable th) {
            Result.Companion companion2 = Result.Companion;
            obj = Result.m670constructorimpl(ResultKt.createFailure(th));
        }
        obj = Integer.valueOf(i);
        if (Result.m674isFailureimpl(obj)) {
        }
        return ((Number) obj).intValue();
    }

    static /* synthetic */ int fieldsCount$default(Class cls, int i, int i2, Object obj) {
        if ((i2 & 1) != 0) {
            i = 0;
        }
        return fieldsCount(cls, i);
    }

    private static final int fieldsCount(Class<?> cls, int i) {
        do {
            Field[] declaredFields = cls.getDeclaredFields();
            Intrinsics.checkExpressionValueIsNotNull(declaredFields, "declaredFields");
            int i2 = 0;
            for (Field field : declaredFields) {
                Intrinsics.checkExpressionValueIsNotNull(field, "it");
                if (!Modifier.isStatic(field.getModifiers())) {
                    i2++;
                }
            }
            i += i2;
            cls = cls.getSuperclass();
        } while (cls != null);
        return i;
    }
}
