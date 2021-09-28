package com.google.android.systemui.smartspace;

import android.app.PendingIntent;
import android.app.smartspace.SmartspaceAction;
import android.app.smartspace.SmartspaceTarget;
import android.app.smartspace.SmartspaceTargetEvent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.util.Log;
import android.view.View;
import com.android.systemui.bcsmartspace.R$dimen;
import com.android.systemui.plugins.BcSmartspaceDataPlugin;
import com.android.systemui.plugins.FalsingManager;
/* loaded from: classes2.dex */
public final class BcSmartSpaceUtil {
    private static FalsingManager sFalsingManager;
    private static BcSmartspaceDataPlugin.IntentStarter sIntentStarter;

    public static int getLoggingCardType(int i) {
        if (i == 10) {
            return 5;
        }
        if (i == 17) {
            return 12;
        }
        if (i == 29) {
            return 15;
        }
        if (i == 21) {
            return 10;
        }
        if (i == 22) {
            return 11;
        }
        switch (i) {
            case 1:
                return 4;
            case 2:
                return 2;
            case 3:
                return 1;
            case 4:
                return 3;
            case 5:
                return 17;
            case 6:
                return 13;
            default:
                switch (i) {
                    case 13:
                        return 6;
                    case 14:
                        return 7;
                    case 15:
                        return 8;
                    default:
                        return 0;
                }
        }
    }

    public static void setOnClickListener(View view, SmartspaceTarget smartspaceTarget, SmartspaceAction smartspaceAction, String str, BcSmartspaceDataPlugin.SmartspaceEventNotifier smartspaceEventNotifier, BcSmartspaceCardLoggingInfo bcSmartspaceCardLoggingInfo) {
        setOnClickListener(view, smartspaceTarget, smartspaceAction, null, smartspaceEventNotifier, str, bcSmartspaceCardLoggingInfo);
    }

    public static void setOnClickListener(View view, SmartspaceTarget smartspaceTarget, SmartspaceAction smartspaceAction, View.OnClickListener onClickListener, BcSmartspaceDataPlugin.SmartspaceEventNotifier smartspaceEventNotifier, String str, BcSmartspaceCardLoggingInfo bcSmartspaceCardLoggingInfo) {
        if (view == null || smartspaceAction == null || (smartspaceAction.getIntent() == null && smartspaceAction.getPendingIntent() == null)) {
            Log.e(str, "No tap action can be set up");
            return;
        }
        BcSmartspaceDataPlugin.IntentStarter intentStarter = sIntentStarter;
        if (intentStarter == null) {
            intentStarter = defaultIntentStarter(str);
        }
        view.setOnClickListener(new View.OnClickListener(intentStarter, smartspaceAction, onClickListener, smartspaceEventNotifier, str, smartspaceTarget) { // from class: com.google.android.systemui.smartspace.BcSmartSpaceUtil$$ExternalSyntheticLambda0
            public final /* synthetic */ BcSmartspaceDataPlugin.IntentStarter f$1;
            public final /* synthetic */ SmartspaceAction f$2;
            public final /* synthetic */ View.OnClickListener f$3;
            public final /* synthetic */ BcSmartspaceDataPlugin.SmartspaceEventNotifier f$4;
            public final /* synthetic */ String f$5;
            public final /* synthetic */ SmartspaceTarget f$6;

            {
                this.f$1 = r2;
                this.f$2 = r3;
                this.f$3 = r4;
                this.f$4 = r5;
                this.f$5 = r6;
                this.f$6 = r7;
            }

            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                BcSmartSpaceUtil.m662$r8$lambda$pF3BUC1agVsehUiTfvGt35rdDQ(BcSmartspaceCardLoggingInfo.this, this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, view2);
            }
        });
    }

    public static /* synthetic */ void lambda$setOnClickListener$0(BcSmartspaceCardLoggingInfo bcSmartspaceCardLoggingInfo, BcSmartspaceDataPlugin.IntentStarter intentStarter, SmartspaceAction smartspaceAction, View.OnClickListener onClickListener, BcSmartspaceDataPlugin.SmartspaceEventNotifier smartspaceEventNotifier, String str, SmartspaceTarget smartspaceTarget, View view) {
        BcSmartspaceLogger.log(BcSmartspaceEvent.SMARTSPACE_CARD_CLICK, bcSmartspaceCardLoggingInfo);
        FalsingManager falsingManager = sFalsingManager;
        if (falsingManager == null || !falsingManager.isFalseTap(1)) {
            intentStarter.startFromAction(smartspaceAction, view);
            if (onClickListener != null) {
                onClickListener.onClick(view);
            }
            if (smartspaceEventNotifier == null) {
                Log.w(str, "Cannot notify target interaction smartspace event: event notifier null.");
            } else {
                smartspaceEventNotifier.notifySmartspaceEvent(new SmartspaceTargetEvent.Builder(1).setSmartspaceTarget(smartspaceTarget).setSmartspaceActionId(smartspaceAction.getId()).build());
            }
        }
    }

    public static Drawable getIconDrawable(Icon icon, Context context) {
        Drawable drawable;
        if (icon == null) {
            return null;
        }
        if (icon.getType() == 1 || icon.getType() == 5) {
            drawable = new BitmapDrawable(context.getResources(), icon.getBitmap());
        } else {
            drawable = icon.loadDrawable(context);
        }
        if (drawable != null) {
            int dimensionPixelSize = context.getResources().getDimensionPixelSize(R$dimen.enhanced_smartspace_icon_size);
            drawable.setBounds(0, 0, dimensionPixelSize, dimensionPixelSize);
        }
        return drawable;
    }

    public static void setFalsingManager(FalsingManager falsingManager) {
        sFalsingManager = falsingManager;
    }

    public static void setIntentStarter(BcSmartspaceDataPlugin.IntentStarter intentStarter) {
        sIntentStarter = intentStarter;
    }

    private static BcSmartspaceDataPlugin.IntentStarter defaultIntentStarter(final String str) {
        return new BcSmartspaceDataPlugin.IntentStarter() { // from class: com.google.android.systemui.smartspace.BcSmartSpaceUtil.1
            @Override // com.android.systemui.plugins.BcSmartspaceDataPlugin.IntentStarter
            public void startIntent(View view, Intent intent) {
                try {
                    view.getContext().startActivity(intent);
                } catch (ActivityNotFoundException | NullPointerException | SecurityException e) {
                    Log.e(str, "Cannot invoke smartspace intent", e);
                }
            }

            @Override // com.android.systemui.plugins.BcSmartspaceDataPlugin.IntentStarter
            public void startPendingIntent(PendingIntent pendingIntent) {
                try {
                    pendingIntent.send();
                } catch (PendingIntent.CanceledException e) {
                    Log.e(str, "Cannot invoke canceled smartspace intent", e);
                }
            }
        };
    }

    public static boolean isLoggable(String str) {
        return Log.isLoggable(str, 2);
    }

    public static int getLoggingDisplaySurface(String str, float f) {
        str.hashCode();
        if (str.equals("com.google.android.apps.nexuslauncher")) {
            return 1;
        }
        if (!str.equals("com.android.systemui")) {
            return 0;
        }
        return ((double) f) > 0.5d ? 3 : 2;
    }
}
