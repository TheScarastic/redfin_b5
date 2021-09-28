package com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
/* loaded from: classes2.dex */
public final class ContentParcelables$ContentGroup {
    @Nullable
    private List<SuggestParcelables$ContentRect> contentRects;
    private boolean hasContentRects;
    private boolean hasNumLines;
    private boolean hasSearchSuggestions;
    private boolean hasSelections;
    private boolean hasText;
    private int numLines;
    @Nullable
    private List<ContentParcelables$SearchSuggestion> searchSuggestions;
    @Nullable
    private List<ContentParcelables$Selection> selections;
    @Nullable
    private String text;

    private ContentParcelables$ContentGroup(Bundle bundle) {
        readFromBundle(bundle);
    }

    private ContentParcelables$ContentGroup() {
    }

    public static ContentParcelables$ContentGroup create(Bundle bundle) {
        return new ContentParcelables$ContentGroup(bundle);
    }

    public Bundle writeToBundle() {
        Bundle bundle = new Bundle();
        if (this.contentRects == null) {
            bundle.putParcelableArrayList("contentRects", null);
        } else {
            ArrayList<? extends Parcelable> arrayList = new ArrayList<>(this.contentRects.size());
            for (SuggestParcelables$ContentRect suggestParcelables$ContentRect : this.contentRects) {
                if (suggestParcelables$ContentRect == null) {
                    arrayList.add(null);
                } else {
                    arrayList.add(suggestParcelables$ContentRect.writeToBundle());
                }
            }
            bundle.putParcelableArrayList("contentRects", arrayList);
        }
        if (this.selections == null) {
            bundle.putParcelableArrayList("selections", null);
        } else {
            ArrayList<? extends Parcelable> arrayList2 = new ArrayList<>(this.selections.size());
            for (ContentParcelables$Selection contentParcelables$Selection : this.selections) {
                if (contentParcelables$Selection == null) {
                    arrayList2.add(null);
                } else {
                    arrayList2.add(contentParcelables$Selection.writeToBundle());
                }
            }
            bundle.putParcelableArrayList("selections", arrayList2);
        }
        bundle.putString("text", this.text);
        bundle.putInt("numLines", this.numLines);
        if (this.searchSuggestions == null) {
            bundle.putParcelableArrayList("searchSuggestions", null);
        } else {
            ArrayList<? extends Parcelable> arrayList3 = new ArrayList<>(this.searchSuggestions.size());
            for (ContentParcelables$SearchSuggestion contentParcelables$SearchSuggestion : this.searchSuggestions) {
                if (contentParcelables$SearchSuggestion == null) {
                    arrayList3.add(null);
                } else {
                    arrayList3.add(contentParcelables$SearchSuggestion.writeToBundle());
                }
            }
            bundle.putParcelableArrayList("searchSuggestions", arrayList3);
        }
        return bundle;
    }

    private void readFromBundle(Bundle bundle) {
        if (!bundle.containsKey("contentRects")) {
            this.hasContentRects = false;
        } else {
            this.hasContentRects = true;
            ArrayList parcelableArrayList = bundle.getParcelableArrayList("contentRects");
            if (parcelableArrayList == null) {
                this.contentRects = null;
            } else {
                this.contentRects = new ArrayList(parcelableArrayList.size());
                Iterator it = parcelableArrayList.iterator();
                while (it.hasNext()) {
                    Bundle bundle2 = (Bundle) it.next();
                    if (bundle2 == null) {
                        this.contentRects.add(null);
                    } else {
                        this.contentRects.add(SuggestParcelables$ContentRect.create(bundle2));
                    }
                }
            }
        }
        if (!bundle.containsKey("selections")) {
            this.hasSelections = false;
        } else {
            this.hasSelections = true;
            ArrayList parcelableArrayList2 = bundle.getParcelableArrayList("selections");
            if (parcelableArrayList2 == null) {
                this.selections = null;
            } else {
                this.selections = new ArrayList(parcelableArrayList2.size());
                Iterator it2 = parcelableArrayList2.iterator();
                while (it2.hasNext()) {
                    Bundle bundle3 = (Bundle) it2.next();
                    if (bundle3 == null) {
                        this.selections.add(null);
                    } else {
                        this.selections.add(ContentParcelables$Selection.create(bundle3));
                    }
                }
            }
        }
        if (!bundle.containsKey("text")) {
            this.hasText = false;
        } else {
            this.hasText = true;
            this.text = bundle.getString("text");
        }
        if (!bundle.containsKey("numLines")) {
            this.hasNumLines = false;
        } else {
            this.hasNumLines = true;
            this.numLines = bundle.getInt("numLines");
        }
        if (!bundle.containsKey("searchSuggestions")) {
            this.hasSearchSuggestions = false;
            return;
        }
        this.hasSearchSuggestions = true;
        ArrayList parcelableArrayList3 = bundle.getParcelableArrayList("searchSuggestions");
        if (parcelableArrayList3 == null) {
            this.searchSuggestions = null;
            return;
        }
        this.searchSuggestions = new ArrayList(parcelableArrayList3.size());
        Iterator it3 = parcelableArrayList3.iterator();
        while (it3.hasNext()) {
            Bundle bundle4 = (Bundle) it3.next();
            if (bundle4 == null) {
                this.searchSuggestions.add(null);
            } else {
                this.searchSuggestions.add(ContentParcelables$SearchSuggestion.create(bundle4));
            }
        }
    }
}
