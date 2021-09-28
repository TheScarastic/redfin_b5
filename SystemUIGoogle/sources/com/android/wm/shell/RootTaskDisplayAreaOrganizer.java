package com.android.wm.shell;

import android.app.ResourcesManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Configuration;
import android.hardware.display.DisplayManager;
import android.os.Binder;
import android.os.IBinder;
import android.util.SparseArray;
import android.view.Display;
import android.view.SurfaceControl;
import android.window.DisplayAreaAppearedInfo;
import android.window.DisplayAreaInfo;
import android.window.DisplayAreaOrganizer;
import com.android.wm.shell.protolog.ShellProtoLogCache;
import com.android.wm.shell.protolog.ShellProtoLogGroup;
import com.android.wm.shell.protolog.ShellProtoLogImpl;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
/* loaded from: classes2.dex */
public class RootTaskDisplayAreaOrganizer extends DisplayAreaOrganizer {
    private static final String TAG = RootTaskDisplayAreaOrganizer.class.getSimpleName();
    private final Context mContext;
    private final SparseArray<DisplayAreaInfo> mDisplayAreasInfo = new SparseArray<>();
    private final SparseArray<SurfaceControl> mLeashes = new SparseArray<>();
    private final SparseArray<ArrayList<RootTaskDisplayAreaListener>> mListeners = new SparseArray<>();
    private final SparseArray<DisplayAreaContext> mDisplayAreaContexts = new SparseArray<>();

    /* loaded from: classes2.dex */
    public interface RootTaskDisplayAreaListener {
        default void onDisplayAreaAppeared(DisplayAreaInfo displayAreaInfo) {
        }

        default void onDisplayAreaInfoChanged(DisplayAreaInfo displayAreaInfo) {
        }

        default void onDisplayAreaVanished(DisplayAreaInfo displayAreaInfo) {
        }
    }

    public RootTaskDisplayAreaOrganizer(Executor executor, Context context) {
        super(executor);
        this.mContext = context;
        List registerOrganizer = registerOrganizer(1);
        for (int size = registerOrganizer.size() - 1; size >= 0; size--) {
            onDisplayAreaAppeared(((DisplayAreaAppearedInfo) registerOrganizer.get(size)).getDisplayAreaInfo(), ((DisplayAreaAppearedInfo) registerOrganizer.get(size)).getLeash());
        }
    }

    public void registerListener(int i, RootTaskDisplayAreaListener rootTaskDisplayAreaListener) {
        ArrayList<RootTaskDisplayAreaListener> arrayList = this.mListeners.get(i);
        if (arrayList == null) {
            arrayList = new ArrayList<>();
            this.mListeners.put(i, arrayList);
        }
        arrayList.add(rootTaskDisplayAreaListener);
        DisplayAreaInfo displayAreaInfo = this.mDisplayAreasInfo.get(i);
        if (displayAreaInfo != null) {
            rootTaskDisplayAreaListener.onDisplayAreaAppeared(displayAreaInfo);
        }
    }

    public void attachToDisplayArea(int i, SurfaceControl.Builder builder) {
        builder.setParent(this.mLeashes.get(i));
    }

    public void onDisplayAreaAppeared(DisplayAreaInfo displayAreaInfo, SurfaceControl surfaceControl) {
        if (displayAreaInfo.featureId == 1) {
            int i = displayAreaInfo.displayId;
            if (this.mDisplayAreasInfo.get(i) == null) {
                this.mDisplayAreasInfo.put(i, displayAreaInfo);
                this.mLeashes.put(i, surfaceControl);
                ArrayList<RootTaskDisplayAreaListener> arrayList = this.mListeners.get(i);
                if (arrayList != null) {
                    for (int size = arrayList.size() - 1; size >= 0; size--) {
                        arrayList.get(size).onDisplayAreaAppeared(displayAreaInfo);
                    }
                }
                applyConfigChangesToContext(displayAreaInfo);
                return;
            }
            throw new IllegalArgumentException("Duplicate DA for displayId: " + i + " displayAreaInfo:" + displayAreaInfo + " mDisplayAreasInfo.get():" + this.mDisplayAreasInfo.get(i));
        }
        throw new IllegalArgumentException("Unknown feature: " + displayAreaInfo.featureId + "displayAreaInfo:" + displayAreaInfo);
    }

    public void onDisplayAreaVanished(DisplayAreaInfo displayAreaInfo) {
        int i = displayAreaInfo.displayId;
        if (this.mDisplayAreasInfo.get(i) != null) {
            this.mDisplayAreasInfo.remove(i);
            ArrayList<RootTaskDisplayAreaListener> arrayList = this.mListeners.get(i);
            if (arrayList != null) {
                for (int size = arrayList.size() - 1; size >= 0; size--) {
                    arrayList.get(size).onDisplayAreaVanished(displayAreaInfo);
                }
            }
            this.mDisplayAreaContexts.remove(i);
            return;
        }
        throw new IllegalArgumentException("onDisplayAreaVanished() Unknown DA displayId: " + i + " displayAreaInfo:" + displayAreaInfo + " mDisplayAreasInfo.get():" + this.mDisplayAreasInfo.get(i));
    }

    public void onDisplayAreaInfoChanged(DisplayAreaInfo displayAreaInfo) {
        int i = displayAreaInfo.displayId;
        if (this.mDisplayAreasInfo.get(i) != null) {
            this.mDisplayAreasInfo.put(i, displayAreaInfo);
            ArrayList<RootTaskDisplayAreaListener> arrayList = this.mListeners.get(i);
            if (arrayList != null) {
                for (int size = arrayList.size() - 1; size >= 0; size--) {
                    arrayList.get(size).onDisplayAreaInfoChanged(displayAreaInfo);
                }
            }
            applyConfigChangesToContext(displayAreaInfo);
            return;
        }
        throw new IllegalArgumentException("onDisplayAreaInfoChanged() Unknown DA displayId: " + i + " displayAreaInfo:" + displayAreaInfo + " mDisplayAreasInfo.get():" + this.mDisplayAreasInfo.get(i));
    }

    private void applyConfigChangesToContext(DisplayAreaInfo displayAreaInfo) {
        int i = displayAreaInfo.displayId;
        Display display = ((DisplayManager) this.mContext.getSystemService(DisplayManager.class)).getDisplay(i);
        if (display != null) {
            DisplayAreaContext displayAreaContext = this.mDisplayAreaContexts.get(i);
            if (displayAreaContext == null) {
                displayAreaContext = new DisplayAreaContext(this.mContext, display);
                this.mDisplayAreaContexts.put(i, displayAreaContext);
            }
            displayAreaContext.updateConfigurationChanges(displayAreaInfo.configuration);
        } else if (ShellProtoLogCache.WM_SHELL_TASK_ORG_enabled) {
            ShellProtoLogImpl.w(ShellProtoLogGroup.WM_SHELL_TASK_ORG, -581313329, 1, null, Long.valueOf((long) i));
        }
    }

    public String toString() {
        return TAG + "#" + this.mDisplayAreasInfo.size();
    }

    /* loaded from: classes2.dex */
    public static class DisplayAreaContext extends ContextWrapper {
        private final ResourcesManager mResourcesManager = ResourcesManager.getInstance();
        private final IBinder mToken;

        public DisplayAreaContext(Context context, Display display) {
            super(null);
            Binder binder = new Binder();
            this.mToken = binder;
            attachBaseContext(context.createTokenContext(binder, display));
        }

        /* access modifiers changed from: private */
        public void updateConfigurationChanges(Configuration configuration) {
            if (getResources().getConfiguration().diff(configuration) != 0) {
                this.mResourcesManager.updateResourcesForActivity(this.mToken, configuration, getDisplayId());
            }
        }
    }
}
