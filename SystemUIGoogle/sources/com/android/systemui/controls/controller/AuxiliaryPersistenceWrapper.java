package com.android.systemui.controls.controller;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.backup.BackupHelper;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import kotlin.Pair;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: AuxiliaryPersistenceWrapper.kt */
/* loaded from: classes.dex */
public final class AuxiliaryPersistenceWrapper {
    public static final Companion Companion = new Companion(null);
    private List<StructureInfo> favorites;
    private ControlsFavoritePersistenceWrapper persistenceWrapper;

    @VisibleForTesting
    public AuxiliaryPersistenceWrapper(ControlsFavoritePersistenceWrapper controlsFavoritePersistenceWrapper) {
        Intrinsics.checkNotNullParameter(controlsFavoritePersistenceWrapper, "wrapper");
        this.persistenceWrapper = controlsFavoritePersistenceWrapper;
        this.favorites = CollectionsKt__CollectionsKt.emptyList();
        initialize();
    }

    /* JADX INFO: 'this' call moved to the top of the method (can break code semantics) */
    public AuxiliaryPersistenceWrapper(File file, Executor executor) {
        this(new ControlsFavoritePersistenceWrapper(file, executor, null, 4, null));
        Intrinsics.checkNotNullParameter(file, "file");
        Intrinsics.checkNotNullParameter(executor, "executor");
    }

    /* compiled from: AuxiliaryPersistenceWrapper.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    public final List<StructureInfo> getFavorites() {
        return this.favorites;
    }

    public final void changeFile(File file) {
        Intrinsics.checkNotNullParameter(file, "file");
        this.persistenceWrapper.changeFileAndBackupManager(file, null);
        initialize();
    }

    public final void initialize() {
        List<StructureInfo> list;
        if (this.persistenceWrapper.getFileExists()) {
            list = this.persistenceWrapper.readFavorites();
        } else {
            list = CollectionsKt__CollectionsKt.emptyList();
        }
        this.favorites = list;
    }

    public final List<StructureInfo> getCachedFavoritesAndRemoveFor(ComponentName componentName) {
        Intrinsics.checkNotNullParameter(componentName, "componentName");
        if (!this.persistenceWrapper.getFileExists()) {
            return CollectionsKt__CollectionsKt.emptyList();
        }
        List<StructureInfo> list = this.favorites;
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        for (Object obj : list) {
            if (Intrinsics.areEqual(((StructureInfo) obj).getComponentName(), componentName)) {
                arrayList.add(obj);
            } else {
                arrayList2.add(obj);
            }
        }
        Pair pair = new Pair(arrayList, arrayList2);
        List<StructureInfo> list2 = (List) pair.component1();
        List<StructureInfo> list3 = (List) pair.component2();
        this.favorites = list3;
        if (!getFavorites().isEmpty()) {
            this.persistenceWrapper.storeFavorites(list3);
        } else {
            this.persistenceWrapper.deleteFile();
        }
        return list2;
    }

    /* compiled from: AuxiliaryPersistenceWrapper.kt */
    /* loaded from: classes.dex */
    public static final class DeletionJobService extends JobService {
        public static final Companion Companion = new Companion(null);
        private static final int DELETE_FILE_JOB_ID = 1000;
        private static final long WEEK_IN_MILLIS = TimeUnit.DAYS.toMillis(7);

        @Override // android.app.job.JobService
        public boolean onStopJob(JobParameters jobParameters) {
            return true;
        }

        /* compiled from: AuxiliaryPersistenceWrapper.kt */
        /* loaded from: classes.dex */
        public static final class Companion {
            public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
                this();
            }

            @VisibleForTesting
            public static /* synthetic */ void getDELETE_FILE_JOB_ID$frameworks__base__packages__SystemUI__android_common__SystemUI_core$annotations() {
            }

            private Companion() {
            }

            public final int getDELETE_FILE_JOB_ID$frameworks__base__packages__SystemUI__android_common__SystemUI_core() {
                return DeletionJobService.DELETE_FILE_JOB_ID;
            }

            public final JobInfo getJobForContext(Context context) {
                Intrinsics.checkNotNullParameter(context, "context");
                JobInfo build = new JobInfo.Builder(getDELETE_FILE_JOB_ID$frameworks__base__packages__SystemUI__android_common__SystemUI_core() + context.getUserId(), new ComponentName(context, DeletionJobService.class)).setMinimumLatency(DeletionJobService.WEEK_IN_MILLIS).setPersisted(true).build();
                Intrinsics.checkNotNullExpressionValue(build, "Builder(jobId, componentName)\n                    .setMinimumLatency(WEEK_IN_MILLIS)\n                    .setPersisted(true)\n                    .build()");
                return build;
            }
        }

        @VisibleForTesting
        public final void attachContext(Context context) {
            Intrinsics.checkNotNullParameter(context, "context");
            attachBaseContext(context);
        }

        @Override // android.app.job.JobService
        public boolean onStartJob(JobParameters jobParameters) {
            Intrinsics.checkNotNullParameter(jobParameters, "params");
            synchronized (BackupHelper.Companion.getControlsDataLock()) {
                getBaseContext().deleteFile("aux_controls_favorites.xml");
            }
            return false;
        }
    }
}
