package com.android.systemui.assist;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Interpolator;
import com.android.systemui.R$dimen;
import com.android.systemui.animation.Interpolators;
/* loaded from: classes.dex */
public class AssistDisclosure {
    private final Context mContext;
    private final Handler mHandler;
    private Runnable mShowRunnable = new Runnable() { // from class: com.android.systemui.assist.AssistDisclosure.1
        @Override // java.lang.Runnable
        public void run() {
            AssistDisclosure.this.show();
        }
    };
    private AssistDisclosureView mView;
    private boolean mViewAdded;
    private final WindowManager mWm;

    public AssistDisclosure(Context context, Handler handler) {
        this.mContext = context;
        this.mHandler = handler;
        this.mWm = (WindowManager) context.getSystemService(WindowManager.class);
    }

    public void postShow() {
        this.mHandler.removeCallbacks(this.mShowRunnable);
        this.mHandler.post(this.mShowRunnable);
    }

    /* access modifiers changed from: private */
    public void show() {
        if (this.mView == null) {
            this.mView = new AssistDisclosureView(this.mContext);
        }
        if (!this.mViewAdded) {
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(2015, 525576, -3);
            layoutParams.setTitle("AssistDisclosure");
            layoutParams.setFitInsetsTypes(0);
            this.mWm.addView(this.mView, layoutParams);
            this.mViewAdded = true;
        }
    }

    /* access modifiers changed from: private */
    public void hide() {
        if (this.mViewAdded) {
            this.mWm.removeView(this.mView);
            this.mViewAdded = false;
        }
    }

    /* access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class AssistDisclosureView extends View implements ValueAnimator.AnimatorUpdateListener {
        private final ValueAnimator mAlphaInAnimator;
        private final ValueAnimator mAlphaOutAnimator;
        private final AnimatorSet mAnimator;
        private final Paint mPaint;
        private final Paint mShadowPaint;
        private int mAlpha = 0;
        private float mThickness = getResources().getDimension(R$dimen.assist_disclosure_thickness);
        private float mShadowThickness = getResources().getDimension(R$dimen.assist_disclosure_shadow_thickness);

        public AssistDisclosureView(Context context) {
            super(context);
            Paint paint = new Paint();
            this.mPaint = paint;
            Paint paint2 = new Paint();
            this.mShadowPaint = paint2;
            ValueAnimator duration = ValueAnimator.ofInt(0, 222).setDuration(400L);
            this.mAlphaInAnimator = duration;
            duration.addUpdateListener(this);
            Interpolator interpolator = Interpolators.CUSTOM_40_40;
            duration.setInterpolator(interpolator);
            ValueAnimator duration2 = ValueAnimator.ofInt(222, 0).setDuration(300L);
            this.mAlphaOutAnimator = duration2;
            duration2.addUpdateListener(this);
            duration2.setInterpolator(interpolator);
            AnimatorSet animatorSet = new AnimatorSet();
            this.mAnimator = animatorSet;
            animatorSet.play(duration).before(duration2);
            animatorSet.addListener(new AnimatorListenerAdapter(AssistDisclosure.this) { // from class: com.android.systemui.assist.AssistDisclosure.AssistDisclosureView.1
                boolean mCancelled;

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationStart(Animator animator) {
                    this.mCancelled = false;
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationCancel(Animator animator) {
                    this.mCancelled = true;
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    if (!this.mCancelled) {
                        AssistDisclosure.this.hide();
                    }
                }
            });
            PorterDuffXfermode porterDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC);
            paint.setColor(-1);
            paint.setXfermode(porterDuffXfermode);
            paint2.setColor(-12303292);
            paint2.setXfermode(porterDuffXfermode);
        }

        @Override // android.view.View
        protected void onAttachedToWindow() {
            super.onAttachedToWindow();
            startAnimation();
            sendAccessibilityEvent(16777216);
        }

        @Override // android.view.View
        protected void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            this.mAnimator.cancel();
            this.mAlpha = 0;
        }

        private void startAnimation() {
            this.mAnimator.cancel();
            this.mAnimator.start();
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            this.mPaint.setAlpha(this.mAlpha);
            this.mShadowPaint.setAlpha(this.mAlpha / 4);
            drawGeometry(canvas, this.mShadowPaint, this.mShadowThickness);
            drawGeometry(canvas, this.mPaint, 0.0f);
        }

        private void drawGeometry(Canvas canvas, Paint paint, float f) {
            int width = getWidth();
            int height = getHeight();
            float f2 = this.mThickness;
            float f3 = (float) height;
            float f4 = f3 - f2;
            float f5 = (float) width;
            drawBeam(canvas, 0.0f, f4, f5, f3, paint, f);
            drawBeam(canvas, 0.0f, 0.0f, f2, f4, paint, f);
            float f6 = f5 - f2;
            drawBeam(canvas, f6, 0.0f, f5, f4, paint, f);
            drawBeam(canvas, f2, 0.0f, f6, f2, paint, f);
        }

        private void drawBeam(Canvas canvas, float f, float f2, float f3, float f4, Paint paint, float f5) {
            canvas.drawRect(f - f5, f2 - f5, f3 + f5, f4 + f5, paint);
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            ValueAnimator valueAnimator2 = this.mAlphaOutAnimator;
            if (valueAnimator == valueAnimator2) {
                this.mAlpha = ((Integer) valueAnimator2.getAnimatedValue()).intValue();
            } else {
                ValueAnimator valueAnimator3 = this.mAlphaInAnimator;
                if (valueAnimator == valueAnimator3) {
                    this.mAlpha = ((Integer) valueAnimator3.getAnimatedValue()).intValue();
                }
            }
            invalidate();
        }
    }
}
