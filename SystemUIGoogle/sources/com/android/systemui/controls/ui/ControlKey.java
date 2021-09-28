package com.android.systemui.controls.ui;

import android.content.ComponentName;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ControlsUiControllerImpl.kt */
/* access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class ControlKey {
    private final ComponentName componentName;
    private final String controlId;

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ControlKey)) {
            return false;
        }
        ControlKey controlKey = (ControlKey) obj;
        return Intrinsics.areEqual(this.componentName, controlKey.componentName) && Intrinsics.areEqual(this.controlId, controlKey.controlId);
    }

    public int hashCode() {
        return (this.componentName.hashCode() * 31) + this.controlId.hashCode();
    }

    public String toString() {
        return "ControlKey(componentName=" + this.componentName + ", controlId=" + this.controlId + ')';
    }

    public ControlKey(ComponentName componentName, String str) {
        Intrinsics.checkNotNullParameter(componentName, "componentName");
        Intrinsics.checkNotNullParameter(str, "controlId");
        this.componentName = componentName;
        this.controlId = str;
    }
}
