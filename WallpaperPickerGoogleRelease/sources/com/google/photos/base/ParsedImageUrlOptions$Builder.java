package com.google.photos.base;

import java.util.EnumMap;
import java.util.Map;
/* loaded from: classes.dex */
public class ParsedImageUrlOptions$Builder {
    public Map<ImageUrlOptionsEnum, ImageUrlOptionsParsing$TokenInfo> existingOptionTokenInfoMap;
    public String existingOptionString = "";
    public Map<ImageUrlOptionsEnum, ParsedImageUrlOptions$OptionState> newOptionMap = new EnumMap(ImageUrlOptionsEnum.class);

    public ParsedImageUrlOptions$Builder(ParsedImageUrlOptions$1 parsedImageUrlOptions$1) {
        EnumMap enumMap = new EnumMap(ImageUrlOptionsEnum.class);
        this.existingOptionTokenInfoMap = enumMap;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0047, code lost:
        if (r1.get(r6).signed != false) goto L_0x005a;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final com.google.photos.base.ParsedImageUrlOptions$Builder setIsSigned(com.google.photos.base.ImageUrlOptionsEnum r6, boolean r7) {
        /*
        // Method dump skipped, instructions count: 290
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.photos.base.ParsedImageUrlOptions$Builder.setIsSigned(com.google.photos.base.ImageUrlOptionsEnum, boolean):com.google.photos.base.ParsedImageUrlOptions$Builder");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0036, code lost:
        if (r9.isInfinite() == false) goto L_0x0063;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0044, code lost:
        if (((java.lang.Long) r8).longValue() >= 0) goto L_0x0063;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x004e, code lost:
        if (((java.lang.Integer) r8).intValue() >= 0) goto L_0x0063;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final com.google.photos.base.ParsedImageUrlOptions$Builder setOptionWithReadableError(com.google.photos.base.ImageUrlOptionsEnum r7, java.lang.Object r8, java.lang.String r9) {
        /*
            r6 = this;
            if (r8 == 0) goto L_0x007c
            com.google.photos.base.ImageUrlOptionType r9 = r7.getOptionType()
            int r9 = r9.ordinal()
            r0 = 1
            r1 = 0
            switch(r9) {
                case 0: goto L_0x0063;
                case 1: goto L_0x005c;
                case 2: goto L_0x0053;
                case 3: goto L_0x0047;
                case 4: goto L_0x0039;
                case 5: goto L_0x0029;
                case 6: goto L_0x0063;
                default: goto L_0x000f;
            }
        L_0x000f:
            java.lang.RuntimeException r6 = new java.lang.RuntimeException
            com.google.photos.base.ImageUrlOptionType r7 = r7.getOptionType()
            java.lang.String r7 = java.lang.String.valueOf(r7)
            int r8 = r7.length()
            int r8 = r8 + 24
            java.lang.String r9 = "Unexpected option type: "
            java.lang.String r7 = com.bumptech.glide.Registry$NoModelLoaderAvailableException$$ExternalSyntheticOutline0.m(r8, r9, r7)
            r6.<init>(r7)
            throw r6
        L_0x0029:
            r9 = r8
            java.lang.Float r9 = (java.lang.Float) r9
            boolean r2 = r9.isNaN()
            if (r2 != 0) goto L_0x0051
            boolean r9 = r9.isInfinite()
            if (r9 != 0) goto L_0x0051
            goto L_0x0063
        L_0x0039:
            r9 = r8
            java.lang.Long r9 = (java.lang.Long) r9
            long r2 = r9.longValue()
            r4 = 0
            int r9 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r9 < 0) goto L_0x0051
            goto L_0x0063
        L_0x0047:
            r9 = r8
            java.lang.Integer r9 = (java.lang.Integer) r9
            int r9 = r9.intValue()
            if (r9 < 0) goto L_0x0051
            goto L_0x0063
        L_0x0051:
            r0 = r1
            goto L_0x0063
        L_0x0053:
            r9 = r8
            java.lang.String r9 = (java.lang.String) r9
            boolean r9 = r9.isEmpty()
            r0 = r0 ^ r9
            goto L_0x0063
        L_0x005c:
            r9 = r8
            java.lang.Boolean r9 = (java.lang.Boolean) r9
            boolean r0 = r9.booleanValue()
        L_0x0063:
            if (r0 != 0) goto L_0x0071
            com.google.photos.base.ParsedImageUrlOptions$OptionState r8 = new com.google.photos.base.ParsedImageUrlOptions$OptionState
            r9 = 0
            r8.<init>(r9, r1)
            java.util.Map<com.google.photos.base.ImageUrlOptionsEnum, com.google.photos.base.ParsedImageUrlOptions$OptionState> r9 = r6.newOptionMap
            r9.put(r7, r8)
            goto L_0x007b
        L_0x0071:
            com.google.photos.base.ParsedImageUrlOptions$OptionState r9 = new com.google.photos.base.ParsedImageUrlOptions$OptionState
            r9.<init>(r8, r1)
            java.util.Map<com.google.photos.base.ImageUrlOptionsEnum, com.google.photos.base.ParsedImageUrlOptions$OptionState> r8 = r6.newOptionMap
            r8.put(r7, r9)
        L_0x007b:
            return r6
        L_0x007c:
            java.lang.NullPointerException r6 = new java.lang.NullPointerException
            int r7 = r9.length()
            int r7 = r7 + 51
            java.lang.String r8 = "Cannot set an option to null. Did you mean clear"
            java.lang.String r0 = "()?"
            java.lang.String r7 = androidx.viewpager2.widget.FakeDrag$$ExternalSyntheticOutline0.m(r7, r8, r9, r0)
            r6.<init>(r7)
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.photos.base.ParsedImageUrlOptions$Builder.setOptionWithReadableError(com.google.photos.base.ImageUrlOptionsEnum, java.lang.Object, java.lang.String):com.google.photos.base.ParsedImageUrlOptions$Builder");
    }
}
