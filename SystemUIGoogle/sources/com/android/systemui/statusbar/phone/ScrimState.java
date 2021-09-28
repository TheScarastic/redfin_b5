package com.android.systemui.statusbar.phone;

import android.graphics.Color;
import android.os.Trace;
import com.android.systemui.dock.DockManager;
import com.android.systemui.scrim.ScrimView;
/* loaded from: classes.dex */
public enum ScrimState {
    UNINITIALIZED,
    OFF {
        @Override // com.android.systemui.statusbar.phone.ScrimState
        public boolean isLowPowerState() {
            return true;
        }

        @Override // com.android.systemui.statusbar.phone.ScrimState
        public void prepare(ScrimState scrimState) {
            this.mFrontTint = -16777216;
            this.mBehindTint = -16777216;
            this.mBubbleTint = scrimState.mBubbleTint;
            this.mFrontAlpha = 1.0f;
            this.mBehindAlpha = 1.0f;
            this.mBubbleAlpha = scrimState.mBubbleAlpha;
            this.mAnimationDuration = 1000;
        }
    },
    KEYGUARD {
        @Override // com.android.systemui.statusbar.phone.ScrimState
        public void prepare(ScrimState scrimState) {
            float f;
            this.mBlankScreen = false;
            if (scrimState == ScrimState.AOD) {
                this.mAnimationDuration = 500;
                if (this.mDisplayRequiresBlanking) {
                    this.mBlankScreen = true;
                }
            } else if (scrimState == ScrimState.KEYGUARD) {
                this.mAnimationDuration = 500;
            } else {
                this.mAnimationDuration = 220;
            }
            this.mFrontTint = -16777216;
            this.mBehindTint = -16777216;
            boolean z = this.mClipQsScrim;
            this.mNotifTint = z ? -16777216 : 0;
            this.mBubbleTint = 0;
            this.mFrontAlpha = 0.0f;
            if (z) {
                f = 1.0f;
            } else {
                f = this.mScrimBehindAlphaKeyguard;
            }
            this.mBehindAlpha = f;
            this.mNotifAlpha = z ? this.mScrimBehindAlphaKeyguard : 0.0f;
            this.mBubbleAlpha = 0.0f;
            if (z) {
                updateScrimColor(this.mScrimBehind, 1.0f, -16777216);
            }
        }
    },
    AUTH_SCRIMMED {
        @Override // com.android.systemui.statusbar.phone.ScrimState
        public void prepare(ScrimState scrimState) {
            this.mNotifTint = scrimState.mNotifTint;
            this.mNotifAlpha = scrimState.mNotifAlpha;
            this.mBehindTint = scrimState.mBehindTint;
            this.mBehindAlpha = scrimState.mBehindAlpha;
            this.mFrontTint = -16777216;
            this.mFrontAlpha = 0.66f;
        }
    },
    BOUNCER {
        @Override // com.android.systemui.statusbar.phone.ScrimState
        public void prepare(ScrimState scrimState) {
            boolean z = this.mClipQsScrim;
            this.mBehindAlpha = z ? 1.0f : this.mDefaultScrimAlpha;
            this.mBehindTint = z ? -16777216 : 0;
            this.mNotifAlpha = z ? this.mDefaultScrimAlpha : 0.0f;
            this.mNotifTint = 0;
            this.mFrontAlpha = 0.0f;
            this.mBubbleAlpha = 0.0f;
        }
    },
    BOUNCER_SCRIMMED {
        @Override // com.android.systemui.statusbar.phone.ScrimState
        public void prepare(ScrimState scrimState) {
            this.mBehindAlpha = 0.0f;
            this.mBubbleAlpha = 0.0f;
            this.mFrontAlpha = this.mDefaultScrimAlpha;
        }
    },
    SHADE_LOCKED {
        @Override // com.android.systemui.statusbar.phone.ScrimState
        public int getBehindTint() {
            return -16777216;
        }

        @Override // com.android.systemui.statusbar.phone.ScrimState
        public void prepare(ScrimState scrimState) {
            boolean z = this.mClipQsScrim;
            this.mBehindAlpha = z ? 1.0f : this.mDefaultScrimAlpha;
            this.mNotifAlpha = 1.0f;
            this.mBubbleAlpha = 0.0f;
            this.mFrontAlpha = 0.0f;
            this.mBehindTint = -16777216;
            if (z) {
                updateScrimColor(this.mScrimBehind, 1.0f, -16777216);
            }
        }
    },
    BRIGHTNESS_MIRROR {
        @Override // com.android.systemui.statusbar.phone.ScrimState
        public void prepare(ScrimState scrimState) {
            this.mBehindAlpha = 0.0f;
            this.mFrontAlpha = 0.0f;
            this.mBubbleAlpha = 0.0f;
        }
    },
    AOD {
        @Override // com.android.systemui.statusbar.phone.ScrimState
        public boolean isLowPowerState() {
            return true;
        }

        @Override // com.android.systemui.statusbar.phone.ScrimState
        public void prepare(ScrimState scrimState) {
            float f;
            boolean alwaysOn = this.mDozeParameters.getAlwaysOn();
            boolean isQuickPickupEnabled = this.mDozeParameters.isQuickPickupEnabled();
            boolean isDocked = this.mDockManager.isDocked();
            this.mBlankScreen = this.mDisplayRequiresBlanking;
            this.mFrontTint = -16777216;
            if (alwaysOn || isDocked || isQuickPickupEnabled) {
                f = this.mAodFrontScrimAlpha;
            } else {
                f = 1.0f;
            }
            this.mFrontAlpha = f;
            this.mBehindTint = -16777216;
            this.mBehindAlpha = 0.0f;
            boolean z = false;
            this.mBubbleTint = 0;
            this.mBubbleAlpha = 0.0f;
            this.mAnimationDuration = 1000;
            if (this.mDozeParameters.shouldControlScreenOff() && !this.mDozeParameters.shouldControlUnlockedScreenOff()) {
                z = true;
            }
            this.mAnimateChange = z;
        }

        @Override // com.android.systemui.statusbar.phone.ScrimState
        public float getMaxLightRevealScrimAlpha() {
            return (!this.mWallpaperSupportsAmbientMode || this.mHasBackdrop) ? 1.0f : 0.0f;
        }
    },
    PULSING {
        @Override // com.android.systemui.statusbar.phone.ScrimState
        public void prepare(ScrimState scrimState) {
            this.mFrontAlpha = this.mAodFrontScrimAlpha;
            this.mBubbleAlpha = 0.0f;
            this.mBehindTint = -16777216;
            this.mFrontTint = -16777216;
            this.mBlankScreen = this.mDisplayRequiresBlanking;
            this.mAnimationDuration = this.mWakeLockScreenSensorActive ? 1000 : 220;
        }

        @Override // com.android.systemui.statusbar.phone.ScrimState
        public float getMaxLightRevealScrimAlpha() {
            if (this.mWakeLockScreenSensorActive) {
                return 0.6f;
            }
            return ScrimState.AOD.getMaxLightRevealScrimAlpha();
        }
    },
    UNLOCKED {
        @Override // com.android.systemui.statusbar.phone.ScrimState
        public void prepare(ScrimState scrimState) {
            this.mBehindAlpha = this.mClipQsScrim ? 1.0f : 0.0f;
            this.mNotifAlpha = 0.0f;
            this.mFrontAlpha = 0.0f;
            this.mBubbleAlpha = 0.0f;
            this.mAnimationDuration = this.mKeyguardFadingAway ? this.mKeyguardFadingAwayDuration : 300;
            this.mAnimateChange = !this.mLaunchingAffordanceWithPreview;
            this.mFrontTint = 0;
            this.mBehindTint = -16777216;
            this.mBubbleTint = 0;
            this.mBlankScreen = false;
            if (scrimState == ScrimState.AOD) {
                updateScrimColor(this.mScrimInFront, 1.0f, -16777216);
                updateScrimColor(this.mScrimBehind, 1.0f, -16777216);
                ScrimView scrimView = this.mScrimForBubble;
                if (scrimView != null) {
                    updateScrimColor(scrimView, 1.0f, -16777216);
                }
                this.mFrontTint = -16777216;
                this.mBehindTint = -16777216;
                this.mBubbleTint = -16777216;
                this.mBlankScreen = true;
            }
            if (this.mClipQsScrim) {
                updateScrimColor(this.mScrimBehind, 1.0f, -16777216);
            }
        }
    },
    BUBBLE_EXPANDED {
        @Override // com.android.systemui.statusbar.phone.ScrimState
        public void prepare(ScrimState scrimState) {
            this.mFrontTint = 0;
            this.mBehindTint = 0;
            this.mBubbleTint = -16777216;
            this.mFrontAlpha = 0.0f;
            this.mBehindAlpha = this.mDefaultScrimAlpha;
            this.mAnimationDuration = 220;
            this.mBlankScreen = false;
        }
    };
    
    boolean mAnimateChange;
    long mAnimationDuration;
    float mAodFrontScrimAlpha;
    float mBehindAlpha;
    int mBehindTint;
    boolean mBlankScreen;
    float mBubbleAlpha;
    int mBubbleTint;
    boolean mClipQsScrim;
    float mDefaultScrimAlpha;
    boolean mDisplayRequiresBlanking;
    DockManager mDockManager;
    DozeParameters mDozeParameters;
    float mFrontAlpha;
    int mFrontTint;
    boolean mHasBackdrop;
    boolean mKeyguardFadingAway;
    long mKeyguardFadingAwayDuration;
    boolean mLaunchingAffordanceWithPreview;
    float mNotifAlpha;
    int mNotifTint;
    ScrimView mScrimBehind;
    float mScrimBehindAlphaKeyguard;
    ScrimView mScrimForBubble;
    ScrimView mScrimInFront;
    boolean mWakeLockScreenSensorActive;
    boolean mWallpaperSupportsAmbientMode;

    public float getMaxLightRevealScrimAlpha() {
        return 1.0f;
    }

    public boolean isLowPowerState() {
        return false;
    }

    public void prepare(ScrimState scrimState) {
    }

    ScrimState() {
        this.mBlankScreen = false;
        this.mAnimationDuration = 220;
        this.mFrontTint = 0;
        this.mBehindTint = 0;
        this.mBubbleTint = 0;
        this.mNotifTint = 0;
        this.mAnimateChange = true;
    }

    public void init(ScrimView scrimView, ScrimView scrimView2, ScrimView scrimView3, DozeParameters dozeParameters, DockManager dockManager) {
        this.mScrimInFront = scrimView;
        this.mScrimBehind = scrimView2;
        this.mScrimForBubble = scrimView3;
        this.mDozeParameters = dozeParameters;
        this.mDockManager = dockManager;
        this.mDisplayRequiresBlanking = dozeParameters.getDisplayNeedsBlanking();
    }

    public float getFrontAlpha() {
        return this.mFrontAlpha;
    }

    public float getBehindAlpha() {
        return this.mBehindAlpha;
    }

    public float getNotifAlpha() {
        return this.mNotifAlpha;
    }

    public float getBubbleAlpha() {
        return this.mBubbleAlpha;
    }

    public int getFrontTint() {
        return this.mFrontTint;
    }

    public int getBehindTint() {
        return this.mBehindTint;
    }

    public int getNotifTint() {
        return this.mNotifTint;
    }

    public int getBubbleTint() {
        return this.mBubbleTint;
    }

    public long getAnimationDuration() {
        return this.mAnimationDuration;
    }

    public boolean getBlanksScreen() {
        return this.mBlankScreen;
    }

    public void updateScrimColor(ScrimView scrimView, float f, int i) {
        Trace.traceCounter(4096, scrimView == this.mScrimInFront ? "front_scrim_alpha" : "back_scrim_alpha", (int) (255.0f * f));
        Trace.traceCounter(4096, scrimView == this.mScrimInFront ? "front_scrim_tint" : "back_scrim_tint", Color.alpha(i));
        scrimView.setTint(i);
        scrimView.setViewAlpha(f);
    }

    public boolean getAnimateChange() {
        return this.mAnimateChange;
    }

    public void setAodFrontScrimAlpha(float f) {
        this.mAodFrontScrimAlpha = f;
    }

    public void setScrimBehindAlphaKeyguard(float f) {
        this.mScrimBehindAlphaKeyguard = f;
    }

    public void setDefaultScrimAlpha(float f) {
        this.mDefaultScrimAlpha = f;
    }

    public void setBubbleAlpha(float f) {
        this.mBubbleAlpha = f;
    }

    public void setWallpaperSupportsAmbientMode(boolean z) {
        this.mWallpaperSupportsAmbientMode = z;
    }

    public void setLaunchingAffordanceWithPreview(boolean z) {
        this.mLaunchingAffordanceWithPreview = z;
    }

    public void setHasBackdrop(boolean z) {
        this.mHasBackdrop = z;
    }

    public void setWakeLockScreenSensorActive(boolean z) {
        this.mWakeLockScreenSensorActive = z;
    }

    public void setKeyguardFadingAway(boolean z, long j) {
        this.mKeyguardFadingAway = z;
        this.mKeyguardFadingAwayDuration = j;
    }

    public void setClipQsScrim(boolean z) {
        this.mClipQsScrim = z;
    }
}
