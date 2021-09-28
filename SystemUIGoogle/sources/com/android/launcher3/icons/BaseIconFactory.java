package com.android.launcher3.icons;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.AdaptiveIconDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.UserHandle;
import com.android.launcher3.icons.BitmapInfo;
/* loaded from: classes.dex */
public class BaseIconFactory implements AutoCloseable {
    static final boolean ATLEAST_OREO;
    static final boolean ATLEAST_P;
    private static int PLACEHOLDER_BACKGROUND_COLOR;
    private boolean mBadgeOnLeft;
    private final Canvas mCanvas;
    private final ColorExtractor mColorExtractor;
    protected final Context mContext;
    private boolean mDisableColorExtractor;
    protected final int mFillResIconDpi;
    protected final int mIconBitmapSize;
    private IconNormalizer mNormalizer;
    private final Rect mOldBounds;
    private final PackageManager mPm;
    private ShadowGenerator mShadowGenerator;
    private final boolean mShapeDetection;
    private final Paint mTextPaint;
    private Bitmap mUserBadgeBitmap;
    private int mWrapperBackgroundColor;
    private Drawable mWrapperIcon;

    public static int getBadgeSizeForIconSize(int i) {
        return (int) (((float) i) * 0.444f);
    }

    static {
        int i = Build.VERSION.SDK_INT;
        boolean z = true;
        ATLEAST_OREO = i >= 26;
        if (i < 28) {
            z = false;
        }
        ATLEAST_P = z;
        PLACEHOLDER_BACKGROUND_COLOR = Color.rgb(240, 240, 240);
    }

    /* access modifiers changed from: protected */
    public BaseIconFactory(Context context, int i, int i2, boolean z) {
        this.mOldBounds = new Rect();
        this.mBadgeOnLeft = false;
        this.mWrapperBackgroundColor = -1;
        Paint paint = new Paint(3);
        this.mTextPaint = paint;
        Context applicationContext = context.getApplicationContext();
        this.mContext = applicationContext;
        this.mShapeDetection = z;
        this.mFillResIconDpi = i;
        this.mIconBitmapSize = i2;
        this.mPm = applicationContext.getPackageManager();
        this.mColorExtractor = new ColorExtractor();
        Canvas canvas = new Canvas();
        this.mCanvas = canvas;
        canvas.setDrawFilter(new PaintFlagsDrawFilter(4, 2));
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setColor(PLACEHOLDER_BACKGROUND_COLOR);
        paint.setTextSize(context.getResources().getDisplayMetrics().density * 20.0f);
        clear();
    }

    public BaseIconFactory(Context context, int i, int i2) {
        this(context, i, i2, false);
    }

    /* access modifiers changed from: protected */
    public void clear() {
        this.mWrapperBackgroundColor = -1;
        this.mDisableColorExtractor = false;
        this.mBadgeOnLeft = false;
    }

    public ShadowGenerator getShadowGenerator() {
        if (this.mShadowGenerator == null) {
            this.mShadowGenerator = new ShadowGenerator(this.mIconBitmapSize);
        }
        return this.mShadowGenerator;
    }

    public IconNormalizer getNormalizer() {
        if (this.mNormalizer == null) {
            this.mNormalizer = new IconNormalizer(this.mContext, this.mIconBitmapSize, this.mShapeDetection);
        }
        return this.mNormalizer;
    }

    public BitmapInfo createIconBitmap(Bitmap bitmap) {
        if (!(this.mIconBitmapSize == bitmap.getWidth() && this.mIconBitmapSize == bitmap.getHeight())) {
            bitmap = createIconBitmap(new BitmapDrawable(this.mContext.getResources(), bitmap), 1.0f);
        }
        return BitmapInfo.of(bitmap, extractColor(bitmap));
    }

    public BitmapInfo createBadgedIconBitmap(Drawable drawable, UserHandle userHandle, boolean z) {
        return createBadgedIconBitmap(drawable, userHandle, z, false, (float[]) null);
    }

    public BitmapInfo createBadgedIconBitmap(Drawable drawable, UserHandle userHandle, int i, boolean z) {
        return createBadgedIconBitmap(drawable, userHandle, i, z, (float[]) null);
    }

    public BitmapInfo createBadgedIconBitmap(Drawable drawable, UserHandle userHandle, int i, boolean z, float[] fArr) {
        return createBadgedIconBitmap(drawable, userHandle, ATLEAST_P || (ATLEAST_OREO && i >= 26), z, fArr);
    }

    public BitmapInfo createBadgedIconBitmap(Drawable drawable, UserHandle userHandle, boolean z, boolean z2, float[] fArr) {
        if (fArr == null) {
            fArr = new float[1];
        }
        Drawable normalizeAndWrapToAdaptiveIcon = normalizeAndWrapToAdaptiveIcon(drawable, z, null, fArr);
        Bitmap createIconBitmap = createIconBitmap(normalizeAndWrapToAdaptiveIcon, fArr[0]);
        if (ATLEAST_OREO && (normalizeAndWrapToAdaptiveIcon instanceof AdaptiveIconDrawable)) {
            this.mCanvas.setBitmap(createIconBitmap);
            getShadowGenerator().recreateIcon(Bitmap.createBitmap(createIconBitmap), this.mCanvas);
            this.mCanvas.setBitmap(null);
        }
        if (z2) {
            badgeWithDrawable(createIconBitmap, this.mContext.getDrawable(R$drawable.ic_instant_app_badge));
        }
        if (userHandle != null) {
            Drawable userBadgedIcon = this.mPm.getUserBadgedIcon(new FixedSizeBitmapDrawable(createIconBitmap), userHandle);
            if (userBadgedIcon instanceof BitmapDrawable) {
                createIconBitmap = ((BitmapDrawable) userBadgedIcon).getBitmap();
            } else {
                createIconBitmap = createIconBitmap(userBadgedIcon, 1.0f);
            }
        }
        int extractColor = extractColor(createIconBitmap);
        if (normalizeAndWrapToAdaptiveIcon instanceof BitmapInfo.Extender) {
            return ((BitmapInfo.Extender) normalizeAndWrapToAdaptiveIcon).getExtendedInfo(createIconBitmap, extractColor, this, fArr[0], userHandle);
        }
        return BitmapInfo.of(createIconBitmap, extractColor);
    }

    public Bitmap getUserBadgeBitmap(UserHandle userHandle) {
        if (this.mUserBadgeBitmap == null) {
            int i = this.mIconBitmapSize;
            Drawable userBadgedIcon = this.mPm.getUserBadgedIcon(new FixedSizeBitmapDrawable(Bitmap.createBitmap(i, i, Bitmap.Config.ARGB_8888)), userHandle);
            if (userBadgedIcon instanceof BitmapDrawable) {
                this.mUserBadgeBitmap = ((BitmapDrawable) userBadgedIcon).getBitmap();
            } else {
                int i2 = this.mIconBitmapSize;
                userBadgedIcon.setBounds(0, 0, i2, i2);
                int i3 = this.mIconBitmapSize;
                this.mUserBadgeBitmap = BitmapRenderer.createSoftwareBitmap(i3, i3, new BitmapRenderer(userBadgedIcon) { // from class: com.android.launcher3.icons.BaseIconFactory$$ExternalSyntheticLambda0
                    public final /* synthetic */ Drawable f$0;

                    {
                        this.f$0 = r1;
                    }

                    @Override // com.android.launcher3.icons.BitmapRenderer
                    public final void draw(Canvas canvas) {
                        this.f$0.draw(canvas);
                    }
                });
            }
        }
        return this.mUserBadgeBitmap;
    }

    public Bitmap createScaledBitmapWithoutShadow(Drawable drawable, boolean z) {
        RectF rectF = new RectF();
        float[] fArr = new float[1];
        return createIconBitmap(normalizeAndWrapToAdaptiveIcon(drawable, z, rectF, fArr), Math.min(fArr[0], ShadowGenerator.getScaleForBounds(rectF)));
    }

    public void disableColorExtraction() {
        this.mDisableColorExtractor = true;
    }

    private Drawable normalizeAndWrapToAdaptiveIcon(Drawable drawable, boolean z, RectF rectF, float[] fArr) {
        float f;
        if (drawable == null) {
            return null;
        }
        if (!z || !ATLEAST_OREO) {
            f = getNormalizer().getScale(drawable, rectF, null, null);
        } else {
            if (this.mWrapperIcon == null) {
                this.mWrapperIcon = this.mContext.getDrawable(R$drawable.adaptive_icon_drawable_wrapper).mutate();
            }
            AdaptiveIconDrawable adaptiveIconDrawable = (AdaptiveIconDrawable) this.mWrapperIcon;
            adaptiveIconDrawable.setBounds(0, 0, 1, 1);
            boolean[] zArr = new boolean[1];
            f = getNormalizer().getScale(drawable, rectF, adaptiveIconDrawable.getIconMask(), zArr);
            if (!(drawable instanceof AdaptiveIconDrawable) && !zArr[0]) {
                FixedScaleDrawable fixedScaleDrawable = (FixedScaleDrawable) adaptiveIconDrawable.getForeground();
                fixedScaleDrawable.setDrawable(drawable);
                fixedScaleDrawable.setScale(f);
                f = getNormalizer().getScale(adaptiveIconDrawable, rectF, null, null);
                ((ColorDrawable) adaptiveIconDrawable.getBackground()).setColor(this.mWrapperBackgroundColor);
                drawable = adaptiveIconDrawable;
            }
        }
        fArr[0] = f;
        return drawable;
    }

    public void badgeWithDrawable(Bitmap bitmap, Drawable drawable) {
        this.mCanvas.setBitmap(bitmap);
        badgeWithDrawable(this.mCanvas, drawable);
        this.mCanvas.setBitmap(null);
    }

    public void badgeWithDrawable(Canvas canvas, Drawable drawable) {
        int badgeSizeForIconSize = getBadgeSizeForIconSize(this.mIconBitmapSize);
        if (this.mBadgeOnLeft) {
            int i = this.mIconBitmapSize;
            drawable.setBounds(0, i - badgeSizeForIconSize, badgeSizeForIconSize, i);
        } else {
            int i2 = this.mIconBitmapSize;
            drawable.setBounds(i2 - badgeSizeForIconSize, i2 - badgeSizeForIconSize, i2, i2);
        }
        drawable.draw(canvas);
    }

    private Bitmap createIconBitmap(Drawable drawable, float f) {
        return createIconBitmap(drawable, f, this.mIconBitmapSize);
    }

    public Bitmap createIconBitmap(Drawable drawable, float f, int i) {
        int i2;
        int i3;
        Bitmap createBitmap = Bitmap.createBitmap(i, i, Bitmap.Config.ARGB_8888);
        if (drawable == null) {
            return createBitmap;
        }
        this.mCanvas.setBitmap(createBitmap);
        this.mOldBounds.set(drawable.getBounds());
        if (!ATLEAST_OREO || !(drawable instanceof AdaptiveIconDrawable)) {
            if (drawable instanceof BitmapDrawable) {
                BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
                Bitmap bitmap = bitmapDrawable.getBitmap();
                if (createBitmap != null && bitmap.getDensity() == 0) {
                    bitmapDrawable.setTargetDensity(this.mContext.getResources().getDisplayMetrics());
                }
            }
            int intrinsicWidth = drawable.getIntrinsicWidth();
            int intrinsicHeight = drawable.getIntrinsicHeight();
            if (intrinsicWidth > 0 && intrinsicHeight > 0) {
                float f2 = ((float) intrinsicWidth) / ((float) intrinsicHeight);
                if (intrinsicWidth > intrinsicHeight) {
                    i2 = (int) (((float) i) / f2);
                    i3 = i;
                } else if (intrinsicHeight > intrinsicWidth) {
                    i3 = (int) (((float) i) * f2);
                    i2 = i;
                }
                int i4 = (i - i3) / 2;
                int i5 = (i - i2) / 2;
                drawable.setBounds(i4, i5, i3 + i4, i2 + i5);
                this.mCanvas.save();
                float f3 = (float) (i / 2);
                this.mCanvas.scale(f, f, f3, f3);
                drawable.draw(this.mCanvas);
                this.mCanvas.restore();
            }
            i3 = i;
            i2 = i3;
            int i4 = (i - i3) / 2;
            int i5 = (i - i2) / 2;
            drawable.setBounds(i4, i5, i3 + i4, i2 + i5);
            this.mCanvas.save();
            float f3 = (float) (i / 2);
            this.mCanvas.scale(f, f, f3, f3);
            drawable.draw(this.mCanvas);
            this.mCanvas.restore();
        } else {
            float f4 = (float) i;
            int max = Math.max((int) Math.ceil((double) (0.03125f * f4)), Math.round((f4 * (1.0f - f)) / 2.0f));
            int i6 = i - max;
            drawable.setBounds(max, max, i6, i6);
            if (drawable instanceof BitmapInfo.Extender) {
                ((BitmapInfo.Extender) drawable).drawForPersistence(this.mCanvas);
            } else {
                drawable.draw(this.mCanvas);
            }
        }
        drawable.setBounds(this.mOldBounds);
        this.mCanvas.setBitmap(null);
        return createBitmap;
    }

    @Override // java.lang.AutoCloseable
    public void close() {
        clear();
    }

    private int extractColor(Bitmap bitmap) {
        if (this.mDisableColorExtractor) {
            return 0;
        }
        return this.mColorExtractor.findDominantColorByHue(bitmap);
    }

    /* access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class FixedSizeBitmapDrawable extends BitmapDrawable {
        public FixedSizeBitmapDrawable(Bitmap bitmap) {
            super((Resources) null, bitmap);
        }

        @Override // android.graphics.drawable.BitmapDrawable, android.graphics.drawable.Drawable
        public int getIntrinsicHeight() {
            return getBitmap().getWidth();
        }

        @Override // android.graphics.drawable.BitmapDrawable, android.graphics.drawable.Drawable
        public int getIntrinsicWidth() {
            return getBitmap().getWidth();
        }
    }
}
