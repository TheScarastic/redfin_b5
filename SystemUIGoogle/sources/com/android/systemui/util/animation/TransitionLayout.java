package com.android.systemui.util.animation;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import com.android.systemui.statusbar.CrossFadeHelper;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: TransitionLayout.kt */
/* loaded from: classes2.dex */
public final class TransitionLayout extends ConstraintLayout {
    private final Rect boundsRect;
    private TransitionViewState currentState;
    private int desiredMeasureHeight;
    private int desiredMeasureWidth;
    private boolean isPreDrawApplicatorRegistered;
    private boolean measureAsConstraint;
    private TransitionViewState measureState;
    private final Set<Integer> originalGoneChildrenSet;
    private final Map<Integer, Float> originalViewAlphas;
    private final TransitionLayout$preDrawApplicator$1 preDrawApplicator;
    private int transitionVisibility;
    private boolean updateScheduled;

    /* JADX INFO: 'this' call moved to the top of the method (can break code semantics) */
    public TransitionLayout(Context context) {
        this(context, null, 0, 6, null);
        Intrinsics.checkNotNullParameter(context, "context");
    }

    /* JADX INFO: 'this' call moved to the top of the method (can break code semantics) */
    public TransitionLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0, 4, null);
        Intrinsics.checkNotNullParameter(context, "context");
    }

    public /* synthetic */ TransitionLayout(Context context, AttributeSet attributeSet, int i, int i2, DefaultConstructorMarker defaultConstructorMarker) {
        this(context, (i2 & 2) != 0 ? null : attributeSet, (i2 & 4) != 0 ? 0 : i);
    }

    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public TransitionLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        Intrinsics.checkNotNullParameter(context, "context");
        this.boundsRect = new Rect();
        this.originalGoneChildrenSet = new LinkedHashSet();
        this.originalViewAlphas = new LinkedHashMap();
        this.currentState = new TransitionViewState();
        this.measureState = new TransitionViewState();
        this.preDrawApplicator = new TransitionLayout$preDrawApplicator$1(this);
    }

    public final void setMeasureState(TransitionViewState transitionViewState) {
        Intrinsics.checkNotNullParameter(transitionViewState, "value");
        int width = transitionViewState.getWidth();
        int height = transitionViewState.getHeight();
        if (width != this.desiredMeasureWidth || height != this.desiredMeasureHeight) {
            this.desiredMeasureWidth = width;
            this.desiredMeasureHeight = height;
            if (isInLayout()) {
                forceLayout();
            } else {
                requestLayout();
            }
        }
    }

    @Override // android.view.View
    public void setTransitionVisibility(int i) {
        super.setTransitionVisibility(i);
        this.transitionVisibility = i;
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        int childCount = getChildCount();
        if (childCount > 0) {
            int i = 0;
            while (true) {
                int i2 = i + 1;
                View childAt = getChildAt(i);
                if (childAt.getId() == -1) {
                    childAt.setId(i);
                }
                if (childAt.getVisibility() == 8) {
                    this.originalGoneChildrenSet.add(Integer.valueOf(childAt.getId()));
                }
                this.originalViewAlphas.put(Integer.valueOf(childAt.getId()), Float.valueOf(childAt.getAlpha()));
                if (i2 < childCount) {
                    i = i2;
                } else {
                    return;
                }
            }
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.isPreDrawApplicatorRegistered) {
            getViewTreeObserver().removeOnPreDrawListener(this.preDrawApplicator);
            this.isPreDrawApplicatorRegistered = false;
        }
    }

    /* access modifiers changed from: private */
    public final void applyCurrentState() {
        Integer num;
        int i;
        int i2;
        int childCount = getChildCount();
        int i3 = (int) this.currentState.getContentTranslation().x;
        int i4 = (int) this.currentState.getContentTranslation().y;
        if (childCount > 0) {
            int i5 = 0;
            while (true) {
                int i6 = i5 + 1;
                View childAt = getChildAt(i5);
                WidgetState widgetState = this.currentState.getWidgetStates().get(Integer.valueOf(childAt.getId()));
                if (widgetState != null) {
                    if (!(childAt instanceof TextView) || widgetState.getWidth() >= widgetState.getMeasureWidth()) {
                        num = null;
                    } else {
                        num = Integer.valueOf(((TextView) childAt).getLayout().getParagraphDirection(0) == -1 ? widgetState.getMeasureWidth() - widgetState.getWidth() : 0);
                    }
                    if (!(childAt.getMeasuredWidth() == widgetState.getMeasureWidth() && childAt.getMeasuredHeight() == widgetState.getMeasureHeight())) {
                        childAt.measure(View.MeasureSpec.makeMeasureSpec(widgetState.getMeasureWidth(), 1073741824), View.MeasureSpec.makeMeasureSpec(widgetState.getMeasureHeight(), 1073741824));
                        childAt.layout(0, 0, childAt.getMeasuredWidth(), childAt.getMeasuredHeight());
                    }
                    if (num == null) {
                        i = 0;
                    } else {
                        i = num.intValue();
                    }
                    int x = (((int) widgetState.getX()) + i3) - i;
                    int y = ((int) widgetState.getY()) + i4;
                    boolean z = true;
                    boolean z2 = num != null;
                    childAt.setLeftTopRightBottom(x, y, (z2 ? widgetState.getMeasureWidth() : widgetState.getWidth()) + x, (z2 ? widgetState.getMeasureHeight() : widgetState.getHeight()) + y);
                    childAt.setScaleX(widgetState.getScale());
                    childAt.setScaleY(widgetState.getScale());
                    Rect clipBounds = childAt.getClipBounds();
                    if (clipBounds == null) {
                        clipBounds = new Rect();
                    }
                    clipBounds.set(i, 0, widgetState.getWidth() + i, widgetState.getHeight());
                    childAt.setClipBounds(clipBounds);
                    CrossFadeHelper.fadeIn(childAt, widgetState.getAlpha());
                    if (!widgetState.getGone()) {
                        if (widgetState.getAlpha() != 0.0f) {
                            z = false;
                        }
                        if (!z) {
                            i2 = 0;
                            childAt.setVisibility(i2);
                        }
                    }
                    i2 = 4;
                    childAt.setVisibility(i2);
                }
                if (i6 >= childCount) {
                    break;
                }
                i5 = i6;
            }
        }
        updateBounds();
        setTranslationX(this.currentState.getTranslation().x);
        setTranslationY(this.currentState.getTranslation().y);
        CrossFadeHelper.fadeIn(this, this.currentState.getAlpha());
        int i7 = this.transitionVisibility;
        if (i7 != 0) {
            setTransitionVisibility(i7);
        }
    }

    private final void applyCurrentStateOnPredraw() {
        if (!this.updateScheduled) {
            this.updateScheduled = true;
            if (!this.isPreDrawApplicatorRegistered) {
                getViewTreeObserver().addOnPreDrawListener(this.preDrawApplicator);
                this.isPreDrawApplicatorRegistered = true;
            }
        }
    }

    /* access modifiers changed from: protected */
    @Override // androidx.constraintlayout.widget.ConstraintLayout, android.view.View
    public void onMeasure(int i, int i2) {
        if (this.measureAsConstraint) {
            super.onMeasure(i, i2);
            return;
        }
        int i3 = 0;
        int childCount = getChildCount();
        if (childCount > 0) {
            while (true) {
                int i4 = i3 + 1;
                View childAt = getChildAt(i3);
                WidgetState widgetState = this.currentState.getWidgetStates().get(Integer.valueOf(childAt.getId()));
                if (widgetState != null) {
                    childAt.measure(View.MeasureSpec.makeMeasureSpec(widgetState.getMeasureWidth(), 1073741824), View.MeasureSpec.makeMeasureSpec(widgetState.getMeasureHeight(), 1073741824));
                }
                if (i4 >= childCount) {
                    break;
                }
                i3 = i4;
            }
        }
        setMeasuredDimension(this.desiredMeasureWidth, this.desiredMeasureHeight);
    }

    /* access modifiers changed from: protected */
    @Override // androidx.constraintlayout.widget.ConstraintLayout, android.view.ViewGroup, android.view.View
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        if (this.measureAsConstraint) {
            super.onLayout(z, getLeft(), getTop(), getRight(), getBottom());
            return;
        }
        int childCount = getChildCount();
        if (childCount > 0) {
            int i5 = 0;
            while (true) {
                int i6 = i5 + 1;
                View childAt = getChildAt(i5);
                childAt.layout(0, 0, childAt.getMeasuredWidth(), childAt.getMeasuredHeight());
                if (i6 >= childCount) {
                    break;
                }
                i5 = i6;
            }
        }
        applyCurrentState();
    }

    /* access modifiers changed from: protected */
    @Override // androidx.constraintlayout.widget.ConstraintLayout, android.view.ViewGroup, android.view.View
    public void dispatchDraw(Canvas canvas) {
        if (canvas != null) {
            canvas.save();
        }
        if (canvas != null) {
            canvas.clipRect(this.boundsRect);
        }
        super.dispatchDraw(canvas);
        if (canvas != null) {
            canvas.restore();
        }
    }

    private final void updateBounds() {
        int left = getLeft();
        int top = getTop();
        setLeftTopRightBottom(left, top, this.currentState.getWidth() + left, this.currentState.getHeight() + top);
        this.boundsRect.set(0, 0, getWidth(), getHeight());
    }

    public final TransitionViewState calculateViewState(MeasurementInput measurementInput, ConstraintSet constraintSet, TransitionViewState transitionViewState) {
        Intrinsics.checkNotNullParameter(measurementInput, "input");
        Intrinsics.checkNotNullParameter(constraintSet, "constraintSet");
        if (transitionViewState == null) {
            transitionViewState = new TransitionViewState();
        }
        applySetToFullLayout(constraintSet);
        int measuredHeight = getMeasuredHeight();
        int measuredWidth = getMeasuredWidth();
        this.measureAsConstraint = true;
        measure(measurementInput.getWidthMeasureSpec(), measurementInput.getHeightMeasureSpec());
        int left = getLeft();
        int top = getTop();
        layout(left, top, getMeasuredWidth() + left, getMeasuredHeight() + top);
        this.measureAsConstraint = false;
        transitionViewState.initFromLayout(this);
        ensureViewsNotGone();
        setMeasuredDimension(measuredWidth, measuredHeight);
        applyCurrentStateOnPredraw();
        return transitionViewState;
    }

    private final void applySetToFullLayout(ConstraintSet constraintSet) {
        int childCount = getChildCount();
        if (childCount > 0) {
            int i = 0;
            while (true) {
                int i2 = i + 1;
                View childAt = getChildAt(i);
                if (this.originalGoneChildrenSet.contains(Integer.valueOf(childAt.getId()))) {
                    childAt.setVisibility(8);
                }
                Float f = this.originalViewAlphas.get(Integer.valueOf(childAt.getId()));
                childAt.setAlpha(f == null ? 1.0f : f.floatValue());
                if (i2 >= childCount) {
                    break;
                }
                i = i2;
            }
        }
        constraintSet.applyTo(this);
    }

    private final void ensureViewsNotGone() {
        Boolean bool;
        int childCount = getChildCount();
        if (childCount > 0) {
            int i = 0;
            while (true) {
                int i2 = i + 1;
                View childAt = getChildAt(i);
                WidgetState widgetState = this.currentState.getWidgetStates().get(Integer.valueOf(childAt.getId()));
                if (widgetState == null) {
                    bool = null;
                } else {
                    bool = Boolean.valueOf(widgetState.getGone());
                }
                childAt.setVisibility(!Intrinsics.areEqual(bool, Boolean.FALSE) ? 4 : 0);
                if (i2 < childCount) {
                    i = i2;
                } else {
                    return;
                }
            }
        }
    }

    public final void setState(TransitionViewState transitionViewState) {
        Intrinsics.checkNotNullParameter(transitionViewState, "state");
        this.currentState = transitionViewState;
        applyCurrentState();
    }
}
