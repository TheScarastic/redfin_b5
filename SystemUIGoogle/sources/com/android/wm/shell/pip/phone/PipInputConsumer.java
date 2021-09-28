package com.android.wm.shell.pip.phone;

import android.os.Binder;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.util.Log;
import android.view.BatchedInputEventReceiver;
import android.view.Choreographer;
import android.view.IWindowManager;
import android.view.InputChannel;
import android.view.InputEvent;
import com.android.wm.shell.common.ShellExecutor;
import java.io.PrintWriter;
/* loaded from: classes2.dex */
public class PipInputConsumer {
    private static final String TAG = "PipInputConsumer";
    private InputEventReceiver mInputEventReceiver;
    private InputListener mListener;
    private final ShellExecutor mMainExecutor;
    private final String mName;
    private RegistrationListener mRegistrationListener;
    private final IBinder mToken = new Binder();
    private final IWindowManager mWindowManager;

    /* loaded from: classes2.dex */
    public interface InputListener {
        boolean onInputEvent(InputEvent inputEvent);
    }

    /* loaded from: classes2.dex */
    public interface RegistrationListener {
        void onRegistrationChanged(boolean z);
    }

    /* access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public final class InputEventReceiver extends BatchedInputEventReceiver {
        InputEventReceiver(InputChannel inputChannel, Looper looper, Choreographer choreographer) {
            super(inputChannel, looper, choreographer);
        }

        public void onInputEvent(InputEvent inputEvent) {
            boolean z = true;
            try {
                if (PipInputConsumer.this.mListener != null) {
                    z = PipInputConsumer.this.mListener.onInputEvent(inputEvent);
                }
            } finally {
                finishInputEvent(inputEvent, true);
            }
        }
    }

    public PipInputConsumer(IWindowManager iWindowManager, String str, ShellExecutor shellExecutor) {
        this.mWindowManager = iWindowManager;
        this.mName = str;
        this.mMainExecutor = shellExecutor;
    }

    public void setInputListener(InputListener inputListener) {
        this.mListener = inputListener;
    }

    public void setRegistrationListener(RegistrationListener registrationListener) {
        this.mRegistrationListener = registrationListener;
        this.mMainExecutor.execute(new Runnable() { // from class: com.android.wm.shell.pip.phone.PipInputConsumer$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                PipInputConsumer.this.lambda$setRegistrationListener$0();
            }
        });
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$setRegistrationListener$0() {
        RegistrationListener registrationListener = this.mRegistrationListener;
        if (registrationListener != null) {
            registrationListener.onRegistrationChanged(this.mInputEventReceiver != null);
        }
    }

    public void registerInputConsumer() {
        if (this.mInputEventReceiver == null) {
            InputChannel inputChannel = new InputChannel();
            try {
                this.mWindowManager.destroyInputConsumer(this.mName, 0);
                this.mWindowManager.createInputConsumer(this.mToken, this.mName, 0, inputChannel);
            } catch (RemoteException e) {
                Log.e(TAG, "Failed to create input consumer", e);
            }
            this.mMainExecutor.execute(new Runnable(inputChannel) { // from class: com.android.wm.shell.pip.phone.PipInputConsumer$$ExternalSyntheticLambda2
                public final /* synthetic */ InputChannel f$1;

                {
                    this.f$1 = r2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    PipInputConsumer.this.lambda$registerInputConsumer$1(this.f$1);
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$registerInputConsumer$1(InputChannel inputChannel) {
        this.mInputEventReceiver = new InputEventReceiver(inputChannel, Looper.myLooper(), Choreographer.getSfInstance());
        RegistrationListener registrationListener = this.mRegistrationListener;
        if (registrationListener != null) {
            registrationListener.onRegistrationChanged(true);
        }
    }

    public void unregisterInputConsumer() {
        if (this.mInputEventReceiver != null) {
            try {
                this.mWindowManager.destroyInputConsumer(this.mName, 0);
            } catch (RemoteException e) {
                Log.e(TAG, "Failed to destroy input consumer", e);
            }
            this.mInputEventReceiver.dispose();
            this.mInputEventReceiver = null;
            this.mMainExecutor.execute(new Runnable() { // from class: com.android.wm.shell.pip.phone.PipInputConsumer$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    PipInputConsumer.this.lambda$unregisterInputConsumer$2();
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$unregisterInputConsumer$2() {
        RegistrationListener registrationListener = this.mRegistrationListener;
        if (registrationListener != null) {
            registrationListener.onRegistrationChanged(false);
        }
    }

    public void dump(PrintWriter printWriter, String str) {
        String str2 = str + "  ";
        printWriter.println(str + TAG);
        StringBuilder sb = new StringBuilder();
        sb.append(str2);
        sb.append("registered=");
        sb.append(this.mInputEventReceiver != null);
        printWriter.println(sb.toString());
    }
}
