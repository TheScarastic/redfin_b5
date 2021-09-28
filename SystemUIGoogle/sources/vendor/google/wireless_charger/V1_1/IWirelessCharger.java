package vendor.google.wireless_charger.V1_1;

import android.os.RemoteException;
/* loaded from: classes2.dex */
public interface IWirelessCharger extends vendor.google.wireless_charger.V1_0.IWirelessCharger {
    byte registerCallback(IWirelessChargerInfoCallback iWirelessChargerInfoCallback) throws RemoteException;
}
