package com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2;

import android.os.Bundle;
import android.support.annotation.Nullable;
/* loaded from: classes2.dex */
public final class ContentParcelables$SearchSuggestion {
    @Nullable
    private ContentParcelables$AppActionSuggestion appActionSuggestion;
    @Nullable
    private ContentParcelables$AppIcon appIcon;
    private float confScore;
    @Nullable
    private ContentParcelables$ExecutionInfo executionInfo;
    private boolean hasAppActionSuggestion;
    private boolean hasAppIcon;
    private boolean hasConfScore;
    private boolean hasExecutionInfo;

    private ContentParcelables$SearchSuggestion(Bundle bundle) {
        readFromBundle(bundle);
    }

    private ContentParcelables$SearchSuggestion() {
    }

    public static ContentParcelables$SearchSuggestion create(Bundle bundle) {
        return new ContentParcelables$SearchSuggestion(bundle);
    }

    public Bundle writeToBundle() {
        Bundle bundle = new Bundle();
        ContentParcelables$AppActionSuggestion contentParcelables$AppActionSuggestion = this.appActionSuggestion;
        if (contentParcelables$AppActionSuggestion == null) {
            bundle.putBundle("appActionSuggestion", null);
        } else {
            bundle.putBundle("appActionSuggestion", contentParcelables$AppActionSuggestion.writeToBundle());
        }
        ContentParcelables$AppIcon contentParcelables$AppIcon = this.appIcon;
        if (contentParcelables$AppIcon == null) {
            bundle.putBundle("appIcon", null);
        } else {
            bundle.putBundle("appIcon", contentParcelables$AppIcon.writeToBundle());
        }
        ContentParcelables$ExecutionInfo contentParcelables$ExecutionInfo = this.executionInfo;
        if (contentParcelables$ExecutionInfo == null) {
            bundle.putBundle("executionInfo", null);
        } else {
            bundle.putBundle("executionInfo", contentParcelables$ExecutionInfo.writeToBundle());
        }
        bundle.putFloat("confScore", this.confScore);
        return bundle;
    }

    private void readFromBundle(Bundle bundle) {
        if (!bundle.containsKey("appActionSuggestion")) {
            this.hasAppActionSuggestion = false;
        } else {
            this.hasAppActionSuggestion = true;
            Bundle bundle2 = bundle.getBundle("appActionSuggestion");
            if (bundle2 == null) {
                this.appActionSuggestion = null;
            } else {
                this.appActionSuggestion = ContentParcelables$AppActionSuggestion.create(bundle2);
            }
        }
        if (!bundle.containsKey("appIcon")) {
            this.hasAppIcon = false;
        } else {
            this.hasAppIcon = true;
            Bundle bundle3 = bundle.getBundle("appIcon");
            if (bundle3 == null) {
                this.appIcon = null;
            } else {
                this.appIcon = ContentParcelables$AppIcon.create(bundle3);
            }
        }
        if (!bundle.containsKey("executionInfo")) {
            this.hasExecutionInfo = false;
        } else {
            this.hasExecutionInfo = true;
            Bundle bundle4 = bundle.getBundle("executionInfo");
            if (bundle4 == null) {
                this.executionInfo = null;
            } else {
                this.executionInfo = ContentParcelables$ExecutionInfo.create(bundle4);
            }
        }
        if (!bundle.containsKey("confScore")) {
            this.hasConfScore = false;
            return;
        }
        this.hasConfScore = true;
        this.confScore = bundle.getFloat("confScore");
    }
}
