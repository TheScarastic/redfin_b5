package com.android.systemui.controls.management;

import kotlin.jvm.internal.Intrinsics;
/* compiled from: ControlsModel.kt */
/* loaded from: classes.dex */
public final class ZoneNameWrapper extends ElementWrapper {
    private final CharSequence zoneName;

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return (obj instanceof ZoneNameWrapper) && Intrinsics.areEqual(this.zoneName, ((ZoneNameWrapper) obj).zoneName);
    }

    public int hashCode() {
        return this.zoneName.hashCode();
    }

    public String toString() {
        return "ZoneNameWrapper(zoneName=" + ((Object) this.zoneName) + ')';
    }

    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public ZoneNameWrapper(CharSequence charSequence) {
        super(null);
        Intrinsics.checkNotNullParameter(charSequence, "zoneName");
        this.zoneName = charSequence;
    }

    public final CharSequence getZoneName() {
        return this.zoneName;
    }
}
