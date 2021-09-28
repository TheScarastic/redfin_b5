package com.android.systemui.statusbar.notification.people;

import android.service.notification.StatusBarNotification;
import com.android.systemui.plugins.NotificationPersonExtractorPlugin;
import com.android.systemui.statusbar.policy.ExtensionController;
import java.util.function.Consumer;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: PeopleHubNotificationListener.kt */
/* loaded from: classes.dex */
public final class NotificationPersonExtractorPluginBoundary implements NotificationPersonExtractor {
    private NotificationPersonExtractorPlugin plugin;

    public NotificationPersonExtractorPluginBoundary(ExtensionController extensionController) {
        Intrinsics.checkNotNullParameter(extensionController, "extensionController");
        this.plugin = (NotificationPersonExtractorPlugin) extensionController.newExtension(NotificationPersonExtractorPlugin.class).withPlugin(NotificationPersonExtractorPlugin.class).withCallback(new Consumer<NotificationPersonExtractorPlugin>(this) { // from class: com.android.systemui.statusbar.notification.people.NotificationPersonExtractorPluginBoundary.1
            final /* synthetic */ NotificationPersonExtractorPluginBoundary this$0;

            {
                this.this$0 = r1;
            }

            public final void accept(NotificationPersonExtractorPlugin notificationPersonExtractorPlugin) {
                this.this$0.plugin = notificationPersonExtractorPlugin;
            }
        }).build().get();
    }

    @Override // com.android.systemui.statusbar.notification.people.NotificationPersonExtractor
    public boolean isPersonNotification(StatusBarNotification statusBarNotification) {
        Intrinsics.checkNotNullParameter(statusBarNotification, "sbn");
        NotificationPersonExtractorPlugin notificationPersonExtractorPlugin = this.plugin;
        if (notificationPersonExtractorPlugin == null) {
            return false;
        }
        return notificationPersonExtractorPlugin.isPersonNotification(statusBarNotification);
    }
}
