package com.google.android.systemui.columbus.sensors;

import java.util.ArrayList;
/* loaded from: classes2.dex */
public class Util {
    public static int getMaxId(ArrayList<Float> arrayList) {
        float f = -3.4028235E38f;
        int i = 0;
        for (int i2 = 0; i2 < arrayList.size(); i2++) {
            if (f < arrayList.get(i2).floatValue()) {
                f = arrayList.get(i2).floatValue();
                i = i2;
            }
        }
        return i;
    }
}
