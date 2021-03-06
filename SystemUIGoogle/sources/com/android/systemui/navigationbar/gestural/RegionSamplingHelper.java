package com.android.systemui.navigationbar.gestural;

import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Handler;
import android.view.CompositionSamplingListener;
import android.view.SurfaceControl;
import android.view.View;
import android.view.ViewRootImpl;
import android.view.ViewTreeObserver;
import com.android.systemui.R$dimen;
import java.io.PrintWriter;
/* loaded from: classes.dex */
public class RegionSamplingHelper implements View.OnAttachStateChangeListener, View.OnLayoutChangeListener {
    private final SamplingCallback mCallback;
    private float mCurrentMedianLuma;
    private boolean mFirstSamplingAfterStart;
    private boolean mIsDestroyed;
    private float mLastMedianLuma;
    private final float mLuminanceChangeThreshold;
    private final float mLuminanceThreshold;
    private final View mSampledView;
    private final CompositionSamplingListener mSamplingListener;
    private boolean mWaitingOnDraw;
    private boolean mWindowHasBlurs;
    private boolean mWindowVisible;
    private final Handler mHandler = new Handler();
    private final Rect mSamplingRequestBounds = new Rect();
    private final Rect mRegisteredSamplingBounds = new Rect();
    private boolean mSamplingEnabled = false;
    private boolean mSamplingListenerRegistered = false;
    private SurfaceControl mRegisteredStopLayer = null;
    private ViewTreeObserver.OnDrawListener mUpdateOnDraw = new ViewTreeObserver.OnDrawListener() { // from class: com.android.systemui.navigationbar.gestural.RegionSamplingHelper.1
        @Override // android.view.ViewTreeObserver.OnDrawListener
        public void onDraw() {
            RegionSamplingHelper.this.mHandler.post(RegionSamplingHelper.this.mRemoveDrawRunnable);
            RegionSamplingHelper.this.onDraw();
        }
    };
    private Runnable mRemoveDrawRunnable = new Runnable() { // from class: com.android.systemui.navigationbar.gestural.RegionSamplingHelper.2
        @Override // java.lang.Runnable
        public void run() {
            RegionSamplingHelper.this.mSampledView.getViewTreeObserver().removeOnDrawListener(RegionSamplingHelper.this.mUpdateOnDraw);
        }
    };

    /* loaded from: classes.dex */
    public interface SamplingCallback {
        Rect getSampledRegion(View view);

        default boolean isSamplingEnabled() {
            return true;
        }

        void onRegionDarknessChanged(boolean z);
    }

    public RegionSamplingHelper(View view, SamplingCallback samplingCallback) {
        this.mSamplingListener = new CompositionSamplingListener(view.getContext().getMainExecutor()) { // from class: com.android.systemui.navigationbar.gestural.RegionSamplingHelper.3
            public void onSampleCollected(float f) {
                if (RegionSamplingHelper.this.mSamplingEnabled) {
                    RegionSamplingHelper.this.updateMediaLuma(f);
                }
            }
        };
        this.mSampledView = view;
        view.addOnAttachStateChangeListener(this);
        view.addOnLayoutChangeListener(this);
        Resources resources = view.getResources();
        this.mLuminanceThreshold = resources.getFloat(R$dimen.navigation_luminance_threshold);
        this.mLuminanceChangeThreshold = resources.getFloat(R$dimen.navigation_luminance_change_threshold);
        this.mCallback = samplingCallback;
    }

    /* access modifiers changed from: private */
    public void onDraw() {
        if (this.mWaitingOnDraw) {
            this.mWaitingOnDraw = false;
            updateSamplingListener();
        }
    }

    public void start(Rect rect) {
        if (this.mCallback.isSamplingEnabled()) {
            if (rect != null) {
                this.mSamplingRequestBounds.set(rect);
            }
            this.mSamplingEnabled = true;
            this.mLastMedianLuma = -1.0f;
            this.mFirstSamplingAfterStart = true;
            updateSamplingListener();
        }
    }

    public void stop() {
        this.mSamplingEnabled = false;
        updateSamplingListener();
    }

    public void stopAndDestroy() {
        stop();
        this.mSamplingListener.destroy();
        this.mIsDestroyed = true;
    }

    @Override // android.view.View.OnAttachStateChangeListener
    public void onViewAttachedToWindow(View view) {
        updateSamplingListener();
    }

    @Override // android.view.View.OnAttachStateChangeListener
    public void onViewDetachedFromWindow(View view) {
        stopAndDestroy();
    }

    @Override // android.view.View.OnLayoutChangeListener
    public void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        updateSamplingRect();
    }

    private void updateSamplingListener() {
        if (this.mSamplingEnabled && !this.mSamplingRequestBounds.isEmpty() && this.mWindowVisible && !this.mWindowHasBlurs && (this.mSampledView.isAttachedToWindow() || this.mFirstSamplingAfterStart)) {
            ViewRootImpl viewRootImpl = this.mSampledView.getViewRootImpl();
            SurfaceControl surfaceControl = null;
            SurfaceControl surfaceControl2 = viewRootImpl != null ? viewRootImpl.getSurfaceControl() : null;
            if (surfaceControl2 != null && surfaceControl2.isValid()) {
                surfaceControl = surfaceControl2;
            } else if (!this.mWaitingOnDraw) {
                this.mWaitingOnDraw = true;
                if (this.mHandler.hasCallbacks(this.mRemoveDrawRunnable)) {
                    this.mHandler.removeCallbacks(this.mRemoveDrawRunnable);
                } else {
                    this.mSampledView.getViewTreeObserver().addOnDrawListener(this.mUpdateOnDraw);
                }
            }
            if (!this.mSamplingRequestBounds.equals(this.mRegisteredSamplingBounds) || this.mRegisteredStopLayer != surfaceControl) {
                unregisterSamplingListener();
                this.mSamplingListenerRegistered = true;
                CompositionSamplingListener.register(this.mSamplingListener, 0, surfaceControl, this.mSamplingRequestBounds);
                this.mRegisteredSamplingBounds.set(this.mSamplingRequestBounds);
                this.mRegisteredStopLayer = surfaceControl;
            }
            this.mFirstSamplingAfterStart = false;
            return;
        }
        unregisterSamplingListener();
    }

    private void unregisterSamplingListener() {
        if (this.mSamplingListenerRegistered) {
            this.mSamplingListenerRegistered = false;
            this.mRegisteredStopLayer = null;
            this.mRegisteredSamplingBounds.setEmpty();
            CompositionSamplingListener.unregister(this.mSamplingListener);
        }
    }

    /* access modifiers changed from: private */
    public void updateMediaLuma(float f) {
        this.mCurrentMedianLuma = f;
        if (Math.abs(f - this.mLastMedianLuma) > this.mLuminanceChangeThreshold) {
            this.mCallback.onRegionDarknessChanged(f < this.mLuminanceThreshold);
            this.mLastMedianLuma = f;
        }
    }

    public void updateSamplingRect() {
        Rect sampledRegion = this.mCallback.getSampledRegion(this.mSampledView);
        if (!this.mSamplingRequestBounds.equals(sampledRegion)) {
            this.mSamplingRequestBounds.set(sampledRegion);
            updateSamplingListener();
        }
    }

    public void setWindowVisible(boolean z) {
        this.mWindowVisible = z;
        updateSamplingListener();
    }

    public void setWindowHasBlurs(boolean z) {
        this.mWindowHasBlurs = z;
        updateSamplingListener();
    }

    public void dump(PrintWriter printWriter) {
        printWriter.println("RegionSamplingHelper:");
        printWriter.println("  sampleView isAttached: " + this.mSampledView.isAttachedToWindow());
        StringBuilder sb = new StringBuilder();
        sb.append("  sampleView isScValid: ");
        sb.append(this.mSampledView.isAttachedToWindow() ? Boolean.valueOf(this.mSampledView.getViewRootImpl().getSurfaceControl().isValid()) : "notAttached");
        printWriter.println(sb.toString());
        printWriter.println("  mSamplingEnabled: " + this.mSamplingEnabled);
        printWriter.println("  mSamplingListenerRegistered: " + this.mSamplingListenerRegistered);
        printWriter.println("  mSamplingRequestBounds: " + this.mSamplingRequestBounds);
        printWriter.println("  mRegisteredSamplingBounds: " + this.mRegisteredSamplingBounds);
        printWriter.println("  mLastMedianLuma: " + this.mLastMedianLuma);
        printWriter.println("  mCurrentMedianLuma: " + this.mCurrentMedianLuma);
        printWriter.println("  mWindowVisible: " + this.mWindowVisible);
        printWriter.println("  mWindowHasBlurs: " + this.mWindowHasBlurs);
        printWriter.println("  mWaitingOnDraw: " + this.mWaitingOnDraw);
        printWriter.println("  mRegisteredStopLayer: " + this.mRegisteredStopLayer);
        printWriter.println("  mIsDestroyed: " + this.mIsDestroyed);
    }
}
