package com.android.systemui.statusbar.notification;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import com.android.systemui.R$id;
import com.android.systemui.animation.Interpolators;
import java.util.LinkedHashSet;
import java.util.Set;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.TypeIntrinsics;
/* compiled from: ViewGroupFadeHelper.kt */
/* loaded from: classes.dex */
public final class ViewGroupFadeHelper {
    public static final Companion Companion = new Companion(null);
    private static final Function1<View, Boolean> visibilityIncluder = ViewGroupFadeHelper$Companion$visibilityIncluder$1.INSTANCE;

    public static final void fadeOutAllChildrenExcept(ViewGroup viewGroup, View view, long j, Runnable runnable) {
        Companion.fadeOutAllChildrenExcept(viewGroup, view, j, runnable);
    }

    public static final void reset(ViewGroup viewGroup) {
        Companion.reset(viewGroup);
    }

    /* compiled from: ViewGroupFadeHelper.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final void fadeOutAllChildrenExcept(ViewGroup viewGroup, View view, long j, Runnable runnable) {
            Intrinsics.checkNotNullParameter(viewGroup, "root");
            Intrinsics.checkNotNullParameter(view, "excludedView");
            Set<View> gatherViews = gatherViews(viewGroup, view, ViewGroupFadeHelper.visibilityIncluder);
            for (View view2 : gatherViews) {
                if (view2.getHasOverlappingRendering() && view2.getLayerType() == 0) {
                    view2.setLayerType(2, null);
                    view2.setTag(R$id.view_group_fade_helper_hardware_layer, Boolean.TRUE);
                }
            }
            ValueAnimator ofFloat = ValueAnimator.ofFloat(1.0f, 0.0f);
            ofFloat.setDuration(j);
            ofFloat.setInterpolator(Interpolators.ALPHA_OUT);
            ofFloat.addUpdateListener(new ViewGroupFadeHelper$Companion$fadeOutAllChildrenExcept$animator$1$1(viewGroup, gatherViews));
            ofFloat.addListener(new ViewGroupFadeHelper$Companion$fadeOutAllChildrenExcept$animator$1$2(runnable));
            ofFloat.start();
            viewGroup.setTag(R$id.view_group_fade_helper_modified_views, gatherViews);
            viewGroup.setTag(R$id.view_group_fade_helper_animator, ofFloat);
        }

        private final Set<View> gatherViews(ViewGroup viewGroup, View view, Function1<? super View, Boolean> function1) {
            LinkedHashSet linkedHashSet = new LinkedHashSet();
            ViewParent parent = view.getParent();
            ViewGroup viewGroup2 = view;
            while (true) {
                ViewGroup viewGroup3 = (ViewGroup) parent;
                if (viewGroup3 == null) {
                    break;
                }
                int i = 0;
                int childCount = viewGroup3.getChildCount();
                if (childCount > 0) {
                    while (true) {
                        int i2 = i + 1;
                        View childAt = viewGroup3.getChildAt(i);
                        Intrinsics.checkNotNullExpressionValue(childAt, "child");
                        if (function1.invoke(childAt).booleanValue() && !Intrinsics.areEqual(viewGroup2, childAt)) {
                            linkedHashSet.add(childAt);
                        }
                        if (i2 >= childCount) {
                            break;
                        }
                        i = i2;
                    }
                }
                if (Intrinsics.areEqual(viewGroup3, viewGroup)) {
                    break;
                }
                parent = viewGroup3.getParent();
                viewGroup2 = viewGroup3;
            }
            return linkedHashSet;
        }

        public final void reset(ViewGroup viewGroup) {
            Intrinsics.checkNotNullParameter(viewGroup, "root");
            Set<View> asMutableSet = TypeIntrinsics.asMutableSet(viewGroup.getTag(R$id.view_group_fade_helper_modified_views));
            Animator animator = (Animator) viewGroup.getTag(R$id.view_group_fade_helper_animator);
            if (!(asMutableSet == null || animator == null)) {
                animator.cancel();
                Float f = (Float) viewGroup.getTag(R$id.view_group_fade_helper_previous_value_tag);
                for (View view : asMutableSet) {
                    int i = R$id.view_group_fade_helper_restore_tag;
                    Float f2 = (Float) view.getTag(i);
                    if (f2 != null) {
                        if (Intrinsics.areEqual(f, view.getAlpha())) {
                            view.setAlpha(f2.floatValue());
                        }
                        int i2 = R$id.view_group_fade_helper_hardware_layer;
                        if (Intrinsics.areEqual((Boolean) view.getTag(i2), Boolean.TRUE)) {
                            view.setLayerType(0, null);
                            view.setTag(i2, null);
                        }
                        view.setTag(i, null);
                    }
                }
                viewGroup.setTag(R$id.view_group_fade_helper_modified_views, null);
                viewGroup.setTag(R$id.view_group_fade_helper_previous_value_tag, null);
                viewGroup.setTag(R$id.view_group_fade_helper_animator, null);
            }
        }
    }
}
