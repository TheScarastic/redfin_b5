package com.android.systemui.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.HandlerExecutor;
import android.os.Looper;
import android.os.UserHandle;
import android.text.TextUtils;
import android.util.IndentingPrintWriter;
import android.util.SparseArray;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.Dumpable;
import com.android.systemui.broadcast.logging.BroadcastDispatcherLogger;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.settings.UserTracker;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.concurrent.Executor;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: BroadcastDispatcher.kt */
/* loaded from: classes.dex */
public class BroadcastDispatcher implements Dumpable {
    private final Executor bgExecutor;
    private final Looper bgLooper;
    private final Context context;
    private final DumpManager dumpManager;
    private final BroadcastDispatcher$handler$1 handler;
    private final BroadcastDispatcherLogger logger;
    private final SparseArray<UserBroadcastDispatcher> receiversByUser = new SparseArray<>(20);
    private final UserTracker userTracker;

    public final void registerReceiver(BroadcastReceiver broadcastReceiver, IntentFilter intentFilter) {
        Intrinsics.checkNotNullParameter(broadcastReceiver, "receiver");
        Intrinsics.checkNotNullParameter(intentFilter, "filter");
        registerReceiver$default(this, broadcastReceiver, intentFilter, null, null, 12, null);
    }

    public final void registerReceiver(BroadcastReceiver broadcastReceiver, IntentFilter intentFilter, Executor executor) {
        Intrinsics.checkNotNullParameter(broadcastReceiver, "receiver");
        Intrinsics.checkNotNullParameter(intentFilter, "filter");
        registerReceiver$default(this, broadcastReceiver, intentFilter, executor, null, 8, null);
    }

    public final void registerReceiverWithHandler(BroadcastReceiver broadcastReceiver, IntentFilter intentFilter, Handler handler) {
        Intrinsics.checkNotNullParameter(broadcastReceiver, "receiver");
        Intrinsics.checkNotNullParameter(intentFilter, "filter");
        Intrinsics.checkNotNullParameter(handler, "handler");
        registerReceiverWithHandler$default(this, broadcastReceiver, intentFilter, handler, null, 8, null);
    }

    public BroadcastDispatcher(Context context, Looper looper, Executor executor, DumpManager dumpManager, BroadcastDispatcherLogger broadcastDispatcherLogger, UserTracker userTracker) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(looper, "bgLooper");
        Intrinsics.checkNotNullParameter(executor, "bgExecutor");
        Intrinsics.checkNotNullParameter(dumpManager, "dumpManager");
        Intrinsics.checkNotNullParameter(broadcastDispatcherLogger, "logger");
        Intrinsics.checkNotNullParameter(userTracker, "userTracker");
        this.context = context;
        this.bgLooper = looper;
        this.bgExecutor = executor;
        this.dumpManager = dumpManager;
        this.logger = broadcastDispatcherLogger;
        this.userTracker = userTracker;
        this.handler = new BroadcastDispatcher$handler$1(this, looper);
    }

    public final void initialize() {
        DumpManager dumpManager = this.dumpManager;
        String name = BroadcastDispatcher.class.getName();
        Intrinsics.checkNotNullExpressionValue(name, "javaClass.name");
        dumpManager.registerDumpable(name, this);
    }

    public static /* synthetic */ void registerReceiverWithHandler$default(BroadcastDispatcher broadcastDispatcher, BroadcastReceiver broadcastReceiver, IntentFilter intentFilter, Handler handler, UserHandle userHandle, int i, Object obj) {
        if (obj == null) {
            if ((i & 8) != 0) {
                userHandle = broadcastDispatcher.context.getUser();
                Intrinsics.checkNotNullExpressionValue(userHandle, "fun registerReceiverWithHandler(\n        receiver: BroadcastReceiver,\n        filter: IntentFilter,\n        handler: Handler,\n        user: UserHandle = context.user\n    ) {\n        registerReceiver(receiver, filter, HandlerExecutor(handler), user)\n    }");
            }
            broadcastDispatcher.registerReceiverWithHandler(broadcastReceiver, intentFilter, handler, userHandle);
            return;
        }
        throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: registerReceiverWithHandler");
    }

    public void registerReceiverWithHandler(BroadcastReceiver broadcastReceiver, IntentFilter intentFilter, Handler handler, UserHandle userHandle) {
        Intrinsics.checkNotNullParameter(broadcastReceiver, "receiver");
        Intrinsics.checkNotNullParameter(intentFilter, "filter");
        Intrinsics.checkNotNullParameter(handler, "handler");
        Intrinsics.checkNotNullParameter(userHandle, "user");
        registerReceiver(broadcastReceiver, intentFilter, new HandlerExecutor(handler), userHandle);
    }

    public static /* synthetic */ void registerReceiver$default(BroadcastDispatcher broadcastDispatcher, BroadcastReceiver broadcastReceiver, IntentFilter intentFilter, Executor executor, UserHandle userHandle, int i, Object obj) {
        if (obj == null) {
            if ((i & 4) != 0) {
                executor = null;
            }
            if ((i & 8) != 0) {
                userHandle = null;
            }
            broadcastDispatcher.registerReceiver(broadcastReceiver, intentFilter, executor, userHandle);
            return;
        }
        throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: registerReceiver");
    }

    public void registerReceiver(BroadcastReceiver broadcastReceiver, IntentFilter intentFilter, Executor executor, UserHandle userHandle) {
        Intrinsics.checkNotNullParameter(broadcastReceiver, "receiver");
        Intrinsics.checkNotNullParameter(intentFilter, "filter");
        checkFilter(intentFilter);
        BroadcastDispatcher$handler$1 broadcastDispatcher$handler$1 = this.handler;
        if (executor == null) {
            executor = this.context.getMainExecutor();
        }
        Intrinsics.checkNotNullExpressionValue(executor, "executor ?: context.mainExecutor");
        if (userHandle == null) {
            userHandle = this.context.getUser();
        }
        Intrinsics.checkNotNullExpressionValue(userHandle, "user ?: context.user");
        broadcastDispatcher$handler$1.obtainMessage(0, new ReceiverData(broadcastReceiver, intentFilter, executor, userHandle)).sendToTarget();
    }

    private final void checkFilter(IntentFilter intentFilter) {
        StringBuilder sb = new StringBuilder();
        if (intentFilter.countActions() == 0) {
            sb.append("Filter must contain at least one action. ");
        }
        if (intentFilter.countDataAuthorities() != 0) {
            sb.append("Filter cannot contain DataAuthorities. ");
        }
        if (intentFilter.countDataPaths() != 0) {
            sb.append("Filter cannot contain DataPaths. ");
        }
        if (intentFilter.countDataSchemes() != 0) {
            sb.append("Filter cannot contain DataSchemes. ");
        }
        if (intentFilter.countDataTypes() != 0) {
            sb.append("Filter cannot contain DataTypes. ");
        }
        if (intentFilter.getPriority() != 0) {
            sb.append("Filter cannot modify priority. ");
        }
        if (!TextUtils.isEmpty(sb)) {
            throw new IllegalArgumentException(sb.toString());
        }
    }

    public void unregisterReceiver(BroadcastReceiver broadcastReceiver) {
        Intrinsics.checkNotNullParameter(broadcastReceiver, "receiver");
        this.handler.obtainMessage(1, broadcastReceiver).sendToTarget();
    }

    /* access modifiers changed from: protected */
    @VisibleForTesting
    public UserBroadcastDispatcher createUBRForUser(int i) {
        return new UserBroadcastDispatcher(this.context, i, this.bgLooper, this.bgExecutor, this.logger);
    }

    @Override // com.android.systemui.Dumpable
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        Intrinsics.checkNotNullParameter(fileDescriptor, "fd");
        Intrinsics.checkNotNullParameter(printWriter, "pw");
        Intrinsics.checkNotNullParameter(strArr, "args");
        printWriter.println("Broadcast dispatcher:");
        PrintWriter indentingPrintWriter = new IndentingPrintWriter(printWriter, "  ");
        indentingPrintWriter.increaseIndent();
        int size = this.receiversByUser.size();
        if (size > 0) {
            int i = 0;
            while (true) {
                int i2 = i + 1;
                indentingPrintWriter.println(Intrinsics.stringPlus("User ", Integer.valueOf(this.receiversByUser.keyAt(i))));
                this.receiversByUser.valueAt(i).dump(fileDescriptor, indentingPrintWriter, strArr);
                if (i2 >= size) {
                    break;
                }
                i = i2;
            }
        }
        indentingPrintWriter.decreaseIndent();
    }
}
