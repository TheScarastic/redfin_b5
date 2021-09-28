package com.android.systemui.statusbar.notification.row;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.service.notification.StatusBarNotification;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.R$bool;
import com.android.systemui.R$color;
import com.android.systemui.R$dimen;
import com.android.systemui.R$drawable;
import com.android.systemui.R$layout;
import com.android.systemui.R$string;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.plugins.statusbar.NotificationMenuRowPlugin;
import com.android.systemui.statusbar.AlphaOptimizedImageView;
import com.android.systemui.statusbar.notification.people.PeopleNotificationIdentifier;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.NotificationGuts;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout;
import java.util.ArrayList;
import java.util.Map;
/* loaded from: classes.dex */
public class NotificationMenuRow implements NotificationMenuRowPlugin, View.OnClickListener, ExpandableNotificationRow.LayoutListener {
    private boolean mAnimating;
    private CheckForDrag mCheckForDrag;
    private Context mContext;
    private boolean mDismissRtl;
    private boolean mDismissing;
    private ValueAnimator mFadeAnimator;
    private NotificationMenuRowPlugin.MenuItem mFeedbackItem;
    private boolean mIconsPlaced;
    private NotificationMenuItem mInfoItem;
    private boolean mIsUserTouching;
    private FrameLayout mMenuContainer;
    private boolean mMenuFadedIn;
    private NotificationMenuRowPlugin.OnMenuEventListener mMenuListener;
    private boolean mMenuSnapped;
    private boolean mMenuSnappedOnLeft;
    private boolean mOnLeft;
    private ExpandableNotificationRow mParent;
    private final PeopleNotificationIdentifier mPeopleNotificationIdentifier;
    private boolean mShouldShowMenu;
    private boolean mSnapping;
    private boolean mSnappingToDismiss;
    private NotificationMenuRowPlugin.MenuItem mSnoozeItem;
    private float mTranslation;
    private final Map<View, NotificationMenuRowPlugin.MenuItem> mMenuItemsByView = new ArrayMap();
    private int[] mIconLocation = new int[2];
    private int[] mParentLocation = new int[2];
    private int mHorizSpaceForIcon = -1;
    private int mVertSpaceForIcons = -1;
    private int mIconPadding = -1;
    private float mAlpha = 0.0f;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private ArrayList<NotificationMenuRowPlugin.MenuItem> mLeftMenuItems = new ArrayList<>();
    private ArrayList<NotificationMenuRowPlugin.MenuItem> mRightMenuItems = new ArrayList<>();

    @Override // com.android.systemui.plugins.statusbar.NotificationMenuRowPlugin
    public NotificationMenuRowPlugin.MenuItem menuItemToExposeOnSnap() {
        return null;
    }

    @Override // com.android.systemui.plugins.statusbar.NotificationMenuRowPlugin
    public void setMenuItems(ArrayList<NotificationMenuRowPlugin.MenuItem> arrayList) {
    }

    @Override // com.android.systemui.plugins.statusbar.NotificationMenuRowPlugin
    public boolean shouldShowGutsOnSnapOpen() {
        return false;
    }

    public NotificationMenuRow(Context context, PeopleNotificationIdentifier peopleNotificationIdentifier) {
        this.mContext = context;
        this.mShouldShowMenu = context.getResources().getBoolean(R$bool.config_showNotificationGear);
        this.mPeopleNotificationIdentifier = peopleNotificationIdentifier;
    }

    @Override // com.android.systemui.plugins.statusbar.NotificationMenuRowPlugin
    public ArrayList<NotificationMenuRowPlugin.MenuItem> getMenuItems(Context context) {
        return this.mOnLeft ? this.mLeftMenuItems : this.mRightMenuItems;
    }

    @Override // com.android.systemui.plugins.statusbar.NotificationMenuRowPlugin
    public NotificationMenuRowPlugin.MenuItem getLongpressMenuItem(Context context) {
        return this.mInfoItem;
    }

    @Override // com.android.systemui.plugins.statusbar.NotificationMenuRowPlugin
    public NotificationMenuRowPlugin.MenuItem getFeedbackMenuItem(Context context) {
        return this.mFeedbackItem;
    }

    @Override // com.android.systemui.plugins.statusbar.NotificationMenuRowPlugin
    public NotificationMenuRowPlugin.MenuItem getSnoozeMenuItem(Context context) {
        return this.mSnoozeItem;
    }

    @VisibleForTesting
    protected ExpandableNotificationRow getParent() {
        return this.mParent;
    }

    @VisibleForTesting
    protected boolean isMenuOnLeft() {
        return this.mOnLeft;
    }

    @VisibleForTesting
    protected boolean isMenuSnappedOnLeft() {
        return this.mMenuSnappedOnLeft;
    }

    @VisibleForTesting
    protected boolean isMenuSnapped() {
        return this.mMenuSnapped;
    }

    @VisibleForTesting
    protected boolean isDismissing() {
        return this.mDismissing;
    }

    @VisibleForTesting
    protected boolean isSnapping() {
        return this.mSnapping;
    }

    @VisibleForTesting
    protected boolean isSnappingToDismiss() {
        return this.mSnappingToDismiss;
    }

    @Override // com.android.systemui.plugins.statusbar.NotificationMenuRowPlugin
    public void setMenuClickListener(NotificationMenuRowPlugin.OnMenuEventListener onMenuEventListener) {
        this.mMenuListener = onMenuEventListener;
    }

    @Override // com.android.systemui.plugins.statusbar.NotificationMenuRowPlugin
    public void createMenu(ViewGroup viewGroup, StatusBarNotification statusBarNotification) {
        this.mParent = (ExpandableNotificationRow) viewGroup;
        createMenuViews(true);
    }

    @Override // com.android.systemui.plugins.statusbar.NotificationMenuRowPlugin
    public boolean isMenuVisible() {
        return this.mAlpha > 0.0f;
    }

    @VisibleForTesting
    protected boolean isUserTouching() {
        return this.mIsUserTouching;
    }

    @Override // com.android.systemui.plugins.statusbar.NotificationMenuRowPlugin
    public boolean shouldShowMenu() {
        return this.mShouldShowMenu;
    }

    @Override // com.android.systemui.plugins.statusbar.NotificationMenuRowPlugin
    public View getMenuView() {
        return this.mMenuContainer;
    }

    @VisibleForTesting
    protected float getTranslation() {
        return this.mTranslation;
    }

    @Override // com.android.systemui.plugins.statusbar.NotificationMenuRowPlugin
    public void resetMenu() {
        resetState(true);
    }

    @Override // com.android.systemui.plugins.statusbar.NotificationMenuRowPlugin
    public void onTouchEnd() {
        this.mIsUserTouching = false;
    }

    @Override // com.android.systemui.plugins.statusbar.NotificationMenuRowPlugin
    public void onNotificationUpdated(StatusBarNotification statusBarNotification) {
        if (this.mMenuContainer != null) {
            createMenuViews(!isMenuVisible());
        }
    }

    @Override // com.android.systemui.plugins.statusbar.NotificationMenuRowPlugin
    public void onConfigurationChanged() {
        this.mParent.setLayoutListener(this);
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableNotificationRow.LayoutListener
    public void onLayout() {
        this.mIconsPlaced = false;
        setMenuLocation();
        this.mParent.removeListener();
    }

    private void createMenuViews(boolean z) {
        Resources resources = this.mContext.getResources();
        this.mHorizSpaceForIcon = resources.getDimensionPixelSize(R$dimen.notification_menu_icon_size);
        this.mVertSpaceForIcons = resources.getDimensionPixelSize(R$dimen.notification_min_height);
        this.mLeftMenuItems.clear();
        this.mRightMenuItems.clear();
        boolean z2 = Settings.Secure.getInt(this.mContext.getContentResolver(), "show_notification_snooze", 0) == 1;
        if (z2) {
            this.mSnoozeItem = createSnoozeItem(this.mContext);
        }
        this.mFeedbackItem = createFeedbackItem(this.mContext);
        int peopleNotificationType = this.mPeopleNotificationIdentifier.getPeopleNotificationType(this.mParent.getEntry());
        if (peopleNotificationType == 1) {
            this.mInfoItem = createPartialConversationItem(this.mContext);
        } else if (peopleNotificationType >= 2) {
            this.mInfoItem = createConversationItem(this.mContext);
        } else {
            this.mInfoItem = createInfoItem(this.mContext);
        }
        if (z2) {
            this.mRightMenuItems.add(this.mSnoozeItem);
        }
        this.mRightMenuItems.add(this.mInfoItem);
        this.mRightMenuItems.add(this.mFeedbackItem);
        this.mLeftMenuItems.addAll(this.mRightMenuItems);
        populateMenuViews();
        if (z) {
            resetState(false);
            return;
        }
        this.mIconsPlaced = false;
        setMenuLocation();
        if (!this.mIsUserTouching) {
            onSnapOpen();
        }
    }

    private void populateMenuViews() {
        FrameLayout frameLayout = this.mMenuContainer;
        if (frameLayout != null) {
            frameLayout.removeAllViews();
            this.mMenuItemsByView.clear();
        } else {
            this.mMenuContainer = new FrameLayout(this.mContext);
        }
        int i = Settings.Global.getInt(this.mContext.getContentResolver(), "show_new_notif_dismiss", -1);
        boolean z = true;
        if (i == -1) {
            z = this.mContext.getResources().getBoolean(R$bool.flag_notif_updates);
        } else if (i != 1) {
            z = false;
        }
        if (!z) {
            ArrayList<NotificationMenuRowPlugin.MenuItem> arrayList = this.mOnLeft ? this.mLeftMenuItems : this.mRightMenuItems;
            for (int i2 = 0; i2 < arrayList.size(); i2++) {
                addMenuView(arrayList.get(i2), this.mMenuContainer);
            }
        }
    }

    private void resetState(boolean z) {
        setMenuAlpha(0.0f);
        this.mIconsPlaced = false;
        this.mMenuFadedIn = false;
        this.mAnimating = false;
        this.mSnapping = false;
        this.mDismissing = false;
        this.mMenuSnapped = false;
        setMenuLocation();
        NotificationMenuRowPlugin.OnMenuEventListener onMenuEventListener = this.mMenuListener;
        if (onMenuEventListener != null && z) {
            onMenuEventListener.onMenuReset(this.mParent);
        }
    }

    @Override // com.android.systemui.plugins.statusbar.NotificationMenuRowPlugin
    public void onTouchMove(float f) {
        CheckForDrag checkForDrag;
        boolean z = false;
        this.mSnapping = false;
        if (!isTowardsMenu(f) && isMenuLocationChange()) {
            this.mMenuSnapped = false;
            if (!this.mHandler.hasCallbacks(this.mCheckForDrag)) {
                this.mCheckForDrag = null;
            } else {
                setMenuAlpha(0.0f);
                setMenuLocation();
            }
        }
        if (this.mShouldShowMenu && !NotificationStackScrollLayout.isPinnedHeadsUp(getParent()) && !this.mParent.areGutsExposed() && !this.mParent.showingPulsing() && ((checkForDrag = this.mCheckForDrag) == null || !this.mHandler.hasCallbacks(checkForDrag))) {
            CheckForDrag checkForDrag2 = new CheckForDrag();
            this.mCheckForDrag = checkForDrag2;
            this.mHandler.postDelayed(checkForDrag2, 60);
        }
        if (canBeDismissed()) {
            float dismissThreshold = getDismissThreshold();
            if (f < (-dismissThreshold) || f > dismissThreshold) {
                z = true;
            }
            if (this.mSnappingToDismiss != z) {
                getMenuView().performHapticFeedback(4);
            }
            this.mSnappingToDismiss = z;
        }
    }

    @VisibleForTesting
    protected void beginDrag() {
        this.mSnapping = false;
        ValueAnimator valueAnimator = this.mFadeAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        this.mHandler.removeCallbacks(this.mCheckForDrag);
        this.mCheckForDrag = null;
        this.mIsUserTouching = true;
    }

    @Override // com.android.systemui.plugins.statusbar.NotificationMenuRowPlugin
    public void onTouchStart() {
        beginDrag();
        this.mSnappingToDismiss = false;
    }

    @Override // com.android.systemui.plugins.statusbar.NotificationMenuRowPlugin
    public void onSnapOpen() {
        ExpandableNotificationRow expandableNotificationRow;
        this.mMenuSnapped = true;
        this.mMenuSnappedOnLeft = isMenuOnLeft();
        if (this.mAlpha == 0.0f && (expandableNotificationRow = this.mParent) != null) {
            fadeInMenu((float) expandableNotificationRow.getWidth());
        }
        NotificationMenuRowPlugin.OnMenuEventListener onMenuEventListener = this.mMenuListener;
        if (onMenuEventListener != null) {
            onMenuEventListener.onMenuShown(getParent());
        }
    }

    @Override // com.android.systemui.plugins.statusbar.NotificationMenuRowPlugin
    public void onSnapClosed() {
        cancelDrag();
        this.mMenuSnapped = false;
        this.mSnapping = true;
    }

    @Override // com.android.systemui.plugins.statusbar.NotificationMenuRowPlugin
    public void onDismiss() {
        cancelDrag();
        this.mMenuSnapped = false;
        this.mDismissing = true;
    }

    @VisibleForTesting
    protected void cancelDrag() {
        ValueAnimator valueAnimator = this.mFadeAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        this.mHandler.removeCallbacks(this.mCheckForDrag);
    }

    @VisibleForTesting
    protected float getMinimumSwipeDistance() {
        return ((float) this.mHorizSpaceForIcon) * (getParent().canViewBeDismissed() ? 0.25f : 0.15f);
    }

    @VisibleForTesting
    protected float getMaximumSwipeDistance() {
        return ((float) this.mHorizSpaceForIcon) * 0.2f;
    }

    @Override // com.android.systemui.plugins.statusbar.NotificationMenuRowPlugin
    public boolean isTowardsMenu(float f) {
        return isMenuVisible() && ((isMenuOnLeft() && f <= 0.0f) || (!isMenuOnLeft() && f >= 0.0f));
    }

    @Override // com.android.systemui.plugins.statusbar.NotificationMenuRowPlugin
    public void setAppName(String str) {
        if (str != null) {
            setAppName(str, this.mLeftMenuItems);
            setAppName(str, this.mRightMenuItems);
        }
    }

    private void setAppName(String str, ArrayList<NotificationMenuRowPlugin.MenuItem> arrayList) {
        Resources resources = this.mContext.getResources();
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            NotificationMenuRowPlugin.MenuItem menuItem = arrayList.get(i);
            String format = String.format(resources.getString(R$string.notification_menu_accessibility), str, menuItem.getContentDescription());
            View menuView = menuItem.getMenuView();
            if (menuView != null) {
                menuView.setContentDescription(format);
            }
        }
    }

    @Override // com.android.systemui.plugins.statusbar.NotificationMenuRowPlugin
    public void onParentHeightUpdate() {
        float f;
        if (this.mParent == null) {
            return;
        }
        if ((!this.mLeftMenuItems.isEmpty() || !this.mRightMenuItems.isEmpty()) && this.mMenuContainer != null) {
            int actualHeight = this.mParent.getActualHeight();
            int i = this.mVertSpaceForIcons;
            if (actualHeight < i) {
                f = (float) ((actualHeight / 2) - (this.mHorizSpaceForIcon / 2));
            } else {
                f = (float) ((i - this.mHorizSpaceForIcon) / 2);
            }
            this.mMenuContainer.setTranslationY(f);
        }
    }

    @Override // com.android.systemui.plugins.statusbar.NotificationMenuRowPlugin
    public void onParentTranslationUpdate(float f) {
        this.mTranslation = f;
        if (!this.mAnimating && this.mMenuFadedIn) {
            float width = ((float) this.mParent.getWidth()) * 0.3f;
            float abs = Math.abs(f);
            float f2 = 0.0f;
            if (abs != 0.0f) {
                f2 = abs <= width ? 1.0f : 1.0f - ((abs - width) / (((float) this.mParent.getWidth()) - width));
            }
            setMenuAlpha(f2);
        }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (this.mMenuListener != null) {
            view.getLocationOnScreen(this.mIconLocation);
            this.mParent.getLocationOnScreen(this.mParentLocation);
            int[] iArr = this.mIconLocation;
            int i = iArr[0];
            int[] iArr2 = this.mParentLocation;
            int i2 = (i - iArr2[0]) + (this.mHorizSpaceForIcon / 2);
            int height = (iArr[1] - iArr2[1]) + (view.getHeight() / 2);
            if (this.mMenuItemsByView.containsKey(view)) {
                this.mMenuListener.onMenuClicked(this.mParent, i2, height, this.mMenuItemsByView.get(view));
            }
        }
    }

    /* access modifiers changed from: private */
    public boolean isMenuLocationChange() {
        float f = this.mTranslation;
        int i = this.mIconPadding;
        return (isMenuOnLeft() && ((f > ((float) (-i)) ? 1 : (f == ((float) (-i)) ? 0 : -1)) < 0)) || (!isMenuOnLeft() && ((f > ((float) i) ? 1 : (f == ((float) i) ? 0 : -1)) > 0));
    }

    private void setMenuLocation() {
        FrameLayout frameLayout;
        int i = 0;
        boolean z = this.mTranslation > 0.0f;
        if ((!this.mIconsPlaced || z != isMenuOnLeft()) && !isSnapping() && (frameLayout = this.mMenuContainer) != null && frameLayout.isAttachedToWindow()) {
            boolean z2 = this.mOnLeft;
            this.mOnLeft = z;
            if (z2 != z) {
                populateMenuViews();
            }
            int childCount = this.mMenuContainer.getChildCount();
            while (i < childCount) {
                View childAt = this.mMenuContainer.getChildAt(i);
                float f = (float) (this.mHorizSpaceForIcon * i);
                i++;
                f = (float) (this.mParent.getWidth() - (this.mHorizSpaceForIcon * i));
                if (z) {
                }
                childAt.setX(f);
            }
            this.mIconsPlaced = true;
        }
    }

    @VisibleForTesting
    protected void setMenuAlpha(float f) {
        this.mAlpha = f;
        FrameLayout frameLayout = this.mMenuContainer;
        if (frameLayout != null) {
            if (f == 0.0f) {
                this.mMenuFadedIn = false;
                frameLayout.setVisibility(4);
            } else {
                frameLayout.setVisibility(0);
            }
            int childCount = this.mMenuContainer.getChildCount();
            for (int i = 0; i < childCount; i++) {
                this.mMenuContainer.getChildAt(i).setAlpha(this.mAlpha);
            }
        }
    }

    @VisibleForTesting
    protected int getSpaceForMenu() {
        return this.mHorizSpaceForIcon * this.mMenuContainer.getChildCount();
    }

    /* access modifiers changed from: private */
    /* loaded from: classes.dex */
    public final class CheckForDrag implements Runnable {
        private CheckForDrag() {
        }

        @Override // java.lang.Runnable
        public void run() {
            float abs = Math.abs(NotificationMenuRow.this.mTranslation);
            float spaceForMenu = (float) NotificationMenuRow.this.getSpaceForMenu();
            float width = ((float) NotificationMenuRow.this.mParent.getWidth()) * 0.4f;
            if ((!NotificationMenuRow.this.isMenuVisible() || NotificationMenuRow.this.isMenuLocationChange()) && ((double) abs) >= ((double) spaceForMenu) * 0.4d && abs < width) {
                NotificationMenuRow.this.fadeInMenu(width);
            }
        }
    }

    /* access modifiers changed from: private */
    public void fadeInMenu(final float f) {
        if (!this.mDismissing && !this.mAnimating) {
            if (isMenuLocationChange()) {
                setMenuAlpha(0.0f);
            }
            final float f2 = this.mTranslation;
            final boolean z = f2 > 0.0f;
            setMenuLocation();
            ValueAnimator ofFloat = ValueAnimator.ofFloat(this.mAlpha, 1.0f);
            this.mFadeAnimator = ofFloat;
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.statusbar.notification.row.NotificationMenuRow.1
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    float abs = Math.abs(f2);
                    boolean z2 = z;
                    if (((z2 && f2 <= f) || (!z2 && abs <= f)) && !NotificationMenuRow.this.mMenuFadedIn) {
                        NotificationMenuRow.this.setMenuAlpha(((Float) valueAnimator.getAnimatedValue()).floatValue());
                    }
                }
            });
            this.mFadeAnimator.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.statusbar.notification.row.NotificationMenuRow.2
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationStart(Animator animator) {
                    NotificationMenuRow.this.mAnimating = true;
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationCancel(Animator animator) {
                    NotificationMenuRow.this.setMenuAlpha(0.0f);
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    boolean z2 = false;
                    NotificationMenuRow.this.mAnimating = false;
                    NotificationMenuRow notificationMenuRow = NotificationMenuRow.this;
                    if (notificationMenuRow.mAlpha == 1.0f) {
                        z2 = true;
                    }
                    notificationMenuRow.mMenuFadedIn = z2;
                }
            });
            this.mFadeAnimator.setInterpolator(Interpolators.ALPHA_IN);
            this.mFadeAnimator.setDuration(200L);
            this.mFadeAnimator.start();
        }
    }

    @Override // com.android.systemui.plugins.statusbar.NotificationMenuRowPlugin
    public Point getRevealAnimationOrigin() {
        View menuView = this.mInfoItem.getMenuView();
        int left = menuView.getLeft() + menuView.getPaddingLeft() + (menuView.getWidth() / 2);
        int top = menuView.getTop() + menuView.getPaddingTop() + (menuView.getHeight() / 2);
        if (isMenuOnLeft()) {
            return new Point(left, top);
        }
        return new Point(this.mParent.getRight() - left, top);
    }

    /* access modifiers changed from: package-private */
    public static NotificationMenuRowPlugin.MenuItem createSnoozeItem(Context context) {
        return new NotificationMenuItem(context, context.getResources().getString(R$string.notification_menu_snooze_description), (NotificationSnooze) LayoutInflater.from(context).inflate(R$layout.notification_snooze, (ViewGroup) null, false), R$drawable.ic_snooze);
    }

    /* access modifiers changed from: package-private */
    public static NotificationMenuItem createConversationItem(Context context) {
        return new NotificationMenuItem(context, context.getResources().getString(R$string.notification_menu_gear_description), (NotificationConversationInfo) LayoutInflater.from(context).inflate(R$layout.notification_conversation_info, (ViewGroup) null, false), R$drawable.ic_settings);
    }

    /* access modifiers changed from: package-private */
    public static NotificationMenuItem createPartialConversationItem(Context context) {
        return new NotificationMenuItem(context, context.getResources().getString(R$string.notification_menu_gear_description), (PartialConversationInfo) LayoutInflater.from(context).inflate(R$layout.partial_conversation_info, (ViewGroup) null, false), R$drawable.ic_settings);
    }

    /* access modifiers changed from: package-private */
    public static NotificationMenuItem createInfoItem(Context context) {
        return new NotificationMenuItem(context, context.getResources().getString(R$string.notification_menu_gear_description), (NotificationInfo) LayoutInflater.from(context).inflate(R$layout.notification_info, (ViewGroup) null, false), R$drawable.ic_settings);
    }

    static NotificationMenuRowPlugin.MenuItem createFeedbackItem(Context context) {
        return new NotificationMenuItem(context, null, (FeedbackInfo) LayoutInflater.from(context).inflate(R$layout.feedback_info, (ViewGroup) null, false), -1);
    }

    private void addMenuView(NotificationMenuRowPlugin.MenuItem menuItem, ViewGroup viewGroup) {
        View menuView = menuItem.getMenuView();
        if (menuView != null) {
            menuView.setAlpha(this.mAlpha);
            viewGroup.addView(menuView);
            menuView.setOnClickListener(this);
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) menuView.getLayoutParams();
            int i = this.mHorizSpaceForIcon;
            layoutParams.width = i;
            layoutParams.height = i;
            menuView.setLayoutParams(layoutParams);
        }
        this.mMenuItemsByView.put(menuView, menuItem);
    }

    @VisibleForTesting
    protected float getSnapBackThreshold() {
        return ((float) getSpaceForMenu()) - getMaximumSwipeDistance();
    }

    @VisibleForTesting
    protected float getDismissThreshold() {
        return ((float) getParent().getWidth()) * 0.6f;
    }

    @Override // com.android.systemui.plugins.statusbar.NotificationMenuRowPlugin
    public boolean isWithinSnapMenuThreshold() {
        float translation = getTranslation();
        float snapBackThreshold = getSnapBackThreshold();
        float dismissThreshold = getDismissThreshold();
        if (isMenuOnLeft()) {
            if (translation > snapBackThreshold && translation < dismissThreshold) {
                return true;
            }
        } else if (translation < (-snapBackThreshold) && translation > (-dismissThreshold)) {
            return true;
        }
        return false;
    }

    @Override // com.android.systemui.plugins.statusbar.NotificationMenuRowPlugin
    public boolean isSwipedEnoughToShowMenu() {
        float minimumSwipeDistance = getMinimumSwipeDistance();
        float translation = getTranslation();
        return isMenuVisible() && (!isMenuOnLeft() ? translation < (-minimumSwipeDistance) : translation > minimumSwipeDistance);
    }

    @Override // com.android.systemui.plugins.statusbar.NotificationMenuRowPlugin
    public int getMenuSnapTarget() {
        boolean isMenuOnLeft = isMenuOnLeft();
        int spaceForMenu = getSpaceForMenu();
        return isMenuOnLeft ? spaceForMenu : -spaceForMenu;
    }

    @Override // com.android.systemui.plugins.statusbar.NotificationMenuRowPlugin
    public boolean shouldSnapBack() {
        float translation = getTranslation();
        float snapBackThreshold = getSnapBackThreshold();
        if (isMenuOnLeft()) {
            if (translation < snapBackThreshold) {
                return true;
            }
        } else if (translation > (-snapBackThreshold)) {
            return true;
        }
        return false;
    }

    @Override // com.android.systemui.plugins.statusbar.NotificationMenuRowPlugin
    public boolean isSnappedAndOnSameSide() {
        return isMenuSnapped() && isMenuVisible() && isMenuSnappedOnLeft() == isMenuOnLeft();
    }

    @Override // com.android.systemui.plugins.statusbar.NotificationMenuRowPlugin
    public boolean canBeDismissed() {
        return getParent().canViewBeDismissed();
    }

    @Override // com.android.systemui.plugins.statusbar.NotificationMenuRowPlugin
    public void setDismissRtl(boolean z) {
        this.mDismissRtl = z;
        if (this.mMenuContainer != null) {
            createMenuViews(true);
        }
    }

    /* loaded from: classes.dex */
    public static class NotificationMenuItem implements NotificationMenuRowPlugin.MenuItem {
        String mContentDescription;
        NotificationGuts.GutsContent mGutsContent;
        View mMenuView;

        public NotificationMenuItem(Context context, String str, NotificationGuts.GutsContent gutsContent, int i) {
            Resources resources = context.getResources();
            int dimensionPixelSize = resources.getDimensionPixelSize(R$dimen.notification_menu_icon_padding);
            int color = resources.getColor(R$color.notification_gear_color);
            if (i >= 0) {
                AlphaOptimizedImageView alphaOptimizedImageView = new AlphaOptimizedImageView(context);
                alphaOptimizedImageView.setPadding(dimensionPixelSize, dimensionPixelSize, dimensionPixelSize, dimensionPixelSize);
                alphaOptimizedImageView.setImageDrawable(context.getResources().getDrawable(i));
                alphaOptimizedImageView.setColorFilter(color);
                alphaOptimizedImageView.setAlpha(1.0f);
                this.mMenuView = alphaOptimizedImageView;
            }
            this.mContentDescription = str;
            this.mGutsContent = gutsContent;
        }

        @Override // com.android.systemui.plugins.statusbar.NotificationMenuRowPlugin.MenuItem
        public View getMenuView() {
            return this.mMenuView;
        }

        @Override // com.android.systemui.plugins.statusbar.NotificationMenuRowPlugin.MenuItem
        public View getGutsView() {
            return this.mGutsContent.getContentView();
        }

        @Override // com.android.systemui.plugins.statusbar.NotificationMenuRowPlugin.MenuItem
        public String getContentDescription() {
            return this.mContentDescription;
        }
    }
}
