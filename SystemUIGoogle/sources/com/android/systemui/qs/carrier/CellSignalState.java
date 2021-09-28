package com.android.systemui.qs.carrier;

import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: CellSignalState.kt */
/* loaded from: classes.dex */
public final class CellSignalState {
    public final String contentDescription;
    public final int mobileSignalIconId;
    public final boolean providerModelBehavior;
    public final boolean roaming;
    public final String typeContentDescription;
    public final boolean visible;

    public CellSignalState() {
        this(false, 0, null, null, false, false, 63, null);
    }

    public static /* synthetic */ CellSignalState copy$default(CellSignalState cellSignalState, boolean z, int i, String str, String str2, boolean z2, boolean z3, int i2, Object obj) {
        if ((i2 & 1) != 0) {
            z = cellSignalState.visible;
        }
        if ((i2 & 2) != 0) {
            i = cellSignalState.mobileSignalIconId;
        }
        if ((i2 & 4) != 0) {
            str = cellSignalState.contentDescription;
        }
        if ((i2 & 8) != 0) {
            str2 = cellSignalState.typeContentDescription;
        }
        if ((i2 & 16) != 0) {
            z2 = cellSignalState.roaming;
        }
        if ((i2 & 32) != 0) {
            z3 = cellSignalState.providerModelBehavior;
        }
        return cellSignalState.copy(z, i, str, str2, z2, z3);
    }

    public final CellSignalState copy(boolean z, int i, String str, String str2, boolean z2, boolean z3) {
        return new CellSignalState(z, i, str, str2, z2, z3);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof CellSignalState)) {
            return false;
        }
        CellSignalState cellSignalState = (CellSignalState) obj;
        return this.visible == cellSignalState.visible && this.mobileSignalIconId == cellSignalState.mobileSignalIconId && Intrinsics.areEqual(this.contentDescription, cellSignalState.contentDescription) && Intrinsics.areEqual(this.typeContentDescription, cellSignalState.typeContentDescription) && this.roaming == cellSignalState.roaming && this.providerModelBehavior == cellSignalState.providerModelBehavior;
    }

    public int hashCode() {
        boolean z = this.visible;
        int i = 1;
        if (z) {
            z = true;
        }
        int i2 = z ? 1 : 0;
        int i3 = z ? 1 : 0;
        int i4 = z ? 1 : 0;
        int hashCode = ((i2 * 31) + Integer.hashCode(this.mobileSignalIconId)) * 31;
        String str = this.contentDescription;
        int i5 = 0;
        int hashCode2 = (hashCode + (str == null ? 0 : str.hashCode())) * 31;
        String str2 = this.typeContentDescription;
        if (str2 != null) {
            i5 = str2.hashCode();
        }
        int i6 = (hashCode2 + i5) * 31;
        boolean z2 = this.roaming;
        if (z2) {
            z2 = true;
        }
        int i7 = z2 ? 1 : 0;
        int i8 = z2 ? 1 : 0;
        int i9 = z2 ? 1 : 0;
        int i10 = (i6 + i7) * 31;
        boolean z3 = this.providerModelBehavior;
        if (!z3) {
            i = z3 ? 1 : 0;
        }
        return i10 + i;
    }

    public String toString() {
        return "CellSignalState(visible=" + this.visible + ", mobileSignalIconId=" + this.mobileSignalIconId + ", contentDescription=" + ((Object) this.contentDescription) + ", typeContentDescription=" + ((Object) this.typeContentDescription) + ", roaming=" + this.roaming + ", providerModelBehavior=" + this.providerModelBehavior + ')';
    }

    public CellSignalState(boolean z, int i, String str, String str2, boolean z2, boolean z3) {
        this.visible = z;
        this.mobileSignalIconId = i;
        this.contentDescription = str;
        this.typeContentDescription = str2;
        this.roaming = z2;
        this.providerModelBehavior = z3;
    }

    public /* synthetic */ CellSignalState(boolean z, int i, String str, String str2, boolean z2, boolean z3, int i2, DefaultConstructorMarker defaultConstructorMarker) {
        this((i2 & 1) != 0 ? false : z, (i2 & 2) != 0 ? 0 : i, (i2 & 4) != 0 ? null : str, (i2 & 8) != 0 ? null : str2, (i2 & 16) != 0 ? false : z2, (i2 & 32) != 0 ? false : z3);
    }

    public final CellSignalState changeVisibility(boolean z) {
        if (this.visible == z) {
            return this;
        }
        return copy$default(this, z, 0, null, null, false, false, 62, null);
    }
}
