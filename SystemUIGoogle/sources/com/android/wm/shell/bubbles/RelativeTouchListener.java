package com.android.wm.shell.bubbles;

import android.graphics.PointF;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: RelativeTouchListener.kt */
/* loaded from: classes2.dex */
public abstract class RelativeTouchListener implements View.OnTouchListener {
    private boolean movedEnough;
    private boolean performedLongClick;
    private final PointF touchDown = new PointF();
    private final PointF viewPositionOnTouchDown = new PointF();
    private final VelocityTracker velocityTracker = VelocityTracker.obtain();
    private int touchSlop = -1;

    public abstract boolean onDown(View view, MotionEvent motionEvent);

    public abstract void onMove(View view, MotionEvent motionEvent, float f, float f2, float f3, float f4);

    public abstract void onUp(View view, MotionEvent motionEvent, float f, float f2, float f3, float f4, float f5, float f6);

    @Override // android.view.View.OnTouchListener
    public boolean onTouch(View view, MotionEvent motionEvent) {
        Intrinsics.checkNotNullParameter(view, "v");
        Intrinsics.checkNotNullParameter(motionEvent, "ev");
        addMovement(motionEvent);
        float rawX = motionEvent.getRawX() - this.touchDown.x;
        float rawY = motionEvent.getRawY() - this.touchDown.y;
        int action = motionEvent.getAction();
        if (action != 0) {
            if (action == 1) {
                if (this.movedEnough) {
                    this.velocityTracker.computeCurrentVelocity(1000);
                    PointF pointF = this.viewPositionOnTouchDown;
                    onUp(view, motionEvent, pointF.x, pointF.y, rawX, rawY, this.velocityTracker.getXVelocity(), this.velocityTracker.getYVelocity());
                } else if (!this.performedLongClick) {
                    view.performClick();
                } else {
                    view.getHandler().removeCallbacksAndMessages(null);
                }
                this.velocityTracker.clear();
                this.movedEnough = false;
            } else if (action == 2) {
                if (!this.movedEnough && ((float) Math.hypot((double) rawX, (double) rawY)) > ((float) this.touchSlop) && !this.performedLongClick) {
                    this.movedEnough = true;
                    view.getHandler().removeCallbacksAndMessages(null);
                }
                if (this.movedEnough) {
                    PointF pointF2 = this.viewPositionOnTouchDown;
                    onMove(view, motionEvent, pointF2.x, pointF2.y, rawX, rawY);
                }
            }
        } else if (!onDown(view, motionEvent)) {
            return false;
        } else {
            this.touchSlop = ViewConfiguration.get(view.getContext()).getScaledTouchSlop();
            this.touchDown.set(motionEvent.getRawX(), motionEvent.getRawY());
            this.viewPositionOnTouchDown.set(view.getTranslationX(), view.getTranslationY());
            this.performedLongClick = false;
            view.getHandler().postDelayed(new Runnable(view, this) { // from class: com.android.wm.shell.bubbles.RelativeTouchListener$onTouch$1
                final /* synthetic */ View $v;
                final /* synthetic */ RelativeTouchListener this$0;

                /* access modifiers changed from: package-private */
                {
                    this.$v = r1;
                    this.this$0 = r2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    if (this.$v.isLongClickable()) {
                        RelativeTouchListener.access$setPerformedLongClick$p(this.this$0, this.$v.performLongClick());
                    }
                }
            }, (long) ViewConfiguration.getLongPressTimeout());
        }
        return true;
    }

    private final void addMovement(MotionEvent motionEvent) {
        float rawX = motionEvent.getRawX() - motionEvent.getX();
        float rawY = motionEvent.getRawY() - motionEvent.getY();
        motionEvent.offsetLocation(rawX, rawY);
        this.velocityTracker.addMovement(motionEvent);
        motionEvent.offsetLocation(-rawX, -rawY);
    }
}
