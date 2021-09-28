package com.android.systemui.doze;

import android.app.IWallpaperManager;
import android.os.RemoteException;
import android.util.Log;
import com.android.systemui.doze.DozeMachine;
import com.android.systemui.statusbar.phone.BiometricUnlockController;
import com.android.systemui.statusbar.phone.DozeParameters;
import java.io.PrintWriter;
/* loaded from: classes.dex */
public class DozeWallpaperState implements DozeMachine.Part {
    private static final boolean DEBUG = Log.isLoggable("DozeWallpaperState", 3);
    private final BiometricUnlockController mBiometricUnlockController;
    private final DozeParameters mDozeParameters;
    private boolean mIsAmbientMode;
    private final IWallpaperManager mWallpaperManagerService;

    public DozeWallpaperState(IWallpaperManager iWallpaperManager, BiometricUnlockController biometricUnlockController, DozeParameters dozeParameters) {
        this.mWallpaperManagerService = iWallpaperManager;
        this.mBiometricUnlockController = biometricUnlockController;
        this.mDozeParameters = dozeParameters;
    }

    /* renamed from: com.android.systemui.doze.DozeWallpaperState$1  reason: invalid class name */
    /* loaded from: classes.dex */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$android$systemui$doze$DozeMachine$State;

        static {
            int[] iArr = new int[DozeMachine.State.values().length];
            $SwitchMap$com$android$systemui$doze$DozeMachine$State = iArr;
            try {
                iArr[DozeMachine.State.DOZE.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$android$systemui$doze$DozeMachine$State[DozeMachine.State.DOZE_AOD.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$android$systemui$doze$DozeMachine$State[DozeMachine.State.DOZE_AOD_DOCKED.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$android$systemui$doze$DozeMachine$State[DozeMachine.State.DOZE_AOD_PAUSING.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$android$systemui$doze$DozeMachine$State[DozeMachine.State.DOZE_AOD_PAUSED.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$android$systemui$doze$DozeMachine$State[DozeMachine.State.DOZE_REQUEST_PULSE.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$com$android$systemui$doze$DozeMachine$State[DozeMachine.State.DOZE_PULSE_DONE.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                $SwitchMap$com$android$systemui$doze$DozeMachine$State[DozeMachine.State.DOZE_PULSING.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                $SwitchMap$com$android$systemui$doze$DozeMachine$State[DozeMachine.State.DOZE_PULSING_BRIGHT.ordinal()] = 9;
            } catch (NoSuchFieldError unused9) {
            }
        }
    }

    @Override // com.android.systemui.doze.DozeMachine.Part
    public void transitionTo(DozeMachine.State state, DozeMachine.State state2) {
        boolean z;
        boolean z2;
        boolean z3 = false;
        switch (AnonymousClass1.$SwitchMap$com$android$systemui$doze$DozeMachine$State[state2.ordinal()]) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
                z = true;
                break;
            default:
                z = false;
                break;
        }
        if (z) {
            z2 = this.mDozeParameters.shouldControlScreenOff();
        } else {
            boolean z4 = state == DozeMachine.State.DOZE_PULSING && state2 == DozeMachine.State.FINISH;
            if (((!this.mDozeParameters.getDisplayNeedsBlanking()) && !this.mBiometricUnlockController.unlockedByWakeAndUnlock()) || z4) {
                z3 = true;
            }
            z2 = z3;
        }
        if (z != this.mIsAmbientMode) {
            this.mIsAmbientMode = z;
            if (this.mWallpaperManagerService != null) {
                long j = z2 ? 500 : 0;
                try {
                    if (DEBUG) {
                        Log.i("DozeWallpaperState", "AOD wallpaper state changed to: " + this.mIsAmbientMode + ", animationDuration: " + j);
                    }
                    this.mWallpaperManagerService.setInAmbientMode(this.mIsAmbientMode, j);
                } catch (RemoteException unused) {
                    Log.w("DozeWallpaperState", "Cannot notify state to WallpaperManagerService: " + this.mIsAmbientMode);
                }
            }
        }
    }

    @Override // com.android.systemui.doze.DozeMachine.Part
    public void dump(PrintWriter printWriter) {
        printWriter.println("DozeWallpaperState:");
        printWriter.println(" isAmbientMode: " + this.mIsAmbientMode);
        StringBuilder sb = new StringBuilder();
        sb.append(" hasWallpaperService: ");
        sb.append(this.mWallpaperManagerService != null);
        printWriter.println(sb.toString());
    }
}
