package com.android.wm.shell.pip.phone;

import android.app.RemoteAction;
import android.content.Context;
import android.content.pm.ParceledListSlice;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.util.Size;
import android.view.IWindow;
import android.view.MotionEvent;
import android.view.SurfaceControl;
import android.view.SyncRtSurfaceTransactionApplier;
import android.view.WindowManagerGlobal;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.common.SystemWindows;
import com.android.wm.shell.pip.PipBoundsState;
import com.android.wm.shell.pip.PipMediaController;
import com.android.wm.shell.pip.PipMenuController;
import com.android.wm.shell.pip.phone.PhonePipMenuController;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
/* loaded from: classes2.dex */
public class PhonePipMenuController implements PipMenuController {
    private ParceledListSlice<RemoteAction> mAppActions;
    private SyncRtSurfaceTransactionApplier mApplier;
    private final Context mContext;
    private final ShellExecutor mMainExecutor;
    private final Handler mMainHandler;
    private ParceledListSlice<RemoteAction> mMediaActions;
    private final PipMediaController mMediaController;
    private int mMenuState;
    private final PipBoundsState mPipBoundsState;
    private IBinder mPipMenuInputToken;
    private PipMenuView mPipMenuView;
    private final SystemWindows mSystemWindows;
    private final Matrix mMoveTransform = new Matrix();
    private final Rect mTmpSourceBounds = new Rect();
    private final RectF mTmpSourceRectF = new RectF();
    private final RectF mTmpDestinationRectF = new RectF();
    private final ArrayList<Listener> mListeners = new ArrayList<>();
    private PipMediaController.ActionListener mMediaActionListener = new PipMediaController.ActionListener() { // from class: com.android.wm.shell.pip.phone.PhonePipMenuController.1
        @Override // com.android.wm.shell.pip.PipMediaController.ActionListener
        public void onMediaActionsChanged(List<RemoteAction> list) {
            PhonePipMenuController.this.mMediaActions = new ParceledListSlice(list);
            PhonePipMenuController.this.updateMenuActions();
        }
    };
    private final float[] mTmpValues = new float[9];
    private final Runnable mUpdateEmbeddedMatrix = new Runnable() { // from class: com.android.wm.shell.pip.phone.PhonePipMenuController$$ExternalSyntheticLambda0
        @Override // java.lang.Runnable
        public final void run() {
            PhonePipMenuController.this.lambda$new$0();
        }
    };

    /* loaded from: classes2.dex */
    public interface Listener {
        void onPipDismiss();

        void onPipExpand();

        void onPipMenuStateChangeFinish(int i);

        void onPipMenuStateChangeStart(int i, boolean z, Runnable runnable);

        void onPipShowMenu();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        PipMenuView pipMenuView = this.mPipMenuView;
        if (pipMenuView != null && pipMenuView.getViewRootImpl() != null) {
            this.mMoveTransform.getValues(this.mTmpValues);
            try {
                this.mPipMenuView.getViewRootImpl().getAccessibilityEmbeddedConnection().setScreenMatrix(this.mTmpValues);
            } catch (RemoteException unused) {
            }
        }
    }

    public PhonePipMenuController(Context context, PipBoundsState pipBoundsState, PipMediaController pipMediaController, SystemWindows systemWindows, ShellExecutor shellExecutor, Handler handler) {
        this.mContext = context;
        this.mPipBoundsState = pipBoundsState;
        this.mMediaController = pipMediaController;
        this.mSystemWindows = systemWindows;
        this.mMainExecutor = shellExecutor;
        this.mMainHandler = handler;
    }

    @Override // com.android.wm.shell.pip.PipMenuController
    public boolean isMenuVisible() {
        return (this.mPipMenuView == null || this.mMenuState == 0) ? false : true;
    }

    @Override // com.android.wm.shell.pip.PipMenuController
    public void attach(SurfaceControl surfaceControl) {
        attachPipMenuView();
    }

    @Override // com.android.wm.shell.pip.PipMenuController
    public void detach() {
        hideMenu();
        detachPipMenuView();
    }

    private void attachPipMenuView() {
        if (this.mPipMenuView != null) {
            detachPipMenuView();
        }
        PipMenuView pipMenuView = new PipMenuView(this.mContext, this, this.mMainExecutor, this.mMainHandler);
        this.mPipMenuView = pipMenuView;
        this.mSystemWindows.addView(pipMenuView, getPipMenuLayoutParams("PipMenuView", 0, 0), 0, 1);
    }

    private void detachPipMenuView() {
        PipMenuView pipMenuView = this.mPipMenuView;
        if (pipMenuView != null) {
            this.mApplier = null;
            this.mSystemWindows.removeView(pipMenuView);
            this.mPipMenuView = null;
            this.mPipMenuInputToken = null;
        }
    }

    @Override // com.android.wm.shell.pip.PipMenuController
    public void updateMenuBounds(Rect rect) {
        this.mSystemWindows.updateViewLayout(this.mPipMenuView, getPipMenuLayoutParams("PipMenuView", rect.width(), rect.height()));
        updateMenuLayout(rect);
    }

    public SurfaceControl getSurfaceControl() {
        return this.mSystemWindows.getViewSurface(this.mPipMenuView);
    }

    public void addListener(Listener listener) {
        if (!this.mListeners.contains(listener)) {
            this.mListeners.add(listener);
        }
    }

    /* access modifiers changed from: package-private */
    public Size getEstimatedMinMenuSize() {
        PipMenuView pipMenuView = this.mPipMenuView;
        if (pipMenuView == null) {
            return null;
        }
        return pipMenuView.getEstimatedMinMenuSize();
    }

    public void showMenu() {
        this.mListeners.forEach(PhonePipMenuController$$ExternalSyntheticLambda5.INSTANCE);
    }

    public void showMenuWithPossibleDelay(int i, Rect rect, boolean z, boolean z2, boolean z3) {
        if (z2) {
            fadeOutMenu();
        }
        showMenuInternal(i, rect, z, z2, z2, z3);
    }

    public void showMenu(int i, Rect rect, boolean z, boolean z2, boolean z3) {
        showMenuInternal(i, rect, z, z2, false, z3);
    }

    private void showMenuInternal(int i, Rect rect, boolean z, boolean z2, boolean z3, boolean z4) {
        if (maybeCreateSyncApplier()) {
            movePipMenu(null, null, rect);
            updateMenuBounds(rect);
            this.mPipMenuView.showMenu(i, rect, z, z2, z3, z4);
        }
    }

    @Override // com.android.wm.shell.pip.PipMenuController
    public void movePipMenu(SurfaceControl surfaceControl, SurfaceControl.Transaction transaction, Rect rect) {
        if (!rect.isEmpty() && maybeCreateSyncApplier()) {
            if (surfaceControl == null || transaction == null) {
                this.mTmpSourceBounds.set(0, 0, rect.width(), rect.height());
            } else {
                this.mPipMenuView.getBoundsOnScreen(this.mTmpSourceBounds);
            }
            this.mTmpSourceRectF.set(this.mTmpSourceBounds);
            this.mTmpDestinationRectF.set(rect);
            this.mMoveTransform.setRectToRect(this.mTmpSourceRectF, this.mTmpDestinationRectF, Matrix.ScaleToFit.FILL);
            SyncRtSurfaceTransactionApplier.SurfaceParams build = new SyncRtSurfaceTransactionApplier.SurfaceParams.Builder(getSurfaceControl()).withMatrix(this.mMoveTransform).build();
            if (surfaceControl == null || transaction == null) {
                this.mApplier.scheduleApply(new SyncRtSurfaceTransactionApplier.SurfaceParams[]{build});
            } else {
                this.mApplier.scheduleApply(new SyncRtSurfaceTransactionApplier.SurfaceParams[]{build, new SyncRtSurfaceTransactionApplier.SurfaceParams.Builder(surfaceControl).withMergeTransaction(transaction).build()});
            }
            if (this.mPipMenuView.getViewRootImpl() != null) {
                this.mPipMenuView.getHandler().removeCallbacks(this.mUpdateEmbeddedMatrix);
                this.mPipMenuView.getHandler().post(this.mUpdateEmbeddedMatrix);
            }
        }
    }

    @Override // com.android.wm.shell.pip.PipMenuController
    public void resizePipMenu(SurfaceControl surfaceControl, SurfaceControl.Transaction transaction, Rect rect) {
        if (!rect.isEmpty() && maybeCreateSyncApplier()) {
            SyncRtSurfaceTransactionApplier.SurfaceParams build = new SyncRtSurfaceTransactionApplier.SurfaceParams.Builder(getSurfaceControl()).withWindowCrop(rect).build();
            if (surfaceControl == null || transaction == null) {
                this.mApplier.scheduleApply(new SyncRtSurfaceTransactionApplier.SurfaceParams[]{build});
                return;
            }
            this.mApplier.scheduleApply(new SyncRtSurfaceTransactionApplier.SurfaceParams[]{build, new SyncRtSurfaceTransactionApplier.SurfaceParams.Builder(surfaceControl).withMergeTransaction(transaction).build()});
        }
    }

    private boolean maybeCreateSyncApplier() {
        PipMenuView pipMenuView = this.mPipMenuView;
        if (pipMenuView == null || pipMenuView.getViewRootImpl() == null) {
            Log.v("PhonePipMenuController", "Not going to move PiP, either menu or its parent is not created.");
            return false;
        }
        if (this.mApplier == null) {
            this.mApplier = new SyncRtSurfaceTransactionApplier(this.mPipMenuView);
            this.mPipMenuInputToken = this.mPipMenuView.getViewRootImpl().getInputToken();
        }
        if (this.mApplier != null) {
            return true;
        }
        return false;
    }

    public void pokeMenu() {
        if (isMenuVisible()) {
            this.mPipMenuView.pokeMenu();
        }
    }

    private void fadeOutMenu() {
        if (isMenuVisible()) {
            this.mPipMenuView.fadeOutMenu();
        }
    }

    public void hideMenu() {
        if (isMenuVisible()) {
            this.mPipMenuView.hideMenu();
        }
    }

    public void hideMenu(int i, boolean z) {
        if (isMenuVisible()) {
            this.mPipMenuView.hideMenu(z, i);
        }
    }

    public void hideMenu(Runnable runnable, Runnable runnable2) {
        if (isMenuVisible()) {
            if (runnable != null) {
                runnable.run();
            }
            this.mPipMenuView.hideMenu(runnable2);
        }
    }

    public void setAppActions(ParceledListSlice<RemoteAction> parceledListSlice) {
        this.mAppActions = parceledListSlice;
        updateMenuActions();
    }

    /* access modifiers changed from: package-private */
    public void onPipExpand() {
        this.mListeners.forEach(PhonePipMenuController$$ExternalSyntheticLambda4.INSTANCE);
    }

    /* access modifiers changed from: package-private */
    public void onPipDismiss() {
        this.mListeners.forEach(PhonePipMenuController$$ExternalSyntheticLambda3.INSTANCE);
    }

    private ParceledListSlice<RemoteAction> resolveMenuActions() {
        if (isValidActions(this.mAppActions)) {
            return this.mAppActions;
        }
        return this.mMediaActions;
    }

    /* access modifiers changed from: private */
    public void updateMenuActions() {
        ParceledListSlice<RemoteAction> resolveMenuActions;
        if (this.mPipMenuView != null && (resolveMenuActions = resolveMenuActions()) != null) {
            this.mPipMenuView.setActions(this.mPipBoundsState.getBounds(), resolveMenuActions.getList());
        }
    }

    private static boolean isValidActions(ParceledListSlice<?> parceledListSlice) {
        return parceledListSlice != null && parceledListSlice.getList().size() > 0;
    }

    /* access modifiers changed from: package-private */
    public void onMenuStateChangeStart(int i, boolean z, Runnable runnable) {
        if (i != this.mMenuState) {
            this.mListeners.forEach(new Consumer(i, z, runnable) { // from class: com.android.wm.shell.pip.phone.PhonePipMenuController$$ExternalSyntheticLambda2
                public final /* synthetic */ int f$0;
                public final /* synthetic */ boolean f$1;
                public final /* synthetic */ Runnable f$2;

                {
                    this.f$0 = r1;
                    this.f$1 = r2;
                    this.f$2 = r3;
                }

                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    ((PhonePipMenuController.Listener) obj).onPipMenuStateChangeStart(this.f$0, this.f$1, this.f$2);
                }
            });
            if (i == 2) {
                this.mMediaController.addActionListener(this.mMediaActionListener);
            } else {
                this.mMediaController.removeActionListener(this.mMediaActionListener);
            }
            try {
                WindowManagerGlobal.getWindowSession().grantEmbeddedWindowFocus((IWindow) null, this.mPipMenuInputToken, i != 0);
            } catch (RemoteException e) {
                Log.e("PhonePipMenuController", "Unable to update focus as menu appears/disappears", e);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void onMenuStateChangeFinish(int i) {
        if (i != this.mMenuState) {
            this.mListeners.forEach(new Consumer(i) { // from class: com.android.wm.shell.pip.phone.PhonePipMenuController$$ExternalSyntheticLambda1
                public final /* synthetic */ int f$0;

                {
                    this.f$0 = r1;
                }

                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    ((PhonePipMenuController.Listener) obj).onPipMenuStateChangeFinish(this.f$0);
                }
            });
        }
        this.mMenuState = i;
        if (i != 0) {
            this.mSystemWindows.setShellRootAccessibilityWindow(0, 1, this.mPipMenuView);
        } else {
            this.mSystemWindows.setShellRootAccessibilityWindow(0, 1, null);
        }
    }

    /* access modifiers changed from: package-private */
    public void handlePointerEvent(MotionEvent motionEvent) {
        if (this.mPipMenuView != null) {
            if (motionEvent.isTouchEvent()) {
                this.mPipMenuView.dispatchTouchEvent(motionEvent);
            } else {
                this.mPipMenuView.dispatchGenericMotionEvent(motionEvent);
            }
        }
    }

    public void updateMenuLayout(Rect rect) {
        if (isMenuVisible()) {
            this.mPipMenuView.updateMenuLayout(rect);
        }
    }

    /* access modifiers changed from: package-private */
    public void dump(PrintWriter printWriter, String str) {
        String str2 = str + "  ";
        printWriter.println(str + "PhonePipMenuController");
        printWriter.println(str2 + "mMenuState=" + this.mMenuState);
        printWriter.println(str2 + "mPipMenuView=" + this.mPipMenuView);
        printWriter.println(str2 + "mListeners=" + this.mListeners.size());
    }
}
