package com.android.systemui.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Looper;
import android.os.UserHandle;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.IndentingPrintWriter;
import android.util.Log;
import com.android.internal.util.Preconditions;
import com.android.systemui.Dumpable;
import com.android.systemui.broadcast.logging.BroadcastDispatcherLogger;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;
import kotlin.Unit;
import kotlin.collections.CollectionsKt__MutableCollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt__SequencesKt;
/* compiled from: UserBroadcastDispatcher.kt */
/* loaded from: classes.dex */
public class UserBroadcastDispatcher implements Dumpable {
    public static final Companion Companion = new Companion(null);
    private static final AtomicInteger index = new AtomicInteger(0);
    private final Executor bgExecutor;
    private final UserBroadcastDispatcher$bgHandler$1 bgHandler;
    private final Looper bgLooper;
    private final Context context;
    private final BroadcastDispatcherLogger logger;
    private final int userId;
    private final ArrayMap<String, ActionReceiver> actionsToActionsReceivers = new ArrayMap<>();
    private final ArrayMap<BroadcastReceiver, Set<String>> receiverToActions = new ArrayMap<>();

    public static /* synthetic */ void getActionsToActionsReceivers$frameworks__base__packages__SystemUI__android_common__SystemUI_core$annotations() {
    }

    @Override // com.android.systemui.Dumpable
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        Intrinsics.checkNotNullParameter(fileDescriptor, "fd");
        Intrinsics.checkNotNullParameter(printWriter, "pw");
        Intrinsics.checkNotNullParameter(strArr, "args");
        boolean z = printWriter instanceof IndentingPrintWriter;
        if (z) {
            ((IndentingPrintWriter) printWriter).increaseIndent();
        }
        for (Map.Entry<String, ActionReceiver> entry : getActionsToActionsReceivers$frameworks__base__packages__SystemUI__android_common__SystemUI_core().entrySet()) {
            printWriter.println(Intrinsics.stringPlus(entry.getKey(), ":"));
            entry.getValue().dump(fileDescriptor, printWriter, strArr);
        }
        if (z) {
            ((IndentingPrintWriter) printWriter).decreaseIndent();
        }
    }

    public UserBroadcastDispatcher(Context context, int i, Looper looper, Executor executor, BroadcastDispatcherLogger broadcastDispatcherLogger) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(looper, "bgLooper");
        Intrinsics.checkNotNullParameter(executor, "bgExecutor");
        Intrinsics.checkNotNullParameter(broadcastDispatcherLogger, "logger");
        this.context = context;
        this.userId = i;
        this.bgLooper = looper;
        this.bgExecutor = executor;
        this.logger = broadcastDispatcherLogger;
        this.bgHandler = new UserBroadcastDispatcher$bgHandler$1(this, looper);
    }

    /* compiled from: UserBroadcastDispatcher.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    public final ArrayMap<String, ActionReceiver> getActionsToActionsReceivers$frameworks__base__packages__SystemUI__android_common__SystemUI_core() {
        return this.actionsToActionsReceivers;
    }

    public final boolean isReceiverReferenceHeld$frameworks__base__packages__SystemUI__android_common__SystemUI_core(BroadcastReceiver broadcastReceiver) {
        boolean z;
        Intrinsics.checkNotNullParameter(broadcastReceiver, "receiver");
        Collection<ActionReceiver> values = this.actionsToActionsReceivers.values();
        Intrinsics.checkNotNullExpressionValue(values, "actionsToActionsReceivers.values");
        if (!values.isEmpty()) {
            for (ActionReceiver actionReceiver : values) {
                if (actionReceiver.hasReceiver(broadcastReceiver)) {
                    z = true;
                    break;
                }
            }
        }
        z = false;
        return z || this.receiverToActions.containsKey(broadcastReceiver);
    }

    public final void registerReceiver(ReceiverData receiverData) {
        Intrinsics.checkNotNullParameter(receiverData, "receiverData");
        this.bgHandler.obtainMessage(0, receiverData).sendToTarget();
    }

    public final void unregisterReceiver(BroadcastReceiver broadcastReceiver) {
        Intrinsics.checkNotNullParameter(broadcastReceiver, "receiver");
        this.bgHandler.obtainMessage(1, broadcastReceiver).sendToTarget();
    }

    /* access modifiers changed from: private */
    public final void handleRegisterReceiver(ReceiverData receiverData) {
        Preconditions.checkState(this.bgHandler.getLooper().isCurrentThread(), "This method should only be called from BG thread");
        ArrayMap<BroadcastReceiver, Set<String>> arrayMap = this.receiverToActions;
        BroadcastReceiver receiver = receiverData.getReceiver();
        Set<String> set = arrayMap.get(receiver);
        if (set == null) {
            set = new ArraySet<>();
            arrayMap.put(receiver, set);
        }
        Set<String> set2 = set;
        Iterator<String> actionsIterator = receiverData.getFilter().actionsIterator();
        Sequence sequence = actionsIterator == null ? null : SequencesKt__SequencesKt.asSequence(actionsIterator);
        if (sequence == null) {
            sequence = SequencesKt__SequencesKt.emptySequence();
        }
        boolean unused = CollectionsKt__MutableCollectionsKt.addAll(set2, sequence);
        Iterator<String> actionsIterator2 = receiverData.getFilter().actionsIterator();
        Intrinsics.checkNotNullExpressionValue(actionsIterator2, "receiverData.filter.actionsIterator()");
        while (actionsIterator2.hasNext()) {
            String next = actionsIterator2.next();
            ArrayMap<String, ActionReceiver> actionsToActionsReceivers$frameworks__base__packages__SystemUI__android_common__SystemUI_core = getActionsToActionsReceivers$frameworks__base__packages__SystemUI__android_common__SystemUI_core();
            ActionReceiver actionReceiver = actionsToActionsReceivers$frameworks__base__packages__SystemUI__android_common__SystemUI_core.get(next);
            if (actionReceiver == null) {
                Intrinsics.checkNotNullExpressionValue(next, "it");
                actionReceiver = createActionReceiver$frameworks__base__packages__SystemUI__android_common__SystemUI_core(next);
                actionsToActionsReceivers$frameworks__base__packages__SystemUI__android_common__SystemUI_core.put(next, actionReceiver);
            }
            actionReceiver.addReceiverData(receiverData);
        }
        this.logger.logReceiverRegistered(this.userId, receiverData.getReceiver());
    }

    public ActionReceiver createActionReceiver$frameworks__base__packages__SystemUI__android_common__SystemUI_core(String str) {
        Intrinsics.checkNotNullParameter(str, "action");
        return new ActionReceiver(str, this.userId, new Function2<BroadcastReceiver, IntentFilter, Unit>(this) { // from class: com.android.systemui.broadcast.UserBroadcastDispatcher$createActionReceiver$1
            final /* synthetic */ UserBroadcastDispatcher this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
            }

            /* Return type fixed from 'java.lang.Object' to match base method */
            /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object, java.lang.Object] */
            @Override // kotlin.jvm.functions.Function2
            public /* bridge */ /* synthetic */ Unit invoke(BroadcastReceiver broadcastReceiver, IntentFilter intentFilter) {
                invoke(broadcastReceiver, intentFilter);
                return Unit.INSTANCE;
            }

            public final void invoke(BroadcastReceiver broadcastReceiver, IntentFilter intentFilter) {
                Intrinsics.checkNotNullParameter(broadcastReceiver, "$this$$receiver");
                Intrinsics.checkNotNullParameter(intentFilter, "it");
                this.this$0.context.registerReceiverAsUser(broadcastReceiver, UserHandle.of(this.this$0.userId), intentFilter, null, this.this$0.bgHandler);
                this.this$0.logger.logContextReceiverRegistered(this.this$0.userId, intentFilter);
            }
        }, new Function1<BroadcastReceiver, Unit>(this, str) { // from class: com.android.systemui.broadcast.UserBroadcastDispatcher$createActionReceiver$2
            final /* synthetic */ String $action;
            final /* synthetic */ UserBroadcastDispatcher this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
                this.$action = r2;
            }

            /* Return type fixed from 'java.lang.Object' to match base method */
            /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object] */
            @Override // kotlin.jvm.functions.Function1
            public /* bridge */ /* synthetic */ Unit invoke(BroadcastReceiver broadcastReceiver) {
                invoke(broadcastReceiver);
                return Unit.INSTANCE;
            }

            public final void invoke(BroadcastReceiver broadcastReceiver) {
                Intrinsics.checkNotNullParameter(broadcastReceiver, "$this$$receiver");
                try {
                    this.this$0.context.unregisterReceiver(broadcastReceiver);
                    this.this$0.logger.logContextReceiverUnregistered(this.this$0.userId, this.$action);
                } catch (IllegalArgumentException e) {
                    Log.e("UserBroadcastDispatcher", "Trying to unregister unregistered receiver for user " + this.this$0.userId + ", action " + this.$action, new IllegalStateException(e));
                }
            }
        }, this.bgExecutor, this.logger);
    }

    /* access modifiers changed from: private */
    public final void handleUnregisterReceiver(BroadcastReceiver broadcastReceiver) {
        Preconditions.checkState(this.bgHandler.getLooper().isCurrentThread(), "This method should only be called from BG thread");
        Set<String> orDefault = this.receiverToActions.getOrDefault(broadcastReceiver, new LinkedHashSet());
        Intrinsics.checkNotNullExpressionValue(orDefault, "receiverToActions.getOrDefault(receiver, mutableSetOf())");
        for (String str : orDefault) {
            ActionReceiver actionReceiver = getActionsToActionsReceivers$frameworks__base__packages__SystemUI__android_common__SystemUI_core().get(str);
            if (actionReceiver != null) {
                actionReceiver.removeReceiver(broadcastReceiver);
            }
        }
        this.receiverToActions.remove(broadcastReceiver);
        this.logger.logReceiverUnregistered(this.userId, broadcastReceiver);
    }
}
