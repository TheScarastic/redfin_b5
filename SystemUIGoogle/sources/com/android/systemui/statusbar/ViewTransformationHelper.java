package com.android.systemui.statusbar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import com.android.systemui.R$id;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.statusbar.notification.TransformState;
import java.util.Stack;
/* loaded from: classes.dex */
public class ViewTransformationHelper implements TransformableView, TransformState.TransformInfo {
    private static final int TAG_CONTAINS_TRANSFORMED_VIEW = R$id.contains_transformed_view;
    private ValueAnimator mViewTransformationAnimation;
    private ArrayMap<Integer, View> mTransformedViews = new ArrayMap<>();
    private ArraySet<Integer> mKeysTransformingToSimilar = new ArraySet<>();
    private ArrayMap<Integer, CustomTransformation> mCustomTransformations = new ArrayMap<>();

    /* loaded from: classes.dex */
    public static abstract class CustomTransformation {
        public boolean customTransformTarget(TransformState transformState, TransformState transformState2) {
            return false;
        }

        public Interpolator getCustomInterpolator(int i, boolean z) {
            return null;
        }

        public boolean initTransformation(TransformState transformState, TransformState transformState2) {
            return false;
        }

        public abstract boolean transformFrom(TransformState transformState, TransformableView transformableView, float f);

        public abstract boolean transformTo(TransformState transformState, TransformableView transformableView, float f);
    }

    public void addTransformedView(int i, View view) {
        this.mTransformedViews.put(Integer.valueOf(i), view);
    }

    public void addTransformedView(View view) {
        int id = view.getId();
        if (id != -1) {
            addTransformedView(id, view);
            return;
        }
        throw new IllegalArgumentException("View argument does not have a valid id");
    }

    public void addViewTransformingToSimilar(int i, View view) {
        addTransformedView(i, view);
        this.mKeysTransformingToSimilar.add(Integer.valueOf(i));
    }

    public void addViewTransformingToSimilar(View view) {
        int id = view.getId();
        if (id != -1) {
            addViewTransformingToSimilar(id, view);
            return;
        }
        throw new IllegalArgumentException("View argument does not have a valid id");
    }

    public void reset() {
        this.mTransformedViews.clear();
        this.mKeysTransformingToSimilar.clear();
    }

    public void setCustomTransformation(CustomTransformation customTransformation, int i) {
        this.mCustomTransformations.put(Integer.valueOf(i), customTransformation);
    }

    @Override // com.android.systemui.statusbar.TransformableView
    public TransformState getCurrentState(int i) {
        View view = this.mTransformedViews.get(Integer.valueOf(i));
        if (view == null || view.getVisibility() == 8) {
            return null;
        }
        TransformState createFrom = TransformState.createFrom(view, this);
        if (this.mKeysTransformingToSimilar.contains(Integer.valueOf(i))) {
            createFrom.setIsSameAsAnyView(true);
        }
        return createFrom;
    }

    @Override // com.android.systemui.statusbar.TransformableView
    public void transformTo(TransformableView transformableView, final Runnable runnable) {
        ValueAnimator valueAnimator = this.mViewTransformationAnimation;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.mViewTransformationAnimation = ofFloat;
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(transformableView) { // from class: com.android.systemui.statusbar.ViewTransformationHelper$$ExternalSyntheticLambda0
            public final /* synthetic */ TransformableView f$1;

            {
                this.f$1 = r2;
            }

            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                ViewTransformationHelper.$r8$lambda$0S3YJ7QLfQAko2JyDztLOcIyc50(ViewTransformationHelper.this, this.f$1, valueAnimator2);
            }
        });
        this.mViewTransformationAnimation.setInterpolator(Interpolators.LINEAR);
        this.mViewTransformationAnimation.setDuration(360L);
        this.mViewTransformationAnimation.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.statusbar.ViewTransformationHelper.1
            public boolean mCancelled;

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                if (!this.mCancelled) {
                    Runnable runnable2 = runnable;
                    if (runnable2 != null) {
                        runnable2.run();
                    }
                    ViewTransformationHelper.this.setVisible(false);
                    ViewTransformationHelper.this.mViewTransformationAnimation = null;
                    return;
                }
                ViewTransformationHelper.this.abortTransformations();
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animator) {
                this.mCancelled = true;
            }
        });
        this.mViewTransformationAnimation.start();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$transformTo$0(TransformableView transformableView, ValueAnimator valueAnimator) {
        transformTo(transformableView, valueAnimator.getAnimatedFraction());
    }

    @Override // com.android.systemui.statusbar.TransformableView
    public void transformTo(TransformableView transformableView, float f) {
        for (Integer num : this.mTransformedViews.keySet()) {
            TransformState currentState = getCurrentState(num.intValue());
            if (currentState != null) {
                CustomTransformation customTransformation = this.mCustomTransformations.get(num);
                if (customTransformation == null || !customTransformation.transformTo(currentState, transformableView, f)) {
                    TransformState currentState2 = transformableView.getCurrentState(num.intValue());
                    if (currentState2 != null) {
                        currentState.transformViewTo(currentState2, f);
                        currentState2.recycle();
                    } else {
                        currentState.disappear(f, transformableView);
                    }
                    currentState.recycle();
                } else {
                    currentState.recycle();
                }
            }
        }
    }

    @Override // com.android.systemui.statusbar.TransformableView
    public void transformFrom(TransformableView transformableView) {
        ValueAnimator valueAnimator = this.mViewTransformationAnimation;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.mViewTransformationAnimation = ofFloat;
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(transformableView) { // from class: com.android.systemui.statusbar.ViewTransformationHelper$$ExternalSyntheticLambda1
            public final /* synthetic */ TransformableView f$1;

            {
                this.f$1 = r2;
            }

            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                ViewTransformationHelper.$r8$lambda$jBXAKfDcUZvWuEB_7GKSa7QVmZM(ViewTransformationHelper.this, this.f$1, valueAnimator2);
            }
        });
        this.mViewTransformationAnimation.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.statusbar.ViewTransformationHelper.2
            public boolean mCancelled;

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                if (!this.mCancelled) {
                    ViewTransformationHelper.this.setVisible(true);
                } else {
                    ViewTransformationHelper.this.abortTransformations();
                }
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animator) {
                this.mCancelled = true;
            }
        });
        this.mViewTransformationAnimation.setInterpolator(Interpolators.LINEAR);
        this.mViewTransformationAnimation.setDuration(360L);
        this.mViewTransformationAnimation.start();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$transformFrom$1(TransformableView transformableView, ValueAnimator valueAnimator) {
        transformFrom(transformableView, valueAnimator.getAnimatedFraction());
    }

    @Override // com.android.systemui.statusbar.TransformableView
    public void transformFrom(TransformableView transformableView, float f) {
        for (Integer num : this.mTransformedViews.keySet()) {
            TransformState currentState = getCurrentState(num.intValue());
            if (currentState != null) {
                CustomTransformation customTransformation = this.mCustomTransformations.get(num);
                if (customTransformation == null || !customTransformation.transformFrom(currentState, transformableView, f)) {
                    TransformState currentState2 = transformableView.getCurrentState(num.intValue());
                    if (currentState2 != null) {
                        currentState.transformViewFrom(currentState2, f);
                        currentState2.recycle();
                    } else {
                        currentState.appear(f, transformableView);
                    }
                    currentState.recycle();
                } else {
                    currentState.recycle();
                }
            }
        }
    }

    @Override // com.android.systemui.statusbar.TransformableView
    public void setVisible(boolean z) {
        ValueAnimator valueAnimator = this.mViewTransformationAnimation;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        for (Integer num : this.mTransformedViews.keySet()) {
            TransformState currentState = getCurrentState(num.intValue());
            if (currentState != null) {
                currentState.setVisible(z, false);
                currentState.recycle();
            }
        }
    }

    /* access modifiers changed from: private */
    public void abortTransformations() {
        for (Integer num : this.mTransformedViews.keySet()) {
            TransformState currentState = getCurrentState(num.intValue());
            if (currentState != null) {
                currentState.abortTransformation();
                currentState.recycle();
            }
        }
    }

    public void addRemainingTransformTypes(View view) {
        int id;
        int size = this.mTransformedViews.size();
        for (int i = 0; i < size; i++) {
            Object valueAt = this.mTransformedViews.valueAt(i);
            while (true) {
                View view2 = (View) valueAt;
                if (view2 != view.getParent()) {
                    view2.setTag(TAG_CONTAINS_TRANSFORMED_VIEW, Boolean.TRUE);
                    valueAt = view2.getParent();
                }
            }
        }
        Stack stack = new Stack();
        stack.push(view);
        while (!stack.isEmpty()) {
            View view3 = (View) stack.pop();
            int i2 = TAG_CONTAINS_TRANSFORMED_VIEW;
            if (((Boolean) view3.getTag(i2)) != null || (id = view3.getId()) == -1) {
                view3.setTag(i2, null);
                if ((view3 instanceof ViewGroup) && !this.mTransformedViews.containsValue(view3)) {
                    ViewGroup viewGroup = (ViewGroup) view3;
                    for (int i3 = 0; i3 < viewGroup.getChildCount(); i3++) {
                        stack.push(viewGroup.getChildAt(i3));
                    }
                }
            } else {
                addTransformedView(id, view3);
            }
        }
    }

    public void resetTransformedView(View view) {
        TransformState createFrom = TransformState.createFrom(view, this);
        createFrom.setVisible(true, true);
        createFrom.recycle();
    }

    public ArraySet<View> getAllTransformingViews() {
        return new ArraySet<>(this.mTransformedViews.values());
    }

    @Override // com.android.systemui.statusbar.notification.TransformState.TransformInfo
    public boolean isAnimating() {
        ValueAnimator valueAnimator = this.mViewTransformationAnimation;
        return valueAnimator != null && valueAnimator.isRunning();
    }
}
