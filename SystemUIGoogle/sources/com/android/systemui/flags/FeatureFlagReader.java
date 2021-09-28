package com.android.systemui.flags;

import android.content.res.Resources;
import android.util.SparseArray;
import com.android.systemui.R$bool;
import com.android.systemui.util.wrapper.BuildInfo;
/* loaded from: classes.dex */
public class FeatureFlagReader {
    private final boolean mAreFlagsOverrideable;
    private final SparseArray<CachedFlag> mCachedFlags = new SparseArray<>();
    private final Resources mResources;
    private final SystemPropertiesHelper mSystemPropertiesHelper;

    public FeatureFlagReader(Resources resources, BuildInfo buildInfo, SystemPropertiesHelper systemPropertiesHelper) {
        this.mResources = resources;
        this.mSystemPropertiesHelper = systemPropertiesHelper;
        this.mAreFlagsOverrideable = buildInfo.isDebuggable() && resources.getBoolean(R$bool.are_flags_overrideable);
    }

    public boolean isEnabled(int i) {
        boolean z;
        synchronized (this.mCachedFlags) {
            CachedFlag cachedFlag = this.mCachedFlags.get(i);
            if (cachedFlag == null) {
                String resourceIdToFlagName = resourceIdToFlagName(i);
                boolean z2 = this.mResources.getBoolean(i);
                if (this.mAreFlagsOverrideable) {
                    z2 = this.mSystemPropertiesHelper.getBoolean(flagNameToStorageKey(resourceIdToFlagName), z2);
                }
                CachedFlag cachedFlag2 = new CachedFlag(resourceIdToFlagName, z2);
                this.mCachedFlags.put(i, cachedFlag2);
                cachedFlag = cachedFlag2;
            }
            z = cachedFlag.value;
        }
        return z;
    }

    private String resourceIdToFlagName(int i) {
        String resourceEntryName = this.mResources.getResourceEntryName(i);
        return resourceEntryName.startsWith("flag_") ? resourceEntryName.substring(5) : resourceEntryName;
    }

    private String flagNameToStorageKey(String str) {
        if (str.startsWith("persist.systemui.flag_")) {
            return str;
        }
        return "persist.systemui.flag_" + str;
    }

    /* access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class CachedFlag {
        public final String name;
        public final boolean value;

        private CachedFlag(String str, boolean z) {
            this.name = str;
            this.value = z;
        }
    }
}
