package com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
/* loaded from: classes2.dex */
public final class SuggestParcelables$SetupInfo {
    private SuggestParcelables$ErrorCode errorCode;
    @Nullable
    private String errorMesssage;
    private boolean hasErrorCode;
    private boolean hasErrorMesssage;
    private boolean hasSetupFlags;
    @Nullable
    private List<SuggestParcelables$Flag> setupFlags;

    private SuggestParcelables$SetupInfo(Bundle bundle) {
        readFromBundle(bundle);
    }

    private SuggestParcelables$SetupInfo() {
    }

    public static SuggestParcelables$SetupInfo create(Bundle bundle) {
        return new SuggestParcelables$SetupInfo(bundle);
    }

    public SuggestParcelables$ErrorCode getErrorCode() {
        return this.errorCode;
    }

    @Nullable
    public String getErrorMesssage() {
        return this.errorMesssage;
    }

    public Bundle writeToBundle() {
        Bundle bundle = new Bundle();
        SuggestParcelables$ErrorCode suggestParcelables$ErrorCode = this.errorCode;
        if (suggestParcelables$ErrorCode == null) {
            bundle.putBundle("errorCode", null);
        } else {
            bundle.putBundle("errorCode", suggestParcelables$ErrorCode.writeToBundle());
        }
        bundle.putString("errorMesssage", this.errorMesssage);
        if (this.setupFlags == null) {
            bundle.putParcelableArrayList("setupFlags", null);
        } else {
            ArrayList<? extends Parcelable> arrayList = new ArrayList<>(this.setupFlags.size());
            for (SuggestParcelables$Flag suggestParcelables$Flag : this.setupFlags) {
                if (suggestParcelables$Flag == null) {
                    arrayList.add(null);
                } else {
                    arrayList.add(suggestParcelables$Flag.writeToBundle());
                }
            }
            bundle.putParcelableArrayList("setupFlags", arrayList);
        }
        return bundle;
    }

    private void readFromBundle(Bundle bundle) {
        if (!bundle.containsKey("errorCode")) {
            this.hasErrorCode = false;
        } else {
            this.hasErrorCode = true;
            Bundle bundle2 = bundle.getBundle("errorCode");
            if (bundle2 == null) {
                this.errorCode = null;
            } else {
                this.errorCode = SuggestParcelables$ErrorCode.create(bundle2);
            }
            if (this.errorCode == null) {
                this.hasErrorCode = false;
            }
        }
        if (!bundle.containsKey("errorMesssage")) {
            this.hasErrorMesssage = false;
        } else {
            this.hasErrorMesssage = true;
            this.errorMesssage = bundle.getString("errorMesssage");
        }
        if (!bundle.containsKey("setupFlags")) {
            this.hasSetupFlags = false;
            return;
        }
        this.hasSetupFlags = true;
        ArrayList parcelableArrayList = bundle.getParcelableArrayList("setupFlags");
        if (parcelableArrayList == null) {
            this.setupFlags = null;
            return;
        }
        this.setupFlags = new ArrayList(parcelableArrayList.size());
        Iterator it = parcelableArrayList.iterator();
        while (it.hasNext()) {
            Bundle bundle3 = (Bundle) it.next();
            if (bundle3 == null) {
                this.setupFlags.add(null);
            } else {
                this.setupFlags.add(SuggestParcelables$Flag.create(bundle3));
            }
        }
    }
}
