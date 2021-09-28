package com.android.systemui.navigationbar.buttons;

import android.animation.ObjectAnimator;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.os.SystemClock;
import android.util.Slog;
import android.view.MotionEvent;
import com.android.systemui.Dependency;
import com.android.systemui.R$bool;
import com.android.systemui.R$dimen;
import com.android.systemui.R$integer;
import com.android.systemui.navigationbar.NavigationBarController;
import com.android.systemui.navigationbar.NavigationBarView;
/* loaded from: classes.dex */
public class DeadZone {
    private int mDecay;
    private final int mDisplayId;
    private int mDisplayRotation;
    private int mHold;
    private long mLastPokeTime;
    private final NavigationBarView mNavigationBarView;
    private boolean mShouldFlash;
    private int mSizeMax;
    private int mSizeMin;
    private boolean mVertical;
    private float mFlashFrac = 0.0f;
    private final Runnable mDebugFlash = new Runnable() { // from class: com.android.systemui.navigationbar.buttons.DeadZone.1
        @Override // java.lang.Runnable
        public void run() {
            ObjectAnimator.ofFloat(DeadZone.this, "flash", 1.0f, 0.0f).setDuration(150L).start();
        }
    };
    private final NavigationBarController mNavBarController = (NavigationBarController) Dependency.get(NavigationBarController.class);

    static float lerp(float f, float f2, float f3) {
        return ((f2 - f) * f3) + f;
    }

    public DeadZone(NavigationBarView navigationBarView) {
        this.mNavigationBarView = navigationBarView;
        this.mDisplayId = navigationBarView.getContext().getDisplayId();
        onConfigurationChanged(0);
    }

    private float getSize(long j) {
        int lerp;
        int i = this.mSizeMax;
        if (i == 0) {
            return 0.0f;
        }
        long j2 = j - this.mLastPokeTime;
        int i2 = this.mHold;
        int i3 = this.mDecay;
        if (j2 > ((long) (i2 + i3))) {
            lerp = this.mSizeMin;
        } else if (j2 < ((long) i2)) {
            return (float) i;
        } else {
            lerp = (int) lerp((float) i, (float) this.mSizeMin, ((float) (j2 - ((long) i2))) / ((float) i3));
        }
        return (float) lerp;
    }

    public void setFlashOnTouchCapture(boolean z) {
        this.mShouldFlash = z;
        this.mFlashFrac = 0.0f;
        this.mNavigationBarView.postInvalidate();
    }

    public void onConfigurationChanged(int i) {
        this.mDisplayRotation = i;
        Resources resources = this.mNavigationBarView.getResources();
        this.mHold = resources.getInteger(R$integer.navigation_bar_deadzone_hold);
        this.mDecay = resources.getInteger(R$integer.navigation_bar_deadzone_decay);
        this.mSizeMin = resources.getDimensionPixelSize(R$dimen.navigation_bar_deadzone_size);
        this.mSizeMax = resources.getDimensionPixelSize(R$dimen.navigation_bar_deadzone_size_max);
        boolean z = true;
        if (resources.getInteger(R$integer.navigation_bar_deadzone_orientation) != 1) {
            z = false;
        }
        this.mVertical = z;
        setFlashOnTouchCapture(resources.getBoolean(R$bool.config_dead_zone_flash));
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getToolType(0) == 3) {
            return false;
        }
        int action = motionEvent.getAction();
        if (action == 4) {
            poke(motionEvent);
            return true;
        }
        if (action == 0) {
            this.mNavBarController.touchAutoDim(this.mDisplayId);
            int size = (int) getSize(motionEvent.getEventTime());
            if (!this.mVertical ? motionEvent.getY() < ((float) size) : !(this.mDisplayRotation != 3 ? motionEvent.getX() >= ((float) size) : motionEvent.getX() <= ((float) (this.mNavigationBarView.getWidth() - size)))) {
                Slog.v("DeadZone", "consuming errant click: (" + motionEvent.getX() + "," + motionEvent.getY() + ")");
                if (this.mShouldFlash) {
                    this.mNavigationBarView.post(this.mDebugFlash);
                    this.mNavigationBarView.postInvalidate();
                }
                return true;
            }
        }
        return false;
    }

    private void poke(MotionEvent motionEvent) {
        this.mLastPokeTime = motionEvent.getEventTime();
        if (this.mShouldFlash) {
            this.mNavigationBarView.postInvalidate();
        }
    }

    public void onDraw(Canvas canvas) {
        if (this.mShouldFlash && this.mFlashFrac > 0.0f) {
            int size = (int) getSize(SystemClock.uptimeMillis());
            if (!this.mVertical) {
                canvas.clipRect(0, 0, canvas.getWidth(), size);
            } else if (this.mDisplayRotation == 3) {
                canvas.clipRect(canvas.getWidth() - size, 0, canvas.getWidth(), canvas.getHeight());
            } else {
                canvas.clipRect(0, 0, size, canvas.getHeight());
            }
            canvas.drawARGB((int) (this.mFlashFrac * 255.0f), 221, 238, 170);
        }
    }
}
