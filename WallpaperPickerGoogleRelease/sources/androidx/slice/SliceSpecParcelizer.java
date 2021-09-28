package androidx.slice;

import androidx.versionedparcelable.VersionedParcel;
import java.util.Objects;
/* loaded from: classes.dex */
public final class SliceSpecParcelizer {
    public static SliceSpec read(VersionedParcel versionedParcel) {
        SliceSpec sliceSpec = new SliceSpec();
        sliceSpec.mType = versionedParcel.readString(sliceSpec.mType, 1);
        sliceSpec.mRevision = versionedParcel.readInt(sliceSpec.mRevision, 2);
        return sliceSpec;
    }

    public static void write(SliceSpec sliceSpec, VersionedParcel versionedParcel) {
        Objects.requireNonNull(versionedParcel);
        String str = sliceSpec.mType;
        versionedParcel.setOutputField(1);
        versionedParcel.writeString(str);
        int i = sliceSpec.mRevision;
        if (1 != i) {
            versionedParcel.setOutputField(2);
            versionedParcel.writeInt(i);
        }
    }
}
