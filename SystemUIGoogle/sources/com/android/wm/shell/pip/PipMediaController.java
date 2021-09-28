package com.android.wm.shell.pip;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.app.RemoteAction;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Icon;
import android.media.MediaMetadata;
import android.media.session.MediaController;
import android.media.session.MediaSessionManager;
import android.media.session.PlaybackState;
import android.os.Handler;
import android.os.HandlerExecutor;
import android.os.UserHandle;
import com.android.wm.shell.R;
import com.android.wm.shell.pip.PipMediaController;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
/* loaded from: classes2.dex */
public class PipMediaController {
    private final Context mContext;
    private final HandlerExecutor mHandlerExecutor;
    private final Handler mMainHandler;
    private final BroadcastReceiver mMediaActionReceiver;
    private MediaController mMediaController;
    private final MediaSessionManager mMediaSessionManager;
    private final MediaController.Callback mPlaybackChangedListener = new MediaController.Callback() { // from class: com.android.wm.shell.pip.PipMediaController.2
        @Override // android.media.session.MediaController.Callback
        public void onPlaybackStateChanged(PlaybackState playbackState) {
            PipMediaController.this.notifyActionsChanged();
        }

        @Override // android.media.session.MediaController.Callback
        public void onMetadataChanged(MediaMetadata mediaMetadata) {
            PipMediaController.this.notifyMetadataChanged(mediaMetadata);
        }
    };
    private final MediaSessionManager.OnActiveSessionsChangedListener mSessionsChangedListener = new MediaSessionManager.OnActiveSessionsChangedListener() { // from class: com.android.wm.shell.pip.PipMediaController$$ExternalSyntheticLambda0
        @Override // android.media.session.MediaSessionManager.OnActiveSessionsChangedListener
        public final void onActiveSessionsChanged(List list) {
            PipMediaController.this.resolveActiveMediaController(list);
        }
    };
    private final ArrayList<ActionListener> mActionListeners = new ArrayList<>();
    private final ArrayList<MetadataListener> mMetadataListeners = new ArrayList<>();
    private RemoteAction mPauseAction = getDefaultRemoteAction(R.string.pip_pause, R.drawable.pip_ic_pause_white, "com.android.wm.shell.pip.PAUSE");
    private RemoteAction mPlayAction = getDefaultRemoteAction(R.string.pip_play, R.drawable.pip_ic_play_arrow_white, "com.android.wm.shell.pip.PLAY");
    private RemoteAction mNextAction = getDefaultRemoteAction(R.string.pip_skip_to_next, R.drawable.pip_ic_skip_next_white, "com.android.wm.shell.pip.NEXT");
    private RemoteAction mPrevAction = getDefaultRemoteAction(R.string.pip_skip_to_prev, R.drawable.pip_ic_skip_previous_white, "com.android.wm.shell.pip.PREV");

    /* loaded from: classes2.dex */
    public interface ActionListener {
        void onMediaActionsChanged(List<RemoteAction> list);
    }

    /* loaded from: classes2.dex */
    public interface MetadataListener {
        void onMediaMetadataChanged(MediaMetadata mediaMetadata);
    }

    public PipMediaController(Context context, Handler handler) {
        AnonymousClass1 r0 = new BroadcastReceiver() { // from class: com.android.wm.shell.pip.PipMediaController.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                if (PipMediaController.this.mMediaController != null && PipMediaController.this.mMediaController.getTransportControls() != null) {
                    String action = intent.getAction();
                    action.hashCode();
                    char c = 65535;
                    switch (action.hashCode()) {
                        case 40376596:
                            if (action.equals("com.android.wm.shell.pip.NEXT")) {
                                c = 0;
                                break;
                            }
                            break;
                        case 40442197:
                            if (action.equals("com.android.wm.shell.pip.PLAY")) {
                                c = 1;
                                break;
                            }
                            break;
                        case 40448084:
                            if (action.equals("com.android.wm.shell.pip.PREV")) {
                                c = 2;
                                break;
                            }
                            break;
                        case 1253399509:
                            if (action.equals("com.android.wm.shell.pip.PAUSE")) {
                                c = 3;
                                break;
                            }
                            break;
                    }
                    switch (c) {
                        case 0:
                            PipMediaController.this.mMediaController.getTransportControls().skipToNext();
                            return;
                        case 1:
                            PipMediaController.this.mMediaController.getTransportControls().play();
                            return;
                        case 2:
                            PipMediaController.this.mMediaController.getTransportControls().skipToPrevious();
                            return;
                        case 3:
                            PipMediaController.this.mMediaController.getTransportControls().pause();
                            return;
                        default:
                            return;
                    }
                }
            }
        };
        this.mMediaActionReceiver = r0;
        this.mContext = context;
        this.mMainHandler = handler;
        this.mHandlerExecutor = new HandlerExecutor(handler);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.android.wm.shell.pip.PLAY");
        intentFilter.addAction("com.android.wm.shell.pip.PAUSE");
        intentFilter.addAction("com.android.wm.shell.pip.NEXT");
        intentFilter.addAction("com.android.wm.shell.pip.PREV");
        context.registerReceiverForAllUsers(r0, intentFilter, "com.android.systemui.permission.SELF", handler);
        this.mMediaSessionManager = (MediaSessionManager) context.getSystemService(MediaSessionManager.class);
    }

    public void onActivityPinned() {
        resolveActiveMediaController(this.mMediaSessionManager.getActiveSessionsForUser(null, UserHandle.CURRENT));
    }

    public void addActionListener(ActionListener actionListener) {
        if (!this.mActionListeners.contains(actionListener)) {
            this.mActionListeners.add(actionListener);
            actionListener.onMediaActionsChanged(getMediaActions());
        }
    }

    public void removeActionListener(ActionListener actionListener) {
        actionListener.onMediaActionsChanged(Collections.emptyList());
        this.mActionListeners.remove(actionListener);
    }

    public void addMetadataListener(MetadataListener metadataListener) {
        if (!this.mMetadataListeners.contains(metadataListener)) {
            this.mMetadataListeners.add(metadataListener);
            metadataListener.onMediaMetadataChanged(getMediaMetadata());
        }
    }

    private MediaMetadata getMediaMetadata() {
        MediaController mediaController = this.mMediaController;
        if (mediaController != null) {
            return mediaController.getMetadata();
        }
        return null;
    }

    @SuppressLint({"NewApi"})
    private List<RemoteAction> getMediaActions() {
        MediaController mediaController = this.mMediaController;
        if (mediaController == null || mediaController.getPlaybackState() == null) {
            return Collections.emptyList();
        }
        ArrayList arrayList = new ArrayList();
        boolean isActive = this.mMediaController.getPlaybackState().isActive();
        long actions = this.mMediaController.getPlaybackState().getActions();
        boolean z = true;
        this.mPrevAction.setEnabled((16 & actions) != 0);
        arrayList.add(this.mPrevAction);
        if (!isActive && (4 & actions) != 0) {
            arrayList.add(this.mPlayAction);
        } else if (isActive && (2 & actions) != 0) {
            arrayList.add(this.mPauseAction);
        }
        RemoteAction remoteAction = this.mNextAction;
        if ((actions & 32) == 0) {
            z = false;
        }
        remoteAction.setEnabled(z);
        arrayList.add(this.mNextAction);
        return arrayList;
    }

    private RemoteAction getDefaultRemoteAction(int i, int i2, String str) {
        String string = this.mContext.getString(i);
        Intent intent = new Intent(str);
        intent.setPackage(this.mContext.getPackageName());
        return new RemoteAction(Icon.createWithResource(this.mContext, i2), string, string, PendingIntent.getBroadcast(this.mContext, 0, intent, 201326592));
    }

    public void registerSessionListenerForCurrentUser() {
        this.mMediaSessionManager.removeOnActiveSessionsChangedListener(this.mSessionsChangedListener);
        this.mMediaSessionManager.addOnActiveSessionsChangedListener(null, UserHandle.CURRENT, this.mHandlerExecutor, this.mSessionsChangedListener);
    }

    /* access modifiers changed from: private */
    public void resolveActiveMediaController(List<MediaController> list) {
        ComponentName componentName;
        if (!(list == null || (componentName = (ComponentName) PipUtils.getTopPipActivity(this.mContext).first) == null)) {
            for (int i = 0; i < list.size(); i++) {
                MediaController mediaController = list.get(i);
                if (mediaController.getPackageName().equals(componentName.getPackageName())) {
                    setActiveMediaController(mediaController);
                    return;
                }
            }
        }
        setActiveMediaController(null);
    }

    private void setActiveMediaController(MediaController mediaController) {
        MediaController mediaController2 = this.mMediaController;
        if (mediaController != mediaController2) {
            if (mediaController2 != null) {
                mediaController2.unregisterCallback(this.mPlaybackChangedListener);
            }
            this.mMediaController = mediaController;
            if (mediaController != null) {
                mediaController.registerCallback(this.mPlaybackChangedListener, this.mMainHandler);
            }
            notifyActionsChanged();
            notifyMetadataChanged(getMediaMetadata());
        }
    }

    /* access modifiers changed from: private */
    public void notifyActionsChanged() {
        if (!this.mActionListeners.isEmpty()) {
            this.mActionListeners.forEach(new Consumer(getMediaActions()) { // from class: com.android.wm.shell.pip.PipMediaController$$ExternalSyntheticLambda2
                public final /* synthetic */ List f$0;

                {
                    this.f$0 = r1;
                }

                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    ((PipMediaController.ActionListener) obj).onMediaActionsChanged(this.f$0);
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void notifyMetadataChanged(MediaMetadata mediaMetadata) {
        if (!this.mMetadataListeners.isEmpty()) {
            this.mMetadataListeners.forEach(new Consumer(mediaMetadata) { // from class: com.android.wm.shell.pip.PipMediaController$$ExternalSyntheticLambda1
                public final /* synthetic */ MediaMetadata f$0;

                {
                    this.f$0 = r1;
                }

                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    ((PipMediaController.MetadataListener) obj).onMediaMetadataChanged(this.f$0);
                }
            });
        }
    }
}
