package com.google.android.systemui.fingerprint;

import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;
import com.google.hardware.pixel.display.IDisplay;
/* loaded from: classes2.dex */
public class UdfpsLhbmProvider implements IBinder.DeathRecipient {
    private volatile IDisplay mDisplayHal;

    public UdfpsLhbmProvider() {
        getDisplayHal();
    }

    public void enableLhbm() {
        Log.v("UdfpsLhbmProvider", "enableLhbm");
        IDisplay displayHal = getDisplayHal();
        if (displayHal == null) {
            Log.e("UdfpsLhbmProvider", "enableLhbm | displayHal is null");
            return;
        }
        try {
            displayHal.setLhbmState(true);
        } catch (RemoteException e) {
            Log.e("UdfpsLhbmProvider", "enableLhbm | RemoteException", e);
        }
    }

    public void disableLhbm() {
        Log.v("UdfpsLhbmProvider", "disableLhbm");
        IDisplay displayHal = getDisplayHal();
        if (displayHal == null) {
            Log.e("UdfpsLhbmProvider", "disableLhbm | displayHal is null");
            return;
        }
        try {
            displayHal.setLhbmState(false);
        } catch (RemoteException e) {
            Log.e("UdfpsLhbmProvider", "disableLhbm | RemoteException", e);
        }
    }

    private IDisplay getDisplayHal() {
        IDisplay iDisplay = this.mDisplayHal;
        if (iDisplay != null) {
            return iDisplay;
        }
        IBinder waitForDeclaredService = ServiceManager.waitForDeclaredService("com.google.hardware.pixel.display.IDisplay/default");
        if (waitForDeclaredService == null) {
            Log.e("UdfpsLhbmProvider", "getDisplayHal | Failed to find the Display HAL");
            return null;
        }
        try {
            waitForDeclaredService.linkToDeath(this, 0);
            this.mDisplayHal = IDisplay.Stub.asInterface(waitForDeclaredService);
            return this.mDisplayHal;
        } catch (RemoteException e) {
            Log.e("UdfpsLhbmProvider", "getDisplayHal | Failed to link to death", e);
            return null;
        }
    }

    @Override // android.os.IBinder.DeathRecipient
    public void binderDied() {
        Log.e("UdfpsLhbmProvider", "binderDied | Display HAL died");
        this.mDisplayHal = null;
    }
}
