package com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
/* loaded from: classes2.dex */
public final class ContentParcelables$Contents {
    @Nullable
    private List<ContentParcelables$ContentGroup> contentGroups;
    @Nullable
    private String contentUri;
    @Nullable
    private SuggestParcelables$DebugInfo debugInfo;
    private boolean hasContentGroups;
    private boolean hasContentUri;
    private boolean hasDebugInfo;
    private boolean hasId;
    private boolean hasOpaquePayload;
    private boolean hasScreenSessionId;
    private boolean hasSetupInfo;
    private boolean hasStats;
    @Nullable
    private String id;
    @Nullable
    private String opaquePayload;
    private long screenSessionId;
    @Nullable
    private SuggestParcelables$SetupInfo setupInfo;
    @Nullable
    private SuggestParcelables$Stats stats;

    private ContentParcelables$Contents(Bundle bundle) {
        readFromBundle(bundle);
    }

    private ContentParcelables$Contents() {
    }

    public static ContentParcelables$Contents create(Bundle bundle) {
        return new ContentParcelables$Contents(bundle);
    }

    public static ContentParcelables$Contents create() {
        return new ContentParcelables$Contents();
    }

    @Nullable
    public SuggestParcelables$SetupInfo getSetupInfo() {
        return this.setupInfo;
    }

    public Bundle writeToBundle() {
        Bundle bundle = new Bundle();
        bundle.putString("id", this.id);
        bundle.putLong("screenSessionId", this.screenSessionId);
        if (this.contentGroups == null) {
            bundle.putParcelableArrayList("contentGroups", null);
        } else {
            ArrayList<? extends Parcelable> arrayList = new ArrayList<>(this.contentGroups.size());
            for (ContentParcelables$ContentGroup contentParcelables$ContentGroup : this.contentGroups) {
                if (contentParcelables$ContentGroup == null) {
                    arrayList.add(null);
                } else {
                    arrayList.add(contentParcelables$ContentGroup.writeToBundle());
                }
            }
            bundle.putParcelableArrayList("contentGroups", arrayList);
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
        bundle.putString("opaquePayload", this.opaquePayload);
        SuggestParcelables$SetupInfo suggestParcelables$SetupInfo = this.setupInfo;
        if (suggestParcelables$SetupInfo == null) {
            bundle.putBundle("setupInfo", null);
        } else {
            bundle.putBundle("setupInfo", suggestParcelables$SetupInfo.writeToBundle());
        }
        bundle.putString("contentUri", this.contentUri);
        return bundle;
    }

    private void readFromBundle(Bundle bundle) {
        if (!bundle.containsKey("id")) {
            this.hasId = false;
        } else {
            this.hasId = true;
            this.id = bundle.getString("id");
        }
        if (!bundle.containsKey("screenSessionId")) {
            this.hasScreenSessionId = false;
        } else {
            this.hasScreenSessionId = true;
            this.screenSessionId = bundle.getLong("screenSessionId");
        }
        if (!bundle.containsKey("contentGroups")) {
            this.hasContentGroups = false;
        } else {
            this.hasContentGroups = true;
            ArrayList parcelableArrayList = bundle.getParcelableArrayList("contentGroups");
            if (parcelableArrayList == null) {
                this.contentGroups = null;
            } else {
                this.contentGroups = new ArrayList(parcelableArrayList.size());
                Iterator it = parcelableArrayList.iterator();
                while (it.hasNext()) {
                    Bundle bundle2 = (Bundle) it.next();
                    if (bundle2 == null) {
                        this.contentGroups.add(null);
                    } else {
                        this.contentGroups.add(ContentParcelables$ContentGroup.create(bundle2));
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
        if (!bundle.containsKey("opaquePayload")) {
            this.hasOpaquePayload = false;
        } else {
            this.hasOpaquePayload = true;
            this.opaquePayload = bundle.getString("opaquePayload");
        }
        if (!bundle.containsKey("setupInfo")) {
            this.hasSetupInfo = false;
        } else {
            this.hasSetupInfo = true;
            Bundle bundle5 = bundle.getBundle("setupInfo");
            if (bundle5 == null) {
                this.setupInfo = null;
            } else {
                this.setupInfo = SuggestParcelables$SetupInfo.create(bundle5);
            }
        }
        if (!bundle.containsKey("contentUri")) {
            this.hasContentUri = false;
            return;
        }
        this.hasContentUri = true;
        this.contentUri = bundle.getString("contentUri");
    }
}
