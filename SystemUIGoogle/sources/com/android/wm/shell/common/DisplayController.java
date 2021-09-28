package com.android.wm.shell.common;

import android.content.Context;
import android.content.res.Configuration;
import android.hardware.display.DisplayManager;
import android.os.RemoteException;
import android.util.Slog;
import android.util.SparseArray;
import android.view.Display;
import android.view.IDisplayWindowListener;
import android.view.IWindowManager;
import com.android.wm.shell.common.DisplayChangeController;
import java.util.ArrayList;
/* loaded from: classes2.dex */
public class DisplayController {
    private final DisplayChangeController mChangeController;
    private final Context mContext;
    private final IDisplayWindowListener mDisplayContainerListener;
    private final ShellExecutor mMainExecutor;
    private final IWindowManager mWmService;
    private final SparseArray<DisplayRecord> mDisplays = new SparseArray<>();
    private final ArrayList<OnDisplaysChangedListener> mDisplayChangedListeners = new ArrayList<>();

    /* loaded from: classes2.dex */
    public interface OnDisplaysChangedListener {
        default void onDisplayAdded(int i) {
        }

        default void onDisplayConfigurationChanged(int i, Configuration configuration) {
        }

        default void onDisplayRemoved(int i) {
        }

        default void onFixedRotationFinished(int i) {
        }

        default void onFixedRotationStarted(int i, int i2) {
        }
    }

    public Display getDisplay(int i) {
        return ((DisplayManager) this.mContext.getSystemService(DisplayManager.class)).getDisplay(i);
    }

    public DisplayController(Context context, IWindowManager iWindowManager, ShellExecutor shellExecutor) {
        this.mMainExecutor = shellExecutor;
        this.mContext = context;
        this.mWmService = iWindowManager;
        this.mChangeController = new DisplayChangeController(iWindowManager, shellExecutor);
        DisplayWindowListenerImpl displayWindowListenerImpl = new DisplayWindowListenerImpl();
        this.mDisplayContainerListener = displayWindowListenerImpl;
        try {
            iWindowManager.registerDisplayWindowListener(displayWindowListenerImpl);
        } catch (RemoteException unused) {
            throw new RuntimeException("Unable to register hierarchy listener");
        }
    }

    public DisplayLayout getDisplayLayout(int i) {
        DisplayRecord displayRecord = this.mDisplays.get(i);
        if (displayRecord != null) {
            return displayRecord.mDisplayLayout;
        }
        return null;
    }

    public Context getDisplayContext(int i) {
        DisplayRecord displayRecord = this.mDisplays.get(i);
        if (displayRecord != null) {
            return displayRecord.mContext;
        }
        return null;
    }

    public void addDisplayWindowListener(OnDisplaysChangedListener onDisplaysChangedListener) {
        synchronized (this.mDisplays) {
            if (!this.mDisplayChangedListeners.contains(onDisplaysChangedListener)) {
                this.mDisplayChangedListeners.add(onDisplaysChangedListener);
                for (int i = 0; i < this.mDisplays.size(); i++) {
                    onDisplaysChangedListener.onDisplayAdded(this.mDisplays.keyAt(i));
                }
            }
        }
    }

    public void removeDisplayWindowListener(OnDisplaysChangedListener onDisplaysChangedListener) {
        synchronized (this.mDisplays) {
            this.mDisplayChangedListeners.remove(onDisplaysChangedListener);
        }
    }

    public void addDisplayChangingController(DisplayChangeController.OnDisplayChangingListener onDisplayChangingListener) {
        this.mChangeController.addRotationListener(onDisplayChangingListener);
    }

    /* access modifiers changed from: private */
    public void onDisplayAdded(int i) {
        Context context;
        synchronized (this.mDisplays) {
            if (this.mDisplays.get(i) == null) {
                Display display = getDisplay(i);
                if (display != null) {
                    DisplayRecord displayRecord = new DisplayRecord();
                    displayRecord.mDisplayId = i;
                    if (i == 0) {
                        context = this.mContext;
                    } else {
                        context = this.mContext.createDisplayContext(display);
                    }
                    displayRecord.mContext = context;
                    displayRecord.mDisplayLayout = new DisplayLayout(context, display);
                    this.mDisplays.put(i, displayRecord);
                    for (int i2 = 0; i2 < this.mDisplayChangedListeners.size(); i2++) {
                        this.mDisplayChangedListeners.get(i2).onDisplayAdded(i);
                    }
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void onDisplayConfigurationChanged(int i, Configuration configuration) {
        synchronized (this.mDisplays) {
            DisplayRecord displayRecord = this.mDisplays.get(i);
            if (displayRecord == null) {
                Slog.w("DisplayController", "Skipping Display Configuration change on non-added display.");
                return;
            }
            Display display = getDisplay(i);
            if (display == null) {
                Slog.w("DisplayController", "Skipping Display Configuration change on invalid display. It may have been removed.");
                return;
            }
            Context context = this.mContext;
            if (i != 0) {
                context = context.createDisplayContext(display);
            }
            Context createConfigurationContext = context.createConfigurationContext(configuration);
            displayRecord.mContext = createConfigurationContext;
            displayRecord.mDisplayLayout = new DisplayLayout(createConfigurationContext, display);
            for (int i2 = 0; i2 < this.mDisplayChangedListeners.size(); i2++) {
                this.mDisplayChangedListeners.get(i2).onDisplayConfigurationChanged(i, configuration);
            }
        }
    }

    /* access modifiers changed from: private */
    public void onDisplayRemoved(int i) {
        synchronized (this.mDisplays) {
            if (this.mDisplays.get(i) != null) {
                for (int size = this.mDisplayChangedListeners.size() - 1; size >= 0; size--) {
                    this.mDisplayChangedListeners.get(size).onDisplayRemoved(i);
                }
                this.mDisplays.remove(i);
            }
        }
    }

    /* access modifiers changed from: private */
    public void onFixedRotationStarted(int i, int i2) {
        synchronized (this.mDisplays) {
            if (!(this.mDisplays.get(i) == null || getDisplay(i) == null)) {
                for (int size = this.mDisplayChangedListeners.size() - 1; size >= 0; size--) {
                    this.mDisplayChangedListeners.get(size).onFixedRotationStarted(i, i2);
                }
                return;
            }
            Slog.w("DisplayController", "Skipping onFixedRotationStarted on unknown display, displayId=" + i);
        }
    }

    /* access modifiers changed from: private */
    public void onFixedRotationFinished(int i) {
        synchronized (this.mDisplays) {
            if (!(this.mDisplays.get(i) == null || getDisplay(i) == null)) {
                for (int size = this.mDisplayChangedListeners.size() - 1; size >= 0; size--) {
                    this.mDisplayChangedListeners.get(size).onFixedRotationFinished(i);
                }
                return;
            }
            Slog.w("DisplayController", "Skipping onFixedRotationFinished on unknown display, displayId=" + i);
        }
    }

    /* access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static class DisplayRecord {
        Context mContext;
        int mDisplayId;
        DisplayLayout mDisplayLayout;

        private DisplayRecord() {
        }
    }

    /* access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public class DisplayWindowListenerImpl extends IDisplayWindowListener.Stub {
        private DisplayWindowListenerImpl() {
        }

        public void onDisplayAdded(int i) {
            DisplayController.this.mMainExecutor.execute(new DisplayController$DisplayWindowListenerImpl$$ExternalSyntheticLambda0(this, i));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onDisplayAdded$0(int i) {
            DisplayController.this.onDisplayAdded(i);
        }

        public void onDisplayConfigurationChanged(int i, Configuration configuration) {
            DisplayController.this.mMainExecutor.execute(new DisplayController$DisplayWindowListenerImpl$$ExternalSyntheticLambda4(this, i, configuration));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onDisplayConfigurationChanged$1(int i, Configuration configuration) {
            DisplayController.this.onDisplayConfigurationChanged(i, configuration);
        }

        public void onDisplayRemoved(int i) {
            DisplayController.this.mMainExecutor.execute(new DisplayController$DisplayWindowListenerImpl$$ExternalSyntheticLambda1(this, i));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onDisplayRemoved$2(int i) {
            DisplayController.this.onDisplayRemoved(i);
        }

        public void onFixedRotationStarted(int i, int i2) {
            DisplayController.this.mMainExecutor.execute(new DisplayController$DisplayWindowListenerImpl$$ExternalSyntheticLambda3(this, i, i2));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onFixedRotationStarted$3(int i, int i2) {
            DisplayController.this.onFixedRotationStarted(i, i2);
        }

        public void onFixedRotationFinished(int i) {
            DisplayController.this.mMainExecutor.execute(new DisplayController$DisplayWindowListenerImpl$$ExternalSyntheticLambda2(this, i));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onFixedRotationFinished$4(int i) {
            DisplayController.this.onFixedRotationFinished(i);
        }
    }
}
