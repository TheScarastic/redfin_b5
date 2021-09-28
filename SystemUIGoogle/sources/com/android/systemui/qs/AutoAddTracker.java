package com.android.systemui.qs;

import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.UserHandle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.ArraySet;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.Prefs;
import com.android.systemui.util.UserAwareController;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
/* loaded from: classes.dex */
public class AutoAddTracker implements UserAwareController {
    private static final String[][] CONVERT_PREFS = {new String[]{"QsHotspotAdded", "hotspot"}, new String[]{"QsDataSaverAdded", "saver"}, new String[]{"QsInvertColorsAdded", "inversion"}, new String[]{"QsWorkAdded", "work"}, new String[]{"QsNightDisplayAdded", "night"}};
    private final Context mContext;
    private int mUserId;
    @VisibleForTesting
    protected final ContentObserver mObserver = new ContentObserver(new Handler()) { // from class: com.android.systemui.qs.AutoAddTracker.1
        @Override // android.database.ContentObserver
        public void onChange(boolean z) {
            AutoAddTracker.this.mAutoAdded.clear();
            AutoAddTracker.this.mAutoAdded.addAll(AutoAddTracker.this.getAdded());
        }
    };
    private final ArraySet<String> mAutoAdded = new ArraySet<>(getAdded());

    public AutoAddTracker(Context context, int i) {
        this.mContext = context;
        this.mUserId = i;
    }

    public void initialize() {
        if (this.mUserId == 0) {
            String[][] strArr = CONVERT_PREFS;
            for (String[] strArr2 : strArr) {
                if (Prefs.getBoolean(this.mContext, strArr2[0], false)) {
                    setTileAdded(strArr2[1]);
                    Prefs.remove(this.mContext, strArr2[0]);
                }
            }
        }
        this.mContext.getContentResolver().registerContentObserver(Settings.Secure.getUriFor("qs_auto_tiles"), false, this.mObserver, -1);
    }

    @Override // com.android.systemui.util.UserAwareController
    public void changeUser(UserHandle userHandle) {
        if (userHandle.getIdentifier() != this.mUserId) {
            this.mUserId = userHandle.getIdentifier();
            this.mAutoAdded.clear();
            this.mAutoAdded.addAll(getAdded());
        }
    }

    public boolean isAdded(String str) {
        return this.mAutoAdded.contains(str);
    }

    public void setTileAdded(String str) {
        if (this.mAutoAdded.add(str)) {
            saveTiles();
        }
    }

    public void setTileRemoved(String str) {
        if (this.mAutoAdded.remove(str)) {
            saveTiles();
        }
    }

    private void saveTiles() {
        Settings.Secure.putStringForUser(this.mContext.getContentResolver(), "qs_auto_tiles", TextUtils.join(",", this.mAutoAdded), this.mUserId);
    }

    /* access modifiers changed from: private */
    public Collection<String> getAdded() {
        String stringForUser = Settings.Secure.getStringForUser(this.mContext.getContentResolver(), "qs_auto_tiles", this.mUserId);
        if (stringForUser == null) {
            return Collections.emptyList();
        }
        return Arrays.asList(stringForUser.split(","));
    }

    /* loaded from: classes.dex */
    public static class Builder {
        private final Context mContext;
        private int mUserId;

        public Builder(Context context) {
            this.mContext = context;
        }

        public Builder setUserId(int i) {
            this.mUserId = i;
            return this;
        }

        public AutoAddTracker build() {
            return new AutoAddTracker(this.mContext, this.mUserId);
        }
    }
}
