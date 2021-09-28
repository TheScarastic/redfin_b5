package androidx.lifecycle;

import androidx.lifecycle.Lifecycle;
import java.util.Objects;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* loaded from: classes.dex */
public final class LifecycleController$observer$1 implements LifecycleEventObserver {
    @Override // androidx.lifecycle.LifecycleEventObserver
    public final void onStateChanged(@NotNull LifecycleOwner lifecycleOwner, @NotNull Lifecycle.Event event) {
        Intrinsics.checkNotNullParameter(lifecycleOwner, "source");
        Intrinsics.checkNotNullParameter(event, "<anonymous parameter 1>");
        Lifecycle lifecycle = lifecycleOwner.getLifecycle();
        Intrinsics.checkNotNullExpressionValue(lifecycle, "source.lifecycle");
        if (((LifecycleRegistry) lifecycle).mState == Lifecycle.State.DESTROYED) {
            throw null;
        }
        Lifecycle lifecycle2 = lifecycleOwner.getLifecycle();
        Intrinsics.checkNotNullExpressionValue(lifecycle2, "source.lifecycle");
        LifecycleRegistry lifecycleRegistry = (LifecycleRegistry) lifecycle2;
        Objects.requireNonNull(null);
        throw null;
    }
}
