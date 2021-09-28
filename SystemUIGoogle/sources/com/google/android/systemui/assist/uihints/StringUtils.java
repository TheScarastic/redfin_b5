package com.google.android.systemui.assist.uihints;

import java.lang.reflect.Array;
/* access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class StringUtils {

    /* loaded from: classes2.dex */
    public static final class StringStabilityInfo {
        final String stable;
        final String unstable;

        StringStabilityInfo(String str, String str2) {
            this.stable = str == null ? "" : str;
            this.unstable = str2 == null ? "" : str2;
        }

        StringStabilityInfo(String str, int i) {
            if (i >= str.length()) {
                this.stable = str;
                this.unstable = "";
                return;
            }
            int i2 = i + 1;
            this.stable = str.substring(0, i2);
            this.unstable = str.substring(i2);
        }
    }

    public static StringStabilityInfo calculateStringStabilityInfo(String str, String str2) {
        if (isNullOrEmpty(str) || isNullOrEmpty(str2)) {
            return new StringStabilityInfo("", str2);
        }
        return getRightMostStabilityInfoLeaf(str2, 0, str.length(), 0, str2.length(), calculateLongestCommonSubstringMatrix(str.toLowerCase(), str2.toLowerCase()));
    }

    private static boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }

    private static int[][] calculateLongestCommonSubstringMatrix(String str, String str2) {
        int[][] iArr = (int[][]) Array.newInstance(int.class, str.length(), str2.length());
        int i = 0;
        while (i < str.length()) {
            char charAt = str.charAt(i);
            int i2 = 0;
            while (i2 < str2.length()) {
                if (charAt == str2.charAt(i2)) {
                    iArr[i][i2] = ((i == 0 || i2 == 0) ? 0 : iArr[i - 1][i2 - 1]) + (charAt == ' ' ? 0 : 1);
                }
                i2++;
            }
            i++;
        }
        return iArr;
    }

    private static StringStabilityInfo getRightMostStabilityInfoLeaf(String str, int i, int i2, int i3, int i4, int[][] iArr) {
        int i5;
        int i6 = -1;
        int i7 = 0;
        int i8 = -1;
        while (i < i2) {
            for (int i9 = i3; i9 < i4; i9++) {
                if (iArr[i][i9] > i7) {
                    i7 = iArr[i][i9];
                    i6 = i;
                    i8 = i9;
                }
            }
            i++;
        }
        if (i7 == 0) {
            return new StringStabilityInfo(str, i3 - 1);
        }
        int i10 = i6 + 1;
        if (i10 == i2 || (i5 = i8 + 1) == i4) {
            return new StringStabilityInfo(str, i8);
        }
        return getRightMostStabilityInfoLeaf(str, i10, i2, i5, i4, iArr);
    }
}
