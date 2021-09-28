package androidx.lifecycle;

import androidx.lifecycle.Lifecycle;
import androidx.savedstate.SavedStateRegistry;
import androidx.savedstate.SavedStateRegistryOwner;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
/* loaded from: classes.dex */
public final class SavedStateHandleController implements LifecycleEventObserver {
    public final SavedStateHandle mHandle;
    public boolean mIsAttached = false;
    public final String mKey;

    /* loaded from: classes.dex */
    public static final class OnRecreation implements SavedStateRegistry.AutoRecreated {
        @Override // androidx.savedstate.SavedStateRegistry.AutoRecreated
        public void onRecreated(SavedStateRegistryOwner savedStateRegistryOwner) {
            if (savedStateRegistryOwner instanceof ViewModelStoreOwner) {
                ViewModelStore viewModelStore = ((ViewModelStoreOwner) savedStateRegistryOwner).getViewModelStore();
                SavedStateRegistry savedStateRegistry = savedStateRegistryOwner.getSavedStateRegistry();
                Objects.requireNonNull(viewModelStore);
                Iterator it = new HashSet(viewModelStore.mMap.keySet()).iterator();
                while (it.hasNext()) {
                    SavedStateHandleController.attachHandleIfNeeded(viewModelStore.mMap.get((String) it.next()), savedStateRegistry, savedStateRegistryOwner.getLifecycle());
                }
                if (!new HashSet(viewModelStore.mMap.keySet()).isEmpty()) {
                    savedStateRegistry.runOnNextRecreation(OnRecreation.class);
                    return;
                }
                return;
            }
            throw new IllegalStateException("Internal error: OnRecreation should be registered only on componentsthat implement ViewModelStoreOwner");
        }
    }

    public SavedStateHandleController(String str, SavedStateHandle savedStateHandle) {
        this.mKey = str;
        this.mHandle = savedStateHandle;
    }

    public static void attachHandleIfNeeded(ViewModel viewModel, SavedStateRegistry savedStateRegistry, Lifecycle lifecycle) {
        Object obj;
        Map<String, Object> map = viewModel.mBagOfTags;
        if (map == null) {
            obj = null;
        } else {
            synchronized (map) {
                obj = viewModel.mBagOfTags.get("androidx.lifecycle.savedstate.vm.tag");
            }
        }
        SavedStateHandleController savedStateHandleController = (SavedStateHandleController) obj;
        if (savedStateHandleController != null && !savedStateHandleController.mIsAttached) {
            savedStateHandleController.attachToLifecycle(savedStateRegistry, lifecycle);
            tryToAddRecreator(savedStateRegistry, lifecycle);
        }
    }

    public static void tryToAddRecreator(final SavedStateRegistry savedStateRegistry, final Lifecycle lifecycle) {
        Lifecycle.State state = ((LifecycleRegistry) lifecycle).mState;
        if (state != Lifecycle.State.INITIALIZED) {
            if (!(state.compareTo(Lifecycle.State.STARTED) >= 0)) {
                lifecycle.addObserver(new LifecycleEventObserver() { // from class: androidx.lifecycle.SavedStateHandleController.1
                    @Override // androidx.lifecycle.LifecycleEventObserver
                    public void onStateChanged(LifecycleOwner lifecycleOwner, Lifecycle.Event event) {
                        if (event == Lifecycle.Event.ON_START) {
                            LifecycleRegistry lifecycleRegistry = (LifecycleRegistry) Lifecycle.this;
                            lifecycleRegistry.enforceMainThreadIfNeeded("removeObserver");
                            lifecycleRegistry.mObserverMap.remove(this);
                            savedStateRegistry.runOnNextRecreation(OnRecreation.class);
                        }
                    }
                });
                return;
            }
        }
        savedStateRegistry.runOnNextRecreation(OnRecreation.class);
    }

    public void attachToLifecycle(SavedStateRegistry savedStateRegistry, Lifecycle lifecycle) {
        if (!this.mIsAttached) {
            this.mIsAttached = true;
            lifecycle.addObserver(this);
            savedStateRegistry.registerSavedStateProvider(this.mKey, this.mHandle.mSavedStateProvider);
            return;
        }
        throw new IllegalStateException("Already attached to lifecycleOwner");
    }

    @Override // androidx.lifecycle.LifecycleEventObserver
    public void onStateChanged(LifecycleOwner lifecycleOwner, Lifecycle.Event event) {
        if (event == Lifecycle.Event.ON_DESTROY) {
            this.mIsAttached = false;
            LifecycleRegistry lifecycleRegistry = (LifecycleRegistry) lifecycleOwner.getLifecycle();
            lifecycleRegistry.enforceMainThreadIfNeeded("removeObserver");
            lifecycleRegistry.mObserverMap.remove(this);
        }
    }
}
