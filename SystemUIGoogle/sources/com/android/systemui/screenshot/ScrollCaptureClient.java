package com.android.systemui.screenshot;

import android.content.Context;
import android.graphics.Rect;
import android.media.Image;
import android.media.ImageReader;
import android.os.DeadObjectException;
import android.os.IBinder;
import android.os.ICancellationSignal;
import android.os.RemoteException;
import android.util.Log;
import android.view.IScrollCaptureCallbacks;
import android.view.IScrollCaptureConnection;
import android.view.IScrollCaptureResponseListener;
import android.view.IWindowManager;
import android.view.ScrollCaptureResponse;
import androidx.concurrent.futures.CallbackToFutureAdapter;
import com.android.internal.annotations.VisibleForTesting;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.Objects;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public class ScrollCaptureClient {
    @VisibleForTesting
    static final int MATCH_ANY_TASK = -1;
    private static final String TAG = LogConfig.logTag(ScrollCaptureClient.class);
    private final Executor mBgExecutor;
    private IBinder mHostWindowToken;
    private final IWindowManager mWindowManagerService;

    /* access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public interface Session {
        ListenableFuture<Void> end();

        int getMaxTiles();

        int getPageHeight();

        int getTargetHeight();

        int getTileHeight();

        void release();

        ListenableFuture<CaptureResult> requestTile(int i);
    }

    /* access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class CaptureResult {
        public final Rect captured;
        public final Image image;
        public final Rect requested;

        CaptureResult(Image image, Rect rect, Rect rect2) {
            this.image = image;
            this.requested = rect;
            this.captured = rect2;
        }

        public String toString() {
            return "CaptureResult{requested=" + this.requested + " (" + this.requested.width() + "x" + this.requested.height() + "), captured=" + this.captured + " (" + this.captured.width() + "x" + this.captured.height() + "), image=" + this.image + '}';
        }
    }

    public ScrollCaptureClient(IWindowManager iWindowManager, Executor executor, Context context) {
        Objects.requireNonNull(context.getDisplay(), "context must be associated with a Display!");
        this.mBgExecutor = executor;
        this.mWindowManagerService = iWindowManager;
    }

    public void setHostWindowToken(IBinder iBinder) {
        this.mHostWindowToken = iBinder;
    }

    public ListenableFuture<ScrollCaptureResponse> request(int i) {
        return request(i, -1);
    }

    public ListenableFuture<ScrollCaptureResponse> request(int i, int i2) {
        return CallbackToFutureAdapter.getFuture(new CallbackToFutureAdapter.Resolver(i, i2) { // from class: com.android.systemui.screenshot.ScrollCaptureClient$$ExternalSyntheticLambda0
            public final /* synthetic */ int f$1;
            public final /* synthetic */ int f$2;

            {
                this.f$1 = r2;
                this.f$2 = r3;
            }

            @Override // androidx.concurrent.futures.CallbackToFutureAdapter.Resolver
            public final Object attachCompleter(CallbackToFutureAdapter.Completer completer) {
                return ScrollCaptureClient.$r8$lambda$cnJXGVMvkhtRlp19lL_v4k85vnw(ScrollCaptureClient.this, this.f$1, this.f$2, completer);
            }
        });
    }

    /* access modifiers changed from: private */
    public /* synthetic */ Object lambda$request$0(int i, int i2, final CallbackToFutureAdapter.Completer completer) throws Exception {
        try {
            this.mWindowManagerService.requestScrollCapture(i, this.mHostWindowToken, i2, new IScrollCaptureResponseListener.Stub() { // from class: com.android.systemui.screenshot.ScrollCaptureClient.1
                public void onScrollCaptureResponse(ScrollCaptureResponse scrollCaptureResponse) {
                    completer.set(scrollCaptureResponse);
                }
            });
        } catch (RemoteException e) {
            completer.setException(e);
        }
        return "ScrollCaptureClient#request(displayId=" + i + ", taskId=" + i2 + ")";
    }

    public ListenableFuture<Session> start(ScrollCaptureResponse scrollCaptureResponse, float f) {
        return CallbackToFutureAdapter.getFuture(new CallbackToFutureAdapter.Resolver(scrollCaptureResponse.getConnection(), scrollCaptureResponse, f) { // from class: com.android.systemui.screenshot.ScrollCaptureClient$$ExternalSyntheticLambda1
            public final /* synthetic */ IScrollCaptureConnection f$1;
            public final /* synthetic */ ScrollCaptureResponse f$2;
            public final /* synthetic */ float f$3;

            {
                this.f$1 = r2;
                this.f$2 = r3;
                this.f$3 = r4;
            }

            @Override // androidx.concurrent.futures.CallbackToFutureAdapter.Resolver
            public final Object attachCompleter(CallbackToFutureAdapter.Completer completer) {
                return ScrollCaptureClient.$r8$lambda$wk068HP7VZNs8NQ02V0YtHrBQXA(ScrollCaptureClient.this, this.f$1, this.f$2, this.f$3, completer);
            }
        });
    }

    /* access modifiers changed from: private */
    public /* synthetic */ Object lambda$start$1(IScrollCaptureConnection iScrollCaptureConnection, ScrollCaptureResponse scrollCaptureResponse, float f, CallbackToFutureAdapter.Completer completer) throws Exception {
        if (iScrollCaptureConnection == null || !iScrollCaptureConnection.asBinder().isBinderAlive()) {
            completer.setException(new DeadObjectException("No active connection!"));
            return "";
        }
        new SessionWrapper(iScrollCaptureConnection, scrollCaptureResponse.getWindowBounds(), scrollCaptureResponse.getBoundsInWindow(), f, this.mBgExecutor).start(completer);
        return "IScrollCaptureCallbacks#onCaptureStarted";
    }

    /* access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class SessionWrapper extends IScrollCaptureCallbacks.Stub implements Session, IBinder.DeathRecipient, ImageReader.OnImageAvailableListener {
        private final Executor mBgExecutor;
        private final Rect mBoundsInWindow;
        private ICancellationSignal mCancellationSignal;
        private Rect mCapturedArea;
        private Image mCapturedImage;
        private IScrollCaptureConnection mConnection;
        private CallbackToFutureAdapter.Completer<Void> mEndCompleter;
        private final Object mLock;
        private ImageReader mReader;
        private Rect mRequestRect;
        private CallbackToFutureAdapter.Completer<Session> mStartCompleter;
        private boolean mStarted;
        private final int mTargetHeight;
        private final int mTileHeight;
        private CallbackToFutureAdapter.Completer<CaptureResult> mTileRequestCompleter;
        private final int mTileWidth;
        private final Rect mWindowBounds;

        @Override // com.android.systemui.screenshot.ScrollCaptureClient.Session
        public int getMaxTiles() {
            return 30;
        }

        private SessionWrapper(IScrollCaptureConnection iScrollCaptureConnection, Rect rect, Rect rect2, float f, Executor executor) throws RemoteException {
            this.mLock = new Object();
            Objects.requireNonNull(iScrollCaptureConnection);
            IScrollCaptureConnection iScrollCaptureConnection2 = iScrollCaptureConnection;
            this.mConnection = iScrollCaptureConnection2;
            iScrollCaptureConnection2.asBinder().linkToDeath(this, 0);
            Objects.requireNonNull(rect);
            this.mWindowBounds = rect;
            Objects.requireNonNull(rect2);
            Rect rect3 = rect2;
            this.mBoundsInWindow = rect3;
            int min = Math.min(4194304, (rect3.width() * rect3.height()) / 2);
            this.mTileWidth = rect3.width();
            this.mTileHeight = min / rect3.width();
            this.mTargetHeight = (int) (((float) rect3.height()) * f);
            this.mBgExecutor = executor;
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            Log.d(ScrollCaptureClient.TAG, "binderDied! The target process just crashed :-(");
            this.mConnection = null;
            CallbackToFutureAdapter.Completer<Session> completer = this.mStartCompleter;
            if (completer != null) {
                completer.setException(new DeadObjectException("The remote process died"));
            }
            CallbackToFutureAdapter.Completer<CaptureResult> completer2 = this.mTileRequestCompleter;
            if (completer2 != null) {
                completer2.setException(new DeadObjectException("The remote process died"));
            }
            CallbackToFutureAdapter.Completer<Void> completer3 = this.mEndCompleter;
            if (completer3 != null) {
                completer3.setException(new DeadObjectException("The remote process died"));
            }
        }

        /* access modifiers changed from: private */
        public void start(CallbackToFutureAdapter.Completer<Session> completer) {
            ImageReader newInstance = ImageReader.newInstance(this.mTileWidth, this.mTileHeight, 1, 30, 256);
            this.mReader = newInstance;
            this.mStartCompleter = completer;
            newInstance.setOnImageAvailableListenerWithExecutor(this, this.mBgExecutor);
            try {
                this.mCancellationSignal = this.mConnection.startCapture(this.mReader.getSurface(), this);
                completer.addCancellationListener(new ScrollCaptureClient$SessionWrapper$$ExternalSyntheticLambda3(this), SaveImageInBackgroundTask$$ExternalSyntheticLambda0.INSTANCE);
                this.mStarted = true;
            } catch (RemoteException e) {
                this.mReader.close();
                completer.setException(e);
            }
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$start$0() {
            try {
                this.mCancellationSignal.cancel();
            } catch (RemoteException unused) {
            }
        }

        public void onCaptureStarted() {
            Log.d(ScrollCaptureClient.TAG, "onCaptureStarted");
            this.mStartCompleter.set(this);
        }

        @Override // com.android.systemui.screenshot.ScrollCaptureClient.Session
        public ListenableFuture<CaptureResult> requestTile(int i) {
            this.mRequestRect = new Rect(0, i, this.mTileWidth, this.mTileHeight + i);
            return CallbackToFutureAdapter.getFuture(new ScrollCaptureClient$SessionWrapper$$ExternalSyntheticLambda0(this));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ Object lambda$requestTile$2(CallbackToFutureAdapter.Completer completer) throws Exception {
            IScrollCaptureConnection iScrollCaptureConnection = this.mConnection;
            if (iScrollCaptureConnection == null || !iScrollCaptureConnection.asBinder().isBinderAlive()) {
                completer.setException(new DeadObjectException("Connection is closed!"));
                return "";
            }
            try {
                this.mTileRequestCompleter = completer;
                this.mCancellationSignal = this.mConnection.requestImage(this.mRequestRect);
                completer.addCancellationListener(new ScrollCaptureClient$SessionWrapper$$ExternalSyntheticLambda2(this), SaveImageInBackgroundTask$$ExternalSyntheticLambda0.INSTANCE);
                return "IScrollCaptureCallbacks#onImageRequestCompleted";
            } catch (RemoteException e) {
                completer.setException(e);
                return "IScrollCaptureCallbacks#onImageRequestCompleted";
            }
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$requestTile$1() {
            try {
                this.mCancellationSignal.cancel();
            } catch (RemoteException unused) {
            }
        }

        public void onImageRequestCompleted(int i, Rect rect) {
            synchronized (this.mLock) {
                this.mCapturedArea = rect;
                if (this.mCapturedImage != null || rect == null || rect.isEmpty()) {
                    completeCaptureRequest();
                }
            }
        }

        @Override // android.media.ImageReader.OnImageAvailableListener
        public void onImageAvailable(ImageReader imageReader) {
            synchronized (this.mLock) {
                this.mCapturedImage = this.mReader.acquireLatestImage();
                if (this.mCapturedArea != null) {
                    completeCaptureRequest();
                }
            }
        }

        private void completeCaptureRequest() {
            CaptureResult captureResult = new CaptureResult(this.mCapturedImage, this.mRequestRect, this.mCapturedArea);
            this.mCapturedImage = null;
            this.mRequestRect = null;
            this.mCapturedArea = null;
            this.mTileRequestCompleter.set(captureResult);
        }

        @Override // com.android.systemui.screenshot.ScrollCaptureClient.Session
        public ListenableFuture<Void> end() {
            Log.d(ScrollCaptureClient.TAG, "end()");
            return CallbackToFutureAdapter.getFuture(new ScrollCaptureClient$SessionWrapper$$ExternalSyntheticLambda1(this));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ Object lambda$end$3(CallbackToFutureAdapter.Completer completer) throws Exception {
            if (!this.mStarted) {
                try {
                    this.mConnection.asBinder().unlinkToDeath(this, 0);
                    this.mConnection.close();
                } catch (RemoteException unused) {
                }
                this.mConnection = null;
                completer.set(null);
                return "";
            }
            this.mEndCompleter = completer;
            try {
                this.mConnection.endCapture();
                return "IScrollCaptureCallbacks#onCaptureEnded";
            } catch (RemoteException e) {
                completer.setException(e);
                return "IScrollCaptureCallbacks#onCaptureEnded";
            }
        }

        @Override // com.android.systemui.screenshot.ScrollCaptureClient.Session
        public void release() {
            this.mReader.close();
        }

        public void onCaptureEnded() {
            try {
                this.mConnection.close();
            } catch (RemoteException unused) {
            }
            this.mConnection = null;
            this.mEndCompleter.set(null);
        }

        @Override // com.android.systemui.screenshot.ScrollCaptureClient.Session
        public int getPageHeight() {
            return this.mBoundsInWindow.height();
        }

        @Override // com.android.systemui.screenshot.ScrollCaptureClient.Session
        public int getTileHeight() {
            return this.mTileHeight;
        }

        @Override // com.android.systemui.screenshot.ScrollCaptureClient.Session
        public int getTargetHeight() {
            return this.mTargetHeight;
        }
    }
}
