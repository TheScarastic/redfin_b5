package com.android.customization.model.grid;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.AsyncTask;
import com.android.customization.model.CustomizationManager;
import com.android.customization.module.CustomizationInjector;
import com.android.customization.module.ThemesUserEventLogger;
import com.android.systemui.shared.R;
import com.android.wallpaper.module.InjectorProvider;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public class GridOptionsManager implements CustomizationManager<GridOption> {
    public static GridOptionsManager sGridOptionsManager;
    public final ThemesUserEventLogger mEventLogger;
    public final LauncherGridOptionsProvider mProvider;

    /* loaded from: classes.dex */
    public static class FetchTask extends AsyncTask<Void, Void, List<GridOption>> {
        public final CustomizationManager.OptionsFetchedListener<GridOption> mCallback;
        public final LauncherGridOptionsProvider mProvider;
        public final boolean mReload;

        public FetchTask(LauncherGridOptionsProvider launcherGridOptionsProvider, CustomizationManager.OptionsFetchedListener optionsFetchedListener, boolean z, AnonymousClass1 r4) {
            this.mCallback = optionsFetchedListener;
            this.mProvider = launcherGridOptionsProvider;
            this.mReload = z;
        }

        /* Return type fixed from 'java.lang.Object' to match base method */
        /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object[]] */
        @Override // android.os.AsyncTask
        public List<GridOption> doInBackground(Void[] voidArr) {
            LauncherGridOptionsProvider launcherGridOptionsProvider = this.mProvider;
            boolean z = this.mReload;
            char c = 1;
            if (!(launcherGridOptionsProvider.mPreviewUtils.mProviderInfo != null)) {
                return null;
            }
            List<GridOption> list = launcherGridOptionsProvider.mOptions;
            if (list != null && !z) {
                return list;
            }
            ContentResolver contentResolver = launcherGridOptionsProvider.mContext.getContentResolver();
            String string = launcherGridOptionsProvider.mContext.getResources().getString(Resources.getSystem().getIdentifier("config_icon_mask", "string", "android"));
            try {
                Cursor query = contentResolver.query(launcherGridOptionsProvider.mPreviewUtils.getUri("list_options"), null, null, null, null);
                launcherGridOptionsProvider.mOptions = new ArrayList();
                while (query.moveToNext()) {
                    String string2 = query.getString(query.getColumnIndex("name"));
                    int i = query.getInt(query.getColumnIndex("rows"));
                    int i2 = query.getInt(query.getColumnIndex("cols"));
                    int i3 = query.getInt(query.getColumnIndex("preview_count"));
                    boolean parseBoolean = Boolean.parseBoolean(query.getString(query.getColumnIndex("is_default")));
                    Context context = launcherGridOptionsProvider.mContext;
                    Object[] objArr = new Object[2];
                    objArr[0] = Integer.valueOf(i2);
                    objArr[c] = Integer.valueOf(i);
                    launcherGridOptionsProvider.mOptions.add(new GridOption(context.getString(R.string.grid_title_pattern, objArr), string2, parseBoolean, i, i2, launcherGridOptionsProvider.mPreviewUtils.getUri("preview"), i3, string));
                    c = 1;
                }
                query.close();
            } catch (Exception unused) {
                launcherGridOptionsProvider.mOptions = null;
            }
            return launcherGridOptionsProvider.mOptions;
        }

        @Override // android.os.AsyncTask
        public void onCancelled() {
            super.onCancelled();
            CustomizationManager.OptionsFetchedListener<GridOption> optionsFetchedListener = this.mCallback;
            if (optionsFetchedListener != null) {
                optionsFetchedListener.onError(null);
            }
        }

        /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object] */
        @Override // android.os.AsyncTask
        public void onPostExecute(List<GridOption> list) {
            List<GridOption> list2 = list;
            if (this.mCallback == null) {
                return;
            }
            if (list2 == null || list2.isEmpty()) {
                this.mCallback.onError(null);
            } else {
                this.mCallback.onOptionsLoaded(list2);
            }
        }
    }

    public GridOptionsManager(LauncherGridOptionsProvider launcherGridOptionsProvider, ThemesUserEventLogger themesUserEventLogger) {
        this.mProvider = launcherGridOptionsProvider;
        this.mEventLogger = themesUserEventLogger;
    }

    public static GridOptionsManager getInstance(Context context) {
        if (sGridOptionsManager == null) {
            Context applicationContext = context.getApplicationContext();
            sGridOptionsManager = new GridOptionsManager(new LauncherGridOptionsProvider(applicationContext, applicationContext.getString(R.string.grid_control_metadata_name)), (ThemesUserEventLogger) ((CustomizationInjector) InjectorProvider.getInjector()).getUserEventLogger(applicationContext));
        }
        return sGridOptionsManager;
    }
}
