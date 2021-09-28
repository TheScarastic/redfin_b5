package vendor.google.google_battery.V1_0;

import android.os.IHwInterface;
import android.os.RemoteException;
/* loaded from: classes2.dex */
public interface IGoogleBattery extends IHwInterface {

    @FunctionalInterface
    /* loaded from: classes2.dex */
    public interface getChargingStageAndDeadlineCallback {
        void onValues(byte b, String str, int i);
    }

    void getChargingStageAndDeadline(getChargingStageAndDeadlineCallback getchargingstageanddeadlinecallback) throws RemoteException;
}
