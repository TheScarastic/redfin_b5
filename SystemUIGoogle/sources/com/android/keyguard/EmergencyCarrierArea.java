package com.android.keyguard;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import com.android.systemui.R$id;
/* loaded from: classes.dex */
public class EmergencyCarrierArea extends AlphaOptimizedLinearLayout {
    private CarrierText mCarrierText;
    private EmergencyButton mEmergencyButton;

    public EmergencyCarrierArea(Context context) {
        super(context);
    }

    public EmergencyCarrierArea(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.mCarrierText = (CarrierText) findViewById(R$id.carrier_text);
        EmergencyButton emergencyButton = (EmergencyButton) findViewById(R$id.emergency_call_button);
        this.mEmergencyButton = emergencyButton;
        emergencyButton.setOnTouchListener(new View.OnTouchListener() { // from class: com.android.keyguard.EmergencyCarrierArea.1
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (EmergencyCarrierArea.this.mCarrierText.getVisibility() != 0) {
                    return false;
                }
                int action = motionEvent.getAction();
                if (action == 0) {
                    EmergencyCarrierArea.this.mCarrierText.animate().alpha(0.0f);
                } else if (action == 1) {
                    EmergencyCarrierArea.this.mCarrierText.animate().alpha(1.0f);
                }
                return false;
            }
        });
    }

    public void setCarrierTextVisible(boolean z) {
        this.mCarrierText.setVisibility(z ? 0 : 8);
    }
}
