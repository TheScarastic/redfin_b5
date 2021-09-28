package kotlin.text;

import androidx.appcompat.R$styleable;
import java.util.ArrayList;
import java.util.List;
import kotlin.collections.CollectionsKt;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.collections.CollectionsKt__IterablesKt;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: Indent.kt */
/* access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class StringsKt__IndentKt extends StringsKt__AppendableKt {
    public static String trimIndent(String str) {
        Intrinsics.checkNotNullParameter(str, "$this$trimIndent");
        return replaceIndent(str, "");
    }

    public static final String replaceIndent(String str, String str2) {
        String str3;
        String invoke;
        Intrinsics.checkNotNullParameter(str, "$this$replaceIndent");
        Intrinsics.checkNotNullParameter(str2, "newIndent");
        List<String> lines = StringsKt__StringsKt.lines(str);
        ArrayList<String> arrayList = new ArrayList();
        for (Object obj : lines) {
            if (!StringsKt__StringsJVMKt.isBlank((String) obj)) {
                arrayList.add(obj);
            }
        }
        ArrayList arrayList2 = new ArrayList(CollectionsKt__IterablesKt.collectionSizeOrDefault(arrayList, 10));
        for (String str4 : arrayList) {
            arrayList2.add(Integer.valueOf(indentWidth$StringsKt__IndentKt(str4)));
        }
        Integer num = (Integer) CollectionsKt.minOrNull(arrayList2);
        int i = 0;
        int intValue = num != null ? num.intValue() : 0;
        int length = str.length() + (str2.length() * lines.size());
        Function1<String, String> indentFunction$StringsKt__IndentKt = getIndentFunction$StringsKt__IndentKt(str2);
        int i2 = CollectionsKt__CollectionsKt.getLastIndex(lines);
        ArrayList arrayList3 = new ArrayList();
        for (Object obj2 : lines) {
            int i3 = i + 1;
            if (i < 0) {
                CollectionsKt__CollectionsKt.throwIndexOverflow();
            }
            String str5 = (String) obj2;
            if ((i == 0 || i == i2) && StringsKt__StringsJVMKt.isBlank(str5)) {
                str3 = null;
            } else {
                String drop = StringsKt___StringsKt.drop(str5, intValue);
                if (!(drop == null || (invoke = indentFunction$StringsKt__IndentKt.invoke(drop)) == null)) {
                    str5 = invoke;
                }
                str3 = str5;
            }
            if (str3 != null) {
                arrayList3.add(str3);
            }
            i = i3;
        }
        String sb = ((StringBuilder) CollectionsKt___CollectionsKt.joinTo$default(arrayList3, new StringBuilder(length), "\n", null, null, 0, null, null, R$styleable.AppCompatTheme_windowMinWidthMajor, null)).toString();
        Intrinsics.checkNotNullExpressionValue(sb, "mapIndexedNotNull { inde…\"\\n\")\n        .toString()");
        return sb;
    }

    private static final Function1<String, String> getIndentFunction$StringsKt__IndentKt(String str) {
        if (str.length() == 0) {
            return StringsKt__IndentKt$getIndentFunction$1.INSTANCE;
        }
        return new Function1<String, String>(str) { // from class: kotlin.text.StringsKt__IndentKt$getIndentFunction$2
            final /* synthetic */ String $indent;

            /* access modifiers changed from: package-private */
            {
                this.$indent = r1;
            }

            public final String invoke(String str2) {
                Intrinsics.checkNotNullParameter(str2, "line");
                return this.$indent + str2;
            }
        };
    }

    private static final int indentWidth$StringsKt__IndentKt(String str) {
        int length = str.length();
        int i = 0;
        while (true) {
            if (i >= length) {
                i = -1;
                break;
            } else if (!CharsKt__CharJVMKt.isWhitespace(str.charAt(i))) {
                break;
            } else {
                i++;
            }
        }
        return i == -1 ? str.length() : i;
    }
}
