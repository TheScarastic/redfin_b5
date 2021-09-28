package com.google.android.systemui.columbus.gates;

import com.google.android.systemui.columbus.actions.Action;
import java.util.Iterator;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: CameraVisibility.kt */
/* loaded from: classes2.dex */
public final class CameraVisibility$actionListener$1 implements Action.Listener {
    final /* synthetic */ CameraVisibility this$0;

    /* access modifiers changed from: package-private */
    public CameraVisibility$actionListener$1(CameraVisibility cameraVisibility) {
        this.this$0 = cameraVisibility;
    }

    @Override // com.google.android.systemui.columbus.actions.Action.Listener
    public void onActionAvailabilityChanged(Action action) {
        Object obj;
        Intrinsics.checkNotNullParameter(action, "action");
        CameraVisibility cameraVisibility = this.this$0;
        Iterator it = cameraVisibility.exceptions.iterator();
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
        cameraVisibility.exceptionActive = obj != null;
        this.this$0.updateBlocking();
    }
}
