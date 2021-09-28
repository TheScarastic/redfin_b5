package com.android.systemui.controls.controller;

import android.app.backup.BackupManager;
import android.content.ComponentName;
import android.util.AtomicFile;
import android.util.Log;
import android.util.Xml;
import com.android.systemui.backup.BackupHelper;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import libcore.io.IoUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;
/* compiled from: ControlsFavoritePersistenceWrapper.kt */
/* loaded from: classes.dex */
public final class ControlsFavoritePersistenceWrapper {
    public static final Companion Companion = new Companion(null);
    private BackupManager backupManager;
    private final Executor executor;
    private File file;

    public ControlsFavoritePersistenceWrapper(File file, Executor executor, BackupManager backupManager) {
        Intrinsics.checkNotNullParameter(file, "file");
        Intrinsics.checkNotNullParameter(executor, "executor");
        this.file = file;
        this.executor = executor;
        this.backupManager = backupManager;
    }

    public /* synthetic */ ControlsFavoritePersistenceWrapper(File file, Executor executor, BackupManager backupManager, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this(file, executor, (i & 4) != 0 ? null : backupManager);
    }

    /* compiled from: ControlsFavoritePersistenceWrapper.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    public final void changeFileAndBackupManager(File file, BackupManager backupManager) {
        Intrinsics.checkNotNullParameter(file, "fileName");
        this.file = file;
        this.backupManager = backupManager;
    }

    public final boolean getFileExists() {
        return this.file.exists();
    }

    public final void deleteFile() {
        this.file.delete();
    }

    public final void storeFavorites(List<StructureInfo> list) {
        Intrinsics.checkNotNullParameter(list, "structures");
        if (!list.isEmpty() || this.file.exists()) {
            this.executor.execute(new Runnable(this, list) { // from class: com.android.systemui.controls.controller.ControlsFavoritePersistenceWrapper$storeFavorites$1
                final /* synthetic */ List<StructureInfo> $structures;
                final /* synthetic */ ControlsFavoritePersistenceWrapper this$0;

                /* access modifiers changed from: package-private */
                {
                    this.this$0 = r1;
                    this.$structures = r2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    boolean z;
                    BackupManager backupManager;
                    Log.d("ControlsFavoritePersistenceWrapper", Intrinsics.stringPlus("Saving data to file: ", this.this$0.file));
                    AtomicFile atomicFile = new AtomicFile(this.this$0.file);
                    Object controlsDataLock = BackupHelper.Companion.getControlsDataLock();
                    List<StructureInfo> list2 = this.$structures;
                    try {
                        synchronized (controlsDataLock) {
                            try {
                                FileOutputStream startWrite = atomicFile.startWrite();
                                z = true;
                                try {
                                    XmlSerializer newSerializer = Xml.newSerializer();
                                    newSerializer.setOutput(startWrite, "utf-8");
                                    newSerializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
                                    newSerializer.startDocument(null, Boolean.TRUE);
                                    newSerializer.startTag(null, "version");
                                    newSerializer.text("1");
                                    newSerializer.endTag(null, "version");
                                    newSerializer.startTag(null, "structures");
                                    for (StructureInfo structureInfo : list2) {
                                        newSerializer.startTag(null, "structure");
                                        newSerializer.attribute(null, "component", structureInfo.getComponentName().flattenToString());
                                        newSerializer.attribute(null, "structure", structureInfo.getStructure().toString());
                                        newSerializer.startTag(null, "controls");
                                        for (ControlInfo controlInfo : structureInfo.getControls()) {
                                            newSerializer.startTag(null, "control");
                                            newSerializer.attribute(null, "id", controlInfo.getControlId());
                                            newSerializer.attribute(null, "title", controlInfo.getControlTitle().toString());
                                            newSerializer.attribute(null, "subtitle", controlInfo.getControlSubtitle().toString());
                                            newSerializer.attribute(null, "type", String.valueOf(controlInfo.getDeviceType()));
                                            newSerializer.endTag(null, "control");
                                        }
                                        newSerializer.endTag(null, "controls");
                                        newSerializer.endTag(null, "structure");
                                    }
                                    newSerializer.endTag(null, "structures");
                                    newSerializer.endDocument();
                                    atomicFile.finishWrite(startWrite);
                                } catch (Throwable unused) {
                                    Log.e("ControlsFavoritePersistenceWrapper", "Failed to write file, reverting to previous version");
                                    atomicFile.failWrite(startWrite);
                                    z = false;
                                }
                                IoUtils.closeQuietly(startWrite);
                            } catch (IOException e) {
                                Log.e("ControlsFavoritePersistenceWrapper", "Failed to start write file", e);
                                return;
                            }
                        }
                        if (z && (backupManager = this.this$0.backupManager) != null) {
                            backupManager.dataChanged();
                        }
                    } catch (Throwable th) {
                        throw th;
                    }
                }
            });
        }
    }

    /* JADX DEBUG: Failed to insert an additional move for type inference into block B:27:0x0014 */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r4v0, types: [com.android.systemui.controls.controller.ControlsFavoritePersistenceWrapper] */
    /* JADX WARN: Type inference failed for: r0v1, types: [boolean] */
    /* JADX WARN: Type inference failed for: r0v3, types: [java.lang.AutoCloseable] */
    /* JADX WARN: Type inference failed for: r0v4, types: [java.io.BufferedInputStream, java.lang.AutoCloseable, java.io.InputStream] */
    /* JADX WARN: Type inference failed for: r2v6, types: [java.lang.Object, org.xmlpull.v1.XmlPullParser] */
    /* JADX WARNING: Unknown variable types count: 2 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final java.util.List<com.android.systemui.controls.controller.StructureInfo> readFavorites() {
        /*
            r4 = this;
            java.io.File r0 = r4.file
            boolean r0 = r0.exists()
            if (r0 != 0) goto L_0x0014
            java.lang.String r4 = "ControlsFavoritePersistenceWrapper"
            java.lang.String r0 = "No favorites, returning empty list"
            android.util.Log.d(r4, r0)
            java.util.List r4 = kotlin.collections.CollectionsKt.emptyList()
            return r4
        L_0x0014:
            java.io.BufferedInputStream r0 = new java.io.BufferedInputStream     // Catch: FileNotFoundException -> 0x0071
            java.io.FileInputStream r1 = new java.io.FileInputStream     // Catch: FileNotFoundException -> 0x0071
            java.io.File r2 = r4.file     // Catch: FileNotFoundException -> 0x0071
            r1.<init>(r2)     // Catch: FileNotFoundException -> 0x0071
            r0.<init>(r1)     // Catch: FileNotFoundException -> 0x0071
            java.lang.String r1 = "ControlsFavoritePersistenceWrapper"
            java.lang.String r2 = "Reading data from file: "
            java.io.File r3 = r4.file     // Catch: XmlPullParserException -> 0x005e, IOException -> 0x004f, all -> 0x004d
            java.lang.String r2 = kotlin.jvm.internal.Intrinsics.stringPlus(r2, r3)     // Catch: XmlPullParserException -> 0x005e, IOException -> 0x004f, all -> 0x004d
            android.util.Log.d(r1, r2)     // Catch: XmlPullParserException -> 0x005e, IOException -> 0x004f, all -> 0x004d
            com.android.systemui.backup.BackupHelper$Companion r1 = com.android.systemui.backup.BackupHelper.Companion     // Catch: XmlPullParserException -> 0x005e, IOException -> 0x004f, all -> 0x004d
            java.lang.Object r1 = r1.getControlsDataLock()     // Catch: XmlPullParserException -> 0x005e, IOException -> 0x004f, all -> 0x004d
            monitor-enter(r1)     // Catch: XmlPullParserException -> 0x005e, IOException -> 0x004f, all -> 0x004d
            org.xmlpull.v1.XmlPullParser r2 = android.util.Xml.newPullParser()     // Catch: all -> 0x004a
            r3 = 0
            r2.setInput(r0, r3)     // Catch: all -> 0x004a
            java.lang.String r3 = "parser"
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r2, r3)     // Catch: all -> 0x004a
            java.util.List r2 = r4.parseXml(r2)     // Catch: all -> 0x004a
            monitor-exit(r1)     // Catch: XmlPullParserException -> 0x005e, IOException -> 0x004f, all -> 0x004d
            libcore.io.IoUtils.closeQuietly(r0)
            return r2
        L_0x004a:
            r2 = move-exception
            monitor-exit(r1)     // Catch: XmlPullParserException -> 0x005e, IOException -> 0x004f, all -> 0x004d
            throw r2     // Catch: XmlPullParserException -> 0x005e, IOException -> 0x004f, all -> 0x004d
        L_0x004d:
            r4 = move-exception
            goto L_0x006d
        L_0x004f:
            r1 = move-exception
            java.lang.IllegalStateException r2 = new java.lang.IllegalStateException     // Catch: all -> 0x004d
            java.lang.String r3 = "Failed parsing favorites file: "
            java.io.File r4 = r4.file     // Catch: all -> 0x004d
            java.lang.String r4 = kotlin.jvm.internal.Intrinsics.stringPlus(r3, r4)     // Catch: all -> 0x004d
            r2.<init>(r4, r1)     // Catch: all -> 0x004d
            throw r2     // Catch: all -> 0x004d
        L_0x005e:
            r1 = move-exception
            java.lang.IllegalStateException r2 = new java.lang.IllegalStateException     // Catch: all -> 0x004d
            java.lang.String r3 = "Failed parsing favorites file: "
            java.io.File r4 = r4.file     // Catch: all -> 0x004d
            java.lang.String r4 = kotlin.jvm.internal.Intrinsics.stringPlus(r3, r4)     // Catch: all -> 0x004d
            r2.<init>(r4, r1)     // Catch: all -> 0x004d
            throw r2     // Catch: all -> 0x004d
        L_0x006d:
            libcore.io.IoUtils.closeQuietly(r0)
            throw r4
        L_0x0071:
            java.lang.String r4 = "ControlsFavoritePersistenceWrapper"
            java.lang.String r0 = "No file found"
            android.util.Log.i(r4, r0)
            java.util.List r4 = kotlin.collections.CollectionsKt.emptyList()
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.controls.controller.ControlsFavoritePersistenceWrapper.readFavorites():java.util.List");
    }

    private final List<StructureInfo> parseXml(XmlPullParser xmlPullParser) {
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        ComponentName componentName = null;
        String str = null;
        while (true) {
            int next = xmlPullParser.next();
            if (next == 1) {
                return arrayList;
            }
            String name = xmlPullParser.getName();
            String str2 = "";
            if (name == null) {
                name = str2;
            }
            if (next == 2 && Intrinsics.areEqual(name, "structure")) {
                componentName = ComponentName.unflattenFromString(xmlPullParser.getAttributeValue(null, "component"));
                str = xmlPullParser.getAttributeValue(null, "structure");
                if (str == null) {
                    str = str2;
                }
            } else if (next == 2 && Intrinsics.areEqual(name, "control")) {
                String attributeValue = xmlPullParser.getAttributeValue(null, "id");
                String attributeValue2 = xmlPullParser.getAttributeValue(null, "title");
                String attributeValue3 = xmlPullParser.getAttributeValue(null, "subtitle");
                if (attributeValue3 != null) {
                    str2 = attributeValue3;
                }
                String attributeValue4 = xmlPullParser.getAttributeValue(null, "type");
                Integer valueOf = attributeValue4 == null ? null : Integer.valueOf(Integer.parseInt(attributeValue4));
                if (!(attributeValue == null || attributeValue2 == null || valueOf == null)) {
                    arrayList2.add(new ControlInfo(attributeValue, attributeValue2, str2, valueOf.intValue()));
                }
            } else if (next == 3 && Intrinsics.areEqual(name, "structure")) {
                Intrinsics.checkNotNull(componentName);
                Intrinsics.checkNotNull(str);
                arrayList.add(new StructureInfo(componentName, str, CollectionsKt___CollectionsKt.toList(arrayList2)));
                arrayList2.clear();
            }
        }
    }
}
