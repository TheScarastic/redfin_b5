package com.google.android.systemui.columbus.gates;

import android.content.Context;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import com.google.android.systemui.columbus.actions.Action;
import dagger.Lazy;
import java.util.Set;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: SetupWizard.kt */
/* loaded from: classes2.dex */
public final class SetupWizard extends Gate {
    private boolean exceptionActive;
    private final Set<Action> exceptions;
    private final Lazy<DeviceProvisionedController> provisionedController;
    private boolean setupComplete;
    private final SetupWizard$provisionedListener$1 provisionedListener = new SetupWizard$provisionedListener$1(this);
    private final SetupWizard$actionListener$1 actionListener = new SetupWizard$actionListener$1(this);

    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public SetupWizard(Context context, Set<Action> set, Lazy<DeviceProvisionedController> lazy) {
        super(context, null, 2, null);
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(set, "exceptions");
        Intrinsics.checkNotNullParameter(lazy, "provisionedController");
        this.exceptions = set;
        this.provisionedController = lazy;
    }

    @Override // com.google.android.systemui.columbus.gates.Gate
    protected void onActivate() {
        this.exceptionActive = false;
        for (Action action : this.exceptions) {
            action.registerListener(this.actionListener);
            this.exceptionActive = action.isAvailable() | this.exceptionActive;
        }
        this.setupComplete = isSetupComplete();
        this.provisionedController.get().addCallback(this.provisionedListener);
        updateBlocking();
    }

    @Override // com.google.android.systemui.columbus.gates.Gate
    protected void onDeactivate() {
        this.provisionedController.get().removeCallback(this.provisionedListener);
    }

    /* access modifiers changed from: private */
    public final void updateBlocking() {
        setBlocking(!this.exceptionActive && !this.setupComplete);
    }

    /* access modifiers changed from: private */
    public final boolean isSetupComplete() {
        return this.provisionedController.get().isDeviceProvisioned() && this.provisionedController.get().isCurrentUserSetup();
    }

    @Override // com.google.android.systemui.columbus.gates.Gate
    public String toString() {
        return super.toString() + " [isDeviceProvisioned -> " + this.provisionedController.get().isDeviceProvisioned() + "; isCurrentUserSetup -> " + this.provisionedController.get().isCurrentUserSetup() + ']';
    }
}
