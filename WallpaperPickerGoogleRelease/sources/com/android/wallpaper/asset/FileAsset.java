package com.android.wallpaper.asset;

import android.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
/* loaded from: classes.dex */
public final class FileAsset extends StreamableAsset {
    public final File mFile;

    public FileAsset(File file) {
        this.mFile = file;
    }

    @Override // com.android.wallpaper.asset.StreamableAsset
    public InputStream openInputStream() {
        try {
            return new FileInputStream(this.mFile.getAbsolutePath());
        } catch (FileNotFoundException e) {
            Log.w("FileAsset", "Image file not found", e);
            return null;
        }
    }
}
