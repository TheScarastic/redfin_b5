package com.android.systemui.controls.management;

import android.content.ComponentName;
import android.graphics.drawable.Icon;
import com.android.systemui.controls.ControlInterface;
import com.android.systemui.controls.ControlStatus;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ControlsModel.kt */
/* loaded from: classes.dex */
public final class ControlStatusWrapper extends ElementWrapper implements ControlInterface {
    private final ControlStatus controlStatus;

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return (obj instanceof ControlStatusWrapper) && Intrinsics.areEqual(this.controlStatus, ((ControlStatusWrapper) obj).controlStatus);
    }

    @Override // com.android.systemui.controls.ControlInterface
    public ComponentName getComponent() {
        return this.controlStatus.getComponent();
    }

    @Override // com.android.systemui.controls.ControlInterface
    public String getControlId() {
        return this.controlStatus.getControlId();
    }

    @Override // com.android.systemui.controls.ControlInterface
    public Icon getCustomIcon() {
        return this.controlStatus.getCustomIcon();
    }

    @Override // com.android.systemui.controls.ControlInterface
    public int getDeviceType() {
        return this.controlStatus.getDeviceType();
    }

    @Override // com.android.systemui.controls.ControlInterface
    public boolean getFavorite() {
        return this.controlStatus.getFavorite();
    }

    @Override // com.android.systemui.controls.ControlInterface
    public boolean getRemoved() {
        return this.controlStatus.getRemoved();
    }

    @Override // com.android.systemui.controls.ControlInterface
    public CharSequence getSubtitle() {
        return this.controlStatus.getSubtitle();
    }

    @Override // com.android.systemui.controls.ControlInterface
    public CharSequence getTitle() {
        return this.controlStatus.getTitle();
    }

    public int hashCode() {
        return this.controlStatus.hashCode();
    }

    public String toString() {
        return "ControlStatusWrapper(controlStatus=" + this.controlStatus + ')';
    }

    public final ControlStatus getControlStatus() {
        return this.controlStatus;
    }

    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public ControlStatusWrapper(ControlStatus controlStatus) {
        super(null);
        Intrinsics.checkNotNullParameter(controlStatus, "controlStatus");
        this.controlStatus = controlStatus;
    }
}
