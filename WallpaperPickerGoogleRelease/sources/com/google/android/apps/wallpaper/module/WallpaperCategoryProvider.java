package com.google.android.apps.wallpaper.module;

import android.app.WallpaperInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.os.Parcelable;
import android.support.media.ExifInterface$$ExternalSyntheticOutline0;
import android.support.media.ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0;
import android.util.Log;
import com.android.wallpaper.model.Category;
import com.android.wallpaper.model.CategoryReceiver;
import com.android.wallpaper.model.DownloadableLiveWallpaperInfo;
import com.android.wallpaper.model.LiveWallpaperInfo;
import com.android.wallpaper.model.PartnerWallpaperInfo$$ExternalSyntheticOutline0;
import com.android.wallpaper.model.WallpaperCategory;
import com.android.wallpaper.module.DefaultCategoryProvider;
import com.android.wallpaper.module.DefaultCategoryProvider$FetchCategoriesTask$$ExternalSyntheticLambda1;
import com.android.wallpaper.module.DefaultCategoryProvider$FetchCategoriesTask$$ExternalSyntheticLambda2;
import com.android.wallpaper.module.InjectorProvider;
import com.android.wallpaper.module.NoOpUserEventLogger;
import com.google.android.apps.wallpaper.model.GoogleLiveWallpaperInfo;
import com.google.android.apps.wallpaper.model.MicropaperWallpaperInfo;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.xmlpull.v1.XmlPullParserException;
import wireless.android.learning.acmi.micropaper.frontend.MicropaperFrontend;
/* loaded from: classes.dex */
public class WallpaperCategoryProvider extends DefaultCategoryProvider {
    public static List<Category> sSystemStaticCategories;

    /* loaded from: classes.dex */
    public static class GoogleFetchCategoriesTask extends DefaultCategoryProvider.FetchCategoriesTask {
        public boolean mForceRefresh;
        public Resources mPixelApkResources;
        public Resources mStubApkResources;
        public String mStubPackageName;

        public GoogleFetchCategoriesTask(CategoryReceiver categoryReceiver, Context context, boolean z, AnonymousClass1 r4) {
            super(categoryReceiver, context);
            this.mForceRefresh = z;
        }

        /* Return type fixed from 'java.lang.Object' to match base method */
        /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object[]] */
        /* JADX WARNING: Removed duplicated region for block: B:104:0x030c A[LOOP:5: B:102:0x0306->B:104:0x030c, LOOP_END] */
        /* JADX WARNING: Removed duplicated region for block: B:77:0x01ea  */
        /* JADX WARNING: Removed duplicated region for block: B:81:0x01f5  */
        /* JADX WARNING: Removed duplicated region for block: B:84:0x0221  */
        /* JADX WARNING: Removed duplicated region for block: B:90:0x029b  */
        @Override // android.os.AsyncTask
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public java.lang.Void doInBackground(java.lang.Void[] r15) {
            /*
            // Method dump skipped, instructions count: 797
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.android.apps.wallpaper.module.WallpaperCategoryProvider.GoogleFetchCategoriesTask.doInBackground(java.lang.Object[]):java.lang.Object");
        }

        public Set<String> getExcludedLiveWallpaperPackageNames() {
            HashSet hashSet = new HashSet();
            List<Category> list = DefaultCategoryProvider.sSystemCategories;
            if (list != null) {
                hashSet.addAll((Collection) list.stream().filter(DefaultCategoryProvider$FetchCategoriesTask$$ExternalSyntheticLambda2.INSTANCE).flatMap(DefaultCategoryProvider$FetchCategoriesTask$$ExternalSyntheticLambda1.INSTANCE).collect(Collectors.toSet()));
            }
            Resources resources = this.mStubApkResources;
            if (resources != null) {
                hashSet.addAll(getNexusLiveWallpaperPackageNames(resources, "nexus_live_categories", this.mStubPackageName));
            }
            Resources resources2 = this.mPixelApkResources;
            if (resources2 != null) {
                hashSet.addAll(getNexusLiveWallpaperPackageNames(resources2, "pixel_live_categories", "com.google.pixel.livewallpaper"));
            }
            hashSet.add(MicropaperFrontend.MICROPAPER_SERVICE.getPackageName());
            return hashSet;
        }

        public final List<Category> getNexusLiveWallpaperCategories(Resources resources, String str, String str2) {
            List list;
            List<ResolveInfo> list2;
            int i;
            Context context;
            WallpaperInfo wallpaperInfo;
            Object obj;
            Resources resources2 = resources;
            String str3 = str2;
            ArrayList arrayList = new ArrayList();
            String str4 = "array";
            int identifier = resources2.getIdentifier(str, str4, str3);
            if (identifier == 0) {
                return arrayList;
            }
            String[] stringArray = resources2.getStringArray(identifier);
            int i2 = 0;
            while (i2 < stringArray.length) {
                String str5 = stringArray[i2];
                String string = resources2.getString(resources2.getIdentifier(str5 + "_title", "string", str3));
                String packageNameForNexusLiveWp = getPackageNameForNexusLiveWp(resources2, str5, str3);
                String string2 = resources2.getString(resources2.getIdentifier(str5 + "_featured_service_name", "string", str3));
                int m = PartnerWallpaperInfo$$ExternalSyntheticOutline0.m(str5, "_service_names", resources2, str4, str3);
                if (m == 0) {
                    list = null;
                } else {
                    list = Arrays.asList(resources2.getStringArray(m));
                }
                Context context2 = this.mAppContext;
                Parcelable.Creator<LiveWallpaperInfo> creator = LiveWallpaperInfo.CREATOR;
                if (list != null) {
                    List<ResolveInfo> queryIntentServices = context2.getPackageManager().queryIntentServices(new Intent("android.service.wallpaper.WallpaperService"), 128);
                    ResolveInfo[] resolveInfoArr = new ResolveInfo[list.size()];
                    for (Iterator<ResolveInfo> it = queryIntentServices.iterator(); it.hasNext(); it = it) {
                        ResolveInfo next = it.next();
                        int indexOf = list.indexOf(next.serviceInfo.name);
                        if (indexOf != -1) {
                            resolveInfoArr[indexOf] = next;
                        }
                    }
                    list2 = Arrays.asList(resolveInfoArr);
                } else {
                    list2 = LiveWallpaperInfo.getAllOnDevice(context2);
                }
                List<ResolveInfo> list3 = list2;
                ArrayList arrayList2 = new ArrayList();
                NoOpUserEventLogger liveWallpaperInfoFactory = InjectorProvider.getInjector().getLiveWallpaperInfoFactory(context2);
                int i3 = 0;
                while (i3 < list3.size()) {
                    ResolveInfo resolveInfo = list3.get(i3);
                    if (resolveInfo == null) {
                        Log.e("LiveWallpaperInfo", "Found a null resolve info");
                    } else {
                        try {
                            wallpaperInfo = new WallpaperInfo(context2, resolveInfo);
                        } catch (IOException e) {
                            context = context2;
                            StringBuilder m2 = ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0.m("Skipping wallpaper ");
                            m2.append(resolveInfo.serviceInfo);
                            Log.w("LiveWallpaperInfo", m2.toString(), e);
                        } catch (XmlPullParserException e2) {
                            context = context2;
                            StringBuilder m3 = ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0.m("Skipping wallpaper ");
                            m3.append(resolveInfo.serviceInfo);
                            Log.w("LiveWallpaperInfo", m3.toString(), e2);
                        }
                        if (packageNameForNexusLiveWp.equals(wallpaperInfo.getPackageName())) {
                            Objects.requireNonNull(liveWallpaperInfoFactory);
                            if (wallpaperInfo.getComponent().equals(MicropaperFrontend.MICROPAPER_SERVICE)) {
                                obj = new MicropaperWallpaperInfo(wallpaperInfo);
                                context = context2;
                            } else {
                                context = context2;
                                obj = new GoogleLiveWallpaperInfo(wallpaperInfo, false, null);
                            }
                            arrayList2.add(obj);
                            i3++;
                            list3 = list3;
                            stringArray = stringArray;
                            context2 = context;
                        }
                    }
                    context = context2;
                    i3++;
                    list3 = list3;
                    stringArray = stringArray;
                    context2 = context;
                }
                Context context3 = this.mAppContext;
                Parcelable.Creator<DownloadableLiveWallpaperInfo> creator2 = DownloadableLiveWallpaperInfo.CREATOR;
                List<ResolveInfo> queryIntentServices2 = context3.getPackageManager().queryIntentServices(new Intent("android.service.wallpaper.WallpaperService"), 640);
                if (list != null) {
                    ResolveInfo[] resolveInfoArr2 = new ResolveInfo[list.size()];
                    for (ResolveInfo resolveInfo2 : queryIntentServices2) {
                        int indexOf2 = list.indexOf(resolveInfo2.serviceInfo.name);
                        if (indexOf2 != -1) {
                            resolveInfoArr2[indexOf2] = resolveInfo2;
                        }
                    }
                    queryIntentServices2 = Arrays.asList(resolveInfoArr2);
                }
                List<String> toBeInstalledComponent = DownloadableLiveWallpaperInfo.getToBeInstalledComponent(context3);
                ArrayList arrayList3 = new ArrayList();
                for (ResolveInfo resolveInfo3 : queryIntentServices2) {
                    if (resolveInfo3 != null) {
                        try {
                            WallpaperInfo wallpaperInfo2 = new WallpaperInfo(context3, resolveInfo3);
                            if (packageNameForNexusLiveWp.equals(wallpaperInfo2.getPackageName())) {
                                DownloadableLiveWallpaperInfo downloadableLiveWallpaperInfo = new DownloadableLiveWallpaperInfo(wallpaperInfo2, false, str5);
                                if (downloadableLiveWallpaperInfo.isToBeInstalled(toBeInstalledComponent)) {
                                    arrayList3.add(downloadableLiveWallpaperInfo);
                                }
                            }
                        } catch (IOException | XmlPullParserException e3) {
                            StringBuilder m4 = ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0.m("Skipping wallpaper ");
                            m4.append(resolveInfo3.serviceInfo);
                            Log.w("DownloadLiveWallpaperInfo", m4.toString(), e3);
                        }
                    }
                }
                arrayList2.addAll(arrayList3);
                if (arrayList2.size() != 0) {
                    int i4 = 0;
                    while (true) {
                        if (i4 >= arrayList2.size()) {
                            i = 0;
                            break;
                        } else if (string2.equals(((com.android.wallpaper.model.WallpaperInfo) arrayList2.get(i4)).getWallpaperComponent().getServiceName())) {
                            i = i4;
                            break;
                        } else {
                            i4++;
                        }
                    }
                    arrayList.add(new WallpaperCategory(string, ExifInterface$$ExternalSyntheticOutline0.m("nexus_live_category_", i2), i, arrayList2, 101));
                }
                i2++;
                resources2 = resources;
                str3 = str2;
                str4 = str4;
                stringArray = stringArray;
            }
            return arrayList;
        }

        public final List<String> getNexusLiveWallpaperPackageNames(Resources resources, String str, String str2) {
            ArrayList arrayList = new ArrayList();
            int identifier = resources.getIdentifier(str, "array", str2);
            if (identifier == 0) {
                return arrayList;
            }
            for (String str3 : resources.getStringArray(identifier)) {
                arrayList.add(getPackageNameForNexusLiveWp(resources, str3, str2));
            }
            return arrayList;
        }

        public final String getPackageNameForNexusLiveWp(Resources resources, String str, String str2) {
            return resources.getString(resources.getIdentifier(str + "_package_name", "string", str2));
        }

        /* JADX DEBUG: Failed to insert an additional move for type inference into block B:163:0x01a4 */
        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r3v20, types: [java.util.List] */
        /* JADX WARNING: Removed duplicated region for block: B:124:0x02b8  */
        /* JADX WARNING: Removed duplicated region for block: B:125:0x02c1  */
        /* JADX WARNING: Removed duplicated region for block: B:127:0x02c4  */
        /* JADX WARNING: Removed duplicated region for block: B:132:0x02d5  */
        /* JADX WARNING: Removed duplicated region for block: B:139:0x02f7 A[LOOP:6: B:137:0x02ef->B:139:0x02f7, LOOP_END] */
        /* JADX WARNING: Removed duplicated region for block: B:147:0x0165 A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /* JADX WARNING: Removed duplicated region for block: B:99:0x0184 A[ADDED_TO_REGION] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public java.util.List<com.android.wallpaper.model.Category> getSystemCategories() {
            /*
            // Method dump skipped, instructions count: 780
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.android.apps.wallpaper.module.WallpaperCategoryProvider.GoogleFetchCategoriesTask.getSystemCategories():java.util.List");
        }
    }

    public WallpaperCategoryProvider(Context context) {
        super(context);
    }
}
