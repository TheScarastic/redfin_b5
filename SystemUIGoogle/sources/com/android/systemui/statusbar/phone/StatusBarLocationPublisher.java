package com.android.systemui.statusbar.phone;

import com.android.systemui.statusbar.policy.CallbackController;
import java.lang.ref.WeakReference;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import kotlin.Unit;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: StatusBarLocationPublisher.kt */
/* loaded from: classes.dex */
public final class StatusBarLocationPublisher implements CallbackController<StatusBarMarginUpdatedListener> {
    private final Set<WeakReference<StatusBarMarginUpdatedListener>> listeners = new LinkedHashSet();
    private int marginLeft;
    private int marginRight;

    public final int getMarginLeft() {
        return this.marginLeft;
    }

    public final int getMarginRight() {
        return this.marginRight;
    }

    public void addCallback(StatusBarMarginUpdatedListener statusBarMarginUpdatedListener) {
        Intrinsics.checkNotNullParameter(statusBarMarginUpdatedListener, "listener");
        this.listeners.add(new WeakReference<>(statusBarMarginUpdatedListener));
    }

    public void removeCallback(StatusBarMarginUpdatedListener statusBarMarginUpdatedListener) {
        Intrinsics.checkNotNullParameter(statusBarMarginUpdatedListener, "listener");
        WeakReference<StatusBarMarginUpdatedListener> weakReference = null;
        for (WeakReference<StatusBarMarginUpdatedListener> weakReference2 : this.listeners) {
            if (Intrinsics.areEqual(weakReference2.get(), statusBarMarginUpdatedListener)) {
                weakReference = weakReference2;
            }
        }
        if (weakReference != null) {
            this.listeners.remove(weakReference);
        }
    }

    public final void updateStatusBarMargin(int i, int i2) {
        this.marginLeft = i;
        this.marginRight = i2;
        notifyListeners();
    }

    private final void notifyListeners() {
        List<WeakReference> list;
        synchronized (this) {
            list = CollectionsKt___CollectionsKt.toList(this.listeners);
            Unit unit = Unit.INSTANCE;
        }
        for (WeakReference weakReference : list) {
            if (weakReference.get() == null) {
                this.listeners.remove(weakReference);
            }
            StatusBarMarginUpdatedListener statusBarMarginUpdatedListener = (StatusBarMarginUpdatedListener) weakReference.get();
            if (statusBarMarginUpdatedListener != null) {
                statusBarMarginUpdatedListener.onStatusBarMarginUpdated(getMarginLeft(), getMarginRight());
            }
        }
    }
}
