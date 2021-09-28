package com.android.wm.shell.bubbles;

import android.content.pm.UserInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Looper;
import android.service.notification.NotificationListenerService;
import android.util.ArraySet;
import android.util.Pair;
import android.util.SparseArray;
import android.view.View;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
/* loaded from: classes2.dex */
public interface Bubbles {

    /* loaded from: classes2.dex */
    public interface BubbleExpandListener {
        void onBubbleExpandChanged(boolean z, String str);
    }

    /* loaded from: classes2.dex */
    public interface PendingIntentCanceledListener {
        void onPendingIntentCanceled(Bubble bubble);
    }

    /* loaded from: classes2.dex */
    public interface SuppressionChangedListener {
        void onBubbleNotificationSuppressionChange(Bubble bubble);
    }

    /* loaded from: classes2.dex */
    public interface SysuiProxy {
        void getPendingOrActiveEntry(String str, Consumer<BubbleEntry> consumer);

        void getShouldRestoredEntries(ArraySet<String> arraySet, Consumer<List<BubbleEntry>> consumer);

        void isNotificationShadeExpand(Consumer<Boolean> consumer);

        void notifyInvalidateNotifications(String str);

        void notifyMaybeCancelSummary(String str);

        void notifyRemoveNotification(String str, int i);

        void onStackExpandChanged(boolean z);

        void onUnbubbleConversation(String str);

        void removeNotificationEntry(String str);

        void requestNotificationShadeTopUi(boolean z, String str);

        void setNotificationInterruption(String str);

        void updateNotificationBubbleButton(String str);

        void updateNotificationSuppression(String str);
    }

    void collapseStack();

    void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr);

    void expandStackAndSelectBubble(Bubble bubble);

    void expandStackAndSelectBubble(BubbleEntry bubbleEntry);

    Bubble getBubbleWithShortcutId(String str);

    boolean handleDismissalInterception(BubbleEntry bubbleEntry, List<BubbleEntry> list, IntConsumer intConsumer, Executor executor);

    boolean isBubbleExpanded(String str);

    boolean isBubbleNotificationSuppressedFromShade(String str, String str2);

    boolean isStackExpanded();

    void onConfigChanged(Configuration configuration);

    void onCurrentProfilesChanged(SparseArray<UserInfo> sparseArray);

    void onEntryAdded(BubbleEntry bubbleEntry);

    void onEntryRemoved(BubbleEntry bubbleEntry);

    void onEntryUpdated(BubbleEntry bubbleEntry, boolean z);

    void onRankingUpdated(NotificationListenerService.RankingMap rankingMap, HashMap<String, Pair<BubbleEntry, Boolean>> hashMap);

    void onStatusBarStateChanged(boolean z);

    void onStatusBarVisibilityChanged(boolean z);

    void onTaskbarChanged(Bundle bundle);

    void onUserChanged(int i);

    void onZenStateChanged();

    void removeSuppressedSummaryIfNecessary(String str, Consumer<String> consumer, Executor executor);

    void setBubbleScrim(View view, BiConsumer<Executor, Looper> biConsumer);

    void setExpandListener(BubbleExpandListener bubbleExpandListener);

    void setSysuiProxy(SysuiProxy sysuiProxy);

    void updateForThemeChanges();
}
