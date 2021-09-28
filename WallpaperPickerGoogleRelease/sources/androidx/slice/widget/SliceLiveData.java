package androidx.slice.widget;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;
import androidx.collection.ArraySet;
import androidx.lifecycle.LiveData;
import androidx.slice.Slice;
import androidx.slice.SliceSpec;
import androidx.slice.SliceSpecs;
import androidx.slice.SliceViewManager;
import androidx.slice.SliceViewManagerBase;
import androidx.slice.SliceViewManagerWrapper;
import com.android.wallpaper.widget.PreviewPager$$ExternalSyntheticLambda1;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
/* loaded from: classes.dex */
public final class SliceLiveData {
    public static final Set<SliceSpec> SUPPORTED_SPECS;

    /* loaded from: classes.dex */
    public interface OnErrorListener {
        void onSliceError(int i, Throwable th);
    }

    /* loaded from: classes.dex */
    public static class SliceLiveDataImpl extends LiveData<Slice> {
        public static final /* synthetic */ int $r8$clinit = 0;
        public final SliceViewManager mSliceViewManager;
        public Uri mUri;
        public final Runnable mUpdateSlice = new Runnable() { // from class: androidx.slice.widget.SliceLiveData.SliceLiveDataImpl.1
            @Override // java.lang.Runnable
            public void run() {
                Slice slice;
                try {
                    SliceLiveDataImpl sliceLiveDataImpl = SliceLiveDataImpl.this;
                    Uri uri = sliceLiveDataImpl.mUri;
                    if (uri != null) {
                        slice = sliceLiveDataImpl.mSliceViewManager.bindSlice(uri);
                    } else {
                        slice = sliceLiveDataImpl.mSliceViewManager.bindSlice((Intent) null);
                    }
                    SliceLiveDataImpl sliceLiveDataImpl2 = SliceLiveDataImpl.this;
                    if (sliceLiveDataImpl2.mUri == null && slice != null) {
                        sliceLiveDataImpl2.mUri = slice.getUri();
                        SliceLiveDataImpl sliceLiveDataImpl3 = SliceLiveDataImpl.this;
                        sliceLiveDataImpl3.mSliceViewManager.registerSliceCallback(sliceLiveDataImpl3.mUri, sliceLiveDataImpl3.mSliceCallback);
                    }
                    SliceLiveDataImpl.this.postValue(slice);
                } catch (IllegalArgumentException e) {
                    OnErrorListener onErrorListener = SliceLiveDataImpl.this.mListener;
                    if (onErrorListener != null) {
                        onErrorListener.onSliceError(3, e);
                    } else {
                        Log.e("SliceLiveData", "Error binding slice", e);
                    }
                    SliceLiveDataImpl.this.postValue(null);
                } catch (Exception e2) {
                    OnErrorListener onErrorListener2 = SliceLiveDataImpl.this.mListener;
                    if (onErrorListener2 != null) {
                        onErrorListener2.onSliceError(0, e2);
                    } else {
                        Log.e("SliceLiveData", "Error binding slice", e2);
                    }
                    SliceLiveDataImpl.this.postValue(null);
                }
            }
        };
        public final SliceViewManager.SliceCallback mSliceCallback = new PreviewPager$$ExternalSyntheticLambda1(this);
        public final OnErrorListener mListener = null;

        public SliceLiveDataImpl(Context context, Uri uri, OnErrorListener onErrorListener) {
            this.mSliceViewManager = new SliceViewManagerWrapper(context);
            this.mUri = uri;
        }

        @Override // androidx.lifecycle.LiveData
        public void onActive() {
            AsyncTask.execute(this.mUpdateSlice);
            Uri uri = this.mUri;
            if (uri != null) {
                this.mSliceViewManager.registerSliceCallback(uri, this.mSliceCallback);
            }
        }

        @Override // androidx.lifecycle.LiveData
        public void onInactive() {
            Uri uri = this.mUri;
            if (uri != null) {
                SliceViewManager sliceViewManager = this.mSliceViewManager;
                SliceViewManager.SliceCallback sliceCallback = this.mSliceCallback;
                SliceViewManagerBase sliceViewManagerBase = (SliceViewManagerBase) sliceViewManager;
                synchronized (sliceViewManagerBase.mListenerLookup) {
                    SliceViewManagerBase.SliceListenerImpl remove = sliceViewManagerBase.mListenerLookup.remove(new Pair(uri, sliceCallback));
                    if (remove != null) {
                        remove.stopListening();
                    }
                }
            }
        }
    }

    static {
        List asList = Arrays.asList(SliceSpecs.BASIC, SliceSpecs.LIST, SliceSpecs.LIST_V2, new SliceSpec("androidx.app.slice.BASIC", 1), new SliceSpec("androidx.app.slice.LIST", 1));
        ArraySet arraySet = new ArraySet(0);
        if (asList != null) {
            arraySet.addAll(asList);
        }
        SUPPORTED_SPECS = arraySet;
    }
}
