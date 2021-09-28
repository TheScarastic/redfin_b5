package com.bumptech.glide.load.resource.gif;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import com.bumptech.glide.load.resource.gif.GifFrameLoader;
import com.bumptech.glide.util.Preconditions;
/* loaded from: classes.dex */
public class GifDrawable extends Drawable implements GifFrameLoader.FrameCallback, Animatable {
    public boolean applyGravity;
    public Rect destRect;
    public boolean isRecycled;
    public boolean isRunning;
    public boolean isStarted;
    public int loopCount;
    public Paint paint;
    public final GifState state;
    public boolean isVisible = true;
    public int maxLoopCount = -1;

    /* loaded from: classes.dex */
    public static final class GifState extends Drawable.ConstantState {
        public final GifFrameLoader frameLoader;

        public GifState(GifFrameLoader gifFrameLoader) {
            this.frameLoader = gifFrameLoader;
        }

        @Override // android.graphics.drawable.Drawable.ConstantState
        public int getChangingConfigurations() {
            return 0;
        }

        @Override // android.graphics.drawable.Drawable.ConstantState
        public Drawable newDrawable() {
            return new GifDrawable(this);
        }

        @Override // android.graphics.drawable.Drawable.ConstantState
        public Drawable newDrawable(Resources resources) {
            return new GifDrawable(this);
        }
    }

    public GifDrawable(GifState gifState) {
        this.state = gifState;
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        if (!this.isRecycled) {
            if (this.applyGravity) {
                int intrinsicWidth = getIntrinsicWidth();
                int intrinsicHeight = getIntrinsicHeight();
                Rect bounds = getBounds();
                if (this.destRect == null) {
                    this.destRect = new Rect();
                }
                Gravity.apply(119, intrinsicWidth, intrinsicHeight, bounds, this.destRect);
                this.applyGravity = false;
            }
            Bitmap currentFrame = this.state.frameLoader.getCurrentFrame();
            if (this.destRect == null) {
                this.destRect = new Rect();
            }
            canvas.drawBitmap(currentFrame, (Rect) null, this.destRect, getPaint());
        }
    }

    @Override // android.graphics.drawable.Drawable
    public Drawable.ConstantState getConstantState() {
        return this.state;
    }

    public Bitmap getFirstFrame() {
        return this.state.frameLoader.firstFrame;
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicHeight() {
        return this.state.frameLoader.getCurrentFrame().getHeight();
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicWidth() {
        return this.state.frameLoader.getCurrentFrame().getWidth();
    }

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        return -2;
    }

    public final Paint getPaint() {
        if (this.paint == null) {
            this.paint = new Paint(2);
        }
        return this.paint;
    }

    @Override // android.graphics.drawable.Animatable
    public boolean isRunning() {
        return this.isRunning;
    }

    @Override // android.graphics.drawable.Drawable
    public void onBoundsChange(Rect rect) {
        super.onBoundsChange(rect);
        this.applyGravity = true;
    }

    @Override // com.bumptech.glide.load.resource.gif.GifFrameLoader.FrameCallback
    public void onFrameReady() {
        Drawable.Callback callback = getCallback();
        while (callback instanceof Drawable) {
            callback = ((Drawable) callback).getCallback();
        }
        if (callback == null) {
            stop();
            invalidateSelf();
            return;
        }
        invalidateSelf();
        GifFrameLoader gifFrameLoader = this.state.frameLoader;
        GifFrameLoader.DelayTarget delayTarget = gifFrameLoader.current;
        if ((delayTarget != null ? delayTarget.index : -1) == gifFrameLoader.gifDecoder.getFrameCount() - 1) {
            this.loopCount++;
        }
        int i = this.maxLoopCount;
        if (i != -1 && this.loopCount >= i) {
            stop();
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int i) {
        getPaint().setAlpha(i);
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
        getPaint().setColorFilter(colorFilter);
    }

    @Override // android.graphics.drawable.Drawable
    public boolean setVisible(boolean z, boolean z2) {
        Preconditions.checkArgument(!this.isRecycled, "Cannot change the visibility of a recycled resource. Ensure that you unset the Drawable from your View before changing the View's visibility.");
        this.isVisible = z;
        if (!z) {
            stopRunning();
        } else if (this.isStarted) {
            startRunning();
        }
        return super.setVisible(z, z2);
    }

    @Override // android.graphics.drawable.Animatable
    public void start() {
        this.isStarted = true;
        this.loopCount = 0;
        if (this.isVisible) {
            startRunning();
        }
    }

    public final void startRunning() {
        Preconditions.checkArgument(!this.isRecycled, "You cannot start a recycled Drawable. Ensure thatyou clear any references to the Drawable when clearing the corresponding request.");
        if (this.state.frameLoader.gifDecoder.getFrameCount() == 1) {
            invalidateSelf();
        } else if (!this.isRunning) {
            this.isRunning = true;
            GifFrameLoader gifFrameLoader = this.state.frameLoader;
            if (gifFrameLoader.isCleared) {
                throw new IllegalStateException("Cannot subscribe to a cleared frame loader");
            } else if (!gifFrameLoader.callbacks.contains(this)) {
                boolean isEmpty = gifFrameLoader.callbacks.isEmpty();
                gifFrameLoader.callbacks.add(this);
                if (isEmpty && !gifFrameLoader.isRunning) {
                    gifFrameLoader.isRunning = true;
                    gifFrameLoader.isCleared = false;
                    gifFrameLoader.loadNextFrame();
                }
                invalidateSelf();
            } else {
                throw new IllegalStateException("Cannot subscribe twice in a row");
            }
        }
    }

    @Override // android.graphics.drawable.Animatable
    public void stop() {
        this.isStarted = false;
        stopRunning();
    }

    public final void stopRunning() {
        this.isRunning = false;
        GifFrameLoader gifFrameLoader = this.state.frameLoader;
        gifFrameLoader.callbacks.remove(this);
        if (gifFrameLoader.callbacks.isEmpty()) {
            gifFrameLoader.isRunning = false;
        }
    }

    public GifDrawable(GifFrameLoader gifFrameLoader, Paint paint) {
        GifState gifState = new GifState(gifFrameLoader);
        this.state = gifState;
        this.paint = paint;
    }
}
