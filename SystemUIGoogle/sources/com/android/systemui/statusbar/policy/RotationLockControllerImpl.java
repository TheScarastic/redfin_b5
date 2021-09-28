package com.android.systemui.statusbar.policy;

import android.content.Context;
import com.android.internal.view.RotationPolicy;
import com.android.systemui.statusbar.policy.RotationLockController;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
/* loaded from: classes.dex */
public final class RotationLockControllerImpl implements RotationLockController {
    private final Context mContext;
    private final CopyOnWriteArrayList<RotationLockController.RotationLockControllerCallback> mCallbacks = new CopyOnWriteArrayList<>();
    private final RotationPolicy.RotationPolicyListener mRotationPolicyListener = new RotationPolicy.RotationPolicyListener() { // from class: com.android.systemui.statusbar.policy.RotationLockControllerImpl.1
        public void onChange() {
            RotationLockControllerImpl.this.notifyChanged();
        }
    };

    public RotationLockControllerImpl(Context context) {
        this.mContext = context;
        setListening(true);
    }

    public void addCallback(RotationLockController.RotationLockControllerCallback rotationLockControllerCallback) {
        this.mCallbacks.add(rotationLockControllerCallback);
        notifyChanged(rotationLockControllerCallback);
    }

    public void removeCallback(RotationLockController.RotationLockControllerCallback rotationLockControllerCallback) {
        this.mCallbacks.remove(rotationLockControllerCallback);
    }

    @Override // com.android.systemui.statusbar.policy.RotationLockController
    public int getRotationLockOrientation() {
        return RotationPolicy.getRotationLockOrientation(this.mContext);
    }

    @Override // com.android.systemui.statusbar.policy.RotationLockController
    public boolean isRotationLocked() {
        return RotationPolicy.isRotationLocked(this.mContext);
    }

    @Override // com.android.systemui.statusbar.policy.RotationLockController
    public void setRotationLocked(boolean z) {
        RotationPolicy.setRotationLock(this.mContext, z);
    }

    @Override // com.android.systemui.statusbar.policy.RotationLockController
    public void setRotationLockedAtAngle(boolean z, int i) {
        RotationPolicy.setRotationLockAtAngle(this.mContext, z, i);
    }

    public void setListening(boolean z) {
        if (z) {
            RotationPolicy.registerRotationPolicyListener(this.mContext, this.mRotationPolicyListener, -1);
        } else {
            RotationPolicy.unregisterRotationPolicyListener(this.mContext, this.mRotationPolicyListener);
        }
    }

    /* access modifiers changed from: private */
    public void notifyChanged() {
        Iterator<RotationLockController.RotationLockControllerCallback> it = this.mCallbacks.iterator();
        while (it.hasNext()) {
            notifyChanged(it.next());
        }
    }

    private void notifyChanged(RotationLockController.RotationLockControllerCallback rotationLockControllerCallback) {
        rotationLockControllerCallback.onRotationLockStateChanged(RotationPolicy.isRotationLocked(this.mContext), RotationPolicy.isRotationLockToggleVisible(this.mContext));
    }
}
