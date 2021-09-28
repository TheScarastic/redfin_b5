package androidx.slice;

import androidx.versionedparcelable.VersionedParcelable;
/* loaded from: classes.dex */
public final class SliceSpec implements VersionedParcelable {
    public int mRevision;
    public String mType;

    public SliceSpec() {
        this.mRevision = 1;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof SliceSpec)) {
            return false;
        }
        SliceSpec sliceSpec = (SliceSpec) obj;
        if (!this.mType.equals(sliceSpec.mType) || this.mRevision != sliceSpec.mRevision) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return this.mType.hashCode() + this.mRevision;
    }

    public String toString() {
        return String.format("SliceSpec{%s,%d}", this.mType, Integer.valueOf(this.mRevision));
    }

    public SliceSpec(String str, int i) {
        this.mRevision = 1;
        this.mType = str;
        this.mRevision = i;
    }
}
