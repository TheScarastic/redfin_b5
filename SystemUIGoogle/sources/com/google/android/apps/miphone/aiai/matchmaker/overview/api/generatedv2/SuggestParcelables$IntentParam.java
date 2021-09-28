package com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2;

import android.os.Bundle;
import android.support.annotation.Nullable;
/* loaded from: classes2.dex */
public final class SuggestParcelables$IntentParam {
    private boolean boolValue;
    @Nullable
    private String contentUri;
    private float floatValue;
    private boolean hasBoolValue;
    private boolean hasContentUri;
    private boolean hasFloatValue;
    private boolean hasIntValue;
    private boolean hasIntentValue;
    private boolean hasLongValue;
    private boolean hasName;
    private boolean hasStrValue;
    private boolean hasType;
    private int intValue;
    @Nullable
    private SuggestParcelables$IntentInfo intentValue;
    private long longValue;
    @Nullable
    private String name;
    @Nullable
    private String strValue;
    private SuggestParcelables$IntentParamType type;

    private SuggestParcelables$IntentParam(Bundle bundle) {
        readFromBundle(bundle);
    }

    private SuggestParcelables$IntentParam() {
    }

    public static SuggestParcelables$IntentParam create(Bundle bundle) {
        return new SuggestParcelables$IntentParam(bundle);
    }

    public Bundle writeToBundle() {
        Bundle bundle = new Bundle();
        bundle.putString("name", this.name);
        SuggestParcelables$IntentParamType suggestParcelables$IntentParamType = this.type;
        if (suggestParcelables$IntentParamType == null) {
            bundle.putBundle("type", null);
        } else {
            bundle.putBundle("type", suggestParcelables$IntentParamType.writeToBundle());
        }
        bundle.putString("strValue", this.strValue);
        bundle.putInt("intValue", this.intValue);
        bundle.putFloat("floatValue", this.floatValue);
        bundle.putLong("longValue", this.longValue);
        bundle.putBoolean("boolValue", this.boolValue);
        SuggestParcelables$IntentInfo suggestParcelables$IntentInfo = this.intentValue;
        if (suggestParcelables$IntentInfo == null) {
            bundle.putBundle("intentValue", null);
        } else {
            bundle.putBundle("intentValue", suggestParcelables$IntentInfo.writeToBundle());
        }
        bundle.putString("contentUri", this.contentUri);
        return bundle;
    }

    private void readFromBundle(Bundle bundle) {
        if (!bundle.containsKey("name")) {
            this.hasName = false;
        } else {
            this.hasName = true;
            this.name = bundle.getString("name");
        }
        if (!bundle.containsKey("type")) {
            this.hasType = false;
        } else {
            this.hasType = true;
            Bundle bundle2 = bundle.getBundle("type");
            if (bundle2 == null) {
                this.type = null;
            } else {
                this.type = SuggestParcelables$IntentParamType.create(bundle2);
            }
            if (this.type == null) {
                this.hasType = false;
            }
        }
        if (!bundle.containsKey("strValue")) {
            this.hasStrValue = false;
        } else {
            this.hasStrValue = true;
            this.strValue = bundle.getString("strValue");
        }
        if (!bundle.containsKey("intValue")) {
            this.hasIntValue = false;
        } else {
            this.hasIntValue = true;
            this.intValue = bundle.getInt("intValue");
        }
        if (!bundle.containsKey("floatValue")) {
            this.hasFloatValue = false;
        } else {
            this.hasFloatValue = true;
            this.floatValue = bundle.getFloat("floatValue");
        }
        if (!bundle.containsKey("longValue")) {
            this.hasLongValue = false;
        } else {
            this.hasLongValue = true;
            this.longValue = bundle.getLong("longValue");
        }
        if (!bundle.containsKey("boolValue")) {
            this.hasBoolValue = false;
        } else {
            this.hasBoolValue = true;
            this.boolValue = bundle.getBoolean("boolValue");
        }
        if (!bundle.containsKey("intentValue")) {
            this.hasIntentValue = false;
        } else {
            this.hasIntentValue = true;
            Bundle bundle3 = bundle.getBundle("intentValue");
            if (bundle3 == null) {
                this.intentValue = null;
            } else {
                this.intentValue = SuggestParcelables$IntentInfo.create(bundle3);
            }
        }
        if (!bundle.containsKey("contentUri")) {
            this.hasContentUri = false;
            return;
        }
        this.hasContentUri = true;
        this.contentUri = bundle.getString("contentUri");
    }
}
