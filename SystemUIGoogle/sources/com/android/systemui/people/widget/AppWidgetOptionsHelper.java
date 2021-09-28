package com.android.systemui.people.widget;

import android.appwidget.AppWidgetManager;
import android.os.Bundle;
import com.android.systemui.people.PeopleSpaceUtils;
/* loaded from: classes.dex */
public class AppWidgetOptionsHelper {
    public static void setPeopleTileKey(AppWidgetManager appWidgetManager, int i, PeopleTileKey peopleTileKey) {
        Bundle appWidgetOptions = appWidgetManager.getAppWidgetOptions(i);
        appWidgetOptions.putString("shortcut_id", peopleTileKey.getShortcutId());
        appWidgetOptions.putInt("user_id", peopleTileKey.getUserId());
        appWidgetOptions.putString("package_name", peopleTileKey.getPackageName());
        appWidgetManager.updateAppWidgetOptions(i, appWidgetOptions);
    }

    public static PeopleTileKey getPeopleTileKeyFromBundle(Bundle bundle) {
        String string = bundle.getString("package_name", "");
        return new PeopleTileKey(bundle.getString("shortcut_id", ""), bundle.getInt("user_id", -1), string);
    }

    public static void removePeopleTileKey(AppWidgetManager appWidgetManager, int i) {
        setPeopleTileKey(appWidgetManager, i, PeopleSpaceUtils.EMPTY_KEY);
    }
}
