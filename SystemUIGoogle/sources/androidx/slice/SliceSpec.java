package androidx.slice;

import androidx.versionedparcelable.VersionedParcelable;
/* loaded from: classes.dex */
public final class SliceSpec implements VersionedParcelable {
    int mRevision;
    String mType;

    public SliceSpec() {
        this.mRevision = 1;
    }

    public SliceSpec(String str, int i) {
        this.mRevision = 1;
        this.mType = str;
        this.mRevision = i;
    }

    public String getType() {
        return this.mType;
    }

    public int getRevision() {
        return this.mRevision;
    }

    public boolean canRender(SliceSpec sliceSpec) {
        if (this.mType.equals(sliceSpec.mType) && this.mRevision >= sliceSpec.mRevision) {
            return true;
        }
        return false;
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
}
