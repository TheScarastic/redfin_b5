package com.google.android.systemui.gamedashboard;

import android.content.Intent;
import android.os.Handler;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.screenrecord.RecordingController;
import com.android.systemui.settings.UserContextProvider;
import com.android.systemui.statusbar.phone.KeyguardDismissUtil;
/* loaded from: classes2.dex */
public class ScreenRecordController implements LifecycleOwner {
    private final RecordingController mController;
    private final KeyguardDismissUtil mKeyguardDismissUtil;
    private final LifecycleRegistry mLifecycle = new LifecycleRegistry(this);
    private final ToastController mToast;
    private final Handler mUiHandler;
    private final UserContextProvider mUserContextProvider;

    public ScreenRecordController(RecordingController recordingController, Handler handler, KeyguardDismissUtil keyguardDismissUtil, UserContextProvider userContextProvider, ToastController toastController) {
        this.mUserContextProvider = userContextProvider;
        this.mController = recordingController;
        this.mUiHandler = handler;
        handler.post(new Runnable() { // from class: com.google.android.systemui.gamedashboard.ScreenRecordController$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                ScreenRecordController.m655$r8$lambda$VVKKimw_f7cMO98zJQp3W3Ts4E(ScreenRecordController.this);
            }
        });
        this.mKeyguardDismissUtil = keyguardDismissUtil;
        this.mToast = toastController;
    }

    public /* synthetic */ void lambda$new$0() {
        this.mLifecycle.setCurrentState(Lifecycle.State.CREATED);
    }

    public void handleClick() {
        if (this.mController.isStarting()) {
            cancelCountdown();
        } else if (this.mController.isRecording()) {
            stopRecording();
        } else {
            this.mUiHandler.post(new Runnable() { // from class: com.google.android.systemui.gamedashboard.ScreenRecordController$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    ScreenRecordController.m654$r8$lambda$RdZRHkqHIZr20_EsJrQK0_S58M(ScreenRecordController.this);
                }
            });
        }
    }

    public void cancelCountdown() {
        this.mController.cancelCountdown();
    }

    public void stopRecording() {
        this.mController.stopRecording();
        this.mToast.showRecordSaveText();
    }

    public boolean isRecording() {
        return this.mController.isRecording();
    }

    public boolean isStarting() {
        return this.mController.isStarting();
    }

    public RecordingController getController() {
        return this.mController;
    }

    /* renamed from: showPrompt */
    public void lambda$handleClick$1() {
        this.mKeyguardDismissUtil.executeWhenUnlocked(new ActivityStarter.OnDismissAction(this.mController.getPromptIntent()) { // from class: com.google.android.systemui.gamedashboard.ScreenRecordController$$ExternalSyntheticLambda0
            public final /* synthetic */ Intent f$1;

            {
                this.f$1 = r2;
            }

            @Override // com.android.systemui.plugins.ActivityStarter.OnDismissAction
            public final boolean onDismiss() {
                return ScreenRecordController.$r8$lambda$Nqhd2daMg0sPfq_bCZrR4CfuhtU(ScreenRecordController.this, this.f$1);
            }
        }, false, false);
    }

    public /* synthetic */ boolean lambda$showPrompt$2(Intent intent) {
        this.mUserContextProvider.getUserContext().startActivity(intent);
        return false;
    }

    @Override // androidx.lifecycle.LifecycleOwner
    public Lifecycle getLifecycle() {
        return this.mLifecycle;
    }
}
