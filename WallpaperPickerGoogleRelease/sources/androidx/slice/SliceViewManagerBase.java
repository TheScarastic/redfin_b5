package androidx.slice;

import android.app.slice.SliceManager;
import android.content.ContentProviderClient;
import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.ArrayMap;
import android.util.Pair;
import androidx.slice.SliceViewManager;
import androidx.slice.widget.SliceLiveData;
import com.android.wallpaper.widget.PreviewPager$$ExternalSyntheticLambda1;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public abstract class SliceViewManagerBase extends SliceViewManager {
    public final Context mContext;
    public final ArrayMap<Pair<Uri, SliceViewManager.SliceCallback>, SliceListenerImpl> mListenerLookup = new ArrayMap<>();

    /* loaded from: classes.dex */
    public class SliceListenerImpl {
        public final SliceViewManager.SliceCallback mCallback;
        public final Executor mExecutor;
        public boolean mPinned;
        public Uri mUri;
        public final Runnable mUpdateSlice = new Runnable() { // from class: androidx.slice.SliceViewManagerBase.SliceListenerImpl.1
            @Override // java.lang.Runnable
            public void run() {
                SliceListenerImpl sliceListenerImpl = SliceListenerImpl.this;
                if (!sliceListenerImpl.mPinned) {
                    try {
                        SliceViewManagerBase.this.pinSlice(sliceListenerImpl.mUri);
                        sliceListenerImpl.mPinned = true;
                    } catch (SecurityException unused) {
                    }
                }
                SliceListenerImpl sliceListenerImpl2 = SliceListenerImpl.this;
                Context context = SliceViewManagerBase.this.mContext;
                final Slice wrap = SliceConvert.wrap(((SliceManager) context.getSystemService(SliceManager.class)).bindSlice(sliceListenerImpl2.mUri, SliceConvert.unwrap(SliceLiveData.SUPPORTED_SPECS)), context);
                SliceListenerImpl.this.mExecutor.execute(new Runnable() { // from class: androidx.slice.SliceViewManagerBase.SliceListenerImpl.1.1
                    @Override // java.lang.Runnable
                    public void run() {
                        SliceViewManager.SliceCallback sliceCallback = SliceListenerImpl.this.mCallback;
                        Slice slice = wrap;
                        int i = SliceLiveData.SliceLiveDataImpl.$r8$clinit;
                        ((SliceLiveData.SliceLiveDataImpl) ((PreviewPager$$ExternalSyntheticLambda1) sliceCallback).f$0).postValue(slice);
                    }
                });
            }
        };
        public final ContentObserver mObserver = new ContentObserver(new Handler(Looper.getMainLooper())) { // from class: androidx.slice.SliceViewManagerBase.SliceListenerImpl.2
            @Override // android.database.ContentObserver
            public void onChange(boolean z) {
                AsyncTask.execute(SliceListenerImpl.this.mUpdateSlice);
            }
        };

        public SliceListenerImpl(Uri uri, Executor executor, SliceViewManager.SliceCallback sliceCallback) {
            this.mUri = uri;
            this.mExecutor = executor;
            this.mCallback = sliceCallback;
        }

        public void stopListening() {
            SliceViewManagerBase.this.mContext.getContentResolver().unregisterContentObserver(this.mObserver);
            if (this.mPinned) {
                SliceViewManagerBase.this.unpinSlice(this.mUri);
                this.mPinned = false;
            }
        }
    }

    public SliceViewManagerBase(Context context) {
        this.mContext = context;
    }

    @Override // androidx.slice.SliceViewManager
    public void registerSliceCallback(Uri uri, SliceViewManager.SliceCallback sliceCallback) {
        final Handler handler = new Handler(Looper.getMainLooper());
        SliceListenerImpl sliceListenerImpl = new SliceListenerImpl(uri, new Executor(this) { // from class: androidx.slice.SliceViewManagerBase.1
            @Override // java.util.concurrent.Executor
            public void execute(Runnable runnable) {
                handler.post(runnable);
            }
        }, sliceCallback);
        Pair<Uri, SliceViewManager.SliceCallback> pair = new Pair<>(uri, sliceCallback);
        synchronized (this.mListenerLookup) {
            SliceListenerImpl put = this.mListenerLookup.put(pair, sliceListenerImpl);
            if (put != null) {
                put.stopListening();
            }
        }
        ContentProviderClient acquireContentProviderClient = this.mContext.getContentResolver().acquireContentProviderClient(sliceListenerImpl.mUri);
        if (acquireContentProviderClient != null) {
            acquireContentProviderClient.release();
            this.mContext.getContentResolver().registerContentObserver(sliceListenerImpl.mUri, true, sliceListenerImpl.mObserver);
            if (!sliceListenerImpl.mPinned) {
                try {
                    pinSlice(sliceListenerImpl.mUri);
                    sliceListenerImpl.mPinned = true;
                } catch (SecurityException unused) {
                }
            }
        }
    }
}
