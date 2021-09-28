package android.frameworks.stats;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
/* loaded from: classes.dex */
public interface IStats extends IInterface {
    public static final String DESCRIPTOR = "android$frameworks$stats$IStats".replace('$', '.');

    void reportVendorAtom(VendorAtom vendorAtom) throws RemoteException;

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IStats {
        public static IStats asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(IStats.DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IStats)) {
                return new Proxy(iBinder);
            }
            return (IStats) queryLocalInterface;
        }

        /* access modifiers changed from: private */
        /* loaded from: classes.dex */
        public static class Proxy implements IStats {
            public static IStats sDefaultImpl;
            private IBinder mRemote;
            private int mCachedVersion = -1;
            private String mCachedHash = "-1";

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            @Override // android.frameworks.stats.IStats
            public void reportVendorAtom(VendorAtom vendorAtom) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IStats.DESCRIPTOR);
                    if (vendorAtom != null) {
                        obtain.writeInt(1);
                        vendorAtom.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (this.mRemote.transact(1, obtain, null, 1)) {
                        return;
                    }
                    if (Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().reportVendorAtom(vendorAtom);
                        return;
                    }
                    throw new RemoteException("Method reportVendorAtom is unimplemented.");
                } finally {
                    obtain.recycle();
                }
            }
        }

        public static IStats getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
