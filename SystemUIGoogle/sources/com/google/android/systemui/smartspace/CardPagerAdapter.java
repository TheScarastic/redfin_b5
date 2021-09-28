package com.google.android.systemui.smartspace;

import android.app.smartspace.SmartspaceAction;
import android.app.smartspace.SmartspaceTarget;
import android.app.smartspace.SmartspaceTargetEvent;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.provider.CalendarContract;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.viewpager.widget.PagerAdapter;
import com.android.internal.graphics.ColorUtils;
import com.android.launcher3.icons.GraphicsUtils;
import com.android.systemui.bcsmartspace.R$layout;
import com.android.systemui.plugins.BcSmartspaceDataPlugin;
import com.google.android.systemui.smartspace.BcSmartspaceCardLoggingInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;
/* loaded from: classes2.dex */
public class CardPagerAdapter extends PagerAdapter {
    private int mCurrentTextColor;
    private BcSmartspaceDataPlugin mDataProvider;
    private int mPrimaryTextColor;
    private final View mRoot;
    private List<SmartspaceTarget> mSmartspaceTargets = new ArrayList();
    private final List<SmartspaceTarget> mTargetsExcludingMedia = new ArrayList();
    private final List<SmartspaceTarget> mMediaTargets = new ArrayList();
    private boolean mHasOnlyDefaultDateCard = false;
    private final SparseArray<ViewHolder> mHolders = new SparseArray<>();
    private float mDozeAmount = 0.0f;
    private int mDozeColor = -1;
    private String mDndDescription = null;
    private Drawable mDndImage = null;
    private String mNextAlarmDescription = null;
    private Drawable mNextAlarmImage = null;

    public CardPagerAdapter(View view) {
        this.mRoot = view;
        int attrColor = GraphicsUtils.getAttrColor(view.getContext(), 16842806);
        this.mPrimaryTextColor = attrColor;
        this.mCurrentTextColor = attrColor;
    }

    @Override // androidx.viewpager.widget.PagerAdapter
    public int getCount() {
        return this.mSmartspaceTargets.size();
    }

    @Override // androidx.viewpager.widget.PagerAdapter
    public boolean isViewFromObject(View view, Object obj) {
        return view == ((ViewHolder) obj).card;
    }

    @Override // androidx.viewpager.widget.PagerAdapter
    public void destroyItem(ViewGroup viewGroup, int i, Object obj) {
        ViewHolder viewHolder = (ViewHolder) obj;
        viewGroup.removeView(viewHolder.card);
        if (this.mHolders.get(i) == viewHolder) {
            this.mHolders.remove(i);
        }
    }

    /* access modifiers changed from: package-private */
    public BcSmartspaceCard getCardAtPosition(int i) {
        ViewHolder viewHolder = this.mHolders.get(i);
        if (viewHolder == null) {
            return null;
        }
        return viewHolder.card;
    }

    /* access modifiers changed from: package-private */
    public SmartspaceTarget getTargetAtPosition(int i) {
        if (i < 0 || i >= this.mSmartspaceTargets.size()) {
            return null;
        }
        return this.mSmartspaceTargets.get(i);
    }

    /* access modifiers changed from: package-private */
    public List<SmartspaceTarget> getTargets() {
        return this.mSmartspaceTargets;
    }

    @Override // androidx.viewpager.widget.PagerAdapter
    public int getItemPosition(Object obj) {
        ViewHolder viewHolder = (ViewHolder) obj;
        SmartspaceTarget targetAtPosition = getTargetAtPosition(viewHolder.position);
        if (viewHolder.target == targetAtPosition) {
            return -1;
        }
        if (targetAtPosition == null || getFeatureType(targetAtPosition) != getFeatureType(viewHolder.target) || !Objects.equals(targetAtPosition.getSmartspaceTargetId(), viewHolder.target.getSmartspaceTargetId())) {
            return -2;
        }
        viewHolder.target = targetAtPosition;
        onBindViewHolder(viewHolder);
        return -1;
    }

    @Override // androidx.viewpager.widget.PagerAdapter
    public ViewHolder instantiateItem(ViewGroup viewGroup, int i) {
        SmartspaceTarget smartspaceTarget = this.mSmartspaceTargets.get(i);
        BcSmartspaceCard createBaseCard = createBaseCard(viewGroup, getFeatureType(smartspaceTarget));
        ViewHolder viewHolder = new ViewHolder(i, createBaseCard, smartspaceTarget);
        onBindViewHolder(viewHolder);
        viewGroup.addView(createBaseCard);
        this.mHolders.put(i, viewHolder);
        return viewHolder;
    }

    private int getFeatureType(SmartspaceTarget smartspaceTarget) {
        List actionChips = smartspaceTarget.getActionChips();
        int featureType = smartspaceTarget.getFeatureType();
        if (actionChips == null || actionChips.isEmpty()) {
            return featureType;
        }
        if (featureType == 13 && actionChips.size() == 1) {
            return -2;
        }
        return -1;
    }

    private BcSmartspaceCard createBaseCard(ViewGroup viewGroup, int i) {
        int i2;
        int i3;
        if (i == -2) {
            i2 = R$layout.smartspace_card_at_store;
        } else if (i != 1) {
            i2 = R$layout.smartspace_card;
        } else {
            i2 = R$layout.smartspace_card_date;
        }
        LayoutInflater from = LayoutInflater.from(viewGroup.getContext());
        BcSmartspaceCard bcSmartspaceCard = (BcSmartspaceCard) from.inflate(i2, viewGroup, false);
        if (i == -2) {
            i3 = R$layout.smartspace_card_combination_at_store;
        } else if (i == -1) {
            i3 = R$layout.smartspace_card_combination;
        } else if (i == 3) {
            i3 = R$layout.smartspace_card_generic_landscape_image;
        } else if (i == 4) {
            i3 = R$layout.smartspace_card_flight;
        } else if (i == 9) {
            i3 = R$layout.smartspace_card_sports;
        } else if (i == 10) {
            i3 = R$layout.smartspace_card_weather_forecast;
        } else if (i != 13) {
            i3 = i != 14 ? 0 : R$layout.smartspace_card_loyalty;
        } else {
            i3 = R$layout.smartspace_card_shopping_list;
        }
        if (i3 != 0) {
            bcSmartspaceCard.setSecondaryCard((BcSmartspaceCardSecondary) from.inflate(i3, (ViewGroup) bcSmartspaceCard, false));
        }
        return bcSmartspaceCard;
    }

    private void onBindViewHolder(ViewHolder viewHolder) {
        CardPagerAdapter$$ExternalSyntheticLambda0 cardPagerAdapter$$ExternalSyntheticLambda0;
        SmartspaceTarget smartspaceTarget = this.mSmartspaceTargets.get(viewHolder.position);
        BcSmartspaceCard bcSmartspaceCard = viewHolder.card;
        BcSmartspaceCardLoggingInfo build = new BcSmartspaceCardLoggingInfo.Builder().setInstanceId(InstanceId.create(smartspaceTarget).intValue()).setLoggingCardType(BcSmartSpaceUtil.getLoggingCardType(smartspaceTarget.getFeatureType())).setDisplaySurface(BcSmartSpaceUtil.getLoggingDisplaySurface(this.mRoot.getContext().getPackageName(), this.mDozeAmount)).setRank(viewHolder.position).setCardinality(this.mSmartspaceTargets.size()).build();
        BcSmartspaceDataPlugin bcSmartspaceDataPlugin = this.mDataProvider;
        if (bcSmartspaceDataPlugin == null) {
            cardPagerAdapter$$ExternalSyntheticLambda0 = null;
        } else {
            Objects.requireNonNull(bcSmartspaceDataPlugin);
            cardPagerAdapter$$ExternalSyntheticLambda0 = new BcSmartspaceDataPlugin.SmartspaceEventNotifier() { // from class: com.google.android.systemui.smartspace.CardPagerAdapter$$ExternalSyntheticLambda0
                @Override // com.android.systemui.plugins.BcSmartspaceDataPlugin.SmartspaceEventNotifier
                public final void notifySmartspaceEvent(SmartspaceTargetEvent smartspaceTargetEvent) {
                    BcSmartspaceDataPlugin.this.notifySmartspaceEvent(smartspaceTargetEvent);
                }
            };
        }
        boolean z = true;
        if (this.mSmartspaceTargets.size() <= 1) {
            z = false;
        }
        bcSmartspaceCard.setSmartspaceTarget(smartspaceTarget, cardPagerAdapter$$ExternalSyntheticLambda0, build, z);
        bcSmartspaceCard.setPrimaryTextColor(this.mCurrentTextColor);
        bcSmartspaceCard.setDozeAmount(this.mDozeAmount);
        bcSmartspaceCard.setDnd(this.mDndImage, this.mDndDescription);
        bcSmartspaceCard.setNextAlarm(this.mNextAlarmImage, this.mNextAlarmDescription);
    }

    /* access modifiers changed from: package-private */
    public void setTargets(List<? extends Parcelable> list) {
        this.mTargetsExcludingMedia.clear();
        list.forEach(new Consumer() { // from class: com.google.android.systemui.smartspace.CardPagerAdapter$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                CardPagerAdapter.m663$r8$lambda$SRMUIR888hx7OlxM4EEkQ_BFA(CardPagerAdapter.this, (Parcelable) obj);
            }
        });
        boolean z = true;
        if (this.mTargetsExcludingMedia.isEmpty()) {
            this.mTargetsExcludingMedia.add(new SmartspaceTarget.Builder("date_card_794317_92634", new ComponentName(this.mRoot.getContext(), CardPagerAdapter.class), this.mRoot.getContext().getUser()).setFeatureType(1).setBaseAction(new SmartspaceAction.Builder(UUID.randomUUID().toString(), "unusedTitle").setIntent(getOpenCalendarIntent()).build()).build());
        }
        if (!(this.mTargetsExcludingMedia.size() == 1 && this.mTargetsExcludingMedia.get(0).getFeatureType() == 1)) {
            z = false;
        }
        this.mHasOnlyDefaultDateCard = z;
        updateTargetVisibility();
        notifyDataSetChanged();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$setTargets$0(Parcelable parcelable) {
        this.mTargetsExcludingMedia.add((SmartspaceTarget) parcelable);
    }

    private static Intent getOpenCalendarIntent() {
        return new Intent("android.intent.action.VIEW").setData(ContentUris.appendId(CalendarContract.CONTENT_URI.buildUpon().appendPath("time"), System.currentTimeMillis()).build()).addFlags(270532608);
    }

    /* access modifiers changed from: package-private */
    public void setDataProvider(BcSmartspaceDataPlugin bcSmartspaceDataPlugin) {
        this.mDataProvider = bcSmartspaceDataPlugin;
    }

    /* access modifiers changed from: package-private */
    public void setPrimaryTextColor(int i) {
        this.mPrimaryTextColor = i;
        setDozeAmount(this.mDozeAmount);
    }

    /* access modifiers changed from: package-private */
    public void setDozeAmount(float f) {
        this.mCurrentTextColor = ColorUtils.blendARGB(this.mPrimaryTextColor, this.mDozeColor, f);
        this.mDozeAmount = f;
        updateTargetVisibility();
        refreshCardColors();
    }

    /* access modifiers changed from: package-private */
    public float getDozeAmount() {
        return this.mDozeAmount;
    }

    /* access modifiers changed from: package-private */
    public void setDnd(Drawable drawable, String str) {
        this.mDndImage = drawable;
        this.mDndDescription = str;
        refreshCards();
    }

    /* access modifiers changed from: package-private */
    public void setNextAlarm(Drawable drawable, String str) {
        this.mNextAlarmImage = drawable;
        this.mNextAlarmDescription = str;
        refreshCards();
    }

    /* access modifiers changed from: package-private */
    public void setMediaTarget(SmartspaceTarget smartspaceTarget) {
        this.mMediaTargets.clear();
        if (smartspaceTarget != null) {
            this.mMediaTargets.add(smartspaceTarget);
        }
        updateTargetVisibility();
    }

    private void refreshCards() {
        for (int i = 0; i < this.mHolders.size(); i++) {
            onBindViewHolder(this.mHolders.get(i));
        }
    }

    private void refreshCardColors() {
        for (int i = 0; i < this.mHolders.size(); i++) {
            this.mHolders.get(i).card.setPrimaryTextColor(this.mCurrentTextColor);
            this.mHolders.get(i).card.setDozeAmount(this.mDozeAmount);
        }
    }

    private void updateTargetVisibility() {
        boolean z;
        if (this.mMediaTargets.isEmpty()) {
            this.mSmartspaceTargets = this.mTargetsExcludingMedia;
            notifyDataSetChanged();
            return;
        }
        float f = this.mDozeAmount;
        if (f == 0.0f || !(z = this.mHasOnlyDefaultDateCard)) {
            this.mSmartspaceTargets = this.mTargetsExcludingMedia;
            notifyDataSetChanged();
        } else if (f == 1.0f && z) {
            this.mSmartspaceTargets = this.mMediaTargets;
            notifyDataSetChanged();
        }
    }

    /* access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static class ViewHolder {
        public final BcSmartspaceCard card;
        public final int position;
        public SmartspaceTarget target;

        ViewHolder(int i, BcSmartspaceCard bcSmartspaceCard, SmartspaceTarget smartspaceTarget) {
            this.position = i;
            this.card = bcSmartspaceCard;
            this.target = smartspaceTarget;
        }
    }
}
