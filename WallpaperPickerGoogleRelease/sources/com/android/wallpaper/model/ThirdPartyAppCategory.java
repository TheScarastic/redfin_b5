package com.android.wallpaper.model;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import com.android.wallpaper.asset.Asset;
import com.android.wallpaper.util.ActivityUtils;
/* loaded from: classes.dex */
public class ThirdPartyAppCategory extends Category {
    public final ResolveInfo mResolveInfo;

    public ThirdPartyAppCategory(Context context, ResolveInfo resolveInfo, String str, int i) {
        super(resolveInfo.loadLabel(context.getPackageManager()).toString(), str, i);
        this.mResolveInfo = resolveInfo;
    }

    @Override // com.android.wallpaper.model.Category
    public boolean containsThirdParty(String str) {
        return this.mResolveInfo.activityInfo.packageName.equals(str);
    }

    @Override // com.android.wallpaper.model.Category
    public Drawable getOverlayIcon(Context context) {
        return this.mResolveInfo.loadIcon(context.getPackageManager());
    }

    @Override // com.android.wallpaper.model.Category
    public int getOverlayIconSizeDp() {
        return 48;
    }

    @Override // com.android.wallpaper.model.Category
    public Asset getThumbnail(Context context) {
        return null;
    }

    @Override // com.android.wallpaper.model.Category
    public void show(Activity activity, PickerIntentFactory pickerIntentFactory, int i) {
        ActivityInfo activityInfo = this.mResolveInfo.activityInfo;
        ComponentName componentName = new ComponentName(activityInfo.packageName, activityInfo.name);
        Intent intent = new Intent("android.intent.action.SET_WALLPAPER");
        intent.setComponent(componentName);
        ActivityUtils.startActivityForResultSafely(activity, intent, i);
    }
}
