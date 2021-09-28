package com.android.systemui.statusbar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Icon;
import android.media.MediaMetadata;
import android.media.session.MediaController;
import android.media.session.MediaSession;
import android.media.session.MediaSessionManager;
import android.media.session.PlaybackState;
import android.os.AsyncTask;
import android.os.Trace;
import android.os.UserHandle;
import android.service.notification.StatusBarNotification;
import android.util.ArraySet;
import android.widget.ImageView;
import com.android.internal.statusbar.NotificationVisibility;
import com.android.systemui.Dependency;
import com.android.systemui.Dumpable;
import com.android.systemui.colorextraction.SysuiColorExtractor;
import com.android.systemui.media.MediaData;
import com.android.systemui.media.MediaDataManager;
import com.android.systemui.media.SmartspaceMediaData;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.notification.NotificationEntryListener;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.collection.NotifCollection;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.notifcollection.DismissedByUserStats;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener;
import com.android.systemui.statusbar.notification.logging.NotificationLogger;
import com.android.systemui.statusbar.phone.BiometricUnlockController;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.statusbar.phone.LockscreenWallpaper;
import com.android.systemui.statusbar.phone.ScrimController;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.DeviceConfigProxy;
import com.android.systemui.util.Utils;
import com.android.systemui.util.concurrency.DelayableExecutor;
import dagger.Lazy;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
/* loaded from: classes.dex */
public class NotificationMediaManager implements Dumpable {
    private static final HashSet<Integer> PAUSED_MEDIA_STATES;
    private BackDropView mBackdrop;
    private ImageView mBackdropBack;
    private ImageView mBackdropFront;
    private BiometricUnlockController mBiometricUnlockController;
    private final Context mContext;
    private final NotificationEntryManager mEntryManager;
    private final KeyguardBypassController mKeyguardBypassController;
    private LockscreenWallpaper mLockscreenWallpaper;
    private final DelayableExecutor mMainExecutor;
    private final MediaArtworkProcessor mMediaArtworkProcessor;
    private MediaController mMediaController;
    private final MediaDataManager mMediaDataManager;
    private MediaMetadata mMediaMetadata;
    private String mMediaNotificationKey;
    private final MediaSessionManager mMediaSessionManager;
    private final NotifCollection mNotifCollection;
    private final NotifPipeline mNotifPipeline;
    private Lazy<NotificationShadeWindowController> mNotificationShadeWindowController;
    protected NotificationPresenter mPresenter;
    private ScrimController mScrimController;
    private final Lazy<StatusBar> mStatusBarLazy;
    private final boolean mUsingNotifPipeline;
    private final StatusBarStateController mStatusBarStateController = (StatusBarStateController) Dependency.get(StatusBarStateController.class);
    private final SysuiColorExtractor mColorExtractor = (SysuiColorExtractor) Dependency.get(SysuiColorExtractor.class);
    private final KeyguardStateController mKeyguardStateController = (KeyguardStateController) Dependency.get(KeyguardStateController.class);
    private final Set<AsyncTask<?, ?, ?>> mProcessArtworkTasks = new ArraySet();
    private final MediaController.Callback mMediaListener = new MediaController.Callback() { // from class: com.android.systemui.statusbar.NotificationMediaManager.1
        @Override // android.media.session.MediaController.Callback
        public void onPlaybackStateChanged(PlaybackState playbackState) {
            super.onPlaybackStateChanged(playbackState);
            if (playbackState != null) {
                if (!NotificationMediaManager.this.isPlaybackActive(playbackState.getState())) {
                    NotificationMediaManager.this.clearCurrentMediaNotification();
                }
                NotificationMediaManager.this.findAndUpdateMediaNotifications();
            }
        }

        @Override // android.media.session.MediaController.Callback
        public void onMetadataChanged(MediaMetadata mediaMetadata) {
            super.onMetadataChanged(mediaMetadata);
            NotificationMediaManager.this.mMediaArtworkProcessor.clearCache();
            NotificationMediaManager.this.mMediaMetadata = mediaMetadata;
            NotificationMediaManager.this.dispatchUpdateMediaMetaData(true, true);
        }
    };
    protected final Runnable mHideBackdropFront = new Runnable() { // from class: com.android.systemui.statusbar.NotificationMediaManager.7
        @Override // java.lang.Runnable
        public void run() {
            NotificationMediaManager.this.mBackdropFront.setVisibility(4);
            NotificationMediaManager.this.mBackdropFront.animate().cancel();
            NotificationMediaManager.this.mBackdropFront.setImageDrawable(null);
        }
    };
    private final ArrayList<MediaListener> mMediaListeners = new ArrayList<>();

    /* loaded from: classes.dex */
    public interface MediaListener {
        default void onPrimaryMetadataOrStateChanged(MediaMetadata mediaMetadata, int i) {
        }
    }

    /* access modifiers changed from: private */
    public boolean isPlaybackActive(int i) {
        return (i == 1 || i == 7 || i == 0) ? false : true;
    }

    static {
        HashSet<Integer> hashSet = new HashSet<>();
        PAUSED_MEDIA_STATES = hashSet;
        hashSet.add(0);
        hashSet.add(1);
        hashSet.add(2);
        hashSet.add(7);
        hashSet.add(8);
    }

    public NotificationMediaManager(Context context, Lazy<StatusBar> lazy, Lazy<NotificationShadeWindowController> lazy2, NotificationEntryManager notificationEntryManager, MediaArtworkProcessor mediaArtworkProcessor, KeyguardBypassController keyguardBypassController, NotifPipeline notifPipeline, NotifCollection notifCollection, FeatureFlags featureFlags, DelayableExecutor delayableExecutor, DeviceConfigProxy deviceConfigProxy, MediaDataManager mediaDataManager) {
        this.mContext = context;
        this.mMediaArtworkProcessor = mediaArtworkProcessor;
        this.mKeyguardBypassController = keyguardBypassController;
        this.mMediaSessionManager = (MediaSessionManager) context.getSystemService("media_session");
        this.mStatusBarLazy = lazy;
        this.mNotificationShadeWindowController = lazy2;
        this.mEntryManager = notificationEntryManager;
        this.mMainExecutor = delayableExecutor;
        this.mMediaDataManager = mediaDataManager;
        this.mNotifPipeline = notifPipeline;
        this.mNotifCollection = notifCollection;
        if (!featureFlags.isNewNotifPipelineRenderingEnabled()) {
            setupNEM();
            this.mUsingNotifPipeline = false;
            return;
        }
        setupNotifPipeline();
        this.mUsingNotifPipeline = true;
    }

    private void setupNotifPipeline() {
        this.mNotifPipeline.addCollectionListener(new NotifCollectionListener() { // from class: com.android.systemui.statusbar.NotificationMediaManager.2
            @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener
            public void onEntryAdded(NotificationEntry notificationEntry) {
                NotificationMediaManager.this.mMediaDataManager.onNotificationAdded(notificationEntry.getKey(), notificationEntry.getSbn());
            }

            @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener
            public void onEntryUpdated(NotificationEntry notificationEntry) {
                NotificationMediaManager.this.mMediaDataManager.onNotificationAdded(notificationEntry.getKey(), notificationEntry.getSbn());
            }

            @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener
            public void onEntryBind(NotificationEntry notificationEntry, StatusBarNotification statusBarNotification) {
                NotificationMediaManager.this.findAndUpdateMediaNotifications();
            }

            @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener
            public void onEntryRemoved(NotificationEntry notificationEntry, int i) {
                NotificationMediaManager.this.removeEntry(notificationEntry);
            }

            @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener
            public void onEntryCleanUp(NotificationEntry notificationEntry) {
                NotificationMediaManager.this.removeEntry(notificationEntry);
            }
        });
        this.mMediaDataManager.addListener(new MediaDataManager.Listener() { // from class: com.android.systemui.statusbar.NotificationMediaManager.3
            @Override // com.android.systemui.media.MediaDataManager.Listener
            public void onMediaDataLoaded(String str, String str2, MediaData mediaData, boolean z, boolean z2) {
            }

            @Override // com.android.systemui.media.MediaDataManager.Listener
            public void onSmartspaceMediaDataLoaded(String str, SmartspaceMediaData smartspaceMediaData, boolean z) {
            }

            @Override // com.android.systemui.media.MediaDataManager.Listener
            public void onSmartspaceMediaDataRemoved(String str, boolean z) {
            }

            @Override // com.android.systemui.media.MediaDataManager.Listener
            public void onMediaDataRemoved(String str) {
                NotificationMediaManager.this.mNotifPipeline.getAllNotifs().stream().filter(new NotificationMediaManager$3$$ExternalSyntheticLambda1(str)).findAny().ifPresent(new NotificationMediaManager$3$$ExternalSyntheticLambda0(this));
            }

            /* access modifiers changed from: private */
            public static /* synthetic */ boolean lambda$onMediaDataRemoved$0(String str, NotificationEntry notificationEntry) {
                return Objects.equals(notificationEntry.getKey(), str);
            }

            /* access modifiers changed from: private */
            public /* synthetic */ void lambda$onMediaDataRemoved$1(NotificationEntry notificationEntry) {
                NotificationMediaManager.this.mNotifCollection.dismissNotification(notificationEntry, NotificationMediaManager.this.getDismissedByUserStats(notificationEntry));
            }
        });
    }

    private void setupNEM() {
        this.mEntryManager.addNotificationEntryListener(new NotificationEntryListener() { // from class: com.android.systemui.statusbar.NotificationMediaManager.4
            @Override // com.android.systemui.statusbar.notification.NotificationEntryListener
            public void onPendingEntryAdded(NotificationEntry notificationEntry) {
                NotificationMediaManager.this.mMediaDataManager.onNotificationAdded(notificationEntry.getKey(), notificationEntry.getSbn());
            }

            @Override // com.android.systemui.statusbar.notification.NotificationEntryListener
            public void onPreEntryUpdated(NotificationEntry notificationEntry) {
                NotificationMediaManager.this.mMediaDataManager.onNotificationAdded(notificationEntry.getKey(), notificationEntry.getSbn());
            }

            @Override // com.android.systemui.statusbar.notification.NotificationEntryListener
            public void onEntryInflated(NotificationEntry notificationEntry) {
                NotificationMediaManager.this.findAndUpdateMediaNotifications();
            }

            @Override // com.android.systemui.statusbar.notification.NotificationEntryListener
            public void onEntryReinflated(NotificationEntry notificationEntry) {
                NotificationMediaManager.this.findAndUpdateMediaNotifications();
            }

            @Override // com.android.systemui.statusbar.notification.NotificationEntryListener
            public void onEntryRemoved(NotificationEntry notificationEntry, NotificationVisibility notificationVisibility, boolean z, int i) {
                NotificationMediaManager.this.removeEntry(notificationEntry);
            }
        });
        this.mEntryManager.addCollectionListener(new NotifCollectionListener() { // from class: com.android.systemui.statusbar.NotificationMediaManager.5
            @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener
            public void onEntryCleanUp(NotificationEntry notificationEntry) {
                NotificationMediaManager.this.removeEntry(notificationEntry);
            }
        });
        this.mMediaDataManager.addListener(new MediaDataManager.Listener() { // from class: com.android.systemui.statusbar.NotificationMediaManager.6
            @Override // com.android.systemui.media.MediaDataManager.Listener
            public void onMediaDataLoaded(String str, String str2, MediaData mediaData, boolean z, boolean z2) {
            }

            @Override // com.android.systemui.media.MediaDataManager.Listener
            public void onSmartspaceMediaDataLoaded(String str, SmartspaceMediaData smartspaceMediaData, boolean z) {
            }

            @Override // com.android.systemui.media.MediaDataManager.Listener
            public void onSmartspaceMediaDataRemoved(String str, boolean z) {
            }

            @Override // com.android.systemui.media.MediaDataManager.Listener
            public void onMediaDataRemoved(String str) {
                NotificationEntry pendingOrActiveNotif = NotificationMediaManager.this.mEntryManager.getPendingOrActiveNotif(str);
                if (pendingOrActiveNotif != null) {
                    NotificationMediaManager.this.mEntryManager.performRemoveNotification(pendingOrActiveNotif.getSbn(), NotificationMediaManager.this.getDismissedByUserStats(pendingOrActiveNotif), 2);
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public DismissedByUserStats getDismissedByUserStats(NotificationEntry notificationEntry) {
        int i;
        if (this.mUsingNotifPipeline) {
            i = this.mNotifPipeline.getShadeListCount();
        } else {
            i = this.mEntryManager.getActiveNotificationsCount();
        }
        return new DismissedByUserStats(3, 1, NotificationVisibility.obtain(notificationEntry.getKey(), notificationEntry.getRanking().getRank(), i, true, NotificationLogger.getNotificationLocation(notificationEntry)));
    }

    /* access modifiers changed from: private */
    public void removeEntry(NotificationEntry notificationEntry) {
        onNotificationRemoved(notificationEntry.getKey());
        this.mMediaDataManager.onNotificationRemoved(notificationEntry.getKey());
    }

    public static boolean isPlayingState(int i) {
        return !PAUSED_MEDIA_STATES.contains(Integer.valueOf(i));
    }

    public void setUpWithPresenter(NotificationPresenter notificationPresenter) {
        this.mPresenter = notificationPresenter;
    }

    public void onNotificationRemoved(String str) {
        if (str.equals(this.mMediaNotificationKey)) {
            clearCurrentMediaNotification();
            dispatchUpdateMediaMetaData(true, true);
        }
    }

    public String getMediaNotificationKey() {
        return this.mMediaNotificationKey;
    }

    public MediaMetadata getMediaMetadata() {
        return this.mMediaMetadata;
    }

    public Icon getMediaIcon() {
        if (this.mMediaNotificationKey == null) {
            return null;
        }
        if (this.mUsingNotifPipeline) {
            return (Icon) this.mNotifPipeline.getAllNotifs().stream().filter(new Predicate() { // from class: com.android.systemui.statusbar.NotificationMediaManager$$ExternalSyntheticLambda3
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    return NotificationMediaManager.this.lambda$getMediaIcon$0((NotificationEntry) obj);
                }
            }).findAny().map(NotificationMediaManager$$ExternalSyntheticLambda2.INSTANCE).map(NotificationMediaManager$$ExternalSyntheticLambda1.INSTANCE).orElse(null);
        }
        synchronized (this.mEntryManager) {
            NotificationEntry activeNotificationUnfiltered = this.mEntryManager.getActiveNotificationUnfiltered(this.mMediaNotificationKey);
            if (!(activeNotificationUnfiltered == null || activeNotificationUnfiltered.getIcons().getShelfIcon() == null)) {
                return activeNotificationUnfiltered.getIcons().getShelfIcon().getSourceIcon();
            }
            return null;
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$getMediaIcon$0(NotificationEntry notificationEntry) {
        return Objects.equals(notificationEntry.getKey(), this.mMediaNotificationKey);
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ StatusBarIconView lambda$getMediaIcon$1(NotificationEntry notificationEntry) {
        return notificationEntry.getIcons().getShelfIcon();
    }

    public void addCallback(MediaListener mediaListener) {
        this.mMediaListeners.add(mediaListener);
        mediaListener.onPrimaryMetadataOrStateChanged(this.mMediaMetadata, getMediaControllerPlaybackState(this.mMediaController));
    }

    public void removeCallback(MediaListener mediaListener) {
        this.mMediaListeners.remove(mediaListener);
    }

    public void findAndUpdateMediaNotifications() {
        boolean z;
        boolean findPlayingMediaNotification;
        if (this.mUsingNotifPipeline) {
            z = findPlayingMediaNotification(this.mNotifPipeline.getAllNotifs());
        } else {
            synchronized (this.mEntryManager) {
                findPlayingMediaNotification = findPlayingMediaNotification(this.mEntryManager.getAllNotifs());
            }
            if (findPlayingMediaNotification) {
                this.mEntryManager.updateNotifications("NotificationMediaManager - metaDataChanged");
            }
            z = findPlayingMediaNotification;
        }
        dispatchUpdateMediaMetaData(z, true);
    }

    private boolean findPlayingMediaNotification(Collection<NotificationEntry> collection) {
        NotificationEntry notificationEntry;
        MediaController mediaController;
        boolean z;
        MediaSessionManager mediaSessionManager;
        MediaSession.Token token;
        Iterator<NotificationEntry> it = collection.iterator();
        while (true) {
            if (!it.hasNext()) {
                notificationEntry = null;
                mediaController = null;
                break;
            }
            notificationEntry = it.next();
            if (notificationEntry.isMediaNotification() && (token = (MediaSession.Token) notificationEntry.getSbn().getNotification().extras.getParcelable("android.mediaSession")) != null) {
                mediaController = new MediaController(this.mContext, token);
                if (3 == getMediaControllerPlaybackState(mediaController)) {
                    break;
                }
            }
        }
        if (notificationEntry == null && (mediaSessionManager = this.mMediaSessionManager) != null) {
            for (MediaController mediaController2 : mediaSessionManager.getActiveSessionsForUser(null, UserHandle.ALL)) {
                String packageName = mediaController2.getPackageName();
                Iterator<NotificationEntry> it2 = collection.iterator();
                while (true) {
                    if (it2.hasNext()) {
                        NotificationEntry next = it2.next();
                        if (next.getSbn().getPackageName().equals(packageName)) {
                            mediaController = mediaController2;
                            notificationEntry = next;
                            break;
                        }
                    }
                }
            }
        }
        if (mediaController == null || sameSessions(this.mMediaController, mediaController)) {
            z = false;
        } else {
            clearCurrentMediaNotificationSession();
            this.mMediaController = mediaController;
            mediaController.registerCallback(this.mMediaListener);
            this.mMediaMetadata = this.mMediaController.getMetadata();
            z = true;
        }
        if (notificationEntry != null && !notificationEntry.getSbn().getKey().equals(this.mMediaNotificationKey)) {
            this.mMediaNotificationKey = notificationEntry.getSbn().getKey();
        }
        return z;
    }

    public void clearCurrentMediaNotification() {
        this.mMediaNotificationKey = null;
        clearCurrentMediaNotificationSession();
    }

    /* access modifiers changed from: private */
    public void dispatchUpdateMediaMetaData(boolean z, boolean z2) {
        NotificationPresenter notificationPresenter = this.mPresenter;
        if (notificationPresenter != null) {
            notificationPresenter.updateMediaMetaData(z, z2);
        }
        int mediaControllerPlaybackState = getMediaControllerPlaybackState(this.mMediaController);
        ArrayList arrayList = new ArrayList(this.mMediaListeners);
        for (int i = 0; i < arrayList.size(); i++) {
            ((MediaListener) arrayList.get(i)).onPrimaryMetadataOrStateChanged(this.mMediaMetadata, mediaControllerPlaybackState);
        }
    }

    @Override // com.android.systemui.Dumpable
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.print("    mMediaSessionManager=");
        printWriter.println(this.mMediaSessionManager);
        printWriter.print("    mMediaNotificationKey=");
        printWriter.println(this.mMediaNotificationKey);
        printWriter.print("    mMediaController=");
        printWriter.print(this.mMediaController);
        if (this.mMediaController != null) {
            printWriter.print(" state=" + this.mMediaController.getPlaybackState());
        }
        printWriter.println();
        printWriter.print("    mMediaMetadata=");
        printWriter.print(this.mMediaMetadata);
        if (this.mMediaMetadata != null) {
            printWriter.print(" title=" + ((Object) this.mMediaMetadata.getText("android.media.metadata.TITLE")));
        }
        printWriter.println();
    }

    private boolean sameSessions(MediaController mediaController, MediaController mediaController2) {
        if (mediaController == mediaController2) {
            return true;
        }
        if (mediaController == null) {
            return false;
        }
        return mediaController.controlsSameSession(mediaController2);
    }

    private int getMediaControllerPlaybackState(MediaController mediaController) {
        PlaybackState playbackState;
        if (mediaController == null || (playbackState = mediaController.getPlaybackState()) == null) {
            return 0;
        }
        return playbackState.getState();
    }

    private void clearCurrentMediaNotificationSession() {
        this.mMediaArtworkProcessor.clearCache();
        this.mMediaMetadata = null;
        MediaController mediaController = this.mMediaController;
        if (mediaController != null) {
            mediaController.unregisterCallback(this.mMediaListener);
        }
        this.mMediaController = null;
    }

    public void updateMediaMetaData(boolean z, boolean z2) {
        Bitmap bitmap;
        Trace.beginSection("StatusBar#updateMediaMetaData");
        if (this.mBackdrop == null) {
            Trace.endSection();
            return;
        }
        BiometricUnlockController biometricUnlockController = this.mBiometricUnlockController;
        boolean z3 = biometricUnlockController != null && biometricUnlockController.isWakeAndUnlock();
        if (this.mKeyguardStateController.isLaunchTransitionFadingAway() || z3) {
            this.mBackdrop.setVisibility(4);
            Trace.endSection();
            return;
        }
        MediaMetadata mediaMetadata = getMediaMetadata();
        if (mediaMetadata == null || this.mKeyguardBypassController.getBypassEnabled()) {
            bitmap = null;
        } else {
            bitmap = mediaMetadata.getBitmap("android.media.metadata.ART");
            if (bitmap == null) {
                bitmap = mediaMetadata.getBitmap("android.media.metadata.ALBUM_ART");
            }
        }
        if (z) {
            for (AsyncTask<?, ?, ?> asyncTask : this.mProcessArtworkTasks) {
                asyncTask.cancel(true);
            }
            this.mProcessArtworkTasks.clear();
        }
        if (bitmap == null || Utils.useQsMediaPlayer(this.mContext)) {
            finishUpdateMediaMetaData(z, z2, null);
        } else {
            this.mProcessArtworkTasks.add(new ProcessArtworkTask(this, z, z2).execute(bitmap));
        }
        Trace.endSection();
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x0051  */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x0053  */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x005d  */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x0086  */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x00b2  */
    /* JADX WARNING: Removed duplicated region for block: B:56:0x0116  */
    /* JADX WARNING: Removed duplicated region for block: B:77:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:80:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void finishUpdateMediaMetaData(boolean r10, boolean r11, android.graphics.Bitmap r12) {
        /*
        // Method dump skipped, instructions count: 419
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.NotificationMediaManager.finishUpdateMediaMetaData(boolean, boolean, android.graphics.Bitmap):void");
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$finishUpdateMediaMetaData$2() {
        this.mBackdrop.setVisibility(8);
        this.mBackdropFront.animate().cancel();
        this.mBackdropBack.setImageDrawable(null);
        this.mMainExecutor.execute(this.mHideBackdropFront);
    }

    public void setup(BackDropView backDropView, ImageView imageView, ImageView imageView2, ScrimController scrimController, LockscreenWallpaper lockscreenWallpaper) {
        this.mBackdrop = backDropView;
        this.mBackdropFront = imageView;
        this.mBackdropBack = imageView2;
        this.mScrimController = scrimController;
        this.mLockscreenWallpaper = lockscreenWallpaper;
    }

    public void setBiometricUnlockController(BiometricUnlockController biometricUnlockController) {
        this.mBiometricUnlockController = biometricUnlockController;
    }

    /* access modifiers changed from: private */
    public Bitmap processArtwork(Bitmap bitmap) {
        return this.mMediaArtworkProcessor.processArtwork(this.mContext, bitmap);
    }

    /* access modifiers changed from: private */
    public void removeTask(AsyncTask<?, ?, ?> asyncTask) {
        this.mProcessArtworkTasks.remove(asyncTask);
    }

    /* access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class ProcessArtworkTask extends AsyncTask<Bitmap, Void, Bitmap> {
        private final boolean mAllowEnterAnimation;
        private final WeakReference<NotificationMediaManager> mManagerRef;
        private final boolean mMetaDataChanged;

        ProcessArtworkTask(NotificationMediaManager notificationMediaManager, boolean z, boolean z2) {
            this.mManagerRef = new WeakReference<>(notificationMediaManager);
            this.mMetaDataChanged = z;
            this.mAllowEnterAnimation = z2;
        }

        /* access modifiers changed from: protected */
        public Bitmap doInBackground(Bitmap... bitmapArr) {
            NotificationMediaManager notificationMediaManager = this.mManagerRef.get();
            if (notificationMediaManager == null || bitmapArr.length == 0 || isCancelled()) {
                return null;
            }
            return notificationMediaManager.processArtwork(bitmapArr[0]);
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(Bitmap bitmap) {
            NotificationMediaManager notificationMediaManager = this.mManagerRef.get();
            if (notificationMediaManager != null && !isCancelled()) {
                notificationMediaManager.removeTask(this);
                notificationMediaManager.finishUpdateMediaMetaData(this.mMetaDataChanged, this.mAllowEnterAnimation, bitmap);
            }
        }

        /* access modifiers changed from: protected */
        public void onCancelled(Bitmap bitmap) {
            if (bitmap != null) {
                bitmap.recycle();
            }
            NotificationMediaManager notificationMediaManager = this.mManagerRef.get();
            if (notificationMediaManager != null) {
                notificationMediaManager.removeTask(this);
            }
        }
    }
}
