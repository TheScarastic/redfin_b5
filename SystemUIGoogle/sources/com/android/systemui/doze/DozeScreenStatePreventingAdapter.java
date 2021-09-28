package com.android.systemui.doze;

import com.android.systemui.doze.DozeMachine;
import com.android.systemui.statusbar.phone.DozeParameters;
/* loaded from: classes.dex */
public class DozeScreenStatePreventingAdapter extends DozeMachine.Service.Delegate {
    DozeScreenStatePreventingAdapter(DozeMachine.Service service) {
        super(service);
    }

    @Override // com.android.systemui.doze.DozeMachine.Service.Delegate, com.android.systemui.doze.DozeMachine.Service
    public void setDozeScreenState(int i) {
        if (i == 3) {
            i = 2;
        } else if (i == 4) {
            i = 6;
        }
        super.setDozeScreenState(i);
    }

    public static DozeMachine.Service wrapIfNeeded(DozeMachine.Service service, DozeParameters dozeParameters) {
        return isNeeded(dozeParameters) ? new DozeScreenStatePreventingAdapter(service) : service;
    }

    private static boolean isNeeded(DozeParameters dozeParameters) {
        return !dozeParameters.getDisplayStateSupported();
    }
}
