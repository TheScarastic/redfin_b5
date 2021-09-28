package com.google.android.apps.miphone.aiai.matchmaker.overview.ui;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.RemoteAction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.UserHandle;
import android.os.UserManager;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2.EntitiesData;
import com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2.FeedbackParcelables$ScreenshotOp;
import com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2.FeedbackParcelables$ScreenshotOpStatus;
import com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2.InteractionContextParcelables$InteractionContext;
import com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2.ParserParcelables$ParsedViewHierarchy;
import com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2.SuggestParcelables$Action;
import com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2.SuggestParcelables$ActionGroup;
import com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2.SuggestParcelables$Entities;
import com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2.SuggestParcelables$Entity;
import com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2.SuggestParcelables$IntentInfo;
import com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2.SuggestParcelables$IntentType;
import com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2.SuggestParcelables$InteractionType;
import com.google.android.apps.miphone.aiai.matchmaker.overview.common.BundleUtils;
import com.google.android.apps.miphone.aiai.matchmaker.overview.ui.ContentSuggestionsServiceWrapper;
import com.google.android.apps.miphone.aiai.matchmaker.overview.ui.utils.LogUtils;
import com.google.android.apps.miphone.aiai.matchmaker.overview.ui.utils.Utils;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.function.Supplier;
/* loaded from: classes2.dex */
public class ContentSuggestionsServiceClient {
    private static final Random random = new Random();
    private final BundleUtils bundleUtils = BundleUtils.createWithBackwardsCompatVersion();
    private final Context context;
    private final boolean isAiAiVersionSupported;
    private final ContentSuggestionsServiceWrapper serviceWrapper;
    private final UserManager userManager;

    public ContentSuggestionsServiceClient(Context context, Executor executor, Handler handler) {
        this.context = context;
        this.serviceWrapper = SuggestController.create(context, context, executor, handler).getWrapper();
        this.isAiAiVersionSupported = isVersionCodeSupported(context);
        this.userManager = (UserManager) context.getSystemService(UserManager.class);
    }

    private static boolean isVersionCodeSupported(Context context) {
        try {
            if (context.getPackageManager().getPackageInfo("com.google.android.as", 0).getLongVersionCode() >= 660780) {
                return true;
            }
            return false;
        } catch (PackageManager.NameNotFoundException e) {
            LogUtils.e("Error obtaining package info: ", e);
            return false;
        }
    }

    public void provideScreenshotActions(Bitmap bitmap, Uri uri, String str, String str2, UserHandle userHandle, SuggestParcelables$InteractionType suggestParcelables$InteractionType, ContentSuggestionsServiceWrapper.BundleCallback bundleCallback) {
        if (!this.isAiAiVersionSupported) {
            bundleCallback.onResult(Bundle.EMPTY);
            return;
        }
        int nextInt = random.nextInt();
        long currentTimeMillis = System.currentTimeMillis();
        Bundle obtainScreenshotContextImageBundle = this.bundleUtils.obtainScreenshotContextImageBundle(true, uri, str, str2, currentTimeMillis);
        obtainScreenshotContextImageBundle.putParcelable("android.contentsuggestions.extra.BITMAP", bitmap);
        InteractionContextParcelables$InteractionContext create = InteractionContextParcelables$InteractionContext.create();
        create.setInteractionType(suggestParcelables$InteractionType);
        create.setDisallowCopyPaste(false);
        create.setVersionCode(1);
        create.setIsPrimaryTask(true);
        this.serviceWrapper.connectAndRunAsync(new Runnable(nextInt, obtainScreenshotContextImageBundle, str, str2, currentTimeMillis, create, userHandle, uri, bundleCallback) { // from class: com.google.android.apps.miphone.aiai.matchmaker.overview.ui.ContentSuggestionsServiceClient$$ExternalSyntheticLambda0
            public final /* synthetic */ int f$1;
            public final /* synthetic */ Bundle f$2;
            public final /* synthetic */ String f$3;
            public final /* synthetic */ String f$4;
            public final /* synthetic */ long f$5;
            public final /* synthetic */ InteractionContextParcelables$InteractionContext f$6;
            public final /* synthetic */ UserHandle f$7;
            public final /* synthetic */ Uri f$8;
            public final /* synthetic */ ContentSuggestionsServiceWrapper.BundleCallback f$9;

            {
                this.f$1 = r2;
                this.f$2 = r3;
                this.f$3 = r4;
                this.f$4 = r5;
                this.f$5 = r6;
                this.f$6 = r8;
                this.f$7 = r9;
                this.f$8 = r10;
                this.f$9 = r11;
            }

            @Override // java.lang.Runnable
            public final void run() {
                ContentSuggestionsServiceClient.this.lambda$provideScreenshotActions$0$ContentSuggestionsServiceClient(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, this.f$7, this.f$8, this.f$9);
            }
        });
    }

    public /* synthetic */ void lambda$provideScreenshotActions$0$ContentSuggestionsServiceClient(final int i, Bundle bundle, final String str, final String str2, final long j, final InteractionContextParcelables$InteractionContext interactionContextParcelables$InteractionContext, UserHandle userHandle, final Uri uri, final ContentSuggestionsServiceWrapper.BundleCallback bundleCallback) {
        this.serviceWrapper.processContextImage(i, null, bundle);
        Bundle createSelectionsRequest = this.bundleUtils.createSelectionsRequest(str, str2, i, j, interactionContextParcelables$InteractionContext, new Bundle(), ParserParcelables$ParsedViewHierarchy.create());
        createSelectionsRequest.putBoolean("IsManagedProfile", this.userManager.isManagedProfile(userHandle.getIdentifier()));
        createSelectionsRequest.putParcelable("UserHandle", userHandle);
        this.serviceWrapper.suggestContentSelections(i, createSelectionsRequest, new ContentSuggestionsServiceWrapper.BundleCallback() { // from class: com.google.android.apps.miphone.aiai.matchmaker.overview.ui.ContentSuggestionsServiceClient.1
            @Override // com.google.android.apps.miphone.aiai.matchmaker.overview.ui.ContentSuggestionsServiceWrapper.BundleCallback
            public void onResult(Bundle bundle2) {
                try {
                    ContentSuggestionsServiceClient.this.serviceWrapper.classifyContentSelections(ContentSuggestionsServiceClient.this.bundleUtils.createClassificationsRequest(str, str2, i, j, new Bundle(), interactionContextParcelables$InteractionContext, ContentSuggestionsServiceClient.this.bundleUtils.extractContents(bundle2)), new ContentSuggestionsServiceWrapper.BundleCallback() { // from class: com.google.android.apps.miphone.aiai.matchmaker.overview.ui.ContentSuggestionsServiceClient.1.1
                        @Override // com.google.android.apps.miphone.aiai.matchmaker.overview.ui.ContentSuggestionsServiceWrapper.BundleCallback
                        public void onResult(Bundle bundle3) {
                            try {
                                EntitiesData extractEntitiesParcelable = ContentSuggestionsServiceClient.this.bundleUtils.extractEntitiesParcelable(bundle3);
                                SuggestParcelables$Entities create = extractEntitiesParcelable.entities() == null ? SuggestParcelables$Entities.create() : extractEntitiesParcelable.entities();
                                ArrayList<Notification.Action> arrayList = new ArrayList<>();
                                if (create.getEntities() != null) {
                                    for (SuggestParcelables$Entity suggestParcelables$Entity : (List) Utils.checkNotNull(create.getEntities())) {
                                        Notification.Action generateNotificationAction = ContentSuggestionsServiceClient.generateNotificationAction(ContentSuggestionsServiceClient.this.context, suggestParcelables$Entity, extractEntitiesParcelable, uri);
                                        if (generateNotificationAction != null) {
                                            arrayList.add(generateNotificationAction);
                                        }
                                    }
                                }
                                AnonymousClass1 r6 = AnonymousClass1.this;
                                bundleCallback.onResult(ContentSuggestionsServiceClient.this.bundleUtils.createScreenshotActionsResponse(arrayList));
                            } catch (Throwable th) {
                                LogUtils.e("Failed to handle classification result while generating smart actions for screenshot notification", th);
                                bundleCallback.onResult(Bundle.EMPTY);
                            }
                        }
                    });
                } catch (Throwable th) {
                    LogUtils.e("Failed to handle selections response while generating smart actions for screenshot notification", th);
                    bundleCallback.onResult(Bundle.EMPTY);
                }
            }
        });
    }

    public void notifyOp(String str, FeedbackParcelables$ScreenshotOp feedbackParcelables$ScreenshotOp, FeedbackParcelables$ScreenshotOpStatus feedbackParcelables$ScreenshotOpStatus, long j) {
        this.serviceWrapper.notifyInteractionAsync(str, new Supplier(str, feedbackParcelables$ScreenshotOp, feedbackParcelables$ScreenshotOpStatus, j) { // from class: com.google.android.apps.miphone.aiai.matchmaker.overview.ui.ContentSuggestionsServiceClient$$ExternalSyntheticLambda1
            public final /* synthetic */ String f$1;
            public final /* synthetic */ FeedbackParcelables$ScreenshotOp f$2;
            public final /* synthetic */ FeedbackParcelables$ScreenshotOpStatus f$3;
            public final /* synthetic */ long f$4;

            {
                this.f$1 = r2;
                this.f$2 = r3;
                this.f$3 = r4;
                this.f$4 = r5;
            }

            @Override // java.util.function.Supplier
            public final Object get() {
                return ContentSuggestionsServiceClient.this.lambda$notifyOp$1$ContentSuggestionsServiceClient(this.f$1, this.f$2, this.f$3, this.f$4);
            }
        }, null, null);
    }

    public /* synthetic */ Bundle lambda$notifyOp$1$ContentSuggestionsServiceClient(String str, FeedbackParcelables$ScreenshotOp feedbackParcelables$ScreenshotOp, FeedbackParcelables$ScreenshotOpStatus feedbackParcelables$ScreenshotOpStatus, long j) {
        return this.bundleUtils.createFeedbackRequest(FeedbackDataBuilder.newBuilder(str).addScreenshotOpFeedback(str, feedbackParcelables$ScreenshotOp, feedbackParcelables$ScreenshotOpStatus, j).build());
    }

    public void notifyAction(String str, String str2, boolean z, Intent intent) {
        this.serviceWrapper.notifyInteractionAsync(str, new Supplier(str, str2, z, intent) { // from class: com.google.android.apps.miphone.aiai.matchmaker.overview.ui.ContentSuggestionsServiceClient$$ExternalSyntheticLambda2
            public final /* synthetic */ String f$1;
            public final /* synthetic */ String f$2;
            public final /* synthetic */ boolean f$3;
            public final /* synthetic */ Intent f$4;

            {
                this.f$1 = r2;
                this.f$2 = r3;
                this.f$3 = r4;
                this.f$4 = r5;
            }

            @Override // java.util.function.Supplier
            public final Object get() {
                return ContentSuggestionsServiceClient.this.lambda$notifyAction$2$ContentSuggestionsServiceClient(this.f$1, this.f$2, this.f$3, this.f$4);
            }
        }, null, null);
    }

    public /* synthetic */ Bundle lambda$notifyAction$2$ContentSuggestionsServiceClient(String str, String str2, boolean z, Intent intent) {
        return this.bundleUtils.createFeedbackRequest(FeedbackDataBuilder.newBuilder(str).addScreenshotActionFeedback(str, str2, z, intent).build());
    }

    /* access modifiers changed from: private */
    @Nullable
    public static Notification.Action generateNotificationAction(Context context, SuggestParcelables$Entity suggestParcelables$Entity, EntitiesData entitiesData, @Nullable Uri uri) {
        String str;
        if (suggestParcelables$Entity.getActions() != null && !((List) Utils.checkNotNull(suggestParcelables$Entity.getActions())).isEmpty()) {
            SuggestParcelables$Action mainAction = ((SuggestParcelables$ActionGroup) ((List) Utils.checkNotNull(suggestParcelables$Entity.getActions())).get(0)).getMainAction();
            if (mainAction == null || mainAction.getId() == null) {
                LogUtils.d("Malformed mainAction: Expected id");
            } else {
                if (uri != null && mainAction.getHasProxiedIntentInfo()) {
                    grantReadUriPermissionIfLensAction(context, ((SuggestParcelables$IntentInfo) Utils.checkNotNull(mainAction.getProxiedIntentInfo())).getIntentType(), (Uri) Utils.checkNotNull(uri));
                }
                Bitmap bitmap = entitiesData.getBitmap((String) Utils.checkNotNull(mainAction.getId()));
                PendingIntent pendingIntent = ((EntitiesData) Utils.checkNotNull(entitiesData)).getPendingIntent((String) Utils.checkNotNull(mainAction.getId()));
                if (pendingIntent == null || bitmap == null) {
                    LogUtils.d("Malformed EntitiesData: Expected icon bitmap and intent");
                    return null;
                }
                String firstNonEmptyString = getFirstNonEmptyString((String) Utils.checkNotNull(mainAction.getDisplayName()), (String) Utils.checkNotNull(mainAction.getFullDisplayName()), (String) Utils.checkNotNull(suggestParcelables$Entity.getSearchQueryHint()));
                if (firstNonEmptyString == null) {
                    LogUtils.d("Title expected.");
                    return null;
                }
                RemoteAction remoteAction = new RemoteAction(Icon.createWithBitmap(bitmap), firstNonEmptyString, firstNonEmptyString, pendingIntent);
                if (TextUtils.isEmpty(suggestParcelables$Entity.getSearchQueryHint())) {
                    str = "Smart Action";
                } else {
                    str = suggestParcelables$Entity.getSearchQueryHint();
                }
                return createNotificationActionFromRemoteAction(remoteAction, (String) Utils.checkNotNull(str), 1.0f);
            }
        }
        return null;
    }

    private static void grantReadUriPermissionIfLensAction(Context context, SuggestParcelables$IntentType suggestParcelables$IntentType, Uri uri) {
        if (suggestParcelables$IntentType == SuggestParcelables$IntentType.LENS) {
            context.grantUriPermission("com.google.android.googlequicksearchbox", uri, 1);
        }
    }

    @Nullable
    private static String getFirstNonEmptyString(@Nullable String... strArr) {
        if (strArr == null) {
            return null;
        }
        for (String str : strArr) {
            if (!TextUtils.isEmpty(str)) {
                return str;
            }
        }
        return null;
    }

    private static Notification.Action createNotificationActionFromRemoteAction(RemoteAction remoteAction, String str, float f) {
        Icon icon = remoteAction.shouldShowIcon() ? remoteAction.getIcon() : null;
        Bundle bundle = new Bundle();
        bundle.putString("action_type", str);
        bundle.putFloat("action_score", f);
        return new Notification.Action.Builder(icon, remoteAction.getTitle(), remoteAction.getActionIntent()).setContextual(true).addExtras(bundle).build();
    }
}
