package kotlin.collections;

import java.util.Map;
import kotlin.jvm.internal.markers.KMappedMarker;
/* compiled from: MapWithDefault.kt */
/* access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public interface MapWithDefault<K, V> extends Map<K, V>, KMappedMarker {
    Map<K, V> getMap();

    V getOrImplicitDefault(K k);
}
