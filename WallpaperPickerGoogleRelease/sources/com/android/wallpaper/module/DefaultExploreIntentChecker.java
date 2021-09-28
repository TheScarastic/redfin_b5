package com.android.wallpaper.module;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import com.android.wallpaper.module.ExploreIntentChecker;
import java.util.HashMap;
import java.util.Map;
/* loaded from: classes.dex */
public class DefaultExploreIntentChecker implements ExploreIntentChecker {
    public Context mAppContext;
    public Map<Uri, Intent> mUriToActionViewIntentMap = new HashMap();

    /* loaded from: classes.dex */
    public class FetchActionViewIntentTask extends AsyncTask<Void, Void, Intent> {
        public Context mAppContext;
        public ExploreIntentChecker.IntentReceiver mReceiver;
        public Uri mUri;

        public FetchActionViewIntentTask(Context context, Uri uri, ExploreIntentChecker.IntentReceiver intentReceiver) {
            this.mAppContext = context;
            this.mUri = uri;
            this.mReceiver = intentReceiver;
        }

        /* Return type fixed from 'java.lang.Object' to match base method */
        /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object[]] */
        @Override // android.os.AsyncTask
        public Intent doInBackground(Void[] voidArr) {
            Intent intent = new Intent("android.intent.action.VIEW", this.mUri);
            if (this.mAppContext.getPackageManager().queryIntentActivities(intent, 0).isEmpty()) {
                intent = null;
            }
            DefaultExploreIntentChecker.this.mUriToActionViewIntentMap.put(this.mUri, intent);
            return intent;
        }

        /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object] */
        @Override // android.os.AsyncTask
        public void onPostExecute(Intent intent) {
            this.mReceiver.onIntentReceived(intent);
        }
    }

    public DefaultExploreIntentChecker(Context context) {
        this.mAppContext = context;
    }

    public void fetchValidActionViewIntent(Uri uri, ExploreIntentChecker.IntentReceiver intentReceiver) {
        if (this.mUriToActionViewIntentMap.containsKey(uri)) {
            intentReceiver.onIntentReceived(this.mUriToActionViewIntentMap.get(uri));
        } else {
            new FetchActionViewIntentTask(this.mAppContext, uri, intentReceiver).execute(new Void[0]);
        }
    }
}
