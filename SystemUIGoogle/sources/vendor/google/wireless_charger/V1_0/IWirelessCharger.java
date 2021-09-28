package vendor.google.wireless_charger.V1_0;

import android.os.IHwInterface;
import android.os.RemoteException;
import java.util.ArrayList;
/* loaded from: classes2.dex */
public interface IWirelessCharger extends IHwInterface {

    @FunctionalInterface
    /* loaded from: classes2.dex */
    public interface challengeCallback {
        void onValues(byte b, ArrayList<Byte> arrayList);
    }

    @FunctionalInterface
    /* loaded from: classes2.dex */
    public interface getInformationCallback {
        void onValues(byte b, DockInfo dockInfo);
    }

    @FunctionalInterface
    /* loaded from: classes2.dex */
    public interface isDockPresentCallback {
        void onValues(boolean z, byte b, byte b2, boolean z2, int i);
    }

    @FunctionalInterface
    /* loaded from: classes2.dex */
    public interface keyExchangeCallback {
        void onValues(byte b, KeyExchangeResponse keyExchangeResponse);
    }

    void challenge(byte b, ArrayList<Byte> arrayList, challengeCallback challengecallback) throws RemoteException;

    void getInformation(getInformationCallback getinformationcallback) throws RemoteException;

    void isDockPresent(isDockPresentCallback isdockpresentcallback) throws RemoteException;

    void keyExchange(ArrayList<Byte> arrayList, keyExchangeCallback keyexchangecallback) throws RemoteException;
}
