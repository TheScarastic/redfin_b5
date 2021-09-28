package com.android.systemui.statusbar.notification.stack;

import android.view.LayoutInflater;
import com.android.systemui.statusbar.policy.ConfigurationController;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: NotificationSectionsManager.kt */
/* loaded from: classes.dex */
public final class NotificationSectionsManager$configurationListener$1 implements ConfigurationController.ConfigurationListener {
    final /* synthetic */ NotificationSectionsManager this$0;

    /* access modifiers changed from: package-private */
    public NotificationSectionsManager$configurationListener$1(NotificationSectionsManager notificationSectionsManager) {
        this.this$0 = notificationSectionsManager;
    }

    @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
    public void onLocaleListChanged() {
        NotificationSectionsManager notificationSectionsManager = this.this$0;
        NotificationStackScrollLayout notificationStackScrollLayout = notificationSectionsManager.parent;
        if (notificationStackScrollLayout != null) {
            LayoutInflater from = LayoutInflater.from(notificationStackScrollLayout.getContext());
            Intrinsics.checkNotNullExpressionValue(from, "from(parent.context)");
            notificationSectionsManager.reinflateViews(from);
            return;
        }
        Intrinsics.throwUninitializedPropertyAccessException("parent");
        throw null;
    }
}
