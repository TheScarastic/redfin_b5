package com.android.systemui.controls.controller;

import android.app.PendingIntent;
import android.app.backup.BackupManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.UserHandle;
import android.service.controls.Control;
import android.service.controls.actions.ControlAction;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.Dumpable;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.controls.ControlStatus;
import com.android.systemui.controls.ControlsServiceInfo;
import com.android.systemui.controls.controller.ControlsBindingController;
import com.android.systemui.controls.controller.ControlsController;
import com.android.systemui.controls.management.ControlsListingController;
import com.android.systemui.controls.ui.ControlsUiController;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.util.concurrency.DelayableExecutor;
import java.io.File;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Supplier;
import kotlin.collections.CollectionsKt__CollectionsJVMKt;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.collections.CollectionsKt__IterablesKt;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.collections.SetsKt___SetsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ControlsControllerImpl.kt */
/* loaded from: classes.dex */
public final class ControlsControllerImpl implements Dumpable, ControlsController {
    public static final Companion Companion = new Companion(null);
    private AuxiliaryPersistenceWrapper auxiliaryPersistenceWrapper;
    private final ControlsBindingController bindingController;
    private final BroadcastDispatcher broadcastDispatcher;
    private final Context context;
    private UserHandle currentUser;
    private final DelayableExecutor executor;
    private final ControlsControllerImpl$listingCallback$1 listingCallback;
    private final ControlsListingController listingController;
    private final ControlsFavoritePersistenceWrapper persistenceWrapper;
    private final BroadcastReceiver restoreFinishedReceiver;
    private boolean seedingInProgress;
    private final ControlsUiController uiController;
    private boolean userChanging;
    private UserStructure userStructure;
    private final ControlsControllerImpl$userSwitchReceiver$1 userSwitchReceiver;
    private final List<Consumer<Boolean>> seedingCallbacks = new ArrayList();
    private final ContentObserver settingObserver = new ContentObserver(this) { // from class: com.android.systemui.controls.controller.ControlsControllerImpl$settingObserver$1
        final /* synthetic */ ControlsControllerImpl this$0;

        /* access modifiers changed from: package-private */
        {
            this.this$0 = r1;
        }

        public void onChange(boolean z, Collection<? extends Uri> collection, int i, int i2) {
            Intrinsics.checkNotNullParameter(collection, "uris");
            if (!this.this$0.userChanging && i2 == this.this$0.getCurrentUserId()) {
                this.this$0.resetFavorites();
            }
        }
    };

    @VisibleForTesting
    public static /* synthetic */ void getAuxiliaryPersistenceWrapper$frameworks__base__packages__SystemUI__android_common__SystemUI_core$annotations() {
    }

    @VisibleForTesting
    public static /* synthetic */ void getRestoreFinishedReceiver$frameworks__base__packages__SystemUI__android_common__SystemUI_core$annotations() {
    }

    @VisibleForTesting
    public static /* synthetic */ void getSettingObserver$frameworks__base__packages__SystemUI__android_common__SystemUI_core$annotations() {
    }

    public ControlsControllerImpl(Context context, DelayableExecutor delayableExecutor, ControlsUiController controlsUiController, ControlsBindingController controlsBindingController, ControlsListingController controlsListingController, BroadcastDispatcher broadcastDispatcher, Optional<ControlsFavoritePersistenceWrapper> optional, DumpManager dumpManager, UserTracker userTracker) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(delayableExecutor, "executor");
        Intrinsics.checkNotNullParameter(controlsUiController, "uiController");
        Intrinsics.checkNotNullParameter(controlsBindingController, "bindingController");
        Intrinsics.checkNotNullParameter(controlsListingController, "listingController");
        Intrinsics.checkNotNullParameter(broadcastDispatcher, "broadcastDispatcher");
        Intrinsics.checkNotNullParameter(optional, "optionalWrapper");
        Intrinsics.checkNotNullParameter(dumpManager, "dumpManager");
        Intrinsics.checkNotNullParameter(userTracker, "userTracker");
        this.context = context;
        this.executor = delayableExecutor;
        this.uiController = controlsUiController;
        this.bindingController = controlsBindingController;
        this.listingController = controlsListingController;
        this.broadcastDispatcher = broadcastDispatcher;
        this.userChanging = true;
        UserHandle userHandle = userTracker.getUserHandle();
        this.currentUser = userHandle;
        this.userStructure = new UserStructure(context, userHandle);
        ControlsFavoritePersistenceWrapper orElseGet = optional.orElseGet(new Supplier<ControlsFavoritePersistenceWrapper>(this) { // from class: com.android.systemui.controls.controller.ControlsControllerImpl.1
            final /* synthetic */ ControlsControllerImpl this$0;

            {
                this.this$0 = r1;
            }

            @Override // java.util.function.Supplier
            public final ControlsFavoritePersistenceWrapper get() {
                File file = this.this$0.userStructure.getFile();
                Intrinsics.checkNotNullExpressionValue(file, "userStructure.file");
                return new ControlsFavoritePersistenceWrapper(file, this.this$0.executor, new BackupManager(this.this$0.userStructure.getUserContext()));
            }
        });
        Intrinsics.checkNotNullExpressionValue(orElseGet, "optionalWrapper.orElseGet {\n            ControlsFavoritePersistenceWrapper(\n                    userStructure.file,\n                    executor,\n                    BackupManager(userStructure.userContext)\n            )\n        }");
        this.persistenceWrapper = orElseGet;
        File auxiliaryFile = this.userStructure.getAuxiliaryFile();
        Intrinsics.checkNotNullExpressionValue(auxiliaryFile, "userStructure.auxiliaryFile");
        this.auxiliaryPersistenceWrapper = new AuxiliaryPersistenceWrapper(auxiliaryFile, delayableExecutor);
        ControlsControllerImpl$userSwitchReceiver$1 controlsControllerImpl$userSwitchReceiver$1 = new BroadcastReceiver(this) { // from class: com.android.systemui.controls.controller.ControlsControllerImpl$userSwitchReceiver$1
            final /* synthetic */ ControlsControllerImpl this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
            }

            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                Intrinsics.checkNotNullParameter(context2, "context");
                Intrinsics.checkNotNullParameter(intent, "intent");
                if (Intrinsics.areEqual(intent.getAction(), "android.intent.action.USER_SWITCHED")) {
                    this.this$0.userChanging = true;
                    UserHandle of = UserHandle.of(intent.getIntExtra("android.intent.extra.user_handle", getSendingUserId()));
                    if (Intrinsics.areEqual(this.this$0.currentUser, of)) {
                        this.this$0.userChanging = false;
                        return;
                    }
                    ControlsControllerImpl controlsControllerImpl = this.this$0;
                    Intrinsics.checkNotNullExpressionValue(of, "newUser");
                    controlsControllerImpl.setValuesForUser(of);
                }
            }
        };
        this.userSwitchReceiver = controlsControllerImpl$userSwitchReceiver$1;
        ControlsControllerImpl$restoreFinishedReceiver$1 controlsControllerImpl$restoreFinishedReceiver$1 = new BroadcastReceiver(this) { // from class: com.android.systemui.controls.controller.ControlsControllerImpl$restoreFinishedReceiver$1
            final /* synthetic */ ControlsControllerImpl this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
            }

            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                Intrinsics.checkNotNullParameter(context2, "context");
                Intrinsics.checkNotNullParameter(intent, "intent");
                if (intent.getIntExtra("android.intent.extra.USER_ID", -10000) == this.this$0.getCurrentUserId()) {
                    this.this$0.executor.execute(
                    /*  JADX ERROR: Method code generation error
                        jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x0027: INVOKE  
                          (wrap: com.android.systemui.util.concurrency.DelayableExecutor : 0x001c: IGET  (r2v5 com.android.systemui.util.concurrency.DelayableExecutor A[REMOVE]) = 
                          (wrap: com.android.systemui.controls.controller.ControlsControllerImpl : 0x001a: IGET  (r2v4 com.android.systemui.controls.controller.ControlsControllerImpl A[REMOVE]) = 
                          (r1v0 'this' com.android.systemui.controls.controller.ControlsControllerImpl$restoreFinishedReceiver$1 A[IMMUTABLE_TYPE, THIS])
                         com.android.systemui.controls.controller.ControlsControllerImpl$restoreFinishedReceiver$1.this$0 com.android.systemui.controls.controller.ControlsControllerImpl)
                         com.android.systemui.controls.controller.ControlsControllerImpl.executor com.android.systemui.util.concurrency.DelayableExecutor)
                          (wrap: com.android.systemui.controls.controller.ControlsControllerImpl$restoreFinishedReceiver$1$onReceive$1 : 0x0024: CONSTRUCTOR  (r3v3 com.android.systemui.controls.controller.ControlsControllerImpl$restoreFinishedReceiver$1$onReceive$1 A[REMOVE]) = 
                          (wrap: com.android.systemui.controls.controller.ControlsControllerImpl : 0x0022: IGET  (r1v1 com.android.systemui.controls.controller.ControlsControllerImpl A[REMOVE]) = 
                          (r1v0 'this' com.android.systemui.controls.controller.ControlsControllerImpl$restoreFinishedReceiver$1 A[IMMUTABLE_TYPE, THIS])
                         com.android.systemui.controls.controller.ControlsControllerImpl$restoreFinishedReceiver$1.this$0 com.android.systemui.controls.controller.ControlsControllerImpl)
                         call: com.android.systemui.controls.controller.ControlsControllerImpl$restoreFinishedReceiver$1$onReceive$1.<init>(com.android.systemui.controls.controller.ControlsControllerImpl):void type: CONSTRUCTOR)
                         type: INTERFACE call: java.util.concurrent.Executor.execute(java.lang.Runnable):void in method: com.android.systemui.controls.controller.ControlsControllerImpl$restoreFinishedReceiver$1.onReceive(android.content.Context, android.content.Intent):void, file: classes.dex
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
                        	at jadx.core.codegen.MethodGen.addRegionInsns(MethodGen.java:261)
                        	at jadx.core.codegen.MethodGen.addInstructions(MethodGen.java:254)
                        	at jadx.core.codegen.ClassGen.addMethodCode(ClassGen.java:349)
                        	at jadx.core.codegen.ClassGen.addMethod(ClassGen.java:302)
                        	at jadx.core.codegen.ClassGen.lambda$addInnerClsAndMethods$2(ClassGen.java:271)
                        	at java.base/java.util.stream.ForEachOps$ForEachOp$OfRef.accept(ForEachOps.java:183)
                        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1541)
                        	at java.base/java.util.stream.SortedOps$RefSortingSink.end(SortedOps.java:395)
                        	at java.base/java.util.stream.Sink$ChainedReference.end(Sink.java:258)
                        Caused by: jadx.core.utils.exceptions.JadxRuntimeException: Expected class to be processed at this point, class: com.android.systemui.controls.controller.ControlsControllerImpl$restoreFinishedReceiver$1$onReceive$1, state: NOT_LOADED
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
                        	... 23 more
                        */
                    /*
                        this = this;
                        java.lang.String r0 = "context"
                        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r2, r0)
                        java.lang.String r2 = "intent"
                        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r3, r2)
                        java.lang.String r2 = "android.intent.extra.USER_ID"
                        r0 = -10000(0xffffffffffffd8f0, float:NaN)
                        int r2 = r3.getIntExtra(r2, r0)
                        com.android.systemui.controls.controller.ControlsControllerImpl r3 = r1.this$0
                        int r3 = r3.getCurrentUserId()
                        if (r2 != r3) goto L_0x002a
                        com.android.systemui.controls.controller.ControlsControllerImpl r2 = r1.this$0
                        com.android.systemui.util.concurrency.DelayableExecutor r2 = com.android.systemui.controls.controller.ControlsControllerImpl.access$getExecutor$p(r2)
                        com.android.systemui.controls.controller.ControlsControllerImpl$restoreFinishedReceiver$1$onReceive$1 r3 = new com.android.systemui.controls.controller.ControlsControllerImpl$restoreFinishedReceiver$1$onReceive$1
                        com.android.systemui.controls.controller.ControlsControllerImpl r1 = r1.this$0
                        r3.<init>(r1)
                        r2.execute(r3)
                    L_0x002a:
                        return
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.controls.controller.ControlsControllerImpl$restoreFinishedReceiver$1.onReceive(android.content.Context, android.content.Intent):void");
                }
            };
            this.restoreFinishedReceiver = controlsControllerImpl$restoreFinishedReceiver$1;
            ControlsControllerImpl$listingCallback$1 controlsControllerImpl$listingCallback$1 = new ControlsListingController.ControlsListingCallback(this) { // from class: com.android.systemui.controls.controller.ControlsControllerImpl$listingCallback$1
                final /* synthetic */ ControlsControllerImpl this$0;

                /* access modifiers changed from: package-private */
                {
                    this.this$0 = r1;
                }

                @Override // com.android.systemui.controls.management.ControlsListingController.ControlsListingCallback
                public void onServicesUpdated(List<ControlsServiceInfo> list) {
                    Intrinsics.checkNotNullParameter(list, "serviceInfos");
                    this.this$0.executor.execute(
                    /*  JADX ERROR: Method code generation error
                        jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x0012: INVOKE  
                          (wrap: com.android.systemui.util.concurrency.DelayableExecutor : 0x0007: IGET  (r0v2 com.android.systemui.util.concurrency.DelayableExecutor A[REMOVE]) = 
                          (wrap: com.android.systemui.controls.controller.ControlsControllerImpl : 0x0005: IGET  (r0v1 com.android.systemui.controls.controller.ControlsControllerImpl A[REMOVE]) = (r2v0 'this' com.android.systemui.controls.controller.ControlsControllerImpl$listingCallback$1 A[IMMUTABLE_TYPE, THIS]) com.android.systemui.controls.controller.ControlsControllerImpl$listingCallback$1.this$0 com.android.systemui.controls.controller.ControlsControllerImpl)
                         com.android.systemui.controls.controller.ControlsControllerImpl.executor com.android.systemui.util.concurrency.DelayableExecutor)
                          (wrap: com.android.systemui.controls.controller.ControlsControllerImpl$listingCallback$1$onServicesUpdated$1 : 0x000f: CONSTRUCTOR  (r1v0 com.android.systemui.controls.controller.ControlsControllerImpl$listingCallback$1$onServicesUpdated$1 A[REMOVE]) = 
                          (r3v0 'list' java.util.List<com.android.systemui.controls.ControlsServiceInfo>)
                          (wrap: com.android.systemui.controls.controller.ControlsControllerImpl : 0x000d: IGET  (r2v1 com.android.systemui.controls.controller.ControlsControllerImpl A[REMOVE]) = (r2v0 'this' com.android.systemui.controls.controller.ControlsControllerImpl$listingCallback$1 A[IMMUTABLE_TYPE, THIS]) com.android.systemui.controls.controller.ControlsControllerImpl$listingCallback$1.this$0 com.android.systemui.controls.controller.ControlsControllerImpl)
                         call: com.android.systemui.controls.controller.ControlsControllerImpl$listingCallback$1$onServicesUpdated$1.<init>(java.util.List, com.android.systemui.controls.controller.ControlsControllerImpl):void type: CONSTRUCTOR)
                         type: INTERFACE call: java.util.concurrent.Executor.execute(java.lang.Runnable):void in method: com.android.systemui.controls.controller.ControlsControllerImpl$listingCallback$1.onServicesUpdated(java.util.List<com.android.systemui.controls.ControlsServiceInfo>):void, file: classes.dex
                        	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:285)
                        	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:248)
                        	at jadx.core.codegen.RegionGen.makeSimpleBlock(RegionGen.java:105)
                        	at jadx.core.dex.nodes.IBlock.generate(IBlock.java:15)
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
                        Caused by: jadx.core.utils.exceptions.JadxRuntimeException: Expected class to be processed at this point, class: com.android.systemui.controls.controller.ControlsControllerImpl$listingCallback$1$onServicesUpdated$1, state: NOT_LOADED
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
                        	... 15 more
                        */
                    /*
                        this = this;
                        java.lang.String r0 = "serviceInfos"
                        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r3, r0)
                        com.android.systemui.controls.controller.ControlsControllerImpl r0 = r2.this$0
                        com.android.systemui.util.concurrency.DelayableExecutor r0 = com.android.systemui.controls.controller.ControlsControllerImpl.access$getExecutor$p(r0)
                        com.android.systemui.controls.controller.ControlsControllerImpl$listingCallback$1$onServicesUpdated$1 r1 = new com.android.systemui.controls.controller.ControlsControllerImpl$listingCallback$1$onServicesUpdated$1
                        com.android.systemui.controls.controller.ControlsControllerImpl r2 = r2.this$0
                        r1.<init>(r3, r2)
                        r0.execute(r1)
                        return
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.controls.controller.ControlsControllerImpl$listingCallback$1.onServicesUpdated(java.util.List):void");
                }
            };
            this.listingCallback = controlsControllerImpl$listingCallback$1;
            String name = ControlsControllerImpl.class.getName();
            Intrinsics.checkNotNullExpressionValue(name, "javaClass.name");
            dumpManager.registerDumpable(name, this);
            resetFavorites();
            this.userChanging = false;
            broadcastDispatcher.registerReceiver(controlsControllerImpl$userSwitchReceiver$1, new IntentFilter("android.intent.action.USER_SWITCHED"), delayableExecutor, UserHandle.ALL);
            context.registerReceiver(controlsControllerImpl$restoreFinishedReceiver$1, new IntentFilter("com.android.systemui.backup.RESTORE_FINISHED"), "com.android.systemui.permission.SELF", null);
            controlsListingController.addCallback(controlsControllerImpl$listingCallback$1);
        }

        /* compiled from: ControlsControllerImpl.kt */
        /* loaded from: classes.dex */
        public static final class Companion {
            public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
                this();
            }

            private Companion() {
            }
        }

        @Override // com.android.systemui.util.UserAwareController
        public int getCurrentUserId() {
            return this.currentUser.getIdentifier();
        }

        public final AuxiliaryPersistenceWrapper getAuxiliaryPersistenceWrapper$frameworks__base__packages__SystemUI__android_common__SystemUI_core() {
            return this.auxiliaryPersistenceWrapper;
        }

        /* access modifiers changed from: private */
        public final void setValuesForUser(UserHandle userHandle) {
            Log.d("ControlsControllerImpl", Intrinsics.stringPlus("Changing to user: ", userHandle));
            this.currentUser = userHandle;
            UserStructure userStructure = new UserStructure(this.context, userHandle);
            this.userStructure = userStructure;
            ControlsFavoritePersistenceWrapper controlsFavoritePersistenceWrapper = this.persistenceWrapper;
            File file = userStructure.getFile();
            Intrinsics.checkNotNullExpressionValue(file, "userStructure.file");
            controlsFavoritePersistenceWrapper.changeFileAndBackupManager(file, new BackupManager(this.userStructure.getUserContext()));
            AuxiliaryPersistenceWrapper auxiliaryPersistenceWrapper = this.auxiliaryPersistenceWrapper;
            File auxiliaryFile = this.userStructure.getAuxiliaryFile();
            Intrinsics.checkNotNullExpressionValue(auxiliaryFile, "userStructure.auxiliaryFile");
            auxiliaryPersistenceWrapper.changeFile(auxiliaryFile);
            resetFavorites();
            this.bindingController.changeUser(userHandle);
            this.listingController.changeUser(userHandle);
            this.userChanging = false;
        }

        /* access modifiers changed from: private */
        public final void resetFavorites() {
            Favorites favorites = Favorites.INSTANCE;
            favorites.clear();
            favorites.load(this.persistenceWrapper.readFavorites());
        }

        private final boolean confirmAvailability() {
            if (!this.userChanging) {
                return true;
            }
            Log.w("ControlsControllerImpl", "Controls not available while user is changing");
            return false;
        }

        public void loadForComponent(ComponentName componentName, Consumer<ControlsController.LoadData> consumer, Consumer<Runnable> consumer2) {
            Intrinsics.checkNotNullParameter(componentName, "componentName");
            Intrinsics.checkNotNullParameter(consumer, "dataCallback");
            Intrinsics.checkNotNullParameter(consumer2, "cancelWrapper");
            if (!confirmAvailability()) {
                if (this.userChanging) {
                    this.executor.executeDelayed(new Runnable(this, componentName, consumer, consumer2) { // from class: com.android.systemui.controls.controller.ControlsControllerImpl$loadForComponent$1
                        final /* synthetic */ Consumer<Runnable> $cancelWrapper;
                        final /* synthetic */ ComponentName $componentName;
                        final /* synthetic */ Consumer<ControlsController.LoadData> $dataCallback;
                        final /* synthetic */ ControlsControllerImpl this$0;

                        /* access modifiers changed from: package-private */
                        {
                            this.this$0 = r1;
                            this.$componentName = r2;
                            this.$dataCallback = r3;
                            this.$cancelWrapper = r4;
                        }

                        @Override // java.lang.Runnable
                        public final void run() {
                            this.this$0.loadForComponent(this.$componentName, this.$dataCallback, this.$cancelWrapper);
                        }
                    }, 500, TimeUnit.MILLISECONDS);
                }
                consumer.accept(ControlsControllerKt.createLoadDataObject(CollectionsKt__CollectionsKt.emptyList(), CollectionsKt__CollectionsKt.emptyList(), true));
            }
            consumer2.accept(this.bindingController.bindAndLoad(componentName, new ControlsBindingController.LoadCallback(this, componentName, consumer) { // from class: com.android.systemui.controls.controller.ControlsControllerImpl$loadForComponent$2
                final /* synthetic */ ComponentName $componentName;
                final /* synthetic */ Consumer<ControlsController.LoadData> $dataCallback;
                final /* synthetic */ ControlsControllerImpl this$0;

                /* access modifiers changed from: package-private */
                {
                    this.this$0 = r1;
                    this.$componentName = r2;
                    this.$dataCallback = r3;
                }

                /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object] */
                @Override // java.util.function.Consumer
                public /* bridge */ /* synthetic */ void accept(List<? extends Control> list) {
                    accept((List<Control>) list);
                }

                public void accept(List<Control> list) {
                    Intrinsics.checkNotNullParameter(list, "controls");
                    this.this$0.executor.execute(
                    /*  JADX ERROR: Method code generation error
                        jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x0016: INVOKE  
                          (wrap: com.android.systemui.util.concurrency.DelayableExecutor : 0x0007: IGET  (r0v2 com.android.systemui.util.concurrency.DelayableExecutor A[REMOVE]) = 
                          (wrap: com.android.systemui.controls.controller.ControlsControllerImpl : 0x0005: IGET  (r0v1 com.android.systemui.controls.controller.ControlsControllerImpl A[REMOVE]) = 
                          (r4v0 'this' com.android.systemui.controls.controller.ControlsControllerImpl$loadForComponent$2 A[IMMUTABLE_TYPE, THIS])
                         com.android.systemui.controls.controller.ControlsControllerImpl$loadForComponent$2.this$0 com.android.systemui.controls.controller.ControlsControllerImpl)
                         com.android.systemui.controls.controller.ControlsControllerImpl.executor com.android.systemui.util.concurrency.DelayableExecutor)
                          (wrap: com.android.systemui.controls.controller.ControlsControllerImpl$loadForComponent$2$accept$1 : 0x0013: CONSTRUCTOR  (r1v0 com.android.systemui.controls.controller.ControlsControllerImpl$loadForComponent$2$accept$1 A[REMOVE]) = 
                          (wrap: android.content.ComponentName : 0x000d: IGET  (r2v0 android.content.ComponentName A[REMOVE]) = 
                          (r4v0 'this' com.android.systemui.controls.controller.ControlsControllerImpl$loadForComponent$2 A[IMMUTABLE_TYPE, THIS])
                         com.android.systemui.controls.controller.ControlsControllerImpl$loadForComponent$2.$componentName android.content.ComponentName)
                          (r5v0 'list' java.util.List<android.service.controls.Control>)
                          (wrap: com.android.systemui.controls.controller.ControlsControllerImpl : 0x000f: IGET  (r3v0 com.android.systemui.controls.controller.ControlsControllerImpl A[REMOVE]) = 
                          (r4v0 'this' com.android.systemui.controls.controller.ControlsControllerImpl$loadForComponent$2 A[IMMUTABLE_TYPE, THIS])
                         com.android.systemui.controls.controller.ControlsControllerImpl$loadForComponent$2.this$0 com.android.systemui.controls.controller.ControlsControllerImpl)
                          (wrap: java.util.function.Consumer<com.android.systemui.controls.controller.ControlsController$LoadData> : 0x0011: IGET  (r4v1 java.util.function.Consumer<com.android.systemui.controls.controller.ControlsController$LoadData> A[REMOVE]) = 
                          (r4v0 'this' com.android.systemui.controls.controller.ControlsControllerImpl$loadForComponent$2 A[IMMUTABLE_TYPE, THIS])
                         com.android.systemui.controls.controller.ControlsControllerImpl$loadForComponent$2.$dataCallback java.util.function.Consumer)
                         call: com.android.systemui.controls.controller.ControlsControllerImpl$loadForComponent$2$accept$1.<init>(android.content.ComponentName, java.util.List, com.android.systemui.controls.controller.ControlsControllerImpl, java.util.function.Consumer):void type: CONSTRUCTOR)
                         type: INTERFACE call: java.util.concurrent.Executor.execute(java.lang.Runnable):void in method: com.android.systemui.controls.controller.ControlsControllerImpl$loadForComponent$2.accept(java.util.List<android.service.controls.Control>):void, file: classes.dex
                        	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:285)
                        	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:248)
                        	at jadx.core.codegen.RegionGen.makeSimpleBlock(RegionGen.java:105)
                        	at jadx.core.dex.nodes.IBlock.generate(IBlock.java:15)
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
                        Caused by: jadx.core.utils.exceptions.JadxRuntimeException: Expected class to be processed at this point, class: com.android.systemui.controls.controller.ControlsControllerImpl$loadForComponent$2$accept$1, state: NOT_LOADED
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
                        	... 15 more
                        */
                    /*
                        this = this;
                        java.lang.String r0 = "controls"
                        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r5, r0)
                        com.android.systemui.controls.controller.ControlsControllerImpl r0 = r4.this$0
                        com.android.systemui.util.concurrency.DelayableExecutor r0 = com.android.systemui.controls.controller.ControlsControllerImpl.access$getExecutor$p(r0)
                        com.android.systemui.controls.controller.ControlsControllerImpl$loadForComponent$2$accept$1 r1 = new com.android.systemui.controls.controller.ControlsControllerImpl$loadForComponent$2$accept$1
                        android.content.ComponentName r2 = r4.$componentName
                        com.android.systemui.controls.controller.ControlsControllerImpl r3 = r4.this$0
                        java.util.function.Consumer<com.android.systemui.controls.controller.ControlsController$LoadData> r4 = r4.$dataCallback
                        r1.<init>(r2, r5, r3, r4)
                        r0.execute(r1)
                        return
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.controls.controller.ControlsControllerImpl$loadForComponent$2.accept(java.util.List):void");
                }

                @Override // com.android.systemui.controls.controller.ControlsBindingController.LoadCallback
                public void error(String str) {
                    Intrinsics.checkNotNullParameter(str, "message");
                    this.this$0.executor.execute(
                    /*  JADX ERROR: Method code generation error
                        jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x0016: INVOKE  
                          (wrap: com.android.systemui.util.concurrency.DelayableExecutor : 0x0007: IGET  (r4v2 com.android.systemui.util.concurrency.DelayableExecutor A[REMOVE]) = 
                          (wrap: com.android.systemui.controls.controller.ControlsControllerImpl : 0x0005: IGET  (r4v1 com.android.systemui.controls.controller.ControlsControllerImpl A[REMOVE]) = 
                          (r3v0 'this' com.android.systemui.controls.controller.ControlsControllerImpl$loadForComponent$2 A[IMMUTABLE_TYPE, THIS])
                         com.android.systemui.controls.controller.ControlsControllerImpl$loadForComponent$2.this$0 com.android.systemui.controls.controller.ControlsControllerImpl)
                         com.android.systemui.controls.controller.ControlsControllerImpl.executor com.android.systemui.util.concurrency.DelayableExecutor)
                          (wrap: com.android.systemui.controls.controller.ControlsControllerImpl$loadForComponent$2$error$1 : 0x0013: CONSTRUCTOR  (r0v1 com.android.systemui.controls.controller.ControlsControllerImpl$loadForComponent$2$error$1 A[REMOVE]) = 
                          (wrap: android.content.ComponentName : 0x000d: IGET  (r1v0 android.content.ComponentName A[REMOVE]) = 
                          (r3v0 'this' com.android.systemui.controls.controller.ControlsControllerImpl$loadForComponent$2 A[IMMUTABLE_TYPE, THIS])
                         com.android.systemui.controls.controller.ControlsControllerImpl$loadForComponent$2.$componentName android.content.ComponentName)
                          (wrap: java.util.function.Consumer<com.android.systemui.controls.controller.ControlsController$LoadData> : 0x000f: IGET  (r2v0 java.util.function.Consumer<com.android.systemui.controls.controller.ControlsController$LoadData> A[REMOVE]) = 
                          (r3v0 'this' com.android.systemui.controls.controller.ControlsControllerImpl$loadForComponent$2 A[IMMUTABLE_TYPE, THIS])
                         com.android.systemui.controls.controller.ControlsControllerImpl$loadForComponent$2.$dataCallback java.util.function.Consumer)
                          (wrap: com.android.systemui.controls.controller.ControlsControllerImpl : 0x0011: IGET  (r3v1 com.android.systemui.controls.controller.ControlsControllerImpl A[REMOVE]) = 
                          (r3v0 'this' com.android.systemui.controls.controller.ControlsControllerImpl$loadForComponent$2 A[IMMUTABLE_TYPE, THIS])
                         com.android.systemui.controls.controller.ControlsControllerImpl$loadForComponent$2.this$0 com.android.systemui.controls.controller.ControlsControllerImpl)
                         call: com.android.systemui.controls.controller.ControlsControllerImpl$loadForComponent$2$error$1.<init>(android.content.ComponentName, java.util.function.Consumer, com.android.systemui.controls.controller.ControlsControllerImpl):void type: CONSTRUCTOR)
                         type: INTERFACE call: java.util.concurrent.Executor.execute(java.lang.Runnable):void in method: com.android.systemui.controls.controller.ControlsControllerImpl$loadForComponent$2.error(java.lang.String):void, file: classes.dex
                        	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:285)
                        	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:248)
                        	at jadx.core.codegen.RegionGen.makeSimpleBlock(RegionGen.java:105)
                        	at jadx.core.dex.nodes.IBlock.generate(IBlock.java:15)
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
                        Caused by: jadx.core.utils.exceptions.JadxRuntimeException: Expected class to be processed at this point, class: com.android.systemui.controls.controller.ControlsControllerImpl$loadForComponent$2$error$1, state: NOT_LOADED
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
                        	... 15 more
                        */
                    /*
                        this = this;
                        java.lang.String r0 = "message"
                        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r4, r0)
                        com.android.systemui.controls.controller.ControlsControllerImpl r4 = r3.this$0
                        com.android.systemui.util.concurrency.DelayableExecutor r4 = com.android.systemui.controls.controller.ControlsControllerImpl.access$getExecutor$p(r4)
                        com.android.systemui.controls.controller.ControlsControllerImpl$loadForComponent$2$error$1 r0 = new com.android.systemui.controls.controller.ControlsControllerImpl$loadForComponent$2$error$1
                        android.content.ComponentName r1 = r3.$componentName
                        java.util.function.Consumer<com.android.systemui.controls.controller.ControlsController$LoadData> r2 = r3.$dataCallback
                        com.android.systemui.controls.controller.ControlsControllerImpl r3 = r3.this$0
                        r0.<init>(r1, r2, r3)
                        r4.execute(r0)
                        return
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.controls.controller.ControlsControllerImpl$loadForComponent$2.error(java.lang.String):void");
                }
            }));
        }

        @Override // com.android.systemui.controls.controller.ControlsController
        public boolean addSeedingFavoritesCallback(Consumer<Boolean> consumer) {
            Intrinsics.checkNotNullParameter(consumer, "callback");
            if (!this.seedingInProgress) {
                return false;
            }
            this.executor.execute(new Runnable(this, consumer) { // from class: com.android.systemui.controls.controller.ControlsControllerImpl$addSeedingFavoritesCallback$1
                final /* synthetic */ Consumer<Boolean> $callback;
                final /* synthetic */ ControlsControllerImpl this$0;

                /* access modifiers changed from: package-private */
                {
                    this.this$0 = r1;
                    this.$callback = r2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    if (this.this$0.seedingInProgress) {
                        this.this$0.seedingCallbacks.add(this.$callback);
                    } else {
                        this.$callback.accept(Boolean.FALSE);
                    }
                }
            });
            return true;
        }

        @Override // com.android.systemui.controls.controller.ControlsController
        public void seedFavoritesForComponents(List<ComponentName> list, Consumer<SeedResponse> consumer) {
            Intrinsics.checkNotNullParameter(list, "componentNames");
            Intrinsics.checkNotNullParameter(consumer, "callback");
            if (!this.seedingInProgress && !list.isEmpty()) {
                if (confirmAvailability()) {
                    this.seedingInProgress = true;
                    startSeeding(list, consumer, false);
                } else if (this.userChanging) {
                    this.executor.executeDelayed(new Runnable(this, list, consumer) { // from class: com.android.systemui.controls.controller.ControlsControllerImpl$seedFavoritesForComponents$1
                        final /* synthetic */ Consumer<SeedResponse> $callback;
                        final /* synthetic */ List<ComponentName> $componentNames;
                        final /* synthetic */ ControlsControllerImpl this$0;

                        /* access modifiers changed from: package-private */
                        {
                            this.this$0 = r1;
                            this.$componentNames = r2;
                            this.$callback = r3;
                        }

                        @Override // java.lang.Runnable
                        public final void run() {
                            this.this$0.seedFavoritesForComponents(this.$componentNames, this.$callback);
                        }
                    }, 500, TimeUnit.MILLISECONDS);
                } else {
                    for (ComponentName componentName : list) {
                        String packageName = componentName.getPackageName();
                        Intrinsics.checkNotNullExpressionValue(packageName, "it.packageName");
                        consumer.accept(new SeedResponse(packageName, false));
                    }
                }
            }
        }

        /* access modifiers changed from: private */
        public final void startSeeding(List<ComponentName> list, Consumer<SeedResponse> consumer, boolean z) {
            if (list.isEmpty()) {
                endSeedingCall(!z);
                return;
            }
            ComponentName componentName = list.get(0);
            Log.d("ControlsControllerImpl", Intrinsics.stringPlus("Beginning request to seed favorites for: ", componentName));
            this.bindingController.bindAndLoadSuggested(componentName, new ControlsBindingController.LoadCallback(this, consumer, componentName, CollectionsKt___CollectionsKt.drop(list, 1), z) { // from class: com.android.systemui.controls.controller.ControlsControllerImpl$startSeeding$1
                final /* synthetic */ Consumer<SeedResponse> $callback;
                final /* synthetic */ ComponentName $componentName;
                final /* synthetic */ boolean $didAnyFail;
                final /* synthetic */ List<ComponentName> $remaining;
                final /* synthetic */ ControlsControllerImpl this$0;

                /* access modifiers changed from: package-private */
                {
                    this.this$0 = r1;
                    this.$callback = r2;
                    this.$componentName = r3;
                    this.$remaining = r4;
                    this.$didAnyFail = r5;
                }

                /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object] */
                @Override // java.util.function.Consumer
                public /* bridge */ /* synthetic */ void accept(List<? extends Control> list2) {
                    accept((List<Control>) list2);
                }

                public void accept(List<Control> list2) {
                    Intrinsics.checkNotNullParameter(list2, "controls");
                    this.this$0.executor.execute(
                    /*  JADX ERROR: Method code generation error
                        jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x001c: INVOKE  
                          (wrap: com.android.systemui.util.concurrency.DelayableExecutor : 0x0007: IGET  (r0v2 com.android.systemui.util.concurrency.DelayableExecutor A[REMOVE]) = 
                          (wrap: com.android.systemui.controls.controller.ControlsControllerImpl : 0x0005: IGET  (r0v1 com.android.systemui.controls.controller.ControlsControllerImpl A[REMOVE]) = (r9v0 'this' com.android.systemui.controls.controller.ControlsControllerImpl$startSeeding$1 A[IMMUTABLE_TYPE, THIS]) com.android.systemui.controls.controller.ControlsControllerImpl$startSeeding$1.this$0 com.android.systemui.controls.controller.ControlsControllerImpl)
                         com.android.systemui.controls.controller.ControlsControllerImpl.executor com.android.systemui.util.concurrency.DelayableExecutor)
                          (wrap: com.android.systemui.controls.controller.ControlsControllerImpl$startSeeding$1$accept$1 : 0x0019: CONSTRUCTOR  (r8v0 com.android.systemui.controls.controller.ControlsControllerImpl$startSeeding$1$accept$1 A[REMOVE]) = 
                          (r10v0 'list2' java.util.List<android.service.controls.Control>)
                          (wrap: com.android.systemui.controls.controller.ControlsControllerImpl : 0x000d: IGET  (r3v0 com.android.systemui.controls.controller.ControlsControllerImpl A[REMOVE]) = (r9v0 'this' com.android.systemui.controls.controller.ControlsControllerImpl$startSeeding$1 A[IMMUTABLE_TYPE, THIS]) com.android.systemui.controls.controller.ControlsControllerImpl$startSeeding$1.this$0 com.android.systemui.controls.controller.ControlsControllerImpl)
                          (wrap: java.util.function.Consumer<com.android.systemui.controls.controller.SeedResponse> : 0x000f: IGET  (r4v0 java.util.function.Consumer<com.android.systemui.controls.controller.SeedResponse> A[REMOVE]) = (r9v0 'this' com.android.systemui.controls.controller.ControlsControllerImpl$startSeeding$1 A[IMMUTABLE_TYPE, THIS]) com.android.systemui.controls.controller.ControlsControllerImpl$startSeeding$1.$callback java.util.function.Consumer)
                          (wrap: android.content.ComponentName : 0x0011: IGET  (r5v0 android.content.ComponentName A[REMOVE]) = (r9v0 'this' com.android.systemui.controls.controller.ControlsControllerImpl$startSeeding$1 A[IMMUTABLE_TYPE, THIS]) com.android.systemui.controls.controller.ControlsControllerImpl$startSeeding$1.$componentName android.content.ComponentName)
                          (wrap: java.util.List<android.content.ComponentName> : 0x0013: IGET  (r6v0 java.util.List<android.content.ComponentName> A[REMOVE]) = (r9v0 'this' com.android.systemui.controls.controller.ControlsControllerImpl$startSeeding$1 A[IMMUTABLE_TYPE, THIS]) com.android.systemui.controls.controller.ControlsControllerImpl$startSeeding$1.$remaining java.util.List)
                          (wrap: boolean : 0x0015: IGET  (r7v0 boolean A[REMOVE]) = (r9v0 'this' com.android.systemui.controls.controller.ControlsControllerImpl$startSeeding$1 A[IMMUTABLE_TYPE, THIS]) com.android.systemui.controls.controller.ControlsControllerImpl$startSeeding$1.$didAnyFail boolean)
                         call: com.android.systemui.controls.controller.ControlsControllerImpl$startSeeding$1$accept$1.<init>(java.util.List, com.android.systemui.controls.controller.ControlsControllerImpl, java.util.function.Consumer, android.content.ComponentName, java.util.List, boolean):void type: CONSTRUCTOR)
                         type: INTERFACE call: java.util.concurrent.Executor.execute(java.lang.Runnable):void in method: com.android.systemui.controls.controller.ControlsControllerImpl$startSeeding$1.accept(java.util.List<android.service.controls.Control>):void, file: classes.dex
                        	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:285)
                        	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:248)
                        	at jadx.core.codegen.RegionGen.makeSimpleBlock(RegionGen.java:105)
                        	at jadx.core.dex.nodes.IBlock.generate(IBlock.java:15)
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
                        Caused by: jadx.core.utils.exceptions.JadxRuntimeException: Expected class to be processed at this point, class: com.android.systemui.controls.controller.ControlsControllerImpl$startSeeding$1$accept$1, state: NOT_LOADED
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
                        	... 15 more
                        */
                    /*
                        this = this;
                        java.lang.String r0 = "controls"
                        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r10, r0)
                        com.android.systemui.controls.controller.ControlsControllerImpl r0 = r9.this$0
                        com.android.systemui.util.concurrency.DelayableExecutor r0 = com.android.systemui.controls.controller.ControlsControllerImpl.access$getExecutor$p(r0)
                        com.android.systemui.controls.controller.ControlsControllerImpl$startSeeding$1$accept$1 r8 = new com.android.systemui.controls.controller.ControlsControllerImpl$startSeeding$1$accept$1
                        com.android.systemui.controls.controller.ControlsControllerImpl r3 = r9.this$0
                        java.util.function.Consumer<com.android.systemui.controls.controller.SeedResponse> r4 = r9.$callback
                        android.content.ComponentName r5 = r9.$componentName
                        java.util.List<android.content.ComponentName> r6 = r9.$remaining
                        boolean r7 = r9.$didAnyFail
                        r1 = r8
                        r2 = r10
                        r1.<init>(r2, r3, r4, r5, r6, r7)
                        r0.execute(r8)
                        return
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.controls.controller.ControlsControllerImpl$startSeeding$1.accept(java.util.List):void");
                }

                @Override // com.android.systemui.controls.controller.ControlsBindingController.LoadCallback
                public void error(String str) {
                    Intrinsics.checkNotNullParameter(str, "message");
                    Log.e("ControlsControllerImpl", Intrinsics.stringPlus("Unable to seed favorites: ", str));
                    this.this$0.executor.execute(
                    /*  JADX ERROR: Method code generation error
                        jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x0023: INVOKE  
                          (wrap: com.android.systemui.util.concurrency.DelayableExecutor : 0x0012: IGET  (r5v3 com.android.systemui.util.concurrency.DelayableExecutor A[REMOVE]) = 
                          (wrap: com.android.systemui.controls.controller.ControlsControllerImpl : 0x0010: IGET  (r5v2 com.android.systemui.controls.controller.ControlsControllerImpl A[REMOVE]) = (r4v0 'this' com.android.systemui.controls.controller.ControlsControllerImpl$startSeeding$1 A[IMMUTABLE_TYPE, THIS]) com.android.systemui.controls.controller.ControlsControllerImpl$startSeeding$1.this$0 com.android.systemui.controls.controller.ControlsControllerImpl)
                         com.android.systemui.controls.controller.ControlsControllerImpl.executor com.android.systemui.util.concurrency.DelayableExecutor)
                          (wrap: com.android.systemui.controls.controller.ControlsControllerImpl$startSeeding$1$error$1 : 0x0020: CONSTRUCTOR  (r0v3 com.android.systemui.controls.controller.ControlsControllerImpl$startSeeding$1$error$1 A[REMOVE]) = 
                          (wrap: java.util.function.Consumer<com.android.systemui.controls.controller.SeedResponse> : 0x0018: IGET  (r1v0 java.util.function.Consumer<com.android.systemui.controls.controller.SeedResponse> A[REMOVE]) = (r4v0 'this' com.android.systemui.controls.controller.ControlsControllerImpl$startSeeding$1 A[IMMUTABLE_TYPE, THIS]) com.android.systemui.controls.controller.ControlsControllerImpl$startSeeding$1.$callback java.util.function.Consumer)
                          (wrap: android.content.ComponentName : 0x001a: IGET  (r2v0 android.content.ComponentName A[REMOVE]) = (r4v0 'this' com.android.systemui.controls.controller.ControlsControllerImpl$startSeeding$1 A[IMMUTABLE_TYPE, THIS]) com.android.systemui.controls.controller.ControlsControllerImpl$startSeeding$1.$componentName android.content.ComponentName)
                          (wrap: com.android.systemui.controls.controller.ControlsControllerImpl : 0x001c: IGET  (r3v0 com.android.systemui.controls.controller.ControlsControllerImpl A[REMOVE]) = (r4v0 'this' com.android.systemui.controls.controller.ControlsControllerImpl$startSeeding$1 A[IMMUTABLE_TYPE, THIS]) com.android.systemui.controls.controller.ControlsControllerImpl$startSeeding$1.this$0 com.android.systemui.controls.controller.ControlsControllerImpl)
                          (wrap: java.util.List<android.content.ComponentName> : 0x001e: IGET  (r4v1 java.util.List<android.content.ComponentName> A[REMOVE]) = (r4v0 'this' com.android.systemui.controls.controller.ControlsControllerImpl$startSeeding$1 A[IMMUTABLE_TYPE, THIS]) com.android.systemui.controls.controller.ControlsControllerImpl$startSeeding$1.$remaining java.util.List)
                         call: com.android.systemui.controls.controller.ControlsControllerImpl$startSeeding$1$error$1.<init>(java.util.function.Consumer, android.content.ComponentName, com.android.systemui.controls.controller.ControlsControllerImpl, java.util.List):void type: CONSTRUCTOR)
                         type: INTERFACE call: java.util.concurrent.Executor.execute(java.lang.Runnable):void in method: com.android.systemui.controls.controller.ControlsControllerImpl$startSeeding$1.error(java.lang.String):void, file: classes.dex
                        	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:285)
                        	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:248)
                        	at jadx.core.codegen.RegionGen.makeSimpleBlock(RegionGen.java:105)
                        	at jadx.core.dex.nodes.IBlock.generate(IBlock.java:15)
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
                        Caused by: jadx.core.utils.exceptions.JadxRuntimeException: Expected class to be processed at this point, class: com.android.systemui.controls.controller.ControlsControllerImpl$startSeeding$1$error$1, state: NOT_LOADED
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
                        	... 15 more
                        */
                    /*
                        this = this;
                        java.lang.String r0 = "message"
                        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r5, r0)
                        java.lang.String r0 = "Unable to seed favorites: "
                        java.lang.String r5 = kotlin.jvm.internal.Intrinsics.stringPlus(r0, r5)
                        java.lang.String r0 = "ControlsControllerImpl"
                        android.util.Log.e(r0, r5)
                        com.android.systemui.controls.controller.ControlsControllerImpl r5 = r4.this$0
                        com.android.systemui.util.concurrency.DelayableExecutor r5 = com.android.systemui.controls.controller.ControlsControllerImpl.access$getExecutor$p(r5)
                        com.android.systemui.controls.controller.ControlsControllerImpl$startSeeding$1$error$1 r0 = new com.android.systemui.controls.controller.ControlsControllerImpl$startSeeding$1$error$1
                        java.util.function.Consumer<com.android.systemui.controls.controller.SeedResponse> r1 = r4.$callback
                        android.content.ComponentName r2 = r4.$componentName
                        com.android.systemui.controls.controller.ControlsControllerImpl r3 = r4.this$0
                        java.util.List<android.content.ComponentName> r4 = r4.$remaining
                        r0.<init>(r1, r2, r3, r4)
                        r5.execute(r0)
                        return
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.controls.controller.ControlsControllerImpl$startSeeding$1.error(java.lang.String):void");
                }
            });
        }

        private final void endSeedingCall(boolean z) {
            this.seedingInProgress = false;
            Iterator<T> it = this.seedingCallbacks.iterator();
            while (it.hasNext()) {
                ((Consumer) it.next()).accept(Boolean.valueOf(z));
            }
            this.seedingCallbacks.clear();
        }

        /* access modifiers changed from: package-private */
        public static /* synthetic */ ControlStatus createRemovedStatus$default(ControlsControllerImpl controlsControllerImpl, ComponentName componentName, ControlInfo controlInfo, CharSequence charSequence, boolean z, int i, Object obj) {
            if ((i & 8) != 0) {
                z = true;
            }
            return controlsControllerImpl.createRemovedStatus(componentName, controlInfo, charSequence, z);
        }

        /* access modifiers changed from: private */
        public final ControlStatus createRemovedStatus(ComponentName componentName, ControlInfo controlInfo, CharSequence charSequence, boolean z) {
            Intent intent = new Intent("android.intent.action.MAIN");
            intent.addCategory("android.intent.category.LAUNCHER");
            intent.setPackage(componentName.getPackageName());
            Control build = new Control.StatelessBuilder(controlInfo.getControlId(), PendingIntent.getActivity(this.context, componentName.hashCode(), intent, 67108864)).setTitle(controlInfo.getControlTitle()).setSubtitle(controlInfo.getControlSubtitle()).setStructure(charSequence).setDeviceType(controlInfo.getDeviceType()).build();
            Intrinsics.checkNotNullExpressionValue(build, "control");
            return new ControlStatus(build, componentName, true, z);
        }

        @Override // com.android.systemui.controls.controller.ControlsController
        public void subscribeToFavorites(StructureInfo structureInfo) {
            Intrinsics.checkNotNullParameter(structureInfo, "structureInfo");
            if (confirmAvailability()) {
                this.bindingController.subscribe(structureInfo);
            }
        }

        @Override // com.android.systemui.controls.controller.ControlsController
        public void unsubscribe() {
            if (confirmAvailability()) {
                this.bindingController.unsubscribe();
            }
        }

        @Override // com.android.systemui.controls.controller.ControlsController
        public void addFavorite(ComponentName componentName, CharSequence charSequence, ControlInfo controlInfo) {
            Intrinsics.checkNotNullParameter(componentName, "componentName");
            Intrinsics.checkNotNullParameter(charSequence, "structureName");
            Intrinsics.checkNotNullParameter(controlInfo, "controlInfo");
            if (confirmAvailability()) {
                this.executor.execute(new Runnable(componentName, charSequence, controlInfo, this) { // from class: com.android.systemui.controls.controller.ControlsControllerImpl$addFavorite$1
                    final /* synthetic */ ComponentName $componentName;
                    final /* synthetic */ ControlInfo $controlInfo;
                    final /* synthetic */ CharSequence $structureName;
                    final /* synthetic */ ControlsControllerImpl this$0;

                    /* access modifiers changed from: package-private */
                    {
                        this.$componentName = r1;
                        this.$structureName = r2;
                        this.$controlInfo = r3;
                        this.this$0 = r4;
                    }

                    @Override // java.lang.Runnable
                    public final void run() {
                        Favorites favorites = Favorites.INSTANCE;
                        if (favorites.addFavorite(this.$componentName, this.$structureName, this.$controlInfo)) {
                            this.this$0.persistenceWrapper.storeFavorites(favorites.getAllStructures());
                        }
                    }
                });
            }
        }

        public void replaceFavoritesForStructure(StructureInfo structureInfo) {
            Intrinsics.checkNotNullParameter(structureInfo, "structureInfo");
            if (confirmAvailability()) {
                this.executor.execute(new Runnable(structureInfo, this) { // from class: com.android.systemui.controls.controller.ControlsControllerImpl$replaceFavoritesForStructure$1
                    final /* synthetic */ StructureInfo $structureInfo;
                    final /* synthetic */ ControlsControllerImpl this$0;

                    /* access modifiers changed from: package-private */
                    {
                        this.$structureInfo = r1;
                        this.this$0 = r2;
                    }

                    @Override // java.lang.Runnable
                    public final void run() {
                        Favorites favorites = Favorites.INSTANCE;
                        favorites.replaceControls(this.$structureInfo);
                        this.this$0.persistenceWrapper.storeFavorites(favorites.getAllStructures());
                    }
                });
            }
        }

        @Override // com.android.systemui.controls.controller.ControlsController
        public void refreshStatus(ComponentName componentName, Control control) {
            Intrinsics.checkNotNullParameter(componentName, "componentName");
            Intrinsics.checkNotNullParameter(control, "control");
            if (!confirmAvailability()) {
                Log.d("ControlsControllerImpl", "Controls not available");
                return;
            }
            if (control.getStatus() == 1) {
                this.executor.execute(new Runnable(componentName, control, this) { // from class: com.android.systemui.controls.controller.ControlsControllerImpl$refreshStatus$1
                    final /* synthetic */ ComponentName $componentName;
                    final /* synthetic */ Control $control;
                    final /* synthetic */ ControlsControllerImpl this$0;

                    /* access modifiers changed from: package-private */
                    {
                        this.$componentName = r1;
                        this.$control = r2;
                        this.this$0 = r3;
                    }

                    @Override // java.lang.Runnable
                    public final void run() {
                        Favorites favorites = Favorites.INSTANCE;
                        if (favorites.updateControls(this.$componentName, CollectionsKt__CollectionsJVMKt.listOf(this.$control))) {
                            this.this$0.persistenceWrapper.storeFavorites(favorites.getAllStructures());
                        }
                    }
                });
            }
            this.uiController.onRefreshState(componentName, CollectionsKt__CollectionsJVMKt.listOf(control));
        }

        @Override // com.android.systemui.controls.controller.ControlsController
        public void onActionResponse(ComponentName componentName, String str, int i) {
            Intrinsics.checkNotNullParameter(componentName, "componentName");
            Intrinsics.checkNotNullParameter(str, "controlId");
            if (confirmAvailability()) {
                this.uiController.onActionResponse(componentName, str, i);
            }
        }

        @Override // com.android.systemui.controls.controller.ControlsController
        public void action(ComponentName componentName, ControlInfo controlInfo, ControlAction controlAction) {
            Intrinsics.checkNotNullParameter(componentName, "componentName");
            Intrinsics.checkNotNullParameter(controlInfo, "controlInfo");
            Intrinsics.checkNotNullParameter(controlAction, "action");
            if (confirmAvailability()) {
                this.bindingController.action(componentName, controlInfo, controlAction);
            }
        }

        @Override // com.android.systemui.controls.controller.ControlsController
        public List<StructureInfo> getFavorites() {
            return Favorites.INSTANCE.getAllStructures();
        }

        @Override // com.android.systemui.controls.controller.ControlsController
        public int countFavoritesForComponent(ComponentName componentName) {
            Intrinsics.checkNotNullParameter(componentName, "componentName");
            return Favorites.INSTANCE.getControlsForComponent(componentName).size();
        }

        @Override // com.android.systemui.controls.controller.ControlsController
        public List<StructureInfo> getFavoritesForComponent(ComponentName componentName) {
            Intrinsics.checkNotNullParameter(componentName, "componentName");
            return Favorites.INSTANCE.getStructuresForComponent(componentName);
        }

        public List<ControlInfo> getFavoritesForStructure(ComponentName componentName, CharSequence charSequence) {
            Intrinsics.checkNotNullParameter(componentName, "componentName");
            Intrinsics.checkNotNullParameter(charSequence, "structureName");
            return Favorites.INSTANCE.getControlsForStructure(new StructureInfo(componentName, charSequence, CollectionsKt__CollectionsKt.emptyList()));
        }

        @Override // com.android.systemui.controls.controller.ControlsController
        public StructureInfo getPreferredStructure() {
            return this.uiController.getPreferredStructure(getFavorites());
        }

        @Override // com.android.systemui.Dumpable
        public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
            Intrinsics.checkNotNullParameter(fileDescriptor, "fd");
            Intrinsics.checkNotNullParameter(printWriter, "pw");
            Intrinsics.checkNotNullParameter(strArr, "args");
            printWriter.println("ControlsController state:");
            printWriter.println(Intrinsics.stringPlus("  Changing users: ", Boolean.valueOf(this.userChanging)));
            printWriter.println(Intrinsics.stringPlus("  Current user: ", Integer.valueOf(this.currentUser.getIdentifier())));
            printWriter.println("  Favorites:");
            for (StructureInfo structureInfo : Favorites.INSTANCE.getAllStructures()) {
                printWriter.println(Intrinsics.stringPlus("    ", structureInfo));
                for (ControlInfo controlInfo : structureInfo.getControls()) {
                    printWriter.println(Intrinsics.stringPlus("      ", controlInfo));
                }
            }
            printWriter.println(this.bindingController.toString());
        }

        /* access modifiers changed from: private */
        public final Set<String> findRemoved(Set<String> set, List<Control> list) {
            ArrayList arrayList = new ArrayList(CollectionsKt__IterablesKt.collectionSizeOrDefault(list, 10));
            for (Control control : list) {
                arrayList.add(control.getControlId());
            }
            return SetsKt___SetsKt.minus(set, arrayList);
        }
    }
