package com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2;

import android.os.Bundle;
import android.support.annotation.Nullable;
/* loaded from: classes2.dex */
public final class FeedbackParcelables$ActionMenuItem {
    @Nullable
    private SuggestParcelables$IntentInfo actionIntent;
    private ActionMenuItemDisplayMode displayMode;
    @Nullable
    private String displayName;
    @Nullable
    private String id;
    private int invokeRankIndex;

    /* loaded from: classes2.dex */
    public enum ActionMenuItemDisplayMode {
        UNKNOWN_DISPLAY_MODE(0),
        ON_PRIMARY_MENU(1),
        ON_OVERFLOW_MENU(2);
        
        public final int value;

        ActionMenuItemDisplayMode(int i) {
            this.value = i;
        }

        public Bundle writeToBundle() {
            Bundle bundle = new Bundle();
            bundle.putInt("value", this.value);
            return bundle;
        }
    }

    private FeedbackParcelables$ActionMenuItem() {
    }

    public Bundle writeToBundle() {
        Bundle bundle = new Bundle();
        bundle.putString("id", this.id);
        bundle.putString("displayName", this.displayName);
        bundle.putInt("invokeRankIndex", this.invokeRankIndex);
        ActionMenuItemDisplayMode actionMenuItemDisplayMode = this.displayMode;
        if (actionMenuItemDisplayMode == null) {
            bundle.putBundle("displayMode", null);
        } else {
            bundle.putBundle("displayMode", actionMenuItemDisplayMode.writeToBundle());
        }
        SuggestParcelables$IntentInfo suggestParcelables$IntentInfo = this.actionIntent;
        if (suggestParcelables$IntentInfo == null) {
            bundle.putBundle("actionIntent", null);
        } else {
            bundle.putBundle("actionIntent", suggestParcelables$IntentInfo.writeToBundle());
        }
        return bundle;
    }
}
