package com.android.systemui.privacy.logging;

import android.permission.PermGroupUsage;
import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessageImpl;
import com.android.systemui.privacy.PrivacyDialog;
import com.android.systemui.privacy.PrivacyItem;
import java.util.List;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: PrivacyLogger.kt */
/* loaded from: classes.dex */
public final class PrivacyLogger {
    private final LogBuffer buffer;

    public PrivacyLogger(LogBuffer logBuffer) {
        Intrinsics.checkNotNullParameter(logBuffer, "buffer");
        this.buffer = logBuffer;
    }

    public final void logUpdatedItemFromAppOps(int i, int i2, String str, boolean z) {
        Intrinsics.checkNotNullParameter(str, "packageName");
        LogLevel logLevel = LogLevel.INFO;
        PrivacyLogger$logUpdatedItemFromAppOps$2 privacyLogger$logUpdatedItemFromAppOps$2 = PrivacyLogger$logUpdatedItemFromAppOps$2.INSTANCE;
        LogBuffer logBuffer = this.buffer;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("PrivacyLog", logLevel, privacyLogger$logUpdatedItemFromAppOps$2);
            obtain.setInt1(i);
            obtain.setInt2(i2);
            obtain.setStr1(str);
            obtain.setBool1(z);
            logBuffer.push(obtain);
        }
    }

    public final void logRetrievedPrivacyItemsList(List<PrivacyItem> list) {
        Intrinsics.checkNotNullParameter(list, "list");
        LogLevel logLevel = LogLevel.INFO;
        PrivacyLogger$logRetrievedPrivacyItemsList$2 privacyLogger$logRetrievedPrivacyItemsList$2 = PrivacyLogger$logRetrievedPrivacyItemsList$2.INSTANCE;
        LogBuffer logBuffer = this.buffer;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("PrivacyLog", logLevel, privacyLogger$logRetrievedPrivacyItemsList$2);
            obtain.setStr1(listToString(list));
            logBuffer.push(obtain);
        }
    }

    public final void logPrivacyItemsToHold(List<PrivacyItem> list) {
        Intrinsics.checkNotNullParameter(list, "list");
        LogLevel logLevel = LogLevel.DEBUG;
        PrivacyLogger$logPrivacyItemsToHold$2 privacyLogger$logPrivacyItemsToHold$2 = PrivacyLogger$logPrivacyItemsToHold$2.INSTANCE;
        LogBuffer logBuffer = this.buffer;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("PrivacyLog", logLevel, privacyLogger$logPrivacyItemsToHold$2);
            obtain.setStr1(listToString(list));
            logBuffer.push(obtain);
        }
    }

    public final void logPrivacyItemsUpdateScheduled(long j) {
        LogLevel logLevel = LogLevel.INFO;
        PrivacyLogger$logPrivacyItemsUpdateScheduled$2 privacyLogger$logPrivacyItemsUpdateScheduled$2 = PrivacyLogger$logPrivacyItemsUpdateScheduled$2.INSTANCE;
        LogBuffer logBuffer = this.buffer;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("PrivacyLog", logLevel, privacyLogger$logPrivacyItemsUpdateScheduled$2);
            obtain.setStr1(PrivacyLoggerKt.access$getDATE_FORMAT$p().format(Long.valueOf(System.currentTimeMillis() + j)));
            logBuffer.push(obtain);
        }
    }

    public final void logCurrentProfilesChanged(List<Integer> list) {
        Intrinsics.checkNotNullParameter(list, "profiles");
        LogLevel logLevel = LogLevel.INFO;
        PrivacyLogger$logCurrentProfilesChanged$2 privacyLogger$logCurrentProfilesChanged$2 = PrivacyLogger$logCurrentProfilesChanged$2.INSTANCE;
        LogBuffer logBuffer = this.buffer;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("PrivacyLog", logLevel, privacyLogger$logCurrentProfilesChanged$2);
            obtain.setStr1(list.toString());
            logBuffer.push(obtain);
        }
    }

    public final void logChipVisible(boolean z) {
        LogLevel logLevel = LogLevel.INFO;
        PrivacyLogger$logChipVisible$2 privacyLogger$logChipVisible$2 = PrivacyLogger$logChipVisible$2.INSTANCE;
        LogBuffer logBuffer = this.buffer;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("PrivacyLog", logLevel, privacyLogger$logChipVisible$2);
            obtain.setBool1(z);
            logBuffer.push(obtain);
        }
    }

    public final void logStatusBarIconsVisible(boolean z, boolean z2, boolean z3) {
        LogLevel logLevel = LogLevel.INFO;
        PrivacyLogger$logStatusBarIconsVisible$2 privacyLogger$logStatusBarIconsVisible$2 = PrivacyLogger$logStatusBarIconsVisible$2.INSTANCE;
        LogBuffer logBuffer = this.buffer;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("PrivacyLog", logLevel, privacyLogger$logStatusBarIconsVisible$2);
            obtain.setBool1(z);
            obtain.setBool2(z2);
            obtain.setBool3(z3);
            logBuffer.push(obtain);
        }
    }

    public final void logUnfilteredPermGroupUsage(List<PermGroupUsage> list) {
        Intrinsics.checkNotNullParameter(list, "contents");
        LogLevel logLevel = LogLevel.DEBUG;
        PrivacyLogger$logUnfilteredPermGroupUsage$2 privacyLogger$logUnfilteredPermGroupUsage$2 = PrivacyLogger$logUnfilteredPermGroupUsage$2.INSTANCE;
        LogBuffer logBuffer = this.buffer;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("PrivacyLog", logLevel, privacyLogger$logUnfilteredPermGroupUsage$2);
            obtain.setStr1(list.toString());
            logBuffer.push(obtain);
        }
    }

    public final void logShowDialogContents(List<PrivacyDialog.PrivacyElement> list) {
        Intrinsics.checkNotNullParameter(list, "contents");
        LogLevel logLevel = LogLevel.INFO;
        PrivacyLogger$logShowDialogContents$2 privacyLogger$logShowDialogContents$2 = PrivacyLogger$logShowDialogContents$2.INSTANCE;
        LogBuffer logBuffer = this.buffer;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("PrivacyLog", logLevel, privacyLogger$logShowDialogContents$2);
            obtain.setStr1(list.toString());
            logBuffer.push(obtain);
        }
    }

    public final void logPrivacyDialogDismissed() {
        LogLevel logLevel = LogLevel.INFO;
        PrivacyLogger$logPrivacyDialogDismissed$2 privacyLogger$logPrivacyDialogDismissed$2 = PrivacyLogger$logPrivacyDialogDismissed$2.INSTANCE;
        LogBuffer logBuffer = this.buffer;
        if (!logBuffer.getFrozen()) {
            logBuffer.push(logBuffer.obtain("PrivacyLog", logLevel, privacyLogger$logPrivacyDialogDismissed$2));
        }
    }

    public final void logStartSettingsActivityFromDialog(String str, int i) {
        Intrinsics.checkNotNullParameter(str, "packageName");
        LogLevel logLevel = LogLevel.INFO;
        PrivacyLogger$logStartSettingsActivityFromDialog$2 privacyLogger$logStartSettingsActivityFromDialog$2 = PrivacyLogger$logStartSettingsActivityFromDialog$2.INSTANCE;
        LogBuffer logBuffer = this.buffer;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("PrivacyLog", logLevel, privacyLogger$logStartSettingsActivityFromDialog$2);
            obtain.setStr1(str);
            obtain.setInt1(i);
            logBuffer.push(obtain);
        }
    }

    private final String listToString(List<PrivacyItem> list) {
        return CollectionsKt___CollectionsKt.joinToString$default(list, ", ", null, null, 0, null, PrivacyLogger$listToString$1.INSTANCE, 30, null);
    }
}
