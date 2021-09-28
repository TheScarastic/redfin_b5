package com.android.systemui.shortcut;

import android.content.Context;
import android.os.RemoteException;
import android.view.IWindowManager;
import android.view.WindowManagerGlobal;
import com.android.internal.policy.DividerSnapAlgorithm;
import com.android.systemui.SystemUI;
import com.android.systemui.shortcut.ShortcutKeyServiceProxy;
import com.android.wm.shell.legacysplitscreen.DividerView;
import com.android.wm.shell.legacysplitscreen.LegacySplitScreen;
import java.util.Optional;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public class ShortcutKeyDispatcher extends SystemUI implements ShortcutKeyServiceProxy.Callbacks {
    private final Optional<LegacySplitScreen> mSplitScreenOptional;
    private ShortcutKeyServiceProxy mShortcutKeyServiceProxy = new ShortcutKeyServiceProxy(this);
    private IWindowManager mWindowManagerService = WindowManagerGlobal.getWindowManagerService();
    protected final long META_MASK = 281474976710656L;
    protected final long ALT_MASK = 8589934592L;
    protected final long CTRL_MASK = 17592186044416L;
    protected final long SHIFT_MASK = 4294967296L;
    protected final long SC_DOCK_LEFT = 281474976710727L;
    protected final long SC_DOCK_RIGHT = 281474976710728L;

    public ShortcutKeyDispatcher(Context context, Optional<LegacySplitScreen> optional) {
        super(context);
        this.mSplitScreenOptional = optional;
    }

    public void registerShortcutKey(long j) {
        try {
            this.mWindowManagerService.registerShortcutKey(j, this.mShortcutKeyServiceProxy);
        } catch (RemoteException unused) {
        }
    }

    @Override // com.android.systemui.shortcut.ShortcutKeyServiceProxy.Callbacks
    public void onShortcutKeyPressed(long j) {
        int i = this.mContext.getResources().getConfiguration().orientation;
        if ((j == 281474976710727L || j == 281474976710728L) && i == 2) {
            handleDockKey(j);
        }
    }

    @Override // com.android.systemui.SystemUI
    public void start() {
        registerShortcutKey(281474976710727L);
        registerShortcutKey(281474976710728L);
    }

    private void handleDockKey(long j) {
        this.mSplitScreenOptional.ifPresent(new Consumer(j) { // from class: com.android.systemui.shortcut.ShortcutKeyDispatcher$$ExternalSyntheticLambda0
            public final /* synthetic */ long f$1;

            {
                this.f$1 = r2;
            }

            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ShortcutKeyDispatcher.$r8$lambda$xgYha9PxSzoM87wW9yw4JPhKiGc(ShortcutKeyDispatcher.this, this.f$1, (LegacySplitScreen) obj);
            }
        });
    }

    public /* synthetic */ void lambda$handleDockKey$0(long j, LegacySplitScreen legacySplitScreen) {
        DividerSnapAlgorithm.SnapTarget snapTarget;
        if (legacySplitScreen.isDividerVisible()) {
            DividerView dividerView = legacySplitScreen.getDividerView();
            DividerSnapAlgorithm snapAlgorithm = dividerView.getSnapAlgorithm();
            DividerSnapAlgorithm.SnapTarget calculateNonDismissingSnapTarget = snapAlgorithm.calculateNonDismissingSnapTarget(dividerView.getCurrentPosition());
            if (j == 281474976710727L) {
                snapTarget = snapAlgorithm.getPreviousTarget(calculateNonDismissingSnapTarget);
            } else {
                snapTarget = snapAlgorithm.getNextTarget(calculateNonDismissingSnapTarget);
            }
            dividerView.startDragging(true, false);
            dividerView.stopDragging(snapTarget.position, 0.0f, false, true);
            return;
        }
        legacySplitScreen.splitPrimaryTask();
    }
}
