package com.android.systemui.statusbar.policy;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.view.ContextThemeWrapper;
import android.widget.Button;
import com.android.systemui.shared.system.ActivityManagerWrapper;
import com.android.systemui.shared.system.DevicePolicyManagerWrapper;
import com.android.systemui.shared.system.PackageManagerWrapper;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.policy.SmartReplyView;
import java.util.ArrayList;
import java.util.List;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt__SequencesKt;
import kotlin.sequences.SequencesKt___SequencesKt;
/* compiled from: SmartReplyStateInflater.kt */
/* loaded from: classes.dex */
public final class SmartReplyStateInflaterImpl implements SmartReplyStateInflater {
    private final ActivityManagerWrapper activityManagerWrapper;
    private final SmartReplyConstants constants;
    private final DevicePolicyManagerWrapper devicePolicyManagerWrapper;
    private final PackageManagerWrapper packageManagerWrapper;
    private final SmartActionInflater smartActionsInflater;
    private final SmartReplyInflater smartRepliesInflater;

    public SmartReplyStateInflaterImpl(SmartReplyConstants smartReplyConstants, ActivityManagerWrapper activityManagerWrapper, PackageManagerWrapper packageManagerWrapper, DevicePolicyManagerWrapper devicePolicyManagerWrapper, SmartReplyInflater smartReplyInflater, SmartActionInflater smartActionInflater) {
        Intrinsics.checkNotNullParameter(smartReplyConstants, "constants");
        Intrinsics.checkNotNullParameter(activityManagerWrapper, "activityManagerWrapper");
        Intrinsics.checkNotNullParameter(packageManagerWrapper, "packageManagerWrapper");
        Intrinsics.checkNotNullParameter(devicePolicyManagerWrapper, "devicePolicyManagerWrapper");
        Intrinsics.checkNotNullParameter(smartReplyInflater, "smartRepliesInflater");
        Intrinsics.checkNotNullParameter(smartActionInflater, "smartActionsInflater");
        this.constants = smartReplyConstants;
        this.activityManagerWrapper = activityManagerWrapper;
        this.packageManagerWrapper = packageManagerWrapper;
        this.devicePolicyManagerWrapper = devicePolicyManagerWrapper;
        this.smartRepliesInflater = smartReplyInflater;
        this.smartActionsInflater = smartActionInflater;
    }

    @Override // com.android.systemui.statusbar.policy.SmartReplyStateInflater
    public InflatedSmartReplyState inflateSmartReplyState(NotificationEntry notificationEntry) {
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        return chooseSmartRepliesAndActions(notificationEntry);
    }

    @Override // com.android.systemui.statusbar.policy.SmartReplyStateInflater
    public InflatedSmartReplyViewHolder inflateSmartReplyViewHolder(Context context, Context context2, NotificationEntry notificationEntry, InflatedSmartReplyState inflatedSmartReplyState, InflatedSmartReplyState inflatedSmartReplyState2) {
        boolean z;
        Sequence sequence;
        Intrinsics.checkNotNullParameter(context, "sysuiContext");
        Intrinsics.checkNotNullParameter(context2, "notifPackageContext");
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        Intrinsics.checkNotNullParameter(inflatedSmartReplyState2, "newSmartReplyState");
        Sequence sequence2 = null;
        if (!SmartReplyStateInflaterKt.shouldShowSmartReplyView(notificationEntry, inflatedSmartReplyState2)) {
            return new InflatedSmartReplyViewHolder(null, null);
        }
        boolean z2 = !SmartReplyStateInflaterKt.areSuggestionsSimilar(inflatedSmartReplyState, inflatedSmartReplyState2);
        SmartReplyView inflate = SmartReplyView.inflate(context, this.constants);
        SmartReplyView.SmartReplies smartReplies = inflatedSmartReplyState2.getSmartReplies();
        if (smartReplies == null) {
            z = false;
        } else {
            z = smartReplies.fromAssistant;
        }
        inflate.setSmartRepliesGeneratedByAssistant(z);
        if (smartReplies == null) {
            sequence = null;
        } else {
            List<CharSequence> list = smartReplies.choices;
            Intrinsics.checkNotNullExpressionValue(list, "smartReplies.choices");
            sequence = SequencesKt___SequencesKt.mapIndexed(CollectionsKt___CollectionsKt.asSequence(list), new Function2<Integer, CharSequence, Button>(this, inflate, notificationEntry, smartReplies, z2) { // from class: com.android.systemui.statusbar.policy.SmartReplyStateInflaterImpl$inflateSmartReplyViewHolder$smartReplyButtons$1$1
                final /* synthetic */ boolean $delayOnClickListener;
                final /* synthetic */ NotificationEntry $entry;
                final /* synthetic */ SmartReplyView.SmartReplies $smartReplies;
                final /* synthetic */ SmartReplyView $smartReplyView;
                final /* synthetic */ SmartReplyStateInflaterImpl this$0;

                /* access modifiers changed from: package-private */
                {
                    this.this$0 = r1;
                    this.$smartReplyView = r2;
                    this.$entry = r3;
                    this.$smartReplies = r4;
                    this.$delayOnClickListener = r5;
                }

                /* Return type fixed from 'java.lang.Object' to match base method */
                /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object, java.lang.Object] */
                @Override // kotlin.jvm.functions.Function2
                public /* bridge */ /* synthetic */ Button invoke(Integer num, CharSequence charSequence) {
                    return invoke(num.intValue(), charSequence);
                }

                public final Button invoke(int i, CharSequence charSequence) {
                    SmartReplyInflater access$getSmartRepliesInflater$p = SmartReplyStateInflaterImpl.access$getSmartRepliesInflater$p(this.this$0);
                    SmartReplyView smartReplyView = this.$smartReplyView;
                    Intrinsics.checkNotNullExpressionValue(smartReplyView, "smartReplyView");
                    NotificationEntry notificationEntry2 = this.$entry;
                    SmartReplyView.SmartReplies smartReplies2 = this.$smartReplies;
                    Intrinsics.checkNotNullExpressionValue(charSequence, "choice");
                    return access$getSmartRepliesInflater$p.inflateReplyButton(smartReplyView, notificationEntry2, smartReplies2, i, charSequence, this.$delayOnClickListener);
                }
            });
        }
        if (sequence == null) {
            sequence = SequencesKt__SequencesKt.emptySequence();
        }
        SmartReplyView.SmartActions smartActions = inflatedSmartReplyState2.getSmartActions();
        if (smartActions != null) {
            ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(context2, context.getTheme());
            List<Notification.Action> list2 = smartActions.actions;
            Intrinsics.checkNotNullExpressionValue(list2, "smartActions.actions");
            sequence2 = SequencesKt___SequencesKt.mapIndexed(SequencesKt___SequencesKt.filter(CollectionsKt___CollectionsKt.asSequence(list2), SmartReplyStateInflaterImpl$inflateSmartReplyViewHolder$smartActionButtons$1$1.INSTANCE), new Function2<Integer, Notification.Action, Button>(this, inflate, notificationEntry, smartActions, z2, contextThemeWrapper) { // from class: com.android.systemui.statusbar.policy.SmartReplyStateInflaterImpl$inflateSmartReplyViewHolder$smartActionButtons$1$2
                final /* synthetic */ boolean $delayOnClickListener;
                final /* synthetic */ NotificationEntry $entry;
                final /* synthetic */ SmartReplyView.SmartActions $smartActions;
                final /* synthetic */ SmartReplyView $smartReplyView;
                final /* synthetic */ ContextThemeWrapper $themedPackageContext;
                final /* synthetic */ SmartReplyStateInflaterImpl this$0;

                /* access modifiers changed from: package-private */
                {
                    this.this$0 = r1;
                    this.$smartReplyView = r2;
                    this.$entry = r3;
                    this.$smartActions = r4;
                    this.$delayOnClickListener = r5;
                    this.$themedPackageContext = r6;
                }

                /* Return type fixed from 'java.lang.Object' to match base method */
                /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object, java.lang.Object] */
                @Override // kotlin.jvm.functions.Function2
                public /* bridge */ /* synthetic */ Button invoke(Integer num, Notification.Action action) {
                    return invoke(num.intValue(), action);
                }

                public final Button invoke(int i, Notification.Action action) {
                    SmartActionInflater access$getSmartActionsInflater$p = SmartReplyStateInflaterImpl.access$getSmartActionsInflater$p(this.this$0);
                    SmartReplyView smartReplyView = this.$smartReplyView;
                    Intrinsics.checkNotNullExpressionValue(smartReplyView, "smartReplyView");
                    NotificationEntry notificationEntry2 = this.$entry;
                    SmartReplyView.SmartActions smartActions2 = this.$smartActions;
                    Intrinsics.checkNotNullExpressionValue(action, "action");
                    return access$getSmartActionsInflater$p.inflateActionButton(smartReplyView, notificationEntry2, smartActions2, i, action, this.$delayOnClickListener, this.$themedPackageContext);
                }
            });
        }
        if (sequence2 == null) {
            sequence2 = SequencesKt__SequencesKt.emptySequence();
        }
        return new InflatedSmartReplyViewHolder(inflate, SequencesKt___SequencesKt.toList(SequencesKt___SequencesKt.plus(sequence, sequence2)));
    }

    /* JADX WARNING: Removed duplicated region for block: B:34:0x00aa  */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x00b0  */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x00e3  */
    /* JADX WARNING: Removed duplicated region for block: B:52:0x0105  */
    /* JADX WARNING: Removed duplicated region for block: B:55:0x0110  */
    /* JADX WARNING: Removed duplicated region for block: B:56:0x0112  */
    /* JADX WARNING: Removed duplicated region for block: B:64:0x0129  */
    /* JADX WARNING: Removed duplicated region for block: B:73:0x0145  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final com.android.systemui.statusbar.policy.InflatedSmartReplyState chooseSmartRepliesAndActions(com.android.systemui.statusbar.notification.collection.NotificationEntry r13) {
        /*
        // Method dump skipped, instructions count: 399
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.policy.SmartReplyStateInflaterImpl.chooseSmartRepliesAndActions(com.android.systemui.statusbar.notification.collection.NotificationEntry):com.android.systemui.statusbar.policy.InflatedSmartReplyState");
    }

    private final List<Notification.Action> filterAllowlistedLockTaskApps(List<? extends Notification.Action> list) {
        Intent intent;
        ArrayList arrayList = new ArrayList();
        for (Object obj : list) {
            PendingIntent pendingIntent = ((Notification.Action) obj).actionIntent;
            boolean z = false;
            ResolveInfo resolveInfo = null;
            if (!(pendingIntent == null || (intent = pendingIntent.getIntent()) == null)) {
                resolveInfo = this.packageManagerWrapper.resolveActivity(intent, 0);
            }
            if (resolveInfo != null) {
                z = this.devicePolicyManagerWrapper.isLockTaskPermitted(resolveInfo.activityInfo.packageName);
            }
            if (z) {
                arrayList.add(obj);
            }
        }
        return arrayList;
    }
}
