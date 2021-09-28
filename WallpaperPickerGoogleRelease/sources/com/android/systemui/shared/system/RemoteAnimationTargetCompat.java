package com.android.systemui.shared.system;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.ArrayMap;
import android.view.RemoteAnimationTarget;
import android.view.SurfaceControl;
import android.window.TransitionInfo;
import java.util.ArrayList;
/* loaded from: classes.dex */
public class RemoteAnimationTargetCompat {
    public static final int ACTIVITY_TYPE_ASSISTANT = 4;
    public static final int ACTIVITY_TYPE_HOME = 2;
    public static final int ACTIVITY_TYPE_RECENTS = 3;
    public static final int ACTIVITY_TYPE_STANDARD = 1;
    public static final int ACTIVITY_TYPE_UNDEFINED = 0;
    public static final int MODE_CHANGING = 2;
    public static final int MODE_CLOSING = 1;
    public static final int MODE_OPENING = 0;
    public final int activityType;
    public final Rect clipRect;
    public final Rect contentInsets;
    public final boolean isNotInRecents;
    public final boolean isTranslucent;
    public final SurfaceControlCompat leash;
    public final Rect localBounds;
    private final SurfaceControl mStartLeash;
    public final int mode;
    public final Point position;
    public final int prefixOrderIndex;
    public final int rotationChange;
    public final Rect screenSpaceBounds;
    public final Rect sourceContainerBounds;
    public final int taskId;
    public final ActivityManager.RunningTaskInfo taskInfo;
    public final int windowType;

    public RemoteAnimationTargetCompat(RemoteAnimationTarget remoteAnimationTarget) {
        this.taskId = remoteAnimationTarget.taskId;
        this.mode = remoteAnimationTarget.mode;
        this.leash = new SurfaceControlCompat(remoteAnimationTarget.leash);
        this.isTranslucent = remoteAnimationTarget.isTranslucent;
        this.clipRect = remoteAnimationTarget.clipRect;
        this.position = remoteAnimationTarget.position;
        this.localBounds = remoteAnimationTarget.localBounds;
        this.sourceContainerBounds = remoteAnimationTarget.sourceContainerBounds;
        this.screenSpaceBounds = remoteAnimationTarget.screenSpaceBounds;
        this.prefixOrderIndex = remoteAnimationTarget.prefixOrderIndex;
        this.isNotInRecents = remoteAnimationTarget.isNotInRecents;
        this.contentInsets = remoteAnimationTarget.contentInsets;
        this.activityType = remoteAnimationTarget.windowConfiguration.getActivityType();
        this.taskInfo = remoteAnimationTarget.taskInfo;
        this.rotationChange = 0;
        this.mStartLeash = remoteAnimationTarget.startLeash;
        this.windowType = remoteAnimationTarget.windowType;
    }

    @SuppressLint({"NewApi"})
    private static SurfaceControl createLeash(TransitionInfo transitionInfo, TransitionInfo.Change change, int i, SurfaceControl.Transaction transaction) {
        SurfaceControl surfaceControl;
        if (change.getParent() != null && (change.getFlags() & 2) != 0) {
            return change.getLeash();
        }
        SurfaceControl.Builder builder = new SurfaceControl.Builder();
        SurfaceControl.Builder containerLayer = builder.setName(change.getLeash().toString() + "_transition-leash").setContainerLayer();
        if (change.getParent() == null) {
            surfaceControl = transitionInfo.getRootLeash();
        } else {
            surfaceControl = transitionInfo.getChange(change.getParent()).getLeash();
        }
        SurfaceControl build = containerLayer.setParent(surfaceControl).build();
        setupLeash(build, change, transitionInfo.getChanges().size() - i, transitionInfo, transaction);
        transaction.reparent(change.getLeash(), build);
        transaction.setAlpha(change.getLeash(), 1.0f);
        transaction.show(change.getLeash());
        transaction.setPosition(change.getLeash(), 0.0f, 0.0f);
        transaction.setLayer(change.getLeash(), 0);
        return build;
    }

    private static int newModeToLegacyMode(int i) {
        if (i == 1) {
            return 0;
        }
        if (i != 2) {
            if (i == 3) {
                return 0;
            }
            if (i != 4) {
                return 2;
            }
        }
        return 1;
    }

    @SuppressLint({"NewApi"})
    private static void setupLeash(SurfaceControl surfaceControl, TransitionInfo.Change change, int i, TransitionInfo transitionInfo, SurfaceControl.Transaction transaction) {
        boolean z = false;
        boolean z2 = transitionInfo.getType() == 1 || transitionInfo.getType() == 3;
        int size = transitionInfo.getChanges().size();
        int mode = change.getMode();
        if (TransitionInfo.isIndependent(change, transitionInfo)) {
            if (change.getParent() != null) {
                z = true;
            }
            if (!z) {
                transaction.reparent(surfaceControl, transitionInfo.getRootLeash());
                transaction.setPosition(surfaceControl, (float) (change.getStartAbsBounds().left - transitionInfo.getRootOffset().x), (float) (change.getStartAbsBounds().top - transitionInfo.getRootOffset().y));
            }
            transaction.show(surfaceControl);
            if (mode == 1 || mode == 3) {
                if (z2) {
                    transaction.setLayer(surfaceControl, (transitionInfo.getChanges().size() + size) - i);
                    if ((change.getFlags() & 8) == 0) {
                        transaction.setAlpha(surfaceControl, 0.0f);
                        return;
                    }
                    return;
                }
                transaction.setLayer(surfaceControl, size - i);
            } else if (mode != 2 && mode != 4) {
                transaction.setLayer(surfaceControl, (transitionInfo.getChanges().size() + size) - i);
            } else if (z2) {
                transaction.setLayer(surfaceControl, size - i);
            } else {
                transaction.setLayer(surfaceControl, (transitionInfo.getChanges().size() + size) - i);
            }
        } else if (mode == 1 || mode == 3 || mode == 6) {
            transaction.show(surfaceControl);
            transaction.setPosition(surfaceControl, (float) change.getEndRelOffset().x, (float) change.getEndRelOffset().y);
        }
    }

    public static RemoteAnimationTargetCompat[] wrap(RemoteAnimationTarget[] remoteAnimationTargetArr) {
        int length = remoteAnimationTargetArr != null ? remoteAnimationTargetArr.length : 0;
        RemoteAnimationTargetCompat[] remoteAnimationTargetCompatArr = new RemoteAnimationTargetCompat[length];
        for (int i = 0; i < length; i++) {
            remoteAnimationTargetCompatArr[i] = new RemoteAnimationTargetCompat(remoteAnimationTargetArr[i]);
        }
        return remoteAnimationTargetCompatArr;
    }

    public void release() {
        this.leash.mSurfaceControl.release();
        SurfaceControl surfaceControl = this.mStartLeash;
        if (surfaceControl != null) {
            surfaceControl.release();
        }
    }

    public static RemoteAnimationTargetCompat[] wrap(TransitionInfo transitionInfo, boolean z, SurfaceControl.Transaction transaction, ArrayMap<SurfaceControl, SurfaceControl> arrayMap) {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < transitionInfo.getChanges().size(); i++) {
            if (z == ((((TransitionInfo.Change) transitionInfo.getChanges().get(i)).getFlags() & 2) != 0)) {
                arrayList.add(new RemoteAnimationTargetCompat((TransitionInfo.Change) transitionInfo.getChanges().get(i), transitionInfo.getChanges().size() - i, transitionInfo, transaction));
                if (arrayMap != null) {
                    arrayMap.put(((TransitionInfo.Change) transitionInfo.getChanges().get(i)).getLeash(), ((RemoteAnimationTargetCompat) arrayList.get(arrayList.size() - 1)).leash.mSurfaceControl);
                }
            }
        }
        return (RemoteAnimationTargetCompat[]) arrayList.toArray(new RemoteAnimationTargetCompat[arrayList.size()]);
    }

    public RemoteAnimationTargetCompat(TransitionInfo.Change change, int i, TransitionInfo transitionInfo, SurfaceControl.Transaction transaction) {
        this.taskId = change.getTaskInfo() != null ? change.getTaskInfo().taskId : -1;
        this.mode = newModeToLegacyMode(change.getMode());
        this.leash = new SurfaceControlCompat(createLeash(transitionInfo, change, i, transaction));
        this.isTranslucent = ((change.getFlags() & 4) == 0 && (change.getFlags() & 1) == 0) ? false : true;
        this.clipRect = null;
        this.position = null;
        Rect rect = new Rect(change.getEndAbsBounds());
        this.localBounds = rect;
        rect.offsetTo(change.getEndRelOffset().x, change.getEndRelOffset().y);
        this.sourceContainerBounds = null;
        this.screenSpaceBounds = new Rect(change.getEndAbsBounds());
        this.prefixOrderIndex = i;
        this.contentInsets = new Rect(0, 0, 0, 0);
        if (change.getTaskInfo() != null) {
            this.isNotInRecents = !change.getTaskInfo().isRunning;
            this.activityType = change.getTaskInfo().getActivityType();
        } else {
            this.isNotInRecents = true;
            this.activityType = 0;
        }
        this.taskInfo = change.getTaskInfo();
        this.mStartLeash = null;
        this.rotationChange = change.getEndRotation() - change.getStartRotation();
        this.windowType = -1;
    }
}
