package com.android.systemui.media;

import android.media.session.MediaController;
import android.media.session.PlaybackState;
import android.util.Log;
import com.android.systemui.media.MediaDataManager;
import com.android.systemui.media.MediaTimeoutListener;
import com.android.systemui.statusbar.NotificationMediaManager;
import com.android.systemui.util.concurrency.DelayableExecutor;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import kotlin.Unit;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.TypeIntrinsics;
/* compiled from: MediaTimeoutListener.kt */
/* loaded from: classes.dex */
public final class MediaTimeoutListener implements MediaDataManager.Listener {
    private final DelayableExecutor mainExecutor;
    private final MediaControllerFactory mediaControllerFactory;
    private final Map<String, PlaybackStateListener> mediaListeners = new LinkedHashMap();
    public Function2<? super String, ? super Boolean, Unit> timeoutCallback;

    public MediaTimeoutListener(MediaControllerFactory mediaControllerFactory, DelayableExecutor delayableExecutor) {
        Intrinsics.checkNotNullParameter(mediaControllerFactory, "mediaControllerFactory");
        Intrinsics.checkNotNullParameter(delayableExecutor, "mainExecutor");
        this.mediaControllerFactory = mediaControllerFactory;
        this.mainExecutor = delayableExecutor;
    }

    @Override // com.android.systemui.media.MediaDataManager.Listener
    public void onSmartspaceMediaDataLoaded(String str, SmartspaceMediaData smartspaceMediaData, boolean z) {
        MediaDataManager.Listener.DefaultImpls.onSmartspaceMediaDataLoaded(this, str, smartspaceMediaData, z);
    }

    @Override // com.android.systemui.media.MediaDataManager.Listener
    public void onSmartspaceMediaDataRemoved(String str, boolean z) {
        MediaDataManager.Listener.DefaultImpls.onSmartspaceMediaDataRemoved(this, str, z);
    }

    /* JADX DEBUG: Type inference failed for r0v1. Raw type applied. Possible types: kotlin.jvm.functions.Function2<? super java.lang.String, ? super java.lang.Boolean, kotlin.Unit>, kotlin.jvm.functions.Function2<java.lang.String, java.lang.Boolean, kotlin.Unit> */
    public final Function2<String, Boolean, Unit> getTimeoutCallback() {
        Function2 function2 = this.timeoutCallback;
        if (function2 != null) {
            return function2;
        }
        Intrinsics.throwUninitializedPropertyAccessException("timeoutCallback");
        throw null;
    }

    public final void setTimeoutCallback(Function2<? super String, ? super Boolean, Unit> function2) {
        Intrinsics.checkNotNullParameter(function2, "<set-?>");
        this.timeoutCallback = function2;
    }

    /* JADX DEBUG: Failed to insert an additional move for type inference into block B:15:0x0034 */
    @Override // com.android.systemui.media.MediaDataManager.Listener
    public void onMediaDataLoaded(String str, String str2, MediaData mediaData, boolean z, boolean z2) {
        Object obj;
        Intrinsics.checkNotNullParameter(str, "key");
        Intrinsics.checkNotNullParameter(mediaData, "data");
        PlaybackStateListener playbackStateListener = this.mediaListeners.get(str);
        if (playbackStateListener == null) {
            obj = null;
        } else if (playbackStateListener.getDestroyed()) {
            Log.d("MediaTimeout", Intrinsics.stringPlus("Reusing destroyed listener ", str));
            obj = playbackStateListener;
        } else {
            return;
        }
        boolean z3 = false;
        if (str2 != null && !Intrinsics.areEqual(str, str2)) {
            Map<String, PlaybackStateListener> map = this.mediaListeners;
            Objects.requireNonNull(map, "null cannot be cast to non-null type kotlin.collections.MutableMap<K, V>");
            obj = TypeIntrinsics.asMutableMap(map).remove(str2);
            if (obj != null) {
                Log.d("MediaTimeout", "migrating key " + ((Object) str2) + " to " + str + ", for resumption");
            } else {
                Log.w("MediaTimeout", "Old key " + ((Object) str2) + " for player " + str + " doesn't exist. Continuing...");
            }
        }
        PlaybackStateListener playbackStateListener2 = (PlaybackStateListener) obj;
        if (playbackStateListener2 == null) {
            this.mediaListeners.put(str, new PlaybackStateListener(this, str, mediaData));
            return;
        }
        Boolean playing = playbackStateListener2.getPlaying();
        if (playing != null) {
            z3 = playing.booleanValue();
        }
        Log.d("MediaTimeout", "updating listener for " + str + ", was playing? " + z3);
        playbackStateListener2.setMediaData(mediaData);
        playbackStateListener2.setKey(str);
        this.mediaListeners.put(str, playbackStateListener2);
        if (!Intrinsics.areEqual(Boolean.valueOf(z3), playbackStateListener2.getPlaying())) {
            this.mainExecutor.execute(new Runnable(this, str) { // from class: com.android.systemui.media.MediaTimeoutListener$onMediaDataLoaded$2$1
                final /* synthetic */ String $key;
                final /* synthetic */ MediaTimeoutListener this$0;

                /* access modifiers changed from: package-private */
                {
                    this.this$0 = r1;
                    this.$key = r2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    MediaTimeoutListener.PlaybackStateListener playbackStateListener3 = (MediaTimeoutListener.PlaybackStateListener) this.this$0.mediaListeners.get(this.$key);
                    if (Intrinsics.areEqual(playbackStateListener3 == null ? null : playbackStateListener3.getPlaying(), Boolean.TRUE)) {
                        Log.d("MediaTimeout", Intrinsics.stringPlus("deliver delayed playback state for ", this.$key));
                        this.this$0.getTimeoutCallback().invoke(this.$key, Boolean.FALSE);
                    }
                }
            });
        }
    }

    @Override // com.android.systemui.media.MediaDataManager.Listener
    public void onMediaDataRemoved(String str) {
        Intrinsics.checkNotNullParameter(str, "key");
        PlaybackStateListener remove = this.mediaListeners.remove(str);
        if (remove != null) {
            remove.destroy();
        }
    }

    /* compiled from: MediaTimeoutListener.kt */
    /* access modifiers changed from: private */
    /* loaded from: classes.dex */
    public final class PlaybackStateListener extends MediaController.Callback {
        private Runnable cancellation;
        private boolean destroyed;
        private String key;
        private MediaController mediaController;
        private MediaData mediaData;
        private Boolean playing;
        final /* synthetic */ MediaTimeoutListener this$0;
        private boolean timedOut;

        public PlaybackStateListener(MediaTimeoutListener mediaTimeoutListener, String str, MediaData mediaData) {
            Intrinsics.checkNotNullParameter(mediaTimeoutListener, "this$0");
            Intrinsics.checkNotNullParameter(str, "key");
            Intrinsics.checkNotNullParameter(mediaData, "data");
            this.this$0 = mediaTimeoutListener;
            this.key = str;
            this.mediaData = mediaData;
            setMediaData(mediaData);
        }

        public final String getKey() {
            return this.key;
        }

        public final void setKey(String str) {
            Intrinsics.checkNotNullParameter(str, "<set-?>");
            this.key = str;
        }

        public final boolean getTimedOut() {
            return this.timedOut;
        }

        public final void setTimedOut(boolean z) {
            this.timedOut = z;
        }

        public final Boolean getPlaying() {
            return this.playing;
        }

        public final boolean getDestroyed() {
            return this.destroyed;
        }

        public final void setMediaData(MediaData mediaData) {
            Intrinsics.checkNotNullParameter(mediaData, "value");
            this.destroyed = false;
            MediaController mediaController = this.mediaController;
            if (mediaController != null) {
                mediaController.unregisterCallback(this);
            }
            this.mediaData = mediaData;
            PlaybackState playbackState = null;
            MediaController create = mediaData.getToken() != null ? this.this$0.mediaControllerFactory.create(this.mediaData.getToken()) : null;
            this.mediaController = create;
            if (create != null) {
                create.registerCallback(this);
            }
            MediaController mediaController2 = this.mediaController;
            if (mediaController2 != null) {
                playbackState = mediaController2.getPlaybackState();
            }
            processState(playbackState, false);
        }

        public final void destroy() {
            MediaController mediaController = this.mediaController;
            if (mediaController != null) {
                mediaController.unregisterCallback(this);
            }
            Runnable runnable = this.cancellation;
            if (runnable != null) {
                runnable.run();
            }
            this.destroyed = true;
        }

        @Override // android.media.session.MediaController.Callback
        public void onPlaybackStateChanged(PlaybackState playbackState) {
            processState(playbackState, true);
        }

        @Override // android.media.session.MediaController.Callback
        public void onSessionDestroyed() {
            Log.d("MediaTimeout", Intrinsics.stringPlus("Session destroyed for ", this.key));
            destroy();
        }

        private final void processState(PlaybackState playbackState, boolean z) {
            Log.v("MediaTimeout", "processState " + this.key + ": " + playbackState);
            boolean z2 = playbackState != null && NotificationMediaManager.isPlayingState(playbackState.getState());
            if (!Intrinsics.areEqual(this.playing, Boolean.valueOf(z2)) || this.playing == null) {
                this.playing = Boolean.valueOf(z2);
                if (!z2) {
                    Log.v("MediaTimeout", Intrinsics.stringPlus("schedule timeout for ", this.key));
                    if (this.cancellation != null) {
                        Log.d("MediaTimeout", "cancellation already exists, continuing.");
                        return;
                    }
                    expireMediaTimeout(this.key, Intrinsics.stringPlus("PLAYBACK STATE CHANGED - ", playbackState));
                    this.cancellation = this.this$0.mainExecutor.executeDelayed(new MediaTimeoutListener$PlaybackStateListener$processState$1(this, this.this$0), MediaTimeoutListenerKt.PAUSED_MEDIA_TIMEOUT);
                    return;
                }
                String str = this.key;
                expireMediaTimeout(str, "playback started - " + playbackState + ", " + this.key);
                this.timedOut = false;
                if (z) {
                    this.this$0.getTimeoutCallback().invoke(this.key, Boolean.valueOf(this.timedOut));
                }
            }
        }

        private final void expireMediaTimeout(String str, String str2) {
            Runnable runnable = this.cancellation;
            if (runnable != null) {
                Log.v("MediaTimeout", "media timeout cancelled for  " + str + ", reason: " + str2);
                runnable.run();
            }
            this.cancellation = null;
        }
    }
}
