package android.hardware.common;

import android.os.BadParcelableException;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable;
/* loaded from: classes.dex */
public class Ashmem implements Parcelable {
    public static final Parcelable.Creator<Ashmem> CREATOR = new Parcelable.Creator<Ashmem>() { // from class: android.hardware.common.Ashmem.1
        @Override // android.os.Parcelable.Creator
        public Ashmem createFromParcel(Parcel parcel) {
            Ashmem ashmem = new Ashmem();
            ashmem.readFromParcel(parcel);
            return ashmem;
        }

        @Override // android.os.Parcelable.Creator
        public Ashmem[] newArray(int i) {
            return new Ashmem[i];
        }
    };
    public ParcelFileDescriptor fd;
    public long size = 0;

    public final int getStability() {
        return 1;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        int dataPosition = parcel.dataPosition();
        parcel.writeInt(0);
        if (this.fd != null) {
            parcel.writeInt(1);
            this.fd.writeToParcel(parcel, 0);
        } else {
            parcel.writeInt(0);
        }
        parcel.writeLong(this.size);
        int dataPosition2 = parcel.dataPosition();
        parcel.setDataPosition(dataPosition);
        parcel.writeInt(dataPosition2 - dataPosition);
        parcel.setDataPosition(dataPosition2);
    }

    public final void readFromParcel(Parcel parcel) {
        int dataPosition = parcel.dataPosition();
        int readInt = parcel.readInt();
        if (readInt >= 0) {
            try {
                if (parcel.dataPosition() - dataPosition < readInt) {
                    if (parcel.readInt() != 0) {
                        this.fd = (ParcelFileDescriptor) ParcelFileDescriptor.CREATOR.createFromParcel(parcel);
                    } else {
                        this.fd = null;
                    }
                    if (parcel.dataPosition() - dataPosition < readInt) {
                        this.size = parcel.readLong();
                        if (dataPosition <= Integer.MAX_VALUE - readInt) {
                            parcel.setDataPosition(dataPosition + readInt);
                            return;
                        }
                        throw new BadParcelableException("Overflow in the size of parcelable");
                    } else if (dataPosition > Integer.MAX_VALUE - readInt) {
                        throw new BadParcelableException("Overflow in the size of parcelable");
                    }
                } else if (dataPosition > Integer.MAX_VALUE - readInt) {
                    throw new BadParcelableException("Overflow in the size of parcelable");
                }
            } catch (Throwable th) {
                if (dataPosition > Integer.MAX_VALUE - readInt) {
                    throw new BadParcelableException("Overflow in the size of parcelable");
                }
                parcel.setDataPosition(dataPosition + readInt);
                throw th;
            }
        } else if (dataPosition > Integer.MAX_VALUE - readInt) {
            throw new BadParcelableException("Overflow in the size of parcelable");
        }
        parcel.setDataPosition(dataPosition + readInt);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return describeContents(this.fd) | 0;
    }

    private int describeContents(Object obj) {
        if (obj != null && (obj instanceof Parcelable)) {
            return ((Parcelable) obj).describeContents();
        }
        return 0;
    }
}
