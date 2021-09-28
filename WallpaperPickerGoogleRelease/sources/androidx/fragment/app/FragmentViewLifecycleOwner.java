package androidx.fragment.app;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleRegistry;
import androidx.savedstate.SavedStateRegistry;
import androidx.savedstate.SavedStateRegistryController;
import androidx.savedstate.SavedStateRegistryOwner;
/* loaded from: classes.dex */
public class FragmentViewLifecycleOwner implements SavedStateRegistryOwner {
    public LifecycleRegistry mLifecycleRegistry = null;
    public SavedStateRegistryController mSavedStateRegistryController = null;

    @Override // androidx.lifecycle.LifecycleOwner
    public Lifecycle getLifecycle() {
        if (this.mLifecycleRegistry == null) {
            this.mLifecycleRegistry = new LifecycleRegistry(this, true);
            this.mSavedStateRegistryController = new SavedStateRegistryController(this);
        }
        return this.mLifecycleRegistry;
    }

    @Override // androidx.savedstate.SavedStateRegistryOwner
    public SavedStateRegistry getSavedStateRegistry() {
        return this.mSavedStateRegistryController.mRegistry;
    }

    public void handleLifecycleEvent(Lifecycle.Event event) {
        LifecycleRegistry lifecycleRegistry = this.mLifecycleRegistry;
        lifecycleRegistry.enforceMainThreadIfNeeded("handleLifecycleEvent");
        lifecycleRegistry.moveToState(event.getTargetState());
    }
}
