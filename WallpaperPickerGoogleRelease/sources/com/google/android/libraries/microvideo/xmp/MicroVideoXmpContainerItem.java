package com.google.android.libraries.microvideo.xmp;

import com.adobe.xmp.XMPException;
import com.adobe.xmp.XMPMeta;
import com.adobe.xmp.XMPPathFactory$$ExternalSyntheticOutline0;
import com.adobe.xmp.impl.ParameterAsserts;
import com.adobe.xmp.impl.XMPMetaImpl;
import com.adobe.xmp.impl.XMPNode;
import com.adobe.xmp.impl.XMPNodeUtils;
import com.adobe.xmp.impl.xpath.XMPPath;
import com.adobe.xmp.impl.xpath.XMPPathParser;
import java.util.Objects;
/* loaded from: classes.dex */
public abstract class MicroVideoXmpContainerItem {
    public static String getStructField(XMPMeta xMPMeta, String str, String str2, String str3, String str4) throws XMPException {
        String valueOf = String.valueOf(str2);
        if (str3.length() == 0) {
            throw new XMPException("Empty field namespace URI", 101);
        } else if (str4.length() != 0) {
            XMPPath expandXPath = XMPPathParser.expandXPath(str3, str4);
            if (expandXPath.size() == 2) {
                String str5 = expandXPath.getSegment(1).name;
                StringBuilder sb = new StringBuilder(XMPPathFactory$$ExternalSyntheticOutline0.m(str5, 1));
                sb.append('/');
                sb.append(str5);
                String valueOf2 = String.valueOf(sb.toString());
                String concat = valueOf2.length() != 0 ? valueOf.concat(valueOf2) : new String(valueOf);
                XMPMetaImpl xMPMetaImpl = (XMPMetaImpl) xMPMeta;
                Objects.requireNonNull(xMPMetaImpl);
                ParameterAsserts.assertSchemaNS(str);
                ParameterAsserts.assertPropName(concat);
                XMPNode findNode = XMPNodeUtils.findNode(xMPMetaImpl.tree, XMPPathParser.expandXPath(str, concat), false, null);
                XMPMetaImpl.AnonymousClass2 r4 = findNode != null ? 
                /*  JADX ERROR: Method code generation error
                    jadx.core.utils.exceptions.CodegenException: Error generate insn: ?: TERNARY(r4v5 'r4' com.adobe.xmp.impl.XMPMetaImpl$2) = ((r3v5 'findNode' com.adobe.xmp.impl.XMPNode) != (null com.adobe.xmp.impl.XMPNode)) ? (wrap: com.adobe.xmp.impl.XMPMetaImpl$2 : 0x006d: CONSTRUCTOR  (r4v7 'r4' com.adobe.xmp.impl.XMPMetaImpl$2 A[REMOVE]) = 
                      (wrap: java.lang.Object : 0x0067: INVOKE  (r2v7 java.lang.Object A[REMOVE]) = (r2v4 'xMPMetaImpl' com.adobe.xmp.impl.XMPMetaImpl), (0 int), (r3v5 'findNode' com.adobe.xmp.impl.XMPNode) type: VIRTUAL call: com.adobe.xmp.impl.XMPMetaImpl.evaluateNodeValue(int, com.adobe.xmp.impl.XMPNode):java.lang.Object)
                      (r3v5 'findNode' com.adobe.xmp.impl.XMPNode)
                     call: com.adobe.xmp.impl.XMPMetaImpl.2.<init>(java.lang.Object, com.adobe.xmp.impl.XMPNode):void type: CONSTRUCTOR) : (null com.adobe.xmp.impl.XMPMetaImpl$2) in method: com.google.android.libraries.microvideo.xmp.MicroVideoXmpContainerItem.getStructField(com.adobe.xmp.XMPMeta, java.lang.String, java.lang.String, java.lang.String, java.lang.String):java.lang.String, file: classes.dex
                    	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:285)
                    	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:248)
                    	at jadx.core.codegen.RegionGen.makeSimpleBlock(RegionGen.java:105)
                    	at jadx.core.dex.nodes.IBlock.generate(IBlock.java:15)
                    	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:65)
                    	at jadx.core.dex.regions.Region.generate(Region.java:35)
                    	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:65)
                    	at jadx.core.codegen.RegionGen.makeRegionIndent(RegionGen.java:94)
                    	at jadx.core.codegen.RegionGen.makeIf(RegionGen.java:137)
                    	at jadx.core.dex.regions.conditions.IfRegion.generate(IfRegion.java:137)
                    	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:65)
                    	at jadx.core.dex.regions.Region.generate(Region.java:35)
                    	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:65)
                    	at jadx.core.dex.regions.Region.generate(Region.java:35)
                    	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:65)
                    	at jadx.core.codegen.RegionGen.makeRegionIndent(RegionGen.java:94)
                    	at jadx.core.codegen.RegionGen.makeIf(RegionGen.java:137)
                    	at jadx.core.codegen.RegionGen.connectElseIf(RegionGen.java:170)
                    	at jadx.core.codegen.RegionGen.makeIf(RegionGen.java:147)
                    	at jadx.core.dex.regions.conditions.IfRegion.generate(IfRegion.java:137)
                    	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:65)
                    	at jadx.core.dex.regions.Region.generate(Region.java:35)
                    	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:65)
                    	at jadx.core.codegen.MethodGen.addRegionInsns(MethodGen.java:261)
                    	at jadx.core.codegen.MethodGen.addInstructions(MethodGen.java:254)
                    	at jadx.core.codegen.ClassGen.addMethodCode(ClassGen.java:349)
                    	at jadx.core.codegen.ClassGen.addMethod(ClassGen.java:302)
                    	at jadx.core.codegen.ClassGen.lambda$addInnerClsAndMethods$2(ClassGen.java:271)
                    	at java.base/java.util.stream.ForEachOps$ForEachOp$OfRef.accept(ForEachOps.java:183)
                    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1541)
                    	at java.base/java.util.stream.SortedOps$RefSortingSink.end(SortedOps.java:395)
                    	at java.base/java.util.stream.Sink$ChainedReference.end(Sink.java:258)
                    Caused by: jadx.core.utils.exceptions.JadxRuntimeException: Expected class to be processed at this point, class: com.adobe.xmp.impl.XMPMetaImpl, state: GENERATED_AND_UNLOADED
                    	at jadx.core.dex.nodes.ClassNode.ensureProcessed(ClassNode.java:259)
                    	at jadx.core.codegen.InsnGen.makeConstructor(InsnGen.java:675)
                    	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:393)
                    	at jadx.core.codegen.InsnGen.addWrappedArg(InsnGen.java:141)
                    	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:120)
                    	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:104)
                    	at jadx.core.codegen.InsnGen.makeTernary(InsnGen.java:1022)
                    	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:511)
                    	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:278)
                    	... 31 more
                    */
                /*
                    java.lang.String r4 = java.lang.String.valueOf(r4)
                    int r0 = r5.length()
                    if (r0 == 0) goto L_0x008c
                    r0 = 102(0x66, float:1.43E-43)
                    int r1 = r6.length()
                    if (r1 == 0) goto L_0x0084
                    com.adobe.xmp.impl.xpath.XMPPath r5 = com.adobe.xmp.impl.xpath.XMPPathParser.expandXPath(r5, r6)
                    int r6 = r5.size()
                    r1 = 2
                    if (r6 != r1) goto L_0x007c
                    r6 = 1
                    com.adobe.xmp.impl.xpath.XMPPathSegment r5 = r5.getSegment(r6)
                    java.lang.String r5 = r5.name
                    int r6 = com.adobe.xmp.XMPPathFactory$$ExternalSyntheticOutline0.m(r5, r6)
                    java.lang.StringBuilder r0 = new java.lang.StringBuilder
                    r0.<init>(r6)
                    r6 = 47
                    r0.append(r6)
                    r0.append(r5)
                    java.lang.String r5 = r0.toString()
                    java.lang.String r5 = java.lang.String.valueOf(r5)
                    int r6 = r5.length()
                    if (r6 == 0) goto L_0x0048
                    java.lang.String r4 = r4.concat(r5)
                    goto L_0x004e
                L_0x0048:
                    java.lang.String r5 = new java.lang.String
                    r5.<init>(r4)
                    r4 = r5
                L_0x004e:
                    com.adobe.xmp.impl.XMPMetaImpl r2 = (com.adobe.xmp.impl.XMPMetaImpl) r2
                    java.util.Objects.requireNonNull(r2)
                    com.adobe.xmp.impl.ParameterAsserts.assertSchemaNS(r3)
                    com.adobe.xmp.impl.ParameterAsserts.assertPropName(r4)
                    com.adobe.xmp.impl.xpath.XMPPath r3 = com.adobe.xmp.impl.xpath.XMPPathParser.expandXPath(r3, r4)
                    com.adobe.xmp.impl.XMPNode r4 = r2.tree
                    r5 = 0
                    r6 = 0
                    com.adobe.xmp.impl.XMPNode r3 = com.adobe.xmp.impl.XMPNodeUtils.findNode(r4, r3, r5, r6)
                    if (r3 == 0) goto L_0x0071
                    java.lang.Object r2 = r2.evaluateNodeValue(r5, r3)
                    com.adobe.xmp.impl.XMPMetaImpl$2 r4 = new com.adobe.xmp.impl.XMPMetaImpl$2
                    r4.<init>(r2, r3)
                    goto L_0x0072
                L_0x0071:
                    r4 = r6
                L_0x0072:
                    if (r4 != 0) goto L_0x0075
                    return r6
                L_0x0075:
                    java.lang.Object r2 = r4.val$value
                    java.lang.String r2 = r2.toString()
                    return r2
                L_0x007c:
                    com.adobe.xmp.XMPException r2 = new com.adobe.xmp.XMPException
                    java.lang.String r3 = "The field name must be simple"
                    r2.<init>(r3, r0)
                    throw r2
                L_0x0084:
                    com.adobe.xmp.XMPException r2 = new com.adobe.xmp.XMPException
                    java.lang.String r3 = "Empty f name"
                    r2.<init>(r3, r0)
                    throw r2
                L_0x008c:
                    com.adobe.xmp.XMPException r2 = new com.adobe.xmp.XMPException
                    r3 = 101(0x65, float:1.42E-43)
                    java.lang.String r4 = "Empty field namespace URI"
                    r2.<init>(r4, r3)
                    throw r2
                */
                throw new UnsupportedOperationException("Method not decompiled: com.google.android.libraries.microvideo.xmp.MicroVideoXmpContainerItem.getStructField(com.adobe.xmp.XMPMeta, java.lang.String, java.lang.String, java.lang.String, java.lang.String):java.lang.String");
            }

            public static <T> T requiredNonNullValue(T t, String str) throws XMPException {
                if (t != null) {
                    return t;
                }
                throw new XMPException(str.length() != 0 ? "Missing value for ".concat(str) : new String("Missing value for "), 5);
            }

            public abstract int getLength();

            public abstract String getMime();

            public abstract int getPadding();

            public abstract String getSemantic();
        }
