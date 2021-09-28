package com.android.systemui.biometrics;

import android.graphics.Insets;
import android.graphics.Rect;
import android.hardware.fingerprint.FingerprintSensorPropertiesInternal;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.FrameLayout;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.R$dimen;
import com.android.systemui.R$id;
import com.android.systemui.biometrics.AuthDialog;
/* loaded from: classes.dex */
public class UdfpsDialogMeasureAdapter {
    private final FingerprintSensorPropertiesInternal mSensorProps;
    private final ViewGroup mView;
    private WindowManager mWindowManager;

    public UdfpsDialogMeasureAdapter(ViewGroup viewGroup, FingerprintSensorPropertiesInternal fingerprintSensorPropertiesInternal) {
        this.mView = viewGroup;
        this.mSensorProps = fingerprintSensorPropertiesInternal;
    }

    /* access modifiers changed from: package-private */
    public FingerprintSensorPropertiesInternal getSensorProps() {
        return this.mSensorProps;
    }

    /* access modifiers changed from: package-private */
    public AuthDialog.LayoutParams onMeasureInternal(int i, int i2, AuthDialog.LayoutParams layoutParams) {
        int rotation = this.mView.getDisplay().getRotation();
        if (rotation == 0) {
            return onMeasureInternalPortrait(i, i2);
        }
        if (rotation == 1 || rotation == 3) {
            return onMeasureInternalLandscape(i, i2);
        }
        Log.e("UdfpsDialogMeasurementAdapter", "Unsupported display rotation: " + rotation);
        return layoutParams;
    }

    private AuthDialog.LayoutParams onMeasureInternalPortrait(int i, int i2) {
        int calculateBottomSpacerHeightForPortrait = calculateBottomSpacerHeightForPortrait(this.mSensorProps, getWindowBounds().height(), getViewHeightPx(R$id.indicator), getViewHeightPx(R$id.button_bar), getDialogMarginPx(), getNavbarInsets().bottom);
        int childCount = this.mView.getChildCount();
        int i3 = this.mSensorProps.sensorRadius * 2;
        int i4 = 0;
        for (int i5 = 0; i5 < childCount; i5++) {
            View childAt = this.mView.getChildAt(i5);
            if (childAt.getId() == R$id.biometric_icon_frame) {
                FrameLayout frameLayout = (FrameLayout) childAt;
                frameLayout.getChildAt(0).measure(View.MeasureSpec.makeMeasureSpec(i3, Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(i3, Integer.MIN_VALUE));
                frameLayout.measure(View.MeasureSpec.makeMeasureSpec(childAt.getLayoutParams().width, 1073741824), View.MeasureSpec.makeMeasureSpec(i3, 1073741824));
            } else if (childAt.getId() == R$id.space_above_icon) {
                childAt.measure(View.MeasureSpec.makeMeasureSpec(i, 1073741824), View.MeasureSpec.makeMeasureSpec(childAt.getLayoutParams().height, 1073741824));
            } else if (childAt.getId() == R$id.button_bar) {
                childAt.measure(View.MeasureSpec.makeMeasureSpec(i, 1073741824), View.MeasureSpec.makeMeasureSpec(childAt.getLayoutParams().height, 1073741824));
            } else if (childAt.getId() == R$id.space_below_icon) {
                childAt.measure(View.MeasureSpec.makeMeasureSpec(i, 1073741824), View.MeasureSpec.makeMeasureSpec(calculateBottomSpacerHeightForPortrait, 1073741824));
            } else {
                childAt.measure(View.MeasureSpec.makeMeasureSpec(i, 1073741824), View.MeasureSpec.makeMeasureSpec(i2, Integer.MIN_VALUE));
            }
            if (childAt.getVisibility() != 8) {
                i4 += childAt.getMeasuredHeight();
            }
        }
        return new AuthDialog.LayoutParams(i, i4);
    }

    private AuthDialog.LayoutParams onMeasureInternalLandscape(int i, int i2) {
        int viewHeightPx = getViewHeightPx(R$id.title);
        int viewHeightPx2 = getViewHeightPx(R$id.subtitle);
        int viewHeightPx3 = getViewHeightPx(R$id.description);
        int viewHeightPx4 = getViewHeightPx(R$id.space_above_icon);
        int viewHeightPx5 = getViewHeightPx(R$id.indicator);
        int viewHeightPx6 = getViewHeightPx(R$id.button_bar);
        Insets navbarInsets = getNavbarInsets();
        int calculateBottomSpacerHeightForLandscape = calculateBottomSpacerHeightForLandscape(viewHeightPx, viewHeightPx2, viewHeightPx3, viewHeightPx4, viewHeightPx5, viewHeightPx6, navbarInsets.bottom);
        int calculateHorizontalSpacerWidthForLandscape = calculateHorizontalSpacerWidthForLandscape(this.mSensorProps, getWindowBounds().width(), getDialogMarginPx(), navbarInsets.left + navbarInsets.right);
        int i3 = this.mSensorProps.sensorRadius * 2;
        int i4 = (calculateHorizontalSpacerWidthForLandscape * 2) + i3;
        int childCount = this.mView.getChildCount();
        int i5 = 0;
        for (int i6 = 0; i6 < childCount; i6++) {
            View childAt = this.mView.getChildAt(i6);
            if (childAt.getId() == R$id.biometric_icon_frame) {
                FrameLayout frameLayout = (FrameLayout) childAt;
                frameLayout.getChildAt(0).measure(View.MeasureSpec.makeMeasureSpec(i3, Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(i3, Integer.MIN_VALUE));
                frameLayout.measure(View.MeasureSpec.makeMeasureSpec(i4, 1073741824), View.MeasureSpec.makeMeasureSpec(i3, 1073741824));
            } else if (childAt.getId() == R$id.space_above_icon) {
                childAt.measure(View.MeasureSpec.makeMeasureSpec(i4, 1073741824), View.MeasureSpec.makeMeasureSpec(childAt.getLayoutParams().height - Math.min(calculateBottomSpacerHeightForLandscape, 0), 1073741824));
            } else if (childAt.getId() == R$id.button_bar) {
                childAt.measure(View.MeasureSpec.makeMeasureSpec(i4, 1073741824), View.MeasureSpec.makeMeasureSpec(childAt.getLayoutParams().height, 1073741824));
            } else if (childAt.getId() == R$id.space_below_icon) {
                childAt.measure(View.MeasureSpec.makeMeasureSpec(i4, 1073741824), View.MeasureSpec.makeMeasureSpec(Math.max(calculateBottomSpacerHeightForLandscape, 0), 1073741824));
            } else {
                childAt.measure(View.MeasureSpec.makeMeasureSpec(i4, 1073741824), View.MeasureSpec.makeMeasureSpec(i2, Integer.MIN_VALUE));
            }
            if (childAt.getVisibility() != 8) {
                i5 += childAt.getMeasuredHeight();
            }
        }
        return new AuthDialog.LayoutParams(i4, i5);
    }

    private int getViewHeightPx(int i) {
        View findViewById = this.mView.findViewById(i);
        if (findViewById == null || findViewById.getVisibility() == 8) {
            return 0;
        }
        return findViewById.getMeasuredHeight();
    }

    private int getDialogMarginPx() {
        return this.mView.getResources().getDimensionPixelSize(R$dimen.biometric_dialog_border_padding);
    }

    private Insets getNavbarInsets() {
        WindowManager windowManager = getWindowManager();
        if (windowManager == null || windowManager.getCurrentWindowMetrics() == null) {
            return Insets.NONE;
        }
        return windowManager.getCurrentWindowMetrics().getWindowInsets().getInsets(WindowInsets.Type.navigationBars());
    }

    private Rect getWindowBounds() {
        WindowManager windowManager = getWindowManager();
        if (windowManager == null || windowManager.getCurrentWindowMetrics() == null) {
            return new Rect();
        }
        return windowManager.getCurrentWindowMetrics().getBounds();
    }

    private WindowManager getWindowManager() {
        if (this.mWindowManager == null) {
            this.mWindowManager = (WindowManager) this.mView.getContext().getSystemService(WindowManager.class);
        }
        return this.mWindowManager;
    }

    @VisibleForTesting
    static int calculateBottomSpacerHeightForPortrait(FingerprintSensorPropertiesInternal fingerprintSensorPropertiesInternal, int i, int i2, int i3, int i4, int i5) {
        int i6 = (i - fingerprintSensorPropertiesInternal.sensorLocationY) - fingerprintSensorPropertiesInternal.sensorRadius;
        int i7 = (((i6 - i2) - i3) - i4) - i5;
        Log.d("UdfpsDialogMeasurementAdapter", "Display height: " + i + ", Distance from bottom: " + i6 + ", Bottom margin: " + i4 + ", Navbar bottom inset: " + i5 + ", Bottom spacer height (portrait): " + i7);
        return i7;
    }

    @VisibleForTesting
    static int calculateBottomSpacerHeightForLandscape(int i, int i2, int i3, int i4, int i5, int i6, int i7) {
        int i8 = ((((i + i2) + i3) + i4) - (i5 + i6)) - i7;
        Log.d("UdfpsDialogMeasurementAdapter", "Title height: " + i + ", Subtitle height: " + i2 + ", Description height: " + i3 + ", Top spacer height: " + i4 + ", Text indicator height: " + i5 + ", Button bar height: " + i6 + ", Navbar bottom inset: " + i7 + ", Bottom spacer height (landscape): " + i8);
        return i8;
    }

    @VisibleForTesting
    static int calculateHorizontalSpacerWidthForLandscape(FingerprintSensorPropertiesInternal fingerprintSensorPropertiesInternal, int i, int i2, int i3) {
        int i4 = (i - fingerprintSensorPropertiesInternal.sensorLocationY) - fingerprintSensorPropertiesInternal.sensorRadius;
        int i5 = (i4 - i2) - i3;
        Log.d("UdfpsDialogMeasurementAdapter", "Display width: " + i + ", Distance from edge: " + i4 + ", Dialog margin: " + i2 + ", Navbar horizontal inset: " + i3 + ", Horizontal spacer width (landscape): " + i5);
        return i5;
    }
}
