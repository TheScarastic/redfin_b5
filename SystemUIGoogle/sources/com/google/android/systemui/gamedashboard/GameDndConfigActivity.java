package com.google.android.systemui.gamedashboard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
/* loaded from: classes2.dex */
public final class GameDndConfigActivity extends Activity {
    @Override // android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        startActivityForResult(new Intent("com.google.android.settings.games.GAME_SETTINGS").setPackage("com.android.settings"), 0);
        finish();
    }

    @Override // android.app.Activity
    protected void onActivityResult(int i, int i2, Intent intent) {
        finish();
    }
}
