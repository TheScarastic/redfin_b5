package com.android.systemui.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.ArraySet;
import android.util.IndentingPrintWriter;
import com.android.systemui.Dumpable;
import com.android.systemui.broadcast.logging.BroadcastDispatcherLogger;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;
import kotlin.Unit;
import kotlin.collections.CollectionsKt__MutableCollectionsKt;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt__SequencesKt;
/* compiled from: ActionReceiver.kt */
/* loaded from: classes.dex */
public final class ActionReceiver extends BroadcastReceiver implements Dumpable {
    public static final Companion Companion = new Companion(null);
    private static final AtomicInteger index = new AtomicInteger(0);
    private final String action;
    private final Executor bgExecutor;
    private final BroadcastDispatcherLogger logger;
    private final Function2<BroadcastReceiver, IntentFilter, Unit> registerAction;
    private boolean registered;
    private final Function1<BroadcastReceiver, Unit> unregisterAction;
    private final int userId;
    private final ArraySet<ReceiverData> receiverDatas = new ArraySet<>();
    private final ArraySet<String> activeCategories = new ArraySet<>();

    @Override // com.android.systemui.Dumpable
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        Intrinsics.checkNotNullParameter(fileDescriptor, "fd");
        Intrinsics.checkNotNullParameter(printWriter, "pw");
        Intrinsics.checkNotNullParameter(strArr, "args");
        if (printWriter instanceof IndentingPrintWriter) {
            ((IndentingPrintWriter) printWriter).increaseIndent();
        }
        printWriter.println(Intrinsics.stringPlus("Registered: ", Boolean.valueOf(getRegistered())));
        printWriter.println("Receivers:");
        boolean z = printWriter instanceof IndentingPrintWriter;
        if (z) {
            ((IndentingPrintWriter) printWriter).increaseIndent();
        }
        for (ReceiverData receiverData : this.receiverDatas) {
            printWriter.println(receiverData.getReceiver());
        }
        if (z) {
            ((IndentingPrintWriter) printWriter).decreaseIndent();
        }
        printWriter.println(Intrinsics.stringPlus("Categories: ", CollectionsKt___CollectionsKt.joinToString$default(this.activeCategories, ", ", null, null, 0, null, null, 62, null)));
        if (z) {
            ((IndentingPrintWriter) printWriter).decreaseIndent();
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for r4v0, resolved type: kotlin.jvm.functions.Function2<? super android.content.BroadcastReceiver, ? super android.content.IntentFilter, kotlin.Unit> */
    /* JADX DEBUG: Multi-variable search result rejected for r5v0, resolved type: kotlin.jvm.functions.Function1<? super android.content.BroadcastReceiver, kotlin.Unit> */
    /* JADX WARN: Multi-variable type inference failed */
    public ActionReceiver(String str, int i, Function2<? super BroadcastReceiver, ? super IntentFilter, Unit> function2, Function1<? super BroadcastReceiver, Unit> function1, Executor executor, BroadcastDispatcherLogger broadcastDispatcherLogger) {
        Intrinsics.checkNotNullParameter(str, "action");
        Intrinsics.checkNotNullParameter(function2, "registerAction");
        Intrinsics.checkNotNullParameter(function1, "unregisterAction");
        Intrinsics.checkNotNullParameter(executor, "bgExecutor");
        Intrinsics.checkNotNullParameter(broadcastDispatcherLogger, "logger");
        this.action = str;
        this.userId = i;
        this.registerAction = function2;
        this.unregisterAction = function1;
        this.bgExecutor = executor;
        this.logger = broadcastDispatcherLogger;
    }

    /* compiled from: ActionReceiver.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final AtomicInteger getIndex() {
            return ActionReceiver.index;
        }
    }

    public final boolean getRegistered() {
        return this.registered;
    }

    public final void addReceiverData(ReceiverData receiverData) throws IllegalArgumentException {
        Intrinsics.checkNotNullParameter(receiverData, "receiverData");
        if (receiverData.getFilter().hasAction(this.action)) {
            ArraySet<String> arraySet = this.activeCategories;
            Iterator<String> categoriesIterator = receiverData.getFilter().categoriesIterator();
            Sequence sequence = categoriesIterator == null ? null : SequencesKt__SequencesKt.asSequence(categoriesIterator);
            if (sequence == null) {
                sequence = SequencesKt__SequencesKt.emptySequence();
            }
            boolean z = CollectionsKt__MutableCollectionsKt.addAll(arraySet, sequence);
            if (this.receiverDatas.add(receiverData) && this.receiverDatas.size() == 1) {
                this.registerAction.invoke(this, createFilter());
                this.registered = true;
            } else if (z) {
                this.unregisterAction.invoke(this);
                this.registerAction.invoke(this, createFilter());
            }
        } else {
            throw new IllegalArgumentException("Trying to attach to " + this.action + " without correct action,receiver: " + receiverData.getReceiver());
        }
    }

    public final boolean hasReceiver(BroadcastReceiver broadcastReceiver) {
        Intrinsics.checkNotNullParameter(broadcastReceiver, "receiver");
        ArraySet<ReceiverData> arraySet = this.receiverDatas;
        if ((arraySet instanceof Collection) && arraySet.isEmpty()) {
            return false;
        }
        for (ReceiverData receiverData : arraySet) {
            if (Intrinsics.areEqual(receiverData.getReceiver(), broadcastReceiver)) {
                return true;
            }
        }
        return false;
    }

    private final IntentFilter createFilter() {
        IntentFilter intentFilter = new IntentFilter(this.action);
        for (String str : this.activeCategories) {
            intentFilter.addCategory(str);
        }
        return intentFilter;
    }

    public final void removeReceiver(BroadcastReceiver broadcastReceiver) {
        Intrinsics.checkNotNullParameter(broadcastReceiver, "receiver");
        if ((CollectionsKt__MutableCollectionsKt.removeAll(this.receiverDatas, new Function1<ReceiverData, Boolean>(broadcastReceiver) { // from class: com.android.systemui.broadcast.ActionReceiver$removeReceiver$1
            final /* synthetic */ BroadcastReceiver $receiver;

            /* access modifiers changed from: package-private */
            {
                this.$receiver = r1;
            }

            /* Return type fixed from 'java.lang.Object' to match base method */
            /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object] */
            @Override // kotlin.jvm.functions.Function1
            public /* bridge */ /* synthetic */ Boolean invoke(ReceiverData receiverData) {
                return Boolean.valueOf(invoke(receiverData));
            }

            public final boolean invoke(ReceiverData receiverData) {
                return Intrinsics.areEqual(receiverData.getReceiver(), this.$receiver);
            }
        })) && this.receiverDatas.isEmpty() && this.registered) {
            this.unregisterAction.invoke(this);
            this.registered = false;
            this.activeCategories.clear();
        }
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) throws IllegalStateException {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(intent, "intent");
        if (Intrinsics.areEqual(intent.getAction(), this.action)) {
            int andIncrement = Companion.getIndex().getAndIncrement();
            this.logger.logBroadcastReceived(andIncrement, this.userId, intent);
            this.bgExecutor.execute(new Runnable(this, intent, context, andIncrement) { // from class: com.android.systemui.broadcast.ActionReceiver$onReceive$1
                final /* synthetic */ Context $context;
                final /* synthetic */ int $id;
                final /* synthetic */ Intent $intent;
                final /* synthetic */ ActionReceiver this$0;

                /* access modifiers changed from: package-private */
                {
                    this.this$0 = r1;
                    this.$intent = r2;
                    this.$context = r3;
                    this.$id = r4;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    ArraySet<ReceiverData> access$getReceiverDatas$p = ActionReceiver.access$getReceiverDatas$p(this.this$0);
                    Intent intent2 = this.$intent;
                    ActionReceiver actionReceiver = this.this$0;
                    Context context2 = this.$context;
                    int i = this.$id;
                    for (ReceiverData receiverData : access$getReceiverDatas$p) {
                        if (receiverData.getFilter().matchCategories(intent2.getCategories()) == null) {
                            receiverData.getExecutor().execute(
                            /*  JADX ERROR: Method code generation error
                                jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x003b: INVOKE  
                                  (wrap: java.util.concurrent.Executor : 0x002d: INVOKE  (r10v0 java.util.concurrent.Executor A[REMOVE]) = (r2v1 'receiverData' com.android.systemui.broadcast.ReceiverData) type: VIRTUAL call: com.android.systemui.broadcast.ReceiverData.getExecutor():java.util.concurrent.Executor)
                                  (wrap: com.android.systemui.broadcast.ActionReceiver$onReceive$1$1$1 : 0x0038: CONSTRUCTOR  (r11v0 com.android.systemui.broadcast.ActionReceiver$onReceive$1$1$1 A[REMOVE]) = 
                                  (r2v1 'receiverData' com.android.systemui.broadcast.ReceiverData)
                                  (r8v0 'actionReceiver' com.android.systemui.broadcast.ActionReceiver)
                                  (r9v0 'context2' android.content.Context)
                                  (r7v0 'intent2' android.content.Intent)
                                  (r12v1 'i' int)
                                 call: com.android.systemui.broadcast.ActionReceiver$onReceive$1$1$1.<init>(com.android.systemui.broadcast.ReceiverData, com.android.systemui.broadcast.ActionReceiver, android.content.Context, android.content.Intent, int):void type: CONSTRUCTOR)
                                 type: INTERFACE call: java.util.concurrent.Executor.execute(java.lang.Runnable):void in method: com.android.systemui.broadcast.ActionReceiver$onReceive$1.run():void, file: classes.dex
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
                                	at jadx.core.codegen.RegionGen.makeRegionIndent(RegionGen.java:94)
                                	at jadx.core.codegen.RegionGen.makeLoop(RegionGen.java:221)
                                	at jadx.core.dex.regions.loops.LoopRegion.generate(LoopRegion.java:173)
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
                                Caused by: jadx.core.utils.exceptions.JadxRuntimeException: Expected class to be processed at this point, class: com.android.systemui.broadcast.ActionReceiver$onReceive$1$1$1, state: NOT_LOADED
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
                                	... 27 more
                                */
                            /*
                                this = this;
                                com.android.systemui.broadcast.ActionReceiver r0 = r12.this$0
                                android.util.ArraySet r0 = com.android.systemui.broadcast.ActionReceiver.access$getReceiverDatas$p(r0)
                                android.content.Intent r7 = r12.$intent
                                com.android.systemui.broadcast.ActionReceiver r8 = r12.this$0
                                android.content.Context r9 = r12.$context
                                int r12 = r12.$id
                                java.util.Iterator r0 = r0.iterator()
                            L_0x0012:
                                boolean r1 = r0.hasNext()
                                if (r1 == 0) goto L_0x003f
                                java.lang.Object r1 = r0.next()
                                r2 = r1
                                com.android.systemui.broadcast.ReceiverData r2 = (com.android.systemui.broadcast.ReceiverData) r2
                                android.content.IntentFilter r1 = r2.getFilter()
                                java.util.Set r3 = r7.getCategories()
                                java.lang.String r1 = r1.matchCategories(r3)
                                if (r1 != 0) goto L_0x0012
                                java.util.concurrent.Executor r10 = r2.getExecutor()
                                com.android.systemui.broadcast.ActionReceiver$onReceive$1$1$1 r11 = new com.android.systemui.broadcast.ActionReceiver$onReceive$1$1$1
                                r1 = r11
                                r3 = r8
                                r4 = r9
                                r5 = r7
                                r6 = r12
                                r1.<init>(r2, r3, r4, r5, r6)
                                r10.execute(r11)
                                goto L_0x0012
                            L_0x003f:
                                return
                            */
                            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.broadcast.ActionReceiver$onReceive$1.run():void");
                        }
                    });
                    return;
                }
                throw new IllegalStateException("Received intent for " + ((Object) intent.getAction()) + " in receiver for " + this.action + '}');
            }
        }
