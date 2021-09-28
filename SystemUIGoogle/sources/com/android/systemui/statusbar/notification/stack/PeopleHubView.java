package com.android.systemui.statusbar.notification.stack;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.systemui.R$id;
import com.android.systemui.plugins.statusbar.NotificationMenuRowPlugin;
import com.android.systemui.statusbar.notification.row.StackScrollerDecorView;
import com.android.systemui.statusbar.notification.stack.PeopleHubView;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt___RangesKt;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt___SequencesKt;
/* compiled from: PeopleHubView.kt */
/* loaded from: classes.dex */
public final class PeopleHubView extends StackScrollerDecorView implements SwipeableView {
    private boolean canSwipe;
    private ViewGroup contents;
    private TextView label;
    private Sequence<?> personViewAdapters;

    @Override // com.android.systemui.statusbar.notification.stack.SwipeableView
    public NotificationMenuRowPlugin createMenu() {
        return null;
    }

    @Override // com.android.systemui.statusbar.notification.row.StackScrollerDecorView
    protected View findSecondaryView() {
        return null;
    }

    @Override // com.android.systemui.statusbar.notification.stack.SwipeableView
    public boolean hasFinishedInitialization() {
        return true;
    }

    @Override // com.android.systemui.statusbar.notification.row.StackScrollerDecorView, com.android.systemui.statusbar.notification.row.ExpandableView
    public boolean needsClippingToShelf() {
        return true;
    }

    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public PeopleHubView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(attributeSet, "attrs");
    }

    /* access modifiers changed from: protected */
    @Override // com.android.systemui.statusbar.notification.row.StackScrollerDecorView, android.view.View
    public void onFinishInflate() {
        View requireViewById = requireViewById(R$id.people_list);
        Intrinsics.checkNotNullExpressionValue(requireViewById, "requireViewById(R.id.people_list)");
        this.contents = (ViewGroup) requireViewById;
        View requireViewById2 = requireViewById(R$id.header_label);
        Intrinsics.checkNotNullExpressionValue(requireViewById2, "requireViewById(R.id.header_label)");
        this.label = (TextView) requireViewById2;
        ViewGroup viewGroup = this.contents;
        if (viewGroup != null) {
            this.personViewAdapters = CollectionsKt___CollectionsKt.asSequence(SequencesKt___SequencesKt.toList(SequencesKt___SequencesKt.mapNotNull(CollectionsKt___CollectionsKt.asSequence(RangesKt___RangesKt.until(0, viewGroup.getChildCount())), new Function1<Integer, PersonDataListenerImpl>(this) { // from class: com.android.systemui.statusbar.notification.stack.PeopleHubView$onFinishInflate$1
                final /* synthetic */ PeopleHubView this$0;

                /* access modifiers changed from: package-private */
                {
                    this.this$0 = r1;
                }

                /* Return type fixed from 'java.lang.Object' to match base method */
                /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object] */
                @Override // kotlin.jvm.functions.Function1
                public /* bridge */ /* synthetic */ PeopleHubView.PersonDataListenerImpl invoke(Integer num) {
                    return invoke(num.intValue());
                }

                public final PeopleHubView.PersonDataListenerImpl invoke(int i) {
                    ViewGroup access$getContents$p = PeopleHubView.access$getContents$p(this.this$0);
                    if (access$getContents$p != null) {
                        View childAt = access$getContents$p.getChildAt(i);
                        ImageView imageView = childAt instanceof ImageView ? (ImageView) childAt : null;
                        if (imageView == null) {
                            return null;
                        }
                        return new PeopleHubView.PersonDataListenerImpl(this.this$0, imageView);
                    }
                    Intrinsics.throwUninitializedPropertyAccessException("contents");
                    throw null;
                }
            })));
            super.onFinishInflate();
            setVisible(true, false);
            return;
        }
        Intrinsics.throwUninitializedPropertyAccessException("contents");
        throw null;
    }

    @Override // com.android.systemui.statusbar.notification.row.StackScrollerDecorView
    protected View findContentView() {
        ViewGroup viewGroup = this.contents;
        if (viewGroup != null) {
            return viewGroup;
        }
        Intrinsics.throwUninitializedPropertyAccessException("contents");
        throw null;
    }

    @Override // com.android.systemui.statusbar.notification.stack.SwipeableView
    public void resetTranslation() {
        setTranslationX(0.0f);
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView, com.android.systemui.statusbar.notification.stack.SwipeableView
    public void setTranslation(float f) {
        if (this.canSwipe) {
            super.setTranslation(f);
        }
    }

    public final boolean getCanSwipe() {
        return this.canSwipe;
    }

    /* access modifiers changed from: protected */
    @Override // com.android.systemui.statusbar.notification.row.ExpandableView
    public void applyContentTransformation(float f, float f2) {
        super.applyContentTransformation(f, f2);
        ViewGroup viewGroup = this.contents;
        if (viewGroup != null) {
            int childCount = viewGroup.getChildCount();
            if (childCount > 0) {
                int i = 0;
                while (true) {
                    int i2 = i + 1;
                    ViewGroup viewGroup2 = this.contents;
                    if (viewGroup2 != null) {
                        View childAt = viewGroup2.getChildAt(i);
                        childAt.setAlpha(f);
                        childAt.setTranslationY(f2);
                        if (i2 < childCount) {
                            i = i2;
                        } else {
                            return;
                        }
                    } else {
                        Intrinsics.throwUninitializedPropertyAccessException("contents");
                        throw null;
                    }
                }
            }
        } else {
            Intrinsics.throwUninitializedPropertyAccessException("contents");
            throw null;
        }
    }

    /* compiled from: PeopleHubView.kt */
    /* access modifiers changed from: private */
    /* loaded from: classes.dex */
    public final class PersonDataListenerImpl {
        private final ImageView avatarView;
        final /* synthetic */ PeopleHubView this$0;

        public PersonDataListenerImpl(PeopleHubView peopleHubView, ImageView imageView) {
            Intrinsics.checkNotNullParameter(peopleHubView, "this$0");
            Intrinsics.checkNotNullParameter(imageView, "avatarView");
            this.this$0 = peopleHubView;
            this.avatarView = imageView;
        }
    }
}
