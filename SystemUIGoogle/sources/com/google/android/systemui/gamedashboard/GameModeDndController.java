package com.google.android.systemui.gamedashboard;

import android.app.AutomaticZenRule;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.service.notification.Condition;
import android.util.Log;
import com.android.systemui.R$string;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.settings.CurrentUserTracker;
import java.util.Map;
/* loaded from: classes2.dex */
public final class GameModeDndController {
    private final Context mContext;
    private boolean mFilterActiveOld;
    private boolean mGameActive;
    private boolean mGameActiveOld;
    private final BroadcastReceiver mIntentReceiver;
    private final NotificationManager mNotificationManager;
    private String mRuleName;
    private boolean mUserActive;
    private boolean mUserActiveOld;
    private final CurrentUserTracker mUserTracker;
    private static final Uri CONDITION_ID = new Uri.Builder().scheme("android-app").authority("com.android.systemui").appendPath("game-mode-dnd-controller").build();
    private static final ComponentName COMPONENT_NAME = new ComponentName("com.android.systemui", GameDndConfigActivity.class.getName());
    private String mRuleId = getOrCreateRuleId();
    private boolean mFilterActive = getFilterActive();

    public GameModeDndController(Context context, NotificationManager notificationManager, BroadcastDispatcher broadcastDispatcher) {
        this.mContext = context;
        this.mNotificationManager = notificationManager;
        this.mRuleName = context.getString(R$string.game_mode_dnd_rule_name);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.USER_PRESENT");
        intentFilter.addAction("android.intent.action.SCREEN_OFF");
        intentFilter.addAction("android.intent.action.LOCALE_CHANGED");
        BroadcastReceiver createReceiver = createReceiver();
        this.mIntentReceiver = createReceiver;
        context.registerReceiver(createReceiver, intentFilter);
        AnonymousClass1 r2 = new CurrentUserTracker(broadcastDispatcher) { // from class: com.google.android.systemui.gamedashboard.GameModeDndController.1
            @Override // com.android.systemui.settings.CurrentUserTracker
            public void onUserSwitched(int i) {
                GameModeDndController gameModeDndController = GameModeDndController.this;
                gameModeDndController.mRuleId = gameModeDndController.getOrCreateRuleId();
            }
        };
        this.mUserTracker = r2;
        r2.startTracking();
        updateRule();
    }

    public void refreshRule() {
        this.mFilterActive = getFilterActive();
        updateState();
    }

    /* access modifiers changed from: private */
    public String getOrCreateRuleId() {
        for (Map.Entry<String, AutomaticZenRule> entry : this.mNotificationManager.getAutomaticZenRules().entrySet()) {
            if (entry.getValue().getConditionId().equals(CONDITION_ID)) {
                return entry.getKey();
            }
        }
        try {
            return this.mNotificationManager.addAutomaticZenRule(new AutomaticZenRule(this.mRuleName, null, COMPONENT_NAME, CONDITION_ID, null, 1, true));
        } catch (IllegalArgumentException unused) {
            Log.w("GameModeDndController", "Failed to create zen rule due to missing configuration Activity.");
            return null;
        }
    }

    private boolean getFilterActive() {
        AutomaticZenRule fetchRule = fetchRule();
        return fetchRule != null && fetchRule.getInterruptionFilter() == 2;
    }

    private AutomaticZenRule fetchRule() {
        String str = this.mRuleId;
        if (str == null) {
            return null;
        }
        AutomaticZenRule automaticZenRule = this.mNotificationManager.getAutomaticZenRule(str);
        if (automaticZenRule != null) {
            return automaticZenRule;
        }
        Log.v("GameModeDndController", "Fetched new rule id outside of user switch handler: " + this.mRuleId);
        String orCreateRuleId = getOrCreateRuleId();
        this.mRuleId = orCreateRuleId;
        return this.mNotificationManager.getAutomaticZenRule(orCreateRuleId);
    }

    public boolean isGameModeDndOn() {
        return this.mGameActive && this.mFilterActive && this.mUserActive;
    }

    public void setGameModeDndOn(boolean z) {
        this.mFilterActive = z;
        this.mGameActive = z;
        updateRule();
    }

    private boolean isFilterModified() {
        return this.mFilterActive != this.mFilterActiveOld;
    }

    private boolean isEffectiveStateModified() {
        return (this.mUserActiveOld && this.mGameActiveOld && this.mFilterActiveOld) != isGameModeDndOn();
    }

    private void updateState() {
        this.mGameActiveOld = this.mGameActive;
        this.mUserActiveOld = this.mUserActive;
        this.mFilterActiveOld = this.mFilterActive;
    }

    /* access modifiers changed from: private */
    public void updateRule() {
        try {
            AutomaticZenRule fetchRule = fetchRule();
            if (fetchRule != null) {
                int i = 1;
                if (isFilterModified()) {
                    fetchRule.setInterruptionFilter(this.mFilterActive ? 2 : 1);
                    this.mNotificationManager.updateAutomaticZenRule(this.mRuleId, fetchRule);
                    Log.v("GameModeDndController", "Updated filter: " + this.mFilterActive);
                }
                if (!fetchRule.getName().equals(this.mRuleName)) {
                    fetchRule.setName(this.mRuleName);
                    this.mNotificationManager.updateAutomaticZenRule(this.mRuleId, fetchRule);
                }
                ComponentName configurationActivity = fetchRule.getConfigurationActivity();
                ComponentName componentName = COMPONENT_NAME;
                if (!configurationActivity.equals(componentName)) {
                    fetchRule.setConfigurationActivity(componentName);
                    this.mNotificationManager.updateAutomaticZenRule(this.mRuleId, fetchRule);
                }
                if (isEffectiveStateModified()) {
                    Uri conditionId = fetchRule.getConditionId();
                    if (!isGameModeDndOn()) {
                        i = 0;
                    }
                    Condition condition = new Condition(conditionId, "", i);
                    Log.v("GameModeDndController", "Updated condition: " + Condition.stateToString(condition.state));
                    this.mNotificationManager.setAutomaticZenRuleState(this.mRuleId, condition);
                }
                updateState();
            }
        } catch (Exception e) {
            Log.e("GameModeDndController", "Failed to update Game Mode DND rule: " + this.mRuleId, e);
        }
    }

    public void setGameModeDndRuleActive(boolean z) {
        this.mGameActive = z;
        updateRule();
    }

    private BroadcastReceiver createReceiver() {
        return new BroadcastReceiver() { // from class: com.google.android.systemui.gamedashboard.GameModeDndController.2
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                action.hashCode();
                char c = 65535;
                switch (action.hashCode()) {
                    case -2128145023:
                        if (action.equals("android.intent.action.SCREEN_OFF")) {
                            c = 0;
                            break;
                        }
                        break;
                    case -19011148:
                        if (action.equals("android.intent.action.LOCALE_CHANGED")) {
                            c = 1;
                            break;
                        }
                        break;
                    case 823795052:
                        if (action.equals("android.intent.action.USER_PRESENT")) {
                            c = 2;
                            break;
                        }
                        break;
                }
                switch (c) {
                    case 0:
                        GameModeDndController.this.mUserActive = false;
                        break;
                    case 1:
                        GameModeDndController gameModeDndController = GameModeDndController.this;
                        gameModeDndController.mRuleName = gameModeDndController.mContext.getString(R$string.game_mode_dnd_rule_name);
                        break;
                    case 2:
                        GameModeDndController.this.mUserActive = true;
                        break;
                }
                GameModeDndController.this.updateRule();
            }
        };
    }
}
