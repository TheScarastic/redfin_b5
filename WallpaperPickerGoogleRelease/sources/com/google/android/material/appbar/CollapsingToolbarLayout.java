package com.google.android.material.appbar;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.solver.widgets.analyzer.DependencyGraph$$ExternalSyntheticOutline0;
import androidx.core.math.MathUtils;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.core.view.WindowInsetsCompat;
import com.android.systemui.shared.R;
import com.google.android.material.R$styleable;
import com.google.android.material.animation.AnimationUtils;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.elevation.ElevationOverlayProvider;
import com.google.android.material.internal.CollapsingTextHelper;
import com.google.android.material.internal.DescendantOffsetUtils;
import com.google.android.material.internal.ThemeEnforcement;
import com.google.android.material.theme.overlay.MaterialThemeOverlay;
import com.google.common.math.IntMath;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.WeakHashMap;
/* loaded from: classes.dex */
public class CollapsingToolbarLayout extends FrameLayout {
    public final CollapsingTextHelper collapsingTextHelper;
    public boolean collapsingTitleEnabled;
    public Drawable contentScrim;
    public int currentOffset;
    public boolean drawCollapsingTitle;
    public View dummyView;
    public final ElevationOverlayProvider elevationOverlayProvider;
    public int expandedMarginBottom;
    public int expandedMarginEnd;
    public int expandedMarginStart;
    public int expandedMarginTop;
    public int extraMultilineHeight;
    public boolean extraMultilineHeightEnabled;
    public boolean forceApplySystemWindowInsetTop;
    public WindowInsetsCompat lastInsets;
    public AppBarLayout.OnOffsetChangedListener onOffsetChangedListener;
    public boolean refreshToolbar;
    public int scrimAlpha;
    public long scrimAnimationDuration;
    public ValueAnimator scrimAnimator;
    public int scrimVisibleHeightTrigger;
    public boolean scrimsAreShown;
    public Drawable statusBarScrim;
    public int titleCollapseMode;
    public final Rect tmpRect;
    public ViewGroup toolbar;
    public View toolbarDirectChild;
    public int toolbarId;
    public int topInsetApplied;

    /* loaded from: classes.dex */
    public class OffsetUpdateListener implements AppBarLayout.OnOffsetChangedListener {
        public OffsetUpdateListener() {
        }

        @Override // com.google.android.material.appbar.AppBarLayout.BaseOnOffsetChangedListener
        public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
            CollapsingToolbarLayout collapsingToolbarLayout = CollapsingToolbarLayout.this;
            collapsingToolbarLayout.currentOffset = i;
            WindowInsetsCompat windowInsetsCompat = collapsingToolbarLayout.lastInsets;
            int systemWindowInsetTop = windowInsetsCompat != null ? windowInsetsCompat.getSystemWindowInsetTop() : 0;
            int childCount = CollapsingToolbarLayout.this.getChildCount();
            for (int i2 = 0; i2 < childCount; i2++) {
                View childAt = CollapsingToolbarLayout.this.getChildAt(i2);
                LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
                ViewOffsetHelper viewOffsetHelper = CollapsingToolbarLayout.getViewOffsetHelper(childAt);
                int i3 = layoutParams.collapseMode;
                if (i3 == 1) {
                    viewOffsetHelper.setTopAndBottomOffset(MathUtils.clamp(-i, 0, CollapsingToolbarLayout.this.getMaxOffsetForPinChild(childAt)));
                } else if (i3 == 2) {
                    viewOffsetHelper.setTopAndBottomOffset(Math.round(((float) (-i)) * layoutParams.parallaxMult));
                }
            }
            CollapsingToolbarLayout.this.updateScrimVisibility();
            CollapsingToolbarLayout collapsingToolbarLayout2 = CollapsingToolbarLayout.this;
            if (collapsingToolbarLayout2.statusBarScrim != null && systemWindowInsetTop > 0) {
                WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
                collapsingToolbarLayout2.postInvalidateOnAnimation();
            }
            int height = CollapsingToolbarLayout.this.getHeight();
            CollapsingToolbarLayout collapsingToolbarLayout3 = CollapsingToolbarLayout.this;
            WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap2 = ViewCompat.sViewPropertyAnimatorMap;
            int minimumHeight = (height - collapsingToolbarLayout3.getMinimumHeight()) - systemWindowInsetTop;
            int scrimVisibleHeightTrigger = height - CollapsingToolbarLayout.this.getScrimVisibleHeightTrigger();
            CollapsingTextHelper collapsingTextHelper = CollapsingToolbarLayout.this.collapsingTextHelper;
            float f = (float) minimumHeight;
            float min = Math.min(1.0f, ((float) scrimVisibleHeightTrigger) / f);
            collapsingTextHelper.fadeModeStartFraction = min;
            collapsingTextHelper.fadeModeThresholdFraction = DependencyGraph$$ExternalSyntheticOutline0.m(1.0f, min, 0.5f, min);
            CollapsingToolbarLayout collapsingToolbarLayout4 = CollapsingToolbarLayout.this;
            CollapsingTextHelper collapsingTextHelper2 = collapsingToolbarLayout4.collapsingTextHelper;
            collapsingTextHelper2.currentOffsetY = collapsingToolbarLayout4.currentOffset + minimumHeight;
            collapsingTextHelper2.setExpansionFraction(((float) Math.abs(i)) / f);
        }
    }

    public CollapsingToolbarLayout(Context context) {
        this(context, null);
    }

    public static int getHeightWithMargins(View view) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (!(layoutParams instanceof ViewGroup.MarginLayoutParams)) {
            return view.getMeasuredHeight();
        }
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams;
        return view.getMeasuredHeight() + marginLayoutParams.topMargin + marginLayoutParams.bottomMargin;
    }

    public static ViewOffsetHelper getViewOffsetHelper(View view) {
        ViewOffsetHelper viewOffsetHelper = (ViewOffsetHelper) view.getTag(R.id.view_offset_helper);
        if (viewOffsetHelper != null) {
            return viewOffsetHelper;
        }
        ViewOffsetHelper viewOffsetHelper2 = new ViewOffsetHelper(view);
        view.setTag(R.id.view_offset_helper, viewOffsetHelper2);
        return viewOffsetHelper2;
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup
    public boolean checkLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return layoutParams instanceof LayoutParams;
    }

    @Override // android.view.View
    public void draw(Canvas canvas) {
        Drawable drawable;
        super.draw(canvas);
        ensureToolbar();
        if (this.toolbar == null && (drawable = this.contentScrim) != null && this.scrimAlpha > 0) {
            drawable.mutate().setAlpha(this.scrimAlpha);
            this.contentScrim.draw(canvas);
        }
        if (this.collapsingTitleEnabled && this.drawCollapsingTitle) {
            if (this.toolbar != null && this.contentScrim != null && this.scrimAlpha > 0 && isTitleCollapseFadeMode()) {
                CollapsingTextHelper collapsingTextHelper = this.collapsingTextHelper;
                if (collapsingTextHelper.expandedFraction < collapsingTextHelper.fadeModeThresholdFraction) {
                    int save = canvas.save();
                    canvas.clipRect(this.contentScrim.getBounds(), Region.Op.DIFFERENCE);
                    this.collapsingTextHelper.draw(canvas);
                    canvas.restoreToCount(save);
                }
            }
            this.collapsingTextHelper.draw(canvas);
        }
        if (this.statusBarScrim != null && this.scrimAlpha > 0) {
            WindowInsetsCompat windowInsetsCompat = this.lastInsets;
            int systemWindowInsetTop = windowInsetsCompat != null ? windowInsetsCompat.getSystemWindowInsetTop() : 0;
            if (systemWindowInsetTop > 0) {
                this.statusBarScrim.setBounds(0, -this.currentOffset, getWidth(), systemWindowInsetTop - this.currentOffset);
                this.statusBarScrim.mutate().setAlpha(this.scrimAlpha);
                this.statusBarScrim.draw(canvas);
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0018, code lost:
        r3 = true;
     */
    @Override // android.view.ViewGroup
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean drawChild(android.graphics.Canvas r6, android.view.View r7, long r8) {
        /*
            r5 = this;
            android.graphics.drawable.Drawable r0 = r5.contentScrim
            r1 = 1
            r2 = 0
            if (r0 == 0) goto L_0x003a
            int r3 = r5.scrimAlpha
            if (r3 <= 0) goto L_0x003a
            android.view.View r3 = r5.toolbarDirectChild
            if (r3 == 0) goto L_0x0014
            if (r3 != r5) goto L_0x0011
            goto L_0x0014
        L_0x0011:
            if (r7 != r3) goto L_0x001a
            goto L_0x0018
        L_0x0014:
            android.view.ViewGroup r3 = r5.toolbar
            if (r7 != r3) goto L_0x001a
        L_0x0018:
            r3 = r1
            goto L_0x001b
        L_0x001a:
            r3 = r2
        L_0x001b:
            if (r3 == 0) goto L_0x003a
            int r3 = r5.getWidth()
            int r4 = r5.getHeight()
            r5.updateContentScrimBounds(r0, r7, r3, r4)
            android.graphics.drawable.Drawable r0 = r5.contentScrim
            android.graphics.drawable.Drawable r0 = r0.mutate()
            int r3 = r5.scrimAlpha
            r0.setAlpha(r3)
            android.graphics.drawable.Drawable r0 = r5.contentScrim
            r0.draw(r6)
            r0 = r1
            goto L_0x003b
        L_0x003a:
            r0 = r2
        L_0x003b:
            boolean r5 = super.drawChild(r6, r7, r8)
            if (r5 != 0) goto L_0x0045
            if (r0 == 0) goto L_0x0044
            goto L_0x0045
        L_0x0044:
            r1 = r2
        L_0x0045:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.material.appbar.CollapsingToolbarLayout.drawChild(android.graphics.Canvas, android.view.View, long):boolean");
    }

    @Override // android.view.View, android.view.ViewGroup
    public void drawableStateChanged() {
        super.drawableStateChanged();
        int[] drawableState = getDrawableState();
        Drawable drawable = this.statusBarScrim;
        boolean z = false;
        if (drawable != null && drawable.isStateful()) {
            z = false | drawable.setState(drawableState);
        }
        Drawable drawable2 = this.contentScrim;
        if (drawable2 != null && drawable2.isStateful()) {
            z |= drawable2.setState(drawableState);
        }
        CollapsingTextHelper collapsingTextHelper = this.collapsingTextHelper;
        if (collapsingTextHelper != null) {
            z |= collapsingTextHelper.setState(drawableState);
        }
        if (z) {
            invalidate();
        }
    }

    /* JADX DEBUG: Failed to insert an additional move for type inference into block B:53:0x0028 */
    public final void ensureToolbar() {
        View view;
        if (this.refreshToolbar) {
            ViewGroup viewGroup = null;
            this.toolbar = null;
            this.toolbarDirectChild = null;
            int i = this.toolbarId;
            if (i != -1) {
                ViewGroup viewGroup2 = (ViewGroup) findViewById(i);
                this.toolbar = viewGroup2;
                if (viewGroup2 != null) {
                    ViewParent parent = viewGroup2.getParent();
                    View view2 = viewGroup2;
                    while (parent != this && parent != null) {
                        if (parent instanceof View) {
                            view2 = (View) parent;
                        }
                        parent = parent.getParent();
                        view2 = view2;
                    }
                    this.toolbarDirectChild = view2;
                }
            }
            if (this.toolbar == null) {
                int childCount = getChildCount();
                int i2 = 0;
                while (true) {
                    if (i2 >= childCount) {
                        break;
                    }
                    View childAt = getChildAt(i2);
                    if ((childAt instanceof Toolbar) || (childAt instanceof android.widget.Toolbar)) {
                        viewGroup = (ViewGroup) childAt;
                        break;
                    }
                    i2++;
                }
                this.toolbar = viewGroup;
            }
            if (!this.collapsingTitleEnabled && (view = this.dummyView) != null) {
                ViewParent parent2 = view.getParent();
                if (parent2 instanceof ViewGroup) {
                    ((ViewGroup) parent2).removeView(this.dummyView);
                }
            }
            if (this.collapsingTitleEnabled && this.toolbar != null) {
                if (this.dummyView == null) {
                    this.dummyView = new View(getContext());
                }
                if (this.dummyView.getParent() == null) {
                    this.toolbar.addView(this.dummyView, -1, -1);
                }
            }
            this.refreshToolbar = false;
        }
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup
    public ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-1, -1);
    }

    public final int getMaxOffsetForPinChild(View view) {
        return ((getHeight() - getViewOffsetHelper(view).layoutTop) - view.getHeight()) - ((FrameLayout.LayoutParams) ((LayoutParams) view.getLayoutParams())).bottomMargin;
    }

    public int getScrimVisibleHeightTrigger() {
        int i = this.scrimVisibleHeightTrigger;
        if (i >= 0) {
            return i + this.topInsetApplied + this.extraMultilineHeight;
        }
        WindowInsetsCompat windowInsetsCompat = this.lastInsets;
        int systemWindowInsetTop = windowInsetsCompat != null ? windowInsetsCompat.getSystemWindowInsetTop() : 0;
        WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
        int minimumHeight = getMinimumHeight();
        if (minimumHeight > 0) {
            return Math.min((minimumHeight * 2) + systemWindowInsetTop, getHeight());
        }
        return getHeight() / 3;
    }

    public final boolean isTitleCollapseFadeMode() {
        return this.titleCollapseMode == 1;
    }

    @Override // android.view.View, android.view.ViewGroup
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        ViewParent parent = getParent();
        if (parent instanceof AppBarLayout) {
            AppBarLayout appBarLayout = (AppBarLayout) parent;
            if (isTitleCollapseFadeMode()) {
                appBarLayout.liftOnScroll = false;
            }
            WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
            setFitsSystemWindows(appBarLayout.getFitsSystemWindows());
            if (this.onOffsetChangedListener == null) {
                this.onOffsetChangedListener = new OffsetUpdateListener();
            }
            AppBarLayout.OnOffsetChangedListener onOffsetChangedListener = this.onOffsetChangedListener;
            if (appBarLayout.listeners == null) {
                appBarLayout.listeners = new ArrayList();
            }
            if (onOffsetChangedListener != null && !appBarLayout.listeners.contains(onOffsetChangedListener)) {
                appBarLayout.listeners.add(onOffsetChangedListener);
            }
            requestApplyInsets();
        }
    }

    @Override // android.view.View, android.view.ViewGroup
    public void onDetachedFromWindow() {
        List<AppBarLayout.BaseOnOffsetChangedListener> list;
        ViewParent parent = getParent();
        AppBarLayout.OnOffsetChangedListener onOffsetChangedListener = this.onOffsetChangedListener;
        if (!(onOffsetChangedListener == null || !(parent instanceof AppBarLayout) || (list = ((AppBarLayout) parent).listeners) == null || onOffsetChangedListener == null)) {
            list.remove(onOffsetChangedListener);
        }
        super.onDetachedFromWindow();
    }

    @Override // android.widget.FrameLayout, android.view.View, android.view.ViewGroup
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        WindowInsetsCompat windowInsetsCompat = this.lastInsets;
        if (windowInsetsCompat != null) {
            int systemWindowInsetTop = windowInsetsCompat.getSystemWindowInsetTop();
            int childCount = getChildCount();
            for (int i5 = 0; i5 < childCount; i5++) {
                View childAt = getChildAt(i5);
                WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
                if (!childAt.getFitsSystemWindows() && childAt.getTop() < systemWindowInsetTop) {
                    childAt.offsetTopAndBottom(systemWindowInsetTop);
                }
            }
        }
        int childCount2 = getChildCount();
        for (int i6 = 0; i6 < childCount2; i6++) {
            ViewOffsetHelper viewOffsetHelper = getViewOffsetHelper(getChildAt(i6));
            viewOffsetHelper.layoutTop = viewOffsetHelper.view.getTop();
            viewOffsetHelper.layoutLeft = viewOffsetHelper.view.getLeft();
        }
        updateTextBounds(i, i2, i3, i4, false);
        updateTitleFromToolbarIfNeeded();
        updateScrimVisibility();
        int childCount3 = getChildCount();
        for (int i7 = 0; i7 < childCount3; i7++) {
            getViewOffsetHelper(getChildAt(i7)).applyOffsets();
        }
    }

    @Override // android.widget.FrameLayout, android.view.View
    public void onMeasure(int i, int i2) {
        ensureToolbar();
        super.onMeasure(i, i2);
        int mode = View.MeasureSpec.getMode(i2);
        WindowInsetsCompat windowInsetsCompat = this.lastInsets;
        int i3 = 0;
        int systemWindowInsetTop = windowInsetsCompat != null ? windowInsetsCompat.getSystemWindowInsetTop() : 0;
        if ((mode == 0 || this.forceApplySystemWindowInsetTop) && systemWindowInsetTop > 0) {
            this.topInsetApplied = systemWindowInsetTop;
            super.onMeasure(i, View.MeasureSpec.makeMeasureSpec(getMeasuredHeight() + systemWindowInsetTop, IntMath.MAX_SIGNED_POWER_OF_TWO));
        }
        if (this.extraMultilineHeightEnabled && this.collapsingTextHelper.maxLines > 1) {
            updateTitleFromToolbarIfNeeded();
            updateTextBounds(0, 0, getMeasuredWidth(), getMeasuredHeight(), true);
            StaticLayout staticLayout = this.collapsingTextHelper.textLayout;
            if (staticLayout != null) {
                i3 = staticLayout.getLineCount();
            }
            if (i3 > 1) {
                CollapsingTextHelper collapsingTextHelper = this.collapsingTextHelper;
                TextPaint textPaint = collapsingTextHelper.tmpPaint;
                textPaint.setTextSize(collapsingTextHelper.expandedTextSize);
                textPaint.setTypeface(collapsingTextHelper.expandedTypeface);
                textPaint.setLetterSpacing(collapsingTextHelper.expandedLetterSpacing);
                this.extraMultilineHeight = (i3 - 1) * Math.round(collapsingTextHelper.tmpPaint.descent() + (-collapsingTextHelper.tmpPaint.ascent()));
                super.onMeasure(i, View.MeasureSpec.makeMeasureSpec(getMeasuredHeight() + this.extraMultilineHeight, IntMath.MAX_SIGNED_POWER_OF_TWO));
            }
        }
        ViewGroup viewGroup = this.toolbar;
        if (viewGroup != null) {
            View view = this.toolbarDirectChild;
            if (view == null || view == this) {
                setMinimumHeight(getHeightWithMargins(viewGroup));
            } else {
                setMinimumHeight(getHeightWithMargins(view));
            }
        }
    }

    @Override // android.view.View
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        Drawable drawable = this.contentScrim;
        if (drawable != null) {
            updateContentScrimBounds(drawable, this.toolbar, i, i2);
        }
    }

    public void setContentScrim(Drawable drawable) {
        Drawable drawable2 = this.contentScrim;
        if (drawable2 != drawable) {
            Drawable drawable3 = null;
            if (drawable2 != null) {
                drawable2.setCallback(null);
            }
            if (drawable != null) {
                drawable3 = drawable.mutate();
            }
            this.contentScrim = drawable3;
            if (drawable3 != null) {
                updateContentScrimBounds(drawable3, this.toolbar, getWidth(), getHeight());
                this.contentScrim.setCallback(this);
                this.contentScrim.setAlpha(this.scrimAlpha);
            }
            WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
            postInvalidateOnAnimation();
        }
    }

    public void setScrimAlpha(int i) {
        ViewGroup viewGroup;
        if (i != this.scrimAlpha) {
            if (!(this.contentScrim == null || (viewGroup = this.toolbar) == null)) {
                WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
                viewGroup.postInvalidateOnAnimation();
            }
            this.scrimAlpha = i;
            WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap2 = ViewCompat.sViewPropertyAnimatorMap;
            postInvalidateOnAnimation();
        }
    }

    @Override // android.view.View
    public void setVisibility(int i) {
        super.setVisibility(i);
        boolean z = i == 0;
        Drawable drawable = this.statusBarScrim;
        if (!(drawable == null || drawable.isVisible() == z)) {
            this.statusBarScrim.setVisible(z, false);
        }
        Drawable drawable2 = this.contentScrim;
        if (drawable2 != null && drawable2.isVisible() != z) {
            this.contentScrim.setVisible(z, false);
        }
    }

    public final void updateContentScrimBounds(Drawable drawable, View view, int i, int i2) {
        if (isTitleCollapseFadeMode() && view != null && this.collapsingTitleEnabled) {
            i2 = view.getBottom();
        }
        drawable.setBounds(0, 0, i, i2);
    }

    public final void updateScrimVisibility() {
        if (this.contentScrim != null || this.statusBarScrim != null) {
            int i = 0;
            boolean z = getHeight() + this.currentOffset < getScrimVisibleHeightTrigger();
            WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
            boolean z2 = isLaidOut() && !isInEditMode();
            if (this.scrimsAreShown != z) {
                int i2 = 255;
                if (z2) {
                    if (!z) {
                        i2 = 0;
                    }
                    ensureToolbar();
                    ValueAnimator valueAnimator = this.scrimAnimator;
                    if (valueAnimator == null) {
                        ValueAnimator valueAnimator2 = new ValueAnimator();
                        this.scrimAnimator = valueAnimator2;
                        valueAnimator2.setDuration(this.scrimAnimationDuration);
                        this.scrimAnimator.setInterpolator(i2 > this.scrimAlpha ? AnimationUtils.FAST_OUT_LINEAR_IN_INTERPOLATOR : AnimationUtils.LINEAR_OUT_SLOW_IN_INTERPOLATOR);
                        this.scrimAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.google.android.material.appbar.CollapsingToolbarLayout.2
                            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                            public void onAnimationUpdate(ValueAnimator valueAnimator3) {
                                CollapsingToolbarLayout.this.setScrimAlpha(((Integer) valueAnimator3.getAnimatedValue()).intValue());
                            }
                        });
                    } else if (valueAnimator.isRunning()) {
                        this.scrimAnimator.cancel();
                    }
                    this.scrimAnimator.setIntValues(this.scrimAlpha, i2);
                    this.scrimAnimator.start();
                } else {
                    if (z) {
                        i = 255;
                    }
                    setScrimAlpha(i);
                }
                this.scrimsAreShown = z;
            }
        }
    }

    public final void updateTextBounds(int i, int i2, int i3, int i4, boolean z) {
        View view;
        int i5;
        int i6;
        int i7;
        if (this.collapsingTitleEnabled && (view = this.dummyView) != null) {
            WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
            int i8 = 0;
            boolean z2 = view.isAttachedToWindow() && this.dummyView.getVisibility() == 0;
            this.drawCollapsingTitle = z2;
            if (z2 || z) {
                boolean z3 = getLayoutDirection() == 1;
                View view2 = this.toolbarDirectChild;
                if (view2 == null) {
                    view2 = this.toolbar;
                }
                int maxOffsetForPinChild = getMaxOffsetForPinChild(view2);
                DescendantOffsetUtils.getDescendantRect(this, this.dummyView, this.tmpRect);
                ViewGroup viewGroup = this.toolbar;
                if (viewGroup instanceof Toolbar) {
                    Toolbar toolbar = (Toolbar) viewGroup;
                    i8 = toolbar.mTitleMarginStart;
                    i6 = toolbar.mTitleMarginEnd;
                    i5 = toolbar.mTitleMarginTop;
                    i7 = toolbar.mTitleMarginBottom;
                } else if (viewGroup instanceof android.widget.Toolbar) {
                    android.widget.Toolbar toolbar2 = (android.widget.Toolbar) viewGroup;
                    i8 = toolbar2.getTitleMarginStart();
                    i6 = toolbar2.getTitleMarginEnd();
                    i5 = toolbar2.getTitleMarginTop();
                    i7 = toolbar2.getTitleMarginBottom();
                } else {
                    i7 = 0;
                    i6 = 0;
                    i5 = 0;
                }
                CollapsingTextHelper collapsingTextHelper = this.collapsingTextHelper;
                Rect rect = this.tmpRect;
                int i9 = rect.left + (z3 ? i6 : i8);
                int i10 = rect.top + maxOffsetForPinChild + i5;
                int i11 = rect.right;
                if (!z3) {
                    i8 = i6;
                }
                int i12 = i11 - i8;
                int i13 = (rect.bottom + maxOffsetForPinChild) - i7;
                if (!CollapsingTextHelper.rectEquals(collapsingTextHelper.collapsedBounds, i9, i10, i12, i13)) {
                    collapsingTextHelper.collapsedBounds.set(i9, i10, i12, i13);
                    collapsingTextHelper.boundsChanged = true;
                    collapsingTextHelper.onBoundsChanged();
                }
                CollapsingTextHelper collapsingTextHelper2 = this.collapsingTextHelper;
                int i14 = z3 ? this.expandedMarginEnd : this.expandedMarginStart;
                int i15 = this.tmpRect.top + this.expandedMarginTop;
                int i16 = (i3 - i) - (z3 ? this.expandedMarginStart : this.expandedMarginEnd);
                int i17 = (i4 - i2) - this.expandedMarginBottom;
                if (!CollapsingTextHelper.rectEquals(collapsingTextHelper2.expandedBounds, i14, i15, i16, i17)) {
                    collapsingTextHelper2.expandedBounds.set(i14, i15, i16, i17);
                    collapsingTextHelper2.boundsChanged = true;
                    collapsingTextHelper2.onBoundsChanged();
                }
                this.collapsingTextHelper.recalculate(z);
            }
        }
    }

    public final void updateTitleFromToolbarIfNeeded() {
        CharSequence charSequence;
        if (this.toolbar != null && this.collapsingTitleEnabled && TextUtils.isEmpty(this.collapsingTextHelper.text)) {
            ViewGroup viewGroup = this.toolbar;
            CharSequence charSequence2 = null;
            if (viewGroup instanceof Toolbar) {
                charSequence = ((Toolbar) viewGroup).mTitleText;
            } else {
                charSequence = viewGroup instanceof android.widget.Toolbar ? ((android.widget.Toolbar) viewGroup).getTitle() : null;
            }
            this.collapsingTextHelper.setText(charSequence);
            if (this.collapsingTitleEnabled) {
                charSequence2 = this.collapsingTextHelper.text;
            }
            setContentDescription(charSequence2);
        }
    }

    @Override // android.view.View
    public boolean verifyDrawable(Drawable drawable) {
        return super.verifyDrawable(drawable) || drawable == this.contentScrim || drawable == this.statusBarScrim;
    }

    public CollapsingToolbarLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.collapsingToolbarLayoutStyle);
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup
    /* renamed from: generateDefaultLayoutParams  reason: collision with other method in class */
    public FrameLayout.LayoutParams mo12generateDefaultLayoutParams() {
        return new LayoutParams(-1, -1);
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup
    public FrameLayout.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(getContext(), attributeSet);
    }

    public CollapsingToolbarLayout(Context context, AttributeSet attributeSet, int i) {
        super(MaterialThemeOverlay.wrap(context, attributeSet, i, 2131886764), attributeSet, i);
        int i2;
        boolean z = true;
        this.refreshToolbar = true;
        this.tmpRect = new Rect();
        this.scrimVisibleHeightTrigger = -1;
        this.topInsetApplied = 0;
        this.extraMultilineHeight = 0;
        Context context2 = getContext();
        CollapsingTextHelper collapsingTextHelper = new CollapsingTextHelper(this);
        this.collapsingTextHelper = collapsingTextHelper;
        collapsingTextHelper.textSizeInterpolator = AnimationUtils.DECELERATE_INTERPOLATOR;
        collapsingTextHelper.recalculate(false);
        collapsingTextHelper.isRtlTextDirectionHeuristicsEnabled = false;
        ElevationOverlayProvider elevationOverlayProvider = new ElevationOverlayProvider(context2);
        this.elevationOverlayProvider = elevationOverlayProvider;
        int[] iArr = R$styleable.CollapsingToolbarLayout;
        ThemeEnforcement.checkCompatibleTheme(context2, attributeSet, i, 2131886764);
        ThemeEnforcement.checkTextAppearance(context2, attributeSet, iArr, i, 2131886764, new int[0]);
        TypedArray obtainStyledAttributes = context2.obtainStyledAttributes(attributeSet, iArr, i, 2131886764);
        collapsingTextHelper.setExpandedTextGravity(obtainStyledAttributes.getInt(3, 8388691));
        collapsingTextHelper.setCollapsedTextGravity(obtainStyledAttributes.getInt(0, 8388627));
        int dimensionPixelSize = obtainStyledAttributes.getDimensionPixelSize(4, 0);
        this.expandedMarginBottom = dimensionPixelSize;
        this.expandedMarginEnd = dimensionPixelSize;
        this.expandedMarginTop = dimensionPixelSize;
        this.expandedMarginStart = dimensionPixelSize;
        if (obtainStyledAttributes.hasValue(7)) {
            this.expandedMarginStart = obtainStyledAttributes.getDimensionPixelSize(7, 0);
        }
        if (obtainStyledAttributes.hasValue(6)) {
            this.expandedMarginEnd = obtainStyledAttributes.getDimensionPixelSize(6, 0);
        }
        if (obtainStyledAttributes.hasValue(8)) {
            this.expandedMarginTop = obtainStyledAttributes.getDimensionPixelSize(8, 0);
        }
        if (obtainStyledAttributes.hasValue(5)) {
            this.expandedMarginBottom = obtainStyledAttributes.getDimensionPixelSize(5, 0);
        }
        this.collapsingTitleEnabled = obtainStyledAttributes.getBoolean(18, true);
        collapsingTextHelper.setText(obtainStyledAttributes.getText(16));
        Drawable drawable = null;
        setContentDescription(this.collapsingTitleEnabled ? collapsingTextHelper.text : null);
        collapsingTextHelper.setExpandedTextAppearance(2131886515);
        collapsingTextHelper.setCollapsedTextAppearance(2131886488);
        if (obtainStyledAttributes.hasValue(9)) {
            collapsingTextHelper.setExpandedTextAppearance(obtainStyledAttributes.getResourceId(9, 0));
        }
        if (obtainStyledAttributes.hasValue(1)) {
            collapsingTextHelper.setCollapsedTextAppearance(obtainStyledAttributes.getResourceId(1, 0));
        }
        this.scrimVisibleHeightTrigger = obtainStyledAttributes.getDimensionPixelSize(14, -1);
        if (obtainStyledAttributes.hasValue(12) && (i2 = obtainStyledAttributes.getInt(12, 1)) != collapsingTextHelper.maxLines) {
            collapsingTextHelper.maxLines = i2;
            collapsingTextHelper.clearTexture();
            collapsingTextHelper.recalculate(false);
        }
        this.scrimAnimationDuration = (long) obtainStyledAttributes.getInt(13, 600);
        setContentScrim(obtainStyledAttributes.getDrawable(2));
        Drawable drawable2 = obtainStyledAttributes.getDrawable(15);
        Drawable drawable3 = this.statusBarScrim;
        if (drawable3 != drawable2) {
            if (drawable3 != null) {
                drawable3.setCallback(null);
            }
            drawable = drawable2 != null ? drawable2.mutate() : drawable;
            this.statusBarScrim = drawable;
            if (drawable != null) {
                if (drawable.isStateful()) {
                    this.statusBarScrim.setState(getDrawableState());
                }
                Drawable drawable4 = this.statusBarScrim;
                WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
                drawable4.setLayoutDirection(getLayoutDirection());
                this.statusBarScrim.setVisible(getVisibility() != 0 ? false : z, false);
                this.statusBarScrim.setCallback(this);
                this.statusBarScrim.setAlpha(this.scrimAlpha);
            }
            WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap2 = ViewCompat.sViewPropertyAnimatorMap;
            postInvalidateOnAnimation();
        }
        this.titleCollapseMode = obtainStyledAttributes.getInt(17, 0);
        boolean isTitleCollapseFadeMode = isTitleCollapseFadeMode();
        collapsingTextHelper.fadeModeEnabled = isTitleCollapseFadeMode;
        ViewParent parent = getParent();
        if (parent instanceof AppBarLayout) {
            AppBarLayout appBarLayout = (AppBarLayout) parent;
            if (isTitleCollapseFadeMode()) {
                appBarLayout.liftOnScroll = false;
            }
        }
        if (isTitleCollapseFadeMode && this.contentScrim == null) {
            setContentScrim(new ColorDrawable(elevationOverlayProvider.compositeOverlayIfNeeded(elevationOverlayProvider.colorSurface, getResources().getDimension(R.dimen.design_appbar_elevation))));
        }
        this.toolbarId = obtainStyledAttributes.getResourceId(19, -1);
        this.forceApplySystemWindowInsetTop = obtainStyledAttributes.getBoolean(11, false);
        this.extraMultilineHeightEnabled = obtainStyledAttributes.getBoolean(10, false);
        obtainStyledAttributes.recycle();
        setWillNotDraw(false);
        AnonymousClass1 r1 = new OnApplyWindowInsetsListener() { // from class: com.google.android.material.appbar.CollapsingToolbarLayout.1
            @Override // androidx.core.view.OnApplyWindowInsetsListener
            public WindowInsetsCompat onApplyWindowInsets(View view, WindowInsetsCompat windowInsetsCompat) {
                CollapsingToolbarLayout collapsingToolbarLayout = CollapsingToolbarLayout.this;
                Objects.requireNonNull(collapsingToolbarLayout);
                WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap3 = ViewCompat.sViewPropertyAnimatorMap;
                WindowInsetsCompat windowInsetsCompat2 = collapsingToolbarLayout.getFitsSystemWindows() ? windowInsetsCompat : null;
                if (!Objects.equals(collapsingToolbarLayout.lastInsets, windowInsetsCompat2)) {
                    collapsingToolbarLayout.lastInsets = windowInsetsCompat2;
                    collapsingToolbarLayout.requestLayout();
                }
                return windowInsetsCompat.consumeSystemWindowInsets();
            }
        };
        WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap3 = ViewCompat.sViewPropertyAnimatorMap;
        ViewCompat.Api21Impl.setOnApplyWindowInsetsListener(this, r1);
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup
    public ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return new LayoutParams(layoutParams);
    }

    /* loaded from: classes.dex */
    public static class LayoutParams extends FrameLayout.LayoutParams {
        public int collapseMode;
        public float parallaxMult;

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            this.collapseMode = 0;
            this.parallaxMult = 0.5f;
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.CollapsingToolbarLayout_Layout);
            this.collapseMode = obtainStyledAttributes.getInt(0, 0);
            this.parallaxMult = obtainStyledAttributes.getFloat(1, 0.5f);
            obtainStyledAttributes.recycle();
        }

        public LayoutParams(int i, int i2) {
            super(i, i2);
            this.collapseMode = 0;
            this.parallaxMult = 0.5f;
        }

        public LayoutParams(ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
            this.collapseMode = 0;
            this.parallaxMult = 0.5f;
        }
    }
}
