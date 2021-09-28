package com.adobe.xmp.impl.xpath;

import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public class XMPPath {
    public List segments = new ArrayList(5);

    public XMPPathSegment getSegment(int i) {
        return (XMPPathSegment) this.segments.get(i);
    }

    public int size() {
        return this.segments.size();
    }

    public String toString() {
        int i;
        StringBuffer stringBuffer = new StringBuffer();
        for (int i2 = 1; i2 < size(); i2++) {
            stringBuffer.append(getSegment(i2));
            if (i2 < size() - 1 && ((i = getSegment(i2 + 1).kind) == 1 || i == 2)) {
                stringBuffer.append('/');
            }
        }
        return stringBuffer.toString();
    }
}
