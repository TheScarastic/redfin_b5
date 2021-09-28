package com.google.android.libraries.microvideo.xmp;

import androidx.preference.R$string$$ExternalSyntheticOutline0;
import com.adobe.xmp.XMPPathFactory$$ExternalSyntheticOutline0;
/* loaded from: classes.dex */
public final class AutoValue_MicroVideoXmpContainerItem extends MicroVideoXmpContainerItem {
    public final int length;
    public final String mime;
    public final int padding;
    public final String semantic;

    public AutoValue_MicroVideoXmpContainerItem(String str, String str2, int i, int i2, AnonymousClass1 r5) {
        this.mime = str;
        this.semantic = str2;
        this.length = i;
        this.padding = i2;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof MicroVideoXmpContainerItem)) {
            return false;
        }
        MicroVideoXmpContainerItem microVideoXmpContainerItem = (MicroVideoXmpContainerItem) obj;
        return this.mime.equals(microVideoXmpContainerItem.getMime()) && this.semantic.equals(microVideoXmpContainerItem.getSemantic()) && this.length == microVideoXmpContainerItem.getLength() && this.padding == microVideoXmpContainerItem.getPadding();
    }

    @Override // com.google.android.libraries.microvideo.xmp.MicroVideoXmpContainerItem
    public int getLength() {
        return this.length;
    }

    @Override // com.google.android.libraries.microvideo.xmp.MicroVideoXmpContainerItem
    public String getMime() {
        return this.mime;
    }

    @Override // com.google.android.libraries.microvideo.xmp.MicroVideoXmpContainerItem
    public int getPadding() {
        return this.padding;
    }

    @Override // com.google.android.libraries.microvideo.xmp.MicroVideoXmpContainerItem
    public String getSemantic() {
        return this.semantic;
    }

    public int hashCode() {
        return this.padding ^ ((((((this.mime.hashCode() ^ 1000003) * 1000003) ^ this.semantic.hashCode()) * 1000003) ^ this.length) * 1000003);
    }

    public String toString() {
        String str = this.mime;
        String str2 = this.semantic;
        int i = this.length;
        int i2 = this.padding;
        StringBuilder m = R$string$$ExternalSyntheticOutline0.m(XMPPathFactory$$ExternalSyntheticOutline0.m(str2, XMPPathFactory$$ExternalSyntheticOutline0.m(str, 85)), "MicroVideoXmpContainerItem{mime=", str, ", semantic=", str2);
        m.append(", length=");
        m.append(i);
        m.append(", padding=");
        m.append(i2);
        m.append("}");
        return m.toString();
    }
}
