package com.android.systemui.keyguard;

import java.util.concurrent.TimeUnit;
/* compiled from: FaceAuthScreenBrightnessController.kt */
/* loaded from: classes.dex */
public final class FaceAuthScreenBrightnessControllerKt {
    private static final boolean DEFAULT_USE_FACE_WALLPAPER = false;
    private static final long DEFAULT_ANIMATION_DURATION = TimeUnit.SECONDS.toMillis(4);
    private static final int MAX_SCREEN_BRIGHTNESS = 100;
    private static final int MAX_SCRIM_OPACTY = 50;

    public static final long getDEFAULT_ANIMATION_DURATION() {
        return DEFAULT_ANIMATION_DURATION;
    }

    public static final int getMAX_SCREEN_BRIGHTNESS() {
        return MAX_SCREEN_BRIGHTNESS;
    }

    public static final int getMAX_SCRIM_OPACTY() {
        return MAX_SCRIM_OPACTY;
    }

    public static final boolean getDEFAULT_USE_FACE_WALLPAPER() {
        return DEFAULT_USE_FACE_WALLPAPER;
    }
}
