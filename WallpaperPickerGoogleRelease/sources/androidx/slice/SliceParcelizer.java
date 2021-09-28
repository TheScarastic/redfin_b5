package androidx.slice;

import androidx.versionedparcelable.VersionedParcel;
import java.util.Arrays;
import java.util.Objects;
/* loaded from: classes.dex */
public final class SliceParcelizer {
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r3v7, types: [java.lang.Object[], java.lang.Object] */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static androidx.slice.Slice read(androidx.versionedparcelable.VersionedParcel r9) {
        /*
            androidx.slice.Slice r0 = new androidx.slice.Slice
            r0.<init>()
            androidx.slice.SliceSpec r1 = r0.mSpec
            r2 = 1
            androidx.versionedparcelable.VersionedParcelable r1 = r9.readVersionedParcelable(r1, r2)
            androidx.slice.SliceSpec r1 = (androidx.slice.SliceSpec) r1
            r0.mSpec = r1
            androidx.slice.SliceItem[] r1 = r0.mItems
            r3 = 2
            java.lang.Object[] r1 = r9.readArray(r1, r3)
            androidx.slice.SliceItem[] r1 = (androidx.slice.SliceItem[]) r1
            r0.mItems = r1
            java.lang.String[] r1 = r0.mHints
            r3 = 3
            java.lang.Object[] r1 = r9.readArray(r1, r3)
            java.lang.String[] r1 = (java.lang.String[]) r1
            r0.mHints = r1
            java.lang.String r1 = r0.mUri
            r3 = 4
            java.lang.String r9 = r9.readString(r1, r3)
            r0.mUri = r9
            androidx.slice.SliceItem[] r9 = r0.mItems
            int r9 = r9.length
            int r9 = r9 - r2
        L_0x0033:
            if (r9 < 0) goto L_0x007d
            androidx.slice.SliceItem[] r1 = r0.mItems
            r3 = r1[r9]
            java.lang.Object r3 = r3.mObj
            if (r3 != 0) goto L_0x007a
            java.lang.Class<androidx.slice.SliceItem> r3 = androidx.slice.SliceItem.class
            r4 = r1[r9]
            r5 = 0
            boolean r6 = androidx.slice.ArrayUtils.contains(r1, r4)
            if (r6 != 0) goto L_0x0049
            goto L_0x0070
        L_0x0049:
            int r6 = r1.length
            r7 = r5
        L_0x004b:
            if (r7 >= r6) goto L_0x0070
            r8 = r1[r7]
            boolean r8 = java.util.Objects.equals(r8, r4)
            if (r8 == 0) goto L_0x006d
            if (r6 != r2) goto L_0x0059
            r1 = 0
            goto L_0x0070
        L_0x0059:
            int r4 = r6 + -1
            java.lang.Object r3 = java.lang.reflect.Array.newInstance(r3, r4)
            java.lang.Object[] r3 = (java.lang.Object[]) r3
            java.lang.System.arraycopy(r1, r5, r3, r5, r7)
            int r4 = r7 + 1
            int r6 = r6 - r7
            int r6 = r6 - r2
            java.lang.System.arraycopy(r1, r4, r3, r7, r6)
            r1 = r3
            goto L_0x0070
        L_0x006d:
            int r7 = r7 + 1
            goto L_0x004b
        L_0x0070:
            androidx.slice.SliceItem[] r1 = (androidx.slice.SliceItem[]) r1
            r0.mItems = r1
            if (r1 != 0) goto L_0x007a
            androidx.slice.SliceItem[] r1 = new androidx.slice.SliceItem[r5]
            r0.mItems = r1
        L_0x007a:
            int r9 = r9 + -1
            goto L_0x0033
        L_0x007d:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.slice.SliceParcelizer.read(androidx.versionedparcelable.VersionedParcel):androidx.slice.Slice");
    }

    public static void write(Slice slice, VersionedParcel versionedParcel) {
        Objects.requireNonNull(versionedParcel);
        Objects.requireNonNull(slice);
        SliceSpec sliceSpec = slice.mSpec;
        if (sliceSpec != null) {
            versionedParcel.setOutputField(1);
            versionedParcel.writeVersionedParcelable(sliceSpec);
        }
        if (!Arrays.equals(Slice.NO_ITEMS, slice.mItems)) {
            versionedParcel.writeArray(slice.mItems, 2);
        }
        if (!Arrays.equals(Slice.NO_HINTS, slice.mHints)) {
            versionedParcel.writeArray(slice.mHints, 3);
        }
        String str = slice.mUri;
        if (str != null) {
            versionedParcel.setOutputField(4);
            versionedParcel.writeString(str);
        }
    }
}
