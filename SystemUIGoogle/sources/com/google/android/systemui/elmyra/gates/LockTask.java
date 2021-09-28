package com.google.android.systemui.elmyra.gates;

import android.content.Context;
import android.util.Log;
import com.android.systemui.shared.system.TaskStackChangeListener;
import com.android.systemui.shared.system.TaskStackChangeListeners;
/* loaded from: classes2.dex */
public class LockTask extends Gate {
    private boolean mIsBlocked;
    private final TaskStackChangeListener mTaskStackListener = new TaskStackChangeListener() { // from class: com.google.android.systemui.elmyra.gates.LockTask.1
        @Override // com.android.systemui.shared.system.TaskStackChangeListener
        public void onLockTaskModeChanged(int i) {
            Log.d("Elmyra/LockTask", "Mode: " + i);
            if (i == 0) {
                LockTask.this.mIsBlocked = false;
            } else {
                LockTask.this.mIsBlocked = true;
            }
            LockTask.this.notifyListener();
        }
    };

    public LockTask(Context context) {
        super(context);
    }

    @Override // com.google.android.systemui.elmyra.gates.Gate
    protected void onActivate() {
        TaskStackChangeListeners.getInstance().registerTaskStackListener(this.mTaskStackListener);
    }

    @Override // com.google.android.systemui.elmyra.gates.Gate
    protected void onDeactivate() {
        TaskStackChangeListeners.getInstance().unregisterTaskStackListener(this.mTaskStackListener);
    }

    @Override // com.google.android.systemui.elmyra.gates.Gate
    protected boolean isBlocked() {
        return this.mIsBlocked;
    }
}
