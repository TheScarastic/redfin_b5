package vendor.google.wireless_charger.V1_3;

import android.os.HidlSupport;
import android.os.HwBinder;
import android.os.HwBlob;
import android.os.HwParcel;
import android.os.IHwBinder;
import android.os.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import vendor.google.wireless_charger.V1_0.DockInfo;
import vendor.google.wireless_charger.V1_0.IWirelessCharger;
import vendor.google.wireless_charger.V1_0.KeyExchangeResponse;
import vendor.google.wireless_charger.V1_1.IWirelessChargerInfoCallback;
import vendor.google.wireless_charger.V1_2.IWirelessCharger;
import vendor.google.wireless_charger.V1_2.IWirelessChargerRtxStatusCallback;
import vendor.google.wireless_charger.V1_2.RtxStatusInfo;
/* loaded from: classes2.dex */
public interface IWirelessCharger extends vendor.google.wireless_charger.V1_2.IWirelessCharger {

    @FunctionalInterface
    /* loaded from: classes2.dex */
    public interface getFanCallback {
        void onValues(byte b, FanInfo fanInfo);
    }

    @FunctionalInterface
    /* loaded from: classes2.dex */
    public interface getFanInformationCallback {
        void onValues(byte b, FanDetailedInfo fanDetailedInfo);
    }

    @FunctionalInterface
    /* loaded from: classes2.dex */
    public interface getFeaturesCallback {
        void onValues(byte b, long j);
    }

    @FunctionalInterface
    /* loaded from: classes2.dex */
    public interface getWpcAuthCertificateCallback {
        void onValues(byte b, ArrayList<Byte> arrayList);
    }

    @FunctionalInterface
    /* loaded from: classes2.dex */
    public interface getWpcAuthChallengeResponseCallback {
        void onValues(byte b, byte b2, byte b3, byte b4, ArrayList<Byte> arrayList, ArrayList<Byte> arrayList2);
    }

    @FunctionalInterface
    /* loaded from: classes2.dex */
    public interface getWpcAuthDigestsCallback {
        void onValues(byte b, byte b2, byte b3, ArrayList<byte[]> arrayList);
    }

    @FunctionalInterface
    /* loaded from: classes2.dex */
    public interface setFanCallback {
        void onValues(byte b, FanInfo fanInfo);
    }

    void getFan(byte b, getFanCallback getfancallback) throws RemoteException;

    void getFanInformation(byte b, getFanInformationCallback getfaninformationcallback) throws RemoteException;

    int getFanLevel() throws RemoteException;

    void getFeatures(long j, getFeaturesCallback getfeaturescallback) throws RemoteException;

    void getWpcAuthCertificate(byte b, short s, short s2, getWpcAuthCertificateCallback getwpcauthcertificatecallback) throws RemoteException;

    void getWpcAuthChallengeResponse(byte b, ArrayList<Byte> arrayList, getWpcAuthChallengeResponseCallback getwpcauthchallengeresponsecallback) throws RemoteException;

    void getWpcAuthDigests(byte b, getWpcAuthDigestsCallback getwpcauthdigestscallback) throws RemoteException;

    @Override // vendor.google.wireless_charger.V1_2.IWirelessCharger
    ArrayList<String> interfaceChain() throws RemoteException;

    @Override // vendor.google.wireless_charger.V1_2.IWirelessCharger
    boolean linkToDeath(IHwBinder.DeathRecipient deathRecipient, long j) throws RemoteException;

    void setFan(byte b, byte b2, short s, setFanCallback setfancallback) throws RemoteException;

    byte setFeatures(long j, long j2) throws RemoteException;

    static IWirelessCharger asInterface(IHwBinder iHwBinder) {
        if (iHwBinder == null) {
            return null;
        }
        IWirelessCharger queryLocalInterface = iHwBinder.queryLocalInterface("vendor.google.wireless_charger@1.3::IWirelessCharger");
        if (queryLocalInterface != null && (queryLocalInterface instanceof IWirelessCharger)) {
            return queryLocalInterface;
        }
        Proxy proxy = new Proxy(iHwBinder);
        try {
            Iterator<String> it = proxy.interfaceChain().iterator();
            while (it.hasNext()) {
                if (it.next().equals("vendor.google.wireless_charger@1.3::IWirelessCharger")) {
                    return proxy;
                }
            }
        } catch (RemoteException unused) {
        }
        return null;
    }

    @Deprecated
    static IWirelessCharger getService(String str) throws RemoteException {
        return asInterface(HwBinder.getService("vendor.google.wireless_charger@1.3::IWirelessCharger", str));
    }

    @Deprecated
    static IWirelessCharger getService() throws RemoteException {
        return getService("default");
    }

    /* loaded from: classes2.dex */
    public static final class Proxy implements IWirelessCharger {
        private IHwBinder mRemote;

        public Proxy(IHwBinder iHwBinder) {
            Objects.requireNonNull(iHwBinder);
            this.mRemote = iHwBinder;
        }

        public IHwBinder asBinder() {
            return this.mRemote;
        }

        public String toString() {
            try {
                return interfaceDescriptor() + "@Proxy";
            } catch (RemoteException unused) {
                return "[class or subclass of vendor.google.wireless_charger@1.3::IWirelessCharger]@Proxy";
            }
        }

        public final boolean equals(Object obj) {
            return HidlSupport.interfacesEqual(this, obj);
        }

        public final int hashCode() {
            return asBinder().hashCode();
        }

        @Override // vendor.google.wireless_charger.V1_0.IWirelessCharger
        public void isDockPresent(IWirelessCharger.isDockPresentCallback isdockpresentcallback) throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken("vendor.google.wireless_charger@1.0::IWirelessCharger");
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(1, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
                isdockpresentcallback.onValues(hwParcel2.readBool(), hwParcel2.readInt8(), hwParcel2.readInt8(), hwParcel2.readBool(), hwParcel2.readInt32());
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.google.wireless_charger.V1_0.IWirelessCharger
        public void getInformation(IWirelessCharger.getInformationCallback getinformationcallback) throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken("vendor.google.wireless_charger@1.0::IWirelessCharger");
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(2, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
                byte readInt8 = hwParcel2.readInt8();
                DockInfo dockInfo = new DockInfo();
                dockInfo.readFromParcel(hwParcel2);
                getinformationcallback.onValues(readInt8, dockInfo);
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.google.wireless_charger.V1_0.IWirelessCharger
        public void keyExchange(ArrayList<Byte> arrayList, IWirelessCharger.keyExchangeCallback keyexchangecallback) throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken("vendor.google.wireless_charger@1.0::IWirelessCharger");
            hwParcel.writeInt8Vector(arrayList);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(3, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
                byte readInt8 = hwParcel2.readInt8();
                KeyExchangeResponse keyExchangeResponse = new KeyExchangeResponse();
                keyExchangeResponse.readFromParcel(hwParcel2);
                keyexchangecallback.onValues(readInt8, keyExchangeResponse);
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.google.wireless_charger.V1_0.IWirelessCharger
        public void challenge(byte b, ArrayList<Byte> arrayList, IWirelessCharger.challengeCallback challengecallback) throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken("vendor.google.wireless_charger@1.0::IWirelessCharger");
            hwParcel.writeInt8(b);
            hwParcel.writeInt8Vector(arrayList);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(4, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
                challengecallback.onValues(hwParcel2.readInt8(), hwParcel2.readInt8Vector());
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.google.wireless_charger.V1_1.IWirelessCharger
        public byte registerCallback(IWirelessChargerInfoCallback iWirelessChargerInfoCallback) throws RemoteException {
            IHwBinder iHwBinder;
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken("vendor.google.wireless_charger@1.1::IWirelessCharger");
            if (iWirelessChargerInfoCallback == null) {
                iHwBinder = null;
            } else {
                iHwBinder = iWirelessChargerInfoCallback.asBinder();
            }
            hwParcel.writeStrongBinder(iHwBinder);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(12, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
                return hwParcel2.readInt8();
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.google.wireless_charger.V1_2.IWirelessCharger
        public byte registerRtxCallback(IWirelessChargerRtxStatusCallback iWirelessChargerRtxStatusCallback) throws RemoteException {
            IHwBinder iHwBinder;
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken("vendor.google.wireless_charger@1.2::IWirelessCharger");
            if (iWirelessChargerRtxStatusCallback == null) {
                iHwBinder = null;
            } else {
                iHwBinder = iWirelessChargerRtxStatusCallback.asBinder();
            }
            hwParcel.writeStrongBinder(iHwBinder);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(15, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
                return hwParcel2.readInt8();
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.google.wireless_charger.V1_2.IWirelessCharger
        public boolean isRtxSupported() throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken("vendor.google.wireless_charger@1.2::IWirelessCharger");
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(17, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
                return hwParcel2.readBool();
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.google.wireless_charger.V1_2.IWirelessCharger
        public void getRtxInformation(IWirelessCharger.getRtxInformationCallback getrtxinformationcallback) throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken("vendor.google.wireless_charger@1.2::IWirelessCharger");
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(19, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
                byte readInt8 = hwParcel2.readInt8();
                RtxStatusInfo rtxStatusInfo = new RtxStatusInfo();
                rtxStatusInfo.readFromParcel(hwParcel2);
                getrtxinformationcallback.onValues(readInt8, rtxStatusInfo);
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.google.wireless_charger.V1_2.IWirelessCharger
        public byte setRtxMode(boolean z) throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken("vendor.google.wireless_charger@1.2::IWirelessCharger");
            hwParcel.writeBool(z);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(20, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
                return hwParcel2.readInt8();
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.google.wireless_charger.V1_3.IWirelessCharger
        public void getFanInformation(byte b, getFanInformationCallback getfaninformationcallback) throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken("vendor.google.wireless_charger@1.3::IWirelessCharger");
            hwParcel.writeInt8(b);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(21, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
                byte readInt8 = hwParcel2.readInt8();
                FanDetailedInfo fanDetailedInfo = new FanDetailedInfo();
                fanDetailedInfo.readFromParcel(hwParcel2);
                getfaninformationcallback.onValues(readInt8, fanDetailedInfo);
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.google.wireless_charger.V1_3.IWirelessCharger
        public void getFan(byte b, getFanCallback getfancallback) throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken("vendor.google.wireless_charger@1.3::IWirelessCharger");
            hwParcel.writeInt8(b);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(22, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
                byte readInt8 = hwParcel2.readInt8();
                FanInfo fanInfo = new FanInfo();
                fanInfo.readFromParcel(hwParcel2);
                getfancallback.onValues(readInt8, fanInfo);
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.google.wireless_charger.V1_3.IWirelessCharger
        public void setFan(byte b, byte b2, short s, setFanCallback setfancallback) throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken("vendor.google.wireless_charger@1.3::IWirelessCharger");
            hwParcel.writeInt8(b);
            hwParcel.writeInt8(b2);
            hwParcel.writeInt16(s);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(23, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
                byte readInt8 = hwParcel2.readInt8();
                FanInfo fanInfo = new FanInfo();
                fanInfo.readFromParcel(hwParcel2);
                setfancallback.onValues(readInt8, fanInfo);
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.google.wireless_charger.V1_3.IWirelessCharger
        public int getFanLevel() throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken("vendor.google.wireless_charger@1.3::IWirelessCharger");
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(24, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
                return hwParcel2.readInt32();
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.google.wireless_charger.V1_3.IWirelessCharger
        public void getWpcAuthDigests(byte b, getWpcAuthDigestsCallback getwpcauthdigestscallback) throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken("vendor.google.wireless_charger@1.3::IWirelessCharger");
            hwParcel.writeInt8(b);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(25, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
                byte readInt8 = hwParcel2.readInt8();
                byte readInt82 = hwParcel2.readInt8();
                byte readInt83 = hwParcel2.readInt8();
                ArrayList<byte[]> arrayList = new ArrayList<>();
                HwBlob readBuffer = hwParcel2.readBuffer(16);
                int int32 = readBuffer.getInt32(8);
                HwBlob readEmbeddedBuffer = hwParcel2.readEmbeddedBuffer((long) (int32 * 32), readBuffer.handle(), 0, true);
                arrayList.clear();
                for (int i = 0; i < int32; i++) {
                    byte[] bArr = new byte[32];
                    readEmbeddedBuffer.copyToInt8Array((long) (i * 32), bArr, 32);
                    arrayList.add(bArr);
                }
                getwpcauthdigestscallback.onValues(readInt8, readInt82, readInt83, arrayList);
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.google.wireless_charger.V1_3.IWirelessCharger
        public void getWpcAuthCertificate(byte b, short s, short s2, getWpcAuthCertificateCallback getwpcauthcertificatecallback) throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken("vendor.google.wireless_charger@1.3::IWirelessCharger");
            hwParcel.writeInt8(b);
            hwParcel.writeInt16(s);
            hwParcel.writeInt16(s2);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(26, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
                getwpcauthcertificatecallback.onValues(hwParcel2.readInt8(), hwParcel2.readInt8Vector());
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.google.wireless_charger.V1_3.IWirelessCharger
        public void getWpcAuthChallengeResponse(byte b, ArrayList<Byte> arrayList, getWpcAuthChallengeResponseCallback getwpcauthchallengeresponsecallback) throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken("vendor.google.wireless_charger@1.3::IWirelessCharger");
            hwParcel.writeInt8(b);
            hwParcel.writeInt8Vector(arrayList);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(27, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
                getwpcauthchallengeresponsecallback.onValues(hwParcel2.readInt8(), hwParcel2.readInt8(), hwParcel2.readInt8(), hwParcel2.readInt8(), hwParcel2.readInt8Vector(), hwParcel2.readInt8Vector());
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.google.wireless_charger.V1_3.IWirelessCharger
        public byte setFeatures(long j, long j2) throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken("vendor.google.wireless_charger@1.3::IWirelessCharger");
            hwParcel.writeInt64(j);
            hwParcel.writeInt64(j2);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(28, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
                return hwParcel2.readInt8();
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.google.wireless_charger.V1_3.IWirelessCharger
        public void getFeatures(long j, getFeaturesCallback getfeaturescallback) throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken("vendor.google.wireless_charger@1.3::IWirelessCharger");
            hwParcel.writeInt64(j);
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(29, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
                getfeaturescallback.onValues(hwParcel2.readInt8(), hwParcel2.readInt64());
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.google.wireless_charger.V1_3.IWirelessCharger, vendor.google.wireless_charger.V1_2.IWirelessCharger
        public ArrayList<String> interfaceChain() throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken("android.hidl.base@1.0::IBase");
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(256067662, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
                return hwParcel2.readStringVector();
            } finally {
                hwParcel2.release();
            }
        }

        public String interfaceDescriptor() throws RemoteException {
            HwParcel hwParcel = new HwParcel();
            hwParcel.writeInterfaceToken("android.hidl.base@1.0::IBase");
            HwParcel hwParcel2 = new HwParcel();
            try {
                this.mRemote.transact(256136003, hwParcel, hwParcel2, 0);
                hwParcel2.verifySuccess();
                hwParcel.releaseTemporaryStorage();
                return hwParcel2.readString();
            } finally {
                hwParcel2.release();
            }
        }

        @Override // vendor.google.wireless_charger.V1_3.IWirelessCharger, vendor.google.wireless_charger.V1_2.IWirelessCharger
        public boolean linkToDeath(IHwBinder.DeathRecipient deathRecipient, long j) throws RemoteException {
            return this.mRemote.linkToDeath(deathRecipient, j);
        }
    }
}
