package kotlin.reflect;

import kotlin.jvm.functions.Function1;
import kotlin.reflect.KProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* loaded from: classes.dex */
public interface KProperty1<T, V> extends KProperty<V>, Function1<T, V> {

    /* loaded from: classes.dex */
    public interface Getter<T, V> extends KProperty.Getter<V>, Function1<T, V> {
    }

    V get(T t);

    @Nullable
    Object getDelegate(T t);

    @NotNull
    Getter<T, V> getGetter();
}
