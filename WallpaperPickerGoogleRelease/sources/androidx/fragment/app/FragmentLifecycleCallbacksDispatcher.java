package androidx.fragment.app;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
/* loaded from: classes.dex */
public class FragmentLifecycleCallbacksDispatcher {
    public final FragmentManager mFragmentManager;
    public final CopyOnWriteArrayList<FragmentLifecycleCallbacksHolder> mLifecycleCallbacks = new CopyOnWriteArrayList<>();

    /* loaded from: classes.dex */
    public static final class FragmentLifecycleCallbacksHolder {
    }

    public FragmentLifecycleCallbacksDispatcher(FragmentManager fragmentManager) {
        this.mFragmentManager = fragmentManager;
    }

    public void dispatchOnFragmentActivityCreated(Fragment fragment, Bundle bundle, boolean z) {
        Fragment fragment2 = this.mFragmentManager.mParent;
        if (fragment2 != null) {
            fragment2.getParentFragmentManager().mLifecycleCallbacksDispatcher.dispatchOnFragmentActivityCreated(fragment, bundle, true);
        }
        Iterator<FragmentLifecycleCallbacksHolder> it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            FragmentLifecycleCallbacksHolder next = it.next();
            if (z) {
                Objects.requireNonNull(next);
            } else {
                Objects.requireNonNull(next);
                throw null;
            }
        }
    }

    public void dispatchOnFragmentAttached(Fragment fragment, boolean z) {
        FragmentManager fragmentManager = this.mFragmentManager;
        Context context = fragmentManager.mHost.mContext;
        Fragment fragment2 = fragmentManager.mParent;
        if (fragment2 != null) {
            fragment2.getParentFragmentManager().mLifecycleCallbacksDispatcher.dispatchOnFragmentAttached(fragment, true);
        }
        Iterator<FragmentLifecycleCallbacksHolder> it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            FragmentLifecycleCallbacksHolder next = it.next();
            if (z) {
                Objects.requireNonNull(next);
            } else {
                Objects.requireNonNull(next);
                throw null;
            }
        }
    }

    public void dispatchOnFragmentCreated(Fragment fragment, Bundle bundle, boolean z) {
        Fragment fragment2 = this.mFragmentManager.mParent;
        if (fragment2 != null) {
            fragment2.getParentFragmentManager().mLifecycleCallbacksDispatcher.dispatchOnFragmentCreated(fragment, bundle, true);
        }
        Iterator<FragmentLifecycleCallbacksHolder> it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            FragmentLifecycleCallbacksHolder next = it.next();
            if (z) {
                Objects.requireNonNull(next);
            } else {
                Objects.requireNonNull(next);
                throw null;
            }
        }
    }

    public void dispatchOnFragmentDestroyed(Fragment fragment, boolean z) {
        Fragment fragment2 = this.mFragmentManager.mParent;
        if (fragment2 != null) {
            fragment2.getParentFragmentManager().mLifecycleCallbacksDispatcher.dispatchOnFragmentDestroyed(fragment, true);
        }
        Iterator<FragmentLifecycleCallbacksHolder> it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            FragmentLifecycleCallbacksHolder next = it.next();
            if (z) {
                Objects.requireNonNull(next);
            } else {
                Objects.requireNonNull(next);
                throw null;
            }
        }
    }

    public void dispatchOnFragmentDetached(Fragment fragment, boolean z) {
        Fragment fragment2 = this.mFragmentManager.mParent;
        if (fragment2 != null) {
            fragment2.getParentFragmentManager().mLifecycleCallbacksDispatcher.dispatchOnFragmentDetached(fragment, true);
        }
        Iterator<FragmentLifecycleCallbacksHolder> it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            FragmentLifecycleCallbacksHolder next = it.next();
            if (z) {
                Objects.requireNonNull(next);
            } else {
                Objects.requireNonNull(next);
                throw null;
            }
        }
    }

    public void dispatchOnFragmentPaused(Fragment fragment, boolean z) {
        Fragment fragment2 = this.mFragmentManager.mParent;
        if (fragment2 != null) {
            fragment2.getParentFragmentManager().mLifecycleCallbacksDispatcher.dispatchOnFragmentPaused(fragment, true);
        }
        Iterator<FragmentLifecycleCallbacksHolder> it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            FragmentLifecycleCallbacksHolder next = it.next();
            if (z) {
                Objects.requireNonNull(next);
            } else {
                Objects.requireNonNull(next);
                throw null;
            }
        }
    }

    public void dispatchOnFragmentPreAttached(Fragment fragment, boolean z) {
        FragmentManager fragmentManager = this.mFragmentManager;
        Context context = fragmentManager.mHost.mContext;
        Fragment fragment2 = fragmentManager.mParent;
        if (fragment2 != null) {
            fragment2.getParentFragmentManager().mLifecycleCallbacksDispatcher.dispatchOnFragmentPreAttached(fragment, true);
        }
        Iterator<FragmentLifecycleCallbacksHolder> it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            FragmentLifecycleCallbacksHolder next = it.next();
            if (z) {
                Objects.requireNonNull(next);
            } else {
                Objects.requireNonNull(next);
                throw null;
            }
        }
    }

    public void dispatchOnFragmentPreCreated(Fragment fragment, Bundle bundle, boolean z) {
        Fragment fragment2 = this.mFragmentManager.mParent;
        if (fragment2 != null) {
            fragment2.getParentFragmentManager().mLifecycleCallbacksDispatcher.dispatchOnFragmentPreCreated(fragment, bundle, true);
        }
        Iterator<FragmentLifecycleCallbacksHolder> it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            FragmentLifecycleCallbacksHolder next = it.next();
            if (z) {
                Objects.requireNonNull(next);
            } else {
                Objects.requireNonNull(next);
                throw null;
            }
        }
    }

    public void dispatchOnFragmentResumed(Fragment fragment, boolean z) {
        Fragment fragment2 = this.mFragmentManager.mParent;
        if (fragment2 != null) {
            fragment2.getParentFragmentManager().mLifecycleCallbacksDispatcher.dispatchOnFragmentResumed(fragment, true);
        }
        Iterator<FragmentLifecycleCallbacksHolder> it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            FragmentLifecycleCallbacksHolder next = it.next();
            if (z) {
                Objects.requireNonNull(next);
            } else {
                Objects.requireNonNull(next);
                throw null;
            }
        }
    }

    public void dispatchOnFragmentSaveInstanceState(Fragment fragment, Bundle bundle, boolean z) {
        Fragment fragment2 = this.mFragmentManager.mParent;
        if (fragment2 != null) {
            fragment2.getParentFragmentManager().mLifecycleCallbacksDispatcher.dispatchOnFragmentSaveInstanceState(fragment, bundle, true);
        }
        Iterator<FragmentLifecycleCallbacksHolder> it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            FragmentLifecycleCallbacksHolder next = it.next();
            if (z) {
                Objects.requireNonNull(next);
            } else {
                Objects.requireNonNull(next);
                throw null;
            }
        }
    }

    public void dispatchOnFragmentStarted(Fragment fragment, boolean z) {
        Fragment fragment2 = this.mFragmentManager.mParent;
        if (fragment2 != null) {
            fragment2.getParentFragmentManager().mLifecycleCallbacksDispatcher.dispatchOnFragmentStarted(fragment, true);
        }
        Iterator<FragmentLifecycleCallbacksHolder> it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            FragmentLifecycleCallbacksHolder next = it.next();
            if (z) {
                Objects.requireNonNull(next);
            } else {
                Objects.requireNonNull(next);
                throw null;
            }
        }
    }

    public void dispatchOnFragmentStopped(Fragment fragment, boolean z) {
        Fragment fragment2 = this.mFragmentManager.mParent;
        if (fragment2 != null) {
            fragment2.getParentFragmentManager().mLifecycleCallbacksDispatcher.dispatchOnFragmentStopped(fragment, true);
        }
        Iterator<FragmentLifecycleCallbacksHolder> it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            FragmentLifecycleCallbacksHolder next = it.next();
            if (z) {
                Objects.requireNonNull(next);
            } else {
                Objects.requireNonNull(next);
                throw null;
            }
        }
    }

    public void dispatchOnFragmentViewCreated(Fragment fragment, View view, Bundle bundle, boolean z) {
        Fragment fragment2 = this.mFragmentManager.mParent;
        if (fragment2 != null) {
            fragment2.getParentFragmentManager().mLifecycleCallbacksDispatcher.dispatchOnFragmentViewCreated(fragment, view, bundle, true);
        }
        Iterator<FragmentLifecycleCallbacksHolder> it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            FragmentLifecycleCallbacksHolder next = it.next();
            if (z) {
                Objects.requireNonNull(next);
            } else {
                Objects.requireNonNull(next);
                throw null;
            }
        }
    }

    public void dispatchOnFragmentViewDestroyed(Fragment fragment, boolean z) {
        Fragment fragment2 = this.mFragmentManager.mParent;
        if (fragment2 != null) {
            fragment2.getParentFragmentManager().mLifecycleCallbacksDispatcher.dispatchOnFragmentViewDestroyed(fragment, true);
        }
        Iterator<FragmentLifecycleCallbacksHolder> it = this.mLifecycleCallbacks.iterator();
        while (it.hasNext()) {
            FragmentLifecycleCallbacksHolder next = it.next();
            if (z) {
                Objects.requireNonNull(next);
            } else {
                Objects.requireNonNull(next);
                throw null;
            }
        }
    }
}
