package com.android.systemui.navigationbar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import com.android.systemui.navigationbar.buttons.DeadZone;
/* loaded from: classes.dex */
public class NavigationBarFrame extends FrameLayout {
    private DeadZone mDeadZone = null;

    public NavigationBarFrame(Context context) {
        super(context);
    }

    public NavigationBarFrame(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public NavigationBarFrame(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public void setDeadZone(DeadZone deadZone) {
        this.mDeadZone = deadZone;
    }

    @Override // android.view.View, android.view.ViewGroup
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        DeadZone deadZone;
        if (motionEvent.getAction() != 4 || (deadZone = this.mDeadZone) == null) {
            return super.dispatchTouchEvent(motionEvent);
        }
        return deadZone.onTouchEvent(motionEvent);
    }
}
