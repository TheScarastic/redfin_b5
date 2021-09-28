package com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2;

import android.os.Bundle;
import android.support.annotation.Nullable;
/* loaded from: classes2.dex */
public final class ContentParcelables$AppIcon {
    private ContentParcelables$AppIconType appIconType;
    @Nullable
    private ContentParcelables$AppPackage appPackage;
    private boolean hasAppIconType;
    private boolean hasAppPackage;
    private boolean hasIconUri;
    @Nullable
    private String iconUri;

    private ContentParcelables$AppIcon(Bundle bundle) {
        readFromBundle(bundle);
    }

    public static ContentParcelables$AppIcon create(Bundle bundle) {
        return new ContentParcelables$AppIcon(bundle);
    }

    public Bundle writeToBundle() {
        Bundle bundle = new Bundle();
        bundle.putString("iconUri", this.iconUri);
        ContentParcelables$AppPackage contentParcelables$AppPackage = this.appPackage;
        if (contentParcelables$AppPackage == null) {
            bundle.putBundle("appPackage", null);
        } else {
            bundle.putBundle("appPackage", contentParcelables$AppPackage.writeToBundle());
        }
        ContentParcelables$AppIconType contentParcelables$AppIconType = this.appIconType;
        if (contentParcelables$AppIconType == null) {
            bundle.putBundle("appIconType", null);
        } else {
            bundle.putBundle("appIconType", contentParcelables$AppIconType.writeToBundle());
        }
        return bundle;
    }

    private void readFromBundle(Bundle bundle) {
        if (!bundle.containsKey("iconUri")) {
            this.hasIconUri = false;
        } else {
            this.hasIconUri = true;
            this.iconUri = bundle.getString("iconUri");
        }
        if (!bundle.containsKey("appPackage")) {
            this.hasAppPackage = false;
        } else {
            this.hasAppPackage = true;
            Bundle bundle2 = bundle.getBundle("appPackage");
            if (bundle2 == null) {
                this.appPackage = null;
            } else {
                this.appPackage = ContentParcelables$AppPackage.create(bundle2);
            }
        }
        if (!bundle.containsKey("appIconType")) {
            this.hasAppIconType = false;
            return;
        }
        this.hasAppIconType = true;
        Bundle bundle3 = bundle.getBundle("appIconType");
        if (bundle3 == null) {
            this.appIconType = null;
        } else {
            this.appIconType = ContentParcelables$AppIconType.create(bundle3);
        }
        if (this.appIconType == null) {
            this.hasAppIconType = false;
        }
    }
}
