package com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
/* loaded from: classes2.dex */
public final class SuggestParcelables$Entities {
    @Nullable
    private SuggestParcelables$DebugInfo debugInfo;
    @Nullable
    private List<SuggestParcelables$Entity> entities;
    @Nullable
    private SuggestParcelables$ExtrasInfo extrasInfo;
    private boolean hasDebugInfo;
    private boolean hasEntities;
    private boolean hasExtrasInfo;
    private boolean hasId;
    private boolean hasOpaquePayload;
    private boolean hasSetupInfo;
    private boolean hasStats;
    private boolean hasSuccess;
    @Nullable
    private String id;
    @Nullable
    private String opaquePayload;
    @Nullable
    private SuggestParcelables$SetupInfo setupInfo;
    @Nullable
    private SuggestParcelables$Stats stats;
    private boolean success;

    private SuggestParcelables$Entities(Bundle bundle) {
        readFromBundle(bundle);
    }

    private SuggestParcelables$Entities() {
    }

    public static SuggestParcelables$Entities create(Bundle bundle) {
        return new SuggestParcelables$Entities(bundle);
    }

    public static SuggestParcelables$Entities create() {
        return new SuggestParcelables$Entities();
    }

    @Nullable
    public List<SuggestParcelables$Entity> getEntities() {
        return this.entities;
    }

    @Nullable
    public SuggestParcelables$ExtrasInfo getExtrasInfo() {
        return this.extrasInfo;
    }

    public Bundle writeToBundle() {
        Bundle bundle = new Bundle();
        bundle.putString("id", this.id);
        bundle.putBoolean("success", this.success);
        if (this.entities == null) {
            bundle.putParcelableArrayList("entities", null);
        } else {
            ArrayList<? extends Parcelable> arrayList = new ArrayList<>(this.entities.size());
            for (SuggestParcelables$Entity suggestParcelables$Entity : this.entities) {
                if (suggestParcelables$Entity == null) {
                    arrayList.add(null);
                } else {
                    arrayList.add(suggestParcelables$Entity.writeToBundle());
                }
            }
            bundle.putParcelableArrayList("entities", arrayList);
        }
        SuggestParcelables$Stats suggestParcelables$Stats = this.stats;
        if (suggestParcelables$Stats == null) {
            bundle.putBundle("stats", null);
        } else {
            bundle.putBundle("stats", suggestParcelables$Stats.writeToBundle());
        }
        SuggestParcelables$DebugInfo suggestParcelables$DebugInfo = this.debugInfo;
        if (suggestParcelables$DebugInfo == null) {
            bundle.putBundle("debugInfo", null);
        } else {
            bundle.putBundle("debugInfo", suggestParcelables$DebugInfo.writeToBundle());
        }
        SuggestParcelables$ExtrasInfo suggestParcelables$ExtrasInfo = this.extrasInfo;
        if (suggestParcelables$ExtrasInfo == null) {
            bundle.putBundle("extrasInfo", null);
        } else {
            bundle.putBundle("extrasInfo", suggestParcelables$ExtrasInfo.writeToBundle());
        }
        bundle.putString("opaquePayload", this.opaquePayload);
        SuggestParcelables$SetupInfo suggestParcelables$SetupInfo = this.setupInfo;
        if (suggestParcelables$SetupInfo == null) {
            bundle.putBundle("setupInfo", null);
        } else {
            bundle.putBundle("setupInfo", suggestParcelables$SetupInfo.writeToBundle());
        }
        return bundle;
    }

    private void readFromBundle(Bundle bundle) {
        if (!bundle.containsKey("id")) {
            this.hasId = false;
        } else {
            this.hasId = true;
            this.id = bundle.getString("id");
        }
        if (!bundle.containsKey("success")) {
            this.hasSuccess = false;
        } else {
            this.hasSuccess = true;
            this.success = bundle.getBoolean("success");
        }
        if (!bundle.containsKey("entities")) {
            this.hasEntities = false;
        } else {
            this.hasEntities = true;
            ArrayList parcelableArrayList = bundle.getParcelableArrayList("entities");
            if (parcelableArrayList == null) {
                this.entities = null;
            } else {
                this.entities = new ArrayList(parcelableArrayList.size());
                Iterator it = parcelableArrayList.iterator();
                while (it.hasNext()) {
                    Bundle bundle2 = (Bundle) it.next();
                    if (bundle2 == null) {
                        this.entities.add(null);
                    } else {
                        this.entities.add(SuggestParcelables$Entity.create(bundle2));
                    }
                }
            }
        }
        if (!bundle.containsKey("stats")) {
            this.hasStats = false;
        } else {
            this.hasStats = true;
            Bundle bundle3 = bundle.getBundle("stats");
            if (bundle3 == null) {
                this.stats = null;
            } else {
                this.stats = SuggestParcelables$Stats.create(bundle3);
            }
        }
        if (!bundle.containsKey("debugInfo")) {
            this.hasDebugInfo = false;
        } else {
            this.hasDebugInfo = true;
            Bundle bundle4 = bundle.getBundle("debugInfo");
            if (bundle4 == null) {
                this.debugInfo = null;
            } else {
                this.debugInfo = SuggestParcelables$DebugInfo.create(bundle4);
            }
        }
        if (!bundle.containsKey("extrasInfo")) {
            this.hasExtrasInfo = false;
        } else {
            this.hasExtrasInfo = true;
            Bundle bundle5 = bundle.getBundle("extrasInfo");
            if (bundle5 == null) {
                this.extrasInfo = null;
            } else {
                this.extrasInfo = SuggestParcelables$ExtrasInfo.create(bundle5);
            }
        }
        if (!bundle.containsKey("opaquePayload")) {
            this.hasOpaquePayload = false;
        } else {
            this.hasOpaquePayload = true;
            this.opaquePayload = bundle.getString("opaquePayload");
        }
        if (!bundle.containsKey("setupInfo")) {
            this.hasSetupInfo = false;
            return;
        }
        this.hasSetupInfo = true;
        Bundle bundle6 = bundle.getBundle("setupInfo");
        if (bundle6 == null) {
            this.setupInfo = null;
        } else {
            this.setupInfo = SuggestParcelables$SetupInfo.create(bundle6);
        }
    }
}
