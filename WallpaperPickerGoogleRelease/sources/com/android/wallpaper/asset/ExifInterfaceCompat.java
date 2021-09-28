package com.android.wallpaper.asset;

import android.media.ExifInterface;
import com.android.wallpaper.compat.BuildCompat;
import java.io.IOException;
import java.io.InputStream;
/* loaded from: classes.dex */
public class ExifInterfaceCompat {
    public ExifInterface mFrameworkExifInterface;
    public androidx.exifinterface.media.ExifInterface mSupportExifInterface;

    public ExifInterfaceCompat(InputStream inputStream) throws IOException {
        if (BuildCompat.sSdk >= 27) {
            this.mFrameworkExifInterface = new ExifInterface(inputStream);
        } else {
            this.mSupportExifInterface = new androidx.exifinterface.media.ExifInterface(inputStream);
        }
    }
}
