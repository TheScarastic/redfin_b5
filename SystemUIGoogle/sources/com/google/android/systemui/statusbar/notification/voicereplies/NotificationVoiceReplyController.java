package com.google.android.systemui.statusbar.notification.voicereplies;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.RemoteInput;
import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.statusbar.LockscreenShadeTransitionController;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.NotificationRemoteInputManager;
import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.systemui.statusbar.SysuiStatusBarStateController;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.people.Subscription;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.NotificationContentView;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager;
import com.android.systemui.statusbar.policy.HeadsUpManager;
import com.android.systemui.util.ConvenienceExtensionsKt;
import com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController;
import com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyManager;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import kotlin.Lazy;
import kotlin.LazyKt__LazyJVMKt;
import kotlin.LazyKt__LazyKt;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.collections.ArraysKt___ArraysKt;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.SequencesKt;
import kotlin.sequences.SequencesKt___SequencesKt;
import kotlinx.coroutines.BuildersKt__Builders_commonKt;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.CoroutineScopeKt;
import kotlinx.coroutines.channels.Channel;
import kotlinx.coroutines.channels.ChannelKt;
import kotlinx.coroutines.flow.Flow;
import kotlinx.coroutines.flow.MutableSharedFlow;
import kotlinx.coroutines.flow.MutableStateFlow;
import kotlinx.coroutines.flow.SharedFlowKt;
import kotlinx.coroutines.flow.StateFlowKt;
/* compiled from: NotificationVoiceReplyManager.kt */
/* loaded from: classes2.dex */
public final class NotificationVoiceReplyController implements NotificationVoiceReplyManager.Initializer {
    private final Context context;
    private final int ctaContainerId;
    private final int ctaIconId;
    private final int ctaLayout;
    private final int ctaTextId;
    private final HeadsUpManager headsUpManager;
    private final NotificationLockscreenUserManager lockscreenUserManager;
    private final NotificationVoiceReplyLogger logger;
    private final NotificationShadeWindowController notifShadeWindowController;
    private final NotificationEntryManager notificationEntryManager;
    private final NotificationRemoteInputManager notificationRemoteInputManager;
    private final LockscreenShadeTransitionController shadeTransitionController;
    private final StatusBar statusBar;
    private final StatusBarKeyguardViewManager statusBarKeyguardViewManager;
    private final SysuiStatusBarStateController statusBarStateController;

    public NotificationVoiceReplyController(NotificationEntryManager notificationEntryManager, NotificationLockscreenUserManager notificationLockscreenUserManager, NotificationRemoteInputManager notificationRemoteInputManager, int i, int i2, int i3, int i4, LockscreenShadeTransitionController lockscreenShadeTransitionController, NotificationShadeWindowController notificationShadeWindowController, StatusBarKeyguardViewManager statusBarKeyguardViewManager, StatusBar statusBar, SysuiStatusBarStateController sysuiStatusBarStateController, HeadsUpManager headsUpManager, Context context, NotificationVoiceReplyLogger notificationVoiceReplyLogger) {
        Intrinsics.checkNotNullParameter(notificationEntryManager, "notificationEntryManager");
        Intrinsics.checkNotNullParameter(notificationLockscreenUserManager, "lockscreenUserManager");
        Intrinsics.checkNotNullParameter(notificationRemoteInputManager, "notificationRemoteInputManager");
        Intrinsics.checkNotNullParameter(lockscreenShadeTransitionController, "shadeTransitionController");
        Intrinsics.checkNotNullParameter(notificationShadeWindowController, "notifShadeWindowController");
        Intrinsics.checkNotNullParameter(statusBarKeyguardViewManager, "statusBarKeyguardViewManager");
        Intrinsics.checkNotNullParameter(statusBar, "statusBar");
        Intrinsics.checkNotNullParameter(sysuiStatusBarStateController, "statusBarStateController");
        Intrinsics.checkNotNullParameter(headsUpManager, "headsUpManager");
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(notificationVoiceReplyLogger, "logger");
        this.notificationEntryManager = notificationEntryManager;
        this.lockscreenUserManager = notificationLockscreenUserManager;
        this.notificationRemoteInputManager = notificationRemoteInputManager;
        this.ctaLayout = i;
        this.ctaContainerId = i2;
        this.ctaTextId = i3;
        this.ctaIconId = i4;
        this.shadeTransitionController = lockscreenShadeTransitionController;
        this.notifShadeWindowController = notificationShadeWindowController;
        this.statusBarKeyguardViewManager = statusBarKeyguardViewManager;
        this.statusBar = statusBar;
        this.statusBarStateController = sysuiStatusBarStateController;
        this.headsUpManager = headsUpManager;
        this.context = context;
        this.logger = notificationVoiceReplyLogger;
    }

    @Override // com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyManager.Initializer
    public NotificationVoiceReplyManager connect(CoroutineScope coroutineScope) {
        Intrinsics.checkNotNullParameter(coroutineScope, "scope");
        Connection connection = new Connection(null, null, null, null, null, 31, null);
        return new NotificationVoiceReplyController$connect$1(BuildersKt__Builders_commonKt.launch$default(coroutineScope, null, null, new NotificationVoiceReplyController$connect$job$1(this, connection, null), 3, null), this, connection);
    }

    /* access modifiers changed from: private */
    public final Subscription registerHandler(Connection connection, NotificationVoiceReplyHandler notificationVoiceReplyHandler) {
        ((List) NotificationVoiceReplyManagerKt.getOrPut(connection.getActiveHandlersByUser(), Integer.valueOf(notificationVoiceReplyHandler.getUserId()), NotificationVoiceReplyController$registerHandler$1.INSTANCE)).add(notificationVoiceReplyHandler);
        return NotificationVoiceReplyManagerKt.Subscription(new Function0<Unit>(connection, notificationVoiceReplyHandler, this) { // from class: com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$registerHandler$2
            final /* synthetic */ NotificationVoiceReplyHandler $handler;
            final /* synthetic */ NotificationVoiceReplyController.Connection $this_registerHandler;
            final /* synthetic */ NotificationVoiceReplyController this$0;

            /* access modifiers changed from: package-private */
            {
                this.$this_registerHandler = r1;
                this.$handler = r2;
                this.this$0 = r3;
            }

            @Override // kotlin.jvm.functions.Function0
            public final void invoke() {
                List<NotificationVoiceReplyHandler> list = this.$this_registerHandler.getActiveHandlersByUser().get(Integer.valueOf(this.$handler.getUserId()));
                if (list != null) {
                    NotificationVoiceReplyHandler notificationVoiceReplyHandler2 = this.$handler;
                    NotificationVoiceReplyController.Connection connection2 = this.$this_registerHandler;
                    NotificationVoiceReplyController notificationVoiceReplyController = this.this$0;
                    list.remove(notificationVoiceReplyHandler2);
                    if (list.isEmpty()) {
                        connection2.getActiveHandlersByUser().remove(Integer.valueOf(notificationVoiceReplyHandler2.getUserId()));
                        VoiceReplyTarget activeCandidate = connection2.getActiveCandidate();
                        Integer valueOf = activeCandidate == null ? null : Integer.valueOf(activeCandidate.getUserId());
                        int userId = notificationVoiceReplyHandler2.getUserId();
                        if (valueOf != null && valueOf.intValue() == userId) {
                            LogBuffer logBuffer = notificationVoiceReplyController.logger.getLogBuffer();
                            LogLevel logLevel = LogLevel.DEBUG;
                            NotificationVoiceReplyLogger$logStatic$2 notificationVoiceReplyLogger$logStatic$2 = new NotificationVoiceReplyLogger$logStatic$2("No more registered handlers for current candidate");
                            if (!logBuffer.getFrozen()) {
                                logBuffer.push(logBuffer.obtain("NotifVoiceReply", logLevel, notificationVoiceReplyLogger$logStatic$2));
                            }
                            connection2.getStateFlow().setValue(notificationVoiceReplyController.queryInitialState(connection2));
                        }
                    }
                }
            }
        });
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Removed duplicated region for block: B:10:0x0023  */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x0041  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final /* synthetic */ java.lang.Object resetStateOnUserChange(com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController.Connection r6, kotlin.coroutines.Continuation r7) {
        /*
            r5 = this;
            boolean r0 = r7 instanceof com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$resetStateOnUserChange$1
            if (r0 == 0) goto L_0x0013
            r0 = r7
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$resetStateOnUserChange$1 r0 = (com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$resetStateOnUserChange$1) r0
            int r1 = r0.label
            r2 = -2147483648(0xffffffff80000000, float:-0.0)
            r3 = r1 & r2
            if (r3 == 0) goto L_0x0013
            int r1 = r1 - r2
            r0.label = r1
            goto L_0x0018
        L_0x0013:
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$resetStateOnUserChange$1 r0 = new com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$resetStateOnUserChange$1
            r0.<init>(r5, r7)
        L_0x0018:
            java.lang.Object r7 = r0.result
            java.lang.Object r1 = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()
            int r2 = r0.label
            r3 = 1
            if (r2 == 0) goto L_0x0041
            if (r2 == r3) goto L_0x002d
            java.lang.IllegalStateException r5 = new java.lang.IllegalStateException
            java.lang.String r6 = "call to 'resume' before 'invoke' with coroutine"
            r5.<init>(r6)
            throw r5
        L_0x002d:
            java.lang.Object r5 = r0.L$1
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$resetStateOnUserChange$listener$1 r5 = (com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$resetStateOnUserChange$listener$1) r5
            java.lang.Object r6 = r0.L$0
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController r6 = (com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController) r6
            kotlin.ResultKt.throwOnFailure(r7)     // Catch: all -> 0x003b
            r7 = r5
            r5 = r6
            goto L_0x006d
        L_0x003b:
            r7 = move-exception
            r4 = r7
            r7 = r5
            r5 = r6
            r6 = r4
            goto L_0x0074
        L_0x0041:
            kotlin.ResultKt.throwOnFailure(r7)
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$resetStateOnUserChange$listener$1 r7 = new com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$resetStateOnUserChange$listener$1
            r7.<init>(r6, r5)
            com.android.systemui.statusbar.NotificationLockscreenUserManager r6 = r5.lockscreenUserManager
            r6.addUserChangedListener(r7)
            r0.L$0 = r5     // Catch: all -> 0x0073
            r0.L$1 = r7     // Catch: all -> 0x0073
            r0.label = r3     // Catch: all -> 0x0073
            kotlinx.coroutines.CancellableContinuationImpl r6 = new kotlinx.coroutines.CancellableContinuationImpl     // Catch: all -> 0x0073
            kotlin.coroutines.Continuation r2 = kotlin.coroutines.intrinsics.IntrinsicsKt.intercepted(r0)     // Catch: all -> 0x0073
            r6.<init>(r2, r3)     // Catch: all -> 0x0073
            java.lang.Object r6 = r6.getResult()     // Catch: all -> 0x0073
            java.lang.Object r2 = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()     // Catch: all -> 0x0073
            if (r6 != r2) goto L_0x006a
            kotlin.coroutines.jvm.internal.DebugProbesKt.probeCoroutineSuspended(r0)     // Catch: all -> 0x0073
        L_0x006a:
            if (r6 != r1) goto L_0x006d
            return r1
        L_0x006d:
            kotlin.KotlinNothingValueException r6 = new kotlin.KotlinNothingValueException     // Catch: all -> 0x0073
            r6.<init>()     // Catch: all -> 0x0073
            throw r6     // Catch: all -> 0x0073
        L_0x0073:
            r6 = move-exception
        L_0x0074:
            com.android.systemui.statusbar.NotificationLockscreenUserManager r5 = r5.lockscreenUserManager
            r5.removeUserChangedListener(r7)
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController.resetStateOnUserChange(com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$Connection, kotlin.coroutines.Continuation):java.lang.Object");
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Removed duplicated region for block: B:10:0x0023  */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x0041  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final /* synthetic */ java.lang.Object refreshCandidateOnNotifChanges(com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController.Connection r6, kotlin.coroutines.Continuation r7) {
        /*
            r5 = this;
            boolean r0 = r7 instanceof com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$refreshCandidateOnNotifChanges$1
            if (r0 == 0) goto L_0x0013
            r0 = r7
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$refreshCandidateOnNotifChanges$1 r0 = (com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$refreshCandidateOnNotifChanges$1) r0
            int r1 = r0.label
            r2 = -2147483648(0xffffffff80000000, float:-0.0)
            r3 = r1 & r2
            if (r3 == 0) goto L_0x0013
            int r1 = r1 - r2
            r0.label = r1
            goto L_0x0018
        L_0x0013:
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$refreshCandidateOnNotifChanges$1 r0 = new com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$refreshCandidateOnNotifChanges$1
            r0.<init>(r5, r7)
        L_0x0018:
            java.lang.Object r7 = r0.result
            java.lang.Object r1 = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()
            int r2 = r0.label
            r3 = 1
            if (r2 == 0) goto L_0x0041
            if (r2 == r3) goto L_0x002d
            java.lang.IllegalStateException r5 = new java.lang.IllegalStateException
            java.lang.String r6 = "call to 'resume' before 'invoke' with coroutine"
            r5.<init>(r6)
            throw r5
        L_0x002d:
            java.lang.Object r5 = r0.L$1
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$refreshCandidateOnNotifChanges$listener$1 r5 = (com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$refreshCandidateOnNotifChanges$listener$1) r5
            java.lang.Object r6 = r0.L$0
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController r6 = (com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController) r6
            kotlin.ResultKt.throwOnFailure(r7)     // Catch: all -> 0x003b
            r7 = r5
            r5 = r6
            goto L_0x006d
        L_0x003b:
            r7 = move-exception
            r4 = r7
            r7 = r5
            r5 = r6
            r6 = r4
            goto L_0x0074
        L_0x0041:
            kotlin.ResultKt.throwOnFailure(r7)
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$refreshCandidateOnNotifChanges$listener$1 r7 = new com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$refreshCandidateOnNotifChanges$listener$1
            r7.<init>(r6, r5)
            com.android.systemui.statusbar.notification.NotificationEntryManager r6 = r5.notificationEntryManager
            r6.addNotificationEntryListener(r7)
            r0.L$0 = r5     // Catch: all -> 0x0073
            r0.L$1 = r7     // Catch: all -> 0x0073
            r0.label = r3     // Catch: all -> 0x0073
            kotlinx.coroutines.CancellableContinuationImpl r6 = new kotlinx.coroutines.CancellableContinuationImpl     // Catch: all -> 0x0073
            kotlin.coroutines.Continuation r2 = kotlin.coroutines.intrinsics.IntrinsicsKt.intercepted(r0)     // Catch: all -> 0x0073
            r6.<init>(r2, r3)     // Catch: all -> 0x0073
            java.lang.Object r6 = r6.getResult()     // Catch: all -> 0x0073
            java.lang.Object r2 = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()     // Catch: all -> 0x0073
            if (r6 != r2) goto L_0x006a
            kotlin.coroutines.jvm.internal.DebugProbesKt.probeCoroutineSuspended(r0)     // Catch: all -> 0x0073
        L_0x006a:
            if (r6 != r1) goto L_0x006d
            return r1
        L_0x006d:
            kotlin.KotlinNothingValueException r6 = new kotlin.KotlinNothingValueException     // Catch: all -> 0x0073
            r6.<init>()     // Catch: all -> 0x0073
            throw r6     // Catch: all -> 0x0073
        L_0x0073:
            r6 = move-exception
        L_0x0074:
            com.android.systemui.statusbar.notification.NotificationEntryManager r5 = r5.notificationEntryManager
            r5.removeNotificationEntryListener(r7)
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController.refreshCandidateOnNotifChanges(com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$Connection, kotlin.coroutines.Continuation):java.lang.Object");
    }

    /* access modifiers changed from: private */
    public static final void refreshCandidateOnNotifChanges$newCandidate(Connection connection, NotificationVoiceReplyController notificationVoiceReplyController, NotificationEntry notificationEntry, String str) {
        if (!connection.getEntryReinflations().tryEmit(TuplesKt.to(notificationEntry, str))) {
            NotificationVoiceReplyLogger notificationVoiceReplyLogger = notificationVoiceReplyController.logger;
            String key = notificationEntry.getKey();
            Intrinsics.checkNotNullExpressionValue(key, "entry.key");
            notificationVoiceReplyLogger.logReinflationDropped(key, str);
        }
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ Object notifyHandlersOfReplyAvailability(Connection connection, Continuation continuation) {
        Object collectLatest = NotificationVoiceReplyManagerKt.collectLatest(NotificationVoiceReplyManagerKt.distinctUntilChanged(new NotificationVoiceReplyController$notifyHandlersOfReplyAvailability$$inlined$map$1(connection.getStateFlow()), NotificationVoiceReplyController$notifyHandlersOfReplyAvailability$3.INSTANCE), new NotificationVoiceReplyController$notifyHandlersOfReplyAvailability$4(this, null), continuation);
        return collectLatest == IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED() ? collectLatest : Unit.INSTANCE;
    }

    /* access modifiers changed from: private */
    public final void addCallToAction(View view) {
        ViewGroup viewGroup;
        View findViewById = view.findViewById(16909460);
        ColorStateList colorStateList = null;
        if (findViewById != null && (findViewById instanceof ViewGroup)) {
            viewGroup = (ViewGroup) findViewById;
        } else {
            viewGroup = null;
        }
        if (viewGroup != null) {
            viewGroup.setVisibility(0);
            for (View view2 : ConvenienceExtensionsKt.getChildren(viewGroup)) {
                view2.setVisibility(8);
            }
            View inflate = LayoutInflater.from(this.context).inflate(this.ctaLayout, viewGroup, true);
            TextView textView = (TextView) view.findViewById(16909555);
            if (textView != null) {
                colorStateList = textView.getTextColors();
            }
            if (colorStateList != null) {
                int defaultColor = colorStateList.getDefaultColor();
                View requireViewById = inflate.requireViewById(this.ctaIconId);
                Intrinsics.checkNotNullExpressionValue(requireViewById, "cta.requireViewById(ctaIconId)");
                ((ImageView) requireViewById).setColorFilter(defaultColor);
                View requireViewById2 = inflate.requireViewById(this.ctaTextId);
                Intrinsics.checkNotNullExpressionValue(requireViewById2, "cta.requireViewById(ctaTextId)");
                ((TextView) requireViewById2).setTextColor(defaultColor);
            }
        }
    }

    /* access modifiers changed from: private */
    public final void removeCallToAction(View view) {
        View findViewById = view.findViewById(16909460);
        LinearLayout linearLayout = null;
        if (findViewById != null && (findViewById instanceof LinearLayout)) {
            linearLayout = (LinearLayout) findViewById;
        }
        if (linearLayout != null) {
            linearLayout.removeView((ViewGroup) linearLayout.findViewById(this.ctaContainerId));
            for (View view2 : ConvenienceExtensionsKt.getChildren(linearLayout)) {
                view2.setVisibility(0);
            }
            if (linearLayout.getChildCount() == 0) {
                linearLayout.setVisibility(8);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ Object logUiEventsForActivatedVoiceReplyInputs(Connection connection, Flow flow, Continuation continuation) {
        Object coroutineScope = CoroutineScopeKt.coroutineScope(new NotificationVoiceReplyController$logUiEventsForActivatedVoiceReplyInputs$2(flow, connection, this, null), continuation);
        return coroutineScope == IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED() ? coroutineScope : Unit.INSTANCE;
    }

    /* access modifiers changed from: private */
    public final VoiceReplyState queryInitialState(Connection connection) {
        HasCandidate hasCandidate;
        List<NotificationEntry> visibleNotifications = this.notificationEntryManager.getVisibleNotifications();
        Intrinsics.checkNotNullExpressionValue(visibleNotifications, "notificationEntryManager.visibleNotifications");
        VoiceReplyTarget voiceReplyTarget = (VoiceReplyTarget) SequencesKt.firstOrNull(SequencesKt___SequencesKt.mapNotNull(CollectionsKt___CollectionsKt.asSequence(visibleNotifications), new Function1<NotificationEntry, VoiceReplyTarget>(this, connection) { // from class: com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$queryInitialState$1
            final /* synthetic */ NotificationVoiceReplyController.Connection $this_queryInitialState;
            final /* synthetic */ NotificationVoiceReplyController this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
                this.$this_queryInitialState = r2;
            }

            public final VoiceReplyTarget invoke(NotificationEntry notificationEntry) {
                NotificationVoiceReplyController notificationVoiceReplyController = this.this$0;
                NotificationVoiceReplyController.Connection connection2 = this.$this_queryInitialState;
                Intrinsics.checkNotNullExpressionValue(notificationEntry, "entry");
                return NotificationVoiceReplyController.extractCandidate$default(notificationVoiceReplyController, connection2, notificationEntry, 0, null, 6, null);
            }
        }));
        if (voiceReplyTarget == null) {
            hasCandidate = null;
        } else {
            hasCandidate = new HasCandidate(voiceReplyTarget);
        }
        return hasCandidate == null ? NoCandidate.INSTANCE : hasCandidate;
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ Object stateMachine(Connection connection, Continuation continuation) {
        Object coroutineScope = CoroutineScopeKt.coroutineScope(new NotificationVoiceReplyController$stateMachine$2(connection, this, null), continuation);
        return coroutineScope == IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED() ? coroutineScope : Unit.INSTANCE;
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ Object startVoiceReply(Connection connection, NotificationVoiceReplyHandler notificationVoiceReplyHandler, int i, String str, Function0 function0, Function2 function2, Continuation continuation) {
        Object send = connection.getStartSessionRequests().send(new StartSessionRequest(notificationVoiceReplyHandler, i, str, function0, function2), continuation);
        return send == IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED() ? send : Unit.INSTANCE;
    }

    private final boolean isMessagingStyle(NotificationEntry notificationEntry) {
        return Intrinsics.areEqual(notificationEntry.getSbn().getNotification().getNotificationStyle(), Notification.MessagingStyle.class);
    }

    /* access modifiers changed from: private */
    public final VoiceReplyState extractNewerCandidate(Connection connection, VoiceReplyTarget voiceReplyTarget, NotificationEntry notificationEntry) {
        VoiceReplyTarget extractCandidate$default;
        HasCandidate hasCandidate = null;
        if (Intrinsics.areEqual(notificationEntry.getKey(), voiceReplyTarget.getNotifKey())) {
            Notification.Builder recoverBuilder = Notification.Builder.recoverBuilder(this.context, notificationEntry.getSbn().getNotification());
            VoiceReplyTarget extractCandidate = extractCandidate(connection, notificationEntry, Notification.areStyledNotificationsVisiblyDifferent(recoverBuilder, voiceReplyTarget.getBuilder()) ? notificationEntry.getSbn().getPostTime() : voiceReplyTarget.getPostTime(), LazyKt__LazyKt.lazyOf(recoverBuilder));
            if (extractCandidate != null) {
                hasCandidate = new HasCandidate(extractCandidate);
            }
            if (hasCandidate == null) {
                return queryInitialState(connection);
            }
            return hasCandidate;
        } else if (notificationEntry.getSbn().getPostTime() > voiceReplyTarget.getPostTime() && (extractCandidate$default = extractCandidate$default(this, connection, notificationEntry, 0, null, 6, null)) != null) {
            return new HasCandidate(extractCandidate$default);
        } else {
            return null;
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for r6v0, resolved type: com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController */
    /* JADX WARN: Multi-variable type inference failed */
    /* access modifiers changed from: package-private */
    public static /* synthetic */ VoiceReplyTarget extractCandidate$default(NotificationVoiceReplyController notificationVoiceReplyController, Connection connection, NotificationEntry notificationEntry, long j, Lazy lazy, int i, Object obj) {
        if ((i & 2) != 0) {
            j = notificationEntry.getSbn().getPostTime();
        }
        if ((i & 4) != 0) {
            lazy = LazyKt__LazyJVMKt.lazy(new Function0<Notification.Builder>(notificationVoiceReplyController, notificationEntry) { // from class: com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$extractCandidate$1
                final /* synthetic */ NotificationEntry $entry;
                final /* synthetic */ NotificationVoiceReplyController this$0;

                /* access modifiers changed from: package-private */
                {
                    this.this$0 = r1;
                    this.$entry = r2;
                }

                @Override // kotlin.jvm.functions.Function0
                public final Notification.Builder invoke() {
                    return Notification.Builder.recoverBuilder(this.this$0.context, this.$entry.getSbn().getNotification());
                }
            });
        }
        return notificationVoiceReplyController.extractCandidate(connection, notificationEntry, j, lazy);
    }

    private final VoiceReplyTarget extractCandidate(Connection connection, NotificationEntry notificationEntry, long j, Lazy<? extends Notification.Builder> lazy) {
        if (!isMessagingStyle(notificationEntry)) {
            return null;
        }
        int identifier = notificationEntry.getSbn().getUser().getIdentifier();
        List<NotificationVoiceReplyHandler> list = connection.getActiveHandlersByUser().get(Integer.valueOf(identifier));
        if (list == null) {
            NotificationVoiceReplyLogger notificationVoiceReplyLogger = this.logger;
            String key = notificationEntry.getKey();
            Intrinsics.checkNotNullExpressionValue(key, "entry.key");
            notificationVoiceReplyLogger.logRejectCandidate(key, Intrinsics.stringPlus("no handlers for user=", Integer.valueOf(identifier)));
            return null;
        }
        Notification.Action[] actionArr = notificationEntry.getSbn().getNotification().actions;
        if (actionArr != null) {
            return (VoiceReplyTarget) SequencesKt.firstOrNull(SequencesKt___SequencesKt.mapNotNull(ArraysKt___ArraysKt.asSequence(actionArr), new Function1<Notification.Action, VoiceReplyTarget>(this, notificationEntry, j, lazy, list) { // from class: com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController$extractCandidate$2
                final /* synthetic */ Lazy<Notification.Builder> $builderLazy;
                final /* synthetic */ NotificationEntry $entry;
                final /* synthetic */ List<NotificationVoiceReplyHandler> $handlers;
                final /* synthetic */ long $postTime;
                final /* synthetic */ NotificationVoiceReplyController this$0;

                /* JADX DEBUG: Multi-variable search result rejected for r5v0, resolved type: kotlin.Lazy<? extends android.app.Notification$Builder> */
                /* JADX WARN: Multi-variable type inference failed */
                /* access modifiers changed from: package-private */
                {
                    this.this$0 = r1;
                    this.$entry = r2;
                    this.$postTime = r3;
                    this.$builderLazy = r5;
                    this.$handlers = r6;
                }

                public final VoiceReplyTarget invoke(Notification.Action action) {
                    NotificationVoiceReplyController notificationVoiceReplyController = this.this$0;
                    NotificationEntry notificationEntry2 = this.$entry;
                    Intrinsics.checkNotNullExpressionValue(action, "action");
                    return notificationVoiceReplyController.tryCreateVoiceReplyTarget(notificationEntry2, action, this.$postTime, this.$builderLazy, this.$handlers);
                }
            }));
        }
        NotificationVoiceReplyLogger notificationVoiceReplyLogger2 = this.logger;
        String key2 = notificationEntry.getKey();
        Intrinsics.checkNotNullExpressionValue(key2, "entry.key");
        notificationVoiceReplyLogger2.logRejectCandidate(key2, "no actions");
        return null;
    }

    /* access modifiers changed from: private */
    public final VoiceReplyTarget tryCreateVoiceReplyTarget(NotificationEntry notificationEntry, Notification.Action action, long j, Lazy<? extends Notification.Builder> lazy, List<? extends NotificationVoiceReplyHandler> list) {
        Object obj;
        Button button;
        PendingIntent pendingIntent = action.actionIntent;
        if (pendingIntent == null) {
            NotificationVoiceReplyLogger notificationVoiceReplyLogger = this.logger;
            String key = notificationEntry.getKey();
            Intrinsics.checkNotNullExpressionValue(key, "entry.key");
            notificationVoiceReplyLogger.logRejectCandidate(key, "no action intent");
            return null;
        }
        RemoteInput[] remoteInputs = action.getRemoteInputs();
        if (remoteInputs == null) {
            return null;
        }
        Iterator it = ArraysKt___ArraysKt.asSequence(remoteInputs).iterator();
        while (true) {
            if (!it.hasNext()) {
                obj = null;
                break;
            }
            obj = it.next();
            if (((RemoteInput) obj).getAllowFreeFormInput()) {
                break;
            }
        }
        RemoteInput remoteInput = (RemoteInput) obj;
        if (remoteInput == null) {
            NotificationVoiceReplyLogger notificationVoiceReplyLogger2 = this.logger;
            String key2 = notificationEntry.getKey();
            Intrinsics.checkNotNullExpressionValue(key2, "entry.key");
            notificationVoiceReplyLogger2.logRejectCandidate(key2, "no freeform input");
            return null;
        }
        ExpandableNotificationRow row = notificationEntry.getRow();
        NotificationContentView showingLayout = row == null ? null : row.getShowingLayout();
        if (showingLayout == null) {
            return null;
        }
        View expandedChild = showingLayout.getExpandedChild();
        if (expandedChild == null) {
            button = null;
        } else {
            button = NotificationVoiceReplyManagerKt.getReplyButton(expandedChild, remoteInput);
        }
        if (button != null) {
            return new VoiceReplyTarget(notificationEntry, (Notification.Builder) lazy.getValue(), j, list, pendingIntent, remoteInputs, remoteInput, button, this.notificationRemoteInputManager, this.shadeTransitionController, this.statusBar, this.statusBarStateController, this.logger, this.notifShadeWindowController, this.statusBarKeyguardViewManager);
        }
        NotificationVoiceReplyLogger notificationVoiceReplyLogger3 = this.logger;
        String key3 = notificationEntry.getKey();
        Intrinsics.checkNotNullExpressionValue(key3, "entry.key");
        notificationVoiceReplyLogger3.logRejectCandidate(key3, "no reply button in expanded view");
        return null;
    }

    /* compiled from: NotificationVoiceReplyManager.kt */
    /* access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static final class Connection {
        private final Map<Integer, List<NotificationVoiceReplyHandler>> activeHandlersByUser;
        private final MutableSharedFlow<Pair<NotificationEntry, String>> entryReinflations;
        private final MutableSharedFlow<String> entryRemovals;
        private final Channel<StartSessionRequest> startSessionRequests;
        private final MutableStateFlow<VoiceReplyState> stateFlow;

        public Connection() {
            this(null, null, null, null, null, 31, null);
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof Connection)) {
                return false;
            }
            Connection connection = (Connection) obj;
            return Intrinsics.areEqual(this.entryReinflations, connection.entryReinflations) && Intrinsics.areEqual(this.entryRemovals, connection.entryRemovals) && Intrinsics.areEqual(this.startSessionRequests, connection.startSessionRequests) && Intrinsics.areEqual(this.activeHandlersByUser, connection.activeHandlersByUser) && Intrinsics.areEqual(this.stateFlow, connection.stateFlow);
        }

        public int hashCode() {
            return (((((((this.entryReinflations.hashCode() * 31) + this.entryRemovals.hashCode()) * 31) + this.startSessionRequests.hashCode()) * 31) + this.activeHandlersByUser.hashCode()) * 31) + this.stateFlow.hashCode();
        }

        public String toString() {
            return "Connection(entryReinflations=" + this.entryReinflations + ", entryRemovals=" + this.entryRemovals + ", startSessionRequests=" + this.startSessionRequests + ", activeHandlersByUser=" + this.activeHandlersByUser + ", stateFlow=" + this.stateFlow + ')';
        }

        public Connection(MutableSharedFlow<Pair<NotificationEntry, String>> mutableSharedFlow, MutableSharedFlow<String> mutableSharedFlow2, Channel<StartSessionRequest> channel, Map<Integer, List<NotificationVoiceReplyHandler>> map, MutableStateFlow<VoiceReplyState> mutableStateFlow) {
            Intrinsics.checkNotNullParameter(mutableSharedFlow, "entryReinflations");
            Intrinsics.checkNotNullParameter(mutableSharedFlow2, "entryRemovals");
            Intrinsics.checkNotNullParameter(channel, "startSessionRequests");
            Intrinsics.checkNotNullParameter(map, "activeHandlersByUser");
            Intrinsics.checkNotNullParameter(mutableStateFlow, "stateFlow");
            this.entryReinflations = mutableSharedFlow;
            this.entryRemovals = mutableSharedFlow2;
            this.startSessionRequests = channel;
            this.activeHandlersByUser = map;
            this.stateFlow = mutableStateFlow;
        }

        public final MutableSharedFlow<Pair<NotificationEntry, String>> getEntryReinflations() {
            return this.entryReinflations;
        }

        public /* synthetic */ Connection(MutableSharedFlow mutableSharedFlow, MutableSharedFlow mutableSharedFlow2, Channel channel, Map map, MutableStateFlow mutableStateFlow, int i, DefaultConstructorMarker defaultConstructorMarker) {
            this((i & 1) != 0 ? SharedFlowKt.MutableSharedFlow$default(0, Integer.MAX_VALUE, null, 5, null) : mutableSharedFlow, (i & 2) != 0 ? SharedFlowKt.MutableSharedFlow$default(0, Integer.MAX_VALUE, null, 5, null) : mutableSharedFlow2, (i & 4) != 0 ? ChannelKt.Channel$default(0, 1, null) : channel, (i & 8) != 0 ? new LinkedHashMap() : map, (i & 16) != 0 ? StateFlowKt.MutableStateFlow(NoCandidate.INSTANCE) : mutableStateFlow);
        }

        public final MutableSharedFlow<String> getEntryRemovals() {
            return this.entryRemovals;
        }

        public final Channel<StartSessionRequest> getStartSessionRequests() {
            return this.startSessionRequests;
        }

        public final Map<Integer, List<NotificationVoiceReplyHandler>> getActiveHandlersByUser() {
            return this.activeHandlersByUser;
        }

        public final MutableStateFlow<VoiceReplyState> getStateFlow() {
            return this.stateFlow;
        }

        public final VoiceReplyTarget getActiveCandidate() {
            VoiceReplyState value = this.stateFlow.getValue();
            HasCandidate hasCandidate = value instanceof HasCandidate ? (HasCandidate) value : null;
            if (hasCandidate == null) {
                return null;
            }
            return hasCandidate.getCandidate();
        }
    }
}
