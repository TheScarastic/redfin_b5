package com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2;

import android.os.Bundle;
import android.support.annotation.Nullable;
/* loaded from: classes2.dex */
public final class SuggestParcelables$ContentRect {
    private int beginChar;
    private int contentGroupIndex;
    private SuggestParcelables$ContentType contentType;
    @Nullable
    private String contentUri;
    private int endChar;
    private boolean hasBeginChar;
    private boolean hasContentGroupIndex;
    private boolean hasContentType;
    private boolean hasContentUri;
    private boolean hasEndChar;
    private boolean hasLineId;
    private boolean hasRect;
    private boolean hasText;
    private int lineId;
    @Nullable
    private SuggestParcelables$OnScreenRect rect;
    @Nullable
    private String text;

    private SuggestParcelables$ContentRect(Bundle bundle) {
        readFromBundle(bundle);
    }

    private SuggestParcelables$ContentRect() {
    }

    public static SuggestParcelables$ContentRect create(Bundle bundle) {
        return new SuggestParcelables$ContentRect(bundle);
    }

    public Bundle writeToBundle() {
        Bundle bundle = new Bundle();
        SuggestParcelables$OnScreenRect suggestParcelables$OnScreenRect = this.rect;
        if (suggestParcelables$OnScreenRect == null) {
            bundle.putBundle("rect", null);
        } else {
            bundle.putBundle("rect", suggestParcelables$OnScreenRect.writeToBundle());
        }
        bundle.putString("text", this.text);
        SuggestParcelables$ContentType suggestParcelables$ContentType = this.contentType;
        if (suggestParcelables$ContentType == null) {
            bundle.putBundle("contentType", null);
        } else {
            bundle.putBundle("contentType", suggestParcelables$ContentType.writeToBundle());
        }
        bundle.putInt("lineId", this.lineId);
        bundle.putString("contentUri", this.contentUri);
        bundle.putInt("contentGroupIndex", this.contentGroupIndex);
        bundle.putInt("beginChar", this.beginChar);
        bundle.putInt("endChar", this.endChar);
        return bundle;
    }

    private void readFromBundle(Bundle bundle) {
        if (!bundle.containsKey("rect")) {
            this.hasRect = false;
        } else {
            this.hasRect = true;
            Bundle bundle2 = bundle.getBundle("rect");
            if (bundle2 == null) {
                this.rect = null;
            } else {
                this.rect = SuggestParcelables$OnScreenRect.create(bundle2);
            }
        }
        if (!bundle.containsKey("text")) {
            this.hasText = false;
        } else {
            this.hasText = true;
            this.text = bundle.getString("text");
        }
        if (!bundle.containsKey("contentType")) {
            this.hasContentType = false;
        } else {
            this.hasContentType = true;
            Bundle bundle3 = bundle.getBundle("contentType");
            if (bundle3 == null) {
                this.contentType = null;
            } else {
                this.contentType = SuggestParcelables$ContentType.create(bundle3);
            }
            if (this.contentType == null) {
                this.hasContentType = false;
            }
        }
        if (!bundle.containsKey("lineId")) {
            this.hasLineId = false;
        } else {
            this.hasLineId = true;
            this.lineId = bundle.getInt("lineId");
        }
        if (!bundle.containsKey("contentUri")) {
            this.hasContentUri = false;
        } else {
            this.hasContentUri = true;
            this.contentUri = bundle.getString("contentUri");
        }
        if (!bundle.containsKey("contentGroupIndex")) {
            this.hasContentGroupIndex = false;
        } else {
            this.hasContentGroupIndex = true;
            this.contentGroupIndex = bundle.getInt("contentGroupIndex");
        }
        if (!bundle.containsKey("beginChar")) {
            this.hasBeginChar = false;
        } else {
            this.hasBeginChar = true;
            this.beginChar = bundle.getInt("beginChar");
        }
        if (!bundle.containsKey("endChar")) {
            this.hasEndChar = false;
            return;
        }
        this.hasEndChar = true;
        this.endChar = bundle.getInt("endChar");
    }
}
