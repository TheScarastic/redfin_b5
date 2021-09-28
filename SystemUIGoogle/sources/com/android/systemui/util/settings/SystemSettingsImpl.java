package com.android.systemui.util.settings;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.Settings;
/* loaded from: classes2.dex */
class SystemSettingsImpl implements SystemSettings {
    private final ContentResolver mContentResolver;

    /* access modifiers changed from: package-private */
    public SystemSettingsImpl(ContentResolver contentResolver) {
        this.mContentResolver = contentResolver;
    }

    @Override // com.android.systemui.util.settings.SettingsProxy
    public ContentResolver getContentResolver() {
        return this.mContentResolver;
    }

    @Override // com.android.systemui.util.settings.SettingsProxy
    public Uri getUriFor(String str) {
        return Settings.System.getUriFor(str);
    }

    @Override // com.android.systemui.util.settings.SettingsProxy
    public String getStringForUser(String str, int i) {
        return Settings.System.getStringForUser(this.mContentResolver, str, i);
    }

    @Override // com.android.systemui.util.settings.SettingsProxy
    public boolean putStringForUser(String str, String str2, int i) {
        return Settings.System.putStringForUser(this.mContentResolver, str, str2, i);
    }
}
