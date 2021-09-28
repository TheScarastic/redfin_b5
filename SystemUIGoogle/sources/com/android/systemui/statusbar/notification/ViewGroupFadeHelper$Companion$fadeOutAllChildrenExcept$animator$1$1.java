package com.android.systemui.statusbar.notification;

import android.animation.ValueAnimator;
import android.view.View;
import android.view.ViewGroup;
import com.android.systemui.R$id;
import java.util.Objects;
import java.util.Set;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ViewGroupFadeHelper.kt */
/* loaded from: classes.dex */
final class ViewGroupFadeHelper$Companion$fadeOutAllChildrenExcept$animator$1$1 implements ValueAnimator.AnimatorUpdateListener {
    final /* synthetic */ ViewGroup $root;
    final /* synthetic */ Set<View> $viewsToFadeOut;

    /* access modifiers changed from: package-private */
    public ViewGroupFadeHelper$Companion$fadeOutAllChildrenExcept$animator$1$1(ViewGroup viewGroup, Set<View> set) {
        this.$root = viewGroup;
        this.$viewsToFadeOut = set;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        Float f = (Float) this.$root.getTag(R$id.view_group_fade_helper_previous_value_tag);
        Object animatedValue = valueAnimator.getAnimatedValue();
        Objects.requireNonNull(animatedValue, "null cannot be cast to non-null type kotlin.Float");
        float floatValue = ((Float) animatedValue).floatValue();
        for (View view : this.$viewsToFadeOut) {
            if (!Intrinsics.areEqual(view.getAlpha(), f)) {
                view.setTag(R$id.view_group_fade_helper_restore_tag, Float.valueOf(view.getAlpha()));
            }
            view.setAlpha(floatValue);
        }
        this.$root.setTag(R$id.view_group_fade_helper_previous_value_tag, Float.valueOf(floatValue));
    }
}
