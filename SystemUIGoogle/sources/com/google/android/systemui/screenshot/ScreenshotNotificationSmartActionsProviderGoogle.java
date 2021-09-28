package com.google.android.systemui.screenshot;

import android.app.Notification;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.os.UserHandle;
import android.util.Log;
import com.android.systemui.screenshot.ScreenshotNotificationSmartActionsProvider;
import com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2.FeedbackParcelables$ScreenshotOp;
import com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2.FeedbackParcelables$ScreenshotOpStatus;
import com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2.SuggestParcelables$InteractionType;
import com.google.android.apps.miphone.aiai.matchmaker.overview.ui.ContentSuggestionsServiceClient;
import com.google.android.apps.miphone.aiai.matchmaker.overview.ui.ContentSuggestionsServiceWrapper;
import com.google.common.collect.ImmutableMap;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
/* loaded from: classes2.dex */
public final class ScreenshotNotificationSmartActionsProviderGoogle extends ScreenshotNotificationSmartActionsProvider {
    private final ContentSuggestionsServiceClient mClient;
    private static final ImmutableMap<ScreenshotNotificationSmartActionsProvider.ScreenshotOp, FeedbackParcelables$ScreenshotOp> SCREENSHOT_OP_MAP = ImmutableMap.builder().put(ScreenshotNotificationSmartActionsProvider.ScreenshotOp.RETRIEVE_SMART_ACTIONS, FeedbackParcelables$ScreenshotOp.RETRIEVE_SMART_ACTIONS).put(ScreenshotNotificationSmartActionsProvider.ScreenshotOp.REQUEST_SMART_ACTIONS, FeedbackParcelables$ScreenshotOp.REQUEST_SMART_ACTIONS).put(ScreenshotNotificationSmartActionsProvider.ScreenshotOp.WAIT_FOR_SMART_ACTIONS, FeedbackParcelables$ScreenshotOp.WAIT_FOR_SMART_ACTIONS).build();
    private static final ImmutableMap<ScreenshotNotificationSmartActionsProvider.ScreenshotOpStatus, FeedbackParcelables$ScreenshotOpStatus> SCREENSHOT_OP_STATUS_MAP = ImmutableMap.builder().put(ScreenshotNotificationSmartActionsProvider.ScreenshotOpStatus.SUCCESS, FeedbackParcelables$ScreenshotOpStatus.SUCCESS).put(ScreenshotNotificationSmartActionsProvider.ScreenshotOpStatus.ERROR, FeedbackParcelables$ScreenshotOpStatus.ERROR).put(ScreenshotNotificationSmartActionsProvider.ScreenshotOpStatus.TIMEOUT, FeedbackParcelables$ScreenshotOpStatus.TIMEOUT).build();
    private static final ImmutableMap<ScreenshotNotificationSmartActionsProvider.ScreenshotSmartActionType, SuggestParcelables$InteractionType> SCREENSHOT_INTERACTION_TYPE_MAP = ImmutableMap.builder().put(ScreenshotNotificationSmartActionsProvider.ScreenshotSmartActionType.REGULAR_SMART_ACTIONS, SuggestParcelables$InteractionType.SCREENSHOT_NOTIFICATION).put(ScreenshotNotificationSmartActionsProvider.ScreenshotSmartActionType.QUICK_SHARE_ACTION, SuggestParcelables$InteractionType.QUICK_SHARE).build();

    public ScreenshotNotificationSmartActionsProviderGoogle(Context context, Executor executor, Handler handler) {
        this.mClient = new ContentSuggestionsServiceClient(context, executor, handler);
    }

    @Override // com.android.systemui.screenshot.ScreenshotNotificationSmartActionsProvider
    public CompletableFuture<List<Notification.Action>> getActions(final String str, Uri uri, Bitmap bitmap, ComponentName componentName, ScreenshotNotificationSmartActionsProvider.ScreenshotSmartActionType screenshotSmartActionType, UserHandle userHandle) {
        final CompletableFuture<List<Notification.Action>> completableFuture = new CompletableFuture<>();
        if (bitmap.getConfig() != Bitmap.Config.HARDWARE) {
            Log.e("ScreenshotActionsGoogle", String.format("Bitmap expected: Hardware, Bitmap found: %s. Returning empty list.", bitmap.getConfig()));
            return CompletableFuture.completedFuture(Collections.emptyList());
        }
        final long uptimeMillis = SystemClock.uptimeMillis();
        Log.d("ScreenshotActionsGoogle", "Calling AiAi to obtain screenshot notification smart actions.");
        this.mClient.provideScreenshotActions(bitmap, uri, componentName.getPackageName(), componentName.getClassName(), userHandle, SCREENSHOT_INTERACTION_TYPE_MAP.getOrDefault(screenshotSmartActionType, SuggestParcelables$InteractionType.SCREENSHOT_NOTIFICATION), new ContentSuggestionsServiceWrapper.BundleCallback() { // from class: com.google.android.systemui.screenshot.ScreenshotNotificationSmartActionsProviderGoogle.1
            @Override // com.google.android.apps.miphone.aiai.matchmaker.overview.ui.ContentSuggestionsServiceWrapper.BundleCallback
            public void onResult(Bundle bundle) {
                ScreenshotNotificationSmartActionsProviderGoogle.this.completeFuture(bundle, completableFuture);
                long uptimeMillis2 = SystemClock.uptimeMillis() - uptimeMillis;
                Log.d("ScreenshotActionsGoogle", String.format("Total time taken to get smart actions: %d ms", Long.valueOf(uptimeMillis2)));
                ScreenshotNotificationSmartActionsProviderGoogle.this.notifyOp(str, ScreenshotNotificationSmartActionsProvider.ScreenshotOp.RETRIEVE_SMART_ACTIONS, ScreenshotNotificationSmartActionsProvider.ScreenshotOpStatus.SUCCESS, uptimeMillis2);
            }
        });
        return completableFuture;
    }

    @Override // com.android.systemui.screenshot.ScreenshotNotificationSmartActionsProvider
    public void notifyOp(String str, ScreenshotNotificationSmartActionsProvider.ScreenshotOp screenshotOp, ScreenshotNotificationSmartActionsProvider.ScreenshotOpStatus screenshotOpStatus, long j) {
        this.mClient.notifyOp(str, SCREENSHOT_OP_MAP.getOrDefault(screenshotOp, FeedbackParcelables$ScreenshotOp.OP_UNKNOWN), SCREENSHOT_OP_STATUS_MAP.getOrDefault(screenshotOpStatus, FeedbackParcelables$ScreenshotOpStatus.OP_STATUS_UNKNOWN), j);
    }

    @Override // com.android.systemui.screenshot.ScreenshotNotificationSmartActionsProvider
    public void notifyAction(String str, String str2, boolean z, Intent intent) {
        this.mClient.notifyAction(str, str2, z, intent);
    }

    void completeFuture(Bundle bundle, CompletableFuture<List<Notification.Action>> completableFuture) {
        if (bundle.containsKey("ScreenshotNotificationActions")) {
            completableFuture.complete(bundle.getParcelableArrayList("ScreenshotNotificationActions"));
        } else {
            completableFuture.complete(Collections.emptyList());
        }
    }
}
