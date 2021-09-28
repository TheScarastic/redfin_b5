package com.android.systemui.controls.controller;

import kotlin.jvm.internal.Intrinsics;
/* compiled from: ControlsController.kt */
/* loaded from: classes.dex */
public final class SeedResponse {
    private final boolean accepted;
    private final String packageName;

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof SeedResponse)) {
            return false;
        }
        SeedResponse seedResponse = (SeedResponse) obj;
        return Intrinsics.areEqual(this.packageName, seedResponse.packageName) && this.accepted == seedResponse.accepted;
    }

    public int hashCode() {
        int hashCode = this.packageName.hashCode() * 31;
        boolean z = this.accepted;
        if (z) {
            z = true;
        }
        int i = z ? 1 : 0;
        int i2 = z ? 1 : 0;
        int i3 = z ? 1 : 0;
        return hashCode + i;
    }

    public String toString() {
        return "SeedResponse(packageName=" + this.packageName + ", accepted=" + this.accepted + ')';
    }

    public SeedResponse(String str, boolean z) {
        Intrinsics.checkNotNullParameter(str, "packageName");
        this.packageName = str;
        this.accepted = z;
    }

    public final boolean getAccepted() {
        return this.accepted;
    }

    public final String getPackageName() {
        return this.packageName;
    }
}
