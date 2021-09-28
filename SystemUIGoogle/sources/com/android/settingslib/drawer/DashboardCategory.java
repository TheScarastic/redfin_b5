package com.android.settingslib.drawer;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public class DashboardCategory implements Parcelable {
    public static final Parcelable.Creator<DashboardCategory> CREATOR = new Parcelable.Creator<DashboardCategory>() { // from class: com.android.settingslib.drawer.DashboardCategory.1
        @Override // android.os.Parcelable.Creator
        public DashboardCategory createFromParcel(Parcel parcel) {
            return new DashboardCategory(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public DashboardCategory[] newArray(int i) {
            return new DashboardCategory[i];
        }
    };
    public final String key;
    private List<Tile> mTiles = new ArrayList();

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    DashboardCategory(Parcel parcel) {
        this.key = parcel.readString();
        int readInt = parcel.readInt();
        for (int i = 0; i < readInt; i++) {
            this.mTiles.add(Tile.CREATOR.createFromParcel(parcel));
        }
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.key);
        int size = this.mTiles.size();
        parcel.writeInt(size);
        for (int i2 = 0; i2 < size; i2++) {
            this.mTiles.get(i2).writeToParcel(parcel, i);
        }
    }
}
