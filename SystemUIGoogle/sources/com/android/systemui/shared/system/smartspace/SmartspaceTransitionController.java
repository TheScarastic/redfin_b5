package com.android.systemui.shared.system.smartspace;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.graphics.Rect;
import android.view.View;
import com.android.systemui.shared.system.ActivityManagerWrapper;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: SmartspaceTransitionController.kt */
/* loaded from: classes.dex */
public final class SmartspaceTransitionController {
    public static final Companion Companion = new Companion(null);
    private ISmartspaceCallback launcherSmartspace;
    private View lockscreenSmartspace;
    private SmartspaceState mLauncherSmartspaceState;
    private final SmartspaceTransitionController$ISmartspaceTransitionController$1 ISmartspaceTransitionController = new SmartspaceTransitionController$ISmartspaceTransitionController$1(this);
    private final Rect smartspaceOriginBounds = new Rect();
    private final Rect smartspaceDestinationBounds = new Rect();

    public final ISmartspaceCallback getLauncherSmartspace() {
        return this.launcherSmartspace;
    }

    public final void setLauncherSmartspace(ISmartspaceCallback iSmartspaceCallback) {
        this.launcherSmartspace = iSmartspaceCallback;
    }

    public final View getLockscreenSmartspace() {
        return this.lockscreenSmartspace;
    }

    public final void setLockscreenSmartspace(View view) {
        this.lockscreenSmartspace = view;
    }

    public final void setMLauncherSmartspaceState(SmartspaceState smartspaceState) {
        this.mLauncherSmartspaceState = smartspaceState;
    }

    public final ISmartspaceTransitionController createExternalInterface() {
        return this.ISmartspaceTransitionController;
    }

    public final SmartspaceState updateLauncherSmartSpaceState() {
        ISmartspaceCallback iSmartspaceCallback = this.launcherSmartspace;
        SmartspaceState smartspaceState = iSmartspaceCallback == null ? null : iSmartspaceCallback.getSmartspaceState();
        setMLauncherSmartspaceState(smartspaceState);
        return smartspaceState;
    }

    public final void prepareForUnlockTransition() {
        Rect rect;
        SmartspaceState updateLauncherSmartSpaceState = updateLauncherSmartSpaceState();
        if (updateLauncherSmartSpaceState == null) {
            rect = null;
        } else {
            rect = updateLauncherSmartSpaceState.getBoundsOnScreen();
        }
        if (rect != null && getLockscreenSmartspace() != null) {
            View lockscreenSmartspace = getLockscreenSmartspace();
            Intrinsics.checkNotNull(lockscreenSmartspace);
            lockscreenSmartspace.getBoundsOnScreen(this.smartspaceOriginBounds);
            Rect rect2 = this.smartspaceDestinationBounds;
            rect2.set(updateLauncherSmartSpaceState.getBoundsOnScreen());
            View lockscreenSmartspace2 = getLockscreenSmartspace();
            Intrinsics.checkNotNull(lockscreenSmartspace2);
            View lockscreenSmartspace3 = getLockscreenSmartspace();
            Intrinsics.checkNotNull(lockscreenSmartspace3);
            rect2.offset(-lockscreenSmartspace2.getPaddingLeft(), -lockscreenSmartspace3.getPaddingTop());
        }
    }

    public final void setProgressToDestinationBounds(float f) {
        if (isSmartspaceTransitionPossible()) {
            float min = Math.min(1.0f, f);
            Rect rect = this.smartspaceDestinationBounds;
            int i = rect.left;
            Rect rect2 = this.smartspaceOriginBounds;
            float f2 = ((float) (i - rect2.left)) * min;
            float f3 = ((float) (rect.top - rect2.top)) * min;
            Rect rect3 = new Rect();
            View lockscreenSmartspace = getLockscreenSmartspace();
            Intrinsics.checkNotNull(lockscreenSmartspace);
            lockscreenSmartspace.getBoundsOnScreen(rect3);
            Rect rect4 = this.smartspaceOriginBounds;
            float f4 = (((float) rect4.left) + f2) - ((float) rect3.left);
            float f5 = (((float) rect4.top) + f3) - ((float) rect3.top);
            View view = this.lockscreenSmartspace;
            Intrinsics.checkNotNull(view);
            view.setTranslationX(view.getTranslationX() + f4);
            view.setTranslationY(view.getTranslationY() + f5);
        }
    }

    public final boolean isSmartspaceTransitionPossible() {
        SmartspaceState smartspaceState = this.mLauncherSmartspaceState;
        Rect boundsOnScreen = smartspaceState == null ? null : smartspaceState.getBoundsOnScreen();
        boolean isEmpty = boundsOnScreen == null ? true : boundsOnScreen.isEmpty();
        if (!Companion.isLauncherUnderneath() || isEmpty) {
            return false;
        }
        return true;
    }

    /* compiled from: SmartspaceTransitionController.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final boolean isLauncherUnderneath() {
            ComponentName componentName;
            ActivityManager.RunningTaskInfo runningTask = ActivityManagerWrapper.getInstance().getRunningTask();
            String str = null;
            if (!(runningTask == null || (componentName = runningTask.topActivity) == null)) {
                str = componentName.getClassName();
            }
            if (str == null) {
                return false;
            }
            return str.equals("com.google.android.apps.nexuslauncher.NexusLauncherActivity");
        }
    }
}
