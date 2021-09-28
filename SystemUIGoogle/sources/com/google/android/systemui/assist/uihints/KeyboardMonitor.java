package com.google.android.systemui.assist.uihints;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import com.android.systemui.statusbar.CommandQueue;
import com.google.android.systemui.assist.uihints.NgaMessageHandler;
import java.util.Optional;
import java.util.function.Consumer;
/* loaded from: classes2.dex */
final class KeyboardMonitor implements CommandQueue.Callbacks, NgaMessageHandler.ConfigInfoListener {
    private final Context mContext;
    private PendingIntent mOnKeyboardShowingChanged;
    private boolean mShowing;

    /* access modifiers changed from: package-private */
    public KeyboardMonitor(Context context, Optional<CommandQueue> optional) {
        this.mContext = context;
        optional.ifPresent(new Consumer() { // from class: com.google.android.systemui.assist.uihints.KeyboardMonitor$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                KeyboardMonitor.this.lambda$new$0((CommandQueue) obj);
            }
        });
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(CommandQueue commandQueue) {
        commandQueue.addCallback((CommandQueue.Callbacks) this);
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public void setImeWindowStatus(int i, IBinder iBinder, int i2, int i3, boolean z) {
        boolean z2 = this.mShowing;
        boolean z3 = (i2 & 2) != 0;
        this.mShowing = z3;
        if (z3 != z2) {
            trySendKeyboardShowing();
        }
    }

    @Override // com.google.android.systemui.assist.uihints.NgaMessageHandler.ConfigInfoListener
    public void onConfigInfo(NgaMessageHandler.ConfigInfo configInfo) {
        PendingIntent pendingIntent = this.mOnKeyboardShowingChanged;
        PendingIntent pendingIntent2 = configInfo.onKeyboardShowingChange;
        if (pendingIntent != pendingIntent2) {
            this.mOnKeyboardShowingChanged = pendingIntent2;
            trySendKeyboardShowing();
        }
    }

    private void trySendKeyboardShowing() {
        if (this.mOnKeyboardShowingChanged != null) {
            Intent intent = new Intent();
            intent.putExtra("is_keyboard_showing", this.mShowing);
            try {
                this.mOnKeyboardShowingChanged.send(this.mContext, 0, intent);
            } catch (PendingIntent.CanceledException e) {
                Log.e("KeyboardMonitor", "onKeyboardShowingChanged pending intent cancelled", e);
            }
        }
    }
}
