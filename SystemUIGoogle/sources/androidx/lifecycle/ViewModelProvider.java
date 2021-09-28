package androidx.lifecycle;
/* loaded from: classes.dex */
public class ViewModelProvider {
    private final Factory mFactory;
    private final ViewModelStore mViewModelStore;

    /* loaded from: classes.dex */
    public interface Factory {
        <T extends ViewModel> T create(Class<T> cls);
    }

    /* access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class OnRequeryFactory {
        void onRequery(ViewModel viewModel) {
        }

        OnRequeryFactory() {
        }
    }

    /* access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static abstract class KeyedFactory extends OnRequeryFactory implements Factory {
        public abstract <T extends ViewModel> T create(String str, Class<T> cls);

        KeyedFactory() {
        }

        @Override // androidx.lifecycle.ViewModelProvider.Factory
        public <T extends ViewModel> T create(Class<T> cls) {
            throw new UnsupportedOperationException("create(String, Class<?>) must be called on implementaions of KeyedFactory");
        }
    }

    public ViewModelProvider(ViewModelStore viewModelStore, Factory factory) {
        this.mFactory = factory;
        this.mViewModelStore = viewModelStore;
    }

    public <T extends ViewModel> T get(Class<T> cls) {
        String canonicalName = cls.getCanonicalName();
        if (canonicalName != null) {
            return (T) get("androidx.lifecycle.ViewModelProvider.DefaultKey:" + canonicalName, cls);
        }
        throw new IllegalArgumentException("Local and anonymous classes can not be ViewModels");
    }

    public <T extends ViewModel> T get(String str, Class<T> cls) {
        T t;
        T t2 = (T) this.mViewModelStore.get(str);
        if (cls.isInstance(t2)) {
            Factory factory = this.mFactory;
            if (factory instanceof OnRequeryFactory) {
                ((OnRequeryFactory) factory).onRequery(t2);
            }
            return t2;
        }
        Factory factory2 = this.mFactory;
        if (factory2 instanceof KeyedFactory) {
            t = (T) ((KeyedFactory) factory2).create(str, cls);
        } else {
            t = (T) factory2.create(cls);
        }
        this.mViewModelStore.put(str, t);
        return t;
    }
}
