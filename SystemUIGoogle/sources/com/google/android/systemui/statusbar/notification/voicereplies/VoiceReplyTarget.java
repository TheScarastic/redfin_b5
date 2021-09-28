package com.google.android.systemui.statusbar.notification.voicereplies;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.RemoteInput;
import android.widget.Button;
import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.statusbar.LockscreenShadeTransitionController;
import com.android.systemui.statusbar.NotificationRemoteInputManager;
import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.systemui.statusbar.SysuiStatusBarStateController;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager;
import com.android.systemui.statusbar.policy.RemoteInputView;
import java.util.List;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsJvmKt;
import kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsKt;
import kotlin.coroutines.jvm.internal.Boxing;
import kotlin.coroutines.jvm.internal.DebugProbesKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.CancellableContinuationImpl;
/* compiled from: NotificationVoiceReplyManager.kt */
/* access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class VoiceReplyTarget {
    private final PendingIntent actionIntent;
    private final Notification.Builder builder;
    private final NotificationEntry entry;
    private final Button expandedViewReplyButton;
    private final RemoteInput freeformInput;
    private final List<NotificationVoiceReplyHandler> handlers;
    private final NotificationVoiceReplyLogger logger;
    private final String notifKey;
    private final NotificationShadeWindowController notifShadeWindowController;
    private final NotificationRemoteInputManager notificationRemoteInputManager;
    private final long postTime;
    private final RemoteInput[] remoteInputs;
    private final LockscreenShadeTransitionController shadeTransitionController;
    private final StatusBar statusBar;
    private final StatusBarKeyguardViewManager statusBarKeyguardViewManager;
    private final SysuiStatusBarStateController statusBarStateController;
    private final int userId;

    final /* synthetic */ Object awaitKeyguardReset(Continuation continuation) {
        CancellableContinuationImpl cancellableContinuationImpl = new CancellableContinuationImpl(IntrinsicsKt__IntrinsicsJvmKt.intercepted(continuation), 1);
        VoiceReplyTarget$awaitKeyguardReset$2$callback$1 voiceReplyTarget$awaitKeyguardReset$2$callback$1 = new VoiceReplyTarget$awaitKeyguardReset$2$callback$1(NotificationVoiceReplyManagerKt.AtomicLatch(), this, cancellableContinuationImpl);
        this.statusBarKeyguardViewManager.getBouncer().addKeyguardResetCallback(voiceReplyTarget$awaitKeyguardReset$2$callback$1);
        cancellableContinuationImpl.invokeOnCancellation(new Function1<Throwable, Unit>(this, voiceReplyTarget$awaitKeyguardReset$2$callback$1) { // from class: com.google.android.systemui.statusbar.notification.voicereplies.VoiceReplyTarget$awaitKeyguardReset$2$1
            final /* synthetic */ VoiceReplyTarget$awaitKeyguardReset$2$callback$1 $callback;
            final /* synthetic */ VoiceReplyTarget this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
                this.$callback = r2;
            }

            /* Return type fixed from 'java.lang.Object' to match base method */
            /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object] */
            @Override // kotlin.jvm.functions.Function1
            public /* bridge */ /* synthetic */ Unit invoke(Throwable th) {
                invoke(th);
                return Unit.INSTANCE;
            }

            public final void invoke(Throwable th) {
                this.this$0.statusBarKeyguardViewManager.getBouncer().removeKeyguardResetCallback(this.$callback);
            }
        });
        Object result = cancellableContinuationImpl.getResult();
        if (result == IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
            DebugProbesKt.probeCoroutineSuspended(continuation);
        }
        return result == IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED() ? result : Unit.INSTANCE;
    }

    /* JADX DEBUG: Multi-variable search result rejected for r21v0, resolved type: java.util.List<? extends com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyHandler> */
    /* JADX WARN: Multi-variable type inference failed */
    public VoiceReplyTarget(NotificationEntry notificationEntry, Notification.Builder builder, long j, List<? extends NotificationVoiceReplyHandler> list, PendingIntent pendingIntent, RemoteInput[] remoteInputArr, RemoteInput remoteInput, Button button, NotificationRemoteInputManager notificationRemoteInputManager, LockscreenShadeTransitionController lockscreenShadeTransitionController, StatusBar statusBar, SysuiStatusBarStateController sysuiStatusBarStateController, NotificationVoiceReplyLogger notificationVoiceReplyLogger, NotificationShadeWindowController notificationShadeWindowController, StatusBarKeyguardViewManager statusBarKeyguardViewManager) {
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        Intrinsics.checkNotNullParameter(builder, "builder");
        Intrinsics.checkNotNullParameter(list, "handlers");
        Intrinsics.checkNotNullParameter(pendingIntent, "actionIntent");
        Intrinsics.checkNotNullParameter(remoteInputArr, "remoteInputs");
        Intrinsics.checkNotNullParameter(remoteInput, "freeformInput");
        Intrinsics.checkNotNullParameter(button, "expandedViewReplyButton");
        Intrinsics.checkNotNullParameter(notificationRemoteInputManager, "notificationRemoteInputManager");
        Intrinsics.checkNotNullParameter(lockscreenShadeTransitionController, "shadeTransitionController");
        Intrinsics.checkNotNullParameter(statusBar, "statusBar");
        Intrinsics.checkNotNullParameter(sysuiStatusBarStateController, "statusBarStateController");
        Intrinsics.checkNotNullParameter(notificationVoiceReplyLogger, "logger");
        Intrinsics.checkNotNullParameter(notificationShadeWindowController, "notifShadeWindowController");
        Intrinsics.checkNotNullParameter(statusBarKeyguardViewManager, "statusBarKeyguardViewManager");
        this.entry = notificationEntry;
        this.builder = builder;
        this.postTime = j;
        this.handlers = list;
        this.actionIntent = pendingIntent;
        this.remoteInputs = remoteInputArr;
        this.freeformInput = remoteInput;
        this.expandedViewReplyButton = button;
        this.notificationRemoteInputManager = notificationRemoteInputManager;
        this.shadeTransitionController = lockscreenShadeTransitionController;
        this.statusBar = statusBar;
        this.statusBarStateController = sysuiStatusBarStateController;
        this.logger = notificationVoiceReplyLogger;
        this.notifShadeWindowController = notificationShadeWindowController;
        this.statusBarKeyguardViewManager = statusBarKeyguardViewManager;
        String key = notificationEntry.getKey();
        Intrinsics.checkNotNullExpressionValue(key, "entry.key");
        this.notifKey = key;
        this.userId = notificationEntry.getSbn().getUser().getIdentifier();
    }

    public final NotificationEntry getEntry() {
        return this.entry;
    }

    public final Notification.Builder getBuilder() {
        return this.builder;
    }

    public final long getPostTime() {
        return this.postTime;
    }

    public final List<NotificationVoiceReplyHandler> getHandlers() {
        return this.handlers;
    }

    public final String getNotifKey() {
        return this.notifKey;
    }

    public final int getUserId() {
        return this.userId;
    }

    /* JADX DEBUG: Failed to insert an additional move for type inference into block B:107:0x0260 */
    /* JADX DEBUG: Failed to insert an additional move for type inference into block B:113:0x01f6 */
    /* JADX DEBUG: Failed to insert an additional move for type inference into block B:108:0x0263 */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v2, types: [kotlinx.coroutines.flow.FlowCollector] */
    /* JADX WARN: Type inference failed for: r1v6, types: [java.lang.Object] */
    /* JADX WARN: Type inference failed for: r1v8 */
    /* JADX WARN: Type inference failed for: r1v10, types: [java.lang.Object] */
    /* JADX WARN: Type inference failed for: r11v9 */
    /* JADX WARN: Type inference failed for: r1v16 */
    /* JADX WARN: Type inference failed for: r11v10 */
    /* JADX WARN: Type inference failed for: r1v30 */
    /* JADX WARN: Type inference failed for: r1v31 */
    /* JADX WARNING: Removed duplicated region for block: B:100:0x023a A[Catch: all -> 0x0260, TryCatch #0 {all -> 0x0260, blocks: (B:92:0x01f6, B:94:0x020b, B:95:0x0212, B:98:0x0225, B:100:0x023a, B:101:0x0241), top: B:113:0x01f6 }] */
    /* JADX WARNING: Removed duplicated region for block: B:103:0x0253 A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:104:0x0254  */
    /* JADX WARNING: Removed duplicated region for block: B:10:0x0035  */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x00c0  */
    /* JADX WARNING: Removed duplicated region for block: B:48:0x00d9  */
    /* JADX WARNING: Removed duplicated region for block: B:49:0x00db  */
    /* JADX WARNING: Removed duplicated region for block: B:52:0x00e9  */
    /* JADX WARNING: Removed duplicated region for block: B:53:0x00eb A[Catch: all -> 0x0265, TryCatch #2 {all -> 0x0265, blocks: (B:40:0x00c3, B:42:0x00cb, B:50:0x00dc, B:53:0x00eb, B:55:0x00f1, B:59:0x0101, B:61:0x0109, B:63:0x011e, B:64:0x0125), top: B:116:0x00c3 }] */
    /* JADX WARNING: Removed duplicated region for block: B:55:0x00f1 A[Catch: all -> 0x0265, TRY_LEAVE, TryCatch #2 {all -> 0x0265, blocks: (B:40:0x00c3, B:42:0x00cb, B:50:0x00dc, B:53:0x00eb, B:55:0x00f1, B:59:0x0101, B:61:0x0109, B:63:0x011e, B:64:0x0125), top: B:116:0x00c3 }] */
    /* JADX WARNING: Removed duplicated region for block: B:59:0x0101 A[Catch: all -> 0x0265, TRY_ENTER, TryCatch #2 {all -> 0x0265, blocks: (B:40:0x00c3, B:42:0x00cb, B:50:0x00dc, B:53:0x00eb, B:55:0x00f1, B:59:0x0101, B:61:0x0109, B:63:0x011e, B:64:0x0125), top: B:116:0x00c3 }] */
    /* JADX WARNING: Removed duplicated region for block: B:70:0x0151 A[Catch: all -> 0x0159, TryCatch #3 {all -> 0x0159, blocks: (B:68:0x013c, B:70:0x0151, B:74:0x0160), top: B:117:0x013c }] */
    /* JADX WARNING: Removed duplicated region for block: B:76:0x0172 A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:77:0x0173  */
    /* JADX WARNING: Removed duplicated region for block: B:80:0x0180 A[Catch: all -> 0x0263, TRY_LEAVE, TryCatch #1 {all -> 0x0263, blocks: (B:78:0x0178, B:80:0x0180, B:82:0x018e, B:84:0x01a3, B:85:0x01aa, B:87:0x01cd, B:88:0x01d5), top: B:115:0x0178 }] */
    /* JADX WARNING: Removed duplicated region for block: B:82:0x018e A[Catch: all -> 0x0263, TRY_ENTER, TryCatch #1 {all -> 0x0263, blocks: (B:78:0x0178, B:80:0x0180, B:82:0x018e, B:84:0x01a3, B:85:0x01aa, B:87:0x01cd, B:88:0x01d5), top: B:115:0x0178 }] */
    /* JADX WARNING: Removed duplicated region for block: B:94:0x020b A[Catch: all -> 0x0260, TryCatch #0 {all -> 0x0260, blocks: (B:92:0x01f6, B:94:0x020b, B:95:0x0212, B:98:0x0225, B:100:0x023a, B:101:0x0241), top: B:113:0x01f6 }] */
    /* JADX WARNING: Removed duplicated region for block: B:97:0x0224 A[RETURN] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final java.lang.Object focus(com.google.android.systemui.statusbar.notification.voicereplies.AuthStateRef r26, java.lang.String r27, kotlinx.coroutines.flow.MutableSharedFlow<kotlin.Pair<java.lang.String, com.android.systemui.statusbar.policy.RemoteInputView>> r28, kotlin.coroutines.Continuation<? super kotlin.Unit> r29) {
        /*
        // Method dump skipped, instructions count: 624
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.systemui.statusbar.notification.voicereplies.VoiceReplyTarget.focus(com.google.android.systemui.statusbar.notification.voicereplies.AuthStateRef, java.lang.String, kotlinx.coroutines.flow.MutableSharedFlow, kotlin.coroutines.Continuation):java.lang.Object");
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Removed duplicated region for block: B:10:0x0024  */
    /* JADX WARNING: Removed duplicated region for block: B:16:0x003c  */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x004b A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x0054  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final /* synthetic */ java.lang.Object awaitKeyguardNotOccluded(kotlin.coroutines.Continuation r6) {
        /*
            r5 = this;
            boolean r0 = r6 instanceof com.google.android.systemui.statusbar.notification.voicereplies.VoiceReplyTarget$awaitKeyguardNotOccluded$1
            if (r0 == 0) goto L_0x0013
            r0 = r6
            com.google.android.systemui.statusbar.notification.voicereplies.VoiceReplyTarget$awaitKeyguardNotOccluded$1 r0 = (com.google.android.systemui.statusbar.notification.voicereplies.VoiceReplyTarget$awaitKeyguardNotOccluded$1) r0
            int r1 = r0.label
            r2 = -2147483648(0xffffffff80000000, float:-0.0)
            r3 = r1 & r2
            if (r3 == 0) goto L_0x0013
            int r1 = r1 - r2
            r0.label = r1
            goto L_0x0018
        L_0x0013:
            com.google.android.systemui.statusbar.notification.voicereplies.VoiceReplyTarget$awaitKeyguardNotOccluded$1 r0 = new com.google.android.systemui.statusbar.notification.voicereplies.VoiceReplyTarget$awaitKeyguardNotOccluded$1
            r0.<init>(r5, r6)
        L_0x0018:
            java.lang.Object r6 = r0.result
            java.lang.Object r1 = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()
            int r2 = r0.label
            r3 = 2
            r4 = 1
            if (r2 == 0) goto L_0x003c
            if (r2 == r4) goto L_0x0034
            if (r2 != r3) goto L_0x002c
            kotlin.ResultKt.throwOnFailure(r6)
            goto L_0x006d
        L_0x002c:
            java.lang.IllegalStateException r5 = new java.lang.IllegalStateException
            java.lang.String r6 = "call to 'resume' before 'invoke' with coroutine"
            r5.<init>(r6)
            throw r5
        L_0x0034:
            java.lang.Object r5 = r0.L$0
            com.google.android.systemui.statusbar.notification.voicereplies.VoiceReplyTarget r5 = (com.google.android.systemui.statusbar.notification.voicereplies.VoiceReplyTarget) r5
            kotlin.ResultKt.throwOnFailure(r6)
            goto L_0x004c
        L_0x003c:
            kotlin.ResultKt.throwOnFailure(r6)
        L_0x003f:
            com.android.systemui.statusbar.NotificationShadeWindowController r6 = r5.notifShadeWindowController
            r0.L$0 = r5
            r0.label = r4
            java.lang.Object r6 = com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyManagerKt.awaitStateChange(r6, r0)
            if (r6 != r1) goto L_0x004c
            return r1
        L_0x004c:
            com.google.android.systemui.statusbar.notification.voicereplies.StatusBarWindowState r6 = (com.google.android.systemui.statusbar.notification.voicereplies.StatusBarWindowState) r6
            boolean r2 = r6.getBouncerShowing()
            if (r2 != 0) goto L_0x0070
            boolean r2 = r6.getKeyguardShowing()
            if (r2 != 0) goto L_0x005b
            goto L_0x0070
        L_0x005b:
            boolean r6 = r6.getKeyguardOccluded()
            if (r6 != 0) goto L_0x003f
            r6 = 0
            r0.L$0 = r6
            r0.label = r3
            java.lang.Object r5 = r5.awaitKeyguardReset(r0)
            if (r5 != r1) goto L_0x006d
            return r1
        L_0x006d:
            kotlin.Unit r5 = kotlin.Unit.INSTANCE
            return r5
        L_0x0070:
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyLogger r5 = r5.logger
            r5.logBadWindowState(r6)
            kotlin.Unit r5 = kotlin.Unit.INSTANCE
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.systemui.statusbar.notification.voicereplies.VoiceReplyTarget.awaitKeyguardNotOccluded(kotlin.coroutines.Continuation):java.lang.Object");
    }

    final /* synthetic */ Object expandShade(Continuation continuation) {
        int state = this.statusBarStateController.getState();
        boolean z = false;
        if (state != 0) {
            if (state == 1) {
                CancellableContinuationImpl cancellableContinuationImpl = new CancellableContinuationImpl(IntrinsicsKt__IntrinsicsJvmKt.intercepted(continuation), 1);
                LogBuffer logBuffer = this.logger.getLogBuffer();
                LogLevel logLevel = LogLevel.DEBUG;
                NotificationVoiceReplyLogger$logStatic$2 notificationVoiceReplyLogger$logStatic$2 = new NotificationVoiceReplyLogger$logStatic$2("On keyguard, waiting for SHADE_LOCKED state");
                if (!logBuffer.getFrozen()) {
                    logBuffer.push(logBuffer.obtain("NotifVoiceReply", logLevel, notificationVoiceReplyLogger$logStatic$2));
                }
                VoiceReplyTarget$expandShade$2$callback$1 voiceReplyTarget$expandShade$2$callback$1 = new VoiceReplyTarget$expandShade$2$callback$1(NotificationVoiceReplyManagerKt.AtomicLatch(), this, cancellableContinuationImpl);
                cancellableContinuationImpl.invokeOnCancellation(new Function1<Throwable, Unit>(this, voiceReplyTarget$expandShade$2$callback$1) { // from class: com.google.android.systemui.statusbar.notification.voicereplies.VoiceReplyTarget$expandShade$2$1
                    final /* synthetic */ VoiceReplyTarget$expandShade$2$callback$1 $callback;
                    final /* synthetic */ VoiceReplyTarget this$0;

                    /* access modifiers changed from: package-private */
                    {
                        this.this$0 = r1;
                        this.$callback = r2;
                    }

                    /* Return type fixed from 'java.lang.Object' to match base method */
                    /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object] */
                    @Override // kotlin.jvm.functions.Function1
                    public /* bridge */ /* synthetic */ Unit invoke(Throwable th) {
                        invoke(th);
                        return Unit.INSTANCE;
                    }

                    public final void invoke(Throwable th) {
                        this.this$0.statusBarStateController.removeCallback(this.$callback);
                    }
                });
                this.statusBarStateController.addCallback(voiceReplyTarget$expandShade$2$callback$1);
                LockscreenShadeTransitionController.goToLockedShade$default(this.shadeTransitionController, getEntry().getRow(), false, 2, null);
                Object result = cancellableContinuationImpl.getResult();
                if (result == IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
                    DebugProbesKt.probeCoroutineSuspended(continuation);
                }
                return result;
            } else if (state != 2) {
                LogBuffer logBuffer2 = this.logger.getLogBuffer();
                LogLevel logLevel2 = LogLevel.DEBUG;
                NotificationVoiceReplyLogger$logStatic$2 notificationVoiceReplyLogger$logStatic$22 = new NotificationVoiceReplyLogger$logStatic$2("Unknown state, cancelling");
                if (!logBuffer2.getFrozen()) {
                    logBuffer2.push(logBuffer2.obtain("NotifVoiceReply", logLevel2, notificationVoiceReplyLogger$logStatic$22));
                }
                return Boxing.boxBoolean(z);
            }
        } else if (!this.statusBarStateController.isExpanded()) {
            CancellableContinuationImpl cancellableContinuationImpl2 = new CancellableContinuationImpl(IntrinsicsKt__IntrinsicsJvmKt.intercepted(continuation), 1);
            LogBuffer logBuffer3 = this.logger.getLogBuffer();
            LogLevel logLevel3 = LogLevel.DEBUG;
            NotificationVoiceReplyLogger$logStatic$2 notificationVoiceReplyLogger$logStatic$23 = new NotificationVoiceReplyLogger$logStatic$2("Shade collapsed, waiting for expansion");
            if (!logBuffer3.getFrozen()) {
                logBuffer3.push(logBuffer3.obtain("NotifVoiceReply", logLevel3, notificationVoiceReplyLogger$logStatic$23));
            }
            VoiceReplyTarget$expandShade$3$callback$1 voiceReplyTarget$expandShade$3$callback$1 = new VoiceReplyTarget$expandShade$3$callback$1(NotificationVoiceReplyManagerKt.AtomicLatch(), this, cancellableContinuationImpl2);
            cancellableContinuationImpl2.invokeOnCancellation(new Function1<Throwable, Unit>(this, voiceReplyTarget$expandShade$3$callback$1) { // from class: com.google.android.systemui.statusbar.notification.voicereplies.VoiceReplyTarget$expandShade$3$1
                final /* synthetic */ VoiceReplyTarget$expandShade$3$callback$1 $callback;
                final /* synthetic */ VoiceReplyTarget this$0;

                /* access modifiers changed from: package-private */
                {
                    this.this$0 = r1;
                    this.$callback = r2;
                }

                /* Return type fixed from 'java.lang.Object' to match base method */
                /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object] */
                @Override // kotlin.jvm.functions.Function1
                public /* bridge */ /* synthetic */ Unit invoke(Throwable th) {
                    invoke(th);
                    return Unit.INSTANCE;
                }

                public final void invoke(Throwable th) {
                    this.this$0.statusBarStateController.removeCallback(this.$callback);
                }
            });
            this.statusBarStateController.addCallback(voiceReplyTarget$expandShade$3$callback$1);
            this.statusBar.animateExpandNotificationsPanel();
            Object result2 = cancellableContinuationImpl2.getResult();
            if (result2 == IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
                DebugProbesKt.probeCoroutineSuspended(continuation);
            }
            return result2;
        }
        z = true;
        return Boxing.boxBoolean(z);
    }

    final /* synthetic */ Object awaitFocusState(RemoteInputView remoteInputView, boolean z, Continuation continuation) {
        if (remoteInputView.editTextHasFocus() == z) {
            return Unit.INSTANCE;
        }
        CancellableContinuationImpl cancellableContinuationImpl = new CancellableContinuationImpl(IntrinsicsKt__IntrinsicsJvmKt.intercepted(continuation), 1);
        VoiceReplyTarget$awaitFocusState$2$listener$1 voiceReplyTarget$awaitFocusState$2$listener$1 = new VoiceReplyTarget$awaitFocusState$2$listener$1(z, NotificationVoiceReplyManagerKt.AtomicLatch(), remoteInputView, cancellableContinuationImpl);
        remoteInputView.addOnEditTextFocusChangedListener(voiceReplyTarget$awaitFocusState$2$listener$1);
        cancellableContinuationImpl.invokeOnCancellation(new Function1<Throwable, Unit>(remoteInputView, voiceReplyTarget$awaitFocusState$2$listener$1) { // from class: com.google.android.systemui.statusbar.notification.voicereplies.VoiceReplyTarget$awaitFocusState$2$1
            final /* synthetic */ VoiceReplyTarget$awaitFocusState$2$listener$1 $listener;
            final /* synthetic */ RemoteInputView $this_awaitFocusState;

            /* access modifiers changed from: package-private */
            {
                this.$this_awaitFocusState = r1;
                this.$listener = r2;
            }

            /* Return type fixed from 'java.lang.Object' to match base method */
            /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object] */
            @Override // kotlin.jvm.functions.Function1
            public /* bridge */ /* synthetic */ Unit invoke(Throwable th) {
                invoke(th);
                return Unit.INSTANCE;
            }

            public final void invoke(Throwable th) {
                this.$this_awaitFocusState.removeOnEditTextFocusChangedListener(this.$listener);
            }
        });
        Object result = cancellableContinuationImpl.getResult();
        if (result == IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
            DebugProbesKt.probeCoroutineSuspended(continuation);
        }
        return result == IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED() ? result : Unit.INSTANCE;
    }

    final /* synthetic */ Object awaitFocusGained(RemoteInputView remoteInputView, Continuation continuation) {
        Object awaitFocusState = awaitFocusState(remoteInputView, true, continuation);
        return awaitFocusState == IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED() ? awaitFocusState : Unit.INSTANCE;
    }

    final /* synthetic */ Object awaitFocusLost(RemoteInputView remoteInputView, Continuation continuation) {
        Object awaitFocusState = awaitFocusState(remoteInputView, false, continuation);
        return awaitFocusState == IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED() ? awaitFocusState : Unit.INSTANCE;
    }
}
