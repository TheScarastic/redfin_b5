package com.google.input;

import android.os.BadParcelableException;
import android.os.Parcel;
import android.os.Parcelable;
/* loaded from: classes2.dex */
public class ContextPacket implements Parcelable {
    public static final Parcelable.Creator<ContextPacket> CREATOR = new Parcelable.Creator<ContextPacket>() { // from class: com.google.input.ContextPacket.1
        @Override // android.os.Parcelable.Creator
        public ContextPacket createFromParcel(Parcel parcel) {
            ContextPacket contextPacket = new ContextPacket();
            contextPacket.readFromParcel(parcel);
            return contextPacket;
        }

        @Override // android.os.Parcelable.Creator
        public ContextPacket[] newArray(int i) {
            return new ContextPacket[i];
        }
    };
    public byte orientation;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public final int getStability() {
        return 1;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        int dataPosition = parcel.dataPosition();
        parcel.writeInt(0);
        parcel.writeByte(this.orientation);
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
                    this.orientation = parcel.readByte();
                    if (dataPosition <= Integer.MAX_VALUE - readInt) {
                        parcel.setDataPosition(dataPosition + readInt);
                        return;
                    }
                    throw new BadParcelableException("Overflow in the size of parcelable");
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
}
