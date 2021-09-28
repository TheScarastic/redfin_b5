package com.android.launcher3.icons;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.drawable.AdaptiveIconDrawable;
import android.graphics.drawable.Drawable;
import java.nio.ByteBuffer;
/* loaded from: classes.dex */
public class IconNormalizer {
    private final Bitmap mBitmap;
    private final Canvas mCanvas;
    private boolean mEnableShapeDetection;
    private final float[] mLeftBorder;
    private final int mMaxSize;
    private final Paint mPaintMaskShape;
    private final Paint mPaintMaskShapeOutline;
    private final byte[] mPixels;
    private final float[] mRightBorder;
    private final Rect mBounds = new Rect();
    private final RectF mAdaptiveIconBounds = new RectF();
    private final Path mShapePath = new Path();
    private final Matrix mMatrix = new Matrix();
    private float mAdaptiveIconScale = 0.0f;

    /* access modifiers changed from: package-private */
    public IconNormalizer(Context context, int i, boolean z) {
        int i2 = i * 2;
        this.mMaxSize = i2;
        Bitmap createBitmap = Bitmap.createBitmap(i2, i2, Bitmap.Config.ALPHA_8);
        this.mBitmap = createBitmap;
        this.mCanvas = new Canvas(createBitmap);
        this.mPixels = new byte[i2 * i2];
        this.mLeftBorder = new float[i2];
        this.mRightBorder = new float[i2];
        Paint paint = new Paint();
        this.mPaintMaskShape = paint;
        paint.setColor(-65536);
        paint.setStyle(Paint.Style.FILL);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.XOR));
        Paint paint2 = new Paint();
        this.mPaintMaskShapeOutline = paint2;
        paint2.setStrokeWidth(context.getResources().getDisplayMetrics().density * 2.0f);
        paint2.setStyle(Paint.Style.STROKE);
        paint2.setColor(-16777216);
        paint2.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        this.mEnableShapeDetection = z;
    }

    private static float getScale(float f, float f2, float f3) {
        float f4 = f / f2;
        float f5 = f4 < 0.7853982f ? 0.6597222f : ((1.0f - f4) * 0.040449437f) + 0.6510417f;
        float f6 = f / f3;
        if (f6 > f5) {
            return (float) Math.sqrt((double) (f5 / f6));
        }
        return 1.0f;
    }

    @TargetApi(26)
    public static float normalizeAdaptiveIcon(Drawable drawable, int i, RectF rectF) {
        Rect rect = new Rect(drawable.getBounds());
        drawable.setBounds(0, 0, i, i);
        Path iconMask = ((AdaptiveIconDrawable) drawable).getIconMask();
        Region region = new Region();
        region.setPath(iconMask, new Region(0, 0, i, i));
        Rect bounds = region.getBounds();
        int area = GraphicsUtils.getArea(region);
        if (rectF != null) {
            float f = (float) i;
            rectF.set(((float) bounds.left) / f, ((float) bounds.top) / f, 1.0f - (((float) bounds.right) / f), 1.0f - (((float) bounds.bottom) / f));
        }
        drawable.setBounds(rect);
        float f2 = (float) area;
        return getScale(f2, f2, (float) (i * i));
    }

    private boolean isShape(Path path) {
        if (Math.abs((((float) this.mBounds.width()) / ((float) this.mBounds.height())) - 1.0f) > 0.05f) {
            return false;
        }
        this.mMatrix.reset();
        this.mMatrix.setScale((float) this.mBounds.width(), (float) this.mBounds.height());
        Matrix matrix = this.mMatrix;
        Rect rect = this.mBounds;
        matrix.postTranslate((float) rect.left, (float) rect.top);
        path.transform(this.mMatrix, this.mShapePath);
        this.mCanvas.drawPath(this.mShapePath, this.mPaintMaskShape);
        this.mCanvas.drawPath(this.mShapePath, this.mPaintMaskShapeOutline);
        return isTransparentBitmap();
    }

    private boolean isTransparentBitmap() {
        Rect rect;
        ByteBuffer wrap = ByteBuffer.wrap(this.mPixels);
        wrap.rewind();
        this.mBitmap.copyPixelsToBuffer(wrap);
        Rect rect2 = this.mBounds;
        int i = rect2.top;
        int i2 = this.mMaxSize;
        int i3 = i * i2;
        int i4 = i2 - rect2.right;
        int i5 = 0;
        while (true) {
            rect = this.mBounds;
            if (i >= rect.bottom) {
                break;
            }
            int i6 = rect.left;
            int i7 = i3 + i6;
            while (i6 < this.mBounds.right) {
                if ((this.mPixels[i7] & 255) > 40) {
                    i5++;
                }
                i7++;
                i6++;
            }
            i3 = i7 + i4;
            i++;
        }
        if (((float) i5) / ((float) (rect.width() * this.mBounds.height())) < 0.005f) {
            return true;
        }
        return false;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0050, code lost:
        if (r4 <= r16.mMaxSize) goto L_0x0054;
     */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x0084  */
    /* JADX WARNING: Removed duplicated region for block: B:55:0x00d8 A[Catch: all -> 0x012f, TryCatch #0 {, blocks: (B:4:0x0009, B:6:0x000e, B:8:0x0012, B:10:0x0018, B:12:0x0024, B:13:0x0029, B:16:0x002d, B:20:0x003a, B:23:0x0040, B:25:0x004e, B:27:0x0052, B:29:0x0056, B:31:0x005a, B:32:0x005c, B:36:0x0089, B:42:0x0098, B:43:0x009f, B:47:0x00b0, B:48:0x00ba, B:53:0x00c9, B:55:0x00d8, B:58:0x00e3, B:59:0x00ec, B:60:0x00ef, B:62:0x00fb, B:65:0x010f, B:67:0x0113, B:69:0x0116, B:70:0x011f), top: B:77:0x0009 }] */
    /* JADX WARNING: Removed duplicated region for block: B:62:0x00fb A[Catch: all -> 0x012f, TryCatch #0 {, blocks: (B:4:0x0009, B:6:0x000e, B:8:0x0012, B:10:0x0018, B:12:0x0024, B:13:0x0029, B:16:0x002d, B:20:0x003a, B:23:0x0040, B:25:0x004e, B:27:0x0052, B:29:0x0056, B:31:0x005a, B:32:0x005c, B:36:0x0089, B:42:0x0098, B:43:0x009f, B:47:0x00b0, B:48:0x00ba, B:53:0x00c9, B:55:0x00d8, B:58:0x00e3, B:59:0x00ec, B:60:0x00ef, B:62:0x00fb, B:65:0x010f, B:67:0x0113, B:69:0x0116, B:70:0x011f), top: B:77:0x0009 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized float getScale(android.graphics.drawable.Drawable r17, android.graphics.RectF r18, android.graphics.Path r19, boolean[] r20) {
        /*
        // Method dump skipped, instructions count: 306
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.launcher3.icons.IconNormalizer.getScale(android.graphics.drawable.Drawable, android.graphics.RectF, android.graphics.Path, boolean[]):float");
    }

    private static void convertToConvexArray(float[] fArr, int i, int i2, int i3) {
        float[] fArr2 = new float[fArr.length - 1];
        int i4 = -1;
        float f = Float.MAX_VALUE;
        for (int i5 = i2 + 1; i5 <= i3; i5++) {
            if (fArr[i5] > -1.0f) {
                if (f == Float.MAX_VALUE) {
                    i4 = i2;
                } else {
                    float f2 = ((fArr[i5] - fArr[i4]) / ((float) (i5 - i4))) - f;
                    float f3 = (float) i;
                    if (f2 * f3 < 0.0f) {
                        while (i4 > i2) {
                            i4--;
                            if ((((fArr[i5] - fArr[i4]) / ((float) (i5 - i4))) - fArr2[i4]) * f3 >= 0.0f) {
                                break;
                            }
                        }
                    }
                }
                f = (fArr[i5] - fArr[i4]) / ((float) (i5 - i4));
                for (int i6 = i4; i6 < i5; i6++) {
                    fArr2[i6] = f;
                    fArr[i6] = fArr[i4] + (((float) (i6 - i4)) * f);
                }
                i4 = i5;
            }
        }
    }

    public static int getNormalizedCircleSize(int i) {
        return (int) Math.round(Math.sqrt(((double) ((((float) (i * i)) * 0.6597222f) * 4.0f)) / 3.141592653589793d));
    }
}
