package com.android.systemui.people.widget;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
/* loaded from: classes.dex */
public class PeopleSpaceWidgetPinnedReceiver extends BroadcastReceiver {
    private final PeopleSpaceWidgetManager mPeopleSpaceWidgetManager;

    public PeopleSpaceWidgetPinnedReceiver(PeopleSpaceWidgetManager peopleSpaceWidgetManager) {
        this.mPeopleSpaceWidgetManager = peopleSpaceWidgetManager;
    }

    public static PendingIntent getPendingIntent(Context context, ShortcutInfo shortcutInfo) {
        Intent addFlags = new Intent(context, PeopleSpaceWidgetPinnedReceiver.class).addFlags(268435456);
        addFlags.putExtra("android.intent.extra.shortcut.ID", shortcutInfo.getId());
        addFlags.putExtra("android.intent.extra.USER_ID", shortcutInfo.getUserId());
        addFlags.putExtra("android.intent.extra.PACKAGE_NAME", shortcutInfo.getPackage());
        return PendingIntent.getBroadcast(context, 0, addFlags, 167772160);
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        int intExtra;
        if (context != null && intent != null && (intExtra = intent.getIntExtra("appWidgetId", -1)) != -1) {
            PeopleTileKey peopleTileKey = new PeopleTileKey(intent.getStringExtra("android.intent.extra.shortcut.ID"), intent.getIntExtra("android.intent.extra.USER_ID", -1), intent.getStringExtra("android.intent.extra.PACKAGE_NAME"));
            if (PeopleTileKey.isValid(peopleTileKey)) {
                this.mPeopleSpaceWidgetManager.addNewWidget(intExtra, peopleTileKey);
            }
        }
    }
}
