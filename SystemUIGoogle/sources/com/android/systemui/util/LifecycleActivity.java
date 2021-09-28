package com.android.systemui.util;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import com.android.settingslib.core.lifecycle.Lifecycle;
/* compiled from: LifecycleActivity.kt */
/* loaded from: classes2.dex */
public class LifecycleActivity extends Activity implements LifecycleOwner {
    private final Lifecycle lifecycle = new Lifecycle(this);

    @Override // androidx.lifecycle.LifecycleOwner
    public Lifecycle getLifecycle() {
        return this.lifecycle;
    }

    /* access modifiers changed from: protected */
    @Override // android.app.Activity
    public void onCreate(Bundle bundle) {
        this.lifecycle.onAttach(this);
        this.lifecycle.onCreate(bundle);
        this.lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_CREATE);
        super.onCreate(bundle);
    }

    @Override // android.app.Activity
    public void onCreate(Bundle bundle, PersistableBundle persistableBundle) {
        this.lifecycle.onAttach(this);
        this.lifecycle.onCreate(bundle);
        this.lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_CREATE);
        super.onCreate(bundle, persistableBundle);
    }

    /* access modifiers changed from: protected */
    @Override // android.app.Activity
    public void onStart() {
        this.lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_START);
        super.onStart();
    }

    /* access modifiers changed from: protected */
    @Override // android.app.Activity
    public void onResume() {
        this.lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_RESUME);
        super.onResume();
    }

    /* access modifiers changed from: protected */
    @Override // android.app.Activity
    public void onPause() {
        this.lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE);
        super.onPause();
    }

    /* access modifiers changed from: protected */
    @Override // android.app.Activity
    public void onStop() {
        this.lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_STOP);
        super.onStop();
    }

    /* access modifiers changed from: protected */
    @Override // android.app.Activity
    public void onDestroy() {
        this.lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY);
        super.onDestroy();
    }
}
