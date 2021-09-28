package com.android.systemui.media;

import android.content.Context;
import android.content.res.Configuration;
import android.view.ViewGroup;
import android.view.ViewParent;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.FeatureFlags;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.SysuiStatusBarStateController;
import com.android.systemui.statusbar.notification.stack.MediaHeaderView;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.util.Utils;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: KeyguardMediaController.kt */
/* loaded from: classes.dex */
public final class KeyguardMediaController {
    private final KeyguardBypassController bypassController;
    private final Context context;
    private final FeatureFlags featureFlags;
    private final MediaHost mediaHost;
    private final NotificationLockscreenUserManager notifLockscreenUserManager;
    private MediaHeaderView singlePaneContainer;
    private ViewGroup splitShadeContainer;
    private final SysuiStatusBarStateController statusBarStateController;
    private boolean useSplitShade;
    private Function1<? super Boolean, Unit> visibilityChangedListener;
    private boolean visible;

    public static /* synthetic */ void getUseSplitShade$annotations() {
    }

    public KeyguardMediaController(MediaHost mediaHost, KeyguardBypassController keyguardBypassController, SysuiStatusBarStateController sysuiStatusBarStateController, NotificationLockscreenUserManager notificationLockscreenUserManager, FeatureFlags featureFlags, Context context, ConfigurationController configurationController) {
        Intrinsics.checkNotNullParameter(mediaHost, "mediaHost");
        Intrinsics.checkNotNullParameter(keyguardBypassController, "bypassController");
        Intrinsics.checkNotNullParameter(sysuiStatusBarStateController, "statusBarStateController");
        Intrinsics.checkNotNullParameter(notificationLockscreenUserManager, "notifLockscreenUserManager");
        Intrinsics.checkNotNullParameter(featureFlags, "featureFlags");
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(configurationController, "configurationController");
        this.mediaHost = mediaHost;
        this.bypassController = keyguardBypassController;
        this.statusBarStateController = sysuiStatusBarStateController;
        this.notifLockscreenUserManager = notificationLockscreenUserManager;
        this.featureFlags = featureFlags;
        this.context = context;
        sysuiStatusBarStateController.addCallback(new StatusBarStateController.StateListener(this) { // from class: com.android.systemui.media.KeyguardMediaController.1
            final /* synthetic */ KeyguardMediaController this$0;

            {
                this.this$0 = r1;
            }

            @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
            public void onStateChanged(int i) {
                this.this$0.refreshMediaPosition();
            }
        });
        configurationController.addCallback(new ConfigurationController.ConfigurationListener(this) { // from class: com.android.systemui.media.KeyguardMediaController.2
            final /* synthetic */ KeyguardMediaController this$0;

            {
                this.this$0 = r1;
            }

            @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
            public void onConfigChanged(Configuration configuration) {
                this.this$0.updateResources();
            }
        });
        mediaHost.setExpansion(0.0f);
        mediaHost.setShowsOnlyActiveMedia(true);
        mediaHost.setFalsingProtectionNeeded(true);
        mediaHost.init(2);
        updateResources();
    }

    public final void updateResources() {
        setUseSplitShade(Utils.shouldUseSplitNotificationShade(this.featureFlags, this.context.getResources()));
    }

    public final void setUseSplitShade(boolean z) {
        if (this.useSplitShade != z) {
            this.useSplitShade = z;
            reattachHostView();
            refreshMediaPosition();
        }
    }

    public final void setVisibilityChangedListener(Function1<? super Boolean, Unit> function1) {
        this.visibilityChangedListener = function1;
    }

    public final MediaHeaderView getSinglePaneContainer() {
        return this.singlePaneContainer;
    }

    public final void attachSinglePaneContainer(MediaHeaderView mediaHeaderView) {
        boolean z = this.singlePaneContainer == null;
        this.singlePaneContainer = mediaHeaderView;
        if (z) {
            this.mediaHost.addVisibilityChangeListener(new Function1<Boolean, Unit>(this) { // from class: com.android.systemui.media.KeyguardMediaController$attachSinglePaneContainer$1
                /* Return type fixed from 'java.lang.Object' to match base method */
                /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object] */
                @Override // kotlin.jvm.functions.Function1
                public /* bridge */ /* synthetic */ Unit invoke(Boolean bool) {
                    invoke(bool.booleanValue());
                    return Unit.INSTANCE;
                }

                public final void invoke(boolean z2) {
                    KeyguardMediaController.access$onMediaHostVisibilityChanged((KeyguardMediaController) this.receiver, z2);
                }
            });
        }
        reattachHostView();
        onMediaHostVisibilityChanged(this.mediaHost.getVisible());
    }

    public final void onMediaHostVisibilityChanged(boolean z) {
        refreshMediaPosition();
        if (z) {
            ViewGroup.LayoutParams layoutParams = this.mediaHost.getHostView().getLayoutParams();
            layoutParams.height = -2;
            layoutParams.width = -1;
        }
    }

    public final void attachSplitShadeContainer(ViewGroup viewGroup) {
        Intrinsics.checkNotNullParameter(viewGroup, "container");
        this.splitShadeContainer = viewGroup;
        reattachHostView();
        refreshMediaPosition();
    }

    private final void reattachHostView() {
        ViewGroup viewGroup;
        ViewGroup viewGroup2;
        Integer num;
        Integer num2;
        if (this.useSplitShade) {
            viewGroup2 = this.splitShadeContainer;
            viewGroup = this.singlePaneContainer;
        } else {
            viewGroup = this.splitShadeContainer;
            viewGroup2 = this.singlePaneContainer;
        }
        ViewGroup viewGroup3 = null;
        if (viewGroup == null) {
            num = null;
        } else {
            num = Integer.valueOf(viewGroup.getChildCount());
        }
        if (num != null && num.intValue() == 1) {
            viewGroup.removeAllViews();
        }
        if (viewGroup2 == null) {
            num2 = null;
        } else {
            num2 = Integer.valueOf(viewGroup2.getChildCount());
        }
        if (num2 != null && num2.intValue() == 0) {
            ViewParent parent = this.mediaHost.getHostView().getParent();
            if (parent != null) {
                if (parent instanceof ViewGroup) {
                    viewGroup3 = (ViewGroup) parent;
                }
                if (viewGroup3 != null) {
                    viewGroup3.removeView(this.mediaHost.getHostView());
                }
            }
            viewGroup2.addView(this.mediaHost.getHostView());
        }
    }

    public final void refreshMediaPosition() {
        boolean z = false;
        boolean z2 = this.statusBarStateController.getState() == 1 || this.statusBarStateController.getState() == 3;
        if (this.mediaHost.getVisible() && !this.bypassController.getBypassEnabled() && z2 && this.notifLockscreenUserManager.shouldShowLockscreenNotifications()) {
            z = true;
        }
        this.visible = z;
        if (z) {
            showMediaPlayer();
        } else {
            hideMediaPlayer();
        }
    }

    private final void showMediaPlayer() {
        if (this.useSplitShade) {
            setVisibility(this.splitShadeContainer, 0);
            setVisibility(this.singlePaneContainer, 8);
            return;
        }
        setVisibility(this.singlePaneContainer, 0);
        setVisibility(this.splitShadeContainer, 8);
    }

    private final void hideMediaPlayer() {
        setVisibility(this.splitShadeContainer, 8);
        setVisibility(this.singlePaneContainer, 8);
    }

    private final void setVisibility(ViewGroup viewGroup, int i) {
        Function1<? super Boolean, Unit> function1;
        Integer valueOf = viewGroup == null ? null : Integer.valueOf(viewGroup.getVisibility());
        if (viewGroup != null) {
            viewGroup.setVisibility(i);
        }
        if ((valueOf == null || valueOf.intValue() != i) && (function1 = this.visibilityChangedListener) != null) {
            function1.invoke(Boolean.valueOf(i == 0));
        }
    }
}
