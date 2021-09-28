package androidx.lifecycle;

import android.app.Application;
import androidx.appcompat.view.SupportMenuInflater$$ExternalSyntheticOutline0;
import java.lang.reflect.InvocationTargetException;
/* loaded from: classes.dex */
public class ViewModelProvider {
    public final Factory mFactory;
    public final ViewModelStore mViewModelStore;

    /* loaded from: classes.dex */
    public static class AndroidViewModelFactory extends NewInstanceFactory {
        public static AndroidViewModelFactory sInstance;
        public Application mApplication;

        public AndroidViewModelFactory(Application application) {
            this.mApplication = application;
        }

        @Override // androidx.lifecycle.ViewModelProvider.NewInstanceFactory, androidx.lifecycle.ViewModelProvider.Factory
        public <T extends ViewModel> T create(Class<T> cls) {
            if (!AndroidViewModel.class.isAssignableFrom(cls)) {
                return (T) super.create(cls);
            }
            try {
                return cls.getConstructor(Application.class).newInstance(this.mApplication);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Cannot create an instance of " + cls, e);
            } catch (InstantiationException e2) {
                throw new RuntimeException("Cannot create an instance of " + cls, e2);
            } catch (NoSuchMethodException e3) {
                throw new RuntimeException("Cannot create an instance of " + cls, e3);
            } catch (InvocationTargetException e4) {
                throw new RuntimeException("Cannot create an instance of " + cls, e4);
            }
        }
    }

    /* loaded from: classes.dex */
    public interface Factory {
        <T extends ViewModel> T create(Class<T> cls);
    }

    /* loaded from: classes.dex */
    public static abstract class KeyedFactory extends OnRequeryFactory implements Factory {
        @Override // androidx.lifecycle.ViewModelProvider.Factory
        public <T extends ViewModel> T create(Class<T> cls) {
            throw new UnsupportedOperationException("create(String, Class<?>) must be called on implementaions of KeyedFactory");
        }

        public abstract <T extends ViewModel> T create(String str, Class<T> cls);
    }

    /* loaded from: classes.dex */
    public static class NewInstanceFactory implements Factory {
        public static NewInstanceFactory sInstance;

        @Override // androidx.lifecycle.ViewModelProvider.Factory
        public <T extends ViewModel> T create(Class<T> cls) {
            try {
                return cls.newInstance();
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Cannot create an instance of " + cls, e);
            } catch (InstantiationException e2) {
                throw new RuntimeException("Cannot create an instance of " + cls, e2);
            }
        }
    }

    /* loaded from: classes.dex */
    public static class OnRequeryFactory {
        public void onRequery(ViewModel viewModel) {
        }
    }

    /* JADX WARNING: Illegal instructions before constructor call */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public ViewModelProvider(androidx.lifecycle.ViewModelStoreOwner r2) {
        /*
            r1 = this;
            androidx.activity.ComponentActivity r2 = (androidx.activity.ComponentActivity) r2
            androidx.lifecycle.ViewModelStore r0 = r2.getViewModelStore()
            androidx.lifecycle.ViewModelProvider$Factory r2 = r2.getDefaultViewModelProviderFactory()
            r1.<init>(r0, r2)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.lifecycle.ViewModelProvider.<init>(androidx.lifecycle.ViewModelStoreOwner):void");
    }

    public <T extends ViewModel> T get(Class<T> cls) {
        Object obj;
        String canonicalName = cls.getCanonicalName();
        if (canonicalName != null) {
            String m = SupportMenuInflater$$ExternalSyntheticOutline0.m("androidx.lifecycle.ViewModelProvider.DefaultKey:", canonicalName);
            T t = (T) this.mViewModelStore.mMap.get(m);
            if (cls.isInstance(t)) {
                Factory factory = this.mFactory;
                if (factory instanceof OnRequeryFactory) {
                    ((OnRequeryFactory) factory).onRequery(t);
                }
            } else {
                Factory factory2 = this.mFactory;
                if (factory2 instanceof KeyedFactory) {
                    obj = ((KeyedFactory) factory2).create(m, cls);
                } else {
                    obj = factory2.create(cls);
                }
                t = (T) obj;
                ViewModel put = this.mViewModelStore.mMap.put(m, t);
                if (put != null) {
                    put.onCleared();
                }
            }
            return t;
        }
        throw new IllegalArgumentException("Local and anonymous classes can not be ViewModels");
    }

    public ViewModelProvider(ViewModelStore viewModelStore, Factory factory) {
        this.mFactory = factory;
        this.mViewModelStore = viewModelStore;
    }
}
