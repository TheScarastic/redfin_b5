package com.android.customization.model.grid;

import android.content.Context;
import com.android.wallpaper.util.PreviewUtils;
import java.util.List;
/* loaded from: classes.dex */
public class LauncherGridOptionsProvider {
    public final Context mContext;
    public List<GridOption> mOptions;
    public final PreviewUtils mPreviewUtils;

    public LauncherGridOptionsProvider(Context context, String str) {
        this.mPreviewUtils = new PreviewUtils(context, str);
        this.mContext = context;
    }
}
