package com.google.protobuf;

import com.google.protobuf.ByteString;
import java.util.List;
import java.util.Map;
/* loaded from: classes.dex */
public final class MessageLiteToString {
    public static final String camelCaseToSnakeCase(String str) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char charAt = str.charAt(i);
            if (Character.isUpperCase(charAt)) {
                sb.append("_");
            }
            sb.append(Character.toLowerCase(charAt));
        }
        return sb.toString();
    }

    public static final void printField(StringBuilder sb, int i, String str, Object obj) {
        if (obj instanceof List) {
            for (Object obj2 : (List) obj) {
                printField(sb, i, str, obj2);
            }
        } else if (obj instanceof Map) {
            for (Map.Entry entry : ((Map) obj).entrySet()) {
                printField(sb, i, str, entry);
            }
        } else {
            sb.append('\n');
            int i2 = 0;
            for (int i3 = 0; i3 < i; i3++) {
                sb.append(' ');
            }
            sb.append(str);
            if (obj instanceof String) {
                sb.append(": \"");
                ByteString byteString = ByteString.EMPTY;
                sb.append(TextFormatEscaper.escapeBytes(new ByteString.LiteralByteString(((String) obj).getBytes(Internal.UTF_8))));
                sb.append('\"');
            } else if (obj instanceof ByteString) {
                sb.append(": \"");
                sb.append(TextFormatEscaper.escapeBytes((ByteString) obj));
                sb.append('\"');
            } else if (obj instanceof GeneratedMessageLite) {
                sb.append(" {");
                reflectivePrintWithIndent((GeneratedMessageLite) obj, sb, i + 2);
                sb.append("\n");
                while (i2 < i) {
                    sb.append(' ');
                    i2++;
                }
                sb.append("}");
            } else if (obj instanceof Map.Entry) {
                sb.append(" {");
                Map.Entry entry2 = (Map.Entry) obj;
                int i4 = i + 2;
                printField(sb, i4, "key", entry2.getKey());
                printField(sb, i4, "value", entry2.getValue());
                sb.append("\n");
                while (i2 < i) {
                    sb.append(' ');
                    i2++;
                }
                sb.append("}");
            } else {
                sb.append(": ");
                sb.append(obj.toString());
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:54:0x01cd, code lost:
        if (((java.lang.Integer) r11).intValue() == 0) goto L_0x0222;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:58:0x01de, code lost:
        if (((java.lang.Float) r11).floatValue() == 0.0f) goto L_0x0222;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:62:0x01f0, code lost:
        if (((java.lang.Double) r11).doubleValue() == 0.0d) goto L_0x0222;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:77:0x0222, code lost:
        r7 = true;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void reflectivePrintWithIndent(com.google.protobuf.MessageLite r18, java.lang.StringBuilder r19, int r20) {
        /*
        // Method dump skipped, instructions count: 652
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.protobuf.MessageLiteToString.reflectivePrintWithIndent(com.google.protobuf.MessageLite, java.lang.StringBuilder, int):void");
    }
}
