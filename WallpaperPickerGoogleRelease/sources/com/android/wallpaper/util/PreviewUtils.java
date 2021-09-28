package com.android.wallpaper.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ProviderInfo;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/* loaded from: classes.dex */
public class PreviewUtils {
    public static final ExecutorService sExecutorService = Executors.newSingleThreadExecutor();
    public final Context mContext;
    public final String mProviderAuthority;
    public ProviderInfo mProviderInfo;

    /* loaded from: classes.dex */
    public interface WorkspacePreviewCallback {
    }

    public PreviewUtils(Context context, String str) {
        ProviderInfo providerInfo;
        ActivityInfo activityInfo;
        Bundle bundle;
        this.mContext = context;
        ResolveInfo resolveActivity = context.getPackageManager().resolveActivity(new Intent("android.intent.action.MAIN").addCategory("android.intent.category.HOME"), 65664);
        if (resolveActivity == null || (activityInfo = resolveActivity.activityInfo) == null || (bundle = activityInfo.metaData) == null) {
            this.mProviderAuthority = null;
        } else {
            this.mProviderAuthority = bundle.getString(str);
        }
        if (TextUtils.isEmpty(this.mProviderAuthority)) {
            providerInfo = null;
        } else {
            providerInfo = context.getPackageManager().resolveContentProvider(this.mProviderAuthority, 0);
        }
        this.mProviderInfo = providerInfo;
        if (providerInfo != null && !TextUtils.isEmpty(providerInfo.readPermission) && context.checkSelfPermission(this.mProviderInfo.readPermission) != 0) {
            this.mProviderInfo = null;
        }
    }

    public Uri getUri(String str) {
        return new Uri.Builder().scheme("content").authority(this.mProviderInfo.authority).appendPath(str).build();
    }
}
