package com.android.systemui.util;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
/* loaded from: classes2.dex */
public abstract class ViewController<T extends View> {
    private boolean mInited;
    private View.OnAttachStateChangeListener mOnAttachStateListener = new View.OnAttachStateChangeListener() { // from class: com.android.systemui.util.ViewController.1
        @Override // android.view.View.OnAttachStateChangeListener
        public void onViewAttachedToWindow(View view) {
            ViewController.this.onViewAttached();
        }

        @Override // android.view.View.OnAttachStateChangeListener
        public void onViewDetachedFromWindow(View view) {
            ViewController.this.onViewDetached();
        }
    };
    protected final T mView;

    /* access modifiers changed from: protected */
    public void onInit() {
    }

    protected abstract void onViewAttached();

    protected abstract void onViewDetached();

    /* access modifiers changed from: protected */
    public ViewController(T t) {
        this.mView = t;
    }

    public void init() {
        if (!this.mInited) {
            onInit();
            this.mInited = true;
            if (isAttachedToWindow()) {
                this.mOnAttachStateListener.onViewAttachedToWindow(this.mView);
            }
            addOnAttachStateChangeListener(this.mOnAttachStateListener);
        }
    }

    /* access modifiers changed from: protected */
    public Context getContext() {
        return this.mView.getContext();
    }

    /* access modifiers changed from: protected */
    public Resources getResources() {
        return this.mView.getResources();
    }

    public boolean isAttachedToWindow() {
        T t = this.mView;
        return t != null && t.isAttachedToWindow();
    }

    public void addOnAttachStateChangeListener(View.OnAttachStateChangeListener onAttachStateChangeListener) {
        T t = this.mView;
        if (t != null) {
            t.addOnAttachStateChangeListener(onAttachStateChangeListener);
        }
    }
}
