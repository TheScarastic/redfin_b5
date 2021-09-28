package com.android.systemui.statusbar.policy;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
/* loaded from: classes.dex */
public interface CallbackController<T> {
    void addCallback(T t);

    void removeCallback(T t);

    default T observe(LifecycleOwner lifecycleOwner, T t) {
        return observe(lifecycleOwner.getLifecycle(), (Lifecycle) t);
    }

    default T observe(Lifecycle lifecycle, T t) {
        lifecycle.addObserver(new LifecycleEventObserver(t) { // from class: com.android.systemui.statusbar.policy.CallbackController$$ExternalSyntheticLambda0
            public final /* synthetic */ Object f$1;

            {
                this.f$1 = r2;
            }

            @Override // androidx.lifecycle.LifecycleEventObserver
            public final void onStateChanged(LifecycleOwner lifecycleOwner, Lifecycle.Event event) {
                CallbackController.this.lambda$observe$0(this.f$1, lifecycleOwner, event);
            }
        });
        return t;
    }

    /* JADX DEBUG: Multi-variable search result rejected for r1v0, resolved type: java.lang.Object */
    /* JADX WARN: Multi-variable type inference failed */
    /* access modifiers changed from: private */
    /* synthetic */ default void lambda$observe$0(Object obj, LifecycleOwner lifecycleOwner, Lifecycle.Event event) {
        if (event == Lifecycle.Event.ON_RESUME) {
            addCallback(obj);
        } else if (event == Lifecycle.Event.ON_PAUSE) {
            removeCallback(obj);
        }
    }
}
