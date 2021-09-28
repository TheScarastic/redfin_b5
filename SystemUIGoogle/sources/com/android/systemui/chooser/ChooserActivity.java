package com.android.systemui.chooser;

import android.app.Activity;
import android.os.Bundle;
/* loaded from: classes.dex */
public final class ChooserActivity extends Activity {
    @Override // android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        ChooserHelper.onChoose(this);
        finish();
    }
}
