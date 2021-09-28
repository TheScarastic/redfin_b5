package com.android.systemui.media;

import android.graphics.drawable.Drawable;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: MediaData.kt */
/* loaded from: classes.dex */
public final class MediaDeviceData {
    private final boolean enabled;
    private final Drawable icon;
    private final String name;

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof MediaDeviceData)) {
            return false;
        }
        MediaDeviceData mediaDeviceData = (MediaDeviceData) obj;
        return this.enabled == mediaDeviceData.enabled && Intrinsics.areEqual(this.icon, mediaDeviceData.icon) && Intrinsics.areEqual(this.name, mediaDeviceData.name);
    }

    public int hashCode() {
        boolean z = this.enabled;
        if (z) {
            z = true;
        }
        int i = z ? 1 : 0;
        int i2 = z ? 1 : 0;
        int i3 = z ? 1 : 0;
        int i4 = i * 31;
        Drawable drawable = this.icon;
        int i5 = 0;
        int hashCode = (i4 + (drawable == null ? 0 : drawable.hashCode())) * 31;
        String str = this.name;
        if (str != null) {
            i5 = str.hashCode();
        }
        return hashCode + i5;
    }

    public String toString() {
        return "MediaDeviceData(enabled=" + this.enabled + ", icon=" + this.icon + ", name=" + ((Object) this.name) + ')';
    }

    public MediaDeviceData(boolean z, Drawable drawable, String str) {
        this.enabled = z;
        this.icon = drawable;
        this.name = str;
    }

    public final boolean getEnabled() {
        return this.enabled;
    }

    public final Drawable getIcon() {
        return this.icon;
    }

    public final String getName() {
        return this.name;
    }
}
