package com.android.systemui.tv;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import com.android.systemui.R$anim;
import com.android.systemui.R$dimen;
import com.android.systemui.R$drawable;
import com.android.systemui.R$layout;
import java.util.function.Consumer;
/* loaded from: classes2.dex */
public abstract class TvBottomSheetActivity extends Activity {
    private static final String TAG = "TvBottomSheetActivity";
    private Drawable mBackgroundWithBlur;
    private Drawable mBackgroundWithoutBlur;
    private final Consumer<Boolean> mBlurConsumer = new Consumer() { // from class: com.android.systemui.tv.TvBottomSheetActivity$$ExternalSyntheticLambda0
        @Override // java.util.function.Consumer
        public final void accept(Object obj) {
            TvBottomSheetActivity.this.onBlurChanged(((Boolean) obj).booleanValue());
        }
    };

    /* access modifiers changed from: private */
    public void onBlurChanged(boolean z) {
        String str = TAG;
        Log.v(str, "blur enabled: " + z);
        getWindow().setBackgroundDrawable(z ? this.mBackgroundWithBlur : this.mBackgroundWithoutBlur);
    }

    @Override // android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R$layout.tv_bottom_sheet);
        overridePendingTransition(R$anim.tv_bottom_sheet_enter, 0);
        this.mBackgroundWithBlur = getResources().getDrawable(R$drawable.bottom_sheet_background_with_blur);
        this.mBackgroundWithoutBlur = getResources().getDrawable(R$drawable.bottom_sheet_background);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int i = displayMetrics.widthPixels;
        int i2 = displayMetrics.heightPixels;
        int dimensionPixelSize = getResources().getDimensionPixelSize(R$dimen.bottom_sheet_margin);
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.width = i - (dimensionPixelSize * 2);
        attributes.height = -2;
        attributes.gravity = 81;
        attributes.horizontalMargin = 0.0f;
        attributes.verticalMargin = ((float) dimensionPixelSize) / ((float) i2);
        attributes.format = -2;
        attributes.type = 2008;
        int i3 = attributes.flags | 128;
        attributes.flags = i3;
        attributes.flags = i3 | 16777216;
        getWindow().setAttributes(attributes);
        getWindow().setElevation(getWindow().getElevation() + 5.0f);
        getWindow().setBackgroundBlurRadius(getResources().getDimensionPixelSize(R$dimen.bottom_sheet_background_blur_radius));
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        getWindowManager().addCrossWindowBlurEnabledListener(this.mBlurConsumer);
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public void onDetachedFromWindow() {
        getWindowManager().removeCrossWindowBlurEnabledListener(this.mBlurConsumer);
        super.onDetachedFromWindow();
    }

    @Override // android.app.Activity
    public void finish() {
        super.finish();
        overridePendingTransition(0, R$anim.tv_bottom_sheet_exit);
    }
}
