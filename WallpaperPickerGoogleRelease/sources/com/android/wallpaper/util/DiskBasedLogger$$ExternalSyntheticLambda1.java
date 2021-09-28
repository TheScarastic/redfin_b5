package com.android.wallpaper.util;

import android.content.Context;
import android.os.SystemClock;
import android.util.Log;
import com.android.customization.model.color.ColorCustomizationManager;
import com.android.customization.model.color.ColorOption;
import com.android.customization.model.color.ColorSectionController;
import com.android.customization.picker.WallpaperPreviewer;
import com.android.systemui.shared.system.QuickStepContract;
import com.android.wallpaper.model.WallpaperSectionController;
import com.android.wallpaper.picker.CategoryFragment;
import com.android.wallpaper.picker.DownloadablePreviewFragment;
import com.android.wallpaper.picker.ImagePreviewFragment;
import com.android.wallpaper.picker.LivePreviewFragment;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.Date;
/* loaded from: classes.dex */
public final /* synthetic */ class DiskBasedLogger$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ int $r8$classId = 0;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ DiskBasedLogger$$ExternalSyntheticLambda1(Context context) {
        this.f$0 = context;
    }

    public /* synthetic */ DiskBasedLogger$$ExternalSyntheticLambda1(ColorSectionController.AnonymousClass1 r2) {
        this.f$0 = r2;
    }

    public /* synthetic */ DiskBasedLogger$$ExternalSyntheticLambda1(ColorSectionController colorSectionController) {
        this.f$0 = colorSectionController;
    }

    public /* synthetic */ DiskBasedLogger$$ExternalSyntheticLambda1(WallpaperPreviewer wallpaperPreviewer) {
        this.f$0 = wallpaperPreviewer;
    }

    public /* synthetic */ DiskBasedLogger$$ExternalSyntheticLambda1(WallpaperSectionController wallpaperSectionController) {
        this.f$0 = wallpaperSectionController;
    }

    public /* synthetic */ DiskBasedLogger$$ExternalSyntheticLambda1(CategoryFragment categoryFragment) {
        this.f$0 = categoryFragment;
    }

    public /* synthetic */ DiskBasedLogger$$ExternalSyntheticLambda1(DownloadablePreviewFragment downloadablePreviewFragment) {
        this.f$0 = downloadablePreviewFragment;
    }

    public /* synthetic */ DiskBasedLogger$$ExternalSyntheticLambda1(ImagePreviewFragment.AnonymousClass2 r2) {
        this.f$0 = r2;
    }

    public /* synthetic */ DiskBasedLogger$$ExternalSyntheticLambda1(ImagePreviewFragment imagePreviewFragment) {
        this.f$0 = imagePreviewFragment;
    }

    public /* synthetic */ DiskBasedLogger$$ExternalSyntheticLambda1(LivePreviewFragment livePreviewFragment) {
        this.f$0 = livePreviewFragment;
    }

    @Override // java.lang.Runnable
    public final void run() {
        int i;
        Integer num = null;
        switch (this.$r8$classId) {
            case 0:
                Context context = (Context) this.f$0;
                File file = new File(context.getFilesDir(), "logs.txt");
                if (!file.exists()) {
                    Log.w("DiskBasedLogger", "Disk-based log buffer doesn't exist, so there's nothing to clean up.");
                    return;
                }
                synchronized (DiskBasedLogger.S_LOCK) {
                    try {
                        try {
                            FileInputStream openFileInput = context.openFileInput("logs.txt");
                            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(openFileInput, StandardCharsets.UTF_8));
                            Calendar instance = Calendar.getInstance();
                            instance.add(5, -7);
                            Date time = instance.getTime();
                            File file2 = new File(context.getFilesDir(), "temp_logs.txt");
                            try {
                                FileOutputStream openFileOutput = context.openFileOutput("temp_logs.txt", QuickStepContract.SYSUI_STATE_GLOBAL_ACTIONS_SHOWING);
                                DiskBasedLogger.copyLogsNewerThanDate(bufferedReader, openFileOutput, time);
                                try {
                                    openFileInput.close();
                                } catch (IOException unused) {
                                    Log.e("DiskBasedLogger", "couldn't close input stream for log file");
                                }
                                try {
                                    openFileOutput.close();
                                } catch (IOException unused2) {
                                    Log.e("DiskBasedLogger", "couldn't close output stream for temp log file");
                                }
                                if (file2.exists() && !file2.renameTo(file)) {
                                    Log.e("DiskBasedLogger", "couldn't rename temp logs file to final logs file");
                                }
                            } catch (IOException e) {
                                Log.e("DiskBasedLogger", "Unable to close output stream for disk-based log buffer", e);
                                return;
                            }
                        } catch (IOException e2) {
                            Log.e("DiskBasedLogger", "IO exception opening a buffered reader for the existing logs file", e2);
                            return;
                        }
                    } catch (Throwable th) {
                        throw th;
                    }
                }
                return;
            case 1:
                ColorSectionController colorSectionController = (ColorSectionController) this.f$0;
                ColorOption colorOption = colorSectionController.mSelectedColor;
                if (SystemClock.elapsedRealtime() - colorSectionController.mLastColorApplyingTime >= 500) {
                    colorSectionController.mLastColorApplyingTime = SystemClock.elapsedRealtime();
                    ColorCustomizationManager colorCustomizationManager = colorSectionController.mColorManager;
                    ColorSectionController.AnonymousClass2 r2 = 
                    /*  JADX ERROR: Method code generation error
                        jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x017a: CONSTRUCTOR  (r2v2 'r2' com.android.customization.model.color.ColorSectionController$2) = 
                          (r8v14 'colorSectionController' com.android.customization.model.color.ColorSectionController)
                          (r0v6 'colorOption' com.android.customization.model.color.ColorOption)
                         call: com.android.customization.model.color.ColorSectionController.2.<init>(com.android.customization.model.color.ColorSectionController, com.android.customization.model.color.ColorOption):void type: CONSTRUCTOR in method: com.android.wallpaper.util.DiskBasedLogger$$ExternalSyntheticLambda1.run():void, file: classes.dex
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
                        	at jadx.core.codegen.RegionGen.makeSwitch(RegionGen.java:281)
                        	at jadx.core.dex.regions.SwitchRegion.generate(SwitchRegion.java:79)
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
                        Caused by: jadx.core.utils.exceptions.JadxRuntimeException: Expected class to be processed at this point, class: com.android.customization.model.color.ColorSectionController, state: GENERATED_AND_UNLOADED
                        	at jadx.core.dex.nodes.ClassNode.ensureProcessed(ClassNode.java:259)
                        	at jadx.core.codegen.InsnGen.makeConstructor(InsnGen.java:675)
                        	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:393)
                        	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:278)
                        	... 29 more
                        */
                    /*
                    // Method dump skipped, instructions count: 582
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.android.wallpaper.util.DiskBasedLogger$$ExternalSyntheticLambda1.run():void");
                }
            }
