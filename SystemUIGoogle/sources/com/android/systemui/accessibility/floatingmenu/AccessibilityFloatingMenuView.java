package com.android.systemui.accessibility.floatingmenu;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.MathUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewPropertyAnimator;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerViewAccessibilityDelegate;
import com.android.internal.accessibility.dialog.AccessibilityTarget;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.R$color;
import com.android.systemui.R$dimen;
import com.android.systemui.R$drawable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
/* loaded from: classes.dex */
public class AccessibilityFloatingMenuView extends FrameLayout implements RecyclerView.OnItemTouchListener {
    private final AccessibilityTargetAdapter mAdapter;
    private int mAlignment;
    @VisibleForTesting
    final WindowManager.LayoutParams mCurrentLayoutParams;
    private int mDownX;
    private int mDownY;
    @VisibleForTesting
    final ValueAnimator mDragAnimator;
    private final ValueAnimator mFadeOutAnimator;
    private float mFadeOutValue;
    private int mIconHeight;
    private int mIconWidth;
    private boolean mImeVisibility;
    private int mInset;
    private boolean mIsDownInEnlargedTouchArea;
    private boolean mIsDragging;
    private boolean mIsFadeEffectEnabled;
    private boolean mIsShowing;
    private final Configuration mLastConfiguration;
    private final RecyclerView mListView;
    private int mMargin;
    private Optional<OnDragEndListener> mOnDragEndListener;
    private int mPadding;
    private final Position mPosition;
    private float mRadius;
    private int mRadiusType;
    private int mRelativeToPointerDownX;
    private int mRelativeToPointerDownY;
    private int mScreenHeight;
    private int mScreenWidth;
    @VisibleForTesting
    int mShapeType;
    private int mSizeType;
    private float mSquareScaledTouchSlop;
    private final List<AccessibilityTarget> mTargets;
    private int mTemporaryShapeType;
    private final Handler mUiHandler;
    private final WindowManager mWindowManager;

    /* access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public interface OnDragEndListener {
        void onDragEnd(Position position);
    }

    private float[] createRadii(float f, int i) {
        return i == 0 ? new float[]{f, f, 0.0f, 0.0f, 0.0f, 0.0f, f, f} : i == 2 ? new float[]{0.0f, 0.0f, f, f, f, f, 0.0f, 0.0f} : new float[]{f, f, f, f, f, f, f, f};
    }

    private boolean isMovingTowardsScreenEdge(int i, int i2, int i3) {
        if (i != 1 || i2 <= i3) {
            return i == 0 && i3 > i2;
        }
        return true;
    }

    /* access modifiers changed from: private */
    public int transformToAlignment(float f) {
        return f < 0.5f ? 0 : 1;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
    public void onRequestDisallowInterceptTouchEvent(boolean z) {
    }

    @Override // androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
    public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
    }

    public AccessibilityFloatingMenuView(Context context, Position position) {
        this(context, position, new RecyclerView(context));
    }

    @VisibleForTesting
    AccessibilityFloatingMenuView(Context context, Position position, RecyclerView recyclerView) {
        super(context);
        this.mIsDragging = false;
        this.mSizeType = 0;
        this.mShapeType = 0;
        this.mOnDragEndListener = Optional.empty();
        ArrayList arrayList = new ArrayList();
        this.mTargets = arrayList;
        this.mListView = recyclerView;
        this.mWindowManager = (WindowManager) context.getSystemService(WindowManager.class);
        this.mLastConfiguration = new Configuration(getResources().getConfiguration());
        this.mAdapter = new AccessibilityTargetAdapter(arrayList);
        this.mUiHandler = createUiHandler();
        this.mPosition = position;
        int transformToAlignment = transformToAlignment(position.getPercentageX());
        this.mAlignment = transformToAlignment;
        this.mRadiusType = transformToAlignment == 1 ? 0 : 2;
        updateDimensions();
        this.mCurrentLayoutParams = createDefaultLayoutParams();
        ValueAnimator ofFloat = ValueAnimator.ofFloat(1.0f, this.mFadeOutValue);
        this.mFadeOutAnimator = ofFloat;
        ofFloat.setDuration(1000L);
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.accessibility.floatingmenu.AccessibilityFloatingMenuView$$ExternalSyntheticLambda0
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                AccessibilityFloatingMenuView.this.lambda$new$0(valueAnimator);
            }
        });
        ValueAnimator ofFloat2 = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.mDragAnimator = ofFloat2;
        ofFloat2.setDuration(150L);
        ofFloat2.setInterpolator(new OvershootInterpolator());
        ofFloat2.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.accessibility.floatingmenu.AccessibilityFloatingMenuView.1
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                AccessibilityFloatingMenuView.this.mPosition.update(AccessibilityFloatingMenuView.this.transformCurrentPercentageXToEdge(), AccessibilityFloatingMenuView.this.calculateCurrentPercentageY());
                AccessibilityFloatingMenuView accessibilityFloatingMenuView = AccessibilityFloatingMenuView.this;
                accessibilityFloatingMenuView.mAlignment = accessibilityFloatingMenuView.transformToAlignment(accessibilityFloatingMenuView.mPosition.getPercentageX());
                AccessibilityFloatingMenuView accessibilityFloatingMenuView2 = AccessibilityFloatingMenuView.this;
                accessibilityFloatingMenuView2.updateLocationWith(accessibilityFloatingMenuView2.mPosition);
                AccessibilityFloatingMenuView accessibilityFloatingMenuView3 = AccessibilityFloatingMenuView.this;
                accessibilityFloatingMenuView3.updateInsetWith(accessibilityFloatingMenuView3.getResources().getConfiguration().uiMode, AccessibilityFloatingMenuView.this.mAlignment);
                AccessibilityFloatingMenuView accessibilityFloatingMenuView4 = AccessibilityFloatingMenuView.this;
                accessibilityFloatingMenuView4.mRadiusType = accessibilityFloatingMenuView4.mAlignment == 1 ? 0 : 2;
                AccessibilityFloatingMenuView accessibilityFloatingMenuView5 = AccessibilityFloatingMenuView.this;
                accessibilityFloatingMenuView5.updateRadiusWith(accessibilityFloatingMenuView5.mSizeType, AccessibilityFloatingMenuView.this.mRadiusType, AccessibilityFloatingMenuView.this.mTargets.size());
                AccessibilityFloatingMenuView.this.fadeOut();
                AccessibilityFloatingMenuView.this.mOnDragEndListener.ifPresent(new AccessibilityFloatingMenuView$1$$ExternalSyntheticLambda0(this));
            }

            /* access modifiers changed from: private */
            public /* synthetic */ void lambda$onAnimationEnd$0(OnDragEndListener onDragEndListener) {
                onDragEndListener.onDragEnd(AccessibilityFloatingMenuView.this.mPosition);
            }
        });
        initListView();
        updateStrokeWith(getResources().getConfiguration().uiMode, this.mAlignment);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(ValueAnimator valueAnimator) {
        setAlpha(((Float) valueAnimator.getAnimatedValue()).floatValue());
    }

    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0018, code lost:
        if (r7 != 3) goto L_0x00ba;
     */
    @Override // androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean onInterceptTouchEvent(androidx.recyclerview.widget.RecyclerView r6, android.view.MotionEvent r7) {
        /*
            r5 = this;
            float r6 = r7.getRawX()
            int r6 = (int) r6
            float r0 = r7.getRawY()
            int r0 = (int) r0
            int r7 = r7.getAction()
            r1 = 0
            if (r7 == 0) goto L_0x009d
            r2 = 2
            r3 = 1
            if (r7 == r3) goto L_0x006c
            if (r7 == r2) goto L_0x001c
            r6 = 3
            if (r7 == r6) goto L_0x006c
            goto L_0x00ba
        L_0x001c:
            boolean r7 = r5.mIsDragging
            if (r7 != 0) goto L_0x002a
            int r7 = r5.mDownX
            int r2 = r5.mDownY
            boolean r7 = r5.hasExceededTouchSlop(r7, r2, r6, r0)
            if (r7 == 0) goto L_0x00ba
        L_0x002a:
            boolean r7 = r5.mIsDragging
            if (r7 != 0) goto L_0x0038
            r5.mIsDragging = r3
            float r7 = r5.mRadius
            r5.setRadius(r7, r3)
            r5.setInset(r1, r1)
        L_0x0038:
            int r7 = r5.mAlignment
            int r2 = r5.mDownX
            boolean r7 = r5.isMovingTowardsScreenEdge(r7, r6, r2)
            r5.mTemporaryShapeType = r7
            int r7 = r5.mRelativeToPointerDownX
            int r6 = r6 + r7
            int r7 = r5.mRelativeToPointerDownY
            int r0 = r0 + r7
            android.view.WindowManager$LayoutParams r7 = r5.mCurrentLayoutParams
            int r2 = r5.getMinWindowX()
            int r3 = r5.getMaxWindowX()
            int r6 = android.util.MathUtils.constrain(r6, r2, r3)
            r7.x = r6
            android.view.WindowManager$LayoutParams r6 = r5.mCurrentLayoutParams
            int r7 = r5.getMaxWindowY()
            int r7 = android.util.MathUtils.constrain(r0, r1, r7)
            r6.y = r7
            android.view.WindowManager r6 = r5.mWindowManager
            android.view.WindowManager$LayoutParams r7 = r5.mCurrentLayoutParams
            r6.updateViewLayout(r5, r7)
            goto L_0x00ba
        L_0x006c:
            boolean r6 = r5.mIsDragging
            if (r6 == 0) goto L_0x008f
            r5.mIsDragging = r1
            int r6 = r5.getMinWindowX()
            int r7 = r5.getMaxWindowX()
            android.view.WindowManager$LayoutParams r0 = r5.mCurrentLayoutParams
            int r1 = r0.x
            int r4 = r6 + r7
            int r4 = r4 / r2
            if (r1 <= r4) goto L_0x0084
            r6 = r7
        L_0x0084:
            int r7 = r0.y
            r5.snapToLocation(r6, r7)
            int r6 = r5.mTemporaryShapeType
            r5.setShapeType(r6)
            return r3
        L_0x008f:
            boolean r6 = r5.isOvalShape()
            if (r6 != 0) goto L_0x0099
            r5.setShapeType(r1)
            return r3
        L_0x0099:
            r5.fadeOut()
            goto L_0x00ba
        L_0x009d:
            r5.fadeIn()
            r5.mDownX = r6
            r5.mDownY = r0
            android.view.WindowManager$LayoutParams r7 = r5.mCurrentLayoutParams
            int r2 = r7.x
            int r2 = r2 - r6
            r5.mRelativeToPointerDownX = r2
            int r6 = r7.y
            int r6 = r6 - r0
            r5.mRelativeToPointerDownY = r6
            androidx.recyclerview.widget.RecyclerView r5 = r5.mListView
            android.view.ViewPropertyAnimator r5 = r5.animate()
            r6 = 0
            r5.translationX(r6)
        L_0x00ba:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.accessibility.floatingmenu.AccessibilityFloatingMenuView.onInterceptTouchEvent(androidx.recyclerview.widget.RecyclerView, android.view.MotionEvent):boolean");
    }

    /* access modifiers changed from: package-private */
    public void show() {
        if (!isShowing()) {
            this.mIsShowing = true;
            this.mWindowManager.addView(this, this.mCurrentLayoutParams);
            setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() { // from class: com.android.systemui.accessibility.floatingmenu.AccessibilityFloatingMenuView$$ExternalSyntheticLambda2
                @Override // android.view.View.OnApplyWindowInsetsListener
                public final WindowInsets onApplyWindowInsets(View view, WindowInsets windowInsets) {
                    return AccessibilityFloatingMenuView.this.lambda$show$1(view, windowInsets);
                }
            });
            setSystemGestureExclusion();
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ WindowInsets lambda$show$1(View view, WindowInsets windowInsets) {
        return onWindowInsetsApplied(windowInsets);
    }

    /* access modifiers changed from: package-private */
    public void hide() {
        if (isShowing()) {
            this.mIsShowing = false;
            this.mWindowManager.removeView(this);
            setOnApplyWindowInsetsListener(null);
            setSystemGestureExclusion();
        }
    }

    /* access modifiers changed from: package-private */
    public boolean isShowing() {
        return this.mIsShowing;
    }

    /* access modifiers changed from: package-private */
    public boolean isOvalShape() {
        return this.mShapeType == 0;
    }

    /* access modifiers changed from: package-private */
    public void onTargetsChanged(List<AccessibilityTarget> list) {
        fadeIn();
        this.mTargets.clear();
        this.mTargets.addAll(list);
        onEnabledFeaturesChanged();
        updateRadiusWith(this.mSizeType, this.mRadiusType, this.mTargets.size());
        updateScrollModeWith(hasExceededMaxLayoutHeight());
        setSystemGestureExclusion();
        fadeOut();
    }

    /* access modifiers changed from: package-private */
    public void setSizeType(int i) {
        fadeIn();
        this.mSizeType = i;
        updateItemViewWith(i);
        updateRadiusWith(i, this.mRadiusType, this.mTargets.size());
        updateLocationWith(this.mPosition);
        updateScrollModeWith(hasExceededMaxLayoutHeight());
        updateOffsetWith(this.mShapeType, this.mAlignment);
        setSystemGestureExclusion();
        fadeOut();
    }

    /* access modifiers changed from: package-private */
    public void setShapeType(int i) {
        AccessibilityFloatingMenuView$$ExternalSyntheticLambda3 accessibilityFloatingMenuView$$ExternalSyntheticLambda3;
        fadeIn();
        this.mShapeType = i;
        updateOffsetWith(i, this.mAlignment);
        if (i == 0) {
            accessibilityFloatingMenuView$$ExternalSyntheticLambda3 = null;
        } else {
            accessibilityFloatingMenuView$$ExternalSyntheticLambda3 = new View.OnTouchListener() { // from class: com.android.systemui.accessibility.floatingmenu.AccessibilityFloatingMenuView$$ExternalSyntheticLambda3
                @Override // android.view.View.OnTouchListener
                public final boolean onTouch(View view, MotionEvent motionEvent) {
                    return AccessibilityFloatingMenuView.this.lambda$setShapeType$2(view, motionEvent);
                }
            };
        }
        setOnTouchListener(accessibilityFloatingMenuView$$ExternalSyntheticLambda3);
        fadeOut();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$setShapeType$2(View view, MotionEvent motionEvent) {
        return onTouched(motionEvent);
    }

    public void setOnDragEndListener(OnDragEndListener onDragEndListener) {
        this.mOnDragEndListener = Optional.ofNullable(onDragEndListener);
    }

    /* access modifiers changed from: package-private */
    public void startTranslateXAnimation() {
        fadeIn();
        TranslateAnimation translateAnimation = new TranslateAnimation(1, 0.0f, 1, this.mAlignment == 1 ? 0.5f : -0.5f, 1, 0.0f, 1, 0.0f);
        translateAnimation.setDuration(600);
        translateAnimation.setRepeatMode(2);
        translateAnimation.setInterpolator(new OvershootInterpolator());
        translateAnimation.setRepeatCount(-1);
        translateAnimation.setStartOffset(600);
        this.mListView.startAnimation(translateAnimation);
    }

    /* access modifiers changed from: package-private */
    public void stopTranslateXAnimation() {
        this.mListView.clearAnimation();
        fadeOut();
    }

    /* access modifiers changed from: package-private */
    public Rect getWindowLocationOnScreen() {
        WindowManager.LayoutParams layoutParams = this.mCurrentLayoutParams;
        int i = layoutParams.x;
        int i2 = layoutParams.y;
        return new Rect(i, i2, getWindowWidth() + i, getWindowHeight() + i2);
    }

    /* access modifiers changed from: package-private */
    public void updateOpacityWith(boolean z, float f) {
        this.mIsFadeEffectEnabled = z;
        this.mFadeOutValue = f;
        this.mFadeOutAnimator.cancel();
        float f2 = 1.0f;
        this.mFadeOutAnimator.setFloatValues(1.0f, this.mFadeOutValue);
        if (this.mIsFadeEffectEnabled) {
            f2 = this.mFadeOutValue;
        }
        setAlpha(f2);
    }

    /* access modifiers changed from: package-private */
    public void onEnabledFeaturesChanged() {
        this.mAdapter.notifyDataSetChanged();
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public void fadeIn() {
        if (this.mIsFadeEffectEnabled) {
            this.mFadeOutAnimator.cancel();
            this.mUiHandler.removeCallbacksAndMessages(null);
            this.mUiHandler.post(new Runnable() { // from class: com.android.systemui.accessibility.floatingmenu.AccessibilityFloatingMenuView$$ExternalSyntheticLambda5
                @Override // java.lang.Runnable
                public final void run() {
                    AccessibilityFloatingMenuView.this.lambda$fadeIn$3();
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$fadeIn$3() {
        setAlpha(1.0f);
    }

    @VisibleForTesting
    void fadeOut() {
        if (this.mIsFadeEffectEnabled) {
            this.mUiHandler.postDelayed(new Runnable() { // from class: com.android.systemui.accessibility.floatingmenu.AccessibilityFloatingMenuView$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    AccessibilityFloatingMenuView.this.lambda$fadeOut$4();
                }
            }, 3000);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$fadeOut$4() {
        this.mFadeOutAnimator.start();
    }

    private boolean onTouched(MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        int x = (int) motionEvent.getX();
        int y = (int) motionEvent.getY();
        int marginStartEndWith = getMarginStartEndWith(this.mLastConfiguration);
        Rect rect = new Rect(marginStartEndWith, this.mMargin, getLayoutWidth() + marginStartEndWith, this.mMargin + getLayoutHeight());
        if (action == 0 && rect.contains(x, y)) {
            this.mIsDownInEnlargedTouchArea = true;
        }
        if (!this.mIsDownInEnlargedTouchArea) {
            return false;
        }
        if (action == 1 || action == 3) {
            this.mIsDownInEnlargedTouchArea = false;
        }
        int i = this.mMargin;
        motionEvent.setLocation((float) (x - i), (float) (y - i));
        return this.mListView.dispatchTouchEvent(motionEvent);
    }

    private WindowInsets onWindowInsetsApplied(WindowInsets windowInsets) {
        boolean isVisible = windowInsets.isVisible(WindowInsets.Type.ime());
        if (isVisible != this.mImeVisibility) {
            this.mImeVisibility = isVisible;
            updateLocationWith(this.mPosition);
        }
        return windowInsets;
    }

    private boolean hasExceededTouchSlop(int i, int i2, int i3, int i4) {
        return MathUtils.sq((float) (i3 - i)) + MathUtils.sq((float) (i4 - i2)) > this.mSquareScaledTouchSlop;
    }

    private void setRadius(float f, int i) {
        getMenuGradientDrawable().setCornerRadii(createRadii(f, i));
    }

    private Handler createUiHandler() {
        Looper myLooper = Looper.myLooper();
        Objects.requireNonNull(myLooper, "looper must not be null");
        return new Handler(myLooper);
    }

    private void updateDimensions() {
        Resources resources = getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        this.mScreenWidth = displayMetrics.widthPixels;
        this.mScreenHeight = displayMetrics.heightPixels;
        this.mMargin = resources.getDimensionPixelSize(R$dimen.accessibility_floating_menu_margin);
        this.mInset = resources.getDimensionPixelSize(R$dimen.accessibility_floating_menu_stroke_inset);
        this.mSquareScaledTouchSlop = MathUtils.sq((float) ViewConfiguration.get(getContext()).getScaledTouchSlop());
        updateItemViewDimensionsWith(this.mSizeType);
    }

    private void updateItemViewDimensionsWith(int i) {
        int i2;
        int i3;
        Resources resources = getResources();
        if (i == 0) {
            i2 = R$dimen.accessibility_floating_menu_small_padding;
        } else {
            i2 = R$dimen.accessibility_floating_menu_large_padding;
        }
        this.mPadding = resources.getDimensionPixelSize(i2);
        if (i == 0) {
            i3 = R$dimen.accessibility_floating_menu_small_width_height;
        } else {
            i3 = R$dimen.accessibility_floating_menu_large_width_height;
        }
        int dimensionPixelSize = resources.getDimensionPixelSize(i3);
        this.mIconWidth = dimensionPixelSize;
        this.mIconHeight = dimensionPixelSize;
    }

    private void updateItemViewWith(int i) {
        updateItemViewDimensionsWith(i);
        this.mAdapter.setItemPadding(this.mPadding);
        this.mAdapter.setIconWidthHeight(this.mIconWidth);
        this.mAdapter.notifyDataSetChanged();
    }

    private void initListView() {
        Drawable drawable = getContext().getDrawable(R$drawable.accessibility_floating_menu_background);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        this.mListView.setLayoutParams(new FrameLayout.LayoutParams(-2, -2));
        this.mListView.setBackground(new InstantInsetLayerDrawable(new Drawable[]{drawable}));
        this.mListView.setAdapter(this.mAdapter);
        this.mListView.setLayoutManager(linearLayoutManager);
        this.mListView.addOnItemTouchListener(this);
        this.mListView.animate().setInterpolator(new OvershootInterpolator());
        this.mListView.setAccessibilityDelegateCompat(new RecyclerViewAccessibilityDelegate(this.mListView) { // from class: com.android.systemui.accessibility.floatingmenu.AccessibilityFloatingMenuView.2
            @Override // androidx.recyclerview.widget.RecyclerViewAccessibilityDelegate
            public AccessibilityDelegateCompat getItemDelegate() {
                return new ItemDelegateCompat(this, AccessibilityFloatingMenuView.this);
            }
        });
        updateListViewWith(this.mLastConfiguration);
        addView(this.mListView);
    }

    private void updateListViewWith(Configuration configuration) {
        updateMarginWith(configuration);
        this.mListView.setElevation((float) getResources().getDimensionPixelSize(R$dimen.accessibility_floating_menu_elevation));
    }

    private WindowManager.LayoutParams createDefaultLayoutParams() {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(-2, -2, 2024, 520, -3);
        layoutParams.receiveInsetsIgnoringZOrder = true;
        layoutParams.windowAnimations = 16973827;
        layoutParams.gravity = 8388659;
        layoutParams.x = this.mAlignment == 1 ? getMaxWindowX() : getMinWindowX();
        layoutParams.y = Math.max(0, ((int) (this.mPosition.getPercentageY() * ((float) getMaxWindowY()))) - getInterval());
        updateAccessibilityTitle(layoutParams);
        return layoutParams;
    }

    /* access modifiers changed from: protected */
    @Override // android.view.View
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.mLastConfiguration.setTo(configuration);
        if ((configuration.diff(this.mLastConfiguration) & 4) != 0) {
            updateAccessibilityTitle(this.mCurrentLayoutParams);
        }
        updateDimensions();
        updateListViewWith(configuration);
        updateItemViewWith(this.mSizeType);
        updateColor();
        updateStrokeWith(configuration.uiMode, this.mAlignment);
        updateLocationWith(this.mPosition);
        updateRadiusWith(this.mSizeType, this.mRadiusType, this.mTargets.size());
        updateScrollModeWith(hasExceededMaxLayoutHeight());
        setSystemGestureExclusion();
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public void snapToLocation(int i, int i2) {
        this.mDragAnimator.cancel();
        this.mDragAnimator.removeAllUpdateListeners();
        this.mDragAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(i, i2) { // from class: com.android.systemui.accessibility.floatingmenu.AccessibilityFloatingMenuView$$ExternalSyntheticLambda1
            public final /* synthetic */ int f$1;
            public final /* synthetic */ int f$2;

            {
                this.f$1 = r2;
                this.f$2 = r3;
            }

            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                AccessibilityFloatingMenuView.this.lambda$snapToLocation$5(this.f$1, this.f$2, valueAnimator);
            }
        });
        this.mDragAnimator.start();
    }

    /* access modifiers changed from: private */
    /* renamed from: onDragAnimationUpdate */
    public void lambda$snapToLocation$5(ValueAnimator valueAnimator, int i, int i2) {
        float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        float f = 1.0f - floatValue;
        WindowManager.LayoutParams layoutParams = this.mCurrentLayoutParams;
        layoutParams.x = (int) ((((float) layoutParams.x) * f) + (((float) i) * floatValue));
        layoutParams.y = (int) ((f * ((float) layoutParams.y)) + (floatValue * ((float) i2)));
        this.mWindowManager.updateViewLayout(this, layoutParams);
    }

    private int getMinWindowX() {
        return -getMarginStartEndWith(this.mLastConfiguration);
    }

    private int getMaxWindowX() {
        return (this.mScreenWidth - getMarginStartEndWith(this.mLastConfiguration)) - getLayoutWidth();
    }

    private int getMaxWindowY() {
        return this.mScreenHeight - getWindowHeight();
    }

    private InstantInsetLayerDrawable getMenuLayerDrawable() {
        return (InstantInsetLayerDrawable) this.mListView.getBackground();
    }

    private GradientDrawable getMenuGradientDrawable() {
        return (GradientDrawable) getMenuLayerDrawable().getDrawable(0);
    }

    /* access modifiers changed from: private */
    public void updateLocationWith(Position position) {
        int transformToAlignment = transformToAlignment(position.getPercentageX());
        this.mCurrentLayoutParams.x = transformToAlignment == 1 ? getMaxWindowX() : getMinWindowX();
        this.mCurrentLayoutParams.y = Math.max(0, ((int) (position.getPercentageY() * ((float) getMaxWindowY()))) - getInterval());
        this.mWindowManager.updateViewLayout(this, this.mCurrentLayoutParams);
    }

    private int getInterval() {
        if (!this.mImeVisibility) {
            return 0;
        }
        int i = this.mScreenHeight - this.mWindowManager.getCurrentWindowMetrics().getWindowInsets().getInsets(WindowInsets.Type.ime() | WindowInsets.Type.navigationBars()).bottom;
        int windowHeight = this.mCurrentLayoutParams.y + getWindowHeight();
        if (windowHeight > i) {
            return windowHeight - i;
        }
        return 0;
    }

    private void updateMarginWith(Configuration configuration) {
        int marginStartEndWith = getMarginStartEndWith(configuration);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.mListView.getLayoutParams();
        int i = this.mMargin;
        layoutParams.setMargins(marginStartEndWith, i, marginStartEndWith, i);
        this.mListView.setLayoutParams(layoutParams);
    }

    private void updateOffsetWith(int i, int i2) {
        float layoutWidth = ((float) getLayoutWidth()) / 2.0f;
        if (i == 0) {
            layoutWidth = 0.0f;
        }
        ViewPropertyAnimator animate = this.mListView.animate();
        if (i2 != 1) {
            layoutWidth = -layoutWidth;
        }
        animate.translationX(layoutWidth);
    }

    private void updateScrollModeWith(boolean z) {
        this.mListView.setOverScrollMode(z ? 0 : 2);
    }

    private void updateColor() {
        getMenuGradientDrawable().setColor(getResources().getColor(R$color.accessibility_floating_menu_background));
    }

    private void updateStrokeWith(int i, int i2) {
        updateInsetWith(i, i2);
        int i3 = 0;
        boolean z = (i & 48) == 32;
        Resources resources = getResources();
        i3 = resources.getDimensionPixelSize(R$dimen.accessibility_floating_menu_stroke_width);
        if (z) {
        }
        getMenuGradientDrawable().setStroke(i3, resources.getColor(R$color.accessibility_floating_menu_stroke_dark));
    }

    /* access modifiers changed from: private */
    public void updateRadiusWith(int i, int i2, int i3) {
        float dimensionPixelSize = (float) getResources().getDimensionPixelSize(getRadiusResId(i, i3));
        this.mRadius = dimensionPixelSize;
        setRadius(dimensionPixelSize, i2);
    }

    /* access modifiers changed from: private */
    public void updateInsetWith(int i, int i2) {
        int i3 = 0;
        int i4 = (i & 48) == 32 ? this.mInset : 0;
        int i5 = i2 == 0 ? i4 : 0;
        if (i2 == 1) {
            i3 = i4;
        }
        setInset(i5, i3);
    }

    private void updateAccessibilityTitle(WindowManager.LayoutParams layoutParams) {
        layoutParams.accessibilityTitle = getResources().getString(17039577);
    }

    private void setInset(int i, int i2) {
        InstantInsetLayerDrawable menuLayerDrawable = getMenuLayerDrawable();
        if (menuLayerDrawable.getLayerInsetLeft(0) != i || menuLayerDrawable.getLayerInsetRight(0) != i2) {
            menuLayerDrawable.setLayerInset(0, i, 0, i2, 0);
        }
    }

    @VisibleForTesting
    boolean hasExceededMaxLayoutHeight() {
        return calculateActualLayoutHeight() > getMaxLayoutHeight();
    }

    /* access modifiers changed from: private */
    public float transformCurrentPercentageXToEdge() {
        return ((double) calculateCurrentPercentageX()) < 0.5d ? 0.0f : 1.0f;
    }

    private float calculateCurrentPercentageX() {
        return ((float) this.mCurrentLayoutParams.x) / ((float) getMaxWindowX());
    }

    /* access modifiers changed from: private */
    public float calculateCurrentPercentageY() {
        return ((float) this.mCurrentLayoutParams.y) / ((float) getMaxWindowY());
    }

    private int calculateActualLayoutHeight() {
        return ((this.mPadding + this.mIconHeight) * this.mTargets.size()) + this.mPadding;
    }

    private int getMarginStartEndWith(Configuration configuration) {
        if (configuration == null || configuration.orientation != 1) {
            return 0;
        }
        return this.mMargin;
    }

    private int getRadiusResId(int i, int i2) {
        if (i == 0) {
            return getSmallSizeResIdWith(i2);
        }
        return getLargeSizeResIdWith(i2);
    }

    private int getSmallSizeResIdWith(int i) {
        if (i > 1) {
            return R$dimen.accessibility_floating_menu_small_multiple_radius;
        }
        return R$dimen.accessibility_floating_menu_small_single_radius;
    }

    private int getLargeSizeResIdWith(int i) {
        if (i > 1) {
            return R$dimen.accessibility_floating_menu_large_multiple_radius;
        }
        return R$dimen.accessibility_floating_menu_large_single_radius;
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public Rect getAvailableBounds() {
        return new Rect(0, 0, this.mScreenWidth - getWindowWidth(), this.mScreenHeight - getWindowHeight());
    }

    private int getMaxLayoutHeight() {
        return this.mScreenHeight - (this.mMargin * 2);
    }

    private int getLayoutWidth() {
        return (this.mPadding * 2) + this.mIconWidth;
    }

    private int getLayoutHeight() {
        return Math.min(getMaxLayoutHeight(), calculateActualLayoutHeight());
    }

    private int getWindowWidth() {
        return (getMarginStartEndWith(this.mLastConfiguration) * 2) + getLayoutWidth();
    }

    private int getWindowHeight() {
        return Math.min(this.mScreenHeight, (this.mMargin * 2) + getLayoutHeight());
    }

    private void setSystemGestureExclusion() {
        post(new Runnable(new Rect(0, 0, getWindowWidth(), getWindowHeight())) { // from class: com.android.systemui.accessibility.floatingmenu.AccessibilityFloatingMenuView$$ExternalSyntheticLambda6
            public final /* synthetic */ Rect f$1;

            {
                this.f$1 = r2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                AccessibilityFloatingMenuView.this.lambda$setSystemGestureExclusion$6(this.f$1);
            }
        });
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$setSystemGestureExclusion$6(Rect rect) {
        List list;
        if (this.mIsShowing) {
            list = Collections.singletonList(rect);
        } else {
            list = Collections.emptyList();
        }
        setSystemGestureExclusionRects(list);
    }
}
