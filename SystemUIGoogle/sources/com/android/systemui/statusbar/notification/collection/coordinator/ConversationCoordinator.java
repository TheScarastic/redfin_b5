package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.statusbar.notification.collection.ListEntry;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifSectioner;
import com.android.systemui.statusbar.notification.collection.render.NodeController;
import com.android.systemui.statusbar.notification.people.PeopleNotificationIdentifier;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ConversationCoordinator.kt */
/* loaded from: classes.dex */
public final class ConversationCoordinator implements Coordinator {
    public static final Companion Companion = new Companion(null);
    private final ConversationCoordinator$notificationPromoter$1 notificationPromoter = new ConversationCoordinator$notificationPromoter$1();
    private final PeopleNotificationIdentifier peopleNotificationIdentifier;
    private final NotifSectioner sectioner;

    public ConversationCoordinator(PeopleNotificationIdentifier peopleNotificationIdentifier, NodeController nodeController) {
        Intrinsics.checkNotNullParameter(peopleNotificationIdentifier, "peopleNotificationIdentifier");
        Intrinsics.checkNotNullParameter(nodeController, "peopleHeaderController");
        this.peopleNotificationIdentifier = peopleNotificationIdentifier;
        this.sectioner = new NotifSectioner(this, nodeController) { // from class: com.android.systemui.statusbar.notification.collection.coordinator.ConversationCoordinator$sectioner$1
            final /* synthetic */ NodeController $peopleHeaderController;
            final /* synthetic */ ConversationCoordinator this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
                this.$peopleHeaderController = r2;
            }

            @Override // com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifSectioner
            public boolean isInSection(ListEntry listEntry) {
                Intrinsics.checkNotNullParameter(listEntry, "entry");
                ConversationCoordinator conversationCoordinator = this.this$0;
                NotificationEntry representativeEntry = listEntry.getRepresentativeEntry();
                Intrinsics.checkNotNull(representativeEntry);
                return conversationCoordinator.isConversation(representativeEntry);
            }

            @Override // com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifSectioner
            public NodeController getHeaderNodeController() {
                return this.$peopleHeaderController;
            }
        };
    }

    public final NotifSectioner getSectioner() {
        return this.sectioner;
    }

    @Override // com.android.systemui.statusbar.notification.collection.coordinator.Coordinator
    public void attach(NotifPipeline notifPipeline) {
        Intrinsics.checkNotNullParameter(notifPipeline, "pipeline");
        notifPipeline.addPromoter(this.notificationPromoter);
    }

    /* access modifiers changed from: private */
    public final boolean isConversation(NotificationEntry notificationEntry) {
        return this.peopleNotificationIdentifier.getPeopleNotificationType(notificationEntry) != 0;
    }

    /* compiled from: ConversationCoordinator.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
