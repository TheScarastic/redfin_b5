package com.android.wm.shell.bubbles;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.LocusId;
import android.content.pm.LauncherApps;
import com.android.wm.shell.bubbles.storage.BubbleEntity;
import com.android.wm.shell.bubbles.storage.BubblePersistentRepository;
import com.android.wm.shell.bubbles.storage.BubbleVolatileRepository;
import com.android.wm.shell.common.ShellExecutor;
import java.util.ArrayList;
import java.util.List;
import kotlin.Unit;
import kotlin.collections.CollectionsKt__CollectionsJVMKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.BuildersKt;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.CoroutineScopeKt;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.Job;
/* compiled from: BubbleDataRepository.kt */
/* loaded from: classes2.dex */
public final class BubbleDataRepository {
    private final CoroutineScope ioScope = CoroutineScopeKt.CoroutineScope(Dispatchers.getIO());
    private Job job;
    private final LauncherApps launcherApps;
    private final ShellExecutor mainExecutor;
    private final BubblePersistentRepository persistentRepository;
    private final BubbleVolatileRepository volatileRepository;

    public BubbleDataRepository(Context context, LauncherApps launcherApps, ShellExecutor shellExecutor) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(launcherApps, "launcherApps");
        Intrinsics.checkNotNullParameter(shellExecutor, "mainExecutor");
        this.launcherApps = launcherApps;
        this.mainExecutor = shellExecutor;
        this.volatileRepository = new BubbleVolatileRepository(launcherApps);
        this.persistentRepository = new BubblePersistentRepository(context);
        Dispatchers dispatchers = Dispatchers.INSTANCE;
    }

    public final void addBubble(int i, Bubble bubble) {
        Intrinsics.checkNotNullParameter(bubble, "bubble");
        addBubbles(i, CollectionsKt__CollectionsJVMKt.listOf(bubble));
    }

    public final void addBubbles(int i, List<? extends Bubble> list) {
        Intrinsics.checkNotNullParameter(list, "bubbles");
        List<BubbleEntity> transform = transform(list);
        this.volatileRepository.addBubbles(i, transform);
        if (!transform.isEmpty()) {
            persistToDisk();
        }
    }

    public final void removeBubbles(int i, List<? extends Bubble> list) {
        Intrinsics.checkNotNullParameter(list, "bubbles");
        List<BubbleEntity> transform = transform(list);
        this.volatileRepository.removeBubbles(i, transform);
        if (!transform.isEmpty()) {
            persistToDisk();
        }
    }

    private final void persistToDisk() {
        this.job = BuildersKt.launch$default(this.ioScope, null, null, new BubbleDataRepository$persistToDisk$1(this.job, this, null), 3, null);
    }

    @SuppressLint({"WrongConstant"})
    public final Job loadBubbles(int i, Function1<? super List<? extends Bubble>, Unit> function1) {
        Intrinsics.checkNotNullParameter(function1, "cb");
        return BuildersKt.launch$default(this.ioScope, null, null, new BubbleDataRepository$loadBubbles$1(this, i, function1, null), 3, null);
    }

    private final List<BubbleEntity> transform(List<? extends Bubble> list) {
        ArrayList arrayList = new ArrayList();
        for (Bubble bubble : list) {
            int identifier = bubble.getUser().getIdentifier();
            String packageName = bubble.getPackageName();
            Intrinsics.checkNotNullExpressionValue(packageName, "b.packageName");
            String metadataShortcutId = bubble.getMetadataShortcutId();
            BubbleEntity bubbleEntity = null;
            if (metadataShortcutId != null) {
                String key = bubble.getKey();
                Intrinsics.checkNotNullExpressionValue(key, "b.key");
                int rawDesiredHeight = bubble.getRawDesiredHeight();
                int rawDesiredHeightResId = bubble.getRawDesiredHeightResId();
                String title = bubble.getTitle();
                int taskId = bubble.getTaskId();
                LocusId locusId = bubble.getLocusId();
                bubbleEntity = new BubbleEntity(identifier, packageName, metadataShortcutId, key, rawDesiredHeight, rawDesiredHeightResId, title, taskId, locusId == null ? null : locusId.getId());
            }
            if (bubbleEntity != null) {
                arrayList.add(bubbleEntity);
            }
        }
        return arrayList;
    }
}
