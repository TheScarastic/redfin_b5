package com.android.systemui.shared.system.smartspace;

import com.android.systemui.shared.system.smartspace.ISmartspaceTransitionController;
/* compiled from: SmartspaceTransitionController.kt */
/* loaded from: classes.dex */
public final class SmartspaceTransitionController$ISmartspaceTransitionController$1 extends ISmartspaceTransitionController.Stub {
    final /* synthetic */ SmartspaceTransitionController this$0;

    /* access modifiers changed from: package-private */
    public SmartspaceTransitionController$ISmartspaceTransitionController$1(SmartspaceTransitionController smartspaceTransitionController) {
        this.this$0 = smartspaceTransitionController;
    }

    @Override // com.android.systemui.shared.system.smartspace.ISmartspaceTransitionController
    public void setSmartspace(ISmartspaceCallback iSmartspaceCallback) {
        this.this$0.setLauncherSmartspace(iSmartspaceCallback);
        this.this$0.updateLauncherSmartSpaceState();
    }
}
