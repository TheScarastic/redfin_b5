package androidx.loader.app;

import android.util.Log;
import androidx.appcompat.view.SupportMenuInflater$$ExternalSyntheticOutline0;
import androidx.collection.SparseArrayCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Objects;
/* loaded from: classes.dex */
public class LoaderManagerImpl extends LoaderManager {
    public final LifecycleOwner mLifecycleOwner;
    public final LoaderViewModel mLoaderViewModel;

    /* loaded from: classes.dex */
    public static class LoaderInfo<D> extends MutableLiveData<D> {
        @Override // androidx.lifecycle.LiveData
        public void onActive() {
            if (LoaderManagerImpl.isLoggingEnabled(2)) {
                Log.v("LoaderManager", "  Starting: " + this);
            }
            throw null;
        }

        @Override // androidx.lifecycle.LiveData
        public void onInactive() {
            if (LoaderManagerImpl.isLoggingEnabled(2)) {
                Log.v("LoaderManager", "  Stopping: " + this);
            }
            throw null;
        }

        /* JADX DEBUG: Multi-variable search result rejected for r1v0, resolved type: androidx.lifecycle.Observer<? super D> */
        /* JADX WARN: Multi-variable type inference failed */
        @Override // androidx.lifecycle.LiveData
        public void removeObserver(Observer<? super D> observer) {
            super.removeObserver(observer);
        }

        @Override // androidx.lifecycle.MutableLiveData, androidx.lifecycle.LiveData
        public void setValue(D d) {
            super.setValue(d);
        }

        public String toString() {
            StringBuilder sb = new StringBuilder(64);
            sb.append("LoaderInfo{");
            sb.append(Integer.toHexString(System.identityHashCode(this)));
            sb.append(" #");
            sb.append(0);
            sb.append(" : ");
            throw null;
        }
    }

    /* loaded from: classes.dex */
    public static class LoaderViewModel extends ViewModel {
        public static final ViewModelProvider.Factory FACTORY = new ViewModelProvider.Factory() { // from class: androidx.loader.app.LoaderManagerImpl.LoaderViewModel.1
            @Override // androidx.lifecycle.ViewModelProvider.Factory
            public <T extends ViewModel> T create(Class<T> cls) {
                return new LoaderViewModel();
            }
        };
        public SparseArrayCompat<LoaderInfo> mLoaders = new SparseArrayCompat<>();

        @Override // androidx.lifecycle.ViewModel
        public void onCleared() {
            SparseArrayCompat<LoaderInfo> sparseArrayCompat = this.mLoaders;
            int i = sparseArrayCompat.mSize;
            if (i > 0) {
                LoaderInfo loaderInfo = (LoaderInfo) sparseArrayCompat.mValues[0];
                Objects.requireNonNull(loaderInfo);
                if (LoaderManagerImpl.isLoggingEnabled(3)) {
                    Log.d("LoaderManager", "  Destroying: " + loaderInfo);
                }
                throw null;
            }
            Object[] objArr = sparseArrayCompat.mValues;
            for (int i2 = 0; i2 < i; i2++) {
                objArr[i2] = null;
            }
            sparseArrayCompat.mSize = 0;
        }
    }

    public LoaderManagerImpl(LifecycleOwner lifecycleOwner, ViewModelStore viewModelStore) {
        ViewModel viewModel;
        this.mLifecycleOwner = lifecycleOwner;
        ViewModelProvider.Factory factory = LoaderViewModel.FACTORY;
        String canonicalName = LoaderViewModel.class.getCanonicalName();
        if (canonicalName != null) {
            String m = SupportMenuInflater$$ExternalSyntheticOutline0.m("androidx.lifecycle.ViewModelProvider.DefaultKey:", canonicalName);
            ViewModel viewModel2 = viewModelStore.mMap.get(m);
            if (!LoaderViewModel.class.isInstance(viewModel2)) {
                if (factory instanceof ViewModelProvider.KeyedFactory) {
                    viewModel = ((ViewModelProvider.KeyedFactory) factory).create(m, LoaderViewModel.class);
                } else {
                    viewModel = ((LoaderViewModel.AnonymousClass1) factory).create(LoaderViewModel.class);
                }
                viewModel2 = viewModel;
                ViewModel put = viewModelStore.mMap.put(m, viewModel2);
                if (put != null) {
                    put.onCleared();
                }
            } else if (factory instanceof ViewModelProvider.OnRequeryFactory) {
                ((ViewModelProvider.OnRequeryFactory) factory).onRequery(viewModel2);
            }
            this.mLoaderViewModel = (LoaderViewModel) viewModel2;
            return;
        }
        throw new IllegalArgumentException("Local and anonymous classes can not be ViewModels");
    }

    public static boolean isLoggingEnabled(int i) {
        return Log.isLoggable("LoaderManager", i);
    }

    @Override // androidx.loader.app.LoaderManager
    @Deprecated
    public void dump(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        LoaderViewModel loaderViewModel = this.mLoaderViewModel;
        if (loaderViewModel.mLoaders.mSize > 0) {
            printWriter.print(str);
            printWriter.println("Loaders:");
            SparseArrayCompat<LoaderInfo> sparseArrayCompat = loaderViewModel.mLoaders;
            if (sparseArrayCompat.mSize > 0) {
                printWriter.print(str);
                printWriter.print("  #");
                printWriter.print(loaderViewModel.mLoaders.mKeys[0]);
                printWriter.print(": ");
                ((LoaderInfo) sparseArrayCompat.mValues[0]).toString();
                throw null;
            }
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(128);
        sb.append("LoaderManager{");
        sb.append(Integer.toHexString(System.identityHashCode(this)));
        sb.append(" in ");
        sb.append(this.mLifecycleOwner.getClass().getSimpleName());
        sb.append("{");
        sb.append(Integer.toHexString(System.identityHashCode(this.mLifecycleOwner)));
        sb.append("}}");
        return sb.toString();
    }
}
