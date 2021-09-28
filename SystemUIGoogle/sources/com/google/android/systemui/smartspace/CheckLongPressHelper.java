package com.google.android.systemui.smartspace;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
/* loaded from: classes2.dex */
public class CheckLongPressHelper {
    private boolean mHasPerformedLongPress;
    private Runnable mPendingCheckForLongPress;
    private final float mSlop;
    private final View mView;

    public CheckLongPressHelper(View view) {
        this.mView = view;
        this.mSlop = (float) ViewConfiguration.get(view.getContext()).getScaledTouchSlop();
    }

    public void onTouchEvent(MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        if (action != 0) {
            if (action != 1) {
                if (action != 2) {
                    if (action != 3) {
                        return;
                    }
                } else if (!pointInView(this.mView, motionEvent.getX(), motionEvent.getY(), this.mSlop)) {
                    cancelLongPress();
                    return;
                } else if (this.mPendingCheckForLongPress != null && isStylusButtonPressed(motionEvent)) {
                    triggerLongPress();
                    return;
                } else {
                    return;
                }
            }
            cancelLongPress();
            return;
        }
        cancelLongPress();
        postCheckForLongPress();
        if (isStylusButtonPressed(motionEvent)) {
            triggerLongPress();
        }
    }

    private static boolean pointInView(View view, float f, float f2, float f3) {
        float f4 = -f3;
        return f >= f4 && f2 >= f4 && f < ((float) view.getWidth()) + f3 && f2 < ((float) view.getHeight()) + f3;
    }

    private void postCheckForLongPress() {
        this.mHasPerformedLongPress = false;
        if (this.mPendingCheckForLongPress == null) {
            this.mPendingCheckForLongPress = new Runnable() { // from class: com.google.android.systemui.smartspace.CheckLongPressHelper$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    CheckLongPressHelper.$r8$lambda$_QIqspMb5KlwEKtDwjeBhdQgh2c(CheckLongPressHelper.this);
                }
            };
        }
        this.mView.postDelayed(this.mPendingCheckForLongPress, (long) ViewConfiguration.getLongPressTimeout());
    }

    public void cancelLongPress() {
        this.mHasPerformedLongPress = false;
        clearCallbacks();
    }

    public boolean hasPerformedLongPress() {
        return this.mHasPerformedLongPress;
    }

    /* access modifiers changed from: private */
    public void triggerLongPress() {
        if (this.mView.getParent() != null && this.mView.hasWindowFocus() && !this.mView.isPressed() && !this.mHasPerformedLongPress) {
            if (this.mView.performLongClick()) {
                this.mView.setPressed(false);
                this.mHasPerformedLongPress = true;
            }
            clearCallbacks();
        }
    }

    private void clearCallbacks() {
        Runnable runnable = this.mPendingCheckForLongPress;
        if (runnable != null) {
            this.mView.removeCallbacks(runnable);
            this.mPendingCheckForLongPress = null;
        }
    }

    private static boolean isStylusButtonPressed(MotionEvent motionEvent) {
        if (motionEvent.getToolType(0) != 2 || !motionEvent.isButtonPressed(2)) {
            return false;
        }
        return true;
    }
}
