package kotlin.collections;

import java.util.Map;
import java.util.NoSuchElementException;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: MapWithDefault.kt */
/* access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class MapsKt__MapWithDefaultKt {
    public static final <K, V> V getOrImplicitDefaultNullable(Map<K, ? extends V> map, K k) {
        Intrinsics.checkNotNullParameter(map, "$this$getOrImplicitDefault");
        if (map instanceof MapWithDefault) {
            return (V) ((MapWithDefault) map).getOrImplicitDefault(k);
        }
        V v = (V) map.get(k);
        if (v != null || map.containsKey(k)) {
            return v;
        }
        throw new NoSuchElementException("Key " + k + " is missing in the map.");
    }

    public static <K, V> Map<K, V> withDefault(Map<K, ? extends V> map, Function1<? super K, ? extends V> function1) {
        Intrinsics.checkNotNullParameter(map, "$this$withDefault");
        Intrinsics.checkNotNullParameter(function1, "defaultValue");
        if (map instanceof MapWithDefault) {
            return withDefault(((MapWithDefault) map).getMap(), function1);
        }
        return new MapWithDefaultImpl(map, function1);
    }
}
