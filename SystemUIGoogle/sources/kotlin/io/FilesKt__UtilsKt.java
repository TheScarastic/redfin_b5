package kotlin.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: Utils.kt */
/* access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class FilesKt__UtilsKt extends FilesKt__FileTreeWalkKt {
    public static /* synthetic */ File copyTo$default(File file, File file2, boolean z, int i, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            z = false;
        }
        if ((i2 & 4) != 0) {
            i = 8192;
        }
        return copyTo(file, file2, z, i);
    }

    public static final File copyTo(File file, File file2, boolean z, int i) {
        Intrinsics.checkNotNullParameter(file, "$this$copyTo");
        Intrinsics.checkNotNullParameter(file2, "target");
        if (file.exists()) {
            if (file2.exists()) {
                if (!z) {
                    throw new FileAlreadyExistsException(file, file2, "The destination file already exists.");
                } else if (!file2.delete()) {
                    throw new FileAlreadyExistsException(file, file2, "Tried to overwrite the destination, but failed to delete it.");
                }
            }
            if (!file.isDirectory()) {
                File parentFile = file2.getParentFile();
                if (parentFile != null) {
                    parentFile.mkdirs();
                }
                FileOutputStream fileInputStream = new FileInputStream(file);
                try {
                    fileInputStream = new FileOutputStream(file2);
                    ByteStreamsKt.copyTo(fileInputStream, fileInputStream, i);
                    th = null;
                } finally {
                    try {
                        throw th;
                    } finally {
                    }
                }
            } else if (!file2.mkdirs()) {
                throw new FileSystemException(file, file2, "Failed to create target directory.");
            }
            return file2;
        }
        throw new NoSuchFileException(file, null, "The source file doesn't exist.", 2, null);
    }
}
