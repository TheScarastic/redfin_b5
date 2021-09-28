package com.android.systemui.media;

import android.app.PendingIntent;
import android.graphics.drawable.Icon;
import android.media.session.MediaSession;
import java.util.List;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: MediaData.kt */
/* loaded from: classes.dex */
public final class MediaData {
    private final List<MediaAction> actions;
    private final List<Integer> actionsToShowInCompact;
    private boolean active;
    private final String app;
    private final Icon appIcon;
    private final CharSequence artist;
    private final Icon artwork;
    private final int backgroundColor;
    private final PendingIntent clickIntent;
    private final MediaDeviceData device;
    private boolean hasCheckedForResume;
    private final boolean initialized;
    private final boolean isClearable;
    private boolean isLocalSession;
    private final Boolean isPlaying;
    private long lastActive;
    private final String notificationKey;
    private final String packageName;
    private Runnable resumeAction;
    private boolean resumption;
    private final CharSequence song;
    private final MediaSession.Token token;
    private final int userId;

    public static /* synthetic */ MediaData copy$default(MediaData mediaData, int i, boolean z, int i2, String str, Icon icon, CharSequence charSequence, CharSequence charSequence2, Icon icon2, List list, List list2, String str2, MediaSession.Token token, PendingIntent pendingIntent, MediaDeviceData mediaDeviceData, boolean z2, Runnable runnable, boolean z3, boolean z4, String str3, boolean z5, Boolean bool, boolean z6, long j, int i3, Object obj) {
        return mediaData.copy((i3 & 1) != 0 ? mediaData.userId : i, (i3 & 2) != 0 ? mediaData.initialized : z, (i3 & 4) != 0 ? mediaData.backgroundColor : i2, (i3 & 8) != 0 ? mediaData.app : str, (i3 & 16) != 0 ? mediaData.appIcon : icon, (i3 & 32) != 0 ? mediaData.artist : charSequence, (i3 & 64) != 0 ? mediaData.song : charSequence2, (i3 & 128) != 0 ? mediaData.artwork : icon2, (i3 & 256) != 0 ? mediaData.actions : list, (i3 & 512) != 0 ? mediaData.actionsToShowInCompact : list2, (i3 & 1024) != 0 ? mediaData.packageName : str2, (i3 & 2048) != 0 ? mediaData.token : token, (i3 & 4096) != 0 ? mediaData.clickIntent : pendingIntent, (i3 & 8192) != 0 ? mediaData.device : mediaDeviceData, (i3 & 16384) != 0 ? mediaData.active : z2, (i3 & 32768) != 0 ? mediaData.resumeAction : runnable, (i3 & 65536) != 0 ? mediaData.isLocalSession : z3, (i3 & 131072) != 0 ? mediaData.resumption : z4, (i3 & 262144) != 0 ? mediaData.notificationKey : str3, (i3 & 524288) != 0 ? mediaData.hasCheckedForResume : z5, (i3 & 1048576) != 0 ? mediaData.isPlaying : bool, (i3 & 2097152) != 0 ? mediaData.isClearable : z6, (i3 & 4194304) != 0 ? mediaData.lastActive : j);
    }

    public final MediaData copy(int i, boolean z, int i2, String str, Icon icon, CharSequence charSequence, CharSequence charSequence2, Icon icon2, List<MediaAction> list, List<Integer> list2, String str2, MediaSession.Token token, PendingIntent pendingIntent, MediaDeviceData mediaDeviceData, boolean z2, Runnable runnable, boolean z3, boolean z4, String str3, boolean z5, Boolean bool, boolean z6, long j) {
        Intrinsics.checkNotNullParameter(list, "actions");
        Intrinsics.checkNotNullParameter(list2, "actionsToShowInCompact");
        Intrinsics.checkNotNullParameter(str2, "packageName");
        return new MediaData(i, z, i2, str, icon, charSequence, charSequence2, icon2, list, list2, str2, token, pendingIntent, mediaDeviceData, z2, runnable, z3, z4, str3, z5, bool, z6, j);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof MediaData)) {
            return false;
        }
        MediaData mediaData = (MediaData) obj;
        return this.userId == mediaData.userId && this.initialized == mediaData.initialized && this.backgroundColor == mediaData.backgroundColor && Intrinsics.areEqual(this.app, mediaData.app) && Intrinsics.areEqual(this.appIcon, mediaData.appIcon) && Intrinsics.areEqual(this.artist, mediaData.artist) && Intrinsics.areEqual(this.song, mediaData.song) && Intrinsics.areEqual(this.artwork, mediaData.artwork) && Intrinsics.areEqual(this.actions, mediaData.actions) && Intrinsics.areEqual(this.actionsToShowInCompact, mediaData.actionsToShowInCompact) && Intrinsics.areEqual(this.packageName, mediaData.packageName) && Intrinsics.areEqual(this.token, mediaData.token) && Intrinsics.areEqual(this.clickIntent, mediaData.clickIntent) && Intrinsics.areEqual(this.device, mediaData.device) && this.active == mediaData.active && Intrinsics.areEqual(this.resumeAction, mediaData.resumeAction) && this.isLocalSession == mediaData.isLocalSession && this.resumption == mediaData.resumption && Intrinsics.areEqual(this.notificationKey, mediaData.notificationKey) && this.hasCheckedForResume == mediaData.hasCheckedForResume && Intrinsics.areEqual(this.isPlaying, mediaData.isPlaying) && this.isClearable == mediaData.isClearable && this.lastActive == mediaData.lastActive;
    }

    public int hashCode() {
        int hashCode = Integer.hashCode(this.userId) * 31;
        boolean z = this.initialized;
        int i = 1;
        if (z) {
            z = true;
        }
        int i2 = z ? 1 : 0;
        int i3 = z ? 1 : 0;
        int i4 = z ? 1 : 0;
        int hashCode2 = (((hashCode + i2) * 31) + Integer.hashCode(this.backgroundColor)) * 31;
        String str = this.app;
        int i5 = 0;
        int hashCode3 = (hashCode2 + (str == null ? 0 : str.hashCode())) * 31;
        Icon icon = this.appIcon;
        int hashCode4 = (hashCode3 + (icon == null ? 0 : icon.hashCode())) * 31;
        CharSequence charSequence = this.artist;
        int hashCode5 = (hashCode4 + (charSequence == null ? 0 : charSequence.hashCode())) * 31;
        CharSequence charSequence2 = this.song;
        int hashCode6 = (hashCode5 + (charSequence2 == null ? 0 : charSequence2.hashCode())) * 31;
        Icon icon2 = this.artwork;
        int hashCode7 = (((((((hashCode6 + (icon2 == null ? 0 : icon2.hashCode())) * 31) + this.actions.hashCode()) * 31) + this.actionsToShowInCompact.hashCode()) * 31) + this.packageName.hashCode()) * 31;
        MediaSession.Token token = this.token;
        int hashCode8 = (hashCode7 + (token == null ? 0 : token.hashCode())) * 31;
        PendingIntent pendingIntent = this.clickIntent;
        int hashCode9 = (hashCode8 + (pendingIntent == null ? 0 : pendingIntent.hashCode())) * 31;
        MediaDeviceData mediaDeviceData = this.device;
        int hashCode10 = (hashCode9 + (mediaDeviceData == null ? 0 : mediaDeviceData.hashCode())) * 31;
        boolean z2 = this.active;
        if (z2) {
            z2 = true;
        }
        int i6 = z2 ? 1 : 0;
        int i7 = z2 ? 1 : 0;
        int i8 = z2 ? 1 : 0;
        int i9 = (hashCode10 + i6) * 31;
        Runnable runnable = this.resumeAction;
        int hashCode11 = (i9 + (runnable == null ? 0 : runnable.hashCode())) * 31;
        boolean z3 = this.isLocalSession;
        if (z3) {
            z3 = true;
        }
        int i10 = z3 ? 1 : 0;
        int i11 = z3 ? 1 : 0;
        int i12 = z3 ? 1 : 0;
        int i13 = (hashCode11 + i10) * 31;
        boolean z4 = this.resumption;
        if (z4) {
            z4 = true;
        }
        int i14 = z4 ? 1 : 0;
        int i15 = z4 ? 1 : 0;
        int i16 = z4 ? 1 : 0;
        int i17 = (i13 + i14) * 31;
        String str2 = this.notificationKey;
        int hashCode12 = (i17 + (str2 == null ? 0 : str2.hashCode())) * 31;
        boolean z5 = this.hasCheckedForResume;
        if (z5) {
            z5 = true;
        }
        int i18 = z5 ? 1 : 0;
        int i19 = z5 ? 1 : 0;
        int i20 = z5 ? 1 : 0;
        int i21 = (hashCode12 + i18) * 31;
        Boolean bool = this.isPlaying;
        if (bool != null) {
            i5 = bool.hashCode();
        }
        int i22 = (i21 + i5) * 31;
        boolean z6 = this.isClearable;
        if (!z6) {
            i = z6 ? 1 : 0;
        }
        return ((i22 + i) * 31) + Long.hashCode(this.lastActive);
    }

    public String toString() {
        return "MediaData(userId=" + this.userId + ", initialized=" + this.initialized + ", backgroundColor=" + this.backgroundColor + ", app=" + ((Object) this.app) + ", appIcon=" + this.appIcon + ", artist=" + ((Object) this.artist) + ", song=" + ((Object) this.song) + ", artwork=" + this.artwork + ", actions=" + this.actions + ", actionsToShowInCompact=" + this.actionsToShowInCompact + ", packageName=" + this.packageName + ", token=" + this.token + ", clickIntent=" + this.clickIntent + ", device=" + this.device + ", active=" + this.active + ", resumeAction=" + this.resumeAction + ", isLocalSession=" + this.isLocalSession + ", resumption=" + this.resumption + ", notificationKey=" + ((Object) this.notificationKey) + ", hasCheckedForResume=" + this.hasCheckedForResume + ", isPlaying=" + this.isPlaying + ", isClearable=" + this.isClearable + ", lastActive=" + this.lastActive + ')';
    }

    public MediaData(int i, boolean z, int i2, String str, Icon icon, CharSequence charSequence, CharSequence charSequence2, Icon icon2, List<MediaAction> list, List<Integer> list2, String str2, MediaSession.Token token, PendingIntent pendingIntent, MediaDeviceData mediaDeviceData, boolean z2, Runnable runnable, boolean z3, boolean z4, String str3, boolean z5, Boolean bool, boolean z6, long j) {
        Intrinsics.checkNotNullParameter(list, "actions");
        Intrinsics.checkNotNullParameter(list2, "actionsToShowInCompact");
        Intrinsics.checkNotNullParameter(str2, "packageName");
        this.userId = i;
        this.initialized = z;
        this.backgroundColor = i2;
        this.app = str;
        this.appIcon = icon;
        this.artist = charSequence;
        this.song = charSequence2;
        this.artwork = icon2;
        this.actions = list;
        this.actionsToShowInCompact = list2;
        this.packageName = str2;
        this.token = token;
        this.clickIntent = pendingIntent;
        this.device = mediaDeviceData;
        this.active = z2;
        this.resumeAction = runnable;
        this.isLocalSession = z3;
        this.resumption = z4;
        this.notificationKey = str3;
        this.hasCheckedForResume = z5;
        this.isPlaying = bool;
        this.isClearable = z6;
        this.lastActive = j;
    }

    public /* synthetic */ MediaData(int i, boolean z, int i2, String str, Icon icon, CharSequence charSequence, CharSequence charSequence2, Icon icon2, List list, List list2, String str2, MediaSession.Token token, PendingIntent pendingIntent, MediaDeviceData mediaDeviceData, boolean z2, Runnable runnable, boolean z3, boolean z4, String str3, boolean z5, Boolean bool, boolean z6, long j, int i3, DefaultConstructorMarker defaultConstructorMarker) {
        this(i, (i3 & 2) != 0 ? false : z, i2, str, icon, charSequence, charSequence2, icon2, list, list2, str2, token, pendingIntent, mediaDeviceData, z2, runnable, (i3 & 65536) != 0 ? true : z3, (i3 & 131072) != 0 ? false : z4, (i3 & 262144) != 0 ? null : str3, (i3 & 524288) != 0 ? false : z5, (i3 & 1048576) != 0 ? null : bool, (i3 & 2097152) != 0 ? true : z6, (i3 & 4194304) != 0 ? 0 : j);
    }

    public final int getUserId() {
        return this.userId;
    }

    public final int getBackgroundColor() {
        return this.backgroundColor;
    }

    public final String getApp() {
        return this.app;
    }

    public final Icon getAppIcon() {
        return this.appIcon;
    }

    public final CharSequence getArtist() {
        return this.artist;
    }

    public final CharSequence getSong() {
        return this.song;
    }

    public final Icon getArtwork() {
        return this.artwork;
    }

    public final List<MediaAction> getActions() {
        return this.actions;
    }

    public final List<Integer> getActionsToShowInCompact() {
        return this.actionsToShowInCompact;
    }

    public final String getPackageName() {
        return this.packageName;
    }

    public final MediaSession.Token getToken() {
        return this.token;
    }

    public final PendingIntent getClickIntent() {
        return this.clickIntent;
    }

    public final MediaDeviceData getDevice() {
        return this.device;
    }

    public final boolean getActive() {
        return this.active;
    }

    public final void setActive(boolean z) {
        this.active = z;
    }

    public final Runnable getResumeAction() {
        return this.resumeAction;
    }

    public final void setResumeAction(Runnable runnable) {
        this.resumeAction = runnable;
    }

    public final boolean isLocalSession() {
        return this.isLocalSession;
    }

    public final boolean getResumption() {
        return this.resumption;
    }

    public final boolean getHasCheckedForResume() {
        return this.hasCheckedForResume;
    }

    public final void setHasCheckedForResume(boolean z) {
        this.hasCheckedForResume = z;
    }

    public final Boolean isPlaying() {
        return this.isPlaying;
    }

    public final boolean isClearable() {
        return this.isClearable;
    }

    public final long getLastActive() {
        return this.lastActive;
    }
}
