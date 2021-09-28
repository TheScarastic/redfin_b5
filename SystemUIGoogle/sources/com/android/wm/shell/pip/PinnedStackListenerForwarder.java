package com.android.wm.shell.pip;

import android.app.RemoteAction;
import android.content.ComponentName;
import android.content.pm.ParceledListSlice;
import android.os.RemoteException;
import android.view.IPinnedTaskListener;
import android.view.WindowManagerGlobal;
import com.android.wm.shell.common.ShellExecutor;
import java.util.ArrayList;
import java.util.Iterator;
/* loaded from: classes2.dex */
public class PinnedStackListenerForwarder {
    private final IPinnedTaskListener mListenerImpl = new PinnedTaskListenerImpl();
    private final ArrayList<PinnedTaskListener> mListeners = new ArrayList<>();
    private final ShellExecutor mMainExecutor;

    /* loaded from: classes2.dex */
    public static class PinnedTaskListener {
        public void onActionsChanged(ParceledListSlice<RemoteAction> parceledListSlice) {
        }

        public void onActivityHidden(ComponentName componentName) {
        }

        public void onAspectRatioChanged(float f) {
        }

        public void onImeVisibilityChanged(boolean z, int i) {
        }

        public void onMovementBoundsChanged(boolean z) {
        }
    }

    public PinnedStackListenerForwarder(ShellExecutor shellExecutor) {
        this.mMainExecutor = shellExecutor;
    }

    public void addListener(PinnedTaskListener pinnedTaskListener) {
        this.mListeners.add(pinnedTaskListener);
    }

    public void register(int i) throws RemoteException {
        WindowManagerGlobal.getWindowManagerService().registerPinnedTaskListener(i, this.mListenerImpl);
    }

    /* access modifiers changed from: private */
    public void onMovementBoundsChanged(boolean z) {
        Iterator<PinnedTaskListener> it = this.mListeners.iterator();
        while (it.hasNext()) {
            it.next().onMovementBoundsChanged(z);
        }
    }

    /* access modifiers changed from: private */
    public void onImeVisibilityChanged(boolean z, int i) {
        Iterator<PinnedTaskListener> it = this.mListeners.iterator();
        while (it.hasNext()) {
            it.next().onImeVisibilityChanged(z, i);
        }
    }

    /* access modifiers changed from: private */
    public void onActionsChanged(ParceledListSlice<RemoteAction> parceledListSlice) {
        Iterator<PinnedTaskListener> it = this.mListeners.iterator();
        while (it.hasNext()) {
            it.next().onActionsChanged(parceledListSlice);
        }
    }

    /* access modifiers changed from: private */
    public void onActivityHidden(ComponentName componentName) {
        Iterator<PinnedTaskListener> it = this.mListeners.iterator();
        while (it.hasNext()) {
            it.next().onActivityHidden(componentName);
        }
    }

    /* access modifiers changed from: private */
    public void onAspectRatioChanged(float f) {
        Iterator<PinnedTaskListener> it = this.mListeners.iterator();
        while (it.hasNext()) {
            it.next().onAspectRatioChanged(f);
        }
    }

    /* access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public class PinnedTaskListenerImpl extends IPinnedTaskListener.Stub {
        private PinnedTaskListenerImpl() {
        }

        public void onMovementBoundsChanged(boolean z) {
            PinnedStackListenerForwarder.this.mMainExecutor.execute(new PinnedStackListenerForwarder$PinnedTaskListenerImpl$$ExternalSyntheticLambda3(this, z));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onMovementBoundsChanged$0(boolean z) {
            PinnedStackListenerForwarder.this.onMovementBoundsChanged(z);
        }

        public void onImeVisibilityChanged(boolean z, int i) {
            PinnedStackListenerForwarder.this.mMainExecutor.execute(new PinnedStackListenerForwarder$PinnedTaskListenerImpl$$ExternalSyntheticLambda4(this, z, i));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onImeVisibilityChanged$1(boolean z, int i) {
            PinnedStackListenerForwarder.this.onImeVisibilityChanged(z, i);
        }

        public void onActionsChanged(ParceledListSlice<RemoteAction> parceledListSlice) {
            PinnedStackListenerForwarder.this.mMainExecutor.execute(new PinnedStackListenerForwarder$PinnedTaskListenerImpl$$ExternalSyntheticLambda2(this, parceledListSlice));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onActionsChanged$2(ParceledListSlice parceledListSlice) {
            PinnedStackListenerForwarder.this.onActionsChanged(parceledListSlice);
        }

        public void onActivityHidden(ComponentName componentName) {
            PinnedStackListenerForwarder.this.mMainExecutor.execute(new PinnedStackListenerForwarder$PinnedTaskListenerImpl$$ExternalSyntheticLambda1(this, componentName));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onActivityHidden$3(ComponentName componentName) {
            PinnedStackListenerForwarder.this.onActivityHidden(componentName);
        }

        public void onAspectRatioChanged(float f) {
            PinnedStackListenerForwarder.this.mMainExecutor.execute(new PinnedStackListenerForwarder$PinnedTaskListenerImpl$$ExternalSyntheticLambda0(this, f));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onAspectRatioChanged$4(float f) {
            PinnedStackListenerForwarder.this.onAspectRatioChanged(f);
        }
    }
}
