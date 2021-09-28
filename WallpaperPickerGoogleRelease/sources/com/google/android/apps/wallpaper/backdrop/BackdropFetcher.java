package com.google.android.apps.wallpaper.backdrop;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.DeviceConfig;
import android.util.Log;
import androidx.core.graphics.PathParser$$ExternalSyntheticOutline0;
import com.android.customization.picker.grid.GridFragment$$ExternalSyntheticLambda1;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.VolleyError;
import com.android.wallpaper.network.ServerFetcher;
import com.google.android.apps.common.volley.request.ProtoRequest;
import com.google.android.apps.wallpaper.module.DeviceConfigFilteringLabelProvider;
import com.google.android.apps.wallpaper.module.FilteringLabelProvider;
import com.google.android.gms.internal.zzfit;
import com.google.chrome.dongle.imax.wallpaper2.protos.ImaxWallpaperProto$GetImageFromCollectionRequest;
import com.google.chrome.dongle.imax.wallpaper2.protos.ImaxWallpaperProto$GetImageFromCollectionResponse;
import com.google.chrome.dongle.imax.wallpaper2.protos.ImaxWallpaperProto$GetImagesInCollectionRequest;
import com.google.chrome.dongle.imax.wallpaper2.protos.ImaxWallpaperProto$GetImagesInCollectionResponse;
import com.google.chrome.dongle.imax.wallpaper2.protos.ImaxWallpaperProto$Image;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
/* loaded from: classes.dex */
public class BackdropFetcher implements ServerFetcher {
    public final FilteringLabelProvider mLabelProvider;
    public final zzfit mRequester;

    public BackdropFetcher(zzfit zzfit, FilteringLabelProvider filteringLabelProvider) {
        this.mRequester = zzfit;
        this.mLabelProvider = filteringLabelProvider;
    }

    public void fetchImagesInCollection(Context context, String str, final ServerFetcher.ResultsCallback<ImaxWallpaperProto$Image> resultsCallback) {
        ProtoRequest.Builder builder = new ProtoRequest.Builder();
        AnonymousClass2 r1 = new ProtoRequest.Callback<ImaxWallpaperProto$GetImagesInCollectionResponse>(this) { // from class: com.google.android.apps.wallpaper.backdrop.BackdropFetcher.2
            @Override // com.android.volley.Response.ErrorListener
            public void onErrorResponse(VolleyError volleyError) {
                resultsCallback.onError(volleyError);
            }

            @Override // com.android.volley.Response.Listener
            public void onResponse(Object obj) {
                resultsCallback.onSuccess(((ImaxWallpaperProto$GetImagesInCollectionResponse) obj).getImagesList());
            }
        };
        ImaxWallpaperProto$GetImagesInCollectionRequest.Builder newBuilder = ImaxWallpaperProto$GetImagesInCollectionRequest.newBuilder();
        newBuilder.copyOnWrite();
        ImaxWallpaperProto$GetImagesInCollectionRequest.access$6800((ImaxWallpaperProto$GetImagesInCollectionRequest) newBuilder.instance, str);
        String language = getLanguage();
        newBuilder.copyOnWrite();
        ImaxWallpaperProto$GetImagesInCollectionRequest.access$7100((ImaxWallpaperProto$GetImagesInCollectionRequest) newBuilder.instance, language);
        List<String> filteringLabelList = getFilteringLabelList(context);
        newBuilder.copyOnWrite();
        ImaxWallpaperProto$GetImagesInCollectionRequest.access$7900((ImaxWallpaperProto$GetImagesInCollectionRequest) newBuilder.instance, filteringLabelList);
        builder.url = "https://clients3.google.com/cast/chromecast/home/wallpaper/collection-images?rt=b";
        builder.requestMethod = 1;
        builder.requestBody = newBuilder.build();
        builder.callback = r1;
        builder.responseParser = ImaxWallpaperProto$GetImagesInCollectionResponse.parser();
        builder.headers.put("Accept", "application/x-protobuf");
        this.mRequester.addToRequestQueue(new ProtoRequest(builder));
    }

    public void fetchNextImageInCollection(Context context, String str, String str2, final ServerFetcher.NextImageInCollectionCallback nextImageInCollectionCallback) {
        ProtoRequest.Builder builder = new ProtoRequest.Builder();
        AnonymousClass3 r1 = new ProtoRequest.Callback<ImaxWallpaperProto$GetImageFromCollectionResponse>(this) { // from class: com.google.android.apps.wallpaper.backdrop.BackdropFetcher.3
            @Override // com.android.volley.Response.ErrorListener
            public void onErrorResponse(VolleyError volleyError) {
                nextImageInCollectionCallback.onError(volleyError);
            }

            @Override // com.android.volley.Response.Listener
            public void onResponse(Object obj) {
                ImaxWallpaperProto$GetImageFromCollectionResponse imaxWallpaperProto$GetImageFromCollectionResponse = (ImaxWallpaperProto$GetImageFromCollectionResponse) obj;
                nextImageInCollectionCallback.onSuccess(imaxWallpaperProto$GetImageFromCollectionResponse.getImage(), imaxWallpaperProto$GetImageFromCollectionResponse.getResumeToken());
            }
        };
        ImaxWallpaperProto$GetImageFromCollectionRequest.Builder newBuilder = ImaxWallpaperProto$GetImageFromCollectionRequest.newBuilder();
        newBuilder.copyOnWrite();
        ImaxWallpaperProto$GetImageFromCollectionRequest.access$9600((ImaxWallpaperProto$GetImageFromCollectionRequest) newBuilder.instance, str);
        if (str2 != null) {
            newBuilder.copyOnWrite();
            ImaxWallpaperProto$GetImageFromCollectionRequest.access$10000((ImaxWallpaperProto$GetImageFromCollectionRequest) newBuilder.instance, str2);
        }
        String language = getLanguage();
        newBuilder.copyOnWrite();
        ImaxWallpaperProto$GetImageFromCollectionRequest.access$10300((ImaxWallpaperProto$GetImageFromCollectionRequest) newBuilder.instance, language);
        List<String> filteringLabelList = getFilteringLabelList(context);
        newBuilder.copyOnWrite();
        ImaxWallpaperProto$GetImageFromCollectionRequest.access$11100((ImaxWallpaperProto$GetImageFromCollectionRequest) newBuilder.instance, filteringLabelList);
        builder.url = "https://clients3.google.com/cast/chromecast/home/wallpaper/image?rt=b";
        builder.requestMethod = 1;
        builder.requestBody = newBuilder.build();
        builder.callback = r1;
        builder.responseParser = ImaxWallpaperProto$GetImageFromCollectionResponse.parser();
        builder.headers.put("Accept", "application/x-protobuf");
        ProtoRequest protoRequest = new ProtoRequest(builder);
        protoRequest.mRetryPolicy = new DefaultRetryPolicy(2500, 3, 2.0f);
        this.mRequester.addToRequestQueue(protoRequest);
    }

    public final List<String> getFilteringLabelList(Context context) {
        Collection collection;
        ArrayList arrayList = new ArrayList();
        arrayList.add("update1");
        PackageManager packageManager = context.getPackageManager();
        if (packageManager.hasSystemFeature("com.google.android.feature.PIXEL_EXPERIENCE")) {
            arrayList.add("nexus");
        }
        if (packageManager.hasSystemFeature("com.google.android.feature.PIXEL_2017_EXPERIENCE")) {
            arrayList.add("pixel_2017");
        }
        arrayList.add(Build.MODEL);
        String str = Build.DEVICE;
        arrayList.add(str);
        String str2 = "android-sdk-" + Build.VERSION.SDK_INT;
        arrayList.add(str2);
        arrayList.add(str + "." + str2);
        Objects.requireNonNull((DeviceConfigFilteringLabelProvider) this.mLabelProvider);
        try {
            DeviceConfig.Properties properties = DeviceConfig.getProperties("wallpaper_content", new String[0]);
            collection = (Set) properties.getKeyset().stream().filter(new GridFragment$$ExternalSyntheticLambda1(properties)).collect(Collectors.toSet());
        } catch (Exception e) {
            Log.w("DeviceConfigFiltering", "Couldn't access DeviceConfig properties", e);
            collection = Sets.newHashSetWithExpectedSize(1);
            Collections.addAll(collection, "notargeting");
        }
        arrayList.addAll(collection);
        return arrayList;
    }

    public final String getLanguage() {
        String language = Locale.getDefault().getLanguage();
        String country = Locale.getDefault().getCountry();
        return !country.isEmpty() ? PathParser$$ExternalSyntheticOutline0.m(language, "-", country) : language;
    }
}
