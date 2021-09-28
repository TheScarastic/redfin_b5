package com.android.wm.shell.bubbles;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Person;
import android.content.Context;
import android.content.Intent;
import android.content.LocusId;
import android.content.pm.PackageManager;
import android.content.pm.ShortcutInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.os.UserHandle;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.logging.InstanceId;
import com.android.wm.shell.bubbles.BubbleViewInfoTask;
import com.android.wm.shell.bubbles.Bubbles;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Objects;
import java.util.concurrent.Executor;
@VisibleForTesting
/* loaded from: classes2.dex */
public class Bubble implements BubbleViewProvider {
    private String mAppName;
    private int mAppUid;
    private Bitmap mBadgeBitmap;
    private Bitmap mBubbleBitmap;
    private String mChannelId;
    private PendingIntent mDeleteIntent;
    private int mDesiredHeight;
    private int mDesiredHeightResId;
    private int mDotColor;
    private Path mDotPath;
    private BubbleExpandedView mExpandedView;
    private int mFlags;
    private FlyoutMessage mFlyoutMessage;
    private final String mGroupKey;
    private Icon mIcon;
    private BadgedImageView mIconView;
    private boolean mInflateSynchronously;
    private BubbleViewInfoTask mInflationTask;
    private InstanceId mInstanceId;
    private PendingIntent mIntent;
    private boolean mIntentActive;
    private PendingIntent.CancelListener mIntentCancelListener;
    private boolean mIsBubble;
    private boolean mIsClearable;
    private boolean mIsImportantConversation;
    private boolean mIsVisuallyInterruptive;
    private final String mKey;
    private long mLastAccessed;
    private long mLastUpdated;
    private final LocusId mLocusId;
    private final Executor mMainExecutor;
    private String mMetadataShortcutId;
    private int mNotificationId;
    private String mPackageName;
    private boolean mPendingIntentCanceled;
    private ShortcutInfo mShortcutInfo;
    private boolean mShouldSuppressNotificationDot;
    private boolean mShouldSuppressNotificationList;
    private boolean mShouldSuppressPeek;
    private boolean mShowBubbleUpdateDot;
    private boolean mSuppressFlyout;
    private Bubbles.SuppressionChangedListener mSuppressionListener;
    private int mTaskId;
    private String mTitle;
    private UserHandle mUser;

    /* loaded from: classes2.dex */
    public static class FlyoutMessage {
        public boolean isGroupChat;
        public CharSequence message;
        public Drawable senderAvatar;
        public Icon senderIcon;
        public CharSequence senderName;
    }

    @VisibleForTesting(visibility = VisibleForTesting.Visibility.PRIVATE)
    public Bubble(String str, ShortcutInfo shortcutInfo, int i, int i2, String str2, int i3, String str3, Executor executor) {
        this.mShowBubbleUpdateDot = true;
        this.mAppUid = -1;
        Objects.requireNonNull(str);
        Objects.requireNonNull(shortcutInfo);
        this.mMetadataShortcutId = shortcutInfo.getId();
        this.mShortcutInfo = shortcutInfo;
        this.mKey = str;
        LocusId locusId = null;
        this.mGroupKey = null;
        this.mLocusId = str3 != null ? new LocusId(str3) : locusId;
        this.mFlags = 0;
        this.mUser = shortcutInfo.getUserHandle();
        this.mPackageName = shortcutInfo.getPackage();
        this.mIcon = shortcutInfo.getIcon();
        this.mDesiredHeight = i;
        this.mDesiredHeightResId = i2;
        this.mTitle = str2;
        this.mShowBubbleUpdateDot = false;
        this.mMainExecutor = executor;
        this.mTaskId = i3;
    }

    @VisibleForTesting(visibility = VisibleForTesting.Visibility.PRIVATE)
    public Bubble(BubbleEntry bubbleEntry, Bubbles.SuppressionChangedListener suppressionChangedListener, Bubbles.PendingIntentCanceledListener pendingIntentCanceledListener, Executor executor) {
        this.mShowBubbleUpdateDot = true;
        this.mAppUid = -1;
        this.mKey = bubbleEntry.getKey();
        this.mGroupKey = bubbleEntry.getGroupKey();
        this.mLocusId = bubbleEntry.getLocusId();
        this.mSuppressionListener = suppressionChangedListener;
        this.mIntentCancelListener = new PendingIntent.CancelListener(executor, pendingIntentCanceledListener) { // from class: com.android.wm.shell.bubbles.Bubble$$ExternalSyntheticLambda0
            public final /* synthetic */ Executor f$1;
            public final /* synthetic */ Bubbles.PendingIntentCanceledListener f$2;

            {
                this.f$1 = r2;
                this.f$2 = r3;
            }

            public final void onCancelled(PendingIntent pendingIntent) {
                Bubble.this.lambda$new$1(this.f$1, this.f$2, pendingIntent);
            }
        };
        this.mMainExecutor = executor;
        this.mTaskId = -1;
        setEntry(bubbleEntry);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(Executor executor, Bubbles.PendingIntentCanceledListener pendingIntentCanceledListener, PendingIntent pendingIntent) {
        PendingIntent pendingIntent2 = this.mIntent;
        if (pendingIntent2 != null) {
            pendingIntent2.unregisterCancelListener(this.mIntentCancelListener);
        }
        executor.execute(new Runnable(pendingIntentCanceledListener) { // from class: com.android.wm.shell.bubbles.Bubble$$ExternalSyntheticLambda1
            public final /* synthetic */ Bubbles.PendingIntentCanceledListener f$1;

            {
                this.f$1 = r2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                Bubble.this.lambda$new$0(this.f$1);
            }
        });
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(Bubbles.PendingIntentCanceledListener pendingIntentCanceledListener) {
        pendingIntentCanceledListener.onPendingIntentCanceled(this);
    }

    @Override // com.android.wm.shell.bubbles.BubbleViewProvider
    public String getKey() {
        return this.mKey;
    }

    public String getGroupKey() {
        return this.mGroupKey;
    }

    public LocusId getLocusId() {
        return this.mLocusId;
    }

    public UserHandle getUser() {
        return this.mUser;
    }

    public String getPackageName() {
        return this.mPackageName;
    }

    @Override // com.android.wm.shell.bubbles.BubbleViewProvider
    public Bitmap getBubbleIcon() {
        return this.mBubbleBitmap;
    }

    @Override // com.android.wm.shell.bubbles.BubbleViewProvider
    public Bitmap getAppBadge() {
        return this.mBadgeBitmap;
    }

    @Override // com.android.wm.shell.bubbles.BubbleViewProvider
    public int getDotColor() {
        return this.mDotColor;
    }

    @Override // com.android.wm.shell.bubbles.BubbleViewProvider
    public Path getDotPath() {
        return this.mDotPath;
    }

    public String getAppName() {
        return this.mAppName;
    }

    public ShortcutInfo getShortcutInfo() {
        return this.mShortcutInfo;
    }

    @Override // com.android.wm.shell.bubbles.BubbleViewProvider
    public BadgedImageView getIconView() {
        return this.mIconView;
    }

    @Override // com.android.wm.shell.bubbles.BubbleViewProvider
    public BubbleExpandedView getExpandedView() {
        return this.mExpandedView;
    }

    public String getTitle() {
        return this.mTitle;
    }

    /* access modifiers changed from: package-private */
    public String getShortcutId() {
        if (getShortcutInfo() != null) {
            return getShortcutInfo().getId();
        }
        return getMetadataShortcutId();
    }

    /* access modifiers changed from: package-private */
    public String getMetadataShortcutId() {
        return this.mMetadataShortcutId;
    }

    /* access modifiers changed from: package-private */
    public boolean hasMetadataShortcutId() {
        String str = this.mMetadataShortcutId;
        return str != null && !str.isEmpty();
    }

    /* access modifiers changed from: package-private */
    public void cleanupExpandedView() {
        BubbleExpandedView bubbleExpandedView = this.mExpandedView;
        if (bubbleExpandedView != null) {
            bubbleExpandedView.cleanUpExpandedState();
            this.mExpandedView = null;
        }
        PendingIntent pendingIntent = this.mIntent;
        if (pendingIntent != null) {
            pendingIntent.unregisterCancelListener(this.mIntentCancelListener);
        }
        this.mIntentActive = false;
    }

    /* access modifiers changed from: package-private */
    public void cleanupViews() {
        cleanupExpandedView();
        this.mIconView = null;
    }

    /* access modifiers changed from: package-private */
    public void setPendingIntentCanceled() {
        this.mPendingIntentCanceled = true;
    }

    /* access modifiers changed from: package-private */
    public boolean getPendingIntentCanceled() {
        return this.mPendingIntentCanceled;
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public void setInflateSynchronously(boolean z) {
        this.mInflateSynchronously = z;
    }

    @VisibleForTesting
    void setVisuallyInterruptiveForTest(boolean z) {
        this.mIsVisuallyInterruptive = z;
    }

    /* access modifiers changed from: package-private */
    public void inflate(BubbleViewInfoTask.Callback callback, Context context, BubbleController bubbleController, BubbleStackView bubbleStackView, BubbleIconFactory bubbleIconFactory, boolean z) {
        if (isBubbleLoading()) {
            this.mInflationTask.cancel(true);
        }
        BubbleViewInfoTask bubbleViewInfoTask = new BubbleViewInfoTask(this, context, bubbleController, bubbleStackView, bubbleIconFactory, z, callback, this.mMainExecutor);
        this.mInflationTask = bubbleViewInfoTask;
        if (this.mInflateSynchronously) {
            bubbleViewInfoTask.onPostExecute(bubbleViewInfoTask.doInBackground(new Void[0]));
        } else {
            bubbleViewInfoTask.execute(new Void[0]);
        }
    }

    private boolean isBubbleLoading() {
        BubbleViewInfoTask bubbleViewInfoTask = this.mInflationTask;
        return (bubbleViewInfoTask == null || bubbleViewInfoTask.getStatus() == AsyncTask.Status.FINISHED) ? false : true;
    }

    /* access modifiers changed from: package-private */
    public boolean isInflated() {
        return (this.mIconView == null || this.mExpandedView == null) ? false : true;
    }

    /* access modifiers changed from: package-private */
    public void stopInflation() {
        BubbleViewInfoTask bubbleViewInfoTask = this.mInflationTask;
        if (bubbleViewInfoTask != null) {
            bubbleViewInfoTask.cancel(true);
        }
    }

    /* access modifiers changed from: package-private */
    public void setViewInfo(BubbleViewInfoTask.BubbleViewInfo bubbleViewInfo) {
        if (!isInflated()) {
            this.mIconView = bubbleViewInfo.imageView;
            this.mExpandedView = bubbleViewInfo.expandedView;
        }
        this.mShortcutInfo = bubbleViewInfo.shortcutInfo;
        this.mAppName = bubbleViewInfo.appName;
        this.mFlyoutMessage = bubbleViewInfo.flyoutMessage;
        this.mBadgeBitmap = bubbleViewInfo.badgeBitmap;
        this.mBubbleBitmap = bubbleViewInfo.bubbleBitmap;
        this.mDotColor = bubbleViewInfo.dotColor;
        this.mDotPath = bubbleViewInfo.dotPath;
        BubbleExpandedView bubbleExpandedView = this.mExpandedView;
        if (bubbleExpandedView != null) {
            bubbleExpandedView.update(this);
        }
        BadgedImageView badgedImageView = this.mIconView;
        if (badgedImageView != null) {
            badgedImageView.setRenderedBubble(this);
        }
    }

    @Override // com.android.wm.shell.bubbles.BubbleViewProvider
    public void setExpandedContentAlpha(float f) {
        BubbleExpandedView bubbleExpandedView = this.mExpandedView;
        if (bubbleExpandedView != null) {
            bubbleExpandedView.setAlpha(f);
            this.mExpandedView.setTaskViewAlpha(f);
        }
    }

    @Override // com.android.wm.shell.bubbles.BubbleViewProvider
    public void setTaskViewVisibility(boolean z) {
        BubbleExpandedView bubbleExpandedView = this.mExpandedView;
        if (bubbleExpandedView != null) {
            bubbleExpandedView.setContentVisibility(z);
        }
    }

    /* access modifiers changed from: package-private */
    public void setEntry(BubbleEntry bubbleEntry) {
        PendingIntent pendingIntent;
        Objects.requireNonNull(bubbleEntry);
        this.mLastUpdated = bubbleEntry.getStatusBarNotification().getPostTime();
        this.mIsBubble = bubbleEntry.getStatusBarNotification().getNotification().isBubbleNotification();
        this.mPackageName = bubbleEntry.getStatusBarNotification().getPackageName();
        this.mUser = bubbleEntry.getStatusBarNotification().getUser();
        this.mTitle = getTitle(bubbleEntry);
        this.mChannelId = bubbleEntry.getStatusBarNotification().getNotification().getChannelId();
        this.mNotificationId = bubbleEntry.getStatusBarNotification().getId();
        this.mAppUid = bubbleEntry.getStatusBarNotification().getUid();
        this.mInstanceId = bubbleEntry.getStatusBarNotification().getInstanceId();
        this.mFlyoutMessage = extractFlyoutMessage(bubbleEntry);
        if (bubbleEntry.getRanking() != null) {
            this.mShortcutInfo = bubbleEntry.getRanking().getConversationShortcutInfo();
            this.mIsVisuallyInterruptive = bubbleEntry.getRanking().visuallyInterruptive();
            if (bubbleEntry.getRanking().getChannel() != null) {
                this.mIsImportantConversation = bubbleEntry.getRanking().getChannel().isImportantConversation();
            }
        }
        if (bubbleEntry.getBubbleMetadata() != null) {
            this.mMetadataShortcutId = bubbleEntry.getBubbleMetadata().getShortcutId();
            this.mFlags = bubbleEntry.getBubbleMetadata().getFlags();
            this.mDesiredHeight = bubbleEntry.getBubbleMetadata().getDesiredHeight();
            this.mDesiredHeightResId = bubbleEntry.getBubbleMetadata().getDesiredHeightResId();
            this.mIcon = bubbleEntry.getBubbleMetadata().getIcon();
            if (!this.mIntentActive || (pendingIntent = this.mIntent) == null) {
                PendingIntent pendingIntent2 = this.mIntent;
                if (pendingIntent2 != null) {
                    pendingIntent2.unregisterCancelListener(this.mIntentCancelListener);
                }
                PendingIntent intent = bubbleEntry.getBubbleMetadata().getIntent();
                this.mIntent = intent;
                if (intent != null) {
                    intent.registerCancelListener(this.mIntentCancelListener);
                }
            } else if (pendingIntent != null && bubbleEntry.getBubbleMetadata().getIntent() == null) {
                this.mIntent.unregisterCancelListener(this.mIntentCancelListener);
                this.mIntentActive = false;
                this.mIntent = null;
            }
            this.mDeleteIntent = bubbleEntry.getBubbleMetadata().getDeleteIntent();
        }
        this.mIsClearable = bubbleEntry.isClearable();
        this.mShouldSuppressNotificationDot = bubbleEntry.shouldSuppressNotificationDot();
        this.mShouldSuppressNotificationList = bubbleEntry.shouldSuppressNotificationList();
        this.mShouldSuppressPeek = bubbleEntry.shouldSuppressPeek();
    }

    /* access modifiers changed from: package-private */
    public Icon getIcon() {
        return this.mIcon;
    }

    /* access modifiers changed from: package-private */
    public boolean isVisuallyInterruptive() {
        return this.mIsVisuallyInterruptive;
    }

    /* access modifiers changed from: package-private */
    public long getLastActivity() {
        return Math.max(this.mLastUpdated, this.mLastAccessed);
    }

    /* access modifiers changed from: package-private */
    public void setIntentActive() {
        this.mIntentActive = true;
    }

    /* access modifiers changed from: package-private */
    public boolean isIntentActive() {
        return this.mIntentActive;
    }

    public InstanceId getInstanceId() {
        return this.mInstanceId;
    }

    public String getChannelId() {
        return this.mChannelId;
    }

    public int getNotificationId() {
        return this.mNotificationId;
    }

    @Override // com.android.wm.shell.bubbles.BubbleViewProvider
    public int getTaskId() {
        BubbleExpandedView bubbleExpandedView = this.mExpandedView;
        return bubbleExpandedView != null ? bubbleExpandedView.getTaskId() : this.mTaskId;
    }

    /* access modifiers changed from: package-private */
    public void markAsAccessedAt(long j) {
        this.mLastAccessed = j;
        setSuppressNotification(true);
        setShowDot(false);
    }

    /* access modifiers changed from: package-private */
    public boolean showInShade() {
        return !shouldSuppressNotification() || !this.mIsClearable;
    }

    /* access modifiers changed from: package-private */
    public boolean isSuppressed() {
        return (this.mFlags & 8) != 0;
    }

    /* access modifiers changed from: package-private */
    public boolean isSuppressable() {
        return (this.mFlags & 4) != 0;
    }

    /* access modifiers changed from: package-private */
    public boolean isImportantConversation() {
        return this.mIsImportantConversation;
    }

    @VisibleForTesting
    public void setSuppressNotification(boolean z) {
        Bubbles.SuppressionChangedListener suppressionChangedListener;
        boolean showInShade = showInShade();
        if (z) {
            this.mFlags |= 2;
        } else {
            this.mFlags &= -3;
        }
        if (showInShade() != showInShade && (suppressionChangedListener = this.mSuppressionListener) != null) {
            suppressionChangedListener.onBubbleNotificationSuppressionChange(this);
        }
    }

    public void setSuppressBubble(boolean z) {
        Bubbles.SuppressionChangedListener suppressionChangedListener;
        if (!isSuppressable()) {
            Log.e("Bubble", "calling setSuppressBubble on " + getKey() + " when bubble not suppressable");
            return;
        }
        boolean isSuppressed = isSuppressed();
        if (z) {
            this.mFlags |= 8;
        } else {
            this.mFlags &= -9;
        }
        if (isSuppressed != z && (suppressionChangedListener = this.mSuppressionListener) != null) {
            suppressionChangedListener.onBubbleNotificationSuppressionChange(this);
        }
    }

    /* access modifiers changed from: package-private */
    public void setShowDot(boolean z) {
        this.mShowBubbleUpdateDot = z;
        BadgedImageView badgedImageView = this.mIconView;
        if (badgedImageView != null) {
            badgedImageView.updateDotVisibility(true);
        }
    }

    @Override // com.android.wm.shell.bubbles.BubbleViewProvider
    public boolean showDot() {
        return this.mShowBubbleUpdateDot && !this.mShouldSuppressNotificationDot && !shouldSuppressNotification();
    }

    @VisibleForTesting
    public boolean showFlyout() {
        return !this.mSuppressFlyout && !this.mShouldSuppressPeek && !shouldSuppressNotification() && !this.mShouldSuppressNotificationList;
    }

    /* access modifiers changed from: package-private */
    public void setSuppressFlyout(boolean z) {
        this.mSuppressFlyout = z;
    }

    /* access modifiers changed from: package-private */
    public FlyoutMessage getFlyoutMessage() {
        return this.mFlyoutMessage;
    }

    /* access modifiers changed from: package-private */
    public int getRawDesiredHeight() {
        return this.mDesiredHeight;
    }

    /* access modifiers changed from: package-private */
    public int getRawDesiredHeightResId() {
        return this.mDesiredHeightResId;
    }

    /* access modifiers changed from: package-private */
    public float getDesiredHeight(Context context) {
        int i = this.mDesiredHeightResId;
        if (i != 0) {
            return (float) getDimenForPackageUser(context, i, this.mPackageName, this.mUser.getIdentifier());
        }
        return ((float) this.mDesiredHeight) * context.getResources().getDisplayMetrics().density;
    }

    String getDesiredHeightString() {
        int i = this.mDesiredHeightResId;
        if (i != 0) {
            return String.valueOf(i);
        }
        return String.valueOf(this.mDesiredHeight);
    }

    /* access modifiers changed from: package-private */
    public PendingIntent getBubbleIntent() {
        return this.mIntent;
    }

    /* access modifiers changed from: package-private */
    public PendingIntent getDeleteIntent() {
        return this.mDeleteIntent;
    }

    /* access modifiers changed from: package-private */
    public Intent getSettingsIntent(Context context) {
        Intent intent = new Intent("android.settings.APP_NOTIFICATION_BUBBLE_SETTINGS");
        intent.putExtra("android.provider.extra.APP_PACKAGE", getPackageName());
        int uid = getUid(context);
        if (uid != -1) {
            intent.putExtra("app_uid", uid);
        }
        intent.addFlags(134217728);
        intent.addFlags(268435456);
        intent.addFlags(536870912);
        return intent;
    }

    public int getAppUid() {
        return this.mAppUid;
    }

    private int getUid(Context context) {
        int i = this.mAppUid;
        if (i != -1) {
            return i;
        }
        PackageManager packageManagerForUser = BubbleController.getPackageManagerForUser(context, this.mUser.getIdentifier());
        if (packageManagerForUser == null) {
            return -1;
        }
        try {
            return packageManagerForUser.getApplicationInfo(this.mShortcutInfo.getPackage(), 0).uid;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("Bubble", "cannot find uid", e);
            return -1;
        }
    }

    private int getDimenForPackageUser(Context context, int i, String str, int i2) {
        if (str != null) {
            if (i2 == -1) {
                i2 = 0;
            }
            try {
                return context.createContextAsUser(UserHandle.of(i2), 0).getPackageManager().getResourcesForApplication(str).getDimensionPixelSize(i);
            } catch (PackageManager.NameNotFoundException unused) {
            } catch (Resources.NotFoundException e) {
                Log.e("Bubble", "Couldn't find desired height res id", e);
            }
        }
        return 0;
    }

    private boolean shouldSuppressNotification() {
        return isEnabled(2);
    }

    public boolean shouldAutoExpand() {
        return isEnabled(1);
    }

    /* access modifiers changed from: package-private */
    public void setShouldAutoExpand(boolean z) {
        if (z) {
            enable(1);
        } else {
            disable(1);
        }
    }

    public void setIsBubble(boolean z) {
        this.mIsBubble = z;
    }

    public boolean isBubble() {
        return this.mIsBubble;
    }

    public void enable(int i) {
        this.mFlags = i | this.mFlags;
    }

    public void disable(int i) {
        this.mFlags = (~i) & this.mFlags;
    }

    public boolean isEnabled(int i) {
        return (this.mFlags & i) != 0;
    }

    public String toString() {
        return "Bubble{" + this.mKey + '}';
    }

    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.print("key: ");
        printWriter.println(this.mKey);
        printWriter.print("  showInShade:   ");
        printWriter.println(showInShade());
        printWriter.print("  showDot:       ");
        printWriter.println(showDot());
        printWriter.print("  showFlyout:    ");
        printWriter.println(showFlyout());
        printWriter.print("  lastActivity:  ");
        printWriter.println(getLastActivity());
        printWriter.print("  desiredHeight: ");
        printWriter.println(getDesiredHeightString());
        printWriter.print("  suppressNotif: ");
        printWriter.println(shouldSuppressNotification());
        printWriter.print("  autoExpand:    ");
        printWriter.println(shouldAutoExpand());
        BubbleExpandedView bubbleExpandedView = this.mExpandedView;
        if (bubbleExpandedView != null) {
            bubbleExpandedView.dump(fileDescriptor, printWriter, strArr);
        }
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Bubble)) {
            return false;
        }
        return Objects.equals(this.mKey, ((Bubble) obj).mKey);
    }

    public int hashCode() {
        return Objects.hash(this.mKey);
    }

    private static String getTitle(BubbleEntry bubbleEntry) {
        CharSequence charSequence = bubbleEntry.getStatusBarNotification().getNotification().extras.getCharSequence("android.title");
        if (charSequence == null) {
            return null;
        }
        return charSequence.toString();
    }

    static FlyoutMessage extractFlyoutMessage(BubbleEntry bubbleEntry) {
        Objects.requireNonNull(bubbleEntry);
        Notification notification = bubbleEntry.getStatusBarNotification().getNotification();
        Class notificationStyle = notification.getNotificationStyle();
        FlyoutMessage flyoutMessage = new FlyoutMessage();
        flyoutMessage.isGroupChat = notification.extras.getBoolean("android.isGroupConversation");
        try {
        } catch (ArrayIndexOutOfBoundsException | ClassCastException | NullPointerException e) {
            e.printStackTrace();
        }
        if (Notification.BigTextStyle.class.equals(notificationStyle)) {
            CharSequence charSequence = notification.extras.getCharSequence("android.bigText");
            if (TextUtils.isEmpty(charSequence)) {
                charSequence = notification.extras.getCharSequence("android.text");
            }
            flyoutMessage.message = charSequence;
            return flyoutMessage;
        }
        if (Notification.MessagingStyle.class.equals(notificationStyle)) {
            Notification.MessagingStyle.Message findLatestIncomingMessage = Notification.MessagingStyle.findLatestIncomingMessage(Notification.MessagingStyle.Message.getMessagesFromBundleArray((Parcelable[]) notification.extras.get("android.messages")));
            if (findLatestIncomingMessage != null) {
                flyoutMessage.message = findLatestIncomingMessage.getText();
                Person senderPerson = findLatestIncomingMessage.getSenderPerson();
                Icon icon = null;
                flyoutMessage.senderName = senderPerson != null ? senderPerson.getName() : null;
                flyoutMessage.senderAvatar = null;
                if (senderPerson != null) {
                    icon = senderPerson.getIcon();
                }
                flyoutMessage.senderIcon = icon;
                return flyoutMessage;
            }
        } else if (Notification.InboxStyle.class.equals(notificationStyle)) {
            CharSequence[] charSequenceArray = notification.extras.getCharSequenceArray("android.textLines");
            if (charSequenceArray != null && charSequenceArray.length > 0) {
                flyoutMessage.message = charSequenceArray[charSequenceArray.length - 1];
                return flyoutMessage;
            }
        } else if (Notification.MediaStyle.class.equals(notificationStyle)) {
            return flyoutMessage;
        } else {
            flyoutMessage.message = notification.extras.getCharSequence("android.text");
            return flyoutMessage;
        }
        return flyoutMessage;
    }
}
