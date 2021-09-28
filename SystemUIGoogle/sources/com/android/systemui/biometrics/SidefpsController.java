package com.android.systemui.biometrics;

import android.content.Context;
import android.hardware.display.DisplayManager;
import android.hardware.fingerprint.FingerprintManager;
import android.hardware.fingerprint.FingerprintSensorPropertiesInternal;
import android.hardware.fingerprint.ISidefpsController;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.WindowManager;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.Preconditions;
import com.android.systemui.R$layout;
import com.android.systemui.util.concurrency.DelayableExecutor;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
/* loaded from: classes.dex */
public class SidefpsController {
    private final Context mContext;
    private final WindowManager.LayoutParams mCoreLayoutParams;
    private final int mDisplayHeight;
    private final int mDisplayWidth;
    private final DelayableExecutor mFgExecutor;
    private final FingerprintManager mFingerprintManager;
    private final LayoutInflater mInflater;
    private boolean mIsVisible = false;
    @VisibleForTesting
    final BiometricOrientationEventListener mOrientationListener;
    @VisibleForTesting
    final FingerprintSensorPropertiesInternal mSensorProps;
    private final ISidefpsController mSidefpsControllerImpl;
    private SidefpsView mView;
    private final WindowManager mWindowManager;

    private int getCoreLayoutParamFlags() {
        return 16777512;
    }

    public SidefpsController(Context context, LayoutInflater layoutInflater, FingerprintManager fingerprintManager, WindowManager windowManager, DelayableExecutor delayableExecutor, DisplayManager displayManager, Handler handler) {
        ISidefpsController r1 = new ISidefpsController.Stub() { // from class: com.android.systemui.biometrics.SidefpsController.1
            public void show() {
                SidefpsController.this.mFgExecutor.execute(new SidefpsController$1$$ExternalSyntheticLambda1(this));
            }

            /* access modifiers changed from: private */
            public /* synthetic */ void lambda$show$0() {
                SidefpsController.this.show();
                SidefpsController.this.mIsVisible = true;
            }

            public void hide() {
                SidefpsController.this.mFgExecutor.execute(new SidefpsController$1$$ExternalSyntheticLambda0(this));
            }

            /* access modifiers changed from: private */
            public /* synthetic */ void lambda$hide$1() {
                SidefpsController.this.hide();
                SidefpsController.this.mIsVisible = false;
            }
        };
        this.mSidefpsControllerImpl = r1;
        this.mContext = context;
        this.mInflater = layoutInflater;
        FingerprintManager fingerprintManager2 = (FingerprintManager) Preconditions.checkNotNull(fingerprintManager);
        this.mFingerprintManager = fingerprintManager2;
        this.mWindowManager = windowManager;
        this.mFgExecutor = delayableExecutor;
        this.mOrientationListener = new BiometricOrientationEventListener(context, new Function0() { // from class: com.android.systemui.biometrics.SidefpsController$$ExternalSyntheticLambda0
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return SidefpsController.this.lambda$new$0();
            }
        }, displayManager, handler);
        FingerprintSensorPropertiesInternal findFirstSidefps = findFirstSidefps();
        this.mSensorProps = findFirstSidefps;
        Preconditions.checkArgument(findFirstSidefps != null);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(2009, getCoreLayoutParamFlags(), -3);
        this.mCoreLayoutParams = layoutParams;
        layoutParams.setTitle("SidefpsController");
        layoutParams.setFitInsetsTypes(0);
        layoutParams.gravity = 51;
        layoutParams.layoutInDisplayCutoutMode = 3;
        layoutParams.privateFlags = 536870912;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        this.mDisplayHeight = displayMetrics.heightPixels;
        this.mDisplayWidth = displayMetrics.widthPixels;
        fingerprintManager2.setSidefpsController(r1);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ Unit lambda$new$0() {
        onOrientationChanged();
        return Unit.INSTANCE;
    }

    /* access modifiers changed from: private */
    public void show() {
        SidefpsView sidefpsView = (SidefpsView) this.mInflater.inflate(R$layout.sidefps_view, (ViewGroup) null, false);
        this.mView = sidefpsView;
        sidefpsView.setSensorProperties(this.mSensorProps);
        this.mWindowManager.addView(this.mView, computeLayoutParams());
        this.mOrientationListener.enable();
    }

    /* access modifiers changed from: private */
    public void hide() {
        SidefpsView sidefpsView = this.mView;
        if (sidefpsView != null) {
            this.mWindowManager.removeView(sidefpsView);
            this.mView.setOnTouchListener(null);
            this.mView.setOnHoverListener(null);
            this.mView = null;
        } else {
            Log.v("SidefpsController", "hideUdfpsOverlay | the overlay is already hidden");
        }
        this.mOrientationListener.disable();
    }

    private void onOrientationChanged() {
        if (this.mView != null && this.mIsVisible) {
            hide();
            show();
        }
    }

    private FingerprintSensorPropertiesInternal findFirstSidefps() {
        for (FingerprintSensorPropertiesInternal fingerprintSensorPropertiesInternal : this.mFingerprintManager.getSensorPropertiesInternal()) {
            if (fingerprintSensorPropertiesInternal.isAnySidefpsType()) {
                return new FingerprintSensorPropertiesInternal(fingerprintSensorPropertiesInternal.sensorId, fingerprintSensorPropertiesInternal.sensorStrength, fingerprintSensorPropertiesInternal.maxEnrollmentsPerUser, fingerprintSensorPropertiesInternal.componentInfo, fingerprintSensorPropertiesInternal.sensorType, fingerprintSensorPropertiesInternal.resetLockoutRequiresHardwareAuthToken, 25, 610, 112);
            }
        }
        return null;
    }

    private WindowManager.LayoutParams computeLayoutParams() {
        this.mCoreLayoutParams.flags = getCoreLayoutParamFlags();
        FingerprintSensorPropertiesInternal fingerprintSensorPropertiesInternal = this.mSensorProps;
        int i = fingerprintSensorPropertiesInternal.sensorLocationY;
        int i2 = fingerprintSensorPropertiesInternal.sensorRadius;
        int i3 = i - i2;
        int i4 = i2 * 2;
        int rotation = this.mContext.getDisplay().getRotation();
        if (rotation == 1) {
            WindowManager.LayoutParams layoutParams = this.mCoreLayoutParams;
            layoutParams.x = i3;
            layoutParams.y = 0;
            layoutParams.height = 50;
            layoutParams.width = i4;
        } else if (rotation != 3) {
            WindowManager.LayoutParams layoutParams2 = this.mCoreLayoutParams;
            layoutParams2.x = this.mDisplayWidth - 50;
            layoutParams2.y = i3;
            layoutParams2.height = i4;
            layoutParams2.width = 50;
        } else {
            WindowManager.LayoutParams layoutParams3 = this.mCoreLayoutParams;
            layoutParams3.x = (this.mDisplayHeight - i3) - i4;
            layoutParams3.y = this.mDisplayWidth - 50;
            layoutParams3.height = 50;
            layoutParams3.width = i4;
        }
        return this.mCoreLayoutParams;
    }
}
