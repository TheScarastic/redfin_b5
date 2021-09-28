package com.android.settingslib.graph;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import com.android.settingslib.R$array;
import com.android.settingslib.R$color;
import com.android.settingslib.R$dimen;
import com.android.settingslib.R$fraction;
import com.android.settingslib.R$string;
import com.android.settingslib.Utils;
/* loaded from: classes.dex */
public class BatteryMeterDrawableBase extends Drawable {
    public static final String TAG = BatteryMeterDrawableBase.class.getSimpleName();
    protected final Paint mBatteryPaint;
    protected final Paint mBoltPaint;
    private final float[] mBoltPoints;
    protected float mButtonHeightFraction;
    private int mChargeColor;
    private boolean mCharging;
    private final int[] mColors;
    protected final Context mContext;
    private final int mCriticalLevel;
    protected final Paint mFramePaint;
    private int mHeight;
    private final int mIntrinsicHeight;
    private final int mIntrinsicWidth;
    protected final Paint mPlusPaint;
    private final float[] mPlusPoints;
    private boolean mPowerSaveEnabled;
    protected final Paint mPowersavePaint;
    private boolean mShowPercent;
    private float mSubpixelSmoothingLeft;
    private float mSubpixelSmoothingRight;
    private float mTextHeight;
    protected final Paint mTextPaint;
    private String mWarningString;
    private float mWarningTextHeight;
    protected final Paint mWarningTextPaint;
    private int mWidth;
    private int mLevel = -1;
    protected boolean mPowerSaveAsColorError = true;
    private int mIconTint = -1;
    private float mOldDarkIntensity = -1.0f;
    private final Path mBoltPath = new Path();
    private final Path mPlusPath = new Path();
    private final Rect mPadding = new Rect();
    private final RectF mFrame = new RectF();
    private final RectF mButtonFrame = new RectF();
    private final RectF mBoltFrame = new RectF();
    private final RectF mPlusFrame = new RectF();
    private final Path mShapePath = new Path();
    private final Path mOutlinePath = new Path();
    private final Path mTextPath = new Path();

    protected float getAspectRatio() {
        return 0.58f;
    }

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        return 0;
    }

    protected float getRadiusRatio() {
        return 0.05882353f;
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int i) {
    }

    public BatteryMeterDrawableBase(Context context, int i) {
        this.mContext = context;
        Resources resources = context.getResources();
        TypedArray obtainTypedArray = resources.obtainTypedArray(R$array.batterymeter_color_levels);
        TypedArray obtainTypedArray2 = resources.obtainTypedArray(R$array.batterymeter_color_values);
        int length = obtainTypedArray.length();
        this.mColors = new int[length * 2];
        for (int i2 = 0; i2 < length; i2++) {
            int i3 = i2 * 2;
            this.mColors[i3] = obtainTypedArray.getInt(i2, 0);
            if (obtainTypedArray2.getType(i2) == 2) {
                this.mColors[i3 + 1] = Utils.getColorAttrDefaultColor(context, obtainTypedArray2.getThemeAttributeId(i2, 0));
            } else {
                this.mColors[i3 + 1] = obtainTypedArray2.getColor(i2, 0);
            }
        }
        obtainTypedArray.recycle();
        obtainTypedArray2.recycle();
        this.mWarningString = context.getString(R$string.battery_meter_very_low_overlay_symbol);
        this.mCriticalLevel = this.mContext.getResources().getInteger(17694765);
        this.mButtonHeightFraction = context.getResources().getFraction(R$fraction.battery_button_height_fraction, 1, 1);
        this.mSubpixelSmoothingLeft = context.getResources().getFraction(R$fraction.battery_subpixel_smoothing_left, 1, 1);
        this.mSubpixelSmoothingRight = context.getResources().getFraction(R$fraction.battery_subpixel_smoothing_right, 1, 1);
        Paint paint = new Paint(1);
        this.mFramePaint = paint;
        paint.setColor(i);
        paint.setDither(true);
        paint.setStrokeWidth(0.0f);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        Paint paint2 = new Paint(1);
        this.mBatteryPaint = paint2;
        paint2.setDither(true);
        paint2.setStrokeWidth(0.0f);
        paint2.setStyle(Paint.Style.FILL_AND_STROKE);
        Paint paint3 = new Paint(1);
        this.mTextPaint = paint3;
        paint3.setTypeface(Typeface.create("sans-serif-condensed", 1));
        paint3.setTextAlign(Paint.Align.CENTER);
        Paint paint4 = new Paint(1);
        this.mWarningTextPaint = paint4;
        paint4.setTypeface(Typeface.create("sans-serif", 1));
        paint4.setTextAlign(Paint.Align.CENTER);
        int[] iArr = this.mColors;
        if (iArr.length > 1) {
            paint4.setColor(iArr[1]);
        }
        this.mChargeColor = Utils.getColorStateListDefaultColor(this.mContext, R$color.meter_consumed_color);
        Paint paint5 = new Paint(1);
        this.mBoltPaint = paint5;
        paint5.setColor(Utils.getColorStateListDefaultColor(this.mContext, R$color.batterymeter_bolt_color));
        this.mBoltPoints = loadPoints(resources, R$array.batterymeter_bolt_points);
        Paint paint6 = new Paint(1);
        this.mPlusPaint = paint6;
        paint6.setColor(Utils.getColorStateListDefaultColor(this.mContext, R$color.batterymeter_plus_color));
        this.mPlusPoints = loadPoints(resources, R$array.batterymeter_plus_points);
        Paint paint7 = new Paint(1);
        this.mPowersavePaint = paint7;
        paint7.setColor(paint6.getColor());
        paint7.setStyle(Paint.Style.STROKE);
        paint7.setStrokeWidth((float) context.getResources().getDimensionPixelSize(R$dimen.battery_powersave_outline_thickness));
        this.mIntrinsicWidth = context.getResources().getDimensionPixelSize(R$dimen.battery_width);
        this.mIntrinsicHeight = context.getResources().getDimensionPixelSize(R$dimen.battery_height);
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicHeight() {
        return this.mIntrinsicHeight;
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicWidth() {
        return this.mIntrinsicWidth;
    }

    public void setBatteryLevel(int i) {
        this.mLevel = i;
        postInvalidate();
    }

    protected void postInvalidate() {
        unscheduleSelf(new Runnable() { // from class: com.android.settingslib.graph.BatteryMeterDrawableBase$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                BatteryMeterDrawableBase.this.invalidateSelf();
            }
        });
        scheduleSelf(new Runnable() { // from class: com.android.settingslib.graph.BatteryMeterDrawableBase$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                BatteryMeterDrawableBase.this.invalidateSelf();
            }
        }, 0);
    }

    private static float[] loadPoints(Resources resources, int i) {
        int[] intArray = resources.getIntArray(i);
        int i2 = 0;
        int i3 = 0;
        for (int i4 = 0; i4 < intArray.length; i4 += 2) {
            i2 = Math.max(i2, intArray[i4]);
            i3 = Math.max(i3, intArray[i4 + 1]);
        }
        float[] fArr = new float[intArray.length];
        for (int i5 = 0; i5 < intArray.length; i5 += 2) {
            fArr[i5] = ((float) intArray[i5]) / ((float) i2);
            int i6 = i5 + 1;
            fArr[i6] = ((float) intArray[i6]) / ((float) i3);
        }
        return fArr;
    }

    @Override // android.graphics.drawable.Drawable
    public void setBounds(int i, int i2, int i3, int i4) {
        super.setBounds(i, i2, i3, i4);
        updateSize();
    }

    private void updateSize() {
        Rect bounds = getBounds();
        int i = bounds.bottom;
        Rect rect = this.mPadding;
        int i2 = (i - rect.bottom) - (bounds.top + rect.top);
        this.mHeight = i2;
        this.mWidth = (bounds.right - rect.right) - (bounds.left + rect.left);
        this.mWarningTextPaint.setTextSize(((float) i2) * 0.75f);
        this.mWarningTextHeight = -this.mWarningTextPaint.getFontMetrics().ascent;
    }

    @Override // android.graphics.drawable.Drawable
    public boolean getPadding(Rect rect) {
        Rect rect2 = this.mPadding;
        if (rect2.left == 0 && rect2.top == 0 && rect2.right == 0 && rect2.bottom == 0) {
            return super.getPadding(rect);
        }
        rect.set(rect2);
        return true;
    }

    public void setPadding(int i, int i2, int i3, int i4) {
        Rect rect = this.mPadding;
        rect.left = i;
        rect.top = i2;
        rect.right = i3;
        rect.bottom = i4;
        updateSize();
    }

    private int getColorForLevel(int i) {
        int i2 = 0;
        int i3 = 0;
        while (true) {
            int[] iArr = this.mColors;
            if (i2 >= iArr.length) {
                return i3;
            }
            int i4 = iArr[i2];
            int i5 = iArr[i2 + 1];
            if (i <= i4) {
                return i2 == iArr.length + -2 ? this.mIconTint : i5;
            }
            i2 += 2;
            i3 = i5;
        }
    }

    protected int batteryColorForLevel(int i) {
        if (this.mCharging || (this.mPowerSaveEnabled && this.mPowerSaveAsColorError)) {
            return this.mChargeColor;
        }
        return getColorForLevel(i);
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        float f;
        float f2;
        float f3;
        float[] fArr;
        float[] fArr2;
        int i = this.mLevel;
        Rect bounds = getBounds();
        if (i != -1) {
            float f4 = ((float) i) / 100.0f;
            int i2 = this.mHeight;
            int aspectRatio = (int) (getAspectRatio() * ((float) this.mHeight));
            float f5 = (float) i2;
            int round = Math.round(this.mButtonHeightFraction * f5);
            Rect rect = this.mPadding;
            int i3 = rect.left + bounds.left;
            int i4 = (bounds.bottom - rect.bottom) - i2;
            float f6 = (float) i3;
            float f7 = (float) i4;
            this.mFrame.set(f6, f7, (float) (i3 + aspectRatio), (float) (i2 + i4));
            this.mFrame.offset((float) ((this.mWidth - aspectRatio) / 2), 0.0f);
            RectF rectF = this.mButtonFrame;
            float f8 = ((float) aspectRatio) * 0.28f;
            float round2 = this.mFrame.left + ((float) Math.round(f8));
            RectF rectF2 = this.mFrame;
            float f9 = (float) round;
            rectF.set(round2, rectF2.top, rectF2.right - ((float) Math.round(f8)), this.mFrame.top + f9);
            this.mFrame.top += f9;
            this.mBatteryPaint.setColor(batteryColorForLevel(i));
            if (i >= 96) {
                f4 = 1.0f;
            } else if (i <= this.mCriticalLevel) {
                f4 = 0.0f;
            }
            if (f4 == 1.0f) {
                f = this.mButtonFrame.top;
            } else {
                RectF rectF3 = this.mFrame;
                f = (rectF3.height() * (1.0f - f4)) + rectF3.top;
            }
            this.mShapePath.reset();
            this.mOutlinePath.reset();
            float radiusRatio = getRadiusRatio() * (this.mFrame.height() + f9);
            this.mShapePath.setFillType(Path.FillType.WINDING);
            this.mShapePath.addRoundRect(this.mFrame, radiusRatio, radiusRatio, Path.Direction.CW);
            this.mShapePath.addRect(this.mButtonFrame, Path.Direction.CW);
            this.mOutlinePath.addRoundRect(this.mFrame, radiusRatio, radiusRatio, Path.Direction.CW);
            Path path = new Path();
            path.addRect(this.mButtonFrame, Path.Direction.CW);
            this.mOutlinePath.op(path, Path.Op.XOR);
            boolean z = false;
            if (this.mCharging) {
                RectF rectF4 = this.mFrame;
                float width = rectF4.left + (rectF4.width() / 4.0f) + 1.0f;
                RectF rectF5 = this.mFrame;
                float height = rectF5.top + (rectF5.height() / 6.0f);
                RectF rectF6 = this.mFrame;
                float width2 = (rectF6.right - (rectF6.width() / 4.0f)) + 1.0f;
                RectF rectF7 = this.mFrame;
                float height2 = rectF7.bottom - (rectF7.height() / 10.0f);
                RectF rectF8 = this.mBoltFrame;
                if (!(rectF8.left == width && rectF8.top == height && rectF8.right == width2 && rectF8.bottom == height2)) {
                    rectF8.set(width, height, width2, height2);
                    this.mBoltPath.reset();
                    Path path2 = this.mBoltPath;
                    RectF rectF9 = this.mBoltFrame;
                    RectF rectF10 = this.mBoltFrame;
                    path2.moveTo(rectF9.left + (this.mBoltPoints[0] * rectF9.width()), rectF10.top + (this.mBoltPoints[1] * rectF10.height()));
                    int i5 = 2;
                    while (true) {
                        fArr2 = this.mBoltPoints;
                        if (i5 >= fArr2.length) {
                            break;
                        }
                        Path path3 = this.mBoltPath;
                        RectF rectF11 = this.mBoltFrame;
                        float width3 = rectF11.left + (fArr2[i5] * rectF11.width());
                        RectF rectF12 = this.mBoltFrame;
                        path3.lineTo(width3, rectF12.top + (this.mBoltPoints[i5 + 1] * rectF12.height()));
                        i5 += 2;
                    }
                    Path path4 = this.mBoltPath;
                    RectF rectF13 = this.mBoltFrame;
                    float width4 = rectF13.left + (fArr2[0] * rectF13.width());
                    RectF rectF14 = this.mBoltFrame;
                    path4.lineTo(width4, rectF14.top + (this.mBoltPoints[1] * rectF14.height()));
                }
                RectF rectF15 = this.mBoltFrame;
                float f10 = rectF15.bottom;
                if (Math.min(Math.max((f10 - f) / (f10 - rectF15.top), 0.0f), 1.0f) <= 0.3f) {
                    canvas.drawPath(this.mBoltPath, this.mBoltPaint);
                } else {
                    this.mShapePath.op(this.mBoltPath, Path.Op.DIFFERENCE);
                }
            } else if (this.mPowerSaveEnabled) {
                float width5 = (this.mFrame.width() * 2.0f) / 3.0f;
                RectF rectF16 = this.mFrame;
                float width6 = rectF16.left + ((rectF16.width() - width5) / 2.0f);
                RectF rectF17 = this.mFrame;
                float height3 = rectF17.top + ((rectF17.height() - width5) / 2.0f);
                RectF rectF18 = this.mFrame;
                float width7 = rectF18.right - ((rectF18.width() - width5) / 2.0f);
                RectF rectF19 = this.mFrame;
                float height4 = rectF19.bottom - ((rectF19.height() - width5) / 2.0f);
                RectF rectF20 = this.mPlusFrame;
                if (!(rectF20.left == width6 && rectF20.top == height3 && rectF20.right == width7 && rectF20.bottom == height4)) {
                    rectF20.set(width6, height3, width7, height4);
                    this.mPlusPath.reset();
                    Path path5 = this.mPlusPath;
                    RectF rectF21 = this.mPlusFrame;
                    RectF rectF22 = this.mPlusFrame;
                    path5.moveTo(rectF21.left + (this.mPlusPoints[0] * rectF21.width()), rectF22.top + (this.mPlusPoints[1] * rectF22.height()));
                    int i6 = 2;
                    while (true) {
                        fArr = this.mPlusPoints;
                        if (i6 >= fArr.length) {
                            break;
                        }
                        Path path6 = this.mPlusPath;
                        RectF rectF23 = this.mPlusFrame;
                        float width8 = rectF23.left + (fArr[i6] * rectF23.width());
                        RectF rectF24 = this.mPlusFrame;
                        path6.lineTo(width8, rectF24.top + (this.mPlusPoints[i6 + 1] * rectF24.height()));
                        i6 += 2;
                    }
                    Path path7 = this.mPlusPath;
                    RectF rectF25 = this.mPlusFrame;
                    float width9 = rectF25.left + (fArr[0] * rectF25.width());
                    RectF rectF26 = this.mPlusFrame;
                    path7.lineTo(width9, rectF26.top + (this.mPlusPoints[1] * rectF26.height()));
                }
                this.mShapePath.op(this.mPlusPath, Path.Op.DIFFERENCE);
                if (this.mPowerSaveAsColorError) {
                    canvas.drawPath(this.mPlusPath, this.mPlusPaint);
                }
            }
            String str = null;
            if (this.mCharging || this.mPowerSaveEnabled || i <= this.mCriticalLevel || !this.mShowPercent) {
                f3 = 0.0f;
                f2 = 0.0f;
            } else {
                this.mTextPaint.setColor(getColorForLevel(i));
                this.mTextPaint.setTextSize(f5 * (this.mLevel == 100 ? 0.38f : 0.5f));
                this.mTextHeight = -this.mTextPaint.getFontMetrics().ascent;
                str = String.valueOf(i);
                f3 = (((float) this.mWidth) * 0.5f) + f6;
                f2 = ((((float) this.mHeight) + this.mTextHeight) * 0.47f) + f7;
                if (f > f2) {
                    z = true;
                }
                if (!z) {
                    this.mTextPath.reset();
                    this.mTextPaint.getTextPath(str, 0, str.length(), f3, f2, this.mTextPath);
                    this.mShapePath.op(this.mTextPath, Path.Op.DIFFERENCE);
                }
            }
            canvas.drawPath(this.mShapePath, this.mFramePaint);
            this.mFrame.top = f;
            canvas.save();
            canvas.clipRect(this.mFrame);
            canvas.drawPath(this.mShapePath, this.mBatteryPaint);
            canvas.restore();
            if (!this.mCharging && !this.mPowerSaveEnabled) {
                if (i <= this.mCriticalLevel) {
                    canvas.drawText(this.mWarningString, (((float) this.mWidth) * 0.5f) + f6, ((((float) this.mHeight) + this.mWarningTextHeight) * 0.48f) + f7, this.mWarningTextPaint);
                } else if (z) {
                    canvas.drawText(str, f3, f2, this.mTextPaint);
                }
            }
            if (!this.mCharging && this.mPowerSaveEnabled && this.mPowerSaveAsColorError) {
                canvas.drawPath(this.mOutlinePath, this.mPowersavePaint);
            }
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
        this.mFramePaint.setColorFilter(colorFilter);
        this.mBatteryPaint.setColorFilter(colorFilter);
        this.mWarningTextPaint.setColorFilter(colorFilter);
        this.mBoltPaint.setColorFilter(colorFilter);
        this.mPlusPaint.setColorFilter(colorFilter);
    }
}
