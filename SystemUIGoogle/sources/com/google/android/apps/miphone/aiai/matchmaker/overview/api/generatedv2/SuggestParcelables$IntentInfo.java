package com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
/* loaded from: classes2.dex */
public final class SuggestParcelables$IntentInfo {
    @Nullable
    private String action;
    @Nullable
    private String className;
    private int flags;
    private boolean hasAction;
    private boolean hasClassName;
    private boolean hasFlags;
    private boolean hasIntentParams;
    private boolean hasIntentType;
    private boolean hasMimeType;
    private boolean hasPackageName;
    private boolean hasUri;
    @Nullable
    private List<SuggestParcelables$IntentParam> intentParams;
    private SuggestParcelables$IntentType intentType;
    @Nullable
    private String mimeType;
    @Nullable
    private String packageName;
    @Nullable
    private String uri;

    private SuggestParcelables$IntentInfo(Bundle bundle) {
        readFromBundle(bundle);
    }

    private SuggestParcelables$IntentInfo() {
    }

    public static SuggestParcelables$IntentInfo create(Bundle bundle) {
        return new SuggestParcelables$IntentInfo(bundle);
    }

    public SuggestParcelables$IntentType getIntentType() {
        return this.intentType;
    }

    public Bundle writeToBundle() {
        Bundle bundle = new Bundle();
        if (this.intentParams == null) {
            bundle.putParcelableArrayList("intentParams", null);
        } else {
            ArrayList<? extends Parcelable> arrayList = new ArrayList<>(this.intentParams.size());
            for (SuggestParcelables$IntentParam suggestParcelables$IntentParam : this.intentParams) {
                if (suggestParcelables$IntentParam == null) {
                    arrayList.add(null);
                } else {
                    arrayList.add(suggestParcelables$IntentParam.writeToBundle());
                }
            }
            bundle.putParcelableArrayList("intentParams", arrayList);
        }
        bundle.putString("packageName", this.packageName);
        bundle.putString("className", this.className);
        bundle.putString("action", this.action);
        bundle.putString("uri", this.uri);
        bundle.putString("mimeType", this.mimeType);
        bundle.putInt("flags", this.flags);
        SuggestParcelables$IntentType suggestParcelables$IntentType = this.intentType;
        if (suggestParcelables$IntentType == null) {
            bundle.putBundle("intentType", null);
        } else {
            bundle.putBundle("intentType", suggestParcelables$IntentType.writeToBundle());
        }
        return bundle;
    }

    private void readFromBundle(Bundle bundle) {
        if (!bundle.containsKey("intentParams")) {
            this.hasIntentParams = false;
        } else {
            this.hasIntentParams = true;
            ArrayList parcelableArrayList = bundle.getParcelableArrayList("intentParams");
            if (parcelableArrayList == null) {
                this.intentParams = null;
            } else {
                this.intentParams = new ArrayList(parcelableArrayList.size());
                Iterator it = parcelableArrayList.iterator();
                while (it.hasNext()) {
                    Bundle bundle2 = (Bundle) it.next();
                    if (bundle2 == null) {
                        this.intentParams.add(null);
                    } else {
                        this.intentParams.add(SuggestParcelables$IntentParam.create(bundle2));
                    }
                }
            }
        }
        if (!bundle.containsKey("packageName")) {
            this.hasPackageName = false;
        } else {
            this.hasPackageName = true;
            this.packageName = bundle.getString("packageName");
        }
        if (!bundle.containsKey("className")) {
            this.hasClassName = false;
        } else {
            this.hasClassName = true;
            this.className = bundle.getString("className");
        }
        if (!bundle.containsKey("action")) {
            this.hasAction = false;
        } else {
            this.hasAction = true;
            this.action = bundle.getString("action");
        }
        if (!bundle.containsKey("uri")) {
            this.hasUri = false;
        } else {
            this.hasUri = true;
            this.uri = bundle.getString("uri");
        }
        if (!bundle.containsKey("mimeType")) {
            this.hasMimeType = false;
        } else {
            this.hasMimeType = true;
            this.mimeType = bundle.getString("mimeType");
        }
        if (!bundle.containsKey("flags")) {
            this.hasFlags = false;
        } else {
            this.hasFlags = true;
            this.flags = bundle.getInt("flags");
        }
        if (!bundle.containsKey("intentType")) {
            this.hasIntentType = false;
            return;
        }
        this.hasIntentType = true;
        Bundle bundle3 = bundle.getBundle("intentType");
        if (bundle3 == null) {
            this.intentType = null;
        } else {
            this.intentType = SuggestParcelables$IntentType.create(bundle3);
        }
        if (this.intentType == null) {
            this.hasIntentType = false;
        }
    }
}
