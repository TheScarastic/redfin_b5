package com.android.systemui.globalactions;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.service.quickaccesswallet.QuickAccessWalletClient;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.android.systemui.R$bool;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
import com.android.systemui.R$string;
import com.android.systemui.controls.controller.ControlsController;
import com.android.systemui.plugins.ActivityStarter;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: GlobalActionsInfoProvider.kt */
/* loaded from: classes.dex */
public final class GlobalActionsInfoProvider {
    private final ActivityStarter activityStarter;
    private final Context context;
    private final ControlsController controlsController;
    private PendingIntent pendingIntent;
    private final QuickAccessWalletClient walletClient;

    public GlobalActionsInfoProvider(Context context, QuickAccessWalletClient quickAccessWalletClient, ControlsController controlsController, ActivityStarter activityStarter) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(quickAccessWalletClient, "walletClient");
        Intrinsics.checkNotNullParameter(controlsController, "controlsController");
        Intrinsics.checkNotNullParameter(activityStarter, "activityStarter");
        this.context = context;
        this.walletClient = quickAccessWalletClient;
        this.controlsController = controlsController;
        this.activityStarter = activityStarter;
        String string = context.getResources().getString(R$string.global_actions_change_url);
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setData(Uri.parse(string));
        intent.addFlags(268435456);
        PendingIntent activity = PendingIntent.getActivity(context, 0, intent, 67108864);
        Intrinsics.checkNotNullExpressionValue(activity, "getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)");
        this.pendingIntent = activity;
    }

    public final void addPanel(Context context, ViewGroup viewGroup, int i, Runnable runnable) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(viewGroup, "parent");
        Intrinsics.checkNotNullParameter(runnable, "dismissParent");
        if (!(context.getResources().getConfiguration().orientation == 2) || i <= 4) {
            View inflate = LayoutInflater.from(context).inflate(R$layout.global_actions_change_panel, viewGroup, false);
            Object serviceLabel = this.walletClient.getServiceLabel();
            if (serviceLabel == null) {
                serviceLabel = context.getString(R$string.wallet_title);
            }
            TextView textView = (TextView) inflate.findViewById(R$id.global_actions_change_message);
            if (textView != null) {
                textView.setText(context.getString(R$string.global_actions_change_description, serviceLabel));
            }
            inflate.setOnClickListener(new View.OnClickListener(runnable, this) { // from class: com.android.systemui.globalactions.GlobalActionsInfoProvider$addPanel$1
                final /* synthetic */ Runnable $dismissParent;
                final /* synthetic */ GlobalActionsInfoProvider this$0;

                /* access modifiers changed from: package-private */
                {
                    this.$dismissParent = r1;
                    this.this$0 = r2;
                }

                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    this.$dismissParent.run();
                    this.this$0.activityStarter.postStartActivityDismissingKeyguard(this.this$0.pendingIntent);
                }
            });
            viewGroup.addView(inflate, 0);
            incrementViewCount();
        }
    }

    public final boolean shouldShowMessage() {
        int i;
        if (!this.context.getResources().getBoolean(R$bool.global_actions_show_change_info)) {
            return false;
        }
        SharedPreferences sharedPreferences = this.context.getSharedPreferences("global_actions_info_prefs", 0);
        if (sharedPreferences.contains("view_count") || hadContent()) {
            i = sharedPreferences.getInt("view_count", 0);
        } else {
            i = -1;
        }
        if (i <= -1 || i >= 3) {
            return false;
        }
        return true;
    }

    private final boolean hadContent() {
        boolean z = this.controlsController.getFavorites().size() > 0;
        boolean isWalletFeatureAvailable = this.walletClient.isWalletFeatureAvailable();
        Log.d("GlobalActionsInfo", "Previously had controls " + z + ", cards " + isWalletFeatureAvailable);
        return z || isWalletFeatureAvailable;
    }

    private final void incrementViewCount() {
        SharedPreferences sharedPreferences = this.context.getSharedPreferences("global_actions_info_prefs", 0);
        sharedPreferences.edit().putInt("view_count", sharedPreferences.getInt("view_count", 0) + 1).apply();
    }
}
