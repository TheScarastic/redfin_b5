package com.android.systemui.media;

import com.android.systemui.media.MediaPlayerData;
import com.android.systemui.util.time.SystemClock;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import kotlin.Triple;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.collections.CollectionsKt__IterablesKt;
import kotlin.comparisons.ComparisonsKt__ComparisonsKt;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: MediaCarouselController.kt */
/* loaded from: classes.dex */
public final class MediaPlayerData {
    private static final Comparator<MediaSortKey> comparator;
    private static final TreeMap<MediaSortKey, MediaControlPanel> mediaPlayers;
    private static boolean shouldPrioritizeSs;
    private static SmartspaceMediaData smartspaceMediaData;
    public static final MediaPlayerData INSTANCE = new MediaPlayerData();
    private static final MediaData EMPTY = new MediaData(-1, false, 0, null, null, null, null, null, CollectionsKt__CollectionsKt.emptyList(), CollectionsKt__CollectionsKt.emptyList(), "INVALID", null, null, null, true, null, false, false, null, false, null, false, 0, 8323072, null);
    private static final Map<String, MediaSortKey> mediaData = new LinkedHashMap();

    private MediaPlayerData() {
    }

    static {
        MediaPlayerData$special$$inlined$thenByDescending$4 mediaPlayerData$special$$inlined$thenByDescending$4 = new Comparator<T>(new Comparator<T>(new Comparator<T>(new Comparator<T>(new Comparator<T>() { // from class: com.android.systemui.media.MediaPlayerData$special$$inlined$compareByDescending$1
            @Override // java.util.Comparator
            public final int compare(T t, T t2) {
                int compareValues;
                compareValues = ComparisonsKt__ComparisonsKt.compareValues(((MediaPlayerData.MediaSortKey) t2).getData().isPlaying(), ((MediaPlayerData.MediaSortKey) t).getData().isPlaying());
                return compareValues;
            }
        }) { // from class: com.android.systemui.media.MediaPlayerData$special$$inlined$thenByDescending$1
            final /* synthetic */ Comparator $this_thenByDescending;

            {
                this.$this_thenByDescending = r1;
            }

            @Override // java.util.Comparator
            public final int compare(T t, T t2) {
                int compareValues;
                int compare = this.$this_thenByDescending.compare(t, t2);
                if (compare != 0) {
                    return compare;
                }
                MediaPlayerData mediaPlayerData = MediaPlayerData.INSTANCE;
                boolean shouldPrioritizeSs$frameworks__base__packages__SystemUI__android_common__SystemUI_core = mediaPlayerData.getShouldPrioritizeSs$frameworks__base__packages__SystemUI__android_common__SystemUI_core();
                boolean isSsMediaRec = ((MediaPlayerData.MediaSortKey) t2).isSsMediaRec();
                if (!shouldPrioritizeSs$frameworks__base__packages__SystemUI__android_common__SystemUI_core) {
                    isSsMediaRec = !isSsMediaRec;
                }
                MediaPlayerData.MediaSortKey mediaSortKey = (MediaPlayerData.MediaSortKey) t;
                compareValues = ComparisonsKt__ComparisonsKt.compareValues(Boolean.valueOf(isSsMediaRec), Boolean.valueOf(mediaPlayerData.getShouldPrioritizeSs$frameworks__base__packages__SystemUI__android_common__SystemUI_core() ? mediaSortKey.isSsMediaRec() : !mediaSortKey.isSsMediaRec()));
                return compareValues;
            }
        }) { // from class: com.android.systemui.media.MediaPlayerData$special$$inlined$thenByDescending$2
            final /* synthetic */ Comparator $this_thenByDescending;

            {
                this.$this_thenByDescending = r1;
            }

            @Override // java.util.Comparator
            public final int compare(T t, T t2) {
                int compareValues;
                int compare = this.$this_thenByDescending.compare(t, t2);
                if (compare != 0) {
                    return compare;
                }
                compareValues = ComparisonsKt__ComparisonsKt.compareValues(Boolean.valueOf(((MediaPlayerData.MediaSortKey) t2).getData().isLocalSession()), Boolean.valueOf(((MediaPlayerData.MediaSortKey) t).getData().isLocalSession()));
                return compareValues;
            }
        }) { // from class: com.android.systemui.media.MediaPlayerData$special$$inlined$thenByDescending$3
            final /* synthetic */ Comparator $this_thenByDescending;

            {
                this.$this_thenByDescending = r1;
            }

            @Override // java.util.Comparator
            public final int compare(T t, T t2) {
                int compareValues;
                int compare = this.$this_thenByDescending.compare(t, t2);
                if (compare != 0) {
                    return compare;
                }
                compareValues = ComparisonsKt__ComparisonsKt.compareValues(Boolean.valueOf(!((MediaPlayerData.MediaSortKey) t2).getData().getResumption()), Boolean.valueOf(!((MediaPlayerData.MediaSortKey) t).getData().getResumption()));
                return compareValues;
            }
        }) { // from class: com.android.systemui.media.MediaPlayerData$special$$inlined$thenByDescending$4
            final /* synthetic */ Comparator $this_thenByDescending;

            {
                this.$this_thenByDescending = r1;
            }

            @Override // java.util.Comparator
            public final int compare(T t, T t2) {
                int compareValues;
                int compare = this.$this_thenByDescending.compare(t, t2);
                if (compare != 0) {
                    return compare;
                }
                compareValues = ComparisonsKt__ComparisonsKt.compareValues(Long.valueOf(((MediaPlayerData.MediaSortKey) t2).getUpdateTime()), Long.valueOf(((MediaPlayerData.MediaSortKey) t).getUpdateTime()));
                return compareValues;
            }
        };
        comparator = mediaPlayerData$special$$inlined$thenByDescending$4;
        mediaPlayers = new TreeMap<>(mediaPlayerData$special$$inlined$thenByDescending$4);
    }

    public final boolean getShouldPrioritizeSs$frameworks__base__packages__SystemUI__android_common__SystemUI_core() {
        return shouldPrioritizeSs;
    }

    public final SmartspaceMediaData getSmartspaceMediaData$frameworks__base__packages__SystemUI__android_common__SystemUI_core() {
        return smartspaceMediaData;
    }

    /* compiled from: MediaCarouselController.kt */
    /* loaded from: classes.dex */
    public static final class MediaSortKey {
        private final MediaData data;
        private final boolean isSsMediaRec;
        private final long updateTime;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof MediaSortKey)) {
                return false;
            }
            MediaSortKey mediaSortKey = (MediaSortKey) obj;
            return this.isSsMediaRec == mediaSortKey.isSsMediaRec && Intrinsics.areEqual(this.data, mediaSortKey.data) && this.updateTime == mediaSortKey.updateTime;
        }

        public int hashCode() {
            boolean z = this.isSsMediaRec;
            if (z) {
                z = true;
            }
            int i = z ? 1 : 0;
            int i2 = z ? 1 : 0;
            int i3 = z ? 1 : 0;
            return (((i * 31) + this.data.hashCode()) * 31) + Long.hashCode(this.updateTime);
        }

        public String toString() {
            return "MediaSortKey(isSsMediaRec=" + this.isSsMediaRec + ", data=" + this.data + ", updateTime=" + this.updateTime + ')';
        }

        public MediaSortKey(boolean z, MediaData mediaData, long j) {
            Intrinsics.checkNotNullParameter(mediaData, "data");
            this.isSsMediaRec = z;
            this.data = mediaData;
            this.updateTime = j;
        }

        public final boolean isSsMediaRec() {
            return this.isSsMediaRec;
        }

        public final MediaData getData() {
            return this.data;
        }

        public final long getUpdateTime() {
            return this.updateTime;
        }
    }

    public final void addMediaPlayer(String str, MediaData mediaData2, MediaControlPanel mediaControlPanel, SystemClock systemClock) {
        Intrinsics.checkNotNullParameter(str, "key");
        Intrinsics.checkNotNullParameter(mediaData2, "data");
        Intrinsics.checkNotNullParameter(mediaControlPanel, "player");
        Intrinsics.checkNotNullParameter(systemClock, "clock");
        removeMediaPlayer(str);
        MediaSortKey mediaSortKey = new MediaSortKey(false, mediaData2, systemClock.currentTimeMillis());
        mediaData.put(str, mediaSortKey);
        mediaPlayers.put(mediaSortKey, mediaControlPanel);
    }

    public final void addMediaRecommendation(String str, SmartspaceMediaData smartspaceMediaData2, MediaControlPanel mediaControlPanel, boolean z, SystemClock systemClock) {
        Intrinsics.checkNotNullParameter(str, "key");
        Intrinsics.checkNotNullParameter(smartspaceMediaData2, "data");
        Intrinsics.checkNotNullParameter(mediaControlPanel, "player");
        Intrinsics.checkNotNullParameter(systemClock, "clock");
        shouldPrioritizeSs = z;
        removeMediaPlayer(str);
        MediaSortKey mediaSortKey = new MediaSortKey(true, EMPTY, systemClock.currentTimeMillis());
        mediaData.put(str, mediaSortKey);
        mediaPlayers.put(mediaSortKey, mediaControlPanel);
        smartspaceMediaData = smartspaceMediaData2;
    }

    public final void moveIfExists(String str, String str2) {
        Map<String, MediaSortKey> map;
        MediaSortKey remove;
        Intrinsics.checkNotNullParameter(str2, "newKey");
        if (str != null && !Intrinsics.areEqual(str, str2) && (remove = (map = mediaData).remove(str)) != null) {
            INSTANCE.removeMediaPlayer(str2);
            map.put(str2, remove);
        }
    }

    public final MediaControlPanel getMediaPlayer(String str) {
        Intrinsics.checkNotNullParameter(str, "key");
        MediaSortKey mediaSortKey = mediaData.get(str);
        if (mediaSortKey == null) {
            return null;
        }
        return mediaPlayers.get(mediaSortKey);
    }

    public final int getMediaPlayerIndex(String str) {
        Intrinsics.checkNotNullParameter(str, "key");
        MediaSortKey mediaSortKey = mediaData.get(str);
        Set<Map.Entry<MediaSortKey, MediaControlPanel>> entrySet = mediaPlayers.entrySet();
        Intrinsics.checkNotNullExpressionValue(entrySet, "mediaPlayers.entries");
        int i = 0;
        for (Object obj : entrySet) {
            i++;
            if (i < 0) {
                CollectionsKt__CollectionsKt.throwIndexOverflow();
            }
            if (Intrinsics.areEqual(((Map.Entry) obj).getKey(), mediaSortKey)) {
                return i;
            }
        }
        return -1;
    }

    public final MediaControlPanel removeMediaPlayer(String str) {
        Intrinsics.checkNotNullParameter(str, "key");
        MediaSortKey remove = mediaData.remove(str);
        if (remove == null) {
            return null;
        }
        if (remove.isSsMediaRec()) {
            smartspaceMediaData = null;
        }
        return mediaPlayers.remove(remove);
    }

    public final List<Triple<String, MediaData, Boolean>> mediaData() {
        Set<Map.Entry<String, MediaSortKey>> entrySet = mediaData.entrySet();
        ArrayList arrayList = new ArrayList(CollectionsKt__IterablesKt.collectionSizeOrDefault(entrySet, 10));
        Iterator<T> it = entrySet.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            arrayList.add(new Triple(entry.getKey(), ((MediaSortKey) entry.getValue()).getData(), Boolean.valueOf(((MediaSortKey) entry.getValue()).isSsMediaRec())));
        }
        return arrayList;
    }

    public final Collection<MediaControlPanel> players() {
        Collection<MediaControlPanel> values = mediaPlayers.values();
        Intrinsics.checkNotNullExpressionValue(values, "mediaPlayers.values");
        return values;
    }

    public final Set<MediaSortKey> playerKeys() {
        Set<MediaSortKey> keySet = mediaPlayers.keySet();
        Intrinsics.checkNotNullExpressionValue(keySet, "mediaPlayers.keys");
        return keySet;
    }

    public final int firstActiveMediaIndex() {
        Set<Map.Entry<MediaSortKey, MediaControlPanel>> entrySet = mediaPlayers.entrySet();
        Intrinsics.checkNotNullExpressionValue(entrySet, "mediaPlayers.entries");
        int i = 0;
        for (Object obj : entrySet) {
            i++;
            if (i < 0) {
                CollectionsKt__CollectionsKt.throwIndexOverflow();
            }
            Map.Entry entry = (Map.Entry) obj;
            if (!((MediaSortKey) entry.getKey()).isSsMediaRec() && ((MediaSortKey) entry.getKey()).getData().getActive()) {
                return i;
            }
        }
        return -1;
    }

    public final String smartspaceMediaKey() {
        Iterator<T> it = mediaData.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            if (((MediaSortKey) entry.getValue()).isSsMediaRec()) {
                return (String) entry.getKey();
            }
        }
        return null;
    }

    public final void clear() {
        mediaData.clear();
        mediaPlayers.clear();
    }

    public final boolean hasActiveMediaOrRecommendationCard() {
        SmartspaceMediaData smartspaceMediaData2 = smartspaceMediaData;
        if (smartspaceMediaData2 != null) {
            Boolean valueOf = smartspaceMediaData2 == null ? null : Boolean.valueOf(smartspaceMediaData2.isActive());
            Intrinsics.checkNotNull(valueOf);
            if (valueOf.booleanValue()) {
                return true;
            }
        }
        if (firstActiveMediaIndex() != -1) {
            return true;
        }
        return false;
    }
}
