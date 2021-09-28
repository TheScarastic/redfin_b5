package com.google.android.systemui.smartspace;

import android.app.smartspace.SmartspaceAction;
import android.app.smartspace.SmartspaceTarget;
import android.content.ComponentName;
import android.content.Context;
import android.media.MediaMetadata;
import android.os.UserHandle;
import android.text.TextUtils;
import android.view.View;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.R$string;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.plugins.BcSmartspaceDataPlugin;
import com.android.systemui.settings.CurrentUserTracker;
import com.android.systemui.statusbar.NotificationMediaManager;
import com.android.systemui.util.concurrency.DelayableExecutor;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: KeyguardMediaViewController.kt */
/* loaded from: classes2.dex */
public final class KeyguardMediaViewController {
    private CharSequence artist;
    private final BroadcastDispatcher broadcastDispatcher;
    private final Context context;
    private final ComponentName mediaComponent;
    private final KeyguardMediaViewController$mediaListener$1 mediaListener = new KeyguardMediaViewController$mediaListener$1(this);
    private final NotificationMediaManager mediaManager;
    private final BcSmartspaceDataPlugin plugin;
    private BcSmartspaceDataPlugin.SmartspaceView smartspaceView;
    private CharSequence title;
    private final DelayableExecutor uiExecutor;
    private CurrentUserTracker userTracker;

    @VisibleForTesting
    public static /* synthetic */ void getSmartspaceView$annotations() {
    }

    public KeyguardMediaViewController(Context context, BcSmartspaceDataPlugin bcSmartspaceDataPlugin, DelayableExecutor delayableExecutor, NotificationMediaManager notificationMediaManager, BroadcastDispatcher broadcastDispatcher) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(bcSmartspaceDataPlugin, "plugin");
        Intrinsics.checkNotNullParameter(delayableExecutor, "uiExecutor");
        Intrinsics.checkNotNullParameter(notificationMediaManager, "mediaManager");
        Intrinsics.checkNotNullParameter(broadcastDispatcher, "broadcastDispatcher");
        this.context = context;
        this.plugin = bcSmartspaceDataPlugin;
        this.uiExecutor = delayableExecutor;
        this.mediaManager = notificationMediaManager;
        this.broadcastDispatcher = broadcastDispatcher;
        this.mediaComponent = new ComponentName(context, KeyguardMediaViewController.class);
    }

    public final DelayableExecutor getUiExecutor() {
        return this.uiExecutor;
    }

    public final BcSmartspaceDataPlugin.SmartspaceView getSmartspaceView() {
        return this.smartspaceView;
    }

    public final void setSmartspaceView(BcSmartspaceDataPlugin.SmartspaceView smartspaceView) {
        this.smartspaceView = smartspaceView;
    }

    public final void init() {
        this.plugin.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener(this) { // from class: com.google.android.systemui.smartspace.KeyguardMediaViewController$init$1
            final /* synthetic */ KeyguardMediaViewController this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
            }

            @Override // android.view.View.OnAttachStateChangeListener
            public void onViewAttachedToWindow(View view) {
                Intrinsics.checkNotNullParameter(view, "v");
                this.this$0.setSmartspaceView((BcSmartspaceDataPlugin.SmartspaceView) view);
                KeyguardMediaViewController.access$getMediaManager$p(this.this$0).addCallback(KeyguardMediaViewController.access$getMediaListener$p(this.this$0));
            }

            @Override // android.view.View.OnAttachStateChangeListener
            public void onViewDetachedFromWindow(View view) {
                Intrinsics.checkNotNullParameter(view, "v");
                this.this$0.setSmartspaceView(null);
                KeyguardMediaViewController.access$getMediaManager$p(this.this$0).removeCallback(KeyguardMediaViewController.access$getMediaListener$p(this.this$0));
            }
        });
        this.userTracker = new CurrentUserTracker(this, this.broadcastDispatcher) { // from class: com.google.android.systemui.smartspace.KeyguardMediaViewController$init$2
            final /* synthetic */ KeyguardMediaViewController this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
            }

            @Override // com.android.systemui.settings.CurrentUserTracker
            public void onUserSwitched(int i) {
                this.this$0.reset();
            }
        };
    }

    public final void updateMediaInfo(MediaMetadata mediaMetadata, int i) {
        CharSequence charSequence;
        CharSequence charSequence2;
        if (!NotificationMediaManager.isPlayingState(i)) {
            reset();
            return;
        }
        Unit unit = null;
        if (mediaMetadata == null) {
            charSequence = null;
        } else {
            charSequence = mediaMetadata.getText("android.media.metadata.TITLE");
            if (TextUtils.isEmpty(charSequence)) {
                charSequence = this.context.getResources().getString(R$string.music_controls_no_title);
            }
        }
        if (mediaMetadata == null) {
            charSequence2 = null;
        } else {
            charSequence2 = mediaMetadata.getText("android.media.metadata.ARTIST");
        }
        if (!TextUtils.equals(this.title, charSequence) || !TextUtils.equals(this.artist, charSequence2)) {
            this.title = charSequence;
            this.artist = charSequence2;
            if (charSequence != null) {
                SmartspaceAction build = new SmartspaceAction.Builder("deviceMediaTitle", charSequence.toString()).setSubtitle(this.artist).setIcon(this.mediaManager.getMediaIcon()).build();
                CurrentUserTracker currentUserTracker = this.userTracker;
                if (currentUserTracker != null) {
                    SmartspaceTarget build2 = new SmartspaceTarget.Builder("deviceMedia", this.mediaComponent, UserHandle.of(currentUserTracker.getCurrentUserId())).setFeatureType(15).setHeaderAction(build).build();
                    BcSmartspaceDataPlugin.SmartspaceView smartspaceView = getSmartspaceView();
                    if (smartspaceView != null) {
                        smartspaceView.setMediaTarget(build2);
                        unit = Unit.INSTANCE;
                    }
                } else {
                    Intrinsics.throwUninitializedPropertyAccessException("userTracker");
                    throw null;
                }
            }
            if (unit == null) {
                reset();
            }
        }
    }

    public final void reset() {
        this.title = null;
        this.artist = null;
        BcSmartspaceDataPlugin.SmartspaceView smartspaceView = this.smartspaceView;
        if (smartspaceView != null) {
            smartspaceView.setMediaTarget(null);
        }
    }
}
