/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package android.provider.settings.validators;

import static android.provider.settings.validators.SettingsValidators.ANY_INTEGER_VALIDATOR;
import static android.provider.settings.validators.SettingsValidators.ANY_STRING_VALIDATOR;
import static android.provider.settings.validators.SettingsValidators.BOOLEAN_VALIDATOR;
import static android.provider.settings.validators.SettingsValidators.COMPONENT_NAME_VALIDATOR;
import static android.provider.settings.validators.SettingsValidators.CUSTOM_VIBRATION_PATTERN_VALIDATOR;
import static android.provider.settings.validators.SettingsValidators.LENIENT_IP_ADDRESS_VALIDATOR;
import static android.provider.settings.validators.SettingsValidators.NON_NEGATIVE_INTEGER_VALIDATOR;
import static android.provider.settings.validators.SettingsValidators.URI_VALIDATOR;
import static android.provider.settings.validators.SettingsValidators.VIBRATION_INTENSITY_VALIDATOR;

import android.annotation.Nullable;
import android.compat.annotation.UnsupportedAppUsage;
import android.content.ComponentName;
import android.hardware.display.ColorDisplayManager;
import android.os.BatteryManager;
import android.provider.Settings.System;
import android.util.ArrayMap;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Validators for System settings
 */
public class SystemSettingsValidators {
    @UnsupportedAppUsage
    public static final Map<String, Validator> VALIDATORS = new ArrayMap<>();

    static {
        VALIDATORS.put(
                System.STAY_ON_WHILE_PLUGGED_IN,
                value -> {
                    try {
                        int val = Integer.parseInt(value);
                        return (val == 0)
                                || (val == BatteryManager.BATTERY_PLUGGED_AC)
                                || (val == BatteryManager.BATTERY_PLUGGED_USB)
                                || (val == BatteryManager.BATTERY_PLUGGED_WIRELESS)
                                || (val
                                        == (BatteryManager.BATTERY_PLUGGED_AC
                                                | BatteryManager.BATTERY_PLUGGED_USB))
                                || (val
                                        == (BatteryManager.BATTERY_PLUGGED_AC
                                                | BatteryManager.BATTERY_PLUGGED_WIRELESS))
                                || (val
                                        == (BatteryManager.BATTERY_PLUGGED_USB
                                                | BatteryManager.BATTERY_PLUGGED_WIRELESS))
                                || (val
                                        == (BatteryManager.BATTERY_PLUGGED_AC
                                                | BatteryManager.BATTERY_PLUGGED_USB
                                                | BatteryManager.BATTERY_PLUGGED_WIRELESS));
                    } catch (NumberFormatException e) {
                        return false;
                    }
                });
        VALIDATORS.put(System.END_BUTTON_BEHAVIOR, new InclusiveIntegerRangeValidator(0, 3));
        VALIDATORS.put(System.WIFI_USE_STATIC_IP, BOOLEAN_VALIDATOR);
        VALIDATORS.put(System.BLUETOOTH_DISCOVERABILITY, new InclusiveIntegerRangeValidator(0, 2));
        VALIDATORS.put(System.BLUETOOTH_DISCOVERABILITY_TIMEOUT, NON_NEGATIVE_INTEGER_VALIDATOR);
        VALIDATORS.put(
                System.NEXT_ALARM_FORMATTED,
                new Validator() {
                    private static final int MAX_LENGTH = 1000;

                    @Override
                    public boolean validate(String value) {
                        // TODO: No idea what the correct format is.
                        return value == null || value.length() < MAX_LENGTH;
                    }
                });
        VALIDATORS.put(System.FONT_SCALE, new InclusiveFloatRangeValidator(0.25f, 5.0f));
        VALIDATORS.put(System.DIM_SCREEN, BOOLEAN_VALIDATOR);
        VALIDATORS.put(
                System.DISPLAY_COLOR_MODE,
                new Validator() {
                    @Override
                    public boolean validate(@Nullable String value) {
                        // Assume the actual validation that this device can properly handle this
                        // kind of
                        // color mode further down in ColorDisplayManager / ColorDisplayService.
                        try {
                            final int setting = Integer.parseInt(value);
                            final boolean isInFrameworkRange =
                                    setting >= ColorDisplayManager.COLOR_MODE_NATURAL
                                            && setting <= ColorDisplayManager.COLOR_MODE_AUTOMATIC;
                            final boolean isInVendorRange =
                                    setting >= ColorDisplayManager.VENDOR_COLOR_MODE_RANGE_MIN
                                            && setting
                                                    <= ColorDisplayManager
                                                            .VENDOR_COLOR_MODE_RANGE_MAX;
                            return isInFrameworkRange || isInVendorRange;
                        } catch (NumberFormatException | NullPointerException e) {
                            return false;
                        }
                    }
                });
        VALIDATORS.put(System.DISPLAY_COLOR_MODE_VENDOR_HINT, ANY_STRING_VALIDATOR);
        VALIDATORS.put(System.SCREEN_OFF_TIMEOUT, NON_NEGATIVE_INTEGER_VALIDATOR);
        VALIDATORS.put(System.SCREEN_BRIGHTNESS_FOR_VR, new InclusiveIntegerRangeValidator(0, 255));
        VALIDATORS.put(System.SCREEN_BRIGHTNESS_MODE, BOOLEAN_VALIDATOR);
        VALIDATORS.put(System.ADAPTIVE_SLEEP, BOOLEAN_VALIDATOR);
        VALIDATORS.put(System.MODE_RINGER_STREAMS_AFFECTED, NON_NEGATIVE_INTEGER_VALIDATOR);
        VALIDATORS.put(System.MUTE_STREAMS_AFFECTED, NON_NEGATIVE_INTEGER_VALIDATOR);
        VALIDATORS.put(System.VIBRATE_ON, BOOLEAN_VALIDATOR);
        VALIDATORS.put(System.APPLY_RAMPING_RINGER, BOOLEAN_VALIDATOR);
        VALIDATORS.put(System.ALARM_VIBRATION_INTENSITY, VIBRATION_INTENSITY_VALIDATOR);
        VALIDATORS.put(System.MEDIA_VIBRATION_INTENSITY, VIBRATION_INTENSITY_VALIDATOR);
        VALIDATORS.put(System.NOTIFICATION_VIBRATION_INTENSITY, VIBRATION_INTENSITY_VALIDATOR);
        VALIDATORS.put(System.RING_VIBRATION_INTENSITY, VIBRATION_INTENSITY_VALIDATOR);
        VALIDATORS.put(System.HAPTIC_FEEDBACK_INTENSITY, VIBRATION_INTENSITY_VALIDATOR);
        VALIDATORS.put(System.HARDWARE_HAPTIC_FEEDBACK_INTENSITY, VIBRATION_INTENSITY_VALIDATOR);
        VALIDATORS.put(System.HAPTIC_FEEDBACK_ENABLED, BOOLEAN_VALIDATOR);
        VALIDATORS.put(System.RINGTONE, URI_VALIDATOR);
        VALIDATORS.put(System.NOTIFICATION_SOUND, URI_VALIDATOR);
        VALIDATORS.put(System.ALARM_ALERT, URI_VALIDATOR);
        VALIDATORS.put(System.TEXT_AUTO_REPLACE, BOOLEAN_VALIDATOR);
        VALIDATORS.put(System.TEXT_AUTO_CAPS, BOOLEAN_VALIDATOR);
        VALIDATORS.put(System.TEXT_AUTO_PUNCTUATE, BOOLEAN_VALIDATOR);
        VALIDATORS.put(System.TEXT_SHOW_PASSWORD, BOOLEAN_VALIDATOR);
        VALIDATORS.put(System.AUTO_TIME, BOOLEAN_VALIDATOR);
        VALIDATORS.put(System.AUTO_TIME_ZONE, BOOLEAN_VALIDATOR);
        VALIDATORS.put(System.SHOW_GTALK_SERVICE_STATUS, BOOLEAN_VALIDATOR);
        VALIDATORS.put(
                System.WALLPAPER_ACTIVITY,
                new Validator() {
                    private static final int MAX_LENGTH = 1000;

                    @Override
                    public boolean validate(String value) {
                        if (value != null && value.length() > MAX_LENGTH) {
                            return false;
                        }
                        return ComponentName.unflattenFromString(value) != null;
                    }
                });
        VALIDATORS.put(
                System.TIME_12_24, new DiscreteValueValidator(new String[] {"12", "24", null}));
        VALIDATORS.put(System.SETUP_WIZARD_HAS_RUN, BOOLEAN_VALIDATOR);
        VALIDATORS.put(System.ACCELEROMETER_ROTATION, BOOLEAN_VALIDATOR);
        VALIDATORS.put(System.USER_ROTATION, new InclusiveIntegerRangeValidator(0, 3));
        VALIDATORS.put(System.DTMF_TONE_WHEN_DIALING, BOOLEAN_VALIDATOR);
        VALIDATORS.put(System.SOUND_EFFECTS_ENABLED, BOOLEAN_VALIDATOR);
        VALIDATORS.put(System.HAPTIC_FEEDBACK_ENABLED, BOOLEAN_VALIDATOR);
        VALIDATORS.put(System.POWER_SOUNDS_ENABLED, BOOLEAN_VALIDATOR);
        VALIDATORS.put(System.DOCK_SOUNDS_ENABLED, BOOLEAN_VALIDATOR);
        VALIDATORS.put(System.SHOW_WEB_SUGGESTIONS, BOOLEAN_VALIDATOR);
        VALIDATORS.put(System.WIFI_USE_STATIC_IP, BOOLEAN_VALIDATOR);
        VALIDATORS.put(System.ADVANCED_SETTINGS, BOOLEAN_VALIDATOR);
        VALIDATORS.put(System.SCREEN_AUTO_BRIGHTNESS_ADJ, new InclusiveFloatRangeValidator(-1, 1));
        VALIDATORS.put(System.VIBRATE_INPUT_DEVICES, BOOLEAN_VALIDATOR);
        VALIDATORS.put(System.MASTER_MONO, BOOLEAN_VALIDATOR);
        VALIDATORS.put(System.MASTER_BALANCE, new InclusiveFloatRangeValidator(-1.f, 1.f));
        VALIDATORS.put(System.NOTIFICATIONS_USE_RING_VOLUME, BOOLEAN_VALIDATOR);
        VALIDATORS.put(System.VIBRATE_IN_SILENT, BOOLEAN_VALIDATOR);
        VALIDATORS.put(System.MEDIA_BUTTON_RECEIVER, COMPONENT_NAME_VALIDATOR);
        VALIDATORS.put(System.HIDE_ROTATION_LOCK_TOGGLE_FOR_ACCESSIBILITY, BOOLEAN_VALIDATOR);
        VALIDATORS.put(System.VIBRATE_WHEN_RINGING, BOOLEAN_VALIDATOR);
        VALIDATORS.put(System.DTMF_TONE_TYPE_WHEN_DIALING, BOOLEAN_VALIDATOR);
        VALIDATORS.put(System.HEARING_AID, BOOLEAN_VALIDATOR);
        VALIDATORS.put(System.TTY_MODE, new InclusiveIntegerRangeValidator(0, 3));
        VALIDATORS.put(System.NOTIFICATION_LIGHT_PULSE, BOOLEAN_VALIDATOR);
        VALIDATORS.put(System.POINTER_LOCATION, BOOLEAN_VALIDATOR);
        VALIDATORS.put(System.SHOW_TOUCHES, BOOLEAN_VALIDATOR);
        VALIDATORS.put(System.WINDOW_ORIENTATION_LISTENER_LOG, BOOLEAN_VALIDATOR);
        VALIDATORS.put(System.LOCKSCREEN_SOUNDS_ENABLED, BOOLEAN_VALIDATOR);
        VALIDATORS.put(System.LOCKSCREEN_DISABLED, BOOLEAN_VALIDATOR);
        VALIDATORS.put(System.SIP_RECEIVE_CALLS, BOOLEAN_VALIDATOR);
        VALIDATORS.put(
                System.SIP_CALL_OPTIONS,
                new DiscreteValueValidator(new String[] {"SIP_ALWAYS", "SIP_ADDRESS_ONLY"}));
        VALIDATORS.put(System.SIP_ALWAYS, BOOLEAN_VALIDATOR);
        VALIDATORS.put(System.SIP_ADDRESS_ONLY, BOOLEAN_VALIDATOR);
        VALIDATORS.put(System.SIP_ASK_ME_EACH_TIME, BOOLEAN_VALIDATOR);
        VALIDATORS.put(System.POINTER_SPEED, new InclusiveFloatRangeValidator(-7, 7));
        VALIDATORS.put(System.LOCK_TO_APP_ENABLED, BOOLEAN_VALIDATOR);
        VALIDATORS.put(
                System.EGG_MODE,
                new Validator() {
                    @Override
                    public boolean validate(@Nullable String value) {
                        try {
                            return Long.parseLong(value) >= 0;
                        } catch (NumberFormatException e) {
                            return false;
                        }
                    }
                });
        VALIDATORS.put(System.WIFI_STATIC_IP, LENIENT_IP_ADDRESS_VALIDATOR);
        VALIDATORS.put(System.WIFI_STATIC_GATEWAY, LENIENT_IP_ADDRESS_VALIDATOR);
        VALIDATORS.put(System.WIFI_STATIC_NETMASK, LENIENT_IP_ADDRESS_VALIDATOR);
        VALIDATORS.put(System.WIFI_STATIC_DNS1, LENIENT_IP_ADDRESS_VALIDATOR);
        VALIDATORS.put(System.WIFI_STATIC_DNS2, LENIENT_IP_ADDRESS_VALIDATOR);
        VALIDATORS.put(System.FORCE_FULLSCREEN_CUTOUT_APPS, ANY_STRING_VALIDATOR);
        VALIDATORS.put(System.SHOW_BATTERY_PERCENT, BOOLEAN_VALIDATOR);
        VALIDATORS.put(System.NOTIFICATION_LIGHT_PULSE, BOOLEAN_VALIDATOR);
        VALIDATORS.put(System.QS_FOOTER_TEXT_SHOW, BOOLEAN_VALIDATOR);
        VALIDATORS.put(System.QS_FOOTER_TEXT_STRING, ANY_STRING_VALIDATOR);
        VALIDATORS.put(System.LOCKSCREEN_BATTERY_INFO, BOOLEAN_VALIDATOR);
        VALIDATORS.put(System.NETWORK_TRAFFIC_STATE, BOOLEAN_VALIDATOR);
        VALIDATORS.put(System.NETWORK_TRAFFIC_TYPE, new InclusiveIntegerRangeValidator(0, 4));
        VALIDATORS.put(System.NETWORK_TRAFFIC_AUTOHIDE_THRESHOLD, ANY_INTEGER_VALIDATOR);
        VALIDATORS.put(System.NETWORK_TRAFFIC_ARROW, BOOLEAN_VALIDATOR);
        VALIDATORS.put(System.NETWORK_TRAFFIC_FONT_SIZE, NON_NEGATIVE_INTEGER_VALIDATOR);
        VALIDATORS.put(System.NETWORK_TRAFFIC_VIEW_LOCATION, BOOLEAN_VALIDATOR);
        VALIDATORS.put(System.GAMING_MODE_HEADS_UP, BOOLEAN_VALIDATOR);
        VALIDATORS.put(System.GAMING_MODE_ZEN, BOOLEAN_VALIDATOR);
        VALIDATORS.put(System.GAMING_MODE_RINGER, new InclusiveIntegerRangeValidator(0, 2));
        VALIDATORS.put(System.GAMING_MODE_NAVBAR, BOOLEAN_VALIDATOR);
        VALIDATORS.put(System.GAMING_MODE_HW_BUTTONS, BOOLEAN_VALIDATOR);
        VALIDATORS.put(System.GAMING_MODE_NIGHT_LIGHT, BOOLEAN_VALIDATOR);
        VALIDATORS.put(System.GAMING_MODE_BATTERY_SCHEDULE, BOOLEAN_VALIDATOR);
        VALIDATORS.put(System.GAMING_MODE_BRIGHTNESS_ENABLED, BOOLEAN_VALIDATOR);
        VALIDATORS.put(System.GAMING_MODE_BRIGHTNESS, new InclusiveIntegerRangeValidator(0, 100));
        VALIDATORS.put(System.GAMING_MODE_MEDIA_ENABLED, BOOLEAN_VALIDATOR);
        VALIDATORS.put(System.GAMING_MODE_MEDIA, new InclusiveIntegerRangeValidator(0, 100));
        VALIDATORS.put(System.GAMING_MODE_SCREEN_OFF, BOOLEAN_VALIDATOR);
        VALIDATORS.put(System.NOTIFICATION_HEADERS, BOOLEAN_VALIDATOR);
        VALIDATORS.put(System.RINGTONE_VIBRATION_PATTERN, new InclusiveIntegerRangeValidator(0, 5));
        VALIDATORS.put(System.CUSTOM_RINGTONE_VIBRATION_PATTERN, CUSTOM_VIBRATION_PATTERN_VALIDATOR);
        VALIDATORS.put(System.VIBRATE_ON_CONNECT, BOOLEAN_VALIDATOR);
        VALIDATORS.put(System.VIBRATE_ON_CALLWAITING, BOOLEAN_VALIDATOR);
        VALIDATORS.put(System.VIBRATE_ON_DISCONNECT, BOOLEAN_VALIDATOR);
        VALIDATORS.put(System.FLASHLIGHT_ON_CALL, new InclusiveIntegerRangeValidator(0, 4));
        VALIDATORS.put(System.FLASHLIGHT_ON_CALL_IGNORE_DND, BOOLEAN_VALIDATOR);
        VALIDATORS.put(System.FLASHLIGHT_ON_CALL_RATE, new InclusiveIntegerRangeValidator(1, 5));
        VALIDATORS.put(System.VOLUME_DIALOG_TIMEOUT, new InclusiveIntegerRangeValidator(1, 7));
        VALIDATORS.put(System.TORCH_POWER_BUTTON_GESTURE, new InclusiveIntegerRangeValidator(0, 2));
        VALIDATORS.put(System.DOUBLE_TAP_SLEEP_LOCKSCREEN, BOOLEAN_VALIDATOR);
        VALIDATORS.put(System.DOUBLE_TAP_SLEEP_GESTURE, BOOLEAN_VALIDATOR);
        VALIDATORS.put(System.VOLUME_BUTTON_MUSIC_CONTROL, BOOLEAN_VALIDATOR);
        VALIDATORS.put(System.VOLUME_BUTTON_MUSIC_CONTROL_DELAY, new InclusiveIntegerRangeValidator(300, 2000));
        VALIDATORS.put(System.OMNI_ADVANCED_REBOOT, BOOLEAN_VALIDATOR);
        VALIDATORS.put(System.BATTERY_LIGHT_ENABLED, BOOLEAN_VALIDATOR);
        VALIDATORS.put(System.BATTERY_LIGHT_ALLOW_ON_DND, BOOLEAN_VALIDATOR);
        VALIDATORS.put(System.BATTERY_LIGHT_LOW_BLINKING, BOOLEAN_VALIDATOR);
        VALIDATORS.put(System.BATTERY_LIGHT_LOW_COLOR, ANY_STRING_VALIDATOR);
        VALIDATORS.put(System.BATTERY_LIGHT_MEDIUM_COLOR, ANY_STRING_VALIDATOR);
        VALIDATORS.put(System.BATTERY_LIGHT_FULL_COLOR, ANY_STRING_VALIDATOR);
        VALIDATORS.put(System.BATTERY_LIGHT_REALLYFULL_COLOR, ANY_STRING_VALIDATOR);
        VALIDATORS.put(System.NOTIFICATION_PULSE, BOOLEAN_VALIDATOR);
        VALIDATORS.put(System.AOD_NOTIFICATION_PULSE, BOOLEAN_VALIDATOR);
        VALIDATORS.put(System.NOTIFICATION_PULSE_COLOR_MODE, new InclusiveIntegerRangeValidator(0, 3));
        VALIDATORS.put(System.NOTIFICATION_PULSE_COLOR, ANY_INTEGER_VALIDATOR);
        VALIDATORS.put(System.NOTIFICATION_PULSE_REPEATS, ANY_INTEGER_VALIDATOR);
        VALIDATORS.put(System.NOTIFICATION_PULSE_DURATION, ANY_INTEGER_VALIDATOR);
        VALIDATORS.put(System.QS_FOOTER_SERVICES_SHOW, BOOLEAN_VALIDATOR);
        VALIDATORS.put(System.KEYGAURD_MEDIA_ART, BOOLEAN_VALIDATOR);
        VALIDATORS.put(System.QS_SHOW_BATTERY_ESTIMATE, BOOLEAN_VALIDATOR);
        VALIDATORS.put(System.ENABLE_FLOATING_ROTATION_BUTTON, BOOLEAN_VALIDATOR);
        VALIDATORS.put(System.NAVIGATION_BAR_INVERSE, BOOLEAN_VALIDATOR);
        VALIDATORS.put(System.NAVBAR_LAYOUT_VIEWS,
                new Validator() {
                    @Override
                    public boolean validate(String value) {
                        if (value.equals("default")) return true;
                        int scCount = value.length() - value.replace(";", "").length();
                        if (scCount != 2) return false;
                        value = value.replace(";", ",");
                        String[] args = value.split(",", 0);
                        for (String str : args) {
                            if (!str.equals("left") &&
                                !str.equals("right") &&
                                !str.equals("back") &&
                                !str.equals("home") &&
                                !str.equals("recent") &&
                                !str.equals("space"))
                                return false;
                        }
                        return true;
                    }
                });
        VALIDATORS.put(System.VOLUME_KEY_CURSOR_CONTROL, new InclusiveIntegerRangeValidator(0, 2));
        VALIDATORS.put(System.STATUS_BAR_BATTERY_STYLE, new InclusiveIntegerRangeValidator(0, 2));
        VALIDATORS.put(System.SHOW_BATTERY_PERCENT_INSIDE, BOOLEAN_VALIDATOR);
        VALIDATORS.put(System.VOLUME_BUTTON_QUICK_MUTE, BOOLEAN_VALIDATOR);
        VALIDATORS.put(System.VOLUME_BUTTON_QUICK_MUTE_DELAY, new InclusiveIntegerRangeValidator(300, 1500));
        VALIDATORS.put(System.BACK_GESTURE_HEIGHT, new InclusiveIntegerRangeValidator(0, 5));
        VALIDATORS.put(System.VOLUME_PANEL_ON_LEFT, BOOLEAN_VALIDATOR);
        VALIDATORS.put(System.VOLUME_PANEL_ON_LEFT_LAND, BOOLEAN_VALIDATOR);
        VALIDATORS.put(System.STATUS_BAR_BRIGHTNESS_CONTROL, BOOLEAN_VALIDATOR);
        VALIDATORS.put(System.STATUSBAR_CLOCK_POSITION, new InclusiveIntegerRangeValidator(0, 2));
        VALIDATORS.put(System.NOTIFICATION_VIBRATION_PATTERN, new InclusiveIntegerRangeValidator(0, 5));
        VALIDATORS.put(System.CUSTOM_NOTIFICATION_VIBRATION_PATTERN, CUSTOM_VIBRATION_PATTERN_VALIDATOR);
        VALIDATORS.put(System.DEFAULT_NOTIFICATION_TORCH,
                new Validator() {
                    @Override
                    public boolean validate(String value) {
                        if (value == null) return true;
                        if (value.equals("1")) return true;
                        String[] args = value.split(",");
                        if (args.length != 2) return false;
                        for (String arg : args) {
                            try {
                                if (Integer.parseInt(arg) < 1)
                                    return false;
                            } catch (NumberFormatException e) {
                                return false;
                            }
                        }
                        return true;
                    }
                });
        VALIDATORS.put(System.STATUS_BAR_NOTIF_COUNT, BOOLEAN_VALIDATOR);
        VALIDATORS.put(System.KEYGUARD_QUICK_TOGGLES,
                new Validator() {
                    @Override
                    public boolean validate(String value) {
                        if (value == null) return true;
                        if (!value.contains(";")) return false;
                        final List<String> valid = Arrays.asList(
                            "home",
                            "wallet",
                            "qr",
                            "camera",
                            "flashlight"
                        );
                        final String[] split = value.split(";");
                        if (split.length != 2) return false;
                        if (!split[0].equals("none")) {
                            String[] args = split[0].split(",");
                            for (String arg : args)
                                if (!valid.contains(arg))
                                    return false;
                        }
                        if (!split[1].equals("none")) {
                            String[] args = split[1].split(",");
                            for (String arg : args)
                                if (!valid.contains(arg))
                                    return false;
                        }
                        return true;
                    }
                });
    }
}
