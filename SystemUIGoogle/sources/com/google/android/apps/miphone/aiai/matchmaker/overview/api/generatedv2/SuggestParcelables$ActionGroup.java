package com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
/* loaded from: classes2.dex */
public final class SuggestParcelables$ActionGroup {
    @Nullable
    private List<SuggestParcelables$Action> alternateActions;
    @Nullable
    private String displayName;
    private boolean hasAlternateActions;
    private boolean hasDisplayName;
    private boolean hasId;
    private boolean hasIsHiddenAction;
    private boolean hasMainAction;
    private boolean hasOpaquePayload;
    @Nullable
    private String id;
    private boolean isHiddenAction;
    @Nullable
    private SuggestParcelables$Action mainAction;
    @Nullable
    private String opaquePayload;

    private SuggestParcelables$ActionGroup(Bundle bundle) {
        readFromBundle(bundle);
    }

    private SuggestParcelables$ActionGroup() {
    }

    public static SuggestParcelables$ActionGroup create(Bundle bundle) {
        return new SuggestParcelables$ActionGroup(bundle);
    }

    @Nullable
    public SuggestParcelables$Action getMainAction() {
        return this.mainAction;
    }

    public Bundle writeToBundle() {
        Bundle bundle = new Bundle();
        bundle.putString("id", this.id);
        bundle.putString("displayName", this.displayName);
        SuggestParcelables$Action suggestParcelables$Action = this.mainAction;
        if (suggestParcelables$Action == null) {
            bundle.putBundle("mainAction", null);
        } else {
            bundle.putBundle("mainAction", suggestParcelables$Action.writeToBundle());
        }
        if (this.alternateActions == null) {
            bundle.putParcelableArrayList("alternateActions", null);
        } else {
            ArrayList<? extends Parcelable> arrayList = new ArrayList<>(this.alternateActions.size());
            for (SuggestParcelables$Action suggestParcelables$Action2 : this.alternateActions) {
                if (suggestParcelables$Action2 == null) {
                    arrayList.add(null);
                } else {
                    arrayList.add(suggestParcelables$Action2.writeToBundle());
                }
            }
            bundle.putParcelableArrayList("alternateActions", arrayList);
        }
        bundle.putBoolean("isHiddenAction", this.isHiddenAction);
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
        if (!bundle.containsKey("mainAction")) {
            this.hasMainAction = false;
        } else {
            this.hasMainAction = true;
            Bundle bundle2 = bundle.getBundle("mainAction");
            if (bundle2 == null) {
                this.mainAction = null;
            } else {
                this.mainAction = SuggestParcelables$Action.create(bundle2);
            }
        }
        if (!bundle.containsKey("alternateActions")) {
            this.hasAlternateActions = false;
        } else {
            this.hasAlternateActions = true;
            ArrayList parcelableArrayList = bundle.getParcelableArrayList("alternateActions");
            if (parcelableArrayList == null) {
                this.alternateActions = null;
            } else {
                this.alternateActions = new ArrayList(parcelableArrayList.size());
                Iterator it = parcelableArrayList.iterator();
                while (it.hasNext()) {
                    Bundle bundle3 = (Bundle) it.next();
                    if (bundle3 == null) {
                        this.alternateActions.add(null);
                    } else {
                        this.alternateActions.add(SuggestParcelables$Action.create(bundle3));
                    }
                }
            }
        }
        if (!bundle.containsKey("isHiddenAction")) {
            this.hasIsHiddenAction = false;
        } else {
            this.hasIsHiddenAction = true;
            this.isHiddenAction = bundle.getBoolean("isHiddenAction");
        }
        if (!bundle.containsKey("opaquePayload")) {
            this.hasOpaquePayload = false;
            return;
        }
        this.hasOpaquePayload = true;
        this.opaquePayload = bundle.getString("opaquePayload");
    }
}
