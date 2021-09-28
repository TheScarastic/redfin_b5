package com.android.systemui.doze;

import com.android.systemui.doze.DozeMachine;
import com.android.systemui.statusbar.phone.DozeParameters;
/* loaded from: classes.dex */
public class DozeSuspendScreenStatePreventingAdapter extends DozeMachine.Service.Delegate {
    DozeSuspendScreenStatePreventingAdapter(DozeMachine.Service service) {
        super(service);
    }

    @Override // com.android.systemui.doze.DozeMachine.Service.Delegate, com.android.systemui.doze.DozeMachine.Service
    public void setDozeScreenState(int i) {
        if (i == 4) {
            i = 3;
        }
        super.setDozeScreenState(i);
    }

    public static DozeMachine.Service wrapIfNeeded(DozeMachine.Service service, DozeParameters dozeParameters) {
        return isNeeded(dozeParameters) ? new DozeSuspendScreenStatePreventingAdapter(service) : service;
    }

    private static boolean isNeeded(DozeParameters dozeParameters) {
        return !dozeParameters.getDozeSuspendDisplayStateSupported();
    }
}
