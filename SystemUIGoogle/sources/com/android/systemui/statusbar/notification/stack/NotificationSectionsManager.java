package com.android.systemui.statusbar.notification.stack;

import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.R$layout;
import com.android.systemui.media.KeyguardMediaController;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.notification.NotificationSectionsFeatureManager;
import com.android.systemui.statusbar.notification.collection.render.SectionHeaderController;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.ExpandableView;
import com.android.systemui.statusbar.notification.row.StackScrollerDecorView;
import com.android.systemui.statusbar.notification.stack.NotificationSectionsManager;
import com.android.systemui.statusbar.notification.stack.StackScrollAlgorithm;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.util.ConvenienceExtensionsKt;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import kotlin.NoWhenBranchMatchedException;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.collections.Grouping;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.Sequence;
/* compiled from: NotificationSectionsManager.kt */
/* loaded from: classes.dex */
public final class NotificationSectionsManager implements StackScrollAlgorithm.SectionProvider {
    public static final Companion Companion = new Companion(null);
    private final SectionHeaderController alertingHeaderController;
    private final ConfigurationController configurationController;
    private final NotificationSectionsManager$configurationListener$1 configurationListener = new NotificationSectionsManager$configurationListener$1(this);
    private final SectionHeaderController incomingHeaderController;
    private boolean initialized;
    private final KeyguardMediaController keyguardMediaController;
    private final NotificationSectionsLogger logger;
    private MediaHeaderView mediaControlsView;
    private NotificationStackScrollLayout parent;
    private final SectionHeaderController peopleHeaderController;
    private final NotificationSectionsFeatureManager sectionsFeatureManager;
    private final SectionHeaderController silentHeaderController;
    private final StatusBarStateController statusBarStateController;

    /* compiled from: NotificationSectionsManager.kt */
    /* access modifiers changed from: private */
    /* loaded from: classes.dex */
    public interface SectionUpdateState<T extends ExpandableView> {
        void adjustViewPosition();

        Integer getCurrentPosition();

        Integer getTargetPosition();

        void setCurrentPosition(Integer num);

        void setTargetPosition(Integer num);
    }

    @VisibleForTesting
    public static /* synthetic */ void getAlertingHeaderView$annotations() {
    }

    @VisibleForTesting
    public static /* synthetic */ void getIncomingHeaderView$annotations() {
    }

    @VisibleForTesting
    public static /* synthetic */ void getPeopleHeaderView$annotations() {
    }

    @VisibleForTesting
    public static /* synthetic */ void getSilentHeaderView$annotations() {
    }

    public NotificationSectionsManager(StatusBarStateController statusBarStateController, ConfigurationController configurationController, KeyguardMediaController keyguardMediaController, NotificationSectionsFeatureManager notificationSectionsFeatureManager, NotificationSectionsLogger notificationSectionsLogger, SectionHeaderController sectionHeaderController, SectionHeaderController sectionHeaderController2, SectionHeaderController sectionHeaderController3, SectionHeaderController sectionHeaderController4) {
        Intrinsics.checkNotNullParameter(statusBarStateController, "statusBarStateController");
        Intrinsics.checkNotNullParameter(configurationController, "configurationController");
        Intrinsics.checkNotNullParameter(keyguardMediaController, "keyguardMediaController");
        Intrinsics.checkNotNullParameter(notificationSectionsFeatureManager, "sectionsFeatureManager");
        Intrinsics.checkNotNullParameter(notificationSectionsLogger, "logger");
        Intrinsics.checkNotNullParameter(sectionHeaderController, "incomingHeaderController");
        Intrinsics.checkNotNullParameter(sectionHeaderController2, "peopleHeaderController");
        Intrinsics.checkNotNullParameter(sectionHeaderController3, "alertingHeaderController");
        Intrinsics.checkNotNullParameter(sectionHeaderController4, "silentHeaderController");
        this.statusBarStateController = statusBarStateController;
        this.configurationController = configurationController;
        this.keyguardMediaController = keyguardMediaController;
        this.sectionsFeatureManager = notificationSectionsFeatureManager;
        this.logger = notificationSectionsLogger;
        this.incomingHeaderController = sectionHeaderController;
        this.peopleHeaderController = sectionHeaderController2;
        this.alertingHeaderController = sectionHeaderController3;
        this.silentHeaderController = sectionHeaderController4;
    }

    public final SectionHeaderView getSilentHeaderView() {
        return this.silentHeaderController.getHeaderView();
    }

    public final SectionHeaderView getAlertingHeaderView() {
        return this.alertingHeaderController.getHeaderView();
    }

    public final SectionHeaderView getIncomingHeaderView() {
        return this.incomingHeaderController.getHeaderView();
    }

    public final SectionHeaderView getPeopleHeaderView() {
        return this.peopleHeaderController.getHeaderView();
    }

    @VisibleForTesting
    public final MediaHeaderView getMediaControlsView() {
        return this.mediaControlsView;
    }

    public final void initialize(NotificationStackScrollLayout notificationStackScrollLayout, LayoutInflater layoutInflater) {
        Intrinsics.checkNotNullParameter(notificationStackScrollLayout, "parent");
        Intrinsics.checkNotNullParameter(layoutInflater, "layoutInflater");
        if (!this.initialized) {
            this.initialized = true;
            this.parent = notificationStackScrollLayout;
            reinflateViews(layoutInflater);
            this.configurationController.addCallback(this.configurationListener);
            return;
        }
        throw new IllegalStateException("NotificationSectionsManager already initialized".toString());
    }

    /* JADX WARNING: Removed duplicated region for block: B:21:0x0036  */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x0051  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final <T extends com.android.systemui.statusbar.notification.row.ExpandableView> T reinflateView(T r6, android.view.LayoutInflater r7, int r8) {
        /*
            r5 = this;
            r0 = -1
            r1 = 0
            java.lang.String r2 = "parent"
            if (r6 != 0) goto L_0x0008
        L_0x0006:
            r3 = r0
            goto L_0x0032
        L_0x0008:
            android.view.ViewGroup r3 = r6.getTransientContainer()
            if (r3 != 0) goto L_0x000f
            goto L_0x0012
        L_0x000f:
            r3.removeView(r6)
        L_0x0012:
            android.view.ViewParent r3 = r6.getParent()
            com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout r4 = r5.parent
            if (r4 == 0) goto L_0x0055
            if (r3 != r4) goto L_0x0006
            if (r4 == 0) goto L_0x002e
            int r3 = r4.indexOfChild(r6)
            com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout r4 = r5.parent
            if (r4 == 0) goto L_0x002a
            r4.removeView(r6)
            goto L_0x0032
        L_0x002a:
            kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException(r2)
            throw r1
        L_0x002e:
            kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException(r2)
            throw r1
        L_0x0032:
            com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout r6 = r5.parent
            if (r6 == 0) goto L_0x0051
            r4 = 0
            android.view.View r6 = r7.inflate(r8, r6, r4)
            java.lang.String r7 = "null cannot be cast to non-null type T of com.android.systemui.statusbar.notification.stack.NotificationSectionsManager.reinflateView"
            java.util.Objects.requireNonNull(r6, r7)
            com.android.systemui.statusbar.notification.row.ExpandableView r6 = (com.android.systemui.statusbar.notification.row.ExpandableView) r6
            if (r3 == r0) goto L_0x0050
            com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout r5 = r5.parent
            if (r5 == 0) goto L_0x004c
            r5.addView(r6, r3)
            goto L_0x0050
        L_0x004c:
            kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException(r2)
            throw r1
        L_0x0050:
            return r6
        L_0x0051:
            kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException(r2)
            throw r1
        L_0x0055:
            kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException(r2)
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.notification.stack.NotificationSectionsManager.reinflateView(com.android.systemui.statusbar.notification.row.ExpandableView, android.view.LayoutInflater, int):com.android.systemui.statusbar.notification.row.ExpandableView");
    }

    public final NotificationSection[] createSectionsForBuckets() {
        int[] notificationBuckets = this.sectionsFeatureManager.getNotificationBuckets();
        ArrayList arrayList = new ArrayList(notificationBuckets.length);
        for (int i : notificationBuckets) {
            NotificationStackScrollLayout notificationStackScrollLayout = this.parent;
            if (notificationStackScrollLayout != null) {
                arrayList.add(new NotificationSection(notificationStackScrollLayout, i));
            } else {
                Intrinsics.throwUninitializedPropertyAccessException("parent");
                throw null;
            }
        }
        Object[] array = arrayList.toArray(new NotificationSection[0]);
        Objects.requireNonNull(array, "null cannot be cast to non-null type kotlin.Array<T>");
        return (NotificationSection[]) array;
    }

    public final void reinflateViews(LayoutInflater layoutInflater) {
        Intrinsics.checkNotNullParameter(layoutInflater, "layoutInflater");
        SectionHeaderController sectionHeaderController = this.silentHeaderController;
        NotificationStackScrollLayout notificationStackScrollLayout = this.parent;
        if (notificationStackScrollLayout != null) {
            sectionHeaderController.reinflateView(notificationStackScrollLayout);
            SectionHeaderController sectionHeaderController2 = this.alertingHeaderController;
            NotificationStackScrollLayout notificationStackScrollLayout2 = this.parent;
            if (notificationStackScrollLayout2 != null) {
                sectionHeaderController2.reinflateView(notificationStackScrollLayout2);
                SectionHeaderController sectionHeaderController3 = this.peopleHeaderController;
                NotificationStackScrollLayout notificationStackScrollLayout3 = this.parent;
                if (notificationStackScrollLayout3 != null) {
                    sectionHeaderController3.reinflateView(notificationStackScrollLayout3);
                    SectionHeaderController sectionHeaderController4 = this.incomingHeaderController;
                    NotificationStackScrollLayout notificationStackScrollLayout4 = this.parent;
                    if (notificationStackScrollLayout4 != null) {
                        sectionHeaderController4.reinflateView(notificationStackScrollLayout4);
                        MediaHeaderView mediaHeaderView = (MediaHeaderView) reinflateView(this.mediaControlsView, layoutInflater, R$layout.keyguard_media_header);
                        this.mediaControlsView = mediaHeaderView;
                        this.keyguardMediaController.attachSinglePaneContainer(mediaHeaderView);
                        return;
                    }
                    Intrinsics.throwUninitializedPropertyAccessException("parent");
                    throw null;
                }
                Intrinsics.throwUninitializedPropertyAccessException("parent");
                throw null;
            }
            Intrinsics.throwUninitializedPropertyAccessException("parent");
            throw null;
        }
        Intrinsics.throwUninitializedPropertyAccessException("parent");
        throw null;
    }

    @Override // com.android.systemui.statusbar.notification.stack.StackScrollAlgorithm.SectionProvider
    public boolean beginsSection(View view, View view2) {
        Intrinsics.checkNotNullParameter(view, "view");
        return view == getSilentHeaderView() || view == this.mediaControlsView || view == getPeopleHeaderView() || view == getAlertingHeaderView() || view == getIncomingHeaderView() || !Intrinsics.areEqual(getBucket(view), getBucket(view2));
    }

    /* access modifiers changed from: private */
    public final Integer getBucket(View view) {
        if (view == getSilentHeaderView()) {
            return 6;
        }
        if (view == getIncomingHeaderView()) {
            return 2;
        }
        if (view == this.mediaControlsView) {
            return 1;
        }
        if (view == getPeopleHeaderView()) {
            return 4;
        }
        if (view == getAlertingHeaderView()) {
            return 5;
        }
        if (view instanceof ExpandableNotificationRow) {
            return Integer.valueOf(((ExpandableNotificationRow) view).getEntry().getBucket());
        }
        return null;
    }

    private final void logShadeChild(int i, View view) {
        if (view == getIncomingHeaderView()) {
            this.logger.logIncomingHeader(i);
        } else if (view == this.mediaControlsView) {
            this.logger.logMediaControls(i);
        } else if (view == getPeopleHeaderView()) {
            this.logger.logConversationsHeader(i);
        } else if (view == getAlertingHeaderView()) {
            this.logger.logAlertingHeader(i);
        } else if (view == getSilentHeaderView()) {
            this.logger.logSilentHeader(i);
        } else if (!(view instanceof ExpandableNotificationRow)) {
            this.logger.logOther(i, view.getClass());
        } else {
            ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) view;
            boolean isHeadsUp = expandableNotificationRow.isHeadsUp();
            int bucket = expandableNotificationRow.getEntry().getBucket();
            if (bucket == 2) {
                this.logger.logHeadsUp(i, isHeadsUp);
            } else if (bucket == 4) {
                this.logger.logConversation(i, isHeadsUp);
            } else if (bucket == 5) {
                this.logger.logAlerting(i, isHeadsUp);
            } else if (bucket == 6) {
                this.logger.logSilent(i, isHeadsUp);
            }
        }
    }

    private final void logShadeContents() {
        NotificationStackScrollLayout notificationStackScrollLayout = this.parent;
        if (notificationStackScrollLayout != null) {
            int i = 0;
            for (View view : ConvenienceExtensionsKt.getChildren(notificationStackScrollLayout)) {
                i++;
                if (i < 0) {
                    CollectionsKt__CollectionsKt.throwIndexOverflow();
                }
                logShadeChild(i, view);
            }
            return;
        }
        Intrinsics.throwUninitializedPropertyAccessException("parent");
        throw null;
    }

    private final boolean isUsingMultipleSections() {
        return this.sectionsFeatureManager.getNumberOfBuckets() > 1;
    }

    @VisibleForTesting
    public final void updateSectionBoundaries() {
        updateSectionBoundaries("test");
    }

    private final <T extends ExpandableView> SectionUpdateState<T> expandableViewHeaderState(T t) {
        return new SectionUpdateState<T>(t, this) { // from class: com.android.systemui.statusbar.notification.stack.NotificationSectionsManager$expandableViewHeaderState$1
            final /* synthetic */ ExpandableView $header;
            private Integer currentPosition;
            private final ExpandableView header;
            private Integer targetPosition;
            final /* synthetic */ NotificationSectionsManager this$0;

            /* JADX WARN: Incorrect types in method signature: (TT;Lcom/android/systemui/statusbar/notification/stack/NotificationSectionsManager;)V */
            /* access modifiers changed from: package-private */
            {
                this.$header = r1;
                this.this$0 = r2;
                this.header = r1;
            }

            @Override // com.android.systemui.statusbar.notification.stack.NotificationSectionsManager.SectionUpdateState
            public Integer getCurrentPosition() {
                return this.currentPosition;
            }

            @Override // com.android.systemui.statusbar.notification.stack.NotificationSectionsManager.SectionUpdateState
            public void setCurrentPosition(Integer num) {
                this.currentPosition = num;
            }

            @Override // com.android.systemui.statusbar.notification.stack.NotificationSectionsManager.SectionUpdateState
            public Integer getTargetPosition() {
                return this.targetPosition;
            }

            @Override // com.android.systemui.statusbar.notification.stack.NotificationSectionsManager.SectionUpdateState
            public void setTargetPosition(Integer num) {
                this.targetPosition = num;
            }

            @Override // com.android.systemui.statusbar.notification.stack.NotificationSectionsManager.SectionUpdateState
            public void adjustViewPosition() {
                Integer targetPosition = getTargetPosition();
                Integer currentPosition = getCurrentPosition();
                if (targetPosition == null) {
                    if (currentPosition != null) {
                        NotificationStackScrollLayout notificationStackScrollLayout = this.this$0.parent;
                        if (notificationStackScrollLayout != null) {
                            notificationStackScrollLayout.removeView(this.$header);
                        } else {
                            Intrinsics.throwUninitializedPropertyAccessException("parent");
                            throw null;
                        }
                    }
                } else if (currentPosition == null) {
                    ViewGroup transientContainer = this.$header.getTransientContainer();
                    if (transientContainer != null) {
                        transientContainer.removeTransientView(this.$header);
                    }
                    this.$header.setTransientContainer(null);
                    NotificationStackScrollLayout notificationStackScrollLayout2 = this.this$0.parent;
                    if (notificationStackScrollLayout2 != null) {
                        notificationStackScrollLayout2.addView(this.$header, targetPosition.intValue());
                    } else {
                        Intrinsics.throwUninitializedPropertyAccessException("parent");
                        throw null;
                    }
                } else {
                    NotificationStackScrollLayout notificationStackScrollLayout3 = this.this$0.parent;
                    if (notificationStackScrollLayout3 != null) {
                        notificationStackScrollLayout3.changeViewPosition(this.$header, targetPosition.intValue());
                    } else {
                        Intrinsics.throwUninitializedPropertyAccessException("parent");
                        throw null;
                    }
                }
            }
        };
    }

    private final <T extends StackScrollerDecorView> SectionUpdateState<T> decorViewHeaderState(T t) {
        return new SectionUpdateState<T>(expandableViewHeaderState(t), t) { // from class: com.android.systemui.statusbar.notification.stack.NotificationSectionsManager$decorViewHeaderState$1
            private final /* synthetic */ NotificationSectionsManager.SectionUpdateState<T> $$delegate_0;
            final /* synthetic */ StackScrollerDecorView $header;
            final /* synthetic */ NotificationSectionsManager.SectionUpdateState<T> $inner;

            @Override // com.android.systemui.statusbar.notification.stack.NotificationSectionsManager.SectionUpdateState
            public Integer getCurrentPosition() {
                return this.$$delegate_0.getCurrentPosition();
            }

            @Override // com.android.systemui.statusbar.notification.stack.NotificationSectionsManager.SectionUpdateState
            public Integer getTargetPosition() {
                return this.$$delegate_0.getTargetPosition();
            }

            @Override // com.android.systemui.statusbar.notification.stack.NotificationSectionsManager.SectionUpdateState
            public void setCurrentPosition(Integer num) {
                this.$$delegate_0.setCurrentPosition(num);
            }

            @Override // com.android.systemui.statusbar.notification.stack.NotificationSectionsManager.SectionUpdateState
            public void setTargetPosition(Integer num) {
                this.$$delegate_0.setTargetPosition(num);
            }

            /* JADX WARN: Incorrect types in method signature: (Lcom/android/systemui/statusbar/notification/stack/NotificationSectionsManager$SectionUpdateState<+TT;>;TT;)V */
            /* access modifiers changed from: package-private */
            {
                this.$inner = r1;
                this.$header = r2;
                this.$$delegate_0 = r1;
            }

            @Override // com.android.systemui.statusbar.notification.stack.NotificationSectionsManager.SectionUpdateState
            public void adjustViewPosition() {
                this.$inner.adjustViewPosition();
                if (getTargetPosition() != null && getCurrentPosition() == null) {
                    this.$header.setContentVisible(true);
                }
            }
        };
    }

    /* JADX WARNING: Code restructure failed: missing block: B:61:0x0118, code lost:
        if ((r1.getVisibility() == 8) == false) goto L_0x011a;
     */
    /* JADX WARNING: Removed duplicated region for block: B:104:0x0195  */
    /* JADX WARNING: Removed duplicated region for block: B:105:0x0198  */
    /* JADX WARNING: Removed duplicated region for block: B:113:0x01bc A[LOOP:0: B:34:0x0096->B:113:0x01bc, LOOP_END] */
    /* JADX WARNING: Removed duplicated region for block: B:166:0x01cd A[EDGE_INSN: B:166:0x01cd->B:117:0x01cd ?: BREAK  , SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:77:0x0145  */
    /* JADX WARNING: Removed duplicated region for block: B:80:0x014c  */
    /* JADX WARNING: Removed duplicated region for block: B:87:0x015f A[ADDED_TO_REGION] */
    /* JADX WARNING: Removed duplicated region for block: B:94:0x0176 A[ADDED_TO_REGION] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void updateSectionBoundaries(java.lang.String r24) {
        /*
        // Method dump skipped, instructions count: 659
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.notification.stack.NotificationSectionsManager.updateSectionBoundaries(java.lang.String):void");
    }

    /* JADX DEBUG: Multi-variable search result rejected for r2v0, resolved type: com.android.systemui.statusbar.notification.stack.NotificationSectionsManager$SectionUpdateState<? extends com.android.systemui.statusbar.notification.stack.MediaHeaderView> */
    /* JADX DEBUG: Multi-variable search result rejected for r3v0, resolved type: com.android.systemui.statusbar.notification.stack.NotificationSectionsManager$SectionUpdateState<? extends com.android.systemui.statusbar.notification.stack.SectionHeaderView> */
    /* JADX DEBUG: Multi-variable search result rejected for r4v0, resolved type: com.android.systemui.statusbar.notification.stack.NotificationSectionsManager$SectionUpdateState<? extends com.android.systemui.statusbar.notification.stack.SectionHeaderView> */
    /* JADX DEBUG: Multi-variable search result rejected for r5v0, resolved type: com.android.systemui.statusbar.notification.stack.NotificationSectionsManager$SectionUpdateState<? extends com.android.systemui.statusbar.notification.stack.SectionHeaderView> */
    /* JADX DEBUG: Multi-variable search result rejected for r6v0, resolved type: com.android.systemui.statusbar.notification.stack.NotificationSectionsManager$SectionUpdateState<? extends com.android.systemui.statusbar.notification.stack.SectionHeaderView> */
    /* JADX WARN: Multi-variable type inference failed */
    private static final SectionUpdateState<ExpandableView> updateSectionBoundaries$getSectionState(NotificationSectionsManager notificationSectionsManager, SectionUpdateState<? extends MediaHeaderView> sectionUpdateState, SectionUpdateState<? extends SectionHeaderView> sectionUpdateState2, SectionUpdateState<? extends SectionHeaderView> sectionUpdateState3, SectionUpdateState<? extends SectionHeaderView> sectionUpdateState4, SectionUpdateState<? extends SectionHeaderView> sectionUpdateState5, View view) {
        if (view == notificationSectionsManager.mediaControlsView) {
            return sectionUpdateState;
        }
        if (view == notificationSectionsManager.getIncomingHeaderView()) {
            return sectionUpdateState2;
        }
        if (view == notificationSectionsManager.getPeopleHeaderView()) {
            return sectionUpdateState3;
        }
        if (view == notificationSectionsManager.getAlertingHeaderView()) {
            return sectionUpdateState4;
        }
        if (view == notificationSectionsManager.getSilentHeaderView()) {
            return sectionUpdateState5;
        }
        return null;
    }

    /* compiled from: NotificationSectionsManager.kt */
    /* access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static abstract class SectionBounds {
        public /* synthetic */ SectionBounds(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private SectionBounds() {
        }

        /* compiled from: NotificationSectionsManager.kt */
        /* loaded from: classes.dex */
        public static final class Many extends SectionBounds {
            private final ExpandableView first;
            private final ExpandableView last;

            public static /* synthetic */ Many copy$default(Many many, ExpandableView expandableView, ExpandableView expandableView2, int i, Object obj) {
                if ((i & 1) != 0) {
                    expandableView = many.first;
                }
                if ((i & 2) != 0) {
                    expandableView2 = many.last;
                }
                return many.copy(expandableView, expandableView2);
            }

            public final Many copy(ExpandableView expandableView, ExpandableView expandableView2) {
                Intrinsics.checkNotNullParameter(expandableView, "first");
                Intrinsics.checkNotNullParameter(expandableView2, "last");
                return new Many(expandableView, expandableView2);
            }

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                if (!(obj instanceof Many)) {
                    return false;
                }
                Many many = (Many) obj;
                return Intrinsics.areEqual(this.first, many.first) && Intrinsics.areEqual(this.last, many.last);
            }

            public int hashCode() {
                return (this.first.hashCode() * 31) + this.last.hashCode();
            }

            public String toString() {
                return "Many(first=" + this.first + ", last=" + this.last + ')';
            }

            public final ExpandableView getFirst() {
                return this.first;
            }

            public final ExpandableView getLast() {
                return this.last;
            }

            /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
            public Many(ExpandableView expandableView, ExpandableView expandableView2) {
                super(null);
                Intrinsics.checkNotNullParameter(expandableView, "first");
                Intrinsics.checkNotNullParameter(expandableView2, "last");
                this.first = expandableView;
                this.last = expandableView2;
            }
        }

        /* compiled from: NotificationSectionsManager.kt */
        /* loaded from: classes.dex */
        public static final class One extends SectionBounds {
            private final ExpandableView lone;

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return (obj instanceof One) && Intrinsics.areEqual(this.lone, ((One) obj).lone);
            }

            public int hashCode() {
                return this.lone.hashCode();
            }

            public String toString() {
                return "One(lone=" + this.lone + ')';
            }

            /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
            public One(ExpandableView expandableView) {
                super(null);
                Intrinsics.checkNotNullParameter(expandableView, "lone");
                this.lone = expandableView;
            }

            public final ExpandableView getLone() {
                return this.lone;
            }
        }

        /* compiled from: NotificationSectionsManager.kt */
        /* loaded from: classes.dex */
        public static final class None extends SectionBounds {
            public static final None INSTANCE = new None();

            private None() {
                super(null);
            }
        }

        public final SectionBounds addNotif(ExpandableView expandableView) {
            Intrinsics.checkNotNullParameter(expandableView, "notif");
            if (this instanceof None) {
                return new One(expandableView);
            }
            if (this instanceof One) {
                return new Many(((One) this).getLone(), expandableView);
            }
            if (this instanceof Many) {
                return Many.copy$default((Many) this, null, expandableView, 1, null);
            }
            throw new NoWhenBranchMatchedException();
        }

        public final boolean updateSection(NotificationSection notificationSection) {
            Intrinsics.checkNotNullParameter(notificationSection, "section");
            if (this instanceof None) {
                return setFirstAndLastVisibleChildren(notificationSection, null, null);
            }
            if (this instanceof One) {
                One one = (One) this;
                return setFirstAndLastVisibleChildren(notificationSection, one.getLone(), one.getLone());
            } else if (this instanceof Many) {
                Many many = (Many) this;
                return setFirstAndLastVisibleChildren(notificationSection, many.getFirst(), many.getLast());
            } else {
                throw new NoWhenBranchMatchedException();
            }
        }

        private final boolean setFirstAndLastVisibleChildren(NotificationSection notificationSection, ExpandableView expandableView, ExpandableView expandableView2) {
            return notificationSection.setFirstVisibleChild(expandableView) || notificationSection.setLastVisibleChild(expandableView2);
        }
    }

    public final boolean updateFirstAndLastViewsForAllSections(NotificationSection[] notificationSectionArr, List<? extends ExpandableView> list) {
        SparseArray sparseArray;
        Intrinsics.checkNotNullParameter(notificationSectionArr, "sections");
        Intrinsics.checkNotNullParameter(list, "children");
        NotificationSectionsManager$updateFirstAndLastViewsForAllSections$$inlined$groupingBy$1 notificationSectionsManager$updateFirstAndLastViewsForAllSections$$inlined$groupingBy$1 = new Grouping<ExpandableView, Integer>(CollectionsKt___CollectionsKt.asSequence(list), this) { // from class: com.android.systemui.statusbar.notification.stack.NotificationSectionsManager$updateFirstAndLastViewsForAllSections$$inlined$groupingBy$1
            final /* synthetic */ Sequence $this_groupingBy;
            final /* synthetic */ NotificationSectionsManager this$0;

            {
                this.$this_groupingBy = r1;
                this.this$0 = r2;
            }

            @Override // kotlin.collections.Grouping
            public Iterator<ExpandableView> sourceIterator() {
                return this.$this_groupingBy.iterator();
            }

            @Override // kotlin.collections.Grouping
            public Integer keyOf(ExpandableView expandableView) {
                Integer num = this.this$0.getBucket(expandableView);
                if (num != null) {
                    return Integer.valueOf(num.intValue());
                }
                throw new IllegalArgumentException("Cannot find section bucket for view");
            }
        };
        SectionBounds.None none = SectionBounds.None.INSTANCE;
        int length = notificationSectionArr.length;
        if (length < 0) {
            sparseArray = new SparseArray();
        } else {
            sparseArray = new SparseArray(length);
        }
        Iterator<ExpandableView> sourceIterator = notificationSectionsManager$updateFirstAndLastViewsForAllSections$$inlined$groupingBy$1.sourceIterator();
        while (sourceIterator.hasNext()) {
            ExpandableView next = sourceIterator.next();
            int intValue = notificationSectionsManager$updateFirstAndLastViewsForAllSections$$inlined$groupingBy$1.keyOf(next).intValue();
            Object obj = sparseArray.get(intValue);
            if (obj == null) {
                obj = none;
            }
            sparseArray.put(intValue, ((SectionBounds) obj).addNotif(next));
        }
        boolean z = false;
        for (NotificationSection notificationSection : notificationSectionArr) {
            SectionBounds sectionBounds = (SectionBounds) sparseArray.get(notificationSection.getBucket());
            if (sectionBounds == null) {
                sectionBounds = SectionBounds.None.INSTANCE;
            }
            z = sectionBounds.updateSection(notificationSection) || z;
        }
        return z;
    }

    public final void setHeaderForegroundColor(int i) {
        SectionHeaderView peopleHeaderView = getPeopleHeaderView();
        if (peopleHeaderView != null) {
            peopleHeaderView.setForegroundColor(i);
        }
        SectionHeaderView silentHeaderView = getSilentHeaderView();
        if (silentHeaderView != null) {
            silentHeaderView.setForegroundColor(i);
        }
        SectionHeaderView alertingHeaderView = getAlertingHeaderView();
        if (alertingHeaderView != null) {
            alertingHeaderView.setForegroundColor(i);
        }
    }

    /* compiled from: NotificationSectionsManager.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
