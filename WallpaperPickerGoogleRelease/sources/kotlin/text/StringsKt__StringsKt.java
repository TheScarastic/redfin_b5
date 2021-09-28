package kotlin.text;

import kotlin.collections.ArraysKt___ArraysKt;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* loaded from: classes.dex */
public class StringsKt__StringsKt extends StringsKt__StringNumberConversionsKt {
    public static final int getLastIndex(@NotNull CharSequence charSequence) {
        Intrinsics.checkNotNullParameter(charSequence, "$this$lastIndex");
        return charSequence.length() - 1;
    }

    /* JADX WARNING: Removed duplicated region for block: B:48:0x0096  */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x0095 A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static final int indexOf$StringsKt__StringsKt(java.lang.CharSequence r7, java.lang.CharSequence r8, int r9, int r10, boolean r11, boolean r12) {
        /*
            r0 = -1
            r1 = 0
            if (r12 != 0) goto L_0x0014
            if (r9 >= 0) goto L_0x0007
            r9 = r1
        L_0x0007:
            kotlin.ranges.IntRange r12 = new kotlin.ranges.IntRange
            int r2 = r7.length()
            if (r10 <= r2) goto L_0x0010
            r10 = r2
        L_0x0010:
            r12.<init>(r9, r10)
            goto L_0x0023
        L_0x0014:
            int r12 = getLastIndex(r7)
            if (r9 <= r12) goto L_0x001b
            r9 = r12
        L_0x001b:
            if (r10 >= 0) goto L_0x001e
            r10 = r1
        L_0x001e:
            kotlin.ranges.IntProgression r12 = new kotlin.ranges.IntProgression
            r12.<init>(r9, r10, r0)
        L_0x0023:
            boolean r9 = r7 instanceof java.lang.String
            if (r9 == 0) goto L_0x0050
            boolean r9 = r8 instanceof java.lang.String
            if (r9 == 0) goto L_0x0050
            int r9 = r12.first
            int r10 = r12.last
            int r12 = r12.step
            if (r12 < 0) goto L_0x0036
            if (r9 > r10) goto L_0x009a
            goto L_0x0038
        L_0x0036:
            if (r9 < r10) goto L_0x009a
        L_0x0038:
            r1 = r8
            java.lang.String r1 = (java.lang.String) r1
            r2 = 0
            r3 = r7
            java.lang.String r3 = (java.lang.String) r3
            int r5 = r8.length()
            r4 = r9
            r6 = r11
            boolean r1 = regionMatches(r1, r2, r3, r4, r5, r6)
            if (r1 == 0) goto L_0x004c
            return r9
        L_0x004c:
            if (r9 == r10) goto L_0x009a
            int r9 = r9 + r12
            goto L_0x0038
        L_0x0050:
            int r9 = r12.first
            int r10 = r12.last
            int r12 = r12.step
            if (r12 < 0) goto L_0x005b
            if (r9 > r10) goto L_0x009a
            goto L_0x005d
        L_0x005b:
            if (r9 < r10) goto L_0x009a
        L_0x005d:
            int r2 = r8.length()
            java.lang.String r3 = "other"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r7, r3)
            if (r9 < 0) goto L_0x0092
            int r3 = r8.length()
            int r3 = r3 - r2
            if (r3 < 0) goto L_0x0092
            int r3 = r7.length()
            int r3 = r3 - r2
            if (r9 <= r3) goto L_0x0077
            goto L_0x0092
        L_0x0077:
            r3 = r1
        L_0x0078:
            if (r3 >= r2) goto L_0x0090
            int r4 = r1 + r3
            char r4 = r8.charAt(r4)
            int r5 = r9 + r3
            char r5 = r7.charAt(r5)
            boolean r4 = kotlin.text.CharsKt__CharKt.equals(r4, r5, r11)
            if (r4 != 0) goto L_0x008d
            goto L_0x0092
        L_0x008d:
            int r3 = r3 + 1
            goto L_0x0078
        L_0x0090:
            r2 = 1
            goto L_0x0093
        L_0x0092:
            r2 = r1
        L_0x0093:
            if (r2 == 0) goto L_0x0096
            return r9
        L_0x0096:
            if (r9 == r10) goto L_0x009a
            int r9 = r9 + r12
            goto L_0x005d
        L_0x009a:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlin.text.StringsKt__StringsKt.indexOf$StringsKt__StringsKt(java.lang.CharSequence, java.lang.CharSequence, int, int, boolean, boolean):int");
    }

    public static int indexOf$default(CharSequence charSequence, char c, int i, boolean z, int i2) {
        boolean z2;
        if ((i2 & 2) != 0) {
            i = 0;
        }
        if ((i2 & 4) != 0) {
            z = false;
        }
        if (!z) {
            return ((String) charSequence).indexOf(c, i);
        }
        char[] cArr = {c};
        if (!z) {
            return ((String) charSequence).indexOf(ArraysKt___ArraysKt.single(cArr), i);
        }
        if (i < 0) {
            i = 0;
        }
        int lastIndex = getLastIndex(charSequence);
        if (i <= lastIndex) {
            while (true) {
                char charAt = charSequence.charAt(i);
                int i3 = 0;
                while (true) {
                    if (i3 >= 1) {
                        z2 = false;
                        break;
                    } else if (CharsKt__CharKt.equals(cArr[i3], charAt, z)) {
                        z2 = true;
                        break;
                    } else {
                        i3++;
                    }
                }
                if (!z2) {
                    if (i == lastIndex) {
                        break;
                    }
                    i++;
                } else {
                    return i;
                }
            }
        }
        return -1;
    }

    public static final boolean regionMatches(@NotNull String str, int i, @NotNull String str2, int i2, int i3, boolean z) {
        Intrinsics.checkNotNullParameter(str, "$this$regionMatches");
        Intrinsics.checkNotNullParameter(str2, "other");
        if (!z) {
            return str.regionMatches(i, str2, i2, i3);
        }
        return str.regionMatches(z, i, str2, i2, i3);
    }

    public static boolean startsWith$default(String str, String str2, boolean z, int i) {
        if ((i & 2) != 0) {
            z = false;
        }
        if (!z) {
            return str.startsWith(str2);
        }
        return regionMatches(str, 0, str2, 0, str2.length(), z);
    }

    public static String substringAfter$default(String str, String str2, String str3, int i) {
        String str4 = (i & 2) != 0 ? str : null;
        Intrinsics.checkNotNullParameter(str4, "missingDelimiterValue");
        int indexOf$default = indexOf$default((CharSequence) str, str2, 0, false, 6);
        if (indexOf$default == -1) {
            return str4;
        }
        String substring = str.substring(str2.length() + indexOf$default, str.length());
        Intrinsics.checkNotNullExpressionValue(substring, "(this as java.lang.Strin…ing(startIndex, endIndex)");
        return substring;
    }

    public static String substringAfterLast$default(String str, char c, String str2, int i) {
        String str3 = (i & 2) != 0 ? str : null;
        Intrinsics.checkNotNullParameter(str, "$this$substringAfterLast");
        Intrinsics.checkNotNullParameter(str3, "missingDelimiterValue");
        int lastIndexOf = str.lastIndexOf(c, getLastIndex(str));
        if (lastIndexOf == -1) {
            return str3;
        }
        String substring = str.substring(lastIndexOf + 1, str.length());
        Intrinsics.checkNotNullExpressionValue(substring, "(this as java.lang.Strin…ing(startIndex, endIndex)");
        return substring;
    }

    public static int indexOf$default(CharSequence charSequence, String str, int i, boolean z, int i2) {
        int i3 = (i2 & 2) != 0 ? 0 : i;
        boolean z2 = (i2 & 4) != 0 ? false : z;
        if (!z2) {
            return ((String) charSequence).indexOf(str, i3);
        }
        return indexOf$StringsKt__StringsKt(charSequence, str, i3, charSequence.length(), z2, false);
    }
}
