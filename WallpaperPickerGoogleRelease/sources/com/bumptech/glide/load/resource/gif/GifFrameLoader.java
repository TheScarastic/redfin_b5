package com.bumptech.glide.load.resource.gif;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.gifdecoder.GifDecoder;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.bumptech.glide.signature.ObjectKey;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
/* loaded from: classes.dex */
public class GifFrameLoader {
    public final BitmapPool bitmapPool;
    public final List<FrameCallback> callbacks = new ArrayList();
    public DelayTarget current;
    public Bitmap firstFrame;
    public final GifDecoder gifDecoder;
    public final Handler handler;
    public boolean isCleared;
    public boolean isLoadPending;
    public boolean isRunning;
    public DelayTarget next;
    public OnEveryFrameListener onEveryFrameListener;
    public DelayTarget pendingTarget;
    public RequestBuilder<Bitmap> requestBuilder;
    public final RequestManager requestManager;
    public Transformation<Bitmap> transformation;

    /* loaded from: classes.dex */
    public static class DelayTarget extends SimpleTarget<Bitmap> {
        public final Handler handler;
        public final int index;
        public Bitmap resource;
        public final long targetTime;

        public DelayTarget(Handler handler, int i, long j) {
            this.handler = handler;
            this.index = i;
            this.targetTime = j;
        }

        @Override // com.bumptech.glide.request.target.Target
        public void onResourceReady(Object obj, Transition transition) {
            this.resource = (Bitmap) obj;
            this.handler.sendMessageAtTime(this.handler.obtainMessage(1, this), this.targetTime);
        }
    }

    /* loaded from: classes.dex */
    public interface FrameCallback {
        void onFrameReady();
    }

    /* loaded from: classes.dex */
    public class FrameLoaderCallback implements Handler.Callback {
        public FrameLoaderCallback() {
        }

        @Override // android.os.Handler.Callback
        public boolean handleMessage(Message message) {
            int i = message.what;
            if (i == 1) {
                GifFrameLoader.this.onFrameReady((DelayTarget) message.obj);
                return true;
            } else if (i != 2) {
                return false;
            } else {
                GifFrameLoader.this.requestManager.clear((DelayTarget) message.obj);
                return false;
            }
        }
    }

    /* loaded from: classes.dex */
    public interface OnEveryFrameListener {
        void onFrameReady();
    }

    public GifFrameLoader(Glide glide, GifDecoder gifDecoder, int i, int i2, Transformation<Bitmap> transformation, Bitmap bitmap) {
        BitmapPool bitmapPool = glide.bitmapPool;
        RequestManager with = Glide.with(glide.getContext());
        RequestBuilder<Bitmap> apply = Glide.with(glide.getContext()).asBitmap().apply((BaseRequestOptions<?>) RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE).useAnimationPool(true).skipMemoryCache(true).override(i, i2));
        this.requestManager = with;
        Handler handler = new Handler(Looper.getMainLooper(), new FrameLoaderCallback());
        this.bitmapPool = bitmapPool;
        this.handler = handler;
        this.requestBuilder = apply;
        this.gifDecoder = gifDecoder;
        setFrameTransformation(transformation, bitmap);
    }

    public Bitmap getCurrentFrame() {
        DelayTarget delayTarget = this.current;
        if (delayTarget != null) {
            return delayTarget.resource;
        }
        return this.firstFrame;
    }

    public final void loadNextFrame() {
        if (this.isRunning && !this.isLoadPending) {
            DelayTarget delayTarget = this.pendingTarget;
            if (delayTarget != null) {
                this.pendingTarget = null;
                onFrameReady(delayTarget);
                return;
            }
            this.isLoadPending = true;
            long uptimeMillis = SystemClock.uptimeMillis() + ((long) this.gifDecoder.getNextDelay());
            this.gifDecoder.advance();
            this.next = new DelayTarget(this.handler, this.gifDecoder.getCurrentFrameIndex(), uptimeMillis);
            RequestBuilder<Bitmap> apply = this.requestBuilder.apply((BaseRequestOptions<?>) new RequestOptions().signature(new ObjectKey(Double.valueOf(Math.random()))));
            apply.load(this.gifDecoder);
            apply.into(this.next, null, apply);
        }
    }

    public void onFrameReady(DelayTarget delayTarget) {
        OnEveryFrameListener onEveryFrameListener = this.onEveryFrameListener;
        if (onEveryFrameListener != null) {
            onEveryFrameListener.onFrameReady();
        }
        this.isLoadPending = false;
        if (this.isCleared) {
            this.handler.obtainMessage(2, delayTarget).sendToTarget();
        } else if (!this.isRunning) {
            this.pendingTarget = delayTarget;
        } else {
            if (delayTarget.resource != null) {
                Bitmap bitmap = this.firstFrame;
                if (bitmap != null) {
                    this.bitmapPool.put(bitmap);
                    this.firstFrame = null;
                }
                DelayTarget delayTarget2 = this.current;
                this.current = delayTarget;
                int size = this.callbacks.size();
                while (true) {
                    size--;
                    if (size < 0) {
                        break;
                    }
                    this.callbacks.get(size).onFrameReady();
                }
                if (delayTarget2 != null) {
                    this.handler.obtainMessage(2, delayTarget2).sendToTarget();
                }
            }
            loadNextFrame();
        }
    }

    public void setFrameTransformation(Transformation<Bitmap> transformation, Bitmap bitmap) {
        Objects.requireNonNull(transformation, "Argument must not be null");
        this.transformation = transformation;
        Objects.requireNonNull(bitmap, "Argument must not be null");
        this.firstFrame = bitmap;
        this.requestBuilder = this.requestBuilder.apply((BaseRequestOptions<?>) new RequestOptions().transform(transformation, true));
    }

    public void setOnEveryFrameReadyListener(OnEveryFrameListener onEveryFrameListener) {
        this.onEveryFrameListener = onEveryFrameListener;
    }
}
