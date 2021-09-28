package com.android.systemui.doze.dagger;

import com.android.systemui.doze.DozeMachine;
/* loaded from: classes.dex */
public interface DozeComponent {

    /* loaded from: classes.dex */
    public interface Builder {
        DozeComponent build(DozeMachine.Service service);
    }

    DozeMachine getDozeMachine();
}
