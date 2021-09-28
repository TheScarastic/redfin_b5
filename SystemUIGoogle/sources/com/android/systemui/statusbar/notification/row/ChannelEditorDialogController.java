package com.android.systemui.statusbar.notification.row;

import android.app.INotificationManager;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.TextView;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
import com.android.systemui.statusbar.notification.row.ChannelEditorDialog;
import com.android.systemui.statusbar.notification.row.NotificationInfo;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import kotlin.Unit;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.collections.CollectionsKt__MutableCollectionsKt;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.comparisons.ComparisonsKt__ComparisonsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt___SequencesKt;
/* compiled from: ChannelEditorDialogController.kt */
/* loaded from: classes.dex */
public final class ChannelEditorDialogController {
    private Drawable appIcon;
    private String appName;
    private Boolean appNotificationsCurrentlyEnabled;
    private Integer appUid;
    private final Context context;
    private ChannelEditorDialog dialog;
    private final ChannelEditorDialog.Builder dialogBuilder;
    private final INotificationManager noMan;
    private OnChannelEditorDialogFinishedListener onFinishListener;
    private NotificationInfo.OnSettingsClickListener onSettingsClickListener;
    private String packageName;
    private boolean prepared;
    private final List<NotificationChannel> paddedChannels = new ArrayList();
    private final List<NotificationChannel> providedChannels = new ArrayList();
    private final Map<NotificationChannel, Integer> edits = new LinkedHashMap();
    private boolean appNotificationsEnabled = true;
    private final HashMap<String, CharSequence> groupNameLookup = new HashMap<>();
    private final List<NotificationChannelGroup> channelGroupList = new ArrayList();
    private final int wmFlags = -2130444288;

    @VisibleForTesting
    public static /* synthetic */ void getGroupNameLookup$frameworks__base__packages__SystemUI__android_common__SystemUI_core$annotations() {
    }

    @VisibleForTesting
    public static /* synthetic */ void getPaddedChannels$frameworks__base__packages__SystemUI__android_common__SystemUI_core$annotations() {
    }

    public ChannelEditorDialogController(Context context, INotificationManager iNotificationManager, ChannelEditorDialog.Builder builder) {
        Intrinsics.checkNotNullParameter(context, "c");
        Intrinsics.checkNotNullParameter(iNotificationManager, "noMan");
        Intrinsics.checkNotNullParameter(builder, "dialogBuilder");
        this.noMan = iNotificationManager;
        this.dialogBuilder = builder;
        Context applicationContext = context.getApplicationContext();
        Intrinsics.checkNotNullExpressionValue(applicationContext, "c.applicationContext");
        this.context = applicationContext;
    }

    public final OnChannelEditorDialogFinishedListener getOnFinishListener() {
        return this.onFinishListener;
    }

    public final void setOnFinishListener(OnChannelEditorDialogFinishedListener onChannelEditorDialogFinishedListener) {
        this.onFinishListener = onChannelEditorDialogFinishedListener;
    }

    public final List<NotificationChannel> getPaddedChannels$frameworks__base__packages__SystemUI__android_common__SystemUI_core() {
        return this.paddedChannels;
    }

    public final HashMap<String, CharSequence> getGroupNameLookup$frameworks__base__packages__SystemUI__android_common__SystemUI_core() {
        return this.groupNameLookup;
    }

    public final void prepareDialogForApp(String str, String str2, int i, Set<NotificationChannel> set, Drawable drawable, NotificationInfo.OnSettingsClickListener onSettingsClickListener) {
        Intrinsics.checkNotNullParameter(str, "appName");
        Intrinsics.checkNotNullParameter(str2, "packageName");
        Intrinsics.checkNotNullParameter(set, "channels");
        Intrinsics.checkNotNullParameter(drawable, "appIcon");
        this.appName = str;
        this.packageName = str2;
        this.appUid = Integer.valueOf(i);
        this.appIcon = drawable;
        boolean checkAreAppNotificationsOn = checkAreAppNotificationsOn();
        this.appNotificationsEnabled = checkAreAppNotificationsOn;
        this.onSettingsClickListener = onSettingsClickListener;
        this.appNotificationsCurrentlyEnabled = Boolean.valueOf(checkAreAppNotificationsOn);
        this.channelGroupList.clear();
        this.channelGroupList.addAll(fetchNotificationChannelGroups());
        buildGroupNameLookup();
        this.providedChannels.clear();
        this.providedChannels.addAll(set);
        padToFourChannels(set);
        initDialog();
        this.prepared = true;
    }

    private final void buildGroupNameLookup() {
        for (NotificationChannelGroup notificationChannelGroup : this.channelGroupList) {
            if (notificationChannelGroup.getId() != null) {
                HashMap<String, CharSequence> groupNameLookup$frameworks__base__packages__SystemUI__android_common__SystemUI_core = getGroupNameLookup$frameworks__base__packages__SystemUI__android_common__SystemUI_core();
                String id = notificationChannelGroup.getId();
                Intrinsics.checkNotNullExpressionValue(id, "group.id");
                CharSequence name = notificationChannelGroup.getName();
                Intrinsics.checkNotNullExpressionValue(name, "group.name");
                groupNameLookup$frameworks__base__packages__SystemUI__android_common__SystemUI_core.put(id, name);
            }
        }
    }

    private final void padToFourChannels(Set<NotificationChannel> set) {
        this.paddedChannels.clear();
        boolean unused = CollectionsKt__MutableCollectionsKt.addAll(this.paddedChannels, SequencesKt___SequencesKt.take(CollectionsKt___CollectionsKt.asSequence(set), 4));
        boolean unused2 = CollectionsKt__MutableCollectionsKt.addAll(this.paddedChannels, SequencesKt___SequencesKt.take(SequencesKt___SequencesKt.distinct(SequencesKt___SequencesKt.filterNot(getDisplayableChannels(CollectionsKt___CollectionsKt.asSequence(this.channelGroupList)), new Function1<NotificationChannel, Boolean>(this) { // from class: com.android.systemui.statusbar.notification.row.ChannelEditorDialogController$padToFourChannels$1
            final /* synthetic */ ChannelEditorDialogController this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
            }

            /* Return type fixed from 'java.lang.Object' to match base method */
            /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object] */
            @Override // kotlin.jvm.functions.Function1
            public /* bridge */ /* synthetic */ Boolean invoke(NotificationChannel notificationChannel) {
                return Boolean.valueOf(invoke(notificationChannel));
            }

            public final boolean invoke(NotificationChannel notificationChannel) {
                Intrinsics.checkNotNullParameter(notificationChannel, "it");
                return this.this$0.getPaddedChannels$frameworks__base__packages__SystemUI__android_common__SystemUI_core().contains(notificationChannel);
            }
        })), 4 - this.paddedChannels.size()));
        if (this.paddedChannels.size() == 1 && Intrinsics.areEqual("miscellaneous", this.paddedChannels.get(0).getId())) {
            this.paddedChannels.clear();
        }
    }

    private final Sequence<NotificationChannel> getDisplayableChannels(Sequence<NotificationChannelGroup> sequence) {
        return SequencesKt___SequencesKt.sortedWith(SequencesKt___SequencesKt.flatMap(sequence, ChannelEditorDialogController$getDisplayableChannels$channels$1.INSTANCE), new Comparator<T>() { // from class: com.android.systemui.statusbar.notification.row.ChannelEditorDialogController$getDisplayableChannels$$inlined$compareBy$1
            @Override // java.util.Comparator
            public final int compare(T t, T t2) {
                int compareValues;
                NotificationChannel notificationChannel = (NotificationChannel) t;
                CharSequence name = notificationChannel.getName();
                String str = null;
                String obj = name == null ? null : name.toString();
                if (obj == null) {
                    obj = notificationChannel.getId();
                }
                NotificationChannel notificationChannel2 = (NotificationChannel) t2;
                CharSequence name2 = notificationChannel2.getName();
                if (name2 != null) {
                    str = name2.toString();
                }
                if (str == null) {
                    str = notificationChannel2.getId();
                }
                compareValues = ComparisonsKt__ComparisonsKt.compareValues(obj, str);
                return compareValues;
            }
        });
    }

    public final void show() {
        if (this.prepared) {
            ChannelEditorDialog channelEditorDialog = this.dialog;
            if (channelEditorDialog != null) {
                channelEditorDialog.show();
            } else {
                Intrinsics.throwUninitializedPropertyAccessException("dialog");
                throw null;
            }
        } else {
            throw new IllegalStateException("Must call prepareDialogForApp() before calling show()");
        }
    }

    public final void close() {
        done();
    }

    /* access modifiers changed from: private */
    public final void done() {
        resetState();
        ChannelEditorDialog channelEditorDialog = this.dialog;
        if (channelEditorDialog != null) {
            channelEditorDialog.dismiss();
        } else {
            Intrinsics.throwUninitializedPropertyAccessException("dialog");
            throw null;
        }
    }

    private final void resetState() {
        this.appIcon = null;
        this.appUid = null;
        this.packageName = null;
        this.appName = null;
        this.appNotificationsCurrentlyEnabled = null;
        this.edits.clear();
        this.paddedChannels.clear();
        this.providedChannels.clear();
        this.groupNameLookup.clear();
    }

    public final CharSequence groupNameForId(String str) {
        CharSequence charSequence = this.groupNameLookup.get(str);
        return charSequence == null ? "" : charSequence;
    }

    public final void proposeEditForChannel(NotificationChannel notificationChannel, int i) {
        Intrinsics.checkNotNullParameter(notificationChannel, "channel");
        if (notificationChannel.getImportance() == i) {
            this.edits.remove(notificationChannel);
        } else {
            this.edits.put(notificationChannel, Integer.valueOf(i));
        }
        ChannelEditorDialog channelEditorDialog = this.dialog;
        if (channelEditorDialog != null) {
            channelEditorDialog.updateDoneButtonText(hasChanges());
        } else {
            Intrinsics.throwUninitializedPropertyAccessException("dialog");
            throw null;
        }
    }

    public final void proposeSetAppNotificationsEnabled(boolean z) {
        this.appNotificationsEnabled = z;
        ChannelEditorDialog channelEditorDialog = this.dialog;
        if (channelEditorDialog != null) {
            channelEditorDialog.updateDoneButtonText(hasChanges());
        } else {
            Intrinsics.throwUninitializedPropertyAccessException("dialog");
            throw null;
        }
    }

    public final boolean areAppNotificationsEnabled() {
        return this.appNotificationsEnabled;
    }

    private final boolean hasChanges() {
        return (this.edits.isEmpty() ^ true) || !Intrinsics.areEqual(Boolean.valueOf(this.appNotificationsEnabled), this.appNotificationsCurrentlyEnabled);
    }

    private final List<NotificationChannelGroup> fetchNotificationChannelGroups() {
        try {
            INotificationManager iNotificationManager = this.noMan;
            String str = this.packageName;
            Intrinsics.checkNotNull(str);
            Integer num = this.appUid;
            Intrinsics.checkNotNull(num);
            List<NotificationChannelGroup> list = iNotificationManager.getNotificationChannelGroupsForPackage(str, num.intValue(), false).getList();
            if (!(list instanceof List)) {
                list = null;
            }
            if (list == null) {
                return CollectionsKt__CollectionsKt.emptyList();
            }
            return list;
        } catch (Exception e) {
            Log.e("ChannelDialogController", "Error fetching channel groups", e);
            return CollectionsKt__CollectionsKt.emptyList();
        }
    }

    private final boolean checkAreAppNotificationsOn() {
        try {
            INotificationManager iNotificationManager = this.noMan;
            String str = this.packageName;
            Intrinsics.checkNotNull(str);
            Integer num = this.appUid;
            Intrinsics.checkNotNull(num);
            return iNotificationManager.areNotificationsEnabledForPackage(str, num.intValue());
        } catch (Exception e) {
            Log.e("ChannelDialogController", "Error calling NoMan", e);
            return false;
        }
    }

    private final void applyAppNotificationsOn(boolean z) {
        try {
            INotificationManager iNotificationManager = this.noMan;
            String str = this.packageName;
            Intrinsics.checkNotNull(str);
            Integer num = this.appUid;
            Intrinsics.checkNotNull(num);
            iNotificationManager.setNotificationsEnabledForPackage(str, num.intValue(), z);
        } catch (Exception e) {
            Log.e("ChannelDialogController", "Error calling NoMan", e);
        }
    }

    private final void setChannelImportance(NotificationChannel notificationChannel, int i) {
        try {
            notificationChannel.setImportance(i);
            INotificationManager iNotificationManager = this.noMan;
            String str = this.packageName;
            Intrinsics.checkNotNull(str);
            Integer num = this.appUid;
            Intrinsics.checkNotNull(num);
            iNotificationManager.updateNotificationChannelForPackage(str, num.intValue(), notificationChannel);
        } catch (Exception e) {
            Log.e("ChannelDialogController", "Unable to update notification importance", e);
        }
    }

    @VisibleForTesting
    public final void apply() {
        for (Map.Entry<NotificationChannel, Integer> entry : this.edits.entrySet()) {
            NotificationChannel key = entry.getKey();
            int intValue = entry.getValue().intValue();
            if (key.getImportance() != intValue) {
                setChannelImportance(key, intValue);
            }
        }
        if (!Intrinsics.areEqual(Boolean.valueOf(this.appNotificationsEnabled), this.appNotificationsCurrentlyEnabled)) {
            applyAppNotificationsOn(this.appNotificationsEnabled);
        }
    }

    @VisibleForTesting
    public final void launchSettings(View view) {
        Intrinsics.checkNotNullParameter(view, "sender");
        NotificationInfo.OnSettingsClickListener onSettingsClickListener = this.onSettingsClickListener;
        if (onSettingsClickListener != null) {
            Integer num = this.appUid;
            Intrinsics.checkNotNull(num);
            onSettingsClickListener.onClick(view, null, num.intValue());
        }
    }

    private final void initDialog() {
        this.dialogBuilder.setContext(this.context);
        ChannelEditorDialog build = this.dialogBuilder.build();
        this.dialog = build;
        if (build != null) {
            Window window = build.getWindow();
            if (window != null) {
                window.requestFeature(1);
            }
            ChannelEditorDialog channelEditorDialog = this.dialog;
            if (channelEditorDialog != null) {
                channelEditorDialog.setTitle(" ");
                ChannelEditorDialog channelEditorDialog2 = this.dialog;
                if (channelEditorDialog2 != null) {
                    channelEditorDialog2.setContentView(R$layout.notif_half_shelf);
                    channelEditorDialog2.setCanceledOnTouchOutside(true);
                    channelEditorDialog2.setOnDismissListener(new DialogInterface.OnDismissListener(this) { // from class: com.android.systemui.statusbar.notification.row.ChannelEditorDialogController$initDialog$1$1
                        final /* synthetic */ ChannelEditorDialogController this$0;

                        /* access modifiers changed from: package-private */
                        {
                            this.this$0 = r1;
                        }

                        @Override // android.content.DialogInterface.OnDismissListener
                        public final void onDismiss(DialogInterface dialogInterface) {
                            OnChannelEditorDialogFinishedListener onFinishListener = this.this$0.getOnFinishListener();
                            if (onFinishListener != null) {
                                onFinishListener.onChannelEditorDialogFinished();
                            }
                        }
                    });
                    ChannelEditorListView channelEditorListView = (ChannelEditorListView) channelEditorDialog2.findViewById(R$id.half_shelf_container);
                    if (channelEditorListView != null) {
                        channelEditorListView.setController(this);
                        channelEditorListView.setAppIcon(this.appIcon);
                        channelEditorListView.setAppName(this.appName);
                        channelEditorListView.setChannels(getPaddedChannels$frameworks__base__packages__SystemUI__android_common__SystemUI_core());
                    }
                    channelEditorDialog2.setOnShowListener(new DialogInterface.OnShowListener(this, channelEditorListView) { // from class: com.android.systemui.statusbar.notification.row.ChannelEditorDialogController$initDialog$1$3
                        final /* synthetic */ ChannelEditorListView $listView;
                        final /* synthetic */ ChannelEditorDialogController this$0;

                        /* access modifiers changed from: package-private */
                        {
                            this.this$0 = r1;
                            this.$listView = r2;
                        }

                        @Override // android.content.DialogInterface.OnShowListener
                        public final void onShow(DialogInterface dialogInterface) {
                            for (NotificationChannel notificationChannel : this.this$0.providedChannels) {
                                ChannelEditorListView channelEditorListView2 = this.$listView;
                                if (channelEditorListView2 != null) {
                                    channelEditorListView2.highlightChannel(notificationChannel);
                                }
                            }
                        }
                    });
                    TextView textView = (TextView) channelEditorDialog2.findViewById(R$id.done_button);
                    if (textView != null) {
                        textView.setOnClickListener(new View.OnClickListener(this) { // from class: com.android.systemui.statusbar.notification.row.ChannelEditorDialogController$initDialog$1$4
                            final /* synthetic */ ChannelEditorDialogController this$0;

                            /* access modifiers changed from: package-private */
                            {
                                this.this$0 = r1;
                            }

                            @Override // android.view.View.OnClickListener
                            public final void onClick(View view) {
                                this.this$0.apply();
                                this.this$0.done();
                            }
                        });
                    }
                    TextView textView2 = (TextView) channelEditorDialog2.findViewById(R$id.see_more_button);
                    if (textView2 != null) {
                        textView2.setOnClickListener(new View.OnClickListener(this) { // from class: com.android.systemui.statusbar.notification.row.ChannelEditorDialogController$initDialog$1$5
                            final /* synthetic */ ChannelEditorDialogController this$0;

                            /* access modifiers changed from: package-private */
                            {
                                this.this$0 = r1;
                            }

                            @Override // android.view.View.OnClickListener
                            public final void onClick(View view) {
                                ChannelEditorDialogController channelEditorDialogController = this.this$0;
                                Intrinsics.checkNotNullExpressionValue(view, "it");
                                channelEditorDialogController.launchSettings(view);
                                this.this$0.done();
                            }
                        });
                    }
                    Window window2 = channelEditorDialog2.getWindow();
                    if (window2 != null) {
                        window2.setBackgroundDrawable(new ColorDrawable(0));
                        window2.addFlags(this.wmFlags);
                        window2.setType(2017);
                        window2.setWindowAnimations(16973910);
                        WindowManager.LayoutParams attributes = window2.getAttributes();
                        attributes.format = -3;
                        attributes.setTitle(ChannelEditorDialogController.class.getSimpleName());
                        attributes.gravity = 81;
                        attributes.setFitInsetsTypes(window2.getAttributes().getFitInsetsTypes() & (~WindowInsets.Type.statusBars()));
                        attributes.width = -1;
                        attributes.height = -2;
                        Unit unit = Unit.INSTANCE;
                        window2.setAttributes(attributes);
                        return;
                    }
                    return;
                }
                Intrinsics.throwUninitializedPropertyAccessException("dialog");
                throw null;
            }
            Intrinsics.throwUninitializedPropertyAccessException("dialog");
            throw null;
        }
        Intrinsics.throwUninitializedPropertyAccessException("dialog");
        throw null;
    }
}
