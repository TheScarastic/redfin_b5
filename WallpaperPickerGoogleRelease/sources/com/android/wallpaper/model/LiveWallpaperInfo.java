package com.android.wallpaper.model;

import android.app.Activity;
import android.app.WallpaperInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.media.ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import androidx.core.content.ContextCompat;
import com.android.systemui.shared.R;
import com.android.wallpaper.asset.Asset;
import com.android.wallpaper.asset.LiveWallpaperThumbAsset;
import com.android.wallpaper.compat.BuildCompat;
import com.android.wallpaper.module.InjectorProvider;
import com.android.wallpaper.module.NoOpUserEventLogger;
import com.android.wallpaper.util.ActivityUtils;
import java.io.IOException;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.xmlpull.v1.XmlPullParserException;
/* loaded from: classes.dex */
public class LiveWallpaperInfo extends WallpaperInfo {
    public static final Parcelable.Creator<LiveWallpaperInfo> CREATOR = new Parcelable.Creator<LiveWallpaperInfo>() { // from class: com.android.wallpaper.model.LiveWallpaperInfo.1
        /* Return type fixed from 'java.lang.Object' to match base method */
        @Override // android.os.Parcelable.Creator
        public LiveWallpaperInfo createFromParcel(Parcel parcel) {
            return new LiveWallpaperInfo(parcel);
        }

        /* Return type fixed from 'java.lang.Object[]' to match base method */
        @Override // android.os.Parcelable.Creator
        public LiveWallpaperInfo[] newArray(int i) {
            return new LiveWallpaperInfo[i];
        }
    };
    public final String mCollectionId;
    public WallpaperInfo mInfo;
    public LiveWallpaperThumbAsset mThumbAsset;
    public boolean mVisibleTitle;

    public LiveWallpaperInfo(WallpaperInfo wallpaperInfo) {
        this.mInfo = wallpaperInfo;
        this.mVisibleTitle = true;
        this.mCollectionId = null;
    }

    public static LiveWallpaperInfo fromAttributeSet(Context context, String str, AttributeSet attributeSet) {
        String attributeValue = attributeSet.getAttributeValue(null, "id");
        if (TextUtils.isEmpty(attributeValue)) {
            Log.w("LiveWallpaperInfo", "Live wallpaper declaration without id in category " + str);
            return null;
        }
        String attributeValue2 = attributeSet.getAttributeValue(null, "package");
        String attributeValue3 = attributeSet.getAttributeValue(null, "service");
        if (TextUtils.isEmpty(attributeValue3)) {
            Log.w("LiveWallpaperInfo", "Live wallpaper declaration without service: " + attributeValue);
            return null;
        }
        Intent intent = new Intent("android.service.wallpaper.WallpaperService");
        if (TextUtils.isEmpty(attributeValue2)) {
            String[] split = attributeValue3.split("/");
            if (split == null || split.length != 2) {
                Log.w("LiveWallpaperInfo", "Live wallpaper declaration with invalid service: " + attributeValue);
                return null;
            }
            attributeValue2 = split[0];
            attributeValue3 = split[1];
        }
        intent.setClassName(attributeValue2, attributeValue3);
        List<ResolveInfo> queryIntentServices = context.getPackageManager().queryIntentServices(intent, 128);
        if (queryIntentServices.isEmpty()) {
            Log.w("LiveWallpaperInfo", "Couldn't find live wallpaper for " + attributeValue3);
            return null;
        }
        try {
            return new LiveWallpaperInfo(new WallpaperInfo(context, queryIntentServices.get(0)), false, str);
        } catch (IOException | XmlPullParserException e) {
            StringBuilder m = ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0.m("Skipping wallpaper ");
            m.append(queryIntentServices.get(0).serviceInfo);
            Log.w("LiveWallpaperInfo", m.toString(), e);
            return null;
        }
    }

    public static List<WallpaperInfo> getAll(Context context, Set<String> set) {
        List<ResolveInfo> allOnDevice = getAllOnDevice(context);
        ArrayList arrayList = new ArrayList();
        NoOpUserEventLogger liveWallpaperInfoFactory = InjectorProvider.getInjector().getLiveWallpaperInfoFactory(context);
        int i = 0;
        while (true) {
            ArrayList arrayList2 = (ArrayList) allOnDevice;
            if (i >= arrayList2.size()) {
                return arrayList;
            }
            ResolveInfo resolveInfo = (ResolveInfo) arrayList2.get(i);
            try {
                WallpaperInfo wallpaperInfo = new WallpaperInfo(context, resolveInfo);
                if (set == null || !set.contains(wallpaperInfo.getPackageName())) {
                    arrayList.add(liveWallpaperInfoFactory.getLiveWallpaperInfo(wallpaperInfo));
                }
            } catch (IOException | XmlPullParserException e) {
                StringBuilder m = ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0.m("Skipping wallpaper ");
                m.append(resolveInfo.serviceInfo);
                Log.w("LiveWallpaperInfo", m.toString(), e);
            }
            i++;
        }
    }

    public static List<ResolveInfo> getAllOnDevice(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        String packageName = context.getPackageName();
        List<ResolveInfo> queryIntentServices = packageManager.queryIntentServices(new Intent("android.service.wallpaper.WallpaperService"), 128);
        ArrayList arrayList = new ArrayList();
        Iterator<ResolveInfo> it = queryIntentServices.iterator();
        while (it.hasNext()) {
            ResolveInfo next = it.next();
            if (packageName.equals(next.serviceInfo.packageName)) {
                it.remove();
            } else {
                if ((next.serviceInfo.applicationInfo.flags & 129) != 0) {
                    arrayList.add(next);
                    it.remove();
                }
            }
        }
        if (queryIntentServices.isEmpty()) {
            return arrayList;
        }
        Collections.sort(queryIntentServices, new Comparator<ResolveInfo>() { // from class: com.android.wallpaper.model.LiveWallpaperInfo.2
            public final Collator mCollator = Collator.getInstance();

            /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object, java.lang.Object] */
            @Override // java.util.Comparator
            public int compare(ResolveInfo resolveInfo, ResolveInfo resolveInfo2) {
                return this.mCollator.compare(resolveInfo.loadLabel(packageManager), resolveInfo2.loadLabel(packageManager));
            }
        });
        arrayList.addAll(queryIntentServices);
        return arrayList;
    }

    @Override // com.android.wallpaper.model.WallpaperInfo
    public String getActionUrl(Context context) {
        if (BuildCompat.sSdk >= 25) {
            try {
                Uri loadContextUri = this.mInfo.loadContextUri(context.getPackageManager());
                if (loadContextUri != null) {
                    return loadContextUri.toString();
                }
            } catch (Resources.NotFoundException unused) {
            }
        }
        return null;
    }

    @Override // com.android.wallpaper.model.WallpaperInfo
    public Asset getAsset(Context context) {
        return null;
    }

    @Override // com.android.wallpaper.model.WallpaperInfo
    public List<String> getAttributions(Context context) {
        String str;
        ArrayList arrayList = new ArrayList();
        PackageManager packageManager = context.getPackageManager();
        CharSequence loadLabel = this.mInfo.loadLabel(packageManager);
        if (loadLabel == null) {
            str = null;
        } else {
            str = loadLabel.toString();
        }
        arrayList.add(str);
        try {
            CharSequence loadAuthor = this.mInfo.loadAuthor(packageManager);
            if (loadAuthor != null) {
                arrayList.add(loadAuthor.toString());
            }
        } catch (Resources.NotFoundException unused) {
        }
        try {
            CharSequence loadDescription = this.mInfo.loadDescription(packageManager);
            if (loadDescription != null) {
                arrayList.add(loadDescription.toString());
            }
        } catch (Resources.NotFoundException unused2) {
        }
        return arrayList;
    }

    @Override // com.android.wallpaper.model.WallpaperInfo
    public String getCollectionId(Context context) {
        if (TextUtils.isEmpty(this.mCollectionId)) {
            return context.getString(R.string.live_wallpaper_collection_id);
        }
        return this.mCollectionId;
    }

    @Override // com.android.wallpaper.model.WallpaperInfo
    public Asset getThumbAsset(Context context) {
        if (this.mThumbAsset == null) {
            this.mThumbAsset = new LiveWallpaperThumbAsset(context, this.mInfo);
        }
        return this.mThumbAsset;
    }

    @Override // com.android.wallpaper.model.WallpaperInfo
    public String getTitle(Context context) {
        CharSequence loadLabel;
        if (!this.mVisibleTitle || (loadLabel = this.mInfo.loadLabel(context.getPackageManager())) == null) {
            return null;
        }
        return loadLabel.toString();
    }

    @Override // com.android.wallpaper.model.WallpaperInfo
    public WallpaperInfo getWallpaperComponent() {
        return this.mInfo;
    }

    @Override // com.android.wallpaper.model.WallpaperInfo
    public String getWallpaperId() {
        return this.mInfo.getServiceName();
    }

    @Override // com.android.wallpaper.model.WallpaperInfo
    public void showPreview(Activity activity, InlinePreviewIntentFactory inlinePreviewIntentFactory, int i) {
        Objects.requireNonNull(inlinePreviewIntentFactory);
        if (ContextCompat.checkSelfPermission(activity, "android.permission.BIND_WALLPAPER") == 0) {
            activity.startActivityForResult(inlinePreviewIntentFactory.newIntent(activity, this), i);
            return;
        }
        Intent intent = new Intent("android.service.wallpaper.CHANGE_LIVE_WALLPAPER");
        intent.putExtra("android.service.wallpaper.extra.LIVE_WALLPAPER_COMPONENT", this.mInfo.getComponent());
        ActivityUtils.startActivityForResultSafely(activity, intent, i);
    }

    @Override // com.android.wallpaper.model.WallpaperInfo, android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.mPlaceholderColor);
        parcel.writeParcelable(this.mInfo, 0);
        parcel.writeInt(this.mVisibleTitle ? 1 : 0);
        parcel.writeString(this.mCollectionId);
    }

    public LiveWallpaperInfo(WallpaperInfo wallpaperInfo, boolean z, String str) {
        this.mInfo = wallpaperInfo;
        this.mVisibleTitle = z;
        this.mCollectionId = str;
    }

    public LiveWallpaperInfo(Parcel parcel) {
        super(parcel);
        this.mInfo = (WallpaperInfo) parcel.readParcelable(WallpaperInfo.class.getClassLoader());
        this.mVisibleTitle = parcel.readInt() != 1 ? false : true;
        this.mCollectionId = parcel.readString();
    }
}
