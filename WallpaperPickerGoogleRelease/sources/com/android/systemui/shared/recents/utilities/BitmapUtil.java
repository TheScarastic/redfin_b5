package com.android.systemui.shared.recents.utilities;

import android.graphics.Bitmap;
import android.graphics.ColorSpace;
import android.graphics.ParcelableColorSpace;
import android.hardware.HardwareBuffer;
import android.os.Bundle;
import android.support.media.ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0;
import java.util.Objects;
/* loaded from: classes.dex */
public final class BitmapUtil {
    private static final String KEY_BUFFER = "bitmap_util_buffer";
    private static final String KEY_COLOR_SPACE = "bitmap_util_color_space";

    private BitmapUtil() {
    }

    public static Bitmap bundleToHardwareBitmap(Bundle bundle) {
        if (!bundle.containsKey(KEY_BUFFER) || !bundle.containsKey(KEY_COLOR_SPACE)) {
            throw new IllegalArgumentException("Bundle does not contain a hardware bitmap");
        }
        HardwareBuffer hardwareBuffer = (HardwareBuffer) bundle.getParcelable(KEY_BUFFER);
        Objects.requireNonNull(hardwareBuffer);
        return Bitmap.wrapHardwareBuffer(hardwareBuffer, bundle.getParcelable(KEY_COLOR_SPACE).getColorSpace());
    }

    public static Bundle hardwareBitmapToBundle(Bitmap bitmap) {
        ParcelableColorSpace parcelableColorSpace;
        if (bitmap.getConfig() == Bitmap.Config.HARDWARE) {
            if (bitmap.getColorSpace() == null) {
                parcelableColorSpace = new ParcelableColorSpace(ColorSpace.get(ColorSpace.Named.SRGB));
            } else {
                parcelableColorSpace = new ParcelableColorSpace(bitmap.getColorSpace());
            }
            Bundle bundle = new Bundle();
            bundle.putParcelable(KEY_BUFFER, bitmap.getHardwareBuffer());
            bundle.putParcelable(KEY_COLOR_SPACE, parcelableColorSpace);
            return bundle;
        }
        StringBuilder m = ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0.m("Passed bitmap must have hardware config, found: ");
        m.append(bitmap.getConfig());
        throw new IllegalArgumentException(m.toString());
    }
}
