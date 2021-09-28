package com.android.wm.shell.startingsurface;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.AdaptiveIconDrawable;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Trace;
import android.util.PathParser;
import android.window.SplashScreenView;
/* loaded from: classes2.dex */
public class SplashscreenIconDrawableFactory {
    public static Drawable[] makeIconDrawable(int i, int i2, Drawable drawable, int i3, int i4, Handler handler) {
        Drawable drawable2;
        boolean z = (i == 0 || i == i2) ? false : true;
        if (drawable instanceof Animatable) {
            drawable2 = new AnimatableIconAnimateListener(drawable);
        } else if (drawable instanceof AdaptiveIconDrawable) {
            drawable2 = new ImmobileIconDrawable(drawable, i3, i4, handler);
            z = false;
        } else {
            drawable2 = new ImmobileIconDrawable(new AdaptiveForegroundDrawable(drawable), i3, i4, handler);
        }
        return new Drawable[]{drawable2, z ? new MaskBackgroundDrawable(i) : null};
    }

    public static Drawable[] makeLegacyIconDrawable(Drawable drawable, int i, int i2, Handler handler) {
        return new Drawable[]{new ImmobileIconDrawable(drawable, i, i2, handler)};
    }

    /* loaded from: classes2.dex */
    public static class ImmobileIconDrawable extends Drawable {
        private Bitmap mIconBitmap;
        private final Matrix mMatrix;
        private final Paint mPaint = new Paint(7);

        @Override // android.graphics.drawable.Drawable
        public int getOpacity() {
            return 1;
        }

        @Override // android.graphics.drawable.Drawable
        public void setAlpha(int i) {
        }

        @Override // android.graphics.drawable.Drawable
        public void setColorFilter(ColorFilter colorFilter) {
        }

        ImmobileIconDrawable(Drawable drawable, int i, int i2, Handler handler) {
            Matrix matrix = new Matrix();
            this.mMatrix = matrix;
            float f = ((float) i2) / ((float) i);
            matrix.setScale(f, f);
            handler.post(new SplashscreenIconDrawableFactory$ImmobileIconDrawable$$ExternalSyntheticLambda0(this, drawable, i));
        }

        /* renamed from: preDrawIcon */
        public void lambda$new$0(Drawable drawable, int i) {
            synchronized (this.mPaint) {
                Trace.traceBegin(32, "preDrawIcon");
                this.mIconBitmap = Bitmap.createBitmap(i, i, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(this.mIconBitmap);
                drawable.setBounds(0, 0, i, i);
                drawable.draw(canvas);
                Trace.traceEnd(32);
            }
        }

        @Override // android.graphics.drawable.Drawable
        public void draw(Canvas canvas) {
            synchronized (this.mPaint) {
                Bitmap bitmap = this.mIconBitmap;
                if (bitmap != null) {
                    canvas.drawBitmap(bitmap, this.mMatrix, this.mPaint);
                } else {
                    invalidateSelf();
                }
            }
        }
    }

    /* loaded from: classes2.dex */
    public static class MaskBackgroundDrawable extends Drawable {
        private static Path sMask;
        private final Paint mBackgroundPaint;
        private final Path mMaskScaleOnly = new Path(new Path(sMask));
        private final Matrix mMaskMatrix = new Matrix();

        @Override // android.graphics.drawable.Drawable
        public int getOpacity() {
            return 1;
        }

        @Override // android.graphics.drawable.Drawable
        public void setColorFilter(ColorFilter colorFilter) {
        }

        public MaskBackgroundDrawable(int i) {
            sMask = PathParser.createPathFromPathData(Resources.getSystem().getString(17039948));
            if (i != 0) {
                Paint paint = new Paint(7);
                this.mBackgroundPaint = paint;
                paint.setColor(i);
                paint.setStyle(Paint.Style.FILL);
                return;
            }
            this.mBackgroundPaint = null;
        }

        @Override // android.graphics.drawable.Drawable
        protected void onBoundsChange(Rect rect) {
            if (!rect.isEmpty()) {
                updateLayerBounds(rect);
            }
        }

        protected void updateLayerBounds(Rect rect) {
            this.mMaskMatrix.setScale(((float) rect.width()) / 100.0f, ((float) rect.height()) / 100.0f);
            sMask.transform(this.mMaskMatrix, this.mMaskScaleOnly);
        }

        @Override // android.graphics.drawable.Drawable
        public void draw(Canvas canvas) {
            canvas.clipPath(this.mMaskScaleOnly);
            Paint paint = this.mBackgroundPaint;
            if (paint != null) {
                canvas.drawPath(this.mMaskScaleOnly, paint);
            }
        }

        @Override // android.graphics.drawable.Drawable
        public void setAlpha(int i) {
            Paint paint = this.mBackgroundPaint;
            if (paint != null) {
                paint.setAlpha(i);
            }
        }
    }

    /* loaded from: classes2.dex */
    public static class AdaptiveForegroundDrawable extends MaskBackgroundDrawable {
        protected final Drawable mForegroundDrawable;
        private final Rect mTmpOutRect = new Rect();

        AdaptiveForegroundDrawable(Drawable drawable) {
            super(0);
            this.mForegroundDrawable = drawable;
        }

        @Override // com.android.wm.shell.startingsurface.SplashscreenIconDrawableFactory.MaskBackgroundDrawable
        protected void updateLayerBounds(Rect rect) {
            super.updateLayerBounds(rect);
            int width = rect.width() / 2;
            int height = rect.height() / 2;
            int width2 = (int) (((float) rect.width()) / 1.3333334f);
            int height2 = (int) (((float) rect.height()) / 1.3333334f);
            Rect rect2 = this.mTmpOutRect;
            rect2.set(width - width2, height - height2, width + width2, height + height2);
            Drawable drawable = this.mForegroundDrawable;
            if (drawable != null) {
                drawable.setBounds(rect2);
            }
            invalidateSelf();
        }

        @Override // com.android.wm.shell.startingsurface.SplashscreenIconDrawableFactory.MaskBackgroundDrawable, android.graphics.drawable.Drawable
        public void draw(Canvas canvas) {
            super.draw(canvas);
            this.mForegroundDrawable.draw(canvas);
        }

        @Override // com.android.wm.shell.startingsurface.SplashscreenIconDrawableFactory.MaskBackgroundDrawable, android.graphics.drawable.Drawable
        public void setColorFilter(ColorFilter colorFilter) {
            this.mForegroundDrawable.setColorFilter(colorFilter);
        }
    }

    /* loaded from: classes2.dex */
    public static class AnimatableIconAnimateListener extends AdaptiveForegroundDrawable implements SplashScreenView.IconAnimateListener {
        private Animatable mAnimatableIcon;
        private boolean mAnimationTriggered;
        private final Drawable.Callback mCallback;
        private Animator mIconAnimator;

        AnimatableIconAnimateListener(Drawable drawable) {
            super(drawable);
            AnonymousClass2 r1 = new Drawable.Callback() { // from class: com.android.wm.shell.startingsurface.SplashscreenIconDrawableFactory.AnimatableIconAnimateListener.2
                @Override // android.graphics.drawable.Drawable.Callback
                public void invalidateDrawable(Drawable drawable2) {
                    AnimatableIconAnimateListener.this.invalidateSelf();
                }

                @Override // android.graphics.drawable.Drawable.Callback
                public void scheduleDrawable(Drawable drawable2, Runnable runnable, long j) {
                    AnimatableIconAnimateListener.this.scheduleSelf(runnable, j);
                }

                @Override // android.graphics.drawable.Drawable.Callback
                public void unscheduleDrawable(Drawable drawable2, Runnable runnable) {
                    AnimatableIconAnimateListener.this.unscheduleSelf(runnable);
                }
            };
            this.mCallback = r1;
            this.mForegroundDrawable.setCallback(r1);
        }

        public boolean prepareAnimate(long j, final Runnable runnable) {
            this.mAnimatableIcon = (Animatable) this.mForegroundDrawable;
            ValueAnimator ofInt = ValueAnimator.ofInt(0, 1);
            this.mIconAnimator = ofInt;
            ofInt.setDuration(j);
            this.mIconAnimator.addListener(new Animator.AnimatorListener() { // from class: com.android.wm.shell.startingsurface.SplashscreenIconDrawableFactory.AnimatableIconAnimateListener.1
                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationStart(Animator animator) {
                    Runnable runnable2 = runnable;
                    if (runnable2 != null) {
                        runnable2.run();
                    }
                    AnimatableIconAnimateListener.this.mAnimatableIcon.start();
                }

                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    AnimatableIconAnimateListener.this.mAnimatableIcon.stop();
                }

                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationCancel(Animator animator) {
                    AnimatableIconAnimateListener.this.mAnimatableIcon.stop();
                }

                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationRepeat(Animator animator) {
                    AnimatableIconAnimateListener.this.mAnimatableIcon.stop();
                }
            });
            return true;
        }

        private void ensureAnimationStarted() {
            if (!this.mAnimationTriggered) {
                Animator animator = this.mIconAnimator;
                if (animator != null && !animator.isRunning()) {
                    this.mIconAnimator.start();
                }
                this.mAnimationTriggered = true;
            }
        }

        @Override // com.android.wm.shell.startingsurface.SplashscreenIconDrawableFactory.AdaptiveForegroundDrawable, com.android.wm.shell.startingsurface.SplashscreenIconDrawableFactory.MaskBackgroundDrawable, android.graphics.drawable.Drawable
        public void draw(Canvas canvas) {
            ensureAnimationStarted();
            super.draw(canvas);
        }
    }
}
