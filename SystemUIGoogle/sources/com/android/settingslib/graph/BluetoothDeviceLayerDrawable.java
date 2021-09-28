package com.android.settingslib.graph;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import com.android.settingslib.R$color;
import com.android.settingslib.R$dimen;
import com.android.settingslib.R$fraction;
import com.android.settingslib.Utils;
/* loaded from: classes.dex */
public class BluetoothDeviceLayerDrawable extends LayerDrawable {
    private BluetoothDeviceLayerDrawableState mState;

    private BluetoothDeviceLayerDrawable(Drawable[] drawableArr) {
        super(drawableArr);
    }

    public static BluetoothDeviceLayerDrawable createLayerDrawable(Context context, int i, int i2, float f) {
        Drawable drawable = context.getDrawable(i);
        BatteryMeterDrawable batteryMeterDrawable = new BatteryMeterDrawable(context, context.getColor(R$color.meter_background_color), i2);
        int dimensionPixelSize = context.getResources().getDimensionPixelSize(R$dimen.bt_battery_padding);
        batteryMeterDrawable.setPadding(dimensionPixelSize, dimensionPixelSize, dimensionPixelSize, dimensionPixelSize);
        BluetoothDeviceLayerDrawable bluetoothDeviceLayerDrawable = new BluetoothDeviceLayerDrawable(new Drawable[]{drawable, batteryMeterDrawable});
        bluetoothDeviceLayerDrawable.setLayerGravity(0, 8388611);
        bluetoothDeviceLayerDrawable.setLayerInsetStart(1, drawable.getIntrinsicWidth());
        bluetoothDeviceLayerDrawable.setLayerInsetTop(1, (int) (((float) drawable.getIntrinsicHeight()) * (1.0f - f)));
        bluetoothDeviceLayerDrawable.setConstantState(context, i, i2, f);
        return bluetoothDeviceLayerDrawable;
    }

    public void setConstantState(Context context, int i, int i2, float f) {
        this.mState = new BluetoothDeviceLayerDrawableState(context, i, i2, f);
    }

    @Override // android.graphics.drawable.LayerDrawable, android.graphics.drawable.Drawable
    public Drawable.ConstantState getConstantState() {
        return this.mState;
    }

    /* access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class BatteryMeterDrawable extends BatteryMeterDrawableBase {
        private final float mAspectRatio;
        int mFrameColor;

        @Override // com.android.settingslib.graph.BatteryMeterDrawableBase
        protected float getRadiusRatio() {
            return 0.0f;
        }

        public BatteryMeterDrawable(Context context, int i, int i2) {
            super(context, i);
            Resources resources = context.getResources();
            this.mButtonHeightFraction = resources.getFraction(R$fraction.bt_battery_button_height_fraction, 1, 1);
            this.mAspectRatio = resources.getFraction(R$fraction.bt_battery_ratio_fraction, 1, 1);
            setColorFilter(new PorterDuffColorFilter(Utils.getColorAttrDefaultColor(context, 16843817), PorterDuff.Mode.SRC_IN));
            setBatteryLevel(i2);
            this.mFrameColor = i;
        }

        @Override // com.android.settingslib.graph.BatteryMeterDrawableBase
        protected float getAspectRatio() {
            return this.mAspectRatio;
        }
    }

    /* access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class BluetoothDeviceLayerDrawableState extends Drawable.ConstantState {
        int batteryLevel;
        Context context;
        float iconScale;
        int resId;

        @Override // android.graphics.drawable.Drawable.ConstantState
        public int getChangingConfigurations() {
            return 0;
        }

        public BluetoothDeviceLayerDrawableState(Context context, int i, int i2, float f) {
            this.context = context;
            this.resId = i;
            this.batteryLevel = i2;
            this.iconScale = f;
        }

        @Override // android.graphics.drawable.Drawable.ConstantState
        public Drawable newDrawable() {
            return BluetoothDeviceLayerDrawable.createLayerDrawable(this.context, this.resId, this.batteryLevel, this.iconScale);
        }
    }
}
