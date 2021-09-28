package com.android.systemui.backup;

import android.app.job.JobScheduler;
import android.content.Context;
import android.os.Environment;
import com.android.systemui.controls.controller.AuxiliaryPersistenceWrapper;
import java.io.File;
import kotlin.Unit;
import kotlin.io.FilesKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: BackupHelper.kt */
/* loaded from: classes.dex */
public final class BackupHelperKt {
    /* access modifiers changed from: private */
    public static final Function0<Unit> getPPControlsFile(Context context) {
        return new Function0<Unit>(context) { // from class: com.android.systemui.backup.BackupHelperKt$getPPControlsFile$1
            final /* synthetic */ Context $context;

            /* access modifiers changed from: package-private */
            {
                this.$context = r1;
            }

            @Override // kotlin.jvm.functions.Function0
            public final void invoke() {
                File filesDir = this.$context.getFilesDir();
                File buildPath = Environment.buildPath(filesDir, new String[]{"controls_favorites.xml"});
                if (buildPath.exists()) {
                    File buildPath2 = Environment.buildPath(filesDir, new String[]{"aux_controls_favorites.xml"});
                    Intrinsics.checkNotNullExpressionValue(buildPath, "file");
                    Intrinsics.checkNotNullExpressionValue(buildPath2, "dest");
                    FilesKt.copyTo$default(buildPath, buildPath2, false, 0, 6, null);
                    JobScheduler jobScheduler = (JobScheduler) this.$context.getSystemService(JobScheduler.class);
                    if (jobScheduler != null) {
                        jobScheduler.schedule(AuxiliaryPersistenceWrapper.DeletionJobService.Companion.getJobForContext(this.$context));
                    }
                }
            }
        };
    }
}
