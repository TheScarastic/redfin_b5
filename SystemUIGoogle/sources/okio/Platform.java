package okio;

import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Charsets;
/* compiled from: -Platform.kt */
/* renamed from: okio.-Platform  reason: invalid class name */
/* loaded from: classes2.dex */
public final class Platform {
    public static final String toUtf8String(byte[] bArr) {
        Intrinsics.checkNotNullParameter(bArr, "<this>");
        return new String(bArr, Charsets.UTF_8);
    }

    public static final byte[] asUtf8ToByteArray(String str) {
        Intrinsics.checkNotNullParameter(str, "<this>");
        byte[] bytes = str.getBytes(Charsets.UTF_8);
        Intrinsics.checkNotNullExpressionValue(bytes, "(this as java.lang.String).getBytes(charset)");
        return bytes;
    }
}
