package androidx.leanback.widget;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.SparseArray;
import android.view.View;
import androidx.collection.LruCache;
import java.util.Map;
/* access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class ViewsStateBundle {
    private LruCache<String, SparseArray<Parcelable>> mChildStates;
    private int mSavePolicy = 0;
    private int mLimitNumber = 100;

    public void clear() {
        LruCache<String, SparseArray<Parcelable>> lruCache = this.mChildStates;
        if (lruCache != null) {
            lruCache.evictAll();
        }
    }

    public void remove(int i) {
        LruCache<String, SparseArray<Parcelable>> lruCache = this.mChildStates;
        if (lruCache != null && lruCache.size() != 0) {
            this.mChildStates.remove(getSaveStatesKey(i));
        }
    }

    public final Bundle saveAsBundle() {
        LruCache<String, SparseArray<Parcelable>> lruCache = this.mChildStates;
        if (lruCache == null || lruCache.size() == 0) {
            return null;
        }
        Map<String, SparseArray<Parcelable>> snapshot = this.mChildStates.snapshot();
        Bundle bundle = new Bundle();
        for (Map.Entry<String, SparseArray<Parcelable>> entry : snapshot.entrySet()) {
            bundle.putSparseParcelableArray(entry.getKey(), entry.getValue());
        }
        return bundle;
    }

    public final void loadFromBundle(Bundle bundle) {
        LruCache<String, SparseArray<Parcelable>> lruCache = this.mChildStates;
        if (!(lruCache == null || bundle == null)) {
            lruCache.evictAll();
            for (String str : bundle.keySet()) {
                this.mChildStates.put(str, bundle.getSparseParcelableArray(str));
            }
        }
    }

    public final void loadView(View view, int i) {
        SparseArray<Parcelable> remove;
        if (this.mChildStates != null && (remove = this.mChildStates.remove(getSaveStatesKey(i))) != null) {
            view.restoreHierarchyState(remove);
        }
    }

    protected final void saveViewUnchecked(View view, int i) {
        if (this.mChildStates != null) {
            String saveStatesKey = getSaveStatesKey(i);
            SparseArray<Parcelable> sparseArray = new SparseArray<>();
            view.saveHierarchyState(sparseArray);
            this.mChildStates.put(saveStatesKey, sparseArray);
        }
    }

    public final Bundle saveOnScreenView(Bundle bundle, View view, int i) {
        if (this.mSavePolicy != 0) {
            String saveStatesKey = getSaveStatesKey(i);
            SparseArray<Parcelable> sparseArray = new SparseArray<>();
            view.saveHierarchyState(sparseArray);
            if (bundle == null) {
                bundle = new Bundle();
            }
            bundle.putSparseParcelableArray(saveStatesKey, sparseArray);
        }
        return bundle;
    }

    public final void saveOffscreenView(View view, int i) {
        int i2 = this.mSavePolicy;
        if (i2 == 1) {
            remove(i);
        } else if (i2 == 2 || i2 == 3) {
            saveViewUnchecked(view, i);
        }
    }

    static String getSaveStatesKey(int i) {
        return Integer.toString(i);
    }
}
