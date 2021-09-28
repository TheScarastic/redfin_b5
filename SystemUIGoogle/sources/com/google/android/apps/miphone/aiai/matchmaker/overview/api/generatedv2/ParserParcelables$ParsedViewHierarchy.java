package com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes2.dex */
public final class ParserParcelables$ParsedViewHierarchy {
    private long acquisitionEndTime;
    private long acquisitionStartTime;
    @Nullable
    private String activityClassName;
    private boolean hasKnownIssues;
    private boolean isHomeActivity;
    @Nullable
    private String packageName;
    @Nullable
    private List<ParserParcelables$WindowNode> windows;

    private ParserParcelables$ParsedViewHierarchy() {
    }

    public static ParserParcelables$ParsedViewHierarchy create() {
        return new ParserParcelables$ParsedViewHierarchy();
    }

    public Bundle writeToBundle() {
        Bundle bundle = new Bundle();
        bundle.putLong("acquisitionStartTime", this.acquisitionStartTime);
        bundle.putLong("acquisitionEndTime", this.acquisitionEndTime);
        bundle.putBoolean("isHomeActivity", this.isHomeActivity);
        if (this.windows == null) {
            bundle.putParcelableArrayList("windows", null);
        } else {
            ArrayList<? extends Parcelable> arrayList = new ArrayList<>(this.windows.size());
            for (ParserParcelables$WindowNode parserParcelables$WindowNode : this.windows) {
                if (parserParcelables$WindowNode == null) {
                    arrayList.add(null);
                } else {
                    arrayList.add(parserParcelables$WindowNode.writeToBundle());
                }
            }
            bundle.putParcelableArrayList("windows", arrayList);
        }
        bundle.putBoolean("hasKnownIssues", this.hasKnownIssues);
        bundle.putString("packageName", this.packageName);
        bundle.putString("activityClassName", this.activityClassName);
        bundle.putBundle("insetsRect", null);
        return bundle;
    }
}
