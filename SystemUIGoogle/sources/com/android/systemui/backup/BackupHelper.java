package com.android.systemui.backup;

import android.app.backup.BackupAgentHelper;
import android.app.backup.BackupDataInputStream;
import android.app.backup.BackupDataOutput;
import android.app.backup.FileBackupHelper;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.os.UserHandle;
import android.util.Log;
import com.android.systemui.people.widget.PeopleBackupHelper;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.collections.MapsKt__MapsJVMKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: BackupHelper.kt */
/* loaded from: classes.dex */
public final class BackupHelper extends BackupAgentHelper {
    public static final Companion Companion = new Companion(null);
    private static final Object controlsDataLock = new Object();

    /* compiled from: BackupHelper.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final Object getControlsDataLock() {
            return BackupHelper.controlsDataLock;
        }
    }

    public void onCreate(UserHandle userHandle, int i) {
        Intrinsics.checkNotNullParameter(userHandle, "userHandle");
        super.onCreate();
        addHelper("systemui.files_no_overwrite", new NoOverwriteFileBackupHelper(Companion.getControlsDataLock(), this, MapsKt__MapsJVMKt.mapOf(TuplesKt.to("controls_favorites.xml", BackupHelperKt.getPPControlsFile(this)))));
        if (userHandle.isSystem()) {
            List<String> filesToBackup = PeopleBackupHelper.getFilesToBackup();
            Intrinsics.checkNotNullExpressionValue(filesToBackup, "keys");
            Object[] array = filesToBackup.toArray(new String[0]);
            Objects.requireNonNull(array, "null cannot be cast to non-null type kotlin.Array<T>");
            addHelper("systemui.people.shared_preferences", new PeopleBackupHelper(this, userHandle, (String[]) array));
        }
    }

    @Override // android.app.backup.BackupAgent
    public void onRestoreFinished() {
        super.onRestoreFinished();
        Intent intent = new Intent("com.android.systemui.backup.RESTORE_FINISHED");
        intent.setPackage(getPackageName());
        intent.putExtra("android.intent.extra.USER_ID", getUserId());
        intent.setFlags(1073741824);
        sendBroadcastAsUser(intent, UserHandle.SYSTEM, "com.android.systemui.permission.SELF");
    }

    /* compiled from: BackupHelper.kt */
    /* loaded from: classes.dex */
    private static final class NoOverwriteFileBackupHelper extends FileBackupHelper {
        private final Context context;
        private final Map<String, Function0<Unit>> fileNamesAndPostProcess;
        private final Object lock;

        public final Map<String, Function0<Unit>> getFileNamesAndPostProcess() {
            return this.fileNamesAndPostProcess;
        }

        /* JADX DEBUG: Multi-variable search result rejected for r7v0, resolved type: java.util.Map<java.lang.String, ? extends kotlin.jvm.functions.Function0<kotlin.Unit>> */
        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARNING: Illegal instructions before constructor call */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public NoOverwriteFileBackupHelper(java.lang.Object r5, android.content.Context r6, java.util.Map<java.lang.String, ? extends kotlin.jvm.functions.Function0<kotlin.Unit>> r7) {
            /*
                r4 = this;
                java.lang.String r0 = "lock"
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r5, r0)
                java.lang.String r0 = "context"
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r6, r0)
                java.lang.String r0 = "fileNamesAndPostProcess"
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r7, r0)
                java.util.Set r0 = r7.keySet()
                r1 = 0
                java.lang.String[] r2 = new java.lang.String[r1]
                java.lang.Object[] r0 = r0.toArray(r2)
                java.lang.String r2 = "null cannot be cast to non-null type kotlin.Array<T>"
                java.util.Objects.requireNonNull(r0, r2)
                java.lang.String[] r0 = (java.lang.String[]) r0
                int r2 = r0.length
                java.lang.String[] r2 = new java.lang.String[r2]
                int r3 = r0.length
                java.lang.System.arraycopy(r0, r1, r2, r1, r3)
                r4.<init>(r6, r2)
                r4.lock = r5
                r4.context = r6
                r4.fileNamesAndPostProcess = r7
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.backup.BackupHelper.NoOverwriteFileBackupHelper.<init>(java.lang.Object, android.content.Context, java.util.Map):void");
        }

        @Override // android.app.backup.FileBackupHelper, android.app.backup.BackupHelper
        public void restoreEntity(BackupDataInputStream backupDataInputStream) {
            Intrinsics.checkNotNullParameter(backupDataInputStream, "data");
            if (Environment.buildPath(this.context.getFilesDir(), new String[]{backupDataInputStream.getKey()}).exists()) {
                Log.w("BackupHelper", "File " + ((Object) backupDataInputStream.getKey()) + " already exists. Skipping restore.");
                return;
            }
            synchronized (this.lock) {
                super.restoreEntity(backupDataInputStream);
                Function0<Unit> function0 = getFileNamesAndPostProcess().get(backupDataInputStream.getKey());
                if (function0 != null) {
                    function0.invoke();
                    Unit unit = Unit.INSTANCE;
                }
            }
        }

        @Override // android.app.backup.FileBackupHelper, android.app.backup.BackupHelper
        public void performBackup(ParcelFileDescriptor parcelFileDescriptor, BackupDataOutput backupDataOutput, ParcelFileDescriptor parcelFileDescriptor2) {
            synchronized (this.lock) {
                super.performBackup(parcelFileDescriptor, backupDataOutput, parcelFileDescriptor2);
                Unit unit = Unit.INSTANCE;
            }
        }
    }
}
