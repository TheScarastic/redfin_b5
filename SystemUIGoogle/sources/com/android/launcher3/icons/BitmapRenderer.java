package com.android.launcher3.icons;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.os.Build;
/* loaded from: classes.dex */
public interface BitmapRenderer {
    public static final boolean USE_HARDWARE_BITMAP;

    void draw(Canvas canvas);

    static {
        USE_HARDWARE_BITMAP = Build.VERSION.SDK_INT >= 28;
    }

    static Bitmap createSoftwareBitmap(int i, int i2, BitmapRenderer bitmapRenderer) {
        GraphicsUtils.noteNewBitmapCreated();
        Bitmap createBitmap = Bitmap.createBitmap(i, i2, Bitmap.Config.ARGB_8888);
        bitmapRenderer.draw(new Canvas(createBitmap));
        return createBitmap;
    }

    @TargetApi(28)
    static Bitmap createHardwareBitmap(int i, int i2, BitmapRenderer bitmapRenderer) {
        if (!USE_HARDWARE_BITMAP) {
            return createSoftwareBitmap(i, i2, bitmapRenderer);
        }
        GraphicsUtils.noteNewBitmapCreated();
        Picture picture = new Picture();
        bitmapRenderer.draw(picture.beginRecording(i, i2));
        picture.endRecording();
        return Bitmap.createBitmap(picture);
    }
}
