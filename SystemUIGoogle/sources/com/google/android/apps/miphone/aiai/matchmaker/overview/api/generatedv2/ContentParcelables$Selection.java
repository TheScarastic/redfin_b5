package com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes2.dex */
public final class ContentParcelables$Selection {
    private int contentGroupIndex;
    private boolean hasContentGroupIndex;
    private boolean hasId;
    private boolean hasInteractionType;
    private boolean hasIsSmartSelection;
    private boolean hasOpaquePayload;
    private boolean hasRectIndices;
    private boolean hasSuggestedPresentationMode;
    @Nullable
    private String id;
    private SuggestParcelables$InteractionType interactionType;
    private boolean isSmartSelection;
    @Nullable
    private String opaquePayload;
    @Nullable
    private List<Integer> rectIndices;
    private int suggestedPresentationMode;

    private ContentParcelables$Selection(Bundle bundle) {
        readFromBundle(bundle);
    }

    private ContentParcelables$Selection() {
    }

    public static ContentParcelables$Selection create(Bundle bundle) {
        return new ContentParcelables$Selection(bundle);
    }

    public Bundle writeToBundle() {
        Bundle bundle = new Bundle();
        if (this.rectIndices == null) {
            bundle.putIntegerArrayList("rectIndices", null);
        } else {
            bundle.putIntegerArrayList("rectIndices", new ArrayList<>(this.rectIndices));
        }
        bundle.putString("id", this.id);
        bundle.putBoolean("isSmartSelection", this.isSmartSelection);
        bundle.putInt("suggestedPresentationMode", this.suggestedPresentationMode);
        bundle.putString("opaquePayload", this.opaquePayload);
        SuggestParcelables$InteractionType suggestParcelables$InteractionType = this.interactionType;
        if (suggestParcelables$InteractionType == null) {
            bundle.putBundle("interactionType", null);
        } else {
            bundle.putBundle("interactionType", suggestParcelables$InteractionType.writeToBundle());
        }
        bundle.putInt("contentGroupIndex", this.contentGroupIndex);
        return bundle;
    }

    private void readFromBundle(Bundle bundle) {
        if (!bundle.containsKey("rectIndices")) {
            this.hasRectIndices = false;
        } else {
            this.hasRectIndices = true;
            this.rectIndices = bundle.getIntegerArrayList("rectIndices");
        }
        if (!bundle.containsKey("id")) {
            this.hasId = false;
        } else {
            this.hasId = true;
            this.id = bundle.getString("id");
        }
        if (!bundle.containsKey("isSmartSelection")) {
            this.hasIsSmartSelection = false;
        } else {
            this.hasIsSmartSelection = true;
            this.isSmartSelection = bundle.getBoolean("isSmartSelection");
        }
        if (!bundle.containsKey("suggestedPresentationMode")) {
            this.hasSuggestedPresentationMode = false;
        } else {
            this.hasSuggestedPresentationMode = true;
            this.suggestedPresentationMode = bundle.getInt("suggestedPresentationMode");
        }
        if (!bundle.containsKey("opaquePayload")) {
            this.hasOpaquePayload = false;
        } else {
            this.hasOpaquePayload = true;
            this.opaquePayload = bundle.getString("opaquePayload");
        }
        if (!bundle.containsKey("interactionType")) {
            this.hasInteractionType = false;
        } else {
            this.hasInteractionType = true;
            Bundle bundle2 = bundle.getBundle("interactionType");
            if (bundle2 == null) {
                this.interactionType = null;
            } else {
                this.interactionType = SuggestParcelables$InteractionType.create(bundle2);
            }
            if (this.interactionType == null) {
                this.hasInteractionType = false;
            }
        }
        if (!bundle.containsKey("contentGroupIndex")) {
            this.hasContentGroupIndex = false;
            return;
        }
        this.hasContentGroupIndex = true;
        this.contentGroupIndex = bundle.getInt("contentGroupIndex");
    }
}
