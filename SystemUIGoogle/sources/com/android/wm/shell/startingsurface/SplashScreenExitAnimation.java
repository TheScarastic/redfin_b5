package com.android.wm.shell.startingsurface;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.BlendMode;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.Shader;
import android.util.MathUtils;
import android.view.Choreographer;
import android.view.SurfaceControl;
import android.view.SyncRtSurfaceTransactionApplier;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.PathInterpolator;
import android.window.SplashScreenView;
import com.android.wm.shell.R;
import com.android.wm.shell.animation.Interpolators;
import com.android.wm.shell.common.TransactionPool;
import java.util.Objects;
/* loaded from: classes2.dex */
public class SplashScreenExitAnimation implements Animator.AnimatorListener {
    private final int mAnimationDuration;
    private final int mAppRevealDelay;
    private final int mAppRevealDuration;
    private Runnable mFinishCallback;
    private final Rect mFirstWindowFrame;
    private final SurfaceControl mFirstWindowSurface;
    private final int mIconFadeOutDuration;
    private final float mIconStartAlpha;
    private ValueAnimator mMainAnimator;
    private final int mMainWindowShiftLength;
    private RadialVanishAnimation mRadialVanishAnimation;
    private ShiftUpAnimation mShiftUpAnimation;
    private final SplashScreenView mSplashScreenView;
    private final TransactionPool mTransactionPool;
    private static final String TAG = StartingSurfaceDrawer.TAG;
    private static final Interpolator ICON_INTERPOLATOR = new PathInterpolator(0.15f, 0.0f, 1.0f, 1.0f);
    private static final Interpolator MASK_RADIUS_INTERPOLATOR = new PathInterpolator(0.0f, 0.0f, 0.4f, 1.0f);
    private static final Interpolator SHIFT_UP_INTERPOLATOR = new PathInterpolator(0.0f, 0.0f, 0.0f, 1.0f);

    @Override // android.animation.Animator.AnimatorListener
    public void onAnimationRepeat(Animator animator) {
    }

    @Override // android.animation.Animator.AnimatorListener
    public void onAnimationStart(Animator animator) {
    }

    public SplashScreenExitAnimation(Context context, SplashScreenView splashScreenView, SurfaceControl surfaceControl, Rect rect, int i, TransactionPool transactionPool, Runnable runnable) {
        Rect rect2 = new Rect();
        this.mFirstWindowFrame = rect2;
        this.mSplashScreenView = splashScreenView;
        this.mFirstWindowSurface = surfaceControl;
        if (rect != null) {
            rect2.set(rect);
        }
        View iconView = splashScreenView.getIconView();
        if (iconView == null || iconView.getLayoutParams().width == 0 || iconView.getLayoutParams().height == 0) {
            this.mIconFadeOutDuration = 0;
            this.mIconStartAlpha = 0.0f;
            this.mAppRevealDelay = 0;
        } else {
            iconView.setLayerType(2, null);
            this.mIconFadeOutDuration = context.getResources().getInteger(R.integer.starting_window_app_reveal_icon_fade_out_duration);
            this.mAppRevealDelay = context.getResources().getInteger(R.integer.starting_window_app_reveal_anim_delay);
            this.mIconStartAlpha = iconView.getAlpha();
        }
        int integer = context.getResources().getInteger(R.integer.starting_window_app_reveal_anim_duration);
        this.mAppRevealDuration = integer;
        this.mAnimationDuration = Math.max(this.mIconFadeOutDuration, this.mAppRevealDelay + integer);
        this.mMainWindowShiftLength = i;
        this.mFinishCallback = runnable;
        this.mTransactionPool = transactionPool;
    }

    public void startAnimations() {
        ValueAnimator createAnimator = createAnimator();
        this.mMainAnimator = createAnimator;
        createAnimator.start();
    }

    private ValueAnimator createAnimator() {
        int height = this.mSplashScreenView.getHeight() - 0;
        int width = this.mSplashScreenView.getWidth() / 2;
        int sqrt = (int) (((double) (((float) ((int) Math.sqrt((double) ((height * height) + (width * width))))) * 1.25f)) + 0.5d);
        RadialVanishAnimation radialVanishAnimation = new RadialVanishAnimation(this.mSplashScreenView);
        this.mRadialVanishAnimation = radialVanishAnimation;
        radialVanishAnimation.setCircleCenter(width, 0);
        this.mRadialVanishAnimation.setRadius(0, sqrt);
        this.mRadialVanishAnimation.setRadialPaintParam(new int[]{-1, -1, 0}, new float[]{0.0f, 0.8f, 1.0f});
        SurfaceControl surfaceControl = this.mFirstWindowSurface;
        if (surfaceControl != null && surfaceControl.isValid()) {
            View view = new View(this.mSplashScreenView.getContext());
            view.setBackgroundColor(this.mSplashScreenView.getInitBackgroundColor());
            this.mSplashScreenView.addView(view, new ViewGroup.LayoutParams(-1, this.mMainWindowShiftLength));
            this.mShiftUpAnimation = new ShiftUpAnimation(0.0f, (float) (-this.mMainWindowShiftLength), view);
        }
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        ofFloat.setDuration((long) this.mAnimationDuration);
        ofFloat.setInterpolator(Interpolators.LINEAR);
        ofFloat.addListener(this);
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.wm.shell.startingsurface.SplashScreenExitAnimation$$ExternalSyntheticLambda0
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                SplashScreenExitAnimation.m592$r8$lambda$YqWrEpxbkBdttM7zPTdpWQValo(SplashScreenExitAnimation.this, valueAnimator);
            }
        });
        return ofFloat;
    }

    public /* synthetic */ void lambda$createAnimator$0(ValueAnimator valueAnimator) {
        onAnimationProgress(((Float) valueAnimator.getAnimatedValue()).floatValue());
    }

    /* loaded from: classes2.dex */
    public static class RadialVanishAnimation extends View {
        private int mFinishRadius;
        private int mInitRadius;
        private final Paint mVanishPaint;
        private final SplashScreenView mView;
        private final Point mCircleCenter = new Point();
        private final Matrix mVanishMatrix = new Matrix();

        RadialVanishAnimation(SplashScreenView splashScreenView) {
            super(splashScreenView.getContext());
            Paint paint = new Paint(1);
            this.mVanishPaint = paint;
            this.mView = splashScreenView;
            splashScreenView.addView(this);
            paint.setAlpha(0);
        }

        void onAnimationProgress(float f) {
            if (this.mVanishPaint.getShader() != null) {
                float interpolation = SplashScreenExitAnimation.MASK_RADIUS_INTERPOLATOR.getInterpolation(f);
                float interpolation2 = Interpolators.ALPHA_OUT.getInterpolation(f);
                int i = this.mInitRadius;
                float f2 = ((float) i) + (((float) (this.mFinishRadius - i)) * interpolation);
                this.mVanishMatrix.setScale(f2, f2);
                Matrix matrix = this.mVanishMatrix;
                Point point = this.mCircleCenter;
                matrix.postTranslate((float) point.x, (float) point.y);
                this.mVanishPaint.getShader().setLocalMatrix(this.mVanishMatrix);
                this.mVanishPaint.setAlpha(Math.round(interpolation2 * 255.0f));
                postInvalidate();
            }
        }

        void setRadius(int i, int i2) {
            this.mInitRadius = i;
            this.mFinishRadius = i2;
        }

        void setCircleCenter(int i, int i2) {
            this.mCircleCenter.set(i, i2);
        }

        void setRadialPaintParam(int[] iArr, float[] fArr) {
            this.mVanishPaint.setShader(new RadialGradient(0.0f, 0.0f, 1.0f, iArr, fArr, Shader.TileMode.CLAMP));
            this.mVanishPaint.setBlendMode(BlendMode.DST_OUT);
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            canvas.drawRect(0.0f, 0.0f, (float) this.mView.getWidth(), (float) this.mView.getHeight(), this.mVanishPaint);
        }
    }

    /* loaded from: classes2.dex */
    public final class ShiftUpAnimation {
        private final SyncRtSurfaceTransactionApplier mApplier;
        private final float mFromYDelta;
        private final View mOccludeHoleView;
        private final Matrix mTmpTransform = new Matrix();
        private final float mToYDelta;

        ShiftUpAnimation(float f, float f2, View view) {
            SplashScreenExitAnimation.this = r1;
            this.mFromYDelta = f;
            this.mToYDelta = f2;
            this.mOccludeHoleView = view;
            this.mApplier = new SyncRtSurfaceTransactionApplier(view);
        }

        void onAnimationProgress(float f) {
            if (SplashScreenExitAnimation.this.mFirstWindowSurface != null && SplashScreenExitAnimation.this.mFirstWindowSurface.isValid() && SplashScreenExitAnimation.this.mSplashScreenView.isAttachedToWindow()) {
                float interpolation = SplashScreenExitAnimation.SHIFT_UP_INTERPOLATOR.getInterpolation(f);
                float f2 = this.mFromYDelta;
                float f3 = f2 + ((this.mToYDelta - f2) * interpolation);
                this.mOccludeHoleView.setTranslationY(f3);
                this.mTmpTransform.setTranslate(0.0f, f3);
                SurfaceControl.Transaction acquire = SplashScreenExitAnimation.this.mTransactionPool.acquire();
                acquire.setFrameTimelineVsync(Choreographer.getSfInstance().getVsyncId());
                this.mTmpTransform.postTranslate((float) SplashScreenExitAnimation.this.mFirstWindowFrame.left, (float) (SplashScreenExitAnimation.this.mFirstWindowFrame.top + SplashScreenExitAnimation.this.mMainWindowShiftLength));
                this.mApplier.scheduleApply(new SyncRtSurfaceTransactionApplier.SurfaceParams[]{new SyncRtSurfaceTransactionApplier.SurfaceParams.Builder(SplashScreenExitAnimation.this.mFirstWindowSurface).withMatrix(this.mTmpTransform).withMergeTransaction(acquire).build()});
                SplashScreenExitAnimation.this.mTransactionPool.release(acquire);
            }
        }

        void finish() {
            if (SplashScreenExitAnimation.this.mFirstWindowSurface != null && SplashScreenExitAnimation.this.mFirstWindowSurface.isValid()) {
                SurfaceControl.Transaction acquire = SplashScreenExitAnimation.this.mTransactionPool.acquire();
                if (SplashScreenExitAnimation.this.mSplashScreenView.isAttachedToWindow()) {
                    acquire.setFrameTimelineVsync(Choreographer.getSfInstance().getVsyncId());
                    this.mApplier.scheduleApply(new SyncRtSurfaceTransactionApplier.SurfaceParams[]{new SyncRtSurfaceTransactionApplier.SurfaceParams.Builder(SplashScreenExitAnimation.this.mFirstWindowSurface).withWindowCrop((Rect) null).withMergeTransaction(acquire).build()});
                } else {
                    acquire.setWindowCrop(SplashScreenExitAnimation.this.mFirstWindowSurface, null);
                    acquire.apply();
                }
                SplashScreenExitAnimation.this.mTransactionPool.release(acquire);
                Choreographer sfInstance = Choreographer.getSfInstance();
                SurfaceControl surfaceControl = SplashScreenExitAnimation.this.mFirstWindowSurface;
                Objects.requireNonNull(surfaceControl);
                sfInstance.postCallback(4, new SplashScreenExitAnimation$ShiftUpAnimation$$ExternalSyntheticLambda0(surfaceControl), null);
            }
        }
    }

    private void reset() {
        if (this.mSplashScreenView.isAttachedToWindow()) {
            this.mSplashScreenView.setVisibility(8);
            Runnable runnable = this.mFinishCallback;
            if (runnable != null) {
                runnable.run();
                this.mFinishCallback = null;
            }
        }
        ShiftUpAnimation shiftUpAnimation = this.mShiftUpAnimation;
        if (shiftUpAnimation != null) {
            shiftUpAnimation.finish();
        }
    }

    @Override // android.animation.Animator.AnimatorListener
    public void onAnimationEnd(Animator animator) {
        reset();
    }

    @Override // android.animation.Animator.AnimatorListener
    public void onAnimationCancel(Animator animator) {
        reset();
    }

    private void onAnimationProgress(float f) {
        View iconView = this.mSplashScreenView.getIconView();
        if (iconView != null) {
            iconView.setAlpha(this.mIconStartAlpha * (1.0f - ICON_INTERPOLATOR.getInterpolation(getProgress(f, 0, (long) this.mIconFadeOutDuration))));
        }
        float progress = getProgress(f, (long) this.mAppRevealDelay, (long) this.mAppRevealDuration);
        RadialVanishAnimation radialVanishAnimation = this.mRadialVanishAnimation;
        if (radialVanishAnimation != null) {
            radialVanishAnimation.onAnimationProgress(progress);
        }
        ShiftUpAnimation shiftUpAnimation = this.mShiftUpAnimation;
        if (shiftUpAnimation != null) {
            shiftUpAnimation.onAnimationProgress(progress);
        }
    }

    private float getProgress(float f, long j, long j2) {
        return MathUtils.constrain(((f * ((float) this.mAnimationDuration)) - ((float) j)) / ((float) j2), 0.0f, 1.0f);
    }
}
