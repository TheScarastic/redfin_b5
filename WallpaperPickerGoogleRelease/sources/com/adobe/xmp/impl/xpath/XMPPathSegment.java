package com.adobe.xmp.impl.xpath;
/* loaded from: classes.dex */
public class XMPPathSegment {
    public boolean alias;
    public int aliasForm;
    public int kind;
    public String name;

    public XMPPathSegment(String str, int i) {
        this.name = str;
        this.kind = i;
    }

    public String toString() {
        switch (this.kind) {
            case 1:
            case 2:
            case 3:
            case 4:
                return this.name;
            case 5:
            case 6:
                return this.name;
            default:
                return this.name;
        }
    }
}
