package com.android.wallpaper.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.view.Display;
import com.android.systemui.shared.system.WallpaperManagerCompat;
/* loaded from: classes.dex */
public final class WallpaperCropUtils {
    public static void adjustCropRect(Context context, Rect rect, boolean z) {
        float centerX = (float) rect.centerX();
        float centerY = (float) rect.centerY();
        float width = (float) rect.width();
        float height = (float) rect.height();
        float wallpaperZoomOutMaxScale = WallpaperManagerCompat.getWallpaperZoomOutMaxScale(context);
        if (!z) {
            wallpaperZoomOutMaxScale = 1.0f / wallpaperZoomOutMaxScale;
        }
        float f = (width / 2.0f) / wallpaperZoomOutMaxScale;
        float f2 = (height / 2.0f) / wallpaperZoomOutMaxScale;
        rect.set((int) (centerX - f), (int) (centerY - f2), (int) (centerX + f), (int) (centerY + f2));
    }

    public static Rect calculateCropRect(Context context, float f, Point point, Point point2, Point point3, int i, int i2) {
        int round = Math.round(((float) point.x) * f);
        int round2 = Math.round(((float) point.y) * f);
        Rect rect = new Rect();
        rect.set(0, 0, round, round2);
        Rect rect2 = new Rect(i, i2, point3.x + i, point3.y + i2);
        int i3 = point2.x - point3.x;
        int i4 = (int) (((float) (point2.y - point3.y)) / 2.0f);
        if (isRtl(context)) {
            rect2.left = Math.max(rect2.left - i3, rect.left);
        } else {
            rect2.right = Math.min(rect2.right + i3, rect.right);
        }
        int i5 = rect2.top;
        int min = Math.min(i5 - Math.max(rect.top, i5 - i4), Math.min(rect.bottom, rect2.bottom + i4) - rect2.bottom);
        rect2.top -= min;
        rect2.bottom += min;
        return rect2;
    }

    public static Point calculateCropSurfaceSize(Resources resources, int i, int i2) {
        int i3;
        if (resources.getConfiguration().smallestScreenWidthDp >= 720) {
            float f = (float) i;
            i3 = (int) (f * (((f / ((float) i2)) * -0.30769226f) + 1.6923077f));
        } else {
            i3 = Math.max((int) (((float) i2) * 2.0f), i);
        }
        return new Point(i3, i);
    }

    public static float calculateMinZoom(Point point, Point point2) {
        float f = (float) point2.x;
        float f2 = (float) point2.y;
        float f3 = (float) point.x;
        float f4 = (float) point.y;
        return f / f2 > f3 / f4 ? f / f3 : f2 / f4;
    }

    public static Rect calculateVisibleRect(Point point, Point point2) {
        PointF pointF = new PointF(((float) point.x) / 2.0f, ((float) point.y) / 2.0f);
        int i = point2.x;
        int i2 = point2.y;
        float f = ((float) i) / ((float) i2);
        int i3 = point.x;
        int i4 = point.y;
        if (f > ((float) i3) / ((float) i4)) {
            float f2 = pointF.y;
            float f3 = (((float) i2) / (((float) i) / ((float) i3))) / 2.0f;
            return new Rect(0, (int) (f2 - f3), point.x, (int) (f2 + f3));
        }
        float f4 = pointF.x;
        float f5 = (((float) i) / (((float) i2) / ((float) i4))) / 2.0f;
        return new Rect((int) (f4 - f5), 0, (int) (f4 + f5), point.y);
    }

    public static void fitToSize(Rect rect, int i, int i2) {
        if (!rect.isEmpty()) {
            float max = ((float) Math.max(i, i2)) / ((float) Math.max(rect.width(), rect.height()));
            if (max != 1.0f) {
                rect.left = (int) ((((float) rect.left) * max) + 0.5f);
                rect.top = (int) ((((float) rect.top) * max) + 0.5f);
                rect.right = (int) ((((float) rect.right) * max) + 0.5f);
                rect.bottom = (int) ((((float) rect.bottom) * max) + 0.5f);
            }
        }
    }

    public static Point getDefaultCropSurfaceSize(Resources resources, Display display) {
        Point point = new Point();
        Point point2 = new Point();
        display.getCurrentSizeRange(point, point2);
        Math.max(point2.x, point2.y);
        Math.max(point.x, point.y);
        Point point3 = new Point();
        display.getRealSize(point3);
        return calculateCropSurfaceSize(resources, Math.max(point3.x, point3.y), Math.min(point3.x, point3.y));
    }

    public static boolean isRtl(Context context) {
        return context.getResources().getConfiguration().getLayoutDirection() == 1;
    }

    public static Rect calculateCropRect(Context context, Point point, Point point2, Point point3, Rect rect, float f) {
        return calculateCropRect(context, f, point3, point2, point, (int) (((float) rect.left) * f), (int) (((float) rect.top) * f));
    }
}
