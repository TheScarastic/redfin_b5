package com.google.android.systemui.dreamliner;

import android.os.Bundle;
/* loaded from: classes2.dex */
public class DockInfo {
    private int accessoryType;
    private String manufacturer;
    private String model;
    private String serialNumber;

    public DockInfo(String str, String str2, String str3, int i) {
        this.manufacturer = "";
        this.model = "";
        this.serialNumber = "";
        this.accessoryType = -1;
        this.manufacturer = str;
        this.model = str2;
        this.serialNumber = str3;
        this.accessoryType = i;
    }

    /* access modifiers changed from: package-private */
    public Bundle toBundle() {
        Bundle bundle = new Bundle();
        bundle.putString("manufacturer", this.manufacturer);
        bundle.putString("model", this.model);
        bundle.putString("serialNumber", this.serialNumber);
        bundle.putInt("accessoryType", this.accessoryType);
        return bundle;
    }

    public String toString() {
        return this.manufacturer + ", " + this.model + ", " + this.serialNumber + ", " + this.accessoryType;
    }
}
