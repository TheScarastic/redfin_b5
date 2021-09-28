package com.android.wm.shell.common.split;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceControlViewHost;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewPropertyAnimator;
import android.view.WindowManager;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import com.android.wm.shell.R;
import com.android.wm.shell.animation.Interpolators;
/* loaded from: classes2.dex */
public class DividerView extends FrameLayout implements View.OnTouchListener {
    private View mBackground;
    private GestureDetector mDoubleTapDetector;
    private DividerHandleView mHandle;
    private boolean mInteractive;
    private boolean mMoving;
    private SplitLayout mSplitLayout;
    private int mStartPos;
    private int mTouchElevation;
    private final int mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    private VelocityTracker mVelocityTracker;
    private SurfaceControlViewHost mViewHost;

    public DividerView(Context context) {
        super(context);
    }

    public DividerView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public DividerView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public DividerView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
    }

    public void setup(SplitLayout splitLayout, SurfaceControlViewHost surfaceControlViewHost) {
        this.mSplitLayout = splitLayout;
        this.mViewHost = surfaceControlViewHost;
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.mHandle = (DividerHandleView) findViewById(R.id.docked_divider_handle);
        this.mBackground = findViewById(R.id.docked_divider_background);
        this.mTouchElevation = getResources().getDimensionPixelSize(R.dimen.docked_stack_divider_lift_elevation);
        this.mDoubleTapDetector = new GestureDetector(getContext(), new DoubleTapListener());
        this.mInteractive = true;
        setOnTouchListener(this);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0033, code lost:
        if (r6 != 3) goto L_0x00af;
     */
    @Override // android.view.View.OnTouchListener
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean onTouch(android.view.View r6, android.view.MotionEvent r7) {
        /*
            r5 = this;
            com.android.wm.shell.common.split.SplitLayout r6 = r5.mSplitLayout
            r0 = 0
            if (r6 == 0) goto L_0x00b0
            boolean r6 = r5.mInteractive
            if (r6 != 0) goto L_0x000b
            goto L_0x00b0
        L_0x000b:
            android.view.GestureDetector r6 = r5.mDoubleTapDetector
            boolean r6 = r6.onTouchEvent(r7)
            r1 = 1
            if (r6 == 0) goto L_0x0015
            return r1
        L_0x0015:
            int r6 = r7.getAction()
            r6 = r6 & 255(0xff, float:3.57E-43)
            boolean r2 = r5.isLandscape()
            if (r2 == 0) goto L_0x0026
            float r3 = r7.getRawX()
            goto L_0x002a
        L_0x0026:
            float r3 = r7.getRawY()
        L_0x002a:
            int r3 = (int) r3
            if (r6 == 0) goto L_0x009f
            if (r6 == r1) goto L_0x0064
            r4 = 2
            if (r6 == r4) goto L_0x0037
            r4 = 3
            if (r6 == r4) goto L_0x0064
            goto L_0x00af
        L_0x0037:
            android.view.VelocityTracker r6 = r5.mVelocityTracker
            r6.addMovement(r7)
            boolean r6 = r5.mMoving
            if (r6 != 0) goto L_0x0050
            int r6 = r5.mStartPos
            int r6 = r3 - r6
            int r6 = java.lang.Math.abs(r6)
            int r7 = r5.mTouchSlop
            if (r6 <= r7) goto L_0x0050
            r5.mStartPos = r3
            r5.mMoving = r1
        L_0x0050:
            boolean r6 = r5.mMoving
            if (r6 == 0) goto L_0x00af
            com.android.wm.shell.common.split.SplitLayout r6 = r5.mSplitLayout
            int r6 = r6.getDividePosition()
            int r6 = r6 + r3
            int r7 = r5.mStartPos
            int r6 = r6 - r7
            com.android.wm.shell.common.split.SplitLayout r5 = r5.mSplitLayout
            r5.updateDivideBounds(r6)
            goto L_0x00af
        L_0x0064:
            r5.releaseTouching()
            boolean r6 = r5.mMoving
            if (r6 != 0) goto L_0x006c
            goto L_0x00af
        L_0x006c:
            android.view.VelocityTracker r6 = r5.mVelocityTracker
            r6.addMovement(r7)
            android.view.VelocityTracker r6 = r5.mVelocityTracker
            r7 = 1000(0x3e8, float:1.401E-42)
            r6.computeCurrentVelocity(r7)
            if (r2 == 0) goto L_0x0081
            android.view.VelocityTracker r6 = r5.mVelocityTracker
            float r6 = r6.getXVelocity()
            goto L_0x0087
        L_0x0081:
            android.view.VelocityTracker r6 = r5.mVelocityTracker
            float r6 = r6.getYVelocity()
        L_0x0087:
            com.android.wm.shell.common.split.SplitLayout r7 = r5.mSplitLayout
            int r7 = r7.getDividePosition()
            int r7 = r7 + r3
            int r2 = r5.mStartPos
            int r7 = r7 - r2
            com.android.wm.shell.common.split.SplitLayout r2 = r5.mSplitLayout
            com.android.internal.policy.DividerSnapAlgorithm$SnapTarget r6 = r2.findSnapTarget(r7, r6, r0)
            com.android.wm.shell.common.split.SplitLayout r2 = r5.mSplitLayout
            r2.snapToTarget(r7, r6)
            r5.mMoving = r0
            goto L_0x00af
        L_0x009f:
            android.view.VelocityTracker r6 = android.view.VelocityTracker.obtain()
            r5.mVelocityTracker = r6
            r6.addMovement(r7)
            r5.setTouching()
            r5.mStartPos = r3
            r5.mMoving = r0
        L_0x00af:
            return r1
        L_0x00b0:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wm.shell.common.split.DividerView.onTouch(android.view.View, android.view.MotionEvent):boolean");
    }

    private void setTouching() {
        setSlippery(false);
        this.mHandle.setTouching(true, true);
        if (isLandscape()) {
            this.mBackground.animate().scaleX(1.4f);
        } else {
            this.mBackground.animate().scaleY(1.4f);
        }
        ViewPropertyAnimator animate = this.mBackground.animate();
        Interpolator interpolator = Interpolators.TOUCH_RESPONSE;
        animate.setInterpolator(interpolator).setDuration(150).translationZ((float) this.mTouchElevation).start();
        this.mHandle.animate().setInterpolator(interpolator).setDuration(150).translationZ((float) this.mTouchElevation).start();
    }

    private void releaseTouching() {
        setSlippery(true);
        this.mHandle.setTouching(false, true);
        ViewPropertyAnimator animate = this.mBackground.animate();
        Interpolator interpolator = Interpolators.FAST_OUT_SLOW_IN;
        animate.setInterpolator(interpolator).setDuration(200).translationZ(0.0f).scaleX(1.0f).scaleY(1.0f).start();
        this.mHandle.animate().setInterpolator(interpolator).setDuration(200).translationZ(0.0f).start();
    }

    private void setSlippery(boolean z) {
        if (this.mViewHost != null) {
            WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) getLayoutParams();
            int i = layoutParams.flags;
            if (((i & 536870912) != 0) != z) {
                if (z) {
                    layoutParams.flags = i | 536870912;
                } else {
                    layoutParams.flags = -536870913 & i;
                }
                this.mViewHost.relayout(layoutParams);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void setInteractive(boolean z) {
        if (z != this.mInteractive) {
            this.mInteractive = z;
            releaseTouching();
            this.mHandle.setVisibility(this.mInteractive ? 0 : 4);
        }
    }

    private boolean isLandscape() {
        return getResources().getConfiguration().orientation == 2;
    }

    /* loaded from: classes2.dex */
    private class DoubleTapListener extends GestureDetector.SimpleOnGestureListener {
        private DoubleTapListener() {
        }

        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnDoubleTapListener
        public boolean onDoubleTap(MotionEvent motionEvent) {
            if (DividerView.this.mSplitLayout == null) {
                return true;
            }
            DividerView.this.mSplitLayout.onDoubleTappedDivider();
            return true;
        }
    }
}
