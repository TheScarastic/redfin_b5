package com.android.wm.shell.pip.phone;

import android.graphics.PointF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import com.android.internal.annotations.VisibleForTesting;
import com.android.wm.shell.common.ShellExecutor;
import java.io.PrintWriter;
/* loaded from: classes2.dex */
public class PipTouchState {
    @VisibleForTesting
    public static final long DOUBLE_TAP_TIMEOUT = 200;
    private int mActivePointerId;
    private final Runnable mDoubleTapTimeoutCallback;
    private final Runnable mHoverExitTimeoutCallback;
    private final ShellExecutor mMainExecutor;
    private VelocityTracker mVelocityTracker;
    private final ViewConfiguration mViewConfig;
    private long mDownTouchTime = 0;
    private long mLastDownTouchTime = 0;
    private long mUpTouchTime = 0;
    private final PointF mDownTouch = new PointF();
    private final PointF mDownDelta = new PointF();
    private final PointF mLastTouch = new PointF();
    private final PointF mLastDelta = new PointF();
    private final PointF mVelocity = new PointF();
    private boolean mAllowTouches = true;
    private boolean mIsUserInteracting = false;
    private boolean mIsDoubleTap = false;
    private boolean mIsWaitingForDoubleTap = false;
    private boolean mIsDragging = false;
    private boolean mPreviouslyDragging = false;
    private boolean mStartedDragging = false;
    private boolean mAllowDraggingOffscreen = false;
    private int mLastTouchDisplayId = -1;

    public PipTouchState(ViewConfiguration viewConfiguration, Runnable runnable, Runnable runnable2, ShellExecutor shellExecutor) {
        this.mViewConfig = viewConfiguration;
        this.mDoubleTapTimeoutCallback = runnable;
        this.mHoverExitTimeoutCallback = runnable2;
        this.mMainExecutor = shellExecutor;
    }

    public void reset() {
        this.mAllowDraggingOffscreen = false;
        this.mIsDragging = false;
        this.mStartedDragging = false;
        this.mIsUserInteracting = false;
        this.mLastTouchDisplayId = -1;
    }

    public void onTouchEvent(MotionEvent motionEvent) {
        this.mLastTouchDisplayId = motionEvent.getDisplayId();
        int actionMasked = motionEvent.getActionMasked();
        boolean z = false;
        int i = 0;
        z = false;
        z = false;
        boolean z2 = true;
        if (actionMasked != 0) {
            if (actionMasked != 1) {
                if (actionMasked != 2) {
                    if (actionMasked != 3) {
                        if (actionMasked != 6) {
                            if (actionMasked == 11) {
                                removeHoverExitTimeoutCallback();
                                return;
                            }
                            return;
                        } else if (this.mIsUserInteracting) {
                            addMovementToVelocityTracker(motionEvent);
                            int actionIndex = motionEvent.getActionIndex();
                            if (motionEvent.getPointerId(actionIndex) == this.mActivePointerId) {
                                if (actionIndex == 0) {
                                    i = 1;
                                }
                                this.mActivePointerId = motionEvent.getPointerId(i);
                                this.mLastTouch.set(motionEvent.getRawX(i), motionEvent.getRawY(i));
                                return;
                            }
                            return;
                        } else {
                            return;
                        }
                    }
                } else if (this.mIsUserInteracting) {
                    addMovementToVelocityTracker(motionEvent);
                    int findPointerIndex = motionEvent.findPointerIndex(this.mActivePointerId);
                    if (findPointerIndex == -1) {
                        Log.e("PipTouchState", "Invalid active pointer id on MOVE: " + this.mActivePointerId);
                        return;
                    }
                    float rawX = motionEvent.getRawX(findPointerIndex);
                    float rawY = motionEvent.getRawY(findPointerIndex);
                    PointF pointF = this.mLastDelta;
                    PointF pointF2 = this.mLastTouch;
                    pointF.set(rawX - pointF2.x, rawY - pointF2.y);
                    PointF pointF3 = this.mDownDelta;
                    PointF pointF4 = this.mDownTouch;
                    pointF3.set(rawX - pointF4.x, rawY - pointF4.y);
                    Object[] objArr = this.mDownDelta.length() > ((float) this.mViewConfig.getScaledTouchSlop()) ? 1 : null;
                    if (this.mIsDragging) {
                        this.mStartedDragging = false;
                    } else if (objArr != null) {
                        this.mIsDragging = true;
                        this.mStartedDragging = true;
                    }
                    this.mLastTouch.set(rawX, rawY);
                    return;
                } else {
                    return;
                }
            } else if (this.mIsUserInteracting) {
                addMovementToVelocityTracker(motionEvent);
                this.mVelocityTracker.computeCurrentVelocity(1000, (float) this.mViewConfig.getScaledMaximumFlingVelocity());
                this.mVelocity.set(this.mVelocityTracker.getXVelocity(), this.mVelocityTracker.getYVelocity());
                int findPointerIndex2 = motionEvent.findPointerIndex(this.mActivePointerId);
                if (findPointerIndex2 == -1) {
                    Log.e("PipTouchState", "Invalid active pointer id on UP: " + this.mActivePointerId);
                    return;
                }
                this.mUpTouchTime = motionEvent.getEventTime();
                this.mLastTouch.set(motionEvent.getRawX(findPointerIndex2), motionEvent.getRawY(findPointerIndex2));
                boolean z3 = this.mIsDragging;
                this.mPreviouslyDragging = z3;
                if (!this.mIsDoubleTap && !z3 && this.mUpTouchTime - this.mDownTouchTime < 200) {
                    z = true;
                }
                this.mIsWaitingForDoubleTap = z;
            } else {
                return;
            }
            recycleVelocityTracker();
        } else if (this.mAllowTouches) {
            initOrResetVelocityTracker();
            addMovementToVelocityTracker(motionEvent);
            this.mActivePointerId = motionEvent.getPointerId(0);
            this.mLastTouch.set(motionEvent.getRawX(), motionEvent.getRawY());
            this.mDownTouch.set(this.mLastTouch);
            this.mAllowDraggingOffscreen = true;
            this.mIsUserInteracting = true;
            long eventTime = motionEvent.getEventTime();
            this.mDownTouchTime = eventTime;
            if (this.mPreviouslyDragging || eventTime - this.mLastDownTouchTime >= 200) {
                z2 = false;
            }
            this.mIsDoubleTap = z2;
            this.mIsWaitingForDoubleTap = false;
            this.mIsDragging = false;
            this.mLastDownTouchTime = eventTime;
            Runnable runnable = this.mDoubleTapTimeoutCallback;
            if (runnable != null) {
                this.mMainExecutor.removeCallbacks(runnable);
            }
        }
    }

    public PointF getVelocity() {
        return this.mVelocity;
    }

    public PointF getLastTouchPosition() {
        return this.mLastTouch;
    }

    public PointF getLastTouchDelta() {
        return this.mLastDelta;
    }

    public PointF getDownTouchPosition() {
        return this.mDownTouch;
    }

    public boolean isDragging() {
        return this.mIsDragging;
    }

    public boolean isUserInteracting() {
        return this.mIsUserInteracting;
    }

    public boolean startedDragging() {
        return this.mStartedDragging;
    }

    public void setAllowTouches(boolean z) {
        this.mAllowTouches = z;
        if (this.mIsUserInteracting) {
            reset();
        }
    }

    public boolean isDoubleTap() {
        return this.mIsDoubleTap;
    }

    public boolean isWaitingForDoubleTap() {
        return this.mIsWaitingForDoubleTap;
    }

    public void scheduleDoubleTapTimeoutCallback() {
        if (this.mIsWaitingForDoubleTap) {
            long doubleTapTimeoutCallbackDelay = getDoubleTapTimeoutCallbackDelay();
            this.mMainExecutor.removeCallbacks(this.mDoubleTapTimeoutCallback);
            this.mMainExecutor.executeDelayed(this.mDoubleTapTimeoutCallback, doubleTapTimeoutCallbackDelay);
        }
    }

    @VisibleForTesting
    public long getDoubleTapTimeoutCallbackDelay() {
        if (this.mIsWaitingForDoubleTap) {
            return Math.max(0L, 200 - (this.mUpTouchTime - this.mDownTouchTime));
        }
        return -1;
    }

    public void removeDoubleTapTimeoutCallback() {
        this.mIsWaitingForDoubleTap = false;
        this.mMainExecutor.removeCallbacks(this.mDoubleTapTimeoutCallback);
    }

    @VisibleForTesting
    public void scheduleHoverExitTimeoutCallback() {
        this.mMainExecutor.removeCallbacks(this.mHoverExitTimeoutCallback);
        this.mMainExecutor.executeDelayed(this.mHoverExitTimeoutCallback, 50);
    }

    /* access modifiers changed from: package-private */
    public void removeHoverExitTimeoutCallback() {
        this.mMainExecutor.removeCallbacks(this.mHoverExitTimeoutCallback);
    }

    /* access modifiers changed from: package-private */
    public void addMovementToVelocityTracker(MotionEvent motionEvent) {
        if (this.mVelocityTracker != null) {
            float rawX = motionEvent.getRawX() - motionEvent.getX();
            float rawY = motionEvent.getRawY() - motionEvent.getY();
            motionEvent.offsetLocation(rawX, rawY);
            this.mVelocityTracker.addMovement(motionEvent);
            motionEvent.offsetLocation(-rawX, -rawY);
        }
    }

    private void initOrResetVelocityTracker() {
        VelocityTracker velocityTracker = this.mVelocityTracker;
        if (velocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        } else {
            velocityTracker.clear();
        }
    }

    private void recycleVelocityTracker() {
        VelocityTracker velocityTracker = this.mVelocityTracker;
        if (velocityTracker != null) {
            velocityTracker.recycle();
            this.mVelocityTracker = null;
        }
    }

    public void dump(PrintWriter printWriter, String str) {
        String str2 = str + "  ";
        printWriter.println(str + "PipTouchState");
        printWriter.println(str2 + "mAllowTouches=" + this.mAllowTouches);
        printWriter.println(str2 + "mActivePointerId=" + this.mActivePointerId);
        printWriter.println(str2 + "mLastTouchDisplayId=" + this.mLastTouchDisplayId);
        printWriter.println(str2 + "mDownTouch=" + this.mDownTouch);
        printWriter.println(str2 + "mDownDelta=" + this.mDownDelta);
        printWriter.println(str2 + "mLastTouch=" + this.mLastTouch);
        printWriter.println(str2 + "mLastDelta=" + this.mLastDelta);
        printWriter.println(str2 + "mVelocity=" + this.mVelocity);
        printWriter.println(str2 + "mIsUserInteracting=" + this.mIsUserInteracting);
        printWriter.println(str2 + "mIsDragging=" + this.mIsDragging);
        printWriter.println(str2 + "mStartedDragging=" + this.mStartedDragging);
        printWriter.println(str2 + "mAllowDraggingOffscreen=" + this.mAllowDraggingOffscreen);
    }
}
