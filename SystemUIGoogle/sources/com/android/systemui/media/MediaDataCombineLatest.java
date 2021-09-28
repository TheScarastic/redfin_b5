package com.android.systemui.media;

import com.android.systemui.media.MediaDataManager;
import com.android.systemui.media.MediaDeviceManager;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: MediaDataCombineLatest.kt */
/* loaded from: classes.dex */
public final class MediaDataCombineLatest implements MediaDataManager.Listener, MediaDeviceManager.Listener {
    private final Set<MediaDataManager.Listener> listeners = new LinkedHashSet();
    private final Map<String, Pair<MediaData, MediaDeviceData>> entries = new LinkedHashMap();

    @Override // com.android.systemui.media.MediaDataManager.Listener
    public void onMediaDataLoaded(String str, String str2, MediaData mediaData, boolean z, boolean z2) {
        Intrinsics.checkNotNullParameter(str, "key");
        Intrinsics.checkNotNullParameter(mediaData, "data");
        MediaDeviceData mediaDeviceData = null;
        if (str2 == null || Intrinsics.areEqual(str2, str) || !this.entries.containsKey(str2)) {
            Map<String, Pair<MediaData, MediaDeviceData>> map = this.entries;
            Pair<MediaData, MediaDeviceData> pair = map.get(str);
            if (pair != null) {
                mediaDeviceData = pair.getSecond();
            }
            map.put(str, TuplesKt.to(mediaData, mediaDeviceData));
            update(str, str);
            return;
        }
        Map<String, Pair<MediaData, MediaDeviceData>> map2 = this.entries;
        Pair<MediaData, MediaDeviceData> remove = map2.remove(str2);
        if (remove != null) {
            mediaDeviceData = remove.getSecond();
        }
        map2.put(str, TuplesKt.to(mediaData, mediaDeviceData));
        update(str, str2);
    }

    @Override // com.android.systemui.media.MediaDataManager.Listener
    public void onSmartspaceMediaDataLoaded(String str, SmartspaceMediaData smartspaceMediaData, boolean z) {
        Intrinsics.checkNotNullParameter(str, "key");
        Intrinsics.checkNotNullParameter(smartspaceMediaData, "data");
        for (MediaDataManager.Listener listener : CollectionsKt___CollectionsKt.toSet(this.listeners)) {
            MediaDataManager.Listener.DefaultImpls.onSmartspaceMediaDataLoaded$default(listener, str, smartspaceMediaData, false, 4, null);
        }
    }

    @Override // com.android.systemui.media.MediaDataManager.Listener
    public void onMediaDataRemoved(String str) {
        Intrinsics.checkNotNullParameter(str, "key");
        remove(str);
    }

    @Override // com.android.systemui.media.MediaDataManager.Listener
    public void onSmartspaceMediaDataRemoved(String str, boolean z) {
        Intrinsics.checkNotNullParameter(str, "key");
        for (MediaDataManager.Listener listener : CollectionsKt___CollectionsKt.toSet(this.listeners)) {
            listener.onSmartspaceMediaDataRemoved(str, z);
        }
    }

    @Override // com.android.systemui.media.MediaDeviceManager.Listener
    public void onMediaDeviceChanged(String str, String str2, MediaDeviceData mediaDeviceData) {
        Intrinsics.checkNotNullParameter(str, "key");
        MediaData mediaData = null;
        if (str2 == null || Intrinsics.areEqual(str2, str) || !this.entries.containsKey(str2)) {
            Map<String, Pair<MediaData, MediaDeviceData>> map = this.entries;
            Pair<MediaData, MediaDeviceData> pair = map.get(str);
            if (pair != null) {
                mediaData = pair.getFirst();
            }
            map.put(str, TuplesKt.to(mediaData, mediaDeviceData));
            update(str, str);
            return;
        }
        Map<String, Pair<MediaData, MediaDeviceData>> map2 = this.entries;
        Pair<MediaData, MediaDeviceData> remove = map2.remove(str2);
        if (remove != null) {
            mediaData = remove.getFirst();
        }
        map2.put(str, TuplesKt.to(mediaData, mediaDeviceData));
        update(str, str2);
    }

    @Override // com.android.systemui.media.MediaDeviceManager.Listener
    public void onKeyRemoved(String str) {
        Intrinsics.checkNotNullParameter(str, "key");
        remove(str);
    }

    public final boolean addListener(MediaDataManager.Listener listener) {
        Intrinsics.checkNotNullParameter(listener, "listener");
        return this.listeners.add(listener);
    }

    private final void update(String str, String str2) {
        Pair<MediaData, MediaDeviceData> pair = this.entries.get(str);
        if (pair == null) {
            pair = TuplesKt.to(null, null);
        }
        MediaData component1 = pair.component1();
        MediaDeviceData component2 = pair.component2();
        if (!(component1 == null || component2 == null)) {
            MediaData copy$default = MediaData.copy$default(component1, 0, false, 0, null, null, null, null, null, null, null, null, null, null, component2, false, null, false, false, null, false, null, false, 0, 8380415, null);
            for (MediaDataManager.Listener listener : CollectionsKt___CollectionsKt.toSet(this.listeners)) {
                MediaDataManager.Listener.DefaultImpls.onMediaDataLoaded$default(listener, str, str2, copy$default, false, false, 24, null);
            }
        }
    }

    private final void remove(String str) {
        if (this.entries.remove(str) != null) {
            for (MediaDataManager.Listener listener : CollectionsKt___CollectionsKt.toSet(this.listeners)) {
                listener.onMediaDataRemoved(str);
            }
        }
    }
}
