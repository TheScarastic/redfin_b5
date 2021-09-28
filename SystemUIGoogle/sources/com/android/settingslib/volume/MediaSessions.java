package com.android.settingslib.volume;

import android.app.PendingIntent;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaMetadata;
import android.media.session.MediaController;
import android.media.session.MediaSession;
import android.media.session.MediaSessionManager;
import android.media.session.PlaybackState;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerExecutor;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
/* loaded from: classes.dex */
public class MediaSessions {
    private static final String TAG = Util.logTag(MediaSessions.class);
    private final Callbacks mCallbacks;
    private final Context mContext;
    private final H mHandler;
    private final HandlerExecutor mHandlerExecutor;
    private boolean mInit;
    private final MediaSessionManager mMgr;
    private final Map<MediaSession.Token, MediaControllerRecord> mRecords = new HashMap();
    private final MediaSessionManager.OnActiveSessionsChangedListener mSessionsListener = new MediaSessionManager.OnActiveSessionsChangedListener() { // from class: com.android.settingslib.volume.MediaSessions.1
        @Override // android.media.session.MediaSessionManager.OnActiveSessionsChangedListener
        public void onActiveSessionsChanged(List<MediaController> list) {
            MediaSessions.this.onActiveSessionsUpdatedH(list);
        }
    };
    private final MediaSessionManager.RemoteSessionCallback mRemoteSessionCallback = new MediaSessionManager.RemoteSessionCallback() { // from class: com.android.settingslib.volume.MediaSessions.2
        public void onVolumeChanged(MediaSession.Token token, int i) {
            MediaSessions.this.mHandler.obtainMessage(2, i, 0, token).sendToTarget();
        }

        public void onDefaultRemoteSessionChanged(MediaSession.Token token) {
            MediaSessions.this.mHandler.obtainMessage(3, token).sendToTarget();
        }
    };

    /* loaded from: classes.dex */
    public interface Callbacks {
        void onRemoteRemoved(MediaSession.Token token);

        void onRemoteUpdate(MediaSession.Token token, String str, MediaController.PlaybackInfo playbackInfo);

        void onRemoteVolumeChanged(MediaSession.Token token, int i);
    }

    public MediaSessions(Context context, Looper looper, Callbacks callbacks) {
        this.mContext = context;
        H h = new H(looper);
        this.mHandler = h;
        this.mHandlerExecutor = new HandlerExecutor(h);
        this.mMgr = (MediaSessionManager) context.getSystemService("media_session");
        this.mCallbacks = callbacks;
    }

    public void dump(PrintWriter printWriter) {
        printWriter.println(MediaSessions.class.getSimpleName() + " state:");
        printWriter.print("  mInit: ");
        printWriter.println(this.mInit);
        printWriter.print("  mRecords.size: ");
        printWriter.println(this.mRecords.size());
        int i = 0;
        for (MediaControllerRecord mediaControllerRecord : this.mRecords.values()) {
            i++;
            dump(i, printWriter, mediaControllerRecord.controller);
        }
    }

    public void init() {
        if (D.BUG) {
            Log.d(TAG, "init");
        }
        this.mMgr.addOnActiveSessionsChangedListener(this.mSessionsListener, null, this.mHandler);
        this.mInit = true;
        postUpdateSessions();
        this.mMgr.registerRemoteSessionCallback(this.mHandlerExecutor, this.mRemoteSessionCallback);
    }

    protected void postUpdateSessions() {
        if (this.mInit) {
            this.mHandler.sendEmptyMessage(1);
        }
    }

    public void setVolume(MediaSession.Token token, int i) {
        MediaControllerRecord mediaControllerRecord = this.mRecords.get(token);
        if (mediaControllerRecord == null) {
            String str = TAG;
            Log.w(str, "setVolume: No record found for token " + token);
            return;
        }
        if (D.BUG) {
            String str2 = TAG;
            Log.d(str2, "Setting level to " + i);
        }
        mediaControllerRecord.controller.setVolumeTo(i, 0);
    }

    /* access modifiers changed from: private */
    public void onRemoteVolumeChangedH(MediaSession.Token token, int i) {
        MediaController mediaController = new MediaController(this.mContext, token);
        if (D.BUG) {
            String str = TAG;
            Log.d(str, "remoteVolumeChangedH " + mediaController.getPackageName() + " " + Util.audioManagerFlagsToString(i));
        }
        this.mCallbacks.onRemoteVolumeChanged(mediaController.getSessionToken(), i);
    }

    /* access modifiers changed from: private */
    public void onUpdateRemoteSessionListH(MediaSession.Token token) {
        String str = null;
        MediaController mediaController = token != null ? new MediaController(this.mContext, token) : null;
        if (mediaController != null) {
            str = mediaController.getPackageName();
        }
        if (D.BUG) {
            String str2 = TAG;
            Log.d(str2, "onUpdateRemoteSessionListH " + str);
        }
        postUpdateSessions();
    }

    protected void onActiveSessionsUpdatedH(List<MediaController> list) {
        if (D.BUG) {
            String str = TAG;
            Log.d(str, "onActiveSessionsUpdatedH n=" + list.size());
        }
        HashSet<MediaSession.Token> hashSet = new HashSet(this.mRecords.keySet());
        for (MediaController mediaController : list) {
            MediaSession.Token sessionToken = mediaController.getSessionToken();
            MediaController.PlaybackInfo playbackInfo = mediaController.getPlaybackInfo();
            hashSet.remove(sessionToken);
            if (!this.mRecords.containsKey(sessionToken)) {
                MediaControllerRecord mediaControllerRecord = new MediaControllerRecord(mediaController);
                mediaControllerRecord.name = getControllerName(mediaController);
                this.mRecords.put(sessionToken, mediaControllerRecord);
                mediaController.registerCallback(mediaControllerRecord, this.mHandler);
            }
            MediaControllerRecord mediaControllerRecord2 = this.mRecords.get(sessionToken);
            if (isRemote(playbackInfo)) {
                updateRemoteH(sessionToken, mediaControllerRecord2.name, playbackInfo);
                mediaControllerRecord2.sentRemote = true;
            }
        }
        for (MediaSession.Token token : hashSet) {
            MediaControllerRecord mediaControllerRecord3 = this.mRecords.get(token);
            mediaControllerRecord3.controller.unregisterCallback(mediaControllerRecord3);
            this.mRecords.remove(token);
            if (D.BUG) {
                String str2 = TAG;
                Log.d(str2, "Removing " + mediaControllerRecord3.name + " sentRemote=" + mediaControllerRecord3.sentRemote);
            }
            if (mediaControllerRecord3.sentRemote) {
                this.mCallbacks.onRemoteRemoved(token);
                mediaControllerRecord3.sentRemote = false;
            }
        }
    }

    /* access modifiers changed from: private */
    public static boolean isRemote(MediaController.PlaybackInfo playbackInfo) {
        return playbackInfo != null && playbackInfo.getPlaybackType() == 2;
    }

    protected String getControllerName(MediaController mediaController) {
        String trim;
        PackageManager packageManager = this.mContext.getPackageManager();
        String packageName = mediaController.getPackageName();
        try {
            trim = Objects.toString(packageManager.getApplicationInfo(packageName, 0).loadLabel(packageManager), "").trim();
        } catch (PackageManager.NameNotFoundException unused) {
        }
        return trim.length() > 0 ? trim : packageName;
    }

    /* access modifiers changed from: private */
    public void updateRemoteH(MediaSession.Token token, String str, MediaController.PlaybackInfo playbackInfo) {
        Callbacks callbacks = this.mCallbacks;
        if (callbacks != null) {
            callbacks.onRemoteUpdate(token, str, playbackInfo);
        }
    }

    private static void dump(int i, PrintWriter printWriter, MediaController mediaController) {
        printWriter.println("  Controller " + i + ": " + mediaController.getPackageName());
        Bundle extras = mediaController.getExtras();
        long flags = mediaController.getFlags();
        MediaMetadata metadata = mediaController.getMetadata();
        MediaController.PlaybackInfo playbackInfo = mediaController.getPlaybackInfo();
        PlaybackState playbackState = mediaController.getPlaybackState();
        List<MediaSession.QueueItem> queue = mediaController.getQueue();
        CharSequence queueTitle = mediaController.getQueueTitle();
        int ratingType = mediaController.getRatingType();
        PendingIntent sessionActivity = mediaController.getSessionActivity();
        printWriter.println("    PlaybackState: " + Util.playbackStateToString(playbackState));
        printWriter.println("    PlaybackInfo: " + Util.playbackInfoToString(playbackInfo));
        if (metadata != null) {
            printWriter.println("  MediaMetadata.desc=" + metadata.getDescription());
        }
        printWriter.println("    RatingType: " + ratingType);
        printWriter.println("    Flags: " + flags);
        if (extras != null) {
            printWriter.println("    Extras:");
            for (String str : extras.keySet()) {
                printWriter.println("      " + str + "=" + extras.get(str));
            }
        }
        if (queueTitle != null) {
            printWriter.println("    QueueTitle: " + ((Object) queueTitle));
        }
        if (queue != null && !queue.isEmpty()) {
            printWriter.println("    Queue:");
            Iterator<MediaSession.QueueItem> it = queue.iterator();
            while (it.hasNext()) {
                printWriter.println("      " + it.next());
            }
        }
        if (playbackInfo != null) {
            printWriter.println("    sessionActivity: " + sessionActivity);
        }
    }

    /* access modifiers changed from: private */
    /* loaded from: classes.dex */
    public final class MediaControllerRecord extends MediaController.Callback {
        public final MediaController controller;
        public String name;
        public boolean sentRemote;

        private MediaControllerRecord(MediaController mediaController) {
            this.controller = mediaController;
        }

        private String cb(String str) {
            return str + " " + this.controller.getPackageName() + " ";
        }

        @Override // android.media.session.MediaController.Callback
        public void onAudioInfoChanged(MediaController.PlaybackInfo playbackInfo) {
            if (D.BUG) {
                String str = MediaSessions.TAG;
                Log.d(str, cb("onAudioInfoChanged") + Util.playbackInfoToString(playbackInfo) + " sentRemote=" + this.sentRemote);
            }
            boolean isRemote = MediaSessions.isRemote(playbackInfo);
            if (!isRemote && this.sentRemote) {
                MediaSessions.this.mCallbacks.onRemoteRemoved(this.controller.getSessionToken());
                this.sentRemote = false;
            } else if (isRemote) {
                MediaSessions.this.updateRemoteH(this.controller.getSessionToken(), this.name, playbackInfo);
                this.sentRemote = true;
            }
        }

        @Override // android.media.session.MediaController.Callback
        public void onExtrasChanged(Bundle bundle) {
            if (D.BUG) {
                String str = MediaSessions.TAG;
                Log.d(str, cb("onExtrasChanged") + bundle);
            }
        }

        @Override // android.media.session.MediaController.Callback
        public void onMetadataChanged(MediaMetadata mediaMetadata) {
            if (D.BUG) {
                String str = MediaSessions.TAG;
                Log.d(str, cb("onMetadataChanged") + Util.mediaMetadataToString(mediaMetadata));
            }
        }

        @Override // android.media.session.MediaController.Callback
        public void onPlaybackStateChanged(PlaybackState playbackState) {
            if (D.BUG) {
                String str = MediaSessions.TAG;
                Log.d(str, cb("onPlaybackStateChanged") + Util.playbackStateToString(playbackState));
            }
        }

        @Override // android.media.session.MediaController.Callback
        public void onQueueChanged(List<MediaSession.QueueItem> list) {
            if (D.BUG) {
                String str = MediaSessions.TAG;
                Log.d(str, cb("onQueueChanged") + list);
            }
        }

        @Override // android.media.session.MediaController.Callback
        public void onQueueTitleChanged(CharSequence charSequence) {
            if (D.BUG) {
                String str = MediaSessions.TAG;
                Log.d(str, cb("onQueueTitleChanged") + ((Object) charSequence));
            }
        }

        @Override // android.media.session.MediaController.Callback
        public void onSessionDestroyed() {
            if (D.BUG) {
                Log.d(MediaSessions.TAG, cb("onSessionDestroyed"));
            }
        }

        @Override // android.media.session.MediaController.Callback
        public void onSessionEvent(String str, Bundle bundle) {
            if (D.BUG) {
                String str2 = MediaSessions.TAG;
                Log.d(str2, cb("onSessionEvent") + "event=" + str + " extras=" + bundle);
            }
        }
    }

    /* access modifiers changed from: private */
    /* loaded from: classes.dex */
    public final class H extends Handler {
        private H(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 1) {
                MediaSessions mediaSessions = MediaSessions.this;
                mediaSessions.onActiveSessionsUpdatedH(mediaSessions.mMgr.getActiveSessions(null));
            } else if (i == 2) {
                MediaSessions.this.onRemoteVolumeChangedH((MediaSession.Token) message.obj, message.arg1);
            } else if (i == 3) {
                MediaSessions.this.onUpdateRemoteSessionListH((MediaSession.Token) message.obj);
            }
        }
    }
}
