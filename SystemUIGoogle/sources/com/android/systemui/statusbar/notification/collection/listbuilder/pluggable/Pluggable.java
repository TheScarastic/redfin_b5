package com.android.systemui.statusbar.notification.collection.listbuilder.pluggable;
/* loaded from: classes.dex */
public abstract class Pluggable<This> {
    private PluggableListener<This> mListener;
    private final String mName;

    /* loaded from: classes.dex */
    public interface PluggableListener<T> {
        void onPluggableInvalidated(T t);
    }

    public void onCleanup() {
    }

    /* access modifiers changed from: package-private */
    public Pluggable(String str) {
        this.mName = str;
    }

    public final String getName() {
        return this.mName;
    }

    public final void invalidateList() {
        PluggableListener<This> pluggableListener = this.mListener;
        if (pluggableListener != null) {
            pluggableListener.onPluggableInvalidated(this);
        }
    }

    public final void setInvalidationListener(PluggableListener<This> pluggableListener) {
        this.mListener = pluggableListener;
    }
}
