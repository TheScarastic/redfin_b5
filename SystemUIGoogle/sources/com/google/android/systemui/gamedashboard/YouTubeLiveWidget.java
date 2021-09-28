package com.google.android.systemui.gamedashboard;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.android.systemui.R$drawable;
import com.android.systemui.R$layout;
import com.android.systemui.R$string;
import com.google.android.systemui.gamedashboard.GameDashboardUiEventLogger;
import java.util.List;
/* loaded from: classes2.dex */
public class YouTubeLiveWidget {
    private final Context mContext;
    private final GameDashboardUiEventLogger mUiEventLogger;
    private final WidgetView mWidgetView;

    private static boolean hasYouTubeLiveStream(Context context) {
        List<ResolveInfo> queryIntentActivities = context.getPackageManager().queryIntentActivities(new Intent("com.google.android.youtube.intent.action.CREATE_LIVE_STREAM").setPackage("com.google.android.youtube"), 65536);
        return queryIntentActivities != null && !queryIntentActivities.isEmpty();
    }

    public static YouTubeLiveWidget create(Context context, ViewGroup viewGroup, GameDashboardUiEventLogger gameDashboardUiEventLogger) {
        return new YouTubeLiveWidget(context, (WidgetView) LayoutInflater.from(context).inflate(R$layout.game_menu_widget, viewGroup, false), gameDashboardUiEventLogger);
    }

    public YouTubeLiveWidget(Context context, WidgetView widgetView, GameDashboardUiEventLogger gameDashboardUiEventLogger) {
        this.mContext = context;
        this.mWidgetView = widgetView;
        this.mUiEventLogger = gameDashboardUiEventLogger;
    }

    public WidgetView getView() {
        return this.mWidgetView;
    }

    public void update(String str) {
        if (!hasYouTubeLiveStream(this.mContext)) {
            this.mWidgetView.update(this.mContext.getDrawable(R$drawable.ic_youtube_live_logo), R$string.game_menu_youtube_live_stream_widget_title, R$string.game_menu_youtube_live_stream_widget_description, null);
            this.mWidgetView.setEnabled(false);
            return;
        }
        this.mWidgetView.update(this.mContext.getDrawable(R$drawable.ic_youtube_live_logo), R$string.game_menu_youtube_live_stream_widget_title, R$string.game_menu_youtube_live_stream_widget_description, new View.OnClickListener(str) { // from class: com.google.android.systemui.gamedashboard.YouTubeLiveWidget$$ExternalSyntheticLambda0
            public final /* synthetic */ String f$1;

            {
                this.f$1 = r2;
            }

            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                YouTubeLiveWidget.$r8$lambda$ZbGi5NuiG7vDfZD7xE5oEKRVnEA(YouTubeLiveWidget.this, this.f$1, view);
            }
        });
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$update$0(String str, View view) {
        onYouTubeLiveStreamWidgetClicked(str);
    }

    private void onYouTubeLiveStreamWidgetClicked(String str) {
        this.mUiEventLogger.log(GameDashboardUiEventLogger.GameDashboardEvent.GAME_DASHBOARD_MENU_YOUTUBE_LIVE_WIDGET);
        Intent intent = new Intent("com.google.android.youtube.intent.action.CREATE_LIVE_STREAM").setPackage("com.google.android.youtube");
        intent.putExtra("android.intent.extra.REFERRER", new Uri.Builder().scheme("android-app").appendPath(this.mContext.getPackageName()).build());
        if (!TextUtils.isEmpty(str)) {
            intent.putExtra("GAME_PACKAGE_NAME", str);
            PackageManager packageManager = this.mContext.getPackageManager();
            ApplicationInfo applicationInfo = null;
            try {
                applicationInfo = packageManager.getApplicationInfo(str, 0);
            } catch (PackageManager.NameNotFoundException unused) {
                Log.w("YouTubeLiveWidget", "Fail to query application info for " + str);
            }
            if (applicationInfo != null) {
                intent.putExtra("GAME_TITLE", packageManager.getApplicationLabel(applicationInfo).toString());
            }
        }
        intent.putExtra("CAPTURE_MODE", "SCREEN");
        ((Activity) this.mContext).startActivityForResult(intent, 0);
    }
}
