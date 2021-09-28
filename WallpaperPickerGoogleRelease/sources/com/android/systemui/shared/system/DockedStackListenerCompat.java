package com.android.systemui.shared.system;

import android.view.IDockedStackListener;
/* loaded from: classes.dex */
public class DockedStackListenerCompat {
    public IDockedStackListener.Stub mListener = new IDockedStackListener.Stub() { // from class: com.android.systemui.shared.system.DockedStackListenerCompat.1
        public void onAdjustedForImeChanged(boolean z, long j) {
        }

        public void onDividerVisibilityChanged(boolean z) {
        }

        public void onDockSideChanged(int i) {
            DockedStackListenerCompat.this.onDockSideChanged(i);
        }

        public void onDockedStackExistsChanged(boolean z) {
            DockedStackListenerCompat.this.onDockedStackExistsChanged(z);
        }

        public void onDockedStackMinimizedChanged(boolean z, long j, boolean z2) {
            DockedStackListenerCompat.this.onDockedStackMinimizedChanged(z, j, z2);
        }
    };

    public void onDockSideChanged(int i) {
    }

    public void onDockedStackExistsChanged(boolean z) {
    }

    public void onDockedStackMinimizedChanged(boolean z, long j, boolean z2) {
    }
}
