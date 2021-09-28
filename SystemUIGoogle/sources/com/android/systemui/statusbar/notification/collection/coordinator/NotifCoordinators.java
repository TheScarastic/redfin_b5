package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.Dumpable;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.statusbar.FeatureFlags;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifSectioner;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
/* loaded from: classes.dex */
public class NotifCoordinators implements Dumpable {
    private final List<Coordinator> mCoordinators;
    private final List<NotifSectioner> mOrderedSections;

    public NotifCoordinators(DumpManager dumpManager, FeatureFlags featureFlags, HideNotifsForOtherUsersCoordinator hideNotifsForOtherUsersCoordinator, KeyguardCoordinator keyguardCoordinator, RankingCoordinator rankingCoordinator, AppOpsCoordinator appOpsCoordinator, DeviceProvisionedCoordinator deviceProvisionedCoordinator, BubbleCoordinator bubbleCoordinator, HeadsUpCoordinator headsUpCoordinator, ConversationCoordinator conversationCoordinator, PreparationCoordinator preparationCoordinator, MediaCoordinator mediaCoordinator, SmartspaceDedupingCoordinator smartspaceDedupingCoordinator, VisualStabilityCoordinator visualStabilityCoordinator) {
        ArrayList arrayList = new ArrayList();
        this.mCoordinators = arrayList;
        ArrayList arrayList2 = new ArrayList();
        this.mOrderedSections = arrayList2;
        dumpManager.registerDumpable("NotifCoordinators", this);
        arrayList.add(new HideLocallyDismissedNotifsCoordinator());
        arrayList.add(hideNotifsForOtherUsersCoordinator);
        arrayList.add(keyguardCoordinator);
        arrayList.add(rankingCoordinator);
        arrayList.add(appOpsCoordinator);
        arrayList.add(deviceProvisionedCoordinator);
        arrayList.add(bubbleCoordinator);
        arrayList.add(conversationCoordinator);
        arrayList.add(mediaCoordinator);
        arrayList.add(visualStabilityCoordinator);
        if (featureFlags.isSmartspaceDedupingEnabled()) {
            arrayList.add(smartspaceDedupingCoordinator);
        }
        if (featureFlags.isNewNotifPipelineRenderingEnabled()) {
            arrayList.add(headsUpCoordinator);
            arrayList.add(preparationCoordinator);
        }
        if (featureFlags.isNewNotifPipelineRenderingEnabled()) {
            arrayList2.add(headsUpCoordinator.getSectioner());
        }
        arrayList2.add(appOpsCoordinator.getSectioner());
        arrayList2.add(conversationCoordinator.getSectioner());
        arrayList2.add(rankingCoordinator.getAlertingSectioner());
        arrayList2.add(rankingCoordinator.getSilentSectioner());
    }

    public void attach(NotifPipeline notifPipeline) {
        for (Coordinator coordinator : this.mCoordinators) {
            coordinator.attach(notifPipeline);
        }
        notifPipeline.setSections(this.mOrderedSections);
    }

    @Override // com.android.systemui.Dumpable
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println();
        printWriter.println("NotifCoordinators:");
        Iterator<Coordinator> it = this.mCoordinators.iterator();
        while (it.hasNext()) {
            printWriter.println("\t" + it.next().getClass());
        }
        Iterator<NotifSectioner> it2 = this.mOrderedSections.iterator();
        while (it2.hasNext()) {
            printWriter.println("\t" + it2.next().getName());
        }
    }
}
