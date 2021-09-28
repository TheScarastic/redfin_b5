package com.android.systemui.settings.brightness;

import android.animation.ValueAnimator;
import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.hardware.display.BrightnessInfo;
import android.hardware.display.DisplayManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.provider.Settings;
import android.service.vr.IVrManager;
import android.service.vr.IVrStateCallbacks;
import android.util.Log;
import android.util.MathUtils;
import com.android.internal.display.BrightnessSynchronizer;
import com.android.internal.logging.MetricsLogger;
import com.android.settingslib.RestrictedLockUtilsInternal;
import com.android.settingslib.display.BrightnessUtils;
import com.android.systemui.Dependency;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.settings.CurrentUserTracker;
import com.android.systemui.settings.brightness.ToggleSlider;
import java.util.ArrayList;
import java.util.Iterator;
/* loaded from: classes.dex */
public class BrightnessController implements ToggleSlider.Listener {
    private volatile boolean mAutomatic;
    private final BrightnessObserver mBrightnessObserver;
    private final Context mContext;
    private final ToggleSlider mControl;
    private boolean mControlValueInitialized;
    private final int mDisplayId;
    private final DisplayManager mDisplayManager;
    private boolean mExternalChange;
    private final Handler mHandler;
    private volatile boolean mIsVrModeEnabled;
    private boolean mListening;
    private final float mMaximumBacklightForVr;
    private final float mMinimumBacklightForVr;
    private ValueAnimator mSliderAnimator;
    private final CurrentUserTracker mUserTracker;
    private static final Uri BRIGHTNESS_MODE_URI = Settings.System.getUriFor("screen_brightness_mode");
    private static final Uri BRIGHTNESS_FOR_VR_FLOAT_URI = Settings.System.getUriFor("screen_brightness_for_vr_float");
    private final DisplayManager.DisplayListener mDisplayListener = new DisplayManager.DisplayListener() { // from class: com.android.systemui.settings.brightness.BrightnessController.1
        @Override // android.hardware.display.DisplayManager.DisplayListener
        public void onDisplayAdded(int i) {
        }

        @Override // android.hardware.display.DisplayManager.DisplayListener
        public void onDisplayRemoved(int i) {
        }

        @Override // android.hardware.display.DisplayManager.DisplayListener
        public void onDisplayChanged(int i) {
            BrightnessController.this.mBackgroundHandler.post(BrightnessController.this.mUpdateSliderRunnable);
            BrightnessController.this.notifyCallbacks();
        }
    };
    private ArrayList<BrightnessStateChangeCallback> mChangeCallbacks = new ArrayList<>();
    private float mBrightnessMin = 0.0f;
    private float mBrightnessMax = 1.0f;
    private final Runnable mStartListeningRunnable = new Runnable() { // from class: com.android.systemui.settings.brightness.BrightnessController.2
        @Override // java.lang.Runnable
        public void run() {
            if (!BrightnessController.this.mListening) {
                BrightnessController.this.mListening = true;
                if (BrightnessController.this.mVrManager != null) {
                    try {
                        BrightnessController.this.mVrManager.registerListener(BrightnessController.this.mVrStateCallbacks);
                        BrightnessController brightnessController = BrightnessController.this;
                        brightnessController.mIsVrModeEnabled = brightnessController.mVrManager.getVrModeState();
                    } catch (RemoteException e) {
                        Log.e("StatusBar.BrightnessController", "Failed to register VR mode state listener: ", e);
                    }
                }
                BrightnessController.this.mBrightnessObserver.startObserving();
                BrightnessController.this.mUserTracker.startTracking();
                BrightnessController.this.mUpdateModeRunnable.run();
                BrightnessController.this.mUpdateSliderRunnable.run();
                BrightnessController.this.mHandler.sendEmptyMessage(2);
            }
        }
    };
    private final Runnable mStopListeningRunnable = new Runnable() { // from class: com.android.systemui.settings.brightness.BrightnessController.3
        @Override // java.lang.Runnable
        public void run() {
            if (BrightnessController.this.mListening) {
                BrightnessController.this.mListening = false;
                if (BrightnessController.this.mVrManager != null) {
                    try {
                        BrightnessController.this.mVrManager.unregisterListener(BrightnessController.this.mVrStateCallbacks);
                    } catch (RemoteException e) {
                        Log.e("StatusBar.BrightnessController", "Failed to unregister VR mode state listener: ", e);
                    }
                }
                BrightnessController.this.mBrightnessObserver.stopObserving();
                BrightnessController.this.mUserTracker.stopTracking();
                BrightnessController.this.mHandler.sendEmptyMessage(3);
            }
        }
    };
    private final Runnable mUpdateModeRunnable = new Runnable() { // from class: com.android.systemui.settings.brightness.BrightnessController.4
        @Override // java.lang.Runnable
        public void run() {
            boolean z = false;
            int intForUser = Settings.System.getIntForUser(BrightnessController.this.mContext.getContentResolver(), "screen_brightness_mode", 0, -2);
            BrightnessController brightnessController = BrightnessController.this;
            if (intForUser != 0) {
                z = true;
            }
            brightnessController.mAutomatic = z;
        }
    };
    private final Runnable mUpdateSliderRunnable = new Runnable() { // from class: com.android.systemui.settings.brightness.BrightnessController.5
        @Override // java.lang.Runnable
        public void run() {
            boolean z = BrightnessController.this.mIsVrModeEnabled;
            BrightnessInfo brightnessInfo = BrightnessController.this.mContext.getDisplay().getBrightnessInfo();
            if (brightnessInfo != null) {
                BrightnessController.this.mBrightnessMax = brightnessInfo.brightnessMaximum;
                BrightnessController.this.mBrightnessMin = brightnessInfo.brightnessMinimum;
                BrightnessController.this.mHandler.obtainMessage(1, Float.floatToIntBits(brightnessInfo.brightness), z ? 1 : 0).sendToTarget();
            }
        }
    };
    private final IVrStateCallbacks mVrStateCallbacks = new IVrStateCallbacks.Stub() { // from class: com.android.systemui.settings.brightness.BrightnessController.6
        public void onVrStateChanged(boolean z) {
            BrightnessController.this.mHandler.obtainMessage(4, z ? 1 : 0, 0).sendToTarget();
        }
    };
    private final Handler mBackgroundHandler = new Handler((Looper) Dependency.get(Dependency.BG_LOOPER));
    private final IVrManager mVrManager = IVrManager.Stub.asInterface(ServiceManager.getService("vrmanager"));

    /* loaded from: classes.dex */
    public interface BrightnessStateChangeCallback {
        void onBrightnessLevelChanged();
    }

    /* access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class BrightnessObserver extends ContentObserver {
        BrightnessObserver(Handler handler) {
            super(handler);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z, Uri uri) {
            if (!z) {
                if (BrightnessController.BRIGHTNESS_MODE_URI.equals(uri)) {
                    BrightnessController.this.mBackgroundHandler.post(BrightnessController.this.mUpdateModeRunnable);
                    BrightnessController.this.mBackgroundHandler.post(BrightnessController.this.mUpdateSliderRunnable);
                } else if (BrightnessController.BRIGHTNESS_FOR_VR_FLOAT_URI.equals(uri)) {
                    BrightnessController.this.mBackgroundHandler.post(BrightnessController.this.mUpdateSliderRunnable);
                } else {
                    BrightnessController.this.mBackgroundHandler.post(BrightnessController.this.mUpdateModeRunnable);
                    BrightnessController.this.mBackgroundHandler.post(BrightnessController.this.mUpdateSliderRunnable);
                }
                BrightnessController.this.notifyCallbacks();
            }
        }

        public void startObserving() {
            ContentResolver contentResolver = BrightnessController.this.mContext.getContentResolver();
            contentResolver.unregisterContentObserver(this);
            contentResolver.registerContentObserver(BrightnessController.BRIGHTNESS_MODE_URI, false, this, -1);
            contentResolver.registerContentObserver(BrightnessController.BRIGHTNESS_FOR_VR_FLOAT_URI, false, this, -1);
            BrightnessController.this.mDisplayManager.registerDisplayListener(BrightnessController.this.mDisplayListener, BrightnessController.this.mHandler, 8);
        }

        public void stopObserving() {
            BrightnessController.this.mContext.getContentResolver().unregisterContentObserver(this);
            BrightnessController.this.mDisplayManager.unregisterDisplayListener(BrightnessController.this.mDisplayListener);
        }
    }

    public BrightnessController(Context context, ToggleSlider toggleSlider, BroadcastDispatcher broadcastDispatcher) {
        AnonymousClass7 r0 = new Handler() { // from class: com.android.systemui.settings.brightness.BrightnessController.7
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                boolean z = true;
                BrightnessController.this.mExternalChange = true;
                try {
                    int i = message.what;
                    if (i == 1) {
                        BrightnessController brightnessController = BrightnessController.this;
                        float intBitsToFloat = Float.intBitsToFloat(message.arg1);
                        if (message.arg2 == 0) {
                            z = false;
                        }
                        brightnessController.updateSlider(intBitsToFloat, z);
                    } else if (i == 2) {
                        BrightnessController.this.mControl.setOnChangedListener(BrightnessController.this);
                    } else if (i == 3) {
                        BrightnessController.this.mControl.setOnChangedListener(null);
                    } else if (i != 4) {
                        super.handleMessage(message);
                    } else {
                        BrightnessController brightnessController2 = BrightnessController.this;
                        if (message.arg1 == 0) {
                            z = false;
                        }
                        brightnessController2.updateVrMode(z);
                    }
                } finally {
                    BrightnessController.this.mExternalChange = false;
                }
            }
        };
        this.mHandler = r0;
        this.mContext = context;
        this.mControl = toggleSlider;
        toggleSlider.setMax(65535);
        this.mUserTracker = new CurrentUserTracker(broadcastDispatcher) { // from class: com.android.systemui.settings.brightness.BrightnessController.8
            @Override // com.android.systemui.settings.CurrentUserTracker
            public void onUserSwitched(int i) {
                BrightnessController.this.mBackgroundHandler.post(BrightnessController.this.mUpdateModeRunnable);
                BrightnessController.this.mBackgroundHandler.post(BrightnessController.this.mUpdateSliderRunnable);
            }
        };
        this.mBrightnessObserver = new BrightnessObserver(r0);
        this.mDisplayId = context.getDisplayId();
        PowerManager powerManager = (PowerManager) context.getSystemService(PowerManager.class);
        this.mMinimumBacklightForVr = powerManager.getBrightnessConstraint(5);
        this.mMaximumBacklightForVr = powerManager.getBrightnessConstraint(6);
        this.mDisplayManager = (DisplayManager) context.getSystemService(DisplayManager.class);
    }

    public void registerCallbacks() {
        this.mBackgroundHandler.post(this.mStartListeningRunnable);
    }

    public void unregisterCallbacks() {
        this.mBackgroundHandler.post(this.mStopListeningRunnable);
        this.mControlValueInitialized = false;
    }

    @Override // com.android.systemui.settings.brightness.ToggleSlider.Listener
    public void onChanged(boolean z, int i, boolean z2) {
        float f;
        float f2;
        int i2;
        if (!this.mExternalChange) {
            ValueAnimator valueAnimator = this.mSliderAnimator;
            if (valueAnimator != null) {
                valueAnimator.cancel();
            }
            if (this.mIsVrModeEnabled) {
                i2 = 498;
                f2 = this.mMinimumBacklightForVr;
                f = this.mMaximumBacklightForVr;
            } else {
                i2 = this.mAutomatic ? 219 : 218;
                f2 = this.mBrightnessMin;
                f = this.mBrightnessMax;
            }
            final float min = MathUtils.min(BrightnessUtils.convertGammaToLinearFloat(i, f2, f), f);
            if (z2) {
                MetricsLogger.action(this.mContext, i2, BrightnessSynchronizer.brightnessFloatToInt(min));
            }
            setBrightness(min);
            if (!z) {
                AsyncTask.execute(new Runnable() { // from class: com.android.systemui.settings.brightness.BrightnessController.9
                    @Override // java.lang.Runnable
                    public void run() {
                        BrightnessController.this.mDisplayManager.setBrightness(BrightnessController.this.mDisplayId, min);
                    }
                });
            }
            Iterator<BrightnessStateChangeCallback> it = this.mChangeCallbacks.iterator();
            while (it.hasNext()) {
                it.next().onBrightnessLevelChanged();
            }
        }
    }

    public void checkRestrictionAndSetEnabled() {
        this.mBackgroundHandler.post(new Runnable() { // from class: com.android.systemui.settings.brightness.BrightnessController.10
            @Override // java.lang.Runnable
            public void run() {
                BrightnessController.this.mControl.setEnforcedAdmin(RestrictedLockUtilsInternal.checkIfRestrictionEnforced(BrightnessController.this.mContext, "no_config_brightness", BrightnessController.this.mUserTracker.getCurrentUserId()));
            }
        });
    }

    private void setBrightness(float f) {
        this.mDisplayManager.setTemporaryBrightness(this.mDisplayId, f);
    }

    /* access modifiers changed from: private */
    public void updateVrMode(boolean z) {
        if (this.mIsVrModeEnabled != z) {
            this.mIsVrModeEnabled = z;
            this.mBackgroundHandler.post(this.mUpdateSliderRunnable);
        }
    }

    /* access modifiers changed from: private */
    public void updateSlider(float f, boolean z) {
        float f2;
        float f3;
        if (z) {
            f2 = this.mMinimumBacklightForVr;
            f3 = this.mMaximumBacklightForVr;
        } else {
            f2 = this.mBrightnessMin;
            f3 = this.mBrightnessMax;
        }
        if (!BrightnessSynchronizer.floatEquals(f, BrightnessUtils.convertGammaToLinearFloat(this.mControl.getValue(), f2, f3))) {
            animateSliderTo(BrightnessUtils.convertLinearToGammaFloat(f, f2, f3));
        }
    }

    private void animateSliderTo(int i) {
        if (!this.mControlValueInitialized) {
            this.mControl.setValue(i);
            this.mControlValueInitialized = true;
        }
        ValueAnimator valueAnimator = this.mSliderAnimator;
        if (valueAnimator != null && valueAnimator.isStarted()) {
            this.mSliderAnimator.cancel();
        }
        ValueAnimator ofInt = ValueAnimator.ofInt(this.mControl.getValue(), i);
        this.mSliderAnimator = ofInt;
        ofInt.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.settings.brightness.BrightnessController$$ExternalSyntheticLambda0
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                BrightnessController.this.lambda$animateSliderTo$0(valueAnimator2);
            }
        });
        this.mSliderAnimator.setDuration((long) ((Math.abs(this.mControl.getValue() - i) * 3000) / 65535));
        this.mSliderAnimator.start();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$animateSliderTo$0(ValueAnimator valueAnimator) {
        this.mExternalChange = true;
        this.mControl.setValue(((Integer) valueAnimator.getAnimatedValue()).intValue());
        this.mExternalChange = false;
    }

    /* access modifiers changed from: private */
    public void notifyCallbacks() {
        int size = this.mChangeCallbacks.size();
        for (int i = 0; i < size; i++) {
            this.mChangeCallbacks.get(i).onBrightnessLevelChanged();
        }
    }

    /* loaded from: classes.dex */
    public static class Factory {
        private final BroadcastDispatcher mBroadcastDispatcher;
        private final Context mContext;

        public Factory(Context context, BroadcastDispatcher broadcastDispatcher) {
            this.mContext = context;
            this.mBroadcastDispatcher = broadcastDispatcher;
        }

        public BrightnessController create(ToggleSlider toggleSlider) {
            return new BrightnessController(this.mContext, toggleSlider, this.mBroadcastDispatcher);
        }
    }
}
