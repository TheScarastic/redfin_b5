package com.android.wallpaper.util;

import android.app.job.JobParameters;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.os.Bundle;
import android.view.ViewGroup;
import com.android.customization.model.CustomizationManager;
import com.android.customization.model.color.ColorCustomizationManager;
import com.android.customization.model.color.ColorOption;
import com.android.wallpaper.model.CustomizationSectionController;
import com.android.wallpaper.module.RotationWallpaperUpdateReceiver;
import com.android.wallpaper.picker.CustomizationPickerFragment;
import com.android.wallpaper.util.PreviewUtils;
import com.google.android.apps.wallpaper.module.ClearcutUserEventLogger;
import com.google.android.apps.wallpaper.sync.ArcSyncDataProcessorJobService;
/* loaded from: classes.dex */
public final /* synthetic */ class PreviewUtils$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ int $r8$classId = 4;
    public final /* synthetic */ Object f$0;
    public final /* synthetic */ Object f$1;
    public final /* synthetic */ Object f$2;

    public /* synthetic */ PreviewUtils$$ExternalSyntheticLambda1(Context context, String str, String str2) {
        this.f$0 = context;
        this.f$1 = str;
        this.f$2 = str2;
    }

    public /* synthetic */ PreviewUtils$$ExternalSyntheticLambda1(ColorCustomizationManager colorCustomizationManager, ColorOption colorOption, CustomizationManager.Callback callback) {
        this.f$0 = colorCustomizationManager;
        this.f$1 = colorOption;
        this.f$2 = callback;
    }

    public /* synthetic */ PreviewUtils$$ExternalSyntheticLambda1(RotationWallpaperUpdateReceiver rotationWallpaperUpdateReceiver, Context context, BroadcastReceiver.PendingResult pendingResult) {
        this.f$0 = rotationWallpaperUpdateReceiver;
        this.f$1 = context;
        this.f$2 = pendingResult;
    }

    public /* synthetic */ PreviewUtils$$ExternalSyntheticLambda1(CustomizationPickerFragment customizationPickerFragment, ViewGroup viewGroup, CustomizationSectionController customizationSectionController) {
        this.f$0 = customizationPickerFragment;
        this.f$1 = viewGroup;
        this.f$2 = customizationSectionController;
    }

    public /* synthetic */ PreviewUtils$$ExternalSyntheticLambda1(PreviewUtils previewUtils, Bundle bundle, PreviewUtils.WorkspacePreviewCallback workspacePreviewCallback) {
        this.f$0 = previewUtils;
        this.f$1 = bundle;
        this.f$2 = workspacePreviewCallback;
    }

    public /* synthetic */ PreviewUtils$$ExternalSyntheticLambda1(ClearcutUserEventLogger clearcutUserEventLogger, String str, String str2) {
        this.f$0 = clearcutUserEventLogger;
        this.f$1 = str;
        this.f$2 = str2;
    }

    public /* synthetic */ PreviewUtils$$ExternalSyntheticLambda1(ArcSyncDataProcessorJobService arcSyncDataProcessorJobService, Context context, JobParameters jobParameters) {
        this.f$0 = arcSyncDataProcessorJobService;
        this.f$1 = context;
        this.f$2 = jobParameters;
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(10:91|(10:196|92|(3:94|95|(2:97|210)(2:98|(2:100|209)(1:211)))(1:207)|102|(1:106)|107|187|108|111|(2:113|216)(22:169|114|115|194|116|117|173|118|198|119|189|121|181|123|124|177|126|127|203|129|137|(2:142|217)(2:143|(2:145|218)(4:146|(5:148|183|149|150|(2:154|219))(2:(1:156)(1:157)|(2:159|220))|160|221))))|208|102|(0)|107|187|108|111|(0)(0)) */
    /* JADX WARNING: Code restructure failed: missing block: B:109:0x0329, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:110:0x032a, code lost:
        com.android.wallpaper.util.DiskBasedLogger.e("ArcSyncDataProcessorJob", "Unable to close output stream: " + r0, r7.getApplicationContext());
        r7.doneProcessingCurrentSyncData(r12, r13);
     */
    /* JADX WARNING: Removed duplicated region for block: B:106:0x031e  */
    /* JADX WARNING: Removed duplicated region for block: B:113:0x0349  */
    /* JADX WARNING: Removed duplicated region for block: B:142:0x0424  */
    /* JADX WARNING: Removed duplicated region for block: B:143:0x0429  */
    /* JADX WARNING: Removed duplicated region for block: B:169:0x034e A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:63:0x01fe  */
    /* JADX WARNING: Removed duplicated region for block: B:64:0x0201  */
    @Override // java.lang.Runnable
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void run() {
        /*
        // Method dump skipped, instructions count: 1346
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wallpaper.util.PreviewUtils$$ExternalSyntheticLambda1.run():void");
    }
}
