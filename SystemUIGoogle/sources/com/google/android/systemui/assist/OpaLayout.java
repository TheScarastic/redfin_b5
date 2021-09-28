package com.google.android.systemui.assist;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.Drawable;
import android.os.Trace;
import android.util.ArraySet;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Property;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.PathInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.android.systemui.Dependency;
import com.android.systemui.R$dimen;
import com.android.systemui.R$drawable;
import com.android.systemui.R$id;
import com.android.systemui.R$style;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.assist.AssistManager;
import com.android.systemui.navigationbar.buttons.ButtonInterface;
import com.android.systemui.navigationbar.buttons.KeyButtonDrawable;
import com.android.systemui.navigationbar.buttons.KeyButtonView;
import com.android.systemui.recents.OverviewProxyService;
import com.google.android.systemui.elmyra.feedback.FeedbackEffect;
import com.google.android.systemui.elmyra.sensors.GestureSensor;
import java.util.ArrayList;
/* loaded from: classes2.dex */
public class OpaLayout extends FrameLayout implements ButtonInterface, FeedbackEffect {
    private final Interpolator HOME_DISAPPEAR_INTERPOLATOR;
    private final ArrayList<View> mAnimatedViews;
    private int mAnimationState;
    private View mBlue;
    private View mBottom;
    private final ArraySet<Animator> mCurrentAnimators;
    private boolean mDelayTouchFeedback;
    private final Runnable mDiamondAnimation;
    private boolean mDiamondAnimationDelayed;
    private final Interpolator mDiamondInterpolator;
    private long mGestureAnimationSetDuration;
    private AnimatorSet mGestureAnimatorSet;
    private AnimatorSet mGestureLineSet;
    private int mGestureState;
    private View mGreen;
    private ImageView mHalo;
    private KeyButtonView mHome;
    private int mHomeDiameter;
    private boolean mIsPressed;
    private boolean mIsVertical;
    private View mLeft;
    private boolean mOpaEnabled;
    private boolean mOpaEnabledNeedsUpdate;
    private final OverviewProxyService.OverviewProxyListener mOverviewProxyListener;
    private OverviewProxyService mOverviewProxyService;
    private View mRed;
    private Resources mResources;
    private final Runnable mRetract;
    private View mRight;
    private long mStartTime;
    private View mTop;
    private int mTouchDownX;
    private int mTouchDownY;
    private ImageView mWhite;
    private ImageView mWhiteCutout;
    private boolean mWindowVisible;
    private View mYellow;

    public OpaLayout(Context context) {
        this(context, null);
    }

    public OpaLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public OpaLayout(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.HOME_DISAPPEAR_INTERPOLATOR = new PathInterpolator(0.65f, 0.0f, 1.0f, 1.0f);
        this.mDiamondInterpolator = new PathInterpolator(0.2f, 0.0f, 0.2f, 1.0f);
        this.mCurrentAnimators = new ArraySet<>();
        this.mAnimatedViews = new ArrayList<>();
        this.mAnimationState = 0;
        this.mGestureState = 0;
        this.mRetract = new Runnable() { // from class: com.google.android.systemui.assist.OpaLayout.1
            @Override // java.lang.Runnable
            public void run() {
                OpaLayout.this.cancelCurrentAnimation("retract");
                OpaLayout.this.startRetractAnimation();
            }
        };
        this.mOverviewProxyListener = new OverviewProxyService.OverviewProxyListener() { // from class: com.google.android.systemui.assist.OpaLayout.2
            @Override // com.android.systemui.recents.OverviewProxyService.OverviewProxyListener
            public void onConnectionChanged(boolean z) {
                OpaLayout.this.updateOpaLayout();
            }
        };
        this.mDiamondAnimation = new Runnable() { // from class: com.google.android.systemui.assist.OpaLayout$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                OpaLayout.this.lambda$new$1();
            }
        };
    }

    public OpaLayout(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.mResources = getResources();
        this.mBlue = findViewById(R$id.blue);
        this.mRed = findViewById(R$id.red);
        this.mYellow = findViewById(R$id.yellow);
        this.mGreen = findViewById(R$id.green);
        this.mWhite = (ImageView) findViewById(R$id.white);
        this.mWhiteCutout = (ImageView) findViewById(R$id.white_cutout);
        this.mHalo = (ImageView) findViewById(R$id.halo);
        this.mHome = (KeyButtonView) findViewById(R$id.home_button);
        this.mHalo.setImageDrawable(KeyButtonDrawable.create(new ContextThemeWrapper(getContext(), R$style.DualToneLightTheme), new ContextThemeWrapper(getContext(), R$style.DualToneDarkTheme), R$drawable.halo, true, null));
        this.mHomeDiameter = this.mResources.getDimensionPixelSize(R$dimen.opa_disabled_home_diameter);
        Paint paint = new Paint();
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        this.mWhiteCutout.setLayerType(2, paint);
        this.mAnimatedViews.add(this.mBlue);
        this.mAnimatedViews.add(this.mRed);
        this.mAnimatedViews.add(this.mYellow);
        this.mAnimatedViews.add(this.mGreen);
        this.mAnimatedViews.add(this.mWhite);
        this.mAnimatedViews.add(this.mWhiteCutout);
        this.mAnimatedViews.add(this.mHalo);
        this.mOverviewProxyService = (OverviewProxyService) Dependency.get(OverviewProxyService.class);
    }

    @Override // android.view.View
    public void onWindowVisibilityChanged(int i) {
        super.onWindowVisibilityChanged(i);
        this.mWindowVisible = i == 0;
        if (i == 0) {
            updateOpaLayout();
            return;
        }
        cancelCurrentAnimation("winVis=" + i);
        skipToStartingValue();
    }

    @Override // android.view.View
    public void setOnLongClickListener(View.OnLongClickListener onLongClickListener) {
        if (onLongClickListener == null) {
            this.mHome.setLongClickable(false);
            return;
        }
        this.mHome.setLongClickable(true);
        this.mHome.setOnLongClickListener(new View.OnLongClickListener(onLongClickListener) { // from class: com.google.android.systemui.assist.OpaLayout$$ExternalSyntheticLambda0
            public final /* synthetic */ View.OnLongClickListener f$1;

            {
                this.f$1 = r2;
            }

            @Override // android.view.View.OnLongClickListener
            public final boolean onLongClick(View view) {
                return OpaLayout.this.lambda$setOnLongClickListener$0(this.f$1, view);
            }
        });
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$setOnLongClickListener$0(View.OnLongClickListener onLongClickListener, View view) {
        return onLongClickListener.onLongClick(this.mHome);
    }

    @Override // android.view.View
    public void setOnTouchListener(View.OnTouchListener onTouchListener) {
        this.mHome.setOnTouchListener(onTouchListener);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1() {
        if (this.mCurrentAnimators.isEmpty()) {
            startDiamondAnimation();
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0020, code lost:
        if (r0 != 3) goto L_0x00dc;
     */
    @Override // android.view.ViewGroup
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean onInterceptTouchEvent(android.view.MotionEvent r9) {
        /*
        // Method dump skipped, instructions count: 221
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.systemui.assist.OpaLayout.onInterceptTouchEvent(android.view.MotionEvent):boolean");
    }

    @Override // android.view.View
    public void setAccessibilityDelegate(View.AccessibilityDelegate accessibilityDelegate) {
        super.setAccessibilityDelegate(accessibilityDelegate);
        this.mHome.setAccessibilityDelegate(accessibilityDelegate);
    }

    @Override // com.android.systemui.navigationbar.buttons.ButtonInterface
    public void setImageDrawable(Drawable drawable) {
        this.mWhite.setImageDrawable(drawable);
        this.mWhiteCutout.setImageDrawable(drawable);
    }

    @Override // com.android.systemui.navigationbar.buttons.ButtonInterface
    public void abortCurrentGesture() {
        Trace.beginSection("OpaLayout.abortCurrentGesture: animState=" + this.mAnimationState);
        Trace.endSection();
        this.mHome.abortCurrentGesture();
        this.mIsPressed = false;
        this.mDiamondAnimationDelayed = false;
        removeCallbacks(this.mDiamondAnimation);
        cancelLongPress();
        int i = this.mAnimationState;
        if (i == 3 || i == 1) {
            this.mRetract.run();
        }
    }

    @Override // android.view.View
    protected void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        updateOpaLayout();
    }

    @Override // android.view.View, android.view.ViewGroup
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mOverviewProxyService.addCallback(this.mOverviewProxyListener);
        this.mOpaEnabledNeedsUpdate = true;
        post(new Runnable() { // from class: com.google.android.systemui.assist.OpaLayout$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                OpaLayout.this.getOpaEnabled();
            }
        });
    }

    @Override // android.view.View, android.view.ViewGroup
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mOverviewProxyService.removeCallback(this.mOverviewProxyListener);
    }

    private void startDiamondAnimation() {
        if (allowAnimations()) {
            this.mCurrentAnimators.clear();
            setDotsVisible();
            this.mCurrentAnimators.addAll((ArraySet<? extends Animator>) getDiamondAnimatorSet());
            this.mAnimationState = 1;
            startAll(this.mCurrentAnimators);
            return;
        }
        skipToStartingValue();
    }

    /* access modifiers changed from: private */
    public void startRetractAnimation() {
        if (allowAnimations()) {
            this.mCurrentAnimators.clear();
            this.mCurrentAnimators.addAll((ArraySet<? extends Animator>) getRetractAnimatorSet());
            this.mAnimationState = 2;
            startAll(this.mCurrentAnimators);
            return;
        }
        skipToStartingValue();
    }

    /* access modifiers changed from: private */
    public void startLineAnimation() {
        if (allowAnimations()) {
            this.mCurrentAnimators.clear();
            this.mCurrentAnimators.addAll((ArraySet<? extends Animator>) getLineAnimatorSet());
            this.mAnimationState = 3;
            startAll(this.mCurrentAnimators);
            return;
        }
        skipToStartingValue();
    }

    /* access modifiers changed from: private */
    public void startCollapseAnimation() {
        if (allowAnimations()) {
            this.mCurrentAnimators.clear();
            this.mCurrentAnimators.addAll((ArraySet<? extends Animator>) getCollapseAnimatorSet());
            this.mAnimationState = 3;
            startAll(this.mCurrentAnimators);
            return;
        }
        skipToStartingValue();
    }

    private void startAll(ArraySet<Animator> arraySet) {
        for (int size = arraySet.size() - 1; size >= 0; size--) {
            arraySet.valueAt(size).start();
        }
        for (int size2 = this.mAnimatedViews.size() - 1; size2 >= 0; size2--) {
            this.mAnimatedViews.get(size2).invalidate();
        }
    }

    private boolean allowAnimations() {
        return isAttachedToWindow() && this.mWindowVisible;
    }

    private ArraySet<Animator> getDiamondAnimatorSet() {
        ArraySet<Animator> arraySet = new ArraySet<>();
        View view = this.mTop;
        Property<View, Float> property = View.Y;
        float y = view.getY();
        Resources resources = this.mResources;
        int i = R$dimen.opa_diamond_translation;
        arraySet.add(getPropertyAnimator(view, property, (-OpaUtils.getPxVal(resources, i)) + y, 200, this.mDiamondInterpolator));
        View view2 = this.mTop;
        Property<View, Float> property2 = FrameLayout.SCALE_X;
        Interpolator interpolator = Interpolators.FAST_OUT_SLOW_IN;
        arraySet.add(getPropertyAnimator(view2, property2, 0.8f, 200, interpolator));
        arraySet.add(getPropertyAnimator(this.mTop, FrameLayout.SCALE_Y, 0.8f, 200, interpolator));
        View view3 = this.mBottom;
        arraySet.add(getPropertyAnimator(view3, View.Y, view3.getY() + OpaUtils.getPxVal(this.mResources, i), 200, this.mDiamondInterpolator));
        arraySet.add(getPropertyAnimator(this.mBottom, FrameLayout.SCALE_X, 0.8f, 200, interpolator));
        arraySet.add(getPropertyAnimator(this.mBottom, FrameLayout.SCALE_Y, 0.8f, 200, interpolator));
        View view4 = this.mLeft;
        arraySet.add(getPropertyAnimator(view4, View.X, view4.getX() + (-OpaUtils.getPxVal(this.mResources, i)), 200, this.mDiamondInterpolator));
        arraySet.add(getPropertyAnimator(this.mLeft, FrameLayout.SCALE_X, 0.8f, 200, interpolator));
        arraySet.add(getPropertyAnimator(this.mLeft, FrameLayout.SCALE_Y, 0.8f, 200, interpolator));
        View view5 = this.mRight;
        arraySet.add(getPropertyAnimator(view5, View.X, view5.getX() + OpaUtils.getPxVal(this.mResources, i), 200, this.mDiamondInterpolator));
        arraySet.add(getPropertyAnimator(this.mRight, FrameLayout.SCALE_X, 0.8f, 200, interpolator));
        arraySet.add(getPropertyAnimator(this.mRight, FrameLayout.SCALE_Y, 0.8f, 200, interpolator));
        arraySet.add(getPropertyAnimator(this.mWhite, FrameLayout.SCALE_X, 0.625f, 200, interpolator));
        arraySet.add(getPropertyAnimator(this.mWhite, FrameLayout.SCALE_Y, 0.625f, 200, interpolator));
        arraySet.add(getPropertyAnimator(this.mWhiteCutout, FrameLayout.SCALE_X, 0.625f, 200, interpolator));
        arraySet.add(getPropertyAnimator(this.mWhiteCutout, FrameLayout.SCALE_Y, 0.625f, 200, interpolator));
        arraySet.add(getPropertyAnimator(this.mHalo, FrameLayout.SCALE_X, 0.47619048f, 100, interpolator));
        arraySet.add(getPropertyAnimator(this.mHalo, FrameLayout.SCALE_Y, 0.47619048f, 100, interpolator));
        arraySet.add(getPropertyAnimator(this.mHalo, View.ALPHA, 0.0f, 100, interpolator));
        getLongestAnim(arraySet).addListener(new AnimatorListenerAdapter() { // from class: com.google.android.systemui.assist.OpaLayout.3
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animator) {
                Trace.beginSection("OpaLayout.start.diamond");
                Trace.endSection();
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animator) {
                Trace.beginSection("OpaLayout.cancel.diamond");
                Trace.endSection();
                OpaLayout.this.mCurrentAnimators.clear();
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                Trace.beginSection("OpaLayout.end.diamond");
                Trace.endSection();
                OpaLayout.this.startLineAnimation();
            }
        });
        return arraySet;
    }

    private ArraySet<Animator> getRetractAnimatorSet() {
        ArraySet<Animator> arraySet = new ArraySet<>();
        View view = this.mRed;
        Property<View, Float> property = FrameLayout.TRANSLATION_X;
        Interpolator interpolator = OpaUtils.INTERPOLATOR_40_OUT;
        arraySet.add(getPropertyAnimator(view, property, 0.0f, 190, interpolator));
        arraySet.add(getPropertyAnimator(this.mRed, FrameLayout.TRANSLATION_Y, 0.0f, 190, interpolator));
        arraySet.add(getPropertyAnimator(this.mRed, FrameLayout.SCALE_X, 1.0f, 190, interpolator));
        arraySet.add(getPropertyAnimator(this.mRed, FrameLayout.SCALE_Y, 1.0f, 190, interpolator));
        arraySet.add(getPropertyAnimator(this.mBlue, FrameLayout.TRANSLATION_X, 0.0f, 190, interpolator));
        arraySet.add(getPropertyAnimator(this.mBlue, FrameLayout.TRANSLATION_Y, 0.0f, 190, interpolator));
        arraySet.add(getPropertyAnimator(this.mBlue, FrameLayout.SCALE_X, 1.0f, 190, interpolator));
        arraySet.add(getPropertyAnimator(this.mBlue, FrameLayout.SCALE_Y, 1.0f, 190, interpolator));
        arraySet.add(getPropertyAnimator(this.mGreen, FrameLayout.TRANSLATION_X, 0.0f, 190, interpolator));
        arraySet.add(getPropertyAnimator(this.mGreen, FrameLayout.TRANSLATION_Y, 0.0f, 190, interpolator));
        arraySet.add(getPropertyAnimator(this.mGreen, FrameLayout.SCALE_X, 1.0f, 190, interpolator));
        arraySet.add(getPropertyAnimator(this.mGreen, FrameLayout.SCALE_Y, 1.0f, 190, interpolator));
        arraySet.add(getPropertyAnimator(this.mYellow, FrameLayout.TRANSLATION_X, 0.0f, 190, interpolator));
        arraySet.add(getPropertyAnimator(this.mYellow, FrameLayout.TRANSLATION_Y, 0.0f, 190, interpolator));
        arraySet.add(getPropertyAnimator(this.mYellow, FrameLayout.SCALE_X, 1.0f, 190, interpolator));
        arraySet.add(getPropertyAnimator(this.mYellow, FrameLayout.SCALE_Y, 1.0f, 190, interpolator));
        arraySet.add(getPropertyAnimator(this.mWhite, FrameLayout.SCALE_X, 1.0f, 190, interpolator));
        arraySet.add(getPropertyAnimator(this.mWhite, FrameLayout.SCALE_Y, 1.0f, 190, interpolator));
        arraySet.add(getPropertyAnimator(this.mWhiteCutout, FrameLayout.SCALE_X, 1.0f, 190, interpolator));
        arraySet.add(getPropertyAnimator(this.mWhiteCutout, FrameLayout.SCALE_Y, 1.0f, 190, interpolator));
        ImageView imageView = this.mHalo;
        Property<View, Float> property2 = FrameLayout.SCALE_X;
        Interpolator interpolator2 = Interpolators.FAST_OUT_SLOW_IN;
        arraySet.add(getPropertyAnimator(imageView, property2, 1.0f, 190, interpolator2));
        arraySet.add(getPropertyAnimator(this.mHalo, FrameLayout.SCALE_Y, 1.0f, 190, interpolator2));
        arraySet.add(getPropertyAnimator(this.mHalo, FrameLayout.ALPHA, 1.0f, 190, interpolator2));
        getLongestAnim(arraySet).addListener(new AnimatorListenerAdapter() { // from class: com.google.android.systemui.assist.OpaLayout.4
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animator) {
                Trace.beginSection("OpaLayout.start.retract");
                Trace.endSection();
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animator) {
                Trace.beginSection("OpaLayout.cancel.retract");
                Trace.endSection();
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                Trace.beginSection("OpaLayout.end.retract");
                Trace.endSection();
                OpaLayout.this.mCurrentAnimators.clear();
                OpaLayout.this.skipToStartingValue();
            }
        });
        return arraySet;
    }

    private ArraySet<Animator> getCollapseAnimatorSet() {
        Animator animator;
        Animator animator2;
        Animator animator3;
        Animator animator4;
        ArraySet<Animator> arraySet = new ArraySet<>();
        if (this.mIsVertical) {
            animator = getPropertyAnimator(this.mRed, FrameLayout.TRANSLATION_Y, 0.0f, 133, OpaUtils.INTERPOLATOR_40_OUT);
        } else {
            animator = getPropertyAnimator(this.mRed, FrameLayout.TRANSLATION_X, 0.0f, 133, OpaUtils.INTERPOLATOR_40_OUT);
        }
        arraySet.add(animator);
        View view = this.mRed;
        Property<View, Float> property = FrameLayout.SCALE_X;
        Interpolator interpolator = OpaUtils.INTERPOLATOR_40_OUT;
        arraySet.add(getPropertyAnimator(view, property, 1.0f, 200, interpolator));
        arraySet.add(getPropertyAnimator(this.mRed, FrameLayout.SCALE_Y, 1.0f, 200, interpolator));
        if (this.mIsVertical) {
            animator2 = getPropertyAnimator(this.mBlue, FrameLayout.TRANSLATION_Y, 0.0f, 150, interpolator);
        } else {
            animator2 = getPropertyAnimator(this.mBlue, FrameLayout.TRANSLATION_X, 0.0f, 150, interpolator);
        }
        arraySet.add(animator2);
        arraySet.add(getPropertyAnimator(this.mBlue, FrameLayout.SCALE_X, 1.0f, 200, interpolator));
        arraySet.add(getPropertyAnimator(this.mBlue, FrameLayout.SCALE_Y, 1.0f, 200, interpolator));
        if (this.mIsVertical) {
            animator3 = getPropertyAnimator(this.mYellow, FrameLayout.TRANSLATION_Y, 0.0f, 133, interpolator);
        } else {
            animator3 = getPropertyAnimator(this.mYellow, FrameLayout.TRANSLATION_X, 0.0f, 133, interpolator);
        }
        arraySet.add(animator3);
        arraySet.add(getPropertyAnimator(this.mYellow, FrameLayout.SCALE_X, 1.0f, 200, interpolator));
        arraySet.add(getPropertyAnimator(this.mYellow, FrameLayout.SCALE_Y, 1.0f, 200, interpolator));
        if (this.mIsVertical) {
            animator4 = getPropertyAnimator(this.mGreen, FrameLayout.TRANSLATION_Y, 0.0f, 150, interpolator);
        } else {
            animator4 = getPropertyAnimator(this.mGreen, FrameLayout.TRANSLATION_X, 0.0f, 150, interpolator);
        }
        arraySet.add(animator4);
        arraySet.add(getPropertyAnimator(this.mGreen, FrameLayout.SCALE_X, 1.0f, 200, interpolator));
        arraySet.add(getPropertyAnimator(this.mGreen, FrameLayout.SCALE_Y, 1.0f, 200, interpolator));
        ImageView imageView = this.mWhite;
        Property<View, Float> property2 = FrameLayout.SCALE_X;
        Interpolator interpolator2 = Interpolators.FAST_OUT_SLOW_IN;
        Animator propertyAnimator = getPropertyAnimator(imageView, property2, 1.0f, 150, interpolator2);
        Animator propertyAnimator2 = getPropertyAnimator(this.mWhite, FrameLayout.SCALE_Y, 1.0f, 150, interpolator2);
        Animator propertyAnimator3 = getPropertyAnimator(this.mWhiteCutout, FrameLayout.SCALE_X, 1.0f, 150, interpolator2);
        Animator propertyAnimator4 = getPropertyAnimator(this.mWhiteCutout, FrameLayout.SCALE_Y, 1.0f, 150, interpolator2);
        Animator propertyAnimator5 = getPropertyAnimator(this.mHalo, FrameLayout.SCALE_X, 1.0f, 150, interpolator2);
        Animator propertyAnimator6 = getPropertyAnimator(this.mHalo, FrameLayout.SCALE_Y, 1.0f, 150, interpolator2);
        Animator propertyAnimator7 = getPropertyAnimator(this.mHalo, FrameLayout.ALPHA, 1.0f, 150, interpolator2);
        propertyAnimator.setStartDelay(33);
        propertyAnimator2.setStartDelay(33);
        propertyAnimator3.setStartDelay(33);
        propertyAnimator4.setStartDelay(33);
        propertyAnimator5.setStartDelay(33);
        propertyAnimator6.setStartDelay(33);
        propertyAnimator7.setStartDelay(33);
        arraySet.add(propertyAnimator);
        arraySet.add(propertyAnimator2);
        arraySet.add(propertyAnimator3);
        arraySet.add(propertyAnimator4);
        arraySet.add(propertyAnimator5);
        arraySet.add(propertyAnimator6);
        arraySet.add(propertyAnimator7);
        getLongestAnim(arraySet).addListener(new AnimatorListenerAdapter() { // from class: com.google.android.systemui.assist.OpaLayout.5
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animator5) {
                Trace.beginSection("OpaLayout.start.collapse");
                Trace.endSection();
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animator5) {
                Trace.beginSection("OpaLayout.cancel.collapse");
                Trace.endSection();
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator5) {
                Trace.beginSection("OpaLayout.end.collapse");
                Trace.endSection();
                OpaLayout.this.mCurrentAnimators.clear();
                OpaLayout.this.skipToStartingValue();
            }
        });
        return arraySet;
    }

    private ArraySet<Animator> getLineAnimatorSet() {
        ArraySet<Animator> arraySet = new ArraySet<>();
        if (this.mIsVertical) {
            View view = this.mRed;
            Property<View, Float> property = View.Y;
            float y = view.getY();
            Resources resources = this.mResources;
            int i = R$dimen.opa_line_x_trans_ry;
            float pxVal = y + OpaUtils.getPxVal(resources, i);
            Interpolator interpolator = Interpolators.FAST_OUT_SLOW_IN;
            arraySet.add(getPropertyAnimator(view, property, pxVal, 225, interpolator));
            View view2 = this.mRed;
            Property<View, Float> property2 = View.X;
            float x = view2.getX();
            Resources resources2 = this.mResources;
            int i2 = R$dimen.opa_line_y_translation;
            arraySet.add(getPropertyAnimator(view2, property2, x + OpaUtils.getPxVal(resources2, i2), 133, interpolator));
            View view3 = this.mBlue;
            Property<View, Float> property3 = View.Y;
            float y2 = view3.getY();
            Resources resources3 = this.mResources;
            int i3 = R$dimen.opa_line_x_trans_bg;
            arraySet.add(getPropertyAnimator(view3, property3, y2 + OpaUtils.getPxVal(resources3, i3), 225, interpolator));
            View view4 = this.mYellow;
            arraySet.add(getPropertyAnimator(view4, View.Y, view4.getY() + (-OpaUtils.getPxVal(this.mResources, i)), 225, interpolator));
            View view5 = this.mYellow;
            arraySet.add(getPropertyAnimator(view5, View.X, view5.getX() + (-OpaUtils.getPxVal(this.mResources, i2)), 133, interpolator));
            View view6 = this.mGreen;
            arraySet.add(getPropertyAnimator(view6, View.Y, view6.getY() + (-OpaUtils.getPxVal(this.mResources, i3)), 225, interpolator));
        } else {
            View view7 = this.mRed;
            Property<View, Float> property4 = View.X;
            float x2 = view7.getX();
            Resources resources4 = this.mResources;
            int i4 = R$dimen.opa_line_x_trans_ry;
            float f = x2 + (-OpaUtils.getPxVal(resources4, i4));
            Interpolator interpolator2 = Interpolators.FAST_OUT_SLOW_IN;
            arraySet.add(getPropertyAnimator(view7, property4, f, 225, interpolator2));
            View view8 = this.mRed;
            Property<View, Float> property5 = View.Y;
            float y3 = view8.getY();
            Resources resources5 = this.mResources;
            int i5 = R$dimen.opa_line_y_translation;
            arraySet.add(getPropertyAnimator(view8, property5, y3 + OpaUtils.getPxVal(resources5, i5), 133, interpolator2));
            View view9 = this.mBlue;
            Property<View, Float> property6 = View.X;
            float x3 = view9.getX();
            Resources resources6 = this.mResources;
            int i6 = R$dimen.opa_line_x_trans_bg;
            arraySet.add(getPropertyAnimator(view9, property6, x3 + (-OpaUtils.getPxVal(resources6, i6)), 225, interpolator2));
            View view10 = this.mYellow;
            arraySet.add(getPropertyAnimator(view10, View.X, view10.getX() + OpaUtils.getPxVal(this.mResources, i4), 225, interpolator2));
            View view11 = this.mYellow;
            arraySet.add(getPropertyAnimator(view11, View.Y, view11.getY() + (-OpaUtils.getPxVal(this.mResources, i5)), 133, interpolator2));
            View view12 = this.mGreen;
            arraySet.add(getPropertyAnimator(view12, View.X, view12.getX() + OpaUtils.getPxVal(this.mResources, i6), 225, interpolator2));
        }
        arraySet.add(getPropertyAnimator(this.mWhite, FrameLayout.SCALE_X, 0.0f, 83, this.HOME_DISAPPEAR_INTERPOLATOR));
        arraySet.add(getPropertyAnimator(this.mWhite, FrameLayout.SCALE_Y, 0.0f, 83, this.HOME_DISAPPEAR_INTERPOLATOR));
        arraySet.add(getPropertyAnimator(this.mWhiteCutout, FrameLayout.SCALE_X, 0.0f, 83, this.HOME_DISAPPEAR_INTERPOLATOR));
        arraySet.add(getPropertyAnimator(this.mWhiteCutout, FrameLayout.SCALE_Y, 0.0f, 83, this.HOME_DISAPPEAR_INTERPOLATOR));
        arraySet.add(getPropertyAnimator(this.mHalo, FrameLayout.SCALE_X, 0.0f, 83, this.HOME_DISAPPEAR_INTERPOLATOR));
        arraySet.add(getPropertyAnimator(this.mHalo, FrameLayout.SCALE_Y, 0.0f, 83, this.HOME_DISAPPEAR_INTERPOLATOR));
        getLongestAnim(arraySet).addListener(new AnimatorListenerAdapter() { // from class: com.google.android.systemui.assist.OpaLayout.6
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animator) {
                Trace.beginSection("OpaLayout.start.line");
                Trace.endSection();
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animator) {
                Trace.beginSection("OpaLayout.cancel.line");
                Trace.endSection();
                OpaLayout.this.mCurrentAnimators.clear();
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                Trace.beginSection("OpaLayout.end.line");
                Trace.endSection();
                OpaLayout.this.startCollapseAnimation();
            }
        });
        return arraySet;
    }

    public boolean getOpaEnabled() {
        if (this.mOpaEnabledNeedsUpdate) {
            ((AssistManagerGoogle) Dependency.get(AssistManager.class)).dispatchOpaEnabledState();
            if (this.mOpaEnabledNeedsUpdate) {
                Log.w("OpaLayout", "mOpaEnabledNeedsUpdate not cleared by AssistManagerGoogle!");
            }
        }
        return this.mOpaEnabled;
    }

    public void setOpaEnabled(boolean z) {
        Log.i("OpaLayout", "Setting opa enabled to " + z);
        this.mOpaEnabled = z;
        this.mOpaEnabledNeedsUpdate = false;
        updateOpaLayout();
    }

    public void updateOpaLayout() {
        boolean shouldShowSwipeUpUI = this.mOverviewProxyService.shouldShowSwipeUpUI();
        boolean z = true;
        boolean z2 = this.mOpaEnabled && !shouldShowSwipeUpUI;
        this.mHalo.setVisibility(z2 ? 0 : 4);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.mHalo.getLayoutParams();
        if (z2 || shouldShowSwipeUpUI) {
            z = false;
        }
        int i = z ? this.mHomeDiameter : -1;
        layoutParams.width = i;
        layoutParams.height = i;
        this.mWhite.setLayoutParams(layoutParams);
        this.mWhiteCutout.setLayoutParams(layoutParams);
        ImageView.ScaleType scaleType = z ? ImageView.ScaleType.FIT_CENTER : ImageView.ScaleType.CENTER;
        this.mWhite.setScaleType(scaleType);
        this.mWhiteCutout.setScaleType(scaleType);
    }

    /* access modifiers changed from: private */
    public void cancelCurrentAnimation(String str) {
        Trace.beginSection("OpaLayout.cancelCurrentAnimation: reason=" + str);
        Trace.endSection();
        if (!this.mCurrentAnimators.isEmpty()) {
            for (int size = this.mCurrentAnimators.size() - 1; size >= 0; size--) {
                Animator valueAt = this.mCurrentAnimators.valueAt(size);
                valueAt.removeAllListeners();
                valueAt.cancel();
            }
            this.mCurrentAnimators.clear();
            this.mAnimationState = 0;
        }
        AnimatorSet animatorSet = this.mGestureAnimatorSet;
        if (animatorSet != null) {
            animatorSet.cancel();
            this.mGestureState = 0;
        }
    }

    private void endCurrentAnimation(String str) {
        Trace.beginSection("OpaLayout.endCurrentAnimation: reason=" + str);
        if (!this.mCurrentAnimators.isEmpty()) {
            for (int size = this.mCurrentAnimators.size() - 1; size >= 0; size--) {
                Animator valueAt = this.mCurrentAnimators.valueAt(size);
                valueAt.removeAllListeners();
                valueAt.end();
            }
            this.mCurrentAnimators.clear();
        }
        this.mAnimationState = 0;
    }

    private Animator getLongestAnim(ArraySet<Animator> arraySet) {
        long j = Long.MIN_VALUE;
        Animator animator = null;
        for (int size = arraySet.size() - 1; size >= 0; size--) {
            Animator valueAt = arraySet.valueAt(size);
            if (valueAt.getTotalDuration() > j) {
                j = valueAt.getTotalDuration();
                animator = valueAt;
            }
        }
        return animator;
    }

    private void setDotsVisible() {
        int size = this.mAnimatedViews.size();
        for (int i = 0; i < size; i++) {
            this.mAnimatedViews.get(i).setAlpha(1.0f);
        }
    }

    /* access modifiers changed from: private */
    public void skipToStartingValue() {
        int size = this.mAnimatedViews.size();
        for (int i = 0; i < size; i++) {
            View view = this.mAnimatedViews.get(i);
            view.setScaleY(1.0f);
            view.setScaleX(1.0f);
            view.setTranslationY(0.0f);
            view.setTranslationX(0.0f);
            view.setAlpha(0.0f);
        }
        this.mHalo.setAlpha(1.0f);
        this.mWhite.setAlpha(1.0f);
        this.mWhiteCutout.setAlpha(1.0f);
        this.mAnimationState = 0;
        this.mGestureState = 0;
    }

    @Override // com.android.systemui.navigationbar.buttons.ButtonInterface
    public void setVertical(boolean z) {
        AnimatorSet animatorSet;
        if (!(this.mIsVertical == z || (animatorSet = this.mGestureAnimatorSet) == null)) {
            animatorSet.cancel();
            this.mGestureAnimatorSet = null;
            skipToStartingValue();
        }
        this.mIsVertical = z;
        this.mHome.setVertical(z);
        if (this.mIsVertical) {
            this.mTop = this.mGreen;
            this.mBottom = this.mBlue;
            this.mRight = this.mYellow;
            this.mLeft = this.mRed;
            return;
        }
        this.mTop = this.mRed;
        this.mBottom = this.mYellow;
        this.mLeft = this.mBlue;
        this.mRight = this.mGreen;
    }

    @Override // com.android.systemui.navigationbar.buttons.ButtonInterface
    public void setDarkIntensity(float f) {
        if (this.mWhite.getDrawable() instanceof KeyButtonDrawable) {
            ((KeyButtonDrawable) this.mWhite.getDrawable()).setDarkIntensity(f);
        }
        ((KeyButtonDrawable) this.mHalo.getDrawable()).setDarkIntensity(f);
        this.mWhite.invalidate();
        this.mHalo.invalidate();
        this.mHome.setDarkIntensity(f);
    }

    @Override // com.android.systemui.navigationbar.buttons.ButtonInterface
    public void setDelayTouchFeedback(boolean z) {
        this.mHome.setDelayTouchFeedback(z);
        this.mDelayTouchFeedback = z;
    }

    @Override // com.google.android.systemui.elmyra.feedback.FeedbackEffect
    public void onRelease() {
        if (this.mAnimationState == 0 && this.mGestureState == 1) {
            AnimatorSet animatorSet = this.mGestureAnimatorSet;
            if (animatorSet != null) {
                animatorSet.cancel();
            }
            this.mGestureState = 0;
            startRetractAnimation();
        }
    }

    @Override // com.google.android.systemui.elmyra.feedback.FeedbackEffect
    public void onProgress(float f, int i) {
        if (this.mGestureState != 2 && allowAnimations()) {
            if (this.mAnimationState == 2) {
                endCurrentAnimation("progress=" + f);
            }
            if (this.mAnimationState == 0) {
                if (this.mGestureAnimatorSet == null) {
                    AnimatorSet gestureAnimatorSet = getGestureAnimatorSet();
                    this.mGestureAnimatorSet = gestureAnimatorSet;
                    this.mGestureAnimationSetDuration = gestureAnimatorSet.getTotalDuration();
                }
                this.mGestureAnimatorSet.setCurrentPlayTime((long) (((float) (this.mGestureAnimationSetDuration - 1)) * f));
                if (f == 0.0f) {
                    this.mGestureState = 0;
                } else {
                    this.mGestureState = 1;
                }
            }
        }
    }

    @Override // com.google.android.systemui.elmyra.feedback.FeedbackEffect
    public void onResolve(GestureSensor.DetectionProperties detectionProperties) {
        AnimatorSet animatorSet;
        if (this.mAnimationState == 0) {
            if (this.mGestureState != 1 || (animatorSet = this.mGestureAnimatorSet) == null || animatorSet.isStarted()) {
                skipToStartingValue();
                return;
            }
            this.mGestureAnimatorSet.start();
            this.mGestureState = 2;
        }
    }

    private AnimatorSet getGestureAnimatorSet() {
        AnimatorSet animatorSet = this.mGestureLineSet;
        if (animatorSet != null) {
            animatorSet.removeAllListeners();
            this.mGestureLineSet.cancel();
            return this.mGestureLineSet;
        }
        this.mGestureLineSet = new AnimatorSet();
        ImageView imageView = this.mWhite;
        Interpolator interpolator = OpaUtils.INTERPOLATOR_40_OUT;
        ObjectAnimator scaleObjectAnimator = OpaUtils.getScaleObjectAnimator(imageView, 0.0f, 100, interpolator);
        ObjectAnimator scaleObjectAnimator2 = OpaUtils.getScaleObjectAnimator(this.mWhiteCutout, 0.0f, 100, interpolator);
        ObjectAnimator scaleObjectAnimator3 = OpaUtils.getScaleObjectAnimator(this.mHalo, 0.0f, 100, interpolator);
        scaleObjectAnimator.setStartDelay(50);
        scaleObjectAnimator2.setStartDelay(50);
        this.mGestureLineSet.play(scaleObjectAnimator).with(scaleObjectAnimator2).with(scaleObjectAnimator3);
        View view = this.mTop;
        Interpolator interpolator2 = Interpolators.FAST_OUT_SLOW_IN;
        AnimatorSet.Builder with = this.mGestureLineSet.play(OpaUtils.getScaleObjectAnimator(view, 0.8f, 200, interpolator2)).with(scaleObjectAnimator);
        View view2 = this.mRed;
        Interpolator interpolator3 = Interpolators.LINEAR;
        with.with(OpaUtils.getAlphaObjectAnimator(view2, 1.0f, 50, 130, interpolator3)).with(OpaUtils.getAlphaObjectAnimator(this.mYellow, 1.0f, 50, 130, interpolator3)).with(OpaUtils.getAlphaObjectAnimator(this.mBlue, 1.0f, 50, 113, interpolator3)).with(OpaUtils.getAlphaObjectAnimator(this.mGreen, 1.0f, 50, 113, interpolator3)).with(OpaUtils.getScaleObjectAnimator(this.mBottom, 0.8f, 200, interpolator2)).with(OpaUtils.getScaleObjectAnimator(this.mLeft, 0.8f, 200, interpolator2)).with(OpaUtils.getScaleObjectAnimator(this.mRight, 0.8f, 200, interpolator2));
        if (this.mIsVertical) {
            View view3 = this.mRed;
            Interpolator interpolator4 = OpaUtils.INTERPOLATOR_40_40;
            Resources resources = this.mResources;
            int i = R$dimen.opa_line_x_trans_ry;
            ObjectAnimator translationObjectAnimatorY = OpaUtils.getTranslationObjectAnimatorY(view3, interpolator4, OpaUtils.getPxVal(resources, i), this.mRed.getY() + OpaUtils.getDeltaDiamondPositionLeftY(), 350);
            translationObjectAnimatorY.addListener(new AnimatorListenerAdapter() { // from class: com.google.android.systemui.assist.OpaLayout.7
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    OpaLayout.this.startCollapseAnimation();
                }
            });
            AnimatorSet.Builder with2 = this.mGestureLineSet.play(translationObjectAnimatorY).with(scaleObjectAnimator3);
            View view4 = this.mBlue;
            Resources resources2 = this.mResources;
            int i2 = R$dimen.opa_line_x_trans_bg;
            with2.with(OpaUtils.getTranslationObjectAnimatorY(view4, interpolator4, OpaUtils.getPxVal(resources2, i2), this.mBlue.getY() + OpaUtils.getDeltaDiamondPositionBottomY(this.mResources), 350)).with(OpaUtils.getTranslationObjectAnimatorY(this.mYellow, interpolator4, -OpaUtils.getPxVal(this.mResources, i), this.mYellow.getY() + OpaUtils.getDeltaDiamondPositionRightY(), 350)).with(OpaUtils.getTranslationObjectAnimatorY(this.mGreen, interpolator4, -OpaUtils.getPxVal(this.mResources, i2), this.mGreen.getY() + OpaUtils.getDeltaDiamondPositionTopY(this.mResources), 350));
        } else {
            View view5 = this.mRed;
            Interpolator interpolator5 = OpaUtils.INTERPOLATOR_40_40;
            Resources resources3 = this.mResources;
            int i3 = R$dimen.opa_line_x_trans_ry;
            ObjectAnimator translationObjectAnimatorX = OpaUtils.getTranslationObjectAnimatorX(view5, interpolator5, -OpaUtils.getPxVal(resources3, i3), this.mRed.getX() + OpaUtils.getDeltaDiamondPositionTopX(), 350);
            translationObjectAnimatorX.addListener(new AnimatorListenerAdapter() { // from class: com.google.android.systemui.assist.OpaLayout.8
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    OpaLayout.this.startCollapseAnimation();
                }
            });
            AnimatorSet.Builder with3 = this.mGestureLineSet.play(translationObjectAnimatorX).with(scaleObjectAnimator);
            View view6 = this.mBlue;
            Resources resources4 = this.mResources;
            int i4 = R$dimen.opa_line_x_trans_bg;
            with3.with(OpaUtils.getTranslationObjectAnimatorX(view6, interpolator5, -OpaUtils.getPxVal(resources4, i4), this.mBlue.getX() + OpaUtils.getDeltaDiamondPositionLeftX(this.mResources), 350)).with(OpaUtils.getTranslationObjectAnimatorX(this.mYellow, interpolator5, OpaUtils.getPxVal(this.mResources, i3), this.mYellow.getX() + OpaUtils.getDeltaDiamondPositionBottomX(), 350)).with(OpaUtils.getTranslationObjectAnimatorX(this.mGreen, interpolator5, OpaUtils.getPxVal(this.mResources, i4), this.mGreen.getX() + OpaUtils.getDeltaDiamondPositionRightX(this.mResources), 350));
        }
        return this.mGestureLineSet;
    }

    private Animator getPropertyAnimator(View view, Property<View, Float> property, float f, int i, Interpolator interpolator) {
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(view, property, f);
        ofFloat.setDuration((long) i);
        ofFloat.setInterpolator(interpolator);
        return ofFloat;
    }
}
