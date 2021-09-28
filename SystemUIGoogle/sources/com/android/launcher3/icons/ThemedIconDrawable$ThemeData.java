package com.android.launcher3.icons;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.AdaptiveIconDrawable;
import android.graphics.drawable.Drawable;
import android.os.Process;
import android.os.UserHandle;
import com.android.launcher3.icons.BitmapInfo;
/* loaded from: classes.dex */
public class ThemedIconDrawable$ThemeData {
    final int mResID;
    final Resources mResources;

    public ThemedIconDrawable$ThemeData(Resources resources, int i) {
        this.mResources = resources;
        this.mResID = i;
    }

    public Drawable wrapDrawable(Drawable drawable, int i) {
        if (!(drawable instanceof AdaptiveIconDrawable)) {
            return drawable;
        }
        AdaptiveIconDrawable adaptiveIconDrawable = (AdaptiveIconDrawable) drawable;
        String resourceTypeName = this.mResources.getResourceTypeName(this.mResID);
        if (i == 1 && "array".equals(resourceTypeName)) {
            TypedArray obtainTypedArray = this.mResources.obtainTypedArray(this.mResID);
            int resourceId = obtainTypedArray.getResourceId(IconProvider.getDay(), 0);
            obtainTypedArray.recycle();
            return resourceId == 0 ? drawable : new BitmapInfo.Extender(adaptiveIconDrawable, new ThemedIconDrawable$ThemeData(this.mResources, resourceId)) { // from class: com.android.launcher3.icons.ThemedIconDrawable$ThemedAdaptiveIcon
                protected final ThemedIconDrawable$ThemeData mThemeData;

                {
                    this.mThemeData = r3;
                }

                @Override // com.android.launcher3.icons.BitmapInfo.Extender
                public BitmapInfo getExtendedInfo(Bitmap bitmap, int i2, BaseIconFactory baseIconFactory, float f, UserHandle userHandle) {
                    Bitmap bitmap2;
                    if (Process.myUserHandle().equals(userHandle)) {
                        bitmap2 = null;
                    } else {
                        bitmap2 = baseIconFactory.getUserBadgeBitmap(userHandle);
                    }
                    return 
                    /*  JADX ERROR: Method code generation error
                        jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x001c: RETURN  
                          (wrap: com.android.launcher3.icons.ThemedIconDrawable$ThemedBitmapInfo : 0x0019: CONSTRUCTOR  (r9v2 com.android.launcher3.icons.ThemedIconDrawable$ThemedBitmapInfo A[REMOVE]) = 
                          (r7v0 'bitmap' android.graphics.Bitmap)
                          (r8v0 'i2' int)
                          (wrap: com.android.launcher3.icons.ThemedIconDrawable$ThemeData : 0x0013: IGET  (r3v0 com.android.launcher3.icons.ThemedIconDrawable$ThemeData A[REMOVE]) = (r6v0 'this' com.android.launcher3.icons.ThemedIconDrawable$ThemedAdaptiveIcon A[IMMUTABLE_TYPE, THIS]) com.android.launcher3.icons.ThemedIconDrawable$ThemedAdaptiveIcon.mThemeData com.android.launcher3.icons.ThemedIconDrawable$ThemeData)
                          (r10v0 'f' float)
                          (r9v1 'bitmap2' android.graphics.Bitmap)
                         call: com.android.launcher3.icons.ThemedIconDrawable$ThemedBitmapInfo.<init>(android.graphics.Bitmap, int, com.android.launcher3.icons.ThemedIconDrawable$ThemeData, float, android.graphics.Bitmap):void type: CONSTRUCTOR)
                         in method: com.android.launcher3.icons.ThemedIconDrawable$ThemedAdaptiveIcon.getExtendedInfo(android.graphics.Bitmap, int, com.android.launcher3.icons.BaseIconFactory, float, android.os.UserHandle):com.android.launcher3.icons.BitmapInfo, file: classes.dex
                        	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:285)
                        	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:248)
                        	at jadx.core.codegen.RegionGen.makeSimpleBlock(RegionGen.java:105)
                        	at jadx.core.dex.nodes.IBlock.generate(IBlock.java:15)
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
                        Caused by: jadx.core.utils.exceptions.JadxRuntimeException: Expected class to be processed at this point, class: com.android.launcher3.icons.ThemedIconDrawable$ThemedBitmapInfo, state: NOT_LOADED
                        	at jadx.core.dex.nodes.ClassNode.ensureProcessed(ClassNode.java:259)
                        	at jadx.core.codegen.InsnGen.makeConstructor(InsnGen.java:675)
                        	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:393)
                        	at jadx.core.codegen.InsnGen.addWrappedArg(InsnGen.java:141)
                        	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:120)
                        	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:104)
                        	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:343)
                        	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:278)
                        	... 15 more
                        */
                    /*
                        this = this;
                        android.os.UserHandle r0 = android.os.Process.myUserHandle()
                        boolean r0 = r0.equals(r11)
                        if (r0 == 0) goto L_0x000c
                        r9 = 0
                        goto L_0x0010
                    L_0x000c:
                        android.graphics.Bitmap r9 = r9.getUserBadgeBitmap(r11)
                    L_0x0010:
                        r5 = r9
                        com.android.launcher3.icons.ThemedIconDrawable$ThemedBitmapInfo r9 = new com.android.launcher3.icons.ThemedIconDrawable$ThemedBitmapInfo
                        com.android.launcher3.icons.ThemedIconDrawable$ThemeData r3 = r6.mThemeData
                        r0 = r9
                        r1 = r7
                        r2 = r8
                        r4 = r10
                        r0.<init>(r1, r2, r3, r4, r5)
                        return r9
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.android.launcher3.icons.ThemedIconDrawable$ThemedAdaptiveIcon.getExtendedInfo(android.graphics.Bitmap, int, com.android.launcher3.icons.BaseIconFactory, float, android.os.UserHandle):com.android.launcher3.icons.BitmapInfo");
                }

                @Override // com.android.launcher3.icons.BitmapInfo.Extender
                public void drawForPersistence(Canvas canvas) {
                    draw(canvas);
                }
            };
        } else if (i != 2 || !"array".equals(resourceTypeName)) {
            return "drawable".equals(resourceTypeName) ? new BitmapInfo.Extender(adaptiveIconDrawable, this) { // from class: com.android.launcher3.icons.ThemedIconDrawable$ThemedAdaptiveIcon
                protected final ThemedIconDrawable$ThemeData mThemeData;

                {
                    this.mThemeData = r3;
                }

                /*  JADX ERROR: Method code generation error
                    jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x001c: RETURN  
                      (wrap: com.android.launcher3.icons.ThemedIconDrawable$ThemedBitmapInfo : 0x0019: CONSTRUCTOR  (r9v2 com.android.launcher3.icons.ThemedIconDrawable$ThemedBitmapInfo A[REMOVE]) = 
                      (r7v0 'bitmap' android.graphics.Bitmap)
                      (r8v0 'i2' int)
                      (wrap: com.android.launcher3.icons.ThemedIconDrawable$ThemeData : 0x0013: IGET  (r3v0 com.android.launcher3.icons.ThemedIconDrawable$ThemeData A[REMOVE]) = (r6v0 'this' com.android.launcher3.icons.ThemedIconDrawable$ThemedAdaptiveIcon A[IMMUTABLE_TYPE, THIS]) com.android.launcher3.icons.ThemedIconDrawable$ThemedAdaptiveIcon.mThemeData com.android.launcher3.icons.ThemedIconDrawable$ThemeData)
                      (r10v0 'f' float)
                      (r9v1 'bitmap2' android.graphics.Bitmap)
                     call: com.android.launcher3.icons.ThemedIconDrawable$ThemedBitmapInfo.<init>(android.graphics.Bitmap, int, com.android.launcher3.icons.ThemedIconDrawable$ThemeData, float, android.graphics.Bitmap):void type: CONSTRUCTOR)
                     in method: com.android.launcher3.icons.ThemedIconDrawable$ThemedAdaptiveIcon.getExtendedInfo(android.graphics.Bitmap, int, com.android.launcher3.icons.BaseIconFactory, float, android.os.UserHandle):com.android.launcher3.icons.BitmapInfo, file: classes.dex
                    	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:285)
                    	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:248)
                    	at jadx.core.codegen.RegionGen.makeSimpleBlock(RegionGen.java:105)
                    	at jadx.core.dex.nodes.IBlock.generate(IBlock.java:15)
                    	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:65)
                    	at jadx.core.dex.regions.Region.generate(Region.java:35)
                    	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:65)
                    	at jadx.core.codegen.MethodGen.addRegionInsns(MethodGen.java:261)
                    	at jadx.core.codegen.MethodGen.addInstructions(MethodGen.java:254)
                    	at jadx.core.codegen.ClassGen.addMethodCode(ClassGen.java:349)
                    	at jadx.core.codegen.ClassGen.addMethod(ClassGen.java:302)
                    Caused by: jadx.core.utils.exceptions.JadxRuntimeException: Expected class to be processed at this point, class: com.android.launcher3.icons.ThemedIconDrawable$ThemedBitmapInfo, state: NOT_LOADED
                    	at jadx.core.dex.nodes.ClassNode.ensureProcessed(ClassNode.java:259)
                    	at jadx.core.codegen.InsnGen.makeConstructor(InsnGen.java:675)
                    	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:393)
                    	at jadx.core.codegen.InsnGen.addWrappedArg(InsnGen.java:141)
                    	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:120)
                    	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:104)
                    	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:343)
                    	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:278)
                    	... 10 more
                    */
                @Override // com.android.launcher3.icons.BitmapInfo.Extender
                public com.android.launcher3.icons.BitmapInfo getExtendedInfo(android.graphics.Bitmap r7, int r8, com.android.launcher3.icons.BaseIconFactory r9, float r10, android.os.UserHandle r11) {
                    /*
                        r6 = this;
                        android.os.UserHandle r0 = android.os.Process.myUserHandle()
                        boolean r0 = r0.equals(r11)
                        if (r0 == 0) goto L_0x000c
                        r9 = 0
                        goto L_0x0010
                    L_0x000c:
                        android.graphics.Bitmap r9 = r9.getUserBadgeBitmap(r11)
                    L_0x0010:
                        r5 = r9
                        com.android.launcher3.icons.ThemedIconDrawable$ThemedBitmapInfo r9 = new com.android.launcher3.icons.ThemedIconDrawable$ThemedBitmapInfo
                        com.android.launcher3.icons.ThemedIconDrawable$ThemeData r3 = r6.mThemeData
                        r0 = r9
                        r1 = r7
                        r2 = r8
                        r4 = r10
                        r0.<init>(r1, r2, r3, r4, r5)
                        return r9
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.android.launcher3.icons.ThemedIconDrawable$ThemedAdaptiveIcon.getExtendedInfo(android.graphics.Bitmap, int, com.android.launcher3.icons.BaseIconFactory, float, android.os.UserHandle):com.android.launcher3.icons.BitmapInfo");
                }

                @Override // com.android.launcher3.icons.BitmapInfo.Extender
                public void drawForPersistence(Canvas canvas) {
                    draw(canvas);
                }
            } : drawable;
        } else {
            ((ClockDrawableWrapper) drawable).mThemeData = this;
            return drawable;
        }
    }
}
