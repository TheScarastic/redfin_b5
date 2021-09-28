package com.android.wm.shell.pip.tv;

import android.app.RemoteAction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ParceledListSlice;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceControl;
import com.android.wm.shell.common.SystemWindows;
import com.android.wm.shell.pip.PipBoundsState;
import com.android.wm.shell.pip.PipMediaController;
import com.android.wm.shell.pip.PipMenuController;
import com.android.wm.shell.pip.tv.TvPipMenuView;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes2.dex */
public class TvPipMenuController implements PipMenuController, TvPipMenuView.Listener {
    private final Context mContext;
    private Delegate mDelegate;
    private SurfaceControl mLeash;
    private final Handler mMainHandler;
    private TvPipMenuView mMenuView;
    private final PipBoundsState mPipBoundsState;
    private final SystemWindows mSystemWindows;
    private final List<RemoteAction> mMediaActions = new ArrayList();
    private final List<RemoteAction> mAppActions = new ArrayList();

    /* access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public interface Delegate {
        @Override // com.android.wm.shell.pip.tv.TvPipNotificationController.Delegate
        void closePip();

        void movePipToFullscreen();

        void movePipToNormalPosition();
    }

    public TvPipMenuController(Context context, PipBoundsState pipBoundsState, SystemWindows systemWindows, PipMediaController pipMediaController, Handler handler) {
        this.mContext = context;
        this.mPipBoundsState = pipBoundsState;
        this.mSystemWindows = systemWindows;
        this.mMainHandler = handler;
        context.registerReceiverForAllUsers(new BroadcastReceiver() { // from class: com.android.wm.shell.pip.tv.TvPipMenuController.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                TvPipMenuController.this.hideMenu();
            }
        }, new IntentFilter("android.intent.action.CLOSE_SYSTEM_DIALOGS"), null, handler);
        pipMediaController.addActionListener(new PipMediaController.ActionListener() { // from class: com.android.wm.shell.pip.tv.TvPipMenuController$$ExternalSyntheticLambda0
            @Override // com.android.wm.shell.pip.PipMediaController.ActionListener
            public final void onMediaActionsChanged(List list) {
                TvPipMenuController.this.onMediaActionsChanged(list);
            }
        });
    }

    /* access modifiers changed from: package-private */
    public void setDelegate(Delegate delegate) {
        Log.d("TvPipMenuController", "setDelegate(), delegate=" + delegate);
        if (this.mDelegate != null) {
            throw new IllegalStateException("The delegate has already been set and should not change.");
        } else if (delegate != null) {
            this.mDelegate = delegate;
        } else {
            throw new IllegalArgumentException("The delegate must not be null.");
        }
    }

    @Override // com.android.wm.shell.pip.PipMenuController
    public void attach(SurfaceControl surfaceControl) {
        if (this.mDelegate != null) {
            this.mLeash = surfaceControl;
            attachPipMenuView();
            return;
        }
        throw new IllegalStateException("Delegate is not set.");
    }

    private void attachPipMenuView() {
        Log.d("TvPipMenuController", "attachPipMenuView()");
        if (this.mMenuView != null) {
            detachPipMenuView();
        }
        TvPipMenuView tvPipMenuView = new TvPipMenuView(this.mContext);
        this.mMenuView = tvPipMenuView;
        tvPipMenuView.setListener(this);
        this.mSystemWindows.addView(this.mMenuView, getPipMenuLayoutParams("PipMenuView", 0, 0), 0, 1);
    }

    public void showMenu() {
        Log.d("TvPipMenuController", "showMenu()");
        TvPipMenuView tvPipMenuView = this.mMenuView;
        if (tvPipMenuView != null) {
            this.mSystemWindows.updateViewLayout(tvPipMenuView, getPipMenuLayoutParams("PipMenuView", this.mPipBoundsState.getDisplayBounds().width(), this.mPipBoundsState.getDisplayBounds().height()));
            maybeUpdateMenuViewActions();
            this.mMenuView.show();
            if (this.mMenuView.getWindowSurfaceControl() != null && this.mLeash != null) {
                SurfaceControl.Transaction transaction = new SurfaceControl.Transaction();
                transaction.setRelativeLayer(this.mMenuView.getWindowSurfaceControl(), this.mLeash, -1);
                transaction.apply();
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void hideMenu() {
        hideMenu(true);
    }

    void hideMenu(boolean z) {
        Log.d("TvPipMenuController", "hideMenu(), movePipWindow=" + z);
        if (isMenuVisible()) {
            this.mMenuView.hide();
            if (z) {
                this.mDelegate.movePipToNormalPosition();
            }
        }
    }

    @Override // com.android.wm.shell.pip.PipMenuController
    public void detach() {
        hideMenu();
        detachPipMenuView();
        this.mLeash = null;
    }

    private void detachPipMenuView() {
        Log.d("TvPipMenuController", "detachPipMenuView()");
        TvPipMenuView tvPipMenuView = this.mMenuView;
        if (tvPipMenuView != null) {
            this.mSystemWindows.removeView(tvPipMenuView);
            this.mMenuView = null;
        }
    }

    public void setAppActions(ParceledListSlice<RemoteAction> parceledListSlice) {
        Log.d("TvPipMenuController", "setAppActions()");
        updateAdditionalActionsList(this.mAppActions, parceledListSlice.getList());
    }

    /* access modifiers changed from: private */
    public void onMediaActionsChanged(List<RemoteAction> list) {
        Log.d("TvPipMenuController", "onMediaActionsChanged()");
        updateAdditionalActionsList(this.mMediaActions, list);
    }

    private void updateAdditionalActionsList(List<RemoteAction> list, List<RemoteAction> list2) {
        int size = list2 != null ? list2.size() : 0;
        if (size != 0 || !list.isEmpty()) {
            list.clear();
            if (size > 0) {
                list.addAll(list2);
            }
            maybeUpdateMenuViewActions();
        }
    }

    private void maybeUpdateMenuViewActions() {
        if (this.mMenuView != null) {
            if (!this.mAppActions.isEmpty()) {
                this.mMenuView.setAdditionalActions(this.mAppActions, this.mMainHandler);
            } else {
                this.mMenuView.setAdditionalActions(this.mMediaActions, this.mMainHandler);
            }
        }
    }

    @Override // com.android.wm.shell.pip.PipMenuController
    public boolean isMenuVisible() {
        TvPipMenuView tvPipMenuView = this.mMenuView;
        return tvPipMenuView != null && tvPipMenuView.isVisible();
    }

    @Override // com.android.wm.shell.pip.tv.TvPipMenuView.Listener
    public void onBackPress() {
        hideMenu();
    }

    @Override // com.android.wm.shell.pip.tv.TvPipMenuView.Listener
    public void onCloseButtonClick() {
        this.mDelegate.closePip();
    }

    @Override // com.android.wm.shell.pip.tv.TvPipMenuView.Listener
    public void onFullscreenButtonClick() {
        this.mDelegate.movePipToFullscreen();
    }
}
