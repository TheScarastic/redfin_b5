package com.android.systemui.shared.recents.utilities;

import android.graphics.Color;
import android.os.Handler;
/* loaded from: classes.dex */
public class Utilities {
    public static float clamp(float f, float f2, float f3) {
        return Math.max(f2, Math.min(f3, f));
    }

    public static float computeContrastBetweenColors(int i, int i2) {
        float f;
        float f2;
        float f3;
        float f4;
        float f5;
        float f6;
        float red = ((float) Color.red(i)) / 255.0f;
        float green = ((float) Color.green(i)) / 255.0f;
        float blue = ((float) Color.blue(i)) / 255.0f;
        if (red < 0.03928f) {
            f = red / 12.92f;
        } else {
            f = (float) Math.pow((double) ((red + 0.055f) / 1.055f), 2.4000000953674316d);
        }
        if (green < 0.03928f) {
            f2 = green / 12.92f;
        } else {
            f2 = (float) Math.pow((double) ((green + 0.055f) / 1.055f), 2.4000000953674316d);
        }
        if (blue < 0.03928f) {
            f3 = blue / 12.92f;
        } else {
            f3 = (float) Math.pow((double) ((blue + 0.055f) / 1.055f), 2.4000000953674316d);
        }
        float f7 = f3 * 0.0722f;
        float f8 = f7 + (f2 * 0.7152f) + (f * 0.2126f);
        float red2 = ((float) Color.red(i2)) / 255.0f;
        float green2 = ((float) Color.green(i2)) / 255.0f;
        float blue2 = ((float) Color.blue(i2)) / 255.0f;
        if (red2 < 0.03928f) {
            f4 = red2 / 12.92f;
        } else {
            f4 = (float) Math.pow((double) ((red2 + 0.055f) / 1.055f), 2.4000000953674316d);
        }
        if (green2 < 0.03928f) {
            f5 = green2 / 12.92f;
        } else {
            f5 = (float) Math.pow((double) ((green2 + 0.055f) / 1.055f), 2.4000000953674316d);
        }
        if (blue2 < 0.03928f) {
            f6 = blue2 / 12.92f;
        } else {
            f6 = (float) Math.pow((double) ((blue2 + 0.055f) / 1.055f), 2.4000000953674316d);
        }
        return Math.abs((((f6 * 0.0722f) + ((f5 * 0.7152f) + (f4 * 0.2126f))) + 0.05f) / (f8 + 0.05f));
    }

    public static void postAtFrontOfQueueAsynchronously(Handler handler, Runnable runnable) {
        handler.sendMessageAtFrontOfQueue(handler.obtainMessage().setCallback(runnable));
    }
}
