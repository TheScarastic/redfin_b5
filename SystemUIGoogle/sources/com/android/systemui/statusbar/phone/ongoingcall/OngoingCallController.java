package com.android.systemui.statusbar.phone.ongoingcall;

import android.app.IActivityManager;
import android.app.IUidObserver;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import com.android.systemui.R$id;
import com.android.systemui.animation.ActivityLaunchAnimator;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.statusbar.FeatureFlags;
import com.android.systemui.statusbar.notification.collection.notifcollection.CommonNotifCollection;
import com.android.systemui.statusbar.phone.ongoingcall.OngoingCallController;
import com.android.systemui.statusbar.policy.CallbackController;
import com.android.systemui.util.time.SystemClock;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: OngoingCallController.kt */
/* loaded from: classes.dex */
public final class OngoingCallController implements CallbackController<OngoingCallListener> {
    private final ActivityStarter activityStarter;
    private CallNotificationInfo callNotificationInfo;
    private View chipView;
    private final FeatureFlags featureFlags;
    private final IActivityManager iActivityManager;
    private final OngoingCallLogger logger;
    private final Executor mainExecutor;
    private final CommonNotifCollection notifCollection;
    private final SystemClock systemClock;
    private IUidObserver.Stub uidObserver;
    private boolean isCallAppVisible = true;
    private final List<OngoingCallListener> mListeners = new ArrayList();
    private final OngoingCallController$notifListener$1 notifListener = new OngoingCallController$notifListener$1(this);

    /* access modifiers changed from: private */
    public final boolean isProcessVisibleToUser(int i) {
        return i <= 2;
    }

    public OngoingCallController(CommonNotifCollection commonNotifCollection, FeatureFlags featureFlags, SystemClock systemClock, ActivityStarter activityStarter, Executor executor, IActivityManager iActivityManager, OngoingCallLogger ongoingCallLogger) {
        Intrinsics.checkNotNullParameter(commonNotifCollection, "notifCollection");
        Intrinsics.checkNotNullParameter(featureFlags, "featureFlags");
        Intrinsics.checkNotNullParameter(systemClock, "systemClock");
        Intrinsics.checkNotNullParameter(activityStarter, "activityStarter");
        Intrinsics.checkNotNullParameter(executor, "mainExecutor");
        Intrinsics.checkNotNullParameter(iActivityManager, "iActivityManager");
        Intrinsics.checkNotNullParameter(ongoingCallLogger, "logger");
        this.notifCollection = commonNotifCollection;
        this.featureFlags = featureFlags;
        this.systemClock = systemClock;
        this.activityStarter = activityStarter;
        this.mainExecutor = executor;
        this.iActivityManager = iActivityManager;
        this.logger = ongoingCallLogger;
    }

    public final void init() {
        if (this.featureFlags.isOngoingCallStatusBarChipEnabled()) {
            this.notifCollection.addCollectionListener(this.notifListener);
        }
    }

    public final void setChipView(View view) {
        Intrinsics.checkNotNullParameter(view, "chipView");
        tearDownChipView();
        this.chipView = view;
        if (hasOngoingCall()) {
            updateChip();
        }
    }

    public final void notifyChipVisibilityChanged(boolean z) {
        this.logger.logChipVisibilityChanged(z);
    }

    public final boolean hasOngoingCall() {
        CallNotificationInfo callNotificationInfo = this.callNotificationInfo;
        return Intrinsics.areEqual(callNotificationInfo == null ? null : Boolean.valueOf(callNotificationInfo.isOngoing()), Boolean.TRUE) && !this.isCallAppVisible;
    }

    public void addCallback(OngoingCallListener ongoingCallListener) {
        Intrinsics.checkNotNullParameter(ongoingCallListener, "listener");
        synchronized (this.mListeners) {
            if (!this.mListeners.contains(ongoingCallListener)) {
                this.mListeners.add(ongoingCallListener);
            }
            Unit unit = Unit.INSTANCE;
        }
    }

    public void removeCallback(OngoingCallListener ongoingCallListener) {
        Intrinsics.checkNotNullParameter(ongoingCallListener, "listener");
        synchronized (this.mListeners) {
            this.mListeners.remove(ongoingCallListener);
        }
    }

    /* access modifiers changed from: private */
    public final void updateChip() {
        OngoingCallChronometer ongoingCallChronometer;
        View view;
        CallNotificationInfo callNotificationInfo = this.callNotificationInfo;
        if (callNotificationInfo != null) {
            View view2 = this.chipView;
            if (view2 == null) {
                ongoingCallChronometer = null;
            } else {
                ongoingCallChronometer = getTimeView(view2);
            }
            if (view2 == null) {
                view = null;
            } else {
                view = view2.findViewById(R$id.ongoing_call_chip_background);
            }
            if (view2 == null || ongoingCallChronometer == null || view == null) {
                this.callNotificationInfo = null;
                if (OngoingCallControllerKt.access$getDEBUG$p()) {
                    Log.w("OngoingCallController", "Ongoing call chip view could not be found; Not displaying chip in status bar");
                    return;
                }
                return;
            }
            if (callNotificationInfo.hasValidStartTime()) {
                ongoingCallChronometer.setShouldHideText(false);
                ongoingCallChronometer.setBase((callNotificationInfo.getCallStartTime() - this.systemClock.currentTimeMillis()) + this.systemClock.elapsedRealtime());
                ongoingCallChronometer.start();
            } else {
                ongoingCallChronometer.setShouldHideText(true);
                ongoingCallChronometer.stop();
            }
            Intent intent = callNotificationInfo.getIntent();
            if (intent != null) {
                view2.setOnClickListener(new View.OnClickListener(this, intent, view) { // from class: com.android.systemui.statusbar.phone.ongoingcall.OngoingCallController$updateChip$1$1
                    final /* synthetic */ View $backgroundView;
                    final /* synthetic */ Intent $intent;
                    final /* synthetic */ OngoingCallController this$0;

                    /* access modifiers changed from: package-private */
                    {
                        this.this$0 = r1;
                        this.$intent = r2;
                        this.$backgroundView = r3;
                    }

                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view3) {
                        this.this$0.logger.logChipClicked();
                        this.this$0.activityStarter.postStartActivityDismissingKeyguard(this.$intent, 0, ActivityLaunchAnimator.Controller.Companion.fromView(this.$backgroundView, 34));
                    }
                });
            }
            setUpUidObserver(callNotificationInfo);
            for (OngoingCallListener ongoingCallListener : this.mListeners) {
                ongoingCallListener.onOngoingCallStateChanged(true);
            }
        }
    }

    private final void setUpUidObserver(CallNotificationInfo callNotificationInfo) {
        this.isCallAppVisible = isProcessVisibleToUser(this.iActivityManager.getUidProcessState(callNotificationInfo.getUid(), (String) null));
        IUidObserver.Stub stub = this.uidObserver;
        if (stub != null) {
            this.iActivityManager.unregisterUidObserver(stub);
        }
        OngoingCallController$setUpUidObserver$1 ongoingCallController$setUpUidObserver$1 = new IUidObserver.Stub(callNotificationInfo, this) { // from class: com.android.systemui.statusbar.phone.ongoingcall.OngoingCallController$setUpUidObserver$1
            final /* synthetic */ OngoingCallController.CallNotificationInfo $currentCallNotificationInfo;
            final /* synthetic */ OngoingCallController this$0;

            public void onUidActive(int i) {
            }

            public void onUidCachedChanged(int i, boolean z) {
            }

            public void onUidGone(int i, boolean z) {
            }

            public void onUidIdle(int i, boolean z) {
            }

            /* access modifiers changed from: package-private */
            {
                this.$currentCallNotificationInfo = r1;
                this.this$0 = r2;
            }

            public void onUidStateChanged(int i, int i2, long j, int i3) {
                if (i == this.$currentCallNotificationInfo.getUid()) {
                    boolean z = this.this$0.isCallAppVisible;
                    OngoingCallController ongoingCallController = this.this$0;
                    ongoingCallController.isCallAppVisible = ongoingCallController.isProcessVisibleToUser(i2);
                    if (z != this.this$0.isCallAppVisible) {
                        this.this$0.mainExecutor.execute(
                        /*  JADX ERROR: Method code generation error
                            jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x002c: INVOKE  
                              (wrap: java.util.concurrent.Executor : 0x0021: ONE_ARG  (r1v4 java.util.concurrent.Executor A[REMOVE]) = 
                              (wrap: java.util.concurrent.Executor : 0x0000: IGET  
                              (wrap: com.android.systemui.statusbar.phone.ongoingcall.OngoingCallController : 0x001f: IGET  (r1v3 com.android.systemui.statusbar.phone.ongoingcall.OngoingCallController A[REMOVE]) = 
                              (r0v0 'this' com.android.systemui.statusbar.phone.ongoingcall.OngoingCallController$setUpUidObserver$1 A[IMMUTABLE_TYPE, THIS])
                             com.android.systemui.statusbar.phone.ongoingcall.OngoingCallController$setUpUidObserver$1.this$0 com.android.systemui.statusbar.phone.ongoingcall.OngoingCallController)
                             com.android.systemui.statusbar.phone.ongoingcall.OngoingCallController.mainExecutor java.util.concurrent.Executor)
                            )
                              (wrap: com.android.systemui.statusbar.phone.ongoingcall.OngoingCallController$setUpUidObserver$1$onUidStateChanged$1 : 0x0029: CONSTRUCTOR  (r2v4 com.android.systemui.statusbar.phone.ongoingcall.OngoingCallController$setUpUidObserver$1$onUidStateChanged$1 A[REMOVE]) = 
                              (wrap: com.android.systemui.statusbar.phone.ongoingcall.OngoingCallController : 0x0027: IGET  (r0v1 com.android.systemui.statusbar.phone.ongoingcall.OngoingCallController A[REMOVE]) = 
                              (r0v0 'this' com.android.systemui.statusbar.phone.ongoingcall.OngoingCallController$setUpUidObserver$1 A[IMMUTABLE_TYPE, THIS])
                             com.android.systemui.statusbar.phone.ongoingcall.OngoingCallController$setUpUidObserver$1.this$0 com.android.systemui.statusbar.phone.ongoingcall.OngoingCallController)
                             call: com.android.systemui.statusbar.phone.ongoingcall.OngoingCallController$setUpUidObserver$1$onUidStateChanged$1.<init>(com.android.systemui.statusbar.phone.ongoingcall.OngoingCallController):void type: CONSTRUCTOR)
                             type: INTERFACE call: java.util.concurrent.Executor.execute(java.lang.Runnable):void in method: com.android.systemui.statusbar.phone.ongoingcall.OngoingCallController$setUpUidObserver$1.onUidStateChanged(int, int, long, int):void, file: classes.dex
                            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:285)
                            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:248)
                            	at jadx.core.codegen.RegionGen.makeSimpleBlock(RegionGen.java:105)
                            	at jadx.core.dex.nodes.IBlock.generate(IBlock.java:15)
                            	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:65)
                            	at jadx.core.dex.regions.Region.generate(Region.java:35)
                            	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:65)
                            	at jadx.core.codegen.RegionGen.makeRegionIndent(RegionGen.java:94)
                            	at jadx.core.codegen.RegionGen.makeIf(RegionGen.java:137)
                            	at jadx.core.dex.regions.conditions.IfRegion.generate(IfRegion.java:137)
                            	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:65)
                            	at jadx.core.dex.regions.Region.generate(Region.java:35)
                            	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:65)
                            	at jadx.core.dex.regions.Region.generate(Region.java:35)
                            	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:65)
                            	at jadx.core.codegen.RegionGen.makeRegionIndent(RegionGen.java:94)
                            	at jadx.core.codegen.RegionGen.makeIf(RegionGen.java:137)
                            	at jadx.core.dex.regions.conditions.IfRegion.generate(IfRegion.java:137)
                            	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:65)
                            	at jadx.core.dex.regions.Region.generate(Region.java:35)
                            	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:65)
                            	at jadx.core.dex.regions.Region.generate(Region.java:35)
                            	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:65)
                            	at jadx.core.codegen.MethodGen.addRegionInsns(MethodGen.java:261)
                            	at jadx.core.codegen.MethodGen.addInstructions(MethodGen.java:254)
                            	at jadx.core.codegen.ClassGen.addMethodCode(ClassGen.java:349)
                            	at jadx.core.codegen.ClassGen.addMethod(ClassGen.java:302)
                            	at jadx.core.codegen.ClassGen.lambda$addInnerClsAndMethods$2(ClassGen.java:271)
                            	at java.base/java.util.stream.ForEachOps$ForEachOp$OfRef.accept(ForEachOps.java:183)
                            	at java.base/java.util.ArrayList.forEach(ArrayList.java:1541)
                            	at java.base/java.util.stream.SortedOps$RefSortingSink.end(SortedOps.java:395)
                            	at java.base/java.util.stream.Sink$ChainedReference.end(Sink.java:258)
                            Caused by: jadx.core.utils.exceptions.JadxRuntimeException: Expected class to be processed at this point, class: com.android.systemui.statusbar.phone.ongoingcall.OngoingCallController$setUpUidObserver$1$onUidStateChanged$1, state: NOT_LOADED
                            	at jadx.core.dex.nodes.ClassNode.ensureProcessed(ClassNode.java:259)
                            	at jadx.core.codegen.InsnGen.makeConstructor(InsnGen.java:675)
                            	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:393)
                            	at jadx.core.codegen.InsnGen.addWrappedArg(InsnGen.java:141)
                            	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:120)
                            	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:104)
                            	at jadx.core.codegen.InsnGen.generateMethodArguments(InsnGen.java:976)
                            	at jadx.core.codegen.InsnGen.makeInvoke(InsnGen.java:801)
                            	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:397)
                            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:278)
                            	... 31 more
                            */
                        /*
                            this = this;
                            com.android.systemui.statusbar.phone.ongoingcall.OngoingCallController$CallNotificationInfo r3 = r0.$currentCallNotificationInfo
                            int r3 = r3.getUid()
                            if (r1 != r3) goto L_0x002f
                            com.android.systemui.statusbar.phone.ongoingcall.OngoingCallController r1 = r0.this$0
                            boolean r1 = com.android.systemui.statusbar.phone.ongoingcall.OngoingCallController.access$isCallAppVisible$p(r1)
                            com.android.systemui.statusbar.phone.ongoingcall.OngoingCallController r3 = r0.this$0
                            boolean r2 = com.android.systemui.statusbar.phone.ongoingcall.OngoingCallController.access$isProcessVisibleToUser(r3, r2)
                            com.android.systemui.statusbar.phone.ongoingcall.OngoingCallController.access$setCallAppVisible$p(r3, r2)
                            com.android.systemui.statusbar.phone.ongoingcall.OngoingCallController r2 = r0.this$0
                            boolean r2 = com.android.systemui.statusbar.phone.ongoingcall.OngoingCallController.access$isCallAppVisible$p(r2)
                            if (r1 == r2) goto L_0x002f
                            com.android.systemui.statusbar.phone.ongoingcall.OngoingCallController r1 = r0.this$0
                            java.util.concurrent.Executor r1 = com.android.systemui.statusbar.phone.ongoingcall.OngoingCallController.access$getMainExecutor$p(r1)
                            com.android.systemui.statusbar.phone.ongoingcall.OngoingCallController$setUpUidObserver$1$onUidStateChanged$1 r2 = new com.android.systemui.statusbar.phone.ongoingcall.OngoingCallController$setUpUidObserver$1$onUidStateChanged$1
                            com.android.systemui.statusbar.phone.ongoingcall.OngoingCallController r0 = r0.this$0
                            r2.<init>(r0)
                            r1.execute(r2)
                        L_0x002f:
                            return
                        */
                        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.phone.ongoingcall.OngoingCallController$setUpUidObserver$1.onUidStateChanged(int, int, long, int):void");
                    }
                };
                this.uidObserver = ongoingCallController$setUpUidObserver$1;
                this.iActivityManager.registerUidObserver(ongoingCallController$setUpUidObserver$1, 1, -1, (String) null);
            }

            /* access modifiers changed from: private */
            public final void removeChip() {
                this.callNotificationInfo = null;
                tearDownChipView();
                for (OngoingCallListener ongoingCallListener : this.mListeners) {
                    ongoingCallListener.onOngoingCallStateChanged(true);
                }
                IUidObserver.Stub stub = this.uidObserver;
                if (stub != null) {
                    this.iActivityManager.unregisterUidObserver(stub);
                }
            }

            public final Unit tearDownChipView() {
                OngoingCallChronometer timeView;
                View view = this.chipView;
                if (view == null || (timeView = getTimeView(view)) == null) {
                    return null;
                }
                timeView.stop();
                return Unit.INSTANCE;
            }

            private final OngoingCallChronometer getTimeView(View view) {
                return (OngoingCallChronometer) view.findViewById(R$id.ongoing_call_chip_time);
            }

            /* compiled from: OngoingCallController.kt */
            /* access modifiers changed from: private */
            /* loaded from: classes.dex */
            public static final class CallNotificationInfo {
                private final long callStartTime;
                private final Intent intent;
                private final boolean isOngoing;
                private final String key;
                private final int uid;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    if (!(obj instanceof CallNotificationInfo)) {
                        return false;
                    }
                    CallNotificationInfo callNotificationInfo = (CallNotificationInfo) obj;
                    return Intrinsics.areEqual(this.key, callNotificationInfo.key) && this.callStartTime == callNotificationInfo.callStartTime && Intrinsics.areEqual(this.intent, callNotificationInfo.intent) && this.uid == callNotificationInfo.uid && this.isOngoing == callNotificationInfo.isOngoing;
                }

                public int hashCode() {
                    int hashCode = ((this.key.hashCode() * 31) + Long.hashCode(this.callStartTime)) * 31;
                    Intent intent = this.intent;
                    int hashCode2 = (((hashCode + (intent == null ? 0 : intent.hashCode())) * 31) + Integer.hashCode(this.uid)) * 31;
                    boolean z = this.isOngoing;
                    if (z) {
                        z = true;
                    }
                    int i = z ? 1 : 0;
                    int i2 = z ? 1 : 0;
                    int i3 = z ? 1 : 0;
                    return hashCode2 + i;
                }

                public String toString() {
                    return "CallNotificationInfo(key=" + this.key + ", callStartTime=" + this.callStartTime + ", intent=" + this.intent + ", uid=" + this.uid + ", isOngoing=" + this.isOngoing + ')';
                }

                public CallNotificationInfo(String str, long j, Intent intent, int i, boolean z) {
                    Intrinsics.checkNotNullParameter(str, "key");
                    this.key = str;
                    this.callStartTime = j;
                    this.intent = intent;
                    this.uid = i;
                    this.isOngoing = z;
                }

                public final String getKey() {
                    return this.key;
                }

                public final long getCallStartTime() {
                    return this.callStartTime;
                }

                public final Intent getIntent() {
                    return this.intent;
                }

                public final int getUid() {
                    return this.uid;
                }

                public final boolean isOngoing() {
                    return this.isOngoing;
                }

                public final boolean hasValidStartTime() {
                    return this.callStartTime > 0;
                }
            }
        }
