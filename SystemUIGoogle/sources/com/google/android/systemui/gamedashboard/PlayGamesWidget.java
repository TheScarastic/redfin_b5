package com.google.android.systemui.gamedashboard;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcel;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.android.systemui.R$layout;
import com.google.android.systemui.gamedashboard.GameDashboardUiEventLogger;
/* loaded from: classes2.dex */
public class PlayGamesWidget {
    private final Context mContext;
    private final Handler mMainHandler;
    private final GameDashboardUiEventLogger mUiEventLogger;
    private final ResultReceiver mWidgetResultReceiver = createPlayGamesWidgetResultReceiver();
    private final WidgetView mWidgetView;

    private static boolean hasPlayGamesWidget(Context context) {
        try {
            ProviderInfo[] providerInfoArr = context.getPackageManager().getPackageInfo("com.google.android.play.games", 8).providers;
            for (ProviderInfo providerInfo : providerInfoArr) {
                if (providerInfo.authority.equals("com.google.android.play.games.dashboard.tile.provider") && providerInfo.enabled) {
                    return true;
                }
            }
        } catch (PackageManager.NameNotFoundException unused) {
            Log.v("PlayGamesWidget", "Play Games package not found.");
        }
        return false;
    }

    public static PlayGamesWidget create(Context context, ViewGroup viewGroup, Handler handler, GameDashboardUiEventLogger gameDashboardUiEventLogger) {
        if (!hasPlayGamesWidget(context)) {
            return null;
        }
        return new PlayGamesWidget(context, (WidgetView) LayoutInflater.from(context).inflate(R$layout.game_menu_widget, viewGroup, false), handler, gameDashboardUiEventLogger);
    }

    public PlayGamesWidget(Context context, WidgetView widgetView, Handler handler, GameDashboardUiEventLogger gameDashboardUiEventLogger) {
        this.mContext = context;
        this.mWidgetView = widgetView;
        this.mMainHandler = handler;
        this.mUiEventLogger = gameDashboardUiEventLogger;
    }

    public WidgetView getView() {
        return this.mWidgetView;
    }

    public void update(String str) {
        this.mWidgetView.setLoading(true);
        this.mUiEventLogger.log(GameDashboardUiEventLogger.GameDashboardEvent.GAME_DASHBOARD_PLAY_GAMES_IMPRESSION);
        this.mMainHandler.post(new Runnable(str) { // from class: com.google.android.systemui.gamedashboard.PlayGamesWidget$$ExternalSyntheticLambda1
            public final /* synthetic */ String f$1;

            {
                this.f$1 = r2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                PlayGamesWidget.$r8$lambda$SnFrBXwmoRQ2SzADZtMrNnq5OjY(PlayGamesWidget.this, this.f$1);
            }
        });
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$update$0(String str) {
        Bundle bundle = new Bundle();
        bundle.putString("foregroundAppPackageName", str);
        bundle.putParcelable("resultReceiver", this.mWidgetResultReceiver);
        try {
            Bundle call = this.mContext.getContentResolver().call("com.google.android.play.games.dashboard.tile.provider", "getGameMenuWidgetInfo", (String) null, bundle);
            if (call == null) {
                Log.v("PlayGamesWidget", "Play Games widget provider returns no data.");
                return;
            }
            int i = call.getInt("resultCode");
            if (i == 2) {
                Log.v("PlayGamesWidget", "Play Games widget provider issues asynchronous update.");
            } else if (i == 0) {
                Log.w("PlayGamesWidget", "Play Games widget provider returns UNKNOWN");
            } else {
                updateFromData(call);
            }
        } catch (Exception unused) {
            Log.w("PlayGamesWidget", "Failed to call Play Games widget provider.");
        }
    }

    /* access modifiers changed from: private */
    public void updateFromData(Bundle bundle) {
        Bitmap bitmap = (Bitmap) bundle.getParcelable("icon");
        String string = bundle.getString("title");
        String string2 = bundle.getString("tipText");
        String string3 = bundle.getString("description");
        PendingIntent pendingIntent = (PendingIntent) bundle.getParcelable("onClickPendingIntent");
        boolean z = bundle.getBoolean("isActive");
        this.mWidgetView.update(bitmap, string2, string, string3, z ? new View.OnClickListener(pendingIntent) { // from class: com.google.android.systemui.gamedashboard.PlayGamesWidget$$ExternalSyntheticLambda0
            public final /* synthetic */ PendingIntent f$1;

            {
                this.f$1 = r2;
            }

            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                PlayGamesWidget.m652$r8$lambda$k4MqZvK52zUcXxceuKQhvx7vjs(PlayGamesWidget.this, this.f$1, view);
            }
        } : null);
        this.mWidgetView.setEnabled(z);
        this.mWidgetView.setLoading(false);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$updateFromData$1(PendingIntent pendingIntent, View view) {
        try {
            this.mUiEventLogger.log(GameDashboardUiEventLogger.GameDashboardEvent.GAME_DASHBOARD_MENU_PLAY_GAMES_WIDGET);
            ((Activity) this.mContext).startIntentSenderForResult(pendingIntent.getIntentSender(), 0, null, 0, 0, 0);
        } catch (IntentSender.SendIntentException unused) {
            Log.w("PlayGamesWidget", "Failed to start Play Games PendingIntent.");
        }
    }

    private ResultReceiver createPlayGamesWidgetResultReceiver() {
        PlayGamesWidgetResultReceiver playGamesWidgetResultReceiver = new PlayGamesWidgetResultReceiver(this.mMainHandler);
        Parcel obtain = Parcel.obtain();
        playGamesWidgetResultReceiver.writeToParcel(obtain, 0);
        obtain.setDataPosition(0);
        ResultReceiver resultReceiver = (ResultReceiver) ResultReceiver.CREATOR.createFromParcel(obtain);
        obtain.recycle();
        return resultReceiver;
    }

    /* loaded from: classes2.dex */
    public class PlayGamesWidgetResultReceiver extends ResultReceiver {
        public PlayGamesWidgetResultReceiver(Handler handler) {
            super(handler);
        }

        @Override // android.os.ResultReceiver
        protected void onReceiveResult(int i, Bundle bundle) {
            PlayGamesWidget.this.updateFromData(bundle);
        }
    }
}
