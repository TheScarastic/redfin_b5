package com.android.launcher3.icons;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.RegionIterator;
/* loaded from: classes.dex */
public class GraphicsUtils {
    public static Runnable sOnNewBitmapRunnable = GraphicsUtils$$ExternalSyntheticLambda0.INSTANCE;

    /* access modifiers changed from: private */
    public static /* synthetic */ void lambda$static$0() {
    }

    public static int setColorAlphaBound(int i, int i2) {
        if (i2 < 0) {
            i2 = 0;
        } else if (i2 > 255) {
            i2 = 255;
        }
        return (i & 16777215) | (i2 << 24);
    }

    public static int getArea(Region region) {
        RegionIterator regionIterator = new RegionIterator(region);
        Rect rect = new Rect();
        int i = 0;
        while (regionIterator.next(rect)) {
            i += rect.width() * rect.height();
        }
        return i;
    }

    public static void noteNewBitmapCreated() {
        sOnNewBitmapRunnable.run();
    }

    public static int getAttrColor(Context context, int i) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(new int[]{i});
        int color = obtainStyledAttributes.getColor(0, 0);
        obtainStyledAttributes.recycle();
        return color;
    }
}
