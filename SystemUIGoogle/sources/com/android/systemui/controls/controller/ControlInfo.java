package com.android.systemui.controls.controller;

import android.service.controls.Control;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ControlInfo.kt */
/* loaded from: classes.dex */
public final class ControlInfo {
    public static final Companion Companion = new Companion(null);
    private final String controlId;
    private final CharSequence controlSubtitle;
    private final CharSequence controlTitle;
    private final int deviceType;

    public static /* synthetic */ ControlInfo copy$default(ControlInfo controlInfo, String str, CharSequence charSequence, CharSequence charSequence2, int i, int i2, Object obj) {
        if ((i2 & 1) != 0) {
            str = controlInfo.controlId;
        }
        if ((i2 & 2) != 0) {
            charSequence = controlInfo.controlTitle;
        }
        if ((i2 & 4) != 0) {
            charSequence2 = controlInfo.controlSubtitle;
        }
        if ((i2 & 8) != 0) {
            i = controlInfo.deviceType;
        }
        return controlInfo.copy(str, charSequence, charSequence2, i);
    }

    public final ControlInfo copy(String str, CharSequence charSequence, CharSequence charSequence2, int i) {
        Intrinsics.checkNotNullParameter(str, "controlId");
        Intrinsics.checkNotNullParameter(charSequence, "controlTitle");
        Intrinsics.checkNotNullParameter(charSequence2, "controlSubtitle");
        return new ControlInfo(str, charSequence, charSequence2, i);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ControlInfo)) {
            return false;
        }
        ControlInfo controlInfo = (ControlInfo) obj;
        return Intrinsics.areEqual(this.controlId, controlInfo.controlId) && Intrinsics.areEqual(this.controlTitle, controlInfo.controlTitle) && Intrinsics.areEqual(this.controlSubtitle, controlInfo.controlSubtitle) && this.deviceType == controlInfo.deviceType;
    }

    public int hashCode() {
        return (((((this.controlId.hashCode() * 31) + this.controlTitle.hashCode()) * 31) + this.controlSubtitle.hashCode()) * 31) + Integer.hashCode(this.deviceType);
    }

    public ControlInfo(String str, CharSequence charSequence, CharSequence charSequence2, int i) {
        Intrinsics.checkNotNullParameter(str, "controlId");
        Intrinsics.checkNotNullParameter(charSequence, "controlTitle");
        Intrinsics.checkNotNullParameter(charSequence2, "controlSubtitle");
        this.controlId = str;
        this.controlTitle = charSequence;
        this.controlSubtitle = charSequence2;
        this.deviceType = i;
    }

    public final String getControlId() {
        return this.controlId;
    }

    public final CharSequence getControlTitle() {
        return this.controlTitle;
    }

    public final CharSequence getControlSubtitle() {
        return this.controlSubtitle;
    }

    public final int getDeviceType() {
        return this.deviceType;
    }

    /* compiled from: ControlInfo.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final ControlInfo fromControl(Control control) {
            Intrinsics.checkNotNullParameter(control, "control");
            String controlId = control.getControlId();
            Intrinsics.checkNotNullExpressionValue(controlId, "control.controlId");
            CharSequence title = control.getTitle();
            Intrinsics.checkNotNullExpressionValue(title, "control.title");
            CharSequence subtitle = control.getSubtitle();
            Intrinsics.checkNotNullExpressionValue(subtitle, "control.subtitle");
            return new ControlInfo(controlId, title, subtitle, control.getDeviceType());
        }
    }

    public String toString() {
        return ':' + this.controlId + ':' + ((Object) this.controlTitle) + ':' + this.deviceType;
    }
}
