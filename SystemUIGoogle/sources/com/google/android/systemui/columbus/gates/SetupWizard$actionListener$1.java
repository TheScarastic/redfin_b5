package com.google.android.systemui.columbus.gates;

import com.google.android.systemui.columbus.actions.Action;
import java.util.Iterator;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: SetupWizard.kt */
/* loaded from: classes2.dex */
public final class SetupWizard$actionListener$1 implements Action.Listener {
    final /* synthetic */ SetupWizard this$0;

    /* access modifiers changed from: package-private */
    public SetupWizard$actionListener$1(SetupWizard setupWizard) {
        this.this$0 = setupWizard;
    }

    @Override // com.google.android.systemui.columbus.actions.Action.Listener
    public void onActionAvailabilityChanged(Action action) {
        Object obj;
        Intrinsics.checkNotNullParameter(action, "action");
        SetupWizard setupWizard = this.this$0;
        Iterator it = setupWizard.exceptions.iterator();
        while (true) {
            if (!it.hasNext()) {
                obj = null;
                break;
            }
            obj = it.next();
            if (((Action) obj).isAvailable()) {
                break;
            }
        }
        setupWizard.exceptionActive = obj != null;
        this.this$0.updateBlocking();
    }
}
