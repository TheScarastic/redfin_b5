package com.android.systemui.statusbar.notification.row;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.MathUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.view.animation.Interpolator;
import android.view.animation.PathInterpolator;
import android.widget.FrameLayout;
import com.android.internal.jank.InteractionJankMonitor;
import com.android.settingslib.Utils;
import com.android.systemui.Gefingerpoken;
import com.android.systemui.R$color;
import com.android.systemui.R$drawable;
import com.android.systemui.R$id;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.statusbar.notification.FakeShadowView;
import com.android.systemui.statusbar.notification.NotificationUtils;
/* loaded from: classes.dex */
public abstract class ActivatableNotificationView extends ExpandableOutlineView {
    private AccessibilityManager mAccessibilityManager;
    private boolean mActivated;
    private float mAnimationTranslationY;
    private float mAppearAnimationTranslation;
    private ValueAnimator mAppearAnimator;
    private ValueAnimator mBackgroundColorAnimator;
    NotificationBackgroundView mBackgroundNormal;
    private Interpolator mCurrentAppearInterpolator;
    private int mCurrentBackgroundTint;
    private boolean mDismissed;
    private boolean mDrawingAppearAnimation;
    private FakeShadowView mFakeShadow;
    private int mHeadsUpAddStartLocation;
    private float mHeadsUpLocation;
    private boolean mIsAppearing;
    private boolean mIsBelowSpeedBump;
    private boolean mIsHeadsUpAnimation;
    private long mLastActionUpTime;
    private float mNormalBackgroundVisibilityAmount;
    private int mNormalColor;
    private int mNormalRippleColor;
    private OnActivatedListener mOnActivatedListener;
    private float mOverrideAmount;
    private int mOverrideTint;
    private boolean mRefocusOnDismiss;
    private boolean mShadowHidden;
    private int mStartTint;
    private int mTargetTint;
    private int mTintedRippleColor;
    private Gefingerpoken mTouchHandler;
    private static final Interpolator ACTIVATE_INVERSE_INTERPOLATOR = new PathInterpolator(0.6f, 0.0f, 0.5f, 1.0f);
    private static final Interpolator ACTIVATE_INVERSE_ALPHA_INTERPOLATOR = new PathInterpolator(0.0f, 0.0f, 0.5f, 1.0f);
    int mBgTint = 0;
    private RectF mAppearAnimationRect = new RectF();
    private float mAppearAnimationFraction = -1.0f;
    private ValueAnimator.AnimatorUpdateListener mBackgroundVisibilityUpdater = new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.statusbar.notification.row.ActivatableNotificationView.1
        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            ActivatableNotificationView activatableNotificationView = ActivatableNotificationView.this;
            activatableNotificationView.setNormalBackgroundVisibilityAmount(activatableNotificationView.mBackgroundNormal.getAlpha());
        }
    };
    private final Interpolator mSlowOutFastInInterpolator = new PathInterpolator(0.8f, 0.0f, 0.6f, 1.0f);
    private final Interpolator mSlowOutLinearInInterpolator = new PathInterpolator(0.8f, 0.0f, 1.0f, 1.0f);

    /* loaded from: classes.dex */
    public interface OnActivatedListener {
        void onActivated(ActivatableNotificationView activatableNotificationView);

        void onActivationReset(ActivatableNotificationView activatableNotificationView);
    }

    protected abstract View getContentView();

    /* access modifiers changed from: protected */
    public boolean handleSlideBack() {
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean hideBackground() {
        return false;
    }

    /* access modifiers changed from: protected */
    public void onAppearAnimationFinished(boolean z) {
    }

    protected void onBelowSpeedBumpChanged() {
    }

    public void onTap() {
    }

    public ActivatableNotificationView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setClipChildren(false);
        setClipToPadding(false);
        updateColors();
        initDimens();
    }

    private void updateColors() {
        this.mNormalColor = Utils.getColorAttrDefaultColor(((FrameLayout) this).mContext, 17956909);
        this.mTintedRippleColor = ((FrameLayout) this).mContext.getColor(R$color.notification_ripple_tinted_color);
        this.mNormalRippleColor = ((FrameLayout) this).mContext.getColor(R$color.notification_ripple_untinted_color);
    }

    private void initDimens() {
        this.mHeadsUpAddStartLocation = getResources().getDimensionPixelSize(17105378);
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableOutlineView
    public void onDensityOrFontScaleChanged() {
        super.onDensityOrFontScaleChanged();
        initDimens();
    }

    public void updateBackgroundColors() {
        updateColors();
        initBackground();
        updateBackgroundTint();
    }

    /* access modifiers changed from: protected */
    @Override // android.view.View
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mBackgroundNormal = (NotificationBackgroundView) findViewById(R$id.backgroundNormal);
        FakeShadowView fakeShadowView = (FakeShadowView) findViewById(R$id.fake_shadow);
        this.mFakeShadow = fakeShadowView;
        this.mShadowHidden = fakeShadowView.getVisibility() != 0;
        initBackground();
        updateBackgroundTint();
        updateOutlineAlpha();
    }

    /* access modifiers changed from: protected */
    public void initBackground() {
        this.mBackgroundNormal.setCustomBackground(R$drawable.notification_material_bg);
    }

    /* access modifiers changed from: protected */
    public void updateBackground() {
        this.mBackgroundNormal.setVisibility(hideBackground() ? 4 : 0);
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        Gefingerpoken gefingerpoken = this.mTouchHandler;
        if (gefingerpoken == null || !gefingerpoken.onInterceptTouchEvent(motionEvent)) {
            return super.onInterceptTouchEvent(motionEvent);
        }
        return true;
    }

    /* access modifiers changed from: package-private */
    public void setLastActionUpTime(long j) {
        this.mLastActionUpTime = j;
    }

    public long getAndResetLastActionUpTime() {
        long j = this.mLastActionUpTime;
        this.mLastActionUpTime = 0;
        return j;
    }

    @Override // android.view.View, android.view.ViewGroup
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        this.mBackgroundNormal.setState(getDrawableState());
    }

    /* access modifiers changed from: package-private */
    public void setRippleAllowed(boolean z) {
        this.mBackgroundNormal.setPressedAllowed(z);
    }

    /* access modifiers changed from: package-private */
    public void makeActive() {
        this.mActivated = true;
        OnActivatedListener onActivatedListener = this.mOnActivatedListener;
        if (onActivatedListener != null) {
            onActivatedListener.onActivated(this);
        }
    }

    public void makeInactive(boolean z) {
        if (this.mActivated) {
            this.mActivated = false;
        }
        OnActivatedListener onActivatedListener = this.mOnActivatedListener;
        if (onActivatedListener != null) {
            onActivatedListener.onActivationReset(this);
        }
    }

    private void updateOutlineAlpha() {
        setOutlineAlpha((0.3f * this.mNormalBackgroundVisibilityAmount) + 0.7f);
    }

    /* access modifiers changed from: private */
    public void setNormalBackgroundVisibilityAmount(float f) {
        this.mNormalBackgroundVisibilityAmount = f;
        updateOutlineAlpha();
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView
    public void setBelowSpeedBump(boolean z) {
        super.setBelowSpeedBump(z);
        if (z != this.mIsBelowSpeedBump) {
            this.mIsBelowSpeedBump = z;
            updateBackgroundTint();
            onBelowSpeedBumpChanged();
        }
    }

    /* access modifiers changed from: protected */
    public void setTintColor(int i) {
        setTintColor(i, false);
    }

    /* access modifiers changed from: package-private */
    public void setTintColor(int i, boolean z) {
        if (i != this.mBgTint) {
            this.mBgTint = i;
            updateBackgroundTint(z);
        }
    }

    public void setOverrideTintColor(int i, float f) {
        this.mOverrideTint = i;
        this.mOverrideAmount = f;
        setBackgroundTintColor(calculateBgColor());
    }

    /* access modifiers changed from: protected */
    public void updateBackgroundTint() {
        updateBackgroundTint(false);
    }

    private void updateBackgroundTint(boolean z) {
        ValueAnimator valueAnimator = this.mBackgroundColorAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        this.mBackgroundNormal.setRippleColor(getRippleColor());
        int calculateBgColor = calculateBgColor();
        if (!z) {
            setBackgroundTintColor(calculateBgColor);
            return;
        }
        int i = this.mCurrentBackgroundTint;
        if (calculateBgColor != i) {
            this.mStartTint = i;
            this.mTargetTint = calculateBgColor;
            ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
            this.mBackgroundColorAnimator = ofFloat;
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.statusbar.notification.row.ActivatableNotificationView$$ExternalSyntheticLambda0
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                    ActivatableNotificationView.this.lambda$updateBackgroundTint$0(valueAnimator2);
                }
            });
            this.mBackgroundColorAnimator.setDuration(360L);
            this.mBackgroundColorAnimator.setInterpolator(Interpolators.LINEAR);
            this.mBackgroundColorAnimator.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.statusbar.notification.row.ActivatableNotificationView.2
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    ActivatableNotificationView.this.mBackgroundColorAnimator = null;
                }
            });
            this.mBackgroundColorAnimator.start();
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$updateBackgroundTint$0(ValueAnimator valueAnimator) {
        setBackgroundTintColor(NotificationUtils.interpolateColors(this.mStartTint, this.mTargetTint, valueAnimator.getAnimatedFraction()));
    }

    /* access modifiers changed from: protected */
    public void setBackgroundTintColor(int i) {
        if (i != this.mCurrentBackgroundTint) {
            this.mCurrentBackgroundTint = i;
            if (i == this.mNormalColor) {
                i = 0;
            }
            this.mBackgroundNormal.setTint(i);
        }
    }

    /* access modifiers changed from: protected */
    public void updateBackgroundClipping() {
        this.mBackgroundNormal.setBottomAmountClips(!isChildInGroup());
    }

    /* access modifiers changed from: protected */
    @Override // com.android.systemui.statusbar.notification.row.ExpandableView, android.widget.FrameLayout, android.view.View, android.view.ViewGroup
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        setPivotX((float) (getWidth() / 2));
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableOutlineView, com.android.systemui.statusbar.notification.row.ExpandableView
    public void setActualHeight(int i, boolean z) {
        super.setActualHeight(i, z);
        setPivotY((float) (i / 2));
        this.mBackgroundNormal.setActualHeight(i);
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableOutlineView, com.android.systemui.statusbar.notification.row.ExpandableView
    public void setClipTopAmount(int i) {
        super.setClipTopAmount(i);
        this.mBackgroundNormal.setClipTopAmount(i);
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableOutlineView, com.android.systemui.statusbar.notification.row.ExpandableView
    public void setClipBottomAmount(int i) {
        super.setClipBottomAmount(i);
        this.mBackgroundNormal.setClipBottomAmount(i);
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView
    public long performRemoveAnimation(long j, long j2, float f, boolean z, float f2, Runnable runnable, AnimatorListenerAdapter animatorListenerAdapter) {
        enableAppearDrawing(true);
        this.mIsHeadsUpAnimation = z;
        this.mHeadsUpLocation = f2;
        if (this.mDrawingAppearAnimation) {
            startAppearAnimation(false, f, j2, j, runnable, animatorListenerAdapter);
            return 0;
        } else if (runnable == null) {
            return 0;
        } else {
            runnable.run();
            return 0;
        }
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView
    public void performAddAnimation(long j, long j2, boolean z) {
        enableAppearDrawing(true);
        this.mIsHeadsUpAnimation = z;
        this.mHeadsUpLocation = (float) this.mHeadsUpAddStartLocation;
        if (this.mDrawingAppearAnimation) {
            startAppearAnimation(true, z ? 0.0f : -1.0f, j, j2, null, null);
        }
    }

    private void startAppearAnimation(final boolean z, float f, long j, long j2, final Runnable runnable, AnimatorListenerAdapter animatorListenerAdapter) {
        this.mAnimationTranslationY = f * ((float) getActualHeight());
        cancelAppearAnimation();
        float f2 = 1.0f;
        if (this.mAppearAnimationFraction == -1.0f) {
            if (z) {
                this.mAppearAnimationFraction = 0.0f;
                this.mAppearAnimationTranslation = this.mAnimationTranslationY;
            } else {
                this.mAppearAnimationFraction = 1.0f;
                this.mAppearAnimationTranslation = 0.0f;
            }
        }
        this.mIsAppearing = z;
        if (z) {
            this.mCurrentAppearInterpolator = Interpolators.FAST_OUT_SLOW_IN;
        } else {
            this.mCurrentAppearInterpolator = this.mSlowOutFastInInterpolator;
            f2 = 0.0f;
        }
        ValueAnimator ofFloat = ValueAnimator.ofFloat(this.mAppearAnimationFraction, f2);
        this.mAppearAnimator = ofFloat;
        ofFloat.setInterpolator(Interpolators.LINEAR);
        this.mAppearAnimator.setDuration((long) (((float) j2) * Math.abs(this.mAppearAnimationFraction - f2)));
        this.mAppearAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.statusbar.notification.row.ActivatableNotificationView$$ExternalSyntheticLambda1
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                ActivatableNotificationView.this.lambda$startAppearAnimation$1(valueAnimator);
            }
        });
        if (animatorListenerAdapter != null) {
            this.mAppearAnimator.addListener(animatorListenerAdapter);
        }
        if (j > 0) {
            updateAppearAnimationAlpha();
            updateAppearRect();
            this.mAppearAnimator.setStartDelay(j);
        }
        this.mAppearAnimator.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.statusbar.notification.row.ActivatableNotificationView.3
            private boolean mWasCancelled;

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                Runnable runnable2 = runnable;
                if (runnable2 != null) {
                    runnable2.run();
                }
                if (!this.mWasCancelled) {
                    ActivatableNotificationView.this.enableAppearDrawing(false);
                    ActivatableNotificationView.this.onAppearAnimationFinished(z);
                    InteractionJankMonitor.getInstance().end(ActivatableNotificationView.this.getCujType(z));
                    return;
                }
                InteractionJankMonitor.getInstance().cancel(ActivatableNotificationView.this.getCujType(z));
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animator) {
                this.mWasCancelled = false;
                InteractionJankMonitor.getInstance().begin(new InteractionJankMonitor.Configuration.Builder(ActivatableNotificationView.this.getCujType(z)).setView(ActivatableNotificationView.this));
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animator) {
                this.mWasCancelled = true;
            }
        });
        this.mAppearAnimator.start();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$startAppearAnimation$1(ValueAnimator valueAnimator) {
        this.mAppearAnimationFraction = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        updateAppearAnimationAlpha();
        updateAppearRect();
        invalidate();
    }

    /* access modifiers changed from: private */
    public int getCujType(boolean z) {
        return this.mIsHeadsUpAnimation ? z ? 12 : 13 : z ? 14 : 15;
    }

    private void cancelAppearAnimation() {
        ValueAnimator valueAnimator = this.mAppearAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
            this.mAppearAnimator = null;
        }
    }

    public void cancelAppearDrawing() {
        cancelAppearAnimation();
        enableAppearDrawing(false);
    }

    private void updateAppearRect() {
        float interpolation = this.mCurrentAppearInterpolator.getInterpolation(this.mAppearAnimationFraction);
        this.mAppearAnimationTranslation = (1.0f - interpolation) * this.mAnimationTranslationY;
        setOutlineRect(0.0f, this.mAppearAnimationTranslation, (float) getWidth(), (((float) getActualHeight()) * interpolation) + this.mAppearAnimationTranslation);
    }

    private float getInterpolatedAppearAnimationFraction() {
        float f = this.mAppearAnimationFraction;
        if (f >= 0.0f) {
            return this.mCurrentAppearInterpolator.getInterpolation(f);
        }
        return 1.0f;
    }

    private void updateAppearAnimationAlpha() {
        setContentAlpha(Interpolators.ALPHA_IN.getInterpolation((MathUtils.constrain(this.mAppearAnimationFraction, 0.4f, 1.0f) - 0.4f) / 0.6f));
    }

    private void setContentAlpha(float f) {
        View contentView = getContentView();
        if (contentView.hasOverlappingRendering()) {
            int i = (f == 0.0f || f == 1.0f) ? 0 : 2;
            if (contentView.getLayerType() != i) {
                contentView.setLayerType(i, null);
            }
        }
        contentView.setAlpha(f);
    }

    /* access modifiers changed from: protected */
    @Override // com.android.systemui.statusbar.notification.row.ExpandableOutlineView
    public void applyRoundness() {
        super.applyRoundness();
        applyBackgroundRoundness(getCurrentBackgroundRadiusTop(), getCurrentBackgroundRadiusBottom());
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableOutlineView
    public float getCurrentBackgroundRadiusTop() {
        return MathUtils.lerp(0.0f, super.getCurrentBackgroundRadiusTop(), getInterpolatedAppearAnimationFraction());
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableOutlineView
    public float getCurrentBackgroundRadiusBottom() {
        return MathUtils.lerp(0.0f, super.getCurrentBackgroundRadiusBottom(), getInterpolatedAppearAnimationFraction());
    }

    private void applyBackgroundRoundness(float f, float f2) {
        this.mBackgroundNormal.setRadius(f, f2);
    }

    /* access modifiers changed from: protected */
    public void setBackgroundTop(int i) {
        this.mBackgroundNormal.setBackgroundTop(i);
    }

    public int calculateBgColor() {
        return calculateBgColor(true, true);
    }

    /* access modifiers changed from: protected */
    @Override // com.android.systemui.statusbar.notification.row.ExpandableOutlineView
    public boolean childNeedsClipping(View view) {
        if (!(view instanceof NotificationBackgroundView) || !isClippingNeeded()) {
            return super.childNeedsClipping(view);
        }
        return true;
    }

    private int calculateBgColor(boolean z, boolean z2) {
        int i;
        if (z2 && this.mOverrideTint != 0) {
            return NotificationUtils.interpolateColors(calculateBgColor(z, false), this.mOverrideTint, this.mOverrideAmount);
        }
        if (!z || (i = this.mBgTint) == 0) {
            return this.mNormalColor;
        }
        return i;
    }

    private int getRippleColor() {
        if (this.mBgTint != 0) {
            return this.mTintedRippleColor;
        }
        return this.mNormalRippleColor;
    }

    /* access modifiers changed from: private */
    public void enableAppearDrawing(boolean z) {
        if (z != this.mDrawingAppearAnimation) {
            this.mDrawingAppearAnimation = z;
            if (!z) {
                setContentAlpha(1.0f);
                this.mAppearAnimationFraction = -1.0f;
                setOutlineRect(null);
            }
            invalidate();
        }
    }

    public boolean isDrawingAppearAnimation() {
        return this.mDrawingAppearAnimation;
    }

    /* access modifiers changed from: protected */
    @Override // android.view.View, android.view.ViewGroup
    public void dispatchDraw(Canvas canvas) {
        if (this.mDrawingAppearAnimation) {
            canvas.save();
            canvas.translate(0.0f, this.mAppearAnimationTranslation);
        }
        super.dispatchDraw(canvas);
        if (this.mDrawingAppearAnimation) {
            canvas.restore();
        }
    }

    public void setOnActivatedListener(OnActivatedListener onActivatedListener) {
        this.mOnActivatedListener = onActivatedListener;
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView
    public void setFakeShadowIntensity(float f, float f2, int i, int i2) {
        boolean z = this.mShadowHidden;
        boolean z2 = f == 0.0f;
        this.mShadowHidden = z2;
        if (!z2 || !z) {
            this.mFakeShadow.setFakeShadowTranslationZ(f * (getTranslationZ() + 0.1f), f2, i, i2);
        }
    }

    public int getBackgroundColorWithoutTint() {
        return calculateBgColor(false, false);
    }

    public int getCurrentBackgroundTint() {
        return this.mCurrentBackgroundTint;
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView
    public int getHeadsUpHeightWithoutHeader() {
        return getHeight();
    }

    public void dismiss(boolean z) {
        this.mDismissed = true;
        this.mRefocusOnDismiss = z;
    }

    public void unDismiss() {
        this.mDismissed = false;
    }

    public boolean isDismissed() {
        return this.mDismissed;
    }

    public boolean shouldRefocusOnDismiss() {
        return this.mRefocusOnDismiss || isAccessibilityFocused();
    }

    /* access modifiers changed from: package-private */
    public void setTouchHandler(Gefingerpoken gefingerpoken) {
        this.mTouchHandler = gefingerpoken;
    }

    public void setAccessibilityManager(AccessibilityManager accessibilityManager) {
        this.mAccessibilityManager = accessibilityManager;
    }
}
