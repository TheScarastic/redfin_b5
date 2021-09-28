package com.google.common.util.concurrent;

import androidx.viewpager2.widget.FakeDrag$$ExternalSyntheticOutline0;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.ByFunctionOrdering;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.NaturalOrdering;
import com.google.common.collect.Ordering;
import com.google.common.collect.ReverseOrdering;
import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
/* loaded from: classes.dex */
public final class FuturesGetChecked {
    public static final Ordering<Constructor<?>> WITH_STRING_PARAM_FIRST;

    /* loaded from: classes.dex */
    public interface GetCheckedTypeValidator {
        void validateClass(Class<? extends Exception> cls);
    }

    /* loaded from: classes.dex */
    public static class GetCheckedTypeValidatorHolder {
        public static final GetCheckedTypeValidator BEST_VALIDATOR;
        public static final String CLASS_VALUE_VALIDATOR_NAME;

        /* loaded from: classes.dex */
        public enum ClassValueValidator implements GetCheckedTypeValidator {
            INSTANCE;
            
            public static final ClassValue<Boolean> isValidClass = new ClassValue<Boolean>() { // from class: com.google.common.util.concurrent.FuturesGetChecked.GetCheckedTypeValidatorHolder.ClassValueValidator.1
            };

            @Override // com.google.common.util.concurrent.FuturesGetChecked.GetCheckedTypeValidator
            public void validateClass(Class<? extends Exception> cls) {
                isValidClass.get(cls);
            }
        }

        /* loaded from: classes.dex */
        public enum WeakSetValidator implements GetCheckedTypeValidator {
            INSTANCE;
            
            public static final Set<WeakReference<Class<? extends Exception>>> validClasses = new CopyOnWriteArraySet();

            @Override // com.google.common.util.concurrent.FuturesGetChecked.GetCheckedTypeValidator
            public void validateClass(Class<? extends Exception> cls) {
                Iterator it = ((CopyOnWriteArraySet) validClasses).iterator();
                while (it.hasNext()) {
                    if (cls.equals(((WeakReference) it.next()).get())) {
                        return;
                    }
                }
                FuturesGetChecked.checkExceptionClassValidity(cls);
                Set<WeakReference<Class<? extends Exception>>> set = validClasses;
                if (((CopyOnWriteArraySet) set).size() > 1000) {
                    ((CopyOnWriteArraySet) set).clear();
                }
                ((CopyOnWriteArraySet) set).add(new WeakReference(cls));
            }
        }

        /* JADX DEBUG: Multi-variable search result rejected for r0v7, resolved type: java.lang.Object[] */
        /* JADX WARN: Multi-variable type inference failed */
        static {
            GetCheckedTypeValidator getCheckedTypeValidator;
            String concat = GetCheckedTypeValidatorHolder.class.getName().concat("$ClassValueValidator");
            CLASS_VALUE_VALIDATOR_NAME = concat;
            try {
                getCheckedTypeValidator = (GetCheckedTypeValidator) Class.forName(concat).getEnumConstants()[0];
            } catch (Throwable unused) {
                getCheckedTypeValidator = FuturesGetChecked.weakSetValidator();
            }
            BEST_VALIDATOR = getCheckedTypeValidator;
        }
    }

    static {
        NaturalOrdering naturalOrdering = NaturalOrdering.INSTANCE;
        AnonymousClass1 r1 = new Function<Constructor<?>, Boolean>() { // from class: com.google.common.util.concurrent.FuturesGetChecked.1
            /* Return type fixed from 'java.lang.Object' to match base method */
            /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object] */
            @Override // com.google.common.base.Function
            public Boolean apply(Constructor<?> constructor) {
                return Boolean.valueOf(Arrays.asList(constructor.getParameterTypes()).contains(String.class));
            }
        };
        Objects.requireNonNull(naturalOrdering);
        WITH_STRING_PARAM_FIRST = new ReverseOrdering(new ByFunctionOrdering(r1, naturalOrdering));
    }

    public static void checkExceptionClassValidity(Class<? extends Exception> cls) {
        boolean z;
        Preconditions.checkArgument(isCheckedException(cls), "Futures.getChecked exception type (%s) must not be a RuntimeException", cls);
        try {
            newWithCause(cls, new Exception());
            z = true;
        } catch (Exception unused) {
            z = false;
        }
        Preconditions.checkArgument(z, "Futures.getChecked exception type (%s) must be an accessible class with an accessible constructor whose parameters (if any) must be of type String and/or Throwable", cls);
    }

    public static GetCheckedTypeValidator classValueValidator() {
        return GetCheckedTypeValidatorHolder.ClassValueValidator.INSTANCE;
    }

    public static <V, X extends Exception> V getChecked(GetCheckedTypeValidator getCheckedTypeValidator, Future<V> future, Class<X> cls) throws Exception {
        getCheckedTypeValidator.validateClass(cls);
        try {
            return future.get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw newWithCause(cls, e);
        } catch (ExecutionException e2) {
            Throwable cause = e2.getCause();
            if (cause instanceof Error) {
                throw new ExecutionError((Error) cause);
            } else if (cause instanceof RuntimeException) {
                throw new UncheckedExecutionException(cause);
            } else {
                throw newWithCause(cls, cause);
            }
        }
    }

    public static boolean isCheckedException(Class<? extends Exception> cls) {
        return !RuntimeException.class.isAssignableFrom(cls);
    }

    public static <X extends Exception> X newWithCause(Class<X> cls, Throwable th) {
        Object obj;
        List asList = Arrays.asList(cls.getConstructors());
        Ordering<Constructor<?>> ordering = WITH_STRING_PARAM_FIRST;
        Objects.requireNonNull(ordering);
        if (!(asList instanceof Collection)) {
            Iterator it = asList.iterator();
            ArrayList arrayList = new ArrayList();
            Iterators.addAll(arrayList, it);
            asList = arrayList;
        }
        Object[] array = asList.toArray();
        Arrays.sort(array, ordering);
        for (Constructor constructor : Lists.newArrayList(Arrays.asList(array))) {
            Class<?>[] parameterTypes = constructor.getParameterTypes();
            Object[] objArr = new Object[parameterTypes.length];
            int i = 0;
            while (true) {
                obj = null;
                if (i < parameterTypes.length) {
                    Class<?> cls2 = parameterTypes[i];
                    if (!cls2.equals(String.class)) {
                        if (!cls2.equals(Throwable.class)) {
                            break;
                        }
                        objArr[i] = th;
                    } else {
                        objArr[i] = th.toString();
                    }
                    i++;
                } else {
                    try {
                        obj = constructor.newInstance(objArr);
                        break;
                    } catch (IllegalAccessException | IllegalArgumentException | InstantiationException | InvocationTargetException unused) {
                    }
                }
            }
            X x = (X) ((Exception) obj);
            if (x != null) {
                if (x.getCause() == null) {
                    x.initCause(th);
                }
                return x;
            }
        }
        String valueOf = String.valueOf(cls);
        throw new IllegalArgumentException(FakeDrag$$ExternalSyntheticOutline0.m(valueOf.length() + 82, "No appropriate constructor for exception of type ", valueOf, " in response to chained exception"), th);
    }

    public static GetCheckedTypeValidator weakSetValidator() {
        return GetCheckedTypeValidatorHolder.WeakSetValidator.INSTANCE;
    }
}
