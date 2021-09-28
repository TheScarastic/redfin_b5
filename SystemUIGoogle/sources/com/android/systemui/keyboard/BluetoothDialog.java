package com.android.systemui.keyboard;

import android.content.Context;
import com.android.systemui.statusbar.phone.SystemUIDialog;
/* loaded from: classes.dex */
public class BluetoothDialog extends SystemUIDialog {
    public BluetoothDialog(Context context) {
        super(context);
        getWindow().setType(2008);
        setShowForAllUsers(true);
    }
}
