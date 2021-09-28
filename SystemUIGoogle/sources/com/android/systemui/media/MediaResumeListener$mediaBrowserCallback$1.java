package com.android.systemui.media;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.media.MediaDescription;
import android.media.session.MediaSession;
import android.util.Log;
import com.android.systemui.media.ResumeMediaBrowser;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: MediaResumeListener.kt */
/* loaded from: classes.dex */
public final class MediaResumeListener$mediaBrowserCallback$1 extends ResumeMediaBrowser.Callback {
    final /* synthetic */ MediaResumeListener this$0;

    /* access modifiers changed from: package-private */
    public MediaResumeListener$mediaBrowserCallback$1(MediaResumeListener mediaResumeListener) {
        this.this$0 = mediaResumeListener;
    }

    @Override // com.android.systemui.media.ResumeMediaBrowser.Callback
    public void addTrack(MediaDescription mediaDescription, ComponentName componentName, ResumeMediaBrowser resumeMediaBrowser) {
        Intrinsics.checkNotNullParameter(mediaDescription, "desc");
        Intrinsics.checkNotNullParameter(componentName, "component");
        Intrinsics.checkNotNullParameter(resumeMediaBrowser, "browser");
        MediaSession.Token token = resumeMediaBrowser.getToken();
        PendingIntent appIntent = resumeMediaBrowser.getAppIntent();
        PackageManager packageManager = this.this$0.context.getPackageManager();
        String packageName = componentName.getPackageName();
        Intrinsics.checkNotNullExpressionValue(packageName, "component.packageName");
        Runnable runnable = this.this$0.getResumeAction(componentName);
        try {
            CharSequence applicationLabel = packageManager.getApplicationLabel(packageManager.getApplicationInfo(componentName.getPackageName(), 0));
            Intrinsics.checkNotNullExpressionValue(applicationLabel, "pm.getApplicationLabel(\n                        pm.getApplicationInfo(component.packageName, 0))");
            packageName = applicationLabel;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("MediaResumeListener", "Error getting package information", e);
        }
        Log.d("MediaResumeListener", Intrinsics.stringPlus("Adding resume controls ", mediaDescription));
        MediaDataManager mediaDataManager = this.this$0.mediaDataManager;
        if (mediaDataManager != null) {
            int i = this.this$0.currentUserId;
            Intrinsics.checkNotNullExpressionValue(token, "token");
            String obj = packageName.toString();
            Intrinsics.checkNotNullExpressionValue(appIntent, "appIntent");
            String packageName2 = componentName.getPackageName();
            Intrinsics.checkNotNullExpressionValue(packageName2, "component.packageName");
            mediaDataManager.addResumptionControls(i, mediaDescription, runnable, token, obj, appIntent, packageName2);
            return;
        }
        Intrinsics.throwUninitializedPropertyAccessException("mediaDataManager");
        throw null;
    }
}
