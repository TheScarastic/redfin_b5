package kotlin.reflect;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
/* compiled from: KProperty.kt */
/* loaded from: classes2.dex */
public interface KMutableProperty0<V> extends KProperty0<V>, KProperty {

    /* compiled from: KProperty.kt */
    /* loaded from: classes2.dex */
    public interface Setter<V> extends Function1<V, Unit>, Function1 {
    }

    Setter<V> getSetter();

    void set(V v);
}
