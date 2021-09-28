package com.android.customization.model.themedicon;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ProviderInfo;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
/* loaded from: classes.dex */
public class ThemedIconUtils {
    public final String mProviderAuthority;
    public ProviderInfo mProviderInfo;

    public ThemedIconUtils(Context context, String str) {
        ProviderInfo providerInfo;
        Bundle bundle;
        ResolveInfo resolveActivity = context.getPackageManager().resolveActivity(new Intent("android.intent.action.MAIN").addCategory("android.intent.category.HOME"), 65664);
        if (resolveActivity == null || (bundle = resolveActivity.activityInfo.metaData) == null) {
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

    public Uri getUriForPath(String str) {
        return new Uri.Builder().scheme("content").authority(this.mProviderInfo.authority).appendPath(str).build();
    }
}
