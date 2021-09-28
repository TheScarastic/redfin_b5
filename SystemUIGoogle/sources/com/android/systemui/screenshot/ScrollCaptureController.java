package com.android.systemui.screenshot;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.provider.Settings;
import android.util.Log;
import android.view.ScrollCaptureResponse;
import androidx.concurrent.futures.CallbackToFutureAdapter;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.screenshot.ScrollCaptureClient;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public class ScrollCaptureController {
    private static final String TAG = LogConfig.logTag(ScrollCaptureController.class);
    private final Executor mBgExecutor;
    private CallbackToFutureAdapter.Completer<LongScreenshot> mCaptureCompleter;
    private final ScrollCaptureClient mClient;
    private final Context mContext;
    private ListenableFuture<Void> mEndFuture;
    private boolean mFinishOnBoundary;
    private final ImageTileSet mImageTileSet;
    private boolean mScrollingUp = true;
    private ScrollCaptureClient.Session mSession;
    private ListenableFuture<ScrollCaptureClient.Session> mSessionFuture;
    private ListenableFuture<ScrollCaptureClient.CaptureResult> mTileFuture;

    @VisibleForTesting
    float getTargetTopSizeRatio() {
        return 0.4f;
    }

    /* access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class LongScreenshot {
        private final ImageTileSet mImageTileSet;
        private final ScrollCaptureClient.Session mSession;

        LongScreenshot(ScrollCaptureClient.Session session, ImageTileSet imageTileSet) {
            this.mSession = session;
            this.mImageTileSet = imageTileSet;
        }

        public Bitmap toBitmap() {
            return this.mImageTileSet.toBitmap();
        }

        public void release() {
            this.mImageTileSet.clear();
            this.mSession.release();
        }

        public int getLeft() {
            return this.mImageTileSet.getLeft();
        }

        public int getTop() {
            return this.mImageTileSet.getTop();
        }

        public int getBottom() {
            return this.mImageTileSet.getBottom();
        }

        public int getWidth() {
            return this.mImageTileSet.getWidth();
        }

        public int getHeight() {
            return this.mImageTileSet.getHeight();
        }

        public int getPageHeight() {
            return this.mSession.getPageHeight();
        }

        public String toString() {
            return "LongScreenshot{w=" + this.mImageTileSet.getWidth() + ", h=" + this.mImageTileSet.getHeight() + "}";
        }

        public Drawable getDrawable() {
            return this.mImageTileSet.getDrawable();
        }
    }

    /* access modifiers changed from: package-private */
    public ScrollCaptureController(Context context, Executor executor, ScrollCaptureClient scrollCaptureClient, ImageTileSet imageTileSet) {
        this.mContext = context;
        this.mBgExecutor = executor;
        this.mClient = scrollCaptureClient;
        this.mImageTileSet = imageTileSet;
    }

    /* access modifiers changed from: package-private */
    public ListenableFuture<LongScreenshot> run(ScrollCaptureResponse scrollCaptureResponse) {
        return CallbackToFutureAdapter.getFuture(new CallbackToFutureAdapter.Resolver(scrollCaptureResponse) { // from class: com.android.systemui.screenshot.ScrollCaptureController$$ExternalSyntheticLambda0
            public final /* synthetic */ ScrollCaptureResponse f$1;

            {
                this.f$1 = r2;
            }

            @Override // androidx.concurrent.futures.CallbackToFutureAdapter.Resolver
            public final Object attachCompleter(CallbackToFutureAdapter.Completer completer) {
                return ScrollCaptureController.this.lambda$run$1(this.f$1, completer);
            }
        });
    }

    /* access modifiers changed from: private */
    public /* synthetic */ Object lambda$run$1(ScrollCaptureResponse scrollCaptureResponse, CallbackToFutureAdapter.Completer completer) throws Exception {
        this.mCaptureCompleter = completer;
        this.mBgExecutor.execute(new Runnable(scrollCaptureResponse) { // from class: com.android.systemui.screenshot.ScrollCaptureController$$ExternalSyntheticLambda4
            public final /* synthetic */ ScrollCaptureResponse f$1;

            {
                this.f$1 = r2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                ScrollCaptureController.this.lambda$run$0(this.f$1);
            }
        });
        return "<batch scroll capture>";
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$run$0(ScrollCaptureResponse scrollCaptureResponse) {
        ListenableFuture<ScrollCaptureClient.Session> start = this.mClient.start(scrollCaptureResponse, Settings.Secure.getFloat(this.mContext.getContentResolver(), "screenshot.scroll_max_pages", 3.0f));
        this.mSessionFuture = start;
        start.addListener(new Runnable() { // from class: com.android.systemui.screenshot.ScrollCaptureController$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                ScrollCaptureController.this.onStartComplete();
            }
        }, this.mContext.getMainExecutor());
    }

    /* access modifiers changed from: private */
    public void onStartComplete() {
        try {
            this.mSession = this.mSessionFuture.get();
            requestNextTile(0);
        } catch (InterruptedException | ExecutionException e) {
            Log.e(TAG, "session start failed!");
            this.mCaptureCompleter.setException(e);
        }
    }

    private void requestNextTile(int i) {
        ListenableFuture<ScrollCaptureClient.CaptureResult> requestTile = this.mSession.requestTile(i);
        this.mTileFuture = requestTile;
        requestTile.addListener(new Runnable() { // from class: com.android.systemui.screenshot.ScrollCaptureController$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                ScrollCaptureController.this.lambda$requestNextTile$2();
            }
        }, this.mContext.getMainExecutor());
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$requestNextTile$2() {
        try {
            onCaptureResult(this.mTileFuture.get());
        } catch (InterruptedException | ExecutionException e) {
            Log.e(TAG, "requestTile failed!", e);
            this.mCaptureCompleter.setException(e);
        }
    }

    private void onCaptureResult(ScrollCaptureClient.CaptureResult captureResult) {
        int i;
        int top;
        int tileHeight;
        boolean z = captureResult.captured.height() == 0;
        if (z) {
            if (this.mFinishOnBoundary) {
                finishCapture();
                return;
            }
            this.mImageTileSet.clear();
            this.mFinishOnBoundary = true;
            this.mScrollingUp = !this.mScrollingUp;
        } else if (this.mImageTileSet.size() + 1 >= this.mSession.getMaxTiles()) {
            finishCapture();
            return;
        } else if (this.mScrollingUp && !this.mFinishOnBoundary && ((float) (this.mImageTileSet.getHeight() + captureResult.captured.height())) >= ((float) this.mSession.getTargetHeight()) * 0.4f) {
            this.mImageTileSet.clear();
            this.mScrollingUp = false;
        }
        if (!z) {
            this.mImageTileSet.lambda$addTile$0(new ImageTile(captureResult.image, captureResult.captured));
        }
        Rect gaps = this.mImageTileSet.getGaps();
        if (!gaps.isEmpty()) {
            requestNextTile(gaps.top);
        } else if (this.mImageTileSet.getHeight() >= this.mSession.getTargetHeight()) {
            finishCapture();
        } else if (z) {
            if (this.mScrollingUp) {
                top = captureResult.requested.top;
                tileHeight = this.mSession.getTileHeight();
                i = top - tileHeight;
                requestNextTile(i);
            }
            i = captureResult.requested.bottom;
            requestNextTile(i);
        } else if (this.mScrollingUp) {
            top = this.mImageTileSet.getTop();
            tileHeight = this.mSession.getTileHeight();
            i = top - tileHeight;
            requestNextTile(i);
        } else {
            i = this.mImageTileSet.getBottom();
            requestNextTile(i);
        }
    }

    private void finishCapture() {
        ListenableFuture<Void> end = this.mSession.end();
        this.mEndFuture = end;
        end.addListener(new Runnable() { // from class: com.android.systemui.screenshot.ScrollCaptureController$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                ScrollCaptureController.this.lambda$finishCapture$3();
            }
        }, this.mContext.getMainExecutor());
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$finishCapture$3() {
        this.mCaptureCompleter.set(new LongScreenshot(this.mSession, this.mImageTileSet));
    }
}
