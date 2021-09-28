package com.android.systemui.animation;

import android.animation.ValueAnimator;
import android.graphics.drawable.GradientDrawable;
import android.util.MathUtils;
import android.view.RemoteAnimationTarget;
import com.android.systemui.animation.ActivityLaunchAnimator;
import kotlin.math.MathKt__MathJVMKt;
/* compiled from: ActivityLaunchAnimator.kt */
/* access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class ActivityLaunchAnimator$Runner$startAnimation$2 implements ValueAnimator.AnimatorUpdateListener {
    final /* synthetic */ int $endBottom;
    final /* synthetic */ float $endRadius;
    final /* synthetic */ int $endTop;
    final /* synthetic */ int $endWidth;
    final /* synthetic */ float $endXCenter;
    final /* synthetic */ RemoteAnimationTarget $navigationBar;
    final /* synthetic */ int $startBottom;
    final /* synthetic */ float $startBottomCornerRadius;
    final /* synthetic */ int $startLeft;
    final /* synthetic */ int $startRight;
    final /* synthetic */ int $startTop;
    final /* synthetic */ float $startTopCornerRadius;
    final /* synthetic */ int $startWidth;
    final /* synthetic */ float $startXCenter;
    final /* synthetic */ ActivityLaunchAnimator.State $state;
    final /* synthetic */ RemoteAnimationTarget $window;
    final /* synthetic */ GradientDrawable $windowBackgroundLayer;
    final /* synthetic */ ActivityLaunchAnimator.Runner this$0;
    final /* synthetic */ ActivityLaunchAnimator this$1;

    /* access modifiers changed from: package-private */
    public ActivityLaunchAnimator$Runner$startAnimation$2(ActivityLaunchAnimator.Runner runner, ActivityLaunchAnimator activityLaunchAnimator, float f, float f2, int i, int i2, ActivityLaunchAnimator.State state, int i3, int i4, int i5, int i6, float f3, float f4, float f5, RemoteAnimationTarget remoteAnimationTarget, GradientDrawable gradientDrawable, RemoteAnimationTarget remoteAnimationTarget2, int i7, int i8) {
        this.this$0 = runner;
        this.this$1 = activityLaunchAnimator;
        this.$startXCenter = f;
        this.$endXCenter = f2;
        this.$startWidth = i;
        this.$endWidth = i2;
        this.$state = state;
        this.$startTop = i3;
        this.$endTop = i4;
        this.$startBottom = i5;
        this.$endBottom = i6;
        this.$startTopCornerRadius = f3;
        this.$endRadius = f4;
        this.$startBottomCornerRadius = f5;
        this.$window = remoteAnimationTarget;
        this.$windowBackgroundLayer = gradientDrawable;
        this.$navigationBar = remoteAnimationTarget2;
        this.$startLeft = i7;
        this.$startRight = i8;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        if (!(this.this$0.cancelled)) {
            float animatedFraction = valueAnimator.getAnimatedFraction();
            float interpolation = this.this$1.animationInterpolator.getInterpolation(animatedFraction);
            float lerp = MathUtils.lerp(this.$startXCenter, this.$endXCenter, this.this$1.animationInterpolatorX.getInterpolation(animatedFraction));
            float f = this.this$0.lerp(this.$startWidth, this.$endWidth, interpolation) / ((float) 2);
            this.$state.setTop(MathKt__MathJVMKt.roundToInt(this.this$0.lerp(this.$startTop, this.$endTop, interpolation)));
            this.$state.setBottom(MathKt__MathJVMKt.roundToInt(this.this$0.lerp(this.$startBottom, this.$endBottom, interpolation)));
            this.$state.setLeft(MathKt__MathJVMKt.roundToInt(lerp - f));
            this.$state.setRight(MathKt__MathJVMKt.roundToInt(lerp + f));
            this.$state.setTopCornerRadius(MathUtils.lerp(this.$startTopCornerRadius, this.$endRadius, interpolation));
            this.$state.setBottomCornerRadius(MathUtils.lerp(this.$startBottomCornerRadius, this.$endRadius, interpolation));
            this.$state.setVisible(ActivityLaunchAnimator.Companion.getProgress(animatedFraction, 0, 150) < 1.0f);
            this.this$0.applyStateToWindow(this.$window, this.$state);
            this.this$0.applyStateToWindowBackgroundLayer(this.$windowBackgroundLayer, this.$state, animatedFraction);
            RemoteAnimationTarget remoteAnimationTarget = this.$navigationBar;
            if (remoteAnimationTarget != null) {
                this.this$0.applyStateToNavigationBar(remoteAnimationTarget, this.$state, animatedFraction);
            }
            if (!(this.$state.getTop() == this.$startTop || this.$state.getLeft() == this.$startLeft || this.$state.getBottom() == this.$startBottom || this.$state.getRight() == this.$startRight)) {
                ActivityLaunchAnimator.State state = this.$state;
                state.setTop(state.getTop() + 1);
                ActivityLaunchAnimator.State state2 = this.$state;
                state2.setLeft(state2.getLeft() + 1);
                ActivityLaunchAnimator.State state3 = this.$state;
                state3.setRight(state3.getRight() - 1);
                ActivityLaunchAnimator.State state4 = this.$state;
                state4.setBottom(state4.getBottom() - 1);
            }
            this.this$0.controller.onLaunchAnimationProgress(this.$state, interpolation, animatedFraction);
        }
    }
}
