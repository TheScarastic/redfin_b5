package com.android.systemui.people;

import android.app.PendingIntent;
import android.app.people.ConversationStatus;
import android.app.people.PeopleSpaceTile;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.ImageDecoder;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Bundle;
import android.os.UserHandle;
import android.text.StaticLayout;
import android.text.TextUtils;
import android.util.IconDrawableFactory;
import android.util.Log;
import android.util.Pair;
import android.util.Size;
import android.util.SizeF;
import android.util.TypedValue;
import android.widget.RemoteViews;
import android.widget.TextView;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.core.math.MathUtils;
import com.android.internal.annotations.VisibleForTesting;
import com.android.launcher3.icons.FastBitmapDrawable;
import com.android.systemui.R$dimen;
import com.android.systemui.R$drawable;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
import com.android.systemui.R$string;
import com.android.systemui.people.widget.LaunchConversationActivity;
import com.android.systemui.people.widget.PeopleTileKey;
import java.io.IOException;
import java.text.NumberFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
/* loaded from: classes.dex */
public class PeopleTileViewHelper {
    private int mAppWidgetId;
    private Context mContext;
    private float mDensity;
    private int mHeight;
    private NumberFormat mIntegerFormat;
    private boolean mIsLeftToRight;
    private PeopleTileKey mKey;
    private int mLayoutSize = getLayoutSize();
    private Locale mLocale;
    private int mMediumVerticalPadding;
    private PeopleSpaceTile mTile;
    private int mWidth;
    private static final CharSequence EMOJI_CAKE = "🎂";
    private static final Pattern DOUBLE_EXCLAMATION_PATTERN = Pattern.compile("[!][!]+");
    private static final Pattern DOUBLE_QUESTION_PATTERN = Pattern.compile("[?][?]+");
    private static final Pattern ANY_DOUBLE_MARK_PATTERN = Pattern.compile("[!?][!?]+");
    private static final Pattern MIXED_MARK_PATTERN = Pattern.compile("![?].*|.*[?]!");
    private static final Pattern EMOJI_PATTERN = Pattern.compile("\\p{RI}\\p{RI}|(\\p{Emoji}(\\p{EMod}|\\x{FE0F}\\x{20E3}?|[\\x{E0020}-\\x{E007E}]+\\x{E007F})|[\\p{Emoji}&&\\p{So}])(\\x{200D}\\p{Emoji}(\\p{EMod}|\\x{FE0F}\\x{20E3}?|[\\x{E0020}-\\x{E007E}]+\\x{E007F})?)*");

    PeopleTileViewHelper(Context context, PeopleSpaceTile peopleSpaceTile, int i, int i2, int i3, PeopleTileKey peopleTileKey) {
        this.mContext = context;
        this.mTile = peopleSpaceTile;
        this.mKey = peopleTileKey;
        this.mAppWidgetId = i;
        this.mDensity = context.getResources().getDisplayMetrics().density;
        this.mWidth = i2;
        this.mHeight = i3;
        this.mIsLeftToRight = TextUtils.getLayoutDirectionFromLocale(Locale.getDefault()) == 0;
    }

    public static RemoteViews createRemoteViews(Context context, PeopleSpaceTile peopleSpaceTile, int i, Bundle bundle, PeopleTileKey peopleTileKey) {
        return new RemoteViews((Map) getWidgetSizes(context, bundle).stream().distinct().collect(Collectors.toMap(Function.identity(), new Function(context, peopleSpaceTile, i, peopleTileKey) { // from class: com.android.systemui.people.PeopleTileViewHelper$$ExternalSyntheticLambda1
            public final /* synthetic */ Context f$0;
            public final /* synthetic */ PeopleSpaceTile f$1;
            public final /* synthetic */ int f$2;
            public final /* synthetic */ PeopleTileKey f$3;

            {
                this.f$0 = r1;
                this.f$1 = r2;
                this.f$2 = r3;
                this.f$3 = r4;
            }

            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return PeopleTileViewHelper.lambda$createRemoteViews$0(this.f$0, this.f$1, this.f$2, this.f$3, (SizeF) obj);
            }
        })));
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ RemoteViews lambda$createRemoteViews$0(Context context, PeopleSpaceTile peopleSpaceTile, int i, PeopleTileKey peopleTileKey, SizeF sizeF) {
        return new PeopleTileViewHelper(context, peopleSpaceTile, i, (int) sizeF.getWidth(), (int) sizeF.getHeight(), peopleTileKey).getViews();
    }

    private static List<SizeF> getWidgetSizes(Context context, Bundle bundle) {
        float f = context.getResources().getDisplayMetrics().density;
        ArrayList parcelableArrayList = bundle.getParcelableArrayList("appWidgetSizes");
        if (parcelableArrayList != null && !parcelableArrayList.isEmpty()) {
            return parcelableArrayList;
        }
        int sizeInDp = getSizeInDp(context, R$dimen.default_width, f);
        int sizeInDp2 = getSizeInDp(context, R$dimen.default_height, f);
        ArrayList arrayList = new ArrayList(2);
        arrayList.add(new SizeF((float) bundle.getInt("appWidgetMinWidth", sizeInDp), (float) bundle.getInt("appWidgetMaxHeight", sizeInDp2)));
        arrayList.add(new SizeF((float) bundle.getInt("appWidgetMaxWidth", sizeInDp), (float) bundle.getInt("appWidgetMinHeight", sizeInDp2)));
        return arrayList;
    }

    @VisibleForTesting
    RemoteViews getViews() {
        RemoteViews viewForTile = getViewForTile();
        return setLaunchIntents(setCommonRemoteViewsFields(viewForTile, getMaxAvatarSize(viewForTile)));
    }

    private RemoteViews getViewForTile() {
        List<ConversationStatus> list;
        PeopleSpaceTile peopleSpaceTile = this.mTile;
        if (peopleSpaceTile == null || peopleSpaceTile.isPackageSuspended() || this.mTile.isUserQuieted()) {
            return createSuppressedView();
        }
        if (isDndBlockingTileData(this.mTile)) {
            return createDndRemoteViews().mRemoteViews;
        }
        if (Objects.equals(this.mTile.getNotificationCategory(), "missed_call")) {
            return createMissedCallRemoteViews();
        }
        if (this.mTile.getNotificationKey() != null) {
            return createNotificationRemoteViews();
        }
        if (this.mTile.getStatuses() == null) {
            list = Arrays.asList(new ConversationStatus[0]);
        } else {
            list = (List) this.mTile.getStatuses().stream().filter(new Predicate() { // from class: com.android.systemui.people.PeopleTileViewHelper$$ExternalSyntheticLambda3
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    return PeopleTileViewHelper.this.lambda$getViewForTile$1((ConversationStatus) obj);
                }
            }).collect(Collectors.toList());
        }
        ConversationStatus birthdayStatus = getBirthdayStatus(list);
        if (birthdayStatus != null) {
            return createStatusRemoteViews(birthdayStatus);
        }
        if (!list.isEmpty()) {
            return createStatusRemoteViews(list.stream().max(Comparator.comparing(PeopleTileViewHelper$$ExternalSyntheticLambda2.INSTANCE)).get());
        }
        return createLastInteractionRemoteViews();
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ Long lambda$getViewForTile$2(ConversationStatus conversationStatus) {
        return Long.valueOf(conversationStatus.getStartTimeMillis());
    }

    private static boolean isDndBlockingTileData(PeopleSpaceTile peopleSpaceTile) {
        if (peopleSpaceTile == null) {
            return false;
        }
        int notificationPolicyState = peopleSpaceTile.getNotificationPolicyState();
        if ((notificationPolicyState & 1) != 0) {
            return false;
        }
        if ((notificationPolicyState & 4) != 0 && peopleSpaceTile.isImportantConversation()) {
            return false;
        }
        if ((notificationPolicyState & 8) != 0 && peopleSpaceTile.getContactAffinity() == 1.0f) {
            return false;
        }
        if ((notificationPolicyState & 16) == 0 || (peopleSpaceTile.getContactAffinity() != 0.5f && peopleSpaceTile.getContactAffinity() != 1.0f)) {
            return !peopleSpaceTile.canBypassDnd();
        }
        return false;
    }

    private RemoteViews createSuppressedView() {
        RemoteViews remoteViews;
        PeopleSpaceTile peopleSpaceTile = this.mTile;
        if (peopleSpaceTile == null || !peopleSpaceTile.isUserQuieted()) {
            remoteViews = new RemoteViews(this.mContext.getPackageName(), R$layout.people_tile_suppressed_layout);
        } else {
            remoteViews = new RemoteViews(this.mContext.getPackageName(), R$layout.people_tile_work_profile_quiet_layout);
        }
        remoteViews.setImageViewBitmap(R$id.icon, convertDrawableToDisabledBitmap(this.mContext.getDrawable(R$drawable.ic_conversation_icon)));
        return remoteViews;
    }

    private void setMaxLines(RemoteViews remoteViews, boolean z) {
        int i;
        int i2;
        if (this.mLayoutSize == 2) {
            i2 = R$dimen.content_text_size_for_large;
            i = getLineHeightFromResource(R$dimen.name_text_size_for_large_content);
        } else {
            i2 = R$dimen.content_text_size_for_medium;
            i = getLineHeightFromResource(R$dimen.name_text_size_for_medium_content);
        }
        int max = Math.max(2, Math.floorDiv(getContentHeightForLayout(i, remoteViews.getLayoutId() == R$layout.people_tile_large_with_status_content), getLineHeightFromResource(i2)));
        if (z) {
            max--;
        }
        remoteViews.setInt(R$id.text_content, "setMaxLines", max);
    }

    private int getLineHeightFromResource(int i) {
        try {
            TextView textView = new TextView(this.mContext);
            textView.setTextSize(0, this.mContext.getResources().getDimension(i));
            textView.setTextAppearance(16974253);
            return (int) (((float) textView.getLineHeight()) / this.mDensity);
        } catch (Exception e) {
            Log.e("PeopleTileView", "Could not create text view: " + e);
            return this.getSizeInDp(R$dimen.content_text_size_for_medium);
        }
    }

    private int getSizeInDp(int i) {
        return getSizeInDp(this.mContext, i, this.mDensity);
    }

    public static int getSizeInDp(Context context, int i, float f) {
        return (int) (context.getResources().getDimension(i) / f);
    }

    private int getContentHeightForLayout(int i, boolean z) {
        int i2 = this.mLayoutSize;
        if (i2 == 1) {
            return this.mHeight - ((i + 12) + (this.mMediumVerticalPadding * 2));
        }
        if (i2 != 2) {
            return -1;
        }
        return this.mHeight - ((getSizeInDp(R$dimen.max_people_avatar_size_for_large_content) + i) + (z ? 76 : 62));
    }

    private int getLayoutSize() {
        if (this.mHeight >= getSizeInDp(R$dimen.required_height_for_large) && this.mWidth >= getSizeInDp(R$dimen.required_width_for_large)) {
            return 2;
        }
        if (this.mHeight < getSizeInDp(R$dimen.required_height_for_medium) || this.mWidth < getSizeInDp(R$dimen.required_width_for_medium)) {
            return 0;
        }
        this.mMediumVerticalPadding = Math.max(4, Math.min(Math.floorDiv(this.mHeight - ((getSizeInDp(R$dimen.avatar_size_for_medium) + 4) + getLineHeightFromResource(R$dimen.name_text_size_for_medium_content)), 2), 16));
        return 1;
    }

    private int getMaxAvatarSize(RemoteViews remoteViews) {
        int layoutId = remoteViews.getLayoutId();
        int i = R$dimen.avatar_size_for_medium;
        int sizeInDp = getSizeInDp(i);
        if (layoutId == R$layout.people_tile_medium_empty) {
            return getSizeInDp(R$dimen.max_people_avatar_size_for_large_content);
        }
        if (layoutId == R$layout.people_tile_medium_with_content) {
            return getSizeInDp(i);
        }
        if (layoutId == R$layout.people_tile_small) {
            sizeInDp = Math.min(this.mHeight - (Math.max(18, getLineHeightFromResource(R$dimen.name_text_size_for_small)) + 18), this.mWidth - 8);
        }
        if (layoutId == R$layout.people_tile_small_horizontal) {
            sizeInDp = Math.min(this.mHeight - 10, this.mWidth - 16);
        }
        if (layoutId == R$layout.people_tile_large_with_notification_content) {
            return Math.min(this.mHeight - ((getLineHeightFromResource(R$dimen.content_text_size_for_large) * 3) + 62), getSizeInDp(R$dimen.max_people_avatar_size_for_large_content));
        }
        if (layoutId == R$layout.people_tile_large_with_status_content) {
            return Math.min(this.mHeight - ((getLineHeightFromResource(R$dimen.content_text_size_for_large) * 3) + 76), getSizeInDp(R$dimen.max_people_avatar_size_for_large_content));
        }
        if (layoutId == R$layout.people_tile_large_empty) {
            sizeInDp = Math.min(this.mHeight - (((((getLineHeightFromResource(R$dimen.name_text_size_for_large) + 28) + getLineHeightFromResource(R$dimen.content_text_size_for_large)) + 16) + 10) + 16), this.mWidth - 28);
        }
        if (isDndBlockingTileData(this.mTile) && this.mLayoutSize != 0) {
            sizeInDp = createDndRemoteViews().mAvatarSize;
        }
        return Math.min(sizeInDp, getSizeInDp(R$dimen.max_people_avatar_size));
    }

    private RemoteViews setCommonRemoteViewsFields(RemoteViews remoteViews, int i) {
        int i2;
        try {
            PeopleSpaceTile peopleSpaceTile = this.mTile;
            if (peopleSpaceTile == null) {
                return remoteViews;
            }
            if (peopleSpaceTile.getStatuses() != null && this.mTile.getStatuses().stream().anyMatch(PeopleTileViewHelper$$ExternalSyntheticLambda5.INSTANCE)) {
                int i3 = R$id.availability;
                remoteViews.setViewVisibility(i3, 0);
                i2 = this.mContext.getResources().getDimensionPixelSize(R$dimen.availability_dot_shown_padding);
                remoteViews.setContentDescription(i3, this.mContext.getString(R$string.person_available));
            } else {
                remoteViews.setViewVisibility(R$id.availability, 8);
                i2 = this.mContext.getResources().getDimensionPixelSize(R$dimen.availability_dot_missing_padding);
            }
            boolean z = TextUtils.getLayoutDirectionFromLocale(Locale.getDefault()) == 0;
            remoteViews.setViewPadding(R$id.padding_before_availability, z ? i2 : 0, 0, z ? 0 : i2, 0);
            boolean hasNewStory = getHasNewStory(this.mTile);
            int i4 = R$id.person_icon;
            remoteViews.setImageViewBitmap(i4, getPersonIconBitmap(this.mContext, this.mTile, i, hasNewStory));
            if (hasNewStory) {
                remoteViews.setContentDescription(i4, this.mContext.getString(R$string.new_story_status_content_description, this.mTile.getUserName()));
            } else {
                remoteViews.setContentDescription(i4, null);
            }
            return remoteViews;
        } catch (Exception e) {
            Log.e("PeopleTileView", "Failed to set common fields: " + e);
            return remoteViews;
        }
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$setCommonRemoteViewsFields$3(ConversationStatus conversationStatus) {
        return conversationStatus.getAvailability() == 0;
    }

    private static boolean getHasNewStory(PeopleSpaceTile peopleSpaceTile) {
        return peopleSpaceTile.getStatuses() != null && peopleSpaceTile.getStatuses().stream().anyMatch(PeopleTileViewHelper$$ExternalSyntheticLambda4.INSTANCE);
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$getHasNewStory$4(ConversationStatus conversationStatus) {
        return conversationStatus.getActivity() == 3;
    }

    private RemoteViews setLaunchIntents(RemoteViews remoteViews) {
        if (PeopleTileKey.isValid(this.mKey) && this.mTile != null) {
            try {
                Intent intent = new Intent(this.mContext, LaunchConversationActivity.class);
                intent.addFlags(1350598656);
                intent.putExtra("extra_tile_id", this.mKey.getShortcutId());
                intent.putExtra("extra_package_name", this.mKey.getPackageName());
                intent.putExtra("extra_user_handle", new UserHandle(this.mKey.getUserId()));
                PeopleSpaceTile peopleSpaceTile = this.mTile;
                if (peopleSpaceTile != null) {
                    intent.putExtra("extra_notification_key", peopleSpaceTile.getNotificationKey());
                }
                remoteViews.setOnClickPendingIntent(16908288, PendingIntent.getActivity(this.mContext, this.mAppWidgetId, intent, 167772160));
                return remoteViews;
            } catch (Exception e) {
                Log.e("PeopleTileView", "Failed to add launch intents: " + e);
            }
        }
        return remoteViews;
    }

    private RemoteViewsAndSizes createDndRemoteViews() {
        int i;
        int i2;
        RemoteViews remoteViews = new RemoteViews(this.mContext.getPackageName(), getViewForDndRemoteViews());
        int sizeInDp = getSizeInDp(R$dimen.avatar_size_for_medium_empty);
        int sizeInDp2 = getSizeInDp(R$dimen.max_people_avatar_size);
        String string = this.mContext.getString(R$string.paused_by_dnd);
        int i3 = R$id.text_content;
        remoteViews.setTextViewText(i3, string);
        if (this.mLayoutSize == 2) {
            i = R$dimen.content_text_size_for_large;
        } else {
            i = R$dimen.content_text_size_for_medium;
        }
        remoteViews.setTextViewTextSize(i3, 0, this.mContext.getResources().getDimension(i));
        int lineHeightFromResource = getLineHeightFromResource(i);
        if (this.mLayoutSize == 1) {
            remoteViews.setInt(i3, "setMaxLines", (this.mHeight - 16) / lineHeightFromResource);
        } else {
            int dpToPx = dpToPx((float) 16);
            int dpToPx2 = dpToPx((float) 14);
            if (this.mLayoutSize == 0) {
                i2 = R$dimen.regular_predefined_icon;
            } else {
                i2 = R$dimen.largest_predefined_icon;
            }
            int sizeInDp3 = getSizeInDp(i2);
            int i4 = (this.mHeight - 32) - sizeInDp3;
            int sizeInDp4 = getSizeInDp(R$dimen.padding_between_suppressed_layout_items) * 2;
            int i5 = (i4 - sizeInDp) - sizeInDp4;
            int estimateTextHeight = estimateTextHeight(string, i, this.mWidth - 32);
            if (estimateTextHeight > i5 || this.mLayoutSize != 2) {
                if (this.mLayoutSize != 0) {
                    remoteViews = new RemoteViews(this.mContext.getPackageName(), R$layout.people_tile_small);
                }
                int maxAvatarSize = getMaxAvatarSize(remoteViews);
                remoteViews.setViewVisibility(R$id.messages_count, 8);
                remoteViews.setViewVisibility(R$id.name, 8);
                remoteViews.setContentDescription(R$id.predefined_icon, string);
                sizeInDp = maxAvatarSize;
            } else {
                remoteViews.setViewVisibility(i3, 0);
                remoteViews.setInt(i3, "setMaxLines", i5 / lineHeightFromResource);
                int i6 = R$id.predefined_icon;
                remoteViews.setContentDescription(i6, null);
                int clamp = MathUtils.clamp(Math.min(this.mWidth - 32, (i4 - estimateTextHeight) - sizeInDp4), dpToPx(10.0f), sizeInDp2);
                remoteViews.setViewPadding(16908288, dpToPx, dpToPx2, dpToPx, dpToPx);
                float f = (float) sizeInDp3;
                remoteViews.setViewLayoutWidth(i6, f, 1);
                remoteViews.setViewLayoutHeight(i6, f, 1);
                sizeInDp = clamp;
            }
            int i7 = R$id.predefined_icon;
            remoteViews.setViewVisibility(i7, 0);
            remoteViews.setImageViewResource(i7, R$drawable.ic_qs_dnd_on);
        }
        return new RemoteViewsAndSizes(remoteViews, sizeInDp);
    }

    private RemoteViews createMissedCallRemoteViews() {
        RemoteViews viewForContentLayout = setViewForContentLayout(new RemoteViews(this.mContext.getPackageName(), getLayoutForContent()));
        setPredefinedIconVisible(viewForContentLayout);
        int i = R$id.text_content;
        viewForContentLayout.setViewVisibility(i, 0);
        viewForContentLayout.setViewVisibility(R$id.messages_count, 8);
        setMaxLines(viewForContentLayout, false);
        CharSequence notificationContent = this.mTile.getNotificationContent();
        viewForContentLayout.setTextViewText(i, notificationContent);
        setContentDescriptionForNotificationTextContent(viewForContentLayout, notificationContent, this.mTile.getUserName());
        viewForContentLayout.setColorAttr(i, "setTextColor", 16844099);
        int i2 = R$id.predefined_icon;
        viewForContentLayout.setColorAttr(i2, "setColorFilter", 16844099);
        viewForContentLayout.setImageViewResource(i2, R$drawable.ic_phone_missed);
        if (this.mLayoutSize == 2) {
            viewForContentLayout.setInt(R$id.content, "setGravity", 80);
            int i3 = R$dimen.larger_predefined_icon;
            viewForContentLayout.setViewLayoutHeightDimen(i2, i3);
            viewForContentLayout.setViewLayoutWidthDimen(i2, i3);
        }
        setAvailabilityDotPadding(viewForContentLayout, R$dimen.availability_dot_notification_padding);
        return viewForContentLayout;
    }

    private void setPredefinedIconVisible(RemoteViews remoteViews) {
        remoteViews.setViewVisibility(R$id.predefined_icon, 0);
        if (this.mLayoutSize == 1) {
            int dimensionPixelSize = this.mContext.getResources().getDimensionPixelSize(R$dimen.before_predefined_icon_padding);
            int i = R$id.name;
            boolean z = this.mIsLeftToRight;
            remoteViews.setViewPadding(i, z ? 0 : dimensionPixelSize, 0, z ? dimensionPixelSize : 0, 0);
        }
    }

    private RemoteViews createNotificationRemoteViews() {
        CharSequence charSequence;
        RemoteViews viewForContentLayout = setViewForContentLayout(new RemoteViews(this.mContext.getPackageName(), getLayoutForNotificationContent()));
        CharSequence notificationSender = this.mTile.getNotificationSender();
        Uri notificationDataUri = this.mTile.getNotificationDataUri();
        if (notificationDataUri != null) {
            String string = this.mContext.getString(R$string.new_notification_image_content_description, this.mTile.getUserName());
            int i = R$id.image;
            viewForContentLayout.setContentDescription(i, string);
            viewForContentLayout.setViewVisibility(i, 0);
            viewForContentLayout.setViewVisibility(R$id.text_content, 8);
            try {
                viewForContentLayout.setImageViewBitmap(i, PeopleSpaceUtils.convertDrawableToBitmap(resolveImage(notificationDataUri, this.mContext)));
            } catch (IOException e) {
                Log.e("PeopleTileView", "Could not decode image: " + e);
                int i2 = R$id.text_content;
                viewForContentLayout.setTextViewText(i2, string);
                viewForContentLayout.setViewVisibility(i2, 0);
                viewForContentLayout.setViewVisibility(R$id.image, 8);
            }
        } else {
            setMaxLines(viewForContentLayout, !TextUtils.isEmpty(notificationSender));
            CharSequence notificationContent = this.mTile.getNotificationContent();
            if (notificationSender != null) {
                charSequence = notificationSender;
            } else {
                charSequence = this.mTile.getUserName();
            }
            setContentDescriptionForNotificationTextContent(viewForContentLayout, notificationContent, charSequence);
            viewForContentLayout = decorateBackground(viewForContentLayout, notificationContent);
            int i3 = R$id.text_content;
            viewForContentLayout.setColorAttr(i3, "setTextColor", 16842806);
            viewForContentLayout.setTextViewText(i3, this.mTile.getNotificationContent());
            if (this.mLayoutSize == 2) {
                viewForContentLayout.setViewPadding(R$id.name, 0, 0, 0, this.mContext.getResources().getDimensionPixelSize(R$dimen.above_notification_text_padding));
            }
            viewForContentLayout.setViewVisibility(R$id.image, 8);
            viewForContentLayout.setImageViewResource(R$id.predefined_icon, R$drawable.ic_message);
        }
        if (this.mTile.getMessagesCount() > 1) {
            if (this.mLayoutSize == 1) {
                int dimensionPixelSize = this.mContext.getResources().getDimensionPixelSize(R$dimen.before_messages_count_padding);
                int i4 = R$id.name;
                boolean z = this.mIsLeftToRight;
                viewForContentLayout.setViewPadding(i4, z ? 0 : dimensionPixelSize, 0, z ? dimensionPixelSize : 0, 0);
            }
            int i5 = R$id.messages_count;
            viewForContentLayout.setViewVisibility(i5, 0);
            viewForContentLayout.setTextViewText(i5, getMessagesCountText(this.mTile.getMessagesCount()));
            if (this.mLayoutSize == 0) {
                viewForContentLayout.setViewVisibility(R$id.predefined_icon, 8);
            }
        }
        if (!TextUtils.isEmpty(notificationSender)) {
            int i6 = R$id.subtext;
            viewForContentLayout.setViewVisibility(i6, 0);
            viewForContentLayout.setTextViewText(i6, notificationSender);
        } else {
            viewForContentLayout.setViewVisibility(R$id.subtext, 8);
        }
        setAvailabilityDotPadding(viewForContentLayout, R$dimen.availability_dot_notification_padding);
        return viewForContentLayout;
    }

    private Drawable resolveImage(Uri uri, Context context) throws IOException {
        return ImageDecoder.decodeDrawable(ImageDecoder.createSource(context.getContentResolver(), uri), new ImageDecoder.OnHeaderDecodedListener() { // from class: com.android.systemui.people.PeopleTileViewHelper$$ExternalSyntheticLambda0
            @Override // android.graphics.ImageDecoder.OnHeaderDecodedListener
            public final void onHeaderDecoded(ImageDecoder imageDecoder, ImageDecoder.ImageInfo imageInfo, ImageDecoder.Source source) {
                PeopleTileViewHelper.this.lambda$resolveImage$5(imageDecoder, imageInfo, source);
            }
        });
    }

    private static int getPowerOfTwoForSampleRatio(double d) {
        return Math.max(1, Integer.highestOneBit((int) Math.floor(d)));
    }

    /* access modifiers changed from: private */
    /* renamed from: onHeaderDecoded */
    public void lambda$resolveImage$5(ImageDecoder imageDecoder, ImageDecoder.ImageInfo imageInfo, ImageDecoder.Source source) {
        int applyDimension = (int) TypedValue.applyDimension(1, (float) this.mWidth, this.mContext.getResources().getDisplayMetrics());
        int applyDimension2 = (int) TypedValue.applyDimension(1, (float) this.mHeight, this.mContext.getResources().getDisplayMetrics());
        int max = Math.max(applyDimension, applyDimension2);
        int min = (int) (((double) Math.min(applyDimension, applyDimension2)) * 1.5d);
        if (min < max) {
            max = min;
        }
        Size size = imageInfo.getSize();
        int max2 = Math.max(size.getHeight(), size.getWidth());
        imageDecoder.setTargetSampleSize(getPowerOfTwoForSampleRatio(max2 > max ? (double) ((((float) max2) * 1.0f) / ((float) max)) : 1.0d));
    }

    private void setContentDescriptionForNotificationTextContent(RemoteViews remoteViews, CharSequence charSequence, CharSequence charSequence2) {
        remoteViews.setContentDescription(this.mLayoutSize == 0 ? R$id.predefined_icon : R$id.text_content, this.mContext.getString(R$string.new_notification_text_content_description, charSequence2, charSequence));
    }

    private String getMessagesCountText(int i) {
        if (i >= 6) {
            return this.mContext.getResources().getString(R$string.messages_count_overflow_indicator, 6);
        }
        Locale locale = this.mContext.getResources().getConfiguration().getLocales().get(0);
        if (!locale.equals(this.mLocale)) {
            this.mLocale = locale;
            this.mIntegerFormat = NumberFormat.getIntegerInstance(locale);
        }
        return this.mIntegerFormat.format((long) i);
    }

    private RemoteViews createStatusRemoteViews(ConversationStatus conversationStatus) {
        RemoteViews viewForContentLayout = setViewForContentLayout(new RemoteViews(this.mContext.getPackageName(), getLayoutForContent()));
        CharSequence description = conversationStatus.getDescription();
        if (TextUtils.isEmpty(description)) {
            description = getStatusTextByType(conversationStatus.getActivity());
        }
        setPredefinedIconVisible(viewForContentLayout);
        int i = R$id.text_content;
        viewForContentLayout.setTextViewText(i, description);
        if (conversationStatus.getActivity() == 1 || conversationStatus.getActivity() == 8) {
            setEmojiBackground(viewForContentLayout, EMOJI_CAKE);
        }
        Icon icon = conversationStatus.getIcon();
        if (icon != null) {
            viewForContentLayout.setViewVisibility(R$id.scrim_layout, 0);
            viewForContentLayout.setImageViewIcon(R$id.status_icon, icon);
            int i2 = this.mLayoutSize;
            if (i2 == 2) {
                viewForContentLayout.setInt(R$id.content, "setGravity", 80);
                viewForContentLayout.setViewVisibility(R$id.name, 8);
                viewForContentLayout.setColorAttr(i, "setTextColor", 16842806);
            } else if (i2 == 1) {
                viewForContentLayout.setViewVisibility(i, 8);
                viewForContentLayout.setTextViewText(R$id.name, description);
            }
        } else {
            viewForContentLayout.setColorAttr(i, "setTextColor", 16842808);
            setMaxLines(viewForContentLayout, false);
        }
        setAvailabilityDotPadding(viewForContentLayout, R$dimen.availability_dot_status_padding);
        int i3 = R$id.predefined_icon;
        viewForContentLayout.setImageViewResource(i3, getDrawableForStatus(conversationStatus));
        String string = this.mContext.getString(R$string.new_status_content_description, this.mTile.getUserName(), getContentDescriptionForStatus(conversationStatus));
        int i4 = this.mLayoutSize;
        if (i4 == 0) {
            viewForContentLayout.setContentDescription(i3, string);
        } else if (i4 == 1) {
            if (icon != null) {
                i = R$id.name;
            }
            viewForContentLayout.setContentDescription(i, string);
        } else if (i4 == 2) {
            viewForContentLayout.setContentDescription(i, string);
        }
        return viewForContentLayout;
    }

    private CharSequence getContentDescriptionForStatus(ConversationStatus conversationStatus) {
        CharSequence userName = this.mTile.getUserName();
        if (!TextUtils.isEmpty(conversationStatus.getDescription())) {
            return conversationStatus.getDescription();
        }
        switch (conversationStatus.getActivity()) {
            case 1:
                return this.mContext.getString(R$string.birthday_status_content_description, userName);
            case 2:
                return this.mContext.getString(R$string.anniversary_status_content_description, userName);
            case 3:
                return this.mContext.getString(R$string.new_story_status_content_description, userName);
            case 4:
                return this.mContext.getString(R$string.audio_status);
            case 5:
                return this.mContext.getString(R$string.video_status);
            case 6:
                return this.mContext.getString(R$string.game_status);
            case 7:
                return this.mContext.getString(R$string.location_status_content_description, userName);
            case 8:
                return this.mContext.getString(R$string.upcoming_birthday_status_content_description, userName);
            default:
                return "";
        }
    }

    private int getDrawableForStatus(ConversationStatus conversationStatus) {
        switch (conversationStatus.getActivity()) {
            case 1:
                return R$drawable.ic_cake;
            case 2:
                return R$drawable.ic_celebration;
            case 3:
                return R$drawable.ic_pages;
            case 4:
                return R$drawable.ic_music_note;
            case 5:
                return R$drawable.ic_video;
            case 6:
                return R$drawable.ic_play_games;
            case 7:
                return R$drawable.ic_location;
            case 8:
                return R$drawable.ic_gift;
            default:
                return R$drawable.ic_person;
        }
    }

    private void setAvailabilityDotPadding(RemoteViews remoteViews, int i) {
        int dimensionPixelSize = this.mContext.getResources().getDimensionPixelSize(i);
        int dimensionPixelSize2 = this.mContext.getResources().getDimensionPixelSize(R$dimen.medium_content_padding_above_name);
        int i2 = R$id.medium_content;
        boolean z = this.mIsLeftToRight;
        remoteViews.setViewPadding(i2, z ? dimensionPixelSize : 0, 0, z ? 0 : dimensionPixelSize, dimensionPixelSize2);
    }

    private ConversationStatus getBirthdayStatus(List<ConversationStatus> list) {
        Optional<ConversationStatus> findFirst = list.stream().filter(PeopleTileViewHelper$$ExternalSyntheticLambda6.INSTANCE).findFirst();
        if (findFirst.isPresent()) {
            return findFirst.get();
        }
        if (!TextUtils.isEmpty(this.mTile.getBirthdayText())) {
            return new ConversationStatus.Builder(this.mTile.getId(), 1).build();
        }
        return null;
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$getBirthdayStatus$6(ConversationStatus conversationStatus) {
        return conversationStatus.getActivity() == 1;
    }

    /* access modifiers changed from: private */
    /* renamed from: isStatusValidForEntireStatusView */
    public boolean lambda$getViewForTile$1(ConversationStatus conversationStatus) {
        int activity = conversationStatus.getActivity();
        if (activity == 1 || activity == 2 || !TextUtils.isEmpty(conversationStatus.getDescription()) || conversationStatus.getIcon() != null) {
            return true;
        }
        return false;
    }

    private String getStatusTextByType(int i) {
        switch (i) {
            case 1:
                return this.mContext.getString(R$string.birthday_status);
            case 2:
                return this.mContext.getString(R$string.anniversary_status);
            case 3:
                return this.mContext.getString(R$string.new_story_status);
            case 4:
                return this.mContext.getString(R$string.audio_status);
            case 5:
                return this.mContext.getString(R$string.video_status);
            case 6:
                return this.mContext.getString(R$string.game_status);
            case 7:
                return this.mContext.getString(R$string.location_status);
            case 8:
                return this.mContext.getString(R$string.upcoming_birthday_status);
            default:
                return "";
        }
    }

    private RemoteViews decorateBackground(RemoteViews remoteViews, CharSequence charSequence) {
        CharSequence doubleEmoji = getDoubleEmoji(charSequence);
        if (!TextUtils.isEmpty(doubleEmoji)) {
            setEmojiBackground(remoteViews, doubleEmoji);
            setPunctuationBackground(remoteViews, null);
            return remoteViews;
        }
        CharSequence doublePunctuation = getDoublePunctuation(charSequence);
        setEmojiBackground(remoteViews, null);
        setPunctuationBackground(remoteViews, doublePunctuation);
        return remoteViews;
    }

    private RemoteViews setEmojiBackground(RemoteViews remoteViews, CharSequence charSequence) {
        if (TextUtils.isEmpty(charSequence)) {
            remoteViews.setViewVisibility(R$id.emojis, 8);
            return remoteViews;
        }
        remoteViews.setTextViewText(R$id.emoji1, charSequence);
        remoteViews.setTextViewText(R$id.emoji2, charSequence);
        remoteViews.setTextViewText(R$id.emoji3, charSequence);
        remoteViews.setViewVisibility(R$id.emojis, 0);
        return remoteViews;
    }

    private RemoteViews setPunctuationBackground(RemoteViews remoteViews, CharSequence charSequence) {
        if (TextUtils.isEmpty(charSequence)) {
            remoteViews.setViewVisibility(R$id.punctuations, 8);
            return remoteViews;
        }
        remoteViews.setTextViewText(R$id.punctuation1, charSequence);
        remoteViews.setTextViewText(R$id.punctuation2, charSequence);
        remoteViews.setTextViewText(R$id.punctuation3, charSequence);
        remoteViews.setTextViewText(R$id.punctuation4, charSequence);
        remoteViews.setTextViewText(R$id.punctuation5, charSequence);
        remoteViews.setTextViewText(R$id.punctuation6, charSequence);
        remoteViews.setViewVisibility(R$id.punctuations, 0);
        return remoteViews;
    }

    @VisibleForTesting
    CharSequence getDoublePunctuation(CharSequence charSequence) {
        if (!ANY_DOUBLE_MARK_PATTERN.matcher(charSequence).find()) {
            return null;
        }
        if (MIXED_MARK_PATTERN.matcher(charSequence).find()) {
            return "!?";
        }
        Matcher matcher = DOUBLE_QUESTION_PATTERN.matcher(charSequence);
        if (!matcher.find()) {
            return "!";
        }
        Matcher matcher2 = DOUBLE_EXCLAMATION_PATTERN.matcher(charSequence);
        if (matcher2.find() && matcher.start() >= matcher2.start()) {
            return "!";
        }
        return "?";
    }

    @VisibleForTesting
    CharSequence getDoubleEmoji(CharSequence charSequence) {
        Matcher matcher = EMOJI_PATTERN.matcher(charSequence);
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            arrayList.add(new Pair(Integer.valueOf(start), Integer.valueOf(end)));
            arrayList2.add(charSequence.subSequence(start, end));
        }
        if (arrayList.size() < 2) {
            return null;
        }
        for (int i = 1; i < arrayList.size(); i++) {
            int i2 = i - 1;
            if (((Pair) arrayList.get(i)).first == ((Pair) arrayList.get(i2)).second && Objects.equals(arrayList2.get(i), arrayList2.get(i2))) {
                return (CharSequence) arrayList2.get(i);
            }
        }
        return null;
    }

    private RemoteViews setViewForContentLayout(RemoteViews remoteViews) {
        RemoteViews decorateBackground = decorateBackground(remoteViews, "");
        int i = R$id.predefined_icon;
        decorateBackground.setContentDescription(i, null);
        int i2 = R$id.text_content;
        decorateBackground.setContentDescription(i2, null);
        int i3 = R$id.name;
        decorateBackground.setContentDescription(i3, null);
        int i4 = R$id.image;
        decorateBackground.setContentDescription(i4, null);
        decorateBackground.setAccessibilityTraversalAfter(i2, i3);
        if (this.mLayoutSize == 0) {
            decorateBackground.setViewVisibility(i, 0);
            decorateBackground.setViewVisibility(i3, 8);
        } else {
            decorateBackground.setViewVisibility(i, 8);
            decorateBackground.setViewVisibility(i3, 0);
            decorateBackground.setViewVisibility(i2, 0);
            decorateBackground.setViewVisibility(R$id.subtext, 8);
            decorateBackground.setViewVisibility(i4, 8);
            decorateBackground.setViewVisibility(R$id.scrim_layout, 8);
        }
        if (this.mLayoutSize == 1) {
            int floor = (int) Math.floor((double) (this.mDensity * 16.0f));
            int floor2 = (int) Math.floor((double) (((float) this.mMediumVerticalPadding) * this.mDensity));
            decorateBackground.setViewPadding(R$id.content, floor, floor2, floor, floor2);
            decorateBackground.setViewPadding(i3, 0, 0, 0, 0);
            if (this.mHeight > ((int) (this.mContext.getResources().getDimension(R$dimen.medium_height_for_max_name_text_size) / this.mDensity))) {
                decorateBackground.setTextViewTextSize(i3, 0, (float) ((int) this.mContext.getResources().getDimension(R$dimen.max_name_text_size_for_medium)));
            }
        }
        if (this.mLayoutSize == 2) {
            decorateBackground.setViewPadding(i3, 0, 0, 0, this.mContext.getResources().getDimensionPixelSize(R$dimen.below_name_text_padding));
            decorateBackground.setInt(R$id.content, "setGravity", 48);
        }
        int i5 = R$dimen.regular_predefined_icon;
        decorateBackground.setViewLayoutHeightDimen(i, i5);
        decorateBackground.setViewLayoutWidthDimen(i, i5);
        decorateBackground.setViewVisibility(R$id.messages_count, 8);
        if (this.mTile.getUserName() != null) {
            decorateBackground.setTextViewText(i3, this.mTile.getUserName());
        }
        return decorateBackground;
    }

    private RemoteViews createLastInteractionRemoteViews() {
        RemoteViews remoteViews = new RemoteViews(this.mContext.getPackageName(), getEmptyLayout());
        int i = R$id.name;
        remoteViews.setInt(i, "setMaxLines", 1);
        if (this.mLayoutSize == 0) {
            remoteViews.setViewVisibility(i, 0);
            remoteViews.setViewVisibility(R$id.predefined_icon, 8);
            remoteViews.setViewVisibility(R$id.messages_count, 8);
        }
        if (this.mTile.getUserName() != null) {
            remoteViews.setTextViewText(i, this.mTile.getUserName());
        }
        String lastInteractionString = getLastInteractionString(this.mContext, this.mTile.getLastInteractionTimestamp());
        if (lastInteractionString != null) {
            int i2 = R$id.last_interaction;
            remoteViews.setViewVisibility(i2, 0);
            remoteViews.setTextViewText(i2, lastInteractionString);
        } else {
            remoteViews.setViewVisibility(R$id.last_interaction, 8);
            if (this.mLayoutSize == 1) {
                remoteViews.setInt(i, "setMaxLines", 3);
            }
        }
        return remoteViews;
    }

    private int getEmptyLayout() {
        int i = this.mLayoutSize;
        if (i == 1) {
            return R$layout.people_tile_medium_empty;
        }
        if (i != 2) {
            return getLayoutSmallByHeight();
        }
        return R$layout.people_tile_large_empty;
    }

    private int getLayoutForNotificationContent() {
        int i = this.mLayoutSize;
        if (i == 1) {
            return R$layout.people_tile_medium_with_content;
        }
        if (i != 2) {
            return getLayoutSmallByHeight();
        }
        return R$layout.people_tile_large_with_notification_content;
    }

    private int getLayoutForContent() {
        int i = this.mLayoutSize;
        if (i == 1) {
            return R$layout.people_tile_medium_with_content;
        }
        if (i != 2) {
            return getLayoutSmallByHeight();
        }
        return R$layout.people_tile_large_with_status_content;
    }

    private int getViewForDndRemoteViews() {
        int i = this.mLayoutSize;
        if (i == 1) {
            return R$layout.people_tile_with_suppression_detail_content_horizontal;
        }
        if (i != 2) {
            return getLayoutSmallByHeight();
        }
        return R$layout.people_tile_with_suppression_detail_content_vertical;
    }

    private int getLayoutSmallByHeight() {
        if (this.mHeight >= getSizeInDp(R$dimen.required_height_for_medium)) {
            return R$layout.people_tile_small;
        }
        return R$layout.people_tile_small_horizontal;
    }

    public static Bitmap getPersonIconBitmap(Context context, PeopleSpaceTile peopleSpaceTile, int i) {
        return getPersonIconBitmap(context, peopleSpaceTile, i, getHasNewStory(peopleSpaceTile));
    }

    private static Bitmap getPersonIconBitmap(Context context, PeopleSpaceTile peopleSpaceTile, int i, boolean z) {
        Icon userIcon = peopleSpaceTile.getUserIcon();
        if (userIcon == null) {
            return convertDrawableToDisabledBitmap(context.getDrawable(R$drawable.ic_avatar_with_badge));
        }
        Drawable peopleTileDrawable = new PeopleStoryIconFactory(context, context.getPackageManager(), IconDrawableFactory.newInstance(context, false), i).getPeopleTileDrawable(RoundedBitmapDrawableFactory.create(context.getResources(), userIcon.getBitmap()), peopleSpaceTile.getPackageName(), PeopleSpaceUtils.getUserId(peopleSpaceTile), peopleSpaceTile.isImportantConversation(), z);
        if (isDndBlockingTileData(peopleSpaceTile)) {
            ColorMatrix colorMatrix = new ColorMatrix();
            colorMatrix.setSaturation(0.0f);
            peopleTileDrawable.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        }
        return PeopleSpaceUtils.convertDrawableToBitmap(peopleTileDrawable);
    }

    public static String getLastInteractionString(Context context, long j) {
        if (j == 0) {
            Log.e("PeopleTileView", "Could not get valid last interaction");
            return null;
        }
        Duration ofMillis = Duration.ofMillis(System.currentTimeMillis() - j);
        if (ofMillis.toDays() <= 1) {
            return null;
        }
        if (ofMillis.toDays() < 7) {
            return context.getString(R$string.days_timestamp, Long.valueOf(ofMillis.toDays()));
        }
        if (ofMillis.toDays() == 7) {
            return context.getString(R$string.one_week_timestamp);
        }
        if (ofMillis.toDays() < 14) {
            return context.getString(R$string.over_one_week_timestamp);
        }
        if (ofMillis.toDays() == 14) {
            return context.getString(R$string.two_weeks_timestamp);
        }
        return context.getString(R$string.over_two_weeks_timestamp);
    }

    private int estimateTextHeight(CharSequence charSequence, int i, int i2) {
        StaticLayout buildStaticLayout = buildStaticLayout(charSequence, i, i2);
        if (buildStaticLayout == null) {
            return Integer.MAX_VALUE;
        }
        return pxToDp((float) buildStaticLayout.getHeight());
    }

    private StaticLayout buildStaticLayout(CharSequence charSequence, int i, int i2) {
        try {
            TextView textView = new TextView(this.mContext);
            textView.setTextSize(0, this.mContext.getResources().getDimension(i));
            textView.setTextAppearance(16974253);
            return StaticLayout.Builder.obtain(charSequence, 0, charSequence.length(), textView.getPaint(), dpToPx((float) i2)).setBreakStrategy(0).build();
        } catch (Exception e) {
            Log.e("PeopleTileView", "Could not create static layout: " + e);
            return null;
        }
    }

    private int dpToPx(float f) {
        return (int) (f * this.mDensity);
    }

    private int pxToDp(float f) {
        return (int) (f / this.mDensity);
    }

    /* access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class RemoteViewsAndSizes {
        final int mAvatarSize;
        final RemoteViews mRemoteViews;

        RemoteViewsAndSizes(RemoteViews remoteViews, int i) {
            this.mRemoteViews = remoteViews;
            this.mAvatarSize = i;
        }
    }

    private static Bitmap convertDrawableToDisabledBitmap(Drawable drawable) {
        FastBitmapDrawable fastBitmapDrawable = new FastBitmapDrawable(PeopleSpaceUtils.convertDrawableToBitmap(drawable));
        fastBitmapDrawable.setIsDisabled(true);
        return PeopleSpaceUtils.convertDrawableToBitmap(fastBitmapDrawable);
    }
}
