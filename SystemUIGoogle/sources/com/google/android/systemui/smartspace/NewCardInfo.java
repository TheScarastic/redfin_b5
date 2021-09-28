package com.google.android.systemui.smartspace;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import com.android.systemui.smartspace.nano.SmartspaceProto$CardWrapper;
import com.android.systemui.smartspace.nano.SmartspaceProto$SmartspaceUpdate;
import java.io.ByteArrayOutputStream;
/* loaded from: classes2.dex */
public class NewCardInfo {
    private final SmartspaceProto$SmartspaceUpdate.SmartspaceCard mCard;
    private final Intent mIntent;
    private final boolean mIsPrimary;
    private final PackageInfo mPackageInfo;
    private final long mPublishTime;

    public NewCardInfo(SmartspaceProto$SmartspaceUpdate.SmartspaceCard smartspaceCard, Intent intent, boolean z, long j, PackageInfo packageInfo) {
        this.mCard = smartspaceCard;
        this.mIsPrimary = z;
        this.mIntent = intent;
        this.mPublishTime = j;
        this.mPackageInfo = packageInfo;
    }

    public boolean isPrimary() {
        return this.mIsPrimary;
    }

    public Bitmap retrieveIcon(Context context) {
        SmartspaceProto$SmartspaceUpdate.SmartspaceCard.Image image = this.mCard.icon;
        if (image == null) {
            return null;
        }
        Bitmap bitmap = (Bitmap) retrieveFromIntent(image.key, this.mIntent);
        if (bitmap != null) {
            return bitmap;
        }
        try {
        } catch (Exception unused) {
            Log.e("NewCardInfo", "retrieving bitmap uri=" + image.uri + " gsaRes=" + image.gsaResourceName);
        }
        if (!TextUtils.isEmpty(image.uri)) {
            return MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.parse(image.uri));
        }
        if (!TextUtils.isEmpty(image.gsaResourceName)) {
            Intent.ShortcutIconResource shortcutIconResource = new Intent.ShortcutIconResource();
            shortcutIconResource.packageName = "com.google.android.googlequicksearchbox";
            shortcutIconResource.resourceName = image.gsaResourceName;
            return createIconBitmap(shortcutIconResource, context);
        }
        return null;
    }

    public SmartspaceProto$CardWrapper toWrapper(Context context) {
        SmartspaceProto$CardWrapper smartspaceProto$CardWrapper = new SmartspaceProto$CardWrapper();
        Bitmap retrieveIcon = retrieveIcon(context);
        if (retrieveIcon != null) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            retrieveIcon.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            smartspaceProto$CardWrapper.icon = byteArrayOutputStream.toByteArray();
        }
        smartspaceProto$CardWrapper.card = this.mCard;
        smartspaceProto$CardWrapper.publishTime = this.mPublishTime;
        PackageInfo packageInfo = this.mPackageInfo;
        if (packageInfo != null) {
            smartspaceProto$CardWrapper.gsaVersionCode = packageInfo.versionCode;
            smartspaceProto$CardWrapper.gsaUpdateTime = packageInfo.lastUpdateTime;
        }
        return smartspaceProto$CardWrapper;
    }

    private static <T> T retrieveFromIntent(String str, Intent intent) {
        if (!TextUtils.isEmpty(str)) {
            return (T) intent.getParcelableExtra(str);
        }
        return null;
    }

    static Bitmap createIconBitmap(Intent.ShortcutIconResource shortcutIconResource, Context context) {
        try {
            Resources resourcesForApplication = context.getPackageManager().getResourcesForApplication(shortcutIconResource.packageName);
            if (resourcesForApplication != null) {
                return BitmapFactory.decodeResource(resourcesForApplication, resourcesForApplication.getIdentifier(shortcutIconResource.resourceName, null, null));
            }
        } catch (Exception unused) {
        }
        return null;
    }

    public int getUserId() {
        return this.mIntent.getIntExtra("uid", -1);
    }

    public boolean shouldDiscard() {
        SmartspaceProto$SmartspaceUpdate.SmartspaceCard smartspaceCard = this.mCard;
        return smartspaceCard == null || smartspaceCard.shouldDiscard;
    }
}
