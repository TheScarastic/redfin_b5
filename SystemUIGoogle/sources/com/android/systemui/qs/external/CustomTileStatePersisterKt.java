package com.android.systemui.qs.external;

import android.service.quicksettings.Tile;
import com.android.internal.annotations.VisibleForTesting;
import kotlin.jvm.internal.Intrinsics;
import org.json.JSONObject;
/* compiled from: CustomTileStatePersister.kt */
/* loaded from: classes.dex */
public final class CustomTileStatePersisterKt {
    @VisibleForTesting
    public static final Tile readTileFromString(String str) {
        Intrinsics.checkNotNullParameter(str, "stateString");
        JSONObject jSONObject = new JSONObject(str);
        Tile tile = new Tile();
        tile.setState(jSONObject.getInt("state"));
        tile.setLabel(getStringOrNull(jSONObject, "label"));
        tile.setSubtitle(getStringOrNull(jSONObject, "subtitle"));
        tile.setContentDescription(getStringOrNull(jSONObject, "content_description"));
        tile.setStateDescription(getStringOrNull(jSONObject, "state_description"));
        return tile;
    }

    private static final String getStringOrNull(JSONObject jSONObject, String str) {
        if (jSONObject.has(str)) {
            return jSONObject.getString(str);
        }
        return null;
    }

    @VisibleForTesting
    public static final String writeToString(Tile tile) {
        Intrinsics.checkNotNullParameter(tile, "tile");
        String jSONObject = new JSONObject().put("state", tile.getState()).put("label", tile.getLabel()).put("subtitle", tile.getSubtitle()).put("content_description", tile.getContentDescription()).put("state_description", tile.getStateDescription()).toString();
        Intrinsics.checkNotNullExpressionValue(jSONObject, "with(tile) {\n        JSONObject()\n            .put(STATE, state)\n            .put(LABEL, label)\n            .put(SUBTITLE, subtitle)\n            .put(CONTENT_DESCRIPTION, contentDescription)\n            .put(STATE_DESCRIPTION, stateDescription)\n            .toString()\n    }");
        return jSONObject;
    }
}
