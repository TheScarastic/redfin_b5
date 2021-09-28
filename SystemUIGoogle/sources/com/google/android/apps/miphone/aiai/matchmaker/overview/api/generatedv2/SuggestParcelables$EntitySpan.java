package com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
/* loaded from: classes2.dex */
public final class SuggestParcelables$EntitySpan {
    private boolean hasRectIndices;
    private boolean hasRects;
    private boolean hasSelectionId;
    @Nullable
    private List<Integer> rectIndices;
    @Nullable
    private List<SuggestParcelables$ContentRect> rects;
    @Nullable
    private String selectionId;

    private SuggestParcelables$EntitySpan(Bundle bundle) {
        readFromBundle(bundle);
    }

    private SuggestParcelables$EntitySpan() {
    }

    public static SuggestParcelables$EntitySpan create(Bundle bundle) {
        return new SuggestParcelables$EntitySpan(bundle);
    }

    public Bundle writeToBundle() {
        Bundle bundle = new Bundle();
        if (this.rects == null) {
            bundle.putParcelableArrayList("rects", null);
        } else {
            ArrayList<? extends Parcelable> arrayList = new ArrayList<>(this.rects.size());
            for (SuggestParcelables$ContentRect suggestParcelables$ContentRect : this.rects) {
                if (suggestParcelables$ContentRect == null) {
                    arrayList.add(null);
                } else {
                    arrayList.add(suggestParcelables$ContentRect.writeToBundle());
                }
            }
            bundle.putParcelableArrayList("rects", arrayList);
        }
        bundle.putString("selectionId", this.selectionId);
        if (this.rectIndices == null) {
            bundle.putIntegerArrayList("rectIndices", null);
        } else {
            bundle.putIntegerArrayList("rectIndices", new ArrayList<>(this.rectIndices));
        }
        return bundle;
    }

    private void readFromBundle(Bundle bundle) {
        if (!bundle.containsKey("rects")) {
            this.hasRects = false;
        } else {
            this.hasRects = true;
            ArrayList parcelableArrayList = bundle.getParcelableArrayList("rects");
            if (parcelableArrayList == null) {
                this.rects = null;
            } else {
                this.rects = new ArrayList(parcelableArrayList.size());
                Iterator it = parcelableArrayList.iterator();
                while (it.hasNext()) {
                    Bundle bundle2 = (Bundle) it.next();
                    if (bundle2 == null) {
                        this.rects.add(null);
                    } else {
                        this.rects.add(SuggestParcelables$ContentRect.create(bundle2));
                    }
                }
            }
        }
        if (!bundle.containsKey("selectionId")) {
            this.hasSelectionId = false;
        } else {
            this.hasSelectionId = true;
            this.selectionId = bundle.getString("selectionId");
        }
        if (!bundle.containsKey("rectIndices")) {
            this.hasRectIndices = false;
            return;
        }
        this.hasRectIndices = true;
        this.rectIndices = bundle.getIntegerArrayList("rectIndices");
    }
}
