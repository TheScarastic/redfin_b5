package com.android.wallpaper.module;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import com.android.volley.VolleyError;
import com.android.wallpaper.model.Category;
import com.android.wallpaper.model.CategoryProvider;
import com.android.wallpaper.model.CategoryReceiver;
import com.android.wallpaper.network.ServerFetcher;
import com.google.android.apps.common.volley.request.ProtoRequest;
import com.google.android.apps.wallpaper.backdrop.BackdropCategory;
import com.google.android.apps.wallpaper.backdrop.BackdropFetcher;
import com.google.android.apps.wallpaper.module.WallpaperCategoryProvider;
import com.google.chrome.dongle.imax.wallpaper2.protos.ImaxWallpaperProto$Collection;
import com.google.chrome.dongle.imax.wallpaper2.protos.ImaxWallpaperProto$GetCollectionsRequest;
import com.google.chrome.dongle.imax.wallpaper2.protos.ImaxWallpaperProto$GetCollectionsResponse;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
/* loaded from: classes.dex */
public class DefaultCategoryProvider implements CategoryProvider {
    public static List<Category> sSystemCategories;
    public final Context mAppContext;
    public boolean mFetchedCategories;
    public Locale mLocale;
    public NetworkStatusNotifier mNetworkStatusNotifier;
    public ArrayList<Category> mCategories = new ArrayList<>();
    public int mNetworkStatus = -1;

    /* loaded from: classes.dex */
    public static class FetchCategoriesTask extends AsyncTask<Void, Category, Void> {
        public static final /* synthetic */ int $r8$clinit = 0;
        public final Context mAppContext;
        public PartnerProvider mPartnerProvider;
        public CategoryReceiver mReceiver;

        public FetchCategoriesTask(CategoryReceiver categoryReceiver, Context context) {
            this.mReceiver = categoryReceiver;
            this.mAppContext = context.getApplicationContext();
        }

        /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object] */
        @Override // android.os.AsyncTask
        public void onPostExecute(Void r1) {
            this.mReceiver.doneFetchingCategories();
        }

        /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object[]] */
        @Override // android.os.AsyncTask
        public void onProgressUpdate(Category[] categoryArr) {
            Category[] categoryArr2 = categoryArr;
            super.onProgressUpdate(categoryArr2);
            for (Category category : categoryArr2) {
                if (category != null) {
                    this.mReceiver.onCategoryReceived(category);
                }
            }
        }
    }

    public DefaultCategoryProvider(Context context) {
        this.mAppContext = context.getApplicationContext();
        this.mNetworkStatusNotifier = InjectorProvider.getInjector().getNetworkStatusNotifier(context);
    }

    public void fetchCategories(CategoryReceiver categoryReceiver, boolean z) {
        if (z || !this.mFetchedCategories) {
            if (z) {
                this.mCategories.clear();
                this.mFetchedCategories = false;
            }
            this.mNetworkStatus = ((DefaultNetworkStatusNotifier) this.mNetworkStatusNotifier).getNetworkStatus();
            this.mLocale = getLocale();
            WallpaperCategoryProvider wallpaperCategoryProvider = (WallpaperCategoryProvider) this;
            WallpaperCategoryProvider.AnonymousClass1 r3 = new CategoryReceiver(categoryReceiver, new int[]{0}) { // from class: com.google.android.apps.wallpaper.module.WallpaperCategoryProvider.1
                public final /* synthetic */ int[] val$numFetchesDone;
                public final /* synthetic */ CategoryReceiver val$receiver;

                {
                    this.val$receiver = r2;
                    this.val$numFetchesDone = r3;
                }

                @Override // com.android.wallpaper.model.CategoryReceiver
                public void doneFetchingCategories() {
                    int[] iArr = this.val$numFetchesDone;
                    iArr[0] = iArr[0] + 1;
                    if (iArr[0] == 2) {
                        this.val$receiver.doneFetchingCategories();
                    }
                    WallpaperCategoryProvider.this.mFetchedCategories = true;
                }

                @Override // com.android.wallpaper.model.CategoryReceiver
                public void onCategoryReceived(Category category) {
                    this.val$receiver.onCategoryReceived(category);
                    int indexOf = WallpaperCategoryProvider.this.mCategories.indexOf(category);
                    if (indexOf >= 0) {
                        WallpaperCategoryProvider.this.mCategories.set(indexOf, category);
                    } else {
                        WallpaperCategoryProvider.this.mCategories.add(category);
                    }
                }
            };
            new WallpaperCategoryProvider.GoogleFetchCategoriesTask(r3, wallpaperCategoryProvider.mAppContext, z, null).execute(new Void[0]);
            Context context = wallpaperCategoryProvider.mAppContext;
            Injector injector = InjectorProvider.getInjector();
            if (((DefaultNetworkStatusNotifier) injector.getNetworkStatusNotifier(context)).getNetworkStatus() == 0) {
                r3.doneFetchingCategories();
                return;
            }
            ServerFetcher serverFetcher = ((GoogleWallpapersInjector) injector).getServerFetcher(context);
            BackdropCategory.AnonymousClass1 r7 = new ServerFetcher.ResultsCallback<ImaxWallpaperProto$Collection>(201, r3, context) { // from class: com.google.android.apps.wallpaper.backdrop.BackdropCategory.1
                public final /* synthetic */ Context val$context;
                public final /* synthetic */ int val$priority;
                public final /* synthetic */ CategoryReceiver val$receiver;

                {
                    this.val$priority = r1;
                    this.val$receiver = r2;
                    this.val$context = r3;
                }

                @Override // com.android.wallpaper.network.ServerFetcher.ResultsCallback
                public void onError(VolleyError volleyError) {
                    Log.e("BackdropCategory", "Unable to fetch Backdrop wallpaper categories", volleyError);
                    this.val$receiver.doneFetchingCategories();
                }

                @Override // com.android.wallpaper.network.ServerFetcher.ResultsCallback
                public void onSuccess(List<ImaxWallpaperProto$Collection> list) {
                    for (ImaxWallpaperProto$Collection imaxWallpaperProto$Collection : list) {
                        BackdropCategory backdropCategory = new BackdropCategory(imaxWallpaperProto$Collection, this.val$priority);
                        this.val$receiver.onCategoryReceived(backdropCategory);
                        backdropCategory.fetchWallpapers(this.val$context, null, false);
                    }
                    this.val$receiver.doneFetchingCategories();
                }
            };
            BackdropFetcher backdropFetcher = (BackdropFetcher) serverFetcher;
            Objects.requireNonNull(backdropFetcher);
            ProtoRequest.Builder builder = new ProtoRequest.Builder();
            BackdropFetcher.AnonymousClass1 r2 = new ProtoRequest.Callback<ImaxWallpaperProto$GetCollectionsResponse>(backdropFetcher, r7) { // from class: com.google.android.apps.wallpaper.backdrop.BackdropFetcher.1
                public final /* synthetic */ ServerFetcher.ResultsCallback val$collectionsCallback;

                {
                    this.val$collectionsCallback = r2;
                }

                @Override // com.android.volley.Response.ErrorListener
                public void onErrorResponse(VolleyError volleyError) {
                    this.val$collectionsCallback.onError(volleyError);
                }

                @Override // com.android.volley.Response.Listener
                public void onResponse(Object obj) {
                    this.val$collectionsCallback.onSuccess(((ImaxWallpaperProto$GetCollectionsResponse) obj).getCollectionsList());
                }
            };
            ImaxWallpaperProto$GetCollectionsRequest.Builder newBuilder = ImaxWallpaperProto$GetCollectionsRequest.newBuilder();
            String language = backdropFetcher.getLanguage();
            newBuilder.copyOnWrite();
            ImaxWallpaperProto$GetCollectionsRequest.access$4400((ImaxWallpaperProto$GetCollectionsRequest) newBuilder.instance, language);
            List<String> filteringLabelList = backdropFetcher.getFilteringLabelList(context);
            newBuilder.copyOnWrite();
            ImaxWallpaperProto$GetCollectionsRequest.access$5200((ImaxWallpaperProto$GetCollectionsRequest) newBuilder.instance, filteringLabelList);
            builder.url = "https://clients3.google.com/cast/chromecast/home/wallpaper/collections?rt=b";
            builder.requestMethod = 1;
            builder.requestBody = newBuilder.build();
            builder.callback = r2;
            builder.responseParser = ImaxWallpaperProto$GetCollectionsResponse.parser();
            builder.headers.put("Accept", "application/x-protobuf");
            backdropFetcher.mRequester.addToRequestQueue(new ProtoRequest(builder));
            return;
        }
        Iterator<Category> it = this.mCategories.iterator();
        while (it.hasNext()) {
            categoryReceiver.onCategoryReceived(it.next());
        }
        categoryReceiver.doneFetchingCategories();
    }

    public Category getCategory(int i) {
        if (this.mFetchedCategories) {
            return this.mCategories.get(i);
        }
        throw new IllegalStateException("Categories are not available");
    }

    public final Locale getLocale() {
        return this.mAppContext.getResources().getConfiguration().getLocales().get(0);
    }

    public boolean resetIfNeeded() {
        if (this.mNetworkStatus == ((DefaultNetworkStatusNotifier) this.mNetworkStatusNotifier).getNetworkStatus() && this.mLocale == getLocale()) {
            return false;
        }
        this.mCategories.clear();
        this.mFetchedCategories = false;
        return true;
    }

    public Category getCategory(String str) {
        for (int i = 0; i < this.mCategories.size(); i++) {
            Category category = this.mCategories.get(i);
            if (category.mCollectionId.equals(str)) {
                return category;
            }
        }
        return null;
    }
}
