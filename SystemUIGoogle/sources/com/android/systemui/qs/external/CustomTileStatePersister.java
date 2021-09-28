package com.android.systemui.qs.external;

import android.content.Context;
import android.content.SharedPreferences;
import android.service.quicksettings.Tile;
import android.util.Log;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.json.JSONException;
/* compiled from: CustomTileStatePersister.kt */
/* loaded from: classes.dex */
public final class CustomTileStatePersister {
    public static final Companion Companion = new Companion(null);
    private final SharedPreferences sharedPreferences;

    /* compiled from: CustomTileStatePersister.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    public CustomTileStatePersister(Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        this.sharedPreferences = context.getSharedPreferences("custom_tiles_state", 0);
    }

    public final Tile readState(TileServiceKey tileServiceKey) {
        Intrinsics.checkNotNullParameter(tileServiceKey, "key");
        String string = this.sharedPreferences.getString(tileServiceKey.toString(), null);
        if (string == null) {
            return null;
        }
        try {
            return CustomTileStatePersisterKt.readTileFromString(string);
        } catch (JSONException e) {
            Log.e("TileServicePersistence", Intrinsics.stringPlus("Bad saved state: ", string), e);
            return null;
        }
    }

    public final void persistState(TileServiceKey tileServiceKey, Tile tile) {
        Intrinsics.checkNotNullParameter(tileServiceKey, "key");
        Intrinsics.checkNotNullParameter(tile, "tile");
        this.sharedPreferences.edit().putString(tileServiceKey.toString(), CustomTileStatePersisterKt.writeToString(tile)).apply();
    }

    public final void removeState(TileServiceKey tileServiceKey) {
        Intrinsics.checkNotNullParameter(tileServiceKey, "key");
        this.sharedPreferences.edit().remove(tileServiceKey.toString()).apply();
    }
}
