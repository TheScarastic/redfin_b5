package com.google.android.material.bottomappbar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.R$bool;
import androidx.appcompat.widget.ActionMenuView;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.customview.view.AbsSavedState;
import com.google.android.material.behavior.HideBottomViewOnScrollBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButtonImpl;
import com.google.android.material.internal.ViewUtils;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Objects;
import java.util.WeakHashMap;
/* loaded from: classes.dex */
public class BottomAppBar extends Toolbar implements CoordinatorLayout.AttachedBehavior {
    public static final /* synthetic */ int $r8$clinit = 0;
    public Behavior behavior;
    public int fabAlignmentMode;
    public boolean fabAttached;

    /* loaded from: classes.dex */
    public static class SavedState extends AbsSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.ClassLoaderCreator<SavedState>() { // from class: com.google.android.material.bottomappbar.BottomAppBar.SavedState.1
            /* Return type fixed from 'java.lang.Object' to match base method */
            @Override // android.os.Parcelable.ClassLoaderCreator
            public SavedState createFromParcel(Parcel parcel, ClassLoader classLoader) {
                return new SavedState(parcel, classLoader);
            }

            @Override // android.os.Parcelable.Creator
            public Object[] newArray(int i) {
                return new SavedState[i];
            }

            @Override // android.os.Parcelable.Creator
            public Object createFromParcel(Parcel parcel) {
                return new SavedState(parcel, null);
            }
        };
        public int fabAlignmentMode;
        public boolean fabAttached;

        public SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        @Override // androidx.customview.view.AbsSavedState, android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeParcelable(this.mSuperState, i);
            parcel.writeInt(this.fabAlignmentMode);
            parcel.writeInt(this.fabAttached ? 1 : 0);
        }

        public SavedState(Parcel parcel, ClassLoader classLoader) {
            super(parcel, classLoader);
            this.fabAlignmentMode = parcel.readInt();
            this.fabAttached = parcel.readInt() != 0;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:8:0x001e  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final android.view.View findDependentView() {
        /*
            r3 = this;
            android.view.ViewParent r0 = r3.getParent()
            boolean r0 = r0 instanceof androidx.coordinatorlayout.widget.CoordinatorLayout
            r1 = 0
            if (r0 != 0) goto L_0x000a
            return r1
        L_0x000a:
            android.view.ViewParent r0 = r3.getParent()
            androidx.coordinatorlayout.widget.CoordinatorLayout r0 = (androidx.coordinatorlayout.widget.CoordinatorLayout) r0
            java.util.List r3 = r0.getDependents(r3)
            java.util.Iterator r3 = r3.iterator()
        L_0x0018:
            boolean r0 = r3.hasNext()
            if (r0 == 0) goto L_0x002d
            java.lang.Object r0 = r3.next()
            android.view.View r0 = (android.view.View) r0
            boolean r2 = r0 instanceof com.google.android.material.floatingactionbutton.FloatingActionButton
            if (r2 != 0) goto L_0x002c
            boolean r2 = r0 instanceof com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
            if (r2 == 0) goto L_0x0018
        L_0x002c:
            return r0
        L_0x002d:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.material.bottomappbar.BottomAppBar.findDependentView():android.view.View");
    }

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.AttachedBehavior
    public CoordinatorLayout.Behavior getBehavior() {
        if (this.behavior == null) {
            this.behavior = new Behavior();
        }
        return this.behavior;
    }

    public final boolean isFabVisibleOrWillBeShown() {
        View findDependentView = findDependentView();
        FloatingActionButton floatingActionButton = findDependentView instanceof FloatingActionButton ? (FloatingActionButton) findDependentView : null;
        return floatingActionButton != null && floatingActionButton.getImpl().isOrWillBeShown();
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        R$bool.setParentAbsoluteElevation(this, null);
        throw null;
    }

    @Override // androidx.appcompat.widget.Toolbar, android.view.ViewGroup, android.view.View
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        final ActionMenuView actionMenuView = null;
        if (!z) {
            int i5 = 0;
            while (true) {
                if (i5 >= getChildCount()) {
                    break;
                }
                View childAt = getChildAt(i5);
                if (childAt instanceof ActionMenuView) {
                    actionMenuView = (ActionMenuView) childAt;
                    break;
                }
                i5++;
            }
            if (actionMenuView != null) {
                actionMenuView.setAlpha(1.0f);
                if (!isFabVisibleOrWillBeShown()) {
                    new Runnable(0, false) { // from class: com.google.android.material.bottomappbar.BottomAppBar.8
                        @Override // java.lang.Runnable
                        public void run() {
                            ActionMenuView actionMenuView2 = actionMenuView;
                            BottomAppBar bottomAppBar = BottomAppBar.this;
                            int i6 = r1;
                            boolean z2 = r3;
                            Objects.requireNonNull(bottomAppBar);
                            int i7 = 0;
                            if (i6 == 1 && z2) {
                                boolean isLayoutRtl = ViewUtils.isLayoutRtl(bottomAppBar);
                                int measuredWidth = isLayoutRtl ? bottomAppBar.getMeasuredWidth() : 0;
                                for (int i8 = 0; i8 < bottomAppBar.getChildCount(); i8++) {
                                    View childAt2 = bottomAppBar.getChildAt(i8);
                                    if ((childAt2.getLayoutParams() instanceof Toolbar.LayoutParams) && (((Toolbar.LayoutParams) childAt2.getLayoutParams()).gravity & 8388615) == 8388611) {
                                        if (isLayoutRtl) {
                                            measuredWidth = Math.min(measuredWidth, childAt2.getLeft());
                                        } else {
                                            measuredWidth = Math.max(measuredWidth, childAt2.getRight());
                                        }
                                    }
                                }
                                i7 = measuredWidth - ((isLayoutRtl ? actionMenuView2.getRight() : actionMenuView2.getLeft()) + 0);
                            }
                            actionMenuView2.setTranslationX((float) i7);
                        }
                    }.run();
                    return;
                }
                final int i6 = this.fabAlignmentMode;
                final boolean z2 = this.fabAttached;
                new Runnable() { // from class: com.google.android.material.bottomappbar.BottomAppBar.8
                    @Override // java.lang.Runnable
                    public void run() {
                        ActionMenuView actionMenuView2 = actionMenuView;
                        BottomAppBar bottomAppBar = BottomAppBar.this;
                        int i6 = i6;
                        boolean z2 = z2;
                        Objects.requireNonNull(bottomAppBar);
                        int i7 = 0;
                        if (i6 == 1 && z2) {
                            boolean isLayoutRtl = ViewUtils.isLayoutRtl(bottomAppBar);
                            int measuredWidth = isLayoutRtl ? bottomAppBar.getMeasuredWidth() : 0;
                            for (int i8 = 0; i8 < bottomAppBar.getChildCount(); i8++) {
                                View childAt2 = bottomAppBar.getChildAt(i8);
                                if ((childAt2.getLayoutParams() instanceof Toolbar.LayoutParams) && (((Toolbar.LayoutParams) childAt2.getLayoutParams()).gravity & 8388615) == 8388611) {
                                    if (isLayoutRtl) {
                                        measuredWidth = Math.min(measuredWidth, childAt2.getLeft());
                                    } else {
                                        measuredWidth = Math.max(measuredWidth, childAt2.getRight());
                                    }
                                }
                            }
                            i7 = measuredWidth - ((isLayoutRtl ? actionMenuView2.getRight() : actionMenuView2.getLeft()) + 0);
                        }
                        actionMenuView2.setTranslationX((float) i7);
                    }
                }.run();
                return;
            }
            return;
        }
        throw null;
    }

    @Override // androidx.appcompat.widget.Toolbar, android.view.View
    public void onRestoreInstanceState(Parcelable parcelable) {
        if (!(parcelable instanceof SavedState)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.mSuperState);
        this.fabAlignmentMode = savedState.fabAlignmentMode;
        this.fabAttached = savedState.fabAttached;
    }

    @Override // androidx.appcompat.widget.Toolbar, android.view.View
    public Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.fabAlignmentMode = this.fabAlignmentMode;
        savedState.fabAttached = this.fabAttached;
        return savedState;
    }

    @Override // android.view.View
    public void setElevation(float f) {
        throw null;
    }

    @Override // androidx.appcompat.widget.Toolbar
    public void setSubtitle(CharSequence charSequence) {
    }

    @Override // androidx.appcompat.widget.Toolbar
    public void setTitle(CharSequence charSequence) {
    }

    /* loaded from: classes.dex */
    public static class Behavior extends HideBottomViewOnScrollBehavior<BottomAppBar> {
        public int originalBottomMargin;
        public WeakReference<BottomAppBar> viewRef;
        public final View.OnLayoutChangeListener fabLayoutListener = new View.OnLayoutChangeListener() { // from class: com.google.android.material.bottomappbar.BottomAppBar.Behavior.1
            @Override // android.view.View.OnLayoutChangeListener
            public void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
                if (Behavior.this.viewRef.get() == null || !(view instanceof FloatingActionButton)) {
                    view.removeOnLayoutChangeListener(this);
                    return;
                }
                FloatingActionButton floatingActionButton = (FloatingActionButton) view;
                Behavior.this.fabContentRect.set(0, 0, floatingActionButton.getMeasuredWidth(), floatingActionButton.getMeasuredHeight());
                throw null;
            }
        };
        public final Rect fabContentRect = new Rect();

        public Behavior() {
        }

        @Override // com.google.android.material.behavior.HideBottomViewOnScrollBehavior, androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
        public boolean onLayoutChild(CoordinatorLayout coordinatorLayout, View view, int i) {
            BottomAppBar bottomAppBar = (BottomAppBar) view;
            this.viewRef = new WeakReference<>(bottomAppBar);
            int i2 = BottomAppBar.$r8$clinit;
            View findDependentView = bottomAppBar.findDependentView();
            if (findDependentView != null) {
                WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
                if (!findDependentView.isLaidOut()) {
                    CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) findDependentView.getLayoutParams();
                    layoutParams.anchorGravity = 49;
                    this.originalBottomMargin = ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin;
                    if (findDependentView instanceof FloatingActionButton) {
                        FloatingActionButton floatingActionButton = (FloatingActionButton) findDependentView;
                        floatingActionButton.addOnLayoutChangeListener(this.fabLayoutListener);
                        FloatingActionButtonImpl impl = floatingActionButton.getImpl();
                        if (impl.hideListeners == null) {
                            impl.hideListeners = new ArrayList<>();
                        }
                        impl.hideListeners.add(null);
                        AnonymousClass9 r2 = new AnimatorListenerAdapter() { // from class: com.google.android.material.bottomappbar.BottomAppBar.9
                            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                            public void onAnimationStart(Animator animator) {
                                Objects.requireNonNull(BottomAppBar.this);
                                throw null;
                            }
                        };
                        FloatingActionButtonImpl impl2 = floatingActionButton.getImpl();
                        if (impl2.showListeners == null) {
                            impl2.showListeners = new ArrayList<>();
                        }
                        impl2.showListeners.add(r2);
                        FloatingActionButtonImpl impl3 = floatingActionButton.getImpl();
                        FloatingActionButton.TransformationCallbackWrapper transformationCallbackWrapper = new FloatingActionButton.TransformationCallbackWrapper(null);
                        if (impl3.transformationCallbacks == null) {
                            impl3.transformationCallbacks = new ArrayList<>();
                        }
                        impl3.transformationCallbacks.add(transformationCallbackWrapper);
                    }
                    throw null;
                }
            }
            coordinatorLayout.onLayoutChild(bottomAppBar, i);
            this.height = bottomAppBar.getMeasuredHeight() + ((ViewGroup.MarginLayoutParams) bottomAppBar.getLayoutParams()).bottomMargin;
            return false;
        }

        @Override // com.google.android.material.behavior.HideBottomViewOnScrollBehavior, androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
        public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View view, View view2, View view3, int i, int i2) {
            BottomAppBar bottomAppBar = (BottomAppBar) view;
            return false;
        }

        public Behavior(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
        }
    }
}
