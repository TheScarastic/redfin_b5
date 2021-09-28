package com.android.systemui.util.animation;

import android.animation.ValueAnimator;
import android.util.MathUtils;
import com.android.systemui.animation.Interpolators;
import kotlin.Unit;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: TransitionLayoutController.kt */
/* loaded from: classes2.dex */
public class TransitionLayoutController {
    private TransitionViewState animationStartState;
    private ValueAnimator animator;
    private int currentHeight;
    private int currentWidth;
    private Function2<? super Integer, ? super Integer, Unit> sizeChangedListener;
    private TransitionLayout transitionLayout;
    private TransitionViewState currentState = new TransitionViewState();
    private TransitionViewState state = new TransitionViewState();

    public TransitionLayoutController() {
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        Intrinsics.checkNotNullExpressionValue(ofFloat, "ofFloat(0.0f, 1.0f)");
        this.animator = ofFloat;
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(this) { // from class: com.android.systemui.util.animation.TransitionLayoutController$1$1
            final /* synthetic */ TransitionLayoutController this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
            }

            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                this.this$0.updateStateFromAnimation();
            }
        });
        ofFloat.setInterpolator(Interpolators.FAST_OUT_SLOW_IN);
    }

    public final void setSizeChangedListener(Function2<? super Integer, ? super Integer, Unit> function2) {
        this.sizeChangedListener = function2;
    }

    /* access modifiers changed from: private */
    public final void updateStateFromAnimation() {
        if (this.animationStartState != null && this.animator.isRunning()) {
            TransitionViewState transitionViewState = this.animationStartState;
            Intrinsics.checkNotNull(transitionViewState);
            TransitionViewState interpolatedState = getInterpolatedState(transitionViewState, this.state, this.animator.getAnimatedFraction(), this.currentState);
            this.currentState = interpolatedState;
            applyStateToLayout(interpolatedState);
        }
    }

    private final void applyStateToLayout(TransitionViewState transitionViewState) {
        TransitionLayout transitionLayout = this.transitionLayout;
        if (transitionLayout != null) {
            transitionLayout.setState(transitionViewState);
        }
        if (this.currentHeight != transitionViewState.getHeight() || this.currentWidth != transitionViewState.getWidth()) {
            this.currentHeight = transitionViewState.getHeight();
            int width = transitionViewState.getWidth();
            this.currentWidth = width;
            Function2<? super Integer, ? super Integer, Unit> function2 = this.sizeChangedListener;
            if (function2 != null) {
                function2.invoke(Integer.valueOf(width), Integer.valueOf(this.currentHeight));
            }
        }
    }

    public final TransitionViewState getGoneState(TransitionViewState transitionViewState, DisappearParameters disappearParameters, float f, TransitionViewState transitionViewState2) {
        Intrinsics.checkNotNullParameter(transitionViewState, "viewState");
        Intrinsics.checkNotNullParameter(disappearParameters, "disappearParameters");
        float constrain = MathUtils.constrain(MathUtils.map(disappearParameters.getDisappearStart(), disappearParameters.getDisappearEnd(), 0.0f, 1.0f, f), 0.0f, 1.0f);
        TransitionViewState copy = transitionViewState.copy(transitionViewState2);
        copy.setWidth((int) MathUtils.lerp((float) transitionViewState.getWidth(), ((float) transitionViewState.getWidth()) * disappearParameters.getDisappearSize().x, constrain));
        copy.setHeight((int) MathUtils.lerp((float) transitionViewState.getHeight(), ((float) transitionViewState.getHeight()) * disappearParameters.getDisappearSize().y, constrain));
        copy.getTranslation().x = ((float) (transitionViewState.getWidth() - copy.getWidth())) * disappearParameters.getGonePivot().x;
        copy.getTranslation().y = ((float) (transitionViewState.getHeight() - copy.getHeight())) * disappearParameters.getGonePivot().y;
        copy.getContentTranslation().x = (disappearParameters.getContentTranslationFraction().x - 1.0f) * copy.getTranslation().x;
        copy.getContentTranslation().y = (disappearParameters.getContentTranslationFraction().y - 1.0f) * copy.getTranslation().y;
        copy.setAlpha(MathUtils.constrain(MathUtils.map(disappearParameters.getFadeStartPosition(), 1.0f, 1.0f, 0.0f, constrain), 0.0f, 1.0f));
        return copy;
    }

    public static /* synthetic */ TransitionViewState getInterpolatedState$default(TransitionLayoutController transitionLayoutController, TransitionViewState transitionViewState, TransitionViewState transitionViewState2, float f, TransitionViewState transitionViewState3, int i, Object obj) {
        if (obj == null) {
            if ((i & 8) != 0) {
                transitionViewState3 = null;
            }
            return transitionLayoutController.getInterpolatedState(transitionViewState, transitionViewState2, f, transitionViewState3);
        }
        throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: getInterpolatedState");
    }

    public final TransitionViewState getInterpolatedState(TransitionViewState transitionViewState, TransitionViewState transitionViewState2, float f, TransitionViewState transitionViewState3) {
        TransitionLayoutController transitionLayoutController;
        TransitionViewState transitionViewState4;
        WidgetState widgetState;
        int i;
        int i2;
        float f2;
        float f3;
        float f4;
        float f5;
        float f6;
        boolean z;
        Intrinsics.checkNotNullParameter(transitionViewState, "startState");
        Intrinsics.checkNotNullParameter(transitionViewState2, "endState");
        if (transitionViewState3 == null) {
            transitionViewState4 = new TransitionViewState();
            transitionLayoutController = this;
        } else {
            transitionLayoutController = this;
            transitionViewState4 = transitionViewState3;
        }
        TransitionLayout transitionLayout = transitionLayoutController.transitionLayout;
        if (transitionLayout == null) {
            return transitionViewState4;
        }
        int childCount = transitionLayout.getChildCount();
        if (childCount > 0) {
            int i3 = 0;
            while (true) {
                int i4 = i3 + 1;
                int id = transitionLayout.getChildAt(i3).getId();
                WidgetState widgetState2 = transitionViewState4.getWidgetStates().get(Integer.valueOf(id));
                if (widgetState2 == null) {
                    widgetState2 = new WidgetState(0.0f, 0.0f, 0, 0, 0, 0, 0.0f, 0.0f, false, 511, null);
                }
                WidgetState widgetState3 = transitionViewState.getWidgetStates().get(Integer.valueOf(id));
                if (!(widgetState3 == null || (widgetState = transitionViewState2.getWidgetStates().get(Integer.valueOf(id))) == null)) {
                    if (widgetState3.getGone() != widgetState.getGone()) {
                        if (widgetState3.getGone()) {
                            float map = MathUtils.map(0.8f, 1.0f, 0.0f, 1.0f, f);
                            boolean z2 = f < 0.8f;
                            float scale = widgetState.getScale();
                            float lerp = MathUtils.lerp(0.8f * scale, scale, f);
                            int measureWidth = widgetState.getMeasureWidth();
                            int measureHeight = widgetState.getMeasureHeight();
                            float lerp2 = MathUtils.lerp(widgetState3.getX() - (((float) measureWidth) / 2.0f), widgetState.getX(), f);
                            f5 = MathUtils.lerp(widgetState3.getY() - (((float) measureHeight) / 2.0f), widgetState.getY(), f);
                            i = measureWidth;
                            f6 = lerp2;
                            i2 = measureHeight;
                            z = z2;
                            f2 = map;
                            f3 = lerp;
                            f4 = 1.0f;
                        } else {
                            float map2 = MathUtils.map(0.0f, 0.19999999f, 0.0f, 1.0f, f);
                            boolean z3 = f > 0.19999999f;
                            float scale2 = widgetState3.getScale();
                            float lerp3 = MathUtils.lerp(scale2, 0.8f * scale2, f);
                            int measureWidth2 = widgetState3.getMeasureWidth();
                            int measureHeight2 = widgetState3.getMeasureHeight();
                            float lerp4 = MathUtils.lerp(widgetState3.getX(), widgetState.getX() - (((float) measureWidth2) / 2.0f), f);
                            f5 = MathUtils.lerp(widgetState3.getY(), widgetState.getY() - (((float) measureHeight2) / 2.0f), f);
                            f2 = map2;
                            i = measureWidth2;
                            f3 = lerp3;
                            i2 = measureHeight2;
                            z = z3;
                            f4 = 0.0f;
                            f6 = lerp4;
                        }
                        widgetState2.setGone(z);
                    } else {
                        widgetState2.setGone(widgetState3.getGone());
                        i = widgetState.getMeasureWidth();
                        i2 = widgetState.getMeasureHeight();
                        f3 = MathUtils.lerp(widgetState3.getScale(), widgetState.getScale(), f);
                        f6 = MathUtils.lerp(widgetState3.getX(), widgetState.getX(), f);
                        f5 = MathUtils.lerp(widgetState3.getY(), widgetState.getY(), f);
                        f4 = f;
                        f2 = f4;
                    }
                    widgetState2.setX(f6);
                    widgetState2.setY(f5);
                    widgetState2.setAlpha(MathUtils.lerp(widgetState3.getAlpha(), widgetState.getAlpha(), f2));
                    widgetState2.setWidth((int) MathUtils.lerp((float) widgetState3.getWidth(), (float) widgetState.getWidth(), f4));
                    widgetState2.setHeight((int) MathUtils.lerp((float) widgetState3.getHeight(), (float) widgetState.getHeight(), f4));
                    widgetState2.setScale(f3);
                    widgetState2.setMeasureWidth(i);
                    widgetState2.setMeasureHeight(i2);
                    transitionViewState4.getWidgetStates().put(Integer.valueOf(id), widgetState2);
                }
                if (i4 >= childCount) {
                    break;
                }
                i3 = i4;
            }
        }
        transitionViewState4.setWidth((int) MathUtils.lerp((float) transitionViewState.getWidth(), (float) transitionViewState2.getWidth(), f));
        transitionViewState4.setHeight((int) MathUtils.lerp((float) transitionViewState.getHeight(), (float) transitionViewState2.getHeight(), f));
        transitionViewState4.getTranslation().x = MathUtils.lerp(transitionViewState.getTranslation().x, transitionViewState2.getTranslation().x, f);
        transitionViewState4.getTranslation().y = MathUtils.lerp(transitionViewState.getTranslation().y, transitionViewState2.getTranslation().y, f);
        transitionViewState4.setAlpha(MathUtils.lerp(transitionViewState.getAlpha(), transitionViewState2.getAlpha(), f));
        transitionViewState4.getContentTranslation().x = MathUtils.lerp(transitionViewState.getContentTranslation().x, transitionViewState2.getContentTranslation().x, f);
        transitionViewState4.getContentTranslation().y = MathUtils.lerp(transitionViewState.getContentTranslation().y, transitionViewState2.getContentTranslation().y, f);
        return transitionViewState4;
    }

    public final void attach(TransitionLayout transitionLayout) {
        Intrinsics.checkNotNullParameter(transitionLayout, "transitionLayout");
        this.transitionLayout = transitionLayout;
    }

    public final void setState(TransitionViewState transitionViewState, boolean z, boolean z2, long j, long j2) {
        Intrinsics.checkNotNullParameter(transitionViewState, "state");
        boolean z3 = z2 && this.currentState.getWidth() != 0;
        this.state = TransitionViewState.copy$default(transitionViewState, null, 1, null);
        if (z || this.transitionLayout == null) {
            this.animator.cancel();
            applyStateToLayout(this.state);
            this.currentState = transitionViewState.copy(this.currentState);
        } else if (z3) {
            this.animationStartState = TransitionViewState.copy$default(this.currentState, null, 1, null);
            this.animator.setDuration(j);
            this.animator.setStartDelay(j2);
            this.animator.start();
        } else if (!this.animator.isRunning()) {
            applyStateToLayout(this.state);
            this.currentState = transitionViewState.copy(this.currentState);
        }
    }

    public final void setMeasureState(TransitionViewState transitionViewState) {
        Intrinsics.checkNotNullParameter(transitionViewState, "state");
        TransitionLayout transitionLayout = this.transitionLayout;
        if (transitionLayout != null) {
            transitionLayout.setMeasureState(transitionViewState);
        }
    }
}
