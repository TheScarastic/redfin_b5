package com.android.keyguard;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Button;
import com.android.internal.util.EmergencyAffordanceManager;
import com.android.internal.widget.LockPatternUtils;
import com.android.settingslib.Utils;
import com.android.systemui.R$drawable;
/* loaded from: classes.dex */
public class EmergencyButton extends Button {
    private int mDownX;
    private int mDownY;
    private final EmergencyAffordanceManager mEmergencyAffordanceManager;
    private final boolean mEnableEmergencyCallWhileSimLocked;
    private LockPatternUtils mLockPatternUtils;
    private boolean mLongPressWasDragged;

    public EmergencyButton(Context context) {
        this(context, null);
    }

    public EmergencyButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mEnableEmergencyCallWhileSimLocked = ((Button) this).mContext.getResources().getBoolean(17891553);
        this.mEmergencyAffordanceManager = new EmergencyAffordanceManager(context);
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.mLockPatternUtils = new LockPatternUtils(((Button) this).mContext);
        if (this.mEmergencyAffordanceManager.needsEmergencyAffordance()) {
            setOnLongClickListener(new View.OnLongClickListener() { // from class: com.android.keyguard.EmergencyButton$$ExternalSyntheticLambda0
                @Override // android.view.View.OnLongClickListener
                public final boolean onLongClick(View view) {
                    return EmergencyButton.m6$r8$lambda$2aGPXZbJN74ysWEksvXezI9mJQ(EmergencyButton.this, view);
                }
            });
        }
    }

    public /* synthetic */ boolean lambda$onFinishInflate$0(View view) {
        if (this.mLongPressWasDragged || !this.mEmergencyAffordanceManager.needsEmergencyAffordance()) {
            return false;
        }
        this.mEmergencyAffordanceManager.performEmergencyCall();
        return true;
    }

    @Override // android.widget.TextView, android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        int x = (int) motionEvent.getX();
        int y = (int) motionEvent.getY();
        if (motionEvent.getActionMasked() == 0) {
            this.mDownX = x;
            this.mDownY = y;
            this.mLongPressWasDragged = false;
        } else {
            int abs = Math.abs(x - this.mDownX);
            int abs2 = Math.abs(y - this.mDownY);
            int scaledTouchSlop = ViewConfiguration.get(((Button) this).mContext).getScaledTouchSlop();
            if (Math.abs(abs2) > scaledTouchSlop || Math.abs(abs) > scaledTouchSlop) {
                this.mLongPressWasDragged = true;
            }
        }
        return super.onTouchEvent(motionEvent);
    }

    public void reloadColors() {
        setTextColor(Utils.getColorAttrDefaultColor(getContext(), 16842809));
        setBackground(getContext().getDrawable(R$drawable.kg_emergency_button_background));
    }

    @Override // android.widget.TextView, android.view.View
    public boolean performLongClick() {
        return super.performLongClick();
    }

    public void updateEmergencyCallButton(boolean z, boolean z2, boolean z3) {
        boolean z4;
        if (z2) {
            z4 = z ? true : z3 ? this.mEnableEmergencyCallWhileSimLocked : this.mLockPatternUtils.isSecure(KeyguardUpdateMonitor.getCurrentUser());
        } else {
            z4 = false;
        }
        if (z4) {
            setVisibility(0);
            setText(z ? 17040549 : 17040522);
            return;
        }
        setVisibility(8);
    }
}
