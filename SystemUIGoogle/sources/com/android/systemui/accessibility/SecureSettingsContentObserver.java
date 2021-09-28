package com.android.systemui.accessibility;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import com.android.internal.annotations.VisibleForTesting;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
/* loaded from: classes.dex */
public abstract class SecureSettingsContentObserver<T> {
    private final ContentResolver mContentResolver;
    private final String mKey;
    @VisibleForTesting
    final List<T> mListeners = new ArrayList();
    @VisibleForTesting
    final ContentObserver mContentObserver = new ContentObserver(new Handler(Looper.getMainLooper())) { // from class: com.android.systemui.accessibility.SecureSettingsContentObserver.1
        @Override // android.database.ContentObserver
        public void onChange(boolean z) {
            SecureSettingsContentObserver.this.updateValueChanged();
        }
    };

    abstract void onValueChanged(T t, String str);

    /* access modifiers changed from: protected */
    public SecureSettingsContentObserver(Context context, String str) {
        this.mKey = str;
        this.mContentResolver = context.getContentResolver();
    }

    public void addListener(T t) {
        Objects.requireNonNull(t, "listener must be non-null");
        this.mListeners.add(t);
        if (this.mListeners.size() == 1) {
            this.mContentResolver.registerContentObserver(Settings.Secure.getUriFor(this.mKey), false, this.mContentObserver, -1);
        }
    }

    public void removeListener(T t) {
        Objects.requireNonNull(t, "listener must be non-null");
        this.mListeners.remove(t);
        if (this.mListeners.isEmpty()) {
            this.mContentResolver.unregisterContentObserver(this.mContentObserver);
        }
    }

    public final String getSettingsValue() {
        return Settings.Secure.getStringForUser(this.mContentResolver, this.mKey, -2);
    }

    /* access modifiers changed from: private */
    public void updateValueChanged() {
        String settingsValue = getSettingsValue();
        int size = this.mListeners.size();
        for (int i = 0; i < size; i++) {
            onValueChanged(this.mListeners.get(i), settingsValue);
        }
    }
}
