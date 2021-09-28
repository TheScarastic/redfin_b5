package com.android.settingslib.drawer;

import android.content.pm.ProviderInfo;
import android.os.Parcel;
/* loaded from: classes.dex */
public class ProviderTile extends Tile {
    private String mAuthority = ((ProviderInfo) this.mComponentInfo).authority;
    private String mKey = getMetaData().getString("com.android.settings.keyhint");

    /* access modifiers changed from: package-private */
    public ProviderTile(Parcel parcel) {
        super(parcel);
    }
}
