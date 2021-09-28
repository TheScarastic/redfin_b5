package com.adobe.xmp.impl.xpath;

import androidx.recyclerview.widget.RecyclerView;
import com.adobe.xmp.XMPException;
import com.adobe.xmp.XMPMetaFactory;
import com.adobe.xmp.impl.Utils;
import com.adobe.xmp.impl.XMPSchemaRegistryImpl;
import com.adobe.xmp.properties.XMPAliasInfo;
import com.android.systemui.shared.system.QuickStepContract;
/* loaded from: classes.dex */
public final class XMPPathParser {
    public static XMPPath expandXPath(String str, String str2) throws XMPException {
        int i;
        XMPPathSegment xMPPathSegment;
        boolean z;
        char c;
        int i2;
        XMPPathSegment xMPPathSegment2;
        XMPPath xMPPath = new XMPPath();
        int i3 = 0;
        while (i3 < str2.length() && "/[*".indexOf(str2.charAt(i3)) < 0) {
            i3++;
        }
        if (i3 != 0) {
            String verifyXPathRoot = verifyXPathRoot(str, str2.substring(0, i3));
            XMPAliasInfo findAlias = ((XMPSchemaRegistryImpl) XMPMetaFactory.schema).findAlias(verifyXPathRoot);
            if (findAlias == null) {
                xMPPath.segments.add(new XMPPathSegment(str, RecyclerView.UNDEFINED_DURATION));
                xMPPath.segments.add(new XMPPathSegment(verifyXPathRoot, 1));
            } else {
                xMPPath.segments.add(new XMPPathSegment(findAlias.getNamespace(), RecyclerView.UNDEFINED_DURATION));
                XMPPathSegment xMPPathSegment3 = new XMPPathSegment(verifyXPathRoot(findAlias.getNamespace(), findAlias.getPropName()), 1);
                xMPPathSegment3.alias = true;
                xMPPathSegment3.aliasForm = findAlias.getAliasForm().options;
                xMPPath.segments.add(xMPPathSegment3);
                if (findAlias.getAliasForm().getOption(QuickStepContract.SYSUI_STATE_TRACING_ENABLED)) {
                    XMPPathSegment xMPPathSegment4 = new XMPPathSegment("[?xml:lang='x-default']", 5);
                    xMPPathSegment4.alias = true;
                    xMPPathSegment4.aliasForm = findAlias.getAliasForm().options;
                    xMPPath.segments.add(xMPPathSegment4);
                } else if (findAlias.getAliasForm().getOption(QuickStepContract.SYSUI_STATE_STATUS_BAR_KEYGUARD_SHOWING_OCCLUDED)) {
                    XMPPathSegment xMPPathSegment5 = new XMPPathSegment("[1]", 3);
                    xMPPathSegment5.alias = true;
                    xMPPathSegment5.aliasForm = findAlias.getAliasForm().options;
                    xMPPath.segments.add(xMPPathSegment5);
                }
            }
            int i4 = 0;
            int i5 = 0;
            while (i3 < str2.length()) {
                if (str2.charAt(i3) == '/' && (i3 = i3 + 1) >= str2.length()) {
                    throw new XMPException("Empty XMPPath segment", 102);
                } else if (str2.charAt(i3) != '*' || ((i3 = i3 + 1) < str2.length() && str2.charAt(i3) == '[')) {
                    if (str2.charAt(i3) != '[') {
                        i4 = i3;
                        while (i4 < str2.length() && "/[*".indexOf(str2.charAt(i4)) < 0) {
                            i4++;
                        }
                        if (i4 != i3) {
                            xMPPathSegment = new XMPPathSegment(str2.substring(i3, i4), 1);
                            i = i3;
                            i3 = i4;
                        } else {
                            throw new XMPException("Empty XMPPath segment", 102);
                        }
                    } else {
                        i = i3 + 1;
                        if ('0' > str2.charAt(i) || str2.charAt(i) > '9') {
                            int i6 = i;
                            while (i6 < str2.length() && str2.charAt(i6) != ']' && str2.charAt(i6) != '=') {
                                i6++;
                            }
                            if (i6 >= str2.length()) {
                                throw new XMPException("Missing ']' or '=' for array index", 102);
                            } else if (str2.charAt(i6) != ']') {
                                int i7 = i6 + 1;
                                char charAt = str2.charAt(i7);
                                if (charAt == '\'' || charAt == '\"') {
                                    int i8 = i7 + 1;
                                    while (i8 < str2.length()) {
                                        if (str2.charAt(i8) == charAt) {
                                            int i9 = i8 + 1;
                                            if (i9 >= str2.length() || str2.charAt(i9) != charAt) {
                                                break;
                                            }
                                            i8 = i9;
                                        }
                                        i8++;
                                    }
                                    if (i8 < str2.length()) {
                                        xMPPathSegment = new XMPPathSegment(null, 6);
                                        i2 = i8 + 1;
                                        i4 = i6;
                                        if (i2 < str2.length() || str2.charAt(i2) != ']') {
                                            throw new XMPException("Missing ']' for array index", 102);
                                        }
                                        int i10 = i2 + 1;
                                        xMPPathSegment.name = str2.substring(i3, i10);
                                        i3 = i10;
                                    } else {
                                        throw new XMPException("No terminating quote for array selector", 102);
                                    }
                                } else {
                                    throw new XMPException("Invalid quote in array selector", 102);
                                }
                            } else if ("[last()".equals(str2.substring(i3, i6))) {
                                xMPPathSegment2 = new XMPPathSegment(null, 4);
                                i = i6;
                            } else {
                                throw new XMPException("Invalid non-numeric array index", 102);
                            }
                        } else {
                            while (i < str2.length() && '0' <= str2.charAt(i) && str2.charAt(i) <= '9') {
                                i++;
                            }
                            xMPPathSegment2 = new XMPPathSegment(null, 3);
                        }
                        xMPPathSegment = xMPPathSegment2;
                        i2 = i;
                        i = i5;
                        if (i2 < str2.length()) {
                        }
                        throw new XMPException("Missing ']' for array index", 102);
                    }
                    int i11 = xMPPathSegment.kind;
                    if (i11 == 1) {
                        if (xMPPathSegment.name.charAt(0) == '@') {
                            String valueOf = String.valueOf(xMPPathSegment.name.substring(1));
                            String concat = valueOf.length() != 0 ? "?".concat(valueOf) : new String("?");
                            xMPPathSegment.name = concat;
                            if (!"?xml:lang".equals(concat)) {
                                throw new XMPException("Only xml:lang allowed with '@'", 102);
                            }
                        }
                        if (xMPPathSegment.name.charAt(0) == '?') {
                            i++;
                            xMPPathSegment.kind = 2;
                        }
                        verifyQualName(str2.substring(i, i4));
                        z = false;
                    } else {
                        z = false;
                        if (i11 == 6) {
                            if (xMPPathSegment.name.charAt(1) == '@') {
                                String valueOf2 = String.valueOf(xMPPathSegment.name.substring(2));
                                String concat2 = valueOf2.length() != 0 ? "[?".concat(valueOf2) : new String("[?");
                                xMPPathSegment.name = concat2;
                                if (!concat2.startsWith("[?xml:lang=")) {
                                    throw new XMPException("Only xml:lang allowed with '@'", 102);
                                }
                            }
                            if (xMPPathSegment.name.charAt(1) == '?') {
                                i++;
                                c = 5;
                                xMPPathSegment.kind = 5;
                                verifyQualName(str2.substring(i, i4));
                                xMPPath.segments.add(xMPPathSegment);
                                i5 = i;
                            }
                        }
                    }
                    c = 5;
                    xMPPath.segments.add(xMPPathSegment);
                    i5 = i;
                } else {
                    throw new XMPException("Missing '[' after '*'", 102);
                }
            }
            return xMPPath;
        }
        throw new XMPException("Empty initial XMPPath step", 102);
    }

    public static void verifyQualName(String str) throws XMPException {
        String str2;
        int indexOf = str.indexOf(58);
        if (indexOf > 0) {
            String substring = str.substring(0, indexOf);
            if (Utils.isXMLNameNS(substring)) {
                XMPSchemaRegistryImpl xMPSchemaRegistryImpl = (XMPSchemaRegistryImpl) XMPMetaFactory.schema;
                synchronized (xMPSchemaRegistryImpl) {
                    if (!substring.endsWith(":")) {
                        substring = substring.concat(":");
                    }
                    str2 = (String) xMPSchemaRegistryImpl.prefixToNamespaceMap.get(substring);
                }
                if (str2 == null) {
                    throw new XMPException("Unknown namespace prefix for qualified name", 102);
                }
                return;
            }
        }
        throw new XMPException("Ill-formed qualified name", 102);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x001c, code lost:
        if ((r0 > 255 || com.adobe.xmp.impl.Utils.xmlNameStartChars[r0]) == false) goto L_0x003d;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void verifySimpleXMLName(java.lang.String r6) throws com.adobe.xmp.XMPException {
        /*
            boolean[] r0 = com.adobe.xmp.impl.Utils.xmlNameStartChars
            int r0 = r6.length()
            r1 = 255(0xff, float:3.57E-43)
            r2 = 0
            r3 = 1
            if (r0 <= 0) goto L_0x001f
            char r0 = r6.charAt(r2)
            if (r0 > r1) goto L_0x001b
            boolean[] r4 = com.adobe.xmp.impl.Utils.xmlNameStartChars
            boolean r0 = r4[r0]
            if (r0 == 0) goto L_0x0019
            goto L_0x001b
        L_0x0019:
            r0 = r2
            goto L_0x001c
        L_0x001b:
            r0 = r3
        L_0x001c:
            if (r0 != 0) goto L_0x001f
            goto L_0x003d
        L_0x001f:
            r0 = r3
        L_0x0020:
            int r4 = r6.length()
            if (r0 >= r4) goto L_0x003c
            char r4 = r6.charAt(r0)
            if (r4 > r1) goto L_0x0035
            boolean[] r5 = com.adobe.xmp.impl.Utils.xmlNameChars
            boolean r4 = r5[r4]
            if (r4 == 0) goto L_0x0033
            goto L_0x0035
        L_0x0033:
            r4 = r2
            goto L_0x0036
        L_0x0035:
            r4 = r3
        L_0x0036:
            if (r4 != 0) goto L_0x0039
            goto L_0x003d
        L_0x0039:
            int r0 = r0 + 1
            goto L_0x0020
        L_0x003c:
            r2 = r3
        L_0x003d:
            if (r2 == 0) goto L_0x0040
            return
        L_0x0040:
            com.adobe.xmp.XMPException r6 = new com.adobe.xmp.XMPException
            r0 = 102(0x66, float:1.43E-43)
            java.lang.String r1 = "Bad XML name"
            r6.<init>(r1, r0)
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.adobe.xmp.impl.xpath.XMPPathParser.verifySimpleXMLName(java.lang.String):void");
    }

    public static String verifyXPathRoot(String str, String str2) throws XMPException {
        if (str == null || str.length() == 0) {
            throw new XMPException("Schema namespace URI is required", 101);
        } else if (str2.charAt(0) == '?' || str2.charAt(0) == '@') {
            throw new XMPException("Top level name must not be a qualifier", 102);
        } else if (str2.indexOf(47) >= 0 || str2.indexOf(91) >= 0) {
            throw new XMPException("Top level name must be simple", 102);
        } else {
            String namespacePrefix = ((XMPSchemaRegistryImpl) XMPMetaFactory.schema).getNamespacePrefix(str);
            if (namespacePrefix != null) {
                int indexOf = str2.indexOf(58);
                if (indexOf < 0) {
                    verifySimpleXMLName(str2);
                    return str2.length() != 0 ? namespacePrefix.concat(str2) : new String(namespacePrefix);
                }
                verifySimpleXMLName(str2.substring(0, indexOf));
                verifySimpleXMLName(str2.substring(indexOf));
                String substring = str2.substring(0, indexOf + 1);
                String namespacePrefix2 = ((XMPSchemaRegistryImpl) XMPMetaFactory.schema).getNamespacePrefix(str);
                if (namespacePrefix2 == null) {
                    throw new XMPException("Unknown schema namespace prefix", 101);
                } else if (substring.equals(namespacePrefix2)) {
                    return str2;
                } else {
                    throw new XMPException("Schema namespace URI and prefix mismatch", 101);
                }
            } else {
                throw new XMPException("Unregistered schema namespace URI", 101);
            }
        }
    }
}
