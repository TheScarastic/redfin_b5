package com.android.wm.shell.bubbles;

import android.content.Context;
import android.provider.Settings;
import java.util.Iterator;
import java.util.List;
/* loaded from: classes2.dex */
public class BubbleDebugConfig {
    /* access modifiers changed from: package-private */
    public static boolean forceShowUserEducation(Context context) {
        return Settings.Secure.getInt(context.getContentResolver(), "force_show_bubbles_user_education", 0) != 0;
    }

    /* access modifiers changed from: package-private */
    public static String formatBubblesString(List<Bubble> list, BubbleViewProvider bubbleViewProvider) {
        StringBuilder sb = new StringBuilder();
        Iterator<Bubble> it = list.iterator();
        while (it.hasNext()) {
            Bubble next = it.next();
            if (next == null) {
                sb.append("   <null> !!!!!\n");
            } else {
                sb.append(String.format("%s Bubble{act=%12d, showInShade=%d, key=%s}\n", ((bubbleViewProvider == null || bubbleViewProvider.getKey() == "Overflow" || next != bubbleViewProvider) ? null : 1) != null ? "=>" : "  ", Long.valueOf(next.getLastActivity()), Integer.valueOf(next.showInShade() ? 1 : 0), next.getKey()));
            }
        }
        return sb.toString();
    }
}
