package com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes2.dex */
public final class InteractionContextParcelables$InteractionContext {
    private boolean disallowCopyPaste;
    private boolean expandFocusRect;
    @Nullable
    private SuggestParcelables$OnScreenRect focusRect;
    private int focusRectExpandPx;
    private boolean hasDisallowCopyPaste;
    private boolean hasInteractionType;
    private boolean hasIsPrimaryTask;
    private boolean hasVersionCode;
    @Nullable
    private List<InteractionContextParcelables$InteractionEvent> interactionEvents;
    private SuggestParcelables$InteractionType interactionType;
    private boolean isPrimaryTask;
    private boolean isRtlContent;
    @Nullable
    private ContentParcelables$Contents previousContents;
    private boolean requestDebugInfo;
    private boolean requestStats;
    private long screenSessionId;
    private int versionCode;

    private InteractionContextParcelables$InteractionContext() {
    }

    public static InteractionContextParcelables$InteractionContext create() {
        return new InteractionContextParcelables$InteractionContext();
    }

    public void setDisallowCopyPaste(boolean z) {
        this.disallowCopyPaste = z;
        this.hasDisallowCopyPaste = true;
    }

    public void setVersionCode(int i) {
        this.versionCode = i;
        this.hasVersionCode = true;
    }

    public void setInteractionType(SuggestParcelables$InteractionType suggestParcelables$InteractionType) {
        this.interactionType = suggestParcelables$InteractionType;
        this.hasInteractionType = suggestParcelables$InteractionType != null;
    }

    public void setIsPrimaryTask(boolean z) {
        this.isPrimaryTask = z;
        this.hasIsPrimaryTask = true;
    }

    public Bundle writeToBundle() {
        Bundle bundle = new Bundle();
        bundle.putLong("screenSessionId", this.screenSessionId);
        SuggestParcelables$OnScreenRect suggestParcelables$OnScreenRect = this.focusRect;
        if (suggestParcelables$OnScreenRect == null) {
            bundle.putBundle("focusRect", null);
        } else {
            bundle.putBundle("focusRect", suggestParcelables$OnScreenRect.writeToBundle());
        }
        bundle.putBoolean("expandFocusRect", this.expandFocusRect);
        bundle.putInt("focusRectExpandPx", this.focusRectExpandPx);
        ContentParcelables$Contents contentParcelables$Contents = this.previousContents;
        if (contentParcelables$Contents == null) {
            bundle.putBundle("previousContents", null);
        } else {
            bundle.putBundle("previousContents", contentParcelables$Contents.writeToBundle());
        }
        bundle.putBoolean("requestStats", this.requestStats);
        bundle.putBoolean("requestDebugInfo", this.requestDebugInfo);
        bundle.putBoolean("isRtlContent", this.isRtlContent);
        bundle.putBoolean("disallowCopyPaste", this.disallowCopyPaste);
        bundle.putInt("versionCode", this.versionCode);
        if (this.interactionEvents == null) {
            bundle.putParcelableArrayList("interactionEvents", null);
        } else {
            ArrayList<? extends Parcelable> arrayList = new ArrayList<>(this.interactionEvents.size());
            for (InteractionContextParcelables$InteractionEvent interactionContextParcelables$InteractionEvent : this.interactionEvents) {
                if (interactionContextParcelables$InteractionEvent == null) {
                    arrayList.add(null);
                } else {
                    arrayList.add(interactionContextParcelables$InteractionEvent.writeToBundle());
                }
            }
            bundle.putParcelableArrayList("interactionEvents", arrayList);
        }
        SuggestParcelables$InteractionType suggestParcelables$InteractionType = this.interactionType;
        if (suggestParcelables$InteractionType == null) {
            bundle.putBundle("interactionType", null);
        } else {
            bundle.putBundle("interactionType", suggestParcelables$InteractionType.writeToBundle());
        }
        bundle.putBoolean("isPrimaryTask", this.isPrimaryTask);
        return bundle;
    }
}
