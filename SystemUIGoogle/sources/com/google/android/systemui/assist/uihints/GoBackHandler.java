package com.google.android.systemui.assist.uihints;

import android.hardware.input.InputManager;
import android.os.SystemClock;
import android.view.KeyEvent;
import com.google.android.systemui.assist.uihints.NgaMessageHandler;
/* loaded from: classes2.dex */
class GoBackHandler implements NgaMessageHandler.GoBackListener {
    @Override // com.google.android.systemui.assist.uihints.NgaMessageHandler.GoBackListener
    public void onGoBack() {
        injectBackKeyEvent(0);
        injectBackKeyEvent(1);
    }

    private void injectBackKeyEvent(int i) {
        long uptimeMillis = SystemClock.uptimeMillis();
        InputManager.getInstance().injectInputEvent(new KeyEvent(uptimeMillis, uptimeMillis, i, 4, 0, 0, -1, 0, 72, 257), 0);
    }
}
