package com.android.wm.shell.animation;

import android.view.View;
import com.android.wm.shell.animation.PhysicsAnimator;
import java.util.WeakHashMap;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: PhysicsAnimator.kt */
/* loaded from: classes2.dex */
public final class PhysicsAnimatorKt {
    private static boolean verboseLogging;
    private static final WeakHashMap<Object, PhysicsAnimator<?>> animators = new WeakHashMap<>();
    private static final PhysicsAnimator.SpringConfig globalDefaultSpring = new PhysicsAnimator.SpringConfig(1500.0f, 0.5f);
    private static final float UNSET;
    private static final PhysicsAnimator.FlingConfig globalDefaultFling = new PhysicsAnimator.FlingConfig(1.0f, UNSET, Float.MAX_VALUE);

    public static final <T extends View> PhysicsAnimator<T> getPhysicsAnimator(T t) {
        Intrinsics.checkNotNullParameter(t, "<this>");
        return PhysicsAnimator.Companion.getInstance(t);
    }

    public static final WeakHashMap<Object, PhysicsAnimator<?>> getAnimators() {
        return animators;
    }
}
