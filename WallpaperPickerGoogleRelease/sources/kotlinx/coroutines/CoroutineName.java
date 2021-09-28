package kotlinx.coroutines;

import java.util.Objects;
import kotlin.coroutines.AbstractCoroutineContextElement;
import kotlin.coroutines.CoroutineContext;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* loaded from: classes.dex */
public final class CoroutineName extends AbstractCoroutineContextElement {
    public static final Key Key = new Key(null);

    /* loaded from: classes.dex */
    public static final class Key implements CoroutineContext.Key<CoroutineName> {
        public Key(DefaultConstructorMarker defaultConstructorMarker) {
        }
    }

    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof CoroutineName)) {
            return false;
        }
        Objects.requireNonNull((CoroutineName) obj);
        return Intrinsics.areEqual(null, null);
    }

    public int hashCode() {
        return 0;
    }

    @NotNull
    public String toString() {
        return "CoroutineName(null)";
    }
}
