package com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2;

import android.os.Bundle;
import android.support.annotation.Nullable;
/* loaded from: classes2.dex */
public final class SuggestParcelables$Action {
    @Nullable
    private String dEPRECATEDIconBitmapId;
    @Nullable
    private SuggestParcelables$IntentInfo dEPRECATEDIntentInfo;
    @Nullable
    private String displayName;
    @Nullable
    private String fullDisplayName;
    private boolean hasDEPRECATEDIconBitmapId;
    private boolean hasDEPRECATEDIntentInfo;
    private boolean hasDisplayName;
    private boolean hasFullDisplayName;
    private boolean hasId;
    private boolean hasOpaquePayload;
    private boolean hasProxiedIntentInfo;
    @Nullable
    private String id;
    @Nullable
    private String opaquePayload;
    @Nullable
    private SuggestParcelables$IntentInfo proxiedIntentInfo;

    private SuggestParcelables$Action(Bundle bundle) {
        readFromBundle(bundle);
    }

    private SuggestParcelables$Action() {
    }

    public static SuggestParcelables$Action create(Bundle bundle) {
        return new SuggestParcelables$Action(bundle);
    }

    @Nullable
    public String getId() {
        return this.id;
    }

    @Nullable
    public String getDisplayName() {
        return this.displayName;
    }

    @Nullable
    public String getFullDisplayName() {
        return this.fullDisplayName;
    }

    @Nullable
    public SuggestParcelables$IntentInfo getProxiedIntentInfo() {
        return this.proxiedIntentInfo;
    }

    public boolean getHasProxiedIntentInfo() {
        return this.hasProxiedIntentInfo;
    }

    public Bundle writeToBundle() {
        Bundle bundle = new Bundle();
        bundle.putString("id", this.id);
        bundle.putString("displayName", this.displayName);
        bundle.putString("dEPRECATEDIconBitmapId", this.dEPRECATEDIconBitmapId);
        bundle.putString("fullDisplayName", this.fullDisplayName);
        SuggestParcelables$IntentInfo suggestParcelables$IntentInfo = this.dEPRECATEDIntentInfo;
        if (suggestParcelables$IntentInfo == null) {
            bundle.putBundle("dEPRECATEDIntentInfo", null);
        } else {
            bundle.putBundle("dEPRECATEDIntentInfo", suggestParcelables$IntentInfo.writeToBundle());
        }
        SuggestParcelables$IntentInfo suggestParcelables$IntentInfo2 = this.proxiedIntentInfo;
        if (suggestParcelables$IntentInfo2 == null) {
            bundle.putBundle("proxiedIntentInfo", null);
        } else {
            bundle.putBundle("proxiedIntentInfo", suggestParcelables$IntentInfo2.writeToBundle());
        }
        bundle.putString("opaquePayload", this.opaquePayload);
        return bundle;
    }

    private void readFromBundle(Bundle bundle) {
        if (!bundle.containsKey("id")) {
            this.hasId = false;
        } else {
            this.hasId = true;
            this.id = bundle.getString("id");
        }
        if (!bundle.containsKey("displayName")) {
            this.hasDisplayName = false;
        } else {
            this.hasDisplayName = true;
            this.displayName = bundle.getString("displayName");
        }
        if (!bundle.containsKey("dEPRECATEDIconBitmapId")) {
            this.hasDEPRECATEDIconBitmapId = false;
        } else {
            this.hasDEPRECATEDIconBitmapId = true;
            this.dEPRECATEDIconBitmapId = bundle.getString("dEPRECATEDIconBitmapId");
        }
        if (!bundle.containsKey("fullDisplayName")) {
            this.hasFullDisplayName = false;
        } else {
            this.hasFullDisplayName = true;
            this.fullDisplayName = bundle.getString("fullDisplayName");
        }
        if (!bundle.containsKey("dEPRECATEDIntentInfo")) {
            this.hasDEPRECATEDIntentInfo = false;
        } else {
            this.hasDEPRECATEDIntentInfo = true;
            Bundle bundle2 = bundle.getBundle("dEPRECATEDIntentInfo");
            if (bundle2 == null) {
                this.dEPRECATEDIntentInfo = null;
            } else {
                this.dEPRECATEDIntentInfo = SuggestParcelables$IntentInfo.create(bundle2);
            }
        }
        if (!bundle.containsKey("proxiedIntentInfo")) {
            this.hasProxiedIntentInfo = false;
        } else {
            this.hasProxiedIntentInfo = true;
            Bundle bundle3 = bundle.getBundle("proxiedIntentInfo");
            if (bundle3 == null) {
                this.proxiedIntentInfo = null;
            } else {
                this.proxiedIntentInfo = SuggestParcelables$IntentInfo.create(bundle3);
            }
        }
        if (!bundle.containsKey("opaquePayload")) {
            this.hasOpaquePayload = false;
            return;
        }
        this.hasOpaquePayload = true;
        this.opaquePayload = bundle.getString("opaquePayload");
    }
}
