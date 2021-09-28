package androidx.slice;

import android.os.Bundle;
import android.os.Parcelable;
import androidx.slice.SliceItemHolder;
import androidx.versionedparcelable.VersionedParcel;
import androidx.versionedparcelable.VersionedParcelable;
import java.util.ArrayList;
import java.util.Objects;
/* loaded from: classes.dex */
public final class SliceItemHolderParcelizer {
    private static SliceItemHolder.SliceItemPool sBuilder = new SliceItemHolder.SliceItemPool();

    public static SliceItemHolder read(VersionedParcel versionedParcel) {
        SliceItemHolder sliceItemHolder;
        SliceItemHolder.SliceItemPool sliceItemPool = sBuilder;
        if (sliceItemPool.mCached.size() > 0) {
            ArrayList<SliceItemHolder> arrayList = sliceItemPool.mCached;
            sliceItemHolder = arrayList.remove(arrayList.size() - 1);
        } else {
            sliceItemHolder = new SliceItemHolder(sliceItemPool);
        }
        sliceItemHolder.mVersionedParcelable = versionedParcel.readVersionedParcelable(sliceItemHolder.mVersionedParcelable, 1);
        sliceItemHolder.mParcelable = versionedParcel.readParcelable(sliceItemHolder.mParcelable, 2);
        sliceItemHolder.mStr = versionedParcel.readString(sliceItemHolder.mStr, 3);
        sliceItemHolder.mInt = versionedParcel.readInt(sliceItemHolder.mInt, 4);
        long j = sliceItemHolder.mLong;
        if (versionedParcel.readField(5)) {
            j = versionedParcel.readLong();
        }
        sliceItemHolder.mLong = j;
        Bundle bundle = sliceItemHolder.mBundle;
        if (versionedParcel.readField(6)) {
            bundle = versionedParcel.readBundle();
        }
        sliceItemHolder.mBundle = bundle;
        return sliceItemHolder;
    }

    public static void write(SliceItemHolder sliceItemHolder, VersionedParcel versionedParcel) {
        Objects.requireNonNull(versionedParcel);
        VersionedParcelable versionedParcelable = sliceItemHolder.mVersionedParcelable;
        if (versionedParcelable != null) {
            versionedParcel.setOutputField(1);
            versionedParcel.writeVersionedParcelable(versionedParcelable);
        }
        Parcelable parcelable = sliceItemHolder.mParcelable;
        if (parcelable != null) {
            versionedParcel.setOutputField(2);
            versionedParcel.writeParcelable(parcelable);
        }
        String str = sliceItemHolder.mStr;
        if (str != null) {
            versionedParcel.setOutputField(3);
            versionedParcel.writeString(str);
        }
        int i = sliceItemHolder.mInt;
        if (i != 0) {
            versionedParcel.setOutputField(4);
            versionedParcel.writeInt(i);
        }
        long j = sliceItemHolder.mLong;
        if (0 != j) {
            versionedParcel.setOutputField(5);
            versionedParcel.writeLong(j);
        }
        Bundle bundle = sliceItemHolder.mBundle;
        if (bundle != null) {
            versionedParcel.setOutputField(6);
            versionedParcel.writeBundle(bundle);
        }
    }
}
