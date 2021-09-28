package com.android.systemui.statusbar.commandline;

import android.content.Context;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.FutureTask;
import kotlin.Unit;
import kotlin.collections.ArraysKt___ArraysKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: CommandRegistry.kt */
/* loaded from: classes.dex */
public final class CommandRegistry {
    private final Map<String, CommandWrapper> commandMap = new LinkedHashMap();
    private final Context context;
    private boolean initialized;
    private final Executor mainExecutor;

    public CommandRegistry(Context context, Executor executor) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(executor, "mainExecutor");
        this.context = context;
        this.mainExecutor = executor;
    }

    public final Context getContext() {
        return this.context;
    }

    public final synchronized void registerCommand(String str, Function0<? extends Command> function0, Executor executor) {
        Intrinsics.checkNotNullParameter(str, "name");
        Intrinsics.checkNotNullParameter(function0, "commandFactory");
        Intrinsics.checkNotNullParameter(executor, "executor");
        if (this.commandMap.get(str) == null) {
            this.commandMap.put(str, new CommandWrapper(function0, executor));
        } else {
            throw new IllegalStateException("A command is already registered for (" + str + ')');
        }
    }

    public final synchronized void registerCommand(String str, Function0<? extends Command> function0) {
        Intrinsics.checkNotNullParameter(str, "name");
        Intrinsics.checkNotNullParameter(function0, "commandFactory");
        registerCommand(str, function0, this.mainExecutor);
    }

    public final synchronized void unregisterCommand(String str) {
        Intrinsics.checkNotNullParameter(str, "command");
        this.commandMap.remove(str);
    }

    private final void initializeCommands() {
        this.initialized = true;
        registerCommand("prefs", new Function0<Command>(this) { // from class: com.android.systemui.statusbar.commandline.CommandRegistry$initializeCommands$1
            final /* synthetic */ CommandRegistry this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
            }

            @Override // kotlin.jvm.functions.Function0
            public final Command invoke() {
                return new PrefsCommand(this.this$0.getContext());
            }
        });
    }

    public final void onShellCommand(PrintWriter printWriter, String[] strArr) {
        Intrinsics.checkNotNullParameter(printWriter, "pw");
        Intrinsics.checkNotNullParameter(strArr, "args");
        if (!this.initialized) {
            initializeCommands();
        }
        if (strArr.length == 0) {
            help(printWriter);
            return;
        }
        CommandWrapper commandWrapper = this.commandMap.get(strArr[0]);
        if (commandWrapper == null) {
            help(printWriter);
            return;
        }
        FutureTask futureTask = new FutureTask(new Callable<Unit>(commandWrapper.getCommandFactory().invoke(), printWriter, strArr) { // from class: com.android.systemui.statusbar.commandline.CommandRegistry$onShellCommand$task$1
            final /* synthetic */ String[] $args;
            final /* synthetic */ Command $command;
            final /* synthetic */ PrintWriter $pw;

            /* access modifiers changed from: package-private */
            {
                this.$command = r1;
                this.$pw = r2;
                this.$args = r3;
            }

            @Override // java.util.concurrent.Callable
            public final void call() {
                this.$command.execute(this.$pw, ArraysKt___ArraysKt.drop(this.$args, 1));
            }
        });
        commandWrapper.getExecutor().execute(new Runnable(futureTask) { // from class: com.android.systemui.statusbar.commandline.CommandRegistry$onShellCommand$1
            final /* synthetic */ FutureTask<Unit> $task;

            /* access modifiers changed from: package-private */
            {
                this.$task = r1;
            }

            @Override // java.lang.Runnable
            public final void run() {
                this.$task.run();
            }
        });
        futureTask.get();
    }

    private final void help(PrintWriter printWriter) {
        printWriter.println("Usage: adb shell cmd statusbar <command>");
        printWriter.println("  known commands:");
        for (String str : this.commandMap.keySet()) {
            printWriter.println(Intrinsics.stringPlus("   ", str));
        }
    }
}
