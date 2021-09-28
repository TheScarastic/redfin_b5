package com.android.wm.shell.pip.phone;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.Region;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.MagnificationSpec;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.IAccessibilityInteractionConnection;
import android.view.accessibility.IAccessibilityInteractionConnectionCallback;
import com.android.wm.shell.R;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.pip.PipBoundsState;
import com.android.wm.shell.pip.PipSnapAlgorithm;
import com.android.wm.shell.pip.PipTaskOrganizer;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
/* loaded from: classes2.dex */
public class PipAccessibilityInteractionConnection {
    private List<AccessibilityNodeInfo> mAccessibilityNodeInfoList;
    private final AccessibilityCallbacks mCallbacks;
    private final Context mContext;
    private final ShellExecutor mMainExcutor;
    private final PipMotionHelper mMotionHelper;
    private final PipBoundsState mPipBoundsState;
    private final PipSnapAlgorithm mSnapAlgorithm;
    private final PipTaskOrganizer mTaskOrganizer;
    private final Runnable mUnstashCallback;
    private final Runnable mUpdateMovementBoundCallback;
    private final Rect mNormalBounds = new Rect();
    private final Rect mExpandedBounds = new Rect();
    private final Rect mNormalMovementBounds = new Rect();
    private final Rect mExpandedMovementBounds = new Rect();
    private Rect mTmpBounds = new Rect();
    private final IAccessibilityInteractionConnection mConnectionImpl = new PipAccessibilityInteractionConnectionImpl();

    /* loaded from: classes2.dex */
    public interface AccessibilityCallbacks {
        void onAccessibilityShowMenu();
    }

    public PipAccessibilityInteractionConnection(Context context, PipBoundsState pipBoundsState, PipMotionHelper pipMotionHelper, PipTaskOrganizer pipTaskOrganizer, PipSnapAlgorithm pipSnapAlgorithm, AccessibilityCallbacks accessibilityCallbacks, Runnable runnable, Runnable runnable2, ShellExecutor shellExecutor) {
        this.mContext = context;
        this.mMainExcutor = shellExecutor;
        this.mPipBoundsState = pipBoundsState;
        this.mMotionHelper = pipMotionHelper;
        this.mTaskOrganizer = pipTaskOrganizer;
        this.mSnapAlgorithm = pipSnapAlgorithm;
        this.mUpdateMovementBoundCallback = runnable;
        this.mUnstashCallback = runnable2;
        this.mCallbacks = accessibilityCallbacks;
    }

    public void register(AccessibilityManager accessibilityManager) {
        accessibilityManager.setPictureInPictureActionReplacingConnection(this.mConnectionImpl);
    }

    public void findAccessibilityNodeInfoByAccessibilityId(long j, Region region, int i, IAccessibilityInteractionConnectionCallback iAccessibilityInteractionConnectionCallback, int i2, int i3, long j2, MagnificationSpec magnificationSpec, Bundle bundle) {
        try {
            iAccessibilityInteractionConnectionCallback.setFindAccessibilityNodeInfosResult(j == AccessibilityNodeInfo.ROOT_NODE_ID ? getNodeList() : null, i);
        } catch (RemoteException unused) {
        }
    }

    public void performAccessibilityAction(long j, int i, Bundle bundle, int i2, IAccessibilityInteractionConnectionCallback iAccessibilityInteractionConnectionCallback, int i3, int i4, long j2) {
        boolean z = false;
        if (j == AccessibilityNodeInfo.ROOT_NODE_ID) {
            if (i == R.id.action_pip_resize) {
                if (this.mPipBoundsState.getBounds().width() == this.mNormalBounds.width() && this.mPipBoundsState.getBounds().height() == this.mNormalBounds.height()) {
                    setToExpandedBounds();
                } else {
                    setToNormalBounds();
                }
            } else if (i == R.id.action_pip_stash) {
                this.mMotionHelper.animateToStashedClosestEdge();
            } else if (i == R.id.action_pip_unstash) {
                this.mUnstashCallback.run();
                this.mPipBoundsState.setStashed(0);
            } else if (i == 16) {
                this.mCallbacks.onAccessibilityShowMenu();
            } else if (i == 262144) {
                this.mMotionHelper.expandLeavePip();
            } else if (i == 1048576) {
                this.mMotionHelper.dismissPip();
            } else if (i == 16908354) {
                int i5 = bundle.getInt("ACTION_ARGUMENT_MOVE_WINDOW_X");
                int i6 = bundle.getInt("ACTION_ARGUMENT_MOVE_WINDOW_Y");
                new Rect().set(this.mPipBoundsState.getBounds());
                this.mTmpBounds.offsetTo(i5, i6);
                this.mMotionHelper.movePip(this.mTmpBounds);
            }
            z = true;
        }
        try {
            iAccessibilityInteractionConnectionCallback.setPerformAccessibilityActionResult(z, i2);
        } catch (RemoteException unused) {
        }
    }

    private void setToExpandedBounds() {
        this.mSnapAlgorithm.applySnapFraction(this.mExpandedBounds, this.mExpandedMovementBounds, this.mSnapAlgorithm.getSnapFraction(this.mPipBoundsState.getBounds(), this.mNormalMovementBounds));
        this.mTaskOrganizer.scheduleFinishResizePip(this.mExpandedBounds, new Consumer() { // from class: com.android.wm.shell.pip.phone.PipAccessibilityInteractionConnection$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                PipAccessibilityInteractionConnection.$r8$lambda$aAFUql1gEGsABVQ7Nu_hgl6xnqw(PipAccessibilityInteractionConnection.this, (Rect) obj);
            }
        });
    }

    public /* synthetic */ void lambda$setToExpandedBounds$0(Rect rect) {
        this.mMotionHelper.synchronizePinnedStackBounds();
        this.mUpdateMovementBoundCallback.run();
    }

    private void setToNormalBounds() {
        this.mSnapAlgorithm.applySnapFraction(this.mNormalBounds, this.mNormalMovementBounds, this.mSnapAlgorithm.getSnapFraction(this.mPipBoundsState.getBounds(), this.mExpandedMovementBounds));
        this.mTaskOrganizer.scheduleFinishResizePip(this.mNormalBounds, new Consumer() { // from class: com.android.wm.shell.pip.phone.PipAccessibilityInteractionConnection$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                PipAccessibilityInteractionConnection.$r8$lambda$R68wdMaWRtjeBwG0VY_Hz4Hmhfw(PipAccessibilityInteractionConnection.this, (Rect) obj);
            }
        });
    }

    public /* synthetic */ void lambda$setToNormalBounds$1(Rect rect) {
        this.mMotionHelper.synchronizePinnedStackBounds();
        this.mUpdateMovementBoundCallback.run();
    }

    public void findAccessibilityNodeInfosByViewId(long j, String str, Region region, int i, IAccessibilityInteractionConnectionCallback iAccessibilityInteractionConnectionCallback, int i2, int i3, long j2, MagnificationSpec magnificationSpec) {
        try {
            iAccessibilityInteractionConnectionCallback.setFindAccessibilityNodeInfoResult((AccessibilityNodeInfo) null, i);
        } catch (RemoteException unused) {
        }
    }

    public void findAccessibilityNodeInfosByText(long j, String str, Region region, int i, IAccessibilityInteractionConnectionCallback iAccessibilityInteractionConnectionCallback, int i2, int i3, long j2, MagnificationSpec magnificationSpec) {
        try {
            iAccessibilityInteractionConnectionCallback.setFindAccessibilityNodeInfoResult((AccessibilityNodeInfo) null, i);
        } catch (RemoteException unused) {
        }
    }

    public void findFocus(long j, int i, Region region, int i2, IAccessibilityInteractionConnectionCallback iAccessibilityInteractionConnectionCallback, int i3, int i4, long j2, MagnificationSpec magnificationSpec) {
        try {
            iAccessibilityInteractionConnectionCallback.setFindAccessibilityNodeInfoResult((AccessibilityNodeInfo) null, i2);
        } catch (RemoteException unused) {
        }
    }

    public void focusSearch(long j, int i, Region region, int i2, IAccessibilityInteractionConnectionCallback iAccessibilityInteractionConnectionCallback, int i3, int i4, long j2, MagnificationSpec magnificationSpec) {
        try {
            iAccessibilityInteractionConnectionCallback.setFindAccessibilityNodeInfoResult((AccessibilityNodeInfo) null, i2);
        } catch (RemoteException unused) {
        }
    }

    public void onMovementBoundsChanged(Rect rect, Rect rect2, Rect rect3, Rect rect4) {
        this.mNormalBounds.set(rect);
        this.mExpandedBounds.set(rect2);
        this.mNormalMovementBounds.set(rect3);
        this.mExpandedMovementBounds.set(rect4);
    }

    public static AccessibilityNodeInfo obtainRootAccessibilityNodeInfo(Context context) {
        AccessibilityNodeInfo obtain = AccessibilityNodeInfo.obtain();
        obtain.setSourceNodeId(AccessibilityNodeInfo.ROOT_NODE_ID, -3);
        obtain.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_CLICK);
        obtain.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_DISMISS);
        obtain.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_MOVE_WINDOW);
        obtain.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_EXPAND);
        obtain.addAction(new AccessibilityNodeInfo.AccessibilityAction(R.id.action_pip_resize, context.getString(R.string.accessibility_action_pip_resize)));
        obtain.addAction(new AccessibilityNodeInfo.AccessibilityAction(R.id.action_pip_stash, context.getString(R.string.accessibility_action_pip_stash)));
        obtain.addAction(new AccessibilityNodeInfo.AccessibilityAction(R.id.action_pip_unstash, context.getString(R.string.accessibility_action_pip_unstash)));
        obtain.setImportantForAccessibility(true);
        obtain.setClickable(true);
        obtain.setVisibleToUser(true);
        return obtain;
    }

    private List<AccessibilityNodeInfo> getNodeList() {
        if (this.mAccessibilityNodeInfoList == null) {
            this.mAccessibilityNodeInfoList = new ArrayList(1);
        }
        AccessibilityNodeInfo obtainRootAccessibilityNodeInfo = obtainRootAccessibilityNodeInfo(this.mContext);
        this.mAccessibilityNodeInfoList.clear();
        this.mAccessibilityNodeInfoList.add(obtainRootAccessibilityNodeInfo);
        return this.mAccessibilityNodeInfoList;
    }

    /* loaded from: classes2.dex */
    public class PipAccessibilityInteractionConnectionImpl extends IAccessibilityInteractionConnection.Stub {
        public void clearAccessibilityFocus() throws RemoteException {
        }

        public void notifyOutsideTouch() throws RemoteException {
        }

        private PipAccessibilityInteractionConnectionImpl() {
            PipAccessibilityInteractionConnection.this = r1;
        }

        public void findAccessibilityNodeInfoByAccessibilityId(long j, Region region, int i, IAccessibilityInteractionConnectionCallback iAccessibilityInteractionConnectionCallback, int i2, int i3, long j2, MagnificationSpec magnificationSpec, Bundle bundle) throws RemoteException {
            PipAccessibilityInteractionConnection.this.mMainExcutor.execute(new PipAccessibilityInteractionConnection$PipAccessibilityInteractionConnectionImpl$$ExternalSyntheticLambda3(this, j, region, i, iAccessibilityInteractionConnectionCallback, i2, i3, j2, magnificationSpec, bundle));
        }

        public /* synthetic */ void lambda$findAccessibilityNodeInfoByAccessibilityId$0(long j, Region region, int i, IAccessibilityInteractionConnectionCallback iAccessibilityInteractionConnectionCallback, int i2, int i3, long j2, MagnificationSpec magnificationSpec, Bundle bundle) {
            PipAccessibilityInteractionConnection.this.findAccessibilityNodeInfoByAccessibilityId(j, region, i, iAccessibilityInteractionConnectionCallback, i2, i3, j2, magnificationSpec, bundle);
        }

        public void findAccessibilityNodeInfosByViewId(long j, String str, Region region, int i, IAccessibilityInteractionConnectionCallback iAccessibilityInteractionConnectionCallback, int i2, int i3, long j2, MagnificationSpec magnificationSpec) throws RemoteException {
            PipAccessibilityInteractionConnection.this.mMainExcutor.execute(new PipAccessibilityInteractionConnection$PipAccessibilityInteractionConnectionImpl$$ExternalSyntheticLambda5(this, j, str, region, i, iAccessibilityInteractionConnectionCallback, i2, i3, j2, magnificationSpec));
        }

        public /* synthetic */ void lambda$findAccessibilityNodeInfosByViewId$1(long j, String str, Region region, int i, IAccessibilityInteractionConnectionCallback iAccessibilityInteractionConnectionCallback, int i2, int i3, long j2, MagnificationSpec magnificationSpec) {
            PipAccessibilityInteractionConnection.this.findAccessibilityNodeInfosByViewId(j, str, region, i, iAccessibilityInteractionConnectionCallback, i2, i3, j2, magnificationSpec);
        }

        public void findAccessibilityNodeInfosByText(long j, String str, Region region, int i, IAccessibilityInteractionConnectionCallback iAccessibilityInteractionConnectionCallback, int i2, int i3, long j2, MagnificationSpec magnificationSpec) throws RemoteException {
            PipAccessibilityInteractionConnection.this.mMainExcutor.execute(new PipAccessibilityInteractionConnection$PipAccessibilityInteractionConnectionImpl$$ExternalSyntheticLambda4(this, j, str, region, i, iAccessibilityInteractionConnectionCallback, i2, i3, j2, magnificationSpec));
        }

        public /* synthetic */ void lambda$findAccessibilityNodeInfosByText$2(long j, String str, Region region, int i, IAccessibilityInteractionConnectionCallback iAccessibilityInteractionConnectionCallback, int i2, int i3, long j2, MagnificationSpec magnificationSpec) {
            PipAccessibilityInteractionConnection.this.findAccessibilityNodeInfosByText(j, str, region, i, iAccessibilityInteractionConnectionCallback, i2, i3, j2, magnificationSpec);
        }

        public void findFocus(long j, int i, Region region, int i2, IAccessibilityInteractionConnectionCallback iAccessibilityInteractionConnectionCallback, int i3, int i4, long j2, MagnificationSpec magnificationSpec) throws RemoteException {
            PipAccessibilityInteractionConnection.this.mMainExcutor.execute(new PipAccessibilityInteractionConnection$PipAccessibilityInteractionConnectionImpl$$ExternalSyntheticLambda1(this, j, i, region, i2, iAccessibilityInteractionConnectionCallback, i3, i4, j2, magnificationSpec));
        }

        public /* synthetic */ void lambda$findFocus$3(long j, int i, Region region, int i2, IAccessibilityInteractionConnectionCallback iAccessibilityInteractionConnectionCallback, int i3, int i4, long j2, MagnificationSpec magnificationSpec) {
            PipAccessibilityInteractionConnection.this.findFocus(j, i, region, i2, iAccessibilityInteractionConnectionCallback, i3, i4, j2, magnificationSpec);
        }

        public void focusSearch(long j, int i, Region region, int i2, IAccessibilityInteractionConnectionCallback iAccessibilityInteractionConnectionCallback, int i3, int i4, long j2, MagnificationSpec magnificationSpec) throws RemoteException {
            PipAccessibilityInteractionConnection.this.mMainExcutor.execute(new PipAccessibilityInteractionConnection$PipAccessibilityInteractionConnectionImpl$$ExternalSyntheticLambda0(this, j, i, region, i2, iAccessibilityInteractionConnectionCallback, i3, i4, j2, magnificationSpec));
        }

        public /* synthetic */ void lambda$focusSearch$4(long j, int i, Region region, int i2, IAccessibilityInteractionConnectionCallback iAccessibilityInteractionConnectionCallback, int i3, int i4, long j2, MagnificationSpec magnificationSpec) {
            PipAccessibilityInteractionConnection.this.focusSearch(j, i, region, i2, iAccessibilityInteractionConnectionCallback, i3, i4, j2, magnificationSpec);
        }

        public void performAccessibilityAction(long j, int i, Bundle bundle, int i2, IAccessibilityInteractionConnectionCallback iAccessibilityInteractionConnectionCallback, int i3, int i4, long j2) throws RemoteException {
            PipAccessibilityInteractionConnection.this.mMainExcutor.execute(new PipAccessibilityInteractionConnection$PipAccessibilityInteractionConnectionImpl$$ExternalSyntheticLambda2(this, j, i, bundle, i2, iAccessibilityInteractionConnectionCallback, i3, i4, j2));
        }

        public /* synthetic */ void lambda$performAccessibilityAction$5(long j, int i, Bundle bundle, int i2, IAccessibilityInteractionConnectionCallback iAccessibilityInteractionConnectionCallback, int i3, int i4, long j2) {
            PipAccessibilityInteractionConnection.this.performAccessibilityAction(j, i, bundle, i2, iAccessibilityInteractionConnectionCallback, i3, i4, j2);
        }
    }
}
