package com.android.systemui.controls.management;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Interpolator;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import com.android.systemui.R$dimen;
import com.android.systemui.animation.Interpolators;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ControlsAnimations.kt */
/* loaded from: classes.dex */
public final class ControlsAnimations {
    public static final ControlsAnimations INSTANCE = new ControlsAnimations();
    private static float translationY = -1.0f;

    private ControlsAnimations() {
    }

    public final LifecycleObserver observerForAnimations(ViewGroup viewGroup, Window window, Intent intent) {
        Intrinsics.checkNotNullParameter(viewGroup, "view");
        Intrinsics.checkNotNullParameter(window, "window");
        Intrinsics.checkNotNullParameter(intent, "intent");
        return new LifecycleObserver(intent, viewGroup, window) { // from class: com.android.systemui.controls.management.ControlsAnimations$observerForAnimations$1
            final /* synthetic */ Intent $intent;
            final /* synthetic */ ViewGroup $view;
            final /* synthetic */ Window $window;
            private boolean showAnimation;

            /* access modifiers changed from: package-private */
            {
                this.$intent = r2;
                this.$view = r3;
                this.$window = r4;
                boolean z = false;
                this.showAnimation = r2.getBooleanExtra("extra_animate", false);
                r3.setTransitionGroup(true);
                r3.setTransitionAlpha(0.0f);
                if (ControlsAnimations.translationY == -1.0f ? true : z) {
                    ControlsAnimations controlsAnimations = ControlsAnimations.INSTANCE;
                    ControlsAnimations.translationY = (float) r3.getContext().getResources().getDimensionPixelSize(R$dimen.global_actions_controls_y_translation);
                }
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_START)
            public final void setup() {
                Window window2 = this.$window;
                ViewGroup viewGroup2 = this.$view;
                window2.setAllowEnterTransitionOverlap(true);
                ControlsAnimations controlsAnimations = ControlsAnimations.INSTANCE;
                window2.setEnterTransition(controlsAnimations.enterWindowTransition(viewGroup2.getId()));
                window2.setExitTransition(controlsAnimations.exitWindowTransition(viewGroup2.getId()));
                window2.setReenterTransition(controlsAnimations.enterWindowTransition(viewGroup2.getId()));
                window2.setReturnTransition(controlsAnimations.exitWindowTransition(viewGroup2.getId()));
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
            public final void enterAnimation() {
                if (this.showAnimation) {
                    ControlsAnimations.INSTANCE.enterAnimation(this.$view).start();
                    this.showAnimation = false;
                }
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
            public final void resetAnimation() {
                this.$view.setTranslationY(0.0f);
            }
        };
    }

    public final Animator enterAnimation(View view) {
        Intrinsics.checkNotNullParameter(view, "view");
        Log.d("ControlsUiController", Intrinsics.stringPlus("Enter animation for ", view));
        view.setTransitionAlpha(0.0f);
        view.setAlpha(1.0f);
        view.setTranslationY(translationY);
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(view, "transitionAlpha", 0.0f, 1.0f);
        Interpolator interpolator = Interpolators.DECELERATE_QUINT;
        ofFloat.setInterpolator(interpolator);
        ofFloat.setStartDelay(183);
        ofFloat.setDuration(167L);
        ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(view, "translationY", 0.0f);
        ofFloat2.setInterpolator(interpolator);
        ofFloat2.setStartDelay(217);
        ofFloat2.setDuration(217L);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(ofFloat, ofFloat2);
        return animatorSet;
    }

    public static /* synthetic */ Animator exitAnimation$default(View view, Runnable runnable, int i, Object obj) {
        if ((i & 2) != 0) {
            runnable = null;
        }
        return exitAnimation(view, runnable);
    }

    public static final Animator exitAnimation(View view, Runnable runnable) {
        Intrinsics.checkNotNullParameter(view, "view");
        Log.d("ControlsUiController", Intrinsics.stringPlus("Exit animation for ", view));
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(view, "transitionAlpha", 0.0f);
        Interpolator interpolator = Interpolators.ACCELERATE;
        ofFloat.setInterpolator(interpolator);
        ofFloat.setDuration(183L);
        view.setTranslationY(0.0f);
        ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(view, "translationY", -translationY);
        ofFloat2.setInterpolator(interpolator);
        ofFloat2.setDuration(183L);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(ofFloat, ofFloat2);
        if (runnable != null) {
            animatorSet.addListener(new AnimatorListenerAdapter(runnable) { // from class: com.android.systemui.controls.management.ControlsAnimations$exitAnimation$1$1$1
                final /* synthetic */ Runnable $it;

                /* access modifiers changed from: package-private */
                {
                    this.$it = r1;
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    Intrinsics.checkNotNullParameter(animator, "animation");
                    this.$it.run();
                }
            });
        }
        return animatorSet;
    }

    public final WindowTransition enterWindowTransition(int i) {
        WindowTransition windowTransition = new WindowTransition(ControlsAnimations$enterWindowTransition$1.INSTANCE);
        windowTransition.addTarget(i);
        return windowTransition;
    }

    public final WindowTransition exitWindowTransition(int i) {
        WindowTransition windowTransition = new WindowTransition(ControlsAnimations$exitWindowTransition$1.INSTANCE);
        windowTransition.addTarget(i);
        return windowTransition;
    }
}
