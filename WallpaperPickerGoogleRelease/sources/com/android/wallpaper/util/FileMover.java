package com.android.wallpaper.util;

import android.content.Context;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
/* loaded from: classes.dex */
public class FileMover {
    public static File moveFileBetweenContexts(Context context, String str, Context context2, String str2) throws IOException {
        if (!context.getFileStreamPath(str).exists()) {
            return null;
        }
        FileInputStream openFileInput = context.openFileInput(str);
        try {
            FileOutputStream openFileOutput = context2.openFileOutput(str2, 0);
            FileChannel channel = openFileInput.getChannel();
            channel.transferTo(0, channel.size(), openFileOutput.getChannel());
            openFileOutput.flush();
            context.deleteFile(str);
            openFileOutput.close();
            openFileInput.close();
            return context2.getFileStreamPath(str2);
        } catch (Throwable th) {
            if (openFileInput != null) {
                try {
                    openFileInput.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }
}
