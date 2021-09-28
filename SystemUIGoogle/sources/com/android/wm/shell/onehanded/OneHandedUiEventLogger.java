package com.android.wm.shell.onehanded;

import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.logging.UiEventLogger;
/* loaded from: classes2.dex */
public class OneHandedUiEventLogger {
    private static final String[] EVENT_TAGS = {"one_handed_trigger_gesture_in", "one_handed_trigger_gesture_out", "one_handed_trigger_overspace_out", "one_handed_trigger_pop_ime_out", "one_handed_trigger_rotation_out", "one_handed_trigger_app_taps_out", "one_handed_trigger_timeout_out", "one_handed_trigger_screen_off_out", "one_handed_settings_enabled_on", "one_handed_settings_enabled_off", "one_handed_settings_app_taps_exit_on", "one_handed_settings_app_taps_exit_off", "one_handed_settings_timeout_exit_on", "one_handed_settings_timeout_exit_off", "one_handed_settings_timeout_seconds_never", "one_handed_settings_timeout_seconds_4", "one_handed_settings_timeout_seconds_8", "one_handed_settings_timeout_seconds_12", "one_handed_settings_show_notification_enabled_on", "one_handed_settings_show_notification_enabled_off", "one_handed_settings_shortcut_enabled_on", "one_handed_settings_shortcut_enabled_off"};
    private final UiEventLogger mUiEventLogger;

    public OneHandedUiEventLogger(UiEventLogger uiEventLogger) {
        this.mUiEventLogger = uiEventLogger;
    }

    @VisibleForTesting
    /* loaded from: classes2.dex */
    public enum OneHandedTriggerEvent implements UiEventLogger.UiEventEnum {
        INVALID(0),
        ONE_HANDED_TRIGGER_GESTURE_IN(366),
        ONE_HANDED_TRIGGER_GESTURE_OUT(367),
        ONE_HANDED_TRIGGER_OVERSPACE_OUT(368),
        ONE_HANDED_TRIGGER_POP_IME_OUT(369),
        ONE_HANDED_TRIGGER_ROTATION_OUT(370),
        ONE_HANDED_TRIGGER_APP_TAPS_OUT(371),
        ONE_HANDED_TRIGGER_TIMEOUT_OUT(372),
        ONE_HANDED_TRIGGER_SCREEN_OFF_OUT(449);
        
        private final int mId;

        OneHandedTriggerEvent(int i) {
            this.mId = i;
        }

        public int getId() {
            return this.mId;
        }
    }

    @VisibleForTesting
    /* loaded from: classes2.dex */
    public enum OneHandedSettingsTogglesEvent implements UiEventLogger.UiEventEnum {
        INVALID(0),
        ONE_HANDED_SETTINGS_TOGGLES_ENABLED_ON(356),
        ONE_HANDED_SETTINGS_TOGGLES_ENABLED_OFF(357),
        ONE_HANDED_SETTINGS_TOGGLES_APP_TAPS_EXIT_ON(358),
        ONE_HANDED_SETTINGS_TOGGLES_APP_TAPS_EXIT_OFF(359),
        ONE_HANDED_SETTINGS_TOGGLES_TIMEOUT_EXIT_ON(360),
        ONE_HANDED_SETTINGS_TOGGLES_TIMEOUT_EXIT_OFF(361),
        ONE_HANDED_SETTINGS_TOGGLES_TIMEOUT_SECONDS_NEVER(362),
        ONE_HANDED_SETTINGS_TOGGLES_TIMEOUT_SECONDS_4(363),
        ONE_HANDED_SETTINGS_TOGGLES_TIMEOUT_SECONDS_8(364),
        ONE_HANDED_SETTINGS_TOGGLES_TIMEOUT_SECONDS_12(365),
        ONE_HANDED_SETTINGS_TOGGLES_SHOW_NOTIFICATION_ENABLED_ON(847),
        ONE_HANDED_SETTINGS_TOGGLES_SHOW_NOTIFICATION_ENABLED_OFF(848),
        ONE_HANDED_SETTINGS_TOGGLES_SHORTCUT_ENABLED_ON(870),
        ONE_HANDED_SETTINGS_TOGGLES_SHORTCUT_ENABLED_OFF(871);
        
        private final int mId;

        OneHandedSettingsTogglesEvent(int i) {
            this.mId = i;
        }

        public int getId() {
            return this.mId;
        }
    }

    public void writeEvent(int i) {
        logEvent(i);
    }

    private void logEvent(int i) {
        switch (i) {
            case 0:
                this.mUiEventLogger.log(OneHandedTriggerEvent.ONE_HANDED_TRIGGER_GESTURE_IN);
                return;
            case 1:
                this.mUiEventLogger.log(OneHandedTriggerEvent.ONE_HANDED_TRIGGER_GESTURE_OUT);
                return;
            case 2:
                this.mUiEventLogger.log(OneHandedTriggerEvent.ONE_HANDED_TRIGGER_OVERSPACE_OUT);
                return;
            case 3:
                this.mUiEventLogger.log(OneHandedTriggerEvent.ONE_HANDED_TRIGGER_POP_IME_OUT);
                return;
            case 4:
                this.mUiEventLogger.log(OneHandedTriggerEvent.ONE_HANDED_TRIGGER_ROTATION_OUT);
                return;
            case 5:
                this.mUiEventLogger.log(OneHandedTriggerEvent.ONE_HANDED_TRIGGER_APP_TAPS_OUT);
                return;
            case 6:
                this.mUiEventLogger.log(OneHandedTriggerEvent.ONE_HANDED_TRIGGER_TIMEOUT_OUT);
                return;
            case 7:
                this.mUiEventLogger.log(OneHandedTriggerEvent.ONE_HANDED_TRIGGER_SCREEN_OFF_OUT);
                return;
            case 8:
                this.mUiEventLogger.log(OneHandedSettingsTogglesEvent.ONE_HANDED_SETTINGS_TOGGLES_ENABLED_ON);
                return;
            case 9:
                this.mUiEventLogger.log(OneHandedSettingsTogglesEvent.ONE_HANDED_SETTINGS_TOGGLES_ENABLED_OFF);
                return;
            case 10:
                this.mUiEventLogger.log(OneHandedSettingsTogglesEvent.ONE_HANDED_SETTINGS_TOGGLES_APP_TAPS_EXIT_ON);
                return;
            case 11:
                this.mUiEventLogger.log(OneHandedSettingsTogglesEvent.ONE_HANDED_SETTINGS_TOGGLES_APP_TAPS_EXIT_OFF);
                return;
            case 12:
                this.mUiEventLogger.log(OneHandedSettingsTogglesEvent.ONE_HANDED_SETTINGS_TOGGLES_TIMEOUT_EXIT_ON);
                return;
            case 13:
                this.mUiEventLogger.log(OneHandedSettingsTogglesEvent.ONE_HANDED_SETTINGS_TOGGLES_TIMEOUT_EXIT_OFF);
                return;
            case 14:
                this.mUiEventLogger.log(OneHandedSettingsTogglesEvent.ONE_HANDED_SETTINGS_TOGGLES_TIMEOUT_SECONDS_NEVER);
                return;
            case 15:
                this.mUiEventLogger.log(OneHandedSettingsTogglesEvent.ONE_HANDED_SETTINGS_TOGGLES_TIMEOUT_SECONDS_4);
                return;
            case 16:
                this.mUiEventLogger.log(OneHandedSettingsTogglesEvent.ONE_HANDED_SETTINGS_TOGGLES_TIMEOUT_SECONDS_8);
                return;
            case 17:
                this.mUiEventLogger.log(OneHandedSettingsTogglesEvent.ONE_HANDED_SETTINGS_TOGGLES_TIMEOUT_SECONDS_12);
                return;
            case 18:
                this.mUiEventLogger.log(OneHandedSettingsTogglesEvent.ONE_HANDED_SETTINGS_TOGGLES_SHOW_NOTIFICATION_ENABLED_ON);
                return;
            case 19:
                this.mUiEventLogger.log(OneHandedSettingsTogglesEvent.ONE_HANDED_SETTINGS_TOGGLES_SHOW_NOTIFICATION_ENABLED_OFF);
                return;
            case 20:
                this.mUiEventLogger.log(OneHandedSettingsTogglesEvent.ONE_HANDED_SETTINGS_TOGGLES_SHORTCUT_ENABLED_ON);
                return;
            case 21:
                this.mUiEventLogger.log(OneHandedSettingsTogglesEvent.ONE_HANDED_SETTINGS_TOGGLES_SHORTCUT_ENABLED_OFF);
                return;
            default:
                return;
        }
    }
}
