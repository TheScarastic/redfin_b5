package com.android.systemui.controls.dagger;

import android.content.Context;
import android.database.ContentObserver;
import android.provider.Settings;
import com.android.internal.widget.LockPatternUtils;
import com.android.systemui.controls.controller.ControlsController;
import com.android.systemui.controls.management.ControlsListingController;
import com.android.systemui.controls.ui.ControlsUiController;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.settings.SecureSettings;
import dagger.Lazy;
import java.util.Optional;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ControlsComponent.kt */
/* loaded from: classes.dex */
public final class ControlsComponent {
    private boolean canShowWhileLockedSetting;
    private final Context context;
    private final boolean featureEnabled;
    private final KeyguardStateController keyguardStateController;
    private final Lazy<ControlsController> lazyControlsController;
    private final Lazy<ControlsListingController> lazyControlsListingController;
    private final Lazy<ControlsUiController> lazyControlsUiController;
    private final LockPatternUtils lockPatternUtils;
    private final SecureSettings secureSettings;
    private final ContentObserver showWhileLockedObserver;
    private final UserTracker userTracker;

    /* compiled from: ControlsComponent.kt */
    /* loaded from: classes.dex */
    public enum Visibility {
        AVAILABLE,
        AVAILABLE_AFTER_UNLOCK,
        UNAVAILABLE
    }

    public ControlsComponent(boolean z, Context context, Lazy<ControlsController> lazy, Lazy<ControlsUiController> lazy2, Lazy<ControlsListingController> lazy3, LockPatternUtils lockPatternUtils, KeyguardStateController keyguardStateController, UserTracker userTracker, SecureSettings secureSettings) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(lazy, "lazyControlsController");
        Intrinsics.checkNotNullParameter(lazy2, "lazyControlsUiController");
        Intrinsics.checkNotNullParameter(lazy3, "lazyControlsListingController");
        Intrinsics.checkNotNullParameter(lockPatternUtils, "lockPatternUtils");
        Intrinsics.checkNotNullParameter(keyguardStateController, "keyguardStateController");
        Intrinsics.checkNotNullParameter(userTracker, "userTracker");
        Intrinsics.checkNotNullParameter(secureSettings, "secureSettings");
        this.featureEnabled = z;
        this.context = context;
        this.lazyControlsController = lazy;
        this.lazyControlsUiController = lazy2;
        this.lazyControlsListingController = lazy3;
        this.lockPatternUtils = lockPatternUtils;
        this.keyguardStateController = keyguardStateController;
        this.userTracker = userTracker;
        this.secureSettings = secureSettings;
        ControlsComponent$showWhileLockedObserver$1 controlsComponent$showWhileLockedObserver$1 = new ContentObserver(this) { // from class: com.android.systemui.controls.dagger.ControlsComponent$showWhileLockedObserver$1
            final /* synthetic */ ControlsComponent this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
            }

            @Override // android.database.ContentObserver
            public void onChange(boolean z2) {
                ControlsComponent.access$updateShowWhileLocked(this.this$0);
            }
        };
        this.showWhileLockedObserver = controlsComponent$showWhileLockedObserver$1;
        if (z) {
            secureSettings.registerContentObserver(Settings.Secure.getUriFor("lockscreen_show_controls"), false, controlsComponent$showWhileLockedObserver$1);
            updateShowWhileLocked();
        }
    }

    public final Optional<ControlsController> getControlsController() {
        Optional<ControlsController> optional;
        String str;
        if (this.featureEnabled) {
            optional = Optional.of(this.lazyControlsController.get());
            str = "of(lazyControlsController.get())";
        } else {
            optional = Optional.empty();
            str = "empty()";
        }
        Intrinsics.checkNotNullExpressionValue(optional, str);
        return optional;
    }

    public final Optional<ControlsListingController> getControlsListingController() {
        if (this.featureEnabled) {
            Optional<ControlsListingController> of = Optional.of(this.lazyControlsListingController.get());
            Intrinsics.checkNotNullExpressionValue(of, "{\n            Optional.of(lazyControlsListingController.get())\n        }");
            return of;
        }
        Optional<ControlsListingController> empty = Optional.empty();
        Intrinsics.checkNotNullExpressionValue(empty, "{\n            Optional.empty()\n        }");
        return empty;
    }

    public final boolean isEnabled() {
        return this.featureEnabled;
    }

    public final Visibility getVisibility() {
        if (!isEnabled()) {
            return Visibility.UNAVAILABLE;
        }
        if (this.lockPatternUtils.getStrongAuthForUser(this.userTracker.getUserHandle().getIdentifier()) == 1) {
            return Visibility.AVAILABLE_AFTER_UNLOCK;
        }
        if (this.canShowWhileLockedSetting || this.keyguardStateController.isUnlocked()) {
            return Visibility.AVAILABLE;
        }
        return Visibility.AVAILABLE_AFTER_UNLOCK;
    }

    public final void updateShowWhileLocked() {
        boolean z = false;
        if (this.secureSettings.getInt("lockscreen_show_controls", 0) != 0) {
            z = true;
        }
        this.canShowWhileLockedSetting = z;
    }
}
