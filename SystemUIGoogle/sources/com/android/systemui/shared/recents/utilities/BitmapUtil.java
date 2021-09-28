package com.android.systemui.shared.recents.utilities;

import android.graphics.Bitmap;
import android.graphics.ColorSpace;
import android.graphics.ParcelableColorSpace;
import android.hardware.HardwareBuffer;
import android.os.Bundle;
import java.util.Objects;
/* loaded from: classes.dex */
public final class BitmapUtil {
    public static Bundle hardwareBitmapToBundle(Bitmap bitmap) {
        ParcelableColorSpace parcelableColorSpace;
        if (bitmap.getConfig() == Bitmap.Config.HARDWARE) {
            if (bitmap.getColorSpace() == null) {
                parcelableColorSpace = new ParcelableColorSpace(ColorSpace.get(ColorSpace.Named.SRGB));
            } else {
                parcelableColorSpace = new ParcelableColorSpace(bitmap.getColorSpace());
            }
            Bundle bundle = new Bundle();
            bundle.putParcelable("bitmap_util_buffer", bitmap.getHardwareBuffer());
            bundle.putParcelable("bitmap_util_color_space", parcelableColorSpace);
            return bundle;
        }
        throw new IllegalArgumentException("Passed bitmap must have hardware config, found: " + bitmap.getConfig());
    }

    public static Bitmap bundleToHardwareBitmap(Bundle bundle) {
        if (!bundle.containsKey("bitmap_util_buffer") || !bundle.containsKey("bitmap_util_color_space")) {
            throw new IllegalArgumentException("Bundle does not contain a hardware bitmap");
        }
        HardwareBuffer hardwareBuffer = (HardwareBuffer) bundle.getParcelable("bitmap_util_buffer");
        Objects.requireNonNull(hardwareBuffer);
        return Bitmap.wrapHardwareBuffer(hardwareBuffer, bundle.getParcelable("bitmap_util_color_space").getColorSpace());
    }
}
