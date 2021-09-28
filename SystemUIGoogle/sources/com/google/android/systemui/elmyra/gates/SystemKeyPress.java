package com.google.android.systemui.elmyra.gates;

import android.content.Context;
import com.android.systemui.Dependency;
import com.android.systemui.R$array;
import com.android.systemui.R$integer;
import com.android.systemui.statusbar.CommandQueue;
/* loaded from: classes2.dex */
public class SystemKeyPress extends TransientGate {
    private final int[] mBlockingKeys;
    private final CommandQueue.Callbacks mCommandQueueCallbacks = new CommandQueue.Callbacks() { // from class: com.google.android.systemui.elmyra.gates.SystemKeyPress.1
        @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
        public void handleSystemKey(int i) {
            for (int i2 = 0; i2 < SystemKeyPress.this.mBlockingKeys.length; i2++) {
                if (SystemKeyPress.this.mBlockingKeys[i2] == i) {
                    SystemKeyPress.this.block();
                    return;
                }
            }
        }
    };
    private final CommandQueue mCommandQueue = (CommandQueue) Dependency.get(CommandQueue.class);

    public SystemKeyPress(Context context) {
        super(context, (long) context.getResources().getInteger(R$integer.elmyra_system_key_gate_duration));
        this.mBlockingKeys = context.getResources().getIntArray(R$array.elmyra_blocking_system_keys);
    }

    @Override // com.google.android.systemui.elmyra.gates.Gate
    protected void onActivate() {
        this.mCommandQueue.addCallback(this.mCommandQueueCallbacks);
    }

    @Override // com.google.android.systemui.elmyra.gates.Gate
    protected void onDeactivate() {
        this.mCommandQueue.removeCallback(this.mCommandQueueCallbacks);
    }
}
