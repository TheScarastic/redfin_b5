package com.android.systemui.media;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
import com.android.systemui.util.animation.TransitionLayout;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.collections.SetsKt__SetsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: RecommendationViewHolder.kt */
/* loaded from: classes.dex */
public final class RecommendationViewHolder {
    public static final Companion Companion = new Companion(null);
    private static final Set<Integer> controlsIds = SetsKt__SetsKt.setOf((Object[]) new Integer[]{Integer.valueOf(R$id.recommendation_card_icon), Integer.valueOf(R$id.recommendation_card_text), Integer.valueOf(R$id.media_cover1), Integer.valueOf(R$id.media_cover2), Integer.valueOf(R$id.media_cover3), Integer.valueOf(R$id.media_cover4), Integer.valueOf(R$id.media_cover5), Integer.valueOf(R$id.media_cover6), Integer.valueOf(R$id.media_cover1_container), Integer.valueOf(R$id.media_cover2_container), Integer.valueOf(R$id.media_cover3_container), Integer.valueOf(R$id.media_cover4_container), Integer.valueOf(R$id.media_cover5_container), Integer.valueOf(R$id.media_cover6_container)});
    private static final Set<Integer> gutsIds = SetsKt__SetsKt.setOf((Object[]) new Integer[]{Integer.valueOf(R$id.remove_text), Integer.valueOf(R$id.cancel), Integer.valueOf(R$id.dismiss), Integer.valueOf(R$id.settings)});
    private final View cancel;
    private final ImageView cardIcon;
    private final TextView cardText;
    private final ViewGroup dismiss;
    private final View dismissLabel;
    private final TextView longPressText;
    private final List<ViewGroup> mediaCoverContainers;
    private final List<Integer> mediaCoverContainersResIds;
    private final List<ImageView> mediaCoverItems;
    private final List<Integer> mediaCoverItemsResIds;
    private final TransitionLayout recommendations;
    private final View settings;
    private final TextView settingsText;

    public /* synthetic */ RecommendationViewHolder(View view, DefaultConstructorMarker defaultConstructorMarker) {
        this(view);
    }

    private RecommendationViewHolder(View view) {
        TransitionLayout transitionLayout = (TransitionLayout) view;
        this.recommendations = transitionLayout;
        this.cardIcon = (ImageView) view.requireViewById(R$id.recommendation_card_icon);
        this.cardText = (TextView) view.requireViewById(R$id.recommendation_card_text);
        int i = R$id.media_cover1;
        View requireViewById = view.requireViewById(i);
        Intrinsics.checkNotNullExpressionValue(requireViewById, "itemView.requireViewById(R.id.media_cover1)");
        int i2 = R$id.media_cover2;
        View requireViewById2 = view.requireViewById(i2);
        Intrinsics.checkNotNullExpressionValue(requireViewById2, "itemView.requireViewById(R.id.media_cover2)");
        int i3 = R$id.media_cover3;
        View requireViewById3 = view.requireViewById(i3);
        Intrinsics.checkNotNullExpressionValue(requireViewById3, "itemView.requireViewById(R.id.media_cover3)");
        int i4 = R$id.media_cover4;
        View requireViewById4 = view.requireViewById(i4);
        Intrinsics.checkNotNullExpressionValue(requireViewById4, "itemView.requireViewById(R.id.media_cover4)");
        int i5 = R$id.media_cover5;
        View requireViewById5 = view.requireViewById(i5);
        Intrinsics.checkNotNullExpressionValue(requireViewById5, "itemView.requireViewById(R.id.media_cover5)");
        int i6 = R$id.media_cover6;
        View requireViewById6 = view.requireViewById(i6);
        Intrinsics.checkNotNullExpressionValue(requireViewById6, "itemView.requireViewById(R.id.media_cover6)");
        this.mediaCoverItems = CollectionsKt__CollectionsKt.listOf((Object[]) new ImageView[]{(ImageView) requireViewById, (ImageView) requireViewById2, (ImageView) requireViewById3, (ImageView) requireViewById4, (ImageView) requireViewById5, (ImageView) requireViewById6});
        int i7 = R$id.media_cover1_container;
        View requireViewById7 = view.requireViewById(i7);
        Intrinsics.checkNotNullExpressionValue(requireViewById7, "itemView.requireViewById(R.id.media_cover1_container)");
        int i8 = R$id.media_cover2_container;
        View requireViewById8 = view.requireViewById(i8);
        Intrinsics.checkNotNullExpressionValue(requireViewById8, "itemView.requireViewById(R.id.media_cover2_container)");
        int i9 = R$id.media_cover3_container;
        View requireViewById9 = view.requireViewById(i9);
        Intrinsics.checkNotNullExpressionValue(requireViewById9, "itemView.requireViewById(R.id.media_cover3_container)");
        int i10 = R$id.media_cover4_container;
        View requireViewById10 = view.requireViewById(i10);
        Intrinsics.checkNotNullExpressionValue(requireViewById10, "itemView.requireViewById(R.id.media_cover4_container)");
        int i11 = R$id.media_cover5_container;
        View requireViewById11 = view.requireViewById(i11);
        Intrinsics.checkNotNullExpressionValue(requireViewById11, "itemView.requireViewById(R.id.media_cover5_container)");
        int i12 = R$id.media_cover6_container;
        View requireViewById12 = view.requireViewById(i12);
        Intrinsics.checkNotNullExpressionValue(requireViewById12, "itemView.requireViewById(R.id.media_cover6_container)");
        this.mediaCoverContainers = CollectionsKt__CollectionsKt.listOf((Object[]) new ViewGroup[]{(ViewGroup) requireViewById7, (ViewGroup) requireViewById8, (ViewGroup) requireViewById9, (ViewGroup) requireViewById10, (ViewGroup) requireViewById11, (ViewGroup) requireViewById12});
        this.mediaCoverItemsResIds = CollectionsKt__CollectionsKt.listOf((Object[]) new Integer[]{Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(i3), Integer.valueOf(i4), Integer.valueOf(i5), Integer.valueOf(i6)});
        this.mediaCoverContainersResIds = CollectionsKt__CollectionsKt.listOf((Object[]) new Integer[]{Integer.valueOf(i7), Integer.valueOf(i8), Integer.valueOf(i9), Integer.valueOf(i10), Integer.valueOf(i11), Integer.valueOf(i12)});
        this.longPressText = (TextView) view.requireViewById(R$id.remove_text);
        this.cancel = view.requireViewById(R$id.cancel);
        ViewGroup viewGroup = (ViewGroup) view.requireViewById(R$id.dismiss);
        this.dismiss = viewGroup;
        this.dismissLabel = viewGroup.getChildAt(0);
        this.settings = view.requireViewById(R$id.settings);
        this.settingsText = (TextView) view.requireViewById(R$id.settings_text);
        Drawable background = transitionLayout.getBackground();
        Objects.requireNonNull(background, "null cannot be cast to non-null type com.android.systemui.media.IlluminationDrawable");
        IlluminationDrawable illuminationDrawable = (IlluminationDrawable) background;
        for (ViewGroup viewGroup2 : getMediaCoverContainers()) {
            illuminationDrawable.registerLightSource(viewGroup2);
        }
        View cancel = getCancel();
        Intrinsics.checkNotNullExpressionValue(cancel, "cancel");
        illuminationDrawable.registerLightSource(cancel);
        ViewGroup dismiss = getDismiss();
        Intrinsics.checkNotNullExpressionValue(dismiss, "dismiss");
        illuminationDrawable.registerLightSource(dismiss);
        View dismissLabel = getDismissLabel();
        Intrinsics.checkNotNullExpressionValue(dismissLabel, "dismissLabel");
        illuminationDrawable.registerLightSource(dismissLabel);
        View settings = getSettings();
        Intrinsics.checkNotNullExpressionValue(settings, "settings");
        illuminationDrawable.registerLightSource(settings);
    }

    public final TransitionLayout getRecommendations() {
        return this.recommendations;
    }

    public final ImageView getCardIcon() {
        return this.cardIcon;
    }

    public final TextView getCardText() {
        return this.cardText;
    }

    public final List<ImageView> getMediaCoverItems() {
        return this.mediaCoverItems;
    }

    public final List<ViewGroup> getMediaCoverContainers() {
        return this.mediaCoverContainers;
    }

    public final List<Integer> getMediaCoverItemsResIds() {
        return this.mediaCoverItemsResIds;
    }

    public final List<Integer> getMediaCoverContainersResIds() {
        return this.mediaCoverContainersResIds;
    }

    public final TextView getLongPressText() {
        return this.longPressText;
    }

    public final View getCancel() {
        return this.cancel;
    }

    public final ViewGroup getDismiss() {
        return this.dismiss;
    }

    public final View getDismissLabel() {
        return this.dismissLabel;
    }

    public final View getSettings() {
        return this.settings;
    }

    public final TextView getSettingsText() {
        return this.settingsText;
    }

    public final void marquee(boolean z, long j) {
        this.longPressText.getHandler().postDelayed(new Runnable(this, z) { // from class: com.android.systemui.media.RecommendationViewHolder$marquee$1
            final /* synthetic */ boolean $start;
            final /* synthetic */ RecommendationViewHolder this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
                this.$start = r2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                this.this$0.getLongPressText().setSelected(this.$start);
            }
        }, j);
    }

    /* compiled from: RecommendationViewHolder.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final RecommendationViewHolder create(LayoutInflater layoutInflater, ViewGroup viewGroup) {
            Intrinsics.checkNotNullParameter(layoutInflater, "inflater");
            Intrinsics.checkNotNullParameter(viewGroup, "parent");
            View inflate = layoutInflater.inflate(R$layout.media_smartspace_recommendations, viewGroup, false);
            inflate.setLayoutDirection(3);
            Intrinsics.checkNotNullExpressionValue(inflate, "itemView");
            return new RecommendationViewHolder(inflate, null);
        }

        public final Set<Integer> getControlsIds() {
            return RecommendationViewHolder.controlsIds;
        }

        public final Set<Integer> getGutsIds() {
            return RecommendationViewHolder.gutsIds;
        }
    }
}
