package com.android.wallpaper.module;

import android.os.SystemProperties;
import java.util.Objects;
/* loaded from: classes.dex */
public class DeviceColorLayerResolver implements DrawableLayerResolver {
    public static final String DEVICE_COLOR;
    public static final int LAYER_INDEX;

    static {
        String str = SystemProperties.get("ro.boot.hardware.color");
        DEVICE_COLOR = str;
        Objects.requireNonNull(str);
        LAYER_INDEX = !str.equals("BLK") ? !str.equals("ORG") ? 0 : 2 : 1;
    }
}
