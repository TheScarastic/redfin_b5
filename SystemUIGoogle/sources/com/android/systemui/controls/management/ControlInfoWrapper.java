package com.android.systemui.controls.management;

import android.content.ComponentName;
import android.graphics.drawable.Icon;
import com.android.systemui.controls.ControlInterface;
import com.android.systemui.controls.controller.ControlInfo;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ControlsModel.kt */
/* loaded from: classes.dex */
public final class ControlInfoWrapper extends ElementWrapper implements ControlInterface {
    private final ComponentName component;
    private final ControlInfo controlInfo;
    private Function2<? super ComponentName, ? super String, Icon> customIconGetter;
    private boolean favorite;

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ControlInfoWrapper)) {
            return false;
        }
        ControlInfoWrapper controlInfoWrapper = (ControlInfoWrapper) obj;
        return Intrinsics.areEqual(getComponent(), controlInfoWrapper.getComponent()) && Intrinsics.areEqual(this.controlInfo, controlInfoWrapper.controlInfo) && getFavorite() == controlInfoWrapper.getFavorite();
    }

    public int hashCode() {
        int hashCode = ((getComponent().hashCode() * 31) + this.controlInfo.hashCode()) * 31;
        boolean favorite = getFavorite();
        if (favorite) {
            favorite = true;
        }
        int i = favorite ? 1 : 0;
        int i2 = favorite ? 1 : 0;
        int i3 = favorite ? 1 : 0;
        return hashCode + i;
    }

    public String toString() {
        return "ControlInfoWrapper(component=" + getComponent() + ", controlInfo=" + this.controlInfo + ", favorite=" + getFavorite() + ')';
    }

    @Override // com.android.systemui.controls.ControlInterface
    public boolean getRemoved() {
        return ControlInterface.DefaultImpls.getRemoved(this);
    }

    @Override // com.android.systemui.controls.ControlInterface
    public ComponentName getComponent() {
        return this.component;
    }

    public final ControlInfo getControlInfo() {
        return this.controlInfo;
    }

    @Override // com.android.systemui.controls.ControlInterface
    public boolean getFavorite() {
        return this.favorite;
    }

    public void setFavorite(boolean z) {
        this.favorite = z;
    }

    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public ControlInfoWrapper(ComponentName componentName, ControlInfo controlInfo, boolean z) {
        super(null);
        Intrinsics.checkNotNullParameter(componentName, "component");
        Intrinsics.checkNotNullParameter(controlInfo, "controlInfo");
        this.component = componentName;
        this.controlInfo = controlInfo;
        this.favorite = z;
        this.customIconGetter = ControlInfoWrapper$customIconGetter$1.INSTANCE;
    }

    /* JADX INFO: 'this' call moved to the top of the method (can break code semantics) */
    public ControlInfoWrapper(ComponentName componentName, ControlInfo controlInfo, boolean z, Function2<? super ComponentName, ? super String, Icon> function2) {
        this(componentName, controlInfo, z);
        Intrinsics.checkNotNullParameter(componentName, "component");
        Intrinsics.checkNotNullParameter(controlInfo, "controlInfo");
        Intrinsics.checkNotNullParameter(function2, "customIconGetter");
        this.customIconGetter = function2;
    }

    @Override // com.android.systemui.controls.ControlInterface
    public String getControlId() {
        return this.controlInfo.getControlId();
    }

    @Override // com.android.systemui.controls.ControlInterface
    public CharSequence getTitle() {
        return this.controlInfo.getControlTitle();
    }

    @Override // com.android.systemui.controls.ControlInterface
    public CharSequence getSubtitle() {
        return this.controlInfo.getControlSubtitle();
    }

    @Override // com.android.systemui.controls.ControlInterface
    public int getDeviceType() {
        return this.controlInfo.getDeviceType();
    }

    @Override // com.android.systemui.controls.ControlInterface
    public Icon getCustomIcon() {
        return this.customIconGetter.invoke(getComponent(), getControlId());
    }
}
