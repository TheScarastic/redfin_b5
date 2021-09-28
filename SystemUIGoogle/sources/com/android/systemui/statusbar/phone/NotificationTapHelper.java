package com.android.systemui.statusbar.phone;

import android.view.MotionEvent;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.util.concurrency.DelayableExecutor;
/* loaded from: classes.dex */
public class NotificationTapHelper {
    private final ActivationListener mActivationListener;
    private final DoubleTapListener mDoubleTapListener;
    private final DelayableExecutor mExecutor;
    private final FalsingManager mFalsingManager;
    private final SlideBackListener mSlideBackListener;
    private Runnable mTimeoutCancel;
    private boolean mTrackTouch;

    @FunctionalInterface
    /* loaded from: classes.dex */
    public interface ActivationListener {
        void onActiveChanged(boolean z);
    }

    @FunctionalInterface
    /* loaded from: classes.dex */
    public interface DoubleTapListener {
        boolean onDoubleTap();
    }

    @FunctionalInterface
    /* loaded from: classes.dex */
    public interface SlideBackListener {
        boolean onSlideBack();
    }

    private NotificationTapHelper(FalsingManager falsingManager, DelayableExecutor delayableExecutor, ActivationListener activationListener, DoubleTapListener doubleTapListener, SlideBackListener slideBackListener) {
        this.mFalsingManager = falsingManager;
        this.mExecutor = delayableExecutor;
        this.mActivationListener = activationListener;
        this.mDoubleTapListener = doubleTapListener;
        this.mSlideBackListener = slideBackListener;
    }

    @VisibleForTesting
    boolean onTouchEvent(MotionEvent motionEvent) {
        return onTouchEvent(motionEvent, Integer.MAX_VALUE);
    }

    public boolean onTouchEvent(MotionEvent motionEvent, int i) {
        int actionMasked = motionEvent.getActionMasked();
        boolean z = true;
        if (actionMasked == 0) {
            if (motionEvent.getY() > ((float) i)) {
                z = false;
            }
            this.mTrackTouch = z;
        } else if (actionMasked == 1) {
            this.mTrackTouch = false;
            if (!this.mFalsingManager.isFalseTap(0)) {
                makeInactive();
                return this.mDoubleTapListener.onDoubleTap();
            } else if (this.mFalsingManager.isSimpleTap()) {
                SlideBackListener slideBackListener = this.mSlideBackListener;
                if (slideBackListener != null && slideBackListener.onSlideBack()) {
                    return true;
                }
                if (this.mTimeoutCancel == null) {
                    makeActive();
                    return true;
                }
                makeInactive();
                if (!this.mFalsingManager.isFalseDoubleTap()) {
                    return this.mDoubleTapListener.onDoubleTap();
                }
            } else {
                makeInactive();
            }
        } else if (actionMasked != 2) {
            if (actionMasked == 3) {
                makeInactive();
                this.mTrackTouch = false;
            }
        } else if (this.mTrackTouch && !this.mFalsingManager.isSimpleTap()) {
            makeInactive();
            this.mTrackTouch = false;
        }
        return this.mTrackTouch;
    }

    private void makeActive() {
        this.mTimeoutCancel = this.mExecutor.executeDelayed(new Runnable() { // from class: com.android.systemui.statusbar.phone.NotificationTapHelper$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                NotificationTapHelper.this.makeInactive();
            }
        }, 1200);
        this.mActivationListener.onActiveChanged(true);
    }

    /* access modifiers changed from: private */
    public void makeInactive() {
        this.mActivationListener.onActiveChanged(false);
        Runnable runnable = this.mTimeoutCancel;
        if (runnable != null) {
            runnable.run();
            this.mTimeoutCancel = null;
        }
    }

    /* loaded from: classes.dex */
    public static class Factory {
        private final DelayableExecutor mDelayableExecutor;
        private final FalsingManager mFalsingManager;

        public Factory(FalsingManager falsingManager, DelayableExecutor delayableExecutor) {
            this.mFalsingManager = falsingManager;
            this.mDelayableExecutor = delayableExecutor;
        }

        public NotificationTapHelper create(ActivationListener activationListener, DoubleTapListener doubleTapListener, SlideBackListener slideBackListener) {
            return new NotificationTapHelper(this.mFalsingManager, this.mDelayableExecutor, activationListener, doubleTapListener, slideBackListener);
        }
    }
}
