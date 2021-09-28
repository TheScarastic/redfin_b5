package androidx.lifecycle;

import android.os.Binder;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.media.ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0;
import android.util.Size;
import android.util.SizeF;
import android.util.SparseArray;
import androidx.savedstate.SavedStateRegistry;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
/* loaded from: classes.dex */
public final class SavedStateHandle {
    public static final Class[] ACCEPTABLE_CLASSES = {Boolean.TYPE, boolean[].class, Double.TYPE, double[].class, Integer.TYPE, int[].class, Long.TYPE, long[].class, String.class, String[].class, Binder.class, Bundle.class, Byte.TYPE, byte[].class, Character.TYPE, char[].class, CharSequence.class, CharSequence[].class, ArrayList.class, Float.TYPE, float[].class, Parcelable.class, Parcelable[].class, Serializable.class, Short.TYPE, short[].class, SparseArray.class, Size.class, SizeF.class};
    public final Map<String, ?> mLiveDatas;
    public final Map<String, Object> mRegular;
    public final SavedStateRegistry.SavedStateProvider mSavedStateProvider;
    public final Map<String, SavedStateRegistry.SavedStateProvider> mSavedStateProviders;

    public SavedStateHandle(Map<String, Object> map) {
        this.mSavedStateProviders = new HashMap();
        this.mLiveDatas = new HashMap();
        this.mSavedStateProvider = new SavedStateRegistry.SavedStateProvider() { // from class: androidx.lifecycle.SavedStateHandle.1
            @Override // androidx.savedstate.SavedStateRegistry.SavedStateProvider
            public Bundle saveState() {
                for (Map.Entry entry : new HashMap(SavedStateHandle.this.mSavedStateProviders).entrySet()) {
                    Bundle saveState = ((SavedStateRegistry.SavedStateProvider) entry.getValue()).saveState();
                    SavedStateHandle savedStateHandle = SavedStateHandle.this;
                    String str = (String) entry.getKey();
                    Objects.requireNonNull(savedStateHandle);
                    if (saveState != null) {
                        for (Class cls : SavedStateHandle.ACCEPTABLE_CLASSES) {
                            if (!cls.isInstance(saveState)) {
                            }
                        }
                        StringBuilder m = ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0.m("Can't put value with type ");
                        m.append(saveState.getClass());
                        m.append(" into saved state");
                        throw new IllegalArgumentException(m.toString());
                    }
                    MutableLiveData mutableLiveData = (MutableLiveData) savedStateHandle.mLiveDatas.get(str);
                    if (mutableLiveData != null) {
                        mutableLiveData.setValue(saveState);
                    } else {
                        savedStateHandle.mRegular.put(str, saveState);
                    }
                }
                Set<String> keySet = SavedStateHandle.this.mRegular.keySet();
                ArrayList<? extends Parcelable> arrayList = new ArrayList<>(keySet.size());
                ArrayList<? extends Parcelable> arrayList2 = new ArrayList<>(arrayList.size());
                for (String str2 : keySet) {
                    arrayList.add(str2);
                    arrayList2.add(SavedStateHandle.this.mRegular.get(str2));
                }
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("keys", arrayList);
                bundle.putParcelableArrayList("values", arrayList2);
                return bundle;
            }
        };
        this.mRegular = new HashMap(map);
    }

    public SavedStateHandle() {
        this.mSavedStateProviders = new HashMap();
        this.mLiveDatas = new HashMap();
        this.mSavedStateProvider = new SavedStateRegistry.SavedStateProvider() { // from class: androidx.lifecycle.SavedStateHandle.1
            @Override // androidx.savedstate.SavedStateRegistry.SavedStateProvider
            public Bundle saveState() {
                for (Map.Entry entry : new HashMap(SavedStateHandle.this.mSavedStateProviders).entrySet()) {
                    Bundle saveState = ((SavedStateRegistry.SavedStateProvider) entry.getValue()).saveState();
                    SavedStateHandle savedStateHandle = SavedStateHandle.this;
                    String str = (String) entry.getKey();
                    Objects.requireNonNull(savedStateHandle);
                    if (saveState != null) {
                        for (Class cls : SavedStateHandle.ACCEPTABLE_CLASSES) {
                            if (!cls.isInstance(saveState)) {
                            }
                        }
                        StringBuilder m = ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0.m("Can't put value with type ");
                        m.append(saveState.getClass());
                        m.append(" into saved state");
                        throw new IllegalArgumentException(m.toString());
                    }
                    MutableLiveData mutableLiveData = (MutableLiveData) savedStateHandle.mLiveDatas.get(str);
                    if (mutableLiveData != null) {
                        mutableLiveData.setValue(saveState);
                    } else {
                        savedStateHandle.mRegular.put(str, saveState);
                    }
                }
                Set<String> keySet = SavedStateHandle.this.mRegular.keySet();
                ArrayList<? extends Parcelable> arrayList = new ArrayList<>(keySet.size());
                ArrayList<? extends Parcelable> arrayList2 = new ArrayList<>(arrayList.size());
                for (String str2 : keySet) {
                    arrayList.add(str2);
                    arrayList2.add(SavedStateHandle.this.mRegular.get(str2));
                }
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("keys", arrayList);
                bundle.putParcelableArrayList("values", arrayList2);
                return bundle;
            }
        };
        this.mRegular = new HashMap();
    }
}
