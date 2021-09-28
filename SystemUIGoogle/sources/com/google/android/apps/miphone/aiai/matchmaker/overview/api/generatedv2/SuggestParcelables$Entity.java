package com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
/* loaded from: classes2.dex */
public final class SuggestParcelables$Entity {
    @Nullable
    private List<SuggestParcelables$ActionGroup> actions;
    private float annotationScore;
    @Nullable
    private String annotationSourceName;
    @Nullable
    private String annotationTypeName;
    private int contentGroupIndex;
    private int endIndex;
    @Nullable
    private List<SuggestParcelables$EntitySpan> entitySpans;
    private boolean hasActions;
    private boolean hasAnnotationScore;
    private boolean hasAnnotationSourceName;
    private boolean hasAnnotationTypeName;
    private boolean hasContentGroupIndex;
    private boolean hasEndIndex;
    private boolean hasEntitySpans;
    private boolean hasId;
    private boolean hasInteractionType;
    private boolean hasIsSmartSelection;
    private boolean hasNumWords;
    private boolean hasOpaquePayload;
    private boolean hasSearchQueryHint;
    private boolean hasSelectionIndex;
    private boolean hasShouldStartForResult;
    private boolean hasStartIndex;
    private boolean hasSuggestedPresentationMode;
    private boolean hasVerticalTypeName;
    @Nullable
    private String id;
    private SuggestParcelables$InteractionType interactionType;
    private boolean isSmartSelection;
    private int numWords;
    @Nullable
    private String opaquePayload;
    @Nullable
    private String searchQueryHint;
    private int selectionIndex;
    private boolean shouldStartForResult;
    private int startIndex;
    private int suggestedPresentationMode;
    @Nullable
    private String verticalTypeName;

    private SuggestParcelables$Entity(Bundle bundle) {
        readFromBundle(bundle);
    }

    private SuggestParcelables$Entity() {
    }

    public static SuggestParcelables$Entity create(Bundle bundle) {
        return new SuggestParcelables$Entity(bundle);
    }

    @Nullable
    public List<SuggestParcelables$ActionGroup> getActions() {
        return this.actions;
    }

    @Nullable
    public String getSearchQueryHint() {
        return this.searchQueryHint;
    }

    public Bundle writeToBundle() {
        Bundle bundle = new Bundle();
        bundle.putString("id", this.id);
        if (this.actions == null) {
            bundle.putParcelableArrayList("actions", null);
        } else {
            ArrayList<? extends Parcelable> arrayList = new ArrayList<>(this.actions.size());
            for (SuggestParcelables$ActionGroup suggestParcelables$ActionGroup : this.actions) {
                if (suggestParcelables$ActionGroup == null) {
                    arrayList.add(null);
                } else {
                    arrayList.add(suggestParcelables$ActionGroup.writeToBundle());
                }
            }
            bundle.putParcelableArrayList("actions", arrayList);
        }
        if (this.entitySpans == null) {
            bundle.putParcelableArrayList("entitySpans", null);
        } else {
            ArrayList<? extends Parcelable> arrayList2 = new ArrayList<>(this.entitySpans.size());
            for (SuggestParcelables$EntitySpan suggestParcelables$EntitySpan : this.entitySpans) {
                if (suggestParcelables$EntitySpan == null) {
                    arrayList2.add(null);
                } else {
                    arrayList2.add(suggestParcelables$EntitySpan.writeToBundle());
                }
            }
            bundle.putParcelableArrayList("entitySpans", arrayList2);
        }
        bundle.putString("searchQueryHint", this.searchQueryHint);
        bundle.putString("annotationTypeName", this.annotationTypeName);
        bundle.putString("annotationSourceName", this.annotationSourceName);
        bundle.putString("verticalTypeName", this.verticalTypeName);
        bundle.putFloat("annotationScore", this.annotationScore);
        bundle.putInt("contentGroupIndex", this.contentGroupIndex);
        bundle.putInt("selectionIndex", this.selectionIndex);
        bundle.putBoolean("isSmartSelection", this.isSmartSelection);
        bundle.putInt("suggestedPresentationMode", this.suggestedPresentationMode);
        bundle.putInt("numWords", this.numWords);
        bundle.putInt("startIndex", this.startIndex);
        bundle.putInt("endIndex", this.endIndex);
        bundle.putString("opaquePayload", this.opaquePayload);
        SuggestParcelables$InteractionType suggestParcelables$InteractionType = this.interactionType;
        if (suggestParcelables$InteractionType == null) {
            bundle.putBundle("interactionType", null);
        } else {
            bundle.putBundle("interactionType", suggestParcelables$InteractionType.writeToBundle());
        }
        bundle.putBoolean("shouldStartForResult", this.shouldStartForResult);
        return bundle;
    }

    private void readFromBundle(Bundle bundle) {
        if (!bundle.containsKey("id")) {
            this.hasId = false;
        } else {
            this.hasId = true;
            this.id = bundle.getString("id");
        }
        if (!bundle.containsKey("actions")) {
            this.hasActions = false;
        } else {
            this.hasActions = true;
            ArrayList parcelableArrayList = bundle.getParcelableArrayList("actions");
            if (parcelableArrayList == null) {
                this.actions = null;
            } else {
                this.actions = new ArrayList(parcelableArrayList.size());
                Iterator it = parcelableArrayList.iterator();
                while (it.hasNext()) {
                    Bundle bundle2 = (Bundle) it.next();
                    if (bundle2 == null) {
                        this.actions.add(null);
                    } else {
                        this.actions.add(SuggestParcelables$ActionGroup.create(bundle2));
                    }
                }
            }
        }
        if (!bundle.containsKey("entitySpans")) {
            this.hasEntitySpans = false;
        } else {
            this.hasEntitySpans = true;
            ArrayList parcelableArrayList2 = bundle.getParcelableArrayList("entitySpans");
            if (parcelableArrayList2 == null) {
                this.entitySpans = null;
            } else {
                this.entitySpans = new ArrayList(parcelableArrayList2.size());
                Iterator it2 = parcelableArrayList2.iterator();
                while (it2.hasNext()) {
                    Bundle bundle3 = (Bundle) it2.next();
                    if (bundle3 == null) {
                        this.entitySpans.add(null);
                    } else {
                        this.entitySpans.add(SuggestParcelables$EntitySpan.create(bundle3));
                    }
                }
            }
        }
        if (!bundle.containsKey("searchQueryHint")) {
            this.hasSearchQueryHint = false;
        } else {
            this.hasSearchQueryHint = true;
            this.searchQueryHint = bundle.getString("searchQueryHint");
        }
        if (!bundle.containsKey("annotationTypeName")) {
            this.hasAnnotationTypeName = false;
        } else {
            this.hasAnnotationTypeName = true;
            this.annotationTypeName = bundle.getString("annotationTypeName");
        }
        if (!bundle.containsKey("annotationSourceName")) {
            this.hasAnnotationSourceName = false;
        } else {
            this.hasAnnotationSourceName = true;
            this.annotationSourceName = bundle.getString("annotationSourceName");
        }
        if (!bundle.containsKey("verticalTypeName")) {
            this.hasVerticalTypeName = false;
        } else {
            this.hasVerticalTypeName = true;
            this.verticalTypeName = bundle.getString("verticalTypeName");
        }
        if (!bundle.containsKey("annotationScore")) {
            this.hasAnnotationScore = false;
        } else {
            this.hasAnnotationScore = true;
            this.annotationScore = bundle.getFloat("annotationScore");
        }
        if (!bundle.containsKey("contentGroupIndex")) {
            this.hasContentGroupIndex = false;
        } else {
            this.hasContentGroupIndex = true;
            this.contentGroupIndex = bundle.getInt("contentGroupIndex");
        }
        if (!bundle.containsKey("selectionIndex")) {
            this.hasSelectionIndex = false;
        } else {
            this.hasSelectionIndex = true;
            this.selectionIndex = bundle.getInt("selectionIndex");
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
        if (!bundle.containsKey("numWords")) {
            this.hasNumWords = false;
        } else {
            this.hasNumWords = true;
            this.numWords = bundle.getInt("numWords");
        }
        if (!bundle.containsKey("startIndex")) {
            this.hasStartIndex = false;
        } else {
            this.hasStartIndex = true;
            this.startIndex = bundle.getInt("startIndex");
        }
        if (!bundle.containsKey("endIndex")) {
            this.hasEndIndex = false;
        } else {
            this.hasEndIndex = true;
            this.endIndex = bundle.getInt("endIndex");
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
            Bundle bundle4 = bundle.getBundle("interactionType");
            if (bundle4 == null) {
                this.interactionType = null;
            } else {
                this.interactionType = SuggestParcelables$InteractionType.create(bundle4);
            }
            if (this.interactionType == null) {
                this.hasInteractionType = false;
            }
        }
        if (!bundle.containsKey("shouldStartForResult")) {
            this.hasShouldStartForResult = false;
            return;
        }
        this.hasShouldStartForResult = true;
        this.shouldStartForResult = bundle.getBoolean("shouldStartForResult");
    }
}
